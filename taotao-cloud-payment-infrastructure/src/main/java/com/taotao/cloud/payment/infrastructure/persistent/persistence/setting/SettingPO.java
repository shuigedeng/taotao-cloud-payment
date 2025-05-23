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

package com.taotao.cloud.payment.infrastructure.persistent.persistence.setting;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

import java.util.Objects;

/**
 * 配置表
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-21 21:54:40
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(fluent = true)
@Entity
@Table(name = SettingPO.TABLE_NAME)
@TableName(value = SettingPO.TABLE_NAME, autoResultMap = true)
@org.springframework.data.relational.core.mapping.Table(name = SettingPO.TABLE_NAME)
public class SettingPO extends BaseSuperEntity<SettingPO, Long> {

    public static final String TABLE_NAME = "ttc_setting";

    @Column(name = "`name`", columnDefinition = "varchar(255) not null comment '名称'")
    private String name;

    @Column(name = "`category`", columnDefinition = "varchar(255) not null comment '分类'")
    private String category;

    @Column(name = "`en_code`", unique = true, columnDefinition = "varchar(255) not null comment '编码'")
    private String enCode;

    @Type(value = JsonType.class)
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Column(name = "`value`", columnDefinition = "json not null comment 'json数据'")
    private String value;

    @Override
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public void setId(Long id) {
        super.setId(id);
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getEnCode() {
		return enCode;
	}

	public void setEnCode(String enCode) {
		this.enCode = enCode;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        SettingPO setting = (SettingPO) o;
        return getId() != null && Objects.equals(getId(), setting.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
