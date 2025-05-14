package org.matthew156.utils;

/**
 * Utility class for holding application-wide constant values.
 * <p>
 * This class is not meant to be instantiated. All members are static.
 * It centralizes important string literals or other constants used throughout the application,
 * making them easier to manage and modify.
 * </p>
 *
 * @author Matthew156 (Or your actual name/alias)
 * @version 1.0
 * @since YYYY-MM-DD (Date of creation)
 */
public class Constants {

    /**
     * The default file path for storing or retrieving the task list.
     * This path points to a JSON file named "TaskList.json" located
     * within the "src/main/resources" directory of the project.
     */
    public static final String FILE_PATH = "src/main/resources/TaskList.json";

    /**
     * Private constructor to prevent instantiation of this utility class.
     * <p>
     * As this class only contains static members (constants), it should not be instantiated.
     * Making the constructor private enforces this design.
     * </p>
     * @throws IllegalStateException if an attempt is made to instantiate this utility class.
     */
    private Constants() {
        // This utility class is not meant to be instantiated.
        throw new IllegalStateException("Utility class Contants should not be instantiated.");
    }
}