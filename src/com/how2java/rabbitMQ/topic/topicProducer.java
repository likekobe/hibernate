package com.how2java.rabbitMQ.topic;

import com.how2java.pojo.Product;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.lang3.SerializationUtils;
public class topicProducer {

	private static final String EXCHANGE_NAME="topic_logs";
	public static void main(String[] args) {
		Connection connection=null;
		Channel channel=null;
		try
		{
			ConnectionFactory factory=new ConnectionFactory();
			factory.setHost("localhost");
			
			connection= factory.newConnection();
			channel=connection.createChannel();
			
			//声明一个匹配模式的交换器
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
			
			//待发送的消息
			String[] routingKeys=new String[]{
						"quick.orange.rabbit", 
						"dsa.orange.elephant", 
						"quick.orange.fox", 
						"lazy.brown.fox", 
						"quick.brown.fox", 
						"quick.orange.male.rabbit", 
						"lazy.orange.male.rabbit"};
			
			Product product =null; 
			int id=0;
			float price=1.1F;
			String name="cpu";
			String message="";
			//发送消息
			for(String severity:routingKeys)
			{
				id++;
				price+=(float)id;
				name+=id;
				product=new Product(id, name, price);
				message=product.toString();
				
				//String message="----- Producer : From "+severity+" routingKey's message!";
				//channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
				channel.basicPublish(EXCHANGE_NAME, severity, null, SerializationUtils.serialize(product));
				
				System.out.println("----- TopicSend [x] Sent '" + severity + "':'" + message + "'");
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}finally {
			
			
			if(connection!=null)
			{
				try {
					
					connection.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}

	}

}
