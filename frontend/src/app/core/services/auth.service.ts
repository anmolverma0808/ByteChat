import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface AuthResponse {
  success: boolean;
  message: string;
  data: {
    token: string;
    userId: string;
    email: string;
    fullName: string;
    phone?: string;
    profileImage?: string;
    avatarName?: string;
    bio?: string;
  };
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  
  // Use Angular signals for reactive state
  currentUser = signal<any>(this.getUserFromStorage());

  constructor(private http: HttpClient) {}

  register(payload: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, payload).pipe(
      tap(res => this.setSession(res))
    );
  }

  login(payload: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, payload).pipe(
      tap(res => this.setSession(res))
    );
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUser.set(null);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  private setSession(res: AuthResponse) {
    if (res.success && res.data.token) {
      localStorage.setItem('token', res.data.token);
      localStorage.setItem('user', JSON.stringify(res.data));
      this.currentUser.set(res.data);
    }
  }

  private getUserFromStorage() {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }
}
