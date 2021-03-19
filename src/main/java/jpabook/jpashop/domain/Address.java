package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter //Setter X? > 값 타입은 '이뮤터블'하게 설계되는 것이 좋다.(오직 생성자만)
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected  Address() {
        //jpa를 위한 기본 생성자(리플렉션? 프록시?)
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
