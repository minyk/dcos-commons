package com.mesosphere.sdk.redis.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mesosphere.sdk.api.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * API for redis instance status
 * TODO (drakemin): currently not in use.
 */

@Path("/v0/instance")
public class Instance {
    private static final Logger log = LoggerFactory.getLogger(Instance.class);

    @GET
    public Response list() {
        try {
            return ResponseUtils.plainOkResponse("ok");
        } catch (Exception ex) {
            log.error("Failed to fetch list with exception: " + ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/master")
    public Response master() {
        try {
            return ResponseUtils.plainOkResponse("ok");
        } catch (Exception ex) {
            log.error("Failed to fetch list with exception: " + ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/slaves")
    public Response slaves() {
        try {
            return ResponseUtils.plainOkResponse("ok");
        } catch (Exception ex) {
            log.error("Failed to fetch list with exception: " + ex);
            return Response.serverError().build();
        }
    }
}
