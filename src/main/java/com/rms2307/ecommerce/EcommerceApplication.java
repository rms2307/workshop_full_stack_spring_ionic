package com.rms2307.ecommerce;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rms2307.ecommerce.domain.Categoria;
import com.rms2307.ecommerce.domain.Cidade;
import com.rms2307.ecommerce.domain.Cliente;
import com.rms2307.ecommerce.domain.Endereco;
import com.rms2307.ecommerce.domain.Estado;
import com.rms2307.ecommerce.domain.Produto;
import com.rms2307.ecommerce.domain.enums.TipoCliente;
import com.rms2307.ecommerce.repositories.CategoriaRepository;
import com.rms2307.ecommerce.repositories.CidadeRepository;
import com.rms2307.ecommerce.repositories.ClienteRepository;
import com.rms2307.ecommerce.repositories.EnderecoRepository;
import com.rms2307.ecommerce.repositories.EstadoRepository;
import com.rms2307.ecommerce.repositories.ProdutoRepository;

@SpringBootApplication
public class EcommerceApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritorio");

		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);

		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));

		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));

		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));

		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");

		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		Cidade c4 = new Cidade(null, "Monte Verde", est1);

		est1.getCidades().addAll(Arrays.asList(c1, c4));
		est2.getCidades().addAll(Arrays.asList(c2, c3));

		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3, c4));

		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@email.com", "36378912377", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("12341234", "4567845678"));
		Cliente cli2 = new Cliente(null, "Jose Silva", "jose@email.com", "19078912459", TipoCliente.PESSOAFISICA);
		cli2.getTelefones().addAll(Arrays.asList("45991100", "954338797"));

		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 303", "Jardim", "09234904", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Mattos", "105", "Sala 800", "Centro", "12334904", cli1, c2);
		Endereco e3 = new Endereco(null, "Avenida Barros", "1009", "Casa 01", "Jardim Ana", "09440392", cli2, c2);

		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
		cli2.getEnderecos().addAll(Arrays.asList(e3));

		clienteRepository.saveAll(Arrays.asList(cli1, cli2));

		enderecoRepository.saveAll(Arrays.asList(e1, e2, e3));
	}

}
