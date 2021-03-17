package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello") //반응할(실행할) 신호
    public String hello(Model model) { //모델을 통해 '컨트롤러 > 뷰' 데이터 전달 가능
        model.addAttribute("data", "test"); //key, value 구조
        return "hello"; //대상 화면(뷰) 이름(="hello.html")
        //스프링 부트 thymeleaf 기본 매핑 : 'resources/templates/' + { } + '.html'
    }
}
