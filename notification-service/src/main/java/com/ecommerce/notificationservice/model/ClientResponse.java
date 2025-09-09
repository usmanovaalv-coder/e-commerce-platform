package com.ecommerce.notificationservice.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientResponse {

    long id;
    String name;
    String email;
    String phone;
    String address;

}
