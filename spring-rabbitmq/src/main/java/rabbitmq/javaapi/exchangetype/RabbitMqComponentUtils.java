package rabbitmq.javaapi.exchangetype;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

import static rabbitmq.javaapi.exchangetype.ExchangeConstants.*;

public class RabbitMqComponentUtils {

    public static Channel getChannel() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VIRTUAL_HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }

    /**
     * 显式地关闭Channel，但这不是必须的，在Connection关闭的时候，
     * Channel 也会自动关闭。
     * @param channel
     * @throws Exception
     */
    public static void close(Channel channel) throws Exception{
        channel.close();
        channel.getConnection().close();
    }

    // 设置过期时间
    public static AMQP.BasicProperties getTTLProperties() {
        AMQP.BasicProperties properties = new AMQP.BasicProperties();
        Map<String, Object> headers = new HashMap<>();
        headers.put("name", "depu");
        return properties.builder()
                .contentEncoding("UTF-8")
                .headers(headers)
                .expiration("5000").build();
    }
}
