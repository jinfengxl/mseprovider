#!/bin/bash
#cn_edas@test.aliyunid.com 这个是镜像仓库的用户名
mvn package -Dmaven.test.skip=true
docker build -t mseprovider:v1 .