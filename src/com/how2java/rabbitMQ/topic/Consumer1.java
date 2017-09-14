package com.how2java.rabbitMQ.topic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;

public class Consumer1 {

	private static final String EXCHANGE_NAME="topic_logs";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection =factory.newConnection();
		Channel channel=connection.createChannel();
		
		//声明一个匹配模式的交换器
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		String queueName=channel.queueDeclare().getQueue();
		
		//路由关键字
		String[] routingKeys=new String[]{"*.orange.*"};
		
		//绑定路由关键字
		for(String bindingKey:routingKeys)
		{
			channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
			System.out.println("----- Consumer1: exchange_name:"+EXCHANGE_NAME+", queue_name:"+queueName+", BindRoutingKey:" + bindingKey);
		}
		
		Consumer consumer =new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws UnsupportedEncodingException
			{
				String message=new String(body,"UTF-8");
				System.out.println("----- Consumer1: Received '" + envelope.getRoutingKey() + "' : '" + message + "'");
			}
		};
		channel.basicConsume(queueName, true,consumer);

	}

}
