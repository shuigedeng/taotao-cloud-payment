# 项目编码规范 — taotao-cloud-payment

> 支付领域 DDD 编码规范。补充 `.opencode/AGENTS.md` 中未覆盖的实现细节。

---

## 1. 模块依赖规则

```
api  ←  interfaces  ←  application  →  facade
                          ↓
                     domain  ←  infrastructure
```

- `domain`：零外部依赖，不依赖 Spring、不依赖数据库、不依赖任何框架
- `application`：依赖 `domain`，可依赖 `facade` 接口，不依赖 `infrastructure`
- `infrastructure`：依赖 `domain` 实现仓储，依赖 `application` 实现事件订阅
- `interfaces`：依赖 `application`，不直接依赖 `infrastructure`
- `api`：纯 DTO + 接口定义 + proto，不依赖任何业务模块

### 禁止违反的依赖
```java
// ❌ 禁止：Controller 直接调用 Repository
@Autowired private PaymentRepository paymentRepository;

// ❌ 禁止：Application Service 直接调用 Mapper
@Autowired private PayFlowMapper payFlowMapper;

// ❌ 禁止：Domain Service 注入 Repository
@Autowired private PayFlowRepository payFlowRepository;

// ✅ 正确：Application Service 通过仓储接口操作持久化
private final PayFlowRepository payFlowRepository;
```

## 2. 包结构规范

```
com.taotao.cloud.payment.{module}/
├── aggregate/     # 聚合根（@AggregateRoot）
├── entity/        # 实体（@Entity）
├── valueobject/   # 值对象（@ValueObject | @Embeddable）
├── event/         # 领域事件（extends DomainEvent）
├── repository/    # 仓储接口
└── service/       # 领域服务（@DomainService）
```

### 聚合根的写法
```java
@AggregateRoot
public class PayFlowAgg {
    // 聚合内实体用对象引用（非 ID）
    private List<PayItem> items;

    // 跨聚合用 ID 引用
    private Long orderId;
    private Long memberId;

    // 业务行为方法（不是 setter）
    public void refund(Money amount, String reason) {
        // 校验业务规则
        if (this.status != PayStatus.PAID) {
            throw new DomainException("只有已支付流水才能退款");
        }
        // 修改内部状态
        this.status = PayStatus.REFUNDING;
        // 注册领域事件
        registerEvent(new PayFlowRefundEvent(this.id, amount, reason));
    }

    // 无参构造（框架要求），protected
    protected PayFlowAgg() {}

    // 静态工厂方法
    public static PayFlowAgg create(CreatePayFlowCommand cmd) { ... }
}
```

### 值对象的写法
```java
@Embeddable
public class Money {
    private final BigDecimal amount;
    private final Currency currency;

    // 构造时自验证
    public Money(BigDecimal amount, Currency currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("金额不能为负数");
        }
        this.amount = amount;
        this.currency = currency;
    }

    // 只有 getter，无 setter
    public BigDecimal getAmount() { return amount; }
    public Currency getCurrency() { return currency; }

    // 值对象行为：金额运算
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new DomainException("货币类型不匹配");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    // 覆写 equals/hashCode（基于所有属性）
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0 &&
               currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
```

## 3. Application Service 规范

### 命令服务（写操作）
```java
@ApplicationService
@Service
@Transactional
public class PayFlowCommandServiceImpl implements PayFlowCommandService {
    private final PayFlowRepository payFlowRepository;
    private final PayFlowDomainService payFlowDomainService;
    private final PaymentRefundApi paymentRefundApi; // 防腐层调用

    @Override
    public CreatePayFlowResponse createPayFlow(CreatePayFlowCommand command) {
        // 1. 构建领域对象
        // 2. 调用领域服务（如果需要跨聚合逻辑）
        // 3. 保存聚合
        // 4. 发布领域事件
        // 5. 返回 DTO
        return CreatePayFlowResponse.fromDomain(payFlow);
    }
}
```

### 查询服务（读操作）
```java
@ApplicationService
@Service
@Transactional(readOnly = true)
public class PayFlowQueryServiceImpl implements PayFlowQueryService {
    private final PayFlowQueryRepository payFlowQueryRepository;

    @Override
    public PayFlowDetailResult queryDetail(String payFlowSn) {
        // 直接返回 DTO/Result，不经过领域模型
        return payFlowQueryRepository.getDetailBySn(payFlowSn);
    }
}
```

## 4. Controller 规范

```java
@RestController
@RequestMapping("/{role}/payment/pay-flow")
// role = buyer | seller | manager
public class PayFlowBuyerController extends BusinessController {
    // HTTP 解析 + 参数校验 + Result 封装
    // 禁止业务逻辑

    @GetMapping("/page")
    public Result<PageResult<PayFlowSimpleResult>> page(PayFlowPageQuery query) {
        return Result.success(payFlowQueryService.pageQuery(query));
    }

    @PostMapping("/{sn}/refund")
    public Result<Void> refund(@PathVariable String sn, @RequestBody RefundRequest request) {
        payFlowCommandService.refund(sn, request);
        return Result.success();
    }
}
```

## 5. 枚举规范

```java
// 支付状态枚举，在 common 模块定义
public enum PayStatusEnum {
    UNPAID("未支付"),
    PAID("已支付"),
    REFUNDING("退款中"),
    REFUNDED("已退款"),
    PARTIAL_REFUND("部分退款"),
    CLOSED("已关闭"),
    FAILED("支付失败");

    private final String description;
    // ...
}

// 支付方式枚举
public enum PaymentMethodEnum {
    ALIPAY("支付宝"),
    WECHAT_PAY("微信支付"),
    UNION_PAY("银联支付"),
    BALANCE("余额支付"),
    POINTS("积分支付");

    private final String description;
    // ...
}
```

## 6. 领域事件规范

```java
// 事件定义在 domain/event/
public class PayFlowCreatedEvent extends DomainEvent {
    private final Long payFlowId;
    private final Money amount;
    // 不可变，构造时赋值
}

public class PayFlowRefundEvent extends DomainEvent {
    private final Long payFlowId;
    private final Money refundAmount;
    // ...
}

// 事件在聚合根内注册
// 仓储 save() 时自动 flush 发布
// 订阅在 infrastructure/event/
```

## 7. MapStruct + Assembler 规范

```java
// Assembler 职责：
// - infrastructure/assembler/  : Domain Entity ←→ Persistence PO
// - application/assembler/     : Domain Entity ←→ DTO

@Mapper(componentModel = "spring")
public interface PayFlowAssembler {
    PayFlowPo toPo(PayFlow payFlow);
    PayFlow toDomain(PayFlowPo po);
}
```

## 8. 构建与测试

```bash
# 全量构建（跳过测试）
gradlew build -x test

# 运行所有测试
gradlew test

# 运行指定模块测试
gradlew :taotao-cloud-payment-domain:test

# 代码质量
gradlew checkstyleMain spotlessCheck pmdMain spotbugsMain

# 本地启动
gradlew :taotao-cloud-payment-assembly:bootRun --args='--spring.profiles.active=dev'

# 生成覆盖率报告
gradlew jacocoTestReport
```

## 9. 数据库规范

### 表必备字段
```sql
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`create_by` bigint DEFAULT NULL COMMENT '创建人ID',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT NULL COMMENT '更新人ID',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`is_deleted` tinyint(1) DEFAULT 0 COMMENT '删除标记',
`tenant_id` bigint DEFAULT 0 COMMENT '租户ID',
`version` int DEFAULT 0 COMMENT '乐观锁'
```

### 支付表特有字段建议
```sql
`pay_flow_sn`   varchar(64) NOT NULL COMMENT '支付流水号',
`order_sn`      varchar(64) DEFAULT NULL COMMENT '业务订单号',
`pay_amount`    decimal(10,2) NOT NULL COMMENT '支付金额',
`refund_amount` decimal(10,2) DEFAULT 0 COMMENT '已退款金额',
`pay_status`    varchar(32) NOT NULL COMMENT '支付状态',
`pay_method`    varchar(32) DEFAULT NULL COMMENT '支付方式',
`pay_time`      datetime DEFAULT NULL COMMENT '支付时间',
`notify_url`    varchar(512) DEFAULT NULL COMMENT '异步通知地址',
```

### 禁止
- 循环中查询数据库（N+1 问题）
- `SELECT *`
- 在 Java 代码中拼接 SQL
- 跨聚合直接操作其他聚合的数据表
- 支付金额、退款金额使用 `float`/`double`（必须用 `decimal`）
- 在事务中调用远程 RPC（可能导致分布式事务超时）
