import { Component, signal, OnInit, OnDestroy, ElementRef, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ChatService, SearchResult } from '../../services/chat.service';
import { ModalService } from '../../../shared/services/modal.service';
import { ThemeService } from '../../services/theme.service';
import { Subject, debounceTime, distinctUntilChanged, switchMap, takeUntil, of, catchError } from 'rxjs';

@Component({
  selector: 'app-app-shell',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive, HttpClientModule, FormsModule],
  templateUrl: './app-shell.component.html',
  styleUrls: ['./app-shell.component.css']
})
export class AppShellComponent implements OnInit, OnDestroy {
  isProfileMenuOpen = signal(false);
  
  // Search state
  searchQuery = signal('');
  searchResults = signal<SearchResult[]>([]);
  isSearching = signal(false);
  showSearchResults = signal(false);
  
  private searchSubject = new Subject<string>();
  private destroy$ = new Subject<void>();

  constructor(
    private authService: AuthService,
    private modalService: ModalService,
    public chatService: ChatService,
    public themeService: ThemeService,
    private router: Router,
    private elementRef: ElementRef
  ) {}

  ngOnInit() {
    // Initial fetch to populate unread counts
    this.chatService.getContacts().subscribe();

    // Debounced search logic
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(query => {
        if (!query || query.length < 2) {
          return of({ data: [] });
        }
        this.isSearching.set(true);
        return this.chatService.searchMessages(query).pipe(
          catchError((err: any) => {
            console.error('Search API error:', err);
            this.isSearching.set(false);
            return of({ data: [] });
          })
        );
      }),
      takeUntil(this.destroy$)
    ).subscribe({
      next: (res: any) => {
        this.searchResults.set(res.data || []);
        this.isSearching.set(false);
        this.showSearchResults.set(res.data && res.data.length > 0);
      },
      error: (err) => {
        // This only hits if the entire stream dies (unlikely now with catchError above)
        console.error('Critical search stream error:', err);
        this.isSearching.set(false);
      }
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onSearchInput(event: any) {
    const query = event.target.value;
    this.searchQuery.set(query);
    if (!query) {
      this.searchResults.set([]);
      this.showSearchResults.set(false);
    } else {
      this.searchSubject.next(query);
    }
  }

  onSearchFocus() {
    // Clear results if query is empty or too short
    if (this.searchQuery().length < 2) {
      this.searchResults.set([]);
      this.showSearchResults.set(false);
    } else if (this.searchResults().length > 0) {
      this.showSearchResults.set(true);
    }
  }

  selectSearchResult(result: SearchResult) {
    this.showSearchResults.set(false);
    this.searchQuery.set('');
    this.searchResults.set([]); // Clear results on selection
    
    this.router.navigate(['/app/chat'], { 
      queryParams: { 
        contactId: result.contactId,
        messageId: result.message.id 
      },
      queryParamsHandling: 'merge'
    });
  }

  toggleProfileMenu() {
    this.isProfileMenuOpen.update(v => !v);
  }

  get userInitials() {
    const user = this.authService.currentUser();
    if (user?.fullName) {
      return user.fullName.split(' ').map((n: string) => n[0]).join('').toUpperCase();
    }
    return 'U';
  }

  get userFullName() {
    return this.authService.currentUser()?.fullName || 'User';
  }

  get userEmail() {
    return this.authService.currentUser()?.email || 'user@example.com';
  }

  get userProfileImage() {
    return this.authService.currentUser()?.profileImage;
  }

  get notificationsEnabled() {
    return this.authService.currentUser()?.notificationsEnabled !== false;
  }

  async logout() {
    const confirmed = await this.modalService.confirm(
      'Sign Out',
      'Are you sure you want to log out of your ByteChat account?',
      'Logout',
      'Stay logged in',
      'warning'
    );

    if (confirmed) {
      this.authService.logout();
      window.location.reload();
    }
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    
    // Close profile menu if click is outside
    const profileTrigger = this.elementRef.nativeElement.querySelector('.user-profile-trigger');
    const profileDropdown = this.elementRef.nativeElement.querySelector('.profile-dropdown');
    
    if (this.isProfileMenuOpen() && 
        profileTrigger && !profileTrigger.contains(target) && 
        profileDropdown && !profileDropdown.contains(target)) {
      this.isProfileMenuOpen.set(false);
    }

    // Close search results if click is outside
    const searchField = this.elementRef.nativeElement.querySelector('.search-field');
    const searchResults = this.elementRef.nativeElement.querySelector('.search-results');
    
    if (this.showSearchResults() && 
        searchField && !searchField.contains(target) && 
        searchResults && !searchResults.contains(target)) {
      this.showSearchResults.set(false);
    }
  }
}
