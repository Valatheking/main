package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessGeneral;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.ALICE_NRIC;
import static seedu.address.testutil.TypicalPersons.getTypicalPersonnelDatabaseWithDuties;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.duty.Duty;

/**
 * Contains integration tests (interaction with the Model) and unit tests for SwapCommand.
 */
public class SwapCommandTest {

    private Model model = new ModelManager(getTypicalPersonnelDatabaseWithDuties(), new UserPrefs());

    @Test
    public void execute() {
        List<Duty> duties = model.getDutyCalendar().getNextMonth().getScheduledDuties();
        int nextMonthIndex = model.getDutyCalendar().getNextMonth().getMonthIndex() + 1;
        int nextMonthYear = model.getDutyCalendar().getNextMonth().getYear();
        LocalDate allocatedDate = null;
        LocalDate requestedDate = null;
        for (Duty duty : duties) {
            if (duty.getPersons().contains(ALICE)) {
                allocatedDate = LocalDate.of(nextMonthYear, nextMonthIndex, duty.getDayIndex());
                break;
            }
        }
        for (Duty duty : duties) {
            if (!duty.getPersons().contains(ALICE)) {
                requestedDate = LocalDate.of(nextMonthYear, nextMonthIndex, duty.getDayIndex());
                break;
            }
        }

        assertCommandFailure(new SwapCommand(requestedDate, allocatedDate, ALICE_NRIC), model, new CommandHistory(),
                SwapCommand.MESSAGE_INVALID_DAY_ALLOCATED);
        assertCommandSuccessGeneral(new SwapCommand(allocatedDate, requestedDate, ALICE_NRIC), model,
                new CommandHistory(), new CommandResult(SwapCommand.MESSAGE_SUCCESS), model);
    }
}
