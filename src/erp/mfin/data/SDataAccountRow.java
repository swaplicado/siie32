/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Juan Barajas
 */
public class SDataAccountRow extends erp.lib.table.STableRow {
    
    protected boolean mbIsSelected;

    public SDataAccountRow(java.lang.Object data) {
        moData = data;
        mbIsSelected = true;
        prepareTableRow();
    }
    
    public void setSelected(boolean b) { mbIsSelected = b; }
    
    public boolean isSelected() { return mbIsSelected; }

    @Override
    public void prepareTableRow() {
        SDataAccount account = (SDataAccount) moData;

        mvValues.clear();
        mvValues.add(account.getPkAccountIdXXX());
        mvValues.add(account.getAccount());
        mvValues.add(mbIsSelected);
    }
}
