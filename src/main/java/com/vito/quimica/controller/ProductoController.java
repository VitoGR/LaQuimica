package com.vito.quimica.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.multipart.MultipartFile;

import com.vito.quimica.model.Categoria;
import com.vito.quimica.model.Marca;
import com.vito.quimica.model.Producto;
import com.vito.quimica.model.Usuario;
import com.vito.quimica.service.ICategoriaService;
import com.vito.quimica.service.IMarcaService;
import com.vito.quimica.service.IUsuarioService;
import com.vito.quimica.service.ProductoService;
import com.vito.quimica.service.UploadFileService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

	@Autowired
	private ProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private UploadFileService uploadFileService;

	@Autowired
	private ICategoriaService categoriaService;

	@Autowired
	private IMarcaService marcaService;

	@GetMapping("")
	public String show(Model model) {

		model.addAttribute("productos", productoService.findAll());
		return "/productos/show";
	}

	@GetMapping("/create")
	public String create(Model model) {
		
		
		Producto producto = new Producto();
		List<Categoria> catagoria = categoriaService.listaCategoria();
		List<Marca> marca = marcaService.listaMarcas();
		
		model.addAttribute("productos", producto);
		model.addAttribute("categorias", catagoria);
		model.addAttribute("marcas", marca);

		return "/productos/create";
	}

	@PostMapping("/save")
	public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {

		LOGGER.info("Obj Producto {}", producto);

		Usuario u = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		producto.setUsuario(u);

		if (producto.getId() == null) {
			String nombreImagen = uploadFileService.saveImage(file);
			producto.setImagen(nombreImagen);
		} else {

		}

		productoService.save(producto);

		return "redirect:/productos";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {

		Producto producto = new Producto();

		Optional<Producto> optionalProducto = productoService.get(id);
		// optional es lo que no devuelve cuando hacemos una busqueda pro
		

		producto = optionalProducto.get();

		LOGGER.info("Producto buscado {}", producto);

		model.addAttribute("producto", producto);



		return "productos/edit";
	}

	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {

		Producto p = new Producto();
		p = productoService.get(producto.getId()).get();

		if (file.isEmpty()) {

			producto.setImagen(p.getImagen());
			// cuando editamos el producto pero no cambiamos la imagen
		} else {

			// elimina cuando no sea por defecto (img)
			if (p.getImagen().equals("default.png")) {
				uploadFileService.deleteImage(p.getImagen());
			}

			String nombreImagen = uploadFileService.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);

		return "redirect:/productos";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {

		Producto p = new Producto();

		p = productoService.get(id).get();

		// elimina cuando no sea por defecto (img)
		if (!p.getImagen().equals("default.png")) {
			uploadFileService.deleteImage(p.getImagen());
		}

		productoService.delete(id);

		return "redirect:/productos";
	}
}
