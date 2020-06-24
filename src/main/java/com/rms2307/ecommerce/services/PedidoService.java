package com.rms2307.ecommerce.services;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.rms2307.ecommerce.domain.Cliente;
import com.rms2307.ecommerce.domain.ItemPedido;
import com.rms2307.ecommerce.domain.PagamentoComBoleto;
import com.rms2307.ecommerce.domain.Pedido;
import com.rms2307.ecommerce.domain.enums.EstadoPagamento;
import com.rms2307.ecommerce.repositories.ItemPedidoRepository;
import com.rms2307.ecommerce.repositories.PagamentoRepository;
import com.rms2307.ecommerce.repositories.PedidoRepository;
import com.rms2307.ecommerce.security.UserSS;
import com.rms2307.ecommerce.services.exceptions.AuthorizationException;
import com.rms2307.ecommerce.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PedidoRepository repo;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	public Pedido findById(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.findById(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.findById(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationEmail(obj);
		return obj;
	}

	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.findById(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}
}