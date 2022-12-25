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
        /**
         * Merge: 준영속 상태 엔티티를 영속 상태로 변경할 때 사용하는 기능
         * 1. merge() 날린다.
         * 2. JPA는 1차 캐시 엔티티를 찾고, 없으면 DB에서 조회한다.(병합을 하는 엔티티라면
         * 애초에 준영속 상태일테니 DB에서 찾아온다.
         * 3. 병합으로 값을 채운다 ->  하이버네이트가 기존 객체에 새로운 객체 값을 쭉 밀어 넣는다.
         * - 필드값을 바꿔치기함.
         * - 이제 해당 엔티티는 영속상태로 변경됨.
         * - 코드 동작 방식: 아래와 동일
         *         Item findItem = itemRepository.findOne(itemId);
         *         findItem.setPrice(param.getPrice());
         *         findItem.setName(param.getName());
         *         findItem.setStockQuantity(param.getStockQuantity());
         *         return findItem;
         * 4. 이걸 반환한다.
         * - 이때, 위의 save()에서 인자로 들어온 Item은 영속 상태로 처리하지 않는다.
         * 주의: 병합 쓰면 모든 필드값이 싹다 변경. 만약 인자로 값이 안 들어오는 필드가 있으면 null로 변경될 위험이 있음.
         */
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
            .getResultList();
    }
}
