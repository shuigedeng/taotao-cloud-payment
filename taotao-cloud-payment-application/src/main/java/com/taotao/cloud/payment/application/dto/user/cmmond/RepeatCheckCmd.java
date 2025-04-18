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

package com.taotao.cloud.payment.application.dto.user.cmmond;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 重复校验DTO
 *
 * @author shuigedeng
 * @since 2020/5/2 16:40
 */
@Setter
@Getter
@ToString
@Accessors(fluent = true)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "重复检查DTO")
public class RepeatCheckCmd implements Serializable {

    @Serial
    private static final long serialVersionUID = -4132785717179910025L;

    @Schema(description = "字段值 邮箱 手机号 用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fieldVal;

    @Schema(description = "指用户id 主要作用编辑情况过滤自己的校验", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer dataId;
}
