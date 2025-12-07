const API_BASE = window.location.origin;
const API_USERS = API_BASE + '/api/utilisateurs';
const API_LOGIN = API_USERS + '/login';
const API_REGISTER = API_USERS + '/register';

function showTab(tabName) {
    document.querySelectorAll('.tab').forEach(tab => tab.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));

    document.querySelector(`.tab[onclick="showTab('${tabName}')"]`).classList.add('active');
    document.getElementById(tabName + '-tab').classList.add('active');
}

function showMessage(elementId, message, type) {
    const el = document.getElementById(elementId);
    el.innerHTML = `<div class="alert alert-${type}">${message}</div>`;
    setTimeout(() => el.innerHTML = '', 4000);
}

async function register() {
    const user = {
        nom: document.getElementById('register-nom').value,
        email: document.getElementById('register-email').value,
        password: document.getElementById('register-password').value,
        role: document.getElementById('register-role').value
    };

    if (!user.nom || !user.email || !user.password) {
        showMessage('register-message', 'Veuillez remplir tous les champs', 'error');
        return;
    }

    try {
        const res = await fetch(API_REGISTER, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(user)
        });
        if (res.ok) {
            showMessage('register-message', '✅ Inscription réussie !', 'success');
            setTimeout(() => showTab('login'), 1500);
        } else {
            showMessage('register-message', '❌ Erreur lors de l\'inscription', 'error');
        }
    } catch (err) {
        showMessage('register-message', '❌ ' + err.message, 'error');
    }
}

async function login() {
    const credentials = {
        email: document.getElementById('login-email').value,
        password: document.getElementById('login-password').value
    };

    if (!credentials.email || !credentials.password) {
        showMessage('login-message', 'Veuillez remplir tous les champs', 'error');
        return;
    }

    try {
        const res = await fetch(API_LOGIN, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(credentials)
        });
        if (res.ok) {
            const user = await res.json();
            document.getElementById('currentUser').style.display = 'block';
            document.getElementById('userName').textContent = user.nom;
            document.getElementById('userRole').textContent = user.role;
            document.getElementById('userRole').className = 'badge badge-' + user.role;
            showMessage('login-message', `✅ Bienvenue ${user.nom} !`, 'success');
        } else {
            showMessage('login-message', '❌ Email ou mot de passe incorrect', 'error');
        }
    } catch (err) {
        showMessage('login-message', '❌ ' + err.message, 'error');
    }
}

function logout() {
    document.getElementById('currentUser').style.display = 'none';
    showTab('login');
}
