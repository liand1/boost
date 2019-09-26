package rabbitmq.javaapi.ttl;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import rabbitmq.javaapi.exchangetype.RabbitMqComponentUtils;

import static rabbitmq.javaapi.exchangetype.ExchangeConstants.*;

public class TTLProducerWithJvaApi {

    public static final String MESSAGE = " this is a ttl message !";

    /**
     * 对单个消息体设置过期时间, 即使消息过期,也不会马上从队列中抹去,因为每条消息是否过期是在即将投递到消费
     * 者之前判定的。因为每条消息的过期时间不同,如果要删除所有过期消息势必要扫描整个队列,所以不如等到此消息即将
     * 被消费时再判定是否过期 , 如果过期再进行删除即可。
     * @throws Exception
     */
    public void send() throws Exception {
        Channel channel = RabbitMqComponentUtils.getChannel();
        // 创建一个 type="direct" 、持久化的、非自动删除的交换器
        channel.exchangeDeclare(EXCHANGE_DIRECT, BuiltinExchangeType.DIRECT, true, false, null);
        // 创建一个持久化、非排他的、非自动删除的队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 将交换器与队列通过路由键绑定
        channel.queueBind(QUEUE_NAME, EXCHANGE_DIRECT, ROUTING_KEY1);

        channel.basicPublish(EXCHANGE_DIRECT, ROUTING_KEY1, RabbitMqComponentUtils.getTTLProperties(),
                ("message(overdue):" + MESSAGE).getBytes());
        channel.basicPublish(EXCHANGE_DIRECT, ROUTING_KEY1, null,
                ("message(forever):" + MESSAGE).getBytes());
        RabbitMqComponentUtils.close(channel);
    }


    public static void main(String[] args) throws Exception {
        new TTLProducerWithJvaApi().send();
    }
}
