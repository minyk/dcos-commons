package com.mesosphere.sdk.ceph.api.controller;

import com.mesosphere.sdk.api.ResponseUtils;
import com.mesosphere.sdk.ceph.api.util.CephEtcdClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * REST API for Ceph Keyring.
 */

@Path("/v1/ceph/keyring")
public class CephKeyringController {
    private final Logger log = LoggerFactory.getLogger(CephKeyringController.class);
    private CephEtcdClient cephetcdclient;

    public CephKeyringController(CephEtcdClient client) {
        this.cephetcdclient = client;
    }

    @GET
    @Path("/ceph.client.admin.keyring")
    @Produces("plain/text")
    public Response getAdminkey() {
        try {
            return ResponseUtils.plainOkResponse(cephetcdclient.getAdminKey());
        } catch (Exception e) {
            log.error("Fail to get admin key from Etcd: {}", e.getMessage());
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/ceph.mon.keyring")
    public Response getMonitorKey() {
      try {
          return ResponseUtils.plainOkResponse(cephetcdclient.getMonKey());
      } catch (Exception e) {
          log.error("Fail to get monitor key from Etcd: {}", e.getMessage());
          return Response.serverError().build();
      }
    }
}
