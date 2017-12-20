package com.mesosphere.sdk.redis.api;

//import com.mesosphere.sdk.api.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisMonitor;

//import javax.ws.rs.GET;
import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.util.HashMap;
//import java.util.Map;


/**
 * A read-only API for accessing file artifacts (e.g. config templates) for retrieval by executors.
 */
@Path("/v1/monitor")
public class RedisMonitorResource {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String VIP_POST_FIX = ".l4lb.thisdcos.directory";
    private String proxyHost;

    public RedisMonitorResource(String name) {
        this.proxyHost = "proxy." + name + VIP_POST_FIX;
        logger.debug("Monitor redis at " + proxyHost);
    }

}
