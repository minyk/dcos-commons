package com.mesosphere.sdk.redis.scheduler;

import com.mesosphere.sdk.testing.BaseServiceSpecTest;
import org.junit.Test;

public class ServiceSpecTest extends BaseServiceSpecTest {

    public ServiceSpecTest() {
        super(
                "EXECUTOR_URI", "",
                "LIBMESOS_URI", "",
                "PORT_API", "8080",
                "FRAMEWORK_NAME", "redis",

                "NODE_COUNT", "2",
                "NODE_CPUS", "0.1",
                "NODE_MEM", "512",
                "NODE_DISK", "5000",
                "NODE_DISK_TYPE", "ROOT",

                "ENABLE_VIRTUAL_NETWORK", "yes",
                "VIRTUAL_NETWORK_NAME", "dcos",

                "REDIS_CONF_TCP_BACKLOG", "511",
                "REDIS_CONF_TIMEOUT", "0",
                "REDIS_CONF_TCP_KEEPALIVE", "300",
                "REDIS_CONF_LOGLEVEL", "notice",
                "REDIS_CONF_DATABASES", "16",

                "REDIS_SENTINEL_COUNT", "3",
                "REDIS_SENTINEL_CONF_QUORUM", "2",
                "REDIS_SENTINEL_CONF_DOWN_AFTER", "60000",
                "REDIS_SENTINEL_CONF_FAILOVER_TIMEOUT", "180000",
                "REDIS_SENTINEL_CONF_PARALLEL_SYNCS", "1",
                "REDIS_IMAGE", "bootstrap.dcos.ajway.kr:5000/drake.min/redis:4.0.1-alpine",

                "CORVUS_COUNT", "1",
                "CORVUS_CPUS", "0.1",
                "CORVUS_MEMORY", "32",
                "CORVUS_IMAGE", "minyk/corvus:0.2.6-alpine",

                "SLEEP_DURATION", "1000");
    }

    @Test
    public void testYmlBase() throws Exception {
        testYaml("svc.yml");
    }
}
