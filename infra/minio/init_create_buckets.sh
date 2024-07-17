set -e

echo "init create bukets"
echo MINIO_ROOT_USER: ${MINIO_ROOT_USER}
echo MINIO_ROOT_PASSWORD: ${MINIO_ROOT_PASSWORD}
echo MINIO_DEFULT_BUCKET: ${MINIO_INIT_CREATE_BUCKET}

mc config host add minio http://minio:9000 $MINIO_ROOT_USER $MINIO_ROOT_PASSWORD;
if [ -z "`mc ls minio | grep $MINIO_INIT_CREATE_BUCKET`" ] 
then
    echo create buckets.
    mc mb minio/$MINIO_INIT_CREATE_BUCKET;
else
    echo already created buckets.
fi
exit 0;