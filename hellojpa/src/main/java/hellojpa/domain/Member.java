package hellojpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id를 직접 생성하지 않고 알아서 생성하게끔 하고 싶을 때

    private Long id;

    @Column(name = "name")
    private String username;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "zipcode")
    private String zipCode;

    @OneToMany(mappedBy = "member")
    private List<Orders> ordersList = new ArrayList<>();
    public Member() {
    }
}
