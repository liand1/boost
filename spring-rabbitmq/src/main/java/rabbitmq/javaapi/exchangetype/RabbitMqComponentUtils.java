package rabbitmq.javaapi.exchangetype;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
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

    public static void close(Channel channel) throws Exception{
        channel.close();
        channel.getConnection().close();
    }
}
