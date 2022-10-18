package com.adnstyle.myboard.auth;

import com.adnstyle.myboard.model.domain.JyUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//시큐리티가 /login주소 요청이 오면 낚아채서 로그인을 진행시켜서 완료가 되면
//시큐리티 세션을 만들어준다. (Security ContextHolder)
//시큐리티 세션에 들어갈수 있는 정보는 Object타입 => Authentication타입 객체뿐
//Authentication 안에는 User정보가 있어야함
//User오브젝트 타입 => UserDetails 타입 객체

//정리 시큐리티가 가지고 있는 Security Session에 정보를 저장해주는데 여기에 들어갈수있는 객체가
//Authentication객체로 정해져있다. Authentication안에는 user정보가 저장되는데
//user정보는 UserDetails타입이다

//UserDetails을 implements했기때문에 UserDetails==PrincipalDetails

//UserDetails, OAuth2User 두개의 타입을 implements하여 일반로그인이나 소셜로그인이나 상관없이 세션등록에 사용

//
/*
Authentication타입 객체에 들어갈수있는 타입은 두개뿐
OAuth2User,UserDetails
우리가 회원가입을 하게되면 UserObject가 필요한데(나는 JyUser) OAuth2User,UserDetails은 userObject가 없다
그래서 PrincipalDetails에서 UserDetails를 implements 하고 userObject를 품음
그래서 UserDetails를 PrincipalDetails로 대체함
그런데 소셜로그인을 하면 OAuth2User을 사용해애만하고 일반로그인을 하면 PrincipalDetail을 사용해야해서 번거로움이 발생
OAuth2User도 같이 implements하여 userObject를 사용가능 그래서 이젠 UserDetails, OAuth2User을 implements하고
user객체를 품고있는 부모클래스 PrincipalDetails만 사용하면된다.
계속 중복적인 설명인데 잘 정리해놓자
* */
@Getter
public class PrincipalDetails implements UserDetails, OAuth2User, Serializable {//UserDetails상속받기

    private static final long serialVersionUID = 1L;

    private JyUser jyUser;

    private Map<String,Object> attributes;

    //일반로그인시 사용
    public PrincipalDetails(JyUser jyUser) {
        this.jyUser = jyUser;
    }

    //OAuth 소셜로그인시 사용
    public PrincipalDetails(JyUser jyUser, Map<String,Object> attributes) {//attributes정보를가지고 jyUser를 만든다
        this.jyUser = jyUser;
        this.attributes=attributes;
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

    //OAuth2User을 implements하고 오버라이딩하면 나오는 두개의 메서드
    @Override//sub,name,give_name,family_name,picture,email,email_verified,locale정보를 가져옴<String,Object>
    public Map<String, Object> getAttributes() {//sub:유저의 대표키(primarykey)
        return attributes;//필드에 리턴
    }
    @Override//잘쓰지 않음
    public String getName() {
        return null;
    }
}

