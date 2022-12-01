package jpabook.jpashop.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id") // 요렇게 컬럼 어노테이션을 쓰면 해당 name으로 DB 테이블 내에서 일치하는 컬럼명을 찾아 매핑한다.
    private Long id;

    private String name;

    @Embedded // 내장 타입을 포함했다는 뜻
    private Address address;

    @OneToMany(mappedBy = "member") // 얘 입장에서는 여러 주문을 하니까 일대다 & mappedBy -> FK를 orders 테이블에 있는 member에서 갖고 있으니 얘가 매핑되어지는 것임을 나타내기 위해
    private List<Order> orders = new ArrayList<>(); // 근데 이거 생성자에서 초기화하면 안됨? -> 초기화 고민 안해도 됨. NPE 체크 안해도 되고. 바로 초기화하는 게 안전하기에.
}
