# 聚合设计规范

## 聚合识别原则

### 1. 事务边界
聚合内修改必须在一个事务中完成，聚合间使用最终一致性。

```java
// ✅ 聚合内：直接校验 + 修改
public class PayFlowAgg {
    public void refund(Money amount, String reason) {
        if (this.status != PayStatus.PAID) {
            throw new DomainException("只有已支付流水才能退款");
        }
        this.status = PayStatus.REFUNDING;
        this.refundAmount = this.refundAmount.add(amount);
        registerEvent(new PayFlowRefundEvent(this.id, amount, reason));
    }
}

// ❌ 错误：跨聚合直接操作
public class PayFlowAgg {
    public void refund(Money amount, String reason) {
        // 不应该直接调用 Order 聚合的方法
        order.markAsRefunded(amount);
    }
}
```

### 2. 聚合大小
- 小聚合原则：一个聚合根通常包含 1-3 个实体
- 避免加载过多不必要的数据

```java
// ✅ 好的设计：只包含必要的实体
public class PayFlowAgg {
    private PayFlowId id;          // 支付流水号（值对象）
    private Money amount;          // 金额（值对象）
    private PayStatus status;       // 状态（值对象/枚举）
    private List<PayItem> items;   // 支付项实体（聚合内实体）
}

// ❌ 坏的设计：包含了其他聚合
public class PayFlowAgg {
    private Order order;           // 不应该包含 Order 聚合
    private Member member;         // 不应该包含 Member 聚合
}
```

### 3. 聚合根标识
使用值对象作为 ID，而非基本类型。

```java
// ✅ 正确：业务语义明确的 ID
public class PayFlowId implements Serializable {
    private final String value;
    public PayFlowId(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainException("支付流水号不能为空");
        }
        this.value = value;
    }
    // equals/hashCode 基于 value
}

// ❌ 不推荐：基本类型无法表达业务语义
public class PayFlowAgg {
    private Long id;  // 业务含义不明确
}
```

## 聚合根方法设计

### 命令方法（状态变更）
```java
public class PayFlowAgg {
    // ✅ 命令方法：有业务语义
    public void pay(Money amount) { ... }
    public void refund(Money amount, String reason) { ... }
    public void close(String reason) { ... }

    // ❌ 不要暴露 setter
    public void setStatus(PayStatus status) { ... }  // 贫血模型
}
```

### 查询方法（只读）
```java
public class PayFlowAgg {
    public boolean isPaid() { return status == PayStatus.PAID; }
    public boolean isRefundable() {
        return status == PayStatus.PAID || status == PayStatus.PARTIAL_REFUND;
    }
    public Money getRefundableAmount() {
        return amount.subtract(refundAmount);
    }
}
```

## 不变性维护

聚合根必须保证内部不变量：

```java
public class PayFlowAgg {
    public void refund(Money amount, String reason) {
        // 不变性1: 只有已支付订单可退款
        if (status != PayStatus.PAID && status != PayStatus.PARTIAL_REFUND) {
            throw new DomainException("当前状态不允许退款");
        }
        // 不变性2: 退款金额不能超过可退金额
        if (amount.compareTo(getRefundableAmount()) > 0) {
            throw new DomainException("退款金额超出可退金额");
        }
        // 不变性3: 退款原因不能为空
        if (reason == null || reason.isBlank()) {
            throw new DomainException("退款原因不能为空");
        }
        this.status = PayStatus.REFUNDING;
        registerEvent(new PayFlowRefundEvent(this.id, amount, reason));
    }
}
```

## 支付聚合典型设计

```
PayFlowAgg（聚合根）
├── PayFlowId          (值对象 - ID)
├── Money              (值对象 - 金额)
├── PayStatus          (值对象 - 状态)
├── PaymentMethod      (值对象 - 支付方式)
├── PayItem[]          (实体 - 支付项明细)
└── DomainEvent[]      (领域事件列表)

RefundLogAgg（聚合根）
├── RefundLogId        (值对象 - ID)
├── Money              (值对象 - 退款金额)
├── RefundStatus       (值对象 - 退款状态)
└── DomainEvent[]      (领域事件列表)
```
