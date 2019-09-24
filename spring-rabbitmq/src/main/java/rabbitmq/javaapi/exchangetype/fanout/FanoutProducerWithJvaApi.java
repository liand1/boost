package rabbitmq.javaapi.exchangetype.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import rabbitmq.javaapi.exchangetype.RabbitMqComponentUtils;

import static rabbitmq.javaapi.exchangetype.ExchangeConstants.EXCHANGE_FANOUT;

public class FanoutProducerWithJvaApi {

    /**
     * 它会把所有发送到该交换器的消息路由到所有与该交换器绑定的队列中。
     * 简单的讲，就是把交换机（Exchange）里的消息发送给所有绑定该交换机的队列，忽略routingKey。
     * @throws Exception
     */
    public void send() throws Exception {
        Channel channel = RabbitMqComponentUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_FANOUT, BuiltinExchangeType.FANOUT);
        String message = " this is a fanout message !";
        // 无需指定routingKey
        channel.basicPublish(EXCHANGE_FANOUT, "", null, ("message:"+message).getBytes());
        RabbitMqComponentUtils.close(channel);
    }

    public static void main(String[] args) throws Exception{
        new FanoutProducerWithJvaApi().send();
    }
}
