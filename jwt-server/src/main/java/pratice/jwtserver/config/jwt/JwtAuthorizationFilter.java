package pratice.jwtserver.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pratice.jwtserver.config.auth.PrincipalDetails;
import pratice.jwtserver.model.User;
import pratice.jwtserver.repository.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//시큐리티가 가지고 있는 filter중에서 BasicAuthenticationFilter 라는 것이 있다.
//권한이나 인증이 필요한 특정 주소를 요청했을 때 위 칲러틑 무조건 타게 되어있다.
//만약 인증이 필요한 주소가 아니라면 이 필터를 타지않는다!
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    //인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 타게된다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소 요청이 된다.");

        //Authorization 헤더 값 가져오기
        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
        System.out.println("jwtHeader = " + jwtHeader);


        //header가 올바른지 확인인
       if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        //JWT 토큰을 검증을 해서 정상적인 사용자인지 확인
        String jwtToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");

        // 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
        // 내가 SecurityContext에 직접접근해서 세션을 만들때 자동으로 UserDetailsService에 있는
        // loadByUsername이 호출됨.
        String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken)
                .getClaim("username").asString();

        //서명이 정상적으로 되었는지 확인
        if (username != null) {
            User user = userRepository.findByUsername(username);

            // 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해
            // 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장!
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principalDetails, // 나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                    null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
                    principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
