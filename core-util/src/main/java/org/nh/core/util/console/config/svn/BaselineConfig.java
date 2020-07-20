package org.nh.core.util.console.config.svn;

import java.io.Serializable;

/**
 * Created by root on 3/12/18.
 */
public class BaselineConfig extends Config implements Serializable{
    /**
     * tag版本
     */
    private String tagVersion;

    /**
     * 所在的分支
     */
    private String branchVersion;

    /**
     * 所在分支的基线
     */
    private String baselineOfBranchVersion;

    /**
     * 最新的基线
     */
    private String newestBaselineVersion;

    /**
     * 推荐的对外版本
     */
    private String newStableVersion;


    public BaselineConfig() {
    }

    public BaselineConfig(String repoName, String moduleName, String moduleUrl, String tagVersion, String branchVersion,
                          String baselineOfBranchVersion, String newestBaselineVersion, String newStableVersion) {
        super(repoName, moduleName, moduleUrl);
        this.tagVersion = tagVersion;
        this.branchVersion = branchVersion;
        this.baselineOfBranchVersion = baselineOfBranchVersion;
        this.newestBaselineVersion = newestBaselineVersion;
        this.newStableVersion = newStableVersion;
    }

    public String getTagVersion() {
        return tagVersion;
    }

    public void setTagVersion(String tagVersion) {
        this.tagVersion = tagVersion;
    }

    public String getBranchVersion() {
        return branchVersion;
    }

    public void setBranchVersion(String branchVersion) {
        this.branchVersion = branchVersion;
    }

    public String getBaselineOfBranchVersion() {
        return baselineOfBranchVersion;
    }

    public void setBaselineOfBranchVersion(String baselineOfBranchVersion) {
        this.baselineOfBranchVersion = baselineOfBranchVersion;
    }

    public String getNewestBaselineVersion() {
        return newestBaselineVersion;
    }

    public void setNewestBaselineVersion(String newestBaselineVersion) {
        this.newestBaselineVersion = newestBaselineVersion;
    }

    public String getNewStableVersion() {
        return newStableVersion;
    }

    public void setNewStableVersion(String newStableVersion) {
        this.newStableVersion = newStableVersion;
    }
}
