package com.victorxavier.course_platform.course.consumers;

import com.victorxavier.course_platform.course.dto.UserEventDTO;
import com.victorxavier.course_platform.course.enums.ActionType;
import com.victorxavier.course_platform.course.services.UserService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {

    @Autowired
    UserService userService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${course_platform.broker.queue.userEventQueue.name}", durable = "true"),
            exchange = @Exchange(value = "${course_platform.broker.exchange.userEventExchange}", type = ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true")
    ))
    public void listenUserEvent(@Payload UserEventDTO userEventDTO) {
        var usermodel = userEventDTO.convertToUserModel();

        switch (ActionType.valueOf(userEventDTO.actionType())){
            case CREATE:
            case UPDATE:
                userService.save(usermodel);
                break;
            case DELETE:
                userService.delete(userEventDTO.userId());
                break;
        }

    }
}
