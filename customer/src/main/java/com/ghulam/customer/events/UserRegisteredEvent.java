package com.ghulam.customer.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent {

    public static final String EVENT_TYPE = "UserRegisteredEvent";
    public static final String TOPIC = "finserve.auth.user.registered";

    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}