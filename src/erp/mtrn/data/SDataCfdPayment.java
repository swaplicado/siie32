/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import cfd.DCfdConsts;
import cfd.DCfdUtils;
import cfd.DElement;
import cfd.ver33.DCfdi33Catalogs;
import cfd.ver33.DElementComplemento;
import cfd.ver33.crp10.DElementDoctoRelacionado;
import cfd.ver33.crp10.DElementPagos;
import cfd.ver33.crp10.DElementPagosPago;
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
 * @author Sergio Flores
 */
public class SDataCfdPayment extends erp.lib.data.SDataRegistry implements java.io.Serializable, erp.cfd.SCfdXmlCfdi33 {
    
    public static final String NAME = "CFDI complemento recepción pagos";
    
    // members that belong to SDataCfd:
    protected SDataCfd moDbmsDataCfd;
    protected SDataBizPartner moAuxDbmsDataEmisor;
    protected SDataBizPartnerBranch moAuxDbmsDataEmisorSucursal;
    
    // members that belong to XML of CFDI:
    protected String msAuxCfdConfirmacion;
    protected String msAuxCfdEmisorRegimenFiscal;
    protected SDataBizPartner moAuxCfdDbmsDataReceptor;
    protected String msAuxCfdCfdiRelacionadosTipoRelacion;
    protected SDataCfd moAuxCfdDbmsDataCfdCfdiRelacionado;
    protected ArrayList<SCfdPaymentEntry> maAuxCfdPaymentEntries;
    
    // members that belong to SDataRecord:
    protected int mnAuxFkUserNewId;
    protected int mnAuxFkUserEditId;
    protected int mnAuxFkUserDeleteId;
    
    // auxiliar members:
    protected boolean mbAuxIsProcessingValidation;
    
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
    
    private boolean testAnnulment(java.sql.Connection connection, java.lang.String msg) throws java.lang.Exception {
        moDbmsDataCfd.setAuxIsProcessingValidation(mbAuxIsProcessingValidation);
        
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
    
    public void setAuxCfdConfirmacion(String s) { msAuxCfdConfirmacion = s; }
    public void setAuxCfdEmisorRegimenFiscal(String s) { msAuxCfdEmisorRegimenFiscal = s; }
    public void setAuxCfdDbmsDataReceptor(SDataBizPartner o) { moAuxCfdDbmsDataReceptor = o; }
    public void setAuxCfdCfdiRelacionadosTipoRelacion(String s) { msAuxCfdCfdiRelacionadosTipoRelacion = s; }
    public void setAuxCfdDbmsDataCfdCfdiRelacionado(SDataCfd o) { moAuxCfdDbmsDataCfdCfdiRelacionado = o; }
    
    public String getAuxCfdConfirmacion() { return msAuxCfdConfirmacion; }
    public String getAuxCfdEmisorRegimenFiscal() { return msAuxCfdEmisorRegimenFiscal; }
    public SDataBizPartner getAuxCfdDbmsDataReceptor() { return moAuxCfdDbmsDataReceptor; }
    public String getAuxCfdCfdiRelacionadosTipoRelacion() { return msAuxCfdCfdiRelacionadosTipoRelacion; }
    public SDataCfd getAuxCfdDbmsDataCfdCfdiRelacionado() { return moAuxCfdDbmsDataCfdCfdiRelacionado; }
    public ArrayList<SCfdPaymentEntry> getAuxCfdPaymentEntries() { return maAuxCfdPaymentEntries; }
    
    public void setFkUserNewId(int n) { mnAuxFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnAuxFkUserEditId = n; }
    public void setFkUserDeleteId(int n) {mnAuxFkUserDeleteId = n; }
    
    public int getFkUserNewId() { return mnAuxFkUserNewId; }
    public int getFkUserEditId() { return mnAuxFkUserEditId; }
    public int getFkUserDeleteId() { return mnAuxFkUserDeleteId; }
    
    public void setAuxIsProcessingValidation(boolean b) { mbAuxIsProcessingValidation = b; }
    
    public boolean getAuxIsProcessingValidation() { return mbAuxIsProcessingValidation; }
    
    public void copyCfdMembers(final SDataCfdPayment sourceCfdPayment) {
        this.msAuxCfdConfirmacion = sourceCfdPayment.msAuxCfdConfirmacion;
        this.msAuxCfdEmisorRegimenFiscal = sourceCfdPayment.msAuxCfdEmisorRegimenFiscal;
        this.moAuxCfdDbmsDataReceptor = sourceCfdPayment.moAuxCfdDbmsDataReceptor;
        this.msAuxCfdCfdiRelacionadosTipoRelacion = sourceCfdPayment.msAuxCfdCfdiRelacionadosTipoRelacion;
        this.moAuxCfdDbmsDataCfdCfdiRelacionado = sourceCfdPayment.moAuxCfdDbmsDataCfdCfdiRelacionado;
        this.maAuxCfdPaymentEntries.addAll(sourceCfdPayment.maAuxCfdPaymentEntries);
    }
    
    public void deleteRecord(java.sql.Connection connection) throws java.lang.Exception {
        String sql;
        Statement statement = connection.createStatement();
        
        sql = "DELETE FROM trn_cfd_fin_rec "
                + "WHERE id_cfd = " + moDbmsDataCfd.getPkCfdId() + ";";
        statement.execute(sql);

        sql = "UPDATE fin_rec_ety SET b_del = 1, fid_usr_edit = " + (mnAuxFkUserEditId != 0 ? mnAuxFkUserEditId : SUtilConsts.USR_NA_ID) + ", ts_del = NOW() "
                + "WHERE fid_cfd_n = " + moDbmsDataCfd.getPkCfdId() + " AND NOT b_del;";
        statement.execute(sql);
    }

    public static int getDbmsDataReceptorId(final Statement statement, final int idCfdi) throws Exception {
        int idReceptor = 0;
        
        String sql = "SELECT DISTINCT re.fid_bp_nr "
                + "FROM fin_rec AS r "
                + "INNER JOIN fin_rec_ety re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "WHERE NOT r.b_del AND NOT re.b_del AND re.fid_cfd_n = " + idCfdi + ";";
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            idReceptor = resultSet.getInt(1);
        }
        
        return idReceptor;
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
        
        msAuxCfdConfirmacion = "";
        msAuxCfdEmisorRegimenFiscal = "";
        moAuxCfdDbmsDataReceptor = null;
        msAuxCfdCfdiRelacionadosTipoRelacion = "";
        moAuxCfdDbmsDataCfdCfdiRelacionado = null;
        maAuxCfdPaymentEntries = new ArrayList<>();
        
        mnAuxFkUserNewId = 0;
        mnAuxFkUserEditId = 0;
        mnAuxFkUserDeleteId = 0;
        
        mbAuxIsProcessingValidation = false;
    }

    @Override
    public int read(Object pk, Statement statement) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            // read CFD:
            
            if (moDbmsDataCfd.read(pk, statement) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                Statement statementAux = statement.getConnection().createStatement();
                
                // read 'Emisor' branch and business partner data objects:
                
                moAuxDbmsDataEmisorSucursal = new SDataBizPartnerBranch();
                moAuxDbmsDataEmisorSucursal.read(new int[] { moDbmsDataCfd.getFkCompanyBranchId_n() }, statementAux);
                
                moAuxDbmsDataEmisor = new SDataBizPartner();
                moAuxDbmsDataEmisor.read(new int[] { moAuxDbmsDataEmisorSucursal.getFkBizPartnerId() }, statementAux);
                
                String sql;
                ResultSet resultSet;
                
                // get creation-modification-deletion user from record (journal voucher) entries:
                
                sql = "SELECT fid_usr_new, fid_usr_edit, fid_usr_del "
                        + "FROM fin_rec_ety "
                        + "WHERE fid_cfd_n = " + moDbmsDataCfd.getPkCfdId() + " AND NOT b_del "
                        + "LIMIT 1;";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    mnAuxFkUserNewId = resultSet.getInt(1);
                    mnAuxFkUserEditId = resultSet.getInt(2);
                    mnAuxFkUserDeleteId = resultSet.getInt(3);
                }
                
                // parse CFDI to extract auxiliar data:
                
                if (!moDbmsDataCfd.getDocXml().isEmpty()) {
                    cfd.ver33.DElementComprobante comprobante = DCfdUtils.getCfdi33(moDbmsDataCfd.getDocXml());

                    msAuxCfdConfirmacion = comprobante.getAttConfirmacion().getString();
                    msAuxCfdEmisorRegimenFiscal = comprobante.getEltEmisor().getAttRegimenFiscal().getString();

                    if (comprobante.getEltOpcCfdiRelacionados() != null) {
                        msAuxCfdCfdiRelacionadosTipoRelacion = comprobante.getEltOpcCfdiRelacionados().getAttTipoRelacion().getString();
                        int id = STrnUtilities.getCfdIdByUuid(statement, comprobante.getEltOpcCfdiRelacionados().getEltCfdiRelacionados().get(0).getAttUuid().getString());
                        if (id != SLibConstants.UNDEFINED) {
                            moAuxCfdDbmsDataCfdCfdiRelacionado = new SDataCfd();
                            if (moAuxCfdDbmsDataCfdCfdiRelacionado.read(new int[] { id }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
                            }
                        }
                    }

                    // extract complement:

                    int numberEntry = 0;

                    if (comprobante.getEltOpcComplemento() != null) {
                        for (DElement element : comprobante.getEltOpcComplemento().getElements()) {
                            if (element instanceof DElementPagos) {
                                // get XML payments:

                                DElementPagos pagos = (DElementPagos) element;

                                for (DElementPagosPago pago : pagos.getEltPagos()) {
                                    numberEntry++;

                                    // read current payment's currency ID:

                                    int currencyId;

                                    sql = "SELECT id_cur "
                                            + "FROM erp.cfgu_cur "
                                            + "WHERE cur_key = '" + pago.getAttMonedaP().getString() + "' AND NOT b_del "
                                            + "ORDER BY id_cur "
                                            + "LIMIT 1;";
                                    resultSet = statement.executeQuery(sql);
                                    if (!resultSet.next()) {
                                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n"
                                                + "Moneda " + pago.getAttMonedaP().getString() + " del pago #" + numberEntry + ".");
                                    }
                                    else {
                                        currencyId = resultSet.getInt(1);
                                    }

                                    // read current payment's record (journal voucher)

                                    SDataRecord record;
                                    int[] accountCashKey;

                                    sql = "SELECT fid_rec_year, fid_rec_per, fid_rec_bkc, fid_rec_tp_rec, fid_rec_num, fid_acc_cash_cob, fid_acc_cash_acc_cash "
                                            + "FROM trn_cfd_fin_rec "
                                            + "WHERE id_cfd = " + moDbmsDataCfd.getPkCfdId() + " AND id_ety = " + numberEntry + ";";
                                    resultSet = statement.executeQuery(sql);
                                    if (!resultSet.next()) {
                                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n"
                                                + "Póliza contable del pago #" + numberEntry + ".");
                                    }
                                    else {
                                        record = new SDataRecord();
                                        record.read(new Object[] { resultSet.getInt("fid_rec_year"), resultSet.getInt("fid_rec_per"), resultSet.getInt("fid_rec_bkc"), resultSet.getString("fid_rec_tp_rec"), resultSet.getInt("fid_rec_num") }, statementAux);
                                        accountCashKey = new int[] { resultSet.getInt("fid_acc_cash_cob"), resultSet.getInt("fid_acc_cash_acc_cash") };
                                    }

                                    // add XML payment:

                                    SCfdPaymentEntry paymentEntry = new SCfdPaymentEntry(
                                            numberEntry, 
                                            pago.getAttFechaPago().getDatetime(), 
                                            pago.getAttFormaDePagoP().getString(), 
                                            currencyId, 
                                            pago.getAttMonedaP().getString(), 
                                            pago.getAttMonto().getDouble(), 
                                            currencyId == SModSysConsts.CFGU_CUR_MXN ? 1.0 : pago.getAttTipoCambioP().getDouble(), 
                                            record, 
                                            this);

                                    paymentEntry.Operation = pago.getAttNumOperacion().getString();
                                    paymentEntry.AccountSrcFiscalId = pago.getAttRfcEmisorCtaOrd().getString();
                                    paymentEntry.AccountSrcNumber = pago.getAttCtaOrdenante().getString();
                                    paymentEntry.AccountSrcEntity = pago.getAttNomBancoOrdExt().getString();
                                    paymentEntry.AccountDesFiscalId = pago.getAttRfcEmisorCtaBen().getString();
                                    paymentEntry.AccountDesNumber = pago.getAttCtaBeneficiario().getString();
                                    paymentEntry.AccountDesKey = accountCashKey;

                                    // get XML related documents of XML payments:

                                    int numberDoc = 0;

                                    for (DElementDoctoRelacionado doctoRelacionado : pago.getEltDoctoRelacionados()) {
                                        numberDoc++;

                                        // read DPS:
                                        // NOTE: 2018-05-29, Sergio Flores: This is a vulnerable data retrieval because there is not a strong, reliable, unequivocal reference to 'Documento Relacionado' data object!

                                        SDataDps dps;

                                        sql = "SELECT d.id_year, d.id_doc "
                                                + "FROM trn_dps AS d "
                                                + "INNER JOIN trn_cfd AS c ON d.id_year = c.fid_dps_year_n AND d.id_doc = c.fid_dps_doc_n "
                                                + "WHERE d.num_ser = '" + doctoRelacionado.getAttSerie().getString() + "' AND "
                                                + "d.num = '" + doctoRelacionado.getAttFolio().getString() + "' AND "
                                                + "c.uuid = '" + doctoRelacionado.getAttIdDocumento().getString() + "' AND "
                                                + "NOT d.b_del;";
                                        resultSet = statement.executeQuery(sql);
                                        if (!resultSet.next()) {
                                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP + "\n"
                                                    + "Comprobante " + STrnUtils.formatDocNumber(doctoRelacionado.getAttSerie().getString(), doctoRelacionado.getAttFolio().getString()) + ", "
                                                    + doctoRelacionado.getAttIdDocumento().getString() + ", del documento #" + numberDoc + ", del pago #" + numberEntry + ".");
                                        }
                                        else {
                                            dps = new SDataDps();
                                            dps.read(new int[] { resultSet.getInt(1), resultSet.getInt(2) }, statementAux);
                                        }

                                        // add XML related document of XML payment:

                                        SCfdPaymentEntryDoc paymentEntryDoc = new SCfdPaymentEntryDoc(
                                                numberDoc, 
                                                dps, 
                                                doctoRelacionado.getAttNumParcialidad().getInteger(), 
                                                doctoRelacionado.getAttImpSaldoAnt().getDouble(), 
                                                doctoRelacionado.getAttImpPagado().getDouble(), 
                                                currencyId == dps.getFkCurrencyId() ? 1.0 : doctoRelacionado.getAttTipoCambioDR().getDouble(), 
                                                paymentEntry);

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
                    
                    int idReceptor = getDbmsDataReceptorId(statement, moDbmsDataCfd.getPkCfdId());
                    
                    if (idReceptor == 0) {
                        // CFDI could not be found in accounting, so try to get receptor ID by its fiscal ID:
                        sql = "SELECT id_bp "
                                + "FROM erp.bpsu_bp "
                                + "WHERE fiscal_id = '" + comprobante.getEltReceptor().getAttRfc().getString() + "' "
                                + (comprobante.getEltReceptor().getAttNumRegIdTrib().getString().isEmpty() ? "" : "AND fiscal_frg_id = '" + comprobante.getEltReceptor().getAttNumRegIdTrib().getString() + "' ")
                                + "AND b_cus AND NOT b_del "
                                + "ORDER BY id_bp DESC LIMIT 1 ";
                        resultSet = statement.executeQuery(sql);
                        if (!resultSet.next()) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                    }

                    moAuxCfdDbmsDataReceptor = new SDataBizPartner();
                    moAuxCfdDbmsDataReceptor.read(new int[] { idReceptor }, statementAux);
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
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
            // save CFD:
            
            if (moDbmsDataCfd.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE);
            }
            else {
                deleteRecord(connection);
                
                // save records (journal vouchers) of all payment entries:
                
                int numberEntry = 0;
                HashMap<String, Integer> hmRecordsSortingPositions = new HashMap<>();
                Statement statement = connection.createStatement();
                
                for (SCfdPaymentEntry paymentEntry : maAuxCfdPaymentEntries) {
                    // define next sorting position for current record (journal voucher):
                    
                    int nextSortingPosition;
                    
                    if (hmRecordsSortingPositions.containsKey(paymentEntry.DataRecord.getRecordPrimaryKey())) {
                        nextSortingPosition = hmRecordsSortingPositions.get(paymentEntry.DataRecord.getRecordPrimaryKey());
                    }
                    else {
                        nextSortingPosition = paymentEntry.DataRecord.getLastSortingPosition() + 1;
                    }
                    
                    // save new record (journal voucher) entries:
                    
                    for (SDataRecordEntry entry : paymentEntry.AuxDbmsRecordEntries) {
                        if (entry.getIsRegistryNew()) {
                            entry.setSortingPosition(nextSortingPosition++);
                            entry.setFkCfdId_n(moDbmsDataCfd.getPkCfdId());
                            if (entry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                            }
                        }
                    }
                    
                    // preserve next sorting position for current record (journal voucher):
                    
                    hmRecordsSortingPositions.put(paymentEntry.DataRecord.getRecordPrimaryKey(), nextSortingPosition);
                    
                    // leave user trace into record (journal voucher) that it was modified:
                    
                    paymentEntry.DataRecord.setFkUserEditId(paymentEntry.AuxUserId);
                    if (paymentEntry.DataRecord.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                    
                    String sql = "INSERT INTO trn_cfd_fin_rec VALUES (" + moDbmsDataCfd.getPkCfdId() + ", " + ++numberEntry + ", "
                            + paymentEntry.DataRecord.getPkYearId() + ", " + paymentEntry.DataRecord.getPkPeriodId() + ", " + paymentEntry.DataRecord.getPkBookkeepingCenterId() + ", "
                            + "'" + paymentEntry.DataRecord.getPkRecordTypeId() + "', " + paymentEntry.DataRecord.getPkNumberId() + ", " + paymentEntry.AccountDesKey[0] + ", " + paymentEntry.AccountDesKey[1] + ");";
                    statement.execute(sql);
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
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
            if (testAnnulment(connection, "No se puede anular el documento: ")) {
                if (moDbmsDataCfd.annul(connection) != SLibConstants.DB_ACTION_ANNUL_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_ANNUL);
                }
                else {
                    deleteRecord(connection);

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
     * Implementation of methods of class SCfdXmlCfdi33
     */

    @Override
    public int getCfdType() {
        return SDataConstantsSys.TRNS_TP_CFD_PAY_REC;
    }

    @Override
    public String getComprobanteVersion() {
        return "" + DCfdConsts.CFDI_VER_33; // fixed value required as is in CFDI 3.3
    }

    @Override
    public String getComprobanteSerie() {
        return moDbmsDataCfd.getSeries();
    }

    @Override
    public String getComprobanteFolio() {
        return "" + moDbmsDataCfd.getNumber();
    }

    @Override
    public Date getComprobanteFecha() {
        return moDbmsDataCfd.getTimestamp();
    }

    @Override
    public String getComprobanteFormaPago() {
        return "";  // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public String getComprobanteCondicionesPago() {
        return "";  // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public double getComprobanteSubtotal() {
        return 0;   // fixed value required as is in CFDI 3.3 with Complement of Receipt of Payments 1.0
    }

    @Override
    public double getComprobanteDescuento() {
        return 0;   // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public String getComprobanteMoneda() {
        return DCfdi33Catalogs.ClaveMonedaXxx;  // fixed value required as is in CFDI 3.3 with Complement of Receipt of Payments 1.0
    }

    @Override
    public double getComprobanteTipoCambio() {
        return 0;   // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public double getComprobanteTotal() {
        return 0;   // fixed value required as is in CFDI 3.3 with Complement of Receipt of Payments 1.0
    }

    @Override
    public String getComprobanteTipoComprobante() {
        return DCfdi33Catalogs.CFD_TP_P;    // fixed value required as is in CFDI 3.3 with Complement of Receipt of Payments 1.0
    }

    @Override
    public String getComprobanteMetodoPago() {
        return "";  // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public String getComprobanteLugarExpedicion() {
        return moAuxDbmsDataEmisorSucursal.getDbmsBizPartnerBranchAddressOfficial().getZipCode();
    }

    @Override
    public String getComprobanteConfirmacion() {
        return msAuxCfdConfirmacion;
    }

    @Override
    public String getCfdiRelacionadosTipoRelacion() {
        return msAuxCfdCfdiRelacionadosTipoRelacion;
    }

    @Override
    public ArrayList<String> getCfdiRelacionados() {
        ArrayList<String> cfdis = null;
        
        if (moAuxCfdDbmsDataCfdCfdiRelacionado != null) {
            cfdis = new ArrayList<>();
            cfdis.add(moAuxCfdDbmsDataCfdCfdiRelacionado.getUuid());
        }
        
        return cfdis;
    }

    @Override
    public int getEmisorId() {
        return moAuxDbmsDataEmisor.getPkBizPartnerId();
    }

    @Override
    public int getEmisorSucursalId() {
        return moAuxDbmsDataEmisorSucursal.getPkBizPartnerBranchId();
    }

    @Override
    public String getEmisorRegimenFiscal() {
        return msAuxCfdEmisorRegimenFiscal;
    }

    @Override
    public int getReceptorId() {
        return moAuxCfdDbmsDataReceptor.getPkBizPartnerId();
    }

    @Override
    public int getReceptorSucursalId() {
        return moAuxCfdDbmsDataReceptor.getDbmsHqBranch().getPkBizPartnerBranchId();
    }

    @Override
    public String getReceptorResidenciaFiscal() {
        return moAuxCfdDbmsDataReceptor.getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial().getDbmsDataCountry().getCountryCode();
    }

    @Override
    public String getReceptorNumRegIdTrib() {
        return moAuxCfdDbmsDataReceptor.getFiscalFrgId();
    }

    @Override
    public String getReceptorUsoCFDI() {
        return DCfdi33Catalogs.CFDI_USO_POR_DEF;    // fixed value required as is in CFDI 3.3 with Complement of Receipt of Payments 1.0
    }

    @Override
    public int getDestinatarioId() {
        return SLibConstants.UNDEFINED; // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public int getDestinatarioSucursalId() {
        return SLibConstants.UNDEFINED; // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public int getDestinatarioDomicilioId() {
        return SLibConstants.UNDEFINED; // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public ArrayList<SCfdDataConcepto> getElementsConcepto() throws Exception {
        SCfdDataConcepto concepto = new SCfdDataConcepto();
        concepto.setClaveProdServ(DCfdi33Catalogs.ClaveProdServServsFacturacion);
        concepto.setNoIdentificacion("");
        concepto.setCantidad(1);
        concepto.setClaveUnidad(DCfdi33Catalogs.ClaveUnidadAct);
        concepto.setUnidad("");
        concepto.setDescripcion(DCfdi33Catalogs.ConceptoPago);
        concepto.setValorUnitario(0);
        concepto.setImporte(0);
        concepto.setDescuento(0);
        concepto.setCfdiType(SDataConstantsSys.TRNS_TP_CFD_PAY_REC);

        ArrayList<SCfdDataConcepto> conceptos = new ArrayList<>();
        conceptos.add(concepto);

        return conceptos;
    }

    @Override
    public ArrayList<SCfdDataImpuesto> getElementsImpuestos(float cfdiVersion) {
        return null;    // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }

    @Override
    public DElement getElementComplemento() throws Exception {
        DElementComplemento complemento = new DElementComplemento();

        DElementPagos pagos = new DElementPagos();
        
        for (SCfdPaymentEntry paymentEntry : maAuxCfdPaymentEntries) {
            DElementPagosPago pago = new DElementPagosPago();
            pago.getAttFechaPago().setDatetime(paymentEntry.Date);
            pago.getAttFormaDePagoP().setString(paymentEntry.PaymentWay);
            pago.getAttMonedaP().setString(paymentEntry.CurrencyKey);
            if (paymentEntry.CurrencyId != SModSysConsts.CFGU_CUR_MXN) {
                pago.getAttTipoCambioP().setDouble(paymentEntry.ExchangeRate);
            }
            pago.getAttMonto().setDouble(paymentEntry.Amount);
            pago.getAttNumOperacion().setString(paymentEntry.Operation);
            pago.getAttRfcEmisorCtaOrd().setString(paymentEntry.AccountSrcFiscalId);
            pago.getAttNomBancoOrdExt().setString(paymentEntry.AccountSrcEntity);
            pago.getAttCtaOrdenante().setString(paymentEntry.AccountSrcNumber);
            pago.getAttRfcEmisorCtaBen().setString(paymentEntry.AccountDesFiscalId);
            pago.getAttCtaBeneficiario().setString(paymentEntry.AccountDesNumber);
            pago.getAttTipoCadPago().setString(""); // XXX not supported yet!
            pago.getAttCertPago().setString("");    // XXX not supported yet!
            pago.getAttCadPago().setString("");     // XXX not supported yet!
            pago.getAttSelloPago().setString("");   // XXX not supported yet!
            
            for (SCfdPaymentEntryDoc paymentEntryDoc : paymentEntry.PaymentEntryDocs) {
                DElementDoctoRelacionado doctoRelacionado = new DElementDoctoRelacionado();
                doctoRelacionado.getAttIdDocumento().setString(paymentEntryDoc.DataDps.getDbmsDataCfd().getUuid());
                doctoRelacionado.getAttSerie().setString(paymentEntryDoc.DataDps.getNumberSeries());
                doctoRelacionado.getAttFolio().setString(paymentEntryDoc.DataDps.getNumber());
                doctoRelacionado.getAttMonedaDR().setString(paymentEntryDoc.DataDps.getDbmsCurrencyKey());
                if (!pago.getAttMonedaP().getString().equals(doctoRelacionado.getAttMonedaDR().getString())) {
                    doctoRelacionado.getAttTipoCambioDR().setDouble(paymentEntryDoc.ExchangeRate);
                }
                doctoRelacionado.getAttMetodoDePagoDR().setString(paymentEntryDoc.DataDps.getDbmsDataDpsCfd().getPaymentMethod());
                doctoRelacionado.getAttNumParcialidad().setInteger(paymentEntryDoc.Installment);
                doctoRelacionado.getAttImpSaldoAnt().setDouble(paymentEntryDoc.BalancePrev);
                doctoRelacionado.getAttImpPagado().setDouble(paymentEntryDoc.Payment);
                doctoRelacionado.getAttImpSaldoInsoluto().setDouble(paymentEntryDoc.BalancePend);
                
                pago.getEltDoctoRelacionados().add(doctoRelacionado);
            }
            
            pagos.getEltPagos().add(pago);
        }
        
        ((DElementComplemento) complemento).getElements().add(pagos);
        
        return complemento;
    }

    @Override
    public DElement getElementAddenda() {
        return null;    // not required in CFDI 3.3 with Complement of Receipt of Payments 1.0!
    }
}
