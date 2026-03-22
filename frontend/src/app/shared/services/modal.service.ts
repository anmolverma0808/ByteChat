import { Injectable, signal, Type } from '@angular/core';
import { ConfirmDialogComponent } from '../components/confirm-dialog/confirm-dialog.component';

export interface ModalData {
  component: Type<any>;
  data?: any;
  title?: string;
  size?: 'sm' | 'md' | 'lg' | 'full';
}

@Injectable({
  providedIn: 'root'
})
export class ModalService {
  activeModal = signal<ModalData | null>(null);
  private confirmResolve?: (value: boolean) => void;

  open(modal: ModalData) {
    this.activeModal.set(modal);
  }

  confirm(title: string, message: string, confirmText = 'Confirm', cancelText = 'Cancel', type: 'danger' | 'warning' | 'info' = 'info'): Promise<boolean> {
    return new Promise((resolve) => {
      this.confirmResolve = resolve;
      this.open({
        component: ConfirmDialogComponent,
        title: title,
        size: 'sm',
        data: { title, message, confirmText, cancelText, type }
      });
    });
  }

  resolveConfirm(value: boolean) {
    if (this.confirmResolve) {
      this.confirmResolve(value);
      this.confirmResolve = undefined;
    }
    this.close();
  }

  close() {
    this.activeModal.set(null);
  }
}
