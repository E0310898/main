package chronologer.command;

import chronologer.task.Event;
import chronologer.task.Task;
import chronologer.task.TaskList;
import chronologer.ui.Ui;
import chronologer.storage.Storage;
import chronologer.ui.UiTemporary;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Finds the next free time slot of a duration of user's choosing.
 *
 * @author Hans kurnia
 * @version 1.3
 */
public class SearchCommand extends Command {

    private static final String NO_CLASH = "You have no events that will clash with a slot of this duration";
    private long durationToFind;

    public SearchCommand(Long duration) {
        this.durationToFind = duration;
    }

    public long getDurationToFind() {
        return durationToFind;
    }

    /**
     * Searches for the next available time slot based on the user's duration.
     *
     * @param tasks   Holds the list of all the tasks the user has.
     * @param storage Allows the saving of the file to persistent storage.
     */
    public void execute(TaskList tasks, Storage storage) {
        ArrayList<Event> dateList = new ArrayList<Event>();
        boolean found = false;
        long duration;

        for (Task item : tasks.getTasks()) {
            if (item.getClass() == Event.class) {
                dateList.add((Event) item);
            }
        }

        Collections.sort(dateList);

        if (dateList.size() == 0) {
            UiTemporary.printOutput(NO_CLASH);
            found = true;
        } else if (dateList.size() == 1) {
            UiTemporary.printOutput("You can schedule something after " + dateList.get(0).toString());
        } else {
            for (int i = 0; i < dateList.size(); i++) {
                if (i != 0) {
                    duration = ChronoUnit.HOURS.between(dateList.get(i - 1).getEndDate(),
                            dateList.get(i).getStartDate());
                } else {
                    duration = ChronoUnit.HOURS.between(LocalDateTime.now(), dateList.get(i).getStartDate());
                }
                if (durationToFind <= duration) {
                    if (i != 0) {
                        UiTemporary.printOutput(
                                "Next free time slot of duration " + Long.toString(durationToFind) + "hrs is between \n"
                                        + dateList.get(i - 1).toString() + " and " + dateList.get(i).toString());
                    } else {
                        UiTemporary.printOutput("You can schedule something from now till "
                            + dateList.get(i).toString());
                    }
                    found = true;
                    break;
                }
            }

            if (!found) {
                UiTemporary.printOutput("You can schedule something after the "
                    + dateList.get(dateList.size() - 1).toString());
            }
        }

    }
}