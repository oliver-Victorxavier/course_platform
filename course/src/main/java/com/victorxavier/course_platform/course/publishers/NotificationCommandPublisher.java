package com.victorxavier.course_platform.course.publishers;

import com.victorxavier.course_platform.course.dtos.NotificationCommandDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationCommandPublisher {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value(value = "${course_platform.broker.exchange.notificationCommandExchange}")
    private String notificationCommandExchange;

    @Value(value = "${course_platform.broker.key.notificationCommandKey}")
    private String notificationCommandKey;


    public void publishNotificationCommand(NotificationCommandDTO notificationCommandDTO) {
        rabbitTemplate.convertAndSend(notificationCommandExchange, notificationCommandKey, notificationCommandDTO);
    }

}
