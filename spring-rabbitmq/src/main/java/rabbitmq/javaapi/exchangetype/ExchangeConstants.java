package rabbitmq.javaapi.exchangetype;

public class ExchangeConstants {

    public static final String QUEUE_NAME = "queue_demo";
    public static final String EXCHANGE_DIRECT = "direct_exchange";
    public static final String EXCHANGE_FANOUT = "fanout_exchange";
    public static final String EXCHANGE_TOPIC = "topic_exchange";
    public static final String ROUTING_KEY1 = "direct_routing_key1";
    public static final String ROUTING_KEY2 = "direct_routing_key2";
    public static final String TOPIC_ROUTING_KEY1 = "com.rabbitmq.client";
    public static final String TOPIC_ROUTING_KEY2 = "com.hidden.client";
    public static final String DLX_ROUTING_KEY = "dlx.routing";
    public static final String IP_ADDRESS = "localhost";
    public static final String USERNAME = "admin";
    public static final String PASSWORD = "admin";
    public static final int PORT = 5672;//RabbitMQ 服务端默认端口号为 5672
    public static final String VIRTUAL_HOST = "/";
}
