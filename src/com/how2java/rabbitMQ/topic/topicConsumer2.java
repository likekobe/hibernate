package com.how2java.rabbitMQ.topic;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class topicConsumer2 {
	private final static String EXCHANGE_NAME="topic_logs";
	
	public static void main(String[] args) {
			
		
		try {
			ConnectionFactory factory=new ConnectionFactory();
			Connection connection=factory.newConnection();
			Channel channel=connection.createChannel();
			
			//����һ��ƥ��ģʽ�Ľ�����
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
			String queueName=channel.queueDeclare().getQueue();
			
			//·�ɹؼ���
			String[] routingKeys =new String[]{"*.*.rabbit", "lazy.#"};		
			
			//��·�ɹؼ���
			for(String bindingKey:routingKeys)
			{
				channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
				System.out.println("ReceiveLogsTopic2 exchange:"+EXCHANGE_NAME+", queue:"+queueName+", BindRoutingKey:" + bindingKey);
			}
			
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					System.out.println("----- Consumer2 Received '" + envelope.getRoutingKey() + "' : '" + message + "'");
				}
			};
			channel.basicConsume(queueName, true, consumer);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
