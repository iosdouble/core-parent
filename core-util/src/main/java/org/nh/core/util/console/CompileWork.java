package org.nh.core.util.console;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by root on 4/24/17.
 */
public class CompileWork implements Serializable {

    private Long tagsId;
    private StringBuilder compileLog;
    private AtomicBoolean closeable;

    public CompileWork(Long svnOrGitTagsId) {
        this.tagsId = svnOrGitTagsId;
        this.compileLog = new StringBuilder();
        this.closeable = null;
    }

    public Long getTagsId() {
        return tagsId;
    }

    public void setTagsId(Long tagsId) {
        this.tagsId = tagsId;
    }

    public StringBuilder getCompileLog() {
        return compileLog;
    }

    public void setCompileLog(StringBuilder compileLog) {
        this.compileLog = compileLog;
    }

    public String appendLog(String buf) {
        return compileLog.append("\n").append(buf).toString();
    }

    public void setCloseable(AtomicBoolean atomicBoolean){
        this.closeable = atomicBoolean;
    }

    public void shutdown(){
        if(this.closeable != null)
            closeable.set(true);
    }

}
