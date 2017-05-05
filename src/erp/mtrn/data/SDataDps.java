/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import cfd.DAttributeOptionCondicionesPago;
import cfd.DAttributeOptionImpuestoRetencion;
import cfd.DAttributeOptionImpuestoTraslado;
import cfd.DAttributeOptionMoneda;
import cfd.DAttributeOptionTipoComprobante;
import cfd.DCfdConsts;
import cfd.DCfdTax;
import cfd.DCfdTaxes;
import cfd.DElement;
import cfd.ext.bachoco.DElementLineItem;
import cfd.ext.bachoco.DElementPayment;
import cfd.ext.elektra.DElementAp;
import cfd.ext.elektra.DElementDetailItems;
import cfd.ext.elektra.DElementDetalle;
import cfd.ext.elektra.DElementImpuestos;
import cfd.ext.elektra.DElementProducto;
import cfd.ext.elektra.DElementTrasladado;
import cfd.ext.interfactura.DElementCuerpo;
import cfd.ext.interfactura.DElementFacturaInterfactura;
import cfd.ext.soriana.DElementArticulos;
import cfd.ext.soriana.DElementDSCargaRemisionProv;
import cfd.ext.soriana.DElementFolioNotaEntrada;
import erp.SClient;
import erp.cfd.SCfdConsts;
import erp.cfd.SCfdDataCfdiRelacionado;
import erp.cfd.SCfdDataConcepto;
import erp.cfd.SCfdDataImpuesto;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.gui.session.SSessionCustom;
import erp.gui.session.SSessionItem;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataBookkeepingNumber;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SFinAccountConfig;
import erp.mfin.data.SFinAccountConfigEntry;
import erp.mfin.data.SFinAccountUtilities;
import erp.mfin.data.SFinAmount;
import erp.mfin.data.SFinAmountType;
import erp.mfin.data.SFinAmounts;
import erp.mfin.data.SFinDpsTaxes;
import erp.mfin.data.SFinMovement;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.SDbMmsConfig;
import erp.mod.trn.db.STrnUtils;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;

/**
 *
 * @author Sergio Flores, juan Barajas
 */
public class SDataDps extends erp.lib.data.SDataRegistry implements java.io.Serializable, erp.cfd.SCfdXml {

    public static final int FIELD_REF_BKR = 1;
    public static final int FIELD_SAL_AGT = 2;
    public static final int FIELD_SAL_AGT_SUP = 3;
    public static final int FIELD_CLO_COMMS = 4;
    public static final int FIELD_CLO_COMMS_USR = 5;
    public static final int FIELD_USR = 6;
    public static final String TXT_ADV_BILL = "facturación anticipos";
    public static final String MSG_ERR_FIN_REC_USR = "No se ha especificado la póliza contable de usuario.";
    public static final String MSG_ERR_ACC_UNK_ = "No se encontró la configuración de cuentas contables para el ";
    public static final String MSG_ERR_ACC_EMP_ = "La configuración de cuentas contables está vacía para el ";
    public static final String MSG_ERR_ITM_CFG_ = "Es incorrecta la configuración del ";
    
    public static final int AUT_AUTHORN_REJ_NA = 0;
    public static final int AUT_AUTHORN_REJ_LIM_USR = 1;
    public static final int AUT_AUTHORN_REJ_LIM_USR_MON = 2;
    public static final int AUT_AUTHORN_REJ_LIM_FUNC_MON = 3;
    
    public static final String TXT_AUT_AUTHORN_REJ_NA = "No aplica";
    public static final String TXT_AUT_AUTHORN_REJ_LIM_USR = "Tope evento usuario";
    public static final String TXT_AUT_AUTHORN_REJ_LIM_USR_MON = "Tope mensual usuario";
    public static final String TXT_AUT_AUTHORN_REJ_LIM_FUNC_MON = "Tope mensual área funcional";
    
    public static final int AMT_PRE_PAY = 1;
    public static final int AMT_PRE_PAY_CY = 2;
    
    public static final HashMap<Integer, String> AutAuthornRejMap = new HashMap<Integer, String>();
    
    static {
        AutAuthornRejMap.put(AUT_AUTHORN_REJ_NA, TXT_AUT_AUTHORN_REJ_NA);
        AutAuthornRejMap.put(AUT_AUTHORN_REJ_LIM_USR, TXT_AUT_AUTHORN_REJ_LIM_USR);
        AutAuthornRejMap.put(AUT_AUTHORN_REJ_LIM_USR_MON, TXT_AUT_AUTHORN_REJ_LIM_USR_MON);
        AutAuthornRejMap.put(AUT_AUTHORN_REJ_LIM_FUNC_MON, TXT_AUT_AUTHORN_REJ_LIM_FUNC_MON);
    }
            
    protected int mnPkYearId;
    protected int mnPkDocId;
    protected java.util.Date mtDate;
    protected java.util.Date mtDateDoc;
    protected java.util.Date mtDateStartCredit;
    protected java.util.Date mtDateShipment_n;
    protected java.util.Date mtDateDelivery_n;
    protected java.util.Date mtDateDocLapsing_n;
    protected java.util.Date mtDateDocDelivery_n;
    protected java.lang.String msNumberSeries;
    protected java.lang.String msNumber;
    protected java.lang.String msNumberReference;
    protected java.lang.String msCommissionsReference;
    protected int mnApprovalYear;
    protected int mnApprovalNumber;
    protected int mnDaysOfCredit;
    protected boolean mbIsDiscountDocApplying;
    protected boolean mbIsDiscountDocPercentage;
    protected double mdDiscountDocPercentage;
    protected double mdSubtotalProvisional_r;
    protected double mdDiscountDoc_r;
    protected double mdSubtotal_r;
    protected double mdTaxCharged_r;
    protected double mdTaxRetained_r;
    protected double mdTotal_r;
    protected double mdCommissions_r;
    protected double mdExchangeRate;
    protected double mdExchangeRateSystem;
    protected double mdSubtotalProvisionalCy_r;
    protected double mdDiscountDocCy_r;
    protected double mdSubtotalCy_r;
    protected double mdTaxChargedCy_r;
    protected double mdTaxRetainedCy_r;
    protected double mdTotalCy_r;
    protected double mdCommissionsCy_r;
    protected java.lang.String msDriver;
    protected java.lang.String msPlate;
    protected java.lang.String msTicket;
    protected int mnShipments;
    protected int mnPayments;
    protected java.lang.String msPaymentMethod;
    protected java.lang.String msPaymentAccount;
    protected int mnAutomaticAuthorizationRejection;
    protected boolean mbIsPublic;
    protected boolean mbIsLinked;
    protected boolean mbIsClosed;
    protected boolean mbIsClosedCommissions;
    protected boolean mbIsShipped;
    protected boolean mbIsRebill;
    protected boolean mbIsAudited;
    protected boolean mbIsAuthorized;
    protected boolean mbIsRecordAutomatic;
    protected boolean mbIsCopy;
    protected boolean mbIsCopied;
    protected boolean mbIsSystem;
    protected boolean mbIsDeleted;
    protected int mnFkDpsCategoryId;
    protected int mnFkDpsClassId;
    protected int mnFkDpsTypeId;
    protected int mnFkPaymentTypeId;
    protected int mnFkPaymentSystemTypeId;
    protected int mnFkDpsStatusId;
    protected int mnFkDpsValidityStatusId;
    protected int mnFkDpsAuthorizationStatusId;
    protected int mnFkDpsAnnulationTypeId;
    protected int mnFkDpsNatureId;
    protected int mnFkCompanyBranchId;
    protected int mnFkFunctionalAreaId;
    protected int mnFkBizPartnerId_r;
    protected int mnFkBizPartnerBranchId;
    protected int mnFkBizPartnerBranchAddressId;
    protected int mnFkBizPartnerAltId_r;
    protected int mnFkBizPartnerBranchAltId;
    protected int mnFkBizPartnerBranchAddressAltId;
    protected int mnFkContactBizPartnerBranchId_n;
    protected int mnFkContactContactId_n;
    protected int mnFkTaxIdentityEmisorTypeId;
    protected int mnFkTaxIdentityReceptorTypeId;
    protected int mnFkLanguajeId;
    protected int mnFkCurrencyId;
    protected int mnFkSalesAgentId_n;
    protected int mnFkSalesAgentBizPartnerId_n;
    protected int mnFkSalesSupervisorId_n;
    protected int mnFkSalesSupervisorBizPartnerId_n;
    protected int mnFkIncotermId;
    protected int mnFkSpotSourceId_n;
    protected int mnFkSpotDestinyId_n;
    protected int mnFkModeOfTransportationTypeId;
    protected int mnFkCarrierTypeId;
    protected int mnFkCarrierId_n;
    protected int mnFkVehicleTypeId_n;
    protected int mnFkVehicleId_n;
    protected int mnFkSourceYearId_n;
    protected int mnFkSourceDocId_n;
    protected int mnFkMfgYearId_n;
    protected int mnFkMfgOrderId_n;
    protected int mnFkUserLinkedId;
    protected int mnFkUserClosedId;
    protected int mnFkUserClosedCommissionsId;
    protected int mnFkUserShippedId;
    protected int mnFkUserAuditedId;
    protected int mnFkUserAuthorizedId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserLinkedTs;
    protected java.util.Date mtUserClosedTs;
    protected java.util.Date mtUserClosedCommissionsTs;
    protected java.util.Date mtUserShippedTs;
    protected java.util.Date mtUserAuditedTs;
    protected java.util.Date mtUserAuthorizedTs;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.util.Vector<SDataDpsEntry> mvDbmsDpsEntries;
    protected java.util.Vector<SDataDpsNotes> mvDbmsDpsNotes;

    protected java.lang.Object moDbmsRecordKey;
    protected java.util.Date mtDbmsRecordDate;
    protected java.lang.String msDbmsCurrency;
    protected java.lang.String msDbmsCurrencyKey;

    protected boolean mbAuxIsFormerRecordAutomatic;
    protected java.lang.Object moAuxFormerRecordKey;
    protected erp.mtrn.data.SCfdParams moAuxCfdParams;
    protected boolean mbAuxIsNeedCfd;
    protected boolean mbAuxIsValidate;

    protected erp.mfin.data.SDataBookkeepingNumber moDbmsDataBookkeepingNumber;
    protected erp.mtrn.data.SDataCfd moDbmsDataCfd;
    protected erp.mtrn.data.SDataDpsAddenda moDbmsDataAddenda;

    protected String msCfdExpeditionLocality;
    protected String msCfdExpeditionState;
    protected double mdCfdIvaPorcentaje;
    
    protected erp.mtrn.data.SDataDpsCfd moDbmsDataDpsCfd;
    protected java.lang.String msXtaCfdConfirmation;
    protected java.lang.String msXtaCfdRelationType;
    protected java.lang.String msXtaCfdUseCfdi;
    
    protected java.lang.String msXtaCfdCceReasonTransfer;
    protected java.lang.String msXtaCfdCceOperationType;
    protected java.lang.String msXtaCfdCceImportRequest;
    protected int mnXtaCfdCceCertificateOrigin; 
    protected java.lang.String msXtaCfdCceCertificateOriginNumber;
    protected int mnXtaCfdCceSubdivisionHas;
    protected double mdXtaCfdCceExchangeRateUSD;
    protected double mdXtaCfdCceTotalUSD;
    protected java.lang.String msXtaCfdCceNumberExporter;
    protected java.lang.String msXtaCfdCceIncoterm;

    public SDataDps() {
        super(SDataConstants.TRN_DPS);
        mlRegistryTimeout = 1000 * 60 * 60 * 2; // 2 hr
        mvDbmsDpsEntries = new Vector<>();
        mvDbmsDpsNotes = new Vector<>();
        reset();
    }

    /*
     * Private functions
     */

    private boolean isDpsAuthorizedUser(SDataUserConfigurationTransaction oUserConfig) throws java.sql.SQLException, java.lang.Exception {
        return oUserConfig.getPurchasesOrderLimit_n() == 0 || oUserConfig.getPurchasesOrderLimit_n() >= mdTotal_r;
    }
    
    private boolean isDpsAuthorizedUserMonth(java.sql.Connection connection, SDataUserConfigurationTransaction oUserConfig) throws java.sql.SQLException, java.lang.Exception {
        double amtAccumUser = 0;
        
        amtAccumUser = STrnUtils.getOrderAmountMonthUser(connection, (int[]) getPrimaryKey(), mtDate, mnFkUserNewId);
        
        return oUserConfig.getPurchasesOrderLimitMonthly_n() == 0 || (oUserConfig.getPurchasesOrderLimitMonthly_n() >= mdTotal_r && oUserConfig.getPurchasesOrderLimitMonthly_n() >= (amtAccumUser + mdTotal_r));
    }
    
    private boolean isDpsAuthorizedFunctionalAreaMonth(java.sql.Connection connection) throws java.sql.SQLException, java.lang.Exception {
        double limitFuncArea = 0;
        double amtAccumFuncArea = 0;
        
        limitFuncArea = STrnUtils.getMaxLimitMonthFunctionalArea(connection, mnFkFunctionalAreaId);
        amtAccumFuncArea = STrnUtils.getOrderAmountMonthFunctionalArea(connection, (int[]) getPrimaryKey(), mtDate, mnFkFunctionalAreaId);
        
        return limitFuncArea == 0 || (limitFuncArea >= mdTotal_r && limitFuncArea >= (amtAccumFuncArea + mdTotal_r));
    }
    
    private boolean isDpsAuthorized(java.sql.Connection connection) throws java.sql.SQLException, java.lang.Exception {
        boolean autorized = false;
        Statement oStatement = null;
        SDataUserConfigurationTransaction oUserConfig = null;

        oStatement = connection.createStatement();

        oUserConfig = new SDataUserConfigurationTransaction();

        if (oUserConfig.read(new int[] { mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId }, oStatement) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
        }
        else {
            if (mnFkDpsCategoryId == SDataConstantsSys.TRNS_CT_DPS_PUR) {
                if (isOrderPur()) {
                    autorized = isDpsAuthorizedUser(oUserConfig);
                    
                    if (!autorized) {
                        mnAutomaticAuthorizationRejection = AUT_AUTHORN_REJ_LIM_USR;
                    }
                    else {
                        autorized = isDpsAuthorizedUserMonth(connection, oUserConfig);
                        
                        if (!autorized) {
                            mnAutomaticAuthorizationRejection = AUT_AUTHORN_REJ_LIM_USR_MON;
                        }
                        else {
                            autorized = isDpsAuthorizedFunctionalAreaMonth(connection);
                            
                            if (!autorized) {
                                mnAutomaticAuthorizationRejection = AUT_AUTHORN_REJ_LIM_FUNC_MON;
                            }
                            else {
                                mnAutomaticAuthorizationRejection = AUT_AUTHORN_REJ_NA;
                            }
                        }
                    }
                }
                else if (isDocumentPur()) {
                    if (oUserConfig.getPurchasesDocLimit_n() == 0 || oUserConfig.getPurchasesDocLimit_n() >= mdTotal_r) {
                        autorized = true;
                    }
                }
                /*
                if (isOrderPur() && (oUserConfig.getPurchasesOrderLimit_n() == 0 || oUserConfig.getPurchasesOrderLimit_n() >= mdTotal_r)) {
                    isAutorized = true;
                }
                else if (isDocumentPur() && (oUserConfig.getPurchasesDocLimit_n() == 0 || oUserConfig.getPurchasesDocLimit_n() >= mdTotal_r)) {
                    isAutorized = true;
                }
                */
            }
            else if (mnFkDpsCategoryId == SDataConstantsSys.TRNS_CT_DPS_SAL) {
                if (isOrderSal() && (oUserConfig.getSalesOrderLimit_n() == 0 || oUserConfig.getSalesOrderLimit_n() >= mdTotal_r)) {
                    autorized = true;
                }
                else if (isDocumentSal() && (oUserConfig.getSalesDocLimit_n() == 0 || oUserConfig.getSalesDocLimit_n() >= mdTotal_r)) {
                    autorized = true;
                }
            }
        }

        return autorized;
    }

    private void updateAuthorizationStatus(java.sql.Connection connection) throws java.sql.SQLException, java.lang.Exception {
        boolean isAutPurOrd = false;
        boolean isAutPurDps = false;
        boolean isAutSalOrd = false;
        boolean isAutSalDps = false;
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;

        if (!mbIsDeleted && (isOrder() || isDocument())) {
            statement = connection.createStatement();

            // XXX It is needed a "session" object in SDataRegistry objects in order to know current company, company branch, current entities, decimal an date format objects, etc.

            sql = "SELECT fid_bp FROM erp.bpsu_bpb WHERE id_bpb = " + mnFkCompanyBranchId + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
            else {
                sql = "SELECT b_authorn_pur_ord, b_authorn_pur_doc, b_authorn_sal_ord, b_authorn_sal_doc FROM cfg_param_co WHERE id_co = " + resultSet.getInt("fid_bp") + " ";
                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    isAutPurOrd = resultSet.getBoolean("b_authorn_pur_ord");
                    isAutPurDps = resultSet.getBoolean("b_authorn_pur_doc");
                    isAutSalOrd = resultSet.getBoolean("b_authorn_sal_ord");
                    isAutSalDps = resultSet.getBoolean("b_authorn_sal_doc");
                }
            }

            if (isOrderPur() && isAutPurOrd || isDocumentPur() && isAutPurDps || isOrderSal() && isAutSalOrd || isDocumentSal() && isAutSalDps) {
                if (isDpsAuthorized(connection)) {
                    mbIsAuthorized = true;
                    mnFkDpsAuthorizationStatusId = SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN;
                }
                else {
                    mbIsAuthorized = false;
                    mnFkDpsAuthorizationStatusId = SDataConstantsSys.TRNS_ST_DPS_AUTHORN_PENDING;
                }

                mnFkUserAuthorizedId = mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId;
            }
        }
    }

    private boolean isDebitForBizPartner() {
        return isDocumentSal() || isAdjustmentPur();
    }

    private boolean isDebitForOperations() {
        return isDocumentPur() || isAdjustmentSal();
    }

    private boolean isDebitForTaxes(int taxType) {
        return isDocumentPur() && taxType == SModSysConsts.FINS_TP_TAX_CHARGED ||
                isDocumentSal() && taxType == SModSysConsts.FINS_TP_TAX_RETAINED ||
                isAdjustmentPur() && taxType == SModSysConsts.FINS_TP_TAX_RETAINED ||
                isAdjustmentSal() && taxType == SModSysConsts.FINS_TP_TAX_CHARGED;
    }

    private boolean deleteRecord(java.lang.Object recordKey, boolean deleteWholeRecord, java.sql.Connection connection) throws java.lang.Exception {
        SDataRecord record = new SDataRecord();
        Statement statement = connection.createStatement();

        if (record.read(recordKey, statement) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ);
        }
        else {
            if (deleteWholeRecord) {
                record.setIsRegistryEdited(true);
                record.setIsDeleted(true);
                record.setFkUserDeleteId(mnFkUserDeleteId);

                for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
                    if (!entry.getIsDeleted()) {
                        // Delete aswell all non-deleted entries:

                        entry.setIsRegistryEdited(true);
                        entry.setIsDeleted(true);
                        entry.setFkUserDeleteId(mnFkUserDeleteId);
                    }
                }
            }
            else {
                for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
                    if (!entry.getIsDeleted()) {
                        // Delete aswell all non-deleted entries:
                        
                        if (moDbmsDataBookkeepingNumber != null) {
                            if (entry.getFkBookkeepingYearId_n() == moDbmsDataBookkeepingNumber.getPkYearId() && entry.getFkBookkeepingNumberId_n() == moDbmsDataBookkeepingNumber.getPkNumberId()) {
                                entry.setIsRegistryEdited(true);
                                entry.setIsDeleted(true);
                                entry.setFkUserDeleteId(mnFkUserDeleteId);
                            }
                        }
                        else {
                            if (isDocument()) {
                                if (entry.getFkDpsYearId_n() == mnPkYearId && entry.getFkDpsDocId_n() == mnPkDocId) {
                                    entry.setIsRegistryEdited(true);
                                    entry.setIsDeleted(true);
                                    entry.setFkUserDeleteId(mnFkUserDeleteId);
                                }
                            }
                            else {
                                if (entry.getFkDpsAdjustmentYearId_n() == mnPkYearId && entry.getFkDpsAdjustmentDocId_n() == mnPkDocId) {
                                    entry.setIsRegistryEdited(true);
                                    entry.setIsDeleted(true);
                                    entry.setFkUserDeleteId(mnFkUserDeleteId);
                                }
                            }
                        }
                    }
                }
            }

            if (record.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE);
            }

            if (moDbmsDataBookkeepingNumber != null && !moDbmsDataBookkeepingNumber.getIsDeleted()) {
                moDbmsDataBookkeepingNumber.setIsDeleted(true);
                moDbmsDataBookkeepingNumber.setFkUserDeleteId(mnFkUserDeleteId);
                if (moDbmsDataBookkeepingNumber.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE);
                }
            }
        }

        return true;
    }

    private void deleteLinks(java.sql.Statement statement) throws java.sql.SQLException {
        String sql = "";

        sql = "DELETE FROM trn_dps_dps_supply WHERE id_src_year = " + mnPkYearId + " AND id_src_doc = " + mnPkDocId + " ";
        statement.execute(sql);
        sql = "DELETE FROM trn_dps_dps_supply WHERE id_des_year = " + mnPkYearId + " AND id_des_doc = " + mnPkDocId + " ";
        statement.execute(sql);
        sql = "DELETE FROM trn_dps_dps_adj WHERE id_dps_year = " + mnPkYearId + " AND id_dps_doc = " + mnPkDocId + " ";
        statement.execute(sql);
        sql = "DELETE FROM trn_dps_dps_adj WHERE id_adj_year = " + mnPkYearId + " AND id_adj_doc = " + mnPkDocId + " ";
        statement.execute(sql);
        sql = "DELETE FROM trn_dps_riss WHERE id_old_year = " + mnPkYearId + " AND id_old_doc = " + mnPkDocId + " ";
        statement.execute(sql);
        sql = "DELETE FROM trn_dps_riss WHERE id_new_year = " + mnPkYearId + " AND id_new_doc = " + mnPkDocId + " ";
        statement.execute(sql);
        sql = "DELETE FROM trn_dps_repl WHERE id_old_year = " + mnPkYearId + " AND id_old_doc = " + mnPkDocId + " ";
        statement.execute(sql);
        sql = "DELETE FROM trn_dps_repl WHERE id_new_year = " + mnPkYearId + " "
                + "AND id_new_doc = " + mnPkDocId + " ";
        statement.execute(sql);
        sql = "DELETE FROM trn_dps_iog_chg WHERE id_dps_year = " + mnPkYearId + " AND id_dps_doc = " + mnPkDocId + " ";
        statement.execute(sql);
        sql = "DELETE FROM trn_dps_iog_war WHERE id_dps_year = " + mnPkYearId + " AND id_dps_doc = " + mnPkDocId + " ";
        statement.execute(sql);
        sql = "DELETE FROM trn_dsm_ety WHERE fid_src_dps_year_n = " + mnPkYearId + " AND fid_src_dps_doc_n = " + mnPkDocId + " ";
        statement.execute(sql);
        sql = "DELETE FROM trn_dsm_ety WHERE fid_des_dps_year_n = " + mnPkYearId + " AND fid_des_dps_doc_n = " + mnPkDocId + " ";
        statement.execute(sql);
        sql = "DELETE FROM mkt_comms_pay_ety WHERE fk_year = " + mnPkYearId + " AND fk_doc = " + mnPkDocId + " ";
        statement.execute(sql);
        sql = "DELETE FROM mkt_comms WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
        statement.execute(sql);
    }
    
    private void clearEntryDeliveryReferences(java.sql.Statement statement) throws java.sql.SQLException {
        String sql = "UPDATE trn_dps_ety SET con_prc_year = 0, con_prc_mon = 0 WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId;
        statement.execute(sql);
    }

    private java.lang.String getAccRecordType() {
        String type = "";
        int[] docClassKey = new int[] { mnFkDpsCategoryId, mnFkDpsClassId };

        if (SLibUtilities.compareKeys(docClassKey, SDataConstantsSys.TRNS_CL_DPS_PUR_DOC)) {
            type = SDataConstantsSys.FINU_TP_REC_PUR;
        }
        else if (SLibUtilities.compareKeys(docClassKey, SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ)) {
            type = SDataConstantsSys.FINU_TP_REC_PUR;
        }
        else if (SLibUtilities.compareKeys(docClassKey, SDataConstantsSys.TRNS_CL_DPS_SAL_DOC)) {
            type = SDataConstantsSys.FINU_TP_REC_SAL;
        }
        else if (SLibUtilities.compareKeys(docClassKey, SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ)) {
            type = SDataConstantsSys.FINU_TP_REC_SAL;
        }

        return type;
    }

    private java.lang.Object createAccRecordKey(java.sql.Statement statement) throws java.lang.Exception {
        int[] period = null;
        Object[] key = null;
        SDataBizPartnerBranch branch = new SDataBizPartnerBranch();

        if (branch.read(new int[] { mnFkCompanyBranchId }, statement) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
        }
        else {
            period = SLibTimeUtilities.digestYearMonth(mtDate);

            key = new Object[5];
            key[0] = period[0];
            key[1] = period[1];
            key[2] = branch.getDbmsDataCompanyBranchBkc().getPkBookkepingCenterId();
            key[3] = getAccRecordType();
            key[4] = 0;
        }

        return key;
    }

    private erp.mfin.data.SDataRecordEntry createAccRecordEntry(java.lang.String idAccount, java.lang.String idCostCenter, 
            int[] keyAccMoveSubclass, int[] keySysAccountType, int[] keySysMoveType, int[] keySysMoveTypeXXX, int[] keyAuxDps_n, int[] keyCashAccount_n) {
        SDataRecordEntry entry = new SDataRecordEntry();

        entry.setPkYearId((Integer) ((Object[]) moDbmsRecordKey)[0]);
        entry.setPkPeriodId((Integer) ((Object[]) moDbmsRecordKey)[1]);
        entry.setPkBookkeepingCenterId((Integer) ((Object[]) moDbmsRecordKey)[2]);
        entry.setPkRecordTypeId((String) ((Object[]) moDbmsRecordKey)[3]);
        entry.setPkNumberId((Integer) ((Object[]) moDbmsRecordKey)[4]);
        //entry.setPkEntryId(...); // will be set on save
        entry.setConcept("");
        entry.setReference("");
        entry.setIsReferenceTax(false);
        entry.setDebit(0.0);
        entry.setCredit(0.0);
        entry.setExchangeRate(mdExchangeRate);
        entry.setExchangeRateSystem(mdExchangeRateSystem);
        entry.setDebitCy(0.0);
        entry.setCreditCy(0.0);
        entry.setUnits(0.0);
        entry.setSortingPosition(0);
        entry.setIsSystem(true);
        entry.setIsDeleted(false);
        entry.setFkAccountIdXXX(idAccount);
        entry.setFkAccountingMoveTypeId(keyAccMoveSubclass[0]);
        entry.setFkAccountingMoveClassId(keyAccMoveSubclass[1]);
        entry.setFkAccountingMoveSubclassId(keyAccMoveSubclass[2]);
        entry.setFkSystemMoveClassId(keySysMoveType[0]);
        entry.setFkSystemMoveTypeId(keySysMoveType[1]);
        entry.setFkSystemAccountClassId(keySysAccountType[0]);
        entry.setFkSystemAccountTypeId(keySysAccountType[1]);
        entry.setFkSystemMoveCategoryIdXXX(keySysMoveTypeXXX[0]);
        entry.setFkSystemMoveTypeIdXXX(keySysMoveTypeXXX[1]);
        entry.setFkCurrencyId(mnFkCurrencyId);
        entry.setFkCostCenterIdXXX_n(idCostCenter);
        entry.setFkCheckWalletId_n(SLibConsts.UNDEFINED);
        entry.setFkCheckId_n(SLibConsts.UNDEFINED);
        entry.setFkBizPartnerId_nr(mnFkBizPartnerId_r);
        entry.setFkBizPartnerBranchId_n(mnFkBizPartnerBranchId);
        entry.setFkReferenceCategoryId_n(SLibConsts.UNDEFINED);
        entry.setFkCompanyBranchId_n(keyCashAccount_n == null ? 0 : keyCashAccount_n[0]);
        entry.setFkEntityId_n(keyCashAccount_n == null ? 0 : keyCashAccount_n[1]);
        entry.setFkTaxBasicId_n(SLibConsts.UNDEFINED);
        entry.setFkTaxId_n(SLibConsts.UNDEFINED);
        entry.setFkYearId_n(SLibConsts.UNDEFINED);

        if (isDocument()) {
            entry.setFkDpsYearId_n(mnPkYearId);
            entry.setFkDpsDocId_n(mnPkDocId);
            entry.setFkDpsAdjustmentYearId_n(SLibConsts.UNDEFINED);
            entry.setFkDpsAdjustmentDocId_n(SLibConsts.UNDEFINED);
        }
        else {
            entry.setFkDpsYearId_n(keyAuxDps_n[0]);
            entry.setFkDpsDocId_n(keyAuxDps_n[1]);
            entry.setFkDpsAdjustmentYearId_n(mnPkYearId);
            entry.setFkDpsAdjustmentDocId_n(mnPkDocId);
        }

        entry.setFkDiogYearId_n(SLibConsts.UNDEFINED);
        entry.setFkDiogDocId_n(SLibConsts.UNDEFINED);
        entry.setFkMfgYearId_n(SLibConsts.UNDEFINED);
        entry.setFkMfgOrdId_n(SLibConsts.UNDEFINED);
        entry.setFkCostGicId_n(SLibConsts.UNDEFINED);
        entry.setFkPayrollFormerId_n(SLibConsts.UNDEFINED);
        entry.setFkPayrollId_n(SLibConsts.UNDEFINED);
        entry.setFkItemId_n(SLibConsts.UNDEFINED);
        entry.setFkItemAuxId_n(SLibConsts.UNDEFINED);
        entry.setFkUnitId_n(SLibConsts.UNDEFINED);
        entry.setFkBookkeepingYearId_n(moDbmsDataBookkeepingNumber == null ? 0 : moDbmsDataBookkeepingNumber.getPkYearId());
        entry.setFkBookkeepingNumberId_n(moDbmsDataBookkeepingNumber == null ? 0 : moDbmsDataBookkeepingNumber.getPkNumberId());

        if (mbIsRegistryNew) {
            entry.setFkUserNewId(mnFkUserNewId);
            entry.setFkUserEditId(mnFkUserNewId);
        }
        else {
            entry.setFkUserNewId(mnFkUserEditId);
            entry.setFkUserEditId(mnFkUserEditId);
        }

        return entry;
    }
    
    private double obtainAmountPrepayments(final int amountPrepaymentType) {
        double taxAmount = 0;
        double amountPrepayment = 0;
        
        for (SDataDpsEntry entry : mvDbmsDpsEntries) {
            if (!entry.getIsDeleted() && entry.getIsPrepayment()) {
                for (SDataDpsEntryTax entryTax : entry.getDbmsEntryTaxes()) {
                    taxAmount = SLibUtilities.round(taxAmount + (amountPrepaymentType == AMT_PRE_PAY_CY ? entryTax.getTaxCy() : entryTax.getTax()), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                }
                amountPrepayment = SLibUtilities.round((amountPrepayment + (amountPrepaymentType == AMT_PRE_PAY_CY ? entry.getSubtotalCy_r() : entry.getSubtotal_r()) + taxAmount), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
            }
        }
        
        return amountPrepayment;
    }

    private boolean testDeletion(java.sql.Connection poConnection, java.lang.String psMsg, int pnAction) throws java.sql.SQLException, java.lang.Exception {
        int i = 0;
        int[] anPeriodKey = null;
        double dBalance;
        double dBalanceCy;
        double dAuxBalance = 0;
        double dAuxBalanceCy = 0;
        String sSql = "";
        String sMsg = psMsg;
        String sMsgAux = "";
        Statement oStatement = null;
        ResultSet oResultSet = null;
        DecimalFormat oDecimalFormat = new DecimalFormat("$ #,##0.00");
        CallableStatement oCallableStatement = null;

        if (pnAction == SDbConsts.ACTION_DELETE && mbIsDeleted) {
            mnDbmsErrorId = 1;
            msDbmsError = sMsg + "¡El documento ya está eliminado!";
            throw new Exception(msDbmsError);
        }
        else if (pnAction == SDbConsts.ACTION_ANNUL && mnFkDpsStatusId == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
            mnDbmsErrorId = 2;
            msDbmsError = sMsg + "¡El documento ya está anulado!";
            throw new Exception(msDbmsError);
        }
        else if (mbIsSystem) {
            mnDbmsErrorId = 11;
            msDbmsError = sMsg + "¡El documento es de sistema!";
            throw new Exception(msDbmsError);
        }
        else if (mbIsAudited) {
            mnDbmsErrorId = 12;
            msDbmsError = sMsg + "¡El documento está auditado!";
            throw new Exception(msDbmsError);
        }
        else if (mbIsAuthorized) {
            mnDbmsErrorId = 13;
            msDbmsError = sMsg + "¡El documento está autorizado!";
            throw new Exception(msDbmsError);
        }
        else if (pnAction == SDbConsts.ACTION_DELETE && moDbmsDataCfd != null && moDbmsDataCfd.isStamped()) {
            mnDbmsErrorId = 21;
            msDbmsError = sMsg + "¡El documento está timbrado!";
            throw new Exception(msDbmsError);
        }
        else if (moDbmsDataCfd != null && !mbAuxIsValidate && moDbmsDataCfd.getIsProcessingWebService()) {
            mnDbmsErrorId = 22;
            msDbmsError = sMsg + "¡" + SCfdConsts.ERR_MSG_PROCESSING_WEB_SERVICE + "!";
            throw new Exception(msDbmsError);
        }
        else if (moDbmsDataCfd != null && !mbAuxIsValidate && pnAction != SDbConsts.ACTION_ANNUL && moDbmsDataCfd.getIsProcessingStorageXml()) {
            mnDbmsErrorId = 23;
            msDbmsError = sMsg + "¡" + SCfdConsts.ERR_MSG_PROCESSING_XML_STORAGE + "!";
            throw new Exception(msDbmsError);
        }
        else if (mnFkDpsStatusId != SDataConstantsSys.TRNS_ST_DPS_EMITED) {
            mnDbmsErrorId = 41;
            msDbmsError = sMsg + "¡El documento debe tener estatus 'emitido'!";
            throw new Exception(msDbmsError);
        }
        else if (mnFkDpsValidityStatusId != SDataConstantsSys.TRNS_ST_DPS_VAL_EFF) {
            mnDbmsErrorId = 42;
            msDbmsError = sMsg + "¡El documento debe tener estatus de validez 'efectivo'!";
            throw new Exception(msDbmsError);
        }
        else if (mbIsShipped) {
            mnDbmsErrorId = 51;
            msDbmsError = sMsg + "¡El documento está embarcado!";
            throw new Exception(msDbmsError);
        }
        else {

            // Check that document's date belongs to an open period:

            i = 1;
            anPeriodKey = SLibTimeUtilities.digestYearMonth(mtDate);
            oCallableStatement = poConnection.prepareCall("{ CALL fin_year_per_st(?, ?, ?) }");
            oCallableStatement.setInt(i++, anPeriodKey[0]);
            oCallableStatement.setInt(i++, anPeriodKey[1]);
            oCallableStatement.registerOutParameter(i++, java.sql.Types.INTEGER);
            oCallableStatement.execute();

            if (oCallableStatement.getBoolean(i - 1)) {
                mnDbmsErrorId = 101;
                msDbmsError = sMsg + "¡El período contable de la fecha del documento está cerrado!";
                throw new Exception(msDbmsError);
            }

            if (isDocument()) {
                // Check that document's balance is equal to document's value:

                i = 1;
                anPeriodKey = SLibTimeUtilities.digestYearMonth(mtDate);
                oCallableStatement = poConnection.prepareCall("{ CALL trn_dps_bal(?, ?, ?, ?, ?, ?) }");
                oCallableStatement.setInt(i++, mnPkYearId);
                oCallableStatement.setInt(i++, mnPkDocId);
                oCallableStatement.setInt(i++, getAccSysTypeIdBizPartnerXXX());
                oCallableStatement.registerOutParameter(i++, java.sql.Types.DECIMAL);
                oCallableStatement.registerOutParameter(i++, java.sql.Types.DECIMAL);
                oCallableStatement.registerOutParameter(i++, java.sql.Types.SMALLINT);
                oCallableStatement.execute();

                dBalance = oCallableStatement.getDouble(i - 3);
                dBalanceCy = oCallableStatement.getDouble(i - 2);

                if (isDocumentPur()) {
                    dBalance *= -1d;
                    dBalanceCy *= -1d;
                }

                // Subtract amount prepayment the total and subtract amount prepayment currency the total currency:
                
                dAuxBalance = SLibUtilities.round(dBalance + obtainAmountPrepayments(AMT_PRE_PAY), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                dAuxBalanceCy = SLibUtilities.round(dBalanceCy + obtainAmountPrepayments(AMT_PRE_PAY_CY), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                
                if (dAuxBalance != mdTotal_r) {
                    mnDbmsErrorId = 101;
                    msDbmsError = sMsg + "¡El saldo del documento en la moneda local, " + oDecimalFormat.format(dAuxBalance) + ", " +
                            "es distinto al total del mismo, " + oDecimalFormat.format(mdTotal_r) + "!";
                    throw new Exception(msDbmsError);
                }

                if (dAuxBalanceCy != mdTotalCy_r) {
                    mnDbmsErrorId = 101;
                    msDbmsError = sMsg + "¡El saldo del documento en la moneda del documento, " + oDecimalFormat.format(dAuxBalanceCy) + ", " +
                            "es distinto al total del mismo, " + oDecimalFormat.format(mdTotalCy_r) + "!";
                    throw new Exception(msDbmsError);
                }
            }

            oStatement = poConnection.createStatement();

            for (i = 201; i <= 224; i++) {
                switch (i) {
                    case 201:
                        sSql = "SELECT count(*) AS f_count FROM trn_dps_dps_supply WHERE id_src_year = " + mnPkYearId + " AND id_src_doc = " + mnPkDocId + " ";
                        sMsgAux = "¡El documento tiene vínculo(s) con otro(s) documento(s) como origen!";
                        break;
                    /*
                    case 202:
                        sSql = "SELECT count(*) AS f_count FROM trn_dps_dps_supply WHERE id_des_year = " + mnPkYearId + " AND id_des_doc = " + mnPkDocId + " ";
                        sMsgAux = "¡El documento tiene vínculo(s) con otro(s) documento(s) como destino!";
                        break;
                    */
                    case 203:
                        sSql = "SELECT count(*) AS f_count FROM trn_dps_dps_adj WHERE id_dps_year = " + mnPkYearId + " AND id_dps_doc = " + mnPkDocId + " ";
                        sMsgAux = "¡El documento está asociado con otro documento de ajuste!";
                        break;
                    /*
                    case 204:
                        sSql = "SELECT count(*) AS f_count FROM trn_dps_dps_adj WHERE id_adj_year = " + mnPkYearId + " AND id_adj_doc = " + mnPkDocId + " ";
                        sMsgAux = "¡El documento de ajuste está asociado con otro documento!";
                        break;
                    */
                    case 205:
                        sSql = "SELECT count(*) AS f_count FROM trn_diog WHERE fid_dps_year_n = " + mnPkYearId + " AND fid_dps_doc_n = " + mnPkDocId + " AND b_del = 0 ";
                        sMsgAux = "¡El documento está asociado con un documento de entradas y salidas de mercancías!";
                        break;
                    case 206:
                        sSql = "SELECT count(*) AS f_count " +
                                "FROM trn_diog AS d " +
                                "INNER JOIN trn_diog_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                                "WHERE de.fid_dps_year_n = " + mnPkYearId + " AND de.fid_dps_doc_n = " + mnPkDocId + " AND de.fid_dps_adj_year_n IS NULL AND de.fid_dps_adj_doc_n IS NULL AND de.b_del = 0 AND d.b_del = 0 ";
                        sMsgAux = "¡El documento está asociado con un surtido de almacén!";
                        break;
                    case 207:
                        sSql = "SELECT count(*) AS f_count " +
                                "FROM trn_diog AS d " +
                                "INNER JOIN trn_diog_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                                "WHERE de.fid_dps_year_n = " + mnPkYearId + " AND de.fid_dps_doc_n = " + mnPkDocId + " AND de.fid_dps_adj_year_n IS NOT NULL AND de.fid_dps_adj_doc_n IS NOT NULL AND de.b_del = 0 AND d.b_del = 0 ";
                        sMsgAux = "¡El documento está asociado con una devolución de almacén como documento!";
                        break;
                    case 208:
                        sSql = "SELECT count(*) AS f_count " +
                                "FROM trn_diog AS d " +
                                "INNER JOIN trn_diog_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                                "WHERE de.fid_dps_adj_year_n = " + mnPkYearId + " AND de.fid_dps_adj_doc_n = " + mnPkDocId + " AND de.b_del = 0 AND d.b_del = 0 ";
                        sMsgAux = "¡El documento está asociado con una devolución de almacén como documento de ajuste!";
                        break;

                    case 209:
                        sSql = "SELECT count(*) AS f_count " +
                                "FROM mkt_comms " +
                                "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND b_del = 0 ";
                        sMsgAux = "¡El documento está asociado con un documento de comisiones!";
                        break;
                    case 210:
                        // Not longer needed since September 2014, due to new commissions tables.
                        break;
                    case 211:
                        sSql = "SELECT count(*) AS f_count FROM trn_dps_riss WHERE id_old_year = " + mnPkYearId + " AND id_old_doc = " + mnPkDocId + " ";
                        sMsgAux = "¡El documento ha sido reimpreso!";
                        break;
                    case 212:
                        sSql = "SELECT count(*) AS f_count FROM trn_dps_riss WHERE id_new_year = " + mnPkYearId + " AND id_new_doc = " + mnPkDocId + " ";
                        sMsgAux = "¡El documento es el reemplazo de otro documento!";
                        break;
                    case 213:
                        sSql = "SELECT count(*) AS f_count FROM trn_dps_repl WHERE id_old_year = " + mnPkYearId + " AND id_old_doc = " + mnPkDocId + " ";
                        sMsgAux = "¡El documento ha sido sustituído!";
                        break;
                    case 214:
                        sSql = "SELECT count(*) AS f_count FROM trn_dps_repl WHERE id_new_year = " + mnPkYearId + " AND id_new_doc = " + mnPkDocId + " ";
                        sMsgAux = "¡El documento es la sustitución de otro documento!";
                        break;
                    case 215:
                        sSql = "SELECT count(*) AS f_count FROM trn_dps_iog_chg WHERE id_dps_year = " + mnPkYearId + " AND id_dps_doc = " + mnPkDocId + " ";
                        sMsgAux = "¡El documento está asociado con un cambio de compras-ventas!";
                        break;
                    case 216:
                        sSql = "SELECT count(*) AS f_count FROM trn_dps_iog_war WHERE id_dps_year = " + mnPkYearId + " AND id_dps_doc = " + mnPkDocId + " ";
                        sMsgAux = "¡El documento está asociado con una garantía de compras-ventas!";
                        break;
                    case 217:
                        sSql = "SELECT count(*) AS f_count FROM trn_dsm_ety WHERE fid_src_dps_year_n = " + mnPkYearId + " AND fid_src_dps_doc_n = " + mnPkDocId + " AND b_del = 0 ";
                        sMsgAux = "¡El documento está asociado con un movimiento de asociado de negocios como origen!";
                        break;
                    case 218:
                        sSql = "SELECT count(*) AS f_count FROM trn_dsm_ety WHERE fid_des_dps_year_n = " + mnPkYearId + " AND fid_des_dps_doc_n = " + mnPkDocId + " AND b_del = 0 ";
                        sMsgAux = "¡El documento está asociado con un movimiento de asociado de negocios como destino!";
                        break;
                    case 219:
                        sSql = "SELECT count(*) AS f_count " +
                                "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                                "r.b_del = 0 AND re.b_del = 0 AND re.fid_dps_year_n = " + mnPkYearId + " AND re.fid_dps_doc_n = " + mnPkDocId + " AND " +
                                "r.id_tp_rec IN ('" + SDataConstantsSys.FINU_TP_REC_FY_OPEN + "', '" + SDataConstantsSys.FINU_TP_REC_FY_END + "') ";
                        sMsgAux = "¡El documento está en uso por pólizas contables de cierre o apertura de ejercicio como documento!";
                        break;
                    case 220:
                        sSql = "SELECT count(*) AS f_count " +
                                "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                                "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                                "r.b_del = 0 AND re.b_del = 0 AND re.fid_dps_adj_year_n = " + mnPkYearId + " AND re.fid_dps_adj_doc_n = " + mnPkDocId + " AND " +
                                "r.id_tp_rec IN ('" + SDataConstantsSys.FINU_TP_REC_FY_OPEN + "', '" + SDataConstantsSys.FINU_TP_REC_FY_END + "') ";
                        sMsgAux = "¡El documento está en uso por pólizas contables de cierre o apertura de ejercicio como documento de ajuste!";
                        break;
                    case 221:
                        if (isDocument() || isAdjustment()) {
                            sSql = "SELECT count(*) AS f_count " +
                                    "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                                    "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                                    "r.b_del = 0 AND re.b_del = 0 AND re.fid_dps_year_n = " + mnPkYearId + " AND re.fid_dps_doc_n = " + mnPkDocId + " AND " +
                                    "re.fid_tp_acc_mov <> " + getAccMvtSubclassKeyBizPartner()[0] + " AND " +
                                    "re.fid_tp_acc_mov <> " + getAccMvtSubclassKeyAccountCash()[0] + " ";
                            sMsgAux = "¡El documento está en uso por pólizas contables como documento!";
                        }
                        else {
                            sSql = "";
                        }
                        break;
                    case 222:
                        if (isDocument() || isAdjustment()) {
                            sSql = "SELECT count(*) AS f_count " +
                                    "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                                    "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                                    "r.b_del = 0 AND re.b_del = 0 AND re.fid_dps_adj_year_n = " + mnPkYearId + " AND re.fid_dps_adj_doc_n = " + mnPkDocId + " AND " +
                                    "re.fid_tp_acc_mov <> " + getAccMvtSubclassKeyBizPartner()[0] + " ";
                            sMsgAux = "¡El documento está en uso por pólizas contables como documento de ajuste!";
                        }
                        else {
                            sSql = "";
                        }
                        break;
                    case 223:
                        sSql = "SELECT count(*) AS f_count " +
                                "FROM log_ship " +
                                "WHERE b_del = 0 AND fk_ord_year_n = " + mnPkYearId + " AND fk_ord_doc_n = " + mnPkDocId + " ";
                        sMsgAux = "¡El documento está asociado a un documento de embarques!";
                        break;
                    case 224:
                        sSql = "SELECT count(*) AS f_count " +
                                "FROM log_ship AS d " +
                                "INNER JOIN log_ship_dest_ety AS de ON d.id_ship = de.id_ship " +
                                "WHERE d.b_del = 0 AND de.fk_dps_year_n = " + mnPkYearId + " AND de.fk_dps_doc_n = " + mnPkDocId + " ";
                        sMsgAux = "¡El documento está asociado a una partida de un destino de un documento de embarques!";
                        break;
                    default:
                        sSql = "";
                        sMsgAux = "";
                }

                if (sSql.length() > 0) {
                    oResultSet = oStatement.executeQuery(sSql);
                    if (oResultSet.next() && oResultSet.getInt("f_count") > 0) {
                        mnDbmsErrorId = i;
                        msDbmsError = sMsg + sMsgAux;
                        throw new Exception(msDbmsError);
                    }
                }
            }
        }

        return true;    // if this line is reached, no errors were found
    }

    private boolean testRevertDeletion(java.lang.String psMsg, int pnAction) throws java.sql.SQLException, java.lang.Exception {
        if (pnAction == SDbConsts.ACTION_DELETE && !mbIsDeleted) {
            mnDbmsErrorId = 2;
            msDbmsError = psMsg + "El documento ya está desmarcado como eliminado.";
            throw new Exception(msDbmsError);
        }
        else if (pnAction == SDbConsts.ACTION_ANNUL && mnFkDpsStatusId != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
            mnDbmsErrorId = 1;
            msDbmsError = psMsg + "El documento ya está desmarcado como anulado.";
            throw new Exception(msDbmsError);
        }

        return true;    // if this line is reached, no errors were found

    }

    private void calculateCceTotal() throws SQLException, Exception {
        double valueMxn = 0;
        double valueUsdEty = 0;
        mdXtaCfdCceTotalUSD = 0;
        
        for (SDataDpsEntry dpsEntry : mvDbmsDpsEntries) {
            if (dpsEntry.isAccountable()) {
                valueMxn = SLibUtils.round((dpsEntry.getSubtotalCy_r() * mdExchangeRate), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                valueUsdEty = SLibUtils.round((valueMxn / mdXtaCfdCceExchangeRateUSD), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                
                mdXtaCfdCceTotalUSD = SLibUtils.round(mdXtaCfdCceTotalUSD + valueUsdEty, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
            }
        }
    }
    
    private void calculateDpsTotal(erp.client.SClientInterface piClient_n, int pnDecs) throws SQLException, Exception {
        double dSubtotalProvisional = 0;
        double dDiscountDoc = 0;
        double dSubtotal = 0;
        double dTaxCharged = 0;
        double dTaxRetained = 0;
        double dTotal = 0;
        double dCommissions = 0;
        double dDifference = 0;
        SSessionItem oSessionItem = null;

        mdSubtotalProvisionalCy_r = 0;
        mdDiscountDocCy_r = 0;
        mdSubtotalCy_r = 0;
        mdTaxChargedCy_r = 0;
        mdTaxRetainedCy_r = 0;
        mdTotalCy_r = 0;
        mdCommissionsCy_r = 0;

        if (piClient_n != null) {
            pnDecs = piClient_n.getSessionXXX().getParamsErp().getDecimalsValue();
        }
        else if (pnDecs == 0) {
            pnDecs = 2;
        }

        for (SDataDpsEntry entry : mvDbmsDpsEntries) {
            if (!entry.getIsDeleted()) {
                if (piClient_n != null) {
                    if ((entry.getIsRegistryNew() || entry.getIsRegistryEdited())) {
                        oSessionItem = ((SSessionCustom) piClient_n.getSession().getSessionCustom()).getSessionItem(entry.getFkItemId());

                        if (oSessionItem.getFkUnitAlternativeTypeId() != SDataConstantsSys.ITMU_TP_UNIT_NA && oSessionItem.getUnitAlternativeBaseEquivalence() == 0) {
                            entry.setAuxPreserveQuantity(true);
                        }

                        entry.calculateTotal(piClient_n, mtDate,
                                mnFkTaxIdentityEmisorTypeId, mnFkTaxIdentityReceptorTypeId,
                                mbIsDiscountDocPercentage, mdDiscountDocPercentage, mdExchangeRate);
                    }
                }

                if (entry.isAccountable()) {
                    mdSubtotalProvisionalCy_r += entry.getSubtotalProvisionalCy_r();
                    mdDiscountDocCy_r += entry.getDiscountDocCy();
                    mdSubtotalCy_r += entry.getSubtotalCy_r();
                    mdTaxChargedCy_r += entry.getTaxChargedCy_r();
                    mdTaxRetainedCy_r += entry.getTaxRetainedCy_r();
                    mdTotalCy_r += entry.getTotalCy_r();
                    mdCommissionsCy_r += entry.getCommissionsCy_r();

                    dSubtotalProvisional += entry.getSubtotalProvisional_r();
                    dDiscountDoc += entry.getDiscountDoc();
                    dSubtotal += entry.getSubtotal_r();
                    dTaxCharged += entry.getTaxCharged_r();
                    dTaxRetained += entry.getTaxRetained_r();
                    dTotal += entry.getTotal_r();
                    dCommissions += entry.getCommissions_r();
                }
            }
        }

        /*
         * DOCUMENT'S DOMESTIC CURRENCY VALUE CALCULATION NOTES:
         * Total value of document in domestic currency must be calculated as following,
         * in order to prevent decimal differences due to exchange rate when document
         * was emited in foreign currencies.
         */

        // Total:

        mdTotal_r = SLibUtilities.round(mdTotalCy_r * mdExchangeRate, pnDecs);

        // Taxes:

        mdTaxCharged_r = dTaxCharged;
        mdTaxRetained_r = dTaxRetained;

        // Subtotal:

        mdSubtotal_r = SLibUtilities.round(mdTotal_r - mdTaxCharged_r + mdTaxRetained_r, pnDecs);
        mdDiscountDoc_r = SLibUtilities.round(mdDiscountDocCy_r * mdExchangeRate, pnDecs);
        mdSubtotalProvisional_r = SLibUtilities.round(mdSubtotal_r + mdDiscountDoc_r, pnDecs);

        // Commissions:

        mdCommissions_r = SLibUtilities.round(mdCommissionsCy_r * mdExchangeRate, pnDecs);

        // Adjust any exchange rate difference:

        dDifference = SLibUtilities.round(mdSubtotal_r - dSubtotal, pnDecs);

        if (dDifference != 0) {

            SDataDpsEntry greaterEntry = null;

            // Find greater document entry:

            for (SDataDpsEntry entry : mvDbmsDpsEntries) {
                if (entry.isAccountable()) {
                    if (greaterEntry == null) {
                        greaterEntry = entry;
                    }
                    else {
                        if (entry.getSubtotal_r() > greaterEntry.getSubtotal_r()) {
                            greaterEntry = entry;
                        }
                    }
                }
            }

            // Adjust decimal differences in domestic currency:

            if (greaterEntry != null) {
                greaterEntry.setIsRegistryEdited(true);
                greaterEntry.setSubtotal_r(SLibUtilities.round(greaterEntry.getSubtotal_r() + dDifference, pnDecs));
                greaterEntry.setSubtotalProvisional_r(SLibUtilities.round(greaterEntry.getSubtotalProvisional_r() + dDifference, pnDecs));
            }
        }
    }

    private int getAccSysTypeIdBizPartnerXXX() {
        int id = SLibConsts.UNDEFINED;

        if (isDocumentPur() || isAdjustmentPur()) {
            id = SDataConstantsSys.FINS_TP_ACC_SYS_SUP;
        }
        else if (isDocumentSal() || isAdjustmentSal()) {
            id = SDataConstantsSys.FINS_TP_ACC_SYS_CUS;
        }

        return id;
    }

    private int[] getSysAccTypeKeyBizPartner() {
        int[] key = null;

        if (isDocumentPur() || isAdjustmentPur()) {
            key = SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL;
        }
        else if (isDocumentSal() || isAdjustmentSal()) {
            key = SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL;
        }
        else {
            key = SModSysConsts.FINS_TP_SYS_ACC_NA_NA;
        }

        return key;
    }

    private int[] getSysAccTypeKeyAccountCash(SDataAccountCash cash) {
        int[] key = null;

        switch (cash.getFkAccountCashCategoryId()) {
            case SDataConstantsSys.FINS_CT_ACC_CASH_CASH:
                key = SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH;
                break;
            case SDataConstantsSys.FINS_CT_ACC_CASH_BANK:
                key = SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK;
                break;
            default:
        }

        return key;
    }

    private int[] getSysMvtTypeKeyBizPartnerXXX() {
        int[] key = SDataConstantsSys.FINS_TP_SYS_MOV_NA;

        if (isDocumentPur() || isAdjustmentPur()) {
            key = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP;
        }
        else if (isDocumentSal() || isAdjustmentSal()) {
            key = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS;
        }

        return key;
    }

    private int[] getSysMvtTypeKeyBizPartner() {
        int[] key = SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT;

        if (isDocumentPur()) {
            key = SModSysConsts.FINS_TP_SYS_MOV_PUR;
        }
        else if (isAdjustmentPur()) {
            if (SLibUtils.compareKeys(getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_PUR_CN)) {
                key = SModSysConsts.FINS_TP_SYS_MOV_PUR_DEC;
            }
            else {
                key = SModSysConsts.FINS_TP_SYS_MOV_PUR_INC;
            }
        }
        else if (isDocumentSal()) {
            key = SModSysConsts.FINS_TP_SYS_MOV_SAL;
        }
        else if (isAdjustmentSal()) {
            if (SLibUtils.compareKeys(getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_CN)) {
                key = SModSysConsts.FINS_TP_SYS_MOV_SAL_DEC;
            }
            else {
                key = SModSysConsts.FINS_TP_SYS_MOV_SAL_INC;
            }
        }

        return key;
    }

    private int[] getSysMvtTypeKeyAccountCashXXX(SDataAccountCash cash) {
        int[] key = null;

        switch (cash.getFkAccountCashCategoryId()) {
            case SDataConstantsSys.FINS_CT_ACC_CASH_CASH:
                key = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH;
                break;
            case SDataConstantsSys.FINS_CT_ACC_CASH_BANK:
                key = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK;
                break;
            default:
        }

        return key;
    }

    private int[] getSysMvtTypeKeyAccountCash() {
        int[] key = SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT;

        if (isDocumentPur()) {
            key = SModSysConsts.FINS_TP_SYS_MOV_MO_SUP_ADV;
        }
        else if (isAdjustmentPur()) {
            key = SModSysConsts.FINS_TP_SYS_MOV_MI_SUP_ADV;
        }
        else if (isDocumentSal()) {
            key = SModSysConsts.FINS_TP_SYS_MOV_MI_CUS_ADV;
        }
        else if (isAdjustmentSal()) {
            key = SModSysConsts.FINS_TP_SYS_MOV_MO_CUS_ADV;
        }

        return key;
    }

    private int[] getSysMvtTypeKeyItemXXX() {
        return SDataConstantsSys.FINS_TP_SYS_MOV_NA;
    }

    private int[] getSysMvtTypeKeyItem(final int adjustmentType) {
        int[] key = SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT;

        if (isDocumentPur()) {
            key = SModSysConsts.FINS_TP_SYS_MOV_PUR;
        }
        else if (isAdjustmentPur()) {
            if (SLibUtils.compareKeys(getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_PUR_CN)) {
                key = adjustmentType == SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC ? SModSysConsts.FINS_TP_SYS_MOV_PUR_DEC_DIS : SModSysConsts.FINS_TP_SYS_MOV_PUR_DEC_RET;
            }
            else {
                key = adjustmentType == SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC ? SModSysConsts.FINS_TP_SYS_MOV_PUR_INC_INC : SModSysConsts.FINS_TP_SYS_MOV_PUR_INC_ADD;
            }
        }
        else if (isDocumentSal()) {
            key = SModSysConsts.FINS_TP_SYS_MOV_SAL;
        }
        else if (isAdjustmentSal()) {
            if (SLibUtils.compareKeys(getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_CN)) {
                key = adjustmentType == SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC ? SModSysConsts.FINS_TP_SYS_MOV_SAL_DEC_DIS : SModSysConsts.FINS_TP_SYS_MOV_SAL_DEC_RET;
            }
            else {
                key = adjustmentType == SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC ? SModSysConsts.FINS_TP_SYS_MOV_SAL_INC_INC : SModSysConsts.FINS_TP_SYS_MOV_SAL_INC_ADD;
            }
        }

        return key;
    }

    private int[] getSysMvtTypeKeyTaxXXX(final int taxType, final int taxAppType) {
        int[] key = SDataConstantsSys.FINS_TP_SYS_MOV_NA;

        if (isDocumentPur() || isAdjustmentPur()) {
            if (taxType == SModSysConsts.FINS_TP_TAX_CHARGED) {
                key = taxAppType == SModSysConsts.FINS_TP_TAX_APP_ACCR ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND;
            }
            else {
                key = taxAppType == SModSysConsts.FINS_TP_TAX_APP_ACCR ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND;
            }
        }
        else if (isDocumentSal() || isAdjustmentSal()) {
            if (taxType == SModSysConsts.FINS_TP_TAX_CHARGED) {
                key = taxAppType == SModSysConsts.FINS_TP_TAX_APP_ACCR ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND;
            }
            else {
                key = taxAppType == SModSysConsts.FINS_TP_TAX_APP_ACCR ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND;
            }
        }

        return key;
    }

    private int[] getSysMvtTypeKeyTax(final int taxType, final int taxAppType) {
        int[] key = SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT;

        if (isDocumentPur() || isAdjustmentPur()) {
            if (taxType == SModSysConsts.FINS_TP_TAX_CHARGED) {
                key = taxAppType == SModSysConsts.FINS_TP_TAX_APP_ACCR ? SModSysConsts.FINS_TP_SYS_MOV_PUR_TAX_DBT_PAI : SModSysConsts.FINS_TP_SYS_MOV_PUR_TAX_DBT_PEN;
            }
            else {
                key = taxAppType == SModSysConsts.FINS_TP_TAX_APP_ACCR ? SModSysConsts.FINS_TP_SYS_MOV_PUR_TAX_CDT_PAI : SModSysConsts.FINS_TP_SYS_MOV_PUR_TAX_CDT_PEN;
            }
        }
        else if (isDocumentSal() || isAdjustmentSal()) {
            if (taxType == SModSysConsts.FINS_TP_TAX_CHARGED) {
                key = taxAppType == SModSysConsts.FINS_TP_TAX_APP_ACCR ? SModSysConsts.FINS_TP_SYS_MOV_SAL_TAX_CDT_PAI : SModSysConsts.FINS_TP_SYS_MOV_SAL_TAX_CDT_PEN;
            }
            else {
                key = taxAppType == SModSysConsts.FINS_TP_TAX_APP_ACCR ? SModSysConsts.FINS_TP_SYS_MOV_SAL_TAX_DBT_PAI : SModSysConsts.FINS_TP_SYS_MOV_SAL_TAX_DBT_PEN;
            }
        }

        return key;
    }

    private int getAccItemTypeId(erp.mtrn.data.SDataDpsEntry entry) {
        int id = SDataConstantsSys.UNDEFINED;

        if (isDocumentPur()) {
            id = SDataConstantsSys.FINS_TP_ACC_ITEM_PUR;
        }
        else if (isDocumentSal()) {
            id = SDataConstantsSys.FINS_TP_ACC_ITEM_SAL;
        }
        else if (isAdjustmentPur()) {
            id = entry.getFkDpsAdjustmentTypeId() == SDataConstantsSys.TRNS_TP_DPS_ADJ_RET ? SDataConstantsSys.FINS_TP_ACC_ITEM_PUR_ADJ_DEV : SDataConstantsSys.FINS_TP_ACC_ITEM_PUR_ADJ_DISC;
        }
        else if (isAdjustmentSal()) {
            id = entry.getFkDpsAdjustmentTypeId() == SDataConstantsSys.TRNS_TP_DPS_ADJ_RET ? SDataConstantsSys.FINS_TP_ACC_ITEM_SAL_ADJ_DEV : SDataConstantsSys.FINS_TP_ACC_ITEM_SAL_ADJ_DISC;
        }

        return id;
    }

    /*
     * Public functions
     */

    public int[] getAccMvtSubclassKeyBizPartner() {
        int[] moveSubclassKey = null;

        if (isDocumentPur()) {
            moveSubclassKey = SDataConstantsSys.FINS_CLS_ACC_MOV_PUR;
        }
        else if (isAdjustmentPur()) {
            moveSubclassKey = SDataConstantsSys.FINS_CLS_ACC_MOV_PUR_ADJ_DISC;
        }
        else if (isDocumentSal()) {
            moveSubclassKey = SDataConstantsSys.FINS_CLS_ACC_MOV_SAL;
        }
        else if (isAdjustmentSal()) {
            moveSubclassKey = SDataConstantsSys.FINS_CLS_ACC_MOV_SAL_ADJ_DISC;
        }

        return moveSubclassKey;
    }

    public int[] getAccMvtSubclassKeyBizPartnerAdjustment() {
        int[] moveSubclassKey = null;

        if (isDocumentPur()) {
            moveSubclassKey = SDataConstantsSys.FINS_CLS_ACC_MOV_PUR_ADJ_DISC;
        }
        else if (isDocumentSal()) {
            moveSubclassKey = SDataConstantsSys.FINS_CLS_ACC_MOV_SAL_ADJ_DISC;
        }

        return moveSubclassKey;
    }

    public int[] getAccMvtSubclassKeyAccountCash() {
        int[] moveSubclassKey = null;

        if (isDocumentPur()) {
            moveSubclassKey = SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_OUT_SUP_ADV;
        }
        else if (isAdjustmentPur()) {
            moveSubclassKey = SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_IN_SUP_ADV_REF;
        }
        else if (isDocumentSal()) {
            moveSubclassKey = SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_IN_CUS_ADV;
        }
        else if (isAdjustmentSal()) {
            moveSubclassKey = SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_OUT_CUS_ADV_REF;
        }

        return moveSubclassKey;
    }

    public boolean isForSales() {
        return mnFkDpsCategoryId == SDataConstantsSys.TRNS_CT_DPS_SAL;
    }
    
    public boolean isForPurchases() {
        return mnFkDpsCategoryId == SDataConstantsSys.TRNS_CT_DPS_PUR;
    }
    
    public boolean isDocument() {
        return isDocumentPur() || isDocumentSal();
    }

    public boolean isDocumentPur() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_PUR_DOC);
    }

    public boolean isDocumentSal() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_SAL_DOC);
    }

    public boolean isDocumentOrAdjustment() {
        return isDocument() || isAdjustment();
    }

    public boolean isDocumentOrAdjustmentPur() {
        return isDocumentPur() || isAdjustmentPur();
    }

    public boolean isDocumentOrAdjustmentSal() {
        return isDocumentSal() || isAdjustmentSal();
    }

    public boolean isAdjustment() {
        return isAdjustmentPur() || isAdjustmentSal();
    }

    public boolean isAdjustmentPur() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ);
    }

    public boolean isAdjustmentSal() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ);
    }

    public boolean isOrder() {
        return isOrderPur() || isOrderSal();
    }

    public boolean isOrderPur() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_PUR_ORD);
    }

    public boolean isOrderSal() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_SAL_ORD);
    }

    public boolean isEstimate() {
        return isEstimatePur() || isEstimateSal();
    }

    public boolean isEstimatePur() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_PUR_EST);
    }

    public boolean isEstimateSal() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_SAL_EST);
    }

    public int[] getDpsCategoryKey() { return new int[] { mnFkDpsCategoryId }; }
    public int[] getDpsClassKey() { return new int[] { mnFkDpsCategoryId, mnFkDpsClassId }; }
    public int[] getDpsTypeKey() { return new int[] { mnFkDpsCategoryId, mnFkDpsClassId, mnFkDpsTypeId }; }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setDateDoc(java.util.Date t) { mtDateDoc = t; }
    public void setDateStartCredit(java.util.Date t) { mtDateStartCredit = t; }
    public void setDateShipment_n(java.util.Date t) { mtDateShipment_n = t; }
    public void setDateDelivery_n(java.util.Date t) { mtDateDelivery_n = t; }
    public void setDateDocLapsing_n(java.util.Date t) { mtDateDocLapsing_n = t; }
    public void setDateDocDelivery_n(java.util.Date t) { mtDateDocDelivery_n = t; }
    public void setNumberSeries(java.lang.String s) { msNumberSeries = s; }
    public void setNumber(java.lang.String s) { msNumber = s; }
    public void setNumberReference(java.lang.String s) { msNumberReference = s; }
    public void setCommissionsReference(java.lang.String s) { msCommissionsReference = s; }
    public void setApprovalYear(int n) { mnApprovalYear = n; }
    public void setApprovalNumber(int n) { mnApprovalNumber = n; }
    public void setDaysOfCredit(int n) { mnDaysOfCredit = n; }
    public void setIsDiscountDocApplying(boolean b) { mbIsDiscountDocApplying = b; }
    public void setIsDiscountDocPercentage(boolean b) { mbIsDiscountDocPercentage = b; }
    public void setDiscountDocPercentage(double d) { mdDiscountDocPercentage = d; }
    public void setSubtotalProvisional_r(double d) { mdSubtotalProvisional_r = d; }
    public void setDiscountDoc_r(double d) { mdDiscountDoc_r = d; }
    public void setSubtotal_r(double d) { mdSubtotal_r = d; }
    public void setTaxCharged_r(double d) { mdTaxCharged_r = d; }
    public void setTaxRetained_r(double d) { mdTaxRetained_r = d; }
    public void setTotal_r(double d) { mdTotal_r = d; }
    public void setCommissions_r(double d) { mdCommissions_r = d; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setExchangeRateSystem(double d) { mdExchangeRateSystem = d; }
    public void setSubtotalProvisionalCy_r(double d) { mdSubtotalProvisionalCy_r = d; }
    public void setDiscountDocCy_r(double d) { mdDiscountDocCy_r = d; }
    public void setSubtotalCy_r(double d) { mdSubtotalCy_r = d; }
    public void setTaxChargedCy_r(double d) { mdTaxChargedCy_r = d; }
    public void setTaxRetainedCy_r(double d) { mdTaxRetainedCy_r = d; }
    public void setTotalCy_r(double d) { mdTotalCy_r = d; }
    public void setCommissionsCy_r(double d) { mdCommissionsCy_r = d; }
    public void setDriver(java.lang.String s) { msDriver = s; }
    public void setPlate(java.lang.String s) { msPlate = s; }
    public void setTicket(java.lang.String s) { msTicket = s; }
    public void setShipments(int n) { mnShipments = n; }
    public void setPayments(int n) { mnPayments = n; }
    public void setPaymentMethod(java.lang.String s) { msPaymentMethod = s; }
    public void setPaymentAccount(java.lang.String s) { msPaymentAccount = s; }
    public void setAutomaticAuthorizationRejection(int n) { mnAutomaticAuthorizationRejection = n; }
    public void setIsPublic(boolean b) { mbIsPublic = b; }
    public void setIsLinked(boolean b) { mbIsLinked = b; }
    public void setIsClosed(boolean b) { mbIsClosed = b; }
    public void setIsClosedCommissions(boolean b) { mbIsClosedCommissions = b; }
    public void setIsShipped(boolean b) { mbIsShipped = b; }
    public void setIsRebill(boolean b) { mbIsRebill = b; }
    public void setIsAudited(boolean b) { mbIsAudited = b; }
    public void setIsAuthorized(boolean b) { mbIsAuthorized = b; }
    public void setIsRecordAutomatic(boolean b) { mbIsRecordAutomatic = b; }
    public void setIsCopy(boolean b) { mbIsCopy = b; }
    public void setIsCopied(boolean b) { mbIsCopied = b; }
    public void setIsSystem(boolean b) { mbIsSystem = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkDpsCategoryId(int n) { mnFkDpsCategoryId = n; }
    public void setFkDpsClassId(int n) { mnFkDpsClassId = n; }
    public void setFkDpsTypeId(int n) { mnFkDpsTypeId = n; }
    public void setFkPaymentTypeId(int n) { mnFkPaymentTypeId = n; }
    public void setFkPaymentSystemTypeId(int n) { mnFkPaymentSystemTypeId = n; }
    public void setFkDpsStatusId(int n) { mnFkDpsStatusId = n; }
    public void setFkDpsValidityStatusId(int n) { mnFkDpsValidityStatusId = n; }
    public void setFkDpsAuthorizationStatusId(int n) { mnFkDpsAuthorizationStatusId = n; }
    public void setFkDpsAnnulationTypeId(int n) { mnFkDpsAnnulationTypeId = n; }
    public void setFkDpsNatureId(int n) { mnFkDpsNatureId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkFunctionalAreaId(int n) { mnFkFunctionalAreaId = n; }
    public void setFkBizPartnerId_r(int n) { mnFkBizPartnerId_r = n; }
    public void setFkBizPartnerBranchId(int n) { mnFkBizPartnerBranchId = n; }
    public void setFkBizPartnerBranchAddressId(int n) { mnFkBizPartnerBranchAddressId = n; }
    public void setFkBizPartnerAltId_r(int n) { mnFkBizPartnerAltId_r = n; }
    public void setFkBizPartnerBranchAltId(int n) { mnFkBizPartnerBranchAltId = n; }
    public void setFkBizPartnerBranchAddressAltId(int n) { mnFkBizPartnerBranchAddressAltId = n; }
    public void setFkContactBizPartnerBranchId_n(int n) { mnFkContactBizPartnerBranchId_n = n; }
    public void setFkContactContactId_n(int n) { mnFkContactContactId_n = n; }
    public void setFkTaxIdentityEmisorTypeId(int n) { mnFkTaxIdentityEmisorTypeId = n; }
    public void setFkTaxIdentityReceptorTypeId(int n) { mnFkTaxIdentityReceptorTypeId = n; }
    public void setFkLanguajeId(int n) { mnFkLanguajeId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkSalesAgentId_n(int n) { mnFkSalesAgentId_n = n; }
    public void setFkSalesAgentBizPartnerId_n(int n) { mnFkSalesAgentBizPartnerId_n = n; }
    public void setFkSalesSupervisorId_n(int n) { mnFkSalesSupervisorId_n = n; }
    public void setFkSalesSupervisorBizPartnerId_n(int n) { mnFkSalesSupervisorBizPartnerId_n = n; }
    public void setFkIncotermId(int n) { mnFkIncotermId = n; }
    public void setFkSpotSourceId_n(int n) { mnFkSpotSourceId_n = n; }
    public void setFkSpotDestinyId_n(int n) { mnFkSpotDestinyId_n = n; }
    public void setFkModeOfTransportationTypeId(int n) { mnFkModeOfTransportationTypeId = n; }
    public void setFkCarrierTypeId(int n) { mnFkCarrierTypeId = n; }
    public void setFkCarrierId_n(int n) { mnFkCarrierId_n = n; }
    public void setFkVehicleTypeId_n(int n) { mnFkVehicleTypeId_n = n; }
    public void setFkVehicleId_n(int n) { mnFkVehicleId_n = n; }
    public void setFkSourceYearId_n(int n) { mnFkSourceYearId_n = n; }
    public void setFkSourceDocId_n(int n) { mnFkSourceDocId_n = n; }
    public void setFkMfgYearId_n(int n) { mnFkMfgYearId_n = n; }
    public void setFkMfgOrderId_n(int n) { mnFkMfgOrderId_n = n; }
    public void setFkUserLinkedId(int n) { mnFkUserLinkedId = n; }
    public void setFkUserClosedId(int n) { mnFkUserClosedId = n; }
    public void setFkUserClosedCommissionsId(int n) { mnFkUserClosedCommissionsId = n; }
    public void setFkUserShippedId(int n) { mnFkUserShippedId = n; }
    public void setFkUserAuditedId(int n) { mnFkUserAuditedId = n; }
    public void setFkUserAuthorizedId(int n) { mnFkUserAuthorizedId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserLinkedTs(java.util.Date t) { mtUserLinkedTs = t; }
    public void setUserClosedTs(java.util.Date t) { mtUserClosedTs = t; }
    public void setUserClosedCommissionsTs(java.util.Date t) { mtUserClosedCommissionsTs = t; }
    public void setUserShippedTs(java.util.Date t) { mtUserShippedTs = t; }
    public void setUserAuditedTs(java.util.Date t) { mtUserAuditedTs = t; }
    public void setUserAuthorizedTs(java.util.Date t) { mtUserAuthorizedTs = t; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public java.util.Date getDate() { return mtDate; }
    public java.util.Date getDateDoc() { return mtDateDoc; }
    public java.util.Date getDateStartCredit() { return mtDateStartCredit; }
    public java.util.Date getDateShipment_n() { return mtDateShipment_n; }
    public java.util.Date getDateDelivery_n() { return mtDateDelivery_n; }
    public java.util.Date getDateDocLapsing_n() { return mtDateDocLapsing_n; }
    public java.util.Date getDateDocDelivery_n() { return mtDateDocDelivery_n; }
    public java.lang.String getNumberSeries() { return msNumberSeries; }
    public java.lang.String getNumber() { return msNumber; }
    public java.lang.String getNumberReference() { return msNumberReference; }
    public java.lang.String getCommissionsReference() { return msCommissionsReference; }
    public int getApprovalYear() { return mnApprovalYear; }
    public int getApprovalNumber() { return mnApprovalNumber; }
    public int getDaysOfCredit() { return mnDaysOfCredit; }
    public boolean getIsDiscountDocApplying() { return mbIsDiscountDocApplying; }
    public boolean getIsDiscountDocPercentage() { return mbIsDiscountDocPercentage; }
    public double getDiscountDocPercentage() { return mdDiscountDocPercentage; }
    public double getSubtotalProvisional_r() { return mdSubtotalProvisional_r; }
    public double getDiscountDoc_r() { return mdDiscountDoc_r; }
    public double getSubtotal_r() { return mdSubtotal_r; }
    public double getTaxCharged_r() { return mdTaxCharged_r; }
    public double getTaxRetained_r() { return mdTaxRetained_r; }
    public double getTotal_r() { return mdTotal_r; }
    public double getCommissions_r() { return mdCommissions_r; }
    public double getExchangeRate() { return mdExchangeRate; }
    public double getExchangeRateSystem() { return mdExchangeRateSystem; }
    public double getSubtotalProvisionalCy_r() { return mdSubtotalProvisionalCy_r; }
    public double getDiscountDocCy_r() { return mdDiscountDocCy_r; }
    public double getSubtotalCy_r() { return mdSubtotalCy_r; }
    public double getTaxChargedCy_r() { return mdTaxChargedCy_r; }
    public double getTaxRetainedCy_r() { return mdTaxRetainedCy_r; }
    public double getTotalCy_r() { return mdTotalCy_r; }
    public double getCommissionsCy_r() { return mdCommissionsCy_r; }
    public java.lang.String getDriver() { return msDriver; }
    public java.lang.String getPlate() { return msPlate; }
    public java.lang.String getTicket() { return msTicket; }
    public int getShipments() { return mnShipments; }
    public int getPayments() { return mnPayments; }
    public java.lang.String getPaymentMethod() { return msPaymentMethod; }
    public java.lang.String getPaymentAccount() { return msPaymentAccount; }
    public int getAutomaticAuthorizationRejection() { return mnAutomaticAuthorizationRejection; }
    public boolean getIsPublic() { return mbIsPublic; }
    public boolean getIsLinked() { return mbIsLinked; }
    public boolean getIsClosed() { return mbIsClosed; }
    public boolean getIsClosedCommissions() { return mbIsClosedCommissions; }
    public boolean getIsShipped() { return mbIsShipped; }
    public boolean getIsRebill() { return mbIsRebill; }
    public boolean getIsAudited() { return mbIsAudited; }
    public boolean getIsAuthorized() { return mbIsAuthorized; }
    public boolean getIsRecordAutomatic() { return mbIsRecordAutomatic; }
    public boolean getIsCopy() { return mbIsCopy; }
    public boolean getIsCopied() { return mbIsCopied; }
    public boolean getIsSystem() { return mbIsSystem; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkDpsCategoryId() { return mnFkDpsCategoryId; }
    public int getFkDpsClassId() { return mnFkDpsClassId; }
    public int getFkDpsTypeId() { return mnFkDpsTypeId; }
    public int getFkPaymentTypeId() { return mnFkPaymentTypeId; }
    public int getFkPaymentSystemTypeId() { return mnFkPaymentSystemTypeId; }
    public int getFkDpsStatusId() { return mnFkDpsStatusId; }
    public int getFkDpsValidityStatusId() { return mnFkDpsValidityStatusId; }
    public int getFkDpsAuthorizationStatusId() { return mnFkDpsAuthorizationStatusId; }
    public int getFkDpsAnnulationTypeId() { return mnFkDpsAnnulationTypeId; }
    public int getFkDpsNatureId() { return mnFkDpsNatureId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkFunctionalAreaId() { return mnFkFunctionalAreaId; }
    public int getFkBizPartnerId_r() { return mnFkBizPartnerId_r; }
    public int getFkBizPartnerBranchId() { return mnFkBizPartnerBranchId; }
    public int getFkBizPartnerBranchAddressId() { return mnFkBizPartnerBranchAddressId; }
    public int getFkBizPartnerAltId_r() { return mnFkBizPartnerAltId_r; }
    public int getFkBizPartnerBranchAltId() { return mnFkBizPartnerBranchAltId; }
    public int getFkBizPartnerBranchAddressAltId() { return mnFkBizPartnerBranchAddressAltId; }
    public int getFkContactBizPartnerBranchId_n() { return mnFkContactBizPartnerBranchId_n; }
    public int getFkContactContactId_n() { return mnFkContactContactId_n; }
    public int getFkTaxIdentityEmisorTypeId() { return mnFkTaxIdentityEmisorTypeId; }
    public int getFkTaxIdentityReceptorTypeId() { return mnFkTaxIdentityReceptorTypeId; }
    public int getFkLanguajeId() { return mnFkLanguajeId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkSalesAgentId_n() { return mnFkSalesAgentId_n; }
    public int getFkSalesAgentBizPartnerId_n() { return mnFkSalesAgentBizPartnerId_n; }
    public int getFkSalesSupervisorId_n() { return mnFkSalesSupervisorId_n; }
    public int getFkSalesSupervisorBizPartnerId_n() { return mnFkSalesSupervisorBizPartnerId_n; }
    public int getFkIncotermId() { return mnFkIncotermId; }
    public int getFkSpotSourceId_n() { return mnFkSpotSourceId_n; }
    public int getFkSpotDestinyId_n() { return mnFkSpotDestinyId_n; }
    public int getFkModeOfTransportationTypeId() { return mnFkModeOfTransportationTypeId; }
    public int getFkCarrierTypeId() { return mnFkCarrierTypeId; }
    public int getFkCarrierId_n() { return mnFkCarrierId_n; }
    public int getFkVehicleTypeId_n() { return mnFkVehicleTypeId_n; }
    public int getFkVehicleId_n() { return mnFkVehicleId_n; }
    public int getFkSourceYearId_n() { return mnFkSourceYearId_n; }
    public int getFkSourceDocId_n() { return mnFkSourceDocId_n; }
    public int getFkMfgYearId_n() { return mnFkMfgYearId_n; }
    public int getFkMfgOrderId_n() { return mnFkMfgOrderId_n; }
    public int getFkUserLinkedId() { return mnFkUserLinkedId; }
    public int getFkUserClosedId() { return mnFkUserClosedId; }
    public int getFkUserClosedCommissionsId() { return mnFkUserClosedCommissionsId; }
    public int getFkUserShippedId() { return mnFkUserShippedId; }
    public int getFkUserAuditedId() { return mnFkUserAuditedId; }
    public int getFkUserAuthorizedId() { return mnFkUserAuthorizedId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserLinkedTs() { return mtUserLinkedTs; }
    public java.util.Date getUserClosedTs() { return mtUserClosedTs; }
    public java.util.Date getUserClosedCommissionsTs() { return mtUserClosedCommissionsTs; }
    public java.util.Date getUserShippedTs() { return mtUserShippedTs; }
    public java.util.Date getUserAuditedTs() { return mtUserAuditedTs; }
    public java.util.Date getUserAuthorizedTs() { return mtUserAuthorizedTs; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public java.util.Vector<erp.mtrn.data.SDataDpsEntry> getDbmsDpsEntries() { return mvDbmsDpsEntries; }
    public java.util.Vector<erp.mtrn.data.SDataDpsNotes> getDbmsDpsNotes() { return mvDbmsDpsNotes; }

    public void setDbmsRecordKey(java.lang.Object o) { moDbmsRecordKey = o; }
    public void setDbmsRecordDate(java.util.Date t) { mtDbmsRecordDate = t; }
    public void setDbmsCurrency(java.lang.String s) { msDbmsCurrency = s; }
    public void setDbmsCurrencyKey(java.lang.String s) { msDbmsCurrencyKey = s; }

    public void setAuxIsFormerRecordAutomatic(boolean b) { mbAuxIsFormerRecordAutomatic = b; }
    public void setAuxFormerRecordKey(java.lang.Object o) { moAuxFormerRecordKey = o; }
    public void setAuxCfdParams(erp.mtrn.data.SCfdParams o) { moAuxCfdParams = o; }
    public void setAuxIsNeedCfd(boolean b) { mbAuxIsNeedCfd = b; }
    public void setAuxIsValidate(boolean b) { mbAuxIsValidate = b; }

    public void setDbmsDataBookkeepingNumber(erp.mfin.data.SDataBookkeepingNumber o) { moDbmsDataBookkeepingNumber = o; }
    public void setDbmsDataCfd(erp.mtrn.data.SDataCfd o) { moDbmsDataCfd = o; }
    public void setDbmsDataAddenda(erp.mtrn.data.SDataDpsAddenda o) { moDbmsDataAddenda = o; }
    
    public void setDbmsDataDpsCfd(erp.mtrn.data.SDataDpsCfd o) { moDbmsDataDpsCfd = o; }
    
    public void setXtaCfdConfirmation(String s) { msXtaCfdConfirmation = s; }
    public void setXtaCfdRelationType(String s) { msXtaCfdRelationType = s; }
    public void setXtaCfdUseCfdi(String s) { msXtaCfdUseCfdi = s; }
    
    public void setXtaCfdCceReasonTransfer(java.lang.String s) { msXtaCfdCceReasonTransfer = s; }
    public void setXtaCfdCceOperationType(java.lang.String s) { msXtaCfdCceOperationType = s; }
    public void setXtaCfdCceImportRequest(java.lang.String s) { msXtaCfdCceImportRequest = s; }
    public void setXtaCfdCceCertificateOrigin(int n) { mnXtaCfdCceCertificateOrigin = n; }
    public void setXtaCfdCceCertificateOriginNumber(java.lang.String s) { msXtaCfdCceCertificateOriginNumber = s; }
    public void setXtaCfdCceSubdivisionHas(int n) { mnXtaCfdCceSubdivisionHas = n; }
    public void setXtaCfdCceExchangeRateUSD(double d) { mdXtaCfdCceExchangeRateUSD = d; }
    public void setXtaCfdCceTotalUSD(double d) { mdXtaCfdCceTotalUSD = d; }
    public void setXtaCfdCceNumberExporter(java.lang.String s) { msXtaCfdCceNumberExporter = s; }
    public void setXtaCfdCceIncoterm(java.lang.String s) { msXtaCfdCceIncoterm = s; }

    public java.lang.Object getDbmsRecordKey() { return moDbmsRecordKey; }
    public java.util.Date getDbmsRecordDate() { return mtDbmsRecordDate; }
    public java.lang.String getDbmsCurrency() { return msDbmsCurrency; }
    public java.lang.String getDbmsCurrencyKey() { return msDbmsCurrencyKey; }

    public boolean getAuxIsFormerRecordAutomatic() { return mbAuxIsFormerRecordAutomatic; }
    public java.lang.Object getAuxFormerRecordKey() { return moAuxFormerRecordKey; }
    public erp.mtrn.data.SCfdParams getAuxCfdParams() { return moAuxCfdParams; }
    public boolean getAuxIsNeedCfd() { return mbAuxIsNeedCfd; }
    public boolean getAuxIsValidate() { return mbAuxIsValidate; }

    public erp.mfin.data.SDataBookkeepingNumber getDbmsDataBookkeepingNumber() { return moDbmsDataBookkeepingNumber; }
    public erp.mtrn.data.SDataCfd getDbmsDataCfd() { return moDbmsDataCfd; }
    public erp.mtrn.data.SDataDpsAddenda getDbmsDataAddenda() { return moDbmsDataAddenda; }
    
    public erp.mtrn.data.SDataDpsCfd getDbmsDataDpsCfd() { return moDbmsDataDpsCfd; }
    
    public java.lang.String getXtaCfdConfirmation() { return msXtaCfdConfirmation; }
    public java.lang.String getXtaCfdRelationType() { return msXtaCfdRelationType; }
    public java.lang.String getXtaCfdUseCfdi() { return msXtaCfdUseCfdi; }
    
    public java.lang.String getXtaCfdCceReasonTransfer() { return msXtaCfdCceReasonTransfer; }
    public java.lang.String getXtaCfdCceOperationType() { return msXtaCfdCceOperationType; }
    public java.lang.String getXtaCfdCceImportRequest() { return msXtaCfdCceImportRequest; }
    public int getXtaCfdCceCertificateOrigin() { return mnXtaCfdCceCertificateOrigin; }
    public java.lang.String getXtaCfdCceCertificateOriginNumber() { return msXtaCfdCceCertificateOriginNumber; }
    public int getXtaCfdCceSubdivisionHas() { return mnXtaCfdCceSubdivisionHas; }
    public double getXtaCfdCceExchangeRateUSD() { return mdXtaCfdCceExchangeRateUSD; }
    public double getXtaCfdCceTotalUSD() { return mdXtaCfdCceTotalUSD; }
    public java.lang.String getXtaCfdCceNumberExporter() { return msXtaCfdCceNumberExporter; }
    public java.lang.String getXtaCfdCceIncoterm() { return msXtaCfdCceIncoterm; }

    public erp.mtrn.data.SDataDpsEntry getDbmsDpsEntry(int[] pk) {
        SDataDpsEntry entry = null;

        for (SDataDpsEntry e : mvDbmsDpsEntries) {
            if (SLibUtilities.compareKeys(pk, e.getPrimaryKey())) {
                entry = e;
                break;
            }
        }

        return entry;
    }

    public erp.mtrn.data.SDataDpsNotes getDbmsDpsNotes(int[] pk) {
        SDataDpsNotes notes = null;

        for (SDataDpsNotes n : mvDbmsDpsNotes) {
            if (SLibUtilities.compareKeys(pk, n.getPrimaryKey())) {
                notes = n;
                break;
            }
        }

        return notes;
    }

    public java.lang.String getDpsNumber() {
        return STrnUtils.formatDocNumber(msNumberSeries, msNumber);
    }

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
    public final void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mtDate = null;
        mtDateDoc = null;
        mtDateStartCredit = null;
        mtDateShipment_n = null;
        mtDateDelivery_n = null;
        mtDateDocLapsing_n = null;
        mtDateDocDelivery_n = null;
        msNumberSeries = "";
        msNumber = "";
        msNumberReference = "";
        msCommissionsReference = "";
        mnApprovalYear = 0;
        mnApprovalNumber = 0;
        mnDaysOfCredit = 0;
        mbIsDiscountDocApplying = false;
        mbIsDiscountDocPercentage = false;
        mdDiscountDocPercentage = 0;
        mdSubtotalProvisional_r = 0;
        mdDiscountDoc_r = 0;
        mdSubtotal_r = 0;
        mdTaxCharged_r = 0;
        mdTaxRetained_r = 0;
        mdTotal_r = 0;
        mdCommissions_r = 0;
        mdExchangeRate = 0;
        mdExchangeRateSystem = 0;
        mdSubtotalProvisionalCy_r = 0;
        mdDiscountDocCy_r = 0;
        mdSubtotalCy_r = 0;
        mdTaxChargedCy_r = 0;
        mdTaxRetainedCy_r = 0;
        mdTotalCy_r = 0;
        mdCommissionsCy_r = 0;
        msDriver = "";
        msPlate = "";
        msTicket = "";
        mnShipments = 0;
        mnPayments = 0;
        msPaymentMethod = "";
        msPaymentAccount = "";
        mnAutomaticAuthorizationRejection = 0;
        mbIsPublic = false;
        mbIsLinked = false;
        mbIsClosed = false;
        mbIsClosedCommissions = false;
        mbIsShipped = false;
        mbIsRebill = false;
        mbIsAudited = false;
        mbIsAuthorized = false;
        mbIsRecordAutomatic = false;
        mbIsCopy = false;
        mbIsCopied = false;
        mbIsSystem = false;
        mbIsDeleted = false;
        mnFkDpsCategoryId = 0;
        mnFkDpsClassId = 0;
        mnFkDpsTypeId = 0;
        mnFkPaymentTypeId = 0;
        mnFkPaymentSystemTypeId = 0;
        mnFkDpsStatusId = 0;
        mnFkDpsValidityStatusId = 0;
        mnFkDpsAuthorizationStatusId = 0;
        mnFkDpsAnnulationTypeId = 0;
        mnFkDpsNatureId = 0;
        mnFkCompanyBranchId = 0;
        mnFkFunctionalAreaId = 0;
        mnFkBizPartnerId_r = 0;
        mnFkBizPartnerBranchId = 0;
        mnFkBizPartnerBranchAddressId = 0;
        mnFkBizPartnerAltId_r = 0;
        mnFkBizPartnerBranchAltId = 0;
        mnFkBizPartnerBranchAddressAltId = 0;
        mnFkContactBizPartnerBranchId_n = 0;
        mnFkContactContactId_n = 0;
        mnFkTaxIdentityEmisorTypeId = 0;
        mnFkTaxIdentityReceptorTypeId = 0;
        mnFkLanguajeId = 0;
        mnFkCurrencyId = 0;
        mnFkSalesAgentId_n = 0;
        mnFkSalesAgentBizPartnerId_n = 0;
        mnFkSalesSupervisorId_n = 0;
        mnFkSalesSupervisorBizPartnerId_n = 0;
        mnFkIncotermId = 0;
        mnFkSpotSourceId_n = 0;
        mnFkSpotDestinyId_n = 0;
        mnFkModeOfTransportationTypeId = 0;
        mnFkCarrierTypeId = 0;
        mnFkCarrierId_n = 0;
        mnFkVehicleTypeId_n = 0;
        mnFkVehicleId_n = 0;
        mnFkSourceYearId_n = 0;
        mnFkSourceDocId_n = 0;
        mnFkMfgYearId_n = 0;
        mnFkMfgOrderId_n = 0;
        mnFkUserLinkedId = 0;
        mnFkUserClosedId = 0;
        mnFkUserClosedCommissionsId = 0;
        mnFkUserShippedId = 0;
        mnFkUserAuditedId = 0;
        mnFkUserAuthorizedId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserLinkedTs = null;
        mtUserClosedTs = null;
        mtUserClosedCommissionsTs = null;
        mtUserShippedTs = null;
        mtUserAuditedTs = null;
        mtUserAuthorizedTs = null;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mvDbmsDpsEntries.clear();
        mvDbmsDpsNotes.clear();

        moDbmsRecordKey = null;
        mtDbmsRecordDate = null;
        msDbmsCurrency = "";
        msDbmsCurrencyKey = "";

        mbAuxIsFormerRecordAutomatic = false;
        moAuxFormerRecordKey = null;
        moAuxCfdParams = null;
        mbAuxIsNeedCfd = false;

        moDbmsDataBookkeepingNumber = null;
        moDbmsDataCfd = null;
        moDbmsDataAddenda = null;
        
        moDbmsDataDpsCfd = null;

        msXtaCfdConfirmation = "";
        msXtaCfdRelationType = "";
        msXtaCfdUseCfdi = "";
        
        msXtaCfdCceReasonTransfer = "";
        msXtaCfdCceOperationType = "";
        msXtaCfdCceImportRequest = "";
        mnXtaCfdCceCertificateOrigin = 0;
        msXtaCfdCceCertificateOriginNumber = "";
        mnXtaCfdCceSubdivisionHas = 0;
        mdXtaCfdCceExchangeRateUSD = 0;
        mdXtaCfdCceTotalUSD = 0;
        msXtaCfdCceNumberExporter = "";
        msXtaCfdCceIncoterm = "";
        
        msCfdExpeditionLocality = "";
        msCfdExpeditionState = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] anKey = (int[]) pk;
        int[] anMoveSubclassKey = null;
        String sSql = "";
        String[] asSql = null;
        ResultSet oResultSet = null;
        Statement oStatementAux = null;

        mnLastDbActionResult = SLibConsts.UNDEFINED;
        reset();

        try {
            sSql = "SELECT d.*, c.cur, c.cur_key FROM trn_dps AS d INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                    "WHERE d.id_year = " + anKey[0] + " AND d.id_doc = " + anKey[1] + " ";
            oResultSet = statement.executeQuery(sSql);
            if (!oResultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = oResultSet.getInt("d.id_year");
                mnPkDocId = oResultSet.getInt("d.id_doc");
                mtDate = oResultSet.getDate("d.dt");
                mtDateDoc = oResultSet.getDate("d.dt_doc");
                mtDateStartCredit = oResultSet.getDate("d.dt_start_cred");
                mtDateShipment_n = oResultSet.getDate("d.dt_shipment_n");
                mtDateDelivery_n = oResultSet.getDate("d.dt_delivery_n");
                mtDateDocLapsing_n = oResultSet.getDate("d.dt_doc_lapsing_n");
                mtDateDocDelivery_n = oResultSet.getDate("d.dt_doc_delivery_n");
                msNumberSeries = oResultSet.getString("d.num_ser");
                msNumber = oResultSet.getString("d.num");
                msNumberReference = oResultSet.getString("d.num_ref");
                msCommissionsReference = oResultSet.getString("d.comms_ref");
                mnApprovalYear = oResultSet.getInt("d.approve_year");
                mnApprovalNumber = oResultSet.getInt("d.approve_num");
                mnDaysOfCredit = oResultSet.getInt("d.days_cred");
                mbIsDiscountDocApplying = oResultSet.getBoolean("d.b_disc_doc");
                mbIsDiscountDocPercentage = oResultSet.getBoolean("d.b_disc_doc_per");
                mdDiscountDocPercentage = oResultSet.getDouble("d.disc_doc_per");
                mdSubtotalProvisional_r = oResultSet.getDouble("d.stot_prov_r");
                mdDiscountDoc_r = oResultSet.getDouble("d.disc_doc_r");
                mdSubtotal_r = oResultSet.getDouble("d.stot_r");
                mdTaxCharged_r = oResultSet.getDouble("d.tax_charged_r");
                mdTaxRetained_r = oResultSet.getDouble("d.tax_retained_r");
                mdTotal_r = oResultSet.getDouble("d.tot_r");
                mdCommissions_r = oResultSet.getDouble("d.comms_r");
                mdExchangeRate = oResultSet.getDouble("d.exc_rate");
                mdExchangeRateSystem = oResultSet.getDouble("d.exc_rate_sys");
                mdSubtotalProvisionalCy_r = oResultSet.getDouble("d.stot_prov_cur_r");
                mdDiscountDocCy_r = oResultSet.getDouble("d.disc_doc_cur_r");
                mdSubtotalCy_r = oResultSet.getDouble("d.stot_cur_r");
                mdTaxChargedCy_r = oResultSet.getDouble("d.tax_charged_cur_r");
                mdTaxRetainedCy_r = oResultSet.getDouble("d.tax_retained_cur_r");
                mdTotalCy_r = oResultSet.getDouble("d.tot_cur_r");
                mdCommissionsCy_r = oResultSet.getDouble("d.comms_cur_r");
                msDriver = oResultSet.getString("d.driver");
                msPlate = oResultSet.getString("d.plate");
                msTicket = oResultSet.getString("d.ticket");
                mnShipments = oResultSet.getInt("d.shipments");
                mnPayments = oResultSet.getInt("d.payments");
                msPaymentMethod = oResultSet.getString("d.pay_method");
                msPaymentAccount = oResultSet.getString("d.pay_account");
                //msCfdConfirmationNumber = oResultSet.getString("d.cfd_conf_num");
                mnAutomaticAuthorizationRejection = oResultSet.getInt("d.aut_authorn_rej");
                mbIsPublic = oResultSet.getBoolean("d.b_pub");
                mbIsLinked = oResultSet.getBoolean("d.b_link");
                mbIsClosed = oResultSet.getBoolean("d.b_close");
                mbIsClosedCommissions = oResultSet.getBoolean("d.b_close_comms");
                mbIsShipped = oResultSet.getBoolean("d.b_ship");
                mbIsRebill = oResultSet.getBoolean("b_rebill");
                mbIsAudited = oResultSet.getBoolean("d.b_audit");
                mbIsAuthorized = oResultSet.getBoolean("d.b_authorn");
                mbIsRecordAutomatic = oResultSet.getBoolean("d.b_rec_aut");
                mbIsCopy = oResultSet.getBoolean("d.b_copy");
                mbIsCopied = oResultSet.getBoolean("d.b_copied");
                mbIsSystem = oResultSet.getBoolean("d.b_sys");
                mbIsDeleted = oResultSet.getBoolean("d.b_del");
                mnFkDpsCategoryId = oResultSet.getInt("d.fid_ct_dps");
                mnFkDpsClassId = oResultSet.getInt("d.fid_cl_dps");
                mnFkDpsTypeId = oResultSet.getInt("d.fid_tp_dps");
                mnFkPaymentTypeId = oResultSet.getInt("d.fid_tp_pay");
                mnFkPaymentSystemTypeId = oResultSet.getInt("d.fid_tp_pay_sys");
                mnFkDpsStatusId = oResultSet.getInt("d.fid_st_dps");
                mnFkDpsValidityStatusId = oResultSet.getInt("d.fid_st_dps_val");
                mnFkDpsAuthorizationStatusId = oResultSet.getInt("d.fid_st_dps_authorn");
                mnFkDpsAnnulationTypeId = oResultSet.getInt("fid_tp_dps_ann");
                mnFkDpsNatureId = oResultSet.getInt("d.fid_dps_nat");
                mnFkCompanyBranchId = oResultSet.getInt("d.fid_cob");
                mnFkFunctionalAreaId = oResultSet.getInt("d.fid_func");
                mnFkBizPartnerId_r = oResultSet.getInt("d.fid_bp_r");
                mnFkBizPartnerBranchId = oResultSet.getInt("d.fid_bpb");
                mnFkBizPartnerBranchAddressId = oResultSet.getInt("d.fid_add");
                mnFkBizPartnerAltId_r = oResultSet.getInt("d.fid_bp_alt_r");
                mnFkBizPartnerBranchAltId = oResultSet.getInt("d.fid_bpb_alt");
                mnFkBizPartnerBranchAddressAltId = oResultSet.getInt("d.fid_add_alt");
                mnFkContactBizPartnerBranchId_n = oResultSet.getInt("d.fid_con_bpb_n");
                mnFkContactContactId_n = oResultSet.getInt("d.fid_con_con_n");
                mnFkTaxIdentityEmisorTypeId = oResultSet.getInt("d.fid_tp_tax_idy_emir");
                mnFkTaxIdentityReceptorTypeId = oResultSet.getInt("d.fid_tp_tax_idy_recr");
                mnFkLanguajeId = oResultSet.getInt("d.fid_lan");
                mnFkCurrencyId = oResultSet.getInt("d.fid_cur");
                mnFkSalesAgentId_n = oResultSet.getInt("d.fid_sal_agt_n");
                mnFkSalesAgentBizPartnerId_n = oResultSet.getInt("d.fid_sal_agt_bp_n");
                mnFkSalesSupervisorId_n = oResultSet.getInt("d.fid_sal_sup_n");
                mnFkSalesSupervisorBizPartnerId_n = oResultSet.getInt("d.fid_sal_sup_bp_n");
                mnFkIncotermId = oResultSet.getInt("d.fid_inc");
                mnFkSpotSourceId_n = oResultSet.getInt("d.fid_spot_src_n");
                mnFkSpotDestinyId_n = oResultSet.getInt("d.fid_spot_des_n");
                mnFkModeOfTransportationTypeId = oResultSet.getInt("d.fid_tp_mot");
                mnFkCarrierTypeId = oResultSet.getInt("d.fid_tp_car");
                mnFkCarrierId_n = oResultSet.getInt("d.fid_car_n");
                mnFkVehicleTypeId_n = oResultSet.getInt("d.fid_tp_veh_n");
                mnFkVehicleId_n = oResultSet.getInt("d.fid_veh_n");
                mnFkSourceYearId_n = oResultSet.getInt("d.fid_src_year_n");
                mnFkSourceDocId_n = oResultSet.getInt("d.fid_src_doc_n");
                mnFkMfgYearId_n = oResultSet.getInt("d.fid_mfg_year_n");
                mnFkMfgOrderId_n = oResultSet.getInt("d.fid_mfg_ord_n");
                //mnFkCfdRelationType = oResultSet.getInt("d.fid_cfd_tp_rel");
                //mnFkCfdUse = oResultSet.getInt("d.fid_cfd_use");
                mnFkUserLinkedId = oResultSet.getInt("d.fid_usr_link");
                mnFkUserClosedId = oResultSet.getInt("d.fid_usr_close");
                mnFkUserClosedCommissionsId = oResultSet.getInt("d.fid_usr_close_comms");
                mnFkUserShippedId = oResultSet.getInt("d.fid_usr_ship");
                mnFkUserAuditedId = oResultSet.getInt("d.fid_usr_audit");
                mnFkUserAuthorizedId = oResultSet.getInt("d.fid_usr_authorn");
                mnFkUserNewId = oResultSet.getInt("d.fid_usr_new");
                mnFkUserEditId = oResultSet.getInt("d.fid_usr_edit");
                mnFkUserDeleteId = oResultSet.getInt("d.fid_usr_del");
                mtUserLinkedTs = oResultSet.getTimestamp("d.ts_link");
                mtUserClosedTs = oResultSet.getTimestamp("d.ts_close");
                mtUserClosedCommissionsTs = oResultSet.getTimestamp("d.ts_close_comms");
                mtUserShippedTs = oResultSet.getTimestamp("d.ts_ship");
                mtUserAuditedTs = oResultSet.getTimestamp("d.ts_audit");
                mtUserAuthorizedTs = oResultSet.getTimestamp("d.ts_authorn");
                mtUserNewTs = oResultSet.getTimestamp("d.ts_new");
                mtUserEditTs = oResultSet.getTimestamp("d.ts_edit");
                mtUserDeleteTs = oResultSet.getTimestamp("d.ts_del");

                msDbmsCurrency = oResultSet.getString("c.cur");
                msDbmsCurrencyKey = oResultSet.getString("c.cur_key");

                oStatementAux = statement.getConnection().createStatement();

                // Read aswell document notes:

                sSql = "SELECT id_year, id_doc, id_nts FROM trn_dps_nts " +
                        "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " " +
                        "ORDER BY id_year, id_doc, id_nts ";
                oResultSet = statement.executeQuery(sSql);
                while (oResultSet.next()) {
                    SDataDpsNotes note = new SDataDpsNotes();
                    if (note.read(new int[] { oResultSet.getInt("id_year"), oResultSet.getInt("id_doc"), oResultSet.getInt("id_nts") }, oStatementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsDpsNotes.add(note);
                    }
                }

                // Read aswell document entries:

                sSql = "SELECT id_year, id_doc, id_ety FROM trn_dps_ety " +
                        "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " " +
                        "ORDER BY fid_tp_dps_ety = " + SDataConstantsSys.TRNS_TP_DPS_ETY_VIRT + ", sort_pos, id_ety ";
                oResultSet = statement.executeQuery(sSql);
                while (oResultSet.next()) {
                    SDataDpsEntry entry = new SDataDpsEntry();
                    if (entry.read(new int[] { oResultSet.getInt("id_year"), oResultSet.getInt("id_doc"), oResultSet.getInt("id_ety") }, oStatementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsDpsEntries.add(entry);
                    }
                }

                // Check if document class requires an accounting record:

                anMoveSubclassKey = getAccMvtSubclassKeyBizPartner();

                if (anMoveSubclassKey != null) {
                    // Read aswell accounting record:

                    /*
                     * Attempt 1: try to find accounting record directly.
                     * Attempt 2: try to find accounting record only in non-deleted records.
                     * Attempt 3: if needed, try to find accounting record in all records.
                     */

                    asSql = new String[3];

                    // Query for attempt 1:
                    asSql[0] = "SELECT fid_rec_year AS f_year, fid_rec_per AS f_per, fid_rec_bkc AS f_bkc, fid_rec_tp_rec AS f_tp_rec, fid_rec_num AS f_num " +
                            "FROM trn_dps_rec WHERE id_dps_year = " + mnPkYearId + " AND id_dps_doc = " + mnPkDocId + " ";

                    // Query for attempts 2 and 3:
                    sSql = "SELECT DISTINCT id_year AS f_year, id_per AS f_per, id_bkc AS f_bkc, id_tp_rec AS f_tp_rec, id_num AS f_num FROM fin_rec_ety WHERE " +
                            "fid_tp_acc_mov = " + anMoveSubclassKey[0] + " AND fid_cl_acc_mov = " + anMoveSubclassKey[1] + " AND fid_cls_acc_mov = " + anMoveSubclassKey[2] + " AND " +
                            (isDocument() ? "fid_dps_year_n = " + mnPkYearId + " AND fid_dps_doc_n = " + mnPkDocId + " " : "fid_dps_adj_year_n = " + mnPkYearId + " AND fid_dps_adj_doc_n = " + mnPkDocId + " ");
                    asSql[1] = sSql + "AND b_del = 0 ORDER BY id_ety DESC ";
                    asSql[2] = sSql + "ORDER BY id_ety DESC ";

                    for (int i = 0; i < asSql.length; i++) {
                        oResultSet = statement.executeQuery(asSql[i]);

                        if (oResultSet.next()) {
                            moDbmsRecordKey = new Object[5];
                            ((Object[]) moDbmsRecordKey)[0] = oResultSet.getInt("f_year");
                            ((Object[]) moDbmsRecordKey)[1] = oResultSet.getInt("f_per");
                            ((Object[]) moDbmsRecordKey)[2] = oResultSet.getInt("f_bkc");
                            ((Object[]) moDbmsRecordKey)[3] = oResultSet.getString("f_tp_rec");
                            ((Object[]) moDbmsRecordKey)[4] = oResultSet.getInt("f_num");

                            sSql = "SELECT dt FROM fin_rec WHERE id_year = " + ((Object[]) moDbmsRecordKey)[0] + " AND id_per = " + ((Object[]) moDbmsRecordKey)[1] + " AND " +
                                    "id_bkc = " + ((Object[]) moDbmsRecordKey)[2] + " AND id_tp_rec = '" + ((Object[]) moDbmsRecordKey)[3] + "' AND id_num = " + ((Object[]) moDbmsRecordKey)[4] + " ";
                            oResultSet = statement.executeQuery(sSql);
                            if (!oResultSet.next()) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                            }
                            else {
                                mtDbmsRecordDate = oResultSet.getDate("dt");
                                mbAuxIsFormerRecordAutomatic = mbIsRecordAutomatic;
                                moAuxFormerRecordKey = new Object[5];
                                ((Object[]) moAuxFormerRecordKey)[0] = ((Object[]) moDbmsRecordKey)[0];
                                ((Object[]) moAuxFormerRecordKey)[1] = ((Object[]) moDbmsRecordKey)[1];
                                ((Object[]) moAuxFormerRecordKey)[2] = ((Object[]) moDbmsRecordKey)[2];
                                ((Object[]) moAuxFormerRecordKey)[3] = ((Object[]) moDbmsRecordKey)[3];
                                ((Object[]) moAuxFormerRecordKey)[4] = ((Object[]) moDbmsRecordKey)[4];
                            }

                            break; // accounting record was found!
                        }
                    }
                    
                    // Read aswell Bookkeeping Number, if any (Bookkeeping Numbers implemented from SIIE release 3.2 050.03):

                    sSql = "SELECT DISTINCT fid_bkk_year_n, fid_bkk_num_n "
                            + "FROM fin_rec AS r "
                            + "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                            + "INNER JOIN fin_bkk_num AS b ON re.fid_bkk_year_n = b.id_year AND re.fid_bkk_num_n = b.id_num "
                            + "WHERE NOT r.b_del AND NOT re.b_del AND re.fid_tp_acc_mov = " + anMoveSubclassKey[0] + " AND NOT b.b_del AND ";
                    if (isDocument()) {
                        sSql += "re.fid_dps_year_n = " + mnPkYearId + " AND re.fid_dps_doc_n = " + mnPkDocId + " ";
                    }
                    else {
                        sSql += "re.fid_dps_adj_year_n = " + mnPkYearId + " AND re.fid_dps_adj_doc_n = " + mnPkDocId + " ";
                    }
                    oResultSet = statement.executeQuery(sSql);
                    if (oResultSet.next()) {
                        moDbmsDataBookkeepingNumber = new SDataBookkeepingNumber();
                        if (moDbmsDataBookkeepingNumber.read(new int[] { oResultSet.getInt("fid_bkk_year_n"), oResultSet.getInt("fid_bkk_num_n") }, oStatementAux) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                    }
                }

                // Read aswell CFD:

                sSql = "SELECT id_cfd FROM trn_cfd WHERE fid_dps_year_n = " + mnPkYearId + " AND fid_dps_doc_n = " + mnPkDocId + " ";
                oResultSet = statement.executeQuery(sSql);
                if (oResultSet.next()) {
                    moDbmsDataCfd = new SDataCfd();
                    if (moDbmsDataCfd.read(new int[] { oResultSet.getInt("id_cfd") }, oStatementAux)!= SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                }

                // Read aswell CFD's Addenda:

                sSql = "SELECT COUNT(*) FROM trn_dps_add WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
                oResultSet = statement.executeQuery(sSql);
                if (oResultSet.next()) {
                    if (oResultSet.getInt(1) > 0) {
                        moDbmsDataAddenda = new SDataDpsAddenda();
                        if (moDbmsDataAddenda.read(anKey, oStatementAux)!= SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                    }
                }
                
                // Read code incoterm:

                sSql = "SELECT code FROM erp.logs_inc WHERE id_inc = " + mnFkIncotermId + " ";
                oResultSet = statement.executeQuery(sSql);
                if (oResultSet.next()) {
                    msXtaCfdCceIncoterm = oResultSet.getString("code");
                }

                // Read datas auxiliars for CFD:

                sSql = "SELECT COUNT(*) FROM trn_dps_cfd WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
                oResultSet = statement.executeQuery(sSql);
                if (oResultSet.next()) {
                    if (oResultSet.getInt(1) > 0) {
                        moDbmsDataDpsCfd = new SDataDpsCfd();
                        if (moDbmsDataDpsCfd.read(anKey, oStatementAux)!= SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                        else {
                            msXtaCfdConfirmation = moDbmsDataDpsCfd.getCfdConfirmacion();
                            msXtaCfdRelationType = moDbmsDataDpsCfd.getCfdTipoRelacion();
                            msXtaCfdUseCfdi = moDbmsDataDpsCfd.getCfdUsoCfdi();
                            
                            msXtaCfdCceReasonTransfer = moDbmsDataDpsCfd.getCfdCceMotivoTraslado();
                            msXtaCfdCceOperationType = moDbmsDataDpsCfd.getCfdCceTipoOperacion();
                            msXtaCfdCceImportRequest = moDbmsDataDpsCfd.getCfdCceClaveDePedimento();
                            
                            if (!moDbmsDataDpsCfd.getCfdCceCertificadoOrigen().isEmpty()) {
                                mnXtaCfdCceCertificateOrigin = Integer.parseInt(moDbmsDataDpsCfd.getCfdCceCertificadoOrigen());
                            }
                            msXtaCfdCceCertificateOriginNumber = moDbmsDataDpsCfd.getCfdCceNumCertificadoOrigen();
                            
                            if (!moDbmsDataDpsCfd.getCfdCceSubdivision().isEmpty()) {
                                mnXtaCfdCceSubdivisionHas = Integer.parseInt(moDbmsDataDpsCfd.getCfdCceSubdivision());
                            }
                            if (!moDbmsDataDpsCfd.getCfdCceTipoCambioUSD().isEmpty()) {
                                mdXtaCfdCceExchangeRateUSD = Double.parseDouble(moDbmsDataDpsCfd.getCfdCceTipoCambioUSD());
                            }
                            if (!moDbmsDataDpsCfd.getCfdCceTotalUSD().isEmpty()) {
                                mdXtaCfdCceTotalUSD = Double.parseDouble(moDbmsDataDpsCfd.getCfdCceTotalUSD());
                            }
                            msXtaCfdCceNumberExporter = moDbmsDataDpsCfd.getCfdCceNumExportadorConfiable();
                        }
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
        int i = 0;
        int nParam = 0;
        int nSortingPosition = 0;
        int nDecimals = SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits();
        int nTaxTypeId = SLibConsts.UNDEFINED;
        int nTaxAppTypeId = SLibConsts.UNDEFINED;
        int nTaxAccTypeId = SLibConsts.UNDEFINED;
        int nPositionToAdjust = 0;
        double dDebit = 0;
        double dCredit = 0;
        double dDebitCy = 0;
        double dCreditCy = 0;
        double dGreatestAmount = 0;
        boolean isNewRecord = false;
        boolean appliesPrepayment = false;
        boolean appliesAdvanceBilled = false;
        int[] anAccMvtSubclassKey = null;
        int[] anSysAccTypeKeyBpr = null;
        int[] anSysMvtTypeKeyBpr = null;
        int[] anSysMvtTypeKeyBprXXX = null;
        int[] anSysAccTypeKeyItem = null;
        int[] anSysMvtTypeKeyItem = null;
        int[] anSysMvtTypeKeyItemXXX = null;
        int[] anSysAccTypeKeyTax = null;
        int[] anSysMvtTypeKeyTax = null;
        int[] anSysMvtTypeKeyTaxXXX = null;
        String sSql = "";
        String sConcept = "";
        String sConceptAux = "";
        String sConceptEntry = "";
        String sConceptEntryAux = "";
        String sAccountId = "";
        Statement oStatement = null;
        ResultSet oResultSet = null;
        CallableStatement oCallableStatement = null;
        SDataRecord oRecord = null;
        SDataRecordEntry oRecordEntry = null;
        SFinDpsTaxes oDpsTaxes = null;
        SDataAccountCash oAccountCash = null;
        SFinAccountConfig oConfigBprOp = null;
        SFinAccountConfig oConfigBprAdvBill = null;
        SFinAccountConfig oConfigItem = null;
        SFinAmount oAmount = null;
        SFinAmount oAmountBprBal = null;
        SFinAmount oAmountBprPay = null;
        SFinAmounts oAmounts = new SFinAmounts();
        ArrayList<SFinAmount> aAmountEntries = null;

        mnLastDbActionResult = SLibConsts.UNDEFINED;

        try {
            updateAuthorizationStatus(connection);  // applys only for orders and documents

            nParam = 1;
            oCallableStatement = connection.prepareCall(
                    "{ CALL trn_dps_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?) }");
            oCallableStatement.setInt(nParam++, mnPkYearId);
            oCallableStatement.setInt(nParam++, mnPkDocId);
            oCallableStatement.setDate(nParam++, new java.sql.Date(mtDate.getTime()));
            oCallableStatement.setDate(nParam++, new java.sql.Date(mtDateDoc.getTime()));
            oCallableStatement.setDate(nParam++, new java.sql.Date(mtDateStartCredit.getTime()));
            if (mtDateShipment_n != null) oCallableStatement.setDate(nParam++, new java.sql.Date(mtDateShipment_n.getTime())); else oCallableStatement.setNull(nParam++, java.sql.Types.DATE);
            if (mtDateDelivery_n != null) oCallableStatement.setDate(nParam++, new java.sql.Date(mtDateDelivery_n.getTime())); else oCallableStatement.setNull(nParam++, java.sql.Types.DATE);
            if (mtDateDocLapsing_n != null) oCallableStatement.setDate(nParam++, new java.sql.Date(mtDateDocLapsing_n.getTime())); else oCallableStatement.setNull(nParam++, java.sql.Types.DATE);
            if (mtDateDocDelivery_n != null) oCallableStatement.setDate(nParam++, new java.sql.Date(mtDateDocDelivery_n.getTime())); else oCallableStatement.setNull(nParam++, java.sql.Types.DATE);
            oCallableStatement.setString(nParam++, msNumberSeries);
            oCallableStatement.setString(nParam++, msNumber);
            oCallableStatement.setString(nParam++, msNumberReference);
            oCallableStatement.setString(nParam++, msCommissionsReference);
            oCallableStatement.setInt(nParam++, mnApprovalYear);
            oCallableStatement.setInt(nParam++, mnApprovalNumber);
            oCallableStatement.setInt(nParam++, mnDaysOfCredit);
            oCallableStatement.setBoolean(nParam++, mbIsDiscountDocApplying);
            oCallableStatement.setBoolean(nParam++, mbIsDiscountDocPercentage);
            oCallableStatement.setDouble(nParam++, mdDiscountDocPercentage);
            oCallableStatement.setDouble(nParam++, mdSubtotalProvisional_r);
            oCallableStatement.setDouble(nParam++, mdDiscountDoc_r);
            oCallableStatement.setDouble(nParam++, mdSubtotal_r);
            oCallableStatement.setDouble(nParam++, mdTaxCharged_r);
            oCallableStatement.setDouble(nParam++, mdTaxRetained_r);
            oCallableStatement.setDouble(nParam++, mdTotal_r);
            oCallableStatement.setDouble(nParam++, mdCommissions_r);
            oCallableStatement.setDouble(nParam++, mdExchangeRate);
            oCallableStatement.setDouble(nParam++, mdExchangeRateSystem);
            oCallableStatement.setDouble(nParam++, mdSubtotalProvisionalCy_r);
            oCallableStatement.setDouble(nParam++, mdDiscountDocCy_r);
            oCallableStatement.setDouble(nParam++, mdSubtotalCy_r);
            oCallableStatement.setDouble(nParam++, mdTaxChargedCy_r);
            oCallableStatement.setDouble(nParam++, mdTaxRetainedCy_r);
            oCallableStatement.setDouble(nParam++, mdTotalCy_r);
            oCallableStatement.setDouble(nParam++, mdCommissionsCy_r);
            oCallableStatement.setString(nParam++, msDriver);
            oCallableStatement.setString(nParam++, msPlate);
            oCallableStatement.setString(nParam++, msTicket);
            oCallableStatement.setInt(nParam++, mnShipments);
            oCallableStatement.setInt(nParam++, mnPayments);
            oCallableStatement.setString(nParam++, msPaymentMethod);
            oCallableStatement.setString(nParam++, msPaymentAccount);
            oCallableStatement.setInt(nParam++, mnAutomaticAuthorizationRejection);
            oCallableStatement.setBoolean(nParam++, mbIsPublic);
            oCallableStatement.setBoolean(nParam++, mbIsLinked);
            oCallableStatement.setBoolean(nParam++, mbIsClosed);
            oCallableStatement.setBoolean(nParam++, mbIsClosedCommissions);
            oCallableStatement.setBoolean(nParam++, mbIsShipped);
            oCallableStatement.setBoolean(nParam++, mbIsRebill);
            oCallableStatement.setBoolean(nParam++, mbIsAudited);
            oCallableStatement.setBoolean(nParam++, mbIsAuthorized);
            oCallableStatement.setBoolean(nParam++, mbIsRecordAutomatic);
            oCallableStatement.setBoolean(nParam++, mbIsCopy);
            oCallableStatement.setBoolean(nParam++, mbIsCopied);
            oCallableStatement.setBoolean(nParam++, mbIsSystem);
            oCallableStatement.setBoolean(nParam++, mbIsDeleted);
            oCallableStatement.setInt(nParam++, mnFkDpsCategoryId);
            oCallableStatement.setInt(nParam++, mnFkDpsClassId);
            oCallableStatement.setInt(nParam++, mnFkDpsTypeId);
            oCallableStatement.setInt(nParam++, mnFkPaymentTypeId);
            oCallableStatement.setInt(nParam++, mnFkPaymentSystemTypeId);
            oCallableStatement.setInt(nParam++, mnFkDpsStatusId);
            oCallableStatement.setInt(nParam++, mnFkDpsValidityStatusId);
            oCallableStatement.setInt(nParam++, mnFkDpsAuthorizationStatusId);
            oCallableStatement.setInt(nParam++, mnFkDpsAnnulationTypeId);
            oCallableStatement.setInt(nParam++, mnFkDpsNatureId);
            oCallableStatement.setInt(nParam++, mnFkCompanyBranchId);
            oCallableStatement.setInt(nParam++, mnFkFunctionalAreaId);
            oCallableStatement.setInt(nParam++, mnFkBizPartnerId_r);
            oCallableStatement.setInt(nParam++, mnFkBizPartnerBranchId);
            oCallableStatement.setInt(nParam++, mnFkBizPartnerBranchAddressId);
            oCallableStatement.setInt(nParam++, mnFkBizPartnerAltId_r);
            oCallableStatement.setInt(nParam++, mnFkBizPartnerBranchAltId);
            oCallableStatement.setInt(nParam++, mnFkBizPartnerBranchAddressAltId);
            if (mnFkContactBizPartnerBranchId_n > 0) oCallableStatement.setInt(nParam++, mnFkContactBizPartnerBranchId_n); else oCallableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkContactContactId_n > 0) oCallableStatement.setInt(nParam++, mnFkContactContactId_n); else oCallableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            oCallableStatement.setInt(nParam++, mnFkTaxIdentityEmisorTypeId);
            oCallableStatement.setInt(nParam++, mnFkTaxIdentityReceptorTypeId);
            oCallableStatement.setInt(nParam++, mnFkLanguajeId);
            oCallableStatement.setInt(nParam++, mnFkCurrencyId);
            if (mnFkSalesAgentId_n > 0) oCallableStatement.setInt(nParam++, mnFkSalesAgentId_n); else oCallableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkSalesAgentBizPartnerId_n > 0) oCallableStatement.setInt(nParam++, mnFkSalesAgentBizPartnerId_n); else oCallableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkSalesSupervisorId_n > 0) oCallableStatement.setInt(nParam++, mnFkSalesSupervisorId_n); else oCallableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkSalesSupervisorBizPartnerId_n > 0) oCallableStatement.setInt(nParam++, mnFkSalesSupervisorBizPartnerId_n); else oCallableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            oCallableStatement.setInt(nParam++, mnFkIncotermId);
            if (mnFkSpotSourceId_n > 0) oCallableStatement.setInt(nParam++, mnFkSpotSourceId_n); else oCallableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkSpotDestinyId_n > 0) oCallableStatement.setInt(nParam++, mnFkSpotDestinyId_n); else oCallableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            oCallableStatement.setInt(nParam++, mnFkModeOfTransportationTypeId);
            oCallableStatement.setInt(nParam++, mnFkCarrierTypeId);
            if (mnFkCarrierId_n > 0) oCallableStatement.setInt(nParam++, mnFkCarrierId_n); else oCallableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkVehicleTypeId_n > 0) oCallableStatement.setInt(nParam++, mnFkVehicleTypeId_n); else oCallableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkVehicleId_n > 0) oCallableStatement.setInt(nParam++, mnFkVehicleId_n); else oCallableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkSourceYearId_n > 0) oCallableStatement.setInt(nParam++, mnFkSourceYearId_n); else oCallableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkSourceDocId_n > 0) oCallableStatement.setInt(nParam++, mnFkSourceDocId_n); else oCallableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkMfgYearId_n > 0) oCallableStatement.setInt(nParam++, mnFkMfgYearId_n); else oCallableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkMfgOrderId_n > 0) oCallableStatement.setInt(nParam++, mnFkMfgOrderId_n); else oCallableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            oCallableStatement.setInt(nParam++, mnFkUserLinkedId);
            oCallableStatement.setInt(nParam++, mnFkUserClosedId);
            oCallableStatement.setInt(nParam++, mnFkUserClosedCommissionsId);
            oCallableStatement.setInt(nParam++, mnFkUserShippedId);
            oCallableStatement.setInt(nParam++, mnFkUserAuditedId);
            oCallableStatement.setInt(nParam++, mnFkUserAuthorizedId);
            oCallableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            oCallableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            oCallableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            oCallableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            oCallableStatement.execute();

            mnPkDocId = oCallableStatement.getInt(nParam - 3);
            mnDbmsErrorId = oCallableStatement.getInt(nParam - 2);
            msDbmsError = oCallableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                oStatement = connection.createStatement();
                oDpsTaxes = new SFinDpsTaxes(oStatement);

                // 1. Obtain decimals for calculations:

                sSql = "SELECT decs_val FROM erp.cfg_param_erp WHERE id_erp = 1 ";
                oResultSet = oStatement.executeQuery(sSql);
                if (oResultSet.next()) {
                    nDecimals = oResultSet.getInt("decs_val");
                }

                // 2. Save aswell document entries:

                nSortingPosition = 0;

                for (SDataDpsEntry entry : mvDbmsDpsEntries) {
                    if (entry.getIsRegistryNew() || entry.getIsRegistryEdited()) {
                        entry.setPkYearId(mnPkYearId);
                        entry.setPkDocId(mnPkDocId);

                        if (entry.getIsDeleted() || entry.getFkDpsEntryTypeId() == SDataConstantsSys.TRNS_TP_DPS_ETY_VIRT) {
                            entry.setSortingPosition(0);
                        }
                        else {
                            entry.setSortingPosition(++nSortingPosition);
                        }

                        if (entry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // 3. Save aswell document notes:

                for (SDataDpsNotes notes : mvDbmsDpsNotes) {
                    if (notes.getIsRegistryNew() || notes.getIsRegistryEdited()) {
                        notes.setPkYearId(mnPkYearId);
                        notes.setPkDocId(mnPkDocId);

                        if (notes.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // 4. Save aswell journal voucher if this document class requires it:

                anAccMvtSubclassKey = getAccMvtSubclassKeyBizPartner();

                if (anAccMvtSubclassKey != null) {
                    // 4.1 Prepare journal voucher (accounting record):

                    if (mbIsDeleted) {
                        // Delete aswell former journal voucher:

                        if (moAuxFormerRecordKey != null) {
                            // If document had automatic journal voucher, delete it including its header:

                            deleteRecord(moAuxFormerRecordKey, mbAuxIsFormerRecordAutomatic, connection);
                        }
                    }
                    else {
                        // Save aswell journal voucher:

                        if (moDbmsRecordKey == null) {
                            if (!mbIsRecordAutomatic) {
                                // Journal voucher is not automatic:

                                throw new Exception(MSG_ERR_FIN_REC_USR);
                            }
                            else {
                                // Journal voucher is automatic:

                                if (moAuxFormerRecordKey == null || !mbAuxIsFormerRecordAutomatic) {
                                    isNewRecord = true;
                                    moDbmsRecordKey = (Object[]) createAccRecordKey(oStatement);
                                }
                                else {
                                    if (SLibTimeUtilities.isBelongingToPeriod(mtDate, (Integer) ((Object[]) moAuxFormerRecordKey)[0], (Integer) ((Object[]) moAuxFormerRecordKey)[1])) {
                                        moDbmsRecordKey = moAuxFormerRecordKey;
                                    }
                                    else {
                                        isNewRecord = true;
                                        moDbmsRecordKey = (Object[]) createAccRecordKey(oStatement);
                                    }
                                }
                            }
                        }

                        if (moAuxFormerRecordKey != null) {
                            // If document was previously saved, delete former accounting moves, if needed, including its header:

                            deleteRecord(moAuxFormerRecordKey, (mbAuxIsFormerRecordAutomatic && !mbIsRecordAutomatic) || (mbAuxIsFormerRecordAutomatic && isNewRecord), connection);
                        }

                        // 4.2 Save document's journal voucher (accounting record):

                        // 4.2.1 Define journal voucher's concept:

                        sSql = "SELECT code FROM erp.trnu_tp_dps WHERE " +
                                "id_ct_dps = " + mnFkDpsCategoryId + " AND id_cl_dps = " + mnFkDpsClassId + " AND id_tp_dps = " + mnFkDpsTypeId + " ";
                        oResultSet = oStatement.executeQuery(sSql);
                        if (oResultSet.next()) {
                            sConcept += oResultSet.getString("code") + " " + getDpsNumber() + "; ";
                        }
                        else {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }

                        sSql = "SELECT bp_comm FROM erp.bpsu_bp WHERE id_bp = " + mnFkBizPartnerId_r + " ";
                        oResultSet = oStatement.executeQuery(sSql);
                        if (oResultSet.next()) {
                            sConcept += oResultSet.getString("bp_comm");
                        }
                        else {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }

                        for (SDataDpsEntry entry : mvDbmsDpsEntries) {
                            if (!entry.getIsDeleted()) {
                                sConceptEntry = entry.getConcept();
                                break;
                            }
                        }

                        sConceptAux = sConcept;
                        if (!sConceptEntry.isEmpty()) {
                            sConcept += "; " + sConceptEntry;
                        }

                        if (sConcept.length() > 100) {
                            sConcept = sConcept.substring(0, 100 - 3).trim() + "...";
                        }

                        // 4.2.2 Save journal voucher:
                        
                        nSortingPosition = 0;

                        oRecord = new SDataRecord();
                        if (!isNewRecord) {
                            if (oRecord.read(moDbmsRecordKey, oStatement) != SLibConstants.DB_ACTION_READ_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                            }

                            if (oRecord.getIsDeleted()) {
                                oRecord.setIsDeleted(false);
                            }

                            if (mbIsRecordAutomatic) {
                                oRecord.setDate(mtDate);
                                oRecord.setConcept(sConcept);
                            }

                            for (SDataRecordEntry entry : oRecord.getDbmsRecordEntries()) {
                                if (!entry.getIsDeleted()) {
                                    nSortingPosition = entry.getSortingPosition();
                                }
                            }
                        }
                        else {
                            oRecord.setPrimaryKey(moDbmsRecordKey);
                            oRecord.setDate(mtDate);
                            oRecord.setConcept(sConcept);
                            oRecord.setIsAudited(false);
                            oRecord.setIsAuthorized(false);
                            oRecord.setIsSystem(true);
                            oRecord.setIsDeleted(false);
                            oRecord.setFkCompanyBranchId(mnFkCompanyBranchId);
                            oRecord.setFkCompanyBranchId_n(SLibConsts.UNDEFINED);
                            oRecord.setFkAccountCashId_n(SLibConsts.UNDEFINED);

                            if (mbIsRegistryNew) {
                                oRecord.setFkUserNewId(mnFkUserNewId);
                                oRecord.setFkUserEditId(mnFkUserNewId);
                            }
                            else {
                                oRecord.setFkUserNewId(mnFkUserEditId);
                                oRecord.setFkUserEditId(mnFkUserEditId);
                            }
                        }
                        
                        if (moDbmsDataBookkeepingNumber == null || moDbmsDataBookkeepingNumber.getIsDeleted()) {
                            moDbmsDataBookkeepingNumber = new SDataBookkeepingNumber();
                            moDbmsDataBookkeepingNumber.setPkYearId(mnPkYearId);
                            //moDbmsDataBookkeepingNumber.setPkNumberId(...); will be set on save
                            moDbmsDataBookkeepingNumber.setFkUserNewId(mnFkUserNewId);
                            moDbmsDataBookkeepingNumber.save(connection);
                        }

                        // 4.3 Business partner's asset or liability:
                        
                        // Add values:
                        
                        for (SDataDpsEntry entry : mvDbmsDpsEntries) {
                            if (entry.isAccountable()) {
                                if (isDocument()) { // document is an invoice
                                    if (entry.getIsPrepayment()) {
                                        appliesPrepayment = true;
                                        
                                        if (entry.getQuantity() >= 0) {
                                            if (entry.getKeyCashAccount_n() != null) {
                                                // Increment cash account's balance:
                                                
                                                oAmounts.addAmountForCashAccount(entry.getKeyCashAccount_n(), new SFinAmount(entry.getTotal_r(), entry.getTotalCy_r(), true, SFinAmountType.CASH_ACCOUNT, SFinMovement.INCREMENT)); // add grouping by referenced cash account
                                            }
                                            else {
                                                // Increment advance billed's balance:
                                                
                                                appliesAdvanceBilled = true;
                                                oAmounts.addAmountForAdvanceBilled(new SFinAmount(entry.getTotal_r(), entry.getTotalCy_r(), true, SFinAmountType.ADVANCE_BILLED, SFinMovement.INCREMENT)); // add into lonely group
                                            }
                                        }
                                        else {
                                            // Decrement business partner's balance:
                                            
                                            if (oAmountBprPay == null) {
                                                oAmounts.getAmounts().add(oAmountBprPay = new SFinAmount(0, 0, true, SFinAmountType.UNDEFINED, SFinMovement.DECREMENT)); // in-line instantiation!
                                            }

                                            oAmountBprPay.addAmount(-entry.getTotal_r(), -entry.getTotalCy_r()); // only one amount per business partner
                                        }
                                    }
                                    else {
                                        // Increment business partner's balance:
                                        
                                        if (oAmountBprBal == null) {
                                            oAmounts.getAmounts().add(oAmountBprBal = new SFinAmount(0, 0)); // in-line instantiation!
                                        }
                                        
                                        oAmountBprBal.addAmount(entry.getTotal_r(), entry.getTotalCy_r()); // only one amount per business partner
                                    }
                                }
                                else { // document is a credit note
                                    // Decrement a business partner document's balance:

                                    oAmounts.addAmountForDocument(entry.getKeyAuxDps(), new SFinAmount(entry.getTotal_r(), entry.getTotalCy_r())); // add grouping by referenced invoice
                                }
                            }
                        }
                        
                        // Validate final sum of added values, adjust difference if necessary:
                        
                        oAmounts.checkAmount(mdTotal_r);
                        
                        // Create journal voucher entries:
                        
                        oConfigBprOp = new SFinAccountConfig(SFinAccountUtilities.obtainBizPartnerAccountConfigs(
                                mnFkBizPartnerId_r, STrnUtils.getBizPartnerCategoryId(mnFkDpsCategoryId), oRecord.getPkBookkeepingCenterId(), 
                                mtDate, SDataConstantsSys.FINS_TP_ACC_BP_OP, isDebitForBizPartner(), oStatement));

                        if (appliesAdvanceBilled) {
                            oConfigBprAdvBill = new SFinAccountConfig(SFinAccountUtilities.obtainBizPartnerAccountConfigs(
                                    mnFkBizPartnerId_r, STrnUtils.getBizPartnerCategoryId(mnFkDpsCategoryId), oRecord.getPkBookkeepingCenterId(), 
                                    mtDate, SDataConstantsSys.FINS_TP_ACC_BP_ADV_BILL, isDebitForBizPartner(), oStatement));
                        }

                        anSysAccTypeKeyBpr = getSysAccTypeKeyBizPartner();
                        anSysMvtTypeKeyBpr = getSysMvtTypeKeyBizPartner();
                        anSysMvtTypeKeyBprXXX = getSysMvtTypeKeyBizPartnerXXX();
                        
                        for (SFinAmount amount : oAmounts.getAmounts()) {
                            if (amount.IsPrepayment && amount.Movement == SFinMovement.INCREMENT) {
                                switch (amount.AmountType) {
                                    case CASH_ACCOUNT: // user requested prepayment accounting into a cash account
                                        oAccountCash = new SDataAccountCash();
                                        oAccountCash.read(amount.KeyRefCashAccount, oStatement);

                                        oRecordEntry = createAccRecordEntry(
                                                oAccountCash.getFkAccountId(),
                                                "",
                                                getAccMvtSubclassKeyAccountCash(), getSysAccTypeKeyAccountCash(oAccountCash), getSysMvtTypeKeyAccountCash(), getSysMvtTypeKeyAccountCashXXX(oAccountCash),
                                                null, amount.KeyRefCashAccount);

                                        if (isDebitForBizPartner()) {
                                            oRecordEntry.setDebit(amount.Amount);
                                            oRecordEntry.setCredit(0);
                                            oRecordEntry.setDebitCy(amount.AmountCy);
                                            oRecordEntry.setCreditCy(0);
                                        }
                                        else {
                                            oRecordEntry.setDebit(0);
                                            oRecordEntry.setCredit(amount.Amount);
                                            oRecordEntry.setDebitCy(0);
                                            oRecordEntry.setCreditCy(amount.AmountCy);
                                        }

                                        oRecordEntry.setConcept(sConcept);
                                        oRecordEntry.setSortingPosition(++nSortingPosition);

                                        oRecord.getDbmsRecordEntries().add(oRecordEntry);
                                        break;
                                        
                                    case ADVANCE_BILLED: // user requested prepayment accounting into business partner pending advance payments
                                        aAmountEntries = oConfigBprAdvBill.prorateAmount(amount);
                                        
                                        for (i = 0; i < oConfigBprAdvBill.getAccountConfigEntries().size(); i++) {
                                            oRecordEntry = createAccRecordEntry(
                                                    oConfigBprAdvBill.getAccountConfigEntries().get(i).getAccountId(),
                                                    oConfigBprAdvBill.getAccountConfigEntries().get(i).getCostCenterId(),
                                                    anAccMvtSubclassKey, SModSysConsts.FINS_TP_SYS_ACC_NA_NA, anSysMvtTypeKeyBpr, SDataConstantsSys.FINS_TP_SYS_MOV_NA,
                                                    null, null);
                                            
                                            if (isDebitForBizPartner()) {
                                                oRecordEntry.setDebit(aAmountEntries.get(i).Amount);
                                                oRecordEntry.setCredit(0);
                                                oRecordEntry.setDebitCy(aAmountEntries.get(i).AmountCy);
                                                oRecordEntry.setCreditCy(0);
                                            }
                                            else {
                                                oRecordEntry.setDebit(0);
                                                oRecordEntry.setCredit(aAmountEntries.get(i).Amount);
                                                oRecordEntry.setDebitCy(0);
                                                oRecordEntry.setCreditCy(aAmountEntries.get(i).AmountCy);
                                            }

                                            oRecordEntry.setConcept(sConcept);
                                            oRecordEntry.setSortingPosition(++nSortingPosition);

                                            oRecord.getDbmsRecordEntries().add(oRecordEntry);
                                        }
                                        break;
                                        
                                    default:
                                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n(" + TXT_ADV_BILL + ")");
                                }
                            }
                            else {
                                aAmountEntries = oConfigBprOp.prorateAmount(amount);

                                for (i = 0; i < oConfigBprOp.getAccountConfigEntries().size(); i++) {
                                    oRecordEntry = createAccRecordEntry(
                                            oConfigBprOp.getAccountConfigEntries().get(i).getAccountId(),
                                            oConfigBprOp.getAccountConfigEntries().get(i).getCostCenterId(),
                                            anAccMvtSubclassKey, anSysAccTypeKeyBpr, anSysMvtTypeKeyBpr, anSysMvtTypeKeyBprXXX,
                                            isAdjustment() ? amount.KeyRefDocument : null, null);
                                    if (amount.IsPrepayment) { // decrementing prepayment entry:
                                        if (isDebitForBizPartner()) {
                                            oRecordEntry.setDebit(0);
                                            oRecordEntry.setCredit(aAmountEntries.get(i).Amount);
                                            oRecordEntry.setDebitCy(0);
                                            oRecordEntry.setCreditCy(aAmountEntries.get(i).AmountCy);
                                        }
                                        else {
                                            oRecordEntry.setDebit(aAmountEntries.get(i).Amount);
                                            oRecordEntry.setCredit(0);
                                            oRecordEntry.setDebitCy(aAmountEntries.get(i).AmountCy);
                                            oRecordEntry.setCreditCy(0);
                                        }
                                    }
                                    else { // ordinary entry:
                                        if (isDebitForBizPartner()) {
                                            oRecordEntry.setDebit(aAmountEntries.get(i).Amount);
                                            oRecordEntry.setCredit(0);
                                            oRecordEntry.setDebitCy(aAmountEntries.get(i).AmountCy);
                                            oRecordEntry.setCreditCy(0);
                                        }
                                        else {
                                            oRecordEntry.setDebit(0);
                                            oRecordEntry.setCredit(aAmountEntries.get(i).Amount);
                                            oRecordEntry.setDebitCy(0);
                                            oRecordEntry.setCreditCy(aAmountEntries.get(i).AmountCy);
                                        }
                                    }

                                    oRecordEntry.setConcept(sConcept);
                                    oRecordEntry.setSortingPosition(++nSortingPosition);

                                    oRecord.getDbmsRecordEntries().add(oRecordEntry);
                                }
                            }
                        }

                        // 4.4 Purchases or sales:

                        if (appliesPrepayment) { // prevent from reading prepayment configuration when not needed!
                            oConfigBprOp = new SFinAccountConfig(SFinAccountUtilities.obtainBizPartnerAccountConfigs(
                                    mnFkBizPartnerId_r, STrnUtils.getBizPartnerCategoryId(mnFkDpsCategoryId), oRecord.getPkBookkeepingCenterId(), 
                                    mtDate, SDataConstantsSys.FINS_TP_ACC_BP_PAY, isDebitForBizPartner(), oStatement));
                        }

                        for (SDataDpsEntry entry : mvDbmsDpsEntries) {
                            if (entry.isAccountable()) {
                                if (entry.getIsPrepayment()) {
                                    // Document entry for prepayment:
                                    
                                    if (entry.getQuantity() >= 0) {
                                        oAmount = new SFinAmount(entry.getSubtotal_r(), entry.getSubtotalCy_r());
                                    }
                                    else {
                                        oAmount = new SFinAmount(-entry.getSubtotal_r(), -entry.getSubtotalCy_r());
                                    }

                                    aAmountEntries = oConfigBprOp.prorateAmount(oAmount);
                                    
                                    for (i = 0; i < oConfigBprOp.getAccountConfigEntries().size(); i++) {
                                        oRecordEntry = createAccRecordEntry(
                                                oConfigBprOp.getAccountConfigEntries().get(i).getAccountId(),
                                                oConfigBprOp.getAccountConfigEntries().get(i).getCostCenterId(),
                                                anAccMvtSubclassKey, anSysAccTypeKeyBpr, anSysMvtTypeKeyBpr, anSysMvtTypeKeyBprXXX,
                                                null, null);

                                        oRecordEntry.setFkDpsYearId_n(SLibConsts.UNDEFINED);
                                        oRecordEntry.setFkDpsDocId_n(SLibConsts.UNDEFINED);
                                        
                                        if (entry.getQuantity() >= 0) {
                                            if (isDebitForOperations()) {
                                                oRecordEntry.setDebit(aAmountEntries.get(i).Amount);
                                                oRecordEntry.setCredit(0);
                                                oRecordEntry.setDebitCy(aAmountEntries.get(i).AmountCy);
                                                oRecordEntry.setCreditCy(0);
                                            }
                                            else {
                                                oRecordEntry.setDebit(0);
                                                oRecordEntry.setCredit(aAmountEntries.get(i).Amount);
                                                oRecordEntry.setDebitCy(0);
                                                oRecordEntry.setCreditCy(aAmountEntries.get(i).AmountCy);
                                            }
                                        }
                                        else {
                                            if (isDebitForOperations()) {
                                                oRecordEntry.setDebit(0);
                                                oRecordEntry.setCredit(aAmountEntries.get(i).Amount);
                                                oRecordEntry.setDebitCy(0);
                                                oRecordEntry.setCreditCy(aAmountEntries.get(i).AmountCy);
                                            }
                                            else {
                                                oRecordEntry.setDebit(aAmountEntries.get(i).Amount);
                                                oRecordEntry.setCredit(0);
                                                oRecordEntry.setDebitCy(aAmountEntries.get(i).AmountCy);
                                                oRecordEntry.setCreditCy(0);
                                            }
                                        }

                                        oRecordEntry.setConcept(sConcept);
                                        oRecordEntry.setSortingPosition(++nSortingPosition);

                                        oRecord.getDbmsRecordEntries().add(oRecordEntry);
                                    }
                                }
                                else {
                                    // Standard document entry:
                                    
                                    oConfigItem = new SFinAccountConfig(SFinAccountUtilities.obtainItemAccountConfigs(
                                            entry.getFkItemRefId_n() != SLibConsts.UNDEFINED ? entry.getFkItemRefId_n() : entry.getFkItemId(), oRecord.getPkBookkeepingCenterId(), 
                                            mtDate, getAccItemTypeId(entry), isDebitForOperations(), oStatement));

                                    sConceptEntryAux = sConceptAux;
                                    anSysAccTypeKeyItem = SModSysConsts.FINS_TP_SYS_ACC_NA_NA;
                                    anSysMvtTypeKeyItem = getSysMvtTypeKeyItem(entry.getFkDpsAdjustmentTypeId());
                                    anSysMvtTypeKeyItemXXX = getSysMvtTypeKeyItemXXX();

                                    aAmountEntries = oConfigItem.prorateAmount(new SFinAmount(entry.getSubtotal_r(), entry.getSubtotalCy_r()));

                                    for (i = 0; i < oConfigItem.getAccountConfigEntries().size(); i++) {
                                        oRecordEntry = createAccRecordEntry(
                                                oConfigItem.getAccountConfigEntries().get(i).getAccountId(),
                                                !entry.getFkCostCenterId_n().isEmpty() ? entry.getFkCostCenterId_n() : oConfigItem.getAccountConfigEntries().get(i).getCostCenterId(),
                                                anAccMvtSubclassKey, anSysAccTypeKeyItem, anSysMvtTypeKeyItem, anSysMvtTypeKeyItemXXX,
                                                isAdjustment() ? entry.getKeyAuxDps() : null, null);

                                        if (isDebitForOperations()) {
                                            oRecordEntry.setDebit(aAmountEntries.get(i).Amount);
                                            oRecordEntry.setCredit(0);
                                            oRecordEntry.setDebitCy(aAmountEntries.get(i).AmountCy);
                                            oRecordEntry.setCreditCy(0);
                                        }
                                        else {
                                            oRecordEntry.setDebit(0);
                                            oRecordEntry.setCredit(aAmountEntries.get(i).Amount);
                                            oRecordEntry.setDebitCy(0);
                                            oRecordEntry.setCreditCy(aAmountEntries.get(i).AmountCy);
                                        }

                                        sConceptEntryAux += (entry.getConcept().length() <= 0 ? "" : "; " + entry.getConcept());
                                        if (sConceptEntryAux.length() > 100) {
                                            sConceptEntryAux = sConceptEntryAux.substring(0, 100 - 3).trim() + "...";
                                        }

                                        oRecordEntry.setConcept(sConceptEntryAux);
                                        oRecordEntry.setSortingPosition(++nSortingPosition);

                                        if (entry.getFkItemRefId_n() == SLibConsts.UNDEFINED) {
                                            oRecordEntry.setUnits(entry.getOriginalQuantity());
                                            oRecordEntry.setFkItemId_n(entry.getFkItemId());
                                            oRecordEntry.setFkUnitId_n(entry.getFkOriginalUnitId());
                                            oRecordEntry.setFkItemAuxId_n(SLibConsts.UNDEFINED);
                                        }
                                        else {
                                            oRecordEntry.setUnits(0);
                                            oRecordEntry.setFkItemId_n(entry.getFkItemRefId_n());
                                            oRecordEntry.setFkUnitId_n(SModSysConsts.ITMU_UNIT_NA);
                                            oRecordEntry.setFkItemAuxId_n(entry.getFkItemId());
                                        }

                                        oRecord.getDbmsRecordEntries().add(oRecordEntry);
                                    }
                                }
                                
                                for (SDataDpsEntryTax tax : entry.getDbmsEntryTaxes()) {
                                    if (entry.getQuantity() >= 0) {
                                        oAmount = new SFinAmount(tax.getTax(), tax.getTaxCy(), entry.getIsPrepayment(), SFinAmountType.UNDEFINED, SFinMovement.INCREMENT);
                                    }
                                    else {
                                        oAmount = new SFinAmount(-tax.getTax(), -tax.getTaxCy(), entry.getIsPrepayment(), SFinAmountType.UNDEFINED, SFinMovement.DECREMENT);
                                    }
                                    
                                    oDpsTaxes.addTax(isDocument() ? (int[]) getPrimaryKey() : entry.getKeyAuxDps(), tax.getKeyTax(), oAmount);
                                }
                            }
                        }

                        // 4.5 Purchases or sales taxes:
                        
                        for (SFinDpsTaxes.STax tax : oDpsTaxes.getTaxes()) {
                            if (tax.getMovement() == SFinMovement.DECREMENT) {
                                nTaxAccTypeId = SDataConstantsSys.FINX_ACC_PAY_PEND; // decrement occurs when prepayment applied into invoice
                            }
                            else {
                                nTaxAccTypeId = tax.isPrepayment() || tax.getFkTaxApplicationTypeId() == SModSysConsts.FINS_TP_TAX_APP_ACCR ? SDataConstantsSys.FINX_ACC_PAY : SDataConstantsSys.FINX_ACC_PAY_PEND;
                            }
                            
                            sAccountId = SFinAccountUtilities.obtainTaxAccountId(tax.getKeyTax(), mnFkDpsCategoryId, mtDate, nTaxAccTypeId, oStatement);

                            sSql = "SELECT fid_tp_tax, fid_tp_tax_app FROM erp.finu_tax " +
                                    "WHERE id_tax_bas = " + tax.getPkTaxBasicId() + " AND id_tax = " + tax.getPkTaxId() + " ";
                            oResultSet = oStatement.executeQuery(sSql);
                            if (!oResultSet.next()) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                            }
                            else {
                                nTaxTypeId = oResultSet.getInt("fid_tp_tax");
                                nTaxAppTypeId = tax.isPrepayment() && tax.getMovement() == SFinMovement.INCREMENT ? SModSysConsts.FINS_TP_TAX_APP_ACCR : oResultSet.getInt("fid_tp_tax_app");
                            }

                            anSysAccTypeKeyTax = SModSysConsts.FINS_TP_SYS_ACC_NA_NA;
                            anSysMvtTypeKeyTax = getSysMvtTypeKeyTax(nTaxTypeId, nTaxAppTypeId);
                            anSysMvtTypeKeyTaxXXX = getSysMvtTypeKeyTaxXXX(nTaxTypeId, nTaxAppTypeId);

                            oRecordEntry = createAccRecordEntry(
                                    sAccountId, "", anAccMvtSubclassKey, anSysAccTypeKeyTax, anSysMvtTypeKeyTax, anSysMvtTypeKeyTaxXXX,
                                    isAdjustment() ? tax.getKeyDps() : null, null);

                            if (tax.getMovement() == SFinMovement.INCREMENT) {
                                if (isDebitForTaxes(tax.getFkTaxTypeId())) {
                                    oRecordEntry.setDebit(tax.getValue());
                                    oRecordEntry.setCredit(0);
                                    oRecordEntry.setDebitCy(tax.getValueCy());
                                    oRecordEntry.setCreditCy(0);
                                }
                                else {
                                    oRecordEntry.setDebit(0);
                                    oRecordEntry.setCredit(tax.getValue());
                                    oRecordEntry.setDebitCy(0);
                                    oRecordEntry.setCreditCy(tax.getValueCy());
                                }
                            }
                            else { // prepayment applied into invoice
                                if (isDebitForTaxes(tax.getFkTaxTypeId())) {
                                    oRecordEntry.setDebit(0);
                                    oRecordEntry.setCredit(tax.getValue());
                                    oRecordEntry.setDebitCy(0);
                                    oRecordEntry.setCreditCy(tax.getValueCy());
                                }
                                else {
                                    oRecordEntry.setDebit(tax.getValue());
                                    oRecordEntry.setCredit(0);
                                    oRecordEntry.setDebitCy(tax.getValueCy());
                                    oRecordEntry.setCreditCy(0);
                                }
                            }

                            oRecordEntry.setConcept(sConcept);
                            oRecordEntry.setSortingPosition(++nSortingPosition);
                            oRecordEntry.setFkTaxBasicId_n(tax.getPkTaxBasicId());
                            oRecordEntry.setFkTaxId_n(tax.getPkTaxId());

                            oRecord.getDbmsRecordEntries().add(oRecordEntry);
                        }

                        // 4.6 Validate accounting record:

                        dDebit = 0;
                        dCredit = 0;
                        dDebitCy = 0;
                        dCreditCy = 0;
                        nPositionToAdjust = 0;
                        dGreatestAmount = 0;
                        for (i = 0; i < oRecord.getDbmsRecordEntries().size(); i++) {

                            oRecordEntry = oRecord.getDbmsRecordEntries().get(i);
                            if (!oRecordEntry.getIsDeleted()) {
                                dDebit += oRecordEntry.getDebit();
                                dCredit += oRecordEntry.getCredit();
                                dDebitCy += oRecordEntry.getDebitCy();
                                dCreditCy += oRecordEntry.getCreditCy();

                                if (SLibUtils.compareKeys(new int[] { mnFkDpsCategoryId, mnFkDpsClassId }, SDataConstantsSys.TRNS_CL_DPS_SAL_DOC) ||
                                    SLibUtils.compareKeys(new int[] { mnFkDpsCategoryId, mnFkDpsClassId }, SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ)) {
                                    if (oRecordEntry.getDebit() > dGreatestAmount) {

                                        dGreatestAmount = oRecordEntry.getDebit();
                                        nPositionToAdjust = i;
                                    }
                                }
                                else if (SLibUtils.compareKeys(new int[] { mnFkDpsCategoryId, mnFkDpsClassId }, SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ) ||
                                    SLibUtils.compareKeys(new int[] { mnFkDpsCategoryId, mnFkDpsClassId }, SDataConstantsSys.TRNS_CL_DPS_PUR_DOC)) {
                                    if (oRecordEntry.getCredit() > dGreatestAmount) {

                                        dGreatestAmount = oRecordEntry.getCredit();
                                        nPositionToAdjust = i;
                                    }
                                }
                            }
                        }

                        if (dDebit != dCredit) {
                            oRecordEntry = oRecord.getDbmsRecordEntries().get(nPositionToAdjust);
                            if (SLibUtils.compareKeys(new int[] { mnFkDpsCategoryId, mnFkDpsClassId }, SDataConstantsSys.TRNS_CL_DPS_SAL_DOC) ||
                                SLibUtils.compareKeys(new int[] { mnFkDpsCategoryId, mnFkDpsClassId }, SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ)) {

                                if (dDebit > dCredit) {
                                    oRecordEntry.setDebit(SLibUtilities.round(oRecordEntry.getDebit() - (dDebit - dCredit), nDecimals));
                                }
                                else  {
                                    oRecordEntry.setDebit(SLibUtilities.round(oRecordEntry.getDebit() + (dCredit - dDebit), nDecimals));
                                }
                            }
                            else if (SLibUtils.compareKeys(new int[] { mnFkDpsCategoryId, mnFkDpsClassId }, SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ) ||
                                SLibUtils.compareKeys(new int[] { mnFkDpsCategoryId, mnFkDpsClassId }, SDataConstantsSys.TRNS_CL_DPS_PUR_DOC)) {

                                if (dDebit < dCredit) {
                                    oRecordEntry.setCredit(SLibUtilities.round(oRecordEntry.getCredit() - (dCredit - dDebit), nDecimals));
                                }
                                else  {
                                    oRecordEntry.setCredit(SLibUtilities.round(oRecordEntry.getCredit() + (dDebit - dCredit), nDecimals));
                                }
                            }

                            oRecord.getDbmsRecordEntries().set(nPositionToAdjust, oRecordEntry);
                        }

                        // 4.7 Finally, save journal voucher (acounting record):

                        if (oRecord.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                        else {
                            sSql = "DELETE FROM trn_dps_rec WHERE id_dps_year = " + mnPkYearId + " AND id_dps_doc = " + mnPkDocId + " ";
                            oStatement.execute(sSql);

                            sSql = "INSERT INTO trn_dps_rec VALUES (" + mnPkYearId + ", " + mnPkDocId + ", " +
                                    oRecord.getPkYearId() + ", " + oRecord.getPkPeriodId() + ", " + oRecord.getPkBookkeepingCenterId() + ", " +
                                    "'" + oRecord.getPkRecordTypeId() + "', " + oRecord.getPkNumberId() + ") ";
                            oStatement.execute(sSql);

                            moDbmsRecordKey = oRecord.getPrimaryKey();
                            mtDbmsRecordDate = oRecord.getDate();
                        }
                    }
                }

                // 5. If document is deleted, delete aswell its links:

                if (mbIsDeleted) {
                    deleteLinks(oStatement);
                }

                // Save addenda:
                if (moAuxCfdParams != null) {
                    moDbmsDataAddenda = new SDataDpsAddenda();

                    moDbmsDataAddenda.setPkYearId(mnPkYearId);
                    moDbmsDataAddenda.setPkDocId(mnPkDocId);
                    moDbmsDataAddenda.setLorealFolioNotaRecepcion(moAuxCfdParams.getLorealFolioNotaRecepcion());
                    moDbmsDataAddenda.setBachocoSociedad(moAuxCfdParams.getBachocoSociedad());
                    moDbmsDataAddenda.setBachocoOrganizacionCompra(moAuxCfdParams.getBachocoOrganizacionCompra());
                    moDbmsDataAddenda.setBachocoDivision(moAuxCfdParams.getBachocoDivision());
                    moDbmsDataAddenda.setModeloDpsDescripcion(moAuxCfdParams.getModeloDpsDescripcion());
                    moDbmsDataAddenda.setSorianaTienda(moAuxCfdParams.getSorianaTienda());
                    moDbmsDataAddenda.setSorianaEntregaMercancia(moAuxCfdParams.getSorianaEntregaMercancia());
                    moDbmsDataAddenda.setSorianaRemisionFecha(moAuxCfdParams.getSorianaFechaRemision() == null ? mtDate : moAuxCfdParams.getSorianaFechaRemision());
                    moDbmsDataAddenda.setSorianaRemisionFolio(moAuxCfdParams.getSorianaFolioRemision());
                    moDbmsDataAddenda.setSorianaPedidoFolio(moAuxCfdParams.getSorianaFolioPedido());
                    moDbmsDataAddenda.setSorianaBultoTipo(moAuxCfdParams.getSorianaTipoBulto());
                    moDbmsDataAddenda.setSorianaBultoCantidad(moAuxCfdParams.getSorianaCantidadBulto());
                    moDbmsDataAddenda.setSorianaNotaEntradaFolio(moAuxCfdParams.getSorianaNotaEntradaFolio());
                    moDbmsDataAddenda.setCfdAddendaSubtype(moAuxCfdParams.getCfdAddendaSubtype());
                    moDbmsDataAddenda.setFkCfdAddendaTypeId(moAuxCfdParams.getTipoAddenda());

                    moDbmsDataAddenda.getDbmsDpsAddEntries().clear();

                    for (SDataDpsEntry entry : mvDbmsDpsEntries) {
                        SDataDpsAddendaEntry addendaEntry = new SDataDpsAddendaEntry();

                        addendaEntry.setPkYearId(entry.getPkYearId());
                        addendaEntry.setPkDocId(entry.getPkDocId());
                        addendaEntry.setPkEntryId(entry.getPkEntryId());
                        addendaEntry.setBachocoNumeroPosicion(entry.getDbmsDpsAddBachocoNumberPosition());
                        addendaEntry.setBachocoCentro(entry.getDbmsDpsAddBachocoCenter());
                        addendaEntry.setLorealEntryNumber(entry.getDbmsDpsAddLorealEntryNumber());
                        addendaEntry.setSorianaCodigo(entry.getDbmsDpsAddSorianaBarCode());
                        addendaEntry.setElektraOrder(entry.getDbmsDpsAddElektraOrder());
                        addendaEntry.setElektraBarcode(entry.getDbmsDpsAddElektraBarcode());
                        addendaEntry.setElektraCages(entry.getDbmsDpsAddElektraCages());
                        addendaEntry.setElektraCagePriceUnitary(entry.getDbmsDpsAddElektraCagePriceUnitary());
                        addendaEntry.setElektraParts(entry.getDbmsDpsAddElektraParts());
                        addendaEntry.setElektraPartPriceUnitary(entry.getDbmsDpsAddElektraPartPriceUnitary());

                        moDbmsDataAddenda.getDbmsDpsAddEntries().add(addendaEntry);
                    }

                    if (moAuxCfdParams.getTipoAddenda() != SDataConstantsSys.BPSS_TP_CFD_ADD_NA) {
                        if (moDbmsDataAddenda.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                    else {
                        if (moDbmsDataAddenda.delete(connection) != SLibConstants.DB_ACTION_DELETE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_DELETE_DEP);
                        }
                    }
                }

                // Save XML of purchases when provided:
                
                if (moDbmsDataCfd != null && mnFkDpsCategoryId == SDataConstantsSys.TRNS_CT_DPS_PUR) {
                    moDbmsDataCfd.setFkDpsYearId_n(mnPkYearId);
                    moDbmsDataCfd.setFkDpsDocId_n(mnPkDocId);
                    moDbmsDataCfd.setTimestamp(mtDate);

                    if (moDbmsDataCfd.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }
                
                // Save XML auxiliar from CFDI version 3.3 or international trade:
                
                sSql = "DELETE FROM trn_dps_cfd WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
                oStatement.execute(sSql);
                
                /** save registry dependent for management of information additional for CFDI
                 * if msXtaCfdUseCfdi not empty, if is CFDI version 3.3 or
                 * if msXtaCfdCceOperationType not empty, if is CFDI with international trade node
                */
                if (!msXtaCfdUseCfdi.isEmpty() || !msXtaCfdCceOperationType.isEmpty()) {  
                    moDbmsDataDpsCfd = new SDataDpsCfd();
                    
                    moDbmsDataDpsCfd.setPkYearId(mnPkYearId);
                    moDbmsDataDpsCfd.setPkDocId(mnPkDocId);
                    moDbmsDataDpsCfd.setCfdConfirmacion(msXtaCfdConfirmation);
                    moDbmsDataDpsCfd.setCfdTipoRelacion(msXtaCfdRelationType);
                    moDbmsDataDpsCfd.setCfdUsoCfdi(msXtaCfdUseCfdi);
                    
                    moDbmsDataDpsCfd.setCfdCceMotivoTraslado(msXtaCfdCceReasonTransfer);
                    moDbmsDataDpsCfd.setCfdCceTipoOperacion(msXtaCfdCceOperationType);
                    moDbmsDataDpsCfd.setCfdCceClaveDePedimento(msXtaCfdCceImportRequest);
                    moDbmsDataDpsCfd.setCfdCceCertificadoOrigen(mnXtaCfdCceCertificateOrigin + "");
                    moDbmsDataDpsCfd.setCfdCceNumCertificadoOrigen(msXtaCfdCceCertificateOriginNumber);
                    moDbmsDataDpsCfd.setCfdCceSubdivision(mnXtaCfdCceSubdivisionHas + "");
                    moDbmsDataDpsCfd.setCfdCceTipoCambioUSD(mdXtaCfdCceExchangeRateUSD + "");
                    moDbmsDataDpsCfd.setCfdCceTotalUSD(mdXtaCfdCceTotalUSD + "");
                    moDbmsDataDpsCfd.setCfdCceNumExportadorConfiable(msXtaCfdCceNumberExporter) ;

                    if (moDbmsDataDpsCfd.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }

    @Override
    public int canSave(java.sql.Connection connection) {
        int[] keyMoveSubclass = null;
        Object[] keyRecord = null;
        String sql = "";
        String name = "";
        String idAccount = "";
        String accountFormat = "";
        Statement statement = null;
        ResultSet resultSet = null;
        Vector<SFinAccountConfigEntry> accountConfigs = null;
        SFinDpsTaxes finDpsTaxes = null;

        mnLastDbActionResult = SLibConstants.DB_CAN_SAVE_YES;

        // Check if all elements of this DPS have an account for automatic accounting:

        try {
            keyMoveSubclass = getAccMvtSubclassKeyBizPartner();

            if (keyMoveSubclass != null) {
                // Current document needs an accounting record:

                statement = connection.createStatement();
                finDpsTaxes = new SFinDpsTaxes(statement);

                keyRecord = (Object[]) createAccRecordKey(statement);

                // 1. Check business partner's liability or asset:

                try {
                    accountConfigs = SFinAccountUtilities.obtainBizPartnerAccountConfigs(
                            mnFkBizPartnerId_r, STrnUtils.getBizPartnerCategoryId(mnFkDpsCategoryId), (Integer) keyRecord[2], mtDate,
                            SDataConstantsSys.FINS_TP_ACC_BP_OP, isDebitForBizPartner(), statement);
                }
                catch (Exception e) {
                    msDbmsError = MSG_ERR_ACC_UNK_ + "asociado de negocios.\n[" + e + "]";
                    throw new Exception(msDbmsError);
                }

                // 2. Check purchases or sales:

                accountFormat = SFinAccountUtilities.getConfigAccountFormat(statement);   // current account format is an empty account
                
                for (SDataDpsEntry entry : mvDbmsDpsEntries) {
                    if (entry.isAccountable()) {
                        try {
                            if (entry.getFkItemRefId_n() == 0) {
                                name = entry.getConcept();
                            }
                            else {
                                sql = "SELECT item FROM erp.itmu_item WHERE id_item = " + entry.getFkItemRefId_n() + " ";
                                resultSet = statement.executeQuery(sql);
                                if (!resultSet.next()) {
                                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                                }
                                else {
                                    name = resultSet.getString("item");
                                }
                            }

                            accountConfigs = SFinAccountUtilities.obtainItemAccountConfigs(
                                    entry.getFkItemRefId_n() != 0 ? entry.getFkItemRefId_n() : entry.getFkItemId(), (Integer) keyRecord[2], mtDate,
                                    getAccItemTypeId(entry), isDebitForOperations(), statement);

                            for (SFinAccountConfigEntry config : accountConfigs) {
                                idAccount = config.getAccountId();
                                idAccount = idAccount.replaceAll("0", "");
                                idAccount = idAccount.replaceAll("-", "");
                                if (idAccount.length() == 0) {
                                    msDbmsError = MSG_ERR_ACC_EMP_ + "ítem:\n'" + name + "'.";
                                    throw new Exception(msDbmsError);
                                }
                                
                                if (!entry.getIsPrepayment() && SLibUtils.belongsTo(SFinAccountUtilities.getSystemAccountType(connection, SFinAccountUtilities.obtainAccountLedger(config.getAccountId(), accountFormat)), 
                                        new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_SUP, SDataConstantsSys.FINS_TP_ACC_SYS_CUS, SDataConstantsSys.FINS_TP_ACC_SYS_CDR, SDataConstantsSys.FINS_TP_ACC_SYS_DBR })) {
                                    msDbmsError = MSG_ERR_ITM_CFG_ + "ítem:\n'" + name + "' (configuración anticipo).";
                                    throw new Exception(msDbmsError);
                                }
                            }
                        }
                        catch (Exception e) {
                            if (msDbmsError.isEmpty()) {
                                msDbmsError = MSG_ERR_ACC_UNK_ + "ítem:\n'" + name + "'.\n[" + e + "]";
                            }
                            throw new Exception(msDbmsError);
                        }

                        for (SDataDpsEntryTax tax : entry.getDbmsEntryTaxes()) {
                            finDpsTaxes.addTax(isDocument() ? (int[]) getPrimaryKey() : entry.getKeyAuxDps(), tax.getKeyTax(), new SFinAmount(tax.getTax(), tax.getTaxCy()));
                        }
                    }
                }

                // 3. Check purchases or sales taxes:

                for (SFinDpsTaxes.STax tax : finDpsTaxes.getTaxes()) {
                    try {
                        idAccount = SFinAccountUtilities.obtainTaxAccountId(tax.getKeyTax(), mnFkDpsCategoryId, mtDate,
                                tax.getFkTaxApplicationTypeId() == SModSysConsts.FINS_TP_TAX_APP_ACCR ? SDataConstantsSys.FINX_ACC_PAY : SDataConstantsSys.FINX_ACC_PAY_PEND,
                                statement);
                    }
                    catch (Exception e) {
                        // Read tax name:

                        sql = "SELECT tax FROM erp.finu_tax WHERE id_tax_bas = " + tax.getPkTaxBasicId() + " AND id_tax = " + tax.getPkTaxId() + " ";
                        resultSet = statement.executeQuery(sql);
                        if (!resultSet.next()) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                        else {
                            name = resultSet.getString("tax");
                        }

                        msDbmsError = MSG_ERR_ACC_UNK_ + "impuesto:\n'" + name + "'.\n[" + e + "]";
                        throw new Exception(msDbmsError);
                    }
                }
            }
        }
        catch (SQLException exception) {
            mnLastDbActionResult = SLibConstants.DB_CAN_SAVE_NO;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_CAN_SAVE;
            }
            SLibUtilities.printOutException(this, exception);
        }
        catch (Exception exception) {
            mnLastDbActionResult = SLibConstants.DB_CAN_SAVE_NO;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_CAN_SAVE;
            }
            SLibUtilities.printOutException(this, exception);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int canAnnul(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConsts.UNDEFINED;

        try {
            if (testDeletion(connection, "No se puede anular el documento: ", SDbConsts.ACTION_ANNUL)) {
                mnLastDbActionResult = SLibConstants.DB_CAN_ANNUL_YES;
            }
        }
        catch (SQLException exception) {
            mnLastDbActionResult = SLibConstants.DB_CAN_ANNUL_NO;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_CAN_ANNUL;
            }
            SLibUtilities.printOutException(this, exception);
        }
        catch (Exception exception) {
            mnLastDbActionResult = SLibConstants.DB_CAN_ANNUL_NO;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_CAN_ANNUL;
            }
            SLibUtilities.printOutException(this, exception);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int canDelete(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConsts.UNDEFINED;

        try {
            if (testDeletion(connection, "No se puede eliminar el documento: ", SDbConsts.ACTION_DELETE)) {
                mnLastDbActionResult = SLibConstants.DB_CAN_DELETE_YES;
            }
        }
        catch (SQLException exception) {
            mnLastDbActionResult = SLibConstants.DB_CAN_DELETE_NO;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_CAN_DELETE;
            }
            SLibUtilities.printOutException(this, exception);
        }
        catch (Exception exception) {
            mnLastDbActionResult = SLibConstants.DB_CAN_DELETE_NO;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_CAN_DELETE;
            }
            SLibUtilities.printOutException(this, exception);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int annul(java.sql.Connection connection) {
        int nDecs = 2;
        String sSql = "";
        String sMsg = "No se puede anular el documento: ";
        Statement oStatement = null;
        ResultSet oResultSet = null;

        mnLastDbActionResult = SLibConsts.UNDEFINED;

        try {
            oStatement = connection.createStatement();

            if (mbIsRegistryRequestAnnul) {
                // Set DPS as annuled:

                if (testDeletion(connection, sMsg, SDbConsts.ACTION_ANNUL)) {
                    mnFkDpsStatusId = SDataConstantsSys.TRNS_ST_DPS_ANNULED;

                    sSql = "UPDATE trn_dps SET fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " + "fid_tp_dps_ann=" + mnFkDpsAnnulationTypeId + ", " +
                            "stot_prov_r = 0, disc_doc_r = 0, stot_r = 0, tax_charged_r = 0, tax_retained_r = 0, tot_r = 0, " +
                            "stot_prov_cur_r = 0, disc_doc_cur_r = 0, stot_cur_r = 0, tax_charged_cur_r = 0, tax_retained_cur_r = 0, tot_cur_r = 0, " +
                            "fid_usr_edit = " + mnFkUserEditId + ", ts_edit = NOW() " +
                            "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
                    oStatement.execute(sSql);

                    if (moAuxFormerRecordKey != null) {
                        // If document had automatic accounting record, delete it including its header:

                        deleteRecord(moAuxFormerRecordKey, mbAuxIsFormerRecordAutomatic, connection);
                    }

                    deleteLinks(oStatement);

                    if (moDbmsDataCfd != null) {
                        moDbmsDataCfd.annul(connection);
                    }

                    mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_OK;
                }
            }
            else {
                // Revert DPS annulation:

                if (testRevertDeletion(sMsg, SDbConsts.ACTION_ANNUL)) {
                    // 1. Set DPS fields:

                    mnFkDpsStatusId = SDataConstantsSys.TRNS_ST_DPS_EMITED;

                    // 2. Obtain decimals for calculations:

                    sSql = "SELECT decs_val FROM erp.cfg_param_erp WHERE id_erp = 1 ";
                    oResultSet = oStatement.executeQuery(sSql);
                    if (oResultSet.next()) {
                        nDecs = oResultSet.getInt("decs_val");
                    }

                    // 3. Recalculate DPS value:

                    calculateDpsTotal(null, nDecs);

                    // 4. Save DPS again (this will re-create accounting record again):

                    if (save(connection) == SLibConstants.DB_ACTION_SAVE_OK) {
                        mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_OK;
                    }
                }
            }
        }
        catch (SQLException exception) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_ERROR;
            SLibUtilities.printOutException(this, exception);
        }
        catch (Exception exception) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_ERROR;
            SLibUtilities.printOutException(this, exception);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int delete(java.sql.Connection connection) {
        String sSql = "";
        String sMsg = "No se puede eliminar el documento: ";
        Statement oStatement = null;

        mnLastDbActionResult = SLibConsts.UNDEFINED;

        try {
            oStatement = connection.createStatement();

            if (mbIsRegistryRequestDelete) {
                // Set DPS as deleted:

                if (testDeletion(connection, sMsg, SDbConsts.ACTION_DELETE)) {
                    mbIsDeleted = true;

                    sSql = "UPDATE trn_dps SET b_del = 1, fid_usr_del = " + mnFkUserDeleteId + ", ts_del = NOW() " +
                            "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
                    oStatement.execute(sSql);

                    if (moAuxFormerRecordKey != null) {
                        // If document had automatic accounting record, delete it including its header:

                        deleteRecord(moAuxFormerRecordKey, mbAuxIsFormerRecordAutomatic, connection);
                    }

                    deleteLinks(oStatement);
                    clearEntryDeliveryReferences(oStatement);

                    mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_OK;
                }
            }
            else {
                // Revert DPS deletion:

                if (testRevertDeletion(sMsg, SDbConsts.ACTION_DELETE)) {
                    // 1. Set DPS fields:

                    mbIsDeleted = false;

                    // 4. Save DPS again (this will re-create accounting record again):

                    if (save(connection) == SLibConstants.DB_ACTION_SAVE_OK) {
                        mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_OK;
                    }
                    else {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE);
                    }
                }
            }
        }
        catch (SQLException exception) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, exception);
        }
        catch (Exception exception) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, exception);
        }

        return mnLastDbActionResult;
    }

    /**
     * Calculates DPS value.
     * Exchange rates of document and international trade must be set already.
     * @param client ERP Client. Can be null, and each DPS entry is not calculated, original values remains as the original ones.
     */
    public void calculateTotal(erp.client.SClientInterface client) throws SQLException, Exception {
        calculateDpsTotal(client, 0);
        calculateCceTotal();
    }

    /**
     * Resets all members related to accounting record.
     */
    public void resetRecord() {
        moDbmsRecordKey = null;
        mtDbmsRecordDate = null;
        mbAuxIsFormerRecordAutomatic = false;
        moAuxFormerRecordKey = null;
        moDbmsDataBookkeepingNumber = null;
    }

    /**
     * Create addenda for Soriana
     * @return Addenda
     * @throws java.lang.Exception
     */
    private DElementDSCargaRemisionProv computeAddendaSoriana() throws java.lang.Exception {
        int totalItems = 0;
        Date tDate = null;
        GregorianCalendar oGregorianCalendar = null;
        SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int[] anDateDps = SLibTimeUtilities.digestDate(mtDate);
        int[] anDateEdit = SLibTimeUtilities.digestDate(mtUserEditTs);
        Vector<DElementArticulos> vArticulos = new Vector<>();
        DElementDSCargaRemisionProv addendaSoriana = new DElementDSCargaRemisionProv();

        if (SLibUtilities.compareKeys(anDateDps, anDateEdit)) {
            tDate = mtUserEditTs;
        }
        else {
            oGregorianCalendar = new GregorianCalendar();
            oGregorianCalendar.setTime(mtUserEditTs);
            oGregorianCalendar.set(GregorianCalendar.YEAR, anDateDps[0]);
            oGregorianCalendar.set(GregorianCalendar.MONTH, anDateDps[1] - 1);  // January is month 0
            oGregorianCalendar.set(GregorianCalendar.DATE, anDateDps[2]);
            tDate = oGregorianCalendar.getTime();
            oSimpleDateFormat.format(tDate);
        }

        // Create addenda:
        
        totalItems = 0;
        for (SDataDpsEntry entry : mvDbmsDpsEntries) {
            if (entry.isAccountable()) {
                DElementArticulos vItem = new DElementArticulos();

                vItem.getEltProveedor().setValue(moAuxCfdParams.getReceptor().getDbmsCategorySettingsCus().getCompanyKey());
                vItem.getEltRemision().setValue("" + moAuxCfdParams.getSorianaFolioRemision()); // Serie - Folio del XML (Factura)
                vItem.getEltFolio().setValue(moAuxCfdParams.getSorianaFolioPedido()); // Folio del pedido
                vItem.getEltTienda().setValue("" + moAuxCfdParams.getSorianaTienda()); // Catálogo de tiendas de SORIANA
                vItem.getEltCodigoBarras().setValue(entry.getDbmsDpsAddSorianaBarCode()); // Catálogo de artículos SIIE
                vItem.getEltCantCompra().setValue("" + entry.getOriginalQuantity());
                vItem.getEltCostoNeto().setValue("" + entry.getPriceUnitary());
                vItem.getEltIeps().setValue("0.00");
                vItem.getEltIva().setValue("0.00");
                vArticulos.add(vItem);

                totalItems += entry.getOriginalQuantity();
            }
        }

        addendaSoriana.getEltRemision().getEltProveedor().setValue(moAuxCfdParams.getReceptor().getDbmsCategorySettingsCus().getCompanyKey());
        addendaSoriana.getEltRemision().getEltRemision().setValue("" + moAuxCfdParams.getSorianaFolioRemision()); // Serie - Folio del XML (Factura)
        addendaSoriana.getEltRemision().getEltConsecutivo().setValue("0"); // Si se entrega en parcialidades
        addendaSoriana.getEltRemision().getEltFechaRemision().setValue("" + moAuxCfdParams.getSorianaFechaRemision() + "T00:00:00"); // Fecha de XML remisión (Factura)
        addendaSoriana.getEltRemision().getEltTienda().setValue("" + moAuxCfdParams.getSorianaTienda()); // Catálogo de tiendas SORIANA
        addendaSoriana.getEltRemision().getEltTipoMoneda().setValue("" + mnFkCurrencyId); // Catálogo de monedas soriana (1 pesos, 2 dls, 3 euros)
        addendaSoriana.getEltRemision().getEltTipoBulto().setValue("" + moAuxCfdParams.getSorianaTipoBulto()); // Catálogo de tipos de bulto (1 cajas, 2 bolsas)
        addendaSoriana.getEltRemision().getEltEntregaMercancia().setValue("" + moAuxCfdParams.getSorianaEntregaMercancia()); // Catálogo de entrega de mercacías
        addendaSoriana.getEltRemision().getEltCumpleFiscal().setValue("true");
        addendaSoriana.getEltRemision().getEltCantidadBultos().setValue("" + moAuxCfdParams.getSorianaCantidadBulto()); // Cantidad de bultos con la remisión

        addendaSoriana.getEltRemision().getEltSubTotal().setValue("" + mdSubtotal_r); // Importe subtotal de la remisión
        addendaSoriana.getEltRemision().getEltDescuentos().setValue("" + mdDiscountDoc_r); // Aplicación de descuentos aplicados en la remisión
        addendaSoriana.getEltRemision().getEltIeps().setValue("0.00"); // Si no aplica indicar 0
        addendaSoriana.getEltRemision().getEltIva().setValue("0.00"); // Si no aplica indicar 0
        addendaSoriana.getEltRemision().getEltOtrosImpuestos().setValue("0.00"); // Otros imptos ej. retenciones
        addendaSoriana.getEltRemision().getEltTotal().setValue("" + mdTotal_r); // total remisión
        addendaSoriana.getEltRemision().getEltCantidadPedidos().setValue("1"); // Cantidad de pedidos de la remisión
        addendaSoriana.getEltRemision().getEltFechaEntrega().setValue("" + (mtDateDocDelivery_n == null ? mtDate : mtDateDocDelivery_n) + "T00:00:00"); // F. entrega mercancia, sólo cuando entrega es en tienda
        //addendaSoriana.getEltRemision().getEltEmpaqueCajas().setValue("true"); // No está en el XML que genera de forma manual GS
        //addendaSoriana.getEltRemision().getEltEmpaqueTarimas().setValue("true"); // No está en el XML que genera de forma manual GS
        //addendaSoriana.getEltRemision().getEltCantCajasTarimas().setValue("0"); // No está en el XML que genera de forma manual GS
        //addendaSoriana.getEltRemision().getEltCita().setValue("0"); // No está en el XML que genera de forma manual GS

        //addendaSoriana.getEltPedimento().getEltRemision().setValue("A-00047"); // No está en el XML que genera de forma manual GS

        addendaSoriana.getEltPedido().getEltProveedor().setValue(moAuxCfdParams.getReceptor().getDbmsCategorySettingsCus().getCompanyKey());
        addendaSoriana.getEltPedido().getEltRemision().setValue("" + moAuxCfdParams.getSorianaFolioRemision()); // Serie - Folio del XML (Factura)
        addendaSoriana.getEltPedido().getEltFolio().setValue(moAuxCfdParams.getSorianaFolioPedido()); // Folio del pedido
        addendaSoriana.getEltPedido().getEltTienda().setValue("" + moAuxCfdParams.getSorianaTienda()); // Catálogo de tiendas de SORIANA
        addendaSoriana.getEltPedido().getEltCantArticulos().setValue("" + totalItems); // Total de artículos
        
        if (moAuxCfdParams.getCfdAddendaSubtype() == SDataConstantsSys.BPSS_STP_CFD_ADD_SORIANA_EXT) {
            cfd.ext.soriana.DElementPedidoEmitidoProveedor pedidoEmitidoProveedor = new cfd.ext.soriana.DElementPedidoEmitidoProveedor("SI");
            cfd.ext.soriana.DElementFolioNotaEntrada folioNotaEntrada = new DElementFolioNotaEntrada(moAuxCfdParams.getSorianaNotaEntradaFolio());
            
            addendaSoriana.getEltRemision().setEltOpcFolioNotaEntrada(folioNotaEntrada);
            addendaSoriana.getEltPedido().setEltOpcPedidoEmitidoProveedor(pedidoEmitidoProveedor);
        }

        addendaSoriana.getEltArticulos().getEltHijosArticulos().addAll(vArticulos);

        //addendaSoriana.getEltCajas().getEltRemision().setValue("A-00047");

        //addendaSoriana.getEltArticulosCajas().getEltRemision().setValue("A-00047");

        return addendaSoriana;
    }

    /**
     * Create addenda for L'Oreal
     * @param dIvaPct Tax rate applied to DPS
     * @return Addenda
     * @throws java.lang.Exception
     */
    private DElementFacturaInterfactura computeAddendaLoreal(double dIvapct) throws java.lang.Exception {
        int nMoneda = 0;
        int nCondigoPago = 0;
        double dIvaPctCuerpo = 0;
        java.util.Date tDate = null;
        GregorianCalendar oGregorianCalendar = null;
        int[] anDateDps = SLibTimeUtilities.digestDate(mtDate);
        int[] anDateEdit = SLibTimeUtilities.digestDate(mtUserEditTs);
        Vector<DElementCuerpo> vCuerpo = new Vector<>();
        DElementFacturaInterfactura addendaLoreal = new DElementFacturaInterfactura();

        if (SLibUtilities.compareKeys(anDateDps, anDateEdit)) {
            tDate = mtUserEditTs;
        }
        else {
            oGregorianCalendar = new GregorianCalendar();
            oGregorianCalendar.setTime(mtUserEditTs);
            oGregorianCalendar.set(GregorianCalendar.YEAR, anDateDps[0]);
            oGregorianCalendar.set(GregorianCalendar.MONTH, anDateDps[1] - 1);  // January is month 0
            oGregorianCalendar.set(GregorianCalendar.DATE, anDateDps[2]);
            tDate = oGregorianCalendar.getTime();
        }

        nCondigoPago = moAuxCfdParams.getReceptor().getDbmsCategorySettingsCus().getEffectiveDaysOfCredit();

        for (SDataDpsEntry entry : mvDbmsDpsEntries) {
            if (entry.isAccountable()) {
                DElementCuerpo cuerpo = new DElementCuerpo();

                dIvaPctCuerpo = entry.getDbmsEntryTaxes().get(0).getPercentage() * 100;

                if (dIvapct == dIvaPctCuerpo) {
                    cuerpo.getAttPartida().setInteger(entry.getDbmsDpsAddLorealEntryNumber());
                    cuerpo.getAttCantidad().setDouble(entry.getOriginalQuantity());
                    cuerpo.getAttConcepto().setString(entry.getConcept());
                    cuerpo.getAttPUnitario().setDouble(entry.getPriceUnitary());
                    cuerpo.getAttImporte().setDouble(entry.getSubtotal_r());
                    cuerpo.getAttUnidadMedida().setString(entry.getDbmsUnitSymbol().toUpperCase());
                }
                else {
                    cuerpo.getAttPartida().setInteger(entry.getDbmsDpsAddLorealEntryNumber());
                    cuerpo.getAttCantidad().setDouble(entry.getOriginalQuantity());
                    cuerpo.getAttConcepto().setString(entry.getConcept());
                    cuerpo.getAttPUnitario().setDouble(entry.getPriceUnitary());
                    cuerpo.getAttImporte().setDouble(entry.getSubtotal_r());
                    cuerpo.getAttUnidadMedida().setString(entry.getDbmsUnitSymbol().toUpperCase());
                    cuerpo.getAttIva().setDouble(entry.getTaxCharged_r());
                    cuerpo.getAttIVAPCT().setDouble(dIvaPctCuerpo);
                }
                vCuerpo.add(cuerpo);
            }
        }

        switch (mnFkCurrencyId) {
            case 1: // MXN
                nMoneda = DAttributeOptionMoneda.CFD_MXN;
                break;
            case 2: // USD
                nMoneda = DAttributeOptionMoneda.CFD_USD;
                break;
            case 3: // EUR
                nMoneda = DAttributeOptionMoneda.CFD_EUR;
                break;
            default:
                throw new Exception("La moneda debe ser conocida (" + mnFkCurrencyId + ").");
        }

        // Create addenda:

        addendaLoreal.getAttxmlns().setString("https://www.interfactura.com/Schemas/Documentos");
        addendaLoreal.getAttTipoDocumento().setString("Factura");
        addendaLoreal.getAttschemaLocation().setString("https://www.interfactura.com/Schemas/Documentos https://www.interfactura.com/Schemas/Documentos/DocumentoInterfactura.xsd");

        addendaLoreal.getEltEmisor().getAttRefEmisor().setString("0103399");
        addendaLoreal.getEltReceptor().getAttRefReceptor().setString("0101427");

        addendaLoreal.getEltEncabezado().getAttMoneda().setOption(nMoneda);
        addendaLoreal.getEltEncabezado().getAttFolioOrdenCompra().setString(msNumberReference);
        addendaLoreal.getEltEncabezado().getAttFolioNotaRecepcion().setString(moAuxCfdParams.getLorealFolioNotaRecepcion());
        addendaLoreal.getEltEncabezado().getAttFecha().setDatetime(tDate);
        addendaLoreal.getEltEncabezado().getAttCondicionPago().setString("" + nCondigoPago);
        addendaLoreal.getEltEncabezado().getAttIVAPCT().setDouble(dIvapct);
        addendaLoreal.getEltEncabezado().getAttNumProveedor().setString(moAuxCfdParams.getReceptor().getDbmsCategorySettingsCus().getCompanyKey());
        addendaLoreal.getEltEncabezado().getAttCargoTipo().setString("");
        addendaLoreal.getEltEncabezado().getAttSubTotal().setDouble(mdSubtotal_r);
        addendaLoreal.getEltEncabezado().getAttSerie().setString(msNumberSeries);
        addendaLoreal.getEltEncabezado().getAttFolio().setString(msNumber);
        addendaLoreal.getEltEncabezado().getAttIva().setDouble(mdTaxCharged_r);
        addendaLoreal.getEltEncabezado().getAttTotal().setDouble(mdTotal_r);
        addendaLoreal.getEltEncabezado().getAttObservaciones().setString("");

        addendaLoreal.getEltEncabezado().getEltHijosEncabezado().addAll(vCuerpo);

        return addendaLoreal;
    }

    /**
     * Create addenda for Bachoco
     * @param dIvaPct Tax rate applied to DPS
     * @return Addenda
     * @throws java.lang.Exception
     */
    private DElementPayment computeAddendaBachoco(double dIvapct) throws java.lang.Exception {
        int nMoneda = 0;
        String sStatusDoc = "";
        String sEntityType = "";
        String sReceptorNumber = "";
        java.util.Date tDate = null;
        GregorianCalendar oGregorianCalendar = null;
        SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int[] anDateDps = SLibTimeUtilities.digestDate(mtDate);
        int[] anDateEdit = SLibTimeUtilities.digestDate(mtUserEditTs);
        int[] anTypeDoc = {mnFkDpsCategoryId, mnFkDpsClassId, mnFkDpsTypeId};
        Vector<DElementLineItem> vLineItem = new Vector<>();
        DElementPayment addendaBachoco = new DElementPayment();

        if (SLibUtilities.compareKeys(anDateDps, anDateEdit)) {
            tDate = mtUserEditTs;
        }
        else {
            oGregorianCalendar = new GregorianCalendar();
            oGregorianCalendar.setTime(mtUserEditTs);
            oGregorianCalendar.set(GregorianCalendar.YEAR, anDateDps[0]);
            oGregorianCalendar.set(GregorianCalendar.MONTH, anDateDps[1] - 1);  // January is month 0
            oGregorianCalendar.set(GregorianCalendar.DATE, anDateDps[2]);
            tDate = oGregorianCalendar.getTime();
            oSimpleDateFormat.format(tDate);
        }

        switch (mnFkDpsStatusId) {
            case SDataConstantsSys.TRNS_ST_DPS_EMITED:
                sStatusDoc = "ORIGINAL";
                break;
            case SDataConstantsSys.TRNS_ST_DPS_ANNULED:
                sStatusDoc = "DELETE";
                break;
            default:
                break;
        }

        if (SLibUtilities.compareKeys(anTypeDoc, SDataConstantsSys.TRNU_TP_DPS_SAL_INV)) {
            sEntityType = "INVOICE";
        }
        else if (SLibUtilities.compareKeys(anTypeDoc, SDataConstantsSys.TRNU_TP_DPS_SAL_CN)) {
            sEntityType = "CREDIT_NOTE";
        }

        for (int i = 0; i < (10 - moAuxCfdParams.getReceptor().getDbmsCategorySettingsCus().getCompanyKey().length()); i++) {
            sReceptorNumber += "0";
        }
        sReceptorNumber += moAuxCfdParams.getReceptor().getDbmsCategorySettingsCus().getCompanyKey();

        for (SDataDpsEntry entry : mvDbmsDpsEntries) {
            if (entry.isAccountable()) {
                DElementLineItem vItem = new DElementLineItem();

                vItem.getAttType().setString("SimpleinvoiceLineItemType");
                vItem.getAttNumber().setInteger(entry.getDbmsDpsAddBachocoNumberPosition());
                vItem.getEltTradeItemIdentification().getEltGtin().setValue(entry.getDbmsDpsAddBachocoCenter());
                vItem.getEltAditionalQuantity().getAttQuantityType().setString("NUM_CONSUMER_UNITS"); // XXX UNIDAD CONSUMO
                vItem.getEltAditionalQuantity().setValue("" + entry.getOriginalQuantity());
                vItem.getEltNetPrice().getEltAmount().setValue("" + entry.getPriceUnitary());
                vItem.getEltAdditionalInformation().getEltReferenceIdentification().getAttType().setString("ON");
                vItem.getEltAdditionalInformation().getEltReferenceIdentification().setValue("X"); // ORDEN DE INVERSION
                vLineItem.add(vItem);
            }
        }

        switch (mnFkCurrencyId) {
            case 1: // MXN
                nMoneda = DAttributeOptionMoneda.CFD_MXN;
                break;
            case 2: // USD
                nMoneda = DAttributeOptionMoneda.CFD_USD;
                break;
            case 3: // EUR
                nMoneda = DAttributeOptionMoneda.CFD_EUR;
                break;
            default:
                throw new Exception("La moneda debe ser conocida (" + mnFkCurrencyId + ").");
        }

        // Create addenda:

        addendaBachoco.getAttType().setString("SimpleInvoiceType");
        addendaBachoco.getAttContentVersion().setString("1.3.1");
        addendaBachoco.getAttDocStructureVersion().setString("AMC7.1");
        addendaBachoco.getAttDocStatus().setString(sStatusDoc);
        addendaBachoco.getAttDeliveryDate().setDate(tDate);
        addendaBachoco.getEltPaymentIdentification().getEltEntityType().setValue(sEntityType);
        addendaBachoco.getEltPaymentIdentification().getEltCreatorIdentification().setValue(msNumberSeries + msNumber);
        addendaBachoco.getEltSpecialInstruction().getAttCode().setString("PUR");
        addendaBachoco.getEltSpecialInstruction().getEltText().setValue("X");
        addendaBachoco.getEltOrderIdentification().getEltReferenceIdentification().getAttType().setString("ON");
        addendaBachoco.getEltOrderIdentification().getEltReferenceIdentification().setValue(msNumberReference);
        addendaBachoco.getEltDeliveryNote().getEltReferenceIdentification().setValue(moAuxCfdParams.getBachocoDivision());
        addendaBachoco.getEltBuyer().getEltGln().setValue("X");
        addendaBachoco.getEltBuyer().getEltContactInformation().getEltPersonOrDepartmentName().getEltText().setValue(moAuxCfdParams.getBachocoOrganizacionCompra());
        addendaBachoco.getEltSeller().getEltGln().setValue(sReceptorNumber);
        addendaBachoco.getEltShipTo().getEltGln().setValue(moAuxCfdParams.getBachocoSociedad());
        addendaBachoco.getEltInvoiceCreator().getEltAlternatePartyIdentification().getAttType().setString("IA");
        addendaBachoco.getEltInvoiceCreator().getEltAlternatePartyIdentification().setValue(sReceptorNumber);
        addendaBachoco.getEltCustoms().getEltAlternatePartyIdentification().getAttType().setString("TN");
        addendaBachoco.getEltCustoms().getEltAlternatePartyIdentification().setValue("X"); // XXX No. PEDIMENTO
        addendaBachoco.getEltCurrency().getAttCurrencyISOCode().setOption(nMoneda);
        addendaBachoco.getEltCurrency().getEltCurrencyFunction().setValue("BILLING_CURRENCY"); // DIVISA DE FACTURACION
        addendaBachoco.getEltItem().getEltHijosLineItem().addAll(vLineItem);
        addendaBachoco.getEltTotalAmount().getEltAmount().setValue("" + mdSubtotalCy_r);
        addendaBachoco.getEltTax().getAttType().setString("VAT"); // XXX IMPUESTO
        addendaBachoco.getEltTax().getEltTaxPercentage().setValue("" + dIvapct);
        addendaBachoco.getEltTax().getEltTaxAmount().setValue("" + mdTaxCharged_r);
        addendaBachoco.getEltTax().getEltTaxCategory().setValue("TRANSFERIDO"); // XXX TIPO IMPUESTO
        addendaBachoco.getEltPayableAmount().getEltAmount().setValue("" + mdTotal_r);

        return addendaBachoco;
    }

    /**
     * Create addenda for Grupo Modelo
     * @param dIvaPct Tax rate applied to DPS
     * @return Addenda
     * @throws java.lang.Exception
     */
    @SuppressWarnings("empty-statement")
    private cfd.ext.grupomodelo.DElementAddendaModelo computeAddendaGrupoModelo(double dIvapct) throws java.lang.Exception {
        int nMoneda = 0;
        int nQty = 0;
        int nIvaPercentage = 0;
        String sStatusDoc = "";
        String sEntityType = "";
        String sReceptorNumber = "";
        java.util.Date tDate = null;
        GregorianCalendar oGregorianCalendar = null;
        SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat oDecimalFormat = new DecimalFormat("#0.00");
        int[] anDateDps = SLibTimeUtilities.digestDate(mtDate);
        int[] anDateEdit = SLibTimeUtilities.digestDate(mtUserEditTs);
        int[] anTypeDoc = {mnFkDpsCategoryId, mnFkDpsClassId, mnFkDpsTypeId};
        Vector<cfd.ext.grupomodelo.DElementLineItem> vLineItem = new Vector<>();
        cfd.ext.grupomodelo.DElementAddendaModelo addendaGrupoModelo = new cfd.ext.grupomodelo.DElementAddendaModelo();

        if (SLibUtilities.compareKeys(anDateDps, anDateEdit)) {
            tDate = mtUserEditTs;
        }
        else {
            oGregorianCalendar = new GregorianCalendar();
            oGregorianCalendar.setTime(mtUserEditTs);
            oGregorianCalendar.set(GregorianCalendar.YEAR, anDateDps[0]);
            oGregorianCalendar.set(GregorianCalendar.MONTH, anDateDps[1] - 1);  // January is month 0
            oGregorianCalendar.set(GregorianCalendar.DATE, anDateDps[2]);
            tDate = oGregorianCalendar.getTime();
            oSimpleDateFormat.format(tDate);
        }

        switch (mnFkDpsStatusId) {
            case SDataConstantsSys.TRNS_ST_DPS_EMITED:
                sStatusDoc = "ORIGINAL";
                break;
            case SDataConstantsSys.TRNS_ST_DPS_ANNULED:
                sStatusDoc = "DELETE";
                break;
        }

        if (SLibUtilities.compareKeys(anTypeDoc, SDataConstantsSys.TRNU_TP_DPS_SAL_INV)) {
            sEntityType = "FA";
        }

        sReceptorNumber += moAuxCfdParams.getReceptor().getDbmsCategorySettingsCus().getCompanyKey();
        nIvaPercentage = (int) dIvapct;

        for (SDataDpsEntry entry : mvDbmsDpsEntries) {
            if (entry.isAccountable()) {
                nQty = (int) entry.getOriginalQuantity();
                int[] anSubTypeDoc = { entry.getFkDpsAdjustmentTypeId(), entry.getFkDpsAdjustmentSubtypeId() };
                cfd.ext.grupomodelo.DElementLineItem vItem = new cfd.ext.grupomodelo.DElementLineItem();

                vItem.getAttType().setString("SimpleinvoiceLineItemType");
                vItem.getAttNumber().setInteger(entry.getSortingPosition() * 10);
                vItem.getEltTradeItemIdentification().getEltGtin().setValue(entry.getConceptKey());
                vItem.getEltAlternateTradeItemIdentification().getAttType().setString("BUYER_ASSIGNED");
                vItem.getEltAlternateTradeItemIdentification().setValue(sReceptorNumber);
                vItem.getEltTradeItemDescriptionInformation().getAttLanguage().setString("ES");
                vItem.getEltTradeItemDescriptionInformation().getEltLongText().setValue(entry.msConcept.length() > 35 ? entry.msConcept.substring(0, 34) : entry.msConcept);
                vItem.getEltInvoicedQuantity().getAttUnit().setString(entry.getDbmsOriginalUnitSymbol());
                vItem.getEltInvoicedQuantity().setValue("" + nQty);
                vItem.getEltGrossPrice().getEltAmount().setValue("" + oDecimalFormat.format(entry.getPriceUnitaryCy()));
                vItem.getEltNetPrice().getEltAmount().setValue("" + oDecimalFormat.format(entry.getTotalCy_r() / entry.getOriginalQuantity()));
                vItem.getEltAdditionalInformation().getEltReferenceIdentification().getAttType().setString("DQ");
                vItem.getEltAdditionalInformation().getEltReferenceIdentification().setValue(msNumberSeries + msNumber);
                vItem.getEltTradeItemTaxInformation().getEltTaxTypeDescription().setValue("VAT");
                vItem.getEltTradeItemTaxInformation().getEltTaxCategory().setValue("TRANSFERIDO");
                vItem.getEltTradeItemTaxInformation().getEltItemTaxAmount().getEltTaxPercentage().setValue("" + nIvaPercentage);
                vItem.getEltTradeItemTaxInformation().getEltItemTaxAmount().getEltTaxAmount().setValue("" + oDecimalFormat.format(entry.getPriceUnitaryCy() * (dIvapct / 100)));
                vItem.getEltTotalLineAmount().getEltGrossAmount().getEltAmount().setValue("" + oDecimalFormat.format(entry.getSubtotalCy_r()));
                vItem.getEltTotalLineAmount().getEltNetAmount().getEltAmount().setValue("" + oDecimalFormat.format(entry.getTotalCy_r()));
                vLineItem.add(vItem);

                if (SLibUtilities.compareKeys(anSubTypeDoc, SDataConstantsSys.TRNS_STP_DPS_ADJ_RET_RET)) {
                    sEntityType = "ND";
                }
                else if (SLibUtilities.compareKeys(anSubTypeDoc, SDataConstantsSys.TRNS_STP_DPS_ADJ_DISC_PRICE)) {
                    sEntityType = "NA";
                }
                else if (SLibUtilities.compareKeys(anSubTypeDoc, SDataConstantsSys.TRNS_STP_DPS_ADJ_DISC_DISC)) {
                    sEntityType = "NE";
                }
            }
        }

        switch (mnFkCurrencyId) {
            case 1: // MXN
                nMoneda = DAttributeOptionMoneda.CFD_MXN;
                break;
            case 2: // USD
                nMoneda = DAttributeOptionMoneda.CFD_USD;
                break;
            case 3: // EUR
                nMoneda = DAttributeOptionMoneda.CFD_EUR;
                break;
            default:
                throw new Exception("La moneda debe ser conocida (" + mnFkCurrencyId + ").");
        }

        // Create addenda:

        addendaGrupoModelo.getAttXmlns().setString("http://www.gmodelo.com.mx/CFD/Addenda/Receptor");
        addendaGrupoModelo.getAttSchemaLocation().setString("http://www.gmodelo.com.mx/CFD/Addenda/Receptor https://femodelo.gmodelo.com/Addenda/ADDENDAMODELO.xsd");

        addendaGrupoModelo.getEltPayment().getAttType().setString("SimpleInvoiceType");
        addendaGrupoModelo.getEltPayment().getAttContentVersion().setString("1.3.1");
        addendaGrupoModelo.getEltPayment().getAttDocStructureVersion().setString("AMC7.1");
        addendaGrupoModelo.getEltPayment().getAttDocStatus().setString(sStatusDoc);
        addendaGrupoModelo.getEltPayment().getEltPaymentIdentification().getEltEntityType().setValue(sEntityType);
        addendaGrupoModelo.getEltPayment().getEltPaymentIdentification().getEltCreatorIdentification().setValue(msNumberSeries + msNumber);
        addendaGrupoModelo.getEltPayment().getEltSpecialInstruction().getAttCode().setString("PUR");
        addendaGrupoModelo.getEltPayment().getEltSpecialInstruction().getEltText().setValue(moAuxCfdParams.getModeloDpsDescripcion());
        addendaGrupoModelo.getEltPayment().getEltOrderIdentification().getEltReferenceIdentification().getAttType().setString("ON");
        addendaGrupoModelo.getEltPayment().getEltOrderIdentification().getEltReferenceIdentification().setValue(msNumberReference);
        addendaGrupoModelo.getEltPayment().getEltOrderIdentification().getEltReferenceDate().setValue(oSimpleDateFormat.format(tDate));
        addendaGrupoModelo.getEltPayment().getEltBuyer().getEltGln().setValue("7504001186019");
        addendaGrupoModelo.getEltPayment().getEltBuyer().getEltContactInformation().getEltPersonOrDepartmentName().getEltText().setValue(moAuxCfdParams.getBachocoSociedad());
        addendaGrupoModelo.getEltPayment().getEltInvoiceCreator().getEltGln().setValue("7500000000000");
        addendaGrupoModelo.getEltPayment().getEltInvoiceCreator().getEltAlternatePartyIdentification().getAttType().setString("IA");
        addendaGrupoModelo.getEltPayment().getEltInvoiceCreator().getEltAlternatePartyIdentification().setValue(sReceptorNumber);
        addendaGrupoModelo.getEltPayment().getEltInvoiceCreator().getEltNameAddress().getEltName().setValue(moAuxCfdParams.getEmisor().getProperName().length() > 35 ? moAuxCfdParams.getEmisor().getProperName().substring(0, 34) : moAuxCfdParams.getEmisor().getProperName());
        addendaGrupoModelo.getEltPayment().getEltInvoiceCreator().getEltNameAddress().getEltStreetAddressOne().setValue(moAuxCfdParams.getEmisor().getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial().getStreet());
        addendaGrupoModelo.getEltPayment().getEltInvoiceCreator().getEltNameAddress().getEltCity().setValue(moAuxCfdParams.getEmisor().getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial().getState());
        addendaGrupoModelo.getEltPayment().getEltInvoiceCreator().getEltNameAddress().getEltPostalCode().setValue(moAuxCfdParams.getEmisor().getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial().getZipCode());
        addendaGrupoModelo.getEltPayment().getEltCurrency().getAttCurrencyISOCode().setOption(nMoneda);
        addendaGrupoModelo.getEltPayment().getEltCurrency().getEltCurrencyFunction().setValue("BILLING_CURRENCY");
        addendaGrupoModelo.getEltPayment().getEltCurrency().getEltCurrencyRate().setValue("" + oDecimalFormat.format(mdExchangeRate));
        addendaGrupoModelo.getEltPayment().getEltTotalAmount().getEltAmount().setValue("" + oDecimalFormat.format(mdTotalCy_r));
        addendaGrupoModelo.getEltPayment().getEltBaseAmount().getEltAmount().setValue("" + oDecimalFormat.format(mdSubtotalCy_r));
        addendaGrupoModelo.getEltPayment().getEltTax().getAttType().setString("VAT");
        addendaGrupoModelo.getEltPayment().getEltTax().getEltTaxPercentage().setValue("" + oDecimalFormat.format(dIvapct));
        addendaGrupoModelo.getEltPayment().getEltTax().getEltTaxAmount().setValue("" + oDecimalFormat.format(mdTaxCharged_r));
        addendaGrupoModelo.getEltPayment().getEltTax().getEltTaxCategory().setValue("TRANSFERIDO");
        addendaGrupoModelo.getEltPayment().getEltPayableAmount().getEltAmount().setValue("" + oDecimalFormat.format(mdTotal_r));

        addendaGrupoModelo.getEltPayment().getEltItem().getEltHijosLineItem().addAll(vLineItem);

        return addendaGrupoModelo;
    }

    /**
     * Create addenda for Elektra
     * @param dIvaPct Tax rate applied to DPS
     * @return Addenda
     * @throws java.lang.Exception
     */
    @SuppressWarnings("empty-statement")
    private cfd.ext.elektra.DElementAp computeAddendaElektra() throws java.lang.Exception {
        double dImpuestosTrasladados = 0;
        int[] anTypeDoc = {mnFkDpsCategoryId, mnFkDpsClassId, mnFkDpsTypeId};
        String sNotes = "";
        DElementAp addendaElektra = new DElementAp();

        cfd.ext.elektra.DElementDetailItems oDetailItems = new DElementDetailItems();
        cfd.ext.elektra.DElementDetalle oDetalle = null;
        cfd.ext.elektra.DElementProducto oProducto = null;
        cfd.ext.elektra.DElementTrasladado oTrasladado = null;
        cfd.ext.elektra.DElementImpuestos oImpuestos = null;

        for (SDataDpsNotes notes : mvDbmsDpsNotes) {
            sNotes += notes.getNotes() + "\n";
        }

        for (SDataDpsEntry entry : mvDbmsDpsEntries) {
            if (entry.isAccountable()) {

                oDetalle = new DElementDetalle();
                oDetalle.getAttFolio().setString(entry.getDbmsDpsAddElektraOrder());

                oProducto = new DElementProducto();
                oProducto.getAttCodigoBarras().setString(entry.getDbmsDpsAddElektraBarcode());
                oProducto.getAttCajasEntregadas().setInteger(entry.getDbmsDpsAddElektraCages());
                oProducto.getAttPrecioUnitarioCaja().setDouble(entry.getDbmsDpsAddElektraCagePriceUnitary());
                oProducto.getAttPiezasEntregadas().setInteger(entry.getDbmsDpsAddElektraParts());
                oProducto.getAttPrecioUnitarioPieza().setDouble(entry.getDbmsDpsAddElektraPartPriceUnitary());

                oImpuestos = new DElementImpuestos();
                dImpuestosTrasladados = 0;
                for (SDataDpsEntryTax tax : entry.getDbmsEntryTaxes()) {

                    oTrasladado = new DElementTrasladado();
                    switch (tax.getFkTaxTypeId()) {
                        case SModSysConsts.FINS_TP_TAX_RETAINED:
                            switch (tax.getDbmsCfdTaxId()) {
                                case SModSysConsts.FINS_CFD_TAX_IVA: // IVA
                                    break;
                                case SModSysConsts.FINS_CFD_TAX_ISR: // ISR
                                    break;
                                default:
                            }
                            break;

                        case SModSysConsts.FINS_TP_TAX_CHARGED:
                            switch (tax.getDbmsCfdTaxId()) {
                                case SModSysConsts.FINS_CFD_TAX_IVA: // IVA
                                    oTrasladado.getAttImpuesto().setString("IVA");
                                    break;
                                case SModSysConsts.FINS_CFD_TAX_IEPS: // IEPS
                                    oTrasladado.getAttImpuesto().setString("IEPS");
                                    break;
                                default:
                            }

                            oTrasladado.getAttImporte().setDouble(tax.getTaxCy());
                            oTrasladado.getAttTasa().setDouble(tax.getPercentage());
                            dImpuestosTrasladados += tax.getValue();

                            break;
                        default:
                    }

                    oImpuestos.getEltImpuestosTrasladados().getEltTrasladado().add(oTrasladado);
                }

                oDetalle.getEltProducto().getEltImpuestos().getAttTotalImpuestosTrasladados().setDouble(dImpuestosTrasladados);
                oDetalle.getEltProducto().getEltImpuestos().getEltImpuestosTrasladados().getEltTrasladado().addAll(oImpuestos.getEltImpuestosTrasladados().getEltTrasladado());

                oDetalle.getEltProducto().getAttCodigoBarras().setString(oProducto.getAttCodigoBarras().getString());
                oDetalle.getEltProducto().getAttCajasEntregadas().setInteger(oProducto.getAttCajasEntregadas().getInteger());
                oDetalle.getEltProducto().getAttPrecioUnitarioCaja().setDouble(oProducto.getAttPrecioUnitarioCaja().getDouble());
                oDetalle.getEltProducto().getAttPiezasEntregadas().setInteger(oProducto.getAttPiezasEntregadas().getInteger());
                oDetalle.getEltProducto().getAttPrecioUnitarioPieza().setDouble(oProducto.getAttPrecioUnitarioPieza().getDouble());

                oDetailItems.getEltDetalle().add(oDetalle);
            }
        }

        // Create addenda:

        addendaElektra.getAttXmlns().setString("http://www.tiendasneto.com/ap");
        addendaElektra.getAttSchemaLocation().setString("http://www.tiendasneto.com/ap addenda_prov.xsd");

        addendaElektra.getAttTipoComprobante().setString(
                SLibUtilities.compareKeys(anTypeDoc, SDataConstantsSys.TRNU_TP_DPS_SAL_INV) ? "FE" :
                SLibUtilities.compareKeys(anTypeDoc, SDataConstantsSys.TRNU_TP_DPS_SAL_CN) ? "NC" :
                SLibUtilities.compareKeys(anTypeDoc, SDataConstantsSys.TRNU_TP_DPS_SAL_REC) ? "ND" : "?");
        addendaElektra.getAttPlazoPago().setString(mnDaysOfCredit + " DIAS"); // XXX: Validate is 'DIAS' is declared like a constant
        addendaElektra.getAttObservaciones().setString(sNotes);
        addendaElektra.getEltDetalleProductos().getEltDetalle().addAll(oDetailItems.getEltDetalle());

        return addendaElektra;
    }
    
    public cfd.DElement createCfdiElementComercioExterior11() throws java.lang.Exception {
        double price = 0;
        double valueMxn = 0;
        double valueUsdEty = 0;
        cfd.ver3.cce11.DElementComercioExterior comercioExterior = new cfd.ver3.cce11.DElementComercioExterior();
        cfd.ver3.cce11.DElementMercancia mercancia = null;
        
        if (msXtaCfdCceOperationType.compareTo(SDataConstantsSys.TRNS_CFD_CCE_CAT_TP_OPE_A) != 0) {
            //comercioExterior.getAttMotivoTraslado().setString(msXtaCfdCceReasonTransfer); // not apply is necessary if comprobant type is "T" or "transfer"
            comercioExterior.getAttTipoOperacion().setString(msXtaCfdCceOperationType);
            comercioExterior.getAttClaveDePedimento().setString(msXtaCfdCceImportRequest);
            comercioExterior.getAttCertificadoOrigen().setInteger(mnXtaCfdCceCertificateOrigin);
            comercioExterior.getAttNumCertificadoOrigen().setString(msXtaCfdCceCertificateOriginNumber);
            comercioExterior.getAttNumeroExportadorConfiable().setString(msXtaCfdCceNumberExporter);
            comercioExterior.getAttIncoterm().setString(msXtaCfdCceIncoterm);
            comercioExterior.getAttSubdivision().setInteger(mnXtaCfdCceSubdivisionHas);
            //comercioExterior.getAttObservaciones() // Is optional, not apply in this version
            comercioExterior.getAttTipoCambioUSD().setDouble(mdXtaCfdCceExchangeRateUSD);
            comercioExterior.getAttTotalUSD().setDouble(mdXtaCfdCceTotalUSD);
        }
        
        cfd.ver3.cce11.DElementMercancias mercancias = new cfd.ver3.cce11.DElementMercancias();
        
        for (SDataDpsEntry dpsEntry : mvDbmsDpsEntries) {
            if (dpsEntry.isAccountable()) {
                if (dpsEntry.getSubtotalCy_r() == SLibUtils.round(dpsEntry.getOriginalPriceUnitaryCy() * dpsEntry.getOriginalQuantity(), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits())) {
                    price = SLibUtils.round(dpsEntry.getOriginalPriceUnitaryCy(), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                }
                else {
                    price = SLibUtils.round(dpsEntry.getSubtotalCy_r() / dpsEntry.getOriginalQuantity(), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                }

                mercancia = new cfd.ver3.cce11.DElementMercancia();

                mercancia.getAttNoIdentificacion().setString(dpsEntry.getConceptKey());
                mercancia.getAttFraccionArancelaria().setString(dpsEntry.getDbmsTariffFraction());
                mercancia.getAttCantidadAduana().setDouble(dpsEntry.getOriginalQuantity());
                mercancia.getAttUnidadAduana().setString(dpsEntry.getDbmsCustomsUnit());
                mercancia.getAttValorUnitarioAduana().setDouble(price);

                valueMxn = SLibUtils.round((dpsEntry.getSubtotalCy_r() * mdExchangeRate), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                valueUsdEty = SLibUtils.round((valueMxn / mdXtaCfdCceExchangeRateUSD), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());

                mercancia.getAttValorDolares().setDouble(valueUsdEty);

                mercancias.addMercancia(mercancia);
            }
        }
        
        comercioExterior.setEltMercancias(mercancias);
        
        return comercioExterior;
    }

    private ArrayList<SCfdDataImpuesto> createTaxesCfd32() {
        double dImptoTasa = 0;
        double dImpto = 0;
        Double oValue = null;
        Set<Double> setKeyImptos = null;
        HashMap<Double, Double> hmImpto = null;
        HashMap<Double, Double> hmRetenidoIva = new HashMap<Double, Double>();
        HashMap<Double, Double> hmRetenidoIsr = new HashMap<Double, Double>();
        HashMap<Double, Double> hmTrasladadoIva = new HashMap<Double, Double>();
        HashMap<Double, Double> hmTrasladadoIeps = new HashMap<Double, Double>();
        ArrayList<SCfdDataImpuesto> impuestosXml = null;
        SCfdDataImpuesto impuestoXml = null;

        impuestosXml = new ArrayList<SCfdDataImpuesto>();

        try {
            for (SDataDpsEntry entry : mvDbmsDpsEntries) {
                if (entry.isAccountable()) {
                    for (SDataDpsEntryTax tax : entry.getDbmsEntryTaxes()) {
                        if (tax.getFkTaxCalculationTypeId() != SModSysConsts.FINS_TP_TAX_CAL_RATE) {
                            throw new Exception("Todos los impuestos deben ser en base a una tasa (" + tax.getFkTaxCalculationTypeId() + ").");
                        }
                        else {
                            hmImpto = null;
                            dImptoTasa = 0;
                            
                            switch (tax.getFkTaxTypeId()) {
                                case SModSysConsts.FINS_TP_TAX_RETAINED:
                                    switch (tax.getDbmsCfdTaxId()) {
                                        case SModSysConsts.FINS_CFD_TAX_IVA: // IVA
                                            dImptoTasa = 1;   // on CFDI's XML retained taxes have no rate
                                            hmImpto = hmRetenidoIva;
                                            break;
                                        case SModSysConsts.FINS_CFD_TAX_ISR: // ISR
                                            dImptoTasa = 1;   // on CFDI's XML retained taxes have no rate
                                            hmImpto = hmRetenidoIsr;
                                            break;
                                        default:
                                            throw new Exception("Todos los impuestos retenidos deben ser conocidos (" + tax.getDbmsCfdTaxId() + ").");
                                    }
                                    break;

                                case SModSysConsts.FINS_TP_TAX_CHARGED:
                                    switch (tax.getDbmsCfdTaxId()) {
                                        case SModSysConsts.FINS_CFD_TAX_IVA: // IVA
                                            hmImpto = hmTrasladadoIva;
                                            break;
                                        case SModSysConsts.FINS_CFD_TAX_IEPS: // IEPS
                                            hmImpto = hmTrasladadoIeps;
                                            break;
                                        default:
                                            throw new Exception("Todos los impuestos trasladados deben ser conocidos (" + tax.getDbmsCfdTaxId() + ").");
                                    }
                                    break;

                                default:
                                    throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + tax.getFkTaxTypeId() + ").");
                            }

                            if (dImptoTasa == 0) {
                                dImptoTasa = tax.getPercentage();
                            }

                            oValue = hmImpto.get(dImptoTasa);
                            dImpto = oValue == null ? 0 : oValue.doubleValue();
                            hmImpto.put(dImptoTasa, dImpto + tax.getTaxCy());
                        }
                    }
                }
            }

            // Retained taxes:

            hmImpto = hmRetenidoIva;
            if (!hmImpto.isEmpty()) {
                setKeyImptos = hmImpto.keySet();
                for (Double key : setKeyImptos) {
                    dImpto = hmImpto.get(key);
                    if (dImpto != 0) {
                        impuestoXml = new SCfdDataImpuesto();

                        impuestoXml.setImpuesto(DAttributeOptionImpuestoRetencion.CFD_IVA);
                        impuestoXml.setImpuestoBasico(SModSysConsts.FINS_TP_TAX_RETAINED);
                        impuestoXml.setTasa(1);
                        impuestoXml.setImporte(dImpto);

                        impuestosXml.add(impuestoXml);
                    }
                }
            }

            hmImpto = hmRetenidoIsr;
            if (!hmImpto.isEmpty()) {
                setKeyImptos = hmImpto.keySet();
                for (Double key : setKeyImptos) {
                    dImpto = hmImpto.get(key);

                    if (dImpto != 0) {
                        impuestoXml = new SCfdDataImpuesto();

                        impuestoXml.setImpuesto(DAttributeOptionImpuestoRetencion.CFD_ISR);
                        impuestoXml.setImpuestoBasico(SModSysConsts.FINS_TP_TAX_RETAINED);
                        impuestoXml.setTasa(1);
                        impuestoXml.setImporte(dImpto);

                        impuestosXml.add(impuestoXml);
                    }
                }
            }

            // Charged taxes:

            hmImpto = hmTrasladadoIva;
            if (!hmImpto.isEmpty()) {
                setKeyImptos = hmImpto.keySet();
                for (Double key : setKeyImptos) {
                    dImpto = hmImpto.get(key);

                    impuestoXml = new SCfdDataImpuesto();
                    impuestoXml.setImpuesto(DAttributeOptionImpuestoTraslado.CFD_IVA);
                    impuestoXml.setImpuestoBasico(SModSysConsts.FINS_TP_TAX_CHARGED);
                    impuestoXml.setTasa(key * 100.0);
                    impuestoXml.setImporte(dImpto);
                    mdCfdIvaPorcentaje = (key * 100.0);

                    impuestosXml.add(impuestoXml);
                }
            }

            hmImpto = hmTrasladadoIeps;
            if (!hmImpto.isEmpty()) {
                setKeyImptos = hmImpto.keySet();
                for (Double key : setKeyImptos) {
                    dImpto = hmImpto.get(key);

                    impuestoXml = new SCfdDataImpuesto();
                    impuestoXml.setImpuesto(DAttributeOptionImpuestoTraslado.CFD_IEPS);
                    impuestoXml.setImpuestoBasico(SModSysConsts.FINS_TP_TAX_CHARGED);
                    impuestoXml.setTasa(key * 100.0);
                    impuestoXml.setImporte(dImpto);

                    impuestosXml.add(impuestoXml);
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        return impuestosXml;
    }
      
    private ArrayList<SCfdDataImpuesto> createTaxesCfd33() {
        double dImptoTasa = 0;
        ArrayList<SCfdDataImpuesto> impuestosXml = null;
        SCfdDataImpuesto impuestoXml = null;
        DCfdTaxes cfdTaxes = null;

        impuestosXml = new ArrayList<SCfdDataImpuesto>();
        cfdTaxes = new DCfdTaxes();
        
        try {
            for (SDataDpsEntry entry : mvDbmsDpsEntries) {
                if (entry.isAccountable()) {
                    for (SDataDpsEntryTax tax : entry.getDbmsEntryTaxes()) {
                        if (tax.getFkTaxCalculationTypeId() != SModSysConsts.FINS_TP_TAX_CAL_RATE) {
                            throw new Exception("Todos los impuestos deben ser en base a una tasa (" + tax.getFkTaxCalculationTypeId() + ").");
                        }
                        else {
                            impuestoXml = new SCfdDataImpuesto();
                            dImptoTasa = 0;
                    
                            switch (tax.getFkTaxTypeId()) {
                                case SModSysConsts.FINS_TP_TAX_RETAINED:
                                    switch (tax.getDbmsCfdTaxId()) {
                                        case SModSysConsts.FINS_CFD_TAX_IVA: // IVA
                                            impuestoXml.setImpuesto(DAttributeOptionImpuestoRetencion.CFD_IVA);
                                            impuestoXml.setImpuestoBasico(SModSysConsts.FINS_TP_TAX_RETAINED);
                                            
                                            dImptoTasa = 1;   // on CFDI's XML retained taxes have no rate
                                            break;
                                        case SModSysConsts.FINS_CFD_TAX_ISR: // ISR
                                            impuestoXml.setImpuesto(DAttributeOptionImpuestoRetencion.CFD_ISR);
                                            impuestoXml.setImpuestoBasico(SModSysConsts.FINS_TP_TAX_RETAINED);
                                            
                                            dImptoTasa = 1;   // on CFDI's XML retained taxes have no rate
                                            break;
                                        default:
                                            throw new Exception("Todos los impuestos retenidos deben ser conocidos (" + tax.getDbmsCfdTaxId() + ").");
                                    }
                                    break;

                                case SModSysConsts.FINS_TP_TAX_CHARGED:
                                    switch (tax.getDbmsCfdTaxId()) {
                                        case SModSysConsts.FINS_CFD_TAX_IVA: // IVA
                                            impuestoXml.setImpuesto(DAttributeOptionImpuestoRetencion.CFD_IVA);
                                            impuestoXml.setImpuestoBasico(SModSysConsts.FINS_TP_TAX_CHARGED);
                                            
                                            dImptoTasa = tax.getPercentage();
                                            break;
                                        case SModSysConsts.FINS_CFD_TAX_IEPS: // IEPS
                                            impuestoXml.setImpuesto(DAttributeOptionImpuestoRetencion.CFD_NO_DEFINIDO);
                                            impuestoXml.setImpuestoBasico(SModSysConsts.FINS_TP_TAX_CHARGED);
                                            
                                            dImptoTasa = tax.getPercentage();
                                            break;
                                        default:
                                            throw new Exception("Todos los impuestos trasladados deben ser conocidos (" + tax.getDbmsCfdTaxId() + ").");
                                    }
                                    break;

                                default:
                                    throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + tax.getFkTaxTypeId() + ").");
                            }
                            impuestoXml.setBase(entry.getSubtotalCy_r());
                            impuestoXml.setImpuestoClave(tax.getDbmsCfdTax());
                            impuestoXml.setTasa(dImptoTasa);
                            impuestoXml.setImporte(tax.getTaxCy());
                            impuestoXml.setTipoFactor(tax.getDbmsTaxCalculationType());
                            
                            if (tax.getFkTaxTypeId() == SModSysConsts.FINS_TP_TAX_CHARGED) {
                                cfdTaxes.addTaxCharged(impuestoXml);
                            }
                            else if (tax.getFkTaxTypeId() == SModSysConsts.FINS_TP_TAX_RETAINED) {
                                cfdTaxes.addTaxRetained(impuestoXml);
                            }
                        }
                    }
                }
            }
            
            for (DCfdTax tax : cfdTaxes.getTaxCharged()) {
                impuestosXml.add((SCfdDataImpuesto) tax);
            }
            
            for (DCfdTax tax : cfdTaxes.getTaxRetained()) {
                impuestosXml.add((SCfdDataImpuesto) tax);
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        return impuestosXml;
    }

    @Override
    public int getCfdTipoCfdXml() {
        return SCfdConsts.CFD_TYPE_DPS;
    }

    @Override
    public String getCfdSerie() {
        return msNumberSeries;
    }

    @Override
    public String getCfdFolio() {
        return msNumber;
    }

    @Override
    public String getCfdReferencia() {
        return msNumberReference;
    }

    @Override
    public Date getCfdFecha() {
        int[] anDateDps = SLibTimeUtilities.digestDate(mtDate);
        int[] anDateEdit = SLibTimeUtilities.digestDate(mtUserEditTs);
        java.util.Date tDate = null;
        GregorianCalendar oGregorianCalendar = null;

        if (SLibUtilities.compareKeys(anDateDps, anDateEdit)) {
            tDate = mtUserEditTs;
        }
        else {
            oGregorianCalendar = new GregorianCalendar();
            oGregorianCalendar.setTime(mtUserEditTs);
            oGregorianCalendar.set(GregorianCalendar.YEAR, anDateDps[0]);
            oGregorianCalendar.set(GregorianCalendar.MONTH, anDateDps[1] - 1);  // January is month 0
            oGregorianCalendar.set(GregorianCalendar.DATE, anDateDps[2]);
            tDate = oGregorianCalendar.getTime();
        }

        return tDate;
    }

    @Override
    public String getCfdFormaDePago() {
        return msPaymentMethod;
    }

    @Override
    public int getCfdCondicionesDePago() {
        return mnFkPaymentTypeId == SDataConstantsSys.TRNS_TP_PAY_CASH ? DAttributeOptionCondicionesPago.CFD_CONTADO : DAttributeOptionCondicionesPago.CFD_CREDITO;
    }

    @Override
    public double getCfdSubTotal() {
        return mdSubtotalProvisionalCy_r;
    }

    @Override
    public double getCfdDescuento() {
        return 0;
    }

    @Override
    public String getCfdMotivoDescuento() {
        return "";
    }

    @Override
    public double getCfdTipoCambio() {
        return mdExchangeRate;
    }

    @Override
    public String getCfdMoneda() {
        return msDbmsCurrencyKey;
    }

    @Override
    public double getCfdTotal() {
        return mdTotalCy_r;
    }

    @Override
    public int getCfdTipoDeComprobante() {
        return isDocument() ? DAttributeOptionTipoComprobante.CFD_INGRESO : DAttributeOptionTipoComprobante.CFD_EGRESO;
    }

    @Override
    public int getCfdMetodoDePago() {
        return mnPayments;
    }

    @Override
    public String getCfdNumCtaPago() {
        return msPaymentAccount;
    }

    @Override
    public String getCfdNumConfirmacion() {
        return msXtaCfdConfirmation;
    }
    
    @Override
    public int getEmisor() {
        return moAuxCfdParams.getEmisor().getPkBizPartnerId();
    }

    @Override
    public int getSucursalEmisor() {
        return mnFkCompanyBranchId;
    }

    @Override
    public int getReceptor() {
        return mnFkBizPartnerId_r;
    }

    @Override
    public int getSucursalReceptor() {
        return mnFkBizPartnerBranchId;
    }
    
    @Override
    public ArrayList<DElement> getCfdElementRegimenFiscal() {
        ArrayList<DElement> regimes = null;
        DElement regimen = null;

        for (int i = 0; i < moAuxCfdParams.getRegimenFiscal().length; i++) {
            regimes = new ArrayList<DElement>();
            regimen = new cfd.ver32.DElementRegimenFiscal();

            ((cfd.ver32.DElementRegimenFiscal) regimen).getAttRegimen().setString(moAuxCfdParams.getRegimenFiscal()[i]);
            regimes.add(regimen);
        }

        return regimes;
    }
    
    @Override
    public String getCfdTipoRelacion() {
        return msXtaCfdRelationType;        
    }
    
    @Override
    public String getCfdUsoCfdi() {
        return msXtaCfdUseCfdi;        
    }

    @Override
    public DElement getCfdElementAddenda() {
        DElement addenda = null;

        // Create custom addendas when needed:

        try {
            if (moAuxCfdParams.getReceptor().getDbmsCategorySettingsCus().getFkCfdAddendaTypeId() != SDataConstantsSys.BPSS_TP_CFD_ADD_NA) {
                addenda = new cfd.ver32.DElementAddenda();

                if (isDocumentSal() || isAdjustmentSal()) {
                    switch (moAuxCfdParams.getReceptor().getDbmsCategorySettingsCus().getFkCfdAddendaTypeId()) {
                        case SDataConstantsSys.BPSS_TP_CFD_ADD_SORIANA:
                            ((cfd.ver32.DElementAddenda) addenda).getElements().add(computeAddendaSoriana());
                            break;
                        case SDataConstantsSys.BPSS_TP_CFD_ADD_LOREAL:
                            ((cfd.ver32.DElementAddenda) addenda).getElements().add(computeAddendaLoreal(mdCfdIvaPorcentaje));
                            break;
                        case SDataConstantsSys.BPSS_TP_CFD_ADD_BACHOCO:
                            ((cfd.ver32.DElementAddenda) addenda).getElements().add(computeAddendaBachoco(mdCfdIvaPorcentaje));
                            break;
                        case SDataConstantsSys.BPSS_TP_CFD_ADD_MODELO:
                            ((cfd.ver32.DElementAddenda) addenda).getElements().add(computeAddendaGrupoModelo(mdCfdIvaPorcentaje));
                            break;
                        case SDataConstantsSys.BPSS_TP_CFD_ADD_ELEKTRA:
                            ((cfd.ver32.DElementAddenda) addenda).getElements().add(computeAddendaElektra());
                            break;
                        default:
                            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }

        return addenda;
    }

    @Override
    public DElement getCfdElementComplemento() {
        DElement complemento = null;
        
        try {
            if (!msXtaCfdCceOperationType.isEmpty()) {
                complemento = new cfd.ver32.DElementComplemento();
                
                ((cfd.ver32.DElementComplemento) complemento).getElements().add(createCfdiElementComercioExterior11());
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        return complemento;
    }

    @Override
    public ArrayList<SCfdDataCfdiRelacionado> getCfdCfdiRelacionados() {
        ArrayList<SCfdDataCfdiRelacionado> cfdiRelacinadosXml = new ArrayList<SCfdDataCfdiRelacionado>();
        
        for (SDataDpsEntry dpsEntry : mvDbmsDpsEntries) {
            for (SDataDpsDpsLink dpsEntryLink : dpsEntry.getDbmsDpsLinksAsDestiny()) {
                
            }
        }
        
        return cfdiRelacinadosXml;
    }
    
    @Override
    public ArrayList<SCfdDataConcepto> getCfdConceptos() {
        double price = 0;
        String descripcion = "";
        SCfdDataConcepto conceptoXml = null;
        ArrayList<SCfdDataConcepto> conceptosXml = new ArrayList<SCfdDataConcepto>();
        
        for (SDataDpsEntry dpsEntry : mvDbmsDpsEntries) {
            if (dpsEntry.isAccountable()) {
                descripcion = dpsEntry.getConcept();
                
                for (SDataDpsEntryNotes dpsEntryNotes : dpsEntry.getDbmsEntryNotes()) {
                    if (dpsEntryNotes.getIsCfd()) {
                        descripcion += "\n- " + dpsEntryNotes.getNotes();
                    }
                }
                
                if (dpsEntry.getSubtotalCy_r() == SLibUtils.round(dpsEntry.getOriginalPriceUnitaryCy() * dpsEntry.getOriginalQuantity(), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits())) {
                    price = dpsEntry.getOriginalPriceUnitaryCy();
                }
                else {
                    price = dpsEntry.getSubtotalCy_r() / dpsEntry.getOriginalQuantity();
                }
                
                conceptoXml = new SCfdDataConcepto();
                conceptoXml.setNoIdentificacion(dpsEntry.getConceptKey());
                conceptoXml.setUnidad(dpsEntry.getDbmsOriginalUnitSymbol());
                conceptoXml.setCantidad(dpsEntry.getOriginalQuantity());
                conceptoXml.setDescripcion(descripcion);
                conceptoXml.setValorUnitario(price);
                conceptoXml.setImporte(dpsEntry.getSubtotalCy_r());
                conceptoXml.setClaveProdServ(dpsEntry.getDbmsItemClaveProdServ());
                conceptoXml.setClaveUnidad(dpsEntry.getDbmsUnidadClave());
                
                conceptoXml.computeCfdImpuestosConceptos(dpsEntry);
                
                conceptosXml.add(conceptoXml);
            }
        }

        return conceptosXml;
    }
    
    @Override
    public ArrayList<SCfdDataImpuesto> getCfdImpuestos(float cfdiVersion) {
        ArrayList<SCfdDataImpuesto> cfdDataImpuestos = null;
        
        if (cfdiVersion == DCfdConsts.CFDI_VER_32) {
            cfdDataImpuestos = createTaxesCfd32();
        }
        else if (cfdiVersion == DCfdConsts.CFDI_VER_33) {
            cfdDataImpuestos = createTaxesCfd33();
        }
        
        return cfdDataImpuestos;
    }

    public void saveField(java.sql.Connection connection, final int[] pk, final int field, final Object value) throws Exception {
        String sSql = "";

        mnLastDbActionResult = SLibConsts.UNDEFINED;

        sSql = "UPDATE trn_dps SET ";

        switch (field) {
            case FIELD_REF_BKR:
                sSql += "comms_ref = '" + value + "' ";
                break;
            case FIELD_SAL_AGT:
                sSql += "fid_sal_agt_n = " + value + " ";
                break;
            case FIELD_SAL_AGT_SUP:
                sSql += "fid_sal_sup_n = " + value + " ";
                break;
            case FIELD_CLO_COMMS:
                sSql += "b_close_comms = " + value + " ";
                break;
            case FIELD_CLO_COMMS_USR:
                sSql += "fid_usr_close_comms = " + value + ", ts_close_comms = NOW() ";
            case FIELD_USR:
                sSql += "fid_usr_edit = " + value + ", ts_edit = NOW() ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        sSql += "WHERE id_year = " + pk[0] + " AND id_doc = " + pk[1];

        connection.createStatement().execute(sSql);

        mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
    }
    
    public void sendMail(SClientInterface client, Object dpsKey, int mmsType) {
        int[] mmsConfigKey = null;
        String msg = "";
        String companyName = "";
        String bpName = "";
        String dpsDestNumber = "";
        String dpsReference = "";
        ArrayList<String> toRecipients = null;
        HashSet<String> setCfgEmail = new HashSet<>();
        SDbMmsConfig mmsConfig = null;
        SDataDpsType moDpsType = null;
        SDataDps dpsDest = null;
        boolean isEdited = false;
        boolean send = true;
        
        read(dpsKey, client.getSession().getStatement());
        
        isEdited = mtUserNewTs.compareTo(mtUserEditTs) != 0;
        
        companyName = client.getSessionXXX().getCompany().getCompany();
        bpName = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.BPSU_BP, new int[] { mnFkBizPartnerId_r }, SLibConstants.DESCRIPTION_NAME);
        moDpsType = (SDataDpsType) SDataUtilities.readRegistry(client, SDataConstants.TRNU_TP_DPS, getDpsTypeKey(), SLibConstants.EXEC_MODE_VERBOSE);
        dpsReference = !getNumberReference().isEmpty() ? getNumberReference() : "N/D";
        toRecipients = new ArrayList<>();
        
        for (SDataDpsEntry entry : mvDbmsDpsEntries) {
            try {
                mmsConfigKey = STrnUtilities.readMmsConfigurationByLinkType(client, mmsType, entry.getFkItemId());
                
                if (mmsConfigKey[0] != SLibConsts.UNDEFINED) {
                    mmsConfig = new SDbMmsConfig();
                    mmsConfig.read(client.getSession(), mmsConfigKey);
                    
                    setCfgEmail.add(mmsConfig.getEmail());
                }
            }
            catch (java.lang.Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
        
        if (!setCfgEmail.isEmpty()) {
            msg = "Se enviará correo-e de notificación a los siguientes destinatarios:";
            
            for (String email : setCfgEmail) {
                msg += "\n" + email;
            }
            client.showMsgBoxInformation(msg);
        }
        else {
            send = false;
        }
        
        if (send) {
            for (String cfgEmail : setCfgEmail) {
                try {
                    msg = STrnUtilities.computeMailHeaderBeginTable(companyName, moDpsType.getDpsType(), getDpsNumber(), bpName, mtDate, (isEdited ? mtUserEditTs : mtUserNewTs), isEdited, mbIsRebill);

                    for (SDataDpsEntry entry : mvDbmsDpsEntries) {
                        mmsConfigKey = STrnUtilities.readMmsConfigurationByLinkType(client, mmsType, entry.getFkItemId());

                        if (mmsConfigKey[0] != SLibConsts.UNDEFINED) {
                            mmsConfig = new SDbMmsConfig();
                            mmsConfig.read(client.getSession(), mmsConfigKey);

                            if (cfgEmail.compareTo(mmsConfig.getEmail()) == 0) {
                                if (entry.getDbmsDpsLinksAsDestiny()!= null && !entry.getDbmsDpsLinksAsDestiny().isEmpty()) {
                                    dpsDest = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, entry.getDbmsDpsLinksAsDestiny().get(0).getDbmsSourceDpsKey(), SLibConstants.EXEC_MODE_STEALTH);
                                    dpsDestNumber = dpsDest.getDpsNumber();
                                }
                                else {
                                    dpsDestNumber = "N/D";
                                }

                                msg += STrnUtilities.computeMailItem(client, entry.getFkItemId(), entry.getFkOriginalUnitId(), entry.getConceptKey(), entry.getConcept(), dpsDestNumber, msNumberSeries, msNumber, dpsReference, entry.getOriginalQuantity(), entry.getDbmsUnitSymbol(), getDate(), getDate(), getDpsTypeKey(), isEdited, mbIsRebill);
                            }
                        }
                    }

                    msg += STrnUtilities.computeMailFooterEndTable(SClient.APP_NAME , SClient.APP_COPYRIGHT, SClient.APP_PROVIDER, SClient.VENDOR_WEBSITE , SClient.APP_RELEASE);
                    toRecipients.add(cfgEmail);

                    STrnUtilities.sendMail(client, mmsType, toRecipients, null, null, msg);
                    toRecipients.clear();
                }
                catch (java.lang.Exception e) {
                    SLibUtilities.printOutException(this, e);
                }
            }
        }
    }
}
