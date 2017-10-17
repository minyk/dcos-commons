package com.mesosphere.sdk.redis.scheduler;

import com.mesosphere.sdk.redis.api.FileResource;
import com.mesosphere.sdk.redis.api.Instance;
import com.mesosphere.sdk.scheduler.DefaultScheduler;
import com.mesosphere.sdk.scheduler.SchedulerFlags;
import com.mesosphere.sdk.specification.DefaultService;
import com.mesosphere.sdk.specification.DefaultServiceSpec;
import com.mesosphere.sdk.specification.yaml.RawServiceSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Redis Service
 * TODO (drakemin): currently not in use.
 */
public class RedisService extends DefaultService {

    public RedisService(File pathToYamlSpecification) throws  Exception {
        super(createSchedulerBuilder(pathToYamlSpecification));
    }

    private static DefaultScheduler.Builder createSchedulerBuilder(File pathToYamlSpecification) throws Exception {
        RawServiceSpec rawServiceSpec = RawServiceSpec.newBuilder(pathToYamlSpecification).build();
        SchedulerFlags schedulerFlags = SchedulerFlags.fromEnv();

        DefaultScheduler.Builder schedulerBuilder = DefaultScheduler.newBuilder(
                DefaultServiceSpec.newGenerator(rawServiceSpec, schedulerFlags)
                        .build(), schedulerFlags)
                .setPlansFrom(rawServiceSpec);

        return schedulerBuilder.setCustomResources(getResources());
    }

    private static Collection<Object> getResources() {
        final Collection<Object> apiResources = new ArrayList<>();
        apiResources.add(new Instance());
        apiResources.add(new FileResource());
        return apiResources;
    }
}
