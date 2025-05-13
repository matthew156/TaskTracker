# TaskTracker

A command-line task management application that allows you to create, update, delete, and track the status of your tasks.

## Features

- Add new tasks
- Update existing tasks
- Delete tasks
- Mark tasks as in progress or done
- List all tasks
- Filter tasks by status (todo, in-progress, done)

## Installation

### Prerequisites

- Java JDK 11 or higher
- Maven

### Build Instructions

1. Clone the repository
```bash
git clone https://github.com/your-username/task-cli.git
cd task-cli
```

2. Build the application
```bash
mvn clean package
```

3. Create an alias for easier usage (optional)
```bash
# For Linux/Mac (add to ~/.bashrc or ~/.zshrc)
alias taskcli="java -jar /path/to/task-cli-1.0.jar"

# For Windows (create a batch file named taskcli.bat)
@echo off
java -jar C:\path\to\task-cli-1.0.jar %*
```

## Usage

### Adding a Task

```bash
taskcli add "Complete the project report"
```

This will create a new task with the status "todo" by default and display the generated UUID.

### Updating a Task

```bash
taskcli update <task-id> "Updated task description"
```

Replace `<task-id>` with the UUID of the task you want to update.

### Deleting a Task

```bash
taskcli delete <task-id>
```

Replace `<task-id>` with the UUID of the task you want to delete.

### Marking Task Status

Mark a task as in progress:
```bash
taskcli mark-in-progress <task-id>
```

Mark a task as done:
```bash
taskcli mark-done <task-id>
```

### Listing Tasks

List all tasks:
```bash
taskcli list
```

List only tasks with a specific status:
```bash
taskcli list todo        # List only todo tasks
taskcli list in-progress # List only in-progress tasks
taskcli list done        # List only completed tasks
```

## File Storage

Tasks are stored in a JSON file located at `~/.task-cli/tasks.json` by default.

## Dependencies

- Jackson - For JSON processing
- Picocli - For command-line interface
- org.json - For JSON manipulation

## Project Structure

```
org.matthew156
├── TaskTracker.java       # Main entry point
├── model
│   └── Task.java          # Task data model
└── utils
    ├── Status.java        # Enum for task status
    └── Constants.java     # Application constants
```
