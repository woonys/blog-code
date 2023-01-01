package hellojpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        // 엔티티 매니저 팩토리는 웹 서버가 올라오는 시점에 DB당 하나만 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        // 엔티티 매니저는 고객 요청이 올 때마다 새로 만들어진다.
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // 그냥 변경하면 안됨 -> JPA에서 수정 작업은 반드시 트랜잭션 안에서.
        // code
        try {

            // 저장
//            Member member = new Member();
//            member.setId(1L);
//            member.setName("helloA");
//            em.persist(member);
            // 조회
//            Member findMember = em.find(Member.class, 2L);// 멤버 찾아올 수 있음
//            System.out.println("findMember.id = " + findMember.getId());
//            System.out.println("findMember.name = " + findMember.getName());

            // 수정
//            Member findMember = em.find(Member.class, 2L);
//            findMember.setName("helloB");
            // em.persist() 없어도 된다 -> JPA가 알아서 변경 감지하고 저장하기 때문

            // JPQL: 객체지향 SQL! -> 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                                    .getResultList();
            for (Member member : result) {
                System.out.println("member.getName() = " + member.getName());
                
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
