# Design System: ByteChat
**Project ID:** 12234154953291905358

> **Last Updated:** March 2026 — Re-analyzed from 7 live Stitch screens: Main Chat Interface (updated branding), Contacts Directory, Files Management (updated navigation), App Settings, Call Logs, Sign Up, and Connect Landing Page.

---

## 1. Visual Theme & Atmosphere

ByteChat embodies a **clean, professional productivity cockpit** — a real-time communication platform built for focused, organized work. The aesthetic is **crisp, structured, and confident**, drawing from modern SaaS design traditions while staying approachable. The interface radiates clarity across every screen, from the conversational three-pane chat layout to the data-rich Files Management view.

The mood is **light, purposeful, and task-oriented**. A vibrant Electric Azure Blue (`#258CF4`) accent system provides the brand's entire visual energy against an almost-all-white surface foundation. The three-pane desktop shell (icon-only sidebar → content list panel → primary content/detail panel) creates a natural left-to-right flow. The Landing Page presents a bolder, more expressive marketing personality — with a full-bleed blue CTA section and bold hero typography — while the app's internal screens settle into the calm, structured productivity aesthetic.

**Key Characteristics:**
- **Rocket-branded identity** — A circular Electric Azure Blue icon with a white rocket motif anchors the logo across all screens
- **Icon-only sidebar navigation** — Five compact icons (Chats, Files, Contacts, Settings + user avatar) replace text-label navigation, maximizing content space
- **Blue as the single accent** — All interactivity, selection, CTA, and brand moments are channelled through Electric Azure Blue (`#258CF4`)
- **Controlled density with breathing room** — Content-rich screens (Files table, Contacts list) use generous padding and subtle separators rather than hard borders
- **Dual personality** — Marketing screens (Landing Page) use expressive bold typography and full-bleed color sections; app screens are restrained and information-dense
- **Social authentication breadcrumb** — Sign Up features Google + GitHub social auth options, reflecting a developer/professional target audience

---

## 2. Color Palette & Roles

### Primary Foundation
- **Pure Cloud White** (`#FFFFFF`) — Dominant surface for all primary content areas: chat panels, form fields, card bodies, modal backgrounds, and the main content zone.
- **Frosted Surface Gray** (`#F3F4F6`) — Panel backgrounds (sidebar, conversation list, Contacts list), input resting states, file table row alternates, and the Sign Up page outer background. Provides whisper-thin spatial separation from pure white.
- **Mist Divider Gray** (`#E5E7EB`) — Hairline borders between panels, table row separators, card outlines, and structural dividers. Nearly imperceptible yet essential for spatial organization.

### Accent & Interactive
- **Electric Azure Blue** (`#258CF4`) — The singular brand color and interactive focal point. Applied to: primary CTA buttons (Sign In, Create Account, Upload File), active navigation icon states, active conversation highlights, selected contact rows, the hero heading accent word on the Landing Page, the Contacts detail panel header band, the full-bleed Landing Page CTA section, link text, toggle-on states, and the brand logo background.
- **Hover-Deepened Azure** (`#1A7ADE`) — Pressed/hover state of Electric Azure Blue. Applied via 200ms ease-in-out CSS transition across all interactive blue elements.
- **Soft Azure Tint** (`#EBF4FF`) — Active/selected row background in conversation and contacts lists; focus-ring fill on inputs; subtle highlight states.

### Semantic States
- **Emerald Online Green** (`#22C55E`) — Presence dot for online users (on avatars in all screens), success toast left-accent, positive call direction indicator in Call Logs.
- **Missed-Call Crimson** (`#EF4444`) — Missed call labels and badge counts in Call Logs, error states, form validation failures, critical alert badges, the Logout sidebar action.
- **Caution Amber** (`#F59E0B`) — Warning banners (Settings Danger Zone), near-full storage alerts, non-critical system warnings.
- **Muted Gray** (`#9CA3AF`) — Timestamps, file metadata, call durations, input placeholder text, inactive icon buttons, and Away presence indicators.

### Typography Roles
- **Deep Charcoal** (`#111827`) — Primary text for headings, names, conversation titles, settings labels, hero headline body text. Near-black with a cool undertone.
- **Graphite** (`#374151`) — Body copy, message bubble content, card descriptions, button text on light backgrounds.
- **Cool Slate** (`#6B7280`) — Muted secondary text: message previews, subtitles, contact sub-labels ("Away", "Last seen"), placeholder text, inactive tab labels, file table metadata.
- **Inverse White** (`#FFFFFF`) — Text on Electric Azure Blue backgrounds: CTA button labels, the Contacts detail panel header, the Landing Page CTA section, outgoing message bubble text.

### Overlay & Scrim
- **Modal Scrim** (`rgba(17, 24, 39, 0.5)`) — Semi-transparent overlay behind all modals and dialogs. Focused but not oppressively dark.

### Special: Landing Page
- **Hero Highlight Blue** (`#258CF4`) — Used as an inline text color on the hero headline to emphasize the keyword "anywhere." This is the single decorative use of the brand blue in a typographic context.
- **CTA Section Blue** (`#258CF4`) — Full-bleed section background for the "Ready to start connecting?" call-to-action panel, creating the boldest brand moment on the marketing page.

---

## 3. Typography Rules

**Primary Font Family:** Inter
**Character:** Modern geometric sans-serif built for digital screens. Large x-height, open apertures, and excellent density efficiency make it ideal for ByteChat's mix of dense list content and readable body copy.

### Hierarchy & Weights

| Level | Usage | Weight | Size | Notes |
|---|---|---|---|---|
| **Hero Headline** | Landing Page "ByteChat instantly with anyone, anywhere." | Black (900) | 2.5–3rem | Tight line-height (1.1), accent word in Electric Azure Blue |
| **Marketing Section Title** | "Powerful features for seamless communication" | Bold (700) | 1.5–1.75rem | Centered, generous top margin |
| **Page Title (H1)** | "Create account", "Account Settings", "Contacts" | Bold (700) | 1.5–2rem | −0.02em letter-spacing |
| **Section Header (H2)** | "Quick Access", "Recent Files", "Profile Information" | Semi-bold (600) | 1rem–1.125rem | 0 letter-spacing |
| **App Brand Wordmark** | "ByteChat" in top header bar | Semi-bold (600) | 1.125rem | Paired with circular rocket icon |
| **List Item Name** | Contact names, conversation names, file names | Semi-bold (600) | 0.9375rem (15px) | 0 letter-spacing |
| **Body / Message Text** | Chat messages, descriptions, card copy | Regular (400) | 0.875rem (14px) | 1.6 line-height |
| **Metadata / Caption** | Timestamps, file sizes, call durations, member counts | Regular (400) | 0.75rem (12px) | 0.01em, Cool Slate color |
| **Navigation Label** | *(Icon-only in current design — no visible labels)* | — | — | Icons carry navigation meaning |
| **Button Text** | All CTAs | Semi-bold (600) | 0.875rem (14px) | 0.01em letter-spacing |
| **Form Label** | Input labels ("Full Name", "Email Address") | Medium (500) | 0.875rem (14px) | Deep Charcoal, 4px margin below |
| **Table Column Header** | "NAME", "LAST MODIFIED", "SIZE", "MEMBERS" | Medium (500) | 0.6875rem (11px) | 0.08em uppercase letter-spacing, Cool Slate |

### Spacing Principles
- Hero headlines: **tight line-height (1.1)**, **negative letter-spacing (−0.02em)** for a punchy, bold feel
- Body text: **1.6 line-height** for comfortable reading in chat and descriptions
- Table column headers: **uppercase + wide letter-spacing (0.08em)** to differentiate from data rows
- **8px base grid** — all spacing tokens are multiples of 8

---

## 4. Component Stylings

### Buttons

**Primary CTA (Sign In, Create Account, Upload File, Start Chatting Now)**
- **Shape:** Gently rounded corners (8px/0.5rem) — professional and modern, not bubbly
- **Background:** Electric Azure Blue (`#258CF4`), white Semi-bold Inter text
- **Padding:** 0.75rem vertical, 1.5rem horizontal
- **Width:** Full-width within auth forms; auto-width on Landing Page; full-width on sidebar ("Upload File")
- **Hover:** Deepens to `#1A7ADE` over 200ms ease-in-out
- **Active/Pressed:** Scale nudges to 0.98 for subtle tactile feedback

**Secondary / Ghost Actions**
- **Style:** 1px Mist Divider Gray border, transparent background, Graphite text
- **Hover:** Fills with Frosted Surface Gray

**Social Auth Buttons (Google, GitHub — Sign Up screen)**
- **Style:** Pure Cloud White background, 1px Mist Divider Gray border, gently rounded (8px), with the provider's logo icon on the left
- **Width:** Side-by-side, equal width buttons in a 2-column row
- **Text:** "Google", "GitHub" in Graphite, Regular (400)
- **Divider:** "OR SIGN UP WITH" centered text in Cool Slate with horizontal rules flanking it

**Icon Buttons (Toolbar actions)**
- **Shape:** Circular (50%) or slightly rounded (8px) depending on context
- **Resting:** Muted Gray icon, transparent background
- **Hover:** Frosted Surface Gray fill, icon shifts toward Electric Azure Blue

### Cards & Containers

**Quick Access Category Tiles (Files Management)**
- **Shape:** Generously rounded corners (12px), Pure Cloud White background
- **Border:** 1px Mist Divider Gray, subtle shadow (`0 1px 3px rgba(0,0,0,0.06)`)
- **Content:** Color-coded file type icon (Images=blue, Documents=red/PDF, Spreadsheets=green, Media=purple) + category name + file count in Cool Slate
- **Grid:** 4-column responsive tile row, each tile equal-width
- **Hover:** Gentle lift with enhanced shadow (`0 4px 12px rgba(0,0,0,0.08)`) and a 3px Electric Azure Blue top border accent

**Recent Files Table (Files Management)**
- **Layout:** Full-width table with columns: Name / Last Modified / Size / Members
- **Row height:** ~52px with comfortable padding
- **File icon:** Colored by type (PDF=red, PNG=blue, XLSX=green, MP4=purple), 24px, left-aligned with file name stacked alongside a "Added X hours ago" sub-label in Cool Slate
- **Row hover:** Frosted Surface Gray fill with 150ms ease
- **Column headers:** Uppercase, wide letter-spacing, Cool Slate — very visually recessive relative to data

**Contact Detail Panel (Contacts screen)**
- **Header:** Full-bleed Electric Azure Blue (`#258CF4`) band (~120px tall) — the boldest use of the brand color within the app. The contact's large circular avatar (80px) bleeds across the edge of the header into the white content below.
- **Action Buttons Row:** "Message" (solid Electric Azure Blue, pill-shaped), "Audio" (outlined icon), "Video" (outlined icon), "More ···" (outlined icon) — centered beneath the avatar
- **Sections:** "Contact Information" + "Shared Media" side-by-side in Pure Cloud White, Cool Slate labels, Deep Charcoal values
- **Shared Media Grid:** 3-column square image thumbnails with generously rounded corners (8px) and a "+" add tile

**Settings Section Cards**
- **Background:** Pure Cloud White on Frosted Surface Gray page background
- **Border:** 1px Mist Divider Gray, 12px radius
- **Shadow:** `0 1px 3px rgba(0,0,0,0.08)`
- **Internal Padding:** 24px

**Chat Message Bubbles**
- **Outgoing:** Electric Azure Blue (`#258CF4`) background, white text, 18px radius, right-aligned
- **Incoming:** Frosted Surface Gray (`#F3F4F6`) background, Deep Charcoal text, 18px radius, left-aligned
- **File attachments:** Pure Cloud White card, 1px Mist Divider Gray border, 8px radius, file type icon + name + size + download action

**Landing Page Feature Tiles**
- **Shape:** Generously rounded corners (12px), Pure Cloud White background, 1px Mist Divider Gray border
- **Content:** Small Electric Azure Blue icon at top, feature name in Semi-bold (600), feature description in Cool Slate Regular (400)
- **Grid:** 3-column equal-width on desktop

**Landing Page CTA Section**
- **Background:** Full-bleed Electric Azure Blue (`#258CF4`) — the most expressive moment in the entire design system
- **Text:** Bold white headline, white subtext, white "Create free account" button with blue text (inverted)
- **Shape:** Generously rounded (12px) card set on a white page section, creating striking contrast

**Testimonial / Review Cards (Landing Page)**
- **Shape:** Pure Cloud White, 12px radius, 1px Mist Divider Gray border, subtle shadow
- **Star rating:** Yellow-orange stars (⭐), reviewer name in Semi-bold, review text in Cool Slate

### Navigation (Icon-Only Sidebar)

- **Width:** ~64px — compact, icon-only, no text labels
- **Icons visible:** Chat bubble (Chats), Folder (Files), Person (Contacts), Gear (Settings)
- **Active State:** Electric Azure Blue icon color with a Soft Azure Tint pill-shaped background highlight behind the icon
- **Inactive State:** Muted Gray (`#9CA3AF`) icon; 150ms color transition on hover toward Deep Charcoal
- **User Avatar:** Circular user avatar (36px) pinned at the very bottom of the sidebar — acts as the Profile/Account entry point
- **Logo area:** Circular Electric Azure Blue icon with white rocket motif (32px) at the top-left, adjacent to "ByteChat" wordmark in the header bar where present
- **No text labels:** Navigation meaning is conveyed entirely through icons — relies on established platform conventions

### Conversation List Tabs (Main Chat)

- **Tabs:** All / Unread / Groups — horizontal tab row at the top of the conversation list panel
- **Active Tab:** Electric Azure Blue text + 2px solid Electric Azure Blue bottom underline indicator
- **Inactive Tab:** Cool Slate text, flat, no background, 150ms hover transitions
- **Tab row:** 1px Mist Divider Gray full-width bottom border

### Search Bar

- **Shape:** Pill-shaped (999px radius) in all panels (chat, contacts, files, calls)
- **Background:** Frosted Surface Gray at rest; Pure Cloud White on focus with Electric Azure Blue border + Soft Azure Tint glow
- **Icon:** Magnifying glass in Muted Gray, left-inset; tints to Electric Azure Blue on focus
- **Placeholder text:** Cool Slate, disappears on first keystroke
- **Clear button:** `✕` icon appears on right when content is present

### Dropdowns & Select Menus

- **Shape:** 8px rounded, 1px Mist Divider Gray border, chevron-down in Muted Gray on the right
- **Panel:** Pure Cloud White with 8px radius, 1px border, drop shadow (`0 4px 12px rgba(0,0,0,0.1)`)
- **Selected state:** Electric Azure Blue checkmark aligned right; text stays Graphite
- **Usage:** Timezone selector on Sign Up (visible with "Pacific Time (PT) – UTC-8"), Email Digest Frequency in Settings

### Toggle Switches

- **Track (Off):** 24×14px pill, Mist Divider Gray fill
- **Track (On):** Electric Azure Blue fill
- **Thumb:** Circular white dot (10px), whisper-soft shadow, 200ms slide transition
- **Row layout:** Setting name (Semi-bold, Deep Charcoal) + descriptor (Cool Slate, 12px) on left; toggle flush right

### Checkboxes & Radio Buttons

- **Checkbox unchecked:** 16×16px, 4px radius, 1.5px Mist Divider Gray border, white fill
- **Checkbox checked:** Electric Azure Blue fill with white checkmark SVG
- **Radio unchecked/checked:** Same geometry as checkbox but circular; inner dot replaces checkmark on checked state

### Modals & Dialogs

- **Container:** Pure Cloud White, 12px radius, max-width 480px, centered
- **Scrim:** `rgba(17, 24, 39, 0.5)` behind
- **Shadow:** `0 20px 60px rgba(0,0,0,0.15)`
- **Entry animation:** Scale-in 0.95→1.0 + fade-in, 180ms ease-out
- **Footer:** Right-aligned — destructive in Crimson, confirm in Electric Azure Blue, cancel as ghost

### Toast Notifications & Snackbars

- **Position:** Fixed bottom-right (desktop), bottom-center (mobile)
- **Shape:** Pill (999px), Pure Cloud White background
- **Animation:** Slide-up + fade-in 200ms; auto-dismiss after 4s
- **Semantic variants:** 4px left accent bar in Green (success), Crimson (error), Amber (warning), Blue (info)

### Alert Banners

- **Placement:** Inline, full-width at top of a content zone
- **Structure:** Tinted background + 4px left-color border + icon + message + optional CTA + `✕` dismiss
- **Colors:** Success=`#F0FDF4`, Error=`#FEF2F2`, Warning=`#FFFBEB`

### Floating Action Button (FAB)

- **Shape:** Perfectly circular, 56px; Electric Azure Blue background, white icon
- **Shadow:** `0 4px 16px rgba(37,140,244,0.35)` — blue-tinted elevation
- **Position:** Bottom-right of list panel (conversation list, call list)

### Skeleton Loaders

- **Style:** Frosted Surface Gray placeholders matching content geometry
- **Animation:** 1.5s shimmer sweep (Frosted Gray → `#E9EAEC` → Frosted Gray)
- **Applied to:** Conversation list, contact list, file table, chat message area during data fetch

### Progress & Step Indicators

- **Storage Usage Bar (Files Management sidebar):** Thin (4px height), Frosted Surface Gray track, Electric Azure Blue fill. Label "Storage Usage — 79%" in Cool Slate at 12px. Positioned just above the "Upload File" CTA button.
- **File Upload Progress:** Full-width thin bar below file name in attachment previews
- **Step Indicator (multi-step flows):** Numbered circles + connector line; completed=blue filled, current=blue outline, future=gray outline

### Sticky Header & Fixed Footer

- **App Header (Chat):** Frosted Surface Gray / Pure Cloud White strip with ByteChat logo + "ByteChat" wordmark + search bar; spans full app width
- **Chat Panel Sticky Header:** Contact name + avatar + action icons; 1px Mist Divider Gray bottom border; ~56px
- **Chat Input Bar (Fixed Footer):** Pill-shaped input + attachment/emoji icons + circular send button; 1px Mist Divider Gray top border
- **Files Sidebar Footer:** Storage progress bar + "Upload File" full-width blue button anchored to bottom

---

## 5. Layout Principles

### Application Shell

- **Type:** Hybrid SPA — persistent icon-only sidebar + contextual multi-pane content area
- **Sidebar (always visible):** ~64px icon-only left rail; Frosted Surface Gray background; 1px Mist Divider Gray right border
- **App Header (where present):** Full-width Pure Cloud White bar with logo + wordmark on left, global search + notification bell + profile avatar on right; ~56px tall
- **Content Grid:** Right of sidebar splits into: list panel (~300–340px) + primary content/detail panel (fluid remaining width)
- **100vh fixed:** No page-level scroll; each panel scrolls independently

### Screen-Specific Layouts

| Screen | Layout Pattern |
|---|---|
| **Main Chat** | 3-pane: sidebar / conversation list with tabs / chat + right detail |
| **Call Logs** | 3-pane: sidebar / call list with tabs + search / contact detail |
| **Files Management** | 2-pane: sidebar (with storage bar) / full content area (header search + Quick Access tiles + Recent Files table) |
| **Contacts Directory** | 3-pane: sidebar / alphabetical contact list with search / contact detail with blue header |
| **App Settings** | 2-pane: sidebar navigation + settings section cards in a scrollable content zone |
| **Sign Up** | Single centered card (~480px) with photo hero at top, 2-column form, social auth |
| **Log In** | Single centered card with form fields, Forgot Password link, Sign Up link |
| **Landing Page** | Full-width scrolling marketing page: hero / features / testimonials / CTA / footer |

### Whitespace Strategy

- **Base unit:** 8px (all spacing tokens are multiples)
- **Component internal padding:** 16–24px
- **Card to card gaps:** 16px
- **Panel horizontal padding:** 16px in list panels, 24px in content areas
- **Section spacing on Landing Page:** 80–100px between major sections

### Grid & Breakpoints

- **Desktop (>1024px):** Full multi-pane layout, all panels visible
- **Tablet (768–1024px):** Two-pane; detail panel opens as overlay
- **Mobile (<768px):** Single-pane; sidebar becomes bottom tab bar
- **Landing Page max-width:** 1280px centered container, fluid hero
- **Auth screens (Sign Up / Log In):** ~480px centered card, full-page Frosted Surface Gray background

### Alignment

- **App internals:** Left-aligned throughout (productivity, not marketing)
- **Landing Page:** Center-aligned headings and features; left-aligned hero copy
- **Own messages:** Right-aligned in chat (messaging convention)
- **Table columns:** Left-aligned Name; right-aligned numeric metadata (Size, count)

---

## 6. Dark Mode Guidance

ByteChat's primary designed experience is **light mode**. A future dark variant should map as follows:

| Light Role | Light Value | Dark Equivalent |
|---|---|---|
| Canvas background | `#FFFFFF` | **Midnight Depths** `#0F172A` |
| Panel background | `#F3F4F6` | **Darkened Slate** `#1E293B` |
| Dividers | `#E5E7EB` | **Dim Steel** `#334155` |
| Primary text | `#111827` | `#F1F5F9` |
| Secondary text | `#6B7280` | `#94A3B8` |
| Primary accent | `#258CF4` | `#3B9EF8` (lightened) |
| Active tint | `#EBF4FF` | `rgba(59,158,248,0.15)` |
| Modal scrim | `rgba(17,24,39,0.5)` | `rgba(0,0,0,0.7)` |

**Dark Atmosphere:** Deep-space command center — high-contrast azure accents glowing against deep blue-gray surfaces. Outgoing bubbles stay Electric Azure Blue; incoming use Darkened Slate with near-white text.

---

## 7. Design System Notes for Stitch Generation

### Atmosphere Phrases
- *"Professional, light-mode productivity cockpit with electric blue accent system and icon-only sidebar navigation"*
- *"Clean three-panel desktop chat layout — Frosted Surface Gray panels, Pure Cloud White content areas, Electric Azure Blue (#258CF4) as the sole interactive color"*
- *"Corporate-casual SaaS design with focused whitespace, tabular data structures, and pill-shaped search inputs"*

### Color References

| Descriptive Name | Hex | Usage |
|---|---|---|
| Electric Azure Blue | `#258CF4` | Primary CTA, active states, brand icon |
| Pure Cloud White | `#FFFFFF` | Content surfaces, cards |
| Frosted Surface Gray | `#F3F4F6` | Panels, inputs at rest |
| Mist Divider Gray | `#E5E7EB` | Borders, separators, table rows |
| Deep Charcoal | `#111827` | Primary text |
| Cool Slate | `#6B7280` | Secondary/muted text |
| Emerald Online Green | `#22C55E` | Presence, success |
| Missed-Call Crimson | `#EF4444` | Errors, missed calls |
| Caution Amber | `#F59E0B` | Warnings |

### Shape Vocabulary

| CSS Equivalent | Natural Language for Stitch |
|---|---|
| `border-radius: 0` | sharp, squared-off edges |
| `border-radius: 4px` | slightly softened corners |
| `border-radius: 8px` | gently rounded corners (buttons, inputs, tiles) |
| `border-radius: 12px` | generously rounded corners (cards, modals, CTA section) |
| `border-radius: 18px` | very rounded, pillow-like (message bubbles) |
| `border-radius: 9999px` | pill-shaped (search bar, chat input, tags) |
| `border-radius: 50%` | perfectly circular (avatars, FAB, logo) |

### Per-Screen Component Prompts

**Main Chat Interface:**
*"Three-pane desktop chat — icon-only Electric Azure Blue sidebar, pill-shaped search bar in conversation list, All/Unread/Groups tabs with 2px blue underline indicator, outgoing bubbles in Electric Azure Blue (18px radius, right-aligned), incoming in Frosted Surface Gray (left-aligned), fixed chat input bar at panel bottom, circular blue FAB"*

**Files Management:**
*"Two-column layout: compact icon-only sidebar with storage progress bar and Upload File CTA at the bottom; main area with top search bar, 'Quick Access' 4-column category tile grid (generously rounded, color-coded icons), and tabular 'Recent Files' list (Name/Last Modified/Size/Members columns, file type icons, hairline row dividers)"*

**Contacts Directory:**
*"Three-pane contacts screen — icon-only sidebar; alphabetically grouped contacts list with pill search bar; right panel with full-bleed Electric Azure Blue (#258CF4) header band, large circular avatar overlapping the header edge, action buttons row (Message in solid blue pill, Audio/Video/More outlined), Contact Information + Shared Media sections below on white background"*

**Sign Up:**
*"Centered card (480px) on Frosted Surface Gray background — photo hero image at card top (fading into the form), 'Create account' H1, 2-column form (Full Name + Phone Number stacked alongside Email Address + Timezone dropdown + Password fields), full-width Electric Azure Blue Create Account button, OR divider, Google + GitHub social auth buttons side-by-side, 12px card radius"*

**Landing Page:**
*"Full-width marketing page — hero section with bold Black-weight headline ('ByteChat instantly with anyone, anywhere.' — word 'anywhere' in Electric Azure Blue), product screenshot mockup on right, Electric Azure Blue 'Start Chatting Now' CTA button; features section with 3-column white cards; testimonials; full-bleed Electric Azure Blue CTA banner card ('Ready to start connecting?') with white text and inverted button; footer with logo + link columns"*

### Incremental Iteration
1. Focus on ONE component per prompt
2. Always pair descriptive names with hex codes
3. Specify all interaction states (resting → hover → active → disabled)
4. Reference the shape vocabulary table above (never use CSS class names)
