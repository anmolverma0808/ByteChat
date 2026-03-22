import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContactListComponent } from './contact-list/contact-list.component';
import { ChatAreaComponent } from './chat-area/chat-area.component';
import { ChatService, Contact } from '../core/services/chat.service';

import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, ContactListComponent, ChatAreaComponent],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  selectedContact = signal<Contact | null>(null);

  constructor(
    private route: ActivatedRoute,
    private chatService: ChatService
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const contactId = params['contactId'];
      if (contactId) {
        this.chatService.getContacts().subscribe({
          next: (res) => {
            const contacts = res.data || [];
            const contact = contacts.find((c: Contact) => c.contactUserId === contactId);
            if (contact) {
              this.onSelectContact(contact);
            } else {
              // Fallback: Fetch contact specifically if not in list
              this.chatService.getContact(contactId).subscribe({
                next: (contactRes) => {
                  if (contactRes.data) {
                    this.onSelectContact(contactRes.data);
                  }
                },
                error: (err) => console.error('Error fetching individual contact in ChatComponent', err)
              });
            }
          },
          error: (err) => console.error('Error fetching contacts in ChatComponent', err)
        });
      }
    });
  }

  onSelectContact(contact: Contact) {
    this.selectedContact.set(contact);
  }

  onBack() {
    this.selectedContact.set(null);
  }
}

