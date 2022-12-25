package jpabook.jpashop.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) { // 모델? -> 데이터를 담아서 컨트롤러 -> 뷰에 데이터 전달하는 용도
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult result) { // @NotEmpty, @Size 등 JavaX validation 기능을 활용해서 검증해준다.
        // BindingResult: 오류가 발생하면 해당 코드를 담아서 리턴
        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/"; // 첫번째 페이지로 리다이렉트
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
        /**
         * API를 만들 때 엔티티를 외부로 주면 안된다.
         * 1) 보안 위험성도 있고,
         * 2) API는 외부와 약속하는 표준 스펙임.
         * 그런데 임의로 엔티티 내부 필드를 바꾸거나 하면
         * 표준이 계속 바뀌어 불안정한 스펙이 된다.
         * 따라서 DTO로 바꾸는 게 가장 좋음.용
         */
    }
}
