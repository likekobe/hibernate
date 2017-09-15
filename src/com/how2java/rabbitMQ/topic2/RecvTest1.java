package com.how2java.rabbitMQ.topic2;

import java.io.IOException;

import com.rabbitmq.client.*;

public class RecvTest1 {
	private final static String HOST = "localhost";  
    private static final String EXCHANGE_NAME = "topic_Exc";  
    private static final String QUEUE = "temp_wwww";  
    private static final String ROUTKEY="*_topic_lee";  
     private static final boolean durable = true;  
     public static void main(String[] argv) throws Exception    
        {    
            // 创建连接和频道    
            ConnectionFactory factory = new ConnectionFactory();    
            factory.setHost(HOST);    
            Connection connection = factory.newConnection();    
            Channel channel = connection.createChannel();    
            // 声明转发器    
            channel.exchangeDeclare(EXCHANGE_NAME, "topic",durable);   
            //持久化  
            channel.queueDeclare(QUEUE, durable, false, false, null);  
            channel.basicQos(1);  
            //将消息队列绑定到Exchange  
            channel.queueBind(QUEUE, EXCHANGE_NAME, ROUTKEY);  
  
            System.out    
                    .println(" [*] Waiting for critical messages. To exit press CTRL+C");    
            //声明消费者  
            QueueingConsumer consumer = new QueueingConsumer(channel);    
            channel.basicConsume(QUEUE, true, consumer);    
        
            while (true)    
            {    
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();    
                String message = new String(delivery.getBody());    
                String routingKey = delivery.getEnvelope().getRoutingKey();    
        
                System.out.println(" [x] Received routingKey = " + routingKey    
                        + ",msg = " + message + ".");    
            }    
        }    
}
