package practice.springsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //View를 리턴하겠다.
public class IndexController {

    //localhost:8080
    //localhost:8080/
    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }
}
