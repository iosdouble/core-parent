package org.nh.core.util.net.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
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

    protected File download(HttpRequestBase httpRequestBase,String fileFullName,boolean overwrite){
        return null;
    }
}
