package org.nh.core.util.net.httpclient.bean;

import java.io.Serializable;

/**
 * @Classname HttpResponse
 * @Description TODO
 * @Date 2020/7/20 10:50 AM
 * @Created by nihui
 */
public class HttpResponse implements Serializable {

    private int status;
    private String content;
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "HttpResponse [status=" + status + ", content=" + content + "]";
    }
}
