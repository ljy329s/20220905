package com.adnstyle.myboard.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration//설정클래스임을 알려줌
@EnableWebSecurity//시큐리티 활성화 스프링 시큐리티 필터(SecurityConfig클래스를 말함)가 스프링 필터체인에 등록됨.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
//Secured어노테이션 활성화 특정 컨트롤러 메서드에 간단하게 권한부여 가능, preAuthorize 어노테이션 활성화
public class SecurityConfig {

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
                .defaultSuccessUrl("/user/jyHome");//로그인이 성공하면 갈 경로

        return http.build();
    }

    @Bean//Bean 해당메서드의 리턴되는 오브젝트를 IoC로 등록해준다
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
//403에러가 뜨는건 접근권한이 없다는뜻


//    @Bean
//    public PasswordEncoder passwordEncoder(){//스프링부트 2.0 이후 버전부터는 반드시 PasswordEncoder를 지정해야함
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/error",
//                        "/favicon.ico",
//                        "/**/*.png",
//                        "/**/*.gif",
//                        "/**/*.svg",
//                        "/**/*.jpg",
//                        "/**/*.html",
//                        "/**/*.css",
//                        "/**/*.js",
//                        "/**/*.jsp", "/oauth/**","/**").permitAll()
//                .anyRequest().authenticated()
//
//                .and()
//                .csrf()
//                .disable()
//                .exceptionHandling();
//
//        return http.build();
//    }

//}
//package com.adnstyle.myboard.auth;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration//설정클래스임을 알려줌
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig{
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){//스프링부트 2.0 이후 버전부터는 반드시 PasswordEncoder를 지정해야함
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/error",
//                        "/favicon.ico",
//                        "/**/*.png",
//                        "/**/*.gif",
//                        "/**/*.svg",
//                        "/**/*.jpg",
//                        "/**/*.html",
//                        "/**/*.css",
//                        "/**/*.js",
//                        "/**/*.jsp", "/oauth/**","/**").permitAll()
//                .anyRequest().authenticated()
//
//                .and()
//                    .csrf()
//                    .disable()
//                    .exceptionHandling();
//
//        return http.build();
//    }
//
//}
