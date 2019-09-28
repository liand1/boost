package rabbitmq.javaapi.rpc;

import com.rabbitmq.client.*;
import org.springframework.amqp.utils.SerializationUtils;
import rabbitmq.javaapi.util.RabbitMqComponentUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;


/**
 * 1)当客户端启动时，创建一个匿名的回调队列(名称由 RabbitMQ 自动创建
 * (2) 客户端为 RPC 请求设置 个属性 reply 用来告知 RPC 服务端回复请求时的目的
 * 队列，即回调队列; correlationld 用来标记一个请求。
 * (3)请求被发送到rpc_queue列中。
 * (4) RPC服务端监听rpc_queue队列中的请求，当请求到来时 服务端会处理并且把带有
 * 结果的消息发送给客户端 接收的队列就是 replyTo 设定 回调队列。
 * (5)客户端监昕回调队列 当有消息时 检查 correlationld 属性，如果与请求匹配，
 * 那就是结果了。
 */
public class RpcClientWithJvaApi {

    /**连接*/
    private static Connection connection;
    /**通道*/
    private static Channel channel;
    /**队列名*/
    private String requestQueueName = "rpc_queue";
    /**回复队列名*/
    private static String replyQueueName;
    static{

        try {
            // 创建通道
            channel = RabbitMqComponentUtils.getChannel();
            // 获取连接
            connection = channel.getConnection();
            // 获取队列名
            replyQueueName = channel.queueDeclare().getQueue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Object call(Object object) throws UnsupportedEncodingException, IOException, InterruptedException{
        // 用来关联请求 request) 其调用 RPC 之后的回复 (response)
        final String corrId = UUID.randomUUID().toString();
        // 设置请求属性
        AMQP.BasicProperties props = new AMQP.BasicProperties()
                .builder()
                .correlationId(corrId)
                // 用来设置一个回调队列。
                .replyTo(replyQueueName)
                .build();
        // 发布服务
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(object);
        byte[] objBytes = bo.toByteArray();

        channel.basicPublish("", requestQueueName, props, objBytes);
        final BlockingQueue response = new ArrayBlockingQueue(1);
        // .接受服务端响应结果
        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(SerializationUtils.deserialize(body));
                }
            }
        });
        return response.take();
    }
    /**
     * 释放资源
     * @throws IOException
     * @throws TimeoutException
     */
    public void close() throws IOException, TimeoutException {
        if(channel!=null){
            channel.close();
        }
        if(connection!=null){
            connection.close();
        }

    }

    public static void main(String[] args) throws Exception{
        RpcClientWithJvaApi client = new RpcClientWithJvaApi();
        RpcUser user= new RpcUser(1, "admin", "123456");
        System.out.println(client.call(user));

    }
}
