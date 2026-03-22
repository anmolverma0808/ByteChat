package com.bytechat.service;

import com.bytechat.dto.AddContactRequest;
import com.bytechat.dto.ContactDto;
import com.bytechat.model.Contact;
import com.bytechat.model.Message;
import com.bytechat.model.User;
import com.bytechat.repository.ContactRepository;
import com.bytechat.repository.MessageRepository;
import com.bytechat.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public ContactService(ContactRepository contactRepository, UserRepository userRepository, MessageRepository messageRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public List<ContactDto> getContacts(String userId) {
        List<Contact> contacts = contactRepository.findByUserId(userId);
        return contacts.stream()
                .map(contact -> mapToContactDto(userId, contact.getContactUserId(), contact.getId(), contact.getAddedAt()))
                .collect(Collectors.toList());
    }

    public ContactDto getContact(String userId, String contactUserId) {
        Contact contact = contactRepository.findByUserIdAndContactUserId(userId, contactUserId)
                .orElse(null);
        String contactId = contact != null ? contact.getId() : null;
        Instant addedAt = contact != null ? contact.getAddedAt() : null;
        
        return mapToContactDto(userId, contactUserId, contactId, addedAt);
    }

    private ContactDto mapToContactDto(String userId, String contactUserId, String contactId, Instant addedAt) {
        User contactUser = userRepository.findById(contactUserId).orElse(null);
        
        boolean isDeleted = contactUser == null || contactUser.isDeleted();
        
        String contactName = "Deleted Account";
        String contactEmail = null;
        String contactProfileImage = null;
        String contactPhone = null;
        String contactBio = "This account has been deleted.";

        if (contactUser != null && !contactUser.isDeleted()) {
            contactName = contactUser.getFullName();
            contactEmail = contactUser.getEmail();
            contactProfileImage = contactUser.getProfileImage();
            contactPhone = contactUser.getPhone();
            contactBio = contactUser.getBio();
        }

        // Fetch last message for this pair
        List<Message> pairMessages = messageRepository.findConversation(userId, contactUserId);
        Message lastMessage = pairMessages.isEmpty() ? null : pairMessages.get(pairMessages.size() - 1);

        // Calculate unread count (incoming to the current user)
        long unreadCount = messageRepository.countBySenderIdAndReceiverIdAndReadStatusFalse(
                contactUserId, userId);

        return ContactDto.builder()
                .id(contactId)
                .contactUserId(contactUserId)
                .contactName(contactName)
                .contactEmail(contactEmail)
                .contactProfileImage(contactProfileImage)
                .contactPhone(contactPhone)
                .contactBio(contactBio)
                .lastMessage(lastMessage != null ? lastMessage.getMessage() : null)
                .lastMessageAt(lastMessage != null ? lastMessage.getCreatedAt() : null)
                .unreadCount((int) unreadCount)
                .addedAt(addedAt)
                .deletedAccount(isDeleted)
                .online(contactUser != null && !isDeleted && contactUser.isOnline())
                .build();
    }

    public void ensureContactExists(String userId1, String userId2) {
        if (!userRepository.existsById(userId1) || !userRepository.existsById(userId2)) {
            throw new IllegalArgumentException("User(s) not found");
        }

        // Add user 2 as contact for user 1
        if (!contactRepository.existsByUserIdAndContactUserId(userId1, userId2)) {
            Contact contact1 = Contact.builder()
                    .userId(userId1)
                    .contactUserId(userId2)
                    .addedAt(Instant.now())
                    .build();
            contactRepository.save(contact1);
        }

        // Add user 1 as contact for user 2
        if (!contactRepository.existsByUserIdAndContactUserId(userId2, userId1)) {
            Contact contact2 = Contact.builder()
                    .userId(userId2)
                    .contactUserId(userId1)
                    .addedAt(Instant.now())
                    .build();
            contactRepository.save(contact2);
        }
    }

    public ContactDto addContact(String userId, AddContactRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
                
        User contactUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + request.getEmail()));

        if (user.getId().equals(contactUser.getId())) {
            throw new IllegalArgumentException("You cannot add yourself as a contact");
        }

        // Save for user A (requester)
        if (!contactRepository.existsByUserIdAndContactUserId(userId, contactUser.getId())) {
            Contact contactA = Contact.builder()
                    .userId(userId)
                    .contactUserId(contactUser.getId())
                    .addedAt(Instant.now())
                    .build();
            contactRepository.save(contactA);
        }

        // Save for user B (target)
        if (!contactRepository.existsByUserIdAndContactUserId(contactUser.getId(), userId)) {
            Contact contactB = Contact.builder()
                    .userId(contactUser.getId())
                    .contactUserId(userId)
                    .addedAt(Instant.now())
                    .build();
            contactRepository.save(contactB);
        }

        return ContactDto.builder()
                .contactUserId(contactUser.getId())
                .contactName(contactUser.getFullName())
                .contactEmail(contactUser.getEmail())
                .contactProfileImage(contactUser.getProfileImage())
                .contactPhone(contactUser.getPhone())
                .contactBio(contactUser.getBio())
                .addedAt(Instant.now())
                .build();
    }

    public void removeContact(String userId, String contactUserId) {
        if (!contactRepository.existsByUserIdAndContactUserId(userId, contactUserId)) {
            throw new IllegalArgumentException("Contact not found");
        }
        contactRepository.deleteByUserIdAndContactUserId(userId, contactUserId);
    }
}
