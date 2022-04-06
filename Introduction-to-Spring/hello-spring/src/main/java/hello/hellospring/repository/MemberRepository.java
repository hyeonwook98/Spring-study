package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);  // 리포지토리에 회원 저장
    Optional<Member> findById(Long id); // Id로 정보 찾기
    Optional<Member> findByName(String name); // 이름으로 정보 찾기
    List<Member> findAll(); // 현재까지 저장된 모든 정보 반환
}

