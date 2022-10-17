package com.adnstyle.myboard.auth;

import com.adnstyle.myboard.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//구글로그인이 완료된 후의 후처리가 필요함
// 1.코드받기(받았다는건 인증됐다는것) 2.엑세스토큰(코드를 통해서 엑세스 토큰을 받아야함 사용자 정보에 접근할수있는 권한이 생김)
// 3. 사용자 프로필 정보를 가져와서 4.정보를 토대로 회원가입을 자동으로 진행
//4-2 추가적으로 더 필요한 정보가 있다면 추가적인 정보를 얻는 회원가입창이 떠야한다 ex)쇼핑몰 집주소
@Configuration//설정클래스임을 알려줌
@EnableWebSecurity//시큐리티 활성화 스프링 시큐리티 필터(SecurityConfig클래스를 말함)가 스프링 필터체인에 등록됨.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
//Secured어노테이션 활성화 특정 컨트롤러 메서드에 간단하게 권한부여 가능, preAuthorize 어노테이션 활성화
public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();//csrf.비활성화
        http
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()//user로 들어오는 주소면 인증이 필요하다 / 인증만되면 다 사용할수있는!
                //.antMatchers("/manager/**").access("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")//manager로 들어왔을때 인증뿐 아니라 권한이 있는 사람만 접속 하게끔
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")//admin으로 들어왔을땐 ADMIN권한이 있는 사람만 접속이 가능하다.
                .anyRequest().permitAll()// 이 외의 요청은 모든권한 허용하겠다.
                    .and()//그리고? 조건추가?
                .formLogin()//권한이 필요한 경로에 접근했으면 로그인페이지로 이동시키겠다 스프링시큐리티에서 제공하는 인증방식
                    .loginPage("/loginForm")//사용자정의 로그인페이지 적어줌
                    .loginProcessingUrl("/login")// /login 주소로 호출하면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                    .defaultSuccessUrl("/user/userLogin", true)//로그인이 성공하면 갈 경로 ,true를 붙이면 무조건 성공시에 그 경로로 가게 한다. 안적으면 로그인성공시 누르고자한 경로로 가게된다
                    .and()
                .logout()//로그아웃관련한 설정하기(설정해주지 않으면 자동적으로 로그아웃후 로그인폼으로 이동)
                    .logoutUrl("/logout")//시큐리티의 기본 로그아웃 url(/logout)로 적어줬다, logoutUrl : 시큐리티에게 logout요청하기위한 url을 지정하는 메소드
                    .logoutSuccessUrl("/")//.logoutSuccessUrl:로그아웃이 성공하고 이동할 경로를 지정하는 메소드
                .and()
                .oauth2Login()//소셜로그인을 위해 적
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
        ;// 구글로그인이 완료되면 tip.코드를 받는게 아니라 , (엑세스 토큰+ 사용자프로필정보를 한번에 받음)
        return http.build();
    }

    @Bean//Bean 해당메서드의 리턴되는 오브젝트를 IoC로 등록해준다
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
