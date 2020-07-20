package org.nh.core.util.console.config.git;

import java.io.Serializable;

/**
 * Created by root on 3/14/18.
 */
public class BranchGitConfig extends GitConfig implements Serializable {

    /**
     * 分支版本         1.0.1
     */
    private String branchVersion;

    /**
     * 分支所在基线版本  1.0.1.3
     */
    private String baselineVersion;

    public BranchGitConfig() {
    }

    public BranchGitConfig(String projectName, String gitPath, String branchVersion, String baselineVersion) {
        super(projectName, gitPath);
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

    @Override
    public String toString() {
        return "BranchGitConfig{" +
                "projectName='" + getProjectName() + '\'' +
                ", gitPath='" + getGitPath() + '\'' +
                "branchVersion='" + branchVersion + '\'' +
                ", baselineVersion='" + baselineVersion + '\'' +
                '}';
    }
}
