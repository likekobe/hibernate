package com.how2java.rabbitMQ.hello;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;

public class helloProducer {

	 private final static String QUEUE_NAME = "hello";
	    
	    /** <一句话功能简述>
	     * <功能详细描述>
	     * @param args
	     * @throws IOException 
	     * @throws TimeoutException 
	     * @see [类、类#方法、类#成员]
	     */
	    public static void main(String[] args)
	        throws IOException, TimeoutException
	    {
	        // 创建连接工厂  
	        ConnectionFactory factory = new ConnectionFactory();
	        //      设置RabbitMQ地址  
	        factory.setHost("localhost");
	        //      创建一个新的连接  
	        Connection connection = factory.newConnection();
	        //      创建一个频道  
	        Channel channel = connection.createChannel();
	        //      声明一个队列 -- 在RabbitMQ中，队列声明是幂等性的（一个幂等操作的特点是其任意多次执行所产生的影响均与一次执行的影响相同），
	        //      也就是说，如果不存在，就创建，如果存在，不会对已经存在的队列产生任何影响。   
	        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	        String message = "Hello World!";
	        //      发送消息到队列中  
	        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
	        System.out.println("Producer:-----P [x] Sent '" + message + "'");
	        //      关闭频道和连接  
	        channel.close();
	        connection.close();
	    }
	
}
