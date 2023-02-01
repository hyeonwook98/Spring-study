package pratice.jwtserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import pratice.jwtserver.config.jwt.JwtAuthenticationFilter;
import pratice.jwtserver.config.jwt.JwtAuthorizationFilter;
import pratice.jwtserver.filter.MyFilter3;
import pratice.jwtserver.repository.UserRepository;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션을 사용하지 않겠다. -> jwt토큰방식할때 사용하는 설정임
                .and()
                .formLogin().disable() //jwt토큰을 사용한 서버니까 폼로그인 형식으로 id,pw을 받지않는다.
                .httpBasic().disable() //매 요청시마다 id,password 를 헤더에 담고 가는 방식인 httpBasic을 사용안하겠다. -> 이 방법은 id,pw 암호화가 안되서 노출이 될수있음. 그래서 https를 사용
                .apply(new MyCustomDsl()) // 커스텀 필터 등록
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();

        return http.build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())//CorsConfig에서 만든 필터정보를 넣어준다. 이로써 모든 요청이 이 필터를 거친다.
                    .addFilter(new JwtAuthenticationFilter(authenticationManager)) //초기 로그인 시 인증하는 필터 = 인증
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository)); //로그인 후 토큰 검증하는 필터 = 인가
        }
    }
}
