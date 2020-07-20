package org.nh.core.util.console;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by root on 7/28/17.
 */
public class ProjectWork implements Serializable{

    private AtomicLong createTag = new AtomicLong(0L);
    private AtomicLong createBranch = new AtomicLong(0L);
    private AtomicLong build = new AtomicLong(0L);
    private AtomicLong release = new AtomicLong(0L);

    public Long getCreateTag() {
        return createTag.get();
    }

    public Long getCreateBranch() {
        return createBranch.get();
    }

    public Long getBuild() {
        return build.get();
    }

    public Long getRelease() {
        return release.get();
    }

    public Long incrementAndGetCreateTag() {
        return createTag.incrementAndGet();
    }

    public Long incrementAndGetCreateBranch() {
        return createBranch.incrementAndGet();
    }

    public Long incrementAndGetBuild() {
        return build.incrementAndGet();
    }

    public Long incrementAndGetRelease() {
        return release.incrementAndGet();
    }
}
