import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalService } from '../../services/modal.service';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.css']
})
export class ConfirmDialogComponent {
  title = 'Confirm Action';
  message = 'Are you sure you want to proceed?';
  confirmText = 'Confirm';
  cancelText = 'Cancel';
  type: 'danger' | 'warning' | 'info' = 'info';

  constructor(private modalService: ModalService) {
    const active = this.modalService.activeModal();
    if (active?.data) {
      this.title = active.data.title || this.title;
      this.message = active.data.message || this.message;
      this.confirmText = active.data.confirmText || this.confirmText;
      this.cancelText = active.data.cancelText || this.cancelText;
      this.type = active.data.type || this.type;
    }
  }

  onConfirm() {
    this.modalService.resolveConfirm(true);
  }

  onCancel() {
    this.modalService.resolveConfirm(false);
  }
}
