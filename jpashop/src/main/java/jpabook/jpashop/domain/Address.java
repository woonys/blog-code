package jpabook.jpashop.domain;

import javax.persistence.Embeddable;

import lombok.Getter;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // JPA 구현 라이브러리가 Address 객체 생성할 때 Reflection, Proxy 등의 기술을 쓰기 위해 필요한 재료. 실제로는 쓰지 않을 예정.
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
