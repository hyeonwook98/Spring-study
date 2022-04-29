package hello.core.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClient implements InitializingBean, DisposableBean {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);

    }

    public void setUrl(String url) {
        this.url = url;
    }

    //서비스 시작시 호출
    public void connect() {
        System.out.println("url = " + url);
    }

    public void call(String message) {
        System.out.println("call: "+ url + "message = " +message);
    }

    //서비스 종료시 호출
    public void disconnect() {
        System.out.println("close: "+ url);
    }

    //의존관계 주입이 끝나고 호출되는 메서드
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        call("초기화 연결 메시지");
    }

    //빈이 소멸되기 직전에 호출되는 메서드
    @Override
    public void destroy() throws Exception {
        System.out.println("NetworkClient.destroy");
        disconnect();
    }
}
