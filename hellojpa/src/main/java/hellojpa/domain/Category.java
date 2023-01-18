package hellojpa.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {
    @Id @GeneratedValue
    private Long id;

    private String name;

    // 상위 카테고리 -> 셀프 매핑도 가능
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Category patent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    private List<Item> items = new ArrayList<>();
}
