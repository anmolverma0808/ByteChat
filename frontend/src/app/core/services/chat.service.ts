import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Contact {
  id: string;
  contactUserId: string; 
  contactName: string;
  contactEmail: string;
  contactProfileImage?: string;
  contactPhone?: string;
  contactBio?: string;
  lastMessage?: string;
  lastMessageAt?: string;
  lastActive?: string;
  unreadCount?: number;
  deletedAccount?: boolean;
  online?: boolean;
}

export interface Message {
  id: string;
  senderId: string;
  receiverId: string;
  message: string;
  messageType: 'TEXT' | 'FILE';
  fileUrl?: string;
  createdAt: string;
  readStatus: boolean;
}

export interface SearchResult {
  message: Message;
  contactName: string;
  contactId: string;
  contactProfileImage?: string;
}

import { WebSocketService } from './websocket.service';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private messageReceivedSource = new Subject<Message>();
  messageReceived$ = this.messageReceivedSource.asObservable();

  private conversationReadSource = new Subject<string>();
  conversationRead$ = this.conversationReadSource.asObservable();

  private typingReceivedSubject = new Subject<{senderId: string, isTyping: boolean}>();
  typingReceived$ = this.typingReceivedSubject.asObservable();

  contacts = signal<Contact[]>([]);
  loadingContacts = signal(false);
  totalUnreadCount = computed(() => {
    return this.contacts().filter(c => (c.unreadCount ?? 0) > 0).length;
  });

  constructor(
    private http: HttpClient,
    private wsService: WebSocketService
  ) {
    this.wsService.connect();
    this.wsService.watchMessages().subscribe({
      next: (msg) => {
        this.messageReceivedSource.next(msg);
        this.getContacts(true).subscribe(); // Silent refresh
      },
      error: (err) => console.error('Error in ChatService watchMessages:', err)
    });

    this.conversationRead$.subscribe(() => {
      this.getContacts(true).subscribe(); // Silent refresh
    });

    this.wsService.watchTypingIndicators().subscribe(typing => {
      this.typingReceivedSubject.next(typing);
    });

    this.wsService.watchStatusUpdates().subscribe(status => {
      this.contacts.update(contacts => {
        return contacts.map(c => {
          if (c.contactUserId === status.userId) {
            return { ...c, online: status.online };
          }
          return c;
        });
      });
    });
  }

  getContacts(silent: boolean = false): Observable<any> {
    if (!silent) {
      this.loadingContacts.set(true);
    }
    return this.http.get(`${environment.apiUrl}/contacts`).pipe(
      tap((res: any) => {
        if (res.data) {
          this.contacts.set(res.data);
        }
        this.loadingContacts.set(false);
      }),
      tap({ error: () => this.loadingContacts.set(false) })
    );
  }

  addContact(email: string): Observable<any> {
    return this.http.post(`${environment.apiUrl}/contacts`, { email });
  }

  getContact(contactId: string): Observable<any> {
    return this.http.get(`${environment.apiUrl}/contacts/${contactId}`);
  }

  getMessages(contactId: string): Observable<any> {
    return this.http.get(`${environment.apiUrl}/messages/${contactId}`);
  }

  uploadFile(file: File, receiverId?: string): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    if (receiverId) {
      formData.append('receiverId', receiverId);
    }
    return this.http.post(`${environment.apiUrl}/files/upload`, formData);
  }

  getFiles(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/files`);
  }

  toggleFileStar(fileId: string): Observable<any> {
    return this.http.post(`${environment.apiUrl}/files/toggle-star/${fileId}`, {});
  }

  notifyConversationRead(contactId: string) {
    this.conversationReadSource.next(contactId);
  }

  watchStatusUpdates(): Observable<any> {
    return this.wsService.watchStatusUpdates();
  }

  sendTypingStatus(receiverId: string, isTyping: boolean) {
    this.wsService.sendMessage('/app/chat.typing', { receiverId, isTyping });
  }

  searchMessages(query: string): Observable<any> {
    return this.http.get(`${environment.apiUrl}/messages/search`, { params: { query } });
  }
}
