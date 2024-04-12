/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataItemConfigLanguageRow extends erp.lib.table.STableRow {

    public SDataItemConfigLanguageRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataItemConfigLanguage config = (SDataItemConfigLanguage) moData;

        mvValues.clear();
        mvValues.add(config.getItem());
        mvValues.add(config.getItemShort());
        mvValues.add(config.getDbmsLanguage());
        mvValues.add(config.getIsDeleted());
    }
}
