Redis Scheduler for DC/OS
------------------------

# Redis scheduler for DC/OS

![Image of schduler](frameworks/redis/docs/redis-scheduler.png)

## Features

* Running 1 master node
* Running N slave nodes
* Running N sentinel nodes(Optional)
* Export redis stats to dcos-metrics(Optional)
* redis master client endpoint: `client.<redis-frameworkname>.l4lb.thisdcos.directory:6379`
 * If service is running under the sub group, endpoint is: `client.<subgroup><servicename>.l4lb.thisdcos.directory:6379`

# How to use 
## Build
```bash
$ cd frameworks/redis/
$ ./build.sh
```

## Use Universe

* Serve `frameworks/redis/build/distributions/*.zip` files in httpd.
* Serve `sdk/bootstrap/bootstrap` file in httpd.
* Copy `frameworks/redis/universe/*` to `universe/repo/B/beta-etcd/0/*`
* Replace file assets in `resource.json` with:
```json
    {
      "jre-tar-gz": "https://downloads.mesosphere.com/java/jre-8u131-linux-x64-jce-unlimited.tar.gz",
      "libmesos-bundle-tar-gz": "https://downloads.mesosphere.io/libmesos-bundle/libmesos-bundle-1.10-1.4-63e0814.tar.gz"
    }
``` 

* Replace `bootstrap` asset in `resource.json` with proper location.
* Replcae `scheduler-zip` asset in `resource.json` with proper location.
* Replcae `executor-zip` asset in `resource.json` with proper location.

* Build local universe.

## Use pre-built docker image

* Use `minyk/dcos-redis:v0.1` from Docker hub, and marathon.json(frameworks/redis/docker/marathon.json).
 * See also marathon-subgroup.json

# Limitations
* Only in `replication` mode. `redis-cluster` is not supported yet.
* Some system parameters(vm.overcommit_memory, somaxconn, THP) are not controllable at the scheduler. 
* Scheduler does not provide cli extension.
* Scheduler does not provide a way to configure redis servers.