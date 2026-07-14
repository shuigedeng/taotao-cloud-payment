# 领域服务设计规范

## 何时使用领域服务

### 适用场景
1. **跨聚合的业务逻辑** — 需要协调多个聚合根
2. **无状态的计算服务** — 不属于任何一个聚合的纯计算
3. **外部领域概念** — 聚合根无法自然表达的协作逻辑

### 不适用场景
1. **应该属于聚合根的行为**（如金额合计应放在聚合内）
2. **纯粹的技术性操作**（应该放在 infrastructure 层）
3. **应用层的用例编排**（应该放在 Application Service）

```java
// ✅ 正确：跨聚合的业务逻辑
@DomainService
public class RefundDomainService {
    public PayFlowAgg processRefund(PayFlowAgg payFlow, Money amount, String reason) {
        // 调用聚合根行为
        payFlow.refund(amount, reason);
        // 跨聚合操作通过仓储调用
        return payFlow;
    }
}

// ❌ 错误：应该属于聚合根
@DomainService
public class PayFlowAmountCalculator {
    public Money calculateTotal(List<PayItem> items) {
        // 这个逻辑应该放在 PayFlowAgg 内
        return items.stream().map(PayItem::getAmount).reduce(Money.ZERO, Money::add);
    }
}
```

## 领域服务实现规范

### 1. 无状态设计
```java
@DomainService
@Service
public class PaymentValidationService {
    // 只依赖无状态的领域服务
    private final PaymentRuleService paymentRuleService;

    public void validateRefund(PayFlowAgg payFlow, Money amount) {
        if (!paymentRuleService.isWithinRefundLimit(payFlow, amount)) {
            throw new DomainException("超出退款限额");
        }
    }
}
```

### 2. 业务语义明确
```java
@DomainService
public class PayFlowFactory {
    public PayFlowAgg createPayFlow(CreatePayFlowCommand command) {
        PayFlowAgg payFlow = PayFlowAgg.create(
            PayFlowId.generate(),
            command.getAmount(),
            command.getPaymentMethod()
        );
        // 注册初始领域事件
        payFlow.registerEvent(new PayFlowCreatingEvent(payFlow.getId()));
        return payFlow;
    }
}
```

### 3. 异常处理
```java
@DomainService
public class PayFlowDomainService {
    public PayFlowAgg applyRefund(PayFlowAgg payFlow, RefundSpec spec) {
        // 领域规则校验
        if (!payFlow.isRefundable()) {
            throw new DomainException("当前支付流水不可退款");
        }
        // 调用聚合行为
        payFlow.refund(spec.getAmount(), spec.getReason());
        return payFlow;
    }
}
```

## 领域服务测试
```java
@ExtendWith(MockitoExtension.class)
class PayFlowDomainServiceTest {
    @InjectMocks
    private PayFlowDomainService payFlowDomainService;

    @Test
    void shouldApplyRefundSuccessfully() {
        PayFlowAgg payFlow = mock(PayFlowAgg.class);
        when(payFlow.isRefundable()).thenReturn(true);

        PayFlowAgg result = payFlowDomainService.applyRefund(
            payFlow, new RefundSpec(new Money(100), "质量问题")
        );

        verify(payFlow).refund(any(Money.class), anyString());
        assertNotNull(result);
    }
}
```
