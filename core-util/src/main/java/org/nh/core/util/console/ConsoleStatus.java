package org.nh.core.util.console;

import java.io.Serializable;

/**
 * Created by root on 4/21/17.
 */
public class ConsoleStatus implements Serializable {

    private Sys sys;
    private boolean success = false;
    private int status = -1;
    private String output = "";

    public ConsoleStatus(){
        this.sys = Sys.SVN;
    }

    public ConsoleStatus(Sys sys){
        this.sys = sys;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "\nConsoleStatus{" +
                "sys=" + sys.name() +
                ", success=" + success +
                ", status=" + status +
                ", output='\n" + output + "\n" + '\'' +
                '}';
    }
}
