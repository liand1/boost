package rabbitmq.javaapi.dlx;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import rabbitmq.javaapi.util.RabbitMqComponentUtils;

import java.util.HashMap;
import java.util.Map;

import static rabbitmq.javaapi.util.ExchangeConstants.*;

/**
 * Dead-Letter-Exchange ,可以称之为死信交换器,也有人称之为死信邮箱。当
 * 消息在一个队列中变成死信 (dead message) 之后,它能被重新被发送到另一个交换器中,这个
 * 交换器就是DLX ,绑定 DLX 的队列就称之为死信队列。
 * 消息变成死信 一 般是由于以下几种情况:
 * 1消息被拒绝 (Basic.Reject/Basic.Nack) ,井且设置 requeue 参数为 false;
 * 2消息过期;
 * 3队列达到最大长度。
 */
public class OverdueDLXProducerWithJvaApi {

    public static final String MESSAGE = "this is a DLX message !";

    /**
     * 如果运行报错，可能这是因为已经定义的队列，再次定义是无效的，这就是幂次原理。
     * RabbitMQ不允许重新定义一个已有的队列信息，也就是说不允许
     * 修改已经存在的队列的参数。如果你非要这样做，只会返回异常。
     * @throws Exception
     */
    public void send() throws Exception {
        Channel channel = RabbitMqComponentUtils.getChannel();

        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 3000);
        args.put("x-dead-letter-exchange", EXCHANGE_DLX);
        args.put("x-dead-letter-routing-key", DLX_ROUTING_KEY);

        // 正常的队列绑定
        channel.exchangeDeclare(EXCHANGE_DIRECT, BuiltinExchangeType.DIRECT, true, false, null);
        channel.queueDeclare(QUEUE_NAME, true, false, false, args);
        channel.queueBind(QUEUE_NAME, EXCHANGE_DIRECT, ROUTING_KEY1);

        // 创建死信交换器和队列
        channel.exchangeDeclare(EXCHANGE_DLX, BuiltinExchangeType.DIRECT, true, false, null);
        channel.queueDeclare(QUEUE_DLX, true, false, false, null);
        channel.queueBind(QUEUE_DLX, EXCHANGE_DLX, DLX_ROUTING_KEY);

        channel.basicPublish(EXCHANGE_DIRECT, ROUTING_KEY1, MessageProperties.PERSISTENT_TEXT_PLAIN,
                ("message(overdue):" + MESSAGE).getBytes());

        RabbitMqComponentUtils.close(channel);
    }

    public static void main(String[] args) throws Exception{
        new OverdueDLXProducerWithJvaApi().send();
    }
}
