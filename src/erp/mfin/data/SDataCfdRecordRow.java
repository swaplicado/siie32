/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

/**
 *
 * @author Juan Barajas
 */
public class SDataCfdRecordRow extends erp.lib.table.STableRow {

    protected int mnMoveId;
    protected int mnCfdId;
    protected String msNameXml;
    protected String msPathXml;
    
    public SDataCfdRecordRow(int moveId, int cfdId, String nameXml, String pathXml) {
        mnMoveId = moveId;
        mnCfdId = cfdId;
        msNameXml = nameXml;
        msPathXml = pathXml;
        prepareTableRow();
    }
    
    public void setMoveId(int n) { mnMoveId = n; }
    public void setCfdId(int n) { mnCfdId = n; }
    public void setNameXml(String s) { msNameXml = s; }
    public void setPathXml(String s) { msPathXml = s; }
    
    public int getMoveId() { return mnMoveId; }
    public int getCfdId() { return mnCfdId; }
    public String getNameXml() { return msNameXml; }
    public String getPathXml() { return msPathXml; }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(msNameXml);
    }
}
