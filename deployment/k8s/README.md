# JOB K8s DEPLOYMENT


## Steps:

### Build image

cp ../../build/lib/job-pulsar-flink-fat-1.0.0.jar ./docker
docker/build.sh

### Deploy on k8s

kubectl create -f jobmanager-service.yaml
kubectl create -f jobmanager-deployment.yaml
kubectl create -f taskmanager-deployment.yaml

### Run job

flink run -c com.jene.cognitive.PulsarConsumerFlink /home/job-pulsar-flink-fat-1.0.0.jar
