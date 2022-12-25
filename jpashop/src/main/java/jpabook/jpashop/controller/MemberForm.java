package jpabook.jpashop.controller;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}

/**
 * 폼 객체를 따로 만들어야 하냐 vs. 엔티티 객체를 받아서 쓸 수 없냐?
 * - 요구사항이 단순하면 엔티티를 등록/수정해서 써도 괜찮음.
 * 하지만 실무에서는 그렇게 단순하지 않음.
 * 엔티티를 멤버 등록할 때 DTO 개념으로 써버리면 ->
 * 그에 필요한 기능 (NotNull 체크라던지 validatiion 로직 등)
 * 역시 추가되어야 함.
 * 화면 종속적인 기능이 추가되어야 하니 엔티티 코드가 지저분해진다.
 * 결국 유지보수가 어려워짐.
 * 엔티티는 어디에도 종속되지 않고 핵심 비즈니스 로직만 갖고 있어야 한다.
 * 화면 등록 객체는 DTO라던지 폼 객체를 써야 한다.
 */
