package com.rms2307.ecommerce.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rms2307.ecommerce.domain.Cidade;
import com.rms2307.ecommerce.domain.Cliente;
import com.rms2307.ecommerce.domain.Endereco;
import com.rms2307.ecommerce.domain.enums.Perfil;
import com.rms2307.ecommerce.domain.enums.TipoCliente;
import com.rms2307.ecommerce.dto.ClienteDTO;
import com.rms2307.ecommerce.dto.ClienteNewDTO;
import com.rms2307.ecommerce.dto.EnderecoNewDTO;
import com.rms2307.ecommerce.repositories.ClienteRepository;
import com.rms2307.ecommerce.repositories.EnderecoRepository;
import com.rms2307.ecommerce.security.UserSS;
import com.rms2307.ecommerce.services.exceptions.AuthorizationException;
import com.rms2307.ecommerce.services.exceptions.DataIntegrityException;
import com.rms2307.ecommerce.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	BCryptPasswordEncoder pe;

	@Autowired
	private ClienteRepository repo;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private ImageService imageService;

	@Value("${img.prefix.client.profile}")
	private String prefix;

	@Value("${img.size}")
	private Integer size;

	public Cliente findById(Integer id) {
		// Verifica se é um usuario ADMIN, ou se o cliente buscado é o logado.
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	public List<Cliente> findAll() {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN)) {
			throw new AuthorizationException("Acesso negado");
		}
		return repo.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN)) {
			throw new AuthorizationException("Acesso negado");
		}
		return repo.findAll(pageRequest);
	}

	public Cliente findByEmail(String email) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}
		Cliente obj = repo.findByEmail(email);
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado. Id: " + user.getId());
		}
		return obj;
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = findById(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		newObj.setTelefone(obj.getTelefone());
	}
	
	public void delete(Integer id) {
		Cliente obj = findById(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O Cliente " + obj.getNome() + " contém pedidos.");
		}
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null, objDTO.getTelefone());
	}

	public Cliente fromDTO(ClienteNewDTO objDTO) {
		Cliente cli = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(),
				TipoCliente.toEnum(objDTO.getTipo()), pe.encode(objDTO.getSenha()), objDTO.getTelefone());
//		Cidade cid = new Cidade(objDTO.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(),
				objDTO.getBairro(), objDTO.getCep(), cli, null);
		cli.getEnderecos().add(end);
		return cli;
	}
	
//	ENDEREÇOS
	
	public Endereco addEndereco(Endereco endereco) {
		return enderecoRepository.save(endereco);
	}
	
	public Endereco findEndById(Integer id) {
		Optional<Endereco> obj = enderecoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Endereco.class.getName()));
	}
	
	public Endereco updateEnd(Endereco obj) {
		Endereco newObj = findEndById(obj.getId());
		updateDataEnd(newObj, obj);
		return enderecoRepository.save(newObj);
	}
	
	private void updateDataEnd(Endereco newObj, Endereco obj) {
		newObj.setLogradouro(obj.getLogradouro());
		newObj.setNumero(obj.getNumero());
		newObj.setComplemento(obj.getComplemento());
		newObj.setBairro(obj.getBairro());
		newObj.setCep(obj.getCep());
		newObj.setCidade(obj.getCidade());
	}
	
	public void deleteEndereco(Integer id) {
		try {
			enderecoRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Erro ao remover endereço");
		}
	}

	public Endereco enderecoFromDTO(EnderecoNewDTO objDTO) {
		Cidade cid = new Cidade(objDTO.getCidade_id(), null, null);
		Cliente cli = new Cliente(objDTO.getCliente_id(), null, null, null, null, null, null);
		return new Endereco(objDTO.getId(), objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(),
				objDTO.getBairro(), objDTO.getCep(), cli, cid);
	}
	
//	IMAGENS

	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);
		String fileName = prefix + user.getId() + ".jpg";
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
////		URI uri = s3Service.uploadFile(multipartFile);
////		Cliente cliente = findById(user.getId());
////		cliente.setImageURL(uri.toString());
////		repo.save(cliente);
////		return uri;
	}

}
