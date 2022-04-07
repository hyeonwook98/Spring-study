package hello.core.singleton;

public class SingletonService {

    //자바가 뜰 때 static영역에 싱글톤서비스라는 객체를 만들어서 instance에 넣어둔다
    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance() {
        return instance;
    }

    private SingletonService() {

    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출출");
   }
}
