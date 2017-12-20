package com.mesosphere.sdk.redis.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

/**
 * A read-only API for accessing file artifacts (e.g. config templates) for retrieval by executors.
 */
@Path("/v1/files")
public class FileResource {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String MESOS_SANDBOX = System.getenv("MESOS_SANDBOX");

    @GET
    @Path("/{file}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile(@PathParam("file") String filename) {
        logger.info("Attempting to fetch file '{}'", filename);
        File file;
        switch(filename) {
            case "bootstrap.zip":
                file = new File(MESOS_SANDBOX + "/bootstrap.zip");
                return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                        .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"") //optional
                        .build();
            case "executor.zip":
                file = new File(MESOS_SANDBOX + "/executor.zip");
                return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                        .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"") //optional
                        .build();
            case "jre-8u144-linux-x64.tar.gz":
                file = new File(MESOS_SANDBOX + "/jre-8u144-linux-x64.tar.gz");
                return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                        .header("Content-Disposition", "attachement; filename=\"" + file.getName() + "\"")
                        .build();
            case "libmesos-bundle-1.10-1.4-63e0814.tar.gz":
                file = new File(MESOS_SANDBOX + "/libmesos-bundle-1.10-1.4-63e0814.tar.gz");
                return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                        .header("Content-Disposition", "attachement; filename=\"" + file.getName() + "\"")
                        .build();
            case "stadis-linux-amd64.zip":
                file = new File(MESOS_SANDBOX + "/stadis-linux-amd64.zip");
                return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                        .header("Content-Disposition", "attachement; filename=\"" + file.getName() + "\"")
                        .build();
            default:
                logger.warn("No such file: {}", filename);
                return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
