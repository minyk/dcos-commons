package com.mesosphere.sdk.ceph.scheduler;

import com.mesosphere.sdk.api.types.EndpointProducer;
import com.mesosphere.sdk.ceph.api.FileResource;
import com.mesosphere.sdk.ceph.api.controller.CephConfigController;
import com.mesosphere.sdk.ceph.api.controller.CephKeyringController;
import com.mesosphere.sdk.ceph.api.util.CephEtcdClient;
import com.mesosphere.sdk.scheduler.*;
import com.mesosphere.sdk.specification.*;
import com.mesosphere.sdk.specification.yaml.RawServiceSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Template service.
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String CEPH_ETCD_ENDPOINTS = "CEPH_KV_IP";
    private static final String CEPH_ETCD_ENDPOINTS_PORT = "CEPH_KV_PORT";

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected one file argument, got: " + Arrays.toString(args));
        }

        LOGGER.info("Starting Ceph scheduler");
        // Read config from provided file, and assume any config etcds are in the same directory as the file:
        SchedulerRunner
                .fromSchedulerBuilder(createSchedulerBuilder(new File(args[0])))
                .run();
    }

    private static SchedulerBuilder createSchedulerBuilder(File yamlSpecFile) throws Exception {
        RawServiceSpec rawServiceSpec = RawServiceSpec.newBuilder(yamlSpecFile).build();
        SchedulerConfig schedulerConfig = SchedulerConfig.fromEnv();

        SchedulerBuilder schedulerBuilder = DefaultScheduler.newBuilder(
                DefaultServiceSpec
                        .newGenerator(rawServiceSpec, schedulerConfig, yamlSpecFile.getParentFile())
                        .build(),
                schedulerConfig)
                .setPlansFrom(rawServiceSpec);

        String cephEtcdEndpoints = "http://" + System.getenv(CEPH_ETCD_ENDPOINTS) +
                ":" + System.getenv(CEPH_ETCD_ENDPOINTS_PORT);

        return schedulerBuilder
                .setEndpointProducer("etcd", EndpointProducer.constant(cephEtcdEndpoints))
                .setCustomResources(getResources(cephEtcdEndpoints));
    }

    private static Collection<Object> getResources(String etcdEndpoints) {
        CephEtcdClient client = new CephEtcdClient(etcdEndpoints);
        final Collection<Object> apiResources = new ArrayList<>();
        apiResources.add(new FileResource());
        apiResources.add(new CephKeyringController(client));
        apiResources.add(new CephConfigController(client));
        return apiResources;
    }
}
