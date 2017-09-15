package com.how2java.rabbitMQ.hello;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;

public class helloProducer {

	 private final static String QUEUE_NAME = "hello";
	    
	    /** <һ�仰���ܼ���>
	     * <������ϸ����>
	     * @param args
	     * @throws IOException 
	     * @throws TimeoutException 
	     * @see [�ࡢ��#��������#��Ա]
	     */
	    public static void main(String[] args)
	        throws IOException, TimeoutException
	    {
	        // �������ӹ���  
	        ConnectionFactory factory = new ConnectionFactory();
	        //      ����RabbitMQ��ַ  
	        factory.setHost("localhost");
	        //      ����һ���µ�����  
	        Connection connection = factory.newConnection();
	        //      ����һ��Ƶ��  
	        Channel channel = connection.createChannel();
	        //      ����һ������ -- ��RabbitMQ�У������������ݵ��Եģ�һ���ݵȲ������ص�����������ִ����������Ӱ�����һ��ִ�е�Ӱ����ͬ����
	        //      Ҳ����˵����������ڣ��ʹ�����������ڣ�������Ѿ����ڵĶ��в����κ�Ӱ�졣   
	        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	        String message = "Hello World!";
	        //      ������Ϣ��������  
	        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
	        System.out.println("Producer:-----P [x] Sent '" + message + "'");
	        //      �ر�Ƶ��������  
	        channel.close();
	        connection.close();
	    }
	
}
