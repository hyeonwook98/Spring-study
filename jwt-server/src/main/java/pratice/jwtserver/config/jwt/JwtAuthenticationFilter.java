package pratice.jwtserver.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pratice.jwtserver.config.auth.PrincipalDetails;
import pratice.jwtserver.model.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음.
// /login 요청해서 username,password 전송하면 UsernamePasswordAuthenticationFilter가 동작함.
// 근데 formLogin().disable()했기때문에 추가로 다시 필터등록을 해주어야함.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter 로그인 시도중");

        //1. username, password 받아서
        try {
//            BufferedReader br = request.getReader();
//
//            String input = null;
//            while ((input = br.readLine()) != null) {
//                System.out.println(input);
//            }
            ObjectMapper om = new ObjectMapper();//JSON 데이터를 파싱해줌.
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println("user = " + user);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());//토큰 만들기

            //PrincipalDetailsService의 loadUserByUsername 함수가 실행된 후 정상이면 authentication이 리턴됨. authentication 로그인 정보가 담김.
            //DB에 있는 username과 password가 일치한다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료됨 : " + principalDetails.getUsername()); //로그인이 잘 되었다는 뜻!

            //authentication 객체가 session 영역에 저장을 해야되고 그 방법이 return 해주면 된다.
            //리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는것이다.
            //굳이 JWT토큰을 사용하면서 세션을 만들 이유가 없다. 근데 단지 권한 처리 때문에 session에 넣어준다.
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //attemptAuthentication실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행된다.
    //여기서 JWT토큰을 만들어서 request요청한 사용자에게 JWT토큰을 response 해주면된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
