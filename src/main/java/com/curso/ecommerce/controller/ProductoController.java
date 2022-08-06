package com.curso.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.ProductoService;
import com.curso.ecommerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoService productoService;
	@Autowired
	private UploadFileService upload;
	
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
	public String save(Producto producto,@RequestParam("img") MultipartFile file) throws IOException {
		LOGGER.info("Este es el onjecto producto {}", producto);
		Usuario u = new Usuario(1,"","","","","","","");
		producto.setUsuario(u);
		
		//imagen
		if(producto.getId()==null) {//cuando se crea producto
			String nombreImagen= upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}else {
			
		}
		
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
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		Producto p=new Producto();
		p=productoService.get(producto.getId()).get();//se obtiene la misma imagen y se guarda
		
		
		if(file.isEmpty()) {//editamos el producto pero no la imagen
			producto.setImagen(p.getImagen());
		}else {//cuando se edita tambien la imagen
			
			
			
			//eliminar cuando no sea imagen por defecto
			if(p.getImagen().equals("defaul.jpg")) {
				upload.deleteImage(p.getImagen());
			}
			
			String nombreImagen= upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);
		return "redirect:/productos";
	}
	
	//eliminar
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		Producto p = new Producto();
		p=productoService.get(id).get();
		
		//eliminar cuando no sea imagen por defecto
		if(p.getImagen().equals("defaul.jpg")) {
			upload.deleteImage(p.getImagen());
		}
		
		productoService.delete(id);
		return "redirect:/productos";
		
	}

}
