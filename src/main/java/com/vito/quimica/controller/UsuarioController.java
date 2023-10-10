package com.vito.quimica.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vito.quimica.model.Orden;
import com.vito.quimica.model.Usuario;
import com.vito.quimica.service.IOrdenService;
import com.vito.quimica.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("usuario")
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	@Autowired
	private BCryptPasswordEncoder ec;

	@GetMapping("/registro")
	public String create() {

		return "usuario/registro";
	}

	@PostMapping("/save")
	public String save(Usuario usuario, Model model, SessionStatus status) {
		
		
		// Optional<Usuario> user = usuarioService.findByUsername(usuario.getNombre());
		 //Optional<Usuario> mail = usuarioService.findByUsername(usuario.getEmail());
		 
		logger.info("Usuario registrado: {}", usuario);

		usuario.setTipo("ROLE_USER");
		usuario.setPassword(ec.encode(usuario.getPassword()));

		usuario.setEnabled(true);
	

		usuarioService.save(usuario);

		model.addAttribute("success",
				"El usuario: ".concat(usuario.getNombre()).concat(" se ha registrado correctamente."));

		status.setComplete();
		return "redirect:/usuario/login";

	}

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error, Model model, Principal principal,
			RedirectAttributes flash) {

		if (error != null) {
			model.addAttribute("error", "Usuario o contraseña incorrecta, verifique los datos e intente nuevamente");
		}

		return "usuario/login";
	}

	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		logger.info("Usuario acceso: {}", usuario);

		// Optional<Usuario> user = usuarioService.findByUsername(usuario.getNombre());

		// n@mail.com

		// logger.info("Usuario db: {}", user.get());
		// validamos el email
		// if (user.isPresent()) {
		// session.setAttribute("idusuario", user.get().getId());
		/*
		 * if (user.get().getTipo().equals("ADMIN")) { System.out.println("Holi");
		 * return "redirect:/administrador"; } else { return "redirect:/"; } } else {
		 */
		// }

		return "redirect:/";
	}

	@GetMapping("/compras")
	public String obtenerCompras(Model model, HttpSession session) {
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		List<Orden> ordenes = ordenService.findByUsuario(usuario);

		model.addAttribute("ordenes", ordenes);
		return "usuario/compras";
	}

	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model) {

		Optional<Orden> orden = ordenService.findById(id);

		model.addAttribute("detalles", orden.get().getDetalle());
		model.addAttribute("sesion", session.getAttribute("idusuario"));

		return "/usuario/detallecompra";
	}

	@PostMapping("/cerrar")
	public String cerrarSesion(HttpSession session) {
		session.removeAttribute("idusuario");

		return "redirect:/";
	}

}
