/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mseprovider.web;

import com.aliyun.openservices.ons.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;


/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@RestController
public class BasicController {

    @Value(("${grayEnv:base}"))
    private String gray;

    @Value(("${mqAddress:rmq-cn-x0r3bu0yp03.cn-hangzhou.rmq.aliyuncs.com:8080}"))
    private String mqAddress;

    /**
    @Autowired
    private DynamicConfig dynamicConfig;*/

    // http://127.0.0.1:8080/hello?name=lisi
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {

        try {
            sendMsg44X();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return "Hello" + " "+ gray;
    }

    // http://127.0.0.1:8080/user
    /**
    @RequestMapping("/user")
    @ResponseBody
    public User user() {
        User user = new User();
        user.setName(dynamicConfig.getUserName());
        user.setAge(Integer.parseInt(dynamicConfig.getMoney()));
        return user;
    }
    */

    // http://127.0.0.1:8080/save_user?name=newName&age=11
    /**
    @RequestMapping("/save_user")
    @ResponseBody
    public String saveUser(User u) {
        return "user will save: name=" + u.getName() + ", age=" + u.getAge();
    }
    */

    // http://127.0.0.1:8080/html
    @RequestMapping("/html")
    public String html(){
        return "index.html";
    }

    //@ModelAttribute
    /**
    public void parseUser(@RequestParam(name = "name", defaultValue = "unknown user") String name
            , @RequestParam(name = "age", defaultValue = "12") Integer age, User user) {
        user.setName("zhangsan");
        user.setAge("18");

    }
     */


    /**
    private void sendMsg() throws ClientException {

        //String endpoints = "rmq-cn-x0r3bu0yp03.cn-hangzhou.rmq.aliyuncs.com:8080";

        String endpoints = mqAddress;

        //消息发送的目标Topic名称，需要提前在控制台创建，如果不创建直接使用会返回报错。
        String topic = "xl-test";
        ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfigurationBuilder builder = ClientConfiguration.newBuilder().setEndpoints(endpoints);

        builder.setCredentialProvider(new StaticSessionCredentialsProvider("kuUR1WQF4JkFT53A", "I5yi1WuGxe4Lzhtl"));
        ClientConfiguration configuration = builder.build();

        Producer producer = provider.newProducerBuilder()
                .setTopics(topic)
                .setClientConfiguration(configuration)
                .build();

        String content = "this is xl test message,current version is " + gray;

        System.out.println("current provider is "+gray+" message content: "+content);

        //普通消息发送。
        Message message = provider.newMessageBuilder()
                .setTopic(topic)
                //设置消息索引键，可根据关键字精确查找某条消息。
                .setKeys("xl")
                //消息体。
                .setBody(content.getBytes())
                .build();
        try {
            //发送消息，需要关注发送结果，并捕获失败等异常。
            SendReceipt sendReceipt = producer.send(message);
            System.out.println(sendReceipt.getMessageId());
        } catch (ClientException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/



    /**
     *  用4.X版本的客户端发送消息
     */
    private void sendMsg44X() {

        Properties properties = new Properties();
        // AccessKey ID，阿里云身份验证标识。获取方式，请参见本文前提条件中的获取AccessKey。
        properties.put(PropertyKeyConst.AccessKey, "kuUR1WQF4JkFT53A");
        // AccessKey Secret，阿里云身份验证密钥。获取方式，请参见本文前提条件中的获取AccessKey。
        properties.put(PropertyKeyConst.SecretKey, "I5yi1WuGxe4Lzhtl");
        //设置发送超时时间，单位毫秒。
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");
        // 设置TCP协议接入点，进入控制台的实例详情页面的TCP协议客户端接入点区域查看。
        properties.put(PropertyKeyConst.NAMESRV_ADDR,
                mqAddress);

        Producer producer = ONSFactory.createProducer(properties);
        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
        producer.start();

        String content = "this is xl test message,current version is " + gray;

        Message msg = new Message(
                // Message所属的Topic。
                "xl-test",
                // Message Tag，可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在消息队列RocketMQ版的服务器过滤。
                null,
                // Message Body，任何二进制形式的数据，消息队列RocketMQ版不做任何干预，需要Producer与Consumer协商好一致的序列化和反序列化方式。
                content.getBytes());

        // 异步发送消息，发送结果通过callback返回给客户端。
        producer.sendAsync(msg, new SendCallback() {
            @Override
            public void onSuccess(final SendResult sendResult) {
                // 消息发送成功。
                System.out.println("send message success. topic=" + sendResult.getTopic() + ", msgId=" + sendResult.getMessageId());
                System.out.println("current provider is "+gray+" message content: "+content);

            }

            @Override
            public void onException(OnExceptionContext context) {
                // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理。
                System.out.println("send message failed. topic=" + context.getTopic() + ", msgId=" + context.getMessageId());
            }
        });

        // 在callback返回之前即可取得msgId。
        System.out.println("send message async. topic=" + msg.getTopic() + ", msgId=" + msg.getMsgID());

    }

}
