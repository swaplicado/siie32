/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.data;

import erp.lib.SLibConstants;
import java.util.Vector;

/**
 *
 * @author Sergio Flores
 */
public abstract class SDataProcedure implements java.io.Serializable {

    protected int mnProcedureType;

    protected int mnLastDbActionResult;
    protected java.util.Vector<java.lang.Object> mvParamsIn;
    protected java.util.Vector<java.lang.Object> mvParamsOut;

    public SDataProcedure(int procedureType) {
        mnProcedureType = procedureType;

        mvParamsIn = new Vector<Object>();
        mvParamsOut = new Vector<Object>();
        resetProcedure();
    }

    public void resetProcedure() {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        mvParamsIn.clear();
        mvParamsOut.clear();
    }

    public void setProcedureType(int n) { mnProcedureType = n; }
    public void setLastDbActionResult(int n) { mnLastDbActionResult = n; }

    public int getProcedureType() { return mnProcedureType; }
    public int getLastDbActionResult() { return mnLastDbActionResult; }
    public java.util.Vector<java.lang.Object> getParamsIn() { return mvParamsIn; }
    public java.util.Vector<java.lang.Object> getParamsOut() { return mvParamsOut; }

    public abstract int call(java.sql.Connection connection);
}
