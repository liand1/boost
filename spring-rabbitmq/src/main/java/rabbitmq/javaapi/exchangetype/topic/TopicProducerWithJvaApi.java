package rabbitmq.javaapi.exchangetype.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import rabbitmq.javaapi.util.RabbitMqComponentUtils;

import static rabbitmq.javaapi.util.ExchangeConstants.*;

public class TopicProducerWithJvaApi {

    /**
     * topic是通过routingKey来模糊匹配。
     * * 代表任意一个单词(被"."分隔开的每一段独立的字符串为一个单词，如"com.rabbitmq.client", "com.hidden.client") ->*.*.client
     * # 代表0个或者多个单词 ->#.client
     * @throws Exception
     */
    public void send() throws Exception {
        Channel channel = RabbitMqComponentUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_TOPIC, BuiltinExchangeType.TOPIC);
        String message = " this is a topic message !";
        channel.basicPublish(EXCHANGE_TOPIC, TOPIC_ROUTING_KEY1, null, ("message:"+message).getBytes());
        channel.basicPublish(EXCHANGE_TOPIC, TOPIC_ROUTING_KEY2, null, ("message:"+message).getBytes());
        RabbitMqComponentUtils.close(channel);
    }

    public static void main(String[] args) throws Exception{
        new TopicProducerWithJvaApi().send();
    }
}
