/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.hrs.db.ssc;

import erp.mod.SModConsts;
import erp.mod.hrs.db.SDbEmployee;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Claudio Peña
 */
public class SRowEmployeeSsc implements SGridRow {
    
    public static final int COL_SELECTED = 18;
    public static final int COLS_FIXED = 20;

    protected SGuiSession moSession;
    protected SDbEmployee moEmployee;
    protected Date mtPeriodStart;
    protected Date mtPeriodEnd;
    protected int mnPeriodDays;
    
    protected int mnVacationsDays;
    protected double mdVacationsBonus;
    protected double mdAnnualBonusDays;
    protected double mdSscFactor;    
    protected double mdDailyIncome;
    protected double mdSscCurrent;
    protected Date mtSscLastUpdate;
    protected double mdSscRaw;
    protected double mdVariableIncome;
    protected int mnAbsenceEffectiveDaysSuggested;
    protected int mnAbsenceEffectiveDaysFinal;
    protected double mdSscSuggested;
    protected double mdSscFinal;
    protected boolean mbRowSelected;
    
    /** Lista de las percepciones del período (bimestre) a procesar. */
    protected ArrayList<SSscEarning> maSbcEarnings;
    
    public SRowEmployeeSsc(final SGuiSession session, final SDbEmployee employee, final Date periodStart, final Date periodEnd) {
        moSession = session;
        moEmployee = employee;
        mtPeriodStart = periodStart;
        mtPeriodEnd = periodEnd;
        mnPeriodDays = SLibTimeUtils.countPeriodDays(mtPeriodStart, mtPeriodEnd);

        mnVacationsDays = 0;
        mdVacationsBonus = 0.0;
        mdAnnualBonusDays = 0.0;
        mdSscFactor = 0.0;
        mdDailyIncome = 0.0;
        mdSscCurrent = 0.0;
        mtSscLastUpdate = null;
        mdSscRaw = 0.0;
        mdVariableIncome = 0.0;
        mnAbsenceEffectiveDaysSuggested = 0;
        mnAbsenceEffectiveDaysFinal = 0;
        mdSscSuggested = 0.0;
        mdSscFinal = 0.0;
        mbRowSelected = false;
        
        maSbcEarnings = new ArrayList<>();
    }

    public SDbEmployee getEmployee() { return moEmployee; }
    public Date getPeriodStart() { return mtPeriodStart; }
    public Date getPeriodEnd() { return mtPeriodEnd; }
    public int getPeriodDays() { return mnPeriodDays; }
    
    public void setVacationsDays(int n) { mnVacationsDays = n; }
    public void setVacationsBonus(double d) { mdVacationsBonus = d; }
    public void setAnnualBonusDays(double d) { mdAnnualBonusDays = d; }
    public void setSscFactor(double d) { mdSscFactor = d; }
    public void setDailyIncome(double d) { mdDailyIncome = d; }
    public void setSscCurrent(double d) { mdSscCurrent = d; }
    public void setSscLastUpdate(Date t) { mtSscLastUpdate = t; }
    public void setSscRaw(double d) { mdSscRaw = d; }
    public void setVariableIncome(double d) { mdVariableIncome = d; }
    public void setAbsenceEffectiveDaysSuggested(int days) { mnAbsenceEffectiveDaysSuggested = days; setAbsenceEffectiveDaysFinal(mnAbsenceEffectiveDaysSuggested); setSscSuggested(computeSbc(mnAbsenceEffectiveDaysSuggested)); }
    public void setAbsenceEffectiveDaysFinal(int days) { mnAbsenceEffectiveDaysFinal = days; setSscFinal(computeSbc(mnAbsenceEffectiveDaysFinal)); }
    public void setSscSuggested(double sbc) { SLibUtils.roundAmount(mdSscSuggested) ;mdSscSuggested = sbc; setSscFinal(mdSscSuggested); }
    public void setSscFinal(double sbc) { mdSscFinal = sbc; }
    public void setRowSelected(boolean d) { mbRowSelected = d; }
    
    public int getVacationsDays() { return mnVacationsDays; }
    public double getVacationsBonus() { return mdVacationsBonus; }
    public double getAnnualBonusDays() { return mdAnnualBonusDays; }
    public double getSscFactor() { return mdSscFactor; }
    public double getDailyIncome() { return mdDailyIncome; }
    public double getSscCurrent() { return mdSscCurrent; }
    public Date getSscLastUpdate() { return mtSscLastUpdate; }
    public double getSscRaw() { return mdSscRaw; }
    public double getVariableIncome() { return mdVariableIncome; }
    public int getAbsenceEffectiveDaysSuggested() { return mnAbsenceEffectiveDaysSuggested; }
    public int getAbsenceEffectiveDaysFinal() { return mnAbsenceEffectiveDaysFinal; }
    public double getSscSuggested() { return mdSscSuggested; }
    public double getSscFinal() { return mdSscFinal; }
    public boolean isRowSelected() { return mbRowSelected; }
    
    public ArrayList<SSscEarning> getSbcEarnings() { return maSbcEarnings; }
    
    public SSscEarning getSbcEarnginById(final int earningId) {
        SSscEarning sbcEarning = null;
        
        for (SSscEarning e : maSbcEarnings) {
            if (earningId == e.EarningId) {
                sbcEarning = e;
                break;
            }
        }
        
        return sbcEarning;
    }
    
    private double computeSbc(final int absenceDays) {
        int workedDays = mnPeriodDays - absenceDays; // convenience variable
        return mdSscRaw + (workedDays == 0 ? 0 : (mdVariableIncome / workedDays));
    }
    
    public void computeSbc() {
        setSscSuggested(computeSbc(mnAbsenceEffectiveDaysSuggested));
    }
    
    public SSalarySscBase createSalarySscBase(final SGuiSession session, final Date dateSalarySscBase) {
        return new SSalarySscBase(moEmployee.getPkEmployeeId(), mdSscFinal, dateSalarySscBase, session.getUser().getPkUserId());
    }
    
    @Override
    public int[] getRowPrimaryKey() {
        return moEmployee.getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return moEmployee.getNumber();
    }

    @Override
    public String getRowName() {
        return moEmployee.getXtaEmployeeName();
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return false;
    }

    @Override
    public boolean isRowEdited() {
        return true;
    }

    @Override
    public void setRowEdited(final boolean edited) {
        
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        if (col < COLS_FIXED) {
            switch(col) {
                case 0:
                    value = moEmployee.getXtaEmployeeName();
                    break;
                case 1:
                    value = moEmployee.getNumber();
                    break;
                case 2:
                    value = moEmployee.getDateBenefits();
                    break;
                case 3:
                    value = SLibTimeUtils.formatAge(moEmployee.getDateBenefits(), mtPeriodEnd);
                    break;
                case 4:
                    value = moSession.readField(SModConsts.HRSS_TP_PAY, new int[] { moEmployee.getFkPaymentTypeId() }, SDbRegistry.FIELD_NAME);
                    break;
                case 5:
                    value = mnVacationsDays;
                    break;
                case 6:
                    value = mdVacationsBonus;
                    break;
                case 7:
                    value = mdAnnualBonusDays;
                    break;
                case 8:
                    value = mdSscFactor;
                    break;
                case 9:
                    value = mdDailyIncome;
                    break;
                case 10:
                    value = mdSscCurrent;
                    break;
                case 11:
                    value = mtSscLastUpdate;
                    break;
                case 12:
                    value = mdSscRaw;
                    break;
                case 13:
                    value = mdVariableIncome;
                    break;
                case 14:
                    value = mnAbsenceEffectiveDaysSuggested;
                    break;
                case 15:
                    value = mnAbsenceEffectiveDaysFinal;
                    break;
                case 16:
                    value = mdSscSuggested;
                    break;
                case 17:
                    value = SLibUtils.roundAmount(mdSscFinal);
                    break;
                case COL_SELECTED:
                    value = mbRowSelected;
                    break;
                default:
            }
        }
        else {
            int index = (col - COLS_FIXED) / 2; // variable de conveniencia
            SSscEarning sbcEarning = maSbcEarnings.get(index);
            
            int module = (col - COLS_FIXED) % 2; // variable de conveniencia
            if (module == 0) {
                // col corresponde a la parte exenta de la percepción variable:
                value = sbcEarning.AmountExempt;
            }
            else {
                // col corresponde a la parte gravada de la percepción variable:
                value = sbcEarning.AmountTaxed;
            }
        }
        
        return value;
   }

    @Override
    public void setRowValueAt(Object value , int col) {
        switch (col) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                break;
            case 15:
                setAbsenceEffectiveDaysFinal((int) value);
                break;
            case 16:
                break;
            case 17:
                setSscFinal((double) value);
                break;
            case COL_SELECTED:
                setRowSelected((boolean) value);
                break;
            default:            
        }
    }
}
