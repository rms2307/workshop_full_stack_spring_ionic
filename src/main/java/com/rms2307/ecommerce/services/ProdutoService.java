package com.rms2307.ecommerce.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rms2307.ecommerce.domain.Categoria;
import com.rms2307.ecommerce.domain.Produto;
import com.rms2307.ecommerce.domain.enums.Perfil;
import com.rms2307.ecommerce.dto.ProdutoNewDTO;
import com.rms2307.ecommerce.repositories.CategoriaRepository;
import com.rms2307.ecommerce.repositories.ProdutoRepository;
import com.rms2307.ecommerce.security.UserSS;
import com.rms2307.ecommerce.services.exceptions.AuthorizationException;
import com.rms2307.ecommerce.services.exceptions.DataIntegrityException;
import com.rms2307.ecommerce.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repo;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.produto}")
	private String prefix;

	@Value("${img.size}")
	private Integer size;

	public List<Produto> findAll() {
		return repo.findAll();
	}

	public Page<Produto> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Produto findById(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
	}

	public Produto insert(ProdutoNewDTO objDTO) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN)) {
			throw new AuthorizationException("Acesso negado");
		}
		Produto obj = new Produto(objDTO.getId(), objDTO.getNome(), objDTO.getPreco());
		for (Integer cat_id : objDTO.getCategoria_id()) {
			Categoria cat = categoriaService.findById(cat_id);
			cat.getProdutos().add(obj);
			obj.getCategorias().add(cat);
			categoriaRepository.save(cat);
		}
		obj.setId(null);
		obj = repo.save(obj);
		return obj;
	}

	public Produto update(ProdutoNewDTO objDTO) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN)) {
			throw new AuthorizationException("Acesso negado");
		}
		Produto obj = new Produto(objDTO.getId(), objDTO.getNome(), objDTO.getPreco());
		Produto newObj = findById(objDTO.getId());
		updateData(newObj, obj);
		for (Integer cat_id : objDTO.getCategoria_id()) {
			Categoria cat = categoriaService.findById(cat_id);
			cat.getProdutos().add(newObj);
			newObj.getCategorias().add(cat);
			categoriaRepository.save(cat);
		}
		return repo.save(newObj);
	}

	private void updateData(Produto newObj, Produto obj) {
		newObj.setNome(obj.getNome());
		newObj.setPreco(obj.getPreco());
	}

	public void delete(Integer id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN)) {
			throw new AuthorizationException("Acesso negado");
		}
		findById(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Erro ao remover");
		}
	}

	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy,
			String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.findDistinctByNomeIgnoreCaseContainingAndCategoriasIn(nome, categorias, pageRequest);
	}
	
//	IMAGENS
	
	public URI uploadPicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN)) {
			throw new AuthorizationException("Acesso negado");
		}
		Integer produtoId = repo.produtoId();
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);
		String fileName = prefix + produtoId + ".jpg";
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
	}
	

}
