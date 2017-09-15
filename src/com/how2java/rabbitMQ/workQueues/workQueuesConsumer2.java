package com.how2java.rabbitMQ.workQueues;

import java.io.IOException;

import com.rabbitmq.client.*;

public class workQueuesConsumer2 {
private static final String TASK_QUEUE_NAME = "task_queue";
    
    public static void main(String[] argv)
        throws Exception
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println("Consumer:----- Worker2 [*] Waiting for messages. To exit press CTRL+C");
        // 每次从队列中获取数量
        channel.basicQos(1);
        
        final Consumer consumer = new DefaultConsumer(channel)
        {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                byte[] body)
                throws IOException
            {
                String message = new String(body, "UTF-8");
                
                System.out.println("Consumer:----- Worker2 [x] Received '" + message + "'");
                try
                {
                    doWork(message);
                }
                finally
                {
                    System.out.println("Consumer:----- Worker2 [x] Done");
                    // 消息处理完成确认
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        // 消息消费完成确认
        channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
    }
    
    /**
     * 任务处理
     * 
     * @param task
     *            void
     */
    private static void doWork(String task)
    {
        try
        {
            Thread.sleep(100); // 暂停1秒钟
        }
        catch (InterruptedException _ignored)
        {
            Thread.currentThread().interrupt();
        }
    }
}
