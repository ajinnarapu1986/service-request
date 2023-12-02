package com.sr.user;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * @param : user
	 * @return : Integer
	 */
	@Override
	public Integer saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user = userRepo.save(user);
		return user.getId();
	}

	/**
	 * @param : usernameOrEmail
	 * @return : UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

		User user = userRepo.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new UsernameNotFoundException("User not exists by Username or Email"));

		Set<GrantedAuthority> authorities = user.getRoles().stream().map((role) -> new SimpleGrantedAuthority(role))
				.collect(Collectors.toSet());

		return new org.springframework.security.core.userdetails.User(usernameOrEmail, user.getPassword(), authorities);

	}

}