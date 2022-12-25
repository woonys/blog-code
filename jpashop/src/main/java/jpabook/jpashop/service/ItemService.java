package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional // 전체는 readonly로 세팅하되 특정 메소드에 대해 @Transactional을 부여하고 싶을 경우 -> 이렇게 메소드 위에 달면 더 가까운 어노테이션이 우선권을 갖는다.
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        /**
         * 변경 감지(Dirty checking):영속성 컨텍스트에서 엔티티를 다시 조회한 후 데이터를 수정
         * 리포지토리로부터 해당 객체 불러오면 JPA 영속성 컨텍스트에서 관리 시작
         * 이후에 값을 변경하고 나면 @Transactional에서 커밋 -> 변경 감지 후 DB에 플러시 날린다.
         *
         * 정리하면
         * 트랜잭션 안에서 엔티티를 다시 조회 & 변경할 값 선택 -> 트랜잭션 커밋 시점에 변경 감지(Dirty checking)이 동작해서
         * 데이터베이스에 Update SQL을 실행한다.
         */
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
    }



    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
