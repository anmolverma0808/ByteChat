import { Injectable, signal, effect } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private readonly THEME_KEY = 'bytechat-theme';
  isDarkMode = signal<boolean>(this.getInitialTheme());

  constructor() {
    // Apply theme whenever it changes
    effect(() => {
      const mode = this.isDarkMode();
      if (mode) {
        document.documentElement.setAttribute('data-theme', 'dark');
        localStorage.setItem(this.THEME_KEY, 'dark');
      } else {
        document.documentElement.removeAttribute('data-theme');
        localStorage.setItem(this.THEME_KEY, 'light');
      }
    });
  }

  toggleTheme() {
    this.isDarkMode.update(v => !v);
  }

  private getInitialTheme(): boolean {
    const saved = localStorage.getItem(this.THEME_KEY);
    if (saved) {
      return saved === 'dark';
    }
    // Respect system preference if no manual setting exists
    return window.matchMedia('(prefers-color-scheme: dark)').matches;
  }
}
