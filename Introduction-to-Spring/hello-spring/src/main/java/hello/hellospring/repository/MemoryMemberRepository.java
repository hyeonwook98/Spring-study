package hello.hellospring.repository;
import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence); // 컴퓨터에 저장되는 id값, primary key auto_increments 라고 생각하기
        store.put(member.getId(), member); // store, 맵에 값들을 저장
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) { // store에서 아이디 값을 꺼낸다고 생각
        return Optional.ofNullable(store.get(id)); // 조회되는 값이 없어 null일때를 대비해 Optional로 감싸준다
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream() // loop로 돌린다.
                .filter(member -> member.getName().equals(name))
                /* 루프돌면서 하나라도 입력받은 name과 member.getName()이 같은게 있는 경우 return해준다. */
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }
}

