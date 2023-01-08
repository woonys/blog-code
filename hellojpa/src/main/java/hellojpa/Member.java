package hellojpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class Member {

    /**
     * 권장하는 식별자 전략
     * - Long 형: 10억 넘어가도 괜찮아야 하고
     * - 대체키: 비즈니스적으로 의미 있는 키(자연키)를 키로 쓰는 게 아니라 UUID / Auto increment / Sequence Object 등.
     * 비즈니스를 키로 끌고 오는 건 절대 좋지 않다.
     * - 키 생성 전략
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id를 직접 생성하지 않고 알아서 생성하게끔 하고 싶을 때
    /**
     * JPA는 보통 트랜잭션 커밋 시점에 Insert 문을 실행했는데
     * 만약 Auto_increment를 쓰면 DB에 insert문을 실행하고 나서야 id 값을 알 수 있게 된다.
     * 따라서 Identity 전략을 쓰면 commit 시점이 아닌 그보다 앞선 em.persist() 시점에 즉시 Insert문을 날린 뒤, DB에서 식별자를 조회한다.
     */
    private Long id;

    @Column(name = "name")
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING) // DB에는 enum 타입이 없다 -> @Enumerated 어노테이션을 쓰면 된다.
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP) // 날짜 타입은  DATE(날짜), TIME(시간), TIMESTAMP(날짜+시간) 세 가지 종류가 있음.
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob // VARCHAR를 넘어서는 큰 길이의 데이터를 넣을 때 쓴다.
    private String description;

    @Transient // 테이블에 컬럼 넣고 싶지 않을 때 -> 얘는 메모리에서만 계산하고 디비에는 저장 안할 거야 할 때 이 어노테이션을 달아준다.
    private int temp;

    public Member() {
    }
}
