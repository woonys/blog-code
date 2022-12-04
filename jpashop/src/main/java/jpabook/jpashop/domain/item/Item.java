package jpabook.jpashop.domain.item;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 한 테이블에 Album/Book/Movie 다 때려박는 전략
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 비즈니스 로직 -> 객체지향 관점에서 도메인에 비즈니스 로직이 있는게 응집력이 좋다
    // 세터로 바깥에서 변경하는 게 아니라, 바깥에서 해당 값을 변경하고 싶을 때는 메소드로 호출해서 변경하도록 한다. 이게 객체지향적인 방법.
    /**
     * stock 증가
     */
    public void addStock(int quantity) {

        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quanty) {
        int restStock = this.stockQuantity - quanty;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
