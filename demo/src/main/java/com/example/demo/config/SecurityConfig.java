package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable().and().csrf().disable()
			.authorizeRequests()
				.antMatchers("/", "/login", "/login-error","login-ok").permitAll()
				.antMatchers("/member/**").hasRole("MEMBER")
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/cast/**").hasRole("CAST")
				.and()
			.formLogin()
				.loginProcessingUrl("/login")
				.loginPage("/loginForm")
				.failureUrl("/login-error")
				.defaultSuccessUrl("/login-ok")
				.usernameParameter("username")
				.passwordParameter("password")
				.and()
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.deleteCookies("JSESSIONID")
                .invalidateHttpSession(true).permitAll();
			;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		auth.inMemoryAuthentication()
//			.withUser("member").password(passwordEncoder.encode("member")).roles("MEMBER")
//			.and()
//			.withUser("admin").password(passwordEncoder.encode("admin")).roles("ADMIN")
//			.and()
			.withUser("2011578").password(passwordEncoder.encode("lancers")).roles("ADMIN")
			.and()
//			.withUser("cast").password(passwordEncoder.encode("cast")).roles("CAST")
//			.and()
		;
	}
}
