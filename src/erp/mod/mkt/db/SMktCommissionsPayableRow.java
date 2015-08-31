/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.mkt.db;

import java.util.ArrayList;
import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Néstor Ávalos
 */
public class SMktCommissionsPayableRow implements SGridRow {

    protected String msAuxDpsType;
    protected String msAuxDpsNumber;
    protected Date mtAuxDpsDateDoc;
    protected Date mtAuxDpsDateCommissionMaterialization;
    protected String msAuxDpsBizPartner;
    protected double mdAuxDpsSubtotal;
    protected double mdAuxDpsAdjustment;
    protected double mdAuxDpsCommission;
    protected double mdAuxDpsCommissionAdjustment;
    protected double mdAuxDpsCommissionTotal;
    protected double mdAuxDpsCommissionPaid;
    protected String msAuxDpsCurrencyKey;

    protected ArrayList<SDbCommission> maMktCommisionsEntryPayableRow;

    public void setAuxDpsType(String s) { msAuxDpsType = s; }
    public void setAuxDpsNumber(String s) { msAuxDpsNumber = s; }
    public void setAuxDpsDateDoc(Date t) { mtAuxDpsDateDoc = t; }
    public void setAuxDpsDateCommissionMaterialization(Date t) { mtAuxDpsDateCommissionMaterialization = t; }
    public void setAuxDpsBizPartner(String s) { msAuxDpsBizPartner = s; }
    public void setAuxDpsSubtotal(double d) { mdAuxDpsSubtotal = d; }
    public void setAuxDpsAdjustment(double d) { mdAuxDpsAdjustment = d; }
    public void setAuxDpsCommission(double d) { mdAuxDpsCommission  = d; }
    public void setAuxDpsCommissionAdjustment(double d) { mdAuxDpsCommissionAdjustment  = d; }
    public void setAuxDpsCommissionTotal(double d) { mdAuxDpsCommissionTotal = d; }
    public void setAuxDpsCommissionPaid(double d) { mdAuxDpsCommissionPaid = d; }
    public void setAuxDpsCurrencyKey(String s) { msAuxDpsCurrencyKey = s; }

    public String getAuxDpsType() { return msAuxDpsType; }
    public String getAuxDpsNumber() { return msAuxDpsNumber; }
    public Date getAuxDpsDateDoc() { return mtAuxDpsDateDoc; }
    public Date getAuxDpsDateCommissionMaterialization() { return mtAuxDpsDateCommissionMaterialization; }
    public String getAuxDpsBizPartner() { return msAuxDpsBizPartner; }
    public double getAuxDpsSubtotal() { return mdAuxDpsSubtotal; }
    public double getAuxDpsAdjustment() { return mdAuxDpsAdjustment; }
    public double getAuxDpsCommission() { return mdAuxDpsCommission; }
    public double getAuxDpsCommissionAdjustment() { return mdAuxDpsCommissionAdjustment; }
    public double getAuxDpsCommissionTotal() { return mdAuxDpsCommissionTotal; }
    public double getAuxDpsCommissionPaid() { return mdAuxDpsCommissionPaid; }
    public String getAuxDpsCurrencyKey() { return msAuxDpsCurrencyKey; }

    public ArrayList<SDbCommission> getMktCommisionsEntryPayableRow() { return maMktCommisionsEntryPayableRow; }

    public SMktCommissionsPayableRow() {
        maMktCommisionsEntryPayableRow = new ArrayList<SDbCommission>();

        msAuxDpsType = "";
        msAuxDpsNumber = "";
        mtAuxDpsDateDoc = null;
        mtAuxDpsDateCommissionMaterialization = null;
        msAuxDpsBizPartner = "";
        mdAuxDpsSubtotal = 0;
        mdAuxDpsAdjustment = 0;
        mdAuxDpsCommission = 0;
        mdAuxDpsCommissionAdjustment = 0;
        mdAuxDpsCommissionTotal = 0;
        mdAuxDpsCommissionPaid = 0;
        msAuxDpsCurrencyKey = "";

        maMktCommisionsEntryPayableRow.clear();
    }

    @Override
    public int[] getRowPrimaryKey() {
        return null;
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return false;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(final boolean edited) {

    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = msAuxDpsType;
                break;
            case 1:
                value = msAuxDpsNumber;
                break;
            case 2:
                value = mtAuxDpsDateDoc;
                break;
            case 3:
                value = msAuxDpsBizPartner;
                break;
            case 4:
                value = mdAuxDpsCommissionTotal;
                break;
            case 5:
                value = mdAuxDpsCommissionPaid;
                break;
            case 6:
                value = mdAuxDpsSubtotal;
                break;
            case 7:
                value = mdAuxDpsAdjustment;
                break;
            case 8:
                value = mdAuxDpsSubtotal - mdAuxDpsAdjustment;
                break;
            case 9:
                value = msAuxDpsCurrencyKey;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch(row) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                break;
            default:
        }
    }
}
