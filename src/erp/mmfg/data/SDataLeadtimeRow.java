/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import erp.data.SDataConstants;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataLeadtimeRow extends erp.lib.table.STableRow {

     private int mnFormType;

    public SDataLeadtimeRow(java.lang.Object data, int type) {
        moData = data;
        mnFormType = type;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataLeadtimeCo ltimeCo = null;
        SDataLeadtimeCob ltimeCob = null;

        if (mnFormType == SDataConstants.TRN_SUP_LT_CO) {
            ltimeCo = (SDataLeadtimeCo) moData;

            mvValues.clear();
            mvValues.add(ltimeCo.getDbmsTypeLink());
            mvValues.add(ltimeCo.getDbmsRef());
            mvValues.add(ltimeCo.getLtime());
            mvValues.add(ltimeCo.getIsDeleted());
            mvValues.add(ltimeCo.getDbmsUserNew());
            mvValues.add(ltimeCo.getUserNewTs());
            mvValues.add(ltimeCo.getDbmsUserEdit());
            mvValues.add(ltimeCo.getUserEditTs());
            mvValues.add(ltimeCo.getDbmsUserDelete());
            mvValues.add(ltimeCo.getUserDeleteTs());
        } else if (mnFormType == SDataConstants.TRN_SUP_LT_COB) {
            ltimeCob = (SDataLeadtimeCob) moData;
            
            mvValues.clear();
            mvValues.add(ltimeCob.getDbmsTypeLink());
            mvValues.add(ltimeCob.getDbmsRef());
            mvValues.add(ltimeCob.getLtime());
            mvValues.add(ltimeCob.getIsDeleted());
            mvValues.add(ltimeCob.getDbmsUserNew());
            mvValues.add(ltimeCob.getUserNewTs());
            mvValues.add(ltimeCob.getDbmsUserEdit());
            mvValues.add(ltimeCob.getUserEditTs());
            mvValues.add(ltimeCob.getDbmsUserDelete());
            mvValues.add(ltimeCob.getUserDeleteTs());
        }
    }
}
