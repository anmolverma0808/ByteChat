import { Injectable } from '@angular/core';
import { RxStomp, RxStompConfig } from '@stomp/rx-stomp';
import { map, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthService } from './auth.service';
import * as _SockJS from 'sockjs-client';
const SockJS = (_SockJS as any).default || _SockJS;

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private rxStomp: RxStomp;

  constructor(private authService: AuthService) {
    this.rxStomp = new RxStomp();
  }

  connect() {
    if (this.rxStomp.active) {
      return;
    }

    const token = this.authService.getToken();
    if (!token) {
      console.error('No token found, cannot connect to WebSocket');
      return;
    }

    const config: RxStompConfig = {
      webSocketFactory: () => new SockJS(environment.wsUrl),
      connectHeaders: {
        Authorization: `Bearer ${token}`
      },
      heartbeatIncoming: 0,
      heartbeatOutgoing: 20000,
      reconnectDelay: 5000,
      debug: (msg: string) => {
      }
    };

    this.rxStomp.configure(config);
    this.rxStomp.activate();
  }

  disconnect() {
    this.rxStomp.deactivate();
  }

  watchMessages(): Observable<any> {
    return this.rxStomp.watch('/user/queue/messages').pipe(
      map(message => JSON.parse(message.body))
    );
  }

  watchReadReceipts(): Observable<any> {
    return this.rxStomp.watch('/user/queue/read-receipts').pipe(
      map(message => JSON.parse(message.body))
    );
  }

  watchTypingIndicators(): Observable<any> {
    return this.rxStomp.watch('/user/queue/typing').pipe(
      map(message => JSON.parse(message.body))
    );
  }

  watchStatusUpdates(): Observable<any> {
    return this.rxStomp.watch('/topic/status').pipe(
      map(message => JSON.parse(message.body))
    );
  }

  sendMessage(destination: string, body: any) {
    this.rxStomp.publish({
      destination,
      body: JSON.stringify(body)
    });
  }

  watch(destination: string): Observable<any> {
    return this.rxStomp.watch(destination).pipe(
      map(message => JSON.parse(message.body))
    );
  }
}
