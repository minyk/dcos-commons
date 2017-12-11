dcos-ceph
=========

DC/OS Ceph Scheduler based on DC/OS SDK.

# Feature

* Running Ceph Based on Ceph 12.x(Luminous).
 * Running N MON daemons
 * Running N MGR daemons
 * Running N OSDs
 * Running N MDS (optional)
 * Running N RGW (optional)
 * Running N RESTAPI (optional)
 
* Etcd is used to save Ceph configs.
 * Running an additional etcd daemon(1 node) for development purpose.
 * Pointing an etcd cluster at production environment.

* The scheduler has an api for download a ceph config and keyring.
 * Admin keyring: `/v1/ceph/keyring/ceph.client.admin.keyring`
 * Mon keyring: `/v1/ceph/keyring/ceph.mon.keyring`
 * Ceph config file: `/v1/ceph/config`

# Limitations

* Ceph MGR Dashboard cannot see due to DC/OS SDK does not support an expose method using marathon-lb.

# TODO
[] Add an authentication on the Etcd.
[] Add multiple virtual networks to support Ceph Public/Cluster Network feature.
