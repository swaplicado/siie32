/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Juan Barajas
 */
public class SDialogStampDetailRow extends erp.lib.table.STableRow {

    protected erp.client.SClientInterface miClient;

    protected java.lang.String msTypeMove;
    protected int mnMovIn;
    protected int mnMovOut;
    protected int mnStock;
    protected java.util.Date mtDate;
    protected java.lang.String msTypeCfd;
    protected java.lang.String msTypeDoc;
    protected java.lang.String msNumberSer;
    protected java.lang.String msBranch;
    protected java.lang.String msUuid;

    public SDialogStampDetailRow(erp.client.SClientInterface client) {
        miClient = client;
        reset();
    }

    public void reset() {
        msTypeMove = "";
        mnMovIn = 0;
        mnMovOut = 0;
        mnStock = 0;
        mtDate = null;
        msTypeCfd = "";
        msTypeDoc = "";
        msNumberSer = "";
        msBranch = "";
        msUuid = "";
    }

    public void setTypeMove(java.lang.String s) { msTypeMove = s; }
    public void setMovIn(int n) { mnMovIn = n; }
    public void setMovOut(int n) { mnMovOut = n; }
    public void setStock(int n) { mnStock = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setTypeCfd(java.lang.String s) { msTypeCfd = s; }
    public void setTypeDoc(java.lang.String s) { msTypeDoc = s; }
    public void setNumberSer(java.lang.String s) { msNumberSer = s; }
    public void setBranch(java.lang.String s) { msBranch = s; }
    public void setUuid(java.lang.String s) { msUuid = s; }

    public java.lang.String getTypeMove() { return msTypeMove; }
    public int getMovIn() { return mnMovIn; }
    public int getMovOut() { return mnMovOut; }
    public int getStock() { return mnStock; }
    public java.util.Date getDate() { return mtDate; }
    public java.lang.String getTypeCfd() { return msTypeCfd; }
    public java.lang.String getTypeDoc() { return msTypeDoc; }
    public java.lang.String getNumberSer() { return msNumberSer; }
    public java.lang.String getBranch() { return msBranch; }
    public java.lang.String getUuid() { return msUuid; }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(mtDate);
        mvValues.add(msTypeMove);
        mvValues.add(mnMovIn);
        mvValues.add(mnMovOut);
        mvValues.add(mnStock);
        mvValues.add(msTypeCfd);
        mvValues.add(msTypeDoc);
        mvValues.add(msNumberSer);
        mvValues.add(msBranch);
        mvValues.add(msUuid);
    }
}
