/*
 * © Software Aplicado SA de CV. Todos los derechos reservados. 
 */
package erp.mod.hrs.utils;

import java.util.Date;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SAnniversary {
    
    /** JodaTime local date. */
    protected final LocalDate moPeriodStart;
    /** JodaTime local date. */
    protected final LocalDate moPeriodCutoff;
    /** JodaTime period. */
    protected final Period moElapsedPeriod;
    
    /** JodaTime local date. */
    protected final LocalDate moAnniversaryCurrent;
    /** JodaTime local date. */
    protected final LocalDate moAnniversaryPrev;
    /** JodaTime local date. */
    protected final LocalDate moAnniversaryNext;
    
    protected final boolean mbAnniversaryCurrentTurned;
    
    /**
     * Create anniversary manager.
     * @param start Start date of period.
     * @param cutoff Cutoff date of period.
     */
    public SAnniversary(final Date start, final Date cutoff) {
        moPeriodStart = new LocalDate(start);
        moPeriodCutoff = new LocalDate(cutoff);
        moElapsedPeriod = new Period(moPeriodStart, moPeriodCutoff, PeriodType.yearDay());
        
        moAnniversaryCurrent = moPeriodStart.withYear(moPeriodCutoff.getYear());
        moAnniversaryPrev = moPeriodStart.withYear(moPeriodCutoff.getYear() - 1);
        moAnniversaryNext = moPeriodStart.withYear(moPeriodCutoff.getYear() + 1);
        
        mbAnniversaryCurrentTurned = !moAnniversaryCurrent.isAfter(moPeriodCutoff); // check if date of current anniversary is not after cutoff date!
    }
    
    /**
     * Get start date of period.
     * @return Member <code>LocalDate</code> of start date of period.
     */
    public final LocalDate getPeriodStart() {
        return moPeriodStart;
    }
    
    /**
     * Get cutoff date of period.
     * @return Member <code>LocalDate</code> of cutoff date of period.
     */
    public final LocalDate getPeriodCutoff() {
        return moPeriodCutoff;
    }

    /**
     * Get elapsed period from start date to cutoff date.
     * @return Member <code>Period</code> of elapsed period.
     */
    public Period getElapsedPeriod() {
        return moElapsedPeriod;
    }
    
    /**
     * Get elapsed years from start date to cutoff date.
     * @return Elapsed years.
     */
    public int getElapsedYears() {
        return moElapsedPeriod.getYears();
    }
    
    /**
     * Get elapsed days in last elapsed year from start date to cutoff date.
     * @return Elapsed days.
     */
    public int getElapsedYearDays() {
        return moElapsedPeriod.getDays();
    }
    
    /**
     * Get elapsed days plus one day in last elapsed year from start date to cutoff date.
     * Count of days includes start date of period.
     * @return Elapsed days for benefits.
     */
    public int getElapsedYearDaysForBenefits() {
        return getElapsedYearDays() + 1;
    }
    
    /**
     * Get current anniversary, that is elapsed years plus one year;
     * @return Current anniversary.
     */
    public int getCurrentAnniversary() {
        return getElapsedYears() + 1;
    }
    
    /**
     * Get date of current anniversary in year of cutoff date.
     * @return Member <code>LocalDate</code> of date of current anniversary.
     */
    public LocalDate getCurrentAnniversaryDate() {
        return moAnniversaryCurrent;
    }

    /**
     * Check if current anniversary is turned according to cutoff date.
     * @return <code>true</code> if current anniversary is turned, otherwise <code>false</code>.
     */
    public boolean isCurrentAnniversaryTurned() {
        return mbAnniversaryCurrentTurned;
    }
    
    /**
     * Get elapsed years + proportional part.
     * @return Elapsed years + proportional part.
     */
    public double getElapsedYearsPlusPropPart() {
        return getElapsedYears() + getCurrentAnniversaryPropPart();
    }
    
    /**
     * Get elapsed years + proportional part for benefits.
     * Count of days includes start date of period.
     * @return Elapsed years + proportional part for benefits.
     */
    public double getElapsedYearsPlusPartForBenefits() {
        return getElapsedYears() + getCurrentAnniversaryPropPartForBenefits();
    }
    
    /**
     * Compute current anniversary's proportional part.
     * @param forBenefits Is for benefits?
     * @return Current anniversary's proportional part.
     */
    private double computeCurrentAnniversaryPropPart(final boolean forBenefits) {
        Period period = null;
        
        if (mbAnniversaryCurrentTurned) {
            // anniversary in current year already been turned:
            period = new Period(moAnniversaryCurrent, moAnniversaryNext, PeriodType.days());
        }
        else {
            // anniversary in current year has not been turned yet:
            period = new Period(moAnniversaryPrev, moAnniversaryCurrent, PeriodType.days());
        }
        
        return period.getDays() == 0 ? 0.0 : (forBenefits ? getElapsedYearDaysForBenefits() : getElapsedYearDays()) / (double) period.getDays();
    }
    
    /**
     * Get current anniversary's proportional part.
     * @return Current anniversary's proportional part.
     */
    public double getCurrentAnniversaryPropPart() {
        return computeCurrentAnniversaryPropPart(false);
    }
    
    /**
     * Get current anniversary's proportional part for benefits.
     * Count of days includes start date of period.
     * @return Get current anniversary's proportional part for benefits.
     */
    public double getCurrentAnniversaryPropPartForBenefits() {
        return computeCurrentAnniversaryPropPart(true);
    }
    
    /**
     * Get description of period of years in format: "De 'aaaa' a 'aaaa'".
     * @return 
     */
    public String getPeriodYearsDescription() {
        return "De " + moPeriodStart.getYear() + " a " + moPeriodCutoff.getYear();
    }
    
    /**
     * Get description of period of dates in format: "Del 'dd/mm/aaaa' al 'dd/mm/aaaa'".
     * @return 
     */
    public String getPeriodDatesDescription() {
        return "Del " + SLibUtils.DateFormatDate.format(moPeriodStart.toDate()) + " al " + SLibUtils.DateFormatDate.format(moPeriodCutoff.toDate());
    }
    
    @Override
    public String toString() {
        return "start = " + SLibUtils.IsoFormatDate.format(moPeriodStart.toDate()) + "; "
                + "cutoff = " + SLibUtils.IsoFormatDate.format(moPeriodCutoff.toDate()) + "; "
                + "period type = " + moElapsedPeriod.getPeriodType() + "; "
                + "period = " + moElapsedPeriod.toString() + "; "
                + "years = " + moElapsedPeriod.getYears() + "; "
                + "days = " + moElapsedPeriod.getDays() + "; "
                + "current anniversary = " + SLibUtils.IsoFormatDate.format(moAnniversaryCurrent.toDate()) + "; "
                + "is turned? = " + mbAnniversaryCurrentTurned;
    }
}
