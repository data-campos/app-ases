package com.spin.ops.security;

import static java.util.Collections.emptyList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spin.ops.model.InterfacesJPA.RetornoLogin;
import com.spin.ops.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarios;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		RetornoLogin ret = usuarios.loginPorUsuario(username, "A");

		if (ret.getNm_pessoa_fisica().isEmpty()) {
			throw new UsernameNotFoundException("Bad credentials");
		}
		
		return new User(username, new BCryptPasswordEncoder().encode(ret.getDs_senha()) , emptyList());

	}
}
