/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.server;

import cfd.DCfd;
import erp.cfd.SCfdConsts;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadComponentItems;
import erp.data.SDataReadRegistries;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataConnectionMonitor;
import erp.lib.data.SDataDatabase;
import erp.lib.data.SDataProcedure;
import erp.lib.data.SDataRegistry;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableRow;
import erp.lib.table.STableRowCustom;
import erp.lib.table.STableUtilities;
import erp.mbps.data.SDataEmployee;
import erp.mfin.data.SFinAccountUtilities;
import erp.mtrn.data.SCfdPacket;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataCfdSignLog;
import erp.mtrn.data.SDataSign;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Vector;
import javax.imageio.ImageIO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import sa.lib.SLibConsts;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibUtils;
import sa.lib.srv.SSrvConsts;
import sa.lib.srv.SSrvLock;
import sa.lib.srv.SSrvRequest;
import sa.lib.srv.SSrvResponse;

/**
 *
 * @author Sergio Flores
 * To generate stub:
 * ...[siie_path]\build\classes>"C:\Program Files\Java\jdk1.8.0_XX\bin\rmic" -classpath: .;"[sa-lib-10_path]\build\classes" erp.server.SSessionServer
 */
public class SSessionServer implements SSessionServerRemote, Serializable {

    private SServer moServer;
    private int mnSessionId;
    private int mnPkUserId;
    private String msUserName;
    private int mnPkCompanyId;
    private String msCompanyDatabaseName;

    private boolean mbIsTransactionClosed;
    private Date mtTimestamp;

    private String msSessionServer;
    private SDataDatabase moCompanyDatabase;
    private SDataConnectionMonitor moConnectionMonitor;
    private Statement moStatement;
    private cfd.DCfd moCfd;

    public SSessionServer(SServer server, int sessionId, int userId, String userName, int companyId, String databaseName) {
        moServer = server;
        mnSessionId = sessionId;
        mnPkUserId = userId;
        msUserName = userName;
        mnPkCompanyId = companyId;
        msCompanyDatabaseName = databaseName;

        mbIsTransactionClosed = true;
        mtTimestamp = moServer.getSystemDatetime();

        try {
            msSessionServer = "[SessionServer " + mnSessionId + "]: ";
            moCompanyDatabase = new SDataDatabase(SLibConstants.DBMS_MY_SQL);
            moCompanyDatabase.setUserSettings(SDataConstantsSys.DB_SETTINGS);
            if (moCompanyDatabase.connect(moServer.getParamsApp().getDatabaseHostSrv(), moServer.getParamsApp().getDatabasePortSrv(),
                    msCompanyDatabaseName, moServer.getParamsApp().getDatabaseUser(), moServer.getParamsApp().getDatabasePswd()) != SLibConstants.DB_CONNECTION_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_CON);
            }
            else {
                moConnectionMonitor = new SDataConnectionMonitor(moCompanyDatabase);
                moConnectionMonitor.startMonitor();

                moStatement = moCompanyDatabase.getConnection().createStatement();
                createCfd();
            }
        }
        catch (SQLException e) {
            moServer.renderMessageLn(msSessionServer + e);
        }
        catch (Exception e) {
            moServer.renderMessageLn(msSessionServer + e);
        }
    }

    /*
     * Private functions
     */
    private void createCfd() throws SQLException, NoSuchAlgorithmException, Exception {
        /*
        java.sql.Blob blobKeyPrivate = null;
        java.sql.Blob blobKeyPublic = null;
        String sql = "SELECT cert_key_priv_n, cert_key_pub_n, cert_num, xml_base_dir FROM cfg_param_co WHERE id_co = " + mnPkCompanyId + " ";
        ResultSet resultSet = moStatement.executeQuery(sql);

        moServer.renderMessageLn(msSessionServer + "Creating CFD instance...");

        if (!resultSet.next()) {
            throw new Exception("Company's configuration could not be read.");
        }
        else {
            blobKeyPrivate = resultSet.getBlob("cert_key_priv_n");
            blobKeyPublic = resultSet.getBlob("cert_key_pub_n");

            if (blobKeyPrivate == null) {
                moServer.renderMessageLn(msSessionServer + "CFD private key not found!");
            }

            if (blobKeyPublic == null) {
                moServer.renderMessageLn(msSessionServer + "CFD public key not found!");
            }

            if (blobKeyPrivate != null && blobKeyPublic != null) {
                moCfd = new DCfd(
                        blobKeyPrivate.getBytes(1, (int) blobKeyPrivate.length()),  // first byte is 1
                        blobKeyPublic.getBytes(1, (int) blobKeyPublic.length()),   // first byte is 1
                        resultSet.getString("cert_num"),
                        resultSet.getString("xml_base_dir"));

                moServer.renderMessageLn(msSessionServer + "CFD instance successfully created!");
            }
        }
        */

        String sql = "SELECT xml_base_dir FROM cfg_param_co WHERE id_co = " + mnPkCompanyId + " ";
        ResultSet resultSet = moStatement.executeQuery(sql);

        if (!resultSet.next()) {
            throw new Exception("Company's configuration could not be read.");
        }
        else {
            moCfd = new DCfd(resultSet.getString("xml_base_dir"));
        }
    }

    private JasperPrint requestFillReport(int reportType, Map<String, Object> map) throws JRException, Exception {
        String name = "";
        File file = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;

        /* Begin of special block of code (sflores, 2014-01-15)
         *
         * IMPORTANT:
         * BufferedImage is not serializable, therefore it cannot be send to Server through RMI. QR Code is generated and put into in SSessionServer.requestFillReport().
         */

        if (reportType == SDataConstantsSys.REP_TRN_CFDI || reportType == SDataConstantsSys.REP_TRN_CFDI_PAYROLL) {
            BufferedImage biQrCode = DCfd.createQrCodeBufferedImage((String) map.get("sEmiRfc"), (String) map.get("sRecRfc"), Double.parseDouble("" + map.get("dCfdTotal")), (String) map.get("sCfdiUuid"));
            map.put("oCfdiQrCode", biQrCode.getScaledInstance(biQrCode.getWidth(), biQrCode.getHeight(), Image.SCALE_DEFAULT));
        }
        
        if (reportType == SDataConstantsSys.REP_TRN_DPS_ORDER && (boolean) map.get("bIsSupplier")) {
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            
            if (((SDataEmployee) map.get("oUserBuyer")) != null && ((SDataEmployee) map.get("oUserBuyer")).getXtaImageIconSignature_n() != null) {
                BufferedImage userBuyer = gc.createCompatibleImage(((SDataEmployee) map.get("oUserBuyer")).getXtaImageIconSignature_n().getImage().getWidth(null), ((SDataEmployee) map.get("oUserBuyer")).getXtaImageIconSignature_n().getImage().getHeight(null), Transparency.TRANSLUCENT);                
                Graphics gUserBuyer = userBuyer.createGraphics();
                gUserBuyer.drawImage(((SDataEmployee) map.get("oUserBuyer")).getXtaImageIconSignature_n().getImage(), 0, 0, null);
                map.put("oUserBuyerSign", userBuyer);
                gUserBuyer.dispose();
            }
            
            if (((SDataEmployee) map.get("oUserAuthorize")) != null && ((SDataEmployee) map.get("oUserAuthorize")).getXtaImageIconSignature_n() != null) {
                BufferedImage userAuthorize = gc.createCompatibleImage(((SDataEmployee) map.get("oUserAuthorize")).getXtaImageIconSignature_n().getImage().getWidth(null), ((SDataEmployee) map.get("oUserAuthorize")).getXtaImageIconSignature_n().getImage().getHeight(null), Transparency.TRANSLUCENT);
                Graphics gUserAuthorize = userAuthorize.createGraphics();
                gUserAuthorize.drawImage(((SDataEmployee) map.get("oUserAuthorize")).getXtaImageIconSignature_n().getImage(), 0, 0, null);
                map.put("oUserAuthorizeSign", userAuthorize);
                gUserAuthorize.dispose();
            }
        }

        /* End of special block of code (sflores, 2014-01-15)
         */

        name = SDataUtilities.getReportFileName(reportType);
        if (name.length() > 0) {
            file = new File(name);
            jasperReport = (JasperReport) JRLoader.loadObject(file);

            map.put("oSubreportConnection", moCompanyDatabase.getConnection());
            jasperPrint = JasperFillManager.fillReport(jasperReport, map, moCompanyDatabase.getConnection());
        }

        return jasperPrint;
    }

    @SuppressWarnings("unchecked")
    private JasperPrint requestFillCheck(Object[] oData) throws JRException, Exception {
        String name = "";
        File file = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;

        name = (String) oData[0];
        if (name.length() > 0) {
            file = new File(name);
            jasperReport = (JasperReport) JRLoader.loadObject(file);
            jasperPrint = JasperFillManager.fillReport(jasperReport, (Map<String, Object>) oData[1], moCompanyDatabase.getConnection());
        }

        return jasperPrint;
    }

    /**
     * Executes query to retrieve rows for views.
     */
    private Vector<STableRow> requestExecuteQuery(SQueryRequest queryRequest) throws SQLException, Exception {

        ArrayList<STableField> paPrimaryKeyFields = queryRequest.getPrimaryKeyFields();
        ArrayList<STableField> paQueryFields = queryRequest.getQueryFields();
        ArrayList<STableField> paQueryAdditionalFields = queryRequest.getQueryAdditionalFields();
        ArrayList<SLibRpnArgument>[] paaRpnArguments = queryRequest.getRpnArguments();
        String[] paQuerySentences = queryRequest.getQuerySentences();

        int i;
        int nColsPrimaryKey = paPrimaryKeyFields.size();
        int nColsView = paQueryFields.size();
        int nColsAditional = paQueryAdditionalFields.size();
        int[] anPrimaryKeyDataTypes = new int[nColsPrimaryKey];
        int[] anViewDataTypes = new int[nColsView];
        int[] anAditionalDataTypes = new int[nColsAditional];
        boolean bPrimaryKeyOfIntegers = true;
        boolean bExistsFieldIsEditable = false;
        boolean bExistsFieldStyle = false;
        String sSql = "";
        String[] asPrimaryKeyFieldNames = new String[nColsPrimaryKey];
        String[] asViewFieldNames = new String[nColsView];
        String[] asAditionalFieldNames = new String[nColsAditional];
        Object[] aoAditionalData = null;
        ResultSet oResultSet = null;
        Vector<STableRow> vTableRows = new Vector<STableRow>();

        // To increase performance, data types and field names are stored in arrays:

        for (i = 0; i < nColsPrimaryKey; i++) {
            anPrimaryKeyDataTypes[i] = paPrimaryKeyFields.get(i).getColumnType();
            asPrimaryKeyFieldNames[i] = paPrimaryKeyFields.get(i).getFieldName();
            if (anPrimaryKeyDataTypes[i] != SLibConstants.DATA_TYPE_INTEGER) {
                bPrimaryKeyOfIntegers = false;
            }
        }

        for (i = 0; i < nColsView; i++) {
            anViewDataTypes[i] = paQueryFields.get(i).getColumnType();
            asViewFieldNames[i] = paQueryFields.get(i).getFieldName();
        }

        for (i = 0; i < nColsAditional; i++) {
            anAditionalDataTypes[i] = paQueryAdditionalFields.get(i).getColumnType();
            asAditionalFieldNames[i] = paQueryAdditionalFields.get(i).getFieldName();
        }

        // Execute local variable declarations, if any:

        for (i = 0; i < paQuerySentences.length - 1; i++) {
            moStatement.execute(paQuerySentences[i]);
        }

        // Get query data:

        sSql = paQuerySentences[paQuerySentences.length - 1];

        bExistsFieldIsEditable = sSql.contains(STableConstants.FIELD_IS_EDITABLE);
        bExistsFieldStyle = sSql.contains(STableConstants.FIELD_STYLE);

        oResultSet = moStatement.executeQuery(sSql);

        while (oResultSet.next()) {
            STableRowCustom tableRowCustom = new STableRowCustom();

            // Read row primary key:

            if (bPrimaryKeyOfIntegers) {
                int[] pk = new int[nColsPrimaryKey];
                for (i = 0; i < nColsPrimaryKey; i++) {
                    pk[i] = oResultSet.getInt(asPrimaryKeyFieldNames[i]);
                }
                tableRowCustom.setPrimaryKey(pk);
            }
            else {
                java.lang.Object[] pk = new Object[nColsPrimaryKey];
                for (i = 0; i < nColsPrimaryKey; i++) {
                    switch (anPrimaryKeyDataTypes[i]) {
                        case SLibConstants.DATA_TYPE_BOOLEAN:
                            pk[i] = oResultSet.getBoolean(asPrimaryKeyFieldNames[i]);
                            break;
                        case SLibConstants.DATA_TYPE_INTEGER:
                            pk[i] = oResultSet.getInt(asPrimaryKeyFieldNames[i]);
                            break;
                        case SLibConstants.DATA_TYPE_LONG:
                            pk[i] = oResultSet.getLong(asPrimaryKeyFieldNames[i]);
                            break;
                        case SLibConstants.DATA_TYPE_FLOAT:
                            pk[i] = oResultSet.getFloat(asPrimaryKeyFieldNames[i]);
                            break;
                        case SLibConstants.DATA_TYPE_DOUBLE:
                            pk[i] = oResultSet.getDouble(asPrimaryKeyFieldNames[i]);
                            break;
                        case SLibConstants.DATA_TYPE_STRING:
                            pk[i] = oResultSet.getString(asPrimaryKeyFieldNames[i]);
                            break;
                        case SLibConstants.DATA_TYPE_DATE:
                            pk[i] = (java.util.Date) oResultSet.getDate(asPrimaryKeyFieldNames[i]);
                            break;
                        case SLibConstants.DATA_TYPE_DATE_TIME:
                            pk[i] = (java.util.Date) oResultSet.getTimestamp(asPrimaryKeyFieldNames[i]);
                            break;
                        case SLibConstants.DATA_TYPE_TIME:
                            pk[i] = (java.util.Date) oResultSet.getTime(asPrimaryKeyFieldNames[i]);
                            break;
                        default:
                            pk[i] = oResultSet.getObject(asPrimaryKeyFieldNames[i]);
                            break;
                    }
                }
                tableRowCustom.setPrimaryKey(pk);
            }

            // Read row view data:

            for (i = 0; i < nColsView; i++) {
                if (paaRpnArguments[i] != null) {
                    // Reverse Polac Notation value:

                    tableRowCustom.getValues().add(STableUtilities.calculateRpn(paaRpnArguments[i], oResultSet));
                }
                else {
                    // Raw value:

                    switch (anViewDataTypes[i]) {
                        case SLibConstants.DATA_TYPE_BOOLEAN:
                            tableRowCustom.getValues().add(oResultSet.getBoolean(asViewFieldNames[i]));
                            break;
                        case SLibConstants.DATA_TYPE_INTEGER:
                            tableRowCustom.getValues().add(oResultSet.getInt(asViewFieldNames[i]));
                            break;
                        case SLibConstants.DATA_TYPE_LONG:
                            tableRowCustom.getValues().add(oResultSet.getLong(asViewFieldNames[i]));
                            break;
                        case SLibConstants.DATA_TYPE_FLOAT:
                            tableRowCustom.getValues().add(oResultSet.getFloat(asViewFieldNames[i]));
                            break;
                        case SLibConstants.DATA_TYPE_DOUBLE:
                            tableRowCustom.getValues().add(oResultSet.getDouble(asViewFieldNames[i]));
                            break;
                        case SLibConstants.DATA_TYPE_STRING:
                            tableRowCustom.getValues().add(oResultSet.getString(asViewFieldNames[i]));
                            break;
                        case SLibConstants.DATA_TYPE_DATE:
                            tableRowCustom.getValues().add((java.util.Date) oResultSet.getDate(asViewFieldNames[i]));
                            break;
                        case SLibConstants.DATA_TYPE_DATE_TIME:
                            tableRowCustom.getValues().add((java.util.Date) oResultSet.getTimestamp(asViewFieldNames[i]));
                            break;
                        case SLibConstants.DATA_TYPE_TIME:
                            tableRowCustom.getValues().add((java.util.Date) oResultSet.getTime(asViewFieldNames[i]));
                            break;
                        default:
                            tableRowCustom.getValues().add(oResultSet.getObject(asViewFieldNames[i]));
                            break;
                    }
                }
            }

            // Read row aditional data:

            aoAditionalData = new Object[nColsAditional];

            for (i = 0; i < nColsAditional; i++) {
                switch (anAditionalDataTypes[i]) {
                    case SLibConstants.DATA_TYPE_BOOLEAN:
                        aoAditionalData[i] = oResultSet.getBoolean(asAditionalFieldNames[i]);
                        break;
                    case SLibConstants.DATA_TYPE_INTEGER:
                        aoAditionalData[i] = oResultSet.getInt(asAditionalFieldNames[i]);
                        break;
                    case SLibConstants.DATA_TYPE_LONG:
                        aoAditionalData[i] = oResultSet.getLong(asAditionalFieldNames[i]);
                        break;
                    case SLibConstants.DATA_TYPE_FLOAT:
                        aoAditionalData[i] = oResultSet.getFloat(asAditionalFieldNames[i]);
                        break;
                    case SLibConstants.DATA_TYPE_DOUBLE:
                        aoAditionalData[i] = oResultSet.getDouble(asAditionalFieldNames[i]);
                        break;
                    case SLibConstants.DATA_TYPE_STRING:
                        aoAditionalData[i] = oResultSet.getString(asAditionalFieldNames[i]);
                        break;
                    case SLibConstants.DATA_TYPE_DATE:
                        aoAditionalData[i] = (java.util.Date) oResultSet.getDate(asAditionalFieldNames[i]);
                        break;
                    case SLibConstants.DATA_TYPE_DATE_TIME:
                        aoAditionalData[i] = (java.util.Date) oResultSet.getTimestamp(asAditionalFieldNames[i]);
                        break;
                    case SLibConstants.DATA_TYPE_TIME:
                        aoAditionalData[i] = (java.util.Date) oResultSet.getTime(asAditionalFieldNames[i]);
                        break;
                    default:
                        aoAditionalData[i] = oResultSet.getObject(asAditionalFieldNames[i]);
                        break;
                }
            }

            tableRowCustom.setData(aoAditionalData);

            if (bExistsFieldIsEditable) {
                tableRowCustom.setIsEditable(oResultSet.getBoolean(STableConstants.FIELD_IS_EDITABLE));
            }
            if (bExistsFieldStyle) {
                tableRowCustom.setStyle(oResultSet.getInt(STableConstants.FIELD_STYLE));
            }

            vTableRows.add(tableRowCustom);
        }

        return vTableRows;
    }

    private Vector<Vector<Object>> requestExecuteQuerySimple(String sql) throws SQLException, Exception {
        int i = 0;
        int cols = 0;
        Vector<Vector<Object>> vectors = new Vector<Vector<Object>>();
        ResultSet oResultSet = moStatement.executeQuery(sql);

        cols = oResultSet.getMetaData().getColumnCount();
        while (oResultSet.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (i = 1; i <= cols; i++) {
                vector.add(oResultSet.getObject(i));
            }
            vectors.add(vector);
        }

        return vectors;
    }

    private SDataProcedure requestCallProcedure(SDataProcedure procedure) throws Exception {
        procedure.call(moCompanyDatabase.getConnection());
        return procedure;
    }

    private String requestReadCatalogueDescription(Object packet) throws Exception {
        String sql = (String) packet;
        String descrip = "";
        ResultSet resultSet = moStatement.executeQuery(sql);

        if (resultSet.next()) {
            descrip = resultSet.getString(1);
        }

        return descrip;
    }

    @SuppressWarnings("static-access")
    private int requestEmitCfd(SCfdPacket packet) throws Exception {
        int result = SLibConstants.UNDEFINED;
        SDataCfd cfd = null;
        SDataSign xmlSign = null;
        String xmlFile = "";
        String xmlFileName = "";
        Date dateCfd = null;
        boolean omitXmlDeclaration = false;
        SDataCfdSignLog cfdSignLog = null;

        if (moCfd == null) {
            throw new Exception("CFD has not been instanciated!");
        }
        else {
            if (packet.getCfdRootElement() == null) {
                xmlFile = packet.getXml();
                xmlFileName = packet.getXmlName();
                dateCfd = packet.getXmlDate();
                omitXmlDeclaration = true;
            }
            else if (packet.getCfdRootElement() instanceof cfd.ver2.DElementComprobante) {
                xmlFile = ((cfd.ver2.DElementComprobante) packet.getCfdRootElement()).getElementForXml();

                xmlFileName += ((cfd.ver2.DElementComprobante) packet.getCfdRootElement()).getEltEmisor().getAttRfc().getString() + "_";
                xmlFileName += ((cfd.ver2.DElementComprobante) packet.getCfdRootElement()).getAttTipoDeComprobante().getOption().substring(0, 1).toUpperCase() + "_";
                xmlFileName += (((cfd.ver2.DElementComprobante) packet.getCfdRootElement()).getAttSerie().getString().length() == 0 ? "" : ((cfd.ver2.DElementComprobante) packet.getCfdRootElement()).getAttSerie().getString() + "_");
                xmlFileName += moCfd.getDecimalFormat().format(SLibUtils.parseLong(((cfd.ver2.DElementComprobante) packet.getCfdRootElement()).getAttFolio().getString())) + ".xml";
                dateCfd = ((cfd.ver2.DElementComprobante) packet.getCfdRootElement()).getAttFecha().getDatetime();
            }
            else if (packet.getCfdRootElement() instanceof cfd.ver3.DElementComprobante) {
                xmlFile = ((cfd.ver3.DElementComprobante) packet.getCfdRootElement()).getElementForXml();

                xmlFileName += ((cfd.ver3.DElementComprobante) packet.getCfdRootElement()).getEltEmisor().getAttRfc().getString() + "_";
                xmlFileName += ((cfd.ver3.DElementComprobante) packet.getCfdRootElement()).getAttTipoDeComprobante().getOption().substring(0, 1).toUpperCase() + "_";
                xmlFileName += (((cfd.ver3.DElementComprobante) packet.getCfdRootElement()).getAttSerie().getString().length() == 0 ? "" : ((cfd.ver3.DElementComprobante) packet.getCfdRootElement()).getAttSerie().getString() + "_");
                xmlFileName += moCfd.getDecimalFormat().format(SLibUtils.parseLong(((cfd.ver3.DElementComprobante) packet.getCfdRootElement()).getAttFolio().getString())) + ".xml";
                dateCfd = ((cfd.ver3.DElementComprobante) packet.getCfdRootElement()).getAttFecha().getDatetime();
            }
            else {
                throw new Exception("Not supported CFD version!");
            }

            xmlFile = (omitXmlDeclaration ? "" : "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n") + xmlFile;

            if (packet.getGenerateQrCode() && (packet.getRfcEmisor().isEmpty() || packet.getRfcReceptor().isEmpty() || packet.getUuid().isEmpty())) {
                throw new Exception("The two-dimensional bar code can not be created!");
            }

            cfd = new SDataCfd();
            cfd.setPkCfdId(packet.getCfdId());
            cfd.setTimestamp(dateCfd);
            cfd.setCertNumber(packet.getCertNumber());
            cfd.setStringSigned(packet.getStringSigned());
            cfd.setSignature(packet.getSignature());
            cfd.setDocXml(xmlFile);
            cfd.setDocXmlName(xmlFileName);
            cfd.setAcknowledgmentCancellationXml(packet.getAcknowledgmentCancellation());
            cfd.setUuid(packet.getUuid());
            cfd.setIsConsistent(packet.getIsConsistent());
            cfd.setFkCfdTypeId(packet.getFkCfdTypeId());
            cfd.setFkXmlTypeId(packet.getFkXmlTypeId());
            cfd.setFkXmlStatusId(packet.getFkXmlStatusId());
            cfd.setFkXmlDeliveryTypeId(packet.getFkXmlDeliveryTypeId());
            cfd.setFkXmlDeliveryStatusId(packet.getFkXmlDeliveryStatusId());
            cfd.setFkUserProcessingId(packet.getFkUserDeliveryId());
            cfd.setFkUserDeliveryId(packet.getFkUserDeliveryId());
            cfd.setFkDpsYearId_n(packet.getDpsYearId());
            cfd.setFkDpsDocId_n(packet.getDpsDocId());
            cfd.setFkPayrollPayrollId_n(packet.getPayrollPayrollId());
            cfd.setFkPayrollEmployeeId_n(packet.getPayrollEmployeeId());
            cfd.setFkPayrollBizPartnerId_n(packet.getPayrollBizPartnerId());
            cfd.setFkPayrollReceiptPayrollId_n(packet.getPayrollReceiptPayrollId());
            cfd.setFkPayrollReceiptEmployeeId_n(packet.getPayrollReceiptEmployeeId());
            cfd.setFkPayrollReceiptIssueId_n(packet.getPayrollReceiptIssueId());
            cfd.setGenerateQrCode(packet.getGenerateQrCode());

            cfd.setAuxRfcEmisor(packet.getRfcEmisor());
            cfd.setAuxRfcReceptor(packet.getRfcReceptor());
            cfd.setAuxTotalCy(packet.getTotalCy());

            if (cfd.save(moCompanyDatabase.getConnection()) == SLibConstants.DB_ACTION_SAVE_OK) {
                result = SLibConstants.DB_CFD_OK;

                // XXX Guardar bitácora de timbrado 6

                cfdSignLog = new SDataCfdSignLog();

                if (packet.getLogSignId() != SLibConstants.UNDEFINED) {
                    cfdSignLog.saveField(moCompanyDatabase.getConnection(), new int[] { packet.getLogSignId() }, SDataCfdSignLog.FIELD_CODE_STP, SCfdConsts.STATUS_DIACTIVATE_PAC);
                }

                cfd.saveField(moCompanyDatabase.getConnection(), new int[] { cfd.getPkCfdId() }, SDataCfd.FIELD_ACC_WS, false);
                cfd.saveField(moCompanyDatabase.getConnection(), new int[] { cfd.getPkCfdId() }, SDataCfd.FIELD_ACC_USR, packet.getFkUserDeliveryId());

                if (packet.getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_NEW && packet.getConsumeStamp()) {
                    xmlSign = new SDataSign();
                    if (packet.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                        xmlSign.setPkYearId(SLibTimeUtilities.digestYear(cfd.getTimestamp())[0]);
                        xmlSign.setPkPacId(packet.getPacId());
                        xmlSign.setDate(mtTimestamp);
                        xmlSign.setMoveIn(0);
                        xmlSign.setMoveOut(packet.getQuantityStamp());
                        xmlSign.setIsDeleted(false);
                        xmlSign.setFkSignCategoryId(SDataConstantsSys.TRNS_TP_SIGN_OUT_EMITED[0]);
                        xmlSign.setFkSignTypeId(SDataConstantsSys.TRNS_TP_SIGN_OUT_EMITED[1]);
                        xmlSign.setFkCfdId_n(cfd.getPkCfdId());
                    }
                    else if (packet.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                        xmlSign.setPkYearId(SLibTimeUtilities.digestYear(cfd.getTimestamp())[0]);
                        xmlSign.setPkPacId(packet.getPacId());
                        xmlSign.setDate(mtTimestamp);
                        xmlSign.setMoveIn(0);
                        xmlSign.setMoveOut(packet.getQuantityStamp());
                        xmlSign.setIsDeleted(false);
                        xmlSign.setFkSignCategoryId(SDataConstantsSys.TRNS_TP_SIGN_OUT_ANNULED[0]);
                        xmlSign.setFkSignTypeId(SDataConstantsSys.TRNS_TP_SIGN_OUT_ANNULED[1]);
                        xmlSign.setFkCfdId_n(cfd.getPkCfdId());
                    }
                    
                    // XXX Guardar bitácora de timbrado 7

                    if (packet.getLogSignId() != SLibConstants.UNDEFINED) {
                        cfdSignLog.saveField(moCompanyDatabase.getConnection(), new int[] { packet.getLogSignId() }, SDataCfdSignLog.FIELD_CODE_STP, SCfdConsts.STATUS_CONSUME_STAMP);
                    }

                    if (xmlSign.save(moCompanyDatabase.getConnection()) == SLibConstants.DB_ACTION_SAVE_OK) {
                        result = SLibConstants.DB_CFD_OK;
                    }
                }

                if (result == SLibConstants.DB_CFD_OK) {
                    if (packet.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                        if (packet.getFkCfdTypeId() == SDataConstantsSys.TRNS_TP_CFD_CFD && packet.getDps() != null) {
                           // Annul DPS registry:

                            result = ((SDataRegistry) packet.getDps()).canAnnul(moCompanyDatabase.getConnection());

                            if (result == SLibConstants.DB_CAN_ANNUL_YES) {
                                result = ((SDataRegistry) packet.getDps()).annul(moCompanyDatabase.getConnection());

                                if (result == SLibConstants.DB_ACTION_ANNUL_OK) {
                                    result = SLibConstants.DB_CFD_OK;
                                }
                            } 
                        }
                        else if (packet.getFkCfdTypeId() == SDataConstantsSys.TRNS_TP_CFD_PAY && packet.getPayrollReceiptIssue() != null) {
                            result = ((SDataRegistry) packet.getPayrollReceiptIssue()).canAnnul(moCompanyDatabase.getConnection());

                            if (result == SLibConstants.DB_CAN_ANNUL_YES) {
                                result = ((SDataRegistry) packet.getPayrollReceiptIssue()).annul(moCompanyDatabase.getConnection());

                                if (result == SLibConstants.DB_ACTION_ANNUL_OK) {
                                    result = SLibConstants.DB_CFD_OK;
                                }
                            }
                        }
                    }
                }

                if (result == SLibConstants.DB_CFD_OK) {
                    if (packet.getFkXmlStatusId() != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                        try {
                            moCfd.write(xmlFile, xmlFileName, dateCfd, packet.getStringSigned(), packet.getSignature(), packet.getCertNumber(), packet.getCertBase64(), omitXmlDeclaration);

                            // Set flag XML as correct:

                            cfd.saveField(moCompanyDatabase.getConnection(), new int[] { cfd.getPkCfdId() }, SDataCfd.FIELD_ACC_XML_STO, false);
                            cfd.saveField(moCompanyDatabase.getConnection(), new int[] { cfd.getPkCfdId() }, SDataCfd.FIELD_ACC_USR, packet.getFkUserDeliveryId());
                        }
                        catch (Exception e) {
                            result = SLibConstants.DB_CFD_OK;
                            SLibUtilities.printOutException(this, e);
                        }
                    }
                    else {
                        // Set flag XML as correct:

                        cfd.saveField(moCompanyDatabase.getConnection(), new int[] { cfd.getPkCfdId() }, SDataCfd.FIELD_ACC_XML_STO, false);
                        cfd.saveField(moCompanyDatabase.getConnection(), new int[] { cfd.getPkCfdId() }, SDataCfd.FIELD_ACC_USR, packet.getFkUserDeliveryId());
                    }
                }
            }
            else {
                result = SLibConstants.DB_CFD_ERROR;
            }
        }

        return result;
    }

    private void startTransac() throws SQLException, Exception {
        mbIsTransactionClosed = false;
        moStatement.execute("START TRANSACTION; ");
    }

    private void commitTransac() throws SQLException, Exception {
        moStatement.execute("COMMIT; ");
        mbIsTransactionClosed = true;
    }

    private void rollbackTransac() throws SQLException, Exception {
        moStatement.execute("ROLLBACK; ");
        mbIsTransactionClosed = true;
    }

    /*
     * Public functions
     */

    public int getSessionId() { return mnSessionId; }
    public int getPkUserId() { return mnPkUserId; }
    public String getUserName() { return msUserName; }
    public int getPkCompanyId() { return mnPkCompanyId; }
    public String getCompanyDatabaseName() { return msCompanyDatabaseName; }
    public Date getTimestamp() { return mtTimestamp; }
    public Statement getStatement() { return moStatement; }
    public DCfd getCfd() { return moCfd; }

    public void closeSession() {
        try {
            if (moStatement != null && !moStatement.isClosed()) {
                moStatement.close();
            }
            if (moCompanyDatabase != null) {
                moCompanyDatabase.disconnect();
            }
            if (moConnectionMonitor.isAlive()) {
                moConnectionMonitor.stopMonitor();
            }
        }
        catch (SQLException e) {
            moServer.renderMessageLn(msSessionServer + e);
        }
        catch (Exception e) {
            moServer.renderMessageLn(msSessionServer + e);
        }
    }

    @Override
    public SSrvResponse request(SSrvRequest request) throws RemoteException {
        SSrvLock lock = null;
        SSrvResponse response = new SSrvResponse(SSrvConsts.RESP_TYPE_OK);

        try {
            switch (request.getRequestType()) {
                case SSrvConsts.REQ_LOCK_GAIN:
                    lock = (SSrvLock) request.getPacket();
                    response.setPacket(moServer.getServiceDataLocks().gainLock(mnSessionId, lock.getCompanyId(), lock.getRegistryType(), lock.getPrimaryKey(), lock.getTimeout()));
                    break;

                case SSrvConsts.REQ_LOCK_RECOVER:
                    lock = (SSrvLock) request.getPacket();
                    response.setPacket(moServer.getServiceDataLocks().recoverLock(mnSessionId, lock.getCompanyId(), lock.getRegistryType(), lock.getPrimaryKey(), lock.getTimeout(), lock.getTimestamp()));
                    break;

                case SSrvConsts.REQ_LOCK_STATUS:
                    lock = (SSrvLock) request.getPacket();
                    response.setPacket(moServer.getServiceDataLocks().getLockStatus(lock.getLockId()));
                    if ((Integer) response.getPacket() == 0){
                        response.setPacket( SSrvConsts.LOCK_ST_EXPIRED );
                    }
                    break;

                case SSrvConsts.REQ_LOCK_RELEASE:
                    lock = (SSrvLock) request.getPacket();
                    moServer.getServiceDataLocks().releaseLock(lock.getLockId());
                    break;

                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        catch (Exception e) {
            response.setResponseType(SSrvConsts.RESP_TYPE_ERROR);
            response.setMessage(e.toString());
            moServer.renderMessageLn(msSessionServer + e);
        }

        return response;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SServerResponse request(SServerRequest poRequest) throws RemoteException {
        int nResult = SLibConsts.UNDEFINED;
        Object[] oParams = null;
        SServerResponse oResponse = new SServerResponse(SSrvConsts.RESP_TYPE_OK);

        try {
            switch (poRequest.getRequestType()) {
                case SServerConstants.REQ_COMP_ITEMS_COMBO_BOX:
                    oResponse.setPacket(SDataReadComponentItems.getComponentItemsForComboBox(poRequest.getRegistryType(), moStatement, moServer.getParamsErp(), poRequest.getPrimaryKey()));
                    break;

                case SServerConstants.REQ_COMP_ITEMS_LIST:
                    oResponse.setPacket(SDataReadComponentItems.getComponentItemsForList(poRequest.getRegistryType(), moStatement, moServer.getParamsErp(), poRequest.getPrimaryKey()));
                    break;
                case SServerConstants.REQ_REGS:
                    oResponse.setPacket(SDataReadRegistries.readRegistries(moStatement, poRequest.getRegistryType(), poRequest.getPrimaryKey()));
                    break;

                case SServerConstants.REQ_REPS:
                    oResponse.setPacket(requestFillReport(poRequest.getRegistryType(), (Map<String, Object>) poRequest.getPacket()));
                    break;

                case SServerConstants.REQ_CHECK:
                    oResponse.setPacket(requestFillCheck((Object[]) poRequest.getPacket()));
                    break;

                case SServerConstants.REQ_DB_CAN_READ:
                    nResult = ((SDataRegistry) poRequest.getPacket()).canRead(moCompanyDatabase.getConnection());
                    break;

                case SServerConstants.REQ_DB_CAN_SAVE:
                    nResult = ((SDataRegistry) poRequest.getPacket()).canSave(moCompanyDatabase.getConnection());
                    break;

                case SServerConstants.REQ_DB_CAN_ANNUL:
                    nResult = ((SDataRegistry) poRequest.getPacket()).canAnnul(moCompanyDatabase.getConnection());
                    break;

                case SServerConstants.REQ_DB_CAN_DELETE:
                    nResult = ((SDataRegistry) poRequest.getPacket()).canDelete(moCompanyDatabase.getConnection());
                    break;

                case SServerConstants.REQ_DB_ACTION_READ:
                    nResult = ((SDataRegistry) poRequest.getPacket()).read(poRequest.getPrimaryKey(), moStatement);
                    break;

                case SServerConstants.REQ_DB_ACTION_SAVE:
                    startTransac();
                    nResult = ((SDataRegistry) poRequest.getPacket()).save(moCompanyDatabase.getConnection());
                    if (nResult == SLibConstants.DB_ACTION_SAVE_OK) {
                        commitTransac();
                    }
                    else {
                        rollbackTransac();
                    }
                    break;

                case SServerConstants.REQ_DB_ACTION_ANNUL:
                    startTransac();
                    nResult = ((SDataRegistry) poRequest.getPacket()).annul(moCompanyDatabase.getConnection());
                    if (nResult == SLibConstants.DB_ACTION_ANNUL_OK) {
                        commitTransac();
                    }
                    else {
                        rollbackTransac();
                    }
                    break;

                case SServerConstants.REQ_DB_ACTION_DELETE:
                    startTransac();
                    nResult = ((SDataRegistry) poRequest.getPacket()).delete(moCompanyDatabase.getConnection());
                    if (nResult == SLibConstants.DB_ACTION_DELETE_OK) {
                        commitTransac();
                    }
                    else {
                        rollbackTransac();
                    }
                    break;

                case SServerConstants.REQ_DB_ACTION_PROCESS:
                    startTransac();
                    nResult = ((SDataRegistry) poRequest.getPacket()).process(moCompanyDatabase.getConnection());
                    if (nResult == SLibConstants.DB_ACTION_PROCESS_OK) {
                        commitTransac();
                    }
                    else {
                        rollbackTransac();
                    }
                    break;

                case SServerConstants.REQ_DB_QUERY:
                    oResponse.setPacket(requestExecuteQuery((SQueryRequest) poRequest.getPacket()));
                    break;

                case SServerConstants.REQ_DB_QUERY_SIMPLE:
                    oResponse.setPacket(requestExecuteQuerySimple((String) poRequest.getPacket()));
                    break;

                case SServerConstants.REQ_DB_PROCEDURE:
                    oResponse.setPacket(requestCallProcedure((erp.lib.data.SDataProcedure) poRequest.getPacket()));
                    break;

                case SServerConstants.REQ_DB_CATALOGUE_DESCRIPTION:
                    oResponse.setPacket(requestReadCatalogueDescription(poRequest.getPacket()));
                    break;

                case SServerConstants.REQ_OBJ_FIN_ACC_TAX_ID:
                    oParams = (Object[]) poRequest.getPacket();
                    oResponse.setPacket(SFinAccountUtilities.obtainTaxAccountId(
                            (int[]) oParams[0], (Integer) oParams[1], (java.util.Date) oParams[2],
                            (Integer) oParams[3], moStatement));
                    break;

                case SServerConstants.REQ_OBJ_FIN_ACC_BP:
                    oParams = (Object[]) poRequest.getPacket();
                    oResponse.setPacket(SFinAccountUtilities.obtainBizPartnerAccountConfigs(
                            (Integer) oParams[0], (Integer) oParams[1], (Integer) oParams[2],
                            (java.util.Date) oParams[3], (Integer) oParams[4], (Boolean) oParams[5], moStatement));
                    break;

                case SServerConstants.REQ_OBJ_FIN_ACC_ITEM:
                    oParams = (Object[]) poRequest.getPacket();
                    oResponse.setPacket(SFinAccountUtilities.obtainItemAccountConfigs(
                            (Integer) oParams[0], (Integer) oParams[1], (java.util.Date) oParams[2],
                            (Integer) oParams[3], (Boolean) oParams[4], moStatement));
                    break;

                case SServerConstants.REQ_CFD:
                    startTransac();
                    nResult = requestEmitCfd((erp.mtrn.data.SCfdPacket) poRequest.getPacket());
                    if (nResult == SLibConstants.DB_CFD_OK) {
                        commitTransac();
                    }
                    else {
                        rollbackTransac();
                    }
                    break;

                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }
        }
        catch (net.sf.jasperreports.engine.JRException e) {
            oResponse.setResponseType(SSrvConsts.RESP_TYPE_ERROR);
            oResponse.setMessage(e.toString());
            moServer.renderMessageLn(msSessionServer + e);
        }
        catch (java.sql.SQLException e) {
            oResponse.setResponseType(SSrvConsts.RESP_TYPE_ERROR);
            oResponse.setMessage(e.toString());
            moServer.renderMessageLn(msSessionServer + e);
        }
        catch (java.lang.Exception e) {
            oResponse.setResponseType(SSrvConsts.RESP_TYPE_ERROR);
            oResponse.setMessage(e.toString());
            moServer.renderMessageLn(msSessionServer + e);
        }
        finally {
            switch (poRequest.getRequestType()) {
                case SServerConstants.REQ_DB_ACTION_READ:
                case SServerConstants.REQ_DB_ACTION_SAVE:
                case SServerConstants.REQ_DB_ACTION_PROCESS:
                    oResponse.setResultType(nResult);
                    oResponse.setPacket(((SDataRegistry) poRequest.getPacket()));
                    oResponse.setMessage(((SDataRegistry) poRequest.getPacket()).getDbmsError());
                    break;
                case SServerConstants.REQ_DB_CAN_READ:
                case SServerConstants.REQ_DB_CAN_SAVE:
                case SServerConstants.REQ_DB_CAN_ANNUL:
                case SServerConstants.REQ_DB_CAN_DELETE:
                case SServerConstants.REQ_DB_ACTION_ANNUL:
                case SServerConstants.REQ_DB_ACTION_DELETE:
                    oResponse.setResultType(nResult);
                    oResponse.setMessage(((SDataRegistry) poRequest.getPacket()).getDbmsError());
                    break;
                case SServerConstants.REQ_CFD:
                    oResponse.setResultType(nResult);
                    break;
                default:
                    break;
            }

            if (!mbIsTransactionClosed) {
                try {
                    rollbackTransac();
                }
                catch (java.sql.SQLException e) {
                    oResponse.setResponseType(SSrvConsts.RESP_TYPE_ERROR);
                    oResponse.setMessage(e.toString());
                    moServer.renderMessageLn(msSessionServer + e);
                }
                catch (java.lang.Exception e) {
                    oResponse.setResponseType(SSrvConsts.RESP_TYPE_ERROR);
                    oResponse.setMessage(e.toString());
                    moServer.renderMessageLn(msSessionServer + e);
                }
            }
        }

        return oResponse;
    }

    public static byte[] converterBufferedImageToByteArray(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", byteArrayOS);

        return  byteArrayOS.toByteArray();
    }

    @Override
    public void finalize() throws Throwable {
        closeSession();
    }
}
