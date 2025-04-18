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

package com.taotao.cloud.payment.infrastructure.shared.payment;

import com.taotao.boot.common.enums.ClientTypeEnum;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/** 支持的支付方式表单 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class PaymentSupportForm {

    /**
     * 客户端类型列表
     *
     * @return 客户端类型
     */
    public List<ClientTypeEnum> getClients() {
        List<ClientTypeEnum> keys = new ArrayList<>();
        for (ClientTypeEnum clientTypeEnum : ClientTypeEnum.values()) {
            if (clientTypeEnum.equals(ClientTypeEnum.UNKNOWN)) {
                continue;
            }
            keys.add(clientTypeEnum);
        }
        return keys;
    }

    /**
     * 支付方式列表
     *
     * @return 即支持的支付方式集合
     */
    // public List<PaymentMethodEnum> getPayments() {
    //
    //    List<PaymentMethodEnum> keys = new ArrayList<>();
    //    for (PaymentMethodEnum paymentMethodEnum : PaymentMethodEnum.values()) {
    //        if (paymentMethodEnum.equals(PaymentMethodEnum.BANK_TRANSFER)){
    //            continue;
    //        }
    //        keys.add(paymentMethodEnum);
    //    }
    //    return keys;
    // }
}
