/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mod.SModSysConsts;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sa.lib.SLibConsts;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataDsm extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected java.util.Date mtDate;
    protected java.lang.String msConcept;
    protected boolean mbIsAudited;
    protected boolean mbIsAuthorized;
    protected boolean mbIsRecordAutomatic;
    protected boolean mbIsSystem;
    protected boolean mbIsDeleted;
    protected int mnFkSubsystemCategoryId;
    protected int mnFkCompanyBranchId;
    protected int mnFkUserAuditedId;
    protected int mnFkUserAuthorizedId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserAuditedTs;
    protected java.util.Date mtUserAuthorizedTs;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected boolean mbDbmsIsRecordSaved;
    protected int mnDbmsPkPeriodId;
    protected int mnDbmsPkBookkeepingCenterId;
    protected int mnDbmsFkCompanyBranch;
    protected int mnDbmsErpDecimalsValue;
    protected int mnDbmsTaxModel;
    protected java.lang.String msDbmsCompanyBranchCode;
    protected java.lang.String msDbmsPkRecordTypeId;
    protected java.lang.String msDbmsSubsystemTypeBiz;

    protected boolean mbAuxIsRecordAutomatic;
    protected java.util.Date mtDbmsRecordDate;
    protected java.lang.Object moAuxRecordUserKey;
    protected java.lang.Object moDbmsRecordKey;

    protected java.util.Vector<erp.mtrn.data.SDataDsmNotes> mvDbmsDsmNotes;
    protected java.util.Vector<erp.mtrn.data.SDataDsmEntry> mvDbmsDsmEntry;
    protected java.util.Vector<java.lang.Object> mvBalanceDps;

    protected erp.mfin.data.SDataRecord moDbmsRecord;
    protected int mnParamPkCheckWalletId;
    protected int mnParamPkCheckId;

    public SDataDsm() {
        super(SDataConstants.TRN_DSM);

        mvDbmsDsmNotes = new Vector<SDataDsmNotes>();
        mvDbmsDsmEntry = new Vector<SDataDsmEntry>();
        mvBalanceDps = new Vector<Object>();
        reset();
    }

    /*
     * Private functions
     */

    private erp.mfin.data.SDataRecord saveRecord(java.sql.Connection connection, boolean isNewRecord) {
        String sConcept = "";

        SDataRecord oRecord = null;
        Statement oStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {

            // Render concept:
            //System.out.println("msDbmsSubsystemTypeBiz: " + msDbmsSubsystemTypeBiz);
            //System.out.println("msDbmsCompanyBranchCode: " + msDbmsCompanyBranchCode);
            //System.out.println("mtDate: " + mtDate.toString());
            sConcept = msDbmsSubsystemTypeBiz + "; " + msDbmsCompanyBranchCode + "; " + moDateFormat.format(mtDate).toString();

            // 4.2.2 Save accounting record:

            //System.out.println("saveR 1");
            oRecord = new SDataRecord();
            oStatement = connection.createStatement();
            if (!isNewRecord) {
                //System.out.println("saveR 2");
                if (oRecord.read(moDbmsRecordKey, oStatement) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                if (oRecord.getIsDeleted()) {
                    //System.out.println("DELETE TRUE");
                    oRecord.setIsDeleted(false);
                }
                //System.out.println("!ISNEWREC");

                if (mbIsRecordAutomatic) {
                    oRecord.setConcept(sConcept.length() > 100 ? sConcept.substring(0, 96).trim() + "..." : sConcept);
                }
            }
            else {
                //System.out.println("saveR 3");
                //oRecord.setPrimaryKey(moDbmsRecordKey);
                oRecord.setPkYearId(moDbmsRecordKey != null ? (Integer) ((Object[]) moDbmsRecordKey)[0] : mnPkYearId);
                oRecord.setPkPeriodId(moDbmsRecordKey != null ? (Integer) ((Object[]) moDbmsRecordKey)[1] : mnDbmsPkPeriodId);
                oRecord.setPkBookkeepingCenterId(moDbmsRecordKey != null ? (Integer) ((Object[]) moDbmsRecordKey)[2] : mnDbmsPkBookkeepingCenterId);
                oRecord.setPkRecordTypeId(moDbmsRecordKey != null ? (String) ((Object[]) moDbmsRecordKey)[3] : msDbmsPkRecordTypeId);
                oRecord.setPkNumberId(0);
                oRecord.setConcept(sConcept.length() > 100 ? sConcept.substring(0, 96).trim() + "..." : sConcept);
                oRecord.setDate(mtDate);
                oRecord.setIsAudited(false);
                oRecord.setIsAuthorized(false);
                oRecord.setIsSystem(true);
                oRecord.setIsDeleted(false);
                oRecord.setFkCompanyBranchId(mnFkCompanyBranchId);
                oRecord.setFkCompanyBranchId_n(0);
                oRecord.setFkAccountCashId_n(0);

                if (mbIsRegistryNew) {
                    oRecord.setFkUserNewId(mnFkUserNewId);
                    oRecord.setFkUserEditId(mnFkUserNewId);
                }
                else {
                    oRecord.setFkUserNewId(mnFkUserEditId);
                    oRecord.setFkUserEditId(mnFkUserEditId);
                }
            }

            // Save record entries:

            mnLastDbActionResult = saveRecordEntries(connection, oRecord);

            if (mbDbmsIsRecordSaved) {
                //System.out.println("oR: " + oRecord.getPkRecordTypeId() + "-" + oRecord.getPkNumberId());
                if (oRecord.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
            }

            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK; // h6l2s9u1 773190995 4504

            /*
            // Check if there is record:

            if (moDbmsRecordKey != null && SLibTimeUtilities.isBelongingToPeriod(mtDate, (Integer) moDbmsRecordKey[0], (Integer) moDbmsRecordKey[1]) ) {
                nPkNumberId = (Integer) moDbmsRecordKey[4];
            }
            else {
                // Delete record if there is:

                if (moDbmsRecordKey != null) {
                    oRecord = new SDataRecord();
                    statementAux = connection.createStatement();

                    if (oRecord.read(moDbmsRecordKey, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
                    }
                    else {
                        oRecord.setIsDeleted(true);
                        oRecord.setFkUserDeleteId(mnFkUserEditId);

                        if (oRecord.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                           throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
                        }
                    }
                }

                nPkNumberId = 0;
            }

            oRecord = new SDataRecord();
            oRecord.setPkYearId(moDbmsRecordKey != null ? (Integer) moDbmsRecordKey[0] : mnPkYearId);
            oRecord.setPkPeriodId(moDbmsRecordKey != null ? (Integer) moDbmsRecordKey[1] : mnDbmsPkPeriodId);
            oRecord.setPkBookkeepingCenterId(moDbmsRecordKey != null ? (Integer) moDbmsRecordKey[2] : mnDbmsPkBookkeepingCenterId);
            oRecord.setPkRecordTypeId(moDbmsRecordKey != null ? (String) moDbmsRecordKey[3] : msDbmsPkRecordTypeId);
            oRecord.setPkNumberId(nPkNumberId > 0 ? nPkNumberId : 0);
            oRecord.setDate(mtDate);
            oRecord.setIsAudited(false);
            oRecord.setIsAuthorized(false);
            oRecord.setIsSystem(true);
            oRecord.setIsDeleted(mbIsDeleted == true ? mbIsDeleted : false);
            oRecord.setFkCompanyBranchId(mnFkCompanyBranchId);
            oRecord.setFkCompanyBranchId_n(0);
            oRecord.setFkAccountCashId_n(0);
            oRecord.setFkUserAuditedId(mnFkUserEditId);
            oRecord.setFkUserAuthorizedId(mnFkUserEditId);
            oRecord.setFkUserNewId(mnFkUserNewId);
            oRecord.setFkUserEditId(mnFkUserEditId);
            oRecord.setFkUserDeleteId(mnFkUserEditId);
            oRecord.setConcept(msDbmsSubsystemTypeBiz + "; " + msDbmsCompanyBranchCode + "; " + moDateFormat.format(mtDate).toString());
            //System.out.println("A1");
            if (oRecord.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }

            // Save record entries:
            //System.out.println("A2");
            mnLastDbActionResult = saveRecordEntries(connection, oRecord, nPkNumberId);
            //System.out.println("A3");
            */

            /*
            // III.6 Validate accounting record:
            //System.out.println("III.6 Purchases or sales taxes...");

            for (SDataRecordEntry entry : oRecord.getDbmsRecordEntries()) {
                dDebit += entry.getDebit();
                dCredit += entry.getCredit();
                dDebitCy += entry.getDebitCy();
                dCreditCy += entry.getCreditCy();
            }

            if (dDebit != dCredit) {
                //throw new Exception(SLibConstants.MSG_ERR_ACC_REC_BAL);
            }

            if (dDebitCy != dCreditCy) {
                //throw new Exception(SLibConstants.MSG_ERR_ACC_REC_BAL_CUR);
            }
             */
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return oRecord;
    }

    private int saveRecordEntries(java.sql.Connection connection, erp.mfin.data.SDataRecord oRecord) {
        int nSortPos = 0;
        String sConcept = "";
        Vector<Object> vEntries = new Vector<Object>();

        SDataDsmEntry oDsmEntry = null;
        SDataRecordEntry oRecordEntry = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            nSortPos = 1;
            for (int m = 0; m < mvDbmsDsmEntry.size(); m++) {
                oDsmEntry = (SDataDsmEntry) mvDbmsDsmEntry.get(m);
                if (oDsmEntry != null) {

                    // Render concept for the entry:

                    /**
                     * Version 1.0
                     *
                    sConcept = msDbmsSubsystemTypeBiz + "; " + msDbmsCompanyBranchCode + "; " +
                            moDateFormat.format(mtDate).toString() + "; " + oDsmEntry.getDbmsSubclassMove() + "; " +
                            renderSubsystemSourceToDestiny(oDsmEntry) + "; " +
                            oDsmEntry.getDbmsBiz();
                    */

                    sConcept = renderSubsystemSourceToDestiny(oDsmEntry);

                    // Get accounts debit and credit:

                    vEntries.removeAllElements();
                    //System.out.println("dataDsmEntry.getDbmsAccountPay(): " + oDsmEntry.getDbmsAccountPay());
                    //System.out.println("dataDsmEntry.getDbmsAccountOp(): " + oDsmEntry.getDbmsAccountOp());
                    vEntries.add(oDsmEntry.getDbmsAccountPay());
                    vEntries.add(oDsmEntry.getDbmsAccountOp());

                    for (int n = 0; n < vEntries.size(); n++) {

                        // Check if account isn't empty [SFormRecordApp]:

                        if (vEntries.get(n).toString().length() > 0) {
                            oRecordEntry = new SDataRecordEntry();

                            // oRecordEntry = createRecordEntryHeader(oRecordEntry, nPkNumberId > 0 ? nPkNumberId : oRecord.getPkNumberId());
                            oRecordEntry = createRecordEntryHeader(oRecordEntry, 0);
                            oRecordEntry = createRecordEntry(oRecordEntry, oDsmEntry, sConcept, nSortPos);

                            // Save accounting data depending of subsystem type:

                            oRecordEntry = renderReferenceDps(n, oDsmEntry, oRecordEntry);
                            oRecordEntry.setIsSystem(true);
                            oRecord.getDbmsRecordEntries().add(oRecordEntry);

                            //System.out.println("add ENTRY");

                            /*if (oRecordEntry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
                            }*/

                            nSortPos = nSortPos + 1;
                        }
                    }

                    // Record entry for taxes source:

                    if (oDsmEntry.getFkSourceDpsYearId_n() > 0 && oDsmEntry.getFkSourceDpsDocId_n() > 0) {
                        nSortPos = calculateDpsTaxes(connection, oRecord, oDsmEntry, SDataConstants.TRNX_DSM_ETY_SOURCE, nSortPos, 0, oDsmEntry.getFkSourceDpsYearId_n(), oDsmEntry.getFkSourceDpsDocId_n(), sConcept);
                    }

                    // Record entry for taxes destiny:

                    if (oDsmEntry.getFkDestinyDpsYearId_n() > 0 && oDsmEntry.getFkDestinyDpsDocId_n() > 0) {
                        nSortPos = calculateDpsTaxes(connection, oRecord, oDsmEntry, SDataConstants.TRNX_DSM_ETY_DESTINY, nSortPos, 0, oDsmEntry.getFkDestinyDpsYearId_n(), oDsmEntry.getFkDestinyDpsDocId_n(), sConcept);
                    }

                    mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
                }
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    private erp.mfin.data.SDataRecordEntry createRecordEntryHeader(erp.mfin.data.SDataRecordEntry  oRecordEntry, int nPkNumberId) {

        // Create record entry header:

        oRecordEntry.setPkYearId(moDbmsRecordKey != null ? (Integer)((Object[]) moDbmsRecordKey)[0] : mnPkYearId);
        oRecordEntry.setPkPeriodId(moDbmsRecordKey != null ? (Integer)((Object[]) moDbmsRecordKey)[1] : mnDbmsPkPeriodId);
        oRecordEntry.setPkBookkeepingCenterId(moDbmsRecordKey != null ? (Integer) ((Object[]) moDbmsRecordKey)[2] : mnDbmsPkBookkeepingCenterId);
        oRecordEntry.setPkRecordTypeId(moDbmsRecordKey != null ? (String)((Object[]) moDbmsRecordKey)[3] : msDbmsPkRecordTypeId);
        oRecordEntry.setPkNumberId(nPkNumberId);
        oRecordEntry.setPkEntryId(0);

        return oRecordEntry;
    }

    private erp.mfin.data.SDataRecordEntry createRecordEntry(erp.mfin.data.SDataRecordEntry oRecordEntry, erp.mtrn.data.SDataDsmEntry oDsmEntry, java.lang.String sConcept, int nSortPos) {

        // Create record entry complement:

        oRecordEntry.setConcept(sConcept.length() > 100 ? sConcept.substring(0, 96).trim() + "..." : sConcept);
        oRecordEntry.setReference("");
        oRecordEntry.setFkDpsYearId_n(0);
        oRecordEntry.setFkDpsDocId_n(0);
        oRecordEntry.setFkAccountIdXXX("");
        oRecordEntry.setUnits(0);
        oRecordEntry.setSortingPosition(nSortPos);
        oRecordEntry.setIsDeleted(oDsmEntry.getIsDeleted());
        oRecordEntry.setFkAccountingMoveTypeId(oDsmEntry.getFkAccountingMoveTypeId());
        oRecordEntry.setFkAccountingMoveClassId(oDsmEntry.getFkAccountingMoveClassId());
        oRecordEntry.setFkAccountingMoveSubclassId(oDsmEntry.getFkAccountingMoveSubclassId());
        oRecordEntry.setFkCostCenterIdXXX_n("");
        oRecordEntry.setFkCompanyBranchId_n(mnFkCompanyBranchId);
        oRecordEntry.setFkEntityId_n(0);
        oRecordEntry.setFkTaxBasicId_n(0);
        oRecordEntry.setFkTaxId_n(0);
        oRecordEntry.setFkBizPartnerId_nr(oDsmEntry.getFkBizPartnerId());
        oRecordEntry.setFkBizPartnerBranchId_n(oDsmEntry.getDbmsFkBizPartnerBranchId_n());
        oRecordEntry.setFkYearId_n(0);
        //oRecordEntry.setFkDsmYearId_n(mnPkYearId);    XXX obsolete code (sflores, 2013-06-27)
        //oRecordEntry.setFkDsmDocId_n(mnPkDocId);      XXX obsolete code (sflores, 2013-06-27)
        oRecordEntry.setFkReferenceCategoryId_n(mnFkSubsystemCategoryId);
        oRecordEntry.setFkDpsAdjustmentYearId_n(0);
        oRecordEntry.setFkDpsAdjustmentDocId_n(0);
        oRecordEntry.setFkDiogYearId_n(0);
        oRecordEntry.setFkDiogDocId_n(0);
        oRecordEntry.setFkItemId_n(0);
        oRecordEntry.setFkCheckWalletId_n(0);
        oRecordEntry.setFkCheckId_n(0);
        oRecordEntry.setFkUserNewId(mnFkUserNewId);
        oRecordEntry.setFkUserEditId(1);
        oRecordEntry.setFkUserDeleteId(1);
        oRecordEntry.setUserNewTs(mtDate);
        oRecordEntry.setUserEditTs(mtDate);
        oRecordEntry.setUserDeleteTs(mtDate);

        return oRecordEntry;
    }

    private int saveRecordEntriesTaxes(java.sql.Connection connection, erp.mfin.data.SDataRecord oRecord, erp.mfin.data.SDataRecordEntry oRecordEntry, erp.mtrn.data.SDataDsmEntry dataDsmEntry,
            int nPkNumberId, java.lang.String sConcept, int nDpsYearId, int nDpsDocId, int nDpsCategoryId,
            int nSortPos, Vector<Object> vTpSysMovId, int nPkTaxBasicId, int nPkTaxId, double dValueTax, double dValueTaxCur, int nTypeSource) {

        Vector<Object> vAccs = new Vector<Object>();

        // Add accounts (debit, credit):

        vAccs.add(vTpSysMovId.get(0));
        vAccs.add(vTpSysMovId.get(3));

        try {
            for (int j = 0; j < vAccs.size(); j++) {

                oRecordEntry = new SDataRecordEntry();
                oRecordEntry = createRecordEntryHeader(oRecordEntry, nPkNumberId);
                oRecordEntry = createRecordEntry(oRecordEntry, dataDsmEntry, sConcept, nSortPos);

                // Complement record entry:

                oRecordEntry.setFkDpsYearId_n(nDpsYearId);
                oRecordEntry.setFkDpsDocId_n(nDpsDocId);

                oRecordEntry.setFkAccountIdXXX(vAccs.get(j).toString());
                oRecordEntry.setFkTaxBasicId_n(nPkTaxBasicId);
                oRecordEntry.setFkTaxId_n(nPkTaxId);
                oRecordEntry = renderRecordEntryValuesTaxes(oRecordEntry, dataDsmEntry, j, dValueTax, dValueTaxCur, nTypeSource, vTpSysMovId, nDpsCategoryId);
                oRecordEntry.setIsSystem(true);
                oRecord.getDbmsRecordEntries().add(oRecordEntry);

                nSortPos = nSortPos + 1;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return nSortPos;
    }

    private erp.mfin.data.SDataRecordEntry renderRecordEntryValuesTaxes(erp.mfin.data.SDataRecordEntry oRecordEntry, erp.mtrn.data.SDataDsmEntry oDsmEntry, int j, double dValueTax, double dValueTaxCur, int nTypeSource, Vector<Object> vTpSysMovId, int nDpsCategory) {
        int[] sysMoveTypeKey = null;

        // Assign values depending if it is debit or credit:

        if (j == 0) {
            oRecordEntry.setDebit(dValueTax);
            oRecordEntry.setCredit(0);
            oRecordEntry.setDebitCy(dValueTaxCur);
            oRecordEntry.setCreditCy(0);

            // Set type system move:

            //System.out.println("nTypeSysMove[0]: " + ((Integer)((int [])  vTpSysMovId.get(1))[0]));
            oRecordEntry.setFkSystemMoveCategoryIdXXX((Integer)((int [])  vTpSysMovId.get(1))[0]);

            //System.out.println("nTypeSysMove[1]: " + ((Integer)((int [])  vTpSysMovId.get(1))[1]));
            oRecordEntry.setFkSystemMoveTypeIdXXX((Integer)((int [])  vTpSysMovId.get(1))[1]);

            //System.out.println("nTypeSysMove[2]: " + (vTpSysMovId.get(2).toString()));
            oRecordEntry.setReference(vTpSysMovId.get(2).toString());

            switch (nDpsCategory) {
                case SDataConstantsSys.TRNS_CT_DPS_PUR:
                    sysMoveTypeKey = SModSysConsts.FINS_TP_SYS_MOV_PUR_TAX_DBT_PAI;
                    break;
                case SDataConstantsSys.TRNS_CT_DPS_SAL:
                    sysMoveTypeKey = SModSysConsts.FINS_TP_SYS_MOV_SAL_TAX_DBT_PAI;
                    break;
                default:
            }
        }
        else {
            oRecordEntry.setDebit(0);
            oRecordEntry.setCredit(dValueTax);
            oRecordEntry.setDebitCy(0);
            oRecordEntry.setCreditCy(dValueTaxCur);

            // Set type system move:

            //System.out.println("Q14");

            //System.out.println("nTypeSysMove[2]: " + ((Integer)((int [])  vTpSysMovId.get(4))[0]));
            oRecordEntry.setFkSystemMoveCategoryIdXXX((Integer)((int [])  vTpSysMovId.get(4))[0]);

            //System.out.println("nTypeSysMove[3]: " + ((Integer)((int [])  vTpSysMovId.get(4))[1]));
            oRecordEntry.setFkSystemMoveTypeIdXXX((Integer)((int [])  vTpSysMovId.get(4))[1]);

            //System.out.println("nTypeSysMove[5]: " + (vTpSysMovId.get(5).toString()));
            oRecordEntry.setReference(vTpSysMovId.get(5).toString());

            switch (nDpsCategory) {
                case SDataConstantsSys.TRNS_CT_DPS_PUR:
                    sysMoveTypeKey = SModSysConsts.FINS_TP_SYS_MOV_PUR_TAX_CDT_PAI;
                    break;
                case SDataConstantsSys.TRNS_CT_DPS_SAL:
                    sysMoveTypeKey = SModSysConsts.FINS_TP_SYS_MOV_SAL_TAX_CDT_PAI;
                    break;
                default:
            }
        }

        oRecordEntry.setFkSystemMoveClassId(sysMoveTypeKey[0]);
        oRecordEntry.setFkSystemMoveTypeId(sysMoveTypeKey[1]);
        oRecordEntry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[0]);
        oRecordEntry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[1]);

        if (nTypeSource == SDataConstants.TRNX_DSM_ETY_SOURCE) {
            oRecordEntry.setExchangeRate(oDsmEntry.getSourceExchangeRate());
            oRecordEntry.setExchangeRateSystem(oDsmEntry.getSourceExchangeRateSystem());
            oRecordEntry.setFkCurrencyId(oDsmEntry.getFkSourceCurrencyId());
        }
        else {
            oRecordEntry.setExchangeRate(oDsmEntry.getDestinyExchangeRate());
            oRecordEntry.setExchangeRateSystem(oDsmEntry.getDestinyExchangeRateSystem());
            oRecordEntry.setFkCurrencyId(oDsmEntry.getFkDestinyCurrencyId());
        }

        return oRecordEntry;
    }

    private erp.mfin.data.SDataRecordEntry renderCreditDebit(erp.mfin.data.SDataRecordEntry oRecordEntry, java.lang.String sRef, int nDpsYearId, int nDpsDocId,
            double dDebit, double dCredit, double dDebitCy, double dCreditCy, double dExchangeRate, double dExchangeRateSystem, int nFkCurrencyId, java.lang.String sFkAccountId,
            int[] anSysAccountTypeKey, int[] anSysMoveTypeKey, int[] anSysMoveTypeKeyXXX) {

        oRecordEntry.setDebit(dDebit);
        oRecordEntry.setCredit(dCredit);
        oRecordEntry.setDebitCy(dDebitCy);
        oRecordEntry.setCreditCy(dCreditCy);
        oRecordEntry.setExchangeRate(dExchangeRate);
        oRecordEntry.setExchangeRateSystem(dExchangeRateSystem);
        oRecordEntry.setFkCurrencyId(nFkCurrencyId);
        oRecordEntry.setReference(sRef);
        oRecordEntry.setFkDpsYearId_n(nDpsYearId);
        oRecordEntry.setFkDpsDocId_n(nDpsDocId);
        oRecordEntry.setFkAccountIdXXX(sFkAccountId);
        oRecordEntry.setFkSystemMoveClassId(anSysMoveTypeKey[0]);
        oRecordEntry.setFkSystemMoveTypeId(anSysMoveTypeKey[1]);
        oRecordEntry.setFkSystemAccountClassId(anSysAccountTypeKey[0]);
        oRecordEntry.setFkSystemAccountTypeId(anSysAccountTypeKey[1]);
        oRecordEntry.setFkSystemMoveCategoryIdXXX(anSysMoveTypeKeyXXX[0]);
        oRecordEntry.setFkSystemMoveTypeIdXXX(anSysMoveTypeKeyXXX[1]);

        return oRecordEntry;
    }

    private erp.mfin.data.SDataRecordEntry renderReferenceDps(int pnMoveType, erp.mtrn.data.SDataDsmEntry poDsmEntry, erp.mfin.data.SDataRecordEntry poRecordEntry) {
        int[] anAccMovSubtype = new int[] {poDsmEntry.getFkAccountingMoveTypeId(), poDsmEntry.getFkAccountingMoveClassId(), poDsmEntry.getFkAccountingMoveSubclassId() };

        if (pnMoveType == 0) {
            if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, poDsmEntry.getSourceReference(), 0, 0,
                            0, poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_MI_SUP_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_CDR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, poDsmEntry.getSourceReference(), 0, 0,
                            poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), 0, poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_DBR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_TRA)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, poDsmEntry.getSourceReference(), 0, 0,
                            0, poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, poDsmEntry.getSourceReference(), 0, 0,
                            poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), 0, poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_CLO)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, poDsmEntry.getSourceReference(), 0, 0,
                            0, poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_DEC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_DEC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, poDsmEntry.getSourceReference(), 0, 0,
                            poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), 0, poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_DEC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_DEC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_OPE)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", 0, 0,
                            0, poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getFkSourceAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", 0, 0,
                            poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), 0, poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getFkSourceAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_APP)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", poDsmEntry.getFkSourceDpsYearId_n(), poDsmEntry.getFkSourceDpsDocId_n(),
                            poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), 0, poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_MI_SUP_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_CDR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", poDsmEntry.getFkSourceDpsYearId_n(), poDsmEntry.getFkSourceDpsDocId_n(),
                            0, poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_DBR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_TRA)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", poDsmEntry.getFkSourceDpsYearId_n(), poDsmEntry.getFkSourceDpsDocId_n(),
                            poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), 0, poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", poDsmEntry.getFkSourceDpsYearId_n(), poDsmEntry.getFkSourceDpsDocId_n(),
                            0, poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_CLO)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", poDsmEntry.getFkSourceDpsYearId_n(), poDsmEntry.getFkSourceDpsDocId_n(),
                            poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), 0, poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_DEC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_DEC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", poDsmEntry.getFkSourceDpsYearId_n(), poDsmEntry.getFkSourceDpsDocId_n(),
                            0, poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_DEC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_DEC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_OPE)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", 0, 0,
                            poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), 0, poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getFkSourceAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", 0, 0,
                            0, poDsmEntry.getSourceValue(), 0, poDsmEntry.getSourceValueCy(), poDsmEntry.getSourceExchangeRate(),
                            poDsmEntry.getSourceExchangeRateSystem(), poDsmEntry.getFkSourceCurrencyId(), poDsmEntry.getFkSourceAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                }
            }
        }
        else {
            if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", poDsmEntry.getFkDestinyDpsYearId_n(), poDsmEntry.getFkDestinyDpsDocId_n(),
                            poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), 0, poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_MI_SUP_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_CDR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", poDsmEntry.getFkDestinyDpsYearId_n(), poDsmEntry.getFkDestinyDpsDocId_n(),
                            0, poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_DBR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_TRA)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, poDsmEntry.getDestinyReference(), 0, 0,
                            poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), 0, poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, poDsmEntry.getDestinyReference(), 0, 0,
                            0, poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_CLO)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", 0, 0,
                            poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), 0, poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getFkDestinyAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_DEC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_DEC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", 0, 0,
                            0, poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getFkDestinyAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_DBR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_OPE)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, poDsmEntry.getDestinyReference(), 0, 0,
                            poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), 0, poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, poDsmEntry.getDestinyReference(), 0, 0,
                            0, poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_APP)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, poDsmEntry.getDestinyReference(), 0, 0,
                            0, poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_MI_SUP_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_CDR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, poDsmEntry.getDestinyReference(), 0, 0,
                            poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), 0, poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getDbmsAccountPay(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_PAY : SModSysConsts.FINS_TP_SYS_MOV_MI_DBR_PAY,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_TRA)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", poDsmEntry.getFkDestinyDpsYearId_n(), poDsmEntry.getFkDestinyDpsDocId_n(),
                            0, poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", poDsmEntry.getFkDestinyDpsYearId_n(), poDsmEntry.getFkDestinyDpsDocId_n(),
                            poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), 0, poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_CLO)) {
                switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", 0, 0,
                            0, poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getFkDestinyAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_DEC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_DEC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", 0, 0,
                            poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), 0, poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getFkDestinyAccountId_n(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_DEC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_DEC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA);
                        break;
                }
            }
            else if (SLibUtilities.compareKeys(anAccMovSubtype, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_OPE)) {
                 switch (mnFkSubsystemCategoryId) {
                    case SDataConstantsSys.BPSS_CT_BP_SUP:
                    case SDataConstantsSys.BPSS_CT_BP_CDR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", poDsmEntry.getFkDestinyDpsYearId_n(), poDsmEntry.getFkDestinyDpsDocId_n(),
                            0, poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_SUP ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP);
                        break;
                    case SDataConstantsSys.BPSS_CT_BP_CUS:
                    case SDataConstantsSys.BPSS_CT_BP_DBR:
                        poRecordEntry = renderCreditDebit(poRecordEntry, "", poDsmEntry.getFkDestinyDpsYearId_n(), poDsmEntry.getFkDestinyDpsDocId_n(),
                            poDsmEntry.getDestinyValue(), 0, poDsmEntry.getDestinyValueCy(), 0, poDsmEntry.getDestinyExchangeRate(),
                            poDsmEntry.getDestinyExchangeRateSystem(), poDsmEntry.getFkDestinyCurrencyId(), poDsmEntry.getDbmsAccountOp(),
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL : SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL,
                            mnFkSubsystemCategoryId == SDataConstantsSys.BPSS_CT_BP_CUS ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_TRA : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_TRA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS);
                        break;
                }
            }
        }

        return poRecordEntry;
    }

    private java.lang.String renderReference(erp.mtrn.data.SDataDsmEntry dataDsmEntry, boolean bSource) {
        String sReference = "";

        sReference = "REP. '" + (bSource ? dataDsmEntry.getSourceReference() : dataDsmEntry.getDestinyReference())  + "' ";

        return sReference;
    }

    private java.lang.String renderSubsystemSourceToDestiny(erp.mtrn.data.SDataDsmEntry oDsmEntry) {
        Object oKey = new int[] {oDsmEntry.getFkAccountingMoveTypeId(), oDsmEntry.getFkAccountingMoveClassId(), oDsmEntry.getFkAccountingMoveSubclassId() };
        String sSubsystem = "";

        if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP)) {
            sSubsystem = "APP; " + oDsmEntry.getDbmsDestinyTpDps() + " " + oDsmEntry.getDbmsDestinyDps() + "; " +
                    (oDsmEntry.getDbmsBiz().toString().length() > 30 ? oDsmEntry.getDbmsBiz().toString().substring(0, 27) + "..." : oDsmEntry.getDbmsBiz()) + "; " +
                    renderReference(oDsmEntry, true);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_TRA)) {
            sSubsystem = "TRP; " + renderReference(oDsmEntry, true) + " > " + renderReference(oDsmEntry, false) + "; " +
                    (oDsmEntry.getDbmsBiz().toString().length() > 30 ? oDsmEntry.getDbmsBiz().toString().substring(0, 27) + "..." : oDsmEntry.getDbmsBiz());
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_CLO)) {
            sSubsystem = "CIP; " + oDsmEntry.getFkDestinyAccountId_n() + "; " +
                    (oDsmEntry.getDbmsBiz().toString().length() > 30 ? oDsmEntry.getDbmsBiz().toString().substring(0, 27) + "..." : oDsmEntry.getDbmsBiz()) + "; "  +
                    renderReference(oDsmEntry, true);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_OPE)) {
            sSubsystem = "ABP; " + oDsmEntry.getFkSourceAccountId_n() + "; " +
                    (oDsmEntry.getDbmsBiz().toString().length() > 30 ? oDsmEntry.getDbmsBiz().toString().substring(0, 27) + "..." : oDsmEntry.getDbmsBiz()) + "; "  +
                    renderReference(oDsmEntry, false);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_APP)) {
            sSubsystem = "APS; " + oDsmEntry.getDbmsSourceTpDps() + " " + oDsmEntry.getDbmsSourceDps() + "; " +
                    (oDsmEntry.getDbmsBiz().toString().length() > 30 ? oDsmEntry.getDbmsBiz().toString().substring(0, 27) + "..." : oDsmEntry.getDbmsBiz()) + "; "  +
                    renderReference(oDsmEntry, false);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_TRA)) {
            sSubsystem = "TRS; " + oDsmEntry.getDbmsSourceTpDps() + " " + oDsmEntry.getDbmsSourceDps() + " > " +
                    oDsmEntry.getDbmsDestinyTpDps() + " " + oDsmEntry.getDbmsDestinyDps() + "; " +
                    (oDsmEntry.getDbmsBiz().toString().length() > 30 ? oDsmEntry.getDbmsBiz().toString().substring(0, 27) + "..." : oDsmEntry.getDbmsBiz());
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_CLO)) {
            sSubsystem = "CIS; " + oDsmEntry.getDbmsSourceTpDps() + " " + oDsmEntry.getDbmsSourceDps() + "; " +
                    (oDsmEntry.getDbmsBiz().toString().length() > 30 ? oDsmEntry.getDbmsBiz().toString().substring(0, 27) + "..." : oDsmEntry.getDbmsBiz()) + "; "  +
                     oDsmEntry.getFkDestinyAccountId_n();
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_OPE)) {
            sSubsystem = "ABS; " + oDsmEntry.getDbmsDestinyTpDps() + " " + oDsmEntry.getDbmsDestinyDps() + "; " +
                    (oDsmEntry.getDbmsBiz().toString().length() > 30 ? oDsmEntry.getDbmsBiz().toString().substring(0, 27) + "..." : oDsmEntry.getDbmsBiz()) + "; "  +
                    oDsmEntry.getFkSourceAccountId_n();
        }

        return sSubsystem;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtSupSrc(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {

        // [DEBIT] Set values for type system move:

        // System.out.println("SEUP SRC SModSysConsts.FINS_TP_TAX_CHARGED: " + SModSysConsts.FINS_TP_TAX_CHARGED +
        //        " sAccTaxPay: " + sAccTaxPay +
        //        " sAccTaxPayPend: " + sAccTaxPayPend);

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPay : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPay);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtCusSrc(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {

        // [DEBIT] Set values for type system move (outstanding tax charge):

        // System.out.println("CUS SRC SModSysConsts.FINS_TP_TAX_CHARGED: " + SModSysConsts.FINS_TP_TAX_CHARGED +
        //        " sAccTaxPay: " + sAccTaxPay +
        //        " sAccTaxPayPend: " + sAccTaxPayPend);

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPay);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move (tax charge):

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPay : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtSupDest(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {

        // System.out.println("SModSysConsts.FINS_TP_TAX_CHARGED: " + SModSysConsts.FINS_TP_TAX_CHARGED +
        //        " sAccTaxPay: " + sAccTaxPay +
        //        " sAccTaxPayPend: " + sAccTaxPayPend);

        // [DEBIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPay);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPay : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtCusDest(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {

        // [DEBIT] Set values for type system move (outstanding tax charge):

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPay : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move (tax charge):

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPay);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtSupSrcAdv(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType) {

        // [DEBIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPendAdv : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND_ADV : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPayPendAdv);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND_ADV);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtSupDestAdv(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType) {

        // [DEBIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPayPendAdv);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND_ADV);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPendAdv : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND_ADV : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtCusSrcAdv(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType) {

        // [DEBIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPayPendAdv);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND_ADV);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPendAdv : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND_ADV : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtCusDestAdv(java.util.Vector<Object> vTpSysMov, java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType) {

        // [DEBIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPendAdv : sAccTaxPayPend);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND_ADV : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND);
        vTpSysMov.add("");

        // [CREDIT] Set values for type system move:

        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? sAccTaxPayPend : sAccTaxPayPendAdv);
        vTpSysMov.add(nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND_ADV);
        vTpSysMov.add("");

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtPayApp(java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {

        Vector<Object> vTpSysMov = new Vector<Object>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        // System.out.println("mnFkSubsystemCategoryId: " + mnFkSubsystemCategoryId);

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                vTpSysMov = renderTaxAccDbtCdtSupSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                vTpSysMov = renderTaxAccDbtCdtCusSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtPayTra(java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType, int nTypeSource) {

        Vector<Object> vTpSysMov = new Vector<Object>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:

                // Check if document is source or destiny:

                if (nTypeSource == SDataConstants.TRNX_DSM_ETY_SOURCE) {
                    vTpSysMov = renderTaxAccDbtCdtSupSrcAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                }
                else {
                    vTpSysMov = renderTaxAccDbtCdtSupDestAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                }
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:

                // Check if document is source or destiny:

                if (nTypeSource == SDataConstants.TRNX_DSM_ETY_SOURCE) {
                    vTpSysMov = renderTaxAccDbtCdtCusSrcAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                }
                else {
                    vTpSysMov = renderTaxAccDbtCdtCusDestAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                }
                break;
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtPayClo(java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType) {

        Vector<Object> vTpSysMov = new Vector<Object>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                vTpSysMov = renderTaxAccDbtCdtSupSrcAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                vTpSysMov = renderTaxAccDbtCdtCusSrcAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                break;
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtPayOpen(java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType) {

        Vector<Object> vTpSysMov = new Vector<Object>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                vTpSysMov = renderTaxAccDbtCdtSupDestAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                vTpSysMov = renderTaxAccDbtCdtCusDestAdv(vTpSysMov, sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
                break;
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtBalApp(java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {

        Vector<Object> vTpSysMov = new Vector<Object>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                vTpSysMov = renderTaxAccDbtCdtSupSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                vTpSysMov = renderTaxAccDbtCdtCusSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtBalTra(java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType, int nTypeSource) {

        Vector<Object> vTpSysMov = new Vector<Object>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:

                // Check if document is source or destiny:

                if (nTypeSource == SDataConstants.TRNX_DSM_ETY_SOURCE) {
                    vTpSysMov = renderTaxAccDbtCdtSupSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                }
                else {
                    vTpSysMov = renderTaxAccDbtCdtSupDest(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                }
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:

                // Check if document is source or destiny:

                if (nTypeSource == SDataConstants.TRNX_DSM_ETY_SOURCE) {
                    vTpSysMov = renderTaxAccDbtCdtCusSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                }
                else {
                    vTpSysMov = renderTaxAccDbtCdtCusDest(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                }
                break;
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtBalClo(java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {

        Vector<Object> vTpSysMov = new Vector<Object>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                vTpSysMov = renderTaxAccDbtCdtSupSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                vTpSysMov = renderTaxAccDbtCdtCusSrc(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdtBalOpen(java.lang.String sAccTaxPay, java.lang.String sAccTaxPayPend, int nPkTaxType) {

        Vector<Object> vTpSysMov = new Vector<Object>(); // 0. DB_ACC, 1. DBT_TP, 2. DBT_REF, 3. CDT_ACC, 4. CDT_TP, 5. CDT_REF

        switch (mnFkSubsystemCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                vTpSysMov = renderTaxAccDbtCdtSupDest(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                vTpSysMov = renderTaxAccDbtCdtCusDest(vTpSysMov, sAccTaxPay, sAccTaxPayPend, nPkTaxType);
                break;
            default:
        }

        return vTpSysMov;
    }

    private java.util.Vector<Object> renderTaxAccDbtCdt(erp.mtrn.data.SDataDsmEntry oDsmEntry, java.util.Vector<Object> vTpSysMov, java.lang.Object oKey, java.lang.String sAccount, java.lang.String sAccTaxPay,
            java.lang.String sAccTaxPayPend, java.lang.String sAccTaxPayPendAdv, int nPkTaxType, int nTypeSource) {

        if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP)) {
            vTpSysMov = renderTaxAccDbtCdtPayApp(sAccTaxPay, sAccTaxPayPend, nPkTaxType);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_TRA)) {
            vTpSysMov = renderTaxAccDbtCdtPayTra(sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType, nTypeSource);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_CLO)) {
            vTpSysMov = renderTaxAccDbtCdtPayClo(sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_OPE)) {
            vTpSysMov = renderTaxAccDbtCdtPayOpen(sAccTaxPayPend, sAccTaxPayPendAdv, nPkTaxType);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_APP)) {
            vTpSysMov = renderTaxAccDbtCdtBalApp(sAccTaxPay, sAccTaxPayPend, nPkTaxType);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_TRA)) {
            vTpSysMov = renderTaxAccDbtCdtBalTra(sAccTaxPay, sAccTaxPayPend, nPkTaxType, nTypeSource);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_CLO)) {
            vTpSysMov = renderTaxAccDbtCdtBalClo(sAccTaxPay, sAccTaxPayPend, nPkTaxType);
        }
        else if (SLibUtilities.compareKeys(oKey, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_OPE)) {
            vTpSysMov = renderTaxAccDbtCdtBalOpen(sAccTaxPay, sAccTaxPayPend, nPkTaxType);
        }

        return vTpSysMov;
    }

    private int calculateDpsTaxes(java.sql.Connection connection, erp.mfin.data.SDataRecord oRecord, erp.mtrn.data.SDataDsmEntry oDsmEntry, int nTypeSource, int nSortPos, int nPkNumberId, int nDpsYearId, int nDpsDocId, java.lang.String sConcept) {
        boolean bRefTax = false;
        double dValueTax = 0;
        double dValueTaxCur = 0;
        int nPkTaxBasicId = 0;
        int nPkTaxId = 0;
        int nPkTaxType = 0;
        int nPkTaxTypeApp = 0;
        int nDpsCategory = SLibConsts.UNDEFINED;
        Date tPkDateStartId = null;
        Object oDpsBalance = null;
        Object oDpsTaxBalance = null;
        String sSql = "";
        String sTaxAccountPayId = "";
        String sTaxAccountPayPendId = "";
        Vector<Object> vTpSysMov = new Vector<Object>();
        int[] anTypeSystemMove = new int[] {0, 0};

        Statement statementAux = null;
        Statement statementAux1 = null;
        ResultSet resultSet = null;
        ResultSet resultSet1 = null;
        SDataRecordEntry oRecordEntry = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            statementAux = connection.createStatement();

            // Get DPS category:

            sSql = "SELECT fid_ct_dps FROM trn_dps WHERE id_year = " + nDpsYearId + " AND id_doc = " + nDpsDocId + " ";
            resultSet = statementAux.executeQuery(sSql);
            if (resultSet.next()) {
                nDpsCategory = resultSet.getInt(1);
            }

            // Get type taxes for DPS document:

            sSql = "SELECT tx.* " +
                "FROM trn_dps_ety AS et " +
                "INNER JOIN trn_dps_ety_tax AS tx ON et.id_year = tx.id_year AND et.id_doc = tx.id_doc AND et.id_ety = tx.id_ety AND " +
                "et.b_del = FALSE " +
                "WHERE tx.id_year = " + nDpsYearId + " AND " +
                "tx.id_doc = " + nDpsDocId + " " +
                "GROUP BY tx.id_tax_bas, tx.id_tax";

            //System.out.println("sSql TYPE: " + sSql);

            resultSet = statementAux.executeQuery(sSql);
            while (resultSet.next()) {

                // Read type tax:

                nPkTaxType = resultSet.getInt("tx.fid_tp_tax");
                nPkTaxBasicId = resultSet.getInt("tx.id_tax_bas");
                nPkTaxId = resultSet.getInt("tx.id_tax");
                nPkTaxTypeApp = resultSet.getInt("tx.fid_tp_tax_app");
                tPkDateStartId = mtDate;
                if (nPkTaxTypeApp == SModSysConsts.FINS_TP_TAX_APP_CASH) {

                    // Check if reference has taxes:

                    if (bRefTax) {

                        // Code for next version:

                        //Read from table: TRN_ACC_TAX
                        //Parameters: ID_TAX_BAS, ID_TAX, ID_TP_DOC, APPLICATION_DATE
                        //Read account FID_ACC_PAY_PEND_ADV
                        //Add to vector: vEntries
                    }
                    else {
                        // Find account for debit and credit:

                        statementAux1 = connection.createStatement();
                        sSql = "SELECT a.*, t.per " +
                            "FROM fin_acc_tax AS a " +
                            "INNER JOIN erp.finu_tax AS t ON a.id_tax_bas = t.id_tax_bas AND a.id_tax = t.id_tax " +
                            "WHERE a.b_del = FALSE AND " +
                            "a.id_tax_bas = " + nPkTaxBasicId + " AND " +
                            "a.id_tax = " + nPkTaxId + " AND " +
                            "a.id_ct_dps = " + oDsmEntry.getDbmsFkDpsCategoryId() + " AND " +
                            "a.id_dt_start <= '" + tPkDateStartId + "' " +
                            "ORDER BY a.id_dt_start DESC ";

                        //System.out.println("sSql FIN ACCOUNT: " + sSql);

                        resultSet1 = statementAux1.executeQuery(sSql);
                        if (resultSet1.next()) {
                            sTaxAccountPayId = resultSet1.getString("a.fid_acc_pay");
                            sTaxAccountPayPendId = resultSet1.getString("a.fid_acc_pay_pend");
                            //dPercentageTax = resultSet1.getDouble("t.per");

                            // Get accountId for debit and credit, and systemMoveId (ct and tp):

                            vTpSysMov = renderTaxAccDbtCdt(oDsmEntry, vTpSysMov,
                                    new int [] { oDsmEntry.getFkAccountingMoveTypeId(), oDsmEntry.getFkAccountingMoveClassId(), oDsmEntry.getFkAccountingMoveSubclassId() },
                                    (oDsmEntry.getFkSourceAccountId_n().length() > 0 ? oDsmEntry.getFkSourceAccountId_n() : oDsmEntry.getFkDestinyAccountId_n()),
                                    sTaxAccountPayId, sTaxAccountPayPendId, "", nPkTaxType, nTypeSource);
                            // System.out.println("vTpSysMov: " + vTpSysMov +
                            //        " oDsmEntry.getFkAccountingMoveTypeId(): " + oDsmEntry.getFkAccountingMoveTypeId() +
                            //        " oDsmEntry.getFkAccountingMoveClassId(): " + oDsmEntry.getFkAccountingMoveClassId() +
                            //        " oDsmEntry.getFkAccountingMoveSubclassId(): " + oDsmEntry.getFkAccountingMoveSubclassId() +
                            //        " oDsmEntry.getFkSourceAccountId_n(): " + oDsmEntry.getFkSourceAccountId_n() +
                            //        " oDsmEntry.getFkDestinyAccountId_n(): " + oDsmEntry.getFkDestinyAccountId_n() +
                            //        " sTaxAccountPayId:  " + sTaxAccountPayId +
                            //        " sTaxAccountPayPendId: " + sTaxAccountPayPendId +
                            //        " nPkTaxType: " + nPkTaxType +
                            //        " nTypeSource: " + nTypeSource);


                            // Obtain tp_sys_mov_id (purchase, sales):

                            //System.out.println("nPkTaxType: " + nPkTaxType + " == " + " SModSysConsts.FINS_TP_TAX_CHARGED: " + SModSysConsts.FINS_TP_TAX_CHARGED);
                            //System.out.println("nPkTaxType: " + nPkTaxType + " == " + " SModSysConsts.FINS_TP_TAX_RETAINED: " + SModSysConsts.FINS_TP_TAX_RETAINED);

                            if (oDsmEntry.getDbmsTpSysMovId() == SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1]) {

                                if (nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED) {
                                    anTypeSystemMove = (int[]) (Object) vTpSysMov.get(4);
                                }
                                else{
                                    anTypeSystemMove = (int[]) (Object) vTpSysMov.get(1);
                                }
                            }
                            else {
                                if (nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED) {
                                    anTypeSystemMove = (int[]) (Object) vTpSysMov.get(1);
                                }
                                else{
                                    anTypeSystemMove = (int[]) (Object) vTpSysMov.get(4);
                                }
                            }

                            //System.out.println("anTypeSystemMove[0]: " + anTypeSystemMove[0]);
                            //System.out.println("anTypeSystemMove[1]: " + anTypeSystemMove[1]);
                        }
                    }

                    // Get current balance of dps:

                    oDpsBalance = calculateCurrentBalanceDps(connection, nDpsYearId, nDpsDocId, new int[] { oDsmEntry.getDbmsCtSysMovId(), oDsmEntry.getDbmsTpSysMovId() });
                    //System.out.println("CT_SYS_MOV_ID: " + oDsmEntry.getDbmsCtSysMovId() + " TP_SYS_MOV_ID: " + oDsmEntry.getDbmsTpSysMovId());
                    //System.out.println("BALANCE CUR: " + (((double [])oDpsBalance)[1]) +
                    //     " BALANCE: " + (((double [])oDpsBalance)[0]) +
                    //     " dataDsmEntry.getSourceValue(): " + oDsmEntry.getSourceValueCy());

                    // If balance of Dps is 0 (zero), assign all tax balance:

                    if ((((double [])oDpsBalance)[1]) == 0 || ((double [])oDpsBalance)[1] == oDsmEntry.getSourceValueCy())  {

                        // Read the balance net of tax:

                         oDpsTaxBalance = calculateDpsTaxBalanceNet(connection, nDpsYearId, nDpsDocId, new int[] { oDsmEntry.getDbmsCtSysMovId(), oDsmEntry.getDbmsTpSysMovId() }, oDsmEntry.getFkBizPartnerId(), nPkTaxType, nPkTaxBasicId, nPkTaxId);
                         //System.out.println("[NET]((double [])oDpsTaxBalance)[0]: " + ((double [])oDpsTaxBalance)[0]);
                         //System.out.println("((double [])oDpsTaxBalance)[1]: " + ((double [])oDpsTaxBalance)[1]);

                        // Read the current balance of tax:

                        statementAux1 = connection.createStatement();
                        /*
                            sSql = "SELECT " + (oDsmEntry.getDbmsTpSysMovId() == SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] ?
                                (nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? ((double [])oDpsTaxBalance)[0] + " - SUM(debit) " : ((double [])oDpsTaxBalance)[0] + " - SUM(credit) ") + "AS balance, " +
                                (nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? ((double [])oDpsTaxBalance)[1] + " - SUM(debit_cur) " : ((double [])oDpsTaxBalance)[1] + " - SUM(credit_cur) ") + "AS balance_cur " :
                                (nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? ((double [])oDpsTaxBalance)[0] + " - SUM(credit) " : ((double [])oDpsTaxBalance)[0] + " - SUM(debit) ") + "AS balance, " +
                                (nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? ((double [])oDpsTaxBalance)[1] + " - SUM(credit_cur) " : ((double [])oDpsTaxBalance)[1] + " - SUM(debit_cur) ") + "AS balance_cur ") +
                                "FROM fin_rec_ety " +
                                "WHERE b_del = FALSE AND " +
                                "fid_bp_nr = " + oDsmEntry.getFkBizPartnerId() + " AND fid_tax_bas_n = " + nPkTaxBasicId + " AND " +
                                "fid_tax_n = " + nPkTaxId + " AND fid_dps_year_n = " + nDpsYearId +  " AND fid_dps_doc_n = " + nDpsDocId +  " AND " +
                                "fid_acc = '" + sTaxAccountPayPendId + "'";
                        */

                        sSql = "SELECT " + (oDsmEntry.getDbmsTpSysMovId() == SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] ?
                                ((nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? "SUM(fe.debit - fe.credit) AS balance, " : "SUM(fe.credit - fe.debit) AS balance, ") +
                                (nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? "SUM(fe.debit_cur - fe.credit_cur) AS balance_cur " : "SUM(fe.credit_cur - fe.debit_cur) AS balance_cur ")) :
                                ((nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? "SUM(fe.credit - fe.debit) AS balance, " : "SUM(fe.debit - fe.credit) AS balance, ") +
                                (nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? "SUM(fe.credit_cur - fe.debit_cur) AS balance_cur " : "SUM(fe.debit_cur - fe.credit_cur) AS balance_cur "))) +
                                "FROM fin_rec AS f " +
                                "INNER JOIN fin_rec_ety AS fe ON fe.id_year = f.id_year AND fe.id_per = f.id_per AND fe.id_bkc = f.id_bkc AND fe.id_tp_rec = f.id_tp_rec AND fe.id_num = f.id_num " +
                                "WHERE f.b_del = FALSE AND fe.b_del = FALSE AND " +
                                "fe.fid_bp_nr = " + oDsmEntry.getFkBizPartnerId() + " AND fe.fid_tax_bas_n = " + nPkTaxBasicId + " AND " +
                                "fe.fid_tax_n = " + nPkTaxId + " AND fe.fid_dps_year_n = " + nDpsYearId +  " AND fe.fid_dps_doc_n = " + nDpsDocId +  " AND " +
                                "fe.fid_ct_sys_mov_xxx = " + anTypeSystemMove[0] + " AND fe.fid_tp_sys_mov_xxx = " + anTypeSystemMove[1] + " ";
                        resultSet1 = statementAux1.executeQuery(sSql);
                        if (resultSet1.next()) {
                            dValueTax = resultSet1.getDouble("balance");
                            dValueTaxCur = resultSet1.getDouble("balance_cur");

                            //System.out.println("1. dValueTax: " + dValueTax + " - dValueTaxCur: " + dValueTaxCur);

                            // Verify if 'dValueTax' is equal to 0 for calculate 'dValueTax' based in document taxes:

                            if (dValueTax == 0) {

                                // Read the balance net of tax:

                                oDpsTaxBalance = calculateDpsTaxBalanceNet(connection, nDpsYearId, nDpsDocId, new int[] { oDsmEntry.getDbmsCtSysMovId(), oDsmEntry.getDbmsTpSysMovId() }, oDsmEntry.getFkBizPartnerId(), nPkTaxType, nPkTaxBasicId, nPkTaxId);
                                //System.out.println("[NET]((double [])oDpsTaxBalance)[0]: " + ((double [])oDpsTaxBalance)[0]);
                                //System.out.println("((double [])oDpsTaxBalance)[1]: " + ((double [])oDpsTaxBalance)[1]);

                                switch (mnDbmsTaxModel) {
                                    case SDataConstantsSys.CFGS_TAX_MODEL_DPS:
                                        dValueTaxCur = SLibUtilities.round(((double [])oDpsTaxBalance)[1], mnDbmsErpDecimalsValue);
                                        dValueTax = SLibUtilities.round(((double [])oDpsTaxBalance)[0], mnDbmsErpDecimalsValue);
                                        //System.out.println("Entro A1");
                                        break;
                                    case SDataConstantsSys.CFGS_TAX_MODEL_DPS_EXC:
                                        //dValueTaxCur = (nTypeSource == SDataConstants.TRNX_DSM_ETY_SOURCE ? oDsmEntry.getSourceValueCy() : oDsmEntry.getDestinyValueCy() ) * dPercentageTax;
                                        dValueTaxCur = SLibUtilities.round(((double [])oDpsTaxBalance)[1], mnDbmsErpDecimalsValue);
                                        dValueTax = SLibUtilities.round(dValueTaxCur * (nTypeSource == SDataConstants.TRNX_DSM_ETY_SOURCE ? oDsmEntry.getSourceExchangeRate() : oDsmEntry.getDestinyExchangeRate()), mnDbmsErpDecimalsValue);
                                        //System.out.println("Entro A2");
                                        break;
                                    default:
                                        dValueTaxCur = 0;
                                        dValueTax = 0;
                                        //System.out.println("Entro A3");
                                        break;
                                }

                                //System.out.println("2. dValueTax: " + dValueTax + " - dValueTaxCur: " + dValueTaxCur);
                            }
                            else {
                                switch (mnDbmsTaxModel) {
                                    case SDataConstantsSys.CFGS_TAX_MODEL_DPS_EXC:
                                        dValueTax = SLibUtilities.round(dValueTaxCur * (nTypeSource == SDataConstants.TRNX_DSM_ETY_SOURCE ? oDsmEntry.getSourceExchangeRate() : oDsmEntry.getDestinyExchangeRate()), mnDbmsErpDecimalsValue);
                                        //System.out.println("Entro A2");
                                        break;
                                    case SDataConstantsSys.CFGS_TAX_MODEL_DPS:
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    else {
                        // Read the balance net of DPS:

                        oDpsBalance = calculateDpsBalanceNet(connection, nDpsYearId, nDpsDocId, new int[] {oDsmEntry.getDbmsCtSysMovId(), oDsmEntry.getDbmsTpSysMovId()},
                            oDsmEntry.getFkBizPartnerId(), (oDsmEntry.getDbmsFkDpsCategoryId() == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS));
                        //System.out.println("[DPS]((double [])oDpsBalance)[0]: " + ((double [])oDpsBalance)[0]);
                        //System.out.println("((double [])oDpsBalance)[1]: " + ((double [])oDpsBalance)[1]);

                        // Read the balance net of tax:

                        oDpsTaxBalance = calculateDpsTaxBalanceNet(connection, nDpsYearId, nDpsDocId, new int[] { oDsmEntry.getDbmsCtSysMovId(), oDsmEntry.getDbmsTpSysMovId() }, oDsmEntry.getFkBizPartnerId(), nPkTaxType, nPkTaxBasicId, nPkTaxId);
                        //System.out.println("((double [])oDpsTaxBalance)[0]: " + ((double [])oDpsTaxBalance)[0]);
                        //System.out.println("((double [])oDpsTaxBalance)[1]: " + ((double [])oDpsTaxBalance)[1]);

                        //System.out.println("oDsmEntry.getSourceValue(): " + oDsmEntry.getSourceValue());
                        //System.out.println("oDsmEntry.getSourceValueCy(): " + oDsmEntry.getSourceValueCy());
                        //System.out.println("oDsmEntry.getSourceExchangeRate(): " + oDsmEntry.getSourceExchangeRate());
                        //System.out.println("oDsmEntry.getDestinyValue(): " + oDsmEntry.getDestinyValue());
                        //System.out.println("oDsmEntry.getDestinyValueCy(): " + oDsmEntry.getDestinyValueCy());
                        //System.out.println("oDsmEntry.getDestinyExchangeRate(): " + oDsmEntry.getDestinyExchangeRate());

                        // Check if apply all balance when quantity cur is equal to balance_cur:

                        if ((((double [])oDpsBalance)[1]) == oDsmEntry.getSourceValueCy()) {
                            switch (mnDbmsTaxModel) {
                                case SDataConstantsSys.CFGS_TAX_MODEL_DPS:
                                    dValueTaxCur = SLibUtilities.round(((double [])oDpsTaxBalance)[1], mnDbmsErpDecimalsValue);
                                    dValueTax = SLibUtilities.round(((double [])oDpsTaxBalance)[0], mnDbmsErpDecimalsValue);
                                    //System.out.println("Entro g1");
                                    break;
                                case SDataConstantsSys.CFGS_TAX_MODEL_DPS_EXC:
                                    //dValueTaxCur = (nTypeSource == SDataConstants.TRNX_DSM_ETY_SOURCE ? oDsmEntry.getSourceValueCy() : oDsmEntry.getDestinyValueCy()) * dPercentageTax;
                                    dValueTaxCur = SLibUtilities.round(((double [])oDpsTaxBalance)[1], mnDbmsErpDecimalsValue);
                                    dValueTax = SLibUtilities.round(dValueTaxCur * (nTypeSource == SDataConstants.TRNX_DSM_ETY_SOURCE ? oDsmEntry.getSourceExchangeRate() : oDsmEntry.getDestinyExchangeRate()), mnDbmsErpDecimalsValue);
                                    //System.out.println("Entro g2");
                                    break;
                                default:
                                    dValueTaxCur = 0;
                                    dValueTax = 0;
                                    //System.out.println("Entro g3");
                                    break;
                            }
                        }
                        else {
                            if (((double [])oDpsBalance)[1] > 0) {

                                if (nTypeSource == SDataConstants.TRNX_DSM_ETY_SOURCE) {
                                    switch (mnDbmsTaxModel) {
                                        case SDataConstantsSys.CFGS_TAX_MODEL_DPS:
                                            dValueTaxCur = SLibUtilities.round(((((double []) oDpsTaxBalance)[1]/((double [])oDpsBalance)[1])*oDsmEntry.getSourceValueCy()), mnDbmsErpDecimalsValue);
                                            dValueTax = SLibUtilities.round(((((double []) oDpsTaxBalance)[0]/((double [])oDpsBalance)[0])*oDsmEntry.getSourceValue()), mnDbmsErpDecimalsValue);
                                            //System.out.println("Entro B1");
                                            break;
                                        case SDataConstantsSys.CFGS_TAX_MODEL_DPS_EXC:
                                            //dValueTaxCur = oDsmEntry.getSourceValueCy() * dPercentageTax;
                                            dValueTaxCur = SLibUtilities.round(((((double []) oDpsTaxBalance)[1]/((double [])oDpsBalance)[1])*oDsmEntry.getSourceValueCy()), mnDbmsErpDecimalsValue);
                                            dValueTax = SLibUtilities.round(dValueTaxCur * oDsmEntry.getSourceExchangeRate(), mnDbmsErpDecimalsValue);
                                            //System.out.println("Entro B2");
                                            break;
                                        default:
                                            dValueTaxCur = 0;
                                            dValueTax = 0;
                                            //System.out.println("Entro B3");
                                            break;
                                    }
                                }
                                else {
                                    switch (mnDbmsTaxModel) {
                                        case SDataConstantsSys.CFGS_TAX_MODEL_DPS:
                                            dValueTaxCur = SLibUtilities.round(((((double []) oDpsTaxBalance)[1]/((double [])oDpsBalance)[1])*oDsmEntry.getDestinyValueCy()), mnDbmsErpDecimalsValue);
                                            dValueTax = SLibUtilities.round(((((double []) oDpsTaxBalance)[0]/((double [])oDpsBalance)[0])*oDsmEntry.getDestinyValue()), mnDbmsErpDecimalsValue);
                                            //System.out.println("Entro C1");
                                            break;
                                        case SDataConstantsSys.CFGS_TAX_MODEL_DPS_EXC:
                                            // dValueTaxCur = oDsmEntry.getDestinyValueCy() * dPercentageTax;
                                            dValueTaxCur = SLibUtilities.round(((((double []) oDpsTaxBalance)[1]/((double [])oDpsBalance)[1])*oDsmEntry.getDestinyValueCy()), mnDbmsErpDecimalsValue);
                                            dValueTax = SLibUtilities.round(dValueTaxCur * oDsmEntry.getDestinyExchangeRate(), mnDbmsErpDecimalsValue);
                                            //System.out.println("Entro C2");
                                            break;
                                        default:
                                            dValueTaxCur = 0;
                                            dValueTax = 0;
                                            //System.out.println("Entro C3");
                                            break;
                                    }
                                }

                                // Check that 'dValueTax' and 'dValueTaxCur' is less or equal that 'oDpsTaxBalance[]':

                                switch (mnDbmsTaxModel) {
                                    case SDataConstantsSys.CFGS_TAX_MODEL_DPS:
                                        if (dValueTax > ((double [])oDpsTaxBalance)[0]) {
                                            dValueTax = SLibUtilities.round(((double [])oDpsTaxBalance)[0], mnDbmsErpDecimalsValue);
                                            //System.out.println("Entro E1");
                                        }

                                        if (dValueTaxCur > ((double [])oDpsTaxBalance)[1]) {
                                            dValueTaxCur = SLibUtilities.round(((double [])oDpsTaxBalance)[1], mnDbmsErpDecimalsValue);
                                            //System.out.println("Entro E2");
                                        }
                                        break;
                                    case SDataConstantsSys.CFGS_TAX_MODEL_DPS_EXC:
                                    default:
                                        break;
                                }
                            }
                            else {
                                dValueTax = 0;
                                dValueTaxCur = 0;
                            }
                        }
                    }
                    //System.out.println("dValueTax: " + dValueTax);
                    //System.out.println("dValueTaxCur: " + dValueTaxCur);
                    //System.out.println("mnDbmsErpDecimalsValue: " + mnDbmsErpDecimalsValue);

                    // Save record entry taxes:

                    nSortPos = saveRecordEntriesTaxes(connection, oRecord, oRecordEntry, oDsmEntry, nPkNumberId, sConcept, nDpsYearId, nDpsDocId, nDpsCategory, nSortPos, vTpSysMov, nPkTaxBasicId, nPkTaxId, dValueTax, dValueTaxCur, nTypeSource);
                }
            }

            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return nSortPos;
    }

    private java.lang.Object calculateDpsBalanceNet(java.sql.Connection connection, int nDpsYearId, int nDpsDocId, int[] nTpSysMovId, int nBizPartnerId, int nCtBpId) {
        double dDpsBalanceNet = 0.0;
        double dDpsBalanceCurNet = 0.0;
        String sSql = "";

        Statement statementAux = null;
        ResultSet resultSet = null;

        try {
            statementAux = connection.createStatement();

            sSql = "SELECT " +
                "d.tot_cur_r - COALESCE((" +
                    "SELECT SUM(dda.val_cur) FROM trn_dps_dps_adj AS dda " +
                    "WHERE dda.id_dps_year = d.id_year AND dda.id_dps_doc = d.id_doc), 0) AS f_tot_net_cur, " +
                "d.tot_r - COALESCE((" +
                    "SELECT SUM(dda.val) FROM trn_dps_dps_adj AS dda " +
                    "WHERE dda.id_dps_year = d.id_year AND dda.id_dps_doc = d.id_doc), 0) AS f_tot_net, d.fid_cur " +
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND " +
                "r.id_num = re.id_num AND r.b_del = FALSE AND re.b_del = FALSE AND re.fid_ct_sys_mov_xxx = " + nTpSysMovId[0] + " AND re.fid_tp_sys_mov_xxx =  " + nTpSysMovId[1] + " AND " +
                "re.fid_dps_year_n = " + nDpsYearId + " AND re.fid_dps_doc_n = " + nDpsDocId + " AND re.fid_bp_nr = " + nBizPartnerId + " " +
                "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = " + nCtBpId + " " +
                "LEFT OUTER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc " +
                "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN erp.bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
                "GROUP BY d.fid_cur ";

            //System.out.println("DpsBalanceNet: " + sSql);

            resultSet = statementAux.executeQuery(sSql);
            if (resultSet.next()) {
                dDpsBalanceNet = resultSet.getDouble("f_tot_net");
                dDpsBalanceCurNet = resultSet.getDouble("f_tot_net_cur");
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return new double[] { dDpsBalanceNet, dDpsBalanceCurNet };
    }

    private java.lang.Object calculateDpsTaxBalanceNet(java.sql.Connection connection, int nDpsYearId, int nDpsDocId, int nTpSysMovId[], int nBizPartnerId, int nPkTaxType, int nPkTaxBasicId, int nPkTaxId) {
        double dTaxBalance = 0.0;
        double dTaxBalanceCur = 0.0;
        String sSql = "";
        String sSqlTax = "";
        String sSumTax = "SELECT " + (nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? "SUM(et1.tax_charged_r) " : "SUM(et1.tax_retained_r) ") + " AS balance_adj ";
        String sSumTaxCur = "SELECT " + (nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? "SUM(et1.tax_charged_cur_r) " : "SUM(et1.tax_retained_cur_r) ") + " AS balance_cur_adj ";

        try {
            java.sql.Statement statementAux = null;
            ResultSet resultSet = null;

            sSqlTax = "FROM trn_dps AS d " +
                "INNER JOIN trn_dps_ety AS et ON et.b_del = FALSE AND d.id_year = et.id_year AND d.id_doc = et.id_doc AND d.id_year = " + nDpsYearId + " AND d.id_doc = " + nDpsDocId + " AND " +
                "d.b_del = FALSE AND d.fid_bp_r = " + nBizPartnerId + " " +
                "INNER JOIN trn_dps_dps_adj AS ad ON et.id_year = ad.id_dps_year AND et.id_doc = ad.id_dps_doc AND et.id_ety = ad.id_dps_ety " +
                "INNER JOIN trn_dps_ety AS et1 ON et1.b_del = FALSE AND ad.id_adj_year = et1.id_year AND ad.id_adj_doc = et1.id_doc AND ad.id_adj_ety = et1.id_ety " +
                "INNER JOIN trn_dps AS d1 ON d1.b_del = FALSE AND et1.id_year = d1.id_year AND et1.id_doc = d1.id_doc AND d1.fid_ct_dps = " + renderTypeDps(nTpSysMovId, 0) + " AND " +
                "d1.fid_cl_dps = " + renderTypeDps(nTpSysMovId, 1) + " AND d1.fid_tp_dps =  " + renderTypeDps(nTpSysMovId, 2) + " " +
                "INNER JOIN trn_dps_ety_tax AS tax ON et.id_year = tax.id_year AND et.id_doc = tax.id_doc AND et.id_ety = tax.id_ety AND et.b_del = FALSE AND " +
                "tax.id_tax_bas = " + nPkTaxBasicId + " AND tax.id_tax = " + nPkTaxId + " ";

            statementAux = connection.createStatement();
            sSql = "SELECT SUM(tax.tax) - (COALESCE((" + sSumTax + sSqlTax + "), 0)) AS balance, " +
                "SUM(tax.tax_cur) - (COALESCE((" + sSumTaxCur + sSqlTax + "), 0)) AS balance_cur " +
                "FROM trn_dps AS dps " +
                "INNER JOIN trn_dps_ety AS et ON " +
                "dps.id_year = et.id_year AND dps.id_doc = et.id_doc " +
                "INNER JOIN trn_dps_ety_tax AS tax ON et.id_year = tax.id_year AND et.id_doc = tax.id_doc AND et.id_ety = tax.id_ety AND et.b_del = FALSE " +
                "WHERE dps.b_del = FALSE AND " +
                "dps.id_year = " + nDpsYearId + " AND dps.id_doc = " + nDpsDocId + " AND " +
                "dps.fid_bp_r = " + nBizPartnerId + " AND " +
                "tax.id_tax_bas = " + nPkTaxBasicId + " AND tax.id_tax = " + nPkTaxId + " ";


                        /*
                    sSql = "SELECT " + (oDsmEntry.getDbmsTpSysMovId() == SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] ?
                        (nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? ((double [])oDpsTaxBalance)[0] + " - SUM(debit) " : ((double [])oDpsTaxBalance)[0] + " - SUM(credit) ") + "AS balance, " +
                        (nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? ((double [])oDpsTaxBalance)[1] + " - SUM(debit_cur) " : ((double [])oDpsTaxBalance)[1] + " - SUM(credit_cur) ") + "AS balance_cur " :
                        (nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? ((double [])oDpsTaxBalance)[0] + " - SUM(credit) " : ((double [])oDpsTaxBalance)[0] + " - SUM(debit) ") + "AS balance, " +
                        (nPkTaxType == SModSysConsts.FINS_TP_TAX_CHARGED ? ((double [])oDpsTaxBalance)[1] + " - SUM(credit_cur) " : ((double [])oDpsTaxBalance)[1] + " - SUM(debit_cur) ") + "AS balance_cur ") +
                        "FROM fin_rec_ety " +
                        "WHERE b_del = FALSE AND " +
                        "fid_bp_nr = " + nBizPartnerId + " AND fid_tax_bas_n = " + nPkTaxBasicId + " AND " +
                        "fid_tax_n = " + nPkTaxId + " AND fid_dps_year_n = " + nDpsYearId +  " AND fid_dps_doc_n = " + nDpsDocId +  " AND " +
                        "fid_acc = '" + sTaxAccountPayPendId + "'";
                     */

            //System.out.println("sSql: " + sSql);

            resultSet = statementAux.executeQuery(sSql);

            if (resultSet.next()) {
                dTaxBalance = resultSet.getDouble("balance");
                dTaxBalanceCur = resultSet.getDouble("balance_cur");
            }

            //System.out.println("nDpsYearId: " + nDpsYearId + " - nDpsDocId: " + nDpsDocId + "  - nBizPartnerId: " + nBizPartnerId + " nTypeAccSys: " + nTpSysMovId[0] + "-" + nTpSysMovId[1] +
            //      " - nPkTaxBasicId: " + nPkTaxBasicId + " - nPkTaxId: " + nPkTaxId + " - dTaxBalance: " + dTaxBalance + " - dTaxBalanceCur: " + dTaxBalanceCur);

        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return new double[] { dTaxBalance, dTaxBalanceCur };
    }

    private java.lang.Object calculateCurrentBalanceDps(java.sql.Connection connection, int nDpsYearId, int nDpsDocId, int[] nTpSysMovId) {
        double dBalance = 0;
        double dBalanceCur = 0;
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL trn_dps_bal_get(?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, nDpsYearId);
            callableStatement.setInt(nParam++, nDpsDocId);
            callableStatement.setInt(nParam++, nTpSysMovId[0]);
            callableStatement.setInt(nParam++, nTpSysMovId[1]);
            callableStatement.registerOutParameter(nParam, java.sql.Types.DOUBLE); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.DOUBLE); nParam++;
            callableStatement.registerOutParameter(nParam, java.sql.Types.INTEGER); nParam++;
            callableStatement.execute();

            dBalance = callableStatement.getDouble(nParam - 3);
            dBalanceCur = callableStatement.getDouble(nParam - 2);

            mnLastDbActionResult = SLibConstants.DB_PROCEDURE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_PROCEDURE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return new double[] { dBalance, dBalanceCur };
    }

    private int renderTypeDps(int[] nTpSysMovId, int pos) {
        int type = 0;

        if (SLibUtilities.compareKeys(nTpSysMovId, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP) ||
                SLibUtilities.compareKeys(nTpSysMovId, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CDR)) {
            type = SDataConstantsSys.TRNU_TP_DPS_PUR_CN[pos];
        }
        else if (SLibUtilities.compareKeys(nTpSysMovId, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS) ||
                SLibUtilities.compareKeys(nTpSysMovId, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_DBR)) {
            type = SDataConstantsSys.TRNU_TP_DPS_SAL_CN[pos];
        }

        /*
        switch (pos){
            case 0:
                switch (typeAccSys) {
                    case SDataConstantsSys.FINS_TP_ACC_SYS_SUP:
                    case SDataConstantsSys.FINS_TP_ACC_SYS_CDR:
                        type = SDataConstantsSys.TRNU_TP_DPS_PUR_CN[0];
                        break;
                    case SDataConstantsSys.FINS_TP_ACC_SYS_CUS:
                    case SDataConstantsSys.FINS_TP_ACC_SYS_DBR:
                        type = SDataConstantsSys.TRNU_TP_DPS_SAL_CN[0];
                        break;
                }
                break;
            case 1:
                switch (nTpSysMovId) {
                    case SDataConstantsSys.FINS_TP_ACC_SYS_SUP_TRN:
                switch (typeAccSys) {
                    case SDataConstantsSys.FINS_TP_ACC_SYS_SUP:
                    case SDataConstantsSys.FINS_TP_ACC_SYS_CDR:
                        type = SDataConstantsSys.TRNU_TP_DPS_PUR_CN[1];
                        break;
                    case SDataConstantsSys.FINS_TP_ACC_SYS_CUS:
                    case SDataConstantsSys.FINS_TP_ACC_SYS_DBR:
                        type = SDataConstantsSys.TRNU_TP_DPS_SAL_CN[1];
                        break;
                }
                break;
            case 2:
                switch (nTpSysMovId) {
                    case SDataConstantsSys.FINS_TP_ACC_SYS_SUP_TRN:
                switch (typeAccSys) {
                    case SDataConstantsSys.FINS_TP_ACC_SYS_SUP:
                    case SDataConstantsSys.FINS_TP_ACC_SYS_CDR:
                        type = SDataConstantsSys.TRNU_TP_DPS_PUR_CN[2];
                        break;
                    case SDataConstantsSys.FINS_TP_ACC_SYS_CUS:
                    case SDataConstantsSys.FINS_TP_ACC_SYS_DBR:
                        type = SDataConstantsSys.TRNU_TP_DPS_SAL_CN[2];
                        break;
                }
                break;
         }
         */

        return type;
    }

    private boolean deleteRecord(java.lang.Object recordKey, boolean deleteEverything, java.sql.Connection connection) throws java.lang.Exception {
        SDataRecord record = new SDataRecord();
        Statement statement = connection.createStatement();

        if (record.read(recordKey, statement) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ);
        }
        else {
            if (deleteEverything) {
                record.setIsRegistryEdited(true);
                record.setFkUserEditId(mnFkUserEditId);
                record.setIsDeleted(true);
                record.setFkUserDeleteId(mnFkUserEditId);
                for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
                    if (!entry.getIsDeleted()) {
                        entry.setIsRegistryEdited(true);
                        entry.setFkUserEditId(mnFkUserEditId);
                        entry.setIsDeleted(true);
                        entry.setFkUserDeleteId(mnFkUserEditId);
                    }
                }
            }
            else {
                for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
                    if (!entry.getIsDeleted()) {

                        //System.out.println("entry.getFkDsmYearId_n(): " + entry.getFkDsmYearId_n() + " - " +
                        //        " entry.getFkDsmDocId_n(): " + entry.getFkDsmDocId_n());
                        //System.out.println("mnPkYearId: " + mnPkYearId + " - " +
                        //        " mnPkDocId: " + mnPkDocId);
                        /* XXX obsolete code (sflores, 2013-06-27)
                        if (entry.getFkDsmYearId_n() == mnPkYearId && entry.getFkDsmDocId_n() == mnPkDocId) {
                            entry.setIsRegistryEdited(true);
                            entry.setFkUserEditId(mnFkUserEditId);
                            entry.setIsDeleted(true);
                            entry.setFkUserDeleteId(mnFkUserEditId);
                        }
                        */
                    }
                }
            }

            if (record.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE);
            }
        }

        return true;
    }

    /*
     * Public functions
     */

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setConcept(java.lang.String s) { msConcept = s; }
    public void setIsAudited(boolean b) { mbIsAudited = b; }
    public void setIsAuthorized(boolean b) { mbIsAuthorized = b; }
    public void setIsRecordAutomatic(boolean b) { mbIsRecordAutomatic = b; }
    public void setIsSystem(boolean b) { mbIsSystem = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkSubsystemCategoryId(int n) { mnFkSubsystemCategoryId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkUserAuditedId(int n) { mnFkUserAuditedId = n; }
    public void setFkUserAuthorizedId(int n) { mnFkUserAuthorizedId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserAuditedTs(java.util.Date t) { mtUserAuditedTs = t; }
    public void setUserAuthorizedTs(java.util.Date t) { mtUserAuthorizedTs = t; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public java.util.Date getDate() { return mtDate; }
    public java.lang.String getConcept() { return msConcept; }
    public boolean getIsAudited() { return mbIsAudited; }
    public boolean getIsAuthorized() { return mbIsAuthorized; }
    public boolean getIsRecordAutomatic() { return mbIsRecordAutomatic; }
    public boolean getIsSystem() { return mbIsSystem; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkSubsystemCategoryId() { return mnFkSubsystemCategoryId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkUserAuditedId() { return mnFkUserAuditedId; }
    public int getFkUserAuthorizedId() { return mnFkUserAuthorizedId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserAuditedTs() { return mtUserAuditedTs; }
    public java.util.Date getUserAuthorizedTs() { return mtUserAuthorizedTs; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsIsRecordSaved(boolean b) { mbDbmsIsRecordSaved = b; }
    public void setDbmsPkPeriodId(int n) { mnDbmsPkPeriodId = n; }
    public void setDbmsPkBookkeepingCenterId(int n) { mnDbmsPkBookkeepingCenterId = n; }
    public void setDbmsFkCompanyBranch(int n) { mnDbmsFkCompanyBranch = n;  }
    public void setDbmsErpDecimalsValue(int n) { mnDbmsErpDecimalsValue = n; }
    public void setDbmsErpTaxModel(int n) { mnDbmsTaxModel = n; }
    public void setDbmsCompanyBranchCode(java.lang.String s) { msDbmsCompanyBranchCode = s; }
    public void setDbmsPkRecordTypeId(java.lang.String s) { msDbmsPkRecordTypeId = s; }
    public void setDbmsSubsystemTypeBiz(java.lang.String s) { msDbmsSubsystemTypeBiz = s; }

    public void setAuxIsRecordAutomatic(boolean b) { mbAuxIsRecordAutomatic = b; }
    public void setDbmsRecordDate(java.util.Date t) { mtDbmsRecordDate = t; }
    public void setDbmsRecordKey(java.lang.Object o) { moDbmsRecordKey = o; }
    public void setAuxRecordUserKey(java.lang.Object o) { moAuxRecordUserKey = o; }

    public boolean getAuxIsRecordAutomatic() { return mbAuxIsRecordAutomatic; }
    public java.util.Date getDbmsRecordDate() { return mtDbmsRecordDate; }
    public java.lang.Object getAuxRecordKeyUser() { return moAuxRecordUserKey; }

    public java.lang.Object getDbmsRecordKey() { return moDbmsRecordKey; }
    public java.util.Vector<SDataDsmNotes> getDbmsNotes() { return mvDbmsDsmNotes; }
    public java.util.Vector<SDataDsmEntry> getDbmsEntry() { return mvDbmsDsmEntry; }

    public erp.mfin.data.SDataRecord getDbmsRecord() { return moDbmsRecord; }

    public void setParamPkCheckWalletId(int n) { mnParamPkCheckWalletId = n; }
    public void setParamPkCheckId(int n) { mnParamPkCheckId = n; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mtDate = null;
        msConcept = "";
        mbIsAudited = false;
        mbIsAuthorized = false;
        mbIsRecordAutomatic = false;
        mbIsSystem = false;
        mbIsDeleted = false;
        mnFkSubsystemCategoryId = 0;
        mnFkCompanyBranchId = 0;
        mnFkUserAuditedId = 0;
        mnFkUserAuthorizedId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserAuditedTs = null;
        mtUserAuthorizedTs = null;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mbDbmsIsRecordSaved = true;
        mnDbmsPkPeriodId = 0;
        mnDbmsPkBookkeepingCenterId = 0;
        mnDbmsFkCompanyBranch = 0;
        mnDbmsErpDecimalsValue = 0;
        mnDbmsTaxModel = 0;
        msDbmsCompanyBranchCode = "";
        msDbmsPkRecordTypeId = "";
        msDbmsSubsystemTypeBiz = "";

        mvDbmsDsmNotes.clear();
        mvDbmsDsmEntry.clear();

        mbAuxIsRecordAutomatic = false;
        moAuxRecordUserKey = null;
        moDbmsRecordKey = null;
        mtDbmsRecordDate = null;

        moDbmsRecord = null;
        mnParamPkCheckWalletId = -1;
        mnParamPkCheckId = -1;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;
        java.sql.Statement statementAux = null;
        SDataDsmNotes oDsmNotes = null;
        SDataDsmEntry oDsmEntry = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT ds.* " +
                    "FROM trn_dsm AS ds " +
                    "WHERE ds.id_year = " + key[0] +
                        " AND ds.id_doc = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                mtDate = resultSet.getDate("dt");
                msConcept = resultSet.getString("concept");
                mbIsAudited = resultSet.getBoolean("b_audit");
                mbIsAuthorized = resultSet.getBoolean("b_authorn");
                mbIsRecordAutomatic = resultSet.getBoolean("b_rec_aut");
                mbIsSystem = resultSet.getBoolean("b_sys");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkSubsystemCategoryId = resultSet.getInt("fid_ct_dsm");
                mnFkCompanyBranchId = resultSet.getInt("fid_cob");
                mnFkUserAuditedId = resultSet.getInt("fid_usr_audit");
                mnFkUserAuthorizedId = resultSet.getInt("fid_usr_authorn");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserAuditedTs = resultSet.getTimestamp("ts_audit");
                mtUserAuthorizedTs = resultSet.getTimestamp("ts_authorn");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                // Read record:

                statementAux = statement.getConnection().createStatement();
                sql = "SELECT DISTINCT et.fid_dsm_year_n, et.fid_dsm_doc_n, et.id_year, et.id_per, et.id_bkc, et.id_tp_rec, et.id_num " +
                        "FROM fin_rec_ety AS et " +
                        "WHERE et.fid_dsm_year_n = " + key[0] + " AND " +
                            "et.fid_dsm_doc_n = " + key[1] + " AND " +
                            "et.fid_dps_year_n IS NULL AND " +
                            "et.fid_dps_doc_n IS NULL AND " +
                            "et.fid_dps_adj_year_n IS NULL AND " +
                            "et.fid_dps_adj_doc_n IS NULL AND " +
                            "et.fid_diog_year_n IS NULL AND " +
                            "et.fid_diog_doc_n IS NULL AND " +
                            "et.fid_item_n IS NULL AND " +
                            "et.b_del = FALSE";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    moDbmsRecordKey = new Object[] { resultSet.getInt("id_year"), resultSet.getInt("id_per"), resultSet.getInt("id_bkc"), resultSet.getString("id_tp_rec"), resultSet.getInt("id_num") } ;

                    sql = "SELECT dt FROM fin_rec WHERE id_year = " + ((Object[]) moDbmsRecordKey)[0] + " AND id_per = " + ((Object[]) moDbmsRecordKey)[1] + " AND " +
                            "id_bkc = " + ((Object[]) moDbmsRecordKey)[2] + " AND id_tp_rec = '" + ((Object[]) moDbmsRecordKey)[3] + "' AND id_num = " + ((Object[]) moDbmsRecordKey)[4] + " ";
                    resultSet = statement.executeQuery(sql);
                    if (!resultSet.next()) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mtDbmsRecordDate = resultSet.getDate("dt");
                        mbAuxIsRecordAutomatic = mbIsRecordAutomatic;
                        moAuxRecordUserKey = new Object[5];
                        ((Object[]) moAuxRecordUserKey)[0] = ((Object[]) moDbmsRecordKey)[0];
                        ((Object[]) moAuxRecordUserKey)[1] = ((Object[]) moDbmsRecordKey)[1];
                        ((Object[]) moAuxRecordUserKey)[2] = ((Object[]) moDbmsRecordKey)[2];
                        ((Object[]) moAuxRecordUserKey)[3] = ((Object[]) moDbmsRecordKey)[3];
                        ((Object[]) moAuxRecordUserKey)[4] = ((Object[]) moDbmsRecordKey)[4];
                    }
                }

                // Read notes:

                statementAux = statement.getConnection().createStatement();
                mvDbmsDsmNotes.removeAllElements();
                sql = "SELECT * FROM trn_dsm_nts " +
                    "WHERE id_year = " + key[0] +
                    " AND id_doc = " + key[1] +
                    " ORDER BY nts ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    oDsmNotes = new SDataDsmNotes();
                    if (oDsmNotes.read(new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_doc"), resultSet.getInt("id_nts") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsDsmNotes.add(oDsmNotes);
                    }
                }

                // Read dsm entries:

                statementAux = statement.getConnection().createStatement();
                mvDbmsDsmEntry.removeAllElements();
                sql = "SELECT * FROM trn_dsm_ety " +
                    "WHERE id_year = " + key[0] +
                    " AND id_doc = " + key[1] +
                    " ORDER BY id_doc ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    oDsmEntry = new SDataDsmEntry();
                    if (oDsmEntry.read(new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_doc"), resultSet.getInt("id_ety") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsDsmEntry.add(oDsmEntry);
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        boolean bIsNewRecord = false;
        int nParam = 1;
        int i = 0;
        String sql = "";

        SDataDsmNotes dataDsmNotes = null;
        SDataDsmEntry dataDsmEntry = null;
        java.sql.Statement statementAux = null;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        try {
            callableStatement = connection.prepareCall(
                    "{ CALL trn_dsm_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkDocId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDate.getTime()));
            callableStatement.setString(nParam++, msConcept.length() > 100 ? msConcept.substring(0, 96).trim() + "..." : msConcept);
            callableStatement.setBoolean(nParam++, mbIsAudited);
            callableStatement.setBoolean(nParam++, mbIsAuthorized);
            callableStatement.setBoolean(nParam++, mbIsRecordAutomatic);
            callableStatement.setBoolean(nParam++, mbIsSystem);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkSubsystemCategoryId);
            callableStatement.setInt(nParam++, mnFkCompanyBranchId);
            callableStatement.setInt(nParam++, mnFkUserAuditedId);
            callableStatement.setInt(nParam++, mnFkUserAuthorizedId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkDocId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            }
            else {
                // Save notes:

                statementAux = connection.createStatement();
                sql = "DELETE FROM trn_dsm_nts WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
                statementAux.execute(sql);
                statementAux.close();

                for (i = 0; i < mvDbmsDsmNotes.size(); i++) {
                    dataDsmNotes = (SDataDsmNotes) mvDbmsDsmNotes.get(i);
                    if (dataDsmNotes != null) {
                        dataDsmNotes.setPkYearId(mnPkYearId);
                        dataDsmNotes.setPkDocId(mnPkDocId);
                        dataDsmNotes.setPkNotesId(0);
                        dataDsmNotes.setIsDeleted(mbIsDeleted == true ? mbIsDeleted : dataDsmNotes.getIsDeleted());
                        if (dataDsmNotes.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // Save DSM Entry:

                for (int m = 0; m < mvDbmsDsmEntry.size(); m++) {
                    dataDsmEntry = (SDataDsmEntry) mvDbmsDsmEntry.get(m);

                    if (dataDsmEntry != null) {
                        dataDsmEntry.setPkYearId(mnPkYearId);
                        dataDsmEntry.setPkDocId(mnPkDocId);
                        dataDsmEntry.setIsDeleted(mbIsDeleted == true ? mbIsDeleted : dataDsmEntry.getIsDeleted());
                        if (dataDsmEntry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
                        }
                    }
                }

                // Save record if there is DSM Entry:

                if (mvDbmsDsmEntry.size() > 0) {

                    // 4. Save aswell accounting record if document class requires one:

                    // 4.1 Prepare accounting record:2

                    if (mbIsDeleted) {

                        // Delete aswell former accounting record:

                        //System.out.println("Save 1");
                        if (moAuxRecordUserKey != null) {
                            // If document had automatic accounting record, delete it including its header:
                            //System.out.println("Save 2");
                            deleteRecord(moAuxRecordUserKey, mbAuxIsRecordAutomatic, connection);
                        }
                    }
                    else {
                        // Save aswell accounting record:

                        //System.out.println("Save 3");
                        if (moDbmsRecordKey == null) {
                            if (!mbIsRecordAutomatic) {
                                // Accounting record is not automatic:
                                throw new Exception("No se ha especificado la póliza contable de usuario.");
                            }
                            else {
                                // Accounting record is automatic:

                                //System.out.println("Save 4");
                                if (moAuxRecordUserKey == null || !mbAuxIsRecordAutomatic) {
                                    //System.out.println("Save 5");
                                    bIsNewRecord = true;
                                    // moDbmsRecordKey = (Object[]) createAccountingRecordKey(statementAux);
                                }
                                else {
                                    //System.out.println("Save 6");
                                    if (SLibTimeUtilities.isBelongingToPeriod(mtDate, (Integer) ((Object[]) moAuxRecordUserKey)[0], (Integer) ((Object[]) moAuxRecordUserKey)[1])) {
                                        moDbmsRecordKey = moAuxRecordUserKey;
                                        //System.out.println("Save 7");
                                    }
                                    else {
                                        bIsNewRecord = true;
                                        //System.out.println("Save 8");
                                        // moDbmsRecordKey = (Object[]) createAccountingRecordKey(statementAux);
                                    }
                                }
                            }
                        }

                        if (moAuxRecordUserKey != null) {
                            // If document was previously saved, delete former accounting moves, if needed, including its header:
                            //System.out.println("Save 9");
                            deleteRecord(moAuxRecordUserKey, mbAuxIsRecordAutomatic && !mbIsRecordAutomatic, connection);
                        }

                        // 4.2 Save document's accounting record:

                        // Save record:

                        saveRecord(connection, bIsNewRecord);
                    }
                }
                else {
                    mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int canDelete(java.sql.Connection connection) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int delete(java.sql.Connection connection) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int process(java.sql.Connection connection) {

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            moDbmsRecord = saveRecord(connection, true);
            mnLastDbActionResult = SLibConstants.DB_ACTION_PROCESS_OK;
        }
        catch (Exception exception) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_PROCESS_ERROR;
            SLibUtilities.printOutException(this, exception);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return mtUserEditTs;
    }

    public erp.mfin.data.SDataRecord publicSaveRecord(java.sql.Connection conn) {
        return saveRecord(conn, true);
    }
}
