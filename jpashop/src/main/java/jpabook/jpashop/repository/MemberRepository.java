package jpabook.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.Member;

@Repository
public class MemberRepository {
    // 스프링이 엔티티매니저를 만들어서 여기에 주입한다. 스프링에서 알아서 해줌
    @PersistenceContext
    private EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    //회원목록 뿌리기 위해 리스트 가져오기
    public List<Member> findAll() {
        // JPQL로 던진다 -> SQL은 테이블 대상으로 쿼리를 하는 반면, JPQL은 객체를 대상으로 쿼리를 던진다.
       return em.createQuery("select m from Member m", Member.class)
            .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                 .setParameter("name", name)
                 .getResultList();
    }
}
