# PROJECT KNOWLEDGE BASE

**Generated:** 2026-06-18
**Commit:** `56589ba`
**Branch:** (active branch)

## OVERVIEW

支付领域 DDD 单体服务，基于 **Spring Boot 4.1.0 / JDK 25 / Gradle 9.5**。严格遵循六边形架构 + 领域驱动设计。

核心业务：支付流水(PayFlow)、退款日志(RefundLog)、退款支撑(RefundSupport)、对账、支付单管理等。

## STRUCTURE

```
.opencode/
├── commands/        # 工作流命令（9个）
├── instructions/    # 编码规范
├── skills/          # 技能脚本
├── AGENTS.md        # 代理知识库
└── opencode.json    # OpenCode 配置
```

```
taotao-cloud-payment/
├── api/               # RPC/gRPC 接口 + DTO + proto
│   ├── rpc/           #   Dubbo/gRPC 接口定义
│   └── inner/         #   内部 Feign 接口定义
├── application/       # 应用层：编排、事务、DTO转换、Executor
│   ├── service/       #   应用服务
│   ├── executor/      #   命令/查询执行器
│   ├── handler/       #   事件/消息处理器
│   ├── assembler/     #   DTO 转换器
│   ├── dto/           #   数据传输对象
│   ├── context/       #   用户/请求上下文
│   └── factory/       #   工厂
├── assembly/          # 启动器 + 环境配置 + Docker/K8s
├── common/            # 公共工具、枚举、常量、异常
├── domain/            # ★ 领域层（零外部依赖）
│   ├── aggregate/     #   聚合根
│   ├── entity/        #   实体
│   ├── valueobject/   #   值对象
│   ├── event/         #   领域事件
│   ├── repository/    #   仓储接口
│   └── service/       #   领域服务
├── facade/            # 防腐层（ACL）
├── infrastructure/    # 持久化、MQ、事件、配置、工具
│   ├── persistent/    #   持久化（PO/Mapper/Repository实现）
│   ├── repository/    #   仓储实现
│   ├── event/         #   事件发布/订阅
│   ├── configuration/ #   全局配置（Redis/RocketMQ/AOP等）
│   └── utils/         #   工具类
└── interfaces/        # REST / RPC / gRPC
    ├── controller/    #   REST 控制器
    │   ├── buyer/     #     买家端
    │   ├── seller/    #     卖家端
    │   ├── manager/   #     管理端
    │   └── mall/      #     商城端
    ├── rpc/           #   Dubbo RPC 实现
    └── grpc/          #   gRPC 实现
```

## WHERE TO LOOK

| Task | Location |
|------|----------|
| 新增支付业务功能 | `application/service/` + `application/executor/` + `interfaces/controller/` |
| 修改领域模型 | `domain/aggregate/` 或 `domain/entity/` |
| 值对象 | `domain/valueobject/` — 所有字段 final，无 setter |
| 领域事件 | `domain/event/` — 聚合内 registerEvent，仓储 flush |
| 仓储接口 | `domain/repository/` |
| 仓储实现 | `infrastructure/repository/` 或 `infrastructure/persistent/repository/` |
| API 接口定义 | `api/rpc/` (Dubbo) 或 `api/inner/` (Feign) |
| 外部接口适配 | `facade/` |
| 消息监听 | `infrastructure/event/` |
| 定时任务 | `infrastructure/job/` |
| 支付工具类 | `infrastructure/utils/` (AlipayUtils 等) |
| 系统配置 | `infrastructure/configuration/` |
| 常量枚举 | `common/enums/`, `common/constant/` |

## CONVENTIONS

- 分层依赖方向：`interfaces → application → domain ← infrastructure`（domain 零外部依赖）
- 跨聚合通过 ID 引用，非对象引用
- 事务边界仅开在 `application/` 层
- Controller 按角色 buyer / seller / manager / mall 分包
- 命令/查询命名：`{动词}{名词}{Cmd|Qry}` （如 `AppAddCmd`, `DictPageQry`）
- DTO 分类：`dto/own/`（本域自有）、`dto/external/`（外部依赖），下设 `cmmond/`(命令)、`query/`(查询)、`clientobject/`(CO)
- 领域模型与持久化模型分离（domain entity ≠ PO）
- 聚合根使用 `@AggregateRoot` 注解标记

## ANTI-PATTERNS (THIS PROJECT)

- Controller 中写业务逻辑判断
- 聚合根中注入 Repository 或 Domain Service
- 值对象中包含业务行为以外的逻辑
- Application Service 中包含业务规则判断
- 跨聚合直接操作其他聚合的内部状态
- Application Service 中直接调用 Mapper/DAO

## UNIQUE STYLES

- **API/Interfaces 分离**：`api/` 模块只放接口定义和 DTO，`interfaces/` 模块放实现，区别于常规的单模块做法
- **Controller 按端分层**：buyer / seller / manager / mall 四个子包，各端 API 完全隔离
- **防腐层独立为模块**：`facade/` 作为独立 gradle module，而非 application 的子包
- **命令查询分离(CQRS)**：`application/executor/` 下按 `cmmond/`(命令) 和 `query/`(查询) 分包
- **Handler 模式**：`application/handler/` 处理跨聚合/跨上下文的业务逻辑
- **MapStruct + Record Builder + Lombok 三件套**：减少样板代码的同时保持不可变性
- **事件驱动**：基础设施层统一事件发布 (`DomainEventPublisher`, `RedisEventPublisher`, `GuavaEventPublisher`)
- **gRPC + Dubbo + Feign 三协议支持**：`api/` 定义 + `interfaces/` 实现

## COMMANDS

```bash
gradlew build                              # 编译（跳过测试）
gradlew :taotao-cloud-payment-assembly:bootRun --args='--spring.profiles.active=dev'  # 启动dev
gradlew checkstyleMain spotlessCheck pmdMain spotbugsMain          # 质量检查
gradlew test                               # 测试
gradlew jacocoTestReport                   # 覆盖率报告
gradlew publishToMavenLocal                # 发布到本地
```

## NOTES

- JDK 25 预览特性，`--enable-preview` + 大量 `--add-exports`
- `taotao-cloud-dependencies:2026.07` BOM 未开源，外部构建需要私有仓库凭据
- 四个环境配置：dev / test / pre / pro
- 代码质量门禁：Checkstyle + SpotBugs + PMD + Spotless + OWASP + JaCoCo
- Spring Boot 4.1.0 / Spring 7.0.8
- 支付核心技术栈：支付宝 SDK、微信支付、RocketMQ、Redis、gRPC
