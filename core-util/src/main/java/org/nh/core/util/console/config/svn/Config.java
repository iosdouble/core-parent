package org.nh.core.util.console.config.svn;


import org.nh.core.util.console.config.common.Cfg;

import java.io.Serializable;

/**
 * Created by root on 3/12/18.
 */
public class Config implements Cfg, Serializable{


    /**
     * 仓库名称
     */
    private String repoName;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 模块路径
     */
    private String moduleUrl;

    public Config() {
    }

    public Config(String repoName, String moduleName, String moduleUrl) {
        this.repoName = repoName;
        this.moduleName = moduleName;
        this.moduleUrl = moduleUrl;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleUrl() {
        return moduleUrl;
    }

    public void setModuleUrl(String moduleUrl) {
        this.moduleUrl = moduleUrl;
    }

    @Override
    public String getPath() {
        return repoName + moduleUrl;
    }
}
