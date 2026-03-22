import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalService } from '../shared/services/modal.service';
import { ToastService } from '../shared/services/toast.service';
import { ChatService, Contact } from '../core/services/chat.service';
import { AuthService } from '../core/services/auth.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-files',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './files.component.html',
  styleUrls: ['./files.component.css']
})
export class FilesComponent implements OnInit {
  categories = [
    { name: 'Images', icon: 'bi bi-image', count: 0, color: '#258CF4' },
    { name: 'Documents', icon: 'bi bi-file-earmark-pdf', count: 0, color: '#EF4444' },
    { name: 'Spreadsheets', icon: 'bi bi-file-earmark-spreadsheet', count: 0, color: '#22C55E' },
    { name: 'Media', icon: 'bi bi-camera-reels', count: 0, color: '#A855F7' }
  ];

  files = signal<any[]>([]);
  contacts = signal<Contact[]>([]);
  activeSidebarTab = signal<string>('all'); // 'all' or 'starred'
  selectedCategory = signal<string | null>(null);
  searchQuery = signal<string>('');

  storageInfo = computed(() => {
    const totalBytes = this.files().reduce((acc, f) => acc + (f.rawSize || 0), 0);
    const limitBytes = 1.5 * 1024 * 1024 * 1024; // 1.5 GB
    const percent = Math.min(Math.round((totalBytes / limitBytes) * 100), 100);
    
    return {
      used: this.formatSize(totalBytes),
      total: '1.5 GB',
      percent: percent
    };
  });

  constructor(
    private modalService: ModalService,
    private toastService: ToastService,
    private chatService: ChatService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.loadFiles();
  }

  loadFiles() {
    const currentUserId = this.authService.currentUser()?.userId;
    
    forkJoin({
      files: this.chatService.getFiles(),
      contacts: this.chatService.getContacts()
    }).subscribe({
      next: (res) => {
        const contactsData = res.contacts.data || [];
        this.contacts.set(contactsData);
        
        const filesData = res.files.data || [];
        this.files.set(filesData.map((f: any) => {
          let uploaderName = 'Unknown';
          if (f.senderId === currentUserId) {
            uploaderName = 'YOU';
          } else {
            const contact = contactsData.find((c: any) => c.contactUserId === f.senderId);
            uploaderName = contact ? contact.contactName : 'Contact';
          }

          return {
            id: f.id,
            name: f.fileName,
            type: this.getFileType(f.fileName),
            addedAt: this.formatDate(f.uploadedAt),
            modifiedAt: this.formatDate(f.uploadedAt),
            size: this.formatSize(f.fileSize),
            rawSize: f.fileSize,
            url: f.fileUrl,
            uploaderName: uploaderName,
            isStarred: f.starred || false
          };
        }).sort((a: any, b: any) => new Date(b.addedAt).getTime() - new Date(a.addedAt).getTime()));
        
        this.updateCategories();
      },
      error: () => this.toastService.error('Failed to load files.')
    });
  }

  onSearch(event: any) {
    this.searchQuery.set(event.target.value.toLowerCase());
  }

  get displayedFiles() {
    let filtered = this.files();

    // Search filter
    if (this.searchQuery()) {
      filtered = filtered.filter((f: any) => 
        f.name.toLowerCase().includes(this.searchQuery())
      );
    }

    // Side filter
    if (this.activeSidebarTab() === 'starred') {
      filtered = filtered.filter((f: any) => f.isStarred);
    }

    // Category filter
    if (this.selectedCategory()) {
      filtered = filtered.filter((f: any) => f.type === this.selectedCategory());
    }

    // If no specific category/starred/search filter, just show recent 2
    if (!this.selectedCategory() && this.activeSidebarTab() === 'all' && !this.searchQuery()) {
       return filtered.slice(0, 2);
    }

    return filtered;
  }

  setSidebarTab(tab: string) {
    this.activeSidebarTab.set(tab);
    this.selectedCategory.set(null);
  }

  toggleCategory(catName: string) {
    const type = this.mapCategoryToType(catName);
    if (this.selectedCategory() === type) {
      this.selectedCategory.set(null);
    } else {
      this.selectedCategory.set(type);
      this.activeSidebarTab.set('all'); // Reset sidebar tab when clicking category
    }
  }

  public mapCategoryToType(catName: string): string {
    switch(catName) {
      case 'Images': return 'image';
      case 'Documents': return 'pdf';
      case 'Spreadsheets': return 'excel';
      case 'Media': return 'media';
      default: return '';
    }
  }

  toggleStar(event: Event, file: any) {
    event.stopPropagation();
    this.chatService.toggleFileStar(file.id).subscribe({
      next: (res) => {
        const updatedFile = res.data;
        this.files.update((allFiles: any[]) => 
          allFiles.map((f: any) => f.id === updatedFile.id ? { ...f, isStarred: updatedFile.starred } : f)
        );
      },
      error: () => this.toastService.error('Failed to update star status.')
    });
  }

  updateCategories() {
    const counts = {
      image: 0,
      document: 0,
      excel: 0,
      media: 0
    };

    this.files().forEach((f: any) => {
      if (f.type === 'image') counts.image++;
      else if (f.type === 'pdf') counts.document++;
      else if (f.type === 'excel') counts.excel++;
    });

    this.categories[0].count = counts.image;
    this.categories[1].count = counts.document;
    this.categories[2].count = counts.excel;
  }

  openUploadModal() {
    this.toastService.info('File upload system coming soon!');
  }

  getFileIcon(type: string) {
    switch(type) {
      case 'pdf': return 'bi bi-file-earmark-pdf text-danger';
      case 'image': return 'bi bi-image text-primary';
      case 'excel': return 'bi bi-file-earmark-spreadsheet text-success';
      default: return 'bi bi-file-earmark text-secondary';
    }
  }

  getFileType(fileName: string): string {
    const ext = fileName.split('.').pop()?.toLowerCase();
    if (['png', 'jpg', 'jpeg', 'gif', 'svg'].includes(ext!)) return 'image';
    if (['pdf', 'doc', 'docx'].includes(ext!)) return 'pdf';
    if (['xls', 'xlsx', 'csv'].includes(ext!)) return 'excel';
    return 'file';
  }

  formatDate(dateStr: string): string {
    if (!dateStr) return 'Unknown';
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
  }

  formatSize(bytes: number): string {
    if (!bytes) return '0 B';
    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }
}
