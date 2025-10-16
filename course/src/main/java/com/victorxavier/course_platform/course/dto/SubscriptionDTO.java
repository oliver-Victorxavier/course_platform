package com.victorxavier.course_platform.course.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SubscriptionDTO(

        @NotNull UUID userId
) {
}
