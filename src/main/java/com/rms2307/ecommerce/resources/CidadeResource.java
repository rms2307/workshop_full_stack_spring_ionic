package com.rms2307.ecommerce.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rms2307.ecommerce.domain.Cidade;
import com.rms2307.ecommerce.dto.CidadeDTO;
import com.rms2307.ecommerce.services.CidadeService;

@RestController
@RequestMapping(value = "/estados")
public class CidadeResource {

	@Autowired
	private CidadeService service;

	@GetMapping(value = "/{estado_id}/cidades")
	public ResponseEntity<List<CidadeDTO>> findAll(@PathVariable Integer estado_id) {
		List<Cidade> list = service.findAll(estado_id);
		List<CidadeDTO> listDTO = list.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}

}
