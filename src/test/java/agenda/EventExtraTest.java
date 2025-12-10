package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

public class EventExtraTest {

    @Test
    public void simpleEventDefaultOccurrencesAndTerminationDate() {
        LocalDateTime start = LocalDateTime.of(2020, 11, 1, 10, 0);
        Duration duration = Duration.ofMinutes(60);
        Event simple = new Event("Simple", start, duration);

        assertEquals(1, simple.getNumberOfOccurrences());
        assertEquals(start.toLocalDate(), simple.getTerminationDate());
    }

    @Test
    public void neverEndingEventDefaultOccurrencesAndTerminationDate() {
        LocalDateTime start = LocalDateTime.of(2020, 11, 1, 22, 30);
        Duration duration = Duration.ofMinutes(120);
        Event neverEnding = new Event("Never ending", start, duration);
        neverEnding.setRepetition(ChronoUnit.DAYS);

        assertEquals(1, neverEnding.getNumberOfOccurrences());
        assertEquals(start.toLocalDate(), neverEnding.getTerminationDate());
    }

    @Test
    public void eventIsNotAfterTermination() {
        LocalDateTime start = LocalDateTime.of(2020, 11, 1, 22, 30);
        Duration duration = Duration.ofMinutes(120);

        Event fixed = new Event("Fixed", start, duration);
        fixed.setRepetition(ChronoUnit.WEEKS);
        LocalDate termination = LocalDate.of(2021, 1, 5);
        fixed.setTermination(termination);

        LocalDate after = termination.plusWeeks(1);
        assertFalse(fixed.isInDay(after));
    }
    @Test
    public void addExceptionOnNonRepetitiveEventDoesNothing() {
        LocalDateTime start = LocalDateTime.of(2020, 11, 1, 10, 0);
        Duration duration = Duration.ofMinutes(60);
        Event e = new Event("Simple", start, duration);

        e.addException(start.toLocalDate());
    }

    @Test
    public void setTerminationOnNonRepetitiveEventDoesNothing() {
        LocalDateTime start = LocalDateTime.of(2020, 11, 1, 10, 0);
        Duration duration = Duration.ofMinutes(60);
        Event e = new Event("Simple", start, duration);

        e.setTermination(LocalDate.of(2020, 11, 10));
        e.setTermination(5L);
    }

}
