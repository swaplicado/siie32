/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataItemConfigBizPartnerRow extends erp.lib.table.STableRow {

    public SDataItemConfigBizPartnerRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataItemConfigBizPartner config = (SDataItemConfigBizPartner) moData;

        mvValues.clear();
        mvValues.add(config.getKey());
        mvValues.add(config.getItem());
        mvValues.add(config.getItemShort());
        mvValues.add(config.getDbmsUnit());
        mvValues.add(config.getCfdiUsage());
        mvValues.add(config.getIsDeleted());
    }
}
