/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SFinAccountUtilities;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SFinUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SStockValuationRecordUtils {
    
    /**
     * The function "makeDiogsFromConsumptions" creates record entries for expenses and warehouse
     * transactions based on a list of stock valuation movements.
     * 
     * @param session The session parameter is an instance of the SGuiSession class, which represents
     * the user session in the application.
     * @param recordPk - recordPk: an array of integers representing the primary key of the record to
     * be created. The array should contain the year ID, period ID, bookkeeping center ID, record type
     * ID, and number ID.
     * @param dtStart The dtStart parameter is of type Date and represents the start date for the stock
     * valuation.
     * @param lConsumptions An ArrayList of SDbStockValuationMvt objects, which represents the list of
     * stock valuation movements to be processed.
     * @return The method is returning a boolean value, which is set to true.
     * @throws java.lang.Exception
     */
    public static boolean makeRecordEntriesFromConsumptions(SGuiSession session, final Object[] recordPk, final Date dtStart, ArrayList<SDbStockValuationMvt> lConsumptions) throws Exception {
        SDataRecordEntry oDataRecordEntry;
        SDataRecordEntry oDataRecordEntryWhs;
        SDbStockValuationAccount oRecEtyAcc;
        SStockValuationConfiguration oCfg = SStockValuationUtils.getStockValuationConfig(session.getStatement().getConnection().createStatement());
        HashMap<Integer, java.util.Vector<erp.mfin.data.SFinAccountConfigEntry>> purCfgs = new HashMap<>();
        HashMap<Integer, java.util.Vector<erp.mfin.data.SFinAccountConfigEntry>> assetCfgs = new HashMap<>();
        java.util.Vector<erp.mfin.data.SFinAccountConfigEntry> vConfigs;
        java.util.Vector<erp.mfin.data.SFinAccountConfigEntry> vConfigsWhs;
        HashMap<Integer, SDataItem> lItems = new HashMap<>();
        SDataItem oItem;
        HashMap<Integer, SDataUnit> lUnits = new HashMap<>();
        SDataUnit oUnit;
        HashMap<String, SDataAccount> lAccounts = new HashMap<>();
        SDataAccount oAccount;
        HashMap<String, Integer> lCC = new HashMap<>();
        int nIdCC;
//        HashMap<String, ArrayList<SDataRecordEntry>> assetEntries = new HashMap<>();

        int n = lConsumptions.size();
        int i = 1;
        if ((n * 2) > oCfg.getMaxRecEntries()) {
            throw new Exception("No se puede realizar esta acción, excedería el número máximo de partidas para la póliza, pruebe con un periodo de tiempo más corto");
        }
        SDataRecord oRecord = new SDataRecord();
        oRecord.read((Object) recordPk, session.getStatement());
        int sortPosition = oRecord.getLastSortingPosition();
        for (SDbStockValuationMvt oConsumption : lConsumptions) {
            System.out.println("Contabilizando " + i + " de " + n);
            /**
             * Obtener el ítem
             */
            if (lItems.containsKey(oConsumption.getFkItemId())) {
                oItem = lItems.get(oConsumption.getFkItemId());
            }
            else {
                oItem = new SDataItem();
                oItem.read(new int[] { oConsumption.getFkItemId() }, session.getStatement());
                lItems.put(oConsumption.getFkItemId(), oItem);
            }
            /**
             * Obtener la unidad
             */
            if (oItem.getFkUnitId() != oConsumption.getFkUnitId()) {
                if (lUnits.containsKey(oConsumption.getFkUnitId())) {
                    oUnit = lUnits.get(oConsumption.getFkUnitId());
                }
                else {
                    oUnit = new SDataUnit();
                    oUnit.read(new int[] { oConsumption.getFkUnitId() }, session.getStatement().getConnection().createStatement());
                    lUnits.put(oConsumption.getFkUnitId(), oUnit);
                }
            }
            else {
                oUnit = oItem.getDbmsDataUnit();
            }
            
            /**
             * Partida de gastos
             */
            oDataRecordEntry = new SDataRecordEntry();

            oDataRecordEntry.setPkYearId((int) recordPk[0]);
            oDataRecordEntry.setPkPeriodId((int) recordPk[1]);
            oDataRecordEntry.setPkBookkeepingCenterId((int) recordPk[2]);
            oDataRecordEntry.setPkRecordTypeId((String) recordPk[3]);
            oDataRecordEntry.setPkNumberId((int) recordPk[4]);
            oDataRecordEntry.setConcept(SStockValuationRecordUtils.getConcept(oItem.getName(), oConsumption.getQuantityConsumption(), oUnit.getSymbol(), oCfg.getTextPurEntries()));
            oDataRecordEntry.setReference("");
            oDataRecordEntry.setIsReferenceTax(false);
            oDataRecordEntry.setDebit(oConsumption.getCost_r());
            oDataRecordEntry.setCredit(0d);
            oDataRecordEntry.setExchangeRate(1d);
            oDataRecordEntry.setExchangeRateSystem(1d);
            oDataRecordEntry.setDebitCy(oConsumption.getCost_r());
            oDataRecordEntry.setCreditCy(0d);
            oDataRecordEntry.setUnits(0d);
            oDataRecordEntry.setUserId(0);
            oDataRecordEntry.setSortingPosition(0);
            oDataRecordEntry.setOccasionalFiscalId("");
            oDataRecordEntry.setIsExchangeDifference(false);
            oDataRecordEntry.setIsSystem(true);
            oDataRecordEntry.setIsDeleted(false);
        
            if (purCfgs.containsKey(oConsumption.getFkItemReference_n())) {
                vConfigs = purCfgs.get(oConsumption.getFkItemReference_n());
            }
            else {
                vConfigs = SFinAccountUtilities.obtainItemAccountConfigs(oConsumption.getFkItemReference_n(), 
                                                                                (int) recordPk[2], 
                                                                                dtStart, 
                                                                                oCfg.getFinTpAccItemPur(),
                                                                                true, 
                                                                                session.getStatement().getConnection().createStatement());
                purCfgs.put(oConsumption.getFkItemReference_n(), vConfigs);
            }

            if (vConfigs.size() > 0) {
                if (lAccounts.containsKey(vConfigs.get(0).getAccountId())) {
                    oAccount = lAccounts.get(vConfigs.get(0).getAccountId());
                }
                else {
                    oAccount = new SDataAccount();
                    oAccount.read(new String[] { vConfigs.get(0).getAccountId() }, session.getStatement());
                    lAccounts.put(vConfigs.get(0).getAccountId(), oAccount);
                }

                if (lCC.containsKey(vConfigs.get(0).getCostCenterId())) {
                    nIdCC = lCC.get(vConfigs.get(0).getCostCenterId());
                }
                else {
                    nIdCC = SFinUtils.getCostCenterId(session, vConfigs.get(0).getCostCenterId());
                    lCC.put(vConfigs.get(0).getCostCenterId(), nIdCC);
                }

                SFinUtils.getCostCenterId(session, vConfigs.get(0).getCostCenterId());

                oDataRecordEntry.setFkAccountIdXXX(vConfigs.get(0).getAccountId());
                oDataRecordEntry.setFkAccountId(oAccount.getPkAccountId());
                oDataRecordEntry.setFkCostCenterId_n(nIdCC);
                oDataRecordEntry.setFkAccountingMoveTypeId(oAccount.getFkAccountTypeId_r());
                oDataRecordEntry.setFkAccountingMoveClassId(oAccount.getFkAccountClassId_r());
                oDataRecordEntry.setFkAccountingMoveSubclassId(oAccount.getFkAccountSubclassId_r());
                oDataRecordEntry.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_PUR[0]);
                oDataRecordEntry.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_PUR[1]);
                oDataRecordEntry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[0]);
                oDataRecordEntry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[0]);
                oDataRecordEntry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_PUR_GOOD[0]);
                oDataRecordEntry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_PUR_GOOD[1]);
                oDataRecordEntry.setFkCurrencyId(1);
                oDataRecordEntry.setFkCostCenterIdXXX_n(vConfigs.get(0).getCostCenterId());
            }
            else {
                throw new Exception("No se encontró cuenta contable para el item " + oConsumption.getFkItemId());
            }

            oDataRecordEntry.setFkCheckWalletId_n(0);
            oDataRecordEntry.setFkCheckId_n(0);
            oDataRecordEntry.setFkBizPartnerId_nr(0);
            oDataRecordEntry.setFkBizPartnerBranchId_n(0);
            oDataRecordEntry.setFkReferenceCategoryId_n(0);
            oDataRecordEntry.setFkCompanyBranchId_n(0);
            oDataRecordEntry.setFkEntityId_n(0);
            oDataRecordEntry.setFkPlantEntityId_n(0);
            oDataRecordEntry.setFkTaxBasicId_n(0);
            oDataRecordEntry.setFkTaxId_n(0);
            oDataRecordEntry.setFkYearId_n(0);
            oDataRecordEntry.setFkDpsYearId_n(0);
            oDataRecordEntry.setFkDpsDocId_n(0);
            oDataRecordEntry.setFkDpsAdjustmentYearId_n(0);
            oDataRecordEntry.setFkDpsAdjustmentDocId_n(0);
            oDataRecordEntry.setFkDiogYearId_n(oConsumption.getFkDiogYear());
            oDataRecordEntry.setFkDiogDocId_n(oConsumption.getFkDiogDocId());
            oDataRecordEntry.setFkMfgYearId_n(0);
            oDataRecordEntry.setFkMfgOrdId_n(0);
            oDataRecordEntry.setFkCfdId_n(0);
            oDataRecordEntry.setFkCostGicId_n(0);
            oDataRecordEntry.setFkPayrollFormerId_n(0);
            oDataRecordEntry.setFkPayrollId_n(0);
            oDataRecordEntry.setFkItemId_n(oConsumption.getFkItemId());
            oDataRecordEntry.setFkItemAuxId_n(oConsumption.getFkItemReference_n());
            oDataRecordEntry.setFkUnitId_n(1);
            oDataRecordEntry.setFkBookkeepingYearId_n(0);
            oDataRecordEntry.setFkBookkeepingNumberId_n(0);
            oDataRecordEntry.setFkUserNewId(session.getUser().getPkUserId());
            oDataRecordEntry.setFkUserEditId(1);
            oDataRecordEntry.setFkUserDeleteId(1);
            oDataRecordEntry.setSortingPosition(++sortPosition);

            oDataRecordEntry.save(session.getStatement().getConnection());
            if (oDataRecordEntry.getLastDbActionResult() == SLibConstants.DB_ACTION_SAVE_ERROR) {
                throw new Exception("Error al guardar el movimiento contable.");
            }

            oRecEtyAcc = new SDbStockValuationAccount();

            oRecEtyAcc.setFkFinRecYearId(oDataRecordEntry.getPkYearId());
            oRecEtyAcc.setFkFinRecPerId(oDataRecordEntry.getPkPeriodId());
            oRecEtyAcc.setFkFinRecBkcId(oDataRecordEntry.getPkBookkeepingCenterId());
            oRecEtyAcc.setFkFinRecTpRecId(oDataRecordEntry.getPkRecordTypeId());
            oRecEtyAcc.setFkFinRecNum(oDataRecordEntry.getPkNumberId());
            oRecEtyAcc.setFkFinRecEty(oDataRecordEntry.getPkEntryId());
            oRecEtyAcc.setFkStockValuationId(oConsumption.getFkStockValuationId());
            oRecEtyAcc.setFkValuationMvtId(oConsumption.getPkStockValuationMvtId());

            oRecEtyAcc.save(session);
            
            /**
             * Partida de almacén
             */
            oDataRecordEntryWhs = new SDataRecordEntry();
            
            /** Este es el bloque para partidas individuales:
             * 
             */
            oDataRecordEntryWhs.setPkYearId((int) recordPk[0]);
            oDataRecordEntryWhs.setPkPeriodId((int) recordPk[1]);
            oDataRecordEntryWhs.setPkBookkeepingCenterId((int) recordPk[2]);
            oDataRecordEntryWhs.setPkRecordTypeId((String) recordPk[3]);
            oDataRecordEntryWhs.setPkNumberId((int) recordPk[4]);
            oDataRecordEntryWhs.setConcept(SStockValuationRecordUtils.getConcept(oItem.getName(), oConsumption.getQuantityConsumption(), oUnit.getSymbol(), oCfg.getTextAssetEntries()));
            oDataRecordEntryWhs.setReference("");
            oDataRecordEntryWhs.setIsReferenceTax(false);
            oDataRecordEntryWhs.setDebit(0d);
            oDataRecordEntryWhs.setCredit(oConsumption.getCost_r());
            oDataRecordEntryWhs.setExchangeRate(1d);
            oDataRecordEntryWhs.setExchangeRateSystem(1d);
            oDataRecordEntryWhs.setDebitCy(0d);
            oDataRecordEntryWhs.setCreditCy(oConsumption.getCost_r());
            oDataRecordEntryWhs.setUnits(0d);
            oDataRecordEntryWhs.setUserId(0);
            oDataRecordEntryWhs.setSortingPosition(0);
            oDataRecordEntryWhs.setOccasionalFiscalId("");
            oDataRecordEntryWhs.setIsExchangeDifference(false);
            oDataRecordEntryWhs.setIsSystem(true);
            oDataRecordEntryWhs.setIsDeleted(false);

            if (assetCfgs.containsKey(oConsumption.getFkItemReference_n())) {
                vConfigsWhs = assetCfgs.get(oConsumption.getFkItemReference_n());
            }
            else {
                vConfigsWhs = SFinAccountUtilities.obtainItemAccountConfigs(oConsumption.getFkItemReference_n(), 
                                                                                    (int) recordPk[2], 
                                                                                    dtStart, 
                                                                                    oCfg.getFinTpAccItemAsset(), 
                                                                                    false, 
                                                                                    session.getStatement().getConnection().createStatement());
                assetCfgs.put(oConsumption.getFkItemReference_n(), vConfigsWhs);
            }

            if (vConfigsWhs.size() > 0) {
                if (lAccounts.containsKey(vConfigsWhs.get(0).getAccountId())) {
                    oAccount = lAccounts.get(vConfigsWhs.get(0).getAccountId());
                }
                else {
                    oAccount = new SDataAccount();
                    oAccount.read(new String[] { vConfigsWhs.get(0).getAccountId() }, session.getStatement());
                    lAccounts.put(vConfigsWhs.get(0).getAccountId(), oAccount);
                }

                oDataRecordEntryWhs.setFkAccountIdXXX(vConfigsWhs.get(0).getAccountId());
                oDataRecordEntryWhs.setFkAccountId(oAccount.getPkAccountId());
                oDataRecordEntryWhs.setFkCostCenterId_n(0);
                oDataRecordEntryWhs.setFkAccountingMoveTypeId(oAccount.getFkAccountTypeId_r());
                oDataRecordEntryWhs.setFkAccountingMoveClassId(oAccount.getFkAccountClassId_r());
                oDataRecordEntryWhs.setFkAccountingMoveSubclassId(oAccount.getFkAccountSubclassId_r());
                oDataRecordEntryWhs.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT[0]);
                oDataRecordEntryWhs.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT[1]);
                oDataRecordEntryWhs.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_WAH_WAH[0]);
                oDataRecordEntryWhs.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_WAH_WAH[1]);
                oDataRecordEntryWhs.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_ASSET_STOCK[0]);
                oDataRecordEntryWhs.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_ASSET_STOCK[1]);
                oDataRecordEntryWhs.setFkCurrencyId(1);
                oDataRecordEntryWhs.setFkCostCenterIdXXX_n("");
            }
            else {
                throw new Exception("No se encontró cuenta contable para el item " + oConsumption.getFkItemId());
            }

            oDataRecordEntryWhs.setFkCheckWalletId_n(0);
            oDataRecordEntryWhs.setFkCheckId_n(0);
            oDataRecordEntryWhs.setFkBizPartnerId_nr(0);
            oDataRecordEntryWhs.setFkBizPartnerBranchId_n(0);
            oDataRecordEntryWhs.setFkReferenceCategoryId_n(0);
            oDataRecordEntryWhs.setFkCompanyBranchId_n(oConsumption.getAuxWarehousePk()[0]);
            oDataRecordEntryWhs.setFkEntityId_n(oConsumption.getAuxWarehousePk()[1]);
            oDataRecordEntryWhs.setFkPlantEntityId_n(0);
            oDataRecordEntryWhs.setFkTaxBasicId_n(0);
            oDataRecordEntryWhs.setFkTaxId_n(0);
            oDataRecordEntryWhs.setFkYearId_n(0);
            oDataRecordEntryWhs.setFkDpsYearId_n(0);
            oDataRecordEntryWhs.setFkDpsDocId_n(0);
            oDataRecordEntryWhs.setFkDpsAdjustmentYearId_n(0);
            oDataRecordEntryWhs.setFkDpsAdjustmentDocId_n(0);
            oDataRecordEntryWhs.setFkDiogYearId_n(oConsumption.getFkDiogYear());
            oDataRecordEntryWhs.setFkDiogDocId_n(oConsumption.getFkDiogDocId());
            oDataRecordEntryWhs.setFkMfgYearId_n(0);
            oDataRecordEntryWhs.setFkMfgOrdId_n(0);
            oDataRecordEntryWhs.setFkCfdId_n(0);
            oDataRecordEntryWhs.setFkCostGicId_n(0);
            oDataRecordEntryWhs.setFkPayrollFormerId_n(0);
            oDataRecordEntryWhs.setFkPayrollId_n(0);
            oDataRecordEntryWhs.setFkItemId_n(oConsumption.getFkItemId());
            oDataRecordEntryWhs.setFkItemAuxId_n(oConsumption.getFkItemReference_n());
            oDataRecordEntryWhs.setFkUnitId_n(1);
            oDataRecordEntryWhs.setFkBookkeepingYearId_n(0);
            oDataRecordEntryWhs.setFkBookkeepingNumberId_n(0);
            oDataRecordEntryWhs.setFkUserNewId(session.getUser().getPkUserId());
            oDataRecordEntryWhs.setFkUserEditId(1);
            oDataRecordEntryWhs.setFkUserDeleteId(1);
            oDataRecordEntryWhs.setSortingPosition(++sortPosition);
            
            oDataRecordEntryWhs.save(session.getStatement().getConnection());
            if (oDataRecordEntryWhs.getLastDbActionResult() == SLibConstants.DB_ACTION_SAVE_ERROR) {
                throw new Exception("Error al guardar el movimiento contable.");
            }

            oRecEtyAcc = new SDbStockValuationAccount();
            
            oRecEtyAcc.setFkFinRecYearId(oDataRecordEntryWhs.getPkYearId());
            oRecEtyAcc.setFkFinRecPerId(oDataRecordEntryWhs.getPkPeriodId());
            oRecEtyAcc.setFkFinRecBkcId(oDataRecordEntryWhs.getPkBookkeepingCenterId());
            oRecEtyAcc.setFkFinRecTpRecId(oDataRecordEntryWhs.getPkRecordTypeId());
            oRecEtyAcc.setFkFinRecNum(oDataRecordEntryWhs.getPkNumberId());
            oRecEtyAcc.setFkFinRecEty(oDataRecordEntryWhs.getPkEntryId());
            oRecEtyAcc.setFkStockValuationId(oConsumption.getFkStockValuationId());
            oRecEtyAcc.setFkValuationMvtId(oConsumption.getPkStockValuationMvtId());
            

            oRecEtyAcc.save(session);
            
            i++;
        }
        
        return true;
    }

    private static String getConcept(final String itemName, final double qty, final String unit, final String text) {
        return SLibUtils.DecimalFormatValue2D.format(qty) + " " + unit + " " + itemName + 
                (text.isEmpty() ? "" : (" / " + text));
    }
}
