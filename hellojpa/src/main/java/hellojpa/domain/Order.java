package hellojpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // member-orders 중 FK를 Orders에서 들고 있으니 Orders가 주인! -> 여기서 누구를 join할지 결정하니까 JoinColumn()을 적어줘야 함
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "orders")
    @Column(name = "order_item")
    private List<OrderItem> orderItem = new ArrayList<>();
}
