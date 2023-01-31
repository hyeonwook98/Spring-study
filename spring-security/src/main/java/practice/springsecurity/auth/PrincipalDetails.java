package practice.springsecurity.auth;

//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인을 완료가 되면 시큐리티 session을 만들어준다.(Security ContextHolder)
//오브젝트 타입 => Authentication 타입 객체
//Authentication 안에 User 정보가 있어야 된다.
//User오브젝트타입 => UserDetails 타입 객체

//Security Session => Authentication => UserDetails

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import practice.springsecurity.model.User;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    //해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정이 만료되지 않았는지 리턴 (true: 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨있는지 않았는지 리턴. (true:잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호가 마료되지 않았는지 리턴한다. (true:만료안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화(사용가능)인지 리턴 (true:활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
