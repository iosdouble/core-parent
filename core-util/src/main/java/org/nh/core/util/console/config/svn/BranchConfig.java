package org.nh.core.util.console.config.svn;

import java.io.Serializable;

/**
 * Created by root on 3/12/18.
 */
public class BranchConfig extends Config implements Serializable{

    /**
     * 分支版本         1.0.1
     */
    private String branchVersion;

    /**
     * 分支所在基线版本  1.0.1.3
     */
    private String baselineVersion;

    public BranchConfig() {
    }

    public BranchConfig(String repoName, String moduleName, String moduleUrl, String branchVersion, String baselineVersion) {
        super(repoName, moduleName, moduleUrl);
        this.branchVersion = branchVersion;
        this.baselineVersion = baselineVersion;
    }

    public String getBranchVersion() {
        return branchVersion;
    }

    public void setBranchVersion(String branchVersion) {
        this.branchVersion = branchVersion;
    }

    public String getBaselineVersion() {
        return baselineVersion;
    }

    public void setBaselineVersion(String baselineVersion) {
        this.baselineVersion = baselineVersion;
    }
}
