package com.victorxavier.course_platform.notification.consumers;

import com.victorxavier.course_platform.notification.dtos.NotificationCommandDTO;
import com.victorxavier.course_platform.notification.enums.NotificationStatus;
import com.victorxavier.course_platform.notification.models.NotificationModel;
import com.victorxavier.course_platform.notification.services.NotificationService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class NotificationConsumer {

    private final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${course_platform.broker.queue.notificationCommandQueue.name}", durable = "true"),
            exchange = @Exchange(value = "${course_platform.broker.exchange.notificationCommandExchange}", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
            key = "${course_platform.broker.key.notificationCommandKey}"))
    public void listen(@Payload NotificationCommandDTO notificationCommandDTO) {
        var notificationModel = new NotificationModel();
        BeanUtils.copyProperties(notificationCommandDTO, notificationModel);
        notificationModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        notificationModel.setNotificationStatus(NotificationStatus.CREATED);
        notificationService.saveNotification(notificationModel);
    }

}
