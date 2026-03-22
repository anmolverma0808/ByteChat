package com.bytechat.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "contacts")
@CompoundIndex(name = "user_contact_idx", def = "{'userId': 1, 'contactUserId': 1}", unique = true)
public class Contact {

    @Id
    private String id;
    private String userId;
    private String contactUserId;
    private Instant addedAt = Instant.now();

    public Contact() {}

    public Contact(String id, String userId, String contactUserId, Instant addedAt) {
        this.id = id;
        this.userId = userId;
        this.contactUserId = contactUserId;
        this.addedAt = addedAt != null ? addedAt : Instant.now();
    }

    public static ContactBuilder builder() {
        return new ContactBuilder();
    }

    public static class ContactBuilder {
        private String id;
        private String userId;
        private String contactUserId;
        private Instant addedAt;

        public ContactBuilder id(String id) { this.id = id; return this; }
        public ContactBuilder userId(String userId) { this.userId = userId; return this; }
        public ContactBuilder contactUserId(String contactUserId) { this.contactUserId = contactUserId; return this; }
        public ContactBuilder addedAt(Instant addedAt) { this.addedAt = addedAt; return this; }

        public Contact build() {
            return new Contact(id, userId, contactUserId, addedAt);
        }
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getContactUserId() { return contactUserId; }
    public void setContactUserId(String contactUserId) { this.contactUserId = contactUserId; }
    public Instant getAddedAt() { return addedAt; }
    public void setAddedAt(Instant addedAt) { this.addedAt = addedAt; }
}
