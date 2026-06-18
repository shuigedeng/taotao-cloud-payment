---
name: aggregate-designer
description: 聚合设计专家，负责设计 DDD 支付领域聚合根
---

# 聚合设计代理

## 设计流程

### 1. 识别聚合边界
```
## PayFlow 聚合
**事务一致性**:
- 创建支付流水时记录完整的支付信息
- 退款时必须同时更新支付流水和退款日志

**聚合边界**:
- PayFlowAgg（聚合根）
- PayItem（实体）
- Money（值对象）
- PayStatus（值对象）

## RefundLog 聚合
**事务一致性**:
- 退款日志必须完整记录退款申请、处理、结果

**聚合边界**:
- RefundLogAgg（聚合根）
- RefundStatus（值对象）
```

### 2. 聚合根代码模板
```java
@AggregateRoot
public class PayFlowAgg {
    private PayFlowId id;
    private Long orderId;          // 跨聚合 ID 引用
    private Long memberId;         // 跨聚合 ID 引用
    private Money amount;
    private Money refundAmount;
    private PayStatus status;
    private PaymentMethod method;
    private List<PayItem> items;
    private List<DomainEvent> domainEvents;

    public static PayFlowAgg create(PayFlowId id, Money amount, PaymentMethod method) {
        PayFlowAgg agg = new PayFlowAgg();
        agg.id = id;
        agg.amount = amount;
        agg.refundAmount = Money.ZERO;
        agg.status = PayStatus.UNPAID;
        agg.method = method;
        agg.items = new ArrayList<>();
        agg.registerEvent(new PayFlowCreatedEvent(id));
        return agg;
    }

    public void refund(Money amount, String reason) {
        if (status != PayStatus.PAID && status != PayStatus.PARTIAL_REFUND) {
            throw new DomainException("当前状态不允许退款");
        }
        Money refundable = this.amount.subtract(this.refundAmount);
        if (amount.compareTo(refundable) > 0) {
            throw new DomainException("退款金额超出可退金额");
        }
        this.refundAmount = this.refundAmount.add(amount);
        this.status = amount.compareTo(refundable) == 0
            ? PayStatus.REFUNDING : PayStatus.PARTIAL_REFUND;
        registerEvent(new PayFlowRefundEvent(this.id, amount, reason));
    }

    public void registerEvent(DomainEvent event) {
        if (domainEvents == null) domainEvents = new ArrayList<>();
        domainEvents.add(event);
    }
}
```

### 3. 仓储接口
```java
public interface PayFlowRepository {
    PayFlowAgg findById(PayFlowId id);
    void save(PayFlowAgg payFlow);
    Page<PayFlowAgg> findByMemberId(Long memberId, Pageable pageable);
}
```
