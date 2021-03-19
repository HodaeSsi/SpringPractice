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

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") //> "나는 Order객체의 member변수에 종속되는 변수다"
    //연관관계 주인 설정(*)
    //DB에서는 FK로 연결된 두 변수를 알아서 관리해준다.
    //하지만 언어단(?)에서는 그게 아니다.
    //그러므로 변화에 있어 주종관계를 명시해준다.
    //이때 대체로(거의다) 외래키에 가까운(혹은 외래키를 포함한) 쪽이 주인이 된다.(for. 설계, 성능)
    private List<Order> orders = new ArrayList<>();
}
