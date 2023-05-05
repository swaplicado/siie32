/*
 * © Software Aplicado SA de CV. Todos los derechos reservados. 
 */
package erp.mod.hrs.utils;

import java.util.Date;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 *
 * @author Sergio Flores
 */
public class SAnniversary {
    
    protected final LocalDate mjtStart;
    protected final LocalDate mjtToday;
    protected final Period mjtPeriodElapsed;
    
    protected final LocalDate mjtAnniversaryPrev;
    protected final LocalDate mjtAnniversaryCurr;
    protected final LocalDate mjtAnniversaryNext;
    protected final boolean mbAnniversaryCurrTurned;
    
    public SAnniversary(final Date start, final Date today) {
        mjtStart = new LocalDate(start);
        mjtToday = new LocalDate(today);
        mjtPeriodElapsed = new Period(mjtStart, mjtToday, PeriodType.yearDay());
        
        mjtAnniversaryPrev = mjtStart.withYear(mjtToday.getYear() - 1);
        mjtAnniversaryCurr = mjtStart.withYear(mjtToday.getYear());
        mjtAnniversaryNext = mjtStart.withYear(mjtToday.getYear() + 1);
        mbAnniversaryCurrTurned = !mjtAnniversaryCurr.isAfter(mjtToday);
    }
    
    /**
     * Get parameter start date.
     * @return Parameter start date.
     */
    public final LocalDate getStart() {
        return mjtStart;
    }
    
    /**
     * Get paramenter today.
     * @return Parameter today.
     */
    public final LocalDate getToday() {
        return mjtToday;
    }

    /**
     * Get period elapsed from start date to today.
     * @return Period elapsed.
     */
    public Period getPeriodElapsed() {
        return mjtPeriodElapsed;
    }
    
    /**
     * Get date of previous anniversary according to current anniversary.
     * @return Date of previous anniversary.
     */
    public LocalDate getAnniversaryPrev() {
        return mjtAnniversaryPrev;
    }

    /**
     * Get date of current anniversary according to start date and year of today.
     * @return Date of current anniversary.
     */
    public LocalDate getAnniversaryCurr() {
        return mjtAnniversaryCurr;
    }

    /**
     * Get date of next anniversary according to current anniversary.
     * @return Date of next anniversary.
     */
    public LocalDate getAnniversaryNext() {
        return mjtAnniversaryNext;
    }

    /**
     * Get if current anniversary is turned according to today.
     * @return <code>true</code> if current anniversary is turned, otherwise <code>false</code>.
     */
    public boolean isAnniversaryCurrTurned() {
        return mbAnniversaryCurrTurned;
    }
    
    /**
     * Get elapsed years, that is elapsed period's years.
     * @return Elapsed years.
     */
    public int getElapsedYears() {
        return mjtPeriodElapsed.getYears();
    }
    
    /**
     * Get elapsed days, that is elapsed period's days;
     * @return Elapsed days.
     */
    public int getElapsedDays() {
        return mjtPeriodElapsed.getDays();
    }
    
    /**
     * Get elapsed days for benefits, that is elapsed period's days + 1;
     * @return Elapsed days for benefits.
     */
    public int getElapsedDaysForBenefits() {
        return getElapsedDays() + 1;
    }
    
    /**
     * Get ongoing anniversary, that is elapsed period's years + 1;
     * @return Current anniversary.
     */
    public int getOngoingAnniversary() {
        return getElapsedYears() + 1;
    }
    
    /**
     * Compute current anniversary's proportional part.
     * @param forBenefits Is for benefits?
     * @return Current anniversary's proportional part.
     */
    private double computeAnniversaryProp(final boolean forBenefits) {
        Period period = null;
        
        if (mbAnniversaryCurrTurned) {
            // anniversary in current year already been turned:
            period = new Period(mjtAnniversaryCurr, mjtAnniversaryNext, PeriodType.days());
        }
        else {
            // anniversary in current year has not been turned yet:
            period = new Period(mjtAnniversaryPrev, mjtAnniversaryCurr, PeriodType.days());
        }
        
        return period.getDays() == 0 ? 0.0 : (forBenefits ? getElapsedDaysForBenefits() : getElapsedDays()) / (double) period.getDays();
    }
    
    /**
     * Get ongoing anniversary's proportional part.
     * @return Ongoing anniversary's proportional part.
     */
    public double getOngoingAnniversaryProp() {
        return computeAnniversaryProp(false);
    }
    
    /**
     * Get ongoing anniversary's proportional part for benefits.
     * @return Ongoing anniversary's proportional part for benefits.
     */
    public double getOngoingAnniversaryPropForBenefits() {
        return computeAnniversaryProp(true);
    }
    
    /**
     * Get eligible anniversary for benefits according to current anniversary and start and today dates.
     * If year of start date is greater or equal than year of today date, then the eligible anniversary is 1.
     * Else if current anniversary is turned, then the eligible anniversary is the very ongoing, otherwise the following one (ongoing anniversary + 1).
     * @return 
     */
    public int getEligibleAnniversary() {
        int eligibleAnniversary = 0;
        
        if (mjtStart.getYear() >= mjtToday.getYear()) {
            eligibleAnniversary = 1;
        }
        else if (mbAnniversaryCurrTurned) {
            eligibleAnniversary = getOngoingAnniversary();
        }
        else {
            eligibleAnniversary = getOngoingAnniversary() + 1;
        }
        
        return eligibleAnniversary;
    }
    
    @Override
    public String toString() {
        return "type=" + mjtPeriodElapsed.getPeriodType() + "; period=" + mjtPeriodElapsed.toString() + "; years=" + mjtPeriodElapsed.getYears() + "; days=" + mjtPeriodElapsed.getDays();
    }
}
