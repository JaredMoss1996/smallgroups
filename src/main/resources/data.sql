-- Sample churches
INSERT INTO churches (name, denomination, address, city, state, zip_code) VALUES
    ('First Baptist Church', 'Baptist', '123 Main St', 'Springfield', 'IL', '62701'),
    ('Grace Community Church', 'Non-Denominational', '456 Oak Ave', 'Springfield', 'IL', '62702'),
    ('St. Mary''s Catholic Church', 'Catholic', '789 Church Rd', 'Springfield', 'IL', '62703'),
    ('Calvary Chapel', 'Calvary Chapel', '321 Hope Ln', 'Springfield', 'IL', '62704'),
    ('Trinity Lutheran Church', 'Lutheran', '654 Faith Blvd', 'Springfield', 'IL', '62705');

-- Sample admin user (password: admin123)
INSERT INTO users (email, password, name, provider, enabled, role, approved_for_group_creation) VALUES
    ('admin@example.com', '$2a$10$sH7Z8W8eQQVlQx3n8vXXhO0h8S/xRjXVGX1p6pKXMCXdXmT0Y0fza', 'Admin User', 'local', true, 'ADMIN', true);
