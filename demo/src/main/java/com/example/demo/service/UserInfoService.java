package com.example.demo.service;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.data.UserInfoEntity;

@Scope("prototype")
@Service
public class UserInfoService implements UserDetailsService {
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
System.out.println("■■■■■■＃＃＃＃＃＃＃％％％％％％％");
		if (username == null || "".equals(username)) {
			throw new UsernameNotFoundException("Username is empty");
		}

		UserInfoEntity userInfo = this.getUserInfo();
		if (userInfo == null) {
			throw new UsernameNotFoundException("User not found for name: " + username);
		}
		return userInfo;
	}

	private UserInfoEntity getUserInfo() {
		UserInfoEntity info = new UserInfoEntity();
		info.setUsername("");
		info.setEmail("");
		info.setPassword("");
		return info;
	}

}
