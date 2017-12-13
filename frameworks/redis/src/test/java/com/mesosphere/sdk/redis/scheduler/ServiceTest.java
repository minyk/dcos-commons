package com.mesosphere.sdk.redis.scheduler;

import com.mesosphere.sdk.testing.ServiceTestRunner;
import org.junit.Test;


public class ServiceTest {

    @Test
    public void testSpec() throws Exception {
//        Collection<SimulationTick> ticks = new ArrayList<>();
//
//        ticks.add(Send.register());
//
//        ticks.add(Expect.reconciledImplicitly());
//
//        // "node" task fails to launch on first attempt, without having entered RUNNING.
//        ticks.add(Send.offerBuilder("etcd").build());
//        ticks.add(Expect.launchedTasks("etcd-0-node"));
//        ticks.add(Send.taskStatus("etcd-0-node", Protos.TaskState.TASK_ERROR).build());
//
//        // Because the task has now been "pinned", a different offer which would fit the task is declined:
//        ticks.add(Send.offerBuilder("etcd").build());
//        ticks.add(Expect.declinedLastOffer());
//
//        // It accepts the offer with the correct resource ids:
//        ticks.add(Send.offerBuilder("etcd").setResourcesFromPod(0).build());
//        ticks.add(Expect.launchedTasks("etcd-0-node"));
//        ticks.add(Send.taskStatus("etcd-0-node", Protos.TaskState.TASK_RUNNING).build());
//
//        // With the pod now running, the scheduler now ignores the same resources if they're reoffered:
//        ticks.add(Send.offerBuilder("etcd").setResourcesFromPod(0).build());
//        ticks.add(Expect.declinedLastOffer());
//
//        ticks.add(Expect.allPlansComplete());

        new ServiceTestRunner()
                .setPodEnv("redis", "ETCD_IMAGE", "harbor.ajway.kr/middleware/redis:4.0.1-alpine")
                .setPodEnv("redis", "VIRTUAL_NETWORK_NAME", "dcos")
                .setPodEnv("redis", "CORVUS_IMAGE", "minyk/corvus:0.2.6-alpine")
                .setPodEnv("redis", "VIRTUAL_NETWORK_NAME", "dcos")
                .setPodEnv("redis", "ENABLE_VIRTUAL_NETWORK", "yes")
                .run();
    }

}
