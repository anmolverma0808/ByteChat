import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../core/services/auth.service';
import { UserService, UserProfile } from '../core/services/user.service';
import { ChatService } from '../core/services/chat.service';
import { ToastService } from '../shared/services/toast.service';
import { ModalService } from '../shared/services/modal.service';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  profile = signal<UserProfile | null>(null);
  loading = signal(false);
  uploading = signal(false);
  avatarName = signal('');
  notificationsEnabled = signal(true);

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private chatService: ChatService,
    private toastService: ToastService,
    private modalService: ModalService
  ) {}

  ngOnInit() {
    this.loadProfile();
  }

  loadProfile() {
    this.loading.set(true);
    this.userService.getProfile().subscribe({
      next: (res) => {
        const data = res.data;
        this.profile.set(data);
        this.avatarName.set(data.avatarName || '');
        this.notificationsEnabled.set(data.notificationsEnabled !== false);
        this.loading.set(false);
      },
      error: () => {
        this.toastService.error('Failed to load settings');
        this.loading.set(false);
      }
    });
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      if (file.size > 2 * 1024 * 1024) {
        this.toastService.error('File too large (max 2MB)');
        return;
      }
      this.uploadAvatar(file);
    }
  }

  uploadAvatar(file: File) {
    this.uploading.set(true);
    this.chatService.uploadFile(file).subscribe({
      next: (res) => {
        const fileUrl = res.data.fileUrl;
        const currentProfile = this.profile();
        if (currentProfile) {
          const updatedProfile = { ...currentProfile, profileImage: fileUrl };
          this.userService.updateProfile(updatedProfile).subscribe({
            next: () => {
              this.profile.set(updatedProfile);
              this.toastService.success('Avatar updated');
              this.uploading.set(false);
              this.updateLocalUser({ profileImage: fileUrl });
            },
            error: () => {
              this.toastService.error('Failed to update avatar');
              this.uploading.set(false);
            }
          });
        }
      },
      error: () => {
        this.toastService.error('Upload failed');
        this.uploading.set(false);
      }
    });
  }

  saveAvatarName() {
    const currentProfile = this.profile();
    if (currentProfile) {
      const updatedProfile = { ...currentProfile, avatarName: this.avatarName() };
      this.loading.set(true);
      this.userService.updateProfile(updatedProfile).subscribe({
        next: () => {
          this.profile.set(updatedProfile);
          this.toastService.success('Avatar name saved');
          this.updateLocalUser({ avatarName: this.avatarName() });
          this.loading.set(false);
        },
        error: () => {
          this.toastService.error('Failed to save avatar name');
          this.loading.set(false);
        }
      });
    }
  }

  get userInitials() {
    const name = this.profile()?.fullName || this.authService.currentUser()?.fullName;
    if (!name) return 'U';
    return name.split(' ').map((n: string) => n[0]).join('').toUpperCase();
  }

  async onDeleteAccount() {
    const confirmed = await this.modalService.confirm(
      'Delete Account',
      'Are you sure you want to delete your account? This action is permanent and all your messages and data will be lost.',
      'Delete Permanently',
      'Keep My Account',
      'danger'
    );

    if (confirmed) {
      this.loading.set(true);
      this.userService.deleteAccount().subscribe({
        next: () => {
          this.toastService.success('Account deleted successfully');
          this.authService.logout();
          window.location.reload();
        },
        error: () => {
          this.toastService.error('Failed to delete account');
          this.loading.set(false);
        }
      });
    }
  }

  toggleNotifications() {
    const currentProfile = this.profile();
    if (currentProfile) {
      const newValue = !this.notificationsEnabled();
      this.notificationsEnabled.set(newValue);
      const updatedProfile = { ...currentProfile, notificationsEnabled: newValue };
      
      this.userService.updateProfile(updatedProfile).subscribe({
        next: () => {
          this.profile.set(updatedProfile);
          this.toastService.success(`Notifications ${newValue ? 'enabled' : 'disabled'}`);
          this.updateLocalUser({ notificationsEnabled: newValue });
        },
        error: () => {
          this.toastService.error('Failed to update notification settings');
          this.notificationsEnabled.set(!newValue); // Revert on error
        }
      });
    }
  }

  private updateLocalUser(updates: any) {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    const updatedUser = { ...user, ...updates };
    localStorage.setItem('user', JSON.stringify(updatedUser));
    this.authService.currentUser.set(updatedUser);
  }
}
