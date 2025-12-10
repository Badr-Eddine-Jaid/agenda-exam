package agenda;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class AgendaTestComplementaire {

    @Test
    public void findByTitleReturnsAllMatchingEvents() {
        Agenda agenda = new Agenda();
        LocalDateTime d = LocalDateTime.of(2020, 11, 1, 10, 0);
        Duration oneHour = Duration.ofMinutes(60);

        Event e1 = new Event("Réunion", d, oneHour);
        Event e2 = new Event("Réunion", d.plusHours(2), oneHour);
        Event e3 = new Event("Cours", d.plusHours(4), oneHour);

        agenda.addEvent(e1);
        agenda.addEvent(e2);
        agenda.addEvent(e3);

        var found = agenda.findByTitle("Réunion");
        assertEquals(2, found.size());
        assertTrue(found.contains(e1));
        assertTrue(found.contains(e2));
    }

    @Test
    public void findByTitleReturnsEmptyListWhenNoMatch() {
        Agenda agenda = new Agenda();
        LocalDateTime d = LocalDateTime.of(2020, 11, 1, 10, 0);
        Duration oneHour = Duration.ofMinutes(60);

        Event e1 = new Event("Réunion", d, oneHour);
        agenda.addEvent(e1);

        var found = agenda.findByTitle("Autre");
        assertTrue(found.isEmpty());
    }

    @Test
    public void isFreeForReturnsTrueWhenNoOverlap() {
        Agenda agenda = new Agenda();
        LocalDateTime d = LocalDateTime.of(2020, 11, 1, 10, 0);
        Duration oneHour = Duration.ofMinutes(60);

        Event existing = new Event("Réunion", d, oneHour);
        agenda.addEvent(existing);

        Event candidate = new Event("Autre", d.plusHours(2), oneHour);
        assertTrue(agenda.isFreeFor(candidate));
    }

    @Test
    public void isFreeForReturnsFalseWhenOverlap() {
        Agenda agenda = new Agenda();
        LocalDateTime d = LocalDateTime.of(2020, 11, 1, 10, 0);
        Duration oneHour = Duration.ofMinutes(60);

        Event existing = new Event("Réunion", d, oneHour);
        agenda.addEvent(existing);

        Event candidate = new Event("Chevauchement", d.plusMinutes(30), oneHour);
        assertFalse(agenda.isFreeFor(candidate));
    }

    @Test
    public void isFreeForTrueWhenJustAfterEnd() {
        Agenda agenda = new Agenda();
        LocalDateTime d = LocalDateTime.of(2020, 11, 1, 10, 0);
        Duration oneHour = Duration.ofMinutes(60);

        Event existing = new Event("Réunion", d, oneHour);
        agenda.addEvent(existing);

        Event candidate = new Event("Après", d.plusHours(1), oneHour);
        assertTrue(agenda.isFreeFor(candidate));
    }
    @Test
    public void isFreeForOnEmptyAgendaIsTrue() {
        Agenda agenda = new Agenda();
        LocalDateTime d = LocalDateTime.of(2020, 11, 1, 10, 0);
        Duration oneHour = Duration.ofMinutes(60);
        Event e = new Event("Test", d, oneHour);

        assertTrue(agenda.isFreeFor(e));
    }
    @Test
    public void findByTitleOnEmptyAgendaReturnsEmptyList() {
        Agenda agenda = new Agenda();
        var found = agenda.findByTitle("Quelque chose");
        assertTrue(found.isEmpty());
    }


}
