package com.mesosphere.sdk.redis.scheduler;

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
            new RedisService(new File(args[0])).run();
        } else {
            LOGGER.error("Missing file argument");
            System.exit(1);
        }
    }
}
