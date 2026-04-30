# SaafHisaab PWA

**SaafHisaab** ("Clear Accounts") — A mobile-first Progressive Web App for micro vendors to log daily sales and verify cash reconciliation at the end of every shift.

> This is the **frontend** repository. For the backend API, see [saafhisaab-engine]([../saafhisaab-engine](https://github.com/AFK10b/saafhisaab-engine)).

---

## What It Does

Small vendors — tea stalls, food carts, small shops — open a shift, log sales through the day, close the shift, and instantly get a reconciliation report showing whether the cash in hand matches what was sold.

---

## Screens

| Screen | Description |
|--------|-------------|
| **Login** | Mobile number + password authentication |
| **Owner Dashboard** | Open shift, view active shift, close shift |
| **Sale Entry** | Select menu item, set quantity, log sale |
| **Sales** | View all sales logged in the current shift |
| **Reconciliation** | Report showing opening cash, total sales, expected vs actual cash, discrepancy, and status |

---

## Tech Stack

- **React 18** — UI framework
- **Vite** — Build tool and dev server
- **React Router v6** — Client-side routing
- **Axios** — HTTP client for backend API calls
- **Tailwind CSS** — Utility-first styling
- **PWA** — Installable on mobile via browser

---

## Project Structure

```
saafhisaab-pwa/
├── public/
│   ├── favicon.svg
│   └── icons.svg
└── src/
    ├── api/
    │   └── axios.js              # Axios instance with base URL and JWT interceptor
    ├── components/
    │   └── ProtectedRoute.jsx    # Redirects to login if user is not authenticated
    ├── context/
    │   └── AuthContext.jsx       # Global auth state — user, token, login, logout
    ├── pages/
    │   ├── Login.jsx             # Login form
    │   ├── OwnerDashboard.jsx    # Shift open/close and navigation hub
    │   ├── SaleEntry.jsx         # Log a sale against the active shift
    │   ├── Sales.jsx             # View all sales for the current shift
    │   └── Reconciliation.jsx    # Post-shift reconciliation report
    ├── App.jsx                   # Route definitions
    ├── main.jsx                  # App entry point
    └── index.css                 # Global styles
```

---

## Getting Started

### Prerequisites

- Node.js 18+
- npm
- SaafHisaab backend running (see [saafhisaab-engine](../saafhisaab-engine))

### Install

```bash
cd saafhisaab-pwa
npm install
```

### Run in Development

```bash
npm run dev
```

App will be available at `http://localhost:5173`

### Build for Production

```bash
npm run build
```

### Preview Production Build

```bash
npm run preview
```

### Lint

```bash
npm run lint
```

---

## Backend Connection

This frontend connects to the Spring Boot backend API.

Update the base URL in `src/api/axios.js` to point to your backend:

```js
const api = axios.create({
    baseURL: 'http://localhost:8080/api'
})
```

For production, replace with your deployed backend URL.

---

## Authentication

- JWT-based stateless authentication
- Token stored in memory via `AuthContext`
- All protected routes wrapped in `ProtectedRoute`
- JWT attached automatically to every request via Axios interceptor

---

## MVP Flow

```
1. Login  →  2. Open Shift  →  3. Log Sales  →  4. Close Shift  →  5. View Report
```

Reconciliation statuses:

| Status | Condition | Meaning |
|--------|-----------|---------|
| ✅ CLEAR | Discrepancy = ₹0 | Perfect match |
| ⚠️ WARNING | Discrepancy ≤ ₹50 | Small gap, acceptable |
| 🚨 ALERT | Discrepancy > ₹50 | Needs investigation |

---

## Planned Features

- Menu management UI (add/edit/delete items)
- Shift history and past reports
- Staff role with limited access
- Dashboard analytics and charts
- Full offline PWA support

---

## Related

- [saafhisaab-engine](../saafhisaab-engine) — Spring Boot backend (REST API, JWT auth, reconciliation engine)
