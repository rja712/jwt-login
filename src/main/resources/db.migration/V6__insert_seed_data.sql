-- Insert seed roles
INSERT INTO roles (name) VALUES 
    ('ROLE_USER'),
    ('ROLE_ADMIN')
ON CONFLICT (name) DO NOTHING;

-- Insert seed permissions
INSERT INTO permissions (name) VALUES 
    ('READ_USER'),
    ('WRITE_USER'),
    ('DELETE_USER'),
    ('READ_ADMIN'),
    ('WRITE_ADMIN'),
    ('DELETE_ADMIN')
ON CONFLICT (name) DO NOTHING;

-- Insert seed users (passwords are BCrypt encoded for 'password123')
INSERT INTO users (username, email, password, enabled) VALUES 
    ('john_doe', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true),
    ('jane_smith', 'jane@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true),
    ('admin_user', 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true)
ON CONFLICT (username) DO NOTHING;

-- Assign roles to users
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'john_doe' AND r.name = 'ROLE_USER'
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'jane_smith' AND r.name = 'ROLE_USER'
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'admin_user' AND r.name = 'ROLE_ADMIN'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- Assign permissions to roles
INSERT INTO role_permissions (role_id, permission_id) 
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'ROLE_USER' AND p.name IN ('READ_USER', 'WRITE_USER')
ON CONFLICT (role_id, permission_id) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id) 
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'ROLE_ADMIN' AND p.name IN ('READ_USER', 'WRITE_USER', 'DELETE_USER', 'READ_ADMIN', 'WRITE_ADMIN', 'DELETE_ADMIN')
ON CONFLICT (role_id, permission_id) DO NOTHING;
