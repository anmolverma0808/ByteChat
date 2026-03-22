package com.bytechat.repository;

import com.bytechat.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    @Query("{ $or: [ { 'senderId': ?0, 'receiverId': ?1 }, { 'senderId': ?1, 'receiverId': ?0 } ] }")
    List<Message> findConversation(String userId1, String userId2);

    List<Message> findBySenderIdAndReceiverId(String senderId, String receiverId);
    
    List<Message> findBySenderIdAndReceiverIdAndReadStatusFalse(String senderId, String receiverId);

    List<Message> findByReceiverIdAndReadStatusFalse(String receiverId);

    long countByReceiverIdAndReadStatusFalse(String receiverId);

    long countBySenderIdAndReceiverIdAndReadStatusFalse(String senderId, String receiverId);

    @Query("{ 'message': { $regex: ?0, $options: 'i' }, $or: [ { 'senderId': ?1 }, { 'receiverId': ?1 } ] }")
    List<Message> searchMessages(String query, String userId);
}
