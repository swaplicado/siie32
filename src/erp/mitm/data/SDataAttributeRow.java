/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

/**
 *
 * @author Edwin Carmona
 */
public class SDataAttributeRow extends erp.lib.table.STableRow {

    public SDataAttributeRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataAttributeMaterial att = (SDataAttributeMaterial) moData;

        mvValues.clear();
        mvValues.add(att.getName());
    }
}
