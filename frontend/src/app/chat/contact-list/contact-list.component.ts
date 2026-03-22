import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService, Contact } from '../../core/services/chat.service';

@Component({
  selector: 'app-contact-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './contact-list.component.html',
  styleUrls: ['./contact-list.component.css']
})
export class ContactListComponent implements OnInit {
  @Input() selectedContact: Contact | null = null;
  @Output() contactSelected = new EventEmitter<Contact>();

  activeTab = signal<'all' | 'unread'>('all');
  searchTerm = '';

  constructor(public chatService: ChatService) {} // Made chatService public to access its signal in template

  ngOnInit() {
    this.chatService.getContacts().subscribe();
  }

  filteredContacts() {
    let filtered = [...this.chatService.contacts()]; // Create a copy for sorting
    
    if (this.activeTab() === 'unread') {
      filtered = filtered.filter(c => (c.unreadCount ?? 0) > 0);
    }
    
    if (this.searchTerm) {
      filtered = filtered.filter(c => c.contactName.toLowerCase().includes(this.searchTerm.toLowerCase()));
    }

    // Sort: Active accounts first, then deleted accounts
    // Within each group, sort by name (optional, but good for UX)
    return filtered.sort((a, b) => {
      if (a.deletedAccount && !b.deletedAccount) return 1;
      if (!a.deletedAccount && b.deletedAccount) return -1;
      return a.contactName.localeCompare(b.contactName);
    });
  }

  getInitials(name: string | undefined) {
    if (!name) return '?';
    return name.split(' ').map((n: string) => n[0]).join('').toUpperCase();
  }

  trackByContactId(index: number, contact: Contact) {
    return contact.id;
  }
}
