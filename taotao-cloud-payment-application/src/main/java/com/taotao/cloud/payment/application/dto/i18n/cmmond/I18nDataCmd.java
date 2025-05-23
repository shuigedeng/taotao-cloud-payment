package com.taotao.cloud.payment.application.dto.i18n.cmmond;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 国际化信息传输对象
 */
@Setter
@Getter
@ToString
@Schema(title = "国际化信息传输对象")
public class I18nDataCmd {

	private static final long serialVersionUID = 1L;

	/**
	 * 语言标签
	 */
	@Schema(title = "语言标签")
	private String languageTag;

	/**
	 * 唯一标识 = 业务:关键词
	 */
	@Schema(title = "唯一标识 = 业务:关键词")
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

}
