import axios from "axios";

const API_BASE = "http://localhost:8080/api";

export function getToken() {
  return localStorage.getItem("token");
}

export function setToken(token) {
  localStorage.setItem("token", token);
}

export function logout() {
  localStorage.removeItem("token");
}

export function authHeaders() {
  const token = getToken();
  return token ? { Authorization: token } : {};
}

// --- AUTH for Basic Auth ---
export async function login(username, password) {
  const token = "Basic " + btoa(username + ":" + password);
  setToken(token);
  return { token };
}
