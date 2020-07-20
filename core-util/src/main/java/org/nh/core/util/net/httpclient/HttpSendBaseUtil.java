package org.nh.core.util.net.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.nh.core.util.file.FileUtil;
import org.nh.core.util.file.IOUtil;
import org.nh.core.util.string.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @Classname HttpSendBaseUtil
 * @Description TODO 发送 http/https 请求的基础工具类
 * @Date 2020/6/24 1:32 PM
 * @Created by nihui
 */
public class HttpSendBaseUtil {

    private CloseableHttpClient closeableHttpClient;

    public HttpSendBaseUtil(CloseableHttpClient closeableHttpClient){
        this.closeableHttpClient = closeableHttpClient;
    }

    /**
     * 创建一个用于上传文件的HttpPost 操作
     * @param httpUrl 上传文件的URL
     * @param file 需要上传的文件
     * @param headers 请求头参数
     * @param textParams 附加元素参数
     * @return
     */
    protected HttpPost createUploadHttpPost(String httpUrl, File file, Map<String,String> headers,Map<String,String> textParams ){
        HttpPost httpPost = new HttpPost(httpUrl);

        createHeader(httpPost,headers);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        builder.addBinaryBody("file",file);

        if (textParams!=null){
            for (String key:textParams.keySet()) {
                builder.addTextBody(key,textParams.get(key));
            }
        }
        HttpEntity httpEntity = builder.build();
        httpPost.setEntity(httpEntity);
        return httpPost;
    }




    /**
     * 下载文件资源
     * @param httpRequestBase HttpGet HttpPost
     * @param fileFullName 保存文件的全路径
     * @param overwrite 如果已经存在，是否删除重新生成
     * @return 返回file成功，返回null失败
     */
    protected File download(HttpRequestBase httpRequestBase,String fileFullName,boolean overwrite){

        int status = 0;

        File file = null;

        try{
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpRequestBase);

            HttpEntity httpEntity = closeableHttpResponse.getEntity();

            status = closeableHttpResponse.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK){
                InputStream inputStream = httpEntity.getContent();
                if (inputStream != null){
                    try{
                        file = FileUtil.getFileHisOrCreate(fileFullName,true,overwrite);
                        boolean result = IOUtil.inputStreamToFile(inputStream,file,true);
                        if (result){
                            return file;
                        }
                        if (file!=null){
                            file.deleteOnExit();
                        }
                        return null;
                    }catch (IOException e){
                        e.printStackTrace();
                        if (file!=null){
                            file.deleteOnExit();
                        }
                    }
                }
            }


        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建一个StringEntity 用于POST、GET、PUT等requestBase 发送的Body里的字符串
     * @param contentStr 需要发送的字符串内容
     * @param contentType 传递null 默认为application/x-www-form-urlencoded 否则为用户自己的
     * @return
     */
    protected StringEntity createBodyStringEntity(String contentStr,String contentType){
        if (StringUtils.isBlank(contentStr)){
            contentStr = "";
        }
        StringEntity stringEntity = new StringEntity(contentStr,"UTF-8");
        if (contentType==null){
            stringEntity.setContentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
        }else {
            stringEntity.setContentType(contentType);
        }
        return stringEntity;
    }

    /**
     * 为Http请求设置对应的请求头参数
     * @param requestBase
     * @param headers
     */
    private void createHeader(HttpPost requestBase, Map<String,String> headers) {
        if (headers != null) {
            for (String key : headers.keySet()) {
                requestBase.addHeader(key, headers.get(key));
            }
        }
    }
}
