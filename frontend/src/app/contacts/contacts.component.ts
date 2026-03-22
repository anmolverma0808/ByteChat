import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { UserService, UserProfile } from '../core/services/user.service';
import { AuthService } from '../core/services/auth.service';
import { ChatService } from '../core/services/chat.service';

@Component({
  selector: 'app-contacts',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './contacts.component.html',
  styleUrl: './contacts.component.css'
})
export class ContactsComponent implements OnInit {
  selectedId = signal<string | null>(null);
  searchTerm = signal('');
  
  users = signal<UserProfile[]>([]);
  
  groups = computed(() => {
    const list = this.users();
    const groupsMap = new Map<string, UserProfile[]>();
    
    list.forEach(user => {
      if (user.fullName) {
        const letter = user.fullName[0].toUpperCase();
        if (!groupsMap.has(letter)) {
          groupsMap.set(letter, []);
        }
        groupsMap.get(letter)?.push(user);
      }
    });
    
    return Array.from(groupsMap.keys()).sort().map(letter => ({
      letter,
      members: groupsMap.get(letter)?.sort((a, b) => a.fullName.localeCompare(b.fullName)) || []
    }));
  });

  filteredGroups = computed(() => {
    const term = this.searchTerm().toLowerCase();
    if (!term) return this.groups();
    
    return this.groups().map(group => ({
      ...group,
      members: group.members.filter(m => 
        m.fullName.toLowerCase().includes(term) || 
        m.email.toLowerCase().includes(term)
      )
    })).filter(group => group.members.length > 0);
  });

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private chatService: ChatService,
    private router: Router
  ) {}

  ngOnInit() {
    this.userService.getAllUsers().subscribe({
      next: (res) => {
        // Filter out current user
        const currentUserId = this.authService.currentUser()?.userId;
        this.users.set(res.data.filter((u: any) => u.id !== currentUserId));
        
        // Remove automatic selection of first user to allow list view on mobile
        // if (this.users().length > 0 && !this.selectedId()) {
        //   this.selectedId.set(this.users()[0].id);
        // }
      }
    });

    // Listen for real-time status updates
    this.chatService.watchStatusUpdates().subscribe(status => {
      this.users.update(users => {
        return users.map(u => {
          if (u.id === status.userId) {
            return { ...u, online: status.online };
          }
          return u;
        });
      });
    });
  }

  onSearch(event: Event) {
    const input = event.target as HTMLInputElement;
    this.searchTerm.set(input.value);
  }

  getInitials(name: string) {
    if (!name) return '?';
    return name.split(' ').map((n: string) => n[0]).join('').toUpperCase();
  }

  selectedContact() {
    return this.users().find(u => u.id === this.selectedId()) || null;
  }

  onMessageClick(user: UserProfile) {
    // Add contact in backend to ensure both see each other
    this.chatService.addContact(user.email).subscribe({
      next: () => {
        this.router.navigate(['/app/chat'], { queryParams: { contactId: user.id } });
      },
      error: () => {
        // If already exists or error, still navigate
        this.router.navigate(['/app/chat'], { queryParams: { contactId: user.id } });
      }
    });
  }

  onBack() {
    this.selectedId.set(null);
  }
}
