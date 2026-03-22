import { Routes } from '@angular/router';
import { LandingComponent } from './landing/landing.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { 
    path: 'app', 
    loadComponent: () => import('./core/layout/app-shell/app-shell.component').then(m => m.AppShellComponent),
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'chat', pathMatch: 'full' },
      { path: 'chat', loadComponent: () => import('./chat/chat.component').then(m => m.ChatComponent) },
      { path: 'files', loadComponent: () => import('./files/files.component').then(m => m.FilesComponent) },
      { path: 'contacts', loadComponent: () => import('./contacts/contacts.component').then(m => m.ContactsComponent) },
      { path: 'settings', loadComponent: () => import('./settings/settings.component').then(m => m.SettingsComponent) },
      { path: 'profile', loadComponent: () => import('./settings/profile/profile.component').then(m => m.ProfileComponent) },
    ]
  },
  { path: '**', redirectTo: '' }
];
