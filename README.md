📚 BookShelf — Library Management System
A lightweight and functional desktop application for tracking books. Built with Java Swing, it features a modern look, local database integration, and a flexible theme engine.

🛠 Tech Stack

	Language: Java
	GUI Framework: Swing + FlatLaf (Modern Look & Feel)
	Database: SQLite (Embedded, no server setup required)

✨ Core Features

    CRUD Operations: Create, Read, Update, and Delete book records.
    Book Tracking: Manage statuses (Available, Issued, Archived).
    Advanced Filtering: Instantly filter your collection by genre, author, or status.
    Customization: Integrated theme manager with support for:
        🌙 Dark / Darcula
        ☀️ Light
        🍎 macOS Dark / Light
    Persistent Settings: The app automatically saves and restores your chosen theme on startup.

🚀 Getting Started

    Requirements: Ensure you have JDK 11 or higher installed.
    Installation:
        Clone the repository.
        Ensure the required libraries (flatlaf.jar and sqlite-jdbc.jar) are included in your project's classpath.
    Run: Launch the application via the Main class.

📂 Project Structure

    source/ — Core business logic, validators, and book models.
    ui/ — GUI components, theme management, and dialogs.
    database/ — SQLite connection and query logic.

⚙️ How it Works

    Database: On the first run, the app automatically creates a .db file to store your collection.
    Themes: Go to "Settings" (Параметры) to switch themes on the fly. The UI refreshes instantly without needing a restart.
    Persistence: Your preferences are stored using the Java Preferences API, so your UI choices stay consistent.
