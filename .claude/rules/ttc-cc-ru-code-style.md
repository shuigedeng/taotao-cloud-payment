# 代码风格规范

## 格式化规则
- **缩进**: 4 个空格（不使用 Tab）
- **行宽**: 120 字符
- **大括号**: K&R 风格（左括号不换行）
- **编码**: UTF-8
- **编译参数**: `-parameters`（保留参数名）、`--enable-preview`（JDK 25 预览特性）

## 命名约定

| 元素 | 风格 | 示例 |
|------|------|------|
| 包名 | 全小写 | `com.taotao.cloud.payment.domain.aggregate` |
| 类/接口 | PascalCase | `PayFlowAgg`, `PayFlowRepository` |
| 方法 | 小驼峰 | `findById()`, `createPayFlow()` |
| 常量 | UPPER_SNAKE | `MAX_REFUND_AMOUNT` |
| 枚举 | PascalCase | `PayStatusEnum`, `PaymentMethodEnum` |

## 包路径规范
```
com.taotao.cloud.payment.{
    domain.{aggregate|entity|valueobject|event|repository|service},
    application.{service|executor|handler|assembler|dto|context|factory},
    infrastructure.{persistent|repository|event|configuration|utils},
    interfaces.{controller.{buyer|seller|manager|mall}|rpc|grpc},
    api.{rpc|inner},
    common.{enums|constant|helper|exception},
    facade.{acl}
}
```

## 导入顺序
1. Java 标准库（`java.*`）
2. JDK 扩展（`javax.*`, `jdk.*`）
3. 第三方库（`org.*`, `com.*` 非项目）
4. Spring 框架（`org.springframework.*`）
5. 项目内部包（`com.taotao.cloud.*`）
6. 静态导入

## 三件套规范

### Lombok
```java
@Slf4j                          // 日志
@RequiredArgsConstructor         // 构造器注入（配合 final 字段）
@Getter @Setter (谨慎使用 setter)  // 值对象禁止 setter
```

### Record Builder
```java
@RecordBuilder                   // 生成 builder 模式
public record CreatePayFlowCommand(
    Long orderId,
    Money amount,
    PaymentMethodEnum method
) {}
```

### MapStruct
```java
@Mapper(componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface PayFlowAssembler {
    PayFlowPo toPo(PayFlowAgg payFlow);
    PayFlowAgg toDomain(PayFlowPo po);
}
```

## 禁止项
- 类型擦除：`@SuppressWarnings("unchecked")` 必须附注释说明
- `as any` / `@ts-ignore` / `@ts-expect-error`（非 JS 项目，Java 中同理避免 raw type）
- 空的 catch 块 `catch(Exception e) {}`
- 支付金额使用 `float`/`double`（必须使用 `BigDecimal`）
