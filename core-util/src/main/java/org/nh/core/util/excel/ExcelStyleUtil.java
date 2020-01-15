package org.nh.core.util.excel;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nh.core.util.string.FontContants;

/**
 * @Classname ExcelStyleUtil
 * @Description TODO Excel 样式处理
 * @Date 2020/1/15 2:38 PM
 * @Created by nihui
 */
public class ExcelStyleUtil {

    /**
     * 获取单元格样式（居中、粗体、微软雅黑、18号字）
     * @param excel
     * @return
     */
    public static XSSFCellStyle getCenterBoldStyle(XSSFWorkbook excel) {
        XSSFCellStyle style = excel.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        XSSFFont font = excel.createFont();
        font.setFontName(FontContants.WEI_RUAN_YAN_HEI);
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);

        style.setFont(font);

        return style;
    }

    /**
     * 获取单元格样式（居左、微软雅黑、18号字）
     * @param excel
     * @return
     */
    public static XSSFCellStyle getLeftStyle(XSSFWorkbook excel) {
        XSSFCellStyle style = excel.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        XSSFFont font = excel.createFont();
        font.setFontName(FontContants.WEI_RUAN_YAN_HEI);
        font.setFontHeightInPoints((short) 11);
        font.setBold(false);

        style.setFont(font);

        return style;
    }
}
