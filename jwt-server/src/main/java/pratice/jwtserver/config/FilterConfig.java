package pratice.jwtserver.config;

//시큐리티 필터 체인에 필터를 거는것이아니라 필터를 직접 만드는 것!

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pratice.jwtserver.filter.MyFilter1;
import pratice.jwtserver.filter.MyFilter2;

//필터의 경우 시큐리티 필터체인을 걸지 않고 해당 방법처럼 별도로 필터를 등록할 수 있다.
//시큐리티 필터 체인이 별도로 만든 필터보다 먼저 실행된다. 그래서 별도의 설정으로 실행순서를 변경할수 있다.
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1() {
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*");
        bean.setOrder(0);//낮은 번호가 필터중에서 가장 먼저 실행됨.
        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*");
        bean.setOrder(1);//낮은 번호가 필터중에서 가장 먼저 실행됨.
        return bean;
    }
}
