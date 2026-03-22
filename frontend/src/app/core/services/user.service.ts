import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface UserProfile {
  id: string;
  fullName: string;
  avatarName?: string;
  email: string;
  phone?: string;
  profileImage?: string;
  bio?: string;
  timezone?: string;
  notificationsEnabled: boolean;
  online?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<any> {
    return this.http.get(`${this.apiUrl}`);
  }

  getProfile(): Observable<any> {
    return this.http.get(`${this.apiUrl}/profile`);
  }

  updateProfile(profile: UserProfile): Observable<any> {
    return this.http.put(`${this.apiUrl}/profile`, profile);
  }

  deleteAccount(): Observable<any> {
    return this.http.delete(`${this.apiUrl}/profile`);
  }
}
