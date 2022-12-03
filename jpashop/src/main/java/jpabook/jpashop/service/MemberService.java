package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;

@Service

public class MemberService {

    /**
     * 1. 필드 인젝션
     * 단점 -> 못 바꿔..필드값을..테스트할 떄 진짜 객체 대신 mock을 넣어야 될 때도 있는데.
     */
//    @Autowired
//    private MemberRepository memberRepository;
    /**
     * 2. 수정자 주입(Setter injection)
     * 장점 - 메소드 기반으로 객체를 생성하니 인자로 mock 객체를 주입할 수 있음. 필드는 주입하기 어려운데.
     * 단점 - 런타임 시점에 누군가가 setter로 바꿀 수 있음. 일단 세터로 열려버린 순간 바뀔 여지가 있기 때문.
     */

//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
    private final MemberRepository memberRepository; // 파이널 달면 컴파일 시점에서 체크 가능.
    /**
     * 3. 생성자 주입
     * 장점 1: 생성할 때 인자 주입해버리고 끝 -> 런타임에 필드값이 바뀌거나 할 일이 없다.
     * 장점 2: 테스트 케이스 짤 때 필요한 의존성을 주입해야된다는 걸 명시적으로 나타냄.
     * 요즘 스프링에서는 아래처럼 autowired를 자동으로 어노테이션 달아준다(클래스 내에 생성자가 하나만 있을 때)
     */
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 개발해야 될 기능

    /** 회원 가입
     */
    @Transactional // 데이터 변경은 트랜잭션 안에서! -> javax에서 제공하는 트랜잭션이 있고 스프링에서 제공하는 애가 있는데 지금은 스프링을 많이 보고 있으니 스프링 어노테이션으로 간다.
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복회원 검증
        memberRepository.save(member); // SQL 쿼리 보면 insert 문이 안 나가 -> save 메소드가 em.persist()까지만 하니까. 트랜잭션 커밋될 때 플러시 나가면서 저장한다.
        return member.getId();

    }

    private void validateDuplicateMember(Member member) {
        // Exception 터뜨리기
        List<Member> findMembers = memberRepository.findByName(member.getName()); // 이것만 갖고 validation하기 부족 -> 동일한 name으로 동시에 가입할 수 있음 -> 따라서 name을 unique 조건으로 건다.
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

//        if (memberRepository.findByName(member.getName()).isEmpty()) {
//            return true;
//        } ㅊ
//        return false;
    }

    // 회원 전체 조회
    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 한 명 조회
    @Transactional(readOnly = true) // 조회 메소드는 트랜잭션 안에서 데이터가 바뀌지 않음. 따라서 readOnly를 달면 성능 최적화 가 -> 더티 체킹 안한다던가, 읽기 모드로만 읽는다던가 등등.
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
