package org.nh.core.util.excel;

import org.apache.poi.hssf.usermodel.*;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * @class: ExcelUtil
 * @description: TODO 生成一个Excel文件 待扩展
 * @date 2020/1/15 2:04 PM
**/
public class ExcelUtil {
    /**
     * @功能：手工构建一个简单格式的Excel
     */
    public static void createExcel(Map<String, List<String>> map, String[] strArray) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("sheet1");
        sheet.setDefaultColumnWidth(20);// 默认列宽
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        // 创建一个居中格式
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // 添加excel title
        HSSFCell cell = null;
        for (int i = 0; i < strArray.length; i++) {
            cell = row.createCell((short) i);
            cell.setCellValue(strArray[i]);
            cell.setCellStyle(style);
        }

        // 第五步，写入实体数据 实际应用中这些数据从数据库得到,list中字符串的顺序必须和数组strArray中的顺序一致
        int i = 0;
        for (String str : map.keySet()) {
            row = sheet.createRow((int) i + 1);
            List<String> list = map.get(str);
            // 第四步，创建单元格，并设置值
            if (list == null){
                continue;
            }
            for (int j = 0; j < strArray.length; j++) {
                row.createCell((short) j).setCellValue(list.get(j));
            }

            // 第六步，将文件存到指定位置
            try {
                FileOutputStream fout = new FileOutputStream("/Users/nihui/Documents/IDEAProject/Export-Support/export-support/fortress.xls");
                wb.write(fout);
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }
    }
}

