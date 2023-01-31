package practice.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import practice.springsecurity.model.User;
import practice.springsecurity.repository.UserRepository;

@Controller //View를 리턴하겠다.
public class IndexController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    //localhost:8080
    //localhost:8080/
    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    //스프링 시큐리티가 해당주소를 낚아챔 - SecurityConfig 파일 생성 후 작동안함.
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println("user = " + user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        //회원가입 잘됨. 비밀번호 ex)1234 -> 패스워드가 암호화가 안되어 있어서 시큐리티로 로그인할 수 없다. 그래서 위에 암호화를 따로해주어야함.
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")//이 어노테이션을 하면 ADMIN권한만 들어갈 수 있게된다.
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")//@Secured랑 비슷한 어노테이션 postAuthorize도 있음
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
