#!/bin/bash

OSD_UUID_FILE="${MESOS_SANDBOX}/osd_uuid"
OSD_ID_FILE="${MESOS_SANDBOX}/osd_id"

# See http://docs.ceph.com/docs/jewel/install/manual-deployment/#adding-osds

source /variables_entrypoint.sh
source /common_functions.sh
source /config.kv.etcd.sh

# Get config from etcd
get_config
check_config

# Get client.admin key
get_admin_key

# 2.
/usr/bin/uuidgen > ${OSD_UUID_FILE}
OSD_UUID=`cat ${OSD_UUID_FILE}`

# 3.
ceph osd create ${OSD_UUID} > ${OSD_ID_FILE}
OSD_ID=`cat ${OSD_ID_FILE}`

# 4.
chown -R ceph:ceph ${MESOS_SANDBOX}/cephdata && ln -sf ${MESOS_SANDBOX}/cephdata /var/lib/ceph/osd/ceph-${OSD_ID} ;

# 6.
ceph-osd -i ${OSD_ID} --mkfs --mkkey --osd-uuid ${OSD_UUID}

# 7.
ceph auth add osd.${OSD_ID} osd 'allow *' mon 'allow profile osd' -i /var/lib/ceph/osd/${CLUSTER}-${OSD_ID}/keyring

# 8.
ceph --cluster ${CLUSTER} osd crush add-bucket ${HOSTNAME} host

# 9.
ceph osd crush move ${HOSTNAME} root=default

# 10.
ceph osd crush add osd.${OSD_ID} 1.0 host=${HOSTNAME}
