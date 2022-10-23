package jpabook.jpashop;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        // CQRS -> 커맨드/쿼리 분리 원칙에 의거해서 id를 리턴하는 걸로 짠다. 저장은 사이드 이펙트를 일으킬 수 있는 커맨드 작업이기에 리턴값을 가급적 안 만드는 게 좋다.
        // 하지만 id 정도 리턴받으면 다음에 다시 조회 가능하니까.
        return member.getId();

    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
