package com.taotao.cloud.payment.infrastructure.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 国际化信息模型转换器
 */
@Mapper
public interface I18nDataAssembler {

	I18nDataAssembler INSTANCE = Mappers.getMapper(I18nDataAssembler.class);

//	/**
//	 * i18nMessage 转 I18nData
//	 * @param i18nMessage 国际化消息
//	 * @return I18nData 国际化信息实体
//	 */
//	I18nData messageToPo(I18nMessage i18nMessage);
//
//	/**
//	 * PO 转 PageVO
//	 * @param i18nData 国际化信息
//	 * @return I18nDataPageVO 国际化信息PageVO
//	 */
//	I18nDataPageVO poToPageVo(I18nData i18nData);
//
//	/**
//	 * PO 转 DTI
//	 * @param i18nData 国际化信息
//	 * @return I18nDataDTO 国际化信息DTO
//	 */
//	I18nDataDTO poToDto(I18nData i18nData);
//
//	/**
//	 * PO 转 ExcelVO
//	 * @param i18nData 国际化信息
//	 * @return I18nDataExcelVO 国际化信息ExcelVO
//	 */
//	I18nDataExcelVO poToExcelVo(I18nData i18nData);
//
//	/**
//	 * ExcelVO转 PO
//	 * @param i18nDataExcelVO 国际化信息ExcelVO
//	 * @return I18nData 国际化信息
//	 */
//	I18nData excelVoToPo(I18nDataExcelVO i18nDataExcelVO);

}
