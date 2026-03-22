import { Component, EventEmitter, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../../../core/services/chat.service';

@Component({
  selector: 'app-message-input',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './message-input.component.html',
  styleUrls: ['./message-input.component.css']
})
export class MessageInputComponent {
  @Output() messageSent = new EventEmitter<any>();
  @Output() typing = new EventEmitter<boolean>();
  
  messageText = '';
  private typingTimer: any;
  private lastTypingSentTime = 0;
  private readonly TYPING_THROTTLE = 2000; // 2 seconds
  private readonly TYPING_TIMEOUT = 3000; // 3 seconds
  selectedFile = signal<File | null>(null);
  showEmojiPicker = signal(false);

  emojis = [
    '😀', '😃', '😄', '😁', '😅', '😂', '🤣', '😊', '😇', '🙂', '🙃', '😉', '😌', '😍', '🥰', '😘', '😗', '😙', '😚', '😋', '😛', '😝', '😜', '🤪', '🤨', '🧐', '🤓', '😎', '🤩', '🥳', '😏', '😒', '😞', '😔', '😟', '😕', '🙁', '☹️', '😣', '😖', '😫', '😩', '🥺', '😢', '😭', '😤', '😠', '😡', '🤬', '🤯', '😳', '🥵', '🥶', '😱', '😨', '😰', '😥', '😓', '🤗', '🤔', '🤭', '🤫', '🤥', '😶', '😐', '😑', '😬', '🙄', '😯', '😦', '😧', '😮', '😲', '🥱', '😴', '🤤', '😪', '😵', '🤐', '🥴', '🤢', '🤮', '🤧', '😷', '🤒', '🤕', '🤑', '🤠', '😈', '👿', '👹', '👺', '🤡', '👻', '💀', '☠️', '👽', '👾', '🤖', '💩', '😺', '😸', '😹', '😻', '😼', '😽', '🙀', '😿', '😾',
    '👋', '🤚', '🖐', '✋', '🖖', '👌', '🤏', '✌️', '🤞', '🤟', '🤘', '🤙', '👈', '👉', '👆', '🖕', '👇', '☝️', '👍', '👎', '✊', '👊', '🤛', '🤜', '👏', '🙌', '👐', '🤲', '🤝', '🙏', '✍️', '💅', '🤳', '💪', '🦾', '🦵', '🦿', '🦶', '👂', '🦻', '👃', '🧠', '🦷', '🦴', '👀', '👁', '👅', '👄', '💋', '🩸',
    '❤️', '🧡', '💛', '💚', '💙', '💜', '🖤', '🤍', '🤎', '💔', '❣️', '💕', '💞', '💓', '💗', '💖', '💘', '💝', '💟', '☮️', '✝️', '☪️', '🕉', '☸️', '✡️', '🔯', '🕎', '☯️', '☦️', '🛐', '⛎', '♈️', '♉️', '♊️', '♋️', '♌️', '♍️', '♎️', '♏️', '♐️', '♑️', '♒️', '♓️', '🆔', '⚛️'
  ];

  constructor(private chatService: ChatService) {}

  onTextChange() {
    const now = Date.now();
    
    // Send "typing" status if throttled
    if (now - this.lastTypingSentTime > this.TYPING_THROTTLE) {
      this.typing.emit(true);
      this.lastTypingSentTime = now;
    }

    // Reset timeout timer
    if (this.typingTimer) {
      clearTimeout(this.typingTimer);
    }
    
    this.typingTimer = setTimeout(() => {
      this.typing.emit(false);
      this.lastTypingSentTime = 0;
    }, this.TYPING_TIMEOUT);
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile.set(file);
    }
  }

  toggleEmojiPicker() {
    this.showEmojiPicker.update(v => !v);
  }

  addEmoji(emoji: string) {
    this.messageText += emoji;
    this.showEmojiPicker.set(false);
  }

  send() {
    if (this.messageText.trim() || this.selectedFile()) {
      const payload = {
        text: this.messageText,
        file: this.selectedFile()
      };
      
      this.messageSent.emit(payload);
      
      this.messageText = '';
      this.selectedFile.set(null);
      this.showEmojiPicker.set(false);

      if (this.typingTimer) {
        clearTimeout(this.typingTimer);
      }
      this.typing.emit(false);
      this.lastTypingSentTime = 0;
    }
  }
}
