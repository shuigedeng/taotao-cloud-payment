# 测试规范

## 覆盖率要求
- **单元测试覆盖率**: ≥ 80%（JaCoCo 检查）
- **分支覆盖率**: ≥ 70%
- **领域层**: 行覆盖率 ≥ 90%

## 三层测试策略

### Domain 层（单元测试）
纯 POJO 测试，不启动 Spring 上下文。

```java
class PayFlowAggTest {
    @Test
    void shouldRefundWhenStatusIsPaid() {
        PayFlowAgg payFlow = PayFlowAgg.create(PayFlowId.generate(), new Money(100), PaymentMethod.ALIPAY);
        payFlow.pay();

        payFlow.refund(new Money(50), "质量问题");

        assertThat(payFlow.getStatus()).isEqualTo(PayStatus.REFUNDING);
        assertThat(payFlow.getRefundAmount()).isEqualTo(new Money(50));
        assertThat(payFlow.getDomainEvents()).hasSize(3); // created + paid + refund
    }
}
```

### Application 层（集成测试）
使用 `@SpringBootTest` + 测试切片，验证事务和编排。

```java
@SpringBootTest
@Transactional
class PayFlowCommandServiceTest {
    @Autowired
    private PayFlowCommandService payFlowCommandService;

    @Test
    void shouldCreatePayFlow() {
        CreatePayFlowCommand command = new CreatePayFlowCommand(1L, new Money(100), PaymentMethod.ALIPAY);
        PayFlowResponse response = payFlowCommandService.createPayFlow(command);
        assertThat(response.getId()).isNotNull();
    }
}
```

### Infrastructure 层（持久化测试）
验证 PO 映射、仓储实现、查询正确性。

## 禁止
- 使用 `@DirtiesContext`（破坏测试隔离）
- 测试中连接外部真实服务（使用 Mock/Testcontainers）
- 在 domain 层测试中使用 Spring 注解

## 命令
```bash
gradlew test                                   # 运行全部测试
gradlew :taotao-cloud-payment-domain:test      # 领域层测试
gradlew jacocoTestReport                       # 覆盖率报告
```
