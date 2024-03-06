/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import cfd.DCfdConsts;
import cfd.DCfdUtils;
import cfd.DElement;
import cfd.ver40.DCfdi40Catalogs;
import cfd.ver40.DElementComplemento;
import cfd.ver40.crp20.DElementDoctoRelacionado;
import cfd.ver40.crp20.DElementImpuestosDR;
import cfd.ver40.crp20.DElementImpuestosP;
import cfd.ver40.crp20.DElementPagos;
import cfd.ver40.crp20.DElementPagosPago;
import cfd.ver40.crp20.DElementRetencionDR;
import cfd.ver40.crp20.DElementRetencionP;
import cfd.ver40.crp20.DElementRetencionesDR;
import cfd.ver40.crp20.DElementRetencionesP;
import cfd.ver40.crp20.DElementTotales;
import cfd.ver40.crp20.DElementTrasladoDR;
import cfd.ver40.crp20.DElementTrasladoP;
import cfd.ver40.crp20.DElementTrasladosDR;
import cfd.ver40.crp20.DElementTrasladosP;
import erp.cfd.SCfdDataConcepto;
import erp.cfd.SCfdDataImpuesto;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.STrnUtils;
import erp.mtrn.data.cfd.SCfdPaymentEntry;
import erp.mtrn.data.cfd.SCfdPaymentEntryDoc;
import erp.mtrn.data.cfd.SDataReceiptPayment;
import erp.mtrn.data.cfd.SDataReceiptPaymentPay;
import erp.mtrn.data.cfd.SDataReceiptPaymentPayDocTax;
import erp.mtrn.data.cfd.SDataReceiptPaymentPayTax;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;

/**
 * Database registry of CFDI of Payments.
 * @author Sergio Flores, Isabel Servín
 */
public class SDataCfdPayment extends erp.lib.data.SDataRegistry implements java.io.Serializable, erp.cfd.SCfdXmlCfdi33, erp.cfd.SCfdXmlCfdi40 {
    
    public static final String NAME = "CFDI complemento recepción pagos";
    
    // members that belong to SDataCfd:
    protected SDataCfd moDbmsDataCfd;
    protected SDataBizPartner moAuxDbmsDataEmisor;
    protected SDataBizPartnerBranch moAuxDbmsDataEmisorSucursal;
    
    // actual database registry of receipt of payment:
    protected SDataReceiptPayment moDbmsReceiptPayment;
    
    // members that belong to XML of CFDI:
    protected String msAuxCfdConfirmacion;
    protected String msAuxCfdEmisorRegimenFiscal;
    protected String msAuxCfdReceptorRegimenFiscal;
    protected SDataBizPartner moAuxCfdDbmsDataReceptor;
    protected SDataBizPartner moAuxCfdDbmsDataReceptorFactoring;
    protected String msAuxCfdCfdiRelacionadosTipoRelacion;
    protected String msAuxCfdCfdiRelacionadoUuid; // available when CFDI is not stored in SIIE, e.g., third-party CFDI
    protected STrnCfdRelated moAuxCfdiDocRelacionados;
    protected SDataCfd moAuxCfdDbmsDataCfdCfdiRelacionado; // available when CFDI is stored in SIIE
    protected ArrayList<SCfdPaymentEntry> maAuxCfdPaymentEntries;
    
    // members for CFDI annulment:
    protected int mnAuxAnnulType;
    
    // members that belong to SDataRecord:
    protected int mnAuxFkUserNewId;
    protected int mnAuxFkUserEditId;
    protected int mnAuxFkUserDeleteId;
    
    // auxiliar members:
    protected boolean mbAuxIsProcessingCfdi; // to reduce reading time when extra stuff is useless
    protected boolean mbAuxIsProcessingCfdiValidation;
    protected boolean mbAuxIsProcessingStorageOnly; // for temporary use only, to create the storage of all former receipts of payments prior to SIIE 3.2 191
    
    /**
     * Creates database registry of CFDI of Payments.
     */
    public SDataCfdPayment() {
        super(SDataConstants.TRNX_CFD_PAY_REC);
        reset();
    }
    
    /*
     * Private methods
     */
    
    private SDataBizPartner getEffectiveReceptor() {
        return moAuxCfdDbmsDataReceptorFactoring != null ? moAuxCfdDbmsDataReceptorFactoring : moAuxCfdDbmsDataReceptor;
    }
    
    private boolean testAnnulment(java.sql.Connection connection, java.lang.String msg) throws java.lang.Exception {
        moDbmsDataCfd.setAuxIsProcessingValidation(mbAuxIsProcessingCfdiValidation);
        
        if (moDbmsDataCfd.testDeletion(msg, SDbConsts.ACTION_ANNUL)) {
            // Check that document's date belongs to an open period:

            int param = 1;
            int[] periodKey = SLibTimeUtilities.digestYearMonth(moDbmsDataCfd.getTimestamp());
            CallableStatement callableStatement = connection.prepareCall("{ CALL fin_year_per_st(?, ?, ?) }");
            callableStatement.setInt(param++, periodKey[0]);
            callableStatement.setInt(param++, periodKey[1]);
            callableStatement.registerOutParameter(param++, java.sql.Types.INTEGER);
            callableStatement.execute();

            if (callableStatement.getBoolean(param - 1)) {
                mnDbmsErrorId = 101;
                msDbmsError = msg + "¡El período contable de la fecha del comprobante está cerrado!";
                throw new Exception(msDbmsError);
            }
        }

        return true;    // if this line is reached, no errors were found
    }

    /*
     * Public methods
     */
    
    public void setDbmsDataCfd(SDataCfd o) { moDbmsDataCfd = o; }
    public void setAuxDbmsDataEmisor(SDataBizPartner o) { moAuxDbmsDataEmisor = o; }
    public void setAuxDbmsDataEmisorSucursal(SDataBizPartnerBranch o) { moAuxDbmsDataEmisorSucursal = o; }
    
    public SDataCfd getDbmsDataCfd() { return moDbmsDataCfd; }
    public SDataBizPartner getAuxDbmsDataEmisor() { return moAuxDbmsDataEmisor; }
    public SDataBizPartnerBranch getAuxDbmsDataEmisorSucursal() { return moAuxDbmsDataEmisorSucursal; }
    
    public void setDbmsReceiptPayment(SDataReceiptPayment o) { moDbmsReceiptPayment = o; }
    
    public SDataReceiptPayment getDbmsReceiptPayment() { return moDbmsReceiptPayment; }
    
    public void setAuxCfdConfirmacion(String s) { msAuxCfdConfirmacion = s; }
    public void setAuxCfdEmisorRegimenFiscal(String s) { msAuxCfdEmisorRegimenFiscal = s; }
    public void setAuxCfdReceptorRegimenFiscal(String s) { msAuxCfdReceptorRegimenFiscal = s; }
    public void setAuxCfdDbmsDataReceptor(SDataBizPartner o) { moAuxCfdDbmsDataReceptor = o; }
    public void setAuxCfdDbmsDataReceptorFactoring(SDataBizPartner o) { moAuxCfdDbmsDataReceptorFactoring = o; }
    public void setAuxCfdCfdiRelacionadosTipoRelacion(String s) { msAuxCfdCfdiRelacionadosTipoRelacion = s; }
    public void setAuxCfdCfdiRelacionadoUuid(String s) { msAuxCfdCfdiRelacionadoUuid = s; }
    public void setAuxCfdiDocRelacionados(STrnCfdRelated o) { moAuxCfdiDocRelacionados = o; }
    public void setAuxCfdDbmsDataCfdCfdiRelacionado(SDataCfd o) { moAuxCfdDbmsDataCfdCfdiRelacionado = o; }
    
    public String getAuxCfdConfirmacion() { return msAuxCfdConfirmacion; }
    public String getAuxCfdEmisorRegimenFiscal() { return msAuxCfdEmisorRegimenFiscal; }
    public String getAuxCfdReceptorRegimenFiscal() { return msAuxCfdReceptorRegimenFiscal; }
    public SDataBizPartner getAuxCfdDbmsDataReceptor() { return moAuxCfdDbmsDataReceptor; }
    public SDataBizPartner getAuxCfdDbmsDataReceptorFactoring() { return moAuxCfdDbmsDataReceptorFactoring; }
    public String getAuxCfdCfdiRelacionadosTipoRelacion() { return msAuxCfdCfdiRelacionadosTipoRelacion; }
    public String getAuxCfdCfdiRelacionadoUuid() { return msAuxCfdCfdiRelacionadoUuid; }
    public STrnCfdRelated getAuxCfdiDocRelacionados() { return moAuxCfdiDocRelacionados; }
    public SDataCfd getAuxCfdDbmsDataCfdCfdiRelacionado() { return moAuxCfdDbmsDataCfdCfdiRelacionado; }
    public ArrayList<SCfdPaymentEntry> getAuxCfdPaymentEntries() { return maAuxCfdPaymentEntries; }
    
    public void setAuxAnnulType(int n) { mnAuxAnnulType = n; }
    
    public int getAuxAnnulType() { return mnAuxAnnulType; }
    
    public void setFkUserNewId(int n) { mnAuxFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnAuxFkUserEditId = n; }
    public void setFkUserDeleteId(int n) {mnAuxFkUserDeleteId = n; }
    
    public int getFkUserNewId() { return mnAuxFkUserNewId; }
    public int getFkUserEditId() { return mnAuxFkUserEditId; }
    public int getFkUserDeleteId() { return mnAuxFkUserDeleteId; }
    
    public void setAuxIsProcessingCfdi(boolean b) { mbAuxIsProcessingCfdi = b; }
    public void setAuxIsProcessingCfdiValidation(boolean b) { mbAuxIsProcessingCfdiValidation = b; }
    public void setAuxIsProcessingStorageOnly(boolean b) { mbAuxIsProcessingStorageOnly = b; }
    
    public boolean getAuxIsProcessingCfdi() { return mbAuxIsProcessingCfdi; }
    public boolean getAuxIsProcessingCfdiValidation() { return mbAuxIsProcessingCfdiValidation; }
    public boolean getAuxIsProcessingStorageOnly() { return mbAuxIsProcessingStorageOnly; }
    
    public void copyCfdMembers(final SDataCfdPayment sourceCfdPayment) {
        this.msAuxCfdConfirmacion = sourceCfdPayment.msAuxCfdConfirmacion;
        this.msAuxCfdEmisorRegimenFiscal = sourceCfdPayment.msAuxCfdEmisorRegimenFiscal;
        this.msAuxCfdReceptorRegimenFiscal = sourceCfdPayment.msAuxCfdReceptorRegimenFiscal;
        this.moAuxCfdDbmsDataReceptor = sourceCfdPayment.moAuxCfdDbmsDataReceptor;
        this.moAuxCfdDbmsDataReceptorFactoring = sourceCfdPayment.moAuxCfdDbmsDataReceptorFactoring;
        this.msAuxCfdCfdiRelacionadosTipoRelacion = sourceCfdPayment.msAuxCfdCfdiRelacionadosTipoRelacion;
        this.msAuxCfdCfdiRelacionadoUuid = sourceCfdPayment.msAuxCfdCfdiRelacionadoUuid;
        this.moAuxCfdiDocRelacionados = sourceCfdPayment.moAuxCfdiDocRelacionados;
        this.moAuxCfdDbmsDataCfdCfdiRelacionado = sourceCfdPayment.moAuxCfdDbmsDataCfdCfdiRelacionado;
        this.maAuxCfdPaymentEntries.clear();
        this.maAuxCfdPaymentEntries.addAll(sourceCfdPayment.maAuxCfdPaymentEntries);
    }
    
    private void deleteRecordCfd(java.sql.Connection connection) throws java.lang.Exception {
        String sql;
        Statement statement = connection.createStatement();
        
        sql = "DELETE FROM trn_cfd_fin_rec "
                + "WHERE id_cfd = " + moDbmsDataCfd.getPkCfdId() + ";";
        statement.execute(sql);
    }
    
    public void deleteRecordFin(java.sql.Connection connection) throws java.lang.Exception {
        String sql;
        Statement statement = connection.createStatement();
        
        sql = "UPDATE fin_rec_ety SET b_del = 1, fid_usr_del = " + (mnAuxFkUserDeleteId != 0 ? mnAuxFkUserDeleteId : SUtilConsts.USR_NA_ID) + ", ts_del = NOW() "
                + "WHERE fid_cfd_n = " + moDbmsDataCfd.getPkCfdId() + " AND NOT b_del;";
        statement.execute(sql);
    }
    
    public void deleteAccounting(java.sql.Connection connection) throws java.lang.Exception {
        deleteRecordCfd(connection);
        deleteRecordFin(connection);
    }

    public static int getDbmsDataReceptorId(final Statement statement, final int idCfdi) throws Exception {
        int idReceptor = 0;
        String sql;
        ResultSet resultSet;
        
        sql = "SELECT p.fid_bp "
                + "FROM trn_cfd AS c "
                + "INNER JOIN trn_pay AS p ON p.id_rcp = c.fid_rcp_pay_n " 
                + "WHERE c.id_cfd = " + idCfdi + ";";
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            idReceptor = resultSet.getInt(1);
        }
        else {
            // attemt to find out to whom this registry was issued:
            sql = "SELECT DISTINCT re.fid_bp_nr "
                    + "FROM fin_rec AS r "
                    + "INNER JOIN fin_rec_ety re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                    + "WHERE NOT r.b_del AND NOT re.b_del AND re.fid_cfd_n = " + idCfdi + ";";
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                idReceptor = resultSet.getInt(1);
            }
            else {
                // attemt to find out to whom this registry was issued:
                sql = sql.replace("AND NOT re.b_del ", "AND re.b_del ");
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    idReceptor = resultSet.getInt(1);
                }
            }
        }
        
        resultSet.close();
        
        return idReceptor;
    }
    
    /**
     * Read payment from data registry SDataReceiptPayment.
     * It is assumed that member moDbmsDataCfd has been already read and properly setup.
     * @param statement DBMS statement.
     * @throws Exception 
     */
    private void readPaymentFromReceiptPayment(final Statement statement) throws Exception {
        moDbmsReceiptPayment = new SDataReceiptPayment();
        moDbmsReceiptPayment.setAuxIsProcessingCfdi(mbAuxIsProcessingCfdi);
        if (moDbmsReceiptPayment.read(new int[] { moDbmsDataCfd.getFkReceiptPaymentId_n() }, statement) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\nComprobante de recepción de pagos.");
        }

        // read 'Emisor' branch and business partner data objects:

        moAuxDbmsDataEmisorSucursal = new SDataBizPartnerBranch();
        if (moAuxDbmsDataEmisorSucursal.read(new int[] { moDbmsReceiptPayment.getFkCompanyBranchId() }, statement) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\nSucursal del emisor.");
        }

        moAuxDbmsDataEmisor = new SDataBizPartner();
        if (moAuxDbmsDataEmisor.read(new int[] { moAuxDbmsDataEmisorSucursal.getFkBizPartnerId() }, statement) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\nEmisor.");
        }

        mnAuxFkUserNewId = moDbmsReceiptPayment.getFkUserNewId();
        mnAuxFkUserEditId = moDbmsReceiptPayment.getFkUserEditId();
        mnAuxFkUserDeleteId = moDbmsReceiptPayment.getFkUserDeleteId();

        // parse CFDI to extract auxiliar data:

        msAuxCfdConfirmacion = moDbmsReceiptPayment.getConfirmationNum();
        msAuxCfdEmisorRegimenFiscal = moDbmsReceiptPayment.getTaxRegimeCodeIssuier();
        msAuxCfdReceptorRegimenFiscal = moDbmsReceiptPayment.getTaxRegimeCodeReceptor();
        
        msAuxCfdCfdiRelacionadosTipoRelacion = moDbmsReceiptPayment.getCfdiRelationCode();
        msAuxCfdCfdiRelacionadoUuid = moDbmsReceiptPayment.getCfdiRelatedUuid();
        if (!msAuxCfdCfdiRelacionadoUuid.isEmpty()) {
            int id = STrnUtilities.getCfdIdByUuid(statement, msAuxCfdCfdiRelacionadoUuid);
            if (id != 0) {
                moAuxCfdDbmsDataCfdCfdiRelacionado = new SDataCfd();
                if (moAuxCfdDbmsDataCfdCfdiRelacionado.read(new int[] { id }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\nCFDI relacionado (ID = " + id + ")");
                }
            }
        }

        if (moDbmsReceiptPayment.getFkFactoringBankId_n() != 0) {
            moAuxCfdDbmsDataReceptorFactoring = new SDataBizPartner();
            if (moAuxCfdDbmsDataReceptorFactoring.read(new int[] { moDbmsReceiptPayment.getFkFactoringBankId_n() }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\nReceptor de factoraje.");
            }
        }
        
        for (SDataReceiptPaymentPay pay : moDbmsReceiptPayment.getDbmsReceiptPaymentPays()) {
            SCfdPaymentEntry paymentEntry = pay.createCfdPaymentEntry(this, statement); // convenience variable
            maAuxCfdPaymentEntries.add(paymentEntry);
        }

        moAuxCfdDbmsDataReceptor = new SDataBizPartner();
        if (moAuxCfdDbmsDataReceptor.read(new int[] { moDbmsReceiptPayment.getFkBizPartnerId() }, statement) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\nReceptor.");
        }
    }
    
    /**
     * Read payment from financial records (journal vouchers).
     * It is assumed that member moDbmsDataCfd has been already read and properly setup.
     * @param statement DBMS statement.
     * @throws Exception 
     */
    @Deprecated
    private void readPaymentFromFinRecords(final Statement statement) throws Exception {
        if (moDbmsDataCfd.getFkXmlTypeId() != SDataConstantsSys.TRNS_TP_XML_CFDI_33) {
            throw new Exception("Este tipo de XML no está soportado (ID = " + moDbmsDataCfd.getFkXmlTypeId() + ")");
        }
        
        Statement statementAux = statement.getConnection().createStatement();

        // read 'Emisor' branch and business partner data objects:

        moAuxDbmsDataEmisorSucursal = new SDataBizPartnerBranch();
        moAuxDbmsDataEmisorSucursal.read(new int[] { moDbmsDataCfd.getFkCompanyBranchId_n() }, statementAux);

        moAuxDbmsDataEmisor = new SDataBizPartner();
        moAuxDbmsDataEmisor.read(new int[] { moAuxDbmsDataEmisorSucursal.getFkBizPartnerId() }, statementAux);

        String sql;
        ResultSet resultSet;
        
        int idReceptor = 0;
        cfd.ver33.DElementComprobante comprobante33 = null;
        
        if (!moDbmsDataCfd.getDocXml().isEmpty()) {
            comprobante33 = DCfdUtils.getCfdi33(moDbmsDataCfd.getDocXml());
            
            if (moDbmsDataCfd.getFkXmlStatusId() < SModSysConsts.TRNS_ST_DPS_ANNULED) {
                // get creation-modification-deletion user from financial record (journal voucher) entries:

                // find out who have edited this registry:
                sql = "SELECT DISTINCT fid_usr_new, fid_usr_edit, fid_usr_del "
                        + "FROM fin_rec_ety "
                        + "WHERE fid_cfd_n = " + moDbmsDataCfd.getPkCfdId() + " AND NOT b_del "
                        + "LIMIT 1;";
                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    // a second attempt to find out who have edited this registry:
                    sql = sql.replace("AND NOT b_del ", "AND b_del ");
                    resultSet = statement.executeQuery(sql);
                    if (!resultSet.next()) {
                        throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\n"
                                + "Partidas de pólizas contables relacionadas con el comprobante.");
                    }
                    else {
                        mnAuxFkUserNewId = resultSet.getInt("fid_usr_new");
                        mnAuxFkUserEditId = resultSet.getInt("fid_usr_edit");
                        mnAuxFkUserDeleteId = resultSet.getInt("fid_usr_del");
                    }
                }
                else {
                    mnAuxFkUserNewId = resultSet.getInt("fid_usr_new");
                    mnAuxFkUserEditId = resultSet.getInt("fid_usr_edit");
                    mnAuxFkUserDeleteId = resultSet.getInt("fid_usr_del");
                }

                // parse CFDI to extract auxiliar data:

                msAuxCfdConfirmacion = comprobante33.getAttConfirmacion().getString();
                msAuxCfdEmisorRegimenFiscal = comprobante33.getEltEmisor().getAttRegimenFiscal().getString();

                if (comprobante33.getEltOpcCfdiRelacionados() != null) {
                    msAuxCfdCfdiRelacionadosTipoRelacion = comprobante33.getEltOpcCfdiRelacionados().getAttTipoRelacion().getString();
                    msAuxCfdCfdiRelacionadoUuid = comprobante33.getEltOpcCfdiRelacionados().getEltCfdiRelacionados().get(0).getAttUuid().getString();
                    int id = STrnUtilities.getCfdIdByUuid(statement, msAuxCfdCfdiRelacionadoUuid);
                    if (id != 0) {
                        moAuxCfdDbmsDataCfdCfdiRelacionado = new SDataCfd();
                        if (moAuxCfdDbmsDataCfdCfdiRelacionado.read(new int[] { id }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\n"
                                    + "CFDI relacionado (ID = " + id + ")");
                        }
                    }
                }

                if (moDbmsDataCfd.getFkFactoringBankId_n() != 0) {
                    moAuxCfdDbmsDataReceptorFactoring = new SDataBizPartner();
                    moAuxCfdDbmsDataReceptorFactoring.read(new int[] { moDbmsDataCfd.getFkFactoringBankId_n() }, statementAux);
                }

                // extract complement:

                int numberPago = 0;
                int factoringFeeEntry = 0;
                int[] paymentEntryDocTypes = new int[] { SCfdPaymentEntryDoc.TYPE_INT, SCfdPaymentEntryDoc.TYPE_FEE, SCfdPaymentEntryDoc.TYPE_FEE_VAT };
                HashMap<String, SDataRecord> mapRecords = new HashMap<>(); // key = financial record PK as String; value = financial record

                if (comprobante33.getEltOpcComplemento() != null) {
                    for (DElement element : comprobante33.getEltOpcComplemento().getElements()) {
                        if (element instanceof cfd.ver33.crp10.DElementPagos) {
                            // get XML payments:

                            cfd.ver33.crp10.DElementPagos pagos33 = (cfd.ver33.crp10.DElementPagos) element;

                            for (cfd.ver33.crp10.DElementPagosPago pago33 : pagos33.getEltPagos()) {
                                numberPago++;

                                // read current payment's currency ID:

                                int currencyId;

                                sql = "SELECT id_cur "
                                        + "FROM erp.cfgu_cur "
                                        + "WHERE cur_key = '" + pago33.getAttMonedaP().getString() + "' AND NOT b_del "
                                        + "ORDER BY id_cur "
                                        + "LIMIT 1;";
                                resultSet = statement.executeQuery(sql);
                                if (!resultSet.next()) {
                                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n"
                                            + "Moneda " + pago33.getAttMonedaP().getString() + " del pago #" + numberPago + ".");
                                }
                                else {
                                    currencyId = resultSet.getInt(1);
                                }

                                // read current payment's financial record (journal voucher)

                                SDataRecord record;
                                int paymentEntryType = 0;
                                int[] accountCashDestKey = null;

                                sql = "SELECT ety_type, fid_rec_year, fid_rec_per, fid_rec_bkc, fid_rec_tp_rec, fid_rec_num, fid_acc_cash_cob_n, fid_acc_cash_acc_cash_n "
                                        + "FROM trn_cfd_fin_rec "
                                        + "WHERE id_cfd = " + moDbmsDataCfd.getPkCfdId() + " AND id_ety = " + numberPago + ";";
                                resultSet = statement.executeQuery(sql);
                                if (!resultSet.next()) {
                                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n"
                                            + "Póliza contable del pago #" + numberPago + ".");
                                }
                                else {
                                    String recordKey = SDataRecord.getRecordPrimaryKey(resultSet.getInt("fid_rec_year"), resultSet.getInt("fid_rec_per"), resultSet.getInt("fid_rec_bkc"), resultSet.getString("fid_rec_tp_rec"), resultSet.getInt("fid_rec_num"));
                                    record = mapRecords.get(recordKey);

                                    if (record == null) {
                                        record = new SDataRecord();
                                        record.setAuxReadHeaderOnly(true); // to reduce dramatically reading time, besides entries are useless
                                        record.read(new Object[] { resultSet.getInt("fid_rec_year"), resultSet.getInt("fid_rec_per"), resultSet.getInt("fid_rec_bkc"), resultSet.getString("fid_rec_tp_rec"), resultSet.getInt("fid_rec_num") }, statementAux);
                                        mapRecords.put(recordKey, record);
                                    }

                                    paymentEntryType = resultSet.getInt("ety_type");

                                    if (paymentEntryType == SCfdPaymentEntry.TYPE_FACTORING_FEE) {
                                        factoringFeeEntry++; // to guess (WTF!) type of document payment entry
                                    }

                                    if (resultSet.getInt("fid_acc_cash_cob_n") != 0 && resultSet.getInt("fid_acc_cash_acc_cash_n") != 0) {
                                        accountCashDestKey = new int[] { resultSet.getInt("fid_acc_cash_cob_n"), resultSet.getInt("fid_acc_cash_acc_cash_n") };
                                    }
                                }

                                // add XML payment:

                                SCfdPaymentEntry paymentEntry = new SCfdPaymentEntry(
                                        this,
                                        numberPago,
                                        paymentEntryType,
                                        pago33.getAttFechaPago().getDatetime(),
                                        pago33.getAttFormaDePagoP().getString(),
                                        currencyId,
                                        pago33.getAttMonedaP().getString(),
                                        pago33.getAttMonto().getDouble(),
                                        currencyId == SModSysConsts.CFGU_CUR_MXN ? 1.0 : pago33.getAttTipoCambioP().getDouble(),
                                        record);

                                paymentEntry.Operation = pago33.getAttNumOperacion().getString();
                                paymentEntry.AccountSrcFiscalId = pago33.getAttRfcEmisorCtaOrd().getString();
                                paymentEntry.AccountSrcNumber = pago33.getAttCtaOrdenante().getString();
                                paymentEntry.AccountSrcEntity = pago33.getAttNomBancoOrdExt().getString();
                                paymentEntry.AccountDestFiscalId = pago33.getAttRfcEmisorCtaBen().getString();
                                paymentEntry.AccountDestNumber = pago33.getAttCtaBeneficiario().getString();
                                paymentEntry.AccountDestKey = accountCashDestKey;

                                if (moAuxCfdDbmsDataReceptorFactoring != null) {
                                    paymentEntry.AuxFactoringBankId = moAuxCfdDbmsDataReceptorFactoring.getPkBizPartnerId();
                                    paymentEntry.AuxFactoringBankFiscalId = moAuxCfdDbmsDataReceptorFactoring.getFiscalId();
                                }

                                // get XML related documents of XML payments:

                                int numberDoctoRelacionado = 0;

                                for (cfd.ver33.crp10.DElementDoctoRelacionado doctoRelacionado33 : pago33.getEltDoctoRelacionados()) {
                                    numberDoctoRelacionado++;

                                    // read DPS:

                                    /*
                                    WARNING: 2018-05-29, Sergio Flores:
                                    This is a vulnerable data retrieval because there is not a strong, reliable, unequivocal reference to 'Documento Relacionado' data object!
                                    The document retrieval is done by its document number (there is no way of doing it other way, by now), instead of its primary key.
                                    */

                                    SThinDps thinDps;

                                    sql = "SELECT d.id_year, d.id_doc "
                                            + "FROM trn_dps AS d "
                                            + "INNER JOIN trn_cfd AS c ON d.id_year = c.fid_dps_year_n AND d.id_doc = c.fid_dps_doc_n "
                                            + "WHERE d.num_ser = '" + doctoRelacionado33.getAttSerie().getString() + "' AND "
                                            + "d.num = '" + doctoRelacionado33.getAttFolio().getString() + "' AND "
                                            + "c.uuid = '" + doctoRelacionado33.getAttIdDocumento().getString() + "' AND "
                                            + "NOT d.b_del;";
                                    resultSet = statement.executeQuery(sql);
                                    if (!resultSet.next()) {
                                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n"
                                                + "Comprobante " + STrnUtils.formatDocNumber(doctoRelacionado33.getAttSerie().getString(), doctoRelacionado33.getAttFolio().getString()) + ", "
                                                + doctoRelacionado33.getAttIdDocumento().getString() + ", del documento #" + numberDoctoRelacionado + ", del pago #" + numberPago + ".");
                                    }
                                    else {
                                        thinDps = new SThinDps();
                                        thinDps.read(new int[] { resultSet.getInt(1), resultSet.getInt(2) }, statementAux);
                                    }

                                    // add XML related document of XML payment:

                                    int paymentEntryDocType;

                                    switch (paymentEntryType) {
                                        case SCfdPaymentEntry.TYPE_STANDARD:
                                        case SCfdPaymentEntry.TYPE_FACTORING_PAY:
                                            paymentEntryDocType = SCfdPaymentEntryDoc.TYPE_PAY;
                                            break;

                                        case SCfdPaymentEntry.TYPE_FACTORING_FEE:
                                            /*
                                            WARNING: 2018-05-29, Sergio Flores:
                                            By now there is no way to identify if specific payment is for interests, fees or VAT of fees.
                                            It is assumed that the type that is more frecuent is interests, then fees, then VAT of fees.
                                            */
                                            int index = factoringFeeEntry - 1;  // XXX improve this, precision of data recovered is error prone!
                                            paymentEntryDocType = index < paymentEntryDocTypes.length ? paymentEntryDocTypes[index] : SCfdPaymentEntryDoc.TYPE_INT;
                                            break;

                                        default:
                                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION + "\nEl tipo de pago " + paymentEntryType + " es inválido.");
                                    }

                                    SCfdPaymentEntryDoc paymentEntryDoc = new SCfdPaymentEntryDoc(
                                            paymentEntry,
                                            thinDps,
                                            numberDoctoRelacionado,
                                            paymentEntryDocType,
                                            doctoRelacionado33.getAttNumParcialidad().getInteger(),
                                            doctoRelacionado33.getAttImpSaldoAnt().getDouble(),
                                            doctoRelacionado33.getAttImpPagado().getDouble(),
                                            currencyId == thinDps.getFkCurrencyId() ? 1.0 : doctoRelacionado33.getAttTipoCambioDR().getDouble());

                                    paymentEntryDoc.prepareTableRow();
                                    paymentEntry.PaymentEntryDocs.add(paymentEntryDoc);
                                }

                                paymentEntry.computeTotalPayments();
                                paymentEntry.prepareTableRow();
                                maAuxCfdPaymentEntries.add(paymentEntry);
                            }

                            break;
                        }
                    }
                }

                // read 'Receptor' business partner data object:

                idReceptor = SDataCfdPayment.getDbmsDataReceptorId(statement, moDbmsDataCfd.getPkCfdId());
            }
        }
        
        if (idReceptor == 0 && comprobante33 != null) {
            // CFDI could not be found in accounting or it has been canceled, so try to get receptor ID by its fiscal ID (as customer or bank):
            String rfc = comprobante33.getEltReceptor().getAttRfc().getString();
            String idFiscal = comprobante33.getEltReceptor().getAttNumRegIdTrib().getString();
            
            if (rfc.equals(DCfdConsts.RFC_GEN_NAC) || rfc.equals(DCfdConsts.RFC_GEN_INT)) {
                // there is no way of knowing what is the exact business partner:
                idReceptor = moAuxDbmsDataEmisor.getPkBizPartnerId();
            }
            else {
                // lookup business partner by fiscal ID:
                sql = "SELECT id_bp "
                        + "FROM erp.bpsu_bp "
                        + "WHERE fiscal_id = '" + rfc + "' "
                        + (idFiscal.isEmpty() ? "" : "AND fiscal_frg_id = '" + idFiscal + "' ")
                        + "AND (b_cus OR b_att_bank) AND NOT b_del "
                        + "ORDER BY id_bp DESC LIMIT 1;";
                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n"
                            + "Cliente o banco con RFC: '" + rfc + "'"
                            + (idFiscal.isEmpty() ? "" : ", ID fiscal: '" + idFiscal + "'") + ".");
                }
                else {
                    idReceptor = resultSet.getInt(1);
                }
            }
        }
        
        if (idReceptor == 0) {
            // force a receiver:
            idReceptor = moAuxDbmsDataEmisor.getPkBizPartnerId();
            SLibUtilities.printOutException(this, new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\n"
                    + "Partidas de pólizas contables relacionadas con el comprobante."));
        }

        moAuxCfdDbmsDataReceptor = new SDataBizPartner();
        moAuxCfdDbmsDataReceptor.read(new int[] { idReceptor }, statementAux);
    }
    
    /**
     * Save payment movements into financial records (journal vouchers).
     * @param connection
     * @param isRegistryNew
     * @throws Exception 
     */
    private void saveFinRecords(final Connection connection, final boolean isRegistryNew) throws Exception {
        if (!isRegistryNew) {
            // delete last CFDI accounting movements in database:
            mnAuxFkUserDeleteId = mnAuxFkUserEditId;
            deleteAccounting(connection);
            /*
            // delete last CFDI accounting movements IN MEMORY:
            for (SCfdPaymentEntry paymentEntry : maAuxCfdPaymentEntries) {
                for (SDataRecordEntry recordEntry : paymentEntry.DataRecord.getDbmsRecordEntries()) {
                    if (recordEntry.getFkCfdId_n() == moDbmsDataCfd.getPkCfdId()) {
                        recordEntry.setIsDeleted(true);
                    }
                }
            }
            */
        }

        int numberEntry = 0;
        HashMap<String, Integer> mapRecordsSortingPositions = new HashMap<>(); // key = financial record PK as String; value = next sorting position
        Statement statement = connection.createStatement();

        for (SCfdPaymentEntry paymentEntry : maAuxCfdPaymentEntries) {
            // define next sorting position for current financial record (journal voucher):

            int nextSortingPosition;

            if (!mapRecordsSortingPositions.containsKey(paymentEntry.DataRecord.getRecordPrimaryKey())) {
                // find out what is the next sorting position:
                nextSortingPosition = SDataRecord.getLastSortingPosition(connection, paymentEntry.DataRecord.getPrimaryKey()) + 1;
            }
            else {
                // get the last sorting position associated to current journal voucher:
                nextSortingPosition = mapRecordsSortingPositions.get(paymentEntry.DataRecord.getRecordPrimaryKey());
            }

            // save new financial record (journal voucher) entries:

            for (SDataRecordEntry entry : paymentEntry.AuxDbmsRecordEntries) {
                if (entry.getIsRegistryNew()) {
                    entry.setSortingPosition(nextSortingPosition++);
                    entry.setFkCfdId_n(moDbmsDataCfd.getPkCfdId());
                    if (entry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }
            }

            // preserve next sorting position for current financial record (journal voucher):

            mapRecordsSortingPositions.put(paymentEntry.DataRecord.getRecordPrimaryKey(), nextSortingPosition);

            // leave user trace into financial record (journal voucher) that it was modified:

            paymentEntry.DataRecord.saveField(connection, SDataRecord.FIELD_USER_EDIT_ID, paymentEntry.AuxUserId);

            String sql = "INSERT INTO trn_cfd_fin_rec VALUES (" + moDbmsDataCfd.getPkCfdId() + ", " + ++numberEntry + ", " + paymentEntry.EntryType + ", "
                    + paymentEntry.DataRecord.getPkYearId() + ", " + paymentEntry.DataRecord.getPkPeriodId() + ", " + paymentEntry.DataRecord.getPkBookkeepingCenterId() + ", "
                    + "'" + paymentEntry.DataRecord.getPkRecordTypeId() + "', " + paymentEntry.DataRecord.getPkNumberId() + ", "
                    + (paymentEntry.AccountDestKey == null ? "NULL, NULL" : "" + paymentEntry.AccountDestKey[0] + ", " + paymentEntry.AccountDestKey[1]) + ");";
            statement.execute(sql);
        }
    }
    
    /*
     * Implementation of methods of class SDataRegistry
     */

    @Override
    public void setPrimaryKey(Object pk) {
        moDbmsDataCfd.setPrimaryKey(pk);
    }

    @Override
    public Object getPrimaryKey() {
        return moDbmsDataCfd.getPrimaryKey();
    }

    @Override
    public void reset() {
        super.resetRegistry();

        moDbmsDataCfd = new SDataCfd();
        moAuxDbmsDataEmisor = null;
        moAuxDbmsDataEmisorSucursal = null;
        
        moDbmsReceiptPayment = null;
        
        msAuxCfdConfirmacion = "";
        msAuxCfdEmisorRegimenFiscal = "";
        moAuxCfdDbmsDataReceptor = null;
        moAuxCfdDbmsDataReceptorFactoring = null;
        msAuxCfdCfdiRelacionadosTipoRelacion = "";
        msAuxCfdCfdiRelacionadoUuid = "";
        moAuxCfdDbmsDataCfdCfdiRelacionado = null;
        maAuxCfdPaymentEntries = new ArrayList<>();
        
        mnAuxAnnulType = 0;
        
        mnAuxFkUserNewId = 0;
        mnAuxFkUserEditId = 0;
        mnAuxFkUserDeleteId = 0;
        
        //mbAuxIsProcessingCfdi = false; // prevent from reseting this flag!
        //mbAuxIsProcessingCfdiValidation = false; // prevent from reseting this flag!
        //mbAuxIsProcessingStorageOnly = false; // prevent from reseting this flag!
    }

    @Override
    @SuppressWarnings("Deprecated")
    public int read(Object pk, Statement statement) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            // read CFD:
            
            if (moDbmsDataCfd.read(pk, statement) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            
            // construct payment receipt in memory:
            
            if (moDbmsDataCfd.getFkReceiptPaymentId_n() != 0) {
                // since SIIE 3.2.191 payments receipts are stored in DBMS:
                readPaymentFromReceiptPayment(statement);
            }
            else {
                // up to SIIE 3.2.191 payments receipts where stored in financial records (journal vouchers):
                readPaymentFromFinRecords(statement);
            }
            
            // finish reading registry:

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_READ;
            }
            msDbmsError += "\n" + e.toString();
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            boolean isRegistryNew = moDbmsDataCfd.getPkCfdId() == 0;
            
            if (mbAuxIsProcessingStorageOnly) {
                if (isRegistryNew) {
                    msDbmsError = "El CFDI de recepción de pagos es nuevo, no puede ser regenerado.";
                    throw new Exception();
                }
                else {
                    moDbmsReceiptPayment = new SDataReceiptPayment();
                }
            }
            
            // save payment receipt:
            
            if (isRegistryNew) {
                moDbmsReceiptPayment = new SDataReceiptPayment();
            }
            
            if (moDbmsReceiptPayment != null) {
                moDbmsReceiptPayment.harvestCfdPayment(this);

                if (moDbmsReceiptPayment.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE + "\nTipo de registro: Comprobante de recepción de pagos.");
                }

                moDbmsDataCfd.setFkReceiptPaymentId_n(moDbmsReceiptPayment.getPkReceiptId()); // link payment receipt to CFD
            }
            
            // save CFD:
            
            if (mbAuxIsProcessingStorageOnly) {
                // update only FK of receipt of payment into CFD:
                
                if (moDbmsReceiptPayment == null) {
                    msDbmsError = "El registro de recepción de pago no existe.";
                    throw new Exception();
                }
                else if (moDbmsReceiptPayment.getPkReceiptId() == 0) {
                    msDbmsError = "El registro de recepción de pago no ha sido guardado aún.";
                    throw new Exception();
                }
                
                moDbmsDataCfd.saveField(connection, SDataCfd.FIELD_FK_RCP_PAY, moDbmsReceiptPayment.getPkReceiptId());
            }
            else {
                // save CFD:

                if (moDbmsDataCfd.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE + "\nTipo de registro: CFDI.");
                }

                if (isRegistryNew && moDbmsReceiptPayment != null) {
                    moDbmsReceiptPayment.saveField(connection, SDataReceiptPayment.FIELD_NUM, moDbmsDataCfd.getNumber(), 0);
                }

                // save records (journal vouchers) of all payment entries:

                saveFinRecords(connection, isRegistryNew);
            }

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_SAVE;
            }
            msDbmsError += "\n" + e.toString();
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return moDbmsDataCfd.getTimestamp();
    }

    @Override
    public int canAnnul(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConsts.UNDEFINED;

        try {
            if (testAnnulment(connection, "No se puede anular el comprobante: ")) {
                mnLastDbActionResult = SLibConstants.DB_CAN_ANNUL_YES;
            }
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_CAN_ANNUL_NO;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_CAN_ANNUL;
            }
            msDbmsError += "\n" + e.toString();
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int annul(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConsts.UNDEFINED;

        try {
            if (testAnnulment(connection, "No se puede anular el comprobante: ")) {
                if (moDbmsDataCfd.annul(connection) != SLibConstants.DB_ACTION_ANNUL_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_ANNUL);
                }
                else {
                    deleteAccounting(connection);

                    if (moDbmsReceiptPayment != null) {
                        moDbmsReceiptPayment.setAuxAnnulType(mnAuxAnnulType != 0 ? mnAuxAnnulType : SDataConstantsSys.TRNU_TP_DPS_ANN_NA);
                        moDbmsReceiptPayment.annul(connection);
                    }
                    
                    mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_OK;
                }
            }
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_ERROR;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_ANNUL;
            }
            msDbmsError += "\n" + e.toString();
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    /*
     * Implementation of methods of class SCfdXmlCfdi33 and SCfdXmlCfdi40
     */

    @Override
    public int getCfdType() { // CFDI 3.3 & 4.0
        return SDataConstantsSys.TRNS_TP_CFD_PAY_REC;
    }

    @Override
    public String getComprobanteVersion() { // CFDI 3.3 & 4.0
        return "" + DCfdConsts.CFDI_VER_40; // fixed value required as is in CFDI 4.4
    }

    @Override
    public String getComprobanteSerie() { // CFDI 3.3 & 4.0
        return moDbmsDataCfd.getSeries();
    }

    @Override
    public String getComprobanteFolio() { // CFDI 3.3 & 4.0
        return "" + moDbmsDataCfd.getNumber();
    }

    @Override
    public Date getComprobanteFecha() { // CFDI 3.3 & 4.0
        return moDbmsDataCfd.getTimestamp();
    }

    @Override
    public String getComprobanteFormaPago() { // CFDI 3.3 & 4.0
        return "";  // not required in CFDI 4.0 with Complement of Receipt of Payments 2.0!
    }

    @Override
    public String getComprobanteCondicionesPago() { // CFDI 3.3 & 4.0
        return "";  // not required in CFDI 4.0 with Complement of Receipt of Payments 2.0!
    }

    @Override
    public double getComprobanteSubtotal() { // CFDI 3.3 & 4.0
        return 0;   // fixed value required as is in CFDI 4.0 with Complement of Receipt of Payments 2.0
    }

    @Override
    public double getComprobanteDescuento() { // CFDI 3.3 & 4.0
        return 0;   // not required in CFDI 4.0 with Complement of Receipt of Payments 2.0!
    }

    @Override
    public String getComprobanteMoneda() { // CFDI 3.3 & 4.0
        return DCfdi40Catalogs.ClaveMonedaXxx;  // fixed value required as is in CFDI 4.0 with Complement of Receipt of Payments 2.0
    }

    @Override
    public double getComprobanteTipoCambio() { // CFDI 3.3 & 4.0
        return 0;   // not required in CFDI 4.0 with Complement of Receipt of Payments 2.0!
    }

    @Override
    public double getComprobanteTotal() { // CFDI 3.3 & 4.0
        return 0;   // fixed value required as is in CFDI 4.0 with Complement of Receipt of Payments 2.0
    }

    @Override
    public String getComprobanteTipoComprobante() { // CFDI 3.3 & 4.0
        return DCfdi40Catalogs.CFD_TP_P; // fixed value required as is in CFDI 4.0 with Complement of Receipt of Payments 2.0
    }
    
    @Override
    public String getComprobanteExportacion() { // CFDI 4.0
        return DCfdi40Catalogs.ClaveExportacionNoAplica;
    }

    @Override
    public String getComprobanteMetodoPago() { // CFDI 3.3 & 4.0
        return ""; // not required in CFDI 4.0 with Complement of Receipt of Payments 2.0!
    }

    @Override
    public String getComprobanteLugarExpedicion() { // CFDI 3.3 & 4.0
        return moAuxDbmsDataEmisorSucursal.getDbmsBizPartnerBranchAddressOfficial().getZipCode();
    }

    @Override
    public String getComprobanteConfirmacion() { // CFDI 3.3 & 4.0
        return msAuxCfdConfirmacion;
    }
    
    @Override
    public String getCfdiRelacionados33TipoRelacion() { // CFDI 3.3
        return msAuxCfdCfdiRelacionadosTipoRelacion;
    }

    @Override
    public ArrayList<String> getCfdiRelacionados33() { // CFDI 3.3
        ArrayList<String> cfdis = null;
        
        if (!msAuxCfdCfdiRelacionadoUuid.isEmpty()) {
            cfdis = new ArrayList<>();
            cfdis.add(msAuxCfdCfdiRelacionadoUuid);
        }
        
        return cfdis;
    }
    
    @Override
    public STrnCfdRelated getCfdiRelacionados() { // CFDI 4.0
        STrnCfdRelated rd = new STrnCfdRelated();
        if (!msAuxCfdCfdiRelacionadoUuid.isEmpty()) {
            rd.addRelatedDocument(msAuxCfdCfdiRelacionadosTipoRelacion, msAuxCfdCfdiRelacionadoUuid);
        }
        return rd;
    }
    
    @Override
    public DElement getElementInformacionGlobal() { // CFDI 4.0
        return null;
    }
    
    @Override
    public int getEmisorId() { // CFDI 3.3 & 4.0
        return moAuxDbmsDataEmisor.getPkBizPartnerId();
    }

    @Override
    public int getEmisorSucursalId() { // CFDI 3.3 & 4.0
        return moAuxDbmsDataEmisorSucursal.getPkBizPartnerBranchId();
    }

    @Override
    public String getEmisorRegimenFiscal() { // CFDI 3.3 & 4.0
        return msAuxCfdEmisorRegimenFiscal;
    }

    @Override
    public int getReceptorId() { // CFDI 3.3 & 4.0
        return getEffectiveReceptor().getPkBizPartnerId();
    }

    @Override
    public int getReceptorSucursalId() { // CFDI 3.3 & 4.0
        return getEffectiveReceptor().getDbmsBizPartnerBranchHq().getPkBizPartnerBranchId();
    }

    @Override
    public String getReceptorResidenciaFiscal() { // CFDI 3.3 & 4.0
        return getEffectiveReceptor().getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial().getDbmsDataCountry().getCountryCode();
    }

    @Override
    public String getReceptorNumRegIdTrib() { // CFDI 3.3 & 4.0
        return getEffectiveReceptor().getFiscalFrgId();
    }

    @Override
    public String getReceptorUsoCFDI() { // CFDI 3.3 & 4.0
        return DCfdi40Catalogs.ClaveUsoCfdiPagos; // fixed value required as is in CFDI 4.0 with Complement of Receipt of Payments 2.0
    }
    
    @Override
    public String getReceptorRegimenFiscal() { // CFDI 3.3 & 4.0
        return moDbmsReceiptPayment.getTaxRegimeCodeReceptor();
    }

    @Override
    public int getDestinatarioId() { // CFDI 3.3 & 4.0
        return SLibConstants.UNDEFINED; // not required in CFDI 4.0 with Complement of Receipt of Payments 2.0!
    }

    @Override
    public int getDestinatarioSucursalId() { // CFDI 3.3 & 4.0
        return SLibConstants.UNDEFINED; // not required in CFDI 4.0 with Complement of Receipt of Payments 2.0!
    }

    @Override
    public int getDestinatarioDomicilioId() { // CFDI 3.3 & 4.0
        return SLibConstants.UNDEFINED; // not required in CFDI 4.0 with Complement of Receipt of Payments 2.0!
    }

    @Override
    public ArrayList<SCfdDataConcepto> getElementsConcepto() throws Exception { // CFDI 3.3 & 4.0
        SCfdDataConcepto concepto = new SCfdDataConcepto(SDataConstantsSys.TRNS_TP_CFD_PAY_REC);
        concepto.setClaveProdServ(DCfdi40Catalogs.ClaveProdServServsFacturacion);
        concepto.setNoIdentificacion("");
        concepto.setCantidad(1);
        concepto.setClaveUnidad(DCfdi40Catalogs.ClaveUnidadAct);
        concepto.setUnidad("");
        concepto.setDescripcion(DCfdi40Catalogs.ConceptoPago);
        concepto.setValorUnitario(0);
        concepto.setImporte(0);
        concepto.setDescuento(0);
        concepto.setObjetoImpuesto(DCfdi40Catalogs.ClaveObjetoImpNo);

        ArrayList<SCfdDataConcepto> conceptos = new ArrayList<>();
        conceptos.add(concepto);

        return conceptos;
    }

    @Override
    public ArrayList<SCfdDataImpuesto> getElementsImpuestos(float cfdiVersion) { // CFDI 3.3 & 4.0
        return null;    // not required in CFDI 4.0 with Complement of Receipt of Payments 2.0!
    }

    @Override
    public DElement getElementComplemento() throws Exception { // CFDI 3.3 & 4.0
        DElementComplemento complemento = new DElementComplemento();

        DElementPagos pagos = new DElementPagos();
        
        // Nodo Totales:
        
        DElementTotales totales = pagos.getEltTotales();
        if (moDbmsReceiptPayment.getTotalRetainedVat() != 0) { 
            totales.getAttTotalRetencionesIVA().setDouble(moDbmsReceiptPayment.getTotalRetainedVat()); 
        }
        if (moDbmsReceiptPayment.getTotalRetainedIncomeTax() != 0) { 
            totales.getAttTotalRetencionesISR().setDouble(moDbmsReceiptPayment.getTotalRetainedIncomeTax()); 
        }
        if (moDbmsReceiptPayment.getTotalRetainedSpecialTax() != 0) { 
            totales.getAttTotalRetencionesIEPS().setDouble(moDbmsReceiptPayment.getTotalRetainedSpecialTax()); 
        }
        if (moDbmsReceiptPayment.getTotalChargedVat16Base() != 0) { 
            totales.getAttTotalTrasladosBaseIVA16().setDouble(moDbmsReceiptPayment.getTotalChargedVat16Base()); 
        }
        if (moDbmsReceiptPayment.getTotalChargedVat16Tax()!= 0) { 
            totales.getAttTotalTrasladosImpuestoIVA16().setDouble(moDbmsReceiptPayment.getTotalChargedVat16Tax()); 
        }
        if (moDbmsReceiptPayment.getTotalChargedVat08Base()!= 0) { 
            totales.getAttTotalTrasladosBaseIVA8().setDouble(moDbmsReceiptPayment.getTotalChargedVat08Base()); 
        }
        if (moDbmsReceiptPayment.getTotalChargedVat08Tax() != 0) { 
            totales.getAttTotalTrasladosImpuestoIVA8().setDouble(moDbmsReceiptPayment.getTotalChargedVat08Tax()); 
        }
        if (moDbmsReceiptPayment.getTotalChargedVat00Base() != 0) { 
            totales.getAttTotalTrasladosBaseIVA0().setDouble(moDbmsReceiptPayment.getTotalChargedVat00Base()); 
            totales.getAttTotalTrasladosImpuestoIVA0().setDouble(moDbmsReceiptPayment.getTotalChargedVat00Tax());
            totales.getAttTotalTrasladosImpuestoIVA0().setCanBeZero(true); 
        }
        if (moDbmsReceiptPayment.getTotalChargedVatExemptBase() != 0) { 
            totales.getAttTotalTrasladosBaseIVAExento().setDouble(moDbmsReceiptPayment.getTotalChargedVatExemptBase()); 
        }
        totales.getAttMontoTotalPagos().setDouble(moDbmsReceiptPayment.getTotalPaymentLocal_r());
        
        // Nodo Pagos:
        
        for (SCfdPaymentEntry paymentEntry : maAuxCfdPaymentEntries) {
            DElementPagosPago pago = new DElementPagosPago();
            pago.getAttFechaPago().setDatetime(paymentEntry.PaymentDate);
            pago.getAttFormaDePagoP().setString(paymentEntry.PaymentWay);
            pago.getAttMonedaP().setString(paymentEntry.CurrencyKey);
            if (paymentEntry.CurrencyId != SModSysConsts.CFGU_CUR_MXN) {
                pago.getAttTipoCambioP().setDouble(paymentEntry.ExchangeRate);
            }
            else {
                pago.getAttTipoCambioP().setDouble(1);
                pago.getAttTipoCambioP().setDecimals(0);
            }
            pago.getAttMonto().setDouble(paymentEntry.Amount);
            pago.getAttNumOperacion().setString(paymentEntry.Operation);
            pago.getAttRfcEmisorCtaOrd().setString(paymentEntry.AccountSrcFiscalId);
            pago.getAttNomBancoOrdExt().setString(paymentEntry.AccountSrcEntity);
            pago.getAttCtaOrdenante().setString(paymentEntry.AccountSrcNumber);
            pago.getAttRfcEmisorCtaBen().setString(paymentEntry.AccountDestFiscalId);
            pago.getAttCtaBeneficiario().setString(paymentEntry.AccountDestNumber);
            pago.getAttTipoCadPago().setString(""); // XXX not supported yet!
            pago.getAttCertPago().setString("");    // XXX not supported yet!
            pago.getAttCadPago().setString("");     // XXX not supported yet!
            pago.getAttSelloPago().setString("");   // XXX not supported yet!
            
            // Nodo DocumentosRelacionados:
            
            for (SCfdPaymentEntryDoc paymentEntryDoc : paymentEntry.PaymentEntryDocs) {
                DElementDoctoRelacionado doctoRelacionado = new DElementDoctoRelacionado();
                doctoRelacionado.getAttIdDocumento().setString(paymentEntryDoc.ThinDps.getThinCfd().getUuid());
                doctoRelacionado.getAttSerie().setString(paymentEntryDoc.ThinDps.getNumberSeries());
                doctoRelacionado.getAttFolio().setString(paymentEntryDoc.ThinDps.getNumber());
                doctoRelacionado.getAttMonedaDR().setString(paymentEntryDoc.ThinDps.getDbmsCurrencyKey());
                if (!pago.getAttMonedaP().getString().equals(doctoRelacionado.getAttMonedaDR().getString())) {
                    doctoRelacionado.getAttEquivalenciaDR().setDouble(paymentEntryDoc.ExchangeRate);
                }
                else {
                    doctoRelacionado.getAttEquivalenciaDR().setDouble(1);
                    doctoRelacionado.getAttEquivalenciaDR().setDecimals(10);
                }
                doctoRelacionado.getAttNumParcialidad().setInteger(paymentEntryDoc.Installment);
                doctoRelacionado.getAttImpSaldoAnt().setDouble(paymentEntryDoc.DocBalancePrev);
                doctoRelacionado.getAttImpPagado().setDouble(paymentEntryDoc.DocPayment);
                doctoRelacionado.getAttImpSaldoInsoluto().setDouble(paymentEntryDoc.DocBalancePend);
                doctoRelacionado.getAttObjetoImpDR().setString(paymentEntryDoc.ReceiptPaymentPayDocTaxes != null && paymentEntryDoc.ReceiptPaymentPayDocTaxes.size() > 0 ? 
                        DCfdi40Catalogs.ClaveObjetoImpSí : DCfdi40Catalogs.ClaveObjetoImpNo);
                
                //  Nodo ImpuestosDR:
                
                if (doctoRelacionado.getAttObjetoImpDR().getString().equals(DCfdi40Catalogs.ClaveObjetoImpSí)) { 
                    DElementImpuestosDR impuestosDR = new DElementImpuestosDR(); 
                    ArrayList<DElementRetencionDR> arrRetencionDR = new ArrayList<>();
                    ArrayList<DElementTrasladoDR> arrTrasladoDR = new ArrayList<>();

                    for (SDataReceiptPaymentPayDocTax docTax : paymentEntryDoc.ReceiptPaymentPayDocTaxes) {
                        if (docTax.getBase() != 0.0) {
                            if (docTax.getFkTaxTypeId() == SModSysConsts.FINS_TP_TAX_RETAINED) {
                                DElementRetencionDR retDR = new DElementRetencionDR();
                                retDR.getAttBaseDR().setDouble(docTax.getBase());
                                retDR.getAttImpuestoDR().setString("00" + docTax.getFkCfdTaxId());
                                retDR.getAttTipoFactorDR().setString(docTax.getFactorCode().equals("E") ? "Exento" : "Tasa");
                                retDR.getAttTasaOCuotaDR().setDouble(docTax.getRate());
                                retDR.getAttImporteDR().setDouble(docTax.getTax());
                                arrRetencionDR.add(retDR);
                            }
                            else if (docTax.getFkTaxTypeId() == SModSysConsts.FINS_TP_TAX_CHARGED) {
                                DElementTrasladoDR trasDR = new DElementTrasladoDR();
                                trasDR.getAttBaseDR().setDouble(docTax.getBase());
                                trasDR.getAttImpuestoDR().setString("00" + docTax.getFkCfdTaxId());
                                trasDR.getAttTipoFactorDR().setString(docTax.getFactorCode().equals("E") ? "Exento" : "Tasa");
                                trasDR.getAttTasaOCuotaDR().setDouble(docTax.getRate());
                                trasDR.getAttImporteDR().setDouble(docTax.getTax());
                                if (docTax.getFactorCode().equals("E")) {
                                    trasDR.getAttTasaOCuotaDR().setCanBeZero(false);
                                    trasDR.getAttImporteDR().setCanBeZero(false);
                                }
                                arrTrasladoDR.add(trasDR);
                            }
                        }
                    }
                    if (arrRetencionDR.size() > 0) {
                        DElementRetencionesDR retenciones = new DElementRetencionesDR();
                        retenciones.getEltRetencionDRs().addAll(arrRetencionDR);
                        impuestosDR.setEltRetencionesDR(retenciones);
                    }
                    if (arrTrasladoDR.size() > 0) {
                        DElementTrasladosDR traslados = new DElementTrasladosDR();
                        traslados.getEltTrasladoDRs().addAll(arrTrasladoDR);
                        impuestosDR.setEltTrasladosDR(traslados);
                    }
                    
                    doctoRelacionado.setEltImpuestosDR(impuestosDR);
                }
                
                pago.getEltDoctoRelacionados().add(doctoRelacionado);
            }
            
            // Nodo ImpuestosP:
            
            if (paymentEntry.ReceiptPaymentPayTaxes != null && paymentEntry.ReceiptPaymentPayTaxes.size() > 0) {
                DElementImpuestosP impuestosP = new DElementImpuestosP();
                ArrayList<DElementRetencionP> arrRetencionP = new ArrayList<>();
                ArrayList<DElementTrasladoP> arrTrasladoP = new ArrayList<>();
                
                for (SDataReceiptPaymentPayTax payTax : paymentEntry.ReceiptPaymentPayTaxes) {
                    if (payTax.getFkTaxTypeId() == SModSysConsts.FINS_TP_TAX_RETAINED){
                        DElementRetencionP retP = new DElementRetencionP();
                        retP.getAttImpuestoP().setString("00" + payTax.getFkCfdTaxId());
                        retP.getAttImporteP().setDouble(payTax.getTax());
                        arrRetencionP.add(retP);
                    }
                    else if (payTax.getFkTaxTypeId() == SModSysConsts.FINS_TP_TAX_CHARGED) {
                        DElementTrasladoP trasP = new DElementTrasladoP();
                        trasP.getAttBaseP().setDouble(payTax.getBase());
                        trasP.getAttImpuestoP().setString("00" + payTax.getFkCfdTaxId());
                        trasP.getAttTipoFactorP().setString(payTax.getFactorCode().equals("E") ? "Exento" : "Tasa");
                        trasP.getAttTasaOCuotaP().setDouble(payTax.getRate());
                        trasP.getAttImporteP().setDouble(payTax.getTax());
                        if (payTax.getFactorCode().equals("E")) {
                            trasP.getAttTasaOCuotaP().setCanBeZero(false);
                            trasP.getAttImporteP().setCanBeZero(false);
                        }
                        arrTrasladoP.add(trasP);
                    }
                }
                if (arrRetencionP.size() > 0) {
                    DElementRetencionesP retenciones = new DElementRetencionesP();
                    retenciones.getEltRetencionPs().addAll(arrRetencionP);
                    impuestosP.setEltRetencionesP(retenciones);
                }
                if (arrTrasladoP.size() > 0) {
                    DElementTrasladosP traslados = new DElementTrasladosP();
                    traslados.getEltTrasladoPs().addAll(arrTrasladoP);
                    impuestosP.setEltTrasladosP(traslados);
                }
                
                pago.setEltImpuestosP(impuestosP);
            }
            
            pagos.getEltPagos().add(pago);
        }
        
        ((DElementComplemento) complemento).getElements().add(pagos);
        
        return complemento;
    }

    @Override
    public DElement getElementAddenda() { // CFDI 3.3 & 4.0
        return null; // not implemented yet in CFDI 4.0 with Complement of Receipt of Payments 2.0!
    }
}
