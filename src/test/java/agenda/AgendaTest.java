package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AgendaTest {
    Agenda agenda;

    // November 1st, 2020
    LocalDate nov_1_2020 = LocalDate.of(2020, 11, 1);

    // January 5, 2021
    LocalDate jan_5_2021 = LocalDate.of(2021, 1, 5);

    // November 1st, 2020, 22:30
    LocalDateTime nov_1_2020_22_30 = LocalDateTime.of(2020, 11, 1, 22, 30);

    // 120 minutes
    Duration min_120 = Duration.ofMinutes(120);

    // Un événement simple
    // November 1st, 2020, 22:30, 120 minutes
    Event simple;

    // Un événement qui se répète toutes les semaines et se termine à une date
    // donnée
    Event fixedTermination;

    // Un événement qui se répète toutes les semaines et se termine après un nombre
    // donné d'occurrences
    Event fixedRepetitions;

    // A daily repetitive event, never ending
    // Un événement répétitif quotidien, sans fin
    // November 1st, 2020, 22:30, 120 minutes
    Event neverEnding;

    @BeforeEach
    public void setUp() {
        simple = new Event("Simple event", nov_1_2020_22_30, min_120);

        fixedTermination = new Event("Fixed termination weekly", nov_1_2020_22_30, min_120);
        fixedTermination.setRepetition(ChronoUnit.WEEKS);
        fixedTermination.setTermination(jan_5_2021);

        fixedRepetitions = new Event("Fixed termination weekly", nov_1_2020_22_30, min_120);
        fixedRepetitions.setRepetition(ChronoUnit.WEEKS);
        fixedRepetitions.setTermination(10);

        neverEnding = new Event("Never Ending", nov_1_2020_22_30, min_120);
        neverEnding.setRepetition(ChronoUnit.DAYS);

        agenda = new Agenda();
        agenda.addEvent(simple);
        agenda.addEvent(fixedTermination);
        agenda.addEvent(fixedRepetitions);
        agenda.addEvent(neverEnding);
    }

    @Test
    public void testMultipleEventsInDay() {
        assertEquals(4, agenda.eventsInDay(nov_1_2020).size(),
                "Il y a 4 événements ce jour là");
        assertTrue(agenda.eventsInDay(nov_1_2020).contains(neverEnding));
    }

    @Test
    public void eventsInDayReturnsEmptyListWhenNoEvent() {
        Agenda agenda = new Agenda();
        LocalDate day = LocalDate.of(2020, 11, 1);
        assertTrue(agenda.eventsInDay(day).isEmpty());
    }

    @Test
    public void findByTitleOnEmptyAgendaReturnsEmptyList() {
        Agenda agenda = new Agenda();
        assertTrue(agenda.findByTitle("Titre").isEmpty());
    }

    @Test
    public void isFreeForOnEmptyAgendaIsTrue() {
        Agenda agenda = new Agenda();
        LocalDateTime start = LocalDateTime.of(2020, 11, 1, 10, 0);
        Duration oneHour = Duration.ofMinutes(60);
        Event e = new Event("Test", start, oneHour);

        assertTrue(agenda.isFreeFor(e));
    }
    @Test
    public void isFreeForWhenEventEndsExactlyAtExistingStart() {
        Agenda agenda = new Agenda();
        LocalDateTime start = LocalDateTime.of(2020, 11, 1, 10, 0);
        Duration oneHour = Duration.ofMinutes(60);

        Event existing = new Event("E1", start, oneHour);           // 10h–11h
        agenda.addEvent(existing);

        Event candidate = new Event("Before", start.minusHours(1), oneHour); // 9h–10h

        assertTrue(agenda.isFreeFor(candidate));
    }

    @Test
    public void isFreeForWhenEventStartsExactlyAtExistingEnd() {
        Agenda agenda = new Agenda();
        LocalDateTime start = LocalDateTime.of(2020, 11, 1, 10, 0);
        Duration oneHour = Duration.ofMinutes(60);

        Event existing = new Event("E1", start, oneHour);           // 10h–11h
        agenda.addEvent(existing);

        Event candidate = new Event("After", start.plusHours(1), oneHour); // 11h–12h

        assertTrue(agenda.isFreeFor(candidate));
    }


}
