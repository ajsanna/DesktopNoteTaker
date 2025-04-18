# AJ-Notes Note Taking Desktop App 

A modern, feature-rich desktop note-taking application built with Java Swing. AJ-Notes provides a clean, intuitive interface for creating, editing, and managing your notes with rich text formatting capabilities.



## Features

- 📝 Create and edit notes with rich text formatting
- 🎨 Customize text with different fonts, sizes, and colors
- 💾 Auto-save functionality
- 🔍 Quick search and organization
- 🎯 Dark mode support
- 📂 File system integration
- ⌨️ Keyboard shortcuts
- 🔄 Real-time preview

## Technical Details

### Architecture
AJ-Notes is built using:
- **Java 24**: Utilizes the latest Java features and performance improvements
- **Swing**: For the modern, responsive user interface
- **Maven**: For dependency management and build automation
- **JavaFX**: For enhanced UI components and animations

### Key Components
- **NoteManager**: Handles note creation, editing, and storage
- **UI Components**: Custom-built Swing components for a modern look
- **File System Integration**: Seamless integration with the local file system
- **Formatting Engine**: Rich text formatting capabilities

## Prerequisites

- Java 24 or later
- Maven 3.9.9 or later

## Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/aj-notes.git
cd aj-notes
```

2. Build the project:
```bash
mvn clean package
```

3. Run the application:
```bash
java -jar target/aj-notes-1.0.0.jar
```

## Usage

### Creating a New Note
1. Click the "New Note" button
2. Enter your content
3. Use the formatting toolbar to customize your text
4. Save your note (Ctrl+S or Cmd+S)

### Managing Notes
- **View All Notes**: Click the "View All Notes" button to see your note collection
- **Search**: Use the search bar to find specific notes
- **Organize**: Drag and drop notes to reorder them

### Formatting
- Change font family and size
- Apply bold, italic, and underline styles
- Customize text and background colors
- Use bullet points and numbered lists

## Keyboard Shortcuts

- `Ctrl+N` / `Cmd+N`: New note
- `Ctrl+S` / `Cmd+S`: Save note
- `Ctrl+O` / `Cmd+O`: Open note
- `Ctrl+F` / `Cmd+F`: Search notes
- `Ctrl+B` / `Cmd+B`: Bold text
- `Ctrl+I` / `Cmd+I`: Italic text
- `Ctrl+U` / `Cmd+U`: Underline text

## Development

### Project Structure
```
aj-notes/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── ajnotes/
│   │   │           └── AjNotes.java
│   │   └── resources/
│   └── test/
├── target/
├── pom.xml
└── README.md
```

### Building from Source
1. Ensure you have Java 24 and Maven installed
2. Clone the repository
3. Run `mvn clean package`
4. The executable JAR will be in the `target` directory

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Built with ❤️ using Java Swing
- Inspired by modern note-taking applications
- Special thanks to the Java community for their excellent documentation and tools

## Contact

For any questions or suggestions, please contact:
- Email: ajsanna@yahoo.com
- GitHub: [@ajsanna](https://github.com/ajsanna) 