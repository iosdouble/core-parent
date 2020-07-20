package org.nh.core.util.console.config.git;



import org.nh.core.util.console.config.common.Cfg;

import java.io.Serializable;

/**
 * Created by root on 3/14/18.
 */
public class GitConfig implements Cfg, Serializable{

    /**
     * Git模块名称
     */
    private String projectName;

    /**
     * Git Path
     * getGitPath(String.valueOf(branch.getGitModuleId())).substring(1)
     */
    private String gitPath;

    public GitConfig() {
    }

    public GitConfig(String projectName, String gitPath) {
        this.projectName = projectName;
        this.gitPath = gitPath;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGitPath() {
        return gitPath;
    }

    public void setGitPath(String gitPath) {
        this.gitPath = gitPath;
    }

    @Override
    public String toString() {
        return "GitConfig{" +
                "projectName='" + projectName + '\'' +
                ", gitPath='" + gitPath + '\'' +
                '}';
    }

    @Override
    public String getPath() {
        return gitPath;
    }
}
