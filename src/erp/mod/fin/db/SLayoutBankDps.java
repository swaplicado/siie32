/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

/**
 *
 * @author Juan Barajas
 */
public class SLayoutBankDps {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnDpsCategoryId;
    protected int mnDpsClassId;
    protected int mnDpsTypeId;
    protected int mnDpsCurId;
    protected double mdDpsAmount;
    protected double mdDpsExcRate;

    public SLayoutBankDps(int dpsYear, int dpsDoc, int dpsCategory, int dpsClass, int dpsType, int dpsCur, int userId, double dpsAmount, double dpsExcRate) {
        mnPkYearId = dpsYear;
        mnPkDocId = dpsDoc;
        mnDpsCategoryId = dpsCategory;
        mnDpsClassId = dpsClass;
        mnDpsTypeId = dpsType;
        mnDpsCurId = dpsCur;
        mdDpsAmount = dpsAmount;
        mdDpsExcRate = dpsExcRate;
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setDpsCategoryId(int n) { mnDpsCategoryId = n; }
    public void setDpsClassId(int n) { mnDpsClassId = n; }
    public void setDpsTypeId(int n) { mnDpsTypeId = n; }
    public void setDpsCurId(int n) { mnDpsCurId = n; }
    public void setDpsAmount(double d) { mdDpsAmount = d; }
    public void setDpsExcRate(double d) { mdDpsExcRate = d; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getDpsCategoryId() { return mnDpsCategoryId; }
    public int getDpsClassId() { return mnDpsClassId; }
    public int getDpsTypeId() { return mnDpsTypeId; }
    public int getDpsCurId() { return mnDpsCurId; }
    public double getDpsAmount() { return mdDpsAmount; }
    public double getDpsExcRate() { return mdDpsExcRate; }
}
