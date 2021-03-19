package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

//    @Enumerated(EnumType.ORDINAL) //ORDINAL 절때 쓰지마(숫자 매핑임 0, 1, ... or 1, 2, ...)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //READY, COMP
}
