/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataItemBizPartnerDescriptionRow extends erp.lib.table.STableRow {

    public SDataItemBizPartnerDescriptionRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataItemBizPartnerDescription desc = (SDataItemBizPartnerDescription) moData;

        mvValues.clear();
        mvValues.add(desc.getKey());
        mvValues.add(desc.getItem());
        mvValues.add(desc.getItemShort());
        mvValues.add(desc.getDbmsUnit());
        mvValues.add(desc.getIsDeleted());
    }
}
