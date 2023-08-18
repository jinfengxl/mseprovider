# 基于哪个镜像
FROM java:8
# 将本地文件夹挂载到当前容器
#VOLUME /root/app/demo/eureka


#定义时区参数
ENV TZ=Asia/Shanghai
#设置时区
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo '$TZ' > /etc/timezone

ENV MYPATH /home/admin/mseprovider
WORKDIR $MYPATH

# 复制文件到容器
COPY ./target/mseprovider-1.0-SNAPSHOT.jar $MYPATH/
# 声明需要暴露的端口
EXPOSE 8080
# 配置容器启动后执行的命令
ENTRYPOINT ["java","-jar","mseprovider-1.0-SNAPSHOT.jar"]