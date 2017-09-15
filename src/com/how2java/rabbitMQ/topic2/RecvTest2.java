package com.how2java.rabbitMQ.topic2;

import java.io.IOException;

import com.rabbitmq.client.*;


public class RecvTest2 {
	 private static final String EXCHANGE_NAME = "topic_logs";  
     
	    public static void main(String[] argv) throws Exception {  
	        ConnectionFactory factory = new ConnectionFactory();  
	        factory.setHost("localhost");  
	        Connection connection = factory.newConnection();  
	        Channel channel = connection.createChannel();  
//	      ����һ��ƥ��ģʽ�Ľ�����  
	        channel.exchangeDeclare(EXCHANGE_NAME, "topic");  
	        String queueName = channel.queueDeclare().getQueue();  
	        // ·�ɹؼ���  
	        String[] routingKeys = new String[]{"*.*.rabbit", "lazy.#"};  
//	      ��·�ɹؼ���  
	        for (String bindingKey : routingKeys) {  
	            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);  
	            System.out.println("ReceiveLogsTopic2 exchange:"+EXCHANGE_NAME+", queue:"+queueName+", BindRoutingKey:" + bindingKey);  
	        }  
	  
	        System.out.println("ReceiveLogsTopic2 [*] Waiting for messages. To exit press CTRL+C");  
	  
	        channel.basicQos(1);
	        Consumer consumer = new DefaultConsumer(channel) {  
	            @Override  
	            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {  
	               
	            	try{
	            		String message = new String(body, "UTF-8");  
	            		System.out.println("ReceiveLogsTopic2 [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");  
	           
		            } catch (Exception e) {
						// TODO: handle exception
					}
					finally{
						channel.basicAck(envelope.getDeliveryTag(), false);
					}
	            }  
	        };  
	        channel.basicConsume(queueName, false, consumer);  
	    }  
}
