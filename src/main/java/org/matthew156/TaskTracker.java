package org.matthew156;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.matthew156.model.Task;
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
import static picocli.CommandLine.Model.UsageMessageSpec.SECTION_KEY_COMMAND_LIST;

/**
 * Hello world!
 */

@Command(name = "task-cli", mixinStandardHelpOptions = true, version = "task-cli 1.0",
        description = "Accepts user actions and inputs as a argument to store in a JSON file", subcommands = {Add.class})
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
        System.out.println("Adding items");
        String filepath = "src/main/resources/TaskList.json";
        File file = new File(filepath);
        if (!file.exists()){
            file.createNewFile();
        }
        writeFile(file,filepath);




    }
    public void writeFile(File file, String filepath) throws IOException {
        Task task = new Task(description, Date.from(Instant.now()),Date.from(Instant.now()));
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = new String(Files.readAllBytes(Paths.get(filepath)));
        JSONObject jsonObject = jsonString.equals("")? new JSONObject() : new JSONObject(jsonString);
        FileWriter writer = new FileWriter(file);
        task.setId(jsonObject.length()+1);
        jsonObject.put(String.valueOf(task.getId()), mapper.writeValueAsString(task));
        writer.write(jsonObject.toString());
        writer.flush();
        writer.close();
    }

    @Override
    public Integer call() throws Exception {
        addTask();
        return 0;
    }
}

