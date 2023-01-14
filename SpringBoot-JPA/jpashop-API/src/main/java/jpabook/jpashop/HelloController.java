package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {   //컨트롤러에서 데이터를 실어서 뷰에 넘길 수 있다.
        model.addAttribute("data", "hello!!!");
        return "hello"; //hello.html로 가게된다.
    }
}
