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

package com.taotao.cloud.payment.infrastructure.persistent.persistence.config;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 支付宝配置表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:22
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(fluent = true)
@Entity
@Table(name = AlipayConfigPO.TABLE_NAME)
@TableName(AlipayConfigPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = AlipayConfigPO.TABLE_NAME)
public class AlipayConfigPO extends BaseSuperEntity<AlipayConfigPO, Long> {

    public static final String TABLE_NAME = "ttc_alipay_config";

    /** 应用ID */
    @Column(name = "`app_id`", columnDefinition = "varchar(64) not null comment '应用ID'")
    private String appId;

    /** 编码 */
    @Column(name = "`charset`", columnDefinition = "varchar(32) not null comment '编码'")
    private String charset;

    /** 类型 固定格式json */
    @Column(name = "`format`", columnDefinition = "json not null comment '类型 固定格式json'")
    private String format;

    /** 网关地址 */
    @Column(name = "`gateway_url`", columnDefinition = "varchar(256) not null comment '网关地址'")
    private String gatewayUrl;

    /** 异步回调 */
    @Column(name = "`notify_url`", columnDefinition = "varchar(256) not null comment '异步回调'")
    private String notifyUrl;

    /** 私钥 */
    @Column(name = "`private_key`", columnDefinition = "varchar(64) not null comment '私钥'")
    private String privateKey;

    /** 公钥 */
    @Column(name = "`public_key`", columnDefinition = "varchar(64) not null comment '公钥'")
    private String publicKey;

    /** 回调地址 */
    @Column(name = "`return_url`", columnDefinition = "varchar(256) not null comment '回调地址'")
    private String returnUrl;

    /** 签名方式 */
    @Column(name = "`sign_type`", columnDefinition = "varchar(32) not null comment '签名方式'")
    private String signType;

    /** 商户号 */
    @Column(name = "`service_provider_id`", columnDefinition = "varchar(32) not null comment '商户号'")
    private String serviceProviderId;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getGatewayUrl() {
		return gatewayUrl;
	}

	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(String serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}
}
