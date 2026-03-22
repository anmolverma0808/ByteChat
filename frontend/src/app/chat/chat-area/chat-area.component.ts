import { Component, Input, OnChanges, SimpleChanges, signal, ViewChild, ElementRef, AfterViewChecked, OnInit, OnDestroy, effect, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChatService, Contact, Message } from '../../core/services/chat.service';
import { MessageInputComponent } from './message-input/message-input.component';
import { AuthService } from '../../core/services/auth.service';
import { WebSocketService } from '../../core/services/websocket.service';
import { Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-chat-area',
  standalone: true,
  imports: [CommonModule, MessageInputComponent],
  templateUrl: './chat-area.component.html',
  styleUrls: ['./chat-area.component.css']
})
export class ChatAreaComponent implements OnInit, OnChanges, OnDestroy, AfterViewChecked {
  @Input() contact!: Contact;
  @Output() back = new EventEmitter<void>();
  @ViewChild('scrollMe') private myScrollContainer!: ElementRef;

  messages = signal<Message[]>([]);
  showInfoSidebar = signal(false);
  isContactTyping = signal(false);
  highlightedMessageId = signal<string | null>(null);
  
  private wsSubscription?: Subscription;
  private readReceiptSubscription?: Subscription;
  private statusSubscription?: Subscription;
  private typingIndicatorSubscription?: Subscription;
  private routeSubscription?: Subscription;
  pendingScrollToMessageId = signal<string | null>(null);
  disableAutoScroll = signal(false);
  private lastMessageCount = 0;

  constructor(
    private chatService: ChatService, 
    private authService: AuthService,
    private wsService: WebSocketService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    // Watch for message changes and pending scrolls
    effect(() => {
      const msgs = this.messages();
      const scrollId = this.pendingScrollToMessageId();
      
      if (scrollId && msgs.length > 0) {
        // Attempt to scroll - wrap in timeout to ensure DOM has rendered new messages
        setTimeout(() => {
          if (this.pendingScrollToMessageId() === scrollId) {
            this.scrollToMessage(scrollId);
          }
        }, 150); // Increased delay slightly for DOM safety
      }
    });
  }

  ngOnInit() {
    this.wsService.connect();
    
    this.wsSubscription = this.chatService.messageReceived$.subscribe((msg: Message) => {
      if (this.contact && (msg.senderId === this.contact.contactUserId || msg.receiverId === this.contact.contactUserId)) {
        this.messages.update(prev => {
          const exists = prev.some(m => m.id === msg.id);
          if (exists) return prev;

          const optimisticIndex = prev.findIndex(m => 
            m.id.startsWith('temp-') && 
            m.message === msg.message && 
            m.senderId === msg.senderId
          );

          if (optimisticIndex !== -1) {
            const updated = [...prev];
            updated[optimisticIndex] = msg;
            return updated;
          }

          return [...prev, msg];
        });

        this.disableAutoScroll.set(false); // Enable auto-scroll on new message

        if (msg.senderId === this.contact.contactUserId) {
          this.markAsRead();
        }
      }
    });

    this.readReceiptSubscription = this.wsService.watchReadReceipts().subscribe((res: any) => {
      // res.readerId is the one who read the messages
      if (res.readerId === this.contact.contactUserId) {
        this.messages.update(prev => {
          return prev.map(m => {
            if (m.receiverId === res.readerId && !m.readStatus) {
              return { ...m, readStatus: true };
            }
            return m;
          });
        });
      }
    });

    this.statusSubscription = this.chatService.watchStatusUpdates().subscribe(status => {
      if (this.contact && this.contact.contactUserId === status.userId) {
        this.contact = { ...this.contact, online: status.online };
      }
    });

    this.typingIndicatorSubscription = this.chatService.typingReceived$.subscribe(typing => {
      if (this.contact && typing.senderId === this.contact.contactUserId) {
        this.isContactTyping.set(typing.isTyping);
      }
    });

    this.routeSubscription = this.route.queryParams.subscribe(params => {
      const messageId = params['messageId'];
      if (messageId) {
        this.pendingScrollToMessageId.set(messageId);
        this.highlightedMessageId.set(messageId);
        this.disableAutoScroll.set(true); // Disable auto-scroll to bottom on deep-link
        // Clear highlight after animation
        setTimeout(() => this.highlightedMessageId.set(null), 3000);
      }
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['contact'] && this.contact) {
      const prevId = changes['contact'].previousValue?.contactUserId;
      const currId = this.contact.contactUserId;
      
      // Only reload messages if the actual contact person changed
      if (prevId !== currId) {
        this.disableAutoScroll.set(false); // Reset scroll behavior for new contact
        this.lastMessageCount = 0; // Reset count to force scroll on load
        this.loadMessages();
        this.markAsRead();
      }
    }
  }

  ngOnDestroy() {
    this.wsSubscription?.unsubscribe();
    this.readReceiptSubscription?.unsubscribe();
    this.statusSubscription?.unsubscribe();
    this.typingIndicatorSubscription?.unsubscribe();
    this.routeSubscription?.unsubscribe();
  }

  ngAfterViewChecked() {
    const msgs = this.messages();
    const scrollId = this.pendingScrollToMessageId();

    if (scrollId) {
      this.scrollToMessage(scrollId);
      this.lastMessageCount = msgs.length; // "Consume" these messages so we don't snap to bottom later
    } else if (msgs.length > this.lastMessageCount) {
      if (!this.disableAutoScroll()) {
        this.lastMessageCount = msgs.length;
        // Scroll to bottom asynchronously to ensure DOM has updated
        this.scrollToBottom();
      } else {
        this.lastMessageCount = msgs.length;
      }
    }
  }

  private scrollToMessage(messageId: string, retryCount = 0) {
    const element = document.getElementById('msg-' + messageId);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth', block: 'center' });
      this.pendingScrollToMessageId.set(null);
      
      // Clear the messageId from URL so it doesn't re-scroll on refresh
      this.router.navigate([], {
        relativeTo: this.route,
        queryParams: { messageId: null },
        queryParamsHandling: 'merge',
        replaceUrl: true
      });
    } else if (retryCount < 10) {
      // Retry up to 10 times (1.5 seconds total)
      setTimeout(() => this.scrollToMessage(messageId, retryCount + 1), 150);
    } else {
      console.warn(`Could not find message element msg-${messageId} after multiple retries.`);
      this.pendingScrollToMessageId.set(null);
    }
  }

  onTyping(isTyping: boolean) {
    if (this.contact) {
      this.chatService.sendTypingStatus(this.contact.contactUserId, isTyping);
    }
  }

  loadMessages() {
    this.chatService.getMessages(this.contact.contactUserId).subscribe({
      next: (res) => {
        this.messages.set(res.data || []);
      },
      error: (err) => {
        console.error('Error loading messages:', err);
        this.messages.set([]);
      }
    });
  }

  identifyMessage(index: number, msg: Message) {
    return msg.id;
  }

  isOutgoing(msg: Message) {
    return msg.senderId === this.authService.currentUser()?.userId;
  }

  getInitials(name: string | undefined) {
    if (!name) return '?';
    return name.split(' ').map((n: string) => n[0]).join('').toUpperCase();
  }

  onSendMessage(content: { text: string, file: File | null }) {
    if (content.file) {
      this.chatService.uploadFile(content.file, this.contact.contactUserId).subscribe({
        next: (res) => {
          // Use filename as text if no message provided
          const messageText = content.text.trim() || content.file!.name;
          this.sendWebSocketMessage(messageText, 'FILE', res.data.fileUrl);
        },
        error: (err) => {
          console.error('File upload failed:', err);
          // Fallback or user notification could go here
        }
      });
    } else {
      const messageText = content.text.trim();
      if (!messageText) return;

      // Optimistic update
      const tempId = `temp-${Date.now()}`;
      const optimisticMsg: Message = {
        id: tempId,
        senderId: this.authService.currentUser()?.userId || '',
        receiverId: this.contact.contactUserId,
        message: messageText,
        messageType: 'TEXT',
        createdAt: new Date().toISOString(),
        readStatus: false
      };
      
      this.messages.update(prev => [...prev, optimisticMsg]);
      this.disableAutoScroll.set(false); // Allow scroll to bottom for my own new message
      this.sendWebSocketMessage(messageText, 'TEXT');
    }
  }

  private sendWebSocketMessage(text: string, type: 'TEXT' | 'FILE', fileUrl?: string) {
    const payload = {
      receiverId: this.contact.contactUserId,
      message: text,
      messageType: type,
      fileUrl: fileUrl
    };

    this.wsService.sendMessage('/app/chat.send', payload);
  }

  private scrollToBottom(): void {
    // Small delay to ensure browser has rendered new message bubbles
    setTimeout(() => {
      try {
        const container = this.myScrollContainer.nativeElement;
        container.scrollTop = container.scrollHeight;
      } catch(err) { }
    }, 50);
  }

  toggleInfoSidebar() {
    this.showInfoSidebar.update(v => !v);
  }

  private markAsRead() {
    if (!this.contact) return;
    this.wsService.sendMessage('/app/chat.read', {
      senderId: this.contact.contactUserId
    });
    this.chatService.notifyConversationRead(this.contact.contactUserId);
  }
}
