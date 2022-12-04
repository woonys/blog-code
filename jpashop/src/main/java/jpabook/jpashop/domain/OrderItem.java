package jpabook.jpashop.domain;

import static javax.persistence.FetchType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 당시 가격
    private int count; // 주문 당시 수량

    protected OrderItem() {
    } // 다른 데서 new로 생성하지 못하게 막으려고 protected를 건다. -> 아래처럼 생성 메소드를 사용하는 컨벤션 맞추려고.

    // 생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);  // item 내부 필드로 price를 가져가니까 그걸로 세팅하면 안됨? -> 할인 이런 것들로 인해 item.price와 orderPrice가 다를 수 있다.
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        // item 생성했으니 재고에서 빼줘야 함
        item.removeStock(count);
        return orderItem;
    }
    public void cancel() {
        // 재고 수량 원복
        item.addStock(count);
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }
}