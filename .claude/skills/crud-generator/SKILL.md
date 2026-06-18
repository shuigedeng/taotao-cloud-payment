---
name: crud-generator
description: 按 DDD 分层生成标准 CRUD 代码（Aggregate, Repository, ApplicationService, Controller）
triggers:
  - "生成CRUD"
  - "创建增删改查"
  - "新建模块"
---

# CRUD 代码生成器（DDD）

## 触发条件
用户输入包含 "生成CRUD" 或 "创建增删改查" 等关键词时自动触发。

## 工作流程

### 1. 收集信息
- 聚合根名称（如 PayFlow, RefundLog）
- 字段列表（名称 + 类型 + 业务含义）
- 是否需要分页查询
- 是否包含值对象

### 2. DDD 分层生成

按以下结构生成：

```
com.taotao.cloud.payment.{module}/
├── domain/
│   ├── aggregate/{Aggregate}.java           # 聚合根
│   ├── valueobject/{Field}VO.java           # 值对象
│   ├── event/{Aggregate}CreatedEvent.java   # 领域事件
│   ├── repository/{Aggregate}Repository.java # 仓储接口
│   └── service/{Aggregate}DomainService.java # 领域服务（可选）
├── application/
│   ├── service/{Aggregate}CommandService.java  # 命令服务
│   ├── service/{Aggregate}QueryService.java    # 查询服务
│   ├── executor/cmmond/{Aggregate}AddCmd.java  # 新增命令执行器
│   ├── executor/query/{Aggregate}PageQry.java  # 分页查询执行器
│   ├── dto/own/cmmond/{Aggregate}SaveCmd.java  # 保存命令
│   ├── dto/own/query/{Aggregate}PageQry.java   # 分页查询
│   └── assembler/{Aggregate}Assembler.java     # DTO 转换器
├── infrastructure/
│   ├── persistent/persistence/{Aggregate}PO.java    # 持久化对象
│   ├── persistent/mapper/{Aggregate}Mapper.java     # MyBatis Mapper
│   └── repository/{Aggregate}RepositoryImpl.java   # 仓储实现
└── interfaces/
    └── controller/{role}/{Aggregate}Controller.java # REST 控制器
```

### 3. 聚合根模板
```java
@AggregateRoot
public class {{Aggregate}}Agg {
    private {{Aggregate}}Id id;
    private Long refId;       // 跨聚合 ID 引用
    // ... 其他字段

    protected {{Aggregate}}Agg() {}

    public static {{Aggregate}}Agg create(/* ... */) {
        // 工厂方法
    }

    // 业务行为方法
}
```

### 4. DDD 仓储实现模板
```java
@Repository
public class {{Aggregate}}RepositoryImpl implements {{Aggregate}}Repository {
    private final {{Aggregate}}Mapper mapper;
    private final {{Aggregate}}Converter converter;

    @Override
    public {{Aggregate}}Agg findById({{Aggregate}}Id id) {
        {{Aggregate}}PO po = mapper.selectById(id.getValue());
        return converter.toDomain(po);
    }

    @Override
    public void save({{Aggregate}}Agg aggregate) {
        {{Aggregate}}PO po = converter.toPo(aggregate);
        mapper.insertOrUpdate(po);
        // 发布领域事件
    }
}
```
