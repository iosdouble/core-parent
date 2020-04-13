package org.nh.core.util.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * @Classname ExcelFileUtil
 * @Description TODO Excel文件处理类
 * @Date 2020/1/15 2:42 PM
 * @Created by nihui
 */
public class ExcelFileUtil {

    /**
     * 根据输入流获取XSSFWorkbook对象（excel 2007以上）
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static XSSFWorkbook readExcel(InputStream inputStream) throws IOException {
        XSSFWorkbook xssfWorkbook=new XSSFWorkbook(inputStream);
        return xssfWorkbook;
    }


    /**
     * 根据路径获取获取XSSFWorkbook对象（excel 2007以上），增加缓存处理
     * @param path
     * @return
     * @throws IOException
     */
    public static XSSFWorkbook readExcel(String path) throws IOException{
        File file=new File(path);
        InputStream inputStream=new FileInputStream(file);
        BufferedInputStream bufferedInputStream=new BufferedInputStream(inputStream);
        return readExcel(bufferedInputStream);
    }

}
