# Frontend for Attendance Management

This is a minimal static frontend (vanilla JS) that interacts with the Attendance Management server API provided in this repository.

Files:
- `index.html` - main UI
- `app.js` - frontend logic (calls /api endpoints)
- `styles.css` - basic styles

How to use

1. Start the Spring server (the backend) so the API is available. By default the frontend points to the same origin's `/api` path.
2. Open `frontend/index.html` in your browser. If you serve it from the same origin as the backend (for example by placing files in `src/main/resources/static`), the API_BASE will be `window.location.origin + '/api'` and no changes are needed.
3. If the backend runs on a different host or port, edit `frontend/app.js` and set `API_BASE` to your backend API base, for example:

```js
const API_BASE = 'http://localhost:8080/api';
```

Features implemented
- Login (POST /api/auth/login) — stores JWT in localStorage
- Students CRUD (GET/POST/PUT/DELETE /api/students)
- Attendance mark/list/delete (POST /api/attendance/mark, GET /api/attendance, DELETE /api/attendance/{id})
- Health check (GET /health)

Notes
- This is intentionally small and dependency-free. For production use, add proper form validation, error handling, and CSRF protections if required.
