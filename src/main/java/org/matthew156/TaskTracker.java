package org.matthew156;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.json.JSONObject;
import org.matthew156.model.Task;
import org.matthew156.utils.Status;
import picocli.CommandLine;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Callable;
import static org.matthew156.utils.Contants.FILE_PATH;
@Command(name = "task-cli", mixinStandardHelpOptions = true, version = "task-cli 1.0",
        description = "Accepts user actions and inputs as a argument to store in a JSON file", subcommands = {Add.class, Update.class,
        Delete.class, ListItems.class, MarkInProgress.class, MarkDone.class})
class TaskTracker{


    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new TaskTracker());
        cmd.execute(args);
    }

}
@Command(name = "add", description = "adds a task to the list")
class Add implements Callable<Integer>{
    @Parameters(index = "0", description = "description of task" )
    private String description;
    public void addTask() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()){
            file.createNewFile();
        }
        writeFile(file,FILE_PATH);




    }
    public void writeFile(File file, String filepath) throws IOException {
        Task task = new Task(description, new StdDateFormat().format(Date.from(Instant.now())),new StdDateFormat().format(Date.from(Instant.now())));
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat());
        String jsonString = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
        JSONObject jsonObject = jsonString.equals("")? new JSONObject() : new JSONObject(jsonString);
        FileWriter writer = new FileWriter(file);
        task.setId(UUID.randomUUID());
        JSONObject taskObject = new JSONObject(mapper.writeValueAsString(task));
        jsonObject.put(String.valueOf(task.getId()), taskObject);
        writer.write(jsonObject.toString());
        writer.flush();
        writer.close();
        System.out.println("Task added successfully (ID: " +task.getId() + ")");
    }

    @Override
    public Integer call() throws Exception {
        addTask();
        return 0;
    }
}
@Command(name = "update", description = "updates a task to the list by taking an id and description")
class Update implements Callable<Integer>{

    @Parameters(index = "0", description = "id of the task being updated")
    private UUID id;
    @Parameters(index = "1", description = "description of task" )
    private String description;

    public void updateTask() throws IOException {
        File file = new File(FILE_PATH);
        if(!file.exists() || file.length() == 0)
        {
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\n Example usage: taskcli update <id> <description>");
            System.exit(1);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            Map<UUID, Task>  map = mapper.readValue(jsonString, new TypeReference<Map<UUID, Task>>(){});
            Task task = map.get(id);
            task.setDescription(description);
            task.setUpdatedAt(new StdDateFormat().format(Date.from(Instant.now())));
            map.put(id, task);
            JSONObject json = new JSONObject(map);
            FileWriter writer = new FileWriter(file);
            writer.write(json.toString());
            writer.close();
            System.out.println("Updated ID: " + id);
        }
        catch (Exception e){
            System.out.println("Exception Thrown: " + e.getMessage() +"\n\n");
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\nExample usage: taskcli update <id> <description>");
            System.exit(1);
        }


    }
    @Override
    public Integer call() throws Exception {
        updateTask();
        return 0;
    }


  }

@Command(name = "delete", description = "deletes a task to the list by taking an id")
class Delete implements Callable<Integer>{

    @Parameters(index = "0", description = "id of the task being updated")
    private UUID id;


    public void deleteTask() throws IOException {
        File file = new File(FILE_PATH);
        if(!file.exists() || file.length() == 0)
        {
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\n Example usage: taskcli delete <id> <description>");
            System.exit(1);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            Map<UUID, Task>  map = mapper.readValue(jsonString, new TypeReference<Map<UUID, Task>>(){});
            map.remove(id);
            JSONObject json = new JSONObject(map);
            FileWriter writer = new FileWriter(file);
            writer.write(json.toString());
            writer.close();
            System.out.println("ID: " + id + " deleted.");

        }
        catch (Exception e){
            System.out.println("Exception Thrown: " + e.getMessage() +"\n\n");
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\nExample usage: taskcli update <id> <description>");
            System.exit(1);
        }


    }
    @Override
    public Integer call() throws Exception {
        deleteTask();
        return 0;
    }
}


@Command(name = "mark-in-progress", description = "shows the list of tasks created")
class MarkInProgress implements Callable<Integer>{
    @Parameters(index = "0", description = "id of the task being marked in progress")
    UUID id;

    @Override
    public Integer call() throws Exception {
        MarkStatus markStatus = new MarkStatus(Status.IN_PROGRESS.getName(), id);
        markStatus.updateStatus();
        return 0;
    }


}

class MarkStatus {
    private String status;
    private UUID id;

    public MarkStatus(String status, UUID id) {
        this.status = status;
        this.id = id;
    }


    public void updateStatus() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            System.out.println("File is either empty or missing task. Please add a task to update....");
            System.out.println("\n Example usage: taskcli list");
            System.exit(1);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            Map<UUID, Task> map = mapper.readValue(jsonString, new TypeReference<Map<UUID, Task>>() {
            });
            Task task = map.get(id);
            task.setStatus(status);
            task.setUpdatedAt(new StdDateFormat().format(Date.from(Instant.now())));
            map.put(id, task);
            JSONObject json = new JSONObject(map);
            FileWriter writer = new FileWriter(file);
            writer.write(json.toString());
            writer.close();
            System.out.println("Marked ID: " + id + " as " + status);

        } catch (Exception e) {
            System.out.println("Exception Thrown: " + e.getMessage() + "\n\n");
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\nExample usage: taskcli update <id> <description>");
            System.exit(1);
        }

    }
}

@Command(name = "mark-done", description = "shows the list of tasks created")
class MarkDone implements Callable<Integer>{
    @Parameters(index = "0", description = "id of the task being marked in progress")
    UUID id;
    @Override
    public Integer call() throws Exception {
        MarkStatus markStatus = new MarkStatus(Status.DONE.getName(), id);
        markStatus.updateStatus();
        return 0;
    }


}



@Command(name = "list", description = "shows the list of tasks created", subcommands = {ListItemsByDone.class, ListItemsByInProgress.class, ListItemsByTodo.class})
class ListItems implements Callable<Integer>{

    public void listItems() throws IOException {
        File file = new File(FILE_PATH);
        if(!file.exists() || file.length() == 0)
        {
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\n Example usage: taskcli list");
            System.exit(1);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            Map<UUID, Task>  map = mapper.readValue(jsonString, new TypeReference<Map<UUID, Task>>(){});

            List<Map.Entry<UUID, Task>> list = new ArrayList<>(map.entrySet());
            for (Map.Entry<UUID, Task> entry : list){
                System.out.println(entry.getKey() + ": " + mapper.writeValueAsString(entry.getValue()));
            }

        }
        catch (Exception e){
            System.out.println("Exception Thrown: " + e.getMessage() +"\n\n");
            System.out.println("File is either empty or missing. Please add a task to update....");
            System.out.println("\nExample usage: taskcli update <id> <description>");
            System.exit(1);
        }


    }
    @Override
    public Integer call() throws Exception {
        listItems();
        return 0;
    }


}

@Command(name = "in-progress", description = "shows the list of tasks marked in-progress")
class ListItemsByInProgress implements Callable<Integer>{
    @Override
    public Integer call() throws Exception {
        ListItemsByStatus listItemsByStatus = new ListItemsByStatus(Status.IN_PROGRESS.getName());
        listItemsByStatus.listItemsByStatus();
        return 0;
    }


}

@Command(name = "done", description = "shows the list of tasks marked done")
class ListItemsByDone implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {
        ListItemsByStatus listItemsByStatus = new ListItemsByStatus(Status.DONE.getName());
        listItemsByStatus.listItemsByStatus();
        return 0;
    }


}

@Command(name = "todo", description = "shows the list of tasks marked todo")
class ListItemsByTodo implements Callable<Integer>{
    @Override
    public Integer call() throws Exception {
        ListItemsByStatus listItemsByStatus = new ListItemsByStatus(Status.TODO.getName());
        listItemsByStatus.listItemsByStatus();
        return 0;
    }


}


    class ListItemsByStatus{
    private String status;

        public ListItemsByStatus(String status) {
            this.status = status;
        }

        public void listItemsByStatus()

        {
            File file = new File(FILE_PATH);
            if (!file.exists() || file.length() == 0) {
                System.out.println("File is either empty or missing. Please add a task to update....");
                System.out.println("\n Example usage: taskcli list");
                System.exit(1);
            }
            ObjectMapper mapper = new ObjectMapper();
            try {
                String jsonString = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
                Map<UUID, Task> map = mapper.readValue(jsonString, new TypeReference<Map<UUID, Task>>() {
                });

                List<Map.Entry<UUID, Task>> list = new ArrayList<>(map.entrySet());
                List<Map.Entry<UUID, Task>> resultList = new ArrayList<>();
                for (Map.Entry<UUID, Task> entry : list) {
                    if (entry.getValue().getStatus().equals(status))
                        resultList.add(entry);
                }
                if(resultList.isEmpty())
                {
                    System.out.println("There are no items for " + status + " status");
                }
                for (Map.Entry<UUID, Task> entry : resultList) {
                    System.out.println(entry.getKey() + ": " + mapper.writeValueAsString(entry.getValue()));
                }
            } catch (Exception e) {
                System.out.println("Exception Thrown: " + e.getMessage() + "\n\n");
                System.out.println("File is either empty or missing. Please add a task to update....");
                System.out.println("\nExample usage: taskcli update <id> <description>");
                System.exit(1);
            }
        }
    }