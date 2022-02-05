package com.young.dev.youtubeclone.repository;

import com.young.dev.youtubeclone.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
