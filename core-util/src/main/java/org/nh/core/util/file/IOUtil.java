package org.nh.core.util.file;

import org.apache.commons.lang3.StringUtils;
import org.nh.core.util.string.CharsetContants;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @Classname IOUtil
 * @Description TODO 用于文件读取、文件写入、流处理的工具类
 * @Date 2020/1/15 2:08 PM
 * @Created by nihui
 */
public class IOUtil {

    /**
     * 将输入流写入到一个文件中
     * @param inputStream 输入流
     * @param file 文件对象
     * @param isClosedIs 结束后是否关闭输入流，如果为null，默认是关闭
     * @return
     * @throws IOException
     */
    public static boolean inputStreamToFile(InputStream inputStream, File file, Boolean isClosedIs) throws IOException {
        if (isClosedIs == null) {
            isClosedIs = true;
        }

        if (file == null || file.isDirectory()) {
            return false;
        }

        BufferedInputStream bis = new BufferedInputStream(inputStream);
        OutputStream os = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        byte[] buffer = new byte[1024];
        int i = 0;
        while ((i = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, i);
        }
        bos.flush();
        bos.close();
        os.close();
        bis.close();

        if (isClosedIs) {
            inputStream.close();
        }
        return true;
    }

    /***
     * 将输入流写入一个文件中
     * @param inputStream 输入流
     * @param fileFullName 写入文件全路径
     * @param isCreate 是否是新建
     * @param overWtirte 是否覆盖
     * @param isClosedIs 是否关闭流
     * @return 成功失败
     * @throws IOException
     */

    public static boolean inputStreamToFile(InputStream inputStream,String fileFullName,boolean isCreate,boolean overWtirte,boolean isClosedIs) throws IOException {
        File file = FileUtil.getFileHisOrCreate(fileFullName,isCreate,overWtirte);
        return inputStreamToFile(inputStream,file,isClosedIs);
    }


    /***
     * 将文件写入一个输出流
     *
     * @param file
     * @param outputStream
     * @return
     * @throws IOException
     */
    public static boolean fileToOutputStream(File file, OutputStream outputStream) throws IOException {
        if (file == null || file.isDirectory()) {
            return false;
        }
        InputStream is = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        byte[] buffer = new byte[1024];
        int i = 0;
        // bos.write(buffer);
        while ((i = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, i);
        }
        is.close();
        bis.close();
        bos.flush();
        outputStream.flush();
        bos.close();
        bis.close();
        return true;
    }

    /**
     * 将文件写入一个输出流
     * @param fileFullName
     * @param outputStream
     * @return
     * @throws IOException
     */
    public static boolean fileToOutputStream(String fileFullName, OutputStream outputStream) throws IOException {
        File file = new File(fileFullName);
        return fileToOutputStream(file, outputStream);
    }

    /**
     * 读取文件中的字符串
     *
     * @param file 文件
     * @param charset 字符集
     * @return 文件中的字符串
     * @throws IOException
     */
    public static String getStr4File(File file, String charset) throws IOException {
        if(StringUtils.isBlank(charset)){
            charset=CharsetContants.UTF8;
        }

        if (file == null || file.isDirectory()) {
            return null;
        }
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        InputStreamReader isr = new InputStreamReader(bis, Charset.forName(charset));
        BufferedReader br = new BufferedReader(isr);
        StringBuffer sb = new StringBuffer();
        while (br.ready()) {
            sb.append(br.readLine());
        }
        br.close();
        isr.close();
        bis.close();
        fis.close();

        return sb.toString();
    }

    /**
     * 读取文件中的字符串
     * @param fileFullPath 文件全路径
     * @param charset 字符集
     * @return 字符串
     * @throws IOException
     */
    public static String getStr4File(String fileFullPath, String charset) throws IOException {
        return getStr4File(new File(fileFullPath), charset);
    }

    /**
     * 读取文件中得字符串
     * @param filePath 文件路径
     * @param fileName 文件名
     * @param charset 字符集
     * @return 文件中字符串
     * @throws IOException
     */

    public static String getStr4File(String filePath, String fileName, String charset) throws IOException {
        return getStr4File(new File(filePath + "/" + fileName), charset);
    }

    /***
     * 将字符串写入到文件中
     * @param str 需要写入文件中的字符串
     * @param file 需要写入的文件
     * @param charset 字符集
     * @param isAppend 是否开启追加模式
     * @return 完成的文件
     * @throws IOException
     */
    public static File setStr2File(String str, File file,String charset,boolean isAppend) throws IOException {
        if (file == null) {
            return null;
        }
        if(StringUtils.isBlank(charset)){
            charset=CharsetContants.UTF8;
        }

        if (!file.exists() && !file.isFile()) {
            return null;
        }

        OutputStream os = new FileOutputStream(file, isAppend);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        OutputStreamWriter osw = new OutputStreamWriter(bos, Charset.forName(charset));
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(str);
        bw.flush();
        osw.flush();
        bos.flush();
        os.flush();
        bw.close();
        osw.close();
        bos.close();
        os.close();
        return file;
    }

    /**
     *  将字符串写入到文件中
     * @param str
     * @param fileFullName
     * @param charset
     * @param isCreate
     * @param overWrite
     * @param isAppend
     * @return
     * @throws IOException
     */
    public static File setStr2File(String str, String fileFullName, String charset, boolean isCreate, boolean overWrite,boolean isAppend)
            throws IOException {
        File file = FileUtil.getFileHisOrCreate(fileFullName, isCreate, overWrite);
        return setStr2File(str, file,charset,isAppend);
    }

    /**
     * 将Byte数组写入到文件中
     * @param bytes
     * @param file
     * @return
     */

    public static boolean setByteArr2File(byte[] bytes, File file) {
        if (file == null) {
            return false;
        }
        if (!file.exists() && !file.isFile()) {
            return false;
        }
        OutputStream os = null;
        BufferedOutputStream bos = null;
        try {
            os = new FileOutputStream(file);
            bos = new BufferedOutputStream(os);
            bos.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                bos.flush();
                os.flush();
                bos.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;

    }

    /***
     * 将一个文件的内容拷贝到另一个文件中
     * @param sourceFileFullName
     * @param descFileFullName
     * @param isCreate
     * @param overWrite
     * @return
     * @throws IOException
     */
    public static boolean file2File(String sourceFileFullName, String descFileFullName, boolean isCreate,boolean overWrite
    ) throws IOException {
        InputStream inputStream = new FileInputStream(sourceFileFullName);
        boolean result=inputStreamToFile(inputStream, descFileFullName, isCreate, overWrite, true);
        return result;
    }


    /**
     * 将一个文件的内容拷贝到另一个临时文件中
     * @param sourceFileFullName
     * @param tempFilePrefix
     * @param tempFileSuffix
     * @param tmpDir
     * @return
     * @throws IOException
     */
    public static File file2TempFile(String sourceFileFullName, String tempFilePrefix, String tempFileSuffix, File tmpDir)
            throws IOException {
        File tempFile = File.createTempFile(tempFilePrefix, tempFileSuffix, tmpDir);
        boolean result=file2File(sourceFileFullName, tempFile.getPath(), false, false);
        if(result){
            return tempFile;
        }
        tempFile.delete();
        return null;
    }

    /**
     * 将字符输出流包装成字节输出流，如果字符集为空则默认为UTF-8
     * @param outputStream
     * @param charset
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static OutputStreamWriter getOutputStreamWriter(OutputStream outputStream,String charset) throws FileNotFoundException, UnsupportedEncodingException{
        if(StringUtils.isBlank(charset)){
            charset=CharsetContants.UTF8;
        }
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream, charset);
        return outputStreamWriter;
    }


    /**
     * 将文件包装成字节输出流，如果字符集为空则默认为UTF-8
     * @param file
     * @param charset
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static OutputStreamWriter getOutputStreamWriter(File file,String charset) throws FileNotFoundException, UnsupportedEncodingException{
        FileOutputStream fileOutputStream=new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter=getOutputStreamWriter(fileOutputStream, charset);
        return outputStreamWriter;
    }

    /***
     * 将文件包装成带缓存的字节输出流，如果字符集为空则默认为UTF-8
     * @param file
     * @param charset
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static BufferedWriter getBufferedWriter(File file,String charset) throws FileNotFoundException, UnsupportedEncodingException{
        OutputStreamWriter outputStreamWriter=getOutputStreamWriter(file,charset);
        BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
        return bufferedWriter;
    }
}
