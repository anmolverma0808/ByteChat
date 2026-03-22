import { Component, OnInit, signal } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../shared/services/toast.service';
import { UserService, UserProfile } from '../../core/services/user.service';
import { ChatService } from '../../core/services/chat.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  profileForm: FormGroup;
  loading = signal(false);
  uploading = signal(false);
  currentUserProfile = signal<UserProfile | null>(null);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private chatService: ChatService,
    private toastService: ToastService,
    private location: Location
  ) {
    this.profileForm = this.fb.group({
      fullName: [{ value: '', disabled: true }, Validators.required],
      email: [{ value: '', disabled: true }],
      phone: [{ value: '', disabled: true }],
      bio: ['']
    });
  }

  ngOnInit() {
    this.loadProfile();
  }

  loadProfile() {
    this.loading.set(true);
    this.userService.getProfile().subscribe({
      next: (res) => {
        const profile = res.data;
        this.currentUserProfile.set(profile);
        this.profileForm.patchValue({
          fullName: profile.fullName,
          email: profile.email,
          phone: profile.phone,
          bio: profile.bio
        });
        this.loading.set(false);
      },
      error: (err) => {
        this.toastService.error('Failed to load profile');
        this.loading.set(false);
      }
    });
  }

  get userInitials() {
    const name = this.currentUserProfile()?.fullName || this.authService.currentUser()?.fullName;
    if (!name) return 'U';
    return name.split(' ').map((n: string) => n[0]).join('').toUpperCase();
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      if (file.size > 2 * 1024 * 1024) {
        this.toastService.error('File too large (max 2MB)');
        return;
      }
      this.uploadPhoto(file);
    }
  }

  uploadPhoto(file: File) {
    this.uploading.set(true);
    // Use ChatService.uploadFile for now as a generic upload
    this.chatService.uploadFile(file).subscribe({
      next: (res) => {
        const fileUrl = res.data.fileUrl;
        const currentProfile = this.currentUserProfile();
        if (currentProfile) {
          const updatedProfile = { ...currentProfile, profileImage: fileUrl };
          this.userService.updateProfile(updatedProfile).subscribe({
            next: () => {
              this.currentUserProfile.set(updatedProfile);
              this.toastService.success('Profile photo updated');
              this.uploading.set(false);
              // Update local auth storage to reflect new photo
              this.updateLocalUser({ profileImage: fileUrl });
            },
            error: () => {
              this.toastService.error('Failed to update profile image');
              this.uploading.set(false);
            }
          });
        }
      },
      error: (err) => {
        this.toastService.error('Failed to upload photo');
        this.uploading.set(false);
      }
    });
  }

  onSubmit() {
    if (this.profileForm.valid) {
      this.loading.set(true);
      const bioValue = this.profileForm.get('bio')?.value;
      const currentProfile = this.currentUserProfile();
      
      if (currentProfile) {
        const updateData: UserProfile = { ...currentProfile, bio: bioValue };
        this.userService.updateProfile(updateData).subscribe({
          next: () => {
            this.toastService.success('Profile updated successfully!');
            this.loading.set(false);
            this.updateLocalUser({ bio: bioValue });
          },
          error: () => {
            this.toastService.error('Failed to update profile');
            this.loading.set(false);
          }
        });
      }
    }
  }

  private updateLocalUser(updates: any) {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    const updatedUser = { ...user, ...updates };
    localStorage.setItem('user', JSON.stringify(updatedUser));
    this.authService.currentUser.set(updatedUser);
  }

  onBack() {
    this.location.back();
  }
}
