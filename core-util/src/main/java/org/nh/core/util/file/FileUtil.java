package org.nh.core.util.file;


import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @Classname FiltUtil
 * @Description TODO
 * @Date 2020/1/15 2:13 PM
 * @Created by nihui
 */
public class FileUtil {

    /**
     * 非递归获取目录下所有符合正则表达式的文件
     * @param dirPath 目录
     * @param regex 当该正则表达式为"" 或者null 的时候则表示不会用正则表达式匹配
     * @return
     */
    public static List<File> getFileList(String dirPath,String regex){
        List<File> fileList = new ArrayList<>();
        File fileDir = new File(dirPath);
        if (fileDir==null||!fileDir.exists()||fileDir.isFile()){
            return fileList;
        }
        File[] fileArray = fileDir.listFiles();
        if (fileArray==null||fileArray.length==0){
            return fileList;
        }

        for (File file: fileArray) {
            if (file.isFile()){
                String fileName = file.getName();
                if (StringUtils.isNotBlank(regex)){
                    if (fileName.matches(regex)){
                        fileList.add(file);
                    }
                }else {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }

    /**
     * 非递归获取目录下的所有的文件（不包含目录中的目录）
     * @param dirPath
     * @return
     */
    public static List<File> getFileList(String dirPath){
        return getFileList(dirPath, null);
    }


    /**
     * 非递归获取目录下的所有符合正则表达式的文件（不包含目录中的目录） ,并排序
     * @param dirPath
     * @param regex 当该正则表达式为""或者null的时候，则不会使用正则表达式匹配
     * @return  List<File>，不会为null
     */
    public static List<File> getSortFileList(String dirPath,String regex){
        List<File> fileList=getFileList(dirPath,regex);
        fileList.sort(new Comparator<File>() {

            /**
             * 如果要按照升序排序，
             * 则o1 小于o2，返回-1（负数），相等返回0，01大于02返回1（正数）
             * 如果要按照降序排序
             * 则o1 小于o2，返回1（正数），相等返回0，01大于02返回-1（负数）
             */
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }

        });
        return fileList;
    }

    /**
     * 非递归获取目录下的所有的文件（不包含目录中的目录） ,并排序
     * @param dirPath
     * @return
     */
    public static List<File> getSortFileList(String dirPath){
        return getSortFileList(dirPath, null);
    }

    /**
     * 创建目录，当目录已经存在会返回true，当目录不存在则创建,如果该path为一个file，则返回false
     * @param path 目录路径名称
     * @return 目录是否可用的结果，true or false
     */

    public static boolean mkDir(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                return false;
            }
            return true;
        } else {
            boolean result=file.mkdirs();
            return result;
        }
    }


    /**
     * 创建文件
     * @param file file对象
     * @param overWrite 是否删除重新创建file
     * @return
     * 如果overWrite=false，文件已存在，并且是目录则返回false，是file则返回false
     * 如果overWrite=true，文件已存在，并且是目录则返回false，是file则删除重新创建
     * 如果根目录存在，创建新文件，创建失败则返回false，并且打印错误堆栈信息
     * 如果目录不存在，则先创建目录再创建新文件，创建失败则返回false，并且打印错误堆栈信息
     */
    public static boolean mkFile(File file,boolean overWrite){
        if (!overWrite) {
            if (file.exists()) {
                if(file.isDirectory()){
                    return false;
                }
                return false;
            }
        } else {
            if (file.exists()) {
                if(file.isDirectory()){
                    return false;
                }
                file.delete();
            }
        }
        if (file.getParentFile().exists() && file.getParentFile().isDirectory()) {
            try {
                file.createNewFile();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            String path = file.getParent();
            if (mkDir(path)) {
                try {
                    file.createNewFile();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 创建文件
     *
     * @param fileFullName 文件名称（带路径）
     * @param overWrite 是否删除重新创建file
     * @return
     * 如果overWrite=false，文件已存在，并且是目录则返回false，是file则返回true
     * 如果overWrite=true，文件已存在，并且是目录则返回false，是file则删除重新创建
     * 如果根目录存在，创建新文件，创建失败则返回false，并且打印错误堆栈信息
     * 如果目录不存在，则先创建目录再创建新文件，创建失败则返回false，并且打印错误堆栈信息
     */
    public static boolean mkFile(String fileFullName, boolean overWrite) {
        File file = new File(fileFullName);
        return mkFile(file, overWrite);
    }


    /**
     * 获取文件对象
     *
     * @param fileFullName 文件名称（带路径）
     * @param isCreate 不存在时是否创建是否创建
     * @param overWrite 存在时是否覆盖
     * @return
     * 如果文件存在，如果是目录，则返回null
     * 如果文件存在，如果不是目录，如果overWrite=true，则执行mkFile(file, overWrite)，删除后创建，创建成功返回file，失败返回null
     * 如果文件存在，如果不是目录，如果overWrite=false，则返回file
     * 如果文件不存在，如果isCreate=true，则执行mkFile(file, overWrite)，创建成功返回file，创建失败返回null
     * 如果文件不存在，如果isCreate=false，则返回null
     * 其他情况，返回null
     */
    public static File getFileHisOrCreate(String fileFullName, boolean isCreate, boolean overWrite) {
        File file = new File(fileFullName);
        if(file.exists()){
            if(file.isDirectory()){
                return null;
            }else{
                if(overWrite){
                    boolean result=mkFile(file, overWrite);
                    if(result){
                        return file;
                    }else{
                        return null;
                    }
                }
                return file;
            }
        }else{
            if(isCreate){
                boolean result=mkFile(file, overWrite);
                if(result){
                    return file;
                }else{
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 获取文件后缀名
     * @param fileName 原始文件名称
     * @param hasPoint 是否包含点
     * @return
     */
    public static String getFileNameSuffix(String fileName,boolean hasPoint) {
        int index=fileName.lastIndexOf(".");
        if(!hasPoint) {
            index+=1;
        }
        String suffix = fileName.substring(index);
        return suffix;
    }

}
