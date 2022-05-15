package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded //@Embeddable 동일한 기능
    private Address address;

    @OneToMany(mappedBy = "member") //order에 있는 member에 매핑된다는 의미
    private List<Order> orders = new ArrayList<>();
}
