/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataItemForeignLanguageRow extends erp.lib.table.STableRow {

    public SDataItemForeignLanguageRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataItemForeignLanguage desc = (SDataItemForeignLanguage) moData;

        mvValues.clear();
        mvValues.add(desc.getItem());
        mvValues.add(desc.getItemShort());
        mvValues.add(desc.getDbmsLanguage());
        mvValues.add(desc.getIsDeleted());
    }
}
