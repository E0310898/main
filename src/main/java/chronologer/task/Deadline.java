package chronologer.task;

import chronologer.parser.DateTimeExtractor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This extension of the task class will allow the user to add a task of
 * deadline type.
 *
 * @author Sai Ganesh Suresh
 * @version v1.4
 */
public class Deadline extends Task implements Serializable {

    private static final String DEADLINE = "DEADLINE";

    /**
     * Constructor for deadline task.
     *
     * @param description Description of the deadline
     * @param atDate      Due date for deadline
     */
    public Deadline(String description, LocalDateTime atDate) {
        super(description);
        this.startDate = atDate;
        setReminder(1);
        this.type = DEADLINE;
    }

    // @@author hanskw4267
    /**
     * Constructor for deadline task.
     *
     * @param description Description of the deadline
     * @param atDate      Due date for deadline
     */
    public Deadline(String description, LocalDateTime atDate, String modCode) {
        super(description);
        this.startDate = atDate;
        this.modCode = modCode;
        this.type = DEADLINE;
        setReminder(3);
    }
    // @@author

    @Override
    public String toString() {
        String message = "";
        if (modCode.isBlank()) {
            message = super.getPriorityIcon() + "[D]" + "[" + super.getStatusIcon() + "] " + this.description;
        } else {
            message = super.getPriorityIcon() + "[D]" + "[" + super.getStatusIcon() + "] " + this.modCode + " "
                    + this.description;
        }
        message = message + " (by: " + this.startDate.format(DateTimeExtractor.DATE_FORMATTER) + ")";
        // @@author hanskw4267
        if (!location.isBlank()) {
            message = message + "\n" + location;
        }
        if (!comment.isBlank()) {
            message = message + "\nNote to self: " + comment;
        }
        // @@author
        return message;
    }

    @Override
    public boolean isClash(Task taskToCheck) {
        if (taskToCheck.endDate == null) {
            return (this.startDate.isEqual(taskToCheck.startDate));
        } else {
            return (taskToCheck.startDate.isBefore(this.startDate) && taskToCheck.endDate.isAfter(this.startDate));
        }
    }

}
