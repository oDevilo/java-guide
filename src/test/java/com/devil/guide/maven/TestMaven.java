package com.devil.guide.maven;

import org.junit.Test;

/**
 * @author Devil
 * @since 2021/11/29
 */
public class TestMaven {

    @Test
    public void testVersionChecker() throws Exception {
        DependencyChecker checker = new DependencyChecker("/Users/devil/work/workspace/netease/xprofiler-gateway", "/data/version.json", false);
        checker.check();
    }
}
