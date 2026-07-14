# 架构规范 — DDD 六边形架构

## 分层职责

### Domain 层（零外部依赖）
- 聚合根、实体、值对象、领域事件、仓储接口、领域服务
- **禁止**: 依赖 Spring、数据库、任何框架
- **依赖**: 纯 Java + JDK 标准库

```java
// ✅ 正确：domain 不依赖任何框架
public class PayFlowAgg {
    // 业务行为方法
    public void refund(Money amount, String reason) {
        if (this.status != PayStatus.PAID) {
            throw new DomainException("只有已支付流水才能退款");
        }
        this.status = PayStatus.REFUNDING;
        registerEvent(new PayFlowRefundEvent(this.id, amount, reason));
    }
}
```

### Application 层（用例编排）
- 命令/查询服务、Executor、Handler、Assembler、DTO
- **禁止**: 包含业务规则判断、直接调用 Mapper/DAO
- **事务边界**: 仅开在此层

```java
@ApplicationService
@Service
@Transactional
public class PayFlowCommandService {
    private final PayFlowRepository payFlowRepository;

    public PayFlowResponse refund(RefundCommand command) {
        // 编排领域对象，不含业务规则
        PayFlowAgg payFlow = payFlowRepository.findById(command.getPayFlowId());
        payFlow.refund(command.getAmount(), command.getReason());
        payFlowRepository.save(payFlow);
        return PayFlowResponse.from(payFlow);
    }
}
```

### Infrastructure 层（技术实现）
- 仓储实现、PO、Mapper、事件发布、配置、工具
- **依赖**: domain（实现其接口），不反向依赖 application

```java
@Repository
public class PayFlowRepositoryImpl implements PayFlowRepository {
    private final PayFlowMapper mapper;

    @Override
    public void save(PayFlowAgg payFlow) {
        PayFlowPo po = mapper.toPo(payFlow);
        // 持久化 + 发布领域事件
    }
}
```

### Interfaces 层（入站适配器）
- REST Controller、RPC 实现、gRPC 实现、消息监听
- **禁止**: 业务逻辑、直接调用 Repository

```java
@RestController
@RequestMapping("/buyer/payment/pay-flow")
public class PayFlowBuyerController {
    private final PayFlowCommandService payFlowCommandService;

    @PostMapping("/{sn}/refund")
    public Result<Void> refund(@PathVariable String sn, @RequestBody RefundRequest request) {
        payFlowCommandService.refund(sn, request);
        return Result.success();
    }
}
```

### API 模块（纯定义）
- RPC/gRPC 接口定义、DTO、proto 文件
- **禁止**: 依赖任何业务模块

## 依赖方向

```
api  ←  interfaces  ←  application  →  facade
                          ↓
                     domain  ←  infrastructure
```

- Controller 只能依赖 Application Service
- Application Service 可以依赖 Domain Service + 多个 Repository
- Repository 实现在 Infrastructure，接口在 Domain

## 分层核查清单

- [ ] domain 是否引用了 Spring 注解（@Service/@Autowired）？
- [ ] Controller 是否包含 if/else 业务逻辑？
- [ ] Application Service 中是否有业务规则判断？
- [ ] 事务注解是否仅出现在 Application Service？
- [ ] 跨聚合是否通过 ID 引用而非对象引用？
