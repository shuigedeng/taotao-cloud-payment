package com.taotao.cloud.payment.application.dto.i18n.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 国际化信息分页视图对象
 */
@Setter
@Getter
@ToString
@Schema(title = "国际化信息分页视图对象")
public class I18nDataPageCO {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@Schema(title = "ID")
	private Integer id;

	/**
	 * 语言标签
	 */
	@Schema(title = "语言标签")
	private String languageTag;

	/**
	 * 国际化标识
	 */
	@Schema(title = "国际化标识")
	private String code;

	/**
	 * 文本值，可以使用 { } 加角标，作为占位符
	 */
	@Schema(title = "文本值，可以使用 { } 加角标，作为占位符")
	private String message;

	/**
	 * 备注
	 */
	@Schema(title = "备注")
	private String remarks;

	/**
	 * 创建时间
	 */
	@Schema(title = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@Schema(title = "修改时间")
	private LocalDateTime updateTime;

}
