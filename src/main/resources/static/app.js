const API_BASE_URL = '';

const isLoginPage = window.location.pathname.endsWith('index.html') || window.location.pathname === '/';
const isUsersPage = window.location.pathname.endsWith('users.html');

const tokenKey = 'token';

function getToken() {
    return localStorage.getItem(tokenKey);
}

function saveToken(token) {
    localStorage.setItem(tokenKey, token);
}

function removeToken() {
    localStorage.removeItem(tokenKey);
}

function parseJwt(token) {
    if (!token) return null;
    try {
        return JSON.parse(atob(token.split('.')[1]));
    } catch (e) {
        return null;
    }
}

function isTokenExpired(token) {
    const payload = parseJwt(token);
    if (!payload) return true;
    return payload.exp < Date.now() / 1000;
}

function getCurrentUser() {
    return parseJwt(getToken());
}

function isCurrentUserAdmin() {
    const user = getCurrentUser();
    if (!user || !user.roles) return false;
    return user.roles.includes('ROLE_ADMIN') || user.roles.includes('ADMIN');
}

async function login(username, password) {
    const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({username, password})
    });

    if (!response.ok) {
        const body = await response.json().catch(() => ({}));
        throw new Error(body.message || `Login failed: ${response.status}`);
    }

    const data = await response.json();
    return data.token || data.accessToken;
}

async function graphqlFetch(query) {
    const token = getToken();
    const response = await fetch(`${API_BASE_URL}/graphql`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({query})
    });

    if (!response.ok) {
        throw new Error(`GraphQL request failed: ${response.status}`);
    }

    const body = await response.json();
    if (body.errors && body.errors.length > 0) {
        throw new Error(body.errors[0].message);
    }

    return body.data;
}

async function createUser(username, email, password) {
    const query = 'mutation { createUser(username: "' + username + '", email: "' + email + '", password: "' + password + '") { id } }';
    return await graphqlFetch(query);
}

async function updateUser(id, email, password, enabled) {
    const emailArg = email ? '"' + email + '"' : 'null';
    const passwordArg = password ? '"' + password + '"' : 'null';
    const enabledArg = enabled === null ? 'null' : enabled;
    const query = 'mutation { updateUser(id: ' + id + ', email: ' + emailArg + ', password: ' + passwordArg + ', enabled: ' + enabledArg + ') { id } }';
    return await graphqlFetch(query);
}

async function deleteUser(id) {
    const query = 'mutation { deleteUser(id: ' + id + ') }';
    return await graphqlFetch(query);
}

async function logoutBackend() {
    const token = getToken();
    await fetch(`${API_BASE_URL}/api/auth/logout`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
}

async function getAllUsers() {
    const token = getToken();
    const query = `
        query {
            getAllUsers {
                id
                username
                email
                enabled
                roles
                createdAt
            }
        }
    `;

    const response = await fetch(`${API_BASE_URL}/graphql`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({query})
    });

    if (!response.ok) {
        throw new Error(`Failed to load users: ${response.status}`);
    }

    const body = await response.json();
    if (body.errors && body.errors.length > 0) {
        throw new Error(body.errors[0].message);
    }

    return body.data.getAllUsers;
}

if (isLoginPage) {
    const loginForm = document.getElementById('loginForm');
    const errorDiv = document.getElementById('error');

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        errorDiv.classList.remove('show');

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            const token = await login(username, password);
            saveToken(token);
            window.location.href = 'users.html';
        } catch (error) {
            errorDiv.textContent = error.message;
            errorDiv.classList.add('show');
        }
    });
}

if (isUsersPage) {
    const token = getToken();
    if (!token || isTokenExpired(token)) {
        removeToken();
        window.location.href = 'index.html';
    }

    const currentUser = getCurrentUser();
    const userInfoDiv = document.getElementById('userInfo');
    const viewModeDiv = document.getElementById('viewMode');

    if (userInfoDiv && currentUser) {
        const isAdmin = isCurrentUserAdmin();
        userInfoDiv.textContent = `Welcome, ${currentUser.sub || currentUser.username || 'User'} (${isAdmin ? 'Admin' : 'User'})`;
        if (viewModeDiv) {
            viewModeDiv.textContent = isAdmin ? 'Showing all users' : 'Showing users (admin accounts hidden)';
        }
    }

    const usersTableBody = document.getElementById('usersTableBody');
    const loadingDiv = document.getElementById('loading');
    const errorDiv = document.getElementById('error');
    const logoutBtn = document.getElementById('logoutBtn');
    const addUserBtn = document.getElementById('addUserBtn');
    const enabledHeader = document.getElementById('enabledHeader');
    const enabledSortArrow = document.getElementById('enabledSortArrow');

    let loadedUsers = [];
    // Cycles through: unsorted -> banned users first -> active users first -> unsorted
    let bannedSortOrder = 'none';

    function showError(message) {
        errorDiv.textContent = message;
        errorDiv.classList.add('show');
    }

    function clearError() {
        errorDiv.classList.remove('show');
        errorDiv.textContent = '';
    }

    function getSortedUsers() {
        if (bannedSortOrder === 'none') return loadedUsers;

        const sorted = [...loadedUsers];
        sorted.sort((a, b) => {
            const aBanned = a.enabled ? 1 : 0;
            const bBanned = b.enabled ? 1 : 0;
            return bannedSortOrder === 'banned-first' ? aBanned - bBanned : bBanned - aBanned;
        });
        return sorted;
    }

    function renderUsers() {
        enabledSortArrow.textContent =
            bannedSortOrder === 'banned-first' ? '▲ banned first' :
            bannedSortOrder === 'active-first' ? '▼ active first' : '';

        const users = getSortedUsers();

        usersTableBody.innerHTML = users.map(user => `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email || ''}</td>
                    <td><span class="status-badge ${user.enabled ? 'active' : 'banned'}">${user.enabled ? 'Active' : 'Banned'}</span></td>
                    <td>${(user.roles || []).join(', ')}</td>
                    <td>${user.createdAt ? new Date(user.createdAt).toLocaleString() : ''}</td>
                    <td>
                        <button class="btn btn-small edit-btn" data-id="${user.id}">Edit</button>
                        <button class="btn btn-small delete-btn" data-id="${user.id}">Delete</button>
                    </td>
                </tr>
            `).join('');

        usersTableBody.querySelectorAll('.edit-btn').forEach(btn => {
            btn.addEventListener('click', async () => {
                const id = btn.getAttribute('data-id');
                const email = prompt('Enter new email (or leave empty):');
                const password = prompt('Enter new password (or leave empty):');
                const enabledInput = prompt('Enabled? true/false (or leave empty):');
                const enabled = enabledInput ? enabledInput.toLowerCase() === 'true' : null;
                try {
                    await updateUser(id, email, password, enabled);
                    await loadUsers();
                } catch (error) {
                    showError(error.message);
                }
            });
        });

        usersTableBody.querySelectorAll('.delete-btn').forEach(btn => {
            btn.addEventListener('click', async () => {
                const id = btn.getAttribute('data-id');
                if (!confirm('Delete this user?')) return;
                try {
                    await deleteUser(id);
                    await loadUsers();
                } catch (error) {
                    showError(error.message);
                }
            });
        });
    }

    async function loadUsers() {
        loadingDiv.classList.add('show');
        clearError();

        try {
            loadedUsers = await getAllUsers();
            renderUsers();
            loadingDiv.classList.remove('show');
        } catch (error) {
            loadingDiv.classList.remove('show');
            showError(error.message);
        }
    }

    enabledHeader.addEventListener('click', () => {
        bannedSortOrder =
            bannedSortOrder === 'none' ? 'banned-first' :
            bannedSortOrder === 'banned-first' ? 'active-first' : 'none';
        renderUsers();
    });

    addUserBtn.addEventListener('click', async () => {
        const username = prompt('Enter username:');
        if (!username) return;
        const email = prompt('Enter email:');
        const password = prompt('Enter password:');
        try {
            await createUser(username, email, password);
            await loadUsers();
        } catch (error) {
            showError(error.message);
        }
    });

    logoutBtn.addEventListener('click', async () => {
        try {
            await logoutBackend();
        } catch (e) { /* network error ignored */
        }
        removeToken();
        window.location.href = 'index.html';
    });

    loadUsers();
}
