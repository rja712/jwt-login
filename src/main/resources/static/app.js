const GRAPHQL_ENDPOINT = '/graphql';

// Check if we're on the login page or users page
const isLoginPage = window.location.pathname.endsWith('index.html') || window.location.pathname === '/';
const isUsersPage = window.location.pathname.endsWith('users.html');

// GraphQL query functions
async function graphqlQuery(query, variables = {}) {
    const response = await fetch(GRAPHQL_ENDPOINT, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            query,
            variables
        })
    });

    const data = await response.json();
    
    if (data.errors) {
        throw new Error(data.errors[0].message);
    }
    
    return data.data;
}

// Get all users
async function getAllUsers() {
    const query = `
        query {
            getAllUsers {
                id
                username
                email
                enabled
                createdAt
                roles
            }
        }
    `;
    
    const data = await graphqlQuery(query);
    return data.getAllUsers;
}

// Login page functionality
if (isLoginPage) {
    const loginForm = document.getElementById('loginForm');
    const errorDiv = document.getElementById('error');
    
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        errorDiv.classList.remove('show');
        
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        
        try {
            // For now, just redirect to users page since JWT auth is not implemented yet
            // In a real implementation, you would call a login mutation here
            window.location.href = 'users.html';
        } catch (error) {
            errorDiv.textContent = error.message;
            errorDiv.classList.add('show');
        }
    });
}

// Users page functionality
if (isUsersPage) {
    const usersTableBody = document.getElementById('usersTableBody');
    const loadingDiv = document.getElementById('loading');
    const errorDiv = document.getElementById('error');
    const logoutBtn = document.getElementById('logoutBtn');
    
    // Load users
    async function loadUsers() {
        loadingDiv.classList.add('show');
        errorDiv.classList.remove('show');
        
        try {
            const users = await getAllUsers();
            
            usersTableBody.innerHTML = users.map(user => `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>${user.enabled ? 'Yes' : 'No'}</td>
                    <td>${user.roles.join(', ')}</td>
                    <td>${new Date(user.createdAt).toLocaleString()}</td>
                </tr>
            `).join('');
            
            loadingDiv.classList.remove('show');
        } catch (error) {
            loadingDiv.classList.remove('show');
            errorDiv.textContent = error.message;
            errorDiv.classList.add('show');
        }
    }
    
    // Logout functionality
    logoutBtn.addEventListener('click', () => {
        window.location.href = 'index.html';
    });
    
    // Load users on page load
    loadUsers();
}
