//package com.human.dalligo.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class GoodsSecurityConfig {
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	    http
//	        .csrf(csrf -> csrf.disable())
//	        .authorizeHttpRequests(auth -> auth
//	                .requestMatchers("/store/admin/orders").authenticated()
//	                .anyRequest().permitAll()
//	        )
//	        .formLogin(form -> form
//	                .loginPage("/goodsadminlogin")
//	                .loginProcessingUrl("/goodsadminlogin")
//	                .permitAll()
//	        )
//	        .logout(logout -> logout
//	                .logoutUrl("/logout")
//	                .logoutSuccessUrl("/")   // 🔥 로그아웃 후 메인으로 이동
//	                .invalidateHttpSession(true)
//	                .deleteCookies("JSESSIONID")
//	                .permitAll()
//	            );
//
//	    return http.build();
//	}
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // 인메모리 사용자
//    @Bean
//    public UserDetailsService users() {
//        UserDetails user = User.builder()
//                .username("admin1")
//                .password(passwordEncoder().encode("admin1"))
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
//}