package com.rms2307.ecommerce.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rms2307.ecommerce.domain.Produto;
import com.rms2307.ecommerce.dto.ProdutoDTO;
import com.rms2307.ecommerce.dto.ProdutoNewDTO;
import com.rms2307.ecommerce.resources.utils.URL;
import com.rms2307.ecommerce.services.ProdutoService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService service;
	
	@ApiOperation(value = "Retorna um produto pelo Id")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Produto> findById(@PathVariable Integer id) {
		Produto obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@ApiOperation(value = "Retorna todos os produtos")
	@GetMapping
	public ResponseEntity<List<ProdutoDTO>> findAll() {
		List<Produto> list = service.findAll();
		List<ProdutoDTO> listDTO = list.stream().map(obj -> new ProdutoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}
		
	@ApiOperation(value = "Retorna produtos paginados")
	@GetMapping(value = "/search")
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(value = "nome", defaultValue = "") String nome,
			@RequestParam(value = "categorias", defaultValue = "") String categorias,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		String nomeDecoded = URL.decodeParam(nome);
		List<Integer> ids = URL.decodeIntList(categorias);
		Page<Produto> pages = service.search(nomeDecoded, ids, page, linesPerPage, orderBy, direction);
		Page<ProdutoDTO> pagesDTO = pages.map(obj -> new ProdutoDTO(obj));
		return ResponseEntity.ok().body(pagesDTO);
	}
	
	@ApiOperation(value = "Insere um produto")
	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody ProdutoNewDTO objDTO) {			
		Produto obj = service.insert(objDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@ApiOperation(value = "Atualiza um produto")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody ProdutoNewDTO objDTO, @PathVariable Integer id) {
		objDTO.setId(id);
		service.update(objDTO);
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Remove um produto")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Insere imagem de um produto")
	@PostMapping(value = "/picture")
	public ResponseEntity<Void> uploadPicture(@RequestParam(name = "file") MultipartFile file) {
		URI uri = service.uploadPicture(file);
		return ResponseEntity.created(uri).build();
	}
	
	@ApiOperation(value = "Atualiza imagem de um produto pelo Id")
	@PostMapping(value = "/picture/{id}")
	public ResponseEntity<Void> uploadPictureUpdate(@RequestParam(name = "file") MultipartFile file, @PathVariable Integer id) {
		URI uri = service.uploadPicture(file, id);
		return ResponseEntity.created(uri).build();
	}

}
