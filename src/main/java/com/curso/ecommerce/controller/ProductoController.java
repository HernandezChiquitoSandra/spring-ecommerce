package com.curso.ecommerce.controller;

import java.util.Optional;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoService productoService;
	
	//mostrar productos
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoService.finAll());
		return "productos/show";
	}
	
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	//se va a mostrar en la base de datos
	@PostMapping("/save")
	public String save(Producto producto) {
		LOGGER.info("Este es el onjecto producto {}", producto);
		Usuario u = new Usuario(1,"","","","","","","");
		producto.setUsuario(u);
		
		productoService.save(producto);
		return "redirect:/productos";
	}
	
	//metodo para llevar datos a actualizar
	@GetMapping("/edit/{id}")
	public String adit(@PathVariable Integer id, Model model) {
		Producto producto = new Producto();
		Optional<Producto> optionalProducto=productoService.get(id);
		producto= optionalProducto.get();
		
		LOGGER.info("ProductoBuscado: {}",producto);
		model.addAttribute("producto", producto);
		
		return "productos/edit";
	}
	
	
	//actualizar
	@PostMapping("/update")
	public String update(Producto producto) {
		productoService.update(producto);
		return "redirect:/productos";
	}

}
