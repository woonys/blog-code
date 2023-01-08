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
            // 영속

            Member member = new Member();
            em.persist(member);

            em.flush(); // flush를 먼저 호출하면 아래 라인보다 Insert 문이 먼저 날아간다.
            /*
            플러시 - 영속성 컨텍스트를 비우지 않는다.
            영속성 컨텍스트 변경 내용을 데이터베이스에 동기화하는 것.
            트랜잭션이라는 작업 단위가 더 중요하다. -> 커밋 직전에만 동기화하면 된다.
            */

            System.out.println("=====================");
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}