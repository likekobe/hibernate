package com.how2java.rabbitMQ.topic;

import com.how2java.pojo.Hero;
import com.how2java.pojo.Product;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.engine.StatefulPersistenceContext;
public class topicProducer {

	private static final String EXCHANGE_NAME="topic_logs";
	
	private static final String HOST="localhost";
	
	private static final String TYPE="topic";
	
	private static final int SEND_COUNT=3000;
	
	private static final String PRODUCT_CLASS="product";
	private static final String HERO_CLASS="hero";
	
	private Connection connection=null;
	private Channel channel=null;
	
	public static void main(String[] args) throws Exception {
		topicProducer producer=new topicProducer();
		producer.send();
	}
	
	
	public void send() throws Exception
	{
		try {
			this.connect();
			//声明一个匹配模式的交换器
			channel.exchangeDeclare(EXCHANGE_NAME, TYPE,true);
			
			//路由关键字
			String[] routingKeys=new String[]{
						"like.product.jd", 
						"like.product.tmall", 
						"like.hero.lol", 
						"like.hero.2k", 
						"cmq.product.tmall", 
						"cmq.product.jd", 
						"cmq.hero.lol", 
						"lazy.orange.male.rabbit"};
			
			Random random=new Random();
			String routingKey="";
			Product product=null;
			Hero hero =null;
			byte[] sendMessageByte=null;
			int productCount=0;
			int heroCount=0;
			int otherCount=0;
			
			//模拟发送1000次消息
			for(int i=0;i<SEND_COUNT;i++)
			{
				int index=random.nextInt(routingKeys.length);
				routingKey=routingKeys[index];
				String[] keyWords=routingKey.split("\\.");
				if(HERO_CLASS.equals(keyWords[1]))
				{
					hero =new Hero(i, "盖伦"+i, "Lv"+i);
					sendMessageByte=SerializationUtils.serialize(hero);
					heroCount++;
				}
				else if(PRODUCT_CLASS.equals(keyWords[1]))
				{
					product=new Product(i, "显卡"+i, (float)i);
					sendMessageByte=SerializationUtils.serialize(product);
					productCount++;
				}
				else {
					
					sendMessageByte=SerializationUtils.serialize("其他数据"+i);
					otherCount++;
				}
				
				channel.basicPublish(EXCHANGE_NAME, routingKey, null, sendMessageByte);
				
				System.out.println("----- 生产者发送 数据！ id:"+i+"，关键字：" + routingKey + "，类：" + keyWords[1]);
				
				Thread.sleep(200);
			}
			
			System.out.println("----- 生产者发送 数据统计！ product：" + productCount + "个，hero:"+heroCount+"个，other：" + otherCount+"个");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
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
	
	private void connect() throws IOException, TimeoutException
	{
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost(HOST);
		connection= factory.newConnection();
		channel=connection.createChannel();
	}
	

}
