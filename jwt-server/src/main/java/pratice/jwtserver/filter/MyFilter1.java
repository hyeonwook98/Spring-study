package pratice.jwtserver.filter;

import javax.servlet.*;
import java.io.IOException;
import java.util.logging.LogRecord;

public class MyFilter1 implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터1");
        chain.doFilter(request, response);
    }
}
