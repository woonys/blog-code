package jpabook.jpashop.service;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;

//JPA가 DB까지 접근하는 테스트를 만드는 게 여기서 필요 -> 스프링이랑 integration해서 테스트 돌릴 것!
@RunWith(SpringRunner.class) // JUnit 실행할 때 스프링이랑 같이 엮어서 실행할래! 라고 알려주는 어노테이션
@SpringBootTest // 스프링부트를 띄운 상태에서 테스트를 진행하기 위해 필요한 어노테이션. 의존성 주입을 위해 필요. 아니면 @Autowired 어노테이션이 다 터진다. 이건 스프링에서 해주는 거니까.
@Transactional // 테스트 클래스에 쓰면 롤백하기 위한 용도로 쓰임.
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
//    @Autowired
//    EntityManager em;

    @Test
//    @Rollback(false) // 이젠 트랜잭션에서 롤백 안하니까 insert 쿼리 날라간다!
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");
        //when
        Long savedId = memberService.join(member);
        //then
//        em.flush(); // 이렇게 해도 insert쿼리 나간다 -> flush를 날리니까. 이후에 rollback된다.
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");
        //when
        memberService.join(member1);
        memberService.join(member2); // 위에 test(expected = ) 처리하면 try catch 없어도 된다.
//        try {
//            memberService.join(member2); // 예외 발생해야 됨
//        } catch (IllegalStateException e) {
//            return;
//        }

        //then
        fail("예외가 발생해야 한다.");

    }

}