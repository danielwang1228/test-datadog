```shell
docker run -d --name=solace -p 8080:8080 -p 55555:55555 -p 8008:8008 -p 1883:1883 -p 5672:5672 -p 9443:9443 -p 9000:9000 -e username_admin_globalaccesslevel=admin -e username_admin_password=admin --restart unless-stopped solace/solace-pubsub-standard
```