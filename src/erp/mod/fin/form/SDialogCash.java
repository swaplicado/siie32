/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.form;

import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataRecordEntry;
import erp.mod.fin.db.SDbAbpEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 *
 * @author Sergio Flores
 */
public interface SDialogCash {

    public void setMoveCash(final SDataAccountCash cash, final SDbAbpEntity abpCash);
    public void setMoveSettings(final int[] sysMoveTypeKey, final int moveMode, final Date date, final HashSet<Integer> currentChecks);
    public void setMoveData(final Object[] keyRecord, final SDataRecordEntry[] recordEntries) throws Exception;
    public ArrayList<SDataRecordEntry> getMoveData() throws Exception;
}
