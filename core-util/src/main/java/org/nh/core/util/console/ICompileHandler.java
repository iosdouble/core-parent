package org.nh.core.util.console;

/**
 * Created by root on 4/21/17.
 */
public interface ICompileHandler {

    void onCompileComplete(Long tagsId, String repoName, String moduleName, String moduleUrl, String branchVersion,
                           String tagVersion, ConsoleStatus status);

}
