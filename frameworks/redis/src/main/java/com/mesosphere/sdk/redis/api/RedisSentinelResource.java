package com.mesosphere.sdk.redis.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mesosphere.sdk.api.ResponseUtils;
//import io.lettuce.core.KillArgs;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;
import org.apache.commons.lang3.EnumUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A read-only API for accessing redis sentinel commands.
 */
@Path("/v1/sentinel")
public class RedisSentinelResource {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String VIP_POST_FIX = ".l4lb.thisdcos.directory";
    private String sentinelHost;
    private RedisClient redisClient;
    private final Gson gson = new Gson();

    public RedisSentinelResource(String name) {
        this.sentinelHost = "sentinel." + name + VIP_POST_FIX;
        logger.debug("Monitor redis at " + sentinelHost);
        RedisURI redisUri = RedisURI.create("redis://" + sentinelHost + ":26379");
        redisClient = RedisClient.create(redisUri);
    }

    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfo() {
        StatefulRedisSentinelConnection<String, String> connection = redisClient.connectSentinel();
        if (connection.isOpen()) {
            String json = infoToJSON(connection.sync().info());
            connection.close();
            return ResponseUtils.jsonOkResponse(new JSONObject(json));
        }
        return Response.serverError().build();
    }

    @GET
    @Path("/info/{section}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfoSection(@PathParam("section") String section) {
        if (EnumUtils.isValidEnum(RedisSentinelInfoSection.class, section)) {
            StatefulRedisSentinelConnection<String, String> connection = redisClient.connectSentinel();
            if (connection.isOpen()) {
                String json = infoToJSON(connection.sync().info(section));
                connection.close();
                return ResponseUtils.jsonOkResponse(new JSONObject(json));
            }
        }
        return Response.serverError().build();
    }

    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public Response ping() {
        StatefulRedisSentinelConnection<String, String> connection = redisClient.connectSentinel();
        if (connection.isOpen()) {
            String result = connection.sync().ping();
            connection.close();
            return ResponseUtils.plainOkResponse(result);
        }
        return Response.serverError().build();
    }

    @GET
    @Path("/masters")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMasters() {
        StatefulRedisSentinelConnection<String, String> connection = redisClient.connectSentinel();
        if (connection.isOpen()) {
            String result = serversToJSON(connection.sync().masters());
            connection.close();
            return ResponseUtils.jsonOkResponse(new JSONObject(result));
        }
        return Response.serverError().build();
    }

    @GET
    @Path("/masters/{master}/slaves")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSlaves(@PathParam("master") String name) {
        StatefulRedisSentinelConnection<String, String> connection = redisClient.connectSentinel();
        if (connection.isOpen()) {
            String result = serversToJSON(connection.sync().slaves(name));
            connection.close();
            return ResponseUtils.jsonOkResponse(new JSONObject(result));
        }
        return Response.serverError().build();
    }

//    @GET
//    @Path("/masters/{pattern}/reset")
//    public Response resetMasters(@PathParam("pattern") String pattern) {
//        StatefulRedisSentinelConnection<String, String> connection = redisClient.connectSentinel();
//        if (connection.isOpen()) {
//            long result = connection.sync().reset(pattern);
//            connection.close();
//            return ResponseUtils.plainOkResponse(String.valueOf(result));
//        }
//        return Response.serverError().build();
//    }


    @GET
    @Path("/clients")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getClients() {
        StatefulRedisSentinelConnection<String, String> connection = redisClient.connectSentinel();
        if (connection.isOpen()) {
            String result = connection.sync().clientList();
            connection.close();
            return ResponseUtils.plainOkResponse(result);
        }
        return Response.serverError().build();
    }

//    @DELETE
//    @PathParam("/clients/{id}")
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response killClients(@PathParam("id") long id) {
//        StatefulRedisSentinelConnection<String, String> connection = redisClient.connectSentinel();
//        if (connection.isOpen()) {
//            KillArgs target = KillArgs.Builder.id(id);
//            long result = connection.sync().clientKill(target);
//            connection.close();
//            if (result != 0) {
//                return ResponseUtils.plainOkResponse(String.valueOf(result));
//            }
//        }
//        return Response.serverError().build();
//    }

    private String serversToJSON(List<Map<String, String>> list) {
        JsonArray servers = new JsonArray();
        for (Map<String, String> server : list) {
            JsonObject object = new JsonObject();
            Set<Map.Entry<String, String>> entries = server.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                object.addProperty(entry.getKey(), entry.getValue());
            }
            servers.add(object);
        }
        return gson.toJson(servers);
    }

    private String infoToJSON(String info) {
        String[] infos = info.split("\r\n");
        JsonObject object = new JsonObject();
        for (String s: infos) {
            if (s.contains(":")) {
                String[] kv = s.split(":");
                object.addProperty(kv[0], kv[1]);
            }
        }
        return gson.toJson(object);
    }
}
