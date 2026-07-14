# API 设计规范

## REST 约定

### 路由规范
```
/{role}/{domain}/{resource}
示例：
/buyer/payment/pay-flow/page       买家端支付流水分页
/seller/payment/refund-log/page    卖家端退款日志分页
/manager/payment/config            管理端支付配置
```

### 角色路由
| 角色 | 前缀 | 说明 |
|------|------|------|
| buyer | `/buyer/` | 买家端 API |
| seller | `/seller/` | 卖家端 API |
| manager | `/manager/` | 管理端 API |
| mall | `/mall/` | 商城端 API |

### HTTP 方法
| 方法 | 用途 | 状态码 |
|------|------|--------|
| GET | 查询/分页 | 200 |
| POST | 创建/命令操作 | 200/201 |
| PUT | 全量更新 | 200 |
| DELETE | 删除 | 200/204 |

### 统一响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": "2026-06-18T10:00:00Z"
}
```

### 分页请求
```
GET /{role}/payment/{resource}/page?page=0&size=20&sort=createTime,desc
```

### 分页响应
```json
{
  "code": 200,
  "data": {
    "content": [],
    "page": 0,
    "size": 20,
    "totalElements": 100,
    "totalPages": 5
  }
}
```

## 支付 API 规范

### 命令操作使用 POST
```
POST /buyer/payment/pay-flow/{sn}/refund    # 发起退款
POST /buyer/payment/pay-flow/{sn}/close     # 关闭支付
```

### 参数校验
```java
public class RefundRequest {
    @NotNull(message = "退款金额不能为空")
    private BigDecimal amount;
    @NotBlank(message = "退款原因不能为空")
    private String reason;
}
```

## gRPC / RPC 规范
- 接口定义放在 `api/rpc/` 或 `api/inner/`
- proto 文件放在 `api/src/main/proto/`
- 实现放在 `interfaces/rpc/` 或 `interfaces/grpc/`
