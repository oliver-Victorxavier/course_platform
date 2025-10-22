package com.victorxavier.course_platform.authuser.publishers;

import com.victorxavier.course_platform.authuser.dtos.UserEventDTO;
import com.victorxavier.course_platform.authuser.enums.ActionType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value(value = "${course_platform.broker.exchange.userEvent}")
    private String exchangeUserEvent;

    public void publishUserEvent(UserEventDTO userEventDTO, ActionType  actionType) {

        UserEventDTO eventDTO = new UserEventDTO(
                userEventDTO.userId(),
                userEventDTO.username(),
                userEventDTO.email(),
                userEventDTO.fullName(),
                userEventDTO.userStatus(),
                userEventDTO.userType(),
                userEventDTO.phoneNumber(),
                userEventDTO.cpf(),
                userEventDTO.imageUrl(),
                actionType.toString()
        );

        rabbitTemplate.convertAndSend(exchangeUserEvent, " ", eventDTO);
    }

}
