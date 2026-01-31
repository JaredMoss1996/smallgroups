# Church Small Groups Finder

A web application to help protestant churches manage and find small groups. Based on the Church of the Highlands small group finder concept.

## Features

### Small Group Management
- Create and manage small groups with detailed information
- Filter groups by church name, location, denomination, age group, gender, and type
- Support for group images (via URL)
- Search functionality with multiple criteria
- View group details including meeting times, contact information, and accessibility features

### Authentication & Security
- User registration with email and password
- Login with email and password
- Google OAuth2 authentication support
- Protected endpoints requiring authentication for creating groups
- Session management with logout functionality

### User Interface
- Responsive design using Bootstrap 5
- Clean, modern login and signup pages
- Easy-to-use group creation form
- Interactive search interface with real-time filtering

## Technology Stack

- **Backend**: Spring Boot 3.2.1
- **Frontend**: Thymeleaf, Bootstrap 5, HTMX
- **Database**: H2 (in-memory)
- **Security**: Spring Security with OAuth2
- **Build Tool**: Maven

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository:
```bash
git clone https://github.com/JaredMoss1996/smallgroups.git
cd smallgroups
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

4. Open your browser and navigate to:
```
http://localhost:8080
```

## Configuration

### Google OAuth2 Setup

To enable Google OAuth2 authentication:

1. Go to the [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable the Google+ API
4. Create OAuth 2.0 credentials:
   - Application type: Web application
   - Authorized redirect URIs: `http://localhost:8080/login/oauth2/code/google`
5. Update `src/main/resources/application.properties`:
```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
```

## Usage

### Creating an Account
1. Navigate to the home page (redirects to login)
2. Click "Sign Up"
3. Fill in your name, email, and password
4. Click "Create Account"
5. You'll be redirected to login

### Finding Small Groups
1. After logging in, you'll see the "Find Groups" page
2. Use the search filters to narrow down groups:
   - Church name
   - Location
   - Denomination
   - Age group
   - Gender
   - Type
   - Special features (childcare, handicap accessible)
3. Results update automatically as you type or change filters

### Creating a Small Group
1. Log in to your account
2. Click "Create Group" in the navigation
3. Fill in the group details:
   - Basic information (name, church, description)
   - Location (city, address, coordinates)
   - Group details (age group, gender, type)
   - Meeting information
   - Contact information
   - Group size limits
   - Group image URL (optional)
4. Click "Create Small Group"

### Adding an Image to Your Group
When creating a group, you can add an image by providing a URL to an image hosted online. The image will be displayed on the group's card in the search results.

Supported image formats include JPEG, PNG, and GIF. For best results, use images with a 2:1 aspect ratio (e.g., 400x200 pixels).

## API Endpoints

### Authentication
- `POST /api/auth/signup` - Register a new user
- `POST /api/auth/login` - Login with email/password
- `GET /logout` - Logout

### Groups
- `GET /groups` - Get all groups
- `GET /groups/{id}` - Get a specific group
- `POST /groups` - Create a new group (requires authentication)
- `PUT /groups/{id}` - Update a group (requires authentication)
- `DELETE /groups/{id}` - Delete a group (requires authentication)
- `GET /groups/search` - Search groups with filters

## Database Schema

### Users Table
- `id` - Primary key
- `email` - User email (unique)
- `password` - Hashed password
- `name` - User's full name
- `provider` - Authentication provider (local/google)
- `enabled` - Account status

### Small Groups Table
- `id` - Primary key
- `name` - Group name
- `church_name` - Church name
- `denomination` - Church denomination
- `location` - City/area
- `address` - Full address
- `latitude`, `longitude` - GPS coordinates
- `description` - Group description
- `age_group` - Target age range
- `gender` - Gender specification
- `type` - Group type (Bible Study, Prayer, etc.)
- `meeting_day`, `meeting_time` - Meeting schedule
- `childcare_included` - Childcare availability
- `handicap_accessible` - Accessibility feature
- `contact_name`, `contact_email`, `contact_phone` - Contact info
- `current_size`, `max_size` - Group capacity
- `image_url` - URL to group image

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the MIT License.
