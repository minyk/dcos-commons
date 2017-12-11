package com.mesosphere.sdk.ceph.scheduler;

import com.mesosphere.sdk.testing.ServiceTestRunner;
import org.junit.Test;

public class ServiceTest {

    @Test
    public void testSpec() throws Exception {

    new ServiceTestRunner()
        .setSchedulerEnv("CEPH_IMAGE", "ceph/daemon:tag-build-master-luminous-centos-7")
        .setSchedulerEnv("VIRTUAL_NETWORK_NAME", "dcos")
        .setPodEnv("monitor", "CEPH_MON_NODE_COUNT", "2")
        .setPodEnv("manager", "CEPH_MGR_NODE_COUNT", "1")
        .setPodEnv("metadata", "CEPH_MDS_NODE_COUNT", "1")
        .setPodEnv("gateway", "CEPH_RGW_NODE_COUNT", "1")
        .setPodEnv("restapi", "CEPH_RGW_NODE_COUNT", "1")

        .run();
    }
}
