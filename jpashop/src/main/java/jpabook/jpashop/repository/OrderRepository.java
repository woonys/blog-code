package jpabook.jpashop.repository;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllByString(OrderSearch orderSearch) { // 동적쿼리를 JPQL 문자열로 생성하는 방식 -> 안티패턴
        // 동적쿼리를 짜려면?
        List<Order> resultList = em.createQuery("select o from Order o join o.member m" +
                                                " where o.status = :status " +
                                                " and m.name like :name", Order.class)
                                   .setParameter("status", orderSearch.getOrderStatus())
                                   .setParameter("name", orderSearch.getMemberName())
                                   .setMaxResults(1000) // 최대 1000 건
                                   .getResultList();// JPQL 작성

        /* 동적 쿼리의 안 좋은 예시
        - 문제 1: 이렇게 JPQL을 문자로 생성하면 번거로움
        - 문제 2 : 어디서 버그가 나타나는지 찾기가 매우 힘들다... 반면 MyBatis: 동적 쿼리를 생성하기 매우 좋다는 이점이 있다?
         */
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += "where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " and";
        }
        jpql += " m.name like :name";

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                                    .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }

    /**
     * JPA criteria로 동적 쿼리 -> 역시 별로 좋은 방식은 아님..
     * 방식: JPQL을 자바 코드로 작성할 수 있도록 JPA에서 제공하는 표준 방. 동적 쿼리를 작성할 때 큰 도움.
     * 단점: 유지보수성이 제로에 가까움 -> 무슨 쿼리가 생성될지 감이 전혀 오지 않는다!
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus()); // predicate가 조건이 된다.
            criteria.add(status);
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

    /**
     * 해결책: QueryDSL! -> 동적 쿼리 해결 방안! -> 동적 쿼리 뿐만 아니라 복잡한 정적 쿼리 역시 QueryDSL로 짠다!
     */

//    public List<Order> findAll(OrderSearch orderSearch) {
//        QOrder order = QOrder.order;
//        QMmember member = QMember.member;
//
//        return query
//            .select(order)
//            .from(order)
//            .join(order.member, member)
//            .where(statusEq(orderSearch.getOrderStatus()),
//                   nameLike(orderSearch.getMemberName()))
//            .limit
//            .fetch();
//    }
}
