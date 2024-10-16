package com.joaovssc.urlshortener.controller;

import java.net.URI;
import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joaovssc.urlshortener.dto.ShortenUrlRequest;
import com.joaovssc.urlshortener.dto.ShortenUrlResponse;
import com.joaovssc.urlshortener.entities.UrlEntity;
import com.joaovssc.urlshortener.repositories.UrlRepositories;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class UrlController {
	
	@Autowired
	private UrlRepositories repository;
	
	@PostMapping(value = "/shorten-url")
	public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest dto, HttpServletRequest servletRequest){
		System.out.println("Requisição recebida com URL: " + dto.url());  
		String id;
		  do {
		   id = RandomStringUtils.randomAlphabetic(5, 10);
		  }while(repository.existsById(id));
		  
		  repository.save(new UrlEntity(id, dto.url(), LocalDateTime.now().plusMinutes(1)));
		
		  var redirectUrl = servletRequest.getRequestURL().toString().replace("shorten-url", id);
		  
		return ResponseEntity.ok(new ShortenUrlResponse(redirectUrl));
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Void> redirect(@PathVariable String id){
		var url = repository.findById(id);
		
		if(url.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(url.get().getFullUrl()));
		return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
		
	}
}
