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

    @Embedded //내장타입이기에 써주어야함
    private Address address;

    @Enumerated(EnumType.STRING) //EnumType.ORDINAL은 순서가 밀리게 되는 경우가 있기에 왠만하면 사용X
    private DeliveryStatus status; //READY, COMP
}
