
# Roll Call!

![img.png](img.png)

RollCall is a community-focused web application designed to bring gamers together by facilitating the organization and discovery of gaming events. Users can create, browse, and join events for board games, role-playing games, and card games. The platform also allows users to form and manage groups, fostering a sense of community among gamers.

## Features

- **User Authentication**: Secure login and registration system to manage user access.
- **Event Management**: Users can create, edit, and delete events within groups. Events can be viewed in a detailed list with information such as the event name, start and end times, and associated group.
- **Group Management**: Users can create, search, edit, and delete groups. Each group can host multiple events, creating a mini-community within the platform.
- **Profile Management**: Users can manage their profiles, including credentials and participation in events and groups.

## Planned Features

- **Game Catalog**: Introduce a comprehensive list of board games, role-playing games, and card games. Each event will be able to associate itself with a game from this catalog, enhancing event details and user experience.
- **Top Games Dropdown**: Implement a dropdown feature in the event creation form to suggest the top 10 popular games for easier selection.
- **User Comments**: Enable users to leave comments on event and group pages, fostering interaction and feedback within the community.
- **Geographical Tags**: Allow events to be tagged with geographical information. Users can search for events based on their location or browser location, making it easier to find local gaming communities.
- **Game Addition by Users**: Users can suggest new games to be added to the platform's catalog, pending approval by administrators. This feature encourages community involvement in growing the game database.

## Technical Details

- **Backend**: The application is built on the Spring Boot framework, utilizing Spring Security for authentication and authorization.
- **Database**: Uses JPA repositories with Hibernate for ORM, supporting a relational database management system (RDBMS) such as MySQL or PostgreSQL.
- **Frontend**: Thymeleaf templates for rendering server-side HTML, integrated with Bootstrap for responsive design.

## Setup and Installation

1. **Prerequisites**: Java 11+, Maven, and an RDBMS installed and configured.
2. **Clone the Repository**: `git clone https://yourrepositoryurl.com/rollcall.git`
3. **Configure Application Properties**: Adjust `src/main/resources/application.properties` with your database and server settings.
4. **Build and Run**: Execute `mvn spring-boot:run` from the root directory. The application will start on `http://localhost:8080`.

## Contribution

We welcome contributions to the RollCall project. If you have a feature request, bug report, or pull request, please open an issue or pull request on GitHub.

## License

[MIT License](LICENSE)

---

Remember to replace placeholder texts like the repository URL and adjust any specific installation instructions based on your environment or additional configurations.