
package com.taotao.cloud.payment.domain.dept.entity;

import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.ddd.model.domain.AggregateRoot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@ToString
@Accessors(fluent = true)
@Schema(name = "Dept", description = "部门")
public class DeptEntity extends AggregateRoot<Long> {

	@Schema(name = "name", description = "部门名称")
	private String name;

	@Schema(name = "pid", description = "部门父节点ID")
	private Long pid;

	@Schema(name = "path", description = "部门节点")
	private String path;

	@Schema(name = "sort", description = "部门排序")
	private Integer sort;

	public void checkName(long count) {
		if (count > 0) {
			throw new BusinessException("部门名称已存在，请重新填写");
		}
	}

	public void checkIdAndPid() {
		if (id.equals(pid)) {
			throw new BusinessException("上级部门不能为当前部门");
		}
	}

}
