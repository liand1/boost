package rabbitmq.javaapi.exchangetype.direct;

import com.rabbitmq.client.Channel;
import rabbitmq.javaapi.util.RabbitMqComponentUtils;

import static rabbitmq.javaapi.util.ExchangeConstants.*;

public class DirectProducerWithJvaApi {

    /**
     * direct（指定模式，RabbitMQ的默认模式）
     * 指定exchange为direct，发送者发送消息时设置routing_key（路由键），消费者绑定exchange
     * 并指定bing_key与消息的routing_key相同，此时消息发送到交换器，消费者绑定的queue接收到
     * 消息，再由routing_key决定哪个消费者消费消息，没有对应的routing_key则消息丢失。消费者
     * 根据设置的binding_key（绑定键）来选择性的接收自身业务关心的massage。
     * @throws Exception
     */
    public void send() throws Exception {
        Channel channel = RabbitMqComponentUtils.getChannel();
        String message = " this is a direct message !";
        channel.basicPublish(EXCHANGE_DIRECT, ROUTING_KEY1, null, ("message1:"+message).getBytes());
        channel.basicPublish(EXCHANGE_DIRECT, ROUTING_KEY2, null, ("message2:"+message).getBytes());
        RabbitMqComponentUtils.close(channel);
    }

    public static void main(String[] args) throws Exception{
        new DirectProducerWithJvaApi().send();
    }
}
