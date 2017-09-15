package com.how2java.rabbitMQ.topic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SerializationUtils;

import com.how2java.pojo.Hero;
import com.how2java.pojo.Product;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class topicConsumer2 {
private static final String EXCHANGE_NAME="topic_logs";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection =factory.newConnection();
		Channel channel=connection.createChannel();
		
		//声明一个匹配模式的交换器
		channel.exchangeDeclare(EXCHANGE_NAME, "topic",true);
		String queueName=channel.queueDeclare().getQueue();
		
		//路由关键字
		String[] routingKeys=new String[]{"*.hero.*"};
		
		//绑定路由关键字
		for(String bindingKey:routingKeys)
		{
			channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
			System.out.println("----- Consumer2绑定路由关键字！ exchange_name:"+EXCHANGE_NAME+"， queue_name:"+queueName+"， BindRoutingKey:" + bindingKey);
		}
		
		Consumer consumer =new DefaultConsumer(channel){
			
			int productCount=0;
			int heroCount=0;
			int otherCount=0;
			
			@Override
			public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException
			{
				
				Object obj=SerializationUtils.deserialize(body);
				if(obj instanceof Product)
				{
					productCount++;
					Product product=(Product)obj;
					System.out.println("----- Consumer2接受到消息！product:"+productCount+"个， 路由关键字：" + envelope.getRoutingKey() 
							+ "，类：" + product.getId()+","+product.getName() + ","+product.getPrice());
					
				}
				else if(obj instanceof Hero)
				{
					heroCount++;
					Hero hero=(Hero)obj;
					System.out.println("----- Consumer2接受到消息！ hero:"+heroCount+"个，路由关键字：" + envelope.getRoutingKey() 
							+ "，类：" + hero.getId()+","+hero.getName() + ","+hero.getLevel());
					
				}
				else {
					otherCount++;
					System.out.println("----- Consumer2接受到消息！other:"+otherCount+"个， 路由关键字：" + envelope.getRoutingKey() 
					+ "，类：" +obj);
					
				}
				channel.basicRecover(true);
			}
		};
		channel.basicConsume(queueName, true,consumer);

	}

}
