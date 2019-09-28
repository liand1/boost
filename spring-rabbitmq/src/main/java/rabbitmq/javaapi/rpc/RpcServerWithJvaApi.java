package rabbitmq.javaapi.rpc;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.utils.SerializationUtils;
import rabbitmq.javaapi.exchangetype.RabbitMqComponentUtils;

import java.io.IOException;

@Slf4j
/**
 * RPC Remote Procedure Call 的简称，即远程过程调用。它是 种通过网络从远程计算
 * 机上请求服务，而不需要了解底层网络的技术。通俗点来说，假设有两台服务器 个应
 * 用部署在 服务器上，想要调用 服务器上应用提供的函数或者方法，由于不在同一个
 * 内存空间 不能直接调用，需要通过网络来表达调用的语义和传达调用的数据。
 */
public class RpcServerWithJvaApi {

    /**RPC对列名*/
    private static final String RPC_QUEUE_NAME = "rpc_queue";


    public static void main(String[] args) throws Exception{
        try {
            // 4.创建通道
            Channel channel = RabbitMqComponentUtils.getChannel();
            // 5.声明队列
            channel.queueDeclare(RPC_QUEUE_NAME, true, false, false, null);
            // 6.设置最大服务转发数量一次一个
            channel.basicQos(1);
            System.out.println("waiting RPC requests.............");
            // 7.消费请求信息
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(properties.getCorrelationId())
                            .build();
                    boolean login = false;
                    try {
                        System.out.println(body);
                    }
                    catch (RuntimeException e){
                        e.printStackTrace();
                    }
                    finally {
                        // 8.发布响应信息
                        channel.basicPublish( "", properties.getReplyTo(), replyProps, SerializationUtils.serialize(login));
                        channel.basicAck(envelope.getDeliveryTag(), false);
                        // RabbitMq consumer worker thread notifies the RPC server owner thread
                        synchronized(this) {
                            this.notify();
                        }
                    }
                }
            };
            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
            // 9.Wait and be prepared to consume the message from RPC client.
            while (true) {
                synchronized(consumer) {
                    try {
                        consumer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
//            RabbitMqComponentUtils.close(channel);
        }
    }

}
