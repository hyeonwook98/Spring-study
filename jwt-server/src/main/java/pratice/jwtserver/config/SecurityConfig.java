package pratice.jwtserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import pratice.jwtserver.filter.MyFilter1;
import pratice.jwtserver.filter.MyFilter3;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class); //시큐리티 필터가 먼저 적용된다.
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션을 사용하지 않겠다. -> jwt토큰방식할때 사용하는 설정임
                .and()
                .addFilter(corsFilter) //CorsConfig에서 만든 필터정보를 넣어준다. 이로써 모든 요청이 이 필터를 거친다.
                .formLogin().disable() //jwt토큰을 사용한 서버니까 폼로그인 형식으로 id,pw을 받지않는다.
                .httpBasic().disable() //매 요청시마다 id,password 를 헤더에 담고 가는 방식인 httpBasic을 사용안하겠다. -> 이 방법은 id,pw 암호화가 안되서 노출이 될수있음. 그래서 https를 사용
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
}
