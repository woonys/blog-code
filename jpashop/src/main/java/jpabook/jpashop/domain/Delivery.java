package jpabook.jpashop.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) // ordinal이면 1,2,3,4,... -> 숫자로 들어간다(READY:0, COMP:1, ...) -> 문제: READY와 COMP 사이에 다른 거 들어가면 개망..그러니 String으로 넣자
    private DeliverStatus status; // READY, COMP
}
