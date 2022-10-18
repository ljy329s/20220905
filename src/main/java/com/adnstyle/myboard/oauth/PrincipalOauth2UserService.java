package com.adnstyle.myboard.oauth;

import com.adnstyle.myboard.auth.PrincipalDetails;
import com.adnstyle.myboard.model.domain.JyUser;
import com.adnstyle.myboard.model.repository.JyUserRepository;
import com.adnstyle.myboard.model.service.JyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/*
가져온 정보를 가지고 회원가입하는법
* username = 구글로그인으로 가져온 sub을 google_sub으로 아이디로 사용 google_sub(id) 중복될일이 없다
* password = 우리 서버만의 겟인데어를 암호화해서입력( 우리 사이트의 아이디 비밀번호로 로그인하는게 아니라 구글을 통해서 로그인하기 때문에 사실null만 아니면 상관없다함)
* email = 구글에서 가져온 이메일 그대로
* role = "ROLE_USER" 근데 똑같이 ROLE_USER로 주면 사이트에서 로그인한건지 소셜 로그인한 사용자인지 알수 없기에
jyUser 클래스에 provider과 providerId를 추가해준다
provider = "google"
providerId = super.loadUser(userRequest).getAttributes())를 통해 가져온 sub을 넣어줌
이렇게 받은 정보로 회원가입을 강제로 진행할것
* */
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final JyUserRepository jyUserRepository;
    private final JyUserService jyUserService;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    //구글로그인한 정보가 userRequest여기에 들어온다
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다
    @Override//loadUser입력해서 자동으로 뜨는 오버라이드하기 loadUser에서 후처리가 된다
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration: " + userRequest.getClientRegistration());//Registration으로 어떤 OAuth로 로그인했는지 확인가능.
        System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue());//우리는 getAccessToken 필요없음 super.loadUser(userRequest).getAttributes()여기에 필요한 사용자 정보가 다 있기 때문

        OAuth2User oAuth2User = super.loadUser(userRequest);
        //구글로그인 버튼을 클릭하면 구글로그인창이 뜨고 여기서 로그인을 진행하면 코드를 리턴받는다.(OAuth-Client라이브러리가 받아줌)-코드를 통해서 AcessTocken을 요청
        //userRequest정보로 회원 프로필을 받아야함(이때 사용하는 함수가 loadUser함수).이 함수로 구글로부터 회원프로필을 받아준다.
        System.out.println("getAttributes: " + oAuth2User.getAttributes());

        //회원가입을 강제로 진행
        // String provider =userRequest.getClientRegistration().getClientId();//google
        String provider = userRequest.getClientRegistration().getClientName();//google
        String providerId = oAuth2User.getAttribute("sub");
        String userId = provider + "_" + providerId;//google_sub(sub내용) 이렇게되면 userId가 충돌날일이 없다
        String password = bCryptPasswordEncoder.encode("비밀번호");//OAuth방식에는 크게 의미가 없지만 그냥 만들어봄
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";
        String username = oAuth2User.getAttribute("name");

        //이미 가입되어있는데 또 가입하면 안되니까 해당아이디로 가입이 되어있는지 확인해줘야함
        JyUser jyUser = new JyUser();
        if (jyUserRepository.selectUser(userId) == null) {//아이디 조회하는 메서드
            jyUser.setUserId(userId);
            jyUser.setUserName(username);
            jyUser.setUserPw(password);
            jyUser.setUserEmail(email);
            jyUser.setRole(role);
            jyUser.setProvider(provider);
            jyUser.setProviderId(providerId);

            jyUserService.insertNewScUser(jyUser); //회원가입하는메서드
            System.out.println(jyUser);

        }
        else {
            jyUser=jyUserRepository.selectScUser(userId);
        }
        return new PrincipalDetails(jyUser, oAuth2User.getAttributes());
        //PrincipalDetails세션안에 들어감

    }
}




