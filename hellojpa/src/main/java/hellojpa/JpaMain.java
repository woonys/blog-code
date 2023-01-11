package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import hellojpa.teamexample.Member;
import hellojpa.teamexample.Team;

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
            // 영속

            Member member = new Member();
            member.setUserName("member1");

            em.persist(member);


            Member findMember1 = em.find(Member.class, member.getId());
            Member findMember2 = em.find(Member.class, member.getId());
            System.out.println("result = " + (findMember1 == findMember2));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}