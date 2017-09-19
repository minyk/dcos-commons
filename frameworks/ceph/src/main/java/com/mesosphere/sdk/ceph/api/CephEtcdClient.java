package com.mesosphere.sdk.ceph.api;

import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.promises.EtcdResponsePromise;
import mousio.etcd4j.responses.EtcdKeysResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

/**
 * Etcd Client for Ceph configuration.
 */
public class CephEtcdClient {
    private static final Logger log = LoggerFactory.getLogger(CephEtcdClient.class);

    private final EtcdClient client;
    private String endpoint;
    private static final String CLUSTER_PATH = "/ceph-config/" + System.getenv("FRAMEWORK_NAME");
    private static final String ADMINKEY = CLUSTER_PATH + "/adminKeyring";
    private static final String MONITORKEY = CLUSTER_PATH + "/monKeyring";

    private static final String CONF_GLOBAL = CLUSTER_PATH + "/global";
    private static final int LEN_CONF_GLOBAL = CONF_GLOBAL.length() + 1;
    private static final String CONF_MON = CLUSTER_PATH + "/mon";
    private static final int LEN_CONF_MON = CONF_MON.length() + 1;
    private static final String CONF_MON_HOST = CLUSTER_PATH + "/mon_host";
    private static final int LEN_CONF_MON_HOST = CONF_MON_HOST.length() + 1;
    private static final String CONF_OSD = CLUSTER_PATH + "/osd";
    private static final int LEN_CONF_OSD = CONF_OSD.length() + 1;
    private static final String CONF_CLIENT = CLUSTER_PATH + "/client";
    private static final int LEN_CONF_CLIENT = CONF_CLIENT.length() + 1;
    private static final String CONF_MDS = CLUSTER_PATH + "/mds";
    private static final int LEN_CONF_MDS = CONF_MDS.length() + 1;


    public CephEtcdClient(String endpoints) {
        // create client
        this.endpoint = endpoints;
        client = new EtcdClient(URI.create(endpoint));

    }

    public String getAdminKey() throws Exception {
        EtcdResponsePromise<EtcdKeysResponse> promise = client.get(ADMINKEY).send();
        return promise.get().getNode().getValue();
    }

    public String getMonKey() throws Exception {
        EtcdResponsePromise<EtcdKeysResponse> promise = client.get(MONITORKEY).send();
        return promise.get().getNode().getValue();
    }
    public String getGlobalConf() throws Exception {
        EtcdResponsePromise<EtcdKeysResponse> promise = client.get(CONF_GLOBAL).recursive().send();
        List<EtcdKeysResponse.EtcdNode> list = promise.get().getNode().getNodes();
        StringBuffer buf = new StringBuffer();
        buf.append("[global]\n");
        for (EtcdKeysResponse.EtcdNode node : list) {
            buf.append(node.getKey().substring(LEN_CONF_GLOBAL));
            buf.append(" = ");
            buf.append(node.getValue() + "\n");
        }
        buf.append("\n\n");
        return buf.toString();
    }

    public String getMonitorConf() throws Exception {
        EtcdResponsePromise<EtcdKeysResponse> promise = client.get(CONF_MON).recursive().send();
        List<EtcdKeysResponse.EtcdNode> list = promise.get().getNode().getNodes();
        StringBuffer buf = new StringBuffer();
        buf.append("[mon]\n");
        for (EtcdKeysResponse.EtcdNode node : list) {
            buf.append(node.getKey().substring(LEN_CONF_MON));
            buf.append(" = ");
            buf.append(node.getValue() + "\n");
        }
        buf.append("\n\n");

        EtcdResponsePromise<EtcdKeysResponse> promiseMonHost = client.get(CONF_MON_HOST).recursive().send();
        List<EtcdKeysResponse.EtcdNode> listMonHost = promiseMonHost.get().getNode().getNodes();
        for (EtcdKeysResponse.EtcdNode node : listMonHost) {
            String host = node.getKey().substring(LEN_CONF_MON_HOST);
            buf.append("[mon." + host + "]\n");
            buf.append("host = " + host + "\n");
            buf.append("mon_add = " + node.getValue() + "\n\n");
        }
        return buf.toString();
    }

    public String getOSDConf() throws Exception {
        EtcdResponsePromise<EtcdKeysResponse> promise = client.get(CONF_OSD).recursive().send();
        List<EtcdKeysResponse.EtcdNode> list = promise.get().getNode().getNodes();
        StringBuffer buf = new StringBuffer();
        buf.append("[osd]\n");
        for (EtcdKeysResponse.EtcdNode node : list) {
            buf.append(node.getKey().substring(LEN_CONF_OSD));
            buf.append(" = ");
            buf.append(node.getValue() + "\n");
        }
        buf.append("\n\n");
        return buf.toString();
    }

    public String getMDSConf() throws Exception {
        EtcdResponsePromise<EtcdKeysResponse> promise = client.get(CONF_MDS).recursive().send();
        List<EtcdKeysResponse.EtcdNode> list = promise.get().getNode().getNodes();
        StringBuffer buf = new StringBuffer();
        buf.append("[mds]\n");
        for (EtcdKeysResponse.EtcdNode node : list) {
            buf.append(node.getKey().substring(LEN_CONF_MDS));
            buf.append(" = ");
            buf.append(node.getValue() + "\n");
        }
        buf.append("\n\n");
        return buf.toString();
    }

    public String getClientConf() throws Exception {
        EtcdResponsePromise<EtcdKeysResponse> promise = client.get(CONF_CLIENT).recursive().send();
        List<EtcdKeysResponse.EtcdNode> list = promise.get().getNode().getNodes();
        StringBuffer buf = new StringBuffer();
        buf.append("[client]\n");
        for (EtcdKeysResponse.EtcdNode node : list) {
            buf.append(node.getKey().substring(LEN_CONF_CLIENT));
            buf.append(" = ");
            buf.append(node.getValue() + "\n");
        }
        buf.append("\n\n");
        return buf.toString();
    }
}
