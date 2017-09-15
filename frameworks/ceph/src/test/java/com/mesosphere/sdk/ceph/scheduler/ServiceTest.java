package com.mesosphere.sdk.ceph.scheduler;

import org.junit.Test;

import com.mesosphere.sdk.testing.ServiceTestBuilder;

public class ServiceTest {

    @Test
    public void testSpec() throws Exception {
        new ServiceTestBuilder()
                .setOptions(
                        "ceph_mon.count", "2",
                        "ceph_mon.cpus", "1",
                        "ceph_mon.mem", "32",

                        "ceph_osd.count", "2",
                        "ceph_osd.cpus", "1",
                        "ceph_osd.mem", "32",

                        "ceph_mds.count", "2",
                        "ceph_mds.cpus", "1",
                        "ceph_mds.mem", "32",

                        "ceph_mgr.count", "2",
                        "ceph_mgr.cpus", "1",
                        "ceph_mgr.mem", "32",

                        "ceph_rgw.count", "2",
                        "ceph_rgw.cpus", "1",
                        "ceph_rgw.mem", "32"
                )
                .render();
    }
}
