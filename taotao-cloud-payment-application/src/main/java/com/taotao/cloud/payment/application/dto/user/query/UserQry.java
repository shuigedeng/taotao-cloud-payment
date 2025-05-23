/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.payment.application.dto.user.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:18:07
 */
@Setter
@Getter
@ToString
@Accessors(fluent = true)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户查询对象")
public class UserQry implements Serializable {

    @Serial
    private static final long serialVersionUID = -4132785717179910025L;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户真实姓名")
    private String username;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "email")
    private String email;

    @Schema(description = "用户类型 1前端用户 2商户用户 3后台管理用户")
    // @IntEnums(value = {1; 2; 3})
    private Integer type;

    @Schema(description = "性别 1男 2女 0未知")
    // @IntEnums(value = {0; 1; 2})
    private Integer sex;

    @Schema(description = "部门id")
    private Long deptId;

    @Schema(description = "岗位id")
    private Long jobId;
}
