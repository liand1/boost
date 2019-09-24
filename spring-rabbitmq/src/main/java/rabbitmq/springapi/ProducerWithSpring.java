package rabbitmq.springapi;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rabbitmq.QueueConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ProducerWithSpring {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        Date date = new Date();
        String dateString = new SimpleDateFormat("YYYY-mm-DD hh:MM:ss").format(date);
        System.out.println("[string] send msg:" + dateString);
        this.rabbitTemplate.convertAndSend(QueueConstants.REAL_TIME_QUEUE, dateString);
    }

}
