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

package com.taotao.cloud.payment.application.service;

import com.taotao.boot.ddd.model.application.service.CommandService;

/**
 * 后台部门表服务接口
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:24:48
 */
public interface DeptService extends CommandService {

    /**
     * 获取部门树
     *
     * @return 部门树列表
     * @since 2022-03-23 08:52:34
     */
//    List<DeptTreeVO> tree();
}
