package rabbitmq.javaapi.exchangetype.direct;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import rabbitmq.javaapi.exchangetype.RabbitMqComponentUtils;

import java.io.IOException;

import static rabbitmq.javaapi.exchangetype.ExchangeConstants.*;

@Slf4j
public class DirectConsumerWithJvaApi {

    private void receive() throws Exception {
        Channel channel = RabbitMqComponentUtils.getChannel();
        String queueName = null;
        // create default queue
        queueName = channel.queueDeclare().getQueue();
        // bind queue to exchange and build routing_key
        channel.queueBind(queueName, EXCHANGE_DIRECT, ROUTING_KEY1);
        channel.queueBind(queueName, EXCHANGE_DIRECT, ROUTING_KEY2);
        // create consumer
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {

                String message = new String(body, "UTF-8");
                log.info("receive message：{}", message);
                channel.basicAck(envelope.getDeliveryTag(), true);
            }

        };
        // 声明队列中被消费掉的消息（参数为：队列名称；消息是否自动确认;consumer主体）
        // 为了保证消息从队列可靠地达到消费者， RabbitMQ 提供了消息确认机制( message
        // acknowledgement) 消费者在订阅队列时，可以指定 aut oAck 参数，当 autoAck 等于 false
        // 时， RabbitMQ 会等待消费者显式地回复确认信号后才从内存(或者磁盘)中移去消息(实质上
        // 是先打上删除标记，之后再删除) 。当 autoAck 等于 true 时， RabbitMQ 会自动把发送出去的
        // 消息置为确认，然后从内存(或者磁盘)中删除，而不管消费者是否真正地消费到了这些消息
        // 采用消息确认机制后，只要设置 autoAck 参数为 false ，消费者就有足够的时间处理消息
        // (任务) ，不用担心处理消息过程中消费者进程挂掉后消息丢失的问题 因为 RabbitMQ 会一直
        // 等待持有消息直到消费者显式调 Basic.Ack 命令为止
        // utoAck 参数置为 false ，对于 RabbitMQ 服务端而 ，队列中的消息分成了两个部分
        // 部分是等待投递给消费者的消息:一部分是己经投递给消费者，但是还没有收到消费者确认
        // 信号的消息。 如果 RabbitMQ 直没有收到消费者的确认信号，并且消费此消息的消费者己经
        // 断开连接，则 RabbitMQ 会安排该消息重新进入队列，等待投递给下 个消费者，当然也有可
        // 能还是原来的那个消费者。
        channel.basicConsume(queueName, false, consumer);
    }

    public static void main(String[] args) throws Exception {
        new DirectConsumerWithJvaApi().receive();
    }
}
