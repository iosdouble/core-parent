package org.nh.core.util.excel;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;
import org.nh.core.util.date.DateTimeUtil;

import java.util.Date;
import java.util.List;

/**
 * @Classname ExcelContentUtil
 * @Description TODO Excel内容处理
 * @Date 2020/1/15 2:37 PM
 * @Created by nihui
 */
public class ExcelContentUtil {

    /**
     * 创建excel的一行标题，最上面（居中、粗体、微软雅黑、18号字）
     * @param excel
     * @param sheet
     * @param titleArray
     */
    public static void createTitleRow(XSSFWorkbook excel, XSSFSheet sheet, String[] titleArray){
        if(titleArray==null || titleArray.length==0){
            return;
        }
        XSSFRow titleRow =sheet.createRow(0);
        XSSFCellStyle titleStyle = ExcelStyleUtil.getCenterBoldStyle(excel);

        for (int i = 0; i < titleArray.length; i++) {
            XSSFCell titleCell =titleRow.createCell(i);
            titleCell.setCellStyle(titleStyle);
            titleCell.setCellValue(titleArray[i]);
        }
    }


    /**
     * 创建excel的一行内容（居左、微软雅黑、18号字）
     * @param excel
     * @param sheet
     * @param rowNum
     * @param contentArray
     */
    public static void createContentRow(XSSFWorkbook excel,XSSFSheet sheet,int rowNum,String[] contentArray){
        if(contentArray==null || contentArray.length==0){
            return;
        }
        XSSFRow row =sheet.createRow(rowNum);
        XSSFCellStyle style = ExcelStyleUtil.getLeftStyle(excel);

        for (int i = 0; i < contentArray.length; i++) {
            XSSFCell titleCell =row.createCell(i);
            titleCell.setCellStyle(style);
            titleCell.setCellValue(contentArray[i]);
        }
    }

    public static void createContentRow(XSSFWorkbook excel,XSSFSheet sheet,int rowNum,List<String> contentList){
        if(contentList==null || contentList.size()==0){
            return;
        }
        XSSFRow row =sheet.createRow(rowNum);
        XSSFCellStyle style = ExcelStyleUtil.getLeftStyle(excel);

        for (int i = 0; i < contentList.size(); i++) {
            XSSFCell titleCell =row.createCell(i);
            titleCell.setCellStyle(style);
            titleCell.setCellValue(contentList.get(i));
        }
    }

    /**
     * 获取Excel 某个单元格的数据
     * @param xssfCell
     * @return
     */
    public static String readCellContentToString(XSSFCell xssfCell){
        CellType xssfCellType=xssfCell.getCellTypeEnum();
        String result=null;
        switch (xssfCellType) {
            case STRING:
                result=xssfCell.getStringCellValue();
                break;
            case NUMERIC:
                double tempResult=xssfCell.getNumericCellValue();
                result=String.valueOf(tempResult);
            default:
                break;
        }
        return result;
    }

    /**
     * 获取Excel 某个单元格式数据
     * @param xssfCell
     * @return
     */
    public static Double readCellContentToDouble(XSSFCell xssfCell){
        CellType xssfCellType=xssfCell.getCellTypeEnum();
        Double result=null;
        switch (xssfCellType) {
            case STRING:
                String tempResult=xssfCell.getStringCellValue();
                result=Double.valueOf(tempResult);
                break;
            case NUMERIC:
                result=xssfCell.getNumericCellValue();
            default:
                break;
        }
        return result;
    }

    /**
     * 获取某个单元格数据
     * @param xssfCell
     * @return
     */
    public static Date readCellContentToDate(XSSFCell xssfCell){
        CellType xssfCellType=xssfCell.getCellTypeEnum();
        Date result=null;
        switch (xssfCellType) {
            case STRING:
                String tempResult=xssfCell.getStringCellValue();
                result=DateTimeUtil.getFormatDateTime(tempResult, DateTimeUtil.FORMAT_DATE_NUMBER);
                break;
            case NUMERIC:
                result=xssfCell.getDateCellValue();
            default:
                break;
        }
        return result;
    }

}
