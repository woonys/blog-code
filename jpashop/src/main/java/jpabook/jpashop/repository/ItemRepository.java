package jpabook.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) { // JPA에서는 저장 전까지는 엔티티에 id가 없다. 값이 없다는 건 새로 생성한 객체라는 것.
            em.persist(item); // 엔티티에 아이디값이 없다는 건 신규 객체라는 뜻이니 persist해서 디비에 먼저 등록한다.
        }  // 만약 해당 객체가 내부 디비에 있다면 -> 신규 저장이 아닌 업데이트에 해당할 것.
        em.merge(item); // 이 경우 merge를 한다.
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
            .getResultList();
    }
}
