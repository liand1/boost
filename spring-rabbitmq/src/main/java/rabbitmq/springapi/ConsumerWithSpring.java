package rabbitmq.springapi;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rabbitmq.QueueConstants;

@Component
@RabbitListener(queues = QueueConstants.REAL_TIME_QUEUE)
public class ConsumerWithSpring {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @RabbitHandler
    public void receive(String msg) {
        System.out.println("[string] recieved message:" + msg);
    }

}
