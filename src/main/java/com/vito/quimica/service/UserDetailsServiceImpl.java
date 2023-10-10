package com.vito.quimica.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vito.quimica.model.Usuario;

import jakarta.servlet.http.HttpSession;

@Service("UserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	HttpSession session;

	private Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Empieza el username");
		Optional<Usuario> optionalUser = usuarioService.findByUsername(username);
		
		if(optionalUser.isEmpty()) {
			log.info("No tiene roles");
		}
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
			authorities.add(new SimpleGrantedAuthority(optionalUser.get().getTipo()));
		
		
		if(optionalUser.isPresent()) {
			
			log.info("Entró al if y el id {}", optionalUser.get().getId());
			session.setAttribute("idusuario", optionalUser.get().getId());
			Usuario usuario = optionalUser.get();
			return new User(usuario.getNombre(),
					usuario.getPassword(), 
					usuario.isEnabled(),
					true, 
					true, 
					true, 
					authorities);	
			
		}else {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
		
	}

}
