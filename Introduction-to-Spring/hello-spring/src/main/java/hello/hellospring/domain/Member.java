package hello.hellospring.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity //jpa가 관리하는 엔티티라는 의미
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //db가 알아서 생성해주는 것을 아이덴티티라고한다.
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

