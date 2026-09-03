/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModSysConsts;
import java.util.Date;

/**
 *
 * @author Edwin Carmona
 */
public class SDbStockKardexEntry extends SDbStockValuationKardex {

    public SDbStockKardexEntry(final Date dt, final int idDiogYear, final int idDiogDoc, final int idDiogEty, final int idValuation) {
        super(idValuation);
        this.setMovDate(dt);
        this.setFkStockValuationKardexTypeId(TYPE_VAL_KARDEX_IN);
        this.setFkDiogCategoryId(SModSysConsts.TRNS_CT_IOG_IN);
        this.setFkDiogYearInId_n(idDiogYear);
        this.setFkDiogDocInId_n(idDiogDoc);
        this.setFkDiogEntryInId_n(idDiogEty);
        this.setFkStockValuationKardexId_n(0);
    }
}
