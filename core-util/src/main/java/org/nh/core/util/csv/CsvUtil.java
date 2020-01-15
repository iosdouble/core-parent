package org.nh.core.util.csv;

import org.nh.core.util.file.IOUtil;
import org.nh.core.util.string.CharsetContants;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Classname CsvUtil
 * @Description TODO 生成CSV文件
 * @Date 2020/1/15 2:04 PM
 * @Created by nihui
 */
public class CsvUtil {

    /**
     * 将数据写入一行
     * @param rowData
     * @param bufferedWriter
     * @throws IOException
     */
    private static void writeRow(List<Object> rowData, BufferedWriter bufferedWriter) throws IOException {
        int count = rowData.size();
        for (int i = 0; i < rowData.size(); i++) {
            StringBuffer sb = new StringBuffer();
            String rowStr = null;
            if (i == count - 1) {
                rowStr = sb.append(rowData.get(i)).toString();
            } else {
                rowStr = sb.append(rowData.get(i)).append(",").toString();
            }
            bufferedWriter.write(rowStr);
        }
        bufferedWriter.newLine();
    }

    private static void writeRow(List<Object> rowData, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        int count = rowData.size();
        for (int i = 0; i < rowData.size(); i++) {
            StringBuffer sb = new StringBuffer();
            String rowStr = null;
            if (i == count - 1) {
                rowStr = sb.append(rowData.get(i)).toString();
            } else {
                rowStr = sb.append(rowData.get(i)).append(",").toString();
            }
            byteArrayOutputStream.write(rowStr.getBytes(CharsetContants.UTF8));
        }
        byteArrayOutputStream.write("\r\n".getBytes(CharsetContants.UTF8));
    }


    /**
     * 将数据写入到File 默认实现UTF-8
     * @param file
     * @param headDataList
     * @param datasList
     * @throws Exception
     */
    public static void writeCsv(File file, List<Object> headDataList, List<List<Object>> datasList) throws Exception {

        try (BufferedWriter bufferedWriter = IOUtil.getBufferedWriter(file, CharsetContants.UTF8)) {
            if (headDataList != null) {
                writeRow(headDataList, bufferedWriter);
            }
            if (datasList != null) {
                for (int i = 0; i < datasList.size(); i++) {
                    List<Object> rowData = datasList.get(i);
                    writeRow(rowData, bufferedWriter);
                }
            }
            bufferedWriter.flush();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 将数据写入byteArrayOutputStream
     *
     * @param byteArrayOutputStream
     * @param headDataList
     * @param datasList
     * @throws Exception
     */
    public static void writeCsv(ByteArrayOutputStream byteArrayOutputStream, List<Object> headDataList,
                                List<List<Object>> datasList) throws Exception {
        byte[] utf8Bom={(byte)0xEF,(byte)0xBB,(byte)0xBF};
        byteArrayOutputStream.write(utf8Bom);
        if (headDataList != null) {
            writeRow(headDataList, byteArrayOutputStream);
        }
        if (datasList != null) {
            for (int i = 0; i < datasList.size(); i++) {
                List<Object> rowData = datasList.get(i);
                writeRow(rowData, byteArrayOutputStream);
            }
        }
    }
}
