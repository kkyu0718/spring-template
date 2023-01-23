package com.kyuwon.spring.domain.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Address {

    @Setter
    private String country;

    @Setter
    private String state;

    @Setter
    private String city;

    @Setter
    private String zipCode;

    @Builder
    public Address(String country, String state, String city, String zipCode) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.zipCode = zipCode;
    }
}