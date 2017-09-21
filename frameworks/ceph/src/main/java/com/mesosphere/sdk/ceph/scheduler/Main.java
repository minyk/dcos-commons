package com.mesosphere.sdk.ceph.scheduler;

import com.mesosphere.sdk.api.types.EndpointProducer;
import com.mesosphere.sdk.ceph.api.controller.CephConfigController;
import com.mesosphere.sdk.ceph.api.controller.CephKeyringController;
import com.mesosphere.sdk.ceph.api.util.CephEtcdClient;
import com.mesosphere.sdk.curator.CuratorUtils;
import com.mesosphere.sdk.scheduler.DefaultScheduler;
import com.mesosphere.sdk.scheduler.SchedulerFlags;
import com.mesosphere.sdk.scheduler.SchedulerUtils;
import com.mesosphere.sdk.specification.*;
import com.mesosphere.sdk.specification.yaml.RawServiceSpec;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Template service.
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String CEPH_ZK_URI_ENV = "CEPH_ZOOKEEPER_URI";
    private static final String CEPH_ETCD_ENDPOINTS = "CEPH_KV_IP";
    private static final String CEPH_ETCD_ENDPOINTS_PORT = "CEPH_KV_PORT";

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            new DefaultService(createSchedulerBuilder(new File(args[0]))).run();
        } else {
            LOGGER.error("Missing file argument");
            System.exit(1);
        }
    }

    private static DefaultScheduler.Builder createSchedulerBuilder(File pathToYamlSpecification) throws Exception {
        RawServiceSpec rawServiceSpec = RawServiceSpec.newBuilder(pathToYamlSpecification).build();
        SchedulerFlags schedulerFlags = SchedulerFlags.fromEnv();

        // Allow users to manually specify a ZK location for ceph itself. Otherwise default to our service ZK location:
        String cephZookeeperUri = System.getenv(CEPH_ZK_URI_ENV);
        if (StringUtils.isEmpty(cephZookeeperUri)) {
            // "master.mesos:2181" + "/dcos-service-path__to__my__ceph":
            cephZookeeperUri =
                    SchedulerUtils.getZkHost(rawServiceSpec, schedulerFlags)
                            + CuratorUtils.getServiceRootPath(rawServiceSpec.getName());
        }
        LOGGER.info("Running ceph with zookeeper path: {}", cephZookeeperUri);

        DefaultScheduler.Builder schedulerBuilder = DefaultScheduler.newBuilder(
                DefaultServiceSpec.newGenerator(rawServiceSpec, schedulerFlags, pathToYamlSpecification.getParentFile())
                        .setAllPodsEnv(CEPH_ZK_URI_ENV, cephZookeeperUri)
                        .build(), schedulerFlags)
                .setPlansFrom(rawServiceSpec);

        String cephEtcdEndpoints = "http://" + System.getenv(CEPH_ETCD_ENDPOINTS) +
                                   ":" + System.getenv(CEPH_ETCD_ENDPOINTS_PORT);
        LOGGER.info("Ceph config stores on {}", cephEtcdEndpoints);
        return schedulerBuilder
                .setEndpointProducer("zookeeper", EndpointProducer.constant(cephZookeeperUri))
                .setEndpointProducer("etcd", EndpointProducer.constant(cephEtcdEndpoints))
                .setCustomResources(getResources(cephEtcdEndpoints));
    }

    private static Collection<Object> getResources(String etcdEndpoints) {
        CephEtcdClient client = new CephEtcdClient(etcdEndpoints);
        final Collection<Object> apiResources = new ArrayList<>();
        apiResources.add(new CephKeyringController(client));
        apiResources.add(new CephConfigController(client));
        return apiResources;
    }
}
