package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm()); 
        //view로 객체 넘겨주면, view(화면)에서 객체에 접근이 가능해짐
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
    //@Valid ???
    //@Valid + BindingResult > validation결과 오류가 있다면, 에러를 가진채로 아래 코드들 실행

        if(result.hasErrors()) {
            return "members/createMemberForm";
        }
        
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member); //join()이 '영속성 컨텍스트 -> DB' 메서드야???
        return "redirect:/";
    /**
     * 중복회원에 대한 예외처리 안한듯???
     */
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        //***화면에서 받거나, 화면에 뿌리는 정보는 항상 DTO나 Form을 이용하라.(Entity직접 사용X)
        //특히 API 제작시에는 더더욱 Entity를 노출시키면 안된다.
        model.addAttribute("members", members);
        return "members/memberList";
        //usage맵핑(?) 한 번 놓치니까 인텔리j 이악물고 안찾아주네;;;
    }
}
