package jpabook.jpashop.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")) // category_item 테이블을 매 -> 실전에서는 사용 안 함.
    private List<Item> items = new ArrayList<>();

    @ManyToOne // 내 부모는 하나이니
    @JoinColumn(name = "parent_id")
    private Category patent;

    @OneToMany(mappedBy = "parent") // 반면 자식은 여러 명
    private List<Category> child = new ArrayList<>();
}
