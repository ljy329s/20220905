package com.adnstyle.myboard.auth;

import com.adnstyle.myboard.model.domain.JyUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

//시큐리티가 /login주소 요청이 오면 낚아채서 로그인을 진행시켜서 완료가 되면
//시큐리티 세션을 만들어준다. (Security ContextHolder)
//시큐리티 세션에 들어갈수 있는 정보는 Object타입 => Authentication타입 객체
//Authentication 안에는 User정보가 있어야함
//User오브젝트 타입 => UserDetails 타입 객체

//정리 시큐리티가 가지고 있는 Security Session에 정보를 저장해주는데 여기에 들어갈수있는 객체가
//Authentication객체로 정해져있다. Authentication안에는 user정보가 저장되는데
//user정보는 UserDetails타입이다

//UserDetails을 implements했기때문에 UserDetails==PrincipalDetails

@Getter
public class PrincipalDetails implements UserDetails, Serializable {//UserDetails상속받기

    private static final long serialVersionUID = 1L;

    private JyUser jyUser;

    public PrincipalDetails(JyUser jyUser) {
        this.jyUser = jyUser;
    }

    //해당 User의 권한을 리턴하는곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return jyUser.getRole();
            }
        });
        return collect;
    }

    //비밀번호리턴
    @Override
    public String getPassword() {
        return jyUser.getUserPw();
    }

    //아이디리턴
    @Override
    public String getUsername() {
        return jyUser.getUserId();
    }

    //계정이 만료되지 않았는지를 리턴한다
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //이 계정이 잠겼니? 아닐때 true
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //게정 사용이 오래됐니?너무 오래사용 한건 아니니? 아닐때 true
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화 되어있니? true
    @Override
    public boolean isEnabled() {
        return true;
    }
}

