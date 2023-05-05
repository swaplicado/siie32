/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mbps.data;

import erp.lib.table.STableRow;
import erp.mod.hrs.db.SHrsConsts;
import java.util.Date;

/**
 *
 * @author Sergio Flores
 */
public final class SRowEmployeeWageFactor extends STableRow {

    protected int mnAnnum;
    protected int mnYear;
    protected double mdVacationDays;
    protected double mdVacationBonusPercentage;
    protected double mdAnnualBonusDays;
    protected double mdTotalAnnualDays;
    protected double mdWageFactor;
    protected int mnUserId;
    protected String msUserName;
    protected Date mtUserTs;

    public SRowEmployeeWageFactor(final int annum, final int year,
            final double vacationDays, final double vacationBonusPercentage, final double annualBonusDays,
            final int userId, final String userName, final Date userTs) {
        mnAnnum = annum;
        mnYear = year;
        mdVacationDays = vacationDays;
        mdVacationBonusPercentage = vacationBonusPercentage;
        mdAnnualBonusDays = annualBonusDays;
        mdTotalAnnualDays = SHrsConsts.YEAR_DAYS + (mdVacationDays * mdVacationBonusPercentage) + mdAnnualBonusDays;
        mdWageFactor = mdTotalAnnualDays / SHrsConsts.YEAR_DAYS;
        mnUserId = userId;
        msUserName = userName;
        mtUserTs = userTs;
        
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(mnAnnum);
        mvValues.add(mnYear);
        mvValues.add(mdVacationDays);
        mvValues.add(mdVacationBonusPercentage);
        mvValues.add(mdAnnualBonusDays);
        mvValues.add(mdTotalAnnualDays);
        mvValues.add(mdWageFactor);
        mvValues.add(msUserName);
        mvValues.add(mtUserTs);
    }

    public int getAnnum() { return mnAnnum; }
    public int getYear() { return mnYear; }
    public double getVacationDays() { return mdVacationDays; }
    public double getVacationBonusPercentage() { return mdVacationBonusPercentage; }
    public double getAnnualBonusDays() { return mdAnnualBonusDays; }
    public double getTotalAnnualDays() { return mdTotalAnnualDays; }
    public double getWageFactor() { return mdWageFactor; }
    public int getUserId() { return mnUserId; }
    public String getUserName() { return msUserName; }
    public Date getUserTs() { return mtUserTs; }
}
