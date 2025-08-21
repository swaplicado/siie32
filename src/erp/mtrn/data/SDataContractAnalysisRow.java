/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Juan Barajas, Isabel Servín
 */
public class SDataContractAnalysisRow extends erp.lib.table.STableRow {

    protected erp.client.SClientInterface miClient;

    protected java.util.Date mtDateOrd;
    protected java.lang.String msTypeOrd;
    protected java.lang.String msNumberOrd;
    protected java.lang.String msBranchOrd;
    protected int mnEntryOrdId;
    protected double mdQtyOrd;
    protected java.util.Date mtDateDoc;
    protected java.lang.String msTypeDoc;
    protected java.lang.String msNumberDoc;
    protected java.lang.String msCommissionsReference;
    protected java.lang.String msBranchDoc;
    protected int mnEntryDocId;
    protected double mdQtyDoc;
    protected double mdTotalDoc;
    protected double mdBalance;
    protected java.lang.String msContTank;
    protected java.util.Date mtDateAdj;
    protected java.lang.String msTypeAdj;
    protected java.lang.String msNumberAdj;
    protected java.lang.String msBranchAdj;
    protected int mnEntryAdjId;
    protected double mdQtyAdj;
    protected double mdTotalAdj;

    public SDataContractAnalysisRow(erp.client.SClientInterface client) {
        miClient = client;
        reset();
    }

    public void reset() {
        mtDateOrd = null;
        msTypeOrd = "";
        msNumberOrd = "";
        msBranchOrd = "";
        mnEntryOrdId = 0;
        mdQtyOrd = 0;
        mtDateDoc = null;
        msTypeDoc = "";
        msNumberDoc = "";
        msCommissionsReference = "";
        msBranchDoc = "";
        mnEntryDocId = 0;
        mdQtyDoc = 0;
        mdTotalDoc = 0;
        mdBalance = 0;
        msContTank = "";
        mtDateAdj = null;
        msTypeAdj = "";
        msNumberAdj = "";
        msBranchAdj = "";
        mnEntryAdjId = 0;
        mdQtyAdj = 0;
        mdTotalAdj = 0;
    }

    public void setDateOrd(java.util.Date t) { mtDateOrd = t; }
    public void setTypeOrd(java.lang.String s) { msTypeOrd = s; }
    public void setNumberOrd(java.lang.String s) { msNumberOrd = s; }
    public void setBranchOrd(java.lang.String s) { msBranchOrd = s; }
    public void setSortPosOrd(int n) { mnEntryOrdId = n; }
    public void setQtyOrd(double d) { mdQtyOrd = d; }
    public void setDateDoc(java.util.Date t) { mtDateDoc = t; }
    public void setTypeDoc(java.lang.String s) { msTypeDoc = s; }
    public void setNumberDoc(java.lang.String s) { msNumberDoc = s; }
    public void setCommissionsReference(java.lang.String s) { msCommissionsReference = s; }
    public void setBranchDoc(java.lang.String s) { msBranchDoc = s; }
    public void setSortPosDoc(int n) { mnEntryDocId = n; }
    public void setQtyDoc(double d) { mdQtyDoc = d; }
    public void setTotalDoc(double d) { mdTotalDoc = d; }
    public void setBalance(double d) { mdBalance = d; }
    public void setContTank(java.lang.String s) { msContTank = s; }
    public void setDateAdj(java.util.Date t) { mtDateAdj = t; }
    public void setTypeAdj(java.lang.String s) { msTypeAdj = s; }
    public void setNumberAdj(java.lang.String s) { msNumberAdj = s; }
    public void setBranchAdj(java.lang.String s) { msBranchAdj = s; }
    public void setSortPosAdj(int n) { mnEntryAdjId = n; }
    public void setQtyAdj(double d) { mdQtyAdj = d; }
    public void setTotalAdj(double d) { mdTotalAdj = d; }

    public java.util.Date getDateOrd() { return mtDateOrd; }
    public java.lang.String getTypeOrd() { return msTypeOrd; }
    public java.lang.String getNumberOrd() { return msNumberOrd; }
    public java.lang.String getBranchOrd() { return msBranchOrd; }
    public double getSortPosOrd() { return mnEntryOrdId; }
    public double getQtyOrd() { return mdQtyOrd; }
    public java.util.Date getDateDoc() { return mtDateDoc; }
    public java.lang.String getTypeDoc() { return msTypeDoc; }
    public java.lang.String getNumberDoc() { return msNumberDoc; }
    public java.lang.String getCommissionsReference() { return msCommissionsReference; }
    public java.lang.String getBranchDoc() { return msBranchDoc; }
    public double getSortPosDoc() { return mnEntryDocId; }
    public double getQtyODoc() { return mdQtyDoc; }
    public double getTotalDoc() { return mdTotalDoc; }
    public double getBalance() { return mdBalance; }
    public java.lang.String getContTank() { return msContTank; }
    public java.util.Date getDateAdj() { return mtDateAdj; }
    public java.lang.String getTypeAdj() { return msTypeAdj; }
    public java.lang.String getNumberAdj() { return msNumberAdj; }
    public java.lang.String getBranchAdj() { return msBranchAdj; }
    public double getSortPosAdj() { return mnEntryAdjId; }
    public double getQtyAdj() { return mdQtyAdj; }
    public double getTotalAdj() { return mdTotalAdj; }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(mtDateOrd);
        mvValues.add(msTypeOrd);
        mvValues.add(msNumberOrd);
        mvValues.add(msBranchOrd);
        mvValues.add(mnEntryOrdId);
        mvValues.add(mdQtyOrd);
        mvValues.add(mtDateDoc);
        mvValues.add(msTypeDoc);
        mvValues.add(msNumberDoc);
        mvValues.add(msCommissionsReference);
        mvValues.add(msBranchDoc);
        mvValues.add(mdTotalDoc);
        mvValues.add(mdBalance);
        mvValues.add(mnEntryDocId);
        mvValues.add(mdQtyDoc);
        mvValues.add(msContTank);
        mvValues.add(mtDateAdj);
        mvValues.add(msTypeAdj);
        mvValues.add(msNumberAdj);
        mvValues.add(msBranchAdj);
        mvValues.add(mnEntryAdjId);
        mvValues.add(mdQtyAdj);
        mvValues.add(mdTotalAdj);
    }
}
