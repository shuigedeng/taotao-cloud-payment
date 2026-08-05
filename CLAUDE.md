# taotao-cloud-payment — 支付领域 DDD 单体服务

## 技术栈

| 依赖 | 版本 |
|------|------|
| JDK | 25（预览特性，`--enable-preview`） |
| Gradle | 9.5 |
| Spring Boot | 4.1.0 / Spring 7.0.8 |
| MyBatis-Plus | 3.5.16 |
| MapStruct | 1.6.3 + Record Builder 52 + Lombok 1.18.46 |
| RocketMQ / Kafka | 消息中间件 |
| Redis (Redisson 4.3.1) | 分布式缓存 |
| gRPC / Dubbo / Feign | 三协议 RPC |

## 模块结构

```
api/               RPC/gRPC 接口定义 + DTO + proto
application/       应用层：编排、事务、Executor、Handler、Assembler
assembly/          启动器 + 环境配置 + Docker/K8s
common/            公共工具、枚举、常量、异常
domain/ ★          领域层：聚合根、实体、值对象、领域事件、仓储接口（零外部依赖）
facade/            防腐层 ACL
infrastructure/    持久化、MQ、事件、配置、工具
interfaces/        REST / RPC / gRPC 实现（buyer/seller/manager/mall）
```

## 开发命令

```bash
gradlew build -x test                                   # 编译（跳过测试）
gradlew :taotao-cloud-payment-assembly:bootRun --args='--spring.profiles.active=dev'  # 本地启动
gradlew test                                            # 运行测试
gradlew jacocoTestReport                                # 覆盖率报告
gradlew checkstyleMain spotlessCheck pmdMain spotbugsMain  # 代码质量检查
gradlew publishToMavenLocal                             # 发布到本地
```

## DDD 分层铁律

- **依赖方向**: `interfaces → application → domain ← infrastructure`（domain 零外部依赖）
- **事务边界**: 仅开在 `application/` 层
- **跨聚合**: 通过 ID 引用，非对象引用；通过应用服务协调
- **仓储接口**: 定义在 `domain/repository/`，实现在 `infrastructure/repository/`
- **领域模型 ≠ 持久化模型**: domain entity 与 PO 分离
- **Controller 按端分包**: buyer / seller / manager / mall

## 禁止项

- Controller 中写业务逻辑
- 聚合根中注入 Repository 或 Domain Service
- 值对象包含业务行为以外的逻辑（保持内聚）
- Application Service 中包含业务规则判断（仅编排）
- Application Service 中直接调用 Mapper/DAO
- 跨聚合直接操作其他聚合内部状态
- 在事务中调用远程 RPC（可能导致分布式事务超时）

## 支付域约定

- 金额使用 `BigDecimal`（不可用 `double`/`float`），精度为分
- 交易 ID 使用 `PayFlowId` 值对象封装，非原始 `Long`/`String`
- 幂等键必传：写接口须检查 `idempotentKey` 防重复支付
- 退款金额不得超过原交易可退金额（领域规则在 `domain/` 层校验）
- 支付状态转移通过状态机（`domain/aggregate/enums/PayFlowStatus`），禁止直接 setter 修改
- 审计日志：所有支付/退款操作写入 `pay_event_log`，不可篡改

## 代码约定

- DTO 分类：`dto/external/`（外部依赖）、`dto/own/`（本域自有），下设 `command/` `query/` `clientobject/`
- 命令/查询命名：`{动词}{名词}{Cmd|Qry}`（如 `RefundCmd`, `PayFlowPageQry`）
- 聚合根使用 `@AggregateRoot` 注解标记
- 值对象：所有字段 `final`，构造时自验证，无 setter

## 环境配置

- 四环境：dev / test / pre / pro
- 质量门禁：Checkstyle + SpotBugs + PMD + Spotless + OWASP + JaCoCo
- BOM `taotao-cloud-dependencies:2026.09` 未开源，需私有仓库凭据
- 外部调用配置：Nacos (`localhost:8848`)、Redis (`localhost:6379`)、MySQL (`localhost:3306`)

## 深度参考

各层详细规范见 `.claude/rules/` 目录。项目安全配置见 `.claude/settings.json`。
