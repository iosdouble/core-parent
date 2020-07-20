package org.nh.core.util.console;
import java.io.Serializable;

/**
 * Created by root on 7/27/17.
 */
public class RunWork implements Serializable{

    private String sys;
    private String ip;
    private String userName;
    private String exeDate;
    private String exeIp;
    private String command;
    private String rank;

    public RunWork() {
    }

    public RunWork(Sys sys, String ip, String userName, String exeDate, String exeIp) {
        this.sys = sys.name();
        this.ip = ip;
        this.userName = userName;
        this.exeDate = exeDate;
        this.exeIp = exeIp;
    }

    public RunWork(Sys sys, String ip, String userName, String exeDate, String exeIp, String command, String rank) {
        this.sys = sys.name();
        this.ip = ip;
        this.userName = userName;
        this.exeDate = exeDate;
        this.exeIp = exeIp;
        this.command = command;
        this.rank = rank;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExeDate() {
        return exeDate;
    }

    public void setExeDate(String exeDate) {
        this.exeDate = exeDate;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getExeIp() {
        return exeIp;
    }

    public void setExeIp(String exeIp) {
        this.exeIp = exeIp;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "<tr>" +
                "<td align='center'>" + sys + "</td>" +
                "<td align='center'>" + ip + "</td>" +
                "<td align='center'>" + exeIp + "</td>" +
                "<td align='center'>" + userName + "</td>" +
                "<td align='center'>" + exeDate + "</td>" +

                "<td align='left'>" + command + "</td>"
                + "</tr><tr><td>&nbsp;</td><td colspan='5'>"+rank+"</td></tr>";
    }
}
