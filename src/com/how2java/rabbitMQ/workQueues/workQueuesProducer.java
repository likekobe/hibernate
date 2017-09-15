package com.how2java.rabbitMQ.workQueues;

import com.rabbitmq.client.*;

public class workQueuesProducer {

private static final String TASK_QUEUE_NAME = "task_queue";
    
    public static void main(String[] argv)
        throws java.io.IOException, Exception
    {
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        //      分发消息
        for (int i = 0; i < 5; i++)
        {
            String message = "Hello World! " + i;
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("Producer:----- [x] Sent '" + message + "'");
        }
        channel.close();
        connection.close();
    }
	
}
