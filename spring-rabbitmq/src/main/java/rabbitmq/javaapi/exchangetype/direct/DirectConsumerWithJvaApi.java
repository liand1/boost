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
        channel.basicConsume(queueName, true, consumer);
    }

    public static void main(String[] args) throws Exception {
        new DirectConsumerWithJvaApi().receive();
    }
}
