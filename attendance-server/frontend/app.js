// Simple frontend for Attendance Management

// Configure this to your Spring Boot server's address
const API_BASE = 'http://localhost:8080/api';

const qs = s => document.querySelector(s);

// auth helpers
function setToken(t){ localStorage.setItem('jwt', t); qs('#btn-logout').style.display = t ? 'inline-block' : 'none'; }
function getToken(){ return localStorage.getItem('jwt'); }
function authFetch(path, opts={}){
  opts.headers = opts.headers || {};
  opts.headers['Content-Type'] = opts.headers['Content-Type'] || 'application/json';
  const token = getToken();
  if(token) opts.headers['Authorization'] = 'Bearer ' + token;
  return fetch(API_BASE + path, opts).then(async r => {
    const text = await r.text();
    let json = text ? JSON.parse(text) : null;
    if(!r.ok) throw json || {status:r.status, message: r.statusText};
    return json;
  });
}

// Navigation
function show(panel){ document.querySelectorAll('.panel').forEach(p=>p.style.display='none'); qs(panel).style.display='block'; }

// Login
qs('#nav-login').addEventListener('click', ()=> show('#panel-login'));
qs('#btn-login').addEventListener('click', async ()=>{
  const user = qs('#login-username').value;
  const pass = qs('#login-password').value;
  qs('#login-message').textContent = '';
  try{
    const res = await fetch(API_BASE + '/auth/login', {method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({username:user,password:pass})});
    const data = await res.json();
    setToken(data.token);
    qs('#login-message').textContent = 'Login successful';
    show('#panel-students'); loadStudents(); loadStudentsIntoSelect();
  }catch(e){ qs('#login-message').textContent = e.message || JSON.stringify(e); }
});

qs('#btn-logout').addEventListener('click', ()=>{ setToken(''); localStorage.removeItem('jwt'); show('#panel-login'); });

// Students
async function loadStudents(){
  try{
    const students = await authFetch('/students');
    const ul = qs('#students-list'); ul.innerHTML = '';
    students.forEach(s=>{
      const li = document.createElement('li');
      li.innerHTML = `<div><strong>${escapeHtml(s.name)}</strong> <span class="muted">(${escapeHtml(s.rollNumber)})</span></div>`;
      const actions = document.createElement('div'); actions.className='actions';
      const edit = document.createElement('button'); edit.textContent='Edit'; edit.onclick=()=> editStudent(s);
      const del = document.createElement('button'); del.textContent='Delete'; del.onclick=()=> deleteStudent(s.id);
      actions.appendChild(edit); actions.appendChild(del);
      li.appendChild(actions);
      ul.appendChild(li);
    });
  }catch(e){ qs('#students-message').textContent = e.message || JSON.stringify(e); }
}

qs('#btn-add-student').addEventListener('click', async ()=>{
  const name = qs('#student-name').value.trim();
  const roll = qs('#student-roll').value.trim();
  if(!name) return qs('#students-message').textContent = 'Name required';
  try{
    const s = await authFetch('/students', {method:'POST', body: JSON.stringify({name, rollNumber: roll})});
    qs('#student-name').value = ''; qs('#student-roll').value='';
    loadStudents(); loadStudentsIntoSelect();
  }catch(e){ qs('#students-message').textContent = e.message || JSON.stringify(e); }
});

function editStudent(s){
  const name = prompt('Name', s.name); if(name==null) return;
  const roll = prompt('Roll Number', s.rollNumber); if(roll==null) return;
  authFetch('/students/' + s.id, {method:'PUT', body: JSON.stringify({name, rollNumber: roll})}).then(()=>{ loadStudents(); loadStudentsIntoSelect(); }).catch(e=> qs('#students-message').textContent = e.message || JSON.stringify(e));
}

function deleteStudent(id){ if(!confirm('Delete student?')) return; authFetch('/students/' + id, {method:'DELETE'}).then(()=>{ loadStudents(); loadStudentsIntoSelect(); }).catch(e=> qs('#students-message').textContent = e.message || JSON.stringify(e)); }

// Attendance
qs('#nav-students').addEventListener('click', ()=>{ show('#panel-students'); loadStudents(); loadStudentsIntoSelect(); });
qs('#nav-attendance').addEventListener('click', ()=>{ show('#panel-attendance'); loadStudentsIntoSelect(); loadAttendance(); });

async function loadStudentsIntoSelect(){
  try{
    const students = await authFetch('/students');
    const sel = qs('#att-student-select'); sel.innerHTML='';
    students.forEach(s=>{ const o=document.createElement('option'); o.value=s.id; o.textContent=`${s.name} (${s.rollNumber})`; sel.appendChild(o); });
  }catch(e){ console.warn('loadStudentsIntoSelect', e); }
}

qs('#btn-mark-att').addEventListener('click', async ()=>{
  const studentId = qs('#att-student-select').value;
  const date = qs('#att-date').value;
  const present = qs('#att-present').checked;
  if(!studentId || !date) return qs('#attendance-message').textContent = 'Select student and date';
  try{
    const params = new URLSearchParams({studentId, date, present});
    const res = await authFetch('/attendance/mark?' + params.toString(), {method:'POST'});
    qs('#attendance-message').textContent = 'Marked attendance';
    loadAttendance();
  }catch(e){ qs('#attendance-message').textContent = e.message || JSON.stringify(e); }
});

qs('#btn-load-att').addEventListener('click', ()=> loadAttendance());

async function loadAttendance(){
  try{
    const date = qs('#att-filter-date').value;
    const url = '/attendance' + (date ? ('?date=' + date) : '');
    const items = await authFetch(url);
    const ul = qs('#attendance-list'); ul.innerHTML='';
    items.forEach(a=>{
      const li = document.createElement('li');
      li.innerHTML = `<div>${escapeHtml(a.studentName)} <span class="muted">${a.date}</span> - <strong>${a.present? 'Present':'Absent'}</strong></div>`;
      const del = document.createElement('button'); del.textContent='Delete'; del.onclick=()=> deleteAttendance(a.id);
      li.appendChild(del);
      ul.appendChild(li);
    });
  }catch(e){ qs('#attendance-message').textContent = e.message || JSON.stringify(e); }
}

function deleteAttendance(id){ if(!confirm('Delete attendance record?')) return; authFetch('/attendance/' + id, {method:'DELETE'}).then(()=> loadAttendance()).catch(e=> qs('#attendance-message').textContent = e.message || JSON.stringify(e)); }

// Health
qs('#nav-health').addEventListener('click', ()=> show('#panel-health'));
qs('#btn-check-health').addEventListener('click', async ()=>{
  try{
    // Note: The health check endpoint is usually not under /api
    const r = await fetch('http://localhost:8081/health');
    const txt = await r.text(); qs('#health-output').textContent = txt;
  }catch(e){ qs('#health-output').textContent = e.message || JSON.stringify(e); }
});

// Utilities
function escapeHtml(s){ if(s==null) return ''; return s.toString().replace(/[&<>"']/g, c=> ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":"&#39;"})[c]); }

// initial state
(function init(){ if(getToken()) qs('#btn-logout').style.display='inline-block'; else qs('#btn-logout').style.display='none'; show('#panel-login'); })();