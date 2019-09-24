package rabbitmq.springapi;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rabbitmq.QueueConstants;


@Configuration
public class QueueConfigWithSpring {

    @Bean
    public Queue getRealTimeQueue() {
        return new Queue(QueueConstants.REAL_TIME_QUEUE);
    }

}
