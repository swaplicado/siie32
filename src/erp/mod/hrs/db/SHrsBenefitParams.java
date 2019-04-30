/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import java.util.Date;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public final class SHrsBenefitParams {

    private final SDbEarning moEarning;
    private final SDbBenefitTable moBenefitTable;
    private final SDbBenefitTable moBenefitTableAux;
    private final SHrsReceipt moHrsReceipt;
    private final Date mtDateCutOff;

    public SHrsBenefitParams(SDbEarning earning, SDbBenefitTable benefit, SDbBenefitTable benefitAux, SHrsReceipt hrsReceipt, Date dateCutOff) {
        moEarning = earning;
        moBenefitTable = benefit;
        moBenefitTableAux = benefitAux;
        moHrsReceipt = hrsReceipt;
        mtDateCutOff = dateCutOff;
    }

    public SDbEarning getEarning() { return moEarning; }
    public SDbBenefitTable getBenefitTable() { return moBenefitTable; }
    public SDbBenefitTable getBenefitTableAux() { return moBenefitTableAux; }
    public SHrsReceipt getHrsReceipt() { return moHrsReceipt; }
    public Date getDateCutOff() { return mtDateCutOff; }
}
