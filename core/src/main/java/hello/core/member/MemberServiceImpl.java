package hello.core.member;

public class MemberServiceImpl implements MemberService{
    //현재 인터페이스 뿐만아니라 구현체까지 의존한다. = 추상화와 구체화 모두 의존한다. DIP위반
    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
