/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.data;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataPriceListLinkRow extends erp.lib.table.STableRow {

    public SDataPriceListLinkRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataPriceListLink pListLink = (SDataPriceListLink) moData;

        mvValues.clear();
        mvValues.add(pListLink.getDbmsLinkType());
        mvValues.add(pListLink.getDbmsReference());
    }
}
