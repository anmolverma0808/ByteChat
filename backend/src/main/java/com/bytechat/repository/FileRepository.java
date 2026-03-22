package com.bytechat.repository;

import com.bytechat.model.FileDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends MongoRepository<FileDocument, String> {

    @Query("{ $or: [ { 'senderId': ?0 }, { 'receiverId': ?0 } ] }")
    List<FileDocument> findBySenderIdOrReceiverId(String userId);

    List<FileDocument> findBySenderIdAndReceiverId(String senderId, String receiverId);

    List<FileDocument> findBySenderId(String senderId);
}
