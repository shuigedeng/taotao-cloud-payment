# 值对象设计规范

## 核心特性

### 1. 不可变性
所有字段 final，构造时赋值，只提供 getter 不提供 setter。

```java
@ValueObject
public final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("金额不能为负数");
        }
        if (currency == null) {
            throw new DomainException("货币类型不能为空");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new DomainException("货币类型不匹配");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    // equals/hashCode 基于所有属性
}
```

### 2. 自验证
值对象在构造时必须验证自身有效性。

### 3. 行为内聚
值对象可以包含与其相关的业务行为。

## 支付领域值对象示例

### Money（金额）
```java
// amount: BigDecimal，currency: Currency
// add(), subtract(), multiply(), compareTo(), isZero()
```

### PayFlowId（支付流水号）
```java
// 构造时校验格式：生成规则如 "PF" + yyyyMMdd + 序列号
```

### PayStatus（支付状态）
```java
// 状态转换：UNPAID → PAID → REFUNDING → REFUNDED
//                             → PARTIAL_REFUND
//           UNPAID → CLOSED
//           PAID → PARTIAL_REFUND → REFUNDING → REFUNDED
```

### PaymentMethod（支付方式）
```java
// ALIPAY, WECHAT_PAY, UNION_PAY, BALANCE, POINTS
```

### RefundReason（退款原因）
```java
// 枚举：MULTI_BUY("多拍"), NOT_NEED("不想要"), QUALITY("质量问题"), OTHER("其他")
```

## JPA 映射值对象

```java
@Embeddable
public class Money {
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "currency")
    private String currency;

    protected Money() {}  // JPA 要求

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency.getCurrencyCode();
    }
}
```

## 项目约定
- 使用 `record-builder`（`@RecordBuilder`）或 Lombok `@Value` + `@Builder` 简化不可变对象创建
- 所有值对象必须覆盖 `equals()` 和 `hashCode()`（基于所有字段）
- 值对象字段优先使用 JDK 标准类型（`BigDecimal`, `Currency`, `LocalDateTime`）
