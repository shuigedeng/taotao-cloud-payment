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

package com.taotao.cloud.payment.infrastructure.persistent.persistence.system;

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
import org.hibernate.Hibernate;

import java.util.Objects;

/**
 * 部门表
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
@Table(name = DeptPO.TABLE_NAME)
@TableName(DeptPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = DeptPO.TABLE_NAME)
public class DeptPO extends BaseSuperEntity<DeptPO, Long> {

    public static final String TABLE_NAME = "ttc_dept";

    /** 部门名称 */
    @Column(name = "`name`", columnDefinition = "varchar(32) not null comment '部门名称'")
    private String name;

    /** 上级部门id */
    @Column(name = "`parent_id`", columnDefinition = "bigint not null default 0 comment '上级部门id'")
    private Long parentId;

    /** 公司id */
    @Column(name = "`org_id`", columnDefinition = "bigint not null comment '组织id'")
    private Long orgId;

    /** 备注 */
    @Column(name = "`remark`", columnDefinition = "varchar(255) comment '备注'")
    private String remark;

    /** 备注 */
    @Column(name = "`id_tree`", columnDefinition = "varchar(4096) comment 'id树，逗号连接'")
    private String idTree;

    /** 当前深度 */
    @Column(name = "`depth`", columnDefinition = "int not null default 0 comment '当前深度 已1开始'")
    private Integer depth;

    /** 排序值 */
    @Column(name = "`sort_num`", columnDefinition = "int not null default 0 comment '排序值'")
    private Integer sortNum;

    /** 租户id */
    @Column(name = "`tenant_id`", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
    private String tenantId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIdTree() {
		return idTree;
	}

	public void setIdTree(String idTree) {
		this.idTree = idTree;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        DeptPO dept = (DeptPO) o;
        return getId() != null && Objects.equals(getId(), dept.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
