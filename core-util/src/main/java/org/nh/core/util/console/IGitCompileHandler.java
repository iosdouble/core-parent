package org.nh.core.util.console;

/**
 * Created by root on 3/6/18.
 */
public interface IGitCompileHandler {

    void onCompileComplete(Long gitTagsId, String projectName, String gitPath, String branchVersion, String tagVersion,
                           ConsoleStatus status);


}
