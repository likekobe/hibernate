package com.how2java.rabbitMQ.topic2;

import java.util.UUID;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class SendTest {
	  private final static String HOST = "localhost";  
	    private static final String EXCHANGE_NAME = "topic_Exc";  
	    private static final String QUEUE = "temp_wwww";  
	    private static final String ROUTKEY="*_topic_mq";  
	    private static final boolean durable = true;  
	    public static void main(String[] argv) throws Exception    
	    {    
	        // 创建连接和频道    
	        ConnectionFactory factory = new ConnectionFactory();    
	        factory.setHost(HOST);    
	        Connection connection = factory.newConnection();    
	        Channel channel = connection.createChannel();    
	        //声明转发器  
	        channel.exchangeDeclare(EXCHANGE_NAME, "topic",durable);    
	        //持久化  
	        channel.queueDeclare(QUEUE, durable, false, false, null);  
	        channel.basicQos(1);  
	        //将消息队列绑定到Exchange  
	        channel.queueBind(QUEUE, EXCHANGE_NAME, ROUTKEY);  
	          
	        String msg = UUID.randomUUID().toString();  
	        
	        channel.basicPublish(EXCHANGE_NAME, ROUTKEY, MessageProperties.PERSISTENT_TEXT_PLAIN, msg    
	                .getBytes());   
	        System.out.println(msg);
	        
	        channel.close();    
	        connection.close();    
	    }  
}
