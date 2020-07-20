package org.nh.core.util.console;

import java.io.Serializable;

/**
 * Created by root on 8/15/17.
 */
public class RuleResult implements Serializable{

    private String ip;
    private boolean isSame = false;
    private String rank;

    public RuleResult(String ip, boolean isSame, String rank) {
        this.ip = ip;
        this.isSame = isSame;
        this.rank = rank;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isSame() {
        return isSame;
    }

    public void setSame(boolean same) {
        isSame = same;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
