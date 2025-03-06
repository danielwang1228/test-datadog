# A simple demo for testing datadog
## Including these several projects:
1. test-datalog: a parent project
2. test-dd-common: Contains basic class definitions
3. test-dd-producer: Message requester, also used to receive responses
4. test-dd-consumer: consume messages and communicate with the database
## 
Operation:
1. Import the above 4 projects into IntelliJ IDEA
2. Install test datalog and test dd common using the `mvn`
3. Run the dd-agent container
```shell
docker run -d --name dd-agent \
-e DD_API_KEY=xxxxxxxxxxxxxxxxxxx \
-e DD_SITE="us5.datadoghq.com" \
-v /var/run/docker.sock:/ var/run/docker.sock:ro \
-v /proc/:/ host/proc/:ro \
-v /sys/fs/cgroup/:/ host/sys/fs/cgroup:ro \
-v /var/lib/docker/containers:/ var/lib/docker/containers:ro \
datadog/agent:latest
```
4. Create Docker image
```shell
cd test-dd-consumer
docker build -t test-dd-consumer .
cd test-dd-producer
docker build -t test-dd-producer .
```
4. Run the test container
```shell
docker run -d -p 9092:9092 --link dd-agent --name dd-consumer  test-dd-consumer
docker run -d -p 9091:9091 --link dd-agent --name dd-producer  test-dd-producer
```

do testing
```shell
curl -X POST http://localhost:9091/producer/sendToTestConsume -H "Content-Type:application/json" -d '{"id": 6, "name": "name006",     "age": 600 }'
curl -X POST http://localhost:9091/producer/sendToTestFunction -H "Content-Type:application/json" -d '{     "id": 5,     "name": "name005",     "age": 500 }'
curl localhost:9092/consumer/getAll
```
