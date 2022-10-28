/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import cfd.DCfdUtils;
import cfd.DElement;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public abstract class SCfdPaymentUtils {
    
    public static void sign(final SClientInterface client, final int idCfd) throws Exception {
        /*
        XXX 2022-02-01, Sergio Flores.
        Código preservado para referencias futuras.
        Se comentó porque antes de leer el comprobante es necesario indicar que no se desea leer los renglones de las pólizas contables relacionadas con él.
        
        SDataCfdPayment cfdPayment = (SDataCfdPayment) SDataUtilities.readRegistry(client, SDataConstants.TRNX_CFD_PAY_REC, new int[] { idCfd }, SLibConstants.EXEC_MODE_VERBOSE);
        */
        
        /*
        XXX 2022-02-01, Sergio Flores.
        Código preservado para referencias futuras.
        Se comentó porque es más eficiente leer solamente el puro registro CFD, para no leer ni las pólizas contables ni los documentos relacionados con el comprobante.
        
        SDataCfdPayment cfdPayment = new SDataCfdPayment();
        cfdPayment.setAuxReadJournalVoucherHeadersOnly(true);
        cfdPayment.read(new int[] { idCfd }, client.getSession().getStatement());
        
        SDataCfd cfd = cfdPayment.getDbmsDataCfd();
        */
        
        SDataCfd cfd = (SDataCfd) SDataUtilities.readRegistry(client, SDataConstants.TRN_CFD, new int[] { idCfd }, SLibConstants.EXEC_MODE_VERBOSE);

        if (!cfd.getUuid().isEmpty()) {
            throw new Exception("El CFDI '" + cfd.getCfdNumber() + "' ya está timbrado.");
        }
        else if (cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
            throw new Exception("El CFDI '" + cfd.getCfdNumber() + "' está anulado.");
        }
        else if (!SDataUtilities.isPeriodOpen(client, cfd.getTimestamp())) {
            throw new Exception(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
        }
        else {
            switch(cfd.getFkXmlTypeId()) {
                case SDataConstantsSys.TRNS_TP_XML_CFD:
                case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                    throw new Exception("El tipo del CFDI '" + cfd.getCfdNumber() + "' es obsoleto.");

                case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                    try {
                        if (((SClientInterface) client).getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticSal()) {
                            if (SCfdUtils.signAndSendCfdi(client, cfd, 0, true, true)) {
                                client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_PAY);
                            }
                        }
                        else {
                            if (SCfdUtils.signCfdi(client, cfd, 0)) {
                                client.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_PAY);
                            }
                        }
                    }
                    catch (java.lang.Exception e) {
                        throw new Exception("Ha ocurrido una excepción al timbrar o imprimir el comprobante fiscal:\n" + e);
                    }
                    break;

                default:
                    throw new Exception("El tipo del CFDI '" + cfd.getCfdNumber() + "' es desconocido.");
            }
        }
    }
    
    /**
     * Create temporary tables and data for CFDI 3.3 'complemento de recepción de pagos' 1.0.
     * Intented to be used in method erp.server.SSessionServer.requestFillReport().
     * @param connection
     * @param sessionId
     * @param xml
     * @throws Exception 
     */
    @Deprecated
    @SuppressWarnings("deprecation")
    public static void createTemporaryDataCfdi33_Crp10(final Connection connection, final int sessionId, final String xml) throws Exception {
        cfd.ver33.DElementComprobante comprobante = DCfdUtils.getCfdi33(xml);
        
        if (comprobante.getEltOpcComplemento() != null) {
            for (DElement element : comprobante.getEltOpcComplemento().getElements()) {
                if (element instanceof cfd.ver33.crp10.DElementPagos) {
                    String sql;
                    ResultSet resultSet;
                    Statement statement = connection.createStatement();

                    sql = "CREATE TEMPORARY TABLE IF NOT EXISTS temp_pago ("
                            + "id_pago SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,"
                            + "FechaPago CHAR(20),"
                            + "FormaDePagoP CHAR(2),"
                            + "MonedaP CHAR(3),"
                            + "TipoCambioP DECIMAL(19, 4),"
                            + "Monto DECIMAL(17, 2),"
                            + "NumOperacion CHAR(100),"
                            + "RfcEmisorCtaOrd CHAR(13),"
                            + "NomBancoOrdExt VARCHAR(300),"
                            + "CtaOrdenante VARCHAR(50),"
                            + "RfcEmisorCtaBen VARCHAR(12),"
                            + "CtaBeneficiario VARCHAR(50),"
                            + "session_id INT UNSIGNED,"
                            + "CONSTRAINT pk_temp_pago PRIMARY KEY (id_pago)) ENGINE=InnoDB;";
                    statement.execute(sql);
                    sql = "CREATE TEMPORARY TABLE IF NOT EXISTS temp_pago_docto ("
                            + "id_docto SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,"
                            + "IdDocumento CHAR(36),"
                            + "Serie CHAR(25),"
                            + "Folio CHAR(40),"
                            + "MonedaDR CHAR(3),"
                            + "TipoCambioDR DECIMAL(21, 6),"
                            + "NumParcialidad SMALLINT UNSIGNED,"
                            + "ImpSaldoAnt DECIMAL(17, 2),"
                            + "ImpPagado DECIMAL(17, 2),"
                            + "ImpSaldoInsoluto DECIMAL(17, 2),"
                            + "fk_pago SMALLINT UNSIGNED,"
                            + "session_id INT UNSIGNED,"
                            + "CONSTRAINT pk_temp_pago_docto PRIMARY KEY (id_docto)) ENGINE=InnoDB;";
                    statement.execute(sql);

                    sql = "DELETE FROM temp_pago WHERE session_id = " + sessionId + ";";
                    statement.execute(sql);
                    
                    sql = "DELETE FROM temp_pago_docto WHERE session_id = " + sessionId + ";";
                    statement.execute(sql);
                    
                    cfd.ver33.crp10.DElementPagos pagos = (cfd.ver33.crp10.DElementPagos) element;
                    for (cfd.ver33.crp10.DElementPagosPago pago : pagos.getEltPagos()) {
                        int pagoId;
                        
                        sql = "INSERT INTO temp_pago VALUES ("
                                + "0,"
                                + "'" + SLibUtils.DateFormatDatetime.format(pago.getAttFechaPago().getDatetime()) + "',"
                                + "'" + pago.getAttFormaDePagoP().getString()+ "',"
                                + "'" + pago.getAttMonedaP().getString()+ "',"
                                + pago.getAttTipoCambioP().getDouble() + ","
                                + pago.getAttMonto().getDouble() + ","
                                + "'" + pago.getAttNumOperacion().getString()+ "',"
                                + "'" + pago.getAttRfcEmisorCtaOrd().getString()+ "',"
                                + "'" + pago.getAttNomBancoOrdExt().getString()+ "',"
                                + "'" + pago.getAttCtaOrdenante().getString()+ "',"
                                + "'" + pago.getAttRfcEmisorCtaBen().getString()+ "',"
                                + "'" + pago.getAttCtaBeneficiario().getString()+ "',"
                                + sessionId + ");";
                        statement.execute(sql, Statement.RETURN_GENERATED_KEYS);
                        resultSet = statement.getGeneratedKeys();
                        if (resultSet.next()) {
                            pagoId = resultSet.getInt(1);
                            
                            for (cfd.ver33.crp10.DElementDoctoRelacionado docto : pago.getEltDoctoRelacionados()) {
                                sql = "INSERT INTO temp_pago_docto VALUES ("
                                        + "0,"
                                        + "'" + docto.getAttIdDocumento().getString() + "',"
                                        + "'" + docto.getAttSerie().getString() + "',"
                                        + "'" + docto.getAttFolio().getString() + "',"
                                        + "'" + docto.getAttMonedaDR().getString() + "',"
                                        + docto.getAttTipoCambioDR().getDouble() + ","
                                        + docto.getAttNumParcialidad().getInteger()+ ","
                                        + docto.getAttImpSaldoAnt().getDouble() + ","
                                        + docto.getAttImpPagado().getDouble() + ","
                                        + docto.getAttImpSaldoInsoluto().getDouble() + ","
                                        + pagoId + ","
                                        + sessionId + ");";
                                statement.execute(sql);
                            }
                        }
                    }
                }
            }
        }
    }
}
