package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //readOnly = true를 하면 읽기(조회)하는데 있어 성능을 향상시킨다.
@RequiredArgsConstructor //final이라고 적힌 필드에 있는 것들을 알아서 생성자주입해준다.
public class MemberService {

    private final MemberRepository memberRepository;

    //회원 가입
    @Transactional // 쓰기(변경)이 발생하는 곳에는 readOnly가 영향을 주지않도록 설정한다. default가 false임
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}


