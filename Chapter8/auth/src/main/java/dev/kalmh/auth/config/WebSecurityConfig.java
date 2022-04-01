package dev.kalmh.auth.config;

import dev.kalmh.auth.infra.CustomUserDetailService;
import dev.kalmh.auth.infra.NaverOAuth2Service;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity //security 의존성을 조작할 준비가 되었음을 스프링 IoC에 알려줌
//기본 security 설정들을 확인하기 위해 extends WebSecurityConfigurerAdapter
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final NaverOAuth2Service naverOAuth2Service;

    public WebSecurityConfig(
            PasswordEncoder passwordEncoder,
            CustomUserDetailService customDetailService,
            NaverOAuth2Service naverOAuth2Service
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = customDetailService;
        this.naverOAuth2Service = naverOAuth2Service;
    }
    //사용자 요청 메소드 관리를 위한 설정
    // - 설정이 안되어 있다면 기본적으로 default security password를 사용하게 된다.(터미널 참고)
    // - 사용자 비밀번호 일치 확인, 로그인에 대한 부분이 여기서 일어나게 된다.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService);
    }

    //HttpSecurity 객체 : 전체 어플리케이션을 관리하는 설정에 대한 객체. filter와 유사함.
    // - http 설정에 원하는 값을 함수로 추가할 수 있다.
    // - 보안 설정 조작 가능
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(
                        "/home/**",
                        "/user/signup/**",
                        "/",
                        "/css/**",
                        "/images/**",
                        "/js/**")
                .anonymous()
                .anyRequest()
                .authenticated()
            .and()
                .formLogin()
                .loginPage("/user/login")
                .defaultSuccessUrl("/home")
                .permitAll()
            .and()
                .logout()
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/home")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll()
            .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(this.naverOAuth2Service)
                .and()
                .defaultSuccessUrl("/home")
            .and()
                .oauth2Client();

    }
}
