package com.mesosphere.sdk.redis.scheduler;

import com.mesosphere.sdk.scheduler.DefaultScheduler;
import com.mesosphere.sdk.scheduler.SchedulerFlags;
import com.mesosphere.sdk.specification.*;
import com.mesosphere.sdk.specification.yaml.RawServiceSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Redis service.
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            RawServiceSpec rawServiceSpec = RawServiceSpec.newBuilder(new File(args[0])).build();
            SchedulerFlags schedulerFlags = SchedulerFlags.fromEnv();
            DefaultScheduler.Builder schedulerBuilder = DefaultScheduler.newBuilder(
                    DefaultServiceSpec.newGenerator(rawServiceSpec, schedulerFlags)
                            .build(),
                    schedulerFlags)
                    .setPlansFrom(rawServiceSpec);
            new DefaultService(schedulerBuilder).run();
        } else {
            LOGGER.error("Missing file argument");
            System.exit(1);
        }
    }
}
