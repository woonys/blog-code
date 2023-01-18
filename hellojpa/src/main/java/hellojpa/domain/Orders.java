package hellojpa.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "ORDERS")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // member-orders 중 FK를 Orders에서 들고 있으니 Orders가 주인! -> 여기서 누구를 join할지 결정하니까 JoinColumn()을 적어줘야 함
    // 단방향으로 간다 -> Order에만 Member를 추가하고 Member에는 Order를 추가하지 않는다.
    // 객체 입장에서 양방향 -> 양쪽 다 신경써야 하기 때문에 복잡도가 늘어난다.
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @OneToMany(mappedBy = "orders")
    @Column(name = "order_item")
    private List<OrderItem> orderItem = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "delivery")
    private Delivery delivery;
}
