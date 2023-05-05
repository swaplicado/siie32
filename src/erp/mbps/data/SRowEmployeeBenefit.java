/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mbps.data;

import erp.lib.table.STableRow;
import java.util.Date;

/**
 *
 * @author Sergio Flores
 */
public final class SRowEmployeeBenefit extends STableRow {

    protected int mnAnnum;
    protected int mnYear;
    protected double mdBenefit;
    protected int mnBenefitId;
    protected String msBenefitName;
    protected int mnUserId;
    protected String msUserName;
    protected Date mtUserTs;

    public SRowEmployeeBenefit(final int annum, final int year, 
            final double benefit, final int benefitId, final String benefitName,
            final int userId, final String userName, final Date userTs) {
        mnAnnum = annum;
        mnYear = year;
        mdBenefit = benefit;
        mnBenefitId = benefitId;
        msBenefitName = benefitName;
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
        mvValues.add(mdBenefit);
        mvValues.add(msBenefitName);
        mvValues.add(msUserName);
        mvValues.add(mtUserTs);
    }

    public int getAnnum() { return mnAnnum; }
    public int getYear() { return mnYear; }
    public double getBenefit() { return mdBenefit; }
    public int getBenefitId() { return mnBenefitId; }
    public String getBenefitName() { return msBenefitName; }
    public int getUserId() { return mnUserId; }
    public String getUserName() { return msUserName; }
    public Date getUserTs() { return mtUserTs; }
}
