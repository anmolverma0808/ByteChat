package com.bytechat.repository;

import com.bytechat.model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends MongoRepository<Contact, String> {

    List<Contact> findByUserId(String userId);

    Optional<Contact> findByUserIdAndContactUserId(String userId, String contactUserId);

    boolean existsByUserIdAndContactUserId(String userId, String contactUserId);

    void deleteByUserIdAndContactUserId(String userId, String contactUserId);
}
