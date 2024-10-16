package com.joaovssc.urlshortener.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.joaovssc.urlshortener.entities.UrlEntity;

public interface UrlRepositories extends MongoRepository<UrlEntity, String> {

}
