package agenda;

import java.time.*;
import java.time.temporal.ChronoUnit;

public class Event {

    /**
     * The myTitle of this event
     */
    private String myTitle;
    
    /**
     * The starting time of the event
     */
    private LocalDateTime myStart;

    /**
     * The durarion of the event 
     */
    private Duration myDuration;

    private Repetition repetition;


    /**
     * Constructs an event
     *
     * @param title the title of this event
     * @param start the start time of this event
     * @param duration the duration of this event
     */
    public Event(String title, LocalDateTime start, Duration duration) {
        this.myTitle = title;
        this.myStart = start;
        this.myDuration = duration;
    }

    public void setRepetition(ChronoUnit frequency) {
        this.repetition = new Repetition(frequency);
    }

    public void addException(LocalDate date) {
        if (repetition != null) {
            repetition.addException(date);
        }
    }

    public void setTermination(LocalDate terminationInclusive) {
        if (repetition != null) {
            Termination t = new Termination(myStart.toLocalDate(), repetition.getFrequency(), terminationInclusive);
            repetition.setTermination(t);
        }
    }

    public void setTermination(long numberOfOccurrences) {
        if (repetition != null) {
            Termination t = new Termination(myStart.toLocalDate(), repetition.getFrequency(), numberOfOccurrences);
            repetition.setTermination(t);
        }
    }

    public int getNumberOfOccurrences() {
        if (repetition == null || repetition.getTermination() == null) {
            return 1;
        }
        return (int) repetition.getTermination().numberOfOccurrences();
    }

    public LocalDate getTerminationDate() {
        if (repetition == null || repetition.getTermination() == null) {
            return myStart.toLocalDate();
        }
        return repetition.getTermination().terminationDateInclusive();
    }

    /**
     * Tests if an event occurs on a given day
     *
     * @param aDay the day to test
     * @return true if the event occurs on that day, false otherwise
     */
    public boolean isInDay(LocalDate aDay) {
        LocalDateTime dayStart = aDay.atStartOfDay();
        LocalDateTime dayEnd = aDay.plusDays(1).atStartOfDay();

        if (repetition == null) {
            LocalDateTime eventStart = myStart;
            LocalDateTime eventEnd = myStart.plus(myDuration);
            return eventStart.isBefore(dayEnd) && eventEnd.isAfter(dayStart);
        }

        LocalDate startDate = myStart.toLocalDate();
        if (aDay.isBefore(startDate)) {
            return false;
        }

        ChronoUnit freq = repetition.getFrequency();
        long steps = freq.between(startDate, aDay);
        LocalDateTime occurrenceStart = myStart.plus(steps, freq);

        Termination term = repetition.getTermination();
        if (term != null) {
            long maxIndex = term.numberOfOccurrences() - 1;
            if (steps > maxIndex) {
                return false;
            }
        }

        if (repetition.isException(occurrenceStart.toLocalDate())) {
            return false;
        }

        LocalDateTime occurrenceEnd = occurrenceStart.plus(myDuration);
        return occurrenceStart.isBefore(dayEnd) && occurrenceEnd.isAfter(dayStart);
    }
   
    /**
     * @return the myTitle
     */
    public String getTitle() {
        return myTitle;
    }

    /**
     * @return the myStart
     */
    public LocalDateTime getStart() {
        return myStart;
    }


    /**
     * @return the myDuration
     */
    public Duration getDuration() {
        return myDuration;
    }

    @Override
    public String toString() {
        return "Event{title='%s', start=%s, duration=%s}".formatted(myTitle, myStart, myDuration);
    }
}
