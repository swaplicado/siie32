/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db.ssc;

import sa.lib.SLibUtils;

/**
 *
 * @author Claudio Peña
 */
public class SSscEarning {
    
    public int EarningId;
    public int EarningTypeId;
    public double AmountExempt;
    public double AmountTaxed;
     
    public SSscEarning(final int earningId) {
        this(earningId, 0, 0);
    }

    public SSscEarning(final int earningId, final double amountExempt, final double amountTaxed) {
        EarningId = earningId;
        AmountExempt = amountExempt;
        AmountTaxed = amountTaxed;
    }

    public SSscEarning(final int earningId, final int earningTypeId, final double amountExempt, final double amountTaxed) {
        EarningId = earningId;
        EarningTypeId = earningTypeId;
        AmountExempt = amountExempt;
        AmountTaxed = amountTaxed;
    }

    public double getAmout() {
        return SLibUtils.roundAmount(AmountExempt + AmountTaxed);
    }
}
