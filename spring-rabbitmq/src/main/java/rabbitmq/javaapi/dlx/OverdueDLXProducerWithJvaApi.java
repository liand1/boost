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
        // 如果交换器不设置持久化，那么在 RabbitMQ服务重启之后，相关的交换器元数据会丢失，
        // 不过消息不会丢失，只是不能将消息发送到这个交换器中了。
        channel.exchangeDeclare(EXCHANGE_DIRECT, BuiltinExchangeType.DIRECT, true, false, null);
        // 如果队列不设置持久化，那么在 RabbitMQ 服务重启之后，相关队列的元数据会丢失，
        // 此时数据也会丢失。正所谓"皮之不存，毛将焉附"，队列都没有了，消息又能存在哪里呢?
        channel.queueDeclare(QUEUE_NAME, true, false, false, args);
        channel.queueBind(QUEUE_NAME, EXCHANGE_DIRECT, ROUTING_KEY1);

        // 创建死信交换器和队列
        channel.exchangeDeclare(EXCHANGE_DLX, BuiltinExchangeType.DIRECT, true, false, null);
        channel.queueDeclare(QUEUE_DLX, true, false, false, null);
        channel.queueBind(QUEUE_DLX, EXCHANGE_DLX, DLX_ROUTING_KEY);

        // 通过将消息的投递模式
        //(BasicPropert es 中的 deliveryMode 属性)设置为 即可实现消息的持久化。前面示例
        //中多次提及的 MessageProperties.PERSISTENT TEXT PLAIN 实际上是封装了这个属性
        channel.basicPublish(EXCHANGE_DIRECT, ROUTING_KEY1, MessageProperties.PERSISTENT_TEXT_PLAIN,
                ("message(overdue):" + MESSAGE).getBytes());

        RabbitMqComponentUtils.close(channel);
    }


    /**
     * 题外话
     * 将交换器、队列、消息都设置了持久化之后就能百分之百保证数据不丢失了吗?答案是否
     * 定的，
     * 1.首先从消费者来说，如果在订阅消费队列时将 autoAck 参数设置为 true ，那么 当消费者接
     * 收到相关消息之后，还没来得及处理就看机了，这样也算数据丢失。这种情况很好解决，将
     * autoAck 参数设置为 false 并进行手动确认
     * 2.其次，在持久化的消息正确存入 RabbitMQ 之后，还需要有一段时间(虽然很短，但是不
     * 可忽视〉才能存入磁盘之中。 RabbitMQ 并不会为每条消息都进行同步存盘(调用内核的 fsync
     * 方法)的处理，可能仅仅保存到操作系统缓存之中而不是物理磁盘之中。如果在这段时间内
     * RabbitMQ 服务节点发生了岩机、重启等异常情况，消息保存还没来得及落盘，那么这些消息将
     * 会丢失。这个问题怎么解决呢?这里可以引入 RabbitMQ 镜像队列机制(详细参考 .4节)，相当
     * 于配置了副本，如果主节点 master 在此特殊时间内挂掉，可以自动切换到从节点 slave ),
     * 这样有效地保证了高可用性，除非整个集群都挂掉。
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        new OverdueDLXProducerWithJvaApi().send();
    }
}
