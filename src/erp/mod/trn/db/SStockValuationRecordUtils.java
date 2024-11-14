/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataBookkeepingNumber;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SFinAccountConfigEntry;
import erp.mfin.data.SFinAccountUtilities;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SFinUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SStockValuationRecordUtils {
    
    private static final int TYPE_PUR = 1;
    private static final int TYPE_INV = 2;
    
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
        SStockValuationConfiguration oCfg = SStockValuationUtils.getStockValuationConfig(session.getStatement().getConnection().createStatement());
        HashMap<Integer, java.util.Vector<erp.mfin.data.SFinAccountConfigEntry>> purCfgs = new HashMap<>();
        HashMap<Integer, java.util.Vector<erp.mfin.data.SFinAccountConfigEntry>> assetCfgs = new HashMap<>();
        java.util.Vector<erp.mfin.data.SFinAccountConfigEntry> vPurAccConfigs;
        java.util.Vector<erp.mfin.data.SFinAccountConfigEntry> vWhsAccConfigs;
        HashMap<Integer, SDataItem> lItems = new HashMap<>();
        SDataItem oItem;
        HashMap<Integer, SDataUnit> lUnits = new HashMap<>();
        SDataUnit oUnit;
        HashMap<String, SDataAccount> lAccounts = new HashMap<>();
        SDataAccount oAccount;
        HashMap<String, Integer> lCC = new HashMap<>();
        int nIdDiogEtyCc;
        int nIdCC;
        String sCc;
        ArrayList<SDataRecordEntry> lRecordEntries = new ArrayList<>();
        int n = lConsumptions.size();
        int i = 1;
        if ((n * 2) > oCfg.getMaxRecEntries()) {
            throw new Exception("No se puede realizar esta acción, excedería el número máximo de partidas para la póliza, pruebe con un periodo de tiempo más corto");
        }
        SDataRecord oRecord = new SDataRecord();
        oRecord.read((Object) recordPk, session.getStatement());
        int sortPosition = oRecord.getLastSortingPosition();
        sortPosition = sortPosition == 0 ? 1 : sortPosition;
        int nItemReference;
        SDataBookkeepingNumber bookkeepingNumber = new SDataBookkeepingNumber();
        bookkeepingNumber.setPkYearId(oRecord.getPkYearId());
        bookkeepingNumber.setFkUserNewId(session.getUser().getPkUserId());
        if (bookkeepingNumber.save(session.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP + " Error al guardar SDataBookkeepingNumber.");
        }
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
                oItem.read(new int[]{oConsumption.getFkItemId()}, session.getStatement());
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
                    oUnit.read(new int[]{oConsumption.getFkUnitId()}, session.getStatement().getConnection().createStatement());
                    lUnits.put(oConsumption.getFkUnitId(), oUnit);
                }
            }
            else {
                oUnit = oItem.getDbmsDataUnit();
            }

            nItemReference = 0;
            ArrayList<SFinAccountConfigEntry> lPurAccConfigs = new ArrayList<>();
            /**
             * Si el consumo tiene asociada una Requisición de materiales
             */
            if (oConsumption.getAuxMaterialRequestEntryPk() != null) {
                if (oConsumption.getAuxMaterialRequestEntryPk()[1] > 0) {
                    lPurAccConfigs = SFinAccountUtilities.getConsumptionWhsAccountConfigsByMatReqEty(session.getStatement().getConnection(),
                            oConsumption.getAuxMaterialRequestEntryPk()[0],
                            oConsumption.getAuxMaterialRequestEntryPk()[1]);
                }
                if (lPurAccConfigs.isEmpty() || (! lPurAccConfigs.isEmpty() && lPurAccConfigs.get(0).getCostCenterId().isEmpty())) {
                    lPurAccConfigs = SFinAccountUtilities.getConsumptionWhsAccountConfigsByMatReq(session.getStatement().getConnection(),
                            oConsumption.getAuxMaterialRequestEntryPk()[0]);
                }

                nItemReference = SMaterialRequestUtils.getReferenceItemFromMaterialRequest(session.getStatement().getConnection(), oConsumption.getAuxMaterialRequestEntryPk());
            }

            if (nItemReference == 0) {
                /**
                 * Se deja este comentario para cuando se requiera hacer una inspección
                 * del proceso. Edwin Carmona 2024-07-29  
                 */
//                if (oConsumption.getFkItemId() == 18829) {
//                    int r = 0;
//                }
                if (oItem.getFkDefaultItemRefId_n() > 0) {
                    nItemReference = oItem.getFkDefaultItemRefId_n();
                }
                else if (oItem.getDbmsDataItemGeneric().getFkDefaultItemRefId_n() > 0) {
                    nItemReference = oItem.getDbmsDataItemGeneric().getFkDefaultItemRefId_n();
                }
                else {
                    nItemReference = oConsumption.getFkItemId();
                }
            }

            // Si la configuración es vacía se toma en cuenta la configuración del paquete contable
            if (lPurAccConfigs.isEmpty()) {
                /**
                 * Configuración para partida de gastos (debit)
                 */
                if (purCfgs.containsKey(oConsumption.getFkItemId())) {
                    vPurAccConfigs = purCfgs.get(oConsumption.getFkItemId());
                }
                else {
                    vPurAccConfigs = SFinAccountUtilities.obtainItemAccountConfigs(oConsumption.getFkItemId(),
                            (int) recordPk[2],
                            dtStart,
                            oCfg.getFinTpAccItemAsset(),
                            true,
                            session.getStatement().getConnection().createStatement());
                    purCfgs.put(oConsumption.getFkItemId(), vPurAccConfigs);
                }

                lPurAccConfigs.addAll(vPurAccConfigs);
            }
            
            if (lPurAccConfigs.size() > 0) {
                double dTotalAmount = 0d;
                SFinAccountConfigEntry oConfigMaj = null;
                for (SFinAccountConfigEntry oConfig : lPurAccConfigs) {
                    if (oConfigMaj == null || oConfigMaj.getPercentage() < oConfig.getPercentage()) {
                        oConfigMaj = oConfig;
                    }

                    oConfig.setAuxAmount(SLibUtils.roundAmount(oConfig.getPercentage() * oConsumption.getCost_r()));
                    dTotalAmount = SLibUtils.roundAmount(dTotalAmount + oConfig.getAuxAmount());
                }
                if (Math.abs(SLibUtils.roundAmount(dTotalAmount - oConsumption.getCost_r())) >= 0.01d) {
                    if (oConfigMaj != null) {
                        oConfigMaj.setAuxAmount(SLibUtils.roundAmount(oConfigMaj.getAuxAmount() + (oConsumption.getCost_r() - dTotalAmount)));
                    }
                }
                
                // Se asigna el centro de costo del movimiento de almacén de salida:
                nIdDiogEtyCc = oConsumption.getAuxFkCostCenter();

                for (SFinAccountConfigEntry oConfig : lPurAccConfigs) {
                    if (lAccounts.containsKey(oConfig.getAccountId())) {
                        oAccount = lAccounts.get(oConfig.getAccountId());
                    }
                    else {
                        oAccount = new SDataAccount();
                        oAccount.read(new String[]{oConfig.getAccountId()}, session.getStatement());
                        lAccounts.put(oConfig.getAccountId(), oAccount);
                    }
                    
                    if (nIdDiogEtyCc > 1) {
                        nIdCC = nIdDiogEtyCc;
                        sCc = SFinUtils.getCostCenterFormerIdXXX(session, nIdCC);
                    }
                    else {
                        if (lCC.containsKey(oConfig.getCostCenterId())) {
                            nIdCC = lCC.get(oConfig.getCostCenterId());
                        }
                        else {
                            nIdCC = SFinUtils.getCostCenterId(session, oConfig.getCostCenterId());
                            lCC.put(oConfig.getCostCenterId(), nIdCC);
                        }
                        
                        if (nIdCC == 0 || oConfig.getCostCenterId().isEmpty()) {
                            sCc = SDataUtilities.obtainCostCenterItem(session, oConsumption.getFkItemId());
                            if (sCc.isEmpty()) {
                                throw new Exception("No se encontró centro de costo para el item " + oConsumption.getFkItemId());
                            }
                            nIdCC = SFinUtils.getCostCenterId(session, sCc);
                        }
                        else {
                            sCc = oConfig.getCostCenterId();
                        }
                    }

                    /**
                     * Partida de gastos (debit)
                     */
                    oDataRecordEntry = createRecordEntry(session,
                            TYPE_PUR,
                            recordPk,
                            oItem,
                            oConsumption.getQuantityMovement(),
                            oConfig.getAuxAmount(),
                            oUnit,
                            oCfg.getTextPurEntries(),
                            oAccount,
                            nIdCC,
                            sCc,
                            sortPosition++,
                            new int[]{oConsumption.getFkDiogYearOutId_n(), oConsumption.getFkDiogDocOutId_n()},
                            nItemReference,
                            SModSysConsts.FINS_TP_SYS_MOV_PUR,
                            SModSysConsts.FINS_TP_SYS_ACC_NA_NA,
                            SDataConstantsSys.FINS_TP_SYS_MOV_PUR_GOOD,
                            null,
                            oConsumption.getFkStockValuationId(),
                            oConsumption.getPkStockValuationMvtId(),
                            bookkeepingNumber);
                    lRecordEntries.add(oDataRecordEntry);
                }
            }
            else {
                throw new Exception("No se encontró cuenta contable para el item " + oConsumption.getFkItemId());
            }
            
            /***********************************************************************************************************************************************/

            /**
             * Configuración de la partida de inventarios (credit)
             */
            if (assetCfgs.containsKey(oConsumption.getFkItemId())) {
                vWhsAccConfigs = assetCfgs.get(oConsumption.getFkItemId());
            }
            else {
                vWhsAccConfigs = SFinAccountUtilities.obtainItemAccountConfigs(oConsumption.getFkItemId(),
                        (int) recordPk[2],
                        dtStart,
                        oCfg.getFinTpAccItemPur(),
                        false,
                        session.getStatement().getConnection().createStatement());
                assetCfgs.put(oConsumption.getFkItemId(), vWhsAccConfigs);
            }

            if (vWhsAccConfigs.size() > 0) {
                double dTotalAmount = 0d;
                SFinAccountConfigEntry oConfigMaj = null;
                for (SFinAccountConfigEntry oConfig : vWhsAccConfigs) {
                    if (oConfigMaj == null || oConfigMaj.getPercentage() < oConfig.getPercentage()) {
                        oConfigMaj = oConfig;
                    }

                    oConfig.setAuxAmount(SLibUtils.roundAmount(oConfig.getPercentage() * oConsumption.getCost_r()));
                    dTotalAmount = SLibUtils.roundAmount(dTotalAmount + oConfig.getAuxAmount());
                }
                if (Math.abs(dTotalAmount - oConsumption.getCost_r()) >= 0.01d) {
                    if (oConfigMaj != null) {
                        oConfigMaj.setAuxAmount(SLibUtils.roundAmount(oConfigMaj.getAuxAmount() + (oConsumption.getCost_r() - dTotalAmount)));
                    }
                }
                for (SFinAccountConfigEntry oConfig : vWhsAccConfigs) {
                    if (lAccounts.containsKey(oConfig.getAccountId())) {
                        oAccount = lAccounts.get(oConfig.getAccountId());
                    }
                    else {
                        oAccount = new SDataAccount();
                        oAccount.read(new String[]{oConfig.getAccountId()}, session.getStatement());
                        lAccounts.put(oConfig.getAccountId(), oAccount);
                    }

                    if (lCC.containsKey(oConfig.getCostCenterId())) {
                        nIdCC = lCC.get(oConfig.getCostCenterId());
                    }
                    else {
                        nIdCC = SFinUtils.getCostCenterId(session, oConfig.getCostCenterId());
                        lCC.put(oConfig.getCostCenterId(), nIdCC);
                    }

                    /**
                     * Partida de almacén (credit)
                     */
                    oDataRecordEntryWhs = createRecordEntry(session,
                            TYPE_INV,
                            recordPk,
                            oItem,
                            oConsumption.getQuantityMovement(),
                            oConfig.getAuxAmount(),
                            oUnit,
                            oCfg.getTextAssetEntries(),
                            oAccount,
                            nIdCC,
                            oConfig.getCostCenterId(),
                            sortPosition++,
                            new int[]{oConsumption.getFkDiogYearOutId_n(), oConsumption.getFkDiogDocOutId_n()},
                            nItemReference,
                            SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT,
//                            SModSysConsts.FINS_TP_SYS_ACC_ENT_WAH_WAH,
                            SModSysConsts.FINS_TP_SYS_ACC_NA_NA,
//                            SDataConstantsSys.FINS_TP_SYS_MOV_ASSET_STOCK,
                            SDataConstantsSys.FINS_TP_SYS_MOV_NA,
                            oConsumption.getAuxWarehousePk(),
                            oConsumption.getFkStockValuationId(),
                            oConsumption.getPkStockValuationMvtId(),
                            bookkeepingNumber
                    );
                    lRecordEntries.add(oDataRecordEntryWhs);
                }
            }
            else {
                throw new Exception("No se encontró cuenta contable para el item " + oConsumption.getFkItemId());
            }
            
            i++;
        }

        return true;
    }
    
    /**
     * This function creates a record entry for a stock valuation transaction in a financial system.
     * 
     * @param session The session object represents the current user session in the system.
     * @param movType The movType parameter is an integer that represents the type of movement. It is
     * used to determine whether the record entry is a debit or credit entry.
     * @param recordPk An array containing the primary key values for the record entry. The values are:
     * [yearId, periodId, bookkeepingCenterId, recordTypeId, numberId].
     * @param oItem The parameter "oItem" is an object of type SDataItem, which represents an item or
     * product in the system. It contains information such as the item's name, ID, and other
     * attributes.
     * @param dQuantity The quantity of the item being recorded.
     * @param dCost The parameter `dCost` represents the cost of the item.
     * @param oUnit oUnit is an object of type SDataUnit, which represents a unit of measurement for an
     * item. It contains information such as the unit's symbol and conversion factors to other units.
     * @param sConceptText The parameter `sConceptText` is a string that represents the concept or
     * description of the record entry. It is used to provide additional information about the entry,
     * such as the purpose or reason for the transaction.
     * @param oAccount An object of type SDataAccount, which represents the accounting account
     * associated with the record entry.
     * @param nIdCc The parameter "nIdCc" represents the ID of the cost center associated with the
     * record entry.
     * @param sIdCc The parameter "sIdCc" is a String variable that represents the ID of a cost center.
     * @param sortPosition The sortPosition parameter is used to specify the sorting position of the
     * record entry. It determines the order in which the record entries will be displayed or
     * processed.
     * @param pkDiog - pkDiog[0]: Year ID of the document
     * @param nItemRef The parameter `nItemRef` is an integer that represents the reference item for
     * the record entry. It is used to link the record entry to a specific item in the system.
     * @param fkSystemMove The parameter fkSystemMove is an array of two integers. The first integer
     * represents the foreign key of the system move class, and the second integer represents the
     * foreign key of the system move type.
     * @param fkSystemAccount The parameter fkSystemAccount is an array that contains two integers. The
     * first integer represents the ID of the system account class, and the second integer represents
     * the ID of the system account type.
     * @param fkSystemMoveType The parameter fkSystemMoveType is an array of two integers. The first
     * integer represents the foreign key of the system move category, and the second integer
     * represents the foreign key of the system move type.
     * @param pkWhs The parameter pkWhs is an array that contains the following information:
     * @param stkValuationId The parameter stkValuationId is an integer that represents the ID of the
     * stock valuation.
     * @param stkValuationMvtId The parameter "stkValuationMvtId" represents the ID of the stock
     * valuation movement. It is used to link the stock valuation movement to the record entry in the
     * database.
     * @return The method is returning an instance of the SDataRecordEntry class.
     */
    private static SDataRecordEntry createRecordEntry(SGuiSession session, 
            final int movType, final Object[] recordPk, final SDataItem oItem, final double dQuantity, final double dCost,
            final SDataUnit oUnit, final String sConceptText, final SDataAccount oAccount, final int nIdCc, 
            final String sIdCc, final int sortPosition, final int[] pkDiog, final int nItemRef, final int[] fkSystemMove,
            final int[] fkSystemAccount, final int[] fkSystemMoveType, final int[] pkWhs, final int stkValuationId, final int stkValuationMvtId,
            final SDataBookkeepingNumber bookkeepingNumber) throws Exception {
        SDataRecordEntry oRecordEntry = new SDataRecordEntry();

        oRecordEntry.setPkYearId((int) recordPk[0]);
        oRecordEntry.setPkPeriodId((int) recordPk[1]);
        oRecordEntry.setPkBookkeepingCenterId((int) recordPk[2]);
        oRecordEntry.setPkRecordTypeId((String) recordPk[3]);
        oRecordEntry.setPkNumberId((int) recordPk[4]);
        oRecordEntry.setConcept(SStockValuationRecordUtils.getConcept(oItem.getName(), dQuantity, oUnit.getSymbol(), sConceptText));
        oRecordEntry.setReference("");
        oRecordEntry.setIsReferenceTax(false);
        if (movType == TYPE_INV) {
            oRecordEntry.setDebit(0d);
            oRecordEntry.setCredit(dCost);
            oRecordEntry.setDebitCy(0d);
            oRecordEntry.setCreditCy(dCost);
        }
        else {
            oRecordEntry.setDebit(dCost);
            oRecordEntry.setCredit(0d);
            oRecordEntry.setDebitCy(dCost);
            oRecordEntry.setCreditCy(0d);
        }
        oRecordEntry.setExchangeRate(1d);
        oRecordEntry.setExchangeRateSystem(1d);
        oRecordEntry.setUnits(0d);
        oRecordEntry.setUserId(0);
        oRecordEntry.setSortingPosition(0);
        oRecordEntry.setOccasionalFiscalId("");
        oRecordEntry.setIsExchangeDifference(false);
        oRecordEntry.setIsSystem(true);
        oRecordEntry.setIsDeleted(false);

        oRecordEntry.setFkAccountIdXXX(oAccount.getPkAccountIdXXX());
        oRecordEntry.setFkAccountId(oAccount.getPkAccountId());
        oRecordEntry.setFkCostCenterId_n(nIdCc);
        oRecordEntry.setFkAccountingMoveTypeId(oAccount.getFkAccountTypeId_r());
        oRecordEntry.setFkAccountingMoveClassId(oAccount.getFkAccountClassId_r());
        oRecordEntry.setFkAccountingMoveSubclassId(oAccount.getFkAccountSubclassId_r());
        oRecordEntry.setFkSystemMoveClassId(fkSystemMove[0]);
        oRecordEntry.setFkSystemMoveTypeId(fkSystemMove[1]);
        oRecordEntry.setFkSystemAccountClassId(fkSystemAccount[0]);
        oRecordEntry.setFkSystemAccountTypeId(fkSystemAccount[1]);
        oRecordEntry.setFkSystemMoveCategoryIdXXX(fkSystemMoveType[0]);
        oRecordEntry.setFkSystemMoveTypeIdXXX(fkSystemMoveType[1]);
        oRecordEntry.setFkCurrencyId(1);
        oRecordEntry.setFkCostCenterIdXXX_n(sIdCc);

        oRecordEntry.setFkCheckWalletId_n(0);
        oRecordEntry.setFkCheckId_n(0);
        oRecordEntry.setFkBizPartnerId_nr(0);
        oRecordEntry.setFkBizPartnerBranchId_n(0);
        oRecordEntry.setFkReferenceCategoryId_n(0);
        if (pkWhs != null) {
            oRecordEntry.setFkCompanyBranchId_n(pkWhs[0]);
            oRecordEntry.setFkEntityId_n(pkWhs[1]);
        }
        else {
            oRecordEntry.setFkCompanyBranchId_n(0);
            oRecordEntry.setFkEntityId_n(0);
        }
        oRecordEntry.setFkPlantEntityId_n(0);
        oRecordEntry.setFkTaxBasicId_n(0);
        oRecordEntry.setFkTaxId_n(0);
        oRecordEntry.setFkYearId_n(0);
        oRecordEntry.setFkDpsYearId_n(0);
        oRecordEntry.setFkDpsDocId_n(0);
        oRecordEntry.setFkDpsAdjustmentYearId_n(0);
        oRecordEntry.setFkDpsAdjustmentDocId_n(0);
        oRecordEntry.setFkDiogYearId_n(pkDiog[0]);
        oRecordEntry.setFkDiogDocId_n(pkDiog[1]);
        oRecordEntry.setFkMfgYearId_n(0);
        oRecordEntry.setFkMfgOrdId_n(0);
        oRecordEntry.setFkCfdId_n(0);
        oRecordEntry.setFkCostGicId_n(0);
        oRecordEntry.setFkPayrollFormerId_n(0);
        oRecordEntry.setFkPayrollId_n(0);
        oRecordEntry.setFkItemId_n(nItemRef);
        oRecordEntry.setFkItemAuxId_n(oItem.getPkItemId());
        oRecordEntry.setFkUnitId_n(1);
        oRecordEntry.setFkBookkeepingYearId_n(bookkeepingNumber.getPkYearId());
        oRecordEntry.setFkBookkeepingNumberId_n(bookkeepingNumber.getPkNumberId());
        oRecordEntry.setFkUserNewId(session.getUser().getPkUserId());
        oRecordEntry.setFkUserEditId(1);
        oRecordEntry.setFkUserDeleteId(1);
        oRecordEntry.setSortingPosition(sortPosition);
        
        oRecordEntry.save(session.getStatement().getConnection());
        if (oRecordEntry.getLastDbActionResult() == SLibConstants.DB_ACTION_SAVE_ERROR) {
            throw new Exception("Error al guardar el movimiento contable.");
        }
        
        SDbStockValuationAccount oRecEtyAcc = new SDbStockValuationAccount();
            
        oRecEtyAcc.setFkFinRecYearId(oRecordEntry.getPkYearId());
        oRecEtyAcc.setFkFinRecPerId(oRecordEntry.getPkPeriodId());
        oRecEtyAcc.setFkFinRecBkcId(oRecordEntry.getPkBookkeepingCenterId());
        oRecEtyAcc.setFkFinRecTpRecId(oRecordEntry.getPkRecordTypeId());
        oRecEtyAcc.setFkFinRecNum(oRecordEntry.getPkNumberId());
        oRecEtyAcc.setFkFinRecEty(oRecordEntry.getPkEntryId());
        oRecEtyAcc.setFkStockValuationId(stkValuationId);
        oRecEtyAcc.setFkValuationMvtId(stkValuationMvtId);

        oRecEtyAcc.save(session);
        
        if (oRecEtyAcc.getQueryResultId() != SDbConsts.SAVE_OK) {
            throw new Exception("Error al guardar el link movimiento contable.");
        }
        
        return oRecordEntry; 
    }

    private static String getConcept(final String itemName, final double qty, final String unit, final String text) {
        return SLibUtils.DecimalFormatValue2D.format(qty) + " " + unit + " " + itemName + 
                (text.isEmpty() ? "" : (" / " + text));
    }
}
