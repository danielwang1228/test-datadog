# A simple demo for testing datadog
## Including these several projects:
1. test-datalog: a parent project
2. test-dd-common: Contains basic class definitions
3. test-dd-producer: Message requester, also used to receive responses
4. test-dd-consumer: consume messages and communicate with the database
## 
Operation:
1. Optional: Import the above 4 projects into IntelliJ IDEA
2. Upload all projects to Cloud OS (AWS, Azure or Aliyun .. etc)
3. Install test-datalog and test-dd-common using the `mvn`
```shell
cd test-datalog
mvn install

cd test-dd-common
mvn install
```
4. Run the dd-agent container
```shell
docker run -d --name dd-agent \
-e DD_API_KEY={Your API KEY} \
-e DD_SITE="datadoghq.com" \
-v /var/run/docker.sock:/var/run/docker.sock:ro \
-v /proc/:/host/proc/:ro \
-v /sys/fs/cgroup/:/host/sys/fs/cgroup:ro \
-v /var/lib/docker/containers:/var/lib/docker/containers:ro \
datadog/agent:latest
```
5. Build test-dd-consumer and test-dd-producer
```shell
cd test-dd-consumer
mvn package

cd test-dd-producer
mvn package
```
6. Create Docker image
```shell
cd test-dd-consumer
docker build -t test-dd-consumer .

cd test-dd-producer
docker build -t test-dd-producer .
```
7. Run the test container
```shell
docker run -d -p 9092:9092 --link dd-agent --name dd-consumer  test-dd-consumer
docker run -d -p 9091:9091 --link dd-agent --name dd-producer  test-dd-producer
```

Do testing
```shell
# add userinfo
curl -X POST http://localhost:9091/producer/sendToTestConsume -H "Content-Type:application/json" -d '{"id": 6, "name": "name006",     "age": 600 }'

# add userinfo and return DDResponseOne as Message 
curl -X POST http://localhost:9091/producer/sendToTestFunction -H "Content-Type:application/json" -d '{     "id": 5,     "name": "name005",     "age": 500, "flag": 1 }'

# add userinfo and return DDResponseTwo as Message
curl -X POST http://localhost:9091/producer/sendToTestFunction -H "Content-Type:application/json" -d '{     "id": 5,     "name": "name005",     "age": 500, "flag": 0 }'

# query all of userinfo from DB
curl localhost:9092/consumer/getAll
```
