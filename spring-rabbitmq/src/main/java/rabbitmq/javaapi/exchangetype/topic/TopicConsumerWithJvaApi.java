package rabbitmq.javaapi.exchangetype.topic;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import rabbitmq.javaapi.exchangetype.RabbitMqComponentUtils;

import java.io.IOException;

import static rabbitmq.javaapi.exchangetype.ExchangeConstants.EXCHANGE_TOPIC;

@Slf4j
public class TopicConsumerWithJvaApi {

    //    public static final String BINDING_KEY = "#.client";
    public static final String BINDING_KEY = "*.*.client";
//    public static final String BINDING_KEY = "*.nothing.*";

    public void receive() throws Exception {
        Channel channel = RabbitMqComponentUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_TOPIC, BuiltinExchangeType.TOPIC);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_TOPIC, BINDING_KEY);
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
        channel.basicConsume(queueName, true, consumer);
    }

    public static void main(String[] args) throws Exception{
        new TopicConsumerWithJvaApi().receive();
    }
}
