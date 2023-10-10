package com.vito.quimica.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vito.quimica.model.DetalleOrden;
import com.vito.quimica.model.Orden;
import com.vito.quimica.model.Producto;
import com.vito.quimica.model.Usuario;
import com.vito.quimica.service.IDetalleOrdenService;
import com.vito.quimica.service.IOrdenService;
import com.vito.quimica.service.IUsuarioService;
import com.vito.quimica.service.ProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private IDetalleOrdenService detalleOrdenService; 

	private final Logger logger = LoggerFactory.getLogger(HomeController.class);

	// para almacenar los detalles
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

	// datos
	Orden orden = new Orden();

	@Autowired
	private ProductoService productoService;

	@GetMapping("")
	public String home(Model model, HttpSession session) {
		
		logger.info("Sesión del usuario: {}", session.getAttribute("idusuario"));

		model.addAttribute("productos", productoService.findAll());

		
		//sersion
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		return "usuario/home";
	}

	@GetMapping("/productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {

		logger.info("id enviado como parametro {}", id);

		Producto producto = new Producto();

		Optional<Producto> productoOptional = productoService.get(id);

		producto = productoOptional.get();

		model.addAttribute("producto", producto);

		return "usuario/productohome";
	}

	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {

		DetalleOrden detalleOrden = new DetalleOrden();

		Producto producto = new Producto();

		Double sumaTotal = 0.0;

		Optional<Producto> optionalProducto = productoService.get(id);

		logger.info("Producto agregado {}", optionalProducto.get());
		logger.info("Producto cantidad {}", cantidad);

		producto = optionalProducto.get();

		detalleOrden.setNombre(producto.getDescripcion());
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);

		// validar agregar
		Integer idProducto = producto.getId();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);
		if (!ingresado) {
			detalles.add(detalleOrden);
		}

		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		orden.setTotal(sumaTotal);

		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}

	@GetMapping("/delete/cart{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {

		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();

		for (DetalleOrden detalleOrden : detalles) {
			if (detalleOrden.getProducto().getId() != id) {
				ordenesNueva.add(detalleOrden);
			}
		}

		detalles = ordenesNueva;

		Double sumaTotal = 0.0;

		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}

	@GetMapping("/getCart")
	public String getCart(Model model) {

		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "/usuario/carrito";
	}
	

	@GetMapping("/order")
	public String order(Model model, HttpSession session) {
		
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("usuario", usuario);
		return "usuario/resumenorden";
		
	}

	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session) {
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		
		orden.setUsuario(usuario);
		ordenService.save(orden);
		
		for(DetalleOrden dt: detalles) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}
		
		orden = new Orden();
		detalles.clear();
		
		
		return "redirect:/";
	}
	
	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre, Model model) {
		logger.info("Nombre del prd: {}", nombre);
		
		List<Producto> productos = productoService.findAll().stream().filter(
				p -> p.getDescripcion().contains(nombre)).collect(Collectors.toList());
		
		model.addAttribute("productos",productos);
		return "usuario/home";
	}
	
}
