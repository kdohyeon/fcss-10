package fast.campus.fcss10.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        UserDetails danny = User.withUsername("danny.kim")
                .password("12345")
                .roles("ADMIN")
                .build();

        UserDetails steve = User.withUsername("steve.kim")
                .password("12345")
                .roles("MANAGER")
                .build();

        manager.createUser(danny);
        manager.createUser(steve);

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic(Customizer.withDefaults());

//        httpSecurity.authorizeHttpRequests(c -> c.requestMatchers("/product/{code:^[0-9]*$}")
//                .permitAll());
//        httpSecurity.authorizeHttpRequests(c -> c.anyRequest().denyAll());

        httpSecurity.authorizeHttpRequests(c -> c.requestMatchers(HttpMethod.GET, "/api/v1/hello")
                .authenticated()); // HTTP GET 방식으로 /api/v1/hello 를 요청하면 앱이 사용자를 인증해야 함
        httpSecurity.authorizeHttpRequests(c -> c.requestMatchers(HttpMethod.POST, "/api/v1/hello")
                .permitAll()); // HTTP POST 방식으로 /api/v1/hello 를 요청하면 모두 허용함
        httpSecurity.authorizeHttpRequests(c -> c.requestMatchers("/api/v1/hello/**")
                .authenticated()); // /api/v1/hello/** 식은 접두사 /api/v1/hello 가 붙은 모든 경로를 의미함
        httpSecurity.authorizeHttpRequests(c -> c.anyRequest()
                .denyAll()); // 나머지 요청은 모두 거부함

        httpSecurity.csrf(AbstractHttpConfigurer::disable); // HTTP POST 방식으로 /api/h1/hello 를 호출할 수 있도록 CSRF 비활성화
        return httpSecurity.build();
    }
}
