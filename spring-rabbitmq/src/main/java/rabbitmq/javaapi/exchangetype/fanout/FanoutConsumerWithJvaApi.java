package rabbitmq.javaapi.exchangetype.fanout;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import rabbitmq.javaapi.exchangetype.RabbitMqComponentUtils;

import java.io.IOException;

import static rabbitmq.javaapi.exchangetype.ExchangeConstants.EXCHANGE_FANOUT;

@Slf4j
public class FanoutConsumerWithJvaApi {

    public void receive() throws Exception {
        Channel channel = RabbitMqComponentUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_FANOUT, BuiltinExchangeType.FANOUT);
        String queueName = channel.queueDeclare().getQueue();
        // 无需指定routingKey
        channel.queueBind(queueName, EXCHANGE_FANOUT, "");
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

    public static void main(String[] args) throws Exception {
        for(int i=0; i<3; i++) {
            new FanoutConsumerWithJvaApi().receive();
        }
    }
}
