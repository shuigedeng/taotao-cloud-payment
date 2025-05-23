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
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.scheduling.annotation.Async;

/**
 * VisitsService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-11 16:22:47
 */
public interface IVisitsService extends CommandService {

    /** 提供给定时任务，每天0点执行 */
    void save();

    /**
     * 新增记录
     *
     * @param request /
     */
    @Async
    void count(HttpServletRequest request);

    /**
     * 获取数据
     *
     * @return /
     */
    Object get();

    /**
     * getChartData
     *
     * @return /
     */
    Object getChartData();
}
