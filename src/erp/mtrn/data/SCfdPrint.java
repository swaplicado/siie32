/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import cfd.DCfdUtils;
import cfd.DElement;
import cfd.ver3.nom12.DElementOtroPago;
import cfd.ver33.DCfdi33Consts;
import cfd.ver33.DElementCfdiRelacionado;
import cfd.ver33.DElementConcepto;
import erp.cfd.SCfdConsts;
import erp.cfd.SCfdXmlCatalogs;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mbps.data.SDataBizPartnerBranchContact;
import erp.mcfg.data.SDataCurrency;
import erp.mhrs.data.SDataFormerPayroll;
import erp.mhrs.data.SDataFormerPayrollEmp;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbConfig;
import erp.mod.hrs.db.SDbPayroll;
import erp.mod.hrs.db.SDbPayrollReceipt;
import erp.mod.hrs.db.SHrsConsts;
import erp.mod.hrs.db.SHrsUtils;
import erp.print.SDataConstantsPrint;
import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author Sergio Flores, Alfredo Pérez, Sergio Flores
 */
public class SCfdPrint {

    private static final int CFDI33_NOM12_MAX_LINES_DETAIL = 13;
    private static final int CFDI33_NOM12_MAX_LINES_EXTRA = 5;
    
    private erp.client.SClientInterface miClient;

    public SCfdPrint(erp.client.SClientInterface client) {
        miClient = client;
    }
    
    /**
     * Computes report.
     * @param cfd
     * @param reportType
     * @param map
     * @param printMode Constants defined in SDataConstantsPrint.PRINT_MODE_...
     * @param copies
     * @throws Exception 
     */
    private void computeReport(final erp.mtrn.data.SDataCfd cfd, final int reportType, final Map<String, Object> map, final int printMode, final int copies) throws Exception {
        JasperPrint jasperPrint = SDataUtilities.fillReport(miClient, reportType, map);

        switch (printMode) {
            case SDataConstantsPrint.PRINT_MODE_VIEWER:
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Comprobante Fiscal Digital por Internet");
                jasperViewer.setVisible(true);
                break;
                
            case SDataConstantsPrint.PRINT_MODE_PDF_FILE:
                String sPdfFileName = cfd.getDocXmlName().substring(0, cfd.getDocXmlName().lastIndexOf(".xml"));
                sPdfFileName = miClient.getSessionXXX().getParamsCompany().getXmlBaseDirectory() + sPdfFileName + ".pdf";
                JasperExportManager.exportReportToPdfFile(jasperPrint, sPdfFileName);
                break;
                
            case SDataConstantsPrint.PRINT_MODE_PRINT:
                for (int copy = 1; copy <= copies; copy++) {
                    JasperPrintManager.printReport(jasperPrint, false);
                }
                break;
                
            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }
    }

    /**
     * Prints CFD.
     * @param cfd CFD
     * @param printMode Constants defined in SDataConstantsPrint.PRINT_MODE_...
     * @param dps DPS
     * @throws java.lang.Exception
     * XXX 2018-01-12 (Sergio Flores): normalize these parameters because CFD is contained in DPS! WTF!
     */
    @Deprecated
    public void printCfd(final erp.mtrn.data.SDataCfd cfd, final int printMode, final erp.mtrn.data.SDataDps dps) throws java.lang.Exception {
        int nFkEmiAddressFormatTypeId_n = 0;
        int nFkRecAddressFormatTypeId_n = 0;
        double dSubtotalProvisionalCy = 0;
        double dDiscountDocCy = 0;
        double dSubtotalCy = 0;
        double dTotalCy = 0;
        double dTotalPesoBruto = 0;
        double dTotalPesoNeto = 0;
        float fVersion = 0;
        Map<String, Object> map = null;
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = docBuilder.parse(new ByteArrayInputStream(cfd.getDocXml().getBytes("UTF-8")));
        SDataCurrency cur = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR, new int[] { dps.getFkCurrencyId() }, SLibConstants.EXEC_MODE_SILENT);
        SDataBizPartnerBranchAddress address = null;
        SDataBizPartnerBranchContact contact = null;
        SDataBizPartnerBranch emisor = null;
        SDataBizPartnerBranch receptor = null;
        SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP,
                    new int[] { dps.getFkBizPartnerId_r() }, SLibConstants.EXEC_MODE_SILENT);
        Node node = null;
        Node nodeChild = null;
        NamedNodeMap namedNodeMap = null;
        NamedNodeMap namedNodeMapChild = null;
        String sDocType = "";
        String sDocValueText = "";
        String sTelsEmiDom = "";
        String sTelsEmiExp = "";

        if (dps.getFkBizPartnerBranchAddressId() != 1) {
            address = (SDataBizPartnerBranchAddress) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB_ADD,
                    new int[] { dps.getFkBizPartnerBranchId(), dps.getFkBizPartnerBranchAddressId() }, SLibConstants.EXEC_MODE_SILENT);
        }

        for (SDataDpsEntry entry : dps.getDbmsDpsEntries()) {
            if (entry.isAccountable()) {
                dSubtotalProvisionalCy += entry.getSubtotalProvisionalCy_r();
                dDiscountDocCy += entry.getDiscountDocCy();
                dSubtotalCy += entry.getSubtotalCy_r();
                dTotalCy += entry.getTotalCy_r();
            }
        }

        sDocType = SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNU_TP_DPS, new int[] { dps.getFkDpsCategoryId(), dps.getFkDpsClassId(), dps.getFkDpsTypeId() });
        sDocValueText = SLibUtilities.translateValueToText(dTotalCy, miClient.getSessionXXX().getParamsErp().getDecimalsValue(), dps.getFkLanguajeId(), cur.getTextSingular(), cur.getTextPlural(), cur.getTextPrefix(), cur.getTextSuffix());

        if (sDocType.indexOf("CTE.") != -1) {
            sDocType = SLibUtilities.textTrim(sDocType.substring(0, sDocType.indexOf("CTE.")));
        }

        map = miClient.createReportParams();

        map.put("sXmlBaseDir", miClient.getSessionXXX().getParamsCompany().getXmlBaseDirectory());
        map.put("nPkYearId", cfd.getFkDpsYearId_n());
        map.put("nPkDocId", cfd.getFkDpsDocId_n());
        map.put("sDocTipo", sDocType);
        map.put("bDocEsMonedaLocal", miClient.getSessionXXX().getParamsErp().getFkCurrencyId() == dps.getFkCurrencyId());
        map.put("dDocSubtotalProvisional", dSubtotalProvisionalCy);
        map.put("dDocDescuento", dDiscountDocCy);
        map.put("dDocSubtotal", dSubtotalCy);
        map.put("dDocTotal", dTotalCy);
        map.put("sDocTotalConLetra", sDocValueText);
        map.put("sDocCadenaOriginal", cfd.getStringSigned());
        map.put("bIsAnnulled", dps.getFkDpsStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED);
        map.put("bIsDeleted", dps.getIsDeleted());

        if (miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().size() > 0) {
            contact = miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(0);
            sTelsEmiDom += (sTelsEmiDom.length() == 0 || contact.getAuxTelephone01().length() == 0 ? "" : ", ") + contact.getAuxTelephone01();
            sTelsEmiDom += (sTelsEmiDom.length() == 0 || contact.getAuxTelephone02().length() == 0 ? "" : ", ") + contact.getAuxTelephone02();
            sTelsEmiDom += (sTelsEmiDom.length() == 0 || contact.getAuxTelephone03().length() == 0 ? "" : ", ") + contact.getAuxTelephone03();
            sTelsEmiDom += (sTelsEmiDom.length() == 0 || contact.getEmail01().length() == 0 ? "" : ", ") + contact.getEmail01();
        }

        map.put("sTelEmiDom", sTelsEmiDom);

        if(miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsBizPartnerBranch(new int[]{dps.getFkCompanyBranchId()}).getDbmsBizPartnerBranchContacts().size() > 0) {
            contact = miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsBizPartnerBranch(new int[]{dps.getFkCompanyBranchId()}).getDbmsBizPartnerBranchContacts().get(0);
            sTelsEmiExp += (sTelsEmiExp.length() == 0 || contact.getAuxTelephone01().length() == 0 ? "" : ", ") + contact.getAuxTelephone01();
            sTelsEmiExp += (sTelsEmiExp.length() == 0 || contact.getAuxTelephone02().length() == 0 ? "" : ", ") + contact.getAuxTelephone02();
            sTelsEmiExp += (sTelsEmiExp.length() == 0 || contact.getAuxTelephone03().length() == 0 ? "" : ", ") + contact.getAuxTelephone03();
            sTelsEmiExp += (sTelsEmiExp.length() == 0 || contact.getEmail01().length() == 0 ? "" : ", ") + contact.getEmail01();
        }

        map.put("sTelEmiExp", sTelsEmiExp);

        contact = miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().size() <= 1 ? null :
                miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(1);

        map.put("sManagerFinance", contact == null ? "" : SLibUtilities.textTrim(contact.getContactPrefix() + " " + contact.getFirstname() + " " + contact.getLastname()));

        // Comprobante:

        node = SXmlUtils.extractElements(doc, "Comprobante").item(0);
        namedNodeMap = node.getAttributes();

        map.put("sCfdVersion", SXmlUtils.extractAttributeValue(namedNodeMap, "version", true));
        map.put("sCfdSerieOpc", SXmlUtils.extractAttributeValue(namedNodeMap, "serie", false));
        map.put("sCfdFolio", SXmlUtils.extractAttributeValue(namedNodeMap, "folio", true));
        map.put("sCfdFecha", SXmlUtils.extractAttributeValue(namedNodeMap, "fecha", true));
        map.put("sCfdSello", SXmlUtils.extractAttributeValue(namedNodeMap, "sello", true));
        map.put("nCfdNoAprobacion", SLibUtilities.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, "noAprobacion", true)));
        map.put("nCfdAnoAprobacion", SLibUtilities.parseInt(SXmlUtils.extractAttributeValue(namedNodeMap, "anoAprobacion", true)));
        map.put("sCfdFormaDePago", SXmlUtils.extractAttributeValue(namedNodeMap, "formaDePago", true));
        map.put("sCfdNoCertificado", SXmlUtils.extractAttributeValue(namedNodeMap, "noCertificado", true));
        map.put("sCfdCondicionesDePagoOpc", SXmlUtils.extractAttributeValue(namedNodeMap, "condicionesDePago", false));
        map.put("dCfdSubTotal", SLibUtilities.parseDouble(SXmlUtils.extractAttributeValue(namedNodeMap, "subTotal", true)));
        map.put("dCfdDescuentoOpc", SLibUtilities.parseDouble(SXmlUtils.extractAttributeValue(namedNodeMap, "descuento", false)));
        map.put("sCfdMotivoDescuentoOpc", SXmlUtils.extractAttributeValue(namedNodeMap, "motivoDescuento", false));
        map.put("dCfdTotal", SLibUtilities.parseDouble(SXmlUtils.extractAttributeValue(namedNodeMap, "total", true)));
        map.put("sCfdMetodoDePagoOpc", SXmlUtils.extractAttributeValue(namedNodeMap, "metodoDePago", false));
        map.put("sCfdTipoDeComprobante", SXmlUtils.extractAttributeValue(namedNodeMap, "tipoDeComprobante", true));

        fVersion = Float.parseFloat(SXmlUtils.extractAttributeValue(namedNodeMap, "version", true));

        if (fVersion == 2.2f) {
            map.put("sCfdNoCuentaPago", SXmlUtils.extractAttributeValue(namedNodeMap, "NumCtaPago", false));
        }

        // Emisor:

        node = SXmlUtils.extractElements(doc, "Emisor").item(0);
        namedNodeMap = node.getAttributes();

        map.put("sEmiRfc", SXmlUtils.extractAttributeValue(namedNodeMap, "rfc", true));
        map.put("sEmiNombre", SXmlUtils.extractAttributeValue(namedNodeMap, "nombre", true));

        nodeChild = SXmlUtils.extractChildElements(node, "DomicilioFiscal").get(0);
        namedNodeMapChild = nodeChild.getAttributes();

        emisor = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB,new int[] { dps.getFkCompanyBranchId(), dps.getFkCompanyBranchId() } , SLibConstants.EXEC_MODE_SILENT);

        if(emisor.getFkAddressFormatTypeId_n() != SLibConsts.UNDEFINED) {
            nFkEmiAddressFormatTypeId_n = emisor.getFkAddressFormatTypeId_n();
        }
        else {
            nFkEmiAddressFormatTypeId_n = miClient.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n();
        }

        map.put("sEmiDomCalle", SXmlUtils.extractAttributeValue(namedNodeMapChild, "calle", true));
        map.put("sEmiDomNoExteriorOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "noExterior", false));
        map.put("sEmiDomNoInteriorOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "noInterior", false));
        map.put("sEmiDomColoniaOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "colonia", false));
        map.put("sEmiDomLocalidadOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "localidad", false));
        map.put("sEmiDomReferenciaOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "referencia", false));
        map.put("sEmiDomMunicipio", SXmlUtils.extractAttributeValue(namedNodeMapChild, "municipio", true));
        map.put("sEmiDomEstado", SXmlUtils.extractAttributeValue(namedNodeMapChild, "estado", true));
        map.put("sEmiDomPais", SXmlUtils.extractAttributeValue(namedNodeMapChild, "pais", true));
        map.put("sEmiDomCodigoPostal", SXmlUtils.extractAttributeValue(namedNodeMapChild, "codigoPostal", true));
        map.put("nFkEmiAddressFormatTypeId_n", nFkEmiAddressFormatTypeId_n);

        if (SXmlUtils.hasChildElement(node, "ExpedidoEn")) {
            nodeChild = SXmlUtils.extractChildElements(node, "ExpedidoEn").get(0);
            namedNodeMapChild = nodeChild.getAttributes();

            map.put("sEmiExpCalleOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "calle", false));
            map.put("sEmiExpNoExteriorOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "noExterior", false));
            map.put("sEmiExpNoInteriorOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "noInterior", false));
            map.put("sEmiExpColoniaOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "colonia", false));
            map.put("sEmiExpLocalidadOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "localidad", false));
            map.put("sEmiExpReferenciaOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "referencia", false));
            map.put("sEmiExpMunicipioOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "municipio", false));
            map.put("sEmiExpEstadoOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "estado", false));
            map.put("sEmiExpPais", SXmlUtils.extractAttributeValue(namedNodeMapChild, "pais", true));
            map.put("sEmiExpCodigoPostalOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "codigoPostal", false));
        }

        if (fVersion == 2.2f) {
            nodeChild = SXmlUtils.extractChildElements(node, "RegimenFiscal").get(0);
            namedNodeMapChild = nodeChild.getAttributes();

            if (SXmlUtils.extractAttributeValue(namedNodeMapChild, "Regimen", false).length() > 0) {
                map.put("sEmiRegimenFiscal", SXmlUtils.extractAttributeValue(namedNodeMapChild, "Regimen", true));
            }
        }

        // Receptor:

        node = SXmlUtils.extractElements(doc, "Receptor").item(0);
        namedNodeMap = node.getAttributes();

        map.put("sRecRfc", SXmlUtils.extractAttributeValue(namedNodeMap, "rfc", true));
        map.put("sRecNombreOpc", SXmlUtils.extractAttributeValue(namedNodeMap, "nombre", false));

        nodeChild = SXmlUtils.extractChildElements(node, "Domicilio").get(0);
        namedNodeMapChild = nodeChild.getAttributes();

        receptor = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB,new int[] { dps.getFkBizPartnerBranchId(), dps.getFkBizPartnerBranchAddressId() } , SLibConstants.EXEC_MODE_SILENT);

        if(receptor.getFkAddressFormatTypeId_n() != SLibConsts.UNDEFINED) {
            nFkRecAddressFormatTypeId_n = receptor.getFkAddressFormatTypeId_n();
        }
        else {
            nFkRecAddressFormatTypeId_n = miClient.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n();
        }

        map.put("sRecDomCalleOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "calle", false));
        map.put("sRecDomNoExteriorOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "noExterior", false));
        map.put("sRecDomNoInteriorOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "noInterior", false));
        map.put("sRecDomColoniaOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "colonia", false));
        map.put("sRecDomLocalidadOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "localidad", false));
        map.put("sRecDomReferenciaOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "referencia", false));
        map.put("sRecDomMunicipioOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "municipio", false));
        map.put("sRecDomEstadoOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "estado", false));
        map.put("sRecDomPais", SXmlUtils.extractAttributeValue(namedNodeMapChild, "pais", true));
        map.put("sRecDomCodigoPostalOpc", SXmlUtils.extractAttributeValue(namedNodeMapChild, "codigoPostal", false));
        map.put("nFkRecAddressFormatTypeId_n", nFkRecAddressFormatTypeId_n);

        map.put("sFiscalId", bizPartner.getFiscalFrgId());

        if (address != null) {
            map.put("sRecEnt", address.getAddress());
            map.put("sRecEntCalleOpc", address.getStreet());
            map.put("sRecEntNoExteriorOpc", address.getStreetNumberExt());
            map.put("sRecEntNoInteriorOpc", address.getStreetNumberInt());
            map.put("sRecEntColoniaOpc", address.getNeighborhood());
            map.put("sRecEntLocalidadOpc", address.getLocality());
            map.put("sRecEntReferenciaOpc", address.getReference());
            map.put("sRecEntMunicipioOpc", address.getCounty());
            map.put("sRecEntEstadoOpc", address.getState());
            map.put("sRecEntPais", address.getDbmsDataCountry().getCountry());
            map.put("sRecEntCodigoPostalOpc", address.getZipCode());
        }

        // Addenda:

        for (SDataDpsEntry entry : dps.getDbmsDpsEntries()) {
            if (entry.isAccountable()) {
                dTotalPesoBruto += entry.getWeightGross();
                dTotalPesoNeto += entry.getMass();
            }
        }

        map.put("sAddClaveMoneda", dps.getDbmsCurrencyKey());
        map.put("dAddTipoDeCambio", dps.getExchangeRate());
        map.put("nAddDiasDeCredito", dps.getDaysOfCredit());
        map.put("sAddEmbarque", "");
        map.put("sAddOrdenDeEmbarque", "");
        map.put("sAddOrdenDeCompra", dps.getNumberReference());
        map.put("sAddContrato", dps.getAuxCfdParams().getContrato());
        map.put("sAddPedido", dps.getAuxCfdParams().getPedido());
        map.put("sAddFactura", dps.getAuxCfdParams().getFactura());
        map.put("sAddCliente", dps.getAuxCfdParams().getReceptor().getDbmsCategorySettingsCus().getKey());
        map.put("sAddSucursal", dps.getFkCompanyBranchId());
        map.put("sAddAgente",  dps.getFkSalesAgentId_n());
        map.put("sAddRuta", dps.getAuxCfdParams().getRuta());
        map.put("sAddChofer", dps.getDriver());
        map.put("sAddPlacas", dps.getPlate());
        map.put("sAddBoleto", dps.getTicket());
        map.put("dAddPesoBruto", dTotalPesoBruto);
        map.put("dAddPesoNeto", dTotalPesoNeto);
        map.put("sAddUnidadPesoBruto", dps.getAuxCfdParams().getUnidadPesoBruto());
        map.put("sAddUnidadPesoNeto", dps.getAuxCfdParams().getUnidadPesoBruto());

        map.put("sAddPagFecha", miClient.getSessionXXX().getFormatters().getDateTextFormat().format(dps.getDate()).toUpperCase());
        map.put("sAddPagFechaDeVencimiento", miClient.getSessionXXX().getFormatters().getDateTextFormat().format(SLibTimeUtilities.addDate(dps.getDateStartCredit(), 0, 0, dps.getDaysOfCredit())).toUpperCase());
        map.put("dAddPagImporte", dps.getTotalCy_r());
        map.put("sAddPagClaveMoneda", dps.getDbmsCurrencyKey());
        map.put("dAddPagInteresMoratorio", dps.getAuxCfdParams().getInterestDelayRate());
        
        computeReport(cfd, SDataConstantsSys.REP_TRN_CFD, map, printMode, 1);
    }

    /**
     * Prints CFDI 3.2.
     * @param cfd CFD
     * @param printMode Constants defined in SDataConstantsPrint.PRINT_MODE_...
     * @param dps DPS
     * @throws java.lang.Exception
     * XXX 2018-01-12 (Sergio Flores): normalize these parameters because CFD is contained in DPS! WTF!
     */
    @Deprecated
    public void printCfdi32(final erp.mtrn.data.SDataCfd cfd, final int printMode, final erp.mtrn.data.SDataDps dps) throws java.lang.Exception {
        int nFkEmiAddressFormatTypeId_n = 0;
        int nFkRecAddressFormatTypeId_n = 0;
        double dSubtotalProvisionalCy = 0;
        double dDiscountDocCy = 0;
        double dSubtotalCy = 0;
        double dTotalCy = 0;
        double dTotalPesoBruto = 0;
        double dTotalPesoNeto = 0;
        Map<String, Object> map = null;
        SDataCurrency cur = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR, new int[] { dps.getFkCurrencyId() }, SLibConstants.EXEC_MODE_SILENT);
        SDataBizPartnerBranchAddress address = null;
        SDataBizPartnerBranchContact contact = null;
        SDataBizPartnerBranch emisor = null;
        SDataBizPartnerBranch receptor = null;
        SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { dps.getFkBizPartnerId_r() }, SLibConstants.EXEC_MODE_SILENT);
        String sDocType = "";
        String sDocValueText = "";
        String sTelsEmiDom = "";
        String sTelsEmiExp = "";
        cfd.ver32.DElementComprobante comprobante = null;

        if (dps.getFkBizPartnerBranchAddressId() != 1) {
            address = (SDataBizPartnerBranchAddress) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB_ADD,
                    new int[] { dps.getFkBizPartnerBranchId(), dps.getFkBizPartnerBranchAddressId() }, SLibConstants.EXEC_MODE_SILENT);
        }

        for (SDataDpsEntry entry : dps.getDbmsDpsEntries()) {
            if (entry.isAccountable()) {
                dSubtotalProvisionalCy += entry.getSubtotalProvisionalCy_r();
                dDiscountDocCy += entry.getDiscountDocCy();
                dSubtotalCy += entry.getSubtotalCy_r();
                dTotalCy += entry.getTotalCy_r();
            }
        }

        sDocType = SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNU_TP_DPS, new int[] { dps.getFkDpsCategoryId(), dps.getFkDpsClassId(), dps.getFkDpsTypeId() });
        sDocValueText = SLibUtilities.translateValueToText(dTotalCy, miClient.getSessionXXX().getParamsErp().getDecimalsValue(), dps.getFkLanguajeId(), cur.getTextSingular(), cur.getTextPlural(), cur.getTextPrefix(), cur.getTextSuffix());

        if (sDocType.contains("CTE.")) {
            sDocType = SLibUtilities.textTrim(sDocType.substring(0, sDocType.indexOf("CTE.")));
        }

        map = miClient.createReportParams();

        map.put("sXmlBaseDir", miClient.getSessionXXX().getParamsCompany().getXmlBaseDirectory());
        map.put("nPkYearId", cfd.getFkDpsYearId_n());
        map.put("nPkDocId", cfd.getFkDpsDocId_n());
        map.put("sDocTipo", sDocType);
        map.put("bDocEsMonedaLocal", miClient.getSessionXXX().getParamsErp().getFkCurrencyId() == dps.getFkCurrencyId());
        map.put("dDocSubtotalProvisional", dSubtotalProvisionalCy);
        map.put("dDocDescuento", dDiscountDocCy);
        map.put("dDocSubtotal", dSubtotalCy);
        map.put("dDocTotal", dTotalCy);
        map.put("sDocTotalConLetra", sDocValueText);
        map.put("sDocCadenaOriginal", cfd.getStringSigned());
        map.put("bIsAnnulled", dps.getFkDpsStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED);
        map.put("bIsDeleted", dps.getIsDeleted());

        if (miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().size() > 0) {
            contact = miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(0);
            sTelsEmiDom += (sTelsEmiDom.length() == 0 || contact.getAuxTelephone01().length() == 0 ? "" : ", ") + contact.getAuxTelephone01();
            sTelsEmiDom += (sTelsEmiDom.length() == 0 || contact.getAuxTelephone02().length() == 0 ? "" : ", ") + contact.getAuxTelephone02();
            sTelsEmiDom += (sTelsEmiDom.length() == 0 || contact.getAuxTelephone03().length() == 0 ? "" : ", ") + contact.getAuxTelephone03();
            sTelsEmiDom += (sTelsEmiDom.length() == 0 || contact.getEmail01().length() == 0 ? "" : ", ") + contact.getEmail01();
        }

        map.put("sTelEmiDom", sTelsEmiDom);
        map.put("sWeb", miClient.getSessionXXX().getCompany().getDbmsDataCompany().getWeb() == null ? "" : miClient.getSessionXXX().getCompany().getDbmsDataCompany().getWeb());

        if (miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsBizPartnerBranch(new int[]{dps.getFkCompanyBranchId()}).getDbmsBizPartnerBranchContacts().size() > 0) {
            contact = miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsBizPartnerBranch(new int[]{dps.getFkCompanyBranchId()}).getDbmsBizPartnerBranchContacts().get(0);
            sTelsEmiExp += (sTelsEmiExp.length() == 0 || contact.getAuxTelephone01().length() == 0 ? "" : ", ") + contact.getAuxTelephone01();
            sTelsEmiExp += (sTelsEmiExp.length() == 0 || contact.getAuxTelephone02().length() == 0 ? "" : ", ") + contact.getAuxTelephone02();
            sTelsEmiExp += (sTelsEmiExp.length() == 0 || contact.getAuxTelephone03().length() == 0 ? "" : ", ") + contact.getAuxTelephone03();
            sTelsEmiExp += (sTelsEmiExp.length() == 0 || contact.getEmail01().length() == 0 ? "" : ", ") + contact.getEmail01();
        }

        map.put("sTelEmiExp", sTelsEmiExp);

        contact = miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().size() <= 1 ? null :
                miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(1);

        map.put("sManagerFinance", contact == null ? "" : SLibUtilities.textTrim(contact.getContactPrefix() + " " + contact.getFirstname() + " " + contact.getLastname()));

        // Comprobante:

        SCfdXmlCatalogs catalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        comprobante = DCfdUtils.getCfdi32(cfd.getDocXml());

        map.put("sCfdVersion", comprobante.getAttVersion().getString());
        map.put("sCfdSerieOpc", comprobante.getAttSerie().getString());
        map.put("sCfdFolio", comprobante.getAttFolio().getString());
        map.put("sCfdFecha", SLibUtils.DbmsDateFormatDatetime.format(comprobante.getAttFecha().getDatetime()));
        map.put("sCfdSello", comprobante.getAttSello().getString());
        map.put("sCfdFormaDePago", comprobante.getAttFormaDePago().getOption());
        map.put("sCfdNoCertificado", comprobante.getAttNoCertificado().getString());
        map.put("sCfdCondicionesDePagoOpc", comprobante.getAttCondicionesDePago().getOption());
        map.put("dCfdSubTotal", comprobante.getAttSubTotal().getDouble());
        map.put("dCfdTotal", comprobante.getAttTotal().getDouble());
        
        if (comprobante.getAttMetodoDePago().getString().length() == 2) {
            map.put("sCfdMetodoDePagoOpc", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY, comprobante.getAttMetodoDePago().getString()));
        }
        else {
            map.put("sCfdMetodoDePagoOpc", comprobante.getAttMetodoDePago().getString());
        }
        
        map.put("sExpedidoEn", comprobante.getAttLugarExpedicion().getString());
        map.put("sCfdTipoDeComprobante", comprobante.getAttTipoDeComprobante().getOption());
        map.put("sCfdNoCuentaPago", comprobante.getAttNumCtaPago().getString());

        // Emisor:

        map.put("sEmiRfc", comprobante.getEltEmisor().getAttRfc().getString());
        map.put("sEmiNombre", comprobante.getEltEmisor().getAttNombre().getString());

        emisor = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { dps.getFkCompanyBranchId(), dps.getFkCompanyBranchId() }, SLibConstants.EXEC_MODE_SILENT);

        if (emisor.getFkAddressFormatTypeId_n() != SLibConsts.UNDEFINED) {
            nFkEmiAddressFormatTypeId_n = emisor.getFkAddressFormatTypeId_n();
        }
        else {
            nFkEmiAddressFormatTypeId_n = miClient.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n();
        }

        map.put("sEmiDomCalle", comprobante.getEltEmisor().getEltDomicilioFiscal().getAttCalle().getString());
        map.put("sEmiDomNoExteriorOpc", comprobante.getEltEmisor().getEltDomicilioFiscal().getAttNoExterior().getString());
        map.put("sEmiDomNoInteriorOpc", comprobante.getEltEmisor().getEltDomicilioFiscal().getAttNoInterior().getString());
        map.put("sEmiDomColoniaOpc", comprobante.getEltEmisor().getEltDomicilioFiscal().getAttColonia().getString());
        map.put("sEmiDomLocalidadOpc", comprobante.getEltEmisor().getEltDomicilioFiscal().getAttLocalidad().getString());
        map.put("sEmiDomReferenciaOpc", comprobante.getEltEmisor().getEltDomicilioFiscal().getAttReferencia().getString());
        map.put("sEmiDomMunicipio", comprobante.getEltEmisor().getEltDomicilioFiscal().getAttMunicipio().getString());
        map.put("sEmiDomEstado", comprobante.getEltEmisor().getEltDomicilioFiscal().getAttEstado().getString());
        map.put("sEmiDomPais", comprobante.getEltEmisor().getEltDomicilioFiscal().getAttPais().getString());
        map.put("sEmiDomCodigoPostal", comprobante.getEltEmisor().getEltDomicilioFiscal().getAttCodigoPostal().getString());
        map.put("nFkEmiAddressFormatTypeId_n", nFkEmiAddressFormatTypeId_n);

        if (comprobante.getEltEmisor().getEltHijosRegimenFiscal().size() > 0) { // XXX Fix code: it is possible to have many "RegimenFiscal" nodes!!!
            map.put("sEmiRegimenFiscal", comprobante.getEltEmisor().getEltHijosRegimenFiscal().get(0).getAttRegimen().getString());
        }

        if (comprobante.getEltEmisor().getEltOpcExpedidoEn() != null) {
            map.put("sEmiExpCalleOpc", comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttCalle().getString());
            map.put("sEmiExpNoExteriorOpc", comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttNoExterior().getString());
            map.put("sEmiExpNoInteriorOpc", comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttNoInterior().getString());
            map.put("sEmiExpColoniaOpc", comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttColonia().getString());
            map.put("sEmiExpLocalidadOpc", comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttLocalidad().getString());
            map.put("sEmiExpReferenciaOpc", comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttReferencia().getString());
            map.put("sEmiExpMunicipioOpc", comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttMunicipio().getString());
            map.put("sEmiExpEstadoOpc", comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttEstado().getString());
            map.put("sEmiExpPais", comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttPais().getString());
            map.put("sEmiExpCodigoPostalOpc", comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttCodigoPostal().getString());
        }

        // Receptor:

        map.put("sRecRfc", comprobante.getEltReceptor().getAttRfc().getString());
        map.put("sRecNombreOpc", comprobante.getEltReceptor().getAttNombre().getString());

        receptor = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { dps.getFkBizPartnerBranchId(), dps.getFkBizPartnerBranchAddressId() } , SLibConstants.EXEC_MODE_SILENT);

        if (receptor.getFkAddressFormatTypeId_n() != SLibConsts.UNDEFINED) {
            nFkRecAddressFormatTypeId_n = receptor.getFkAddressFormatTypeId_n();
        }
        else {
            nFkRecAddressFormatTypeId_n = miClient.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n();
        }

        map.put("sRecDomCalleOpc", comprobante.getEltReceptor().getEltDomicilio().getAttCalle().getString());
        map.put("sRecDomNoExteriorOpc", comprobante.getEltReceptor().getEltDomicilio().getAttNoExterior().getString());
        map.put("sRecDomNoInteriorOpc", comprobante.getEltReceptor().getEltDomicilio().getAttNoInterior().getString());
        map.put("sRecDomColoniaOpc", comprobante.getEltReceptor().getEltDomicilio().getAttColonia().getString());
        map.put("sRecDomLocalidadOpc", comprobante.getEltReceptor().getEltDomicilio().getAttLocalidad().getString());
        map.put("sRecDomReferenciaOpc", comprobante.getEltReceptor().getEltDomicilio().getAttReferencia().getString());
        map.put("sRecDomMunicipioOpc", comprobante.getEltReceptor().getEltDomicilio().getAttMunicipio().getString());
        map.put("sRecDomEstadoOpc", comprobante.getEltReceptor().getEltDomicilio().getAttEstado().getString());
        String recDomPais = comprobante.getEltReceptor().getEltDomicilio().getAttPais().getString();
        map.put("sRecDomPais", recDomPais.length() != 3 ? recDomPais : SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.LOCU_CTY, recDomPais, SLibConstants.DESCRIPTION_NAME_LAN).toUpperCase());
        map.put("sRecDomCodigoPostalOpc", comprobante.getEltReceptor().getEltDomicilio().getAttCodigoPostal().getString());
        map.put("nFkRecAddressFormatTypeId_n", nFkRecAddressFormatTypeId_n);
        map.put("sFiscalId", bizPartner.getFiscalFrgId());

        if (address != null) {
            map.put("sRecEnt", address.getAddress());
            map.put("sRecEntCalleOpc", address.getStreet());
            map.put("sRecEntNoExteriorOpc", address.getStreetNumberExt());
            map.put("sRecEntNoInteriorOpc", address.getStreetNumberInt());
            map.put("sRecEntColoniaOpc", address.getNeighborhood());
            map.put("sRecEntLocalidadOpc", address.getLocality());
            map.put("sRecEntReferenciaOpc", address.getReference());
            map.put("sRecEntMunicipioOpc", address.getCounty());
            map.put("sRecEntEstadoOpc", address.getState());
            String recEntPais = address.getDbmsDataCountry().getCountry();
            map.put("sRecEntPais", recEntPais.length() != 3 ? recEntPais : SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.LOCU_CTY, recEntPais, SLibConstants.DESCRIPTION_NAME_LAN).toUpperCase());
            map.put("sRecEntCodigoPostalOpc", address.getZipCode());
        }

        // Stamp:

        if (comprobante.getEltOpcComplemento() != null) {
            for (DElement element : comprobante.getEltOpcComplemento().getElements()) {
                if (element.getName().compareTo("tfd:TimbreFiscalDigital") == 0) {
                    cfd.ver32.DElementTimbreFiscalDigital tfd = (cfd.ver32.DElementTimbreFiscalDigital) element;
                    map.put("sCfdiVersion", tfd.getAttVersion().getString());
                    map.put("sCfdiUuid", tfd.getAttUUID().getString());
                    map.put("sCfdiSelloCFD", tfd.getAttSelloCFD().getString());
                    map.put("sCfdiSelloSAT", tfd.getAttSelloSAT().getString());
                    map.put("sCfdiNoCertificadoSAT", tfd.getAttNoCertificadoSAT().getString());
                    map.put("sCfdiFechaTimbre", tfd.getAttFechaTimbrado().getString());
                }
            }
        }

        // QR Code:

        /* IMPORTANT (sflores, 2014-01-15):
         * BufferedImage is not serializable, therefore it cannot be send to Server through RMI. QR Code is generated and put into in SSessionServer.requestFillReport().
         *
         * BufferedImage biQrCode = DCfd.createQrCodeBufferedImage((String) map.get("sEmiRfc"), (String) map.get("sRecRfc"), Double.parseDouble("" + map.get("dCfdTotal")), (String) map.get("sCfdiUuid"));
         * map.put("oCfdiQrCode", biQrCode.getScaledInstance(biQrCode.getWidth(), biQrCode.getHeight(), Image.SCALE_DEFAULT));
         */

        // Addenda:

        for (SDataDpsEntry entry : dps.getDbmsDpsEntries()) {
            if (entry.isAccountable()) {
                dTotalPesoBruto += entry.getWeightGross();
                dTotalPesoNeto += entry.getMass();
            }
        }

        map.put("sAddClaveMoneda", dps.getDbmsCurrencyKey());
        map.put("dAddTipoDeCambio", dps.getExchangeRate());
        map.put("nAddDiasDeCredito", dps.getDaysOfCredit());
        map.put("sAddEmbarque", "");
        map.put("sAddOrdenDeEmbarque", "");
        map.put("sAddOrdenDeCompra", dps.getNumberReference());
        map.put("sAddContrato", dps.getAuxCfdParams().getContrato());
        map.put("sAddPedido", dps.getAuxCfdParams().getPedido());
        map.put("sAddFactura", dps.getAuxCfdParams().getFactura());
        map.put("sAddCliente", dps.getAuxCfdParams().getReceptor().getDbmsCategorySettingsCus().getKey());
        map.put("sAddSucursal", dps.getFkCompanyBranchId());
        map.put("sAddAgente",  dps.getFkSalesAgentId_n());
        map.put("sAddRuta", dps.getAuxCfdParams().getRuta());
        map.put("sAddChofer", dps.getDriver());
        map.put("sAddPlacas", dps.getPlate());
        map.put("sAddBoleto", dps.getTicket());
        map.put("dAddPesoBruto", dTotalPesoBruto);
        map.put("dAddPesoNeto", dTotalPesoNeto);
        map.put("sAddUnidadPesoBruto", dps.getAuxCfdParams().getUnidadPesoBruto());
        map.put("sAddUnidadPesoNeto", dps.getAuxCfdParams().getUnidadPesoBruto());

        map.put("sAddPagFecha", miClient.getSessionXXX().getFormatters().getDateTextFormat().format(dps.getDate()).toUpperCase());
        map.put("sAddPagFechaDeVencimiento", miClient.getSessionXXX().getFormatters().getDateTextFormat().format(SLibTimeUtilities.addDate(dps.getDateStartCredit(), 0, 0, dps.getDaysOfCredit())).toUpperCase());
        map.put("dAddPagImporte", dps.getTotalCy_r());
        map.put("sAddPagClaveMoneda", dps.getDbmsCurrencyKey());
        map.put("dAddPagInteresMoratorio", dps.getAuxCfdParams().getInterestDelayRate());
        
        computeReport(cfd, SDataConstantsSys.REP_TRN_CFDI, map, printMode, 1);
    }
    
    /**
     * Prints CFDI 3.3.
     * @param cfd CFD
     * @param printMode Constants defined in SDataConstantsPrint.PRINT_MODE_...
     * @param dps DPS
     * @throws java.lang.Exception
     * XXX 2018-01-12 (Sergio Flores): normalize these parameters because CFD is contained in DPS! WTF!
     */
    public void printCfdi33(final erp.mtrn.data.SDataCfd cfd, final int printMode, final erp.mtrn.data.SDataDps dps) throws java.lang.Exception {
        // CFDI's header:
        
        SDataCurrency currency = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR, new int[] { dps.getFkCurrencyId() }, SLibConstants.EXEC_MODE_SILENT);
        String sDocTotal = SLibUtilities.translateValueToText(dps.getTotalCy_r(), miClient.getSessionXXX().getParamsErp().getDecimalsValue(), dps.getFkLanguajeId(), currency.getTextSingular(), currency.getTextPlural(), currency.getTextPrefix(), currency.getTextSuffix());
        String sDocType = SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNU_TP_DPS, dps.getDpsTypeKey());
        
        if (sDocType.contains("CTE.")) {
            sDocType = SLibUtilities.textTrim(sDocType.substring(0, sDocType.indexOf("CTE.")));
        }

        Map<String, Object> paramsMap = miClient.createReportParams();

        paramsMap.put("sXmlBaseDir", miClient.getSessionXXX().getParamsCompany().getXmlBaseDirectory());
        paramsMap.put("nPkYearId", cfd.getFkDpsYearId_n());
        paramsMap.put("nPkDocId", cfd.getFkDpsDocId_n());
        paramsMap.put("sDocTipo", sDocType);
        paramsMap.put("bDocEsMonedaLocal", miClient.getSession().getSessionCustom().isLocalCurrency(new int[] { dps.getFkCurrencyId() }));
        paramsMap.put("dDocSubtotalProvisional", dps.getSubtotalProvisionalCy_r());
        paramsMap.put("dDocDescuento", dps.getDiscountDocCy_r());
        paramsMap.put("dDocSubtotal", dps.getSubtotalCy_r());
        paramsMap.put("dDocTotal", dps.getTotalCy_r());
        paramsMap.put("sDocTotalConLetra", sDocTotal);
        paramsMap.put("sDocCadenaOriginal", cfd.getStringSigned());
        paramsMap.put("bIsAnnulled", dps.getFkDpsStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED);
        paramsMap.put("bIsDeleted", dps.getIsDeleted());
        
        // CFDI's emisor:

        SDataBizPartner emisor = miClient.getSessionXXX().getCompany().getDbmsDataCompany();
        SDataBizPartnerBranch emisorBranch = emisor.getDbmsHqBranch();
        SDataBizPartnerBranchAddress emisorBranchAddress = emisorBranch.getDbmsBizPartnerBranchAddressOfficial();
        SDataBizPartnerBranch emisorBranchIssue = dps.getFkCompanyBranchId() == emisorBranch.getPkBizPartnerBranchId() ? null : emisor.getDbmsBizPartnerBranch(new int[] { dps.getFkCompanyBranchId() });
        SDataBizPartnerBranchAddress emisorBranchAddressIssue = dps.getFkCompanyBranchId() == emisorBranch.getPkBizPartnerBranchId() ? null : emisorBranchIssue.getDbmsBizPartnerBranchAddressOfficial();
        
        paramsMap.put("sEmiDomCalle", emisorBranchAddress.getStreet());
        paramsMap.put("sEmiDomNoExteriorOpc", emisorBranchAddress.getStreetNumberExt());
        paramsMap.put("sEmiDomNoInteriorOpc", emisorBranchAddress.getStreetNumberInt());
        paramsMap.put("sEmiDomColoniaOpc", emisorBranchAddress.getNeighborhood().toUpperCase());
        paramsMap.put("sEmiDomLocalidadOpc", emisorBranchAddress.getLocality().toUpperCase());
        paramsMap.put("sEmiDomReferenciaOpc", emisorBranchAddress.getReference());
        paramsMap.put("sEmiDomMunicipio", emisorBranchAddress.getCounty().toUpperCase());
        paramsMap.put("sEmiDomEstado", emisorBranchAddress.getState().toUpperCase());
        paramsMap.put("sEmiDomPais", emisorBranchAddress.getDbmsDataCountry().getCountry().toUpperCase());
        paramsMap.put("sEmiDomCodigoPostal", emisorBranchAddress.getZipCode());
        paramsMap.put("nFkEmiAddressFormatTypeId_n", emisorBranch.getFkAddressFormatTypeId_n() != SLibConsts.UNDEFINED ? emisorBranch.getFkAddressFormatTypeId_n() : miClient.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n());
        
        String sTelsEmiDom = "";
        
        if (emisorBranch.getDbmsBizPartnerBranchContacts().size() > 0) {
            SDataBizPartnerBranchContact contact = emisorBranch.getDbmsBizPartnerBranchContacts().get(0);
            sTelsEmiDom += (sTelsEmiDom.length() == 0 || contact.getAuxTelephone01().length() == 0 ? "" : ", ") + contact.getAuxTelephone01();
            sTelsEmiDom += (sTelsEmiDom.length() == 0 || contact.getAuxTelephone02().length() == 0 ? "" : ", ") + contact.getAuxTelephone02();
            sTelsEmiDom += (sTelsEmiDom.length() == 0 || contact.getAuxTelephone03().length() == 0 ? "" : ", ") + contact.getAuxTelephone03();
            sTelsEmiDom += (sTelsEmiDom.length() == 0 || contact.getEmail01().length() == 0 ? "" : ", ") + contact.getEmail01();
        }
        
        paramsMap.put("sTelEmiDom", sTelsEmiDom);
        paramsMap.put("sWeb", emisor.getWeb());

        if (emisorBranchIssue != null) {
            paramsMap.put("sEmiExpCalleOpc", emisorBranchAddressIssue.getStreet());
            paramsMap.put("sEmiExpNoExteriorOpc", emisorBranchAddressIssue.getStreetNumberExt());
            paramsMap.put("sEmiExpNoInteriorOpc", emisorBranchAddressIssue.getStreetNumberInt());
            paramsMap.put("sEmiExpColoniaOpc", emisorBranchAddressIssue.getNeighborhood().toUpperCase());
            paramsMap.put("sEmiExpLocalidadOpc", emisorBranchAddressIssue.getLocality().toUpperCase());
            paramsMap.put("sEmiExpReferenciaOpc", emisorBranchAddressIssue.getReference());
            paramsMap.put("sEmiExpMunicipioOpc", emisorBranchAddressIssue.getCounty().toUpperCase());
            paramsMap.put("sEmiExpEstadoOpc", emisorBranchAddressIssue.getState().toUpperCase());
            paramsMap.put("sEmiExpPais", emisorBranchAddressIssue.getDbmsDataCountry().getCountry().toUpperCase());
            paramsMap.put("sEmiExpCodigoPostalOpc", emisorBranchAddressIssue.getZipCode());
            
            String sTelsEmiExp = "";

            if (emisorBranchIssue.getDbmsBizPartnerBranchContacts().size() > 0) {
                SDataBizPartnerBranchContact contact = emisorBranchIssue.getDbmsBizPartnerBranchContacts().get(0);
                sTelsEmiExp += (sTelsEmiExp.length() == 0 || contact.getAuxTelephone01().length() == 0 ? "" : ", ") + contact.getAuxTelephone01();
                sTelsEmiExp += (sTelsEmiExp.length() == 0 || contact.getAuxTelephone02().length() == 0 ? "" : ", ") + contact.getAuxTelephone02();
                sTelsEmiExp += (sTelsEmiExp.length() == 0 || contact.getAuxTelephone03().length() == 0 ? "" : ", ") + contact.getAuxTelephone03();
                sTelsEmiExp += (sTelsEmiExp.length() == 0 || contact.getEmail01().length() == 0 ? "" : ", ") + contact.getEmail01();
            }

            paramsMap.put("sTelEmiExp", sTelsEmiExp);
        }

        SDataBizPartnerBranchContact contact = emisorBranch.getDbmsBizPartnerBranchContacts().size() <= 1 ? null : emisorBranch.getDbmsBizPartnerBranchContacts().get(1);

        paramsMap.put("sManagerFinance", contact == null ? "" : SLibUtilities.textTrim(contact.getContactPrefix() + " " + contact.getFirstname() + " " + contact.getLastname()));
        
        // CFDI's receptor:

        SDataBizPartner receptor = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { dps.getFkBizPartnerId_r() }, SLibConstants.EXEC_MODE_SILENT);
        SDataBizPartnerBranch receptorBranch = receptor.getDbmsBizPartnerBranch(new int[] { dps.getFkBizPartnerBranchId() });
        SDataBizPartnerBranchAddress receptorBranchAddress = receptorBranch.getDbmsBizPartnerBranchAddressOfficial();
        SDataBizPartnerBranchAddress receptorBranchAddressDelivery = dps.getFkBizPartnerBranchAddressId() == 1 ? null : receptorBranch.getDbmsBizPartnerBranchAddress(new int[] { dps.getFkBizPartnerBranchId(), dps.getFkBizPartnerBranchAddressId() });
        boolean isLocalLanguage = dps.getFkLanguajeId() == miClient.getSessionXXX().getParamsErp().getFkLanguageId();
        
        paramsMap.put("sRecDomCalleOpc", receptorBranchAddress.getStreet());
        paramsMap.put("sRecDomNoExteriorOpc", receptorBranchAddress.getStreetNumberExt());
        paramsMap.put("sRecDomNoInteriorOpc", receptorBranchAddress.getStreetNumberInt());
        paramsMap.put("sRecDomColoniaOpc", receptorBranchAddress.getNeighborhood());
        paramsMap.put("sRecDomLocalidadOpc", receptorBranchAddress.getLocality());
        paramsMap.put("sRecDomReferenciaOpc", receptorBranchAddress.getReference());
        paramsMap.put("sRecDomMunicipioOpc", receptorBranchAddress.getCounty());
        paramsMap.put("sRecDomEstadoOpc", receptorBranchAddress.getState().toUpperCase());
        paramsMap.put("sRecDomPais", (isLocalLanguage ? receptorBranchAddress.getDbmsDataCountry().getCountry() : receptorBranchAddress.getDbmsDataCountry().getCountryLan()).toUpperCase());
        paramsMap.put("sRecDomCodigoPostalOpc", receptorBranchAddress.getZipCode());
        paramsMap.put("nFkRecAddressFormatTypeId_n", receptorBranch.getFkAddressFormatTypeId_n() != SLibConsts.UNDEFINED ? receptorBranch.getFkAddressFormatTypeId_n() : miClient.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n());
        
        if (receptorBranchAddressDelivery != null) {
            paramsMap.put("sRecEnt", receptorBranchAddressDelivery.getAddress());
            paramsMap.put("sRecEntCalleOpc", receptorBranchAddressDelivery.getStreet());
            paramsMap.put("sRecEntNoExteriorOpc", receptorBranchAddressDelivery.getStreetNumberExt());
            paramsMap.put("sRecEntNoInteriorOpc", receptorBranchAddressDelivery.getStreetNumberInt());
            paramsMap.put("sRecEntColoniaOpc", receptorBranchAddressDelivery.getNeighborhood());
            paramsMap.put("sRecEntLocalidadOpc", receptorBranchAddressDelivery.getLocality());
            paramsMap.put("sRecEntReferenciaOpc", receptorBranchAddressDelivery.getReference());
            paramsMap.put("sRecEntMunicipioOpc", receptorBranchAddressDelivery.getCounty());
            paramsMap.put("sRecEntEstadoOpc", receptorBranchAddressDelivery.getState().toUpperCase());
            paramsMap.put("sRecEntPais", (isLocalLanguage ? receptorBranchAddressDelivery.getDbmsDataCountry().getCountry() : receptorBranchAddressDelivery.getDbmsDataCountry().getCountryLan()).toUpperCase());
            paramsMap.put("sRecEntCodigoPostalOpc", receptorBranchAddressDelivery.getZipCode());
        }
        
        // Comprobante:

        SCfdXmlCatalogs catalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        cfd.ver33.DElementComprobante comprobante = DCfdUtils.getCfdi33(cfd.getDocXml());

        paramsMap.put("sCfdVersion", "" + comprobante.getVersion());
        paramsMap.put("sCfdSerieOpc", comprobante.getAttSerie().getString());
        paramsMap.put("sCfdFolio", comprobante.getAttFolio().getString());
        paramsMap.put("sCfdFecha", SLibUtils.DbmsDateFormatDatetime.format(comprobante.getAttFecha().getDatetime()));
        paramsMap.put("sCfdSello", comprobante.getAttSello().getString());
        paramsMap.put("sCfdFormaDePago", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY, comprobante.getAttFormaPago().getString()));
        paramsMap.put("sCfdNoCertificado", comprobante.getAttNoCertificado().getString());
        paramsMap.put("sCfdCondicionesDePagoOpc", comprobante.getAttCondicionesDePago().getString());
        paramsMap.put("dCfdSubTotal", comprobante.getAttSubTotal().getDouble());
        paramsMap.put("dCfdTotal", comprobante.getAttTotal().getDouble());
        paramsMap.put("sCfdMetodoDePagoOpc", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_PAY_MET, comprobante.getAttMetodoPago().getString()));
        paramsMap.put("sExpedidoEn", comprobante.getAttLugarExpedicion().getString());
        paramsMap.put("sCfdTipoDeComprobante", comprobante.getAttTipoDeComprobante().getString());

        // Emisor:

        paramsMap.put("sEmiRfc", comprobante.getEltEmisor().getAttRfc().getString());
        paramsMap.put("sEmiNombre", comprobante.getEltEmisor().getAttNombre().getString());
        paramsMap.put("sEmiRegimenFiscal", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, comprobante.getEltEmisor().getAttRegimenFiscal().getString()));

        // Receptor:

        paramsMap.put("sRecRfc", comprobante.getEltReceptor().getAttRfc().getString());
        paramsMap.put("sRecNombreOpc", comprobante.getEltReceptor().getAttNombre().getString());
        paramsMap.put("sFiscalId", comprobante.getEltReceptor().getAttNumRegIdTrib().getString());
        paramsMap.put("sCfdUsoCFDI", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_CFD_USE, comprobante.getEltReceptor().getAttUsoCFDI().getString()));

        // Stamp:

        String sello = "";
        
        if (comprobante.getEltOpcComplemento() != null) {
            for (DElement element : comprobante.getEltOpcComplemento().getElements()) {
                if (element.getName().compareTo("tfd:TimbreFiscalDigital") == 0) {
                    cfd.ver33.DElementTimbreFiscalDigital tfd = (cfd.ver33.DElementTimbreFiscalDigital) element;
                    paramsMap.put("sCfdiVersion", tfd.getAttVersion().getString());
                    paramsMap.put("sCfdiUuid", tfd.getAttUUID().getString());
                    paramsMap.put("sCfdiSelloCFD", sello = tfd.getAttSelloCFD().getString());
                    paramsMap.put("sCfdiSelloSAT", tfd.getAttSelloSAT().getString());
                    paramsMap.put("sCfdiNoCertificadoSAT", tfd.getAttNoCertificadoSAT().getString());
                    paramsMap.put("sCfdiFechaTimbre", tfd.getAttFechaTimbrado().getString());
                    paramsMap.put("sCfdiRfcProvCertif", tfd.getAttRfcProvCertif().getString());
                    paramsMap.put("sCfdiLeyenda", tfd.getAttLeyenda().getString());
                }
            }
        }

        // QR Code:

        /* IMPORTANT (sflores, 2014-01-15):
         * BufferedImage is not serializable, therefore it cannot be send to Server through RMI. QR Code is generated and put into in SSessionServer.requestFillReport().
         *
         * biQrCode = DCfd.createQrCodeBufferedImage((String) map.get("sEmiRfc"), (String) map.get("sRecRfc"), Double.parseDouble("" + map.get("dCfdTotal")), (String) map.get("sCfdiUuid"));
         * map.put("oCfdiQrCode", biQrCode.getScaledInstance(biQrCode.getWidth(), biQrCode.getHeight(), Image.SCALE_DEFAULT));
         */

        // params needed by erp.server.SSessionServer.requestFillReport() to generate QR code:
        
        paramsMap.put("sSelloCfdiUltDig", sello.isEmpty() ? SLibUtils.textRepeat("0", DCfdi33Consts.STAMP_LAST_CHARS) : sello.substring(sello.length() - DCfdi33Consts.STAMP_LAST_CHARS, sello.length()));
        
        // Aditional info (formerly Addenda1's info):

        double dTotalPesoBruto = 0;
        double dTotalPesoNeto = 0;
        
        for (SDataDpsEntry entry : dps.getDbmsDpsEntries()) {
            if (entry.isAccountable()) {
                dTotalPesoBruto += entry.getWeightGross();
                dTotalPesoNeto += entry.getMass();
            }
        }

        paramsMap.put("sAddClaveMoneda", dps.getDbmsCurrencyKey());
        paramsMap.put("dAddTipoDeCambio", dps.getExchangeRate());
        paramsMap.put("nAddDiasDeCredito", dps.getDaysOfCredit());
        paramsMap.put("sAddEmbarque", "");
        paramsMap.put("sAddOrdenDeEmbarque", "");
        paramsMap.put("sAddOrdenDeCompra", dps.getNumberReference());
        paramsMap.put("sAddContrato", dps.getAuxCfdParams().getContrato());
        paramsMap.put("sAddPedido", dps.getAuxCfdParams().getPedido());
        paramsMap.put("sAddFactura", dps.getAuxCfdParams().getFactura());
        paramsMap.put("sAddCliente", dps.getAuxCfdParams().getReceptor().getDbmsCategorySettingsCus().getKey());
        paramsMap.put("sAddSucursal", dps.getFkCompanyBranchId());
        paramsMap.put("sAddAgente",  dps.getFkSalesAgentId_n());
        paramsMap.put("sAddRuta", dps.getAuxCfdParams().getRuta());
        paramsMap.put("sAddChofer", dps.getDriver());
        paramsMap.put("sAddPlacas", dps.getPlate());
        paramsMap.put("sAddBoleto", dps.getTicket());
        paramsMap.put("sAddIncoterm", dps.getFkIncotermId() == SModSysConsts.LOGS_INC_NA ? "" : dps.getDbmsIncotermCode());
        paramsMap.put("dAddPesoBruto", dTotalPesoBruto);
        paramsMap.put("dAddPesoNeto", dTotalPesoNeto);
        paramsMap.put("sAddUnidadPesoBruto", dps.getAuxCfdParams().getUnidadPesoBruto());
        paramsMap.put("sAddUnidadPesoNeto", dps.getAuxCfdParams().getUnidadPesoBruto());

        SimpleDateFormat dateFormatLong = new SimpleDateFormat("dd 'de' MMMMM 'de' yyyy");
        
        paramsMap.put("sAddPagFecha", dateFormatLong.format(dps.getDate()).toUpperCase());
        paramsMap.put("sAddPagFechaDeVencimiento", dateFormatLong.format(SLibTimeUtilities.addDate(dps.getDateStartCredit(), 0, 0, dps.getDaysOfCredit())).toUpperCase());
        paramsMap.put("dAddPagImporte", dps.getTotalCy_r());
        paramsMap.put("sAddPagClaveMoneda", dps.getDbmsCurrencyKey());
        paramsMap.put("dAddPagInteresMoratorio", dps.getAuxCfdParams().getInterestDelayRate());
        
        // adds related CFDI's:
        if (comprobante.getEltOpcCfdiRelacionados() != null) {
            paramsMap.put("sCfdiTipoRelacion", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_REL_TP, comprobante.getEltOpcCfdiRelacionados().getAttTipoRelacion().getString()));
            
            String uuids = "";
            for (DElementCfdiRelacionado cfdiRelacionado : comprobante.getEltOpcCfdiRelacionados().getEltCfdiRelacionados()) {
                uuids += (uuids.isEmpty() ? "" : "\n") + cfdiRelacionado.getAttUuid().getString();
            }
            paramsMap.put("sCfdiUUIDsRelacionados", uuids.toUpperCase());
        }
        
        // adds product/service and unit keys from XML:
        
        ArrayList<String> productKeys = new ArrayList<>();
        ArrayList<String> unitKeys = new ArrayList<>();
        
        for (DElementConcepto clave : comprobante.getEltConceptos().getEltConceptos()) {
            productKeys.add(clave.getAttClaveProdServ().getString());
            unitKeys.add(clave.getAttClaveUnidad().getString());
        }
        
        paramsMap.put("saCfdiProdServKeys", productKeys);
        paramsMap.put("saCfdiUnitKeys",  unitKeys);
        
        // add leyend of reliable exporter when needed:
        
        String numberExporter = emisor.getDbmsCategorySettingsCo().getNumberExporter();
        
        if (!receptor.isDomestic(miClient) && !numberExporter.isEmpty() && !numberExporter.equals("0")) {
            SDataBizPartnerBranch branch = emisorBranchIssue != null ? emisorBranchIssue : emisorBranch;
            SDataBizPartnerBranchAddress address = emisorBranchAddressIssue != null ? emisorBranchAddressIssue : emisorBranchAddress;
            SDataBizPartnerBranchContact admor = branch.getDbmsBizPartnerBranchContactByType(SDataConstantsSys.BPSS_TP_CON_ADM, false);
            
            String leyend = "\"El exportador de los productos incluidos en el presente documento " + numberExporter + " declara que, "
                    + "salvo indicación en sentido contrario, estos productos gozan de un origen preferencial "
                    + SLibUtils.textProperCase(address.getDbmsDataCountry().getCountry()) + ".\"";
            String placeAddress = 
                    SLibUtils.textProperCase(address.getLocality()) + ", "
                    + SLibUtils.textProperCase(address.getState()) + ", "
                    + SLibUtils.textProperCase(address.getDbmsDataCountry().getCountry()) + ", "
                    + "a " + dateFormatLong.format(dps.getDate());
            String company = emisor.getBizPartner();
            
            paramsMap.put("sCceExportadorLeyenda", leyend);
            paramsMap.put("sCceExportadorLugarFecha", placeAddress);
            paramsMap.put("sCceExportadorEmpresa", company);
            
            /* XXX 2020-07-24, Sergio Flores: Código temporal para impresión de la leyenda correspondiente a Japón:
            paramsMap.put("sCceExportadorLeyenda", "The exporter of the goods covered by this document (Authorization No. MXJPN/0201-2019) declares that, except where otherwise clearly indicated, these goods are of Mexico preferential origin under Mexico-Japan EPA.");
            paramsMap.put("sCceExportadorLugarFecha", "Morelia, Michoacan, Mexico, July 22, 2020");
            //paramsMap.put("sCceExportadorEmpresa", company);
            */
            
            if (admor != null) {
                paramsMap.put("sCceExportadorResponsable", SLibUtils.textProperCase(admor.getContact()));
                paramsMap.put("sCceExportadorResponsableCargo", SLibUtils.textProperCase(admor.getCharge()));
                
                /* XXX 2020-07-24, Sergio Flores: Código temporal para impresión de la leyenda correspondiente a Japón:
                //paramsMap.put("sCceExportadorResponsable", SLibUtils.textProperCase(admor.getContact()));
                paramsMap.put("sCceExportadorResponsableCargo", "Legal Representative");
                */
            }
        }
        
        computeReport(cfd, SDataConstantsSys.REP_TRN_CFDI_33, paramsMap, printMode, 1);
    }
    
    /**
     * Prints CFDI 3.3 with 'complemento de recepción de pagos' 1.0.
     * @param client
     * @param cfd CFD
     * @param printMode Constants defined in SDataConstantsPrint.PRINT_MODE_...
     * @throws java.lang.Exception
     */
    public void printCfdi33_Crp10(final SClientInterface client, final erp.mtrn.data.SDataCfd cfd, final int printMode) throws java.lang.Exception {
        Map<String, Object> paramsMap = miClient.createReportParams();
        
        // Comprobante:
        
        SCfdXmlCatalogs catalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        cfd.ver33.DElementComprobante comprobante = DCfdUtils.getCfdi33(cfd.getDocXml());
        
        paramsMap.put("sCfdVersion", "" + comprobante.getVersion());    // param needed by erp.server.SSessionServer.requestFillReport() to generate proper QR code
        paramsMap.put("bIsAnnulled", cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED);
        paramsMap.put("bIsDeleted", false);
        
        paramsMap.put("sCfdiSerie", comprobante.getAttSerie().getString());
        paramsMap.put("sCfdiFolio", comprobante.getAttFolio().getString());
        paramsMap.put("sCfdiFecha", SLibUtils.DbmsDateFormatDatetime.format(comprobante.getAttFecha().getDatetime()));
        paramsMap.put("sCfdiSello", comprobante.getAttSello().getString());
        paramsMap.put("sCfdiNoCertificado", comprobante.getAttNoCertificado().getString());
        paramsMap.put("sCfdiCertificado", comprobante.getAttCertificado().getString());
        paramsMap.put("sCfdiLugarExpedicion", comprobante.getAttLugarExpedicion().getString());
        paramsMap.put("sCfdiConfirmacion", comprobante.getAttConfirmacion().getString());
        
        // CFDI Relacionados:
        
        if (comprobante.getEltOpcCfdiRelacionados() != null) {
            if (!comprobante.getEltOpcCfdiRelacionados().getEltCfdiRelacionados().isEmpty()) {
                // if any, there must be only one related CFDI:
                paramsMap.put("sCfdiRelUUID", comprobante.getEltOpcCfdiRelacionados().getEltCfdiRelacionados().get(0).getAttUuid().getString().toUpperCase());
            }
        }
        
        // Emisor:
        
        paramsMap.put("sEmiRfc", comprobante.getEltEmisor().getAttRfc().getString());
        paramsMap.put("sEmiNombre", comprobante.getEltEmisor().getAttNombre().getString());
        paramsMap.put("sEmiRegimenFiscal", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, comprobante.getEltEmisor().getAttRegimenFiscal().getString()));
        
        // Receptor:
        
        paramsMap.put("sRecRfc", comprobante.getEltReceptor().getAttRfc().getString());
        paramsMap.put("sRecNombre", comprobante.getEltReceptor().getAttNombre().getString());
        paramsMap.put("sRecResidenciaFiscal", comprobante.getEltReceptor().getAttResidenciaFiscal().getString());
        paramsMap.put("sRecNumRegIdTrib", comprobante.getEltReceptor().getAttNumRegIdTrib().getString());
        
        // Stamp:
        
        String sello = "";
        
        if (comprobante.getEltOpcComplemento() != null) {
            for (DElement element : comprobante.getEltOpcComplemento().getElements()) {
                if (element instanceof cfd.ver33.DElementTimbreFiscalDigital) {
                    cfd.ver33.DElementTimbreFiscalDigital tfd = (cfd.ver33.DElementTimbreFiscalDigital) element;
                    paramsMap.put("sTfdVersion", tfd.getAttVersion().getString());
                    paramsMap.put("sTfdUUID", tfd.getAttUUID().getString());
                    paramsMap.put("sTfdFechaTimbrado", tfd.getAttFechaTimbrado().getString());
                    paramsMap.put("sTfdRfcProvCertif", tfd.getAttRfcProvCertif().getString());
                    paramsMap.put("sTfdNoCertificadoSAT", tfd.getAttNoCertificadoSAT().getString());
                    paramsMap.put("sTfdSelloCFD", sello = tfd.getAttSelloCFD().getString());
                    paramsMap.put("sTfdSelloSAT", tfd.getAttSelloSAT().getString());
                    paramsMap.put("sTfdLeyenda", tfd.getAttLeyenda().getString());
                }
            }
        }
        
        // QR code:
        
        /* IMPORTANT (sflores, 2014-01-15):
         * BufferedImage is not serializable, therefore it cannot be send to Server through RMI. QR Code is generated and put into in SSessionServer.requestFillReport().
         *
         * biQrCode = DCfd.createQrCodeBufferedImage((String) map.get("sEmiRfc"), (String) map.get("sRecRfc"), Double.parseDouble("" + map.get("dCfdTotal")), (String) map.get("sCfdiUuid"));
         * map.put("oCfdiQrCode", biQrCode.getScaledInstance(biQrCode.getWidth(), biQrCode.getHeight(), Image.SCALE_DEFAULT));
         */
        
        // params needed by erp.server.SSessionServer.requestFillReport() to generate QR code:
        
        paramsMap.put("sSelloCfdiUltDig", sello.isEmpty() ? SLibUtils.textRepeat("0", DCfdi33Consts.STAMP_LAST_CHARS) : sello.substring(sello.length() - DCfdi33Consts.STAMP_LAST_CHARS, sello.length()));
        
        // Provide XML for temporary tables and data for printing in method erp.server.SSessionServer.requestFillReport():
        paramsMap.put("xml", cfd.getDocXml());
        
        computeReport(cfd, SDataConstantsSys.REP_TRN_CFDI_33_CRP_10, paramsMap, printMode, 1);
    }
    
    /**
     * Prints payroll CFDI 3.2 with complement 1.1.
     * @param cfd
     * @param printMode Constants defined in SDataConstantsPrint.PRINT_MODE_...
     * @param numCopies
     * @param cfdSubtype Constants defined in SCfdConsts.CFDI_PAYROLL_VER_...
     * @throws java.lang.Exception 
     */
    @Deprecated
    public void printPayrollReceipt32_11(final SDataCfd cfd, final int printMode, final int numCopies, final int cfdSubtype) throws java.lang.Exception {
        int i = 0;
        int nTotalTiempoExtra = 0;

        double dIngresoAux = 0;
        double dIngresoDiario = 0;
        double dTotalPercepciones = 0;
        double dTotalDeducciones = 0;
        double dTotalTiempoExtraPagado = 0;
        double dTotalIncapacidades = 0;
        double dTotalIsr = 0;

        String sSql = "";

        SDataFormerPayroll oFormerPayroll = null;
        SDataFormerPayrollEmp oFormerPayrollEmployee = null;
        SDbPayroll payroll = null;
        SDbPayrollReceipt payrollReceipt = null;
        SDataCurrency cur = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR, new int[] { miClient.getSessionXXX().getParamsErp().getFkCurrencyId() }, SLibConstants.EXEC_MODE_SILENT);

        cfd.ver32.DElementComprobante comprobante = null;
        HashMap<String, Object> map = null;

        ArrayList aPercepciones = null;
        ArrayList aDeducciones = null;
        ArrayList aTiempoExtra = null;
        ArrayList aIncapacidades = null;

        DecimalFormat oFixedFormat = new DecimalFormat(SLibUtils.textRepeat("0", 3));
        DecimalFormat oFixedFormatAux = new DecimalFormat(SLibUtils.textRepeat("0", 2));

        map = miClient.createReportParams();

        switch (cfdSubtype) {
            case SCfdConsts.CFDI_PAYROLL_VER_OLD:
                oFormerPayroll = new SDataFormerPayroll();
                oFormerPayroll.read(new int[] { cfd.getFkPayrollPayrollId_n() }, miClient.getSession().getStatement());
                
                oFormerPayrollEmployee = new SDataFormerPayrollEmp();
                oFormerPayrollEmployee.read(new int[] { cfd.getFkPayrollPayrollId_n(), cfd.getFkPayrollEmployeeId_n() }, miClient.getSession().getStatement());
                
                sSql = "SELECT id_pay, pay_note AS f_pay_nts FROM hrs_sie_pay WHERE id_pay = " + oFormerPayroll.getPkPayrollId();
                break;
            case SCfdConsts.CFDI_PAYROLL_VER_CUR:
                payroll = new SDbPayroll();
                payroll.read(miClient.getSession(), new int[] { cfd.getFkPayrollReceiptPayrollId_n() });
                
                payrollReceipt = new SDbPayrollReceipt();
                payrollReceipt.read(miClient.getSession(), new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n() });
                
                sSql = "SELECT id_pay, nts AS f_pay_nts FROM hrs_pay WHERE id_pay = " + payroll.getPkPayrollId();
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        SCfdXmlCatalogs catalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        comprobante = DCfdUtils.getCfdi32(cfd.getDocXml());
        
        map.put("sSql", sSql);
        map.put("sCfdFecha", SLibUtils.DbmsDateFormatDatetime.format(comprobante.getAttFecha().getDatetime()));
        map.put("sCfdFormaDePago", comprobante.getAttFormaDePago().getOption());
        map.put("sCfdNoCuentaPago", comprobante.getAttNumCtaPago().getString());
        map.put("sCfdCondicionesDePagoOpc", comprobante.getAttCondicionesDePago().getOption());
        map.put("dCfdSubtotal", comprobante.getAttSubTotal().getDouble());
        map.put("dCfdDescuento", comprobante.getAttDescuento().getDouble());
        map.put("dCfdTotal", comprobante.getAttTotal().getDouble());
        map.put("sCfdMetodoDePagoOpc", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY, comprobante.getAttMetodoDePago().getString()));
        map.put("sExpedidoEn", comprobante.getAttLugarExpedicion().getString());
        map.put("sCfdTipoComprobante", comprobante.getAttTipoDeComprobante().getOption());
        map.put("sCfdNoCuentaPago", comprobante.getAttNumCtaPago().getString());
        map.put("sCfdNoCertificado", comprobante.getAttNoCertificado().getString());
        map.put("sEmiRegimenFiscal", comprobante.getEltEmisor().getEltHijosRegimenFiscal().get(0).getAttRegimen().getString());
        map.put("sEmiRfc", comprobante.getEltEmisor().getAttRfc().getString());
        map.put("sRecRfc", comprobante.getEltReceptor().getAttRfc().getString());
        map.put("dCfdTotal", comprobante.getAttTotal().getDouble());
        map.put("sDocTotalConLetra", SLibUtilities.translateValueToText(comprobante.getAttTotal().getDouble(), miClient.getSessionXXX().getParamsErp().getDecimalsValue(), miClient.getSessionXXX().getParamsErp().getFkLanguageId(),
                cur.getTextSingular(), cur.getTextPlural(), cur.getTextPrefix(), cur.getTextSuffix()));
        map.put("sCfdMoneda", comprobante.getAttMoneda().getString());
        map.put("dCfdTipoCambio", comprobante.getAttTipoCambio().getDouble());
        map.put("ReceptorNombre", comprobante.getEltReceptor().getAttNombre().getString());
        map.put("bIsAnnulled", cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED);
        map.put("nPkPayrollId", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayroll.getPkPayrollId() : payroll.getPkPayrollId());
        map.put("NominaNumTipo", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? (oFormerPayroll.getNumber() + " " + oFormerPayroll.getType()) : (payroll.getNumber() + " " + payroll.getAuxPaymentType()));
        map.put("NominaFolio", comprobante.getAttSerie().getString() + "-" + comprobante.getAttFolio().getString());
        map.put("sXmlBaseDir", miClient.getSessionXXX().getParamsCompany().getXmlBaseDirectory());
        map.put("sCfdVersion", comprobante.getAttVersion().getString());

        map.put("dCfdConceptoCantidad", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttCantidad().getDouble());
        map.put("sCfdConceptoUnidad", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttUnidad().getString());
        map.put("sCfdConceptoNoIdentifiacion", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttNoIdentificacion().getString());
        map.put("sCfdConceptoDescripcion", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttDescripcion().getString());
        map.put("dCfdConceptoValorUnitario", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttValorUnitario().getDouble());
        map.put("dCfdConceptoImporte", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttImporte().getDouble());

        for (DElement element : comprobante.getEltOpcComplemento().getElements()) {

            if (element.getName().compareTo("nomina:Nomina") == 0) {

                map.put("RegistroPatronal", ((cfd.ver3.nom11.DElementNomina) element).getAttRegistroPatronal().getString());
                map.put("NumEmpleado", ((cfd.ver3.nom11.DElementNomina) element).getAttNumEmpleado().getString());
                map.put("CURP", ((cfd.ver3.nom11.DElementNomina) element).getAttCurp().getString());
                //map.put("TipoRegimen", SCfdConsts.RegimenMap.get(((cfd.ver3.nom11.DElementNomina) element).getAttTipoRegimen().getInteger()));
                map.put("TipoRegimen", miClient.getSession().readField(SModConsts.HRSS_TP_REC_SCHE, new int[] { ((cfd.ver3.nom11.DElementNomina) element).getAttTipoRegimen().getInteger() }, SDbRegistry.FIELD_NAME));
                map.put("NumSeguridadSocial", ((cfd.ver3.nom11.DElementNomina) element).getAttNumSeguridadSocial().getString());
                map.put("FechaPago", SLibUtils.DateFormatDate.format(((cfd.ver3.nom11.DElementNomina) element).getAttFechaPago().getDate()));
                map.put("FechaInicialPago", SLibUtils.DateFormatDate.format(((cfd.ver3.nom11.DElementNomina) element).getAttFechaInicialPago().getDate()));
                map.put("FechaFinalPago", SLibUtils.DateFormatDate.format(((cfd.ver3.nom11.DElementNomina) element).getAttFechaFinalPago().getDate()));
                map.put("NumDiasNoLaborados", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getDaysNotWorked() : payrollReceipt.getDaysNotWorked_r());
                map.put("NumDiasLaborados", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getDaysWorked() : payrollReceipt.getDaysWorked()); // XXX Optional
                map.put("NumDiasPagar", 0d); // Calculate?, navalos (2014-03-13)
                map.put("NumDiasPagados", ((cfd.ver3.nom11.DElementNomina) element).getAttNumDiasPagados().getDouble());
                map.put("Departamento", ((cfd.ver3.nom11.DElementNomina) element).getAttDepartamento().getString());
                map.put("CLABE", ((cfd.ver3.nom11.DElementNomina) element).getAttClabe().getString());
                //map.put("Banco", SCfdConsts.BancoMap.get(((cfd.ver3.nom11.DElementNomina) element).getAttBanco().getInteger()));
                if (((cfd.ver3.nom11.DElementNomina) element).getAttBanco().getInteger() > 0) {
                    map.put("Banco", miClient.getSession().readField(SModConsts.HRSS_BANK, new int[] { ((cfd.ver3.nom11.DElementNomina) element).getAttBanco().getInteger() }, SDbRegistry.FIELD_NAME));
                }
                map.put("FechaInicioRelLaboral", SLibUtils.DateFormatDate.format(((cfd.ver3.nom11.DElementNomina) element).getAttFechaInicioRelLaboral().getDate()));
                map.put("Antiguedad", ((cfd.ver3.nom11.DElementNomina) element).getAttAntiguedad().getInteger());
                map.put("Puesto", ((cfd.ver3.nom11.DElementNomina) element).getAttPuesto().getString());
                map.put("TipoContrato", ((cfd.ver3.nom11.DElementNomina) element).getAttTipoContrato().getString());
                map.put("TipoJornada", ((cfd.ver3.nom11.DElementNomina) element).getAttTipoJornada().getString());
                map.put("PeriodicidadPago", ((cfd.ver3.nom11.DElementNomina) element).getAttPeriodicidadPago().getString());
                //map.put("RiesgoPuesto", SCfdConsts.RiesgoMap.get(((cfd.ver3.nom11.DElementNomina) element).getAttRiesgoPuesto().getInteger()));
                map.put("RiesgoPuesto", miClient.getSession().readField(SModConsts.HRSS_TP_POS_RISK, new int[] { ((cfd.ver3.nom11.DElementNomina) element).getAttRiesgoPuesto().getInteger() }, SDbRegistry.FIELD_NAME));
                map.put("TipoPago", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? SModSysConsts.HRSS_TP_PAY_FOR : payrollReceipt.getFkPaymentTypeId());
                map.put("Sueldo", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getSalary() : payrollReceipt.getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_WEE ? payrollReceipt.getSalary() : payrollReceipt.getWage());
                map.put("SalarioBaseCotApor", ((cfd.ver3.nom11.DElementNomina) element).getAttSalarioBaseCotApor().getDouble());
                map.put("SalarioDiarioIntegrado", ((cfd.ver3.nom11.DElementNomina) element).getAttSalarioDiarioIntegrado().getDouble());
                
                if (payrollReceipt.getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_FOR) {
                    dIngresoAux = cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getSalary() : payrollReceipt.getWage();
                    dIngresoDiario = dIngresoAux * SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS;
                    map.put("IngresoDiario", SLibUtils.round(dIngresoDiario, SLibConsts.DATA_TYPE_DEC));
                }

                map.put("TipoEmpleado", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getEmployeeType() : miClient.getSession().readField(SModConsts.HRSU_TP_EMP, new int[] { payrollReceipt.getFkEmployeeTypeId() }, SDbRegistry.FIELD_CODE));
                map.put("Categoria", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getEmployeeCategory() : miClient.getSession().readField(SModConsts.HRSU_TP_WRK, new int[] { payrollReceipt.getFkWorkerTypeId() }, SDbRegistry.FIELD_CODE));
                map.put("TipoSalario", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getSalaryType() : miClient.getSession().readField(SModConsts.HRSS_TP_SAL, new int[] { payrollReceipt.getFkSalaryTypeId() }, SDbRegistry.FIELD_NAME));
                map.put("Ejercicio", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? (oFormerPayroll.getYear() + "-" + oFixedFormatAux.format(oFormerPayroll.getPeriod())) : (payroll.getPeriodYear() + "-" + oFixedFormatAux.format(payroll.getPeriod())));

                aPercepciones = new ArrayList();
                aDeducciones = new ArrayList();
                aTiempoExtra = new ArrayList();
                aIncapacidades = new ArrayList();

                dTotalPercepciones = 0;
                dTotalDeducciones = 0;
                nTotalTiempoExtra = 0;
                dTotalTiempoExtraPagado = 0;
                dTotalIncapacidades = 0;
                dTotalIsr = 0;

                // Perceptions:

                i = 0;
                if (((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones() != null) {
                    for (i = 0; i < ((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().size(); i++) {
                        aPercepciones.add(oFixedFormat.format(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttTipoPercepcion().getInteger()));
                        aPercepciones.add(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString());
                        aPercepciones.add(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttConcepto().getString());
                        aPercepciones.add(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteGravado().getDouble() +
                                ((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteExento().getDouble());
                        aPercepciones.add(null); // pending to be used, navalos (2014-03-13)

                        dTotalPercepciones += ((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteGravado().getDouble() +
                                ((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteExento().getDouble();
                    }
                }

                for (int j = i; j < 10; j++) {
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                }

                // ExtraTimes:

                i = 0;
                if (((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras() != null) {
                    for (i = 0; i < ((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().size(); i++) {

                        aTiempoExtra.add(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(i).getAttTipoHoras().getString().compareTo(SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE) == 0 ?
                            SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE : SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE);
                        aTiempoExtra.add(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(i).getAttDias().getInteger());
                        aTiempoExtra.add(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(i).getAttHorasExtra().getInteger());
                        aTiempoExtra.add(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(i).getAttImportePagado().getDouble());

                        nTotalTiempoExtra += ((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(i).getAttHorasExtra().getInteger();
                        dTotalTiempoExtraPagado += ((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(i).getAttImportePagado().getDouble();
                    }
                }

                for (int j = i; j < 5; j++) {
                    aTiempoExtra.add(null);
                    aTiempoExtra.add(null);
                    aTiempoExtra.add(null);
                    aTiempoExtra.add(null);
                }

                // Deductions:

                i = 0;
                if (((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones() != null) {
                    for (i = 0; i < ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().size(); i++) {

                        aDeducciones.add(oFixedFormat.format(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttTipoDeduccion().getInteger()));
                        aDeducciones.add(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttClave().getString());
                        aDeducciones.add(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttConcepto().getString());
                        aDeducciones.add(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteGravado().getDouble() +
                                ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteExento().getDouble());
                        aDeducciones.add(null); // pending to be used, navalos (2014-03-13)

                        // Obtain isr tax

                        if (((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttClave().getString().compareTo(SCfdConsts.PAYROLL_PER_ISR) == 0 &&
                            ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttTipoDeduccion().getInteger() == SModSysConsts.HRSS_TP_DED_TAX) {
                            dTotalIsr += ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteGravado().getDouble();
                        }

                        dTotalDeducciones += ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteGravado().getDouble() +
                                ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteExento().getDouble();
                    }
                }

                for (int j = i; j < 10; j++) {
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                }

                // Incapacities:

                i = 0;
                if (((cfd.ver3.nom11.DElementNomina) element).getEltIncapacidades() != null) {
                    for (i = 0; i < ((cfd.ver3.nom11.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().size(); i++) {

                        aIncapacidades.add("");
                        aIncapacidades.add(oFixedFormat.format(((cfd.ver3.nom11.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttTipoIncapacidad().getInteger()));
                        aIncapacidades.add(((cfd.ver3.nom11.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttDiasIncapacidad().getDouble());
                        aIncapacidades.add(((cfd.ver3.nom11.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttDescuento().getDouble());

                        dTotalIncapacidades += ((cfd.ver3.nom11.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttDescuento().getDouble();
                    }
                }

                for (int j = i; j < 5; j++) {
                    aIncapacidades.add(null);
                    aIncapacidades.add(null);
                    aIncapacidades.add(null);
                    aIncapacidades.add(null);
                }

                map.put("oPerceptions", aPercepciones);
                map.put("oDeductions", aDeducciones);
                map.put("oExtratimes", aTiempoExtra);
                map.put("oIncapacities", aIncapacidades);
                map.put("TotalPercepcionesGravado", dTotalPercepciones);
                map.put("TotalPercepcionesExento", null);
                map.put("TotalDeduccionesGravado", dTotalDeducciones);
                map.put("TotalDeduccionesExento", null);
                map.put("TotalTiempoExtra", nTotalTiempoExtra);
                map.put("TotalTiempoExtraPagado", dTotalTiempoExtraPagado);
                map.put("TotalIncapacidades", dTotalIncapacidades);
                map.put("dCfdTotalIsr", dTotalIsr);
            }
            else if (element.getName().compareTo("tfd:TimbreFiscalDigital") == 0) {
                cfd.ver32.DElementTimbreFiscalDigital tfd = (cfd.ver32.DElementTimbreFiscalDigital) element;
                map.put("sCfdiVersion", tfd.getAttVersion().getString());
                map.put("sCfdiUuid", tfd.getAttUUID().getString());
                map.put("sCfdiSelloCFD", tfd.getAttSelloCFD().getString());
                map.put("sCfdiSelloSAT", tfd.getAttSelloSAT().getString());
                map.put("sCfdiNoCertificadoSAT", tfd.getAttNoCertificadoSAT().getString());
                map.put("sCfdiFechaTimbre", tfd.getAttFechaTimbrado().getString());
            }
        }
        
        computeReport(cfd, SDataConstantsSys.REP_TRN_CFDI_PAYROLL, map, printMode, numCopies);
    }
    
    /**
     * Prints payroll CFDI 3.2 with complement 1.2.
     * @param cfd
     * @param printMode Constants defined in SDataConstantsPrint.PRINT_MODE_...
     * @param numCopies
     * @param cfdSubtype Constants defined in SCfdConsts.CFDI_PAYROLL_VER_...
     * @throws java.lang.Exception 
     */
    @Deprecated
    public void printPayrollReceipt32_12(final SDataCfd cfd, final int printMode, final int numCopies, final int cfdSubtype) throws java.lang.Exception {
        int i = 0;
        int overTime = 0;
        int nTotalTiempoExtra = 0;

        double dIngresoAux = 0;
        double dIngresoDiario = 0;
        double dTotalPercepciones = 0;
        double dTotalDeducciones = 0;
        double dTotalTiempoExtraPagado = 0;
        double dTotalIncapacidades = 0;
        double dTotalIsr = 0;

        String sCodeDisability = "";
        String sSql = "";

        SDataFormerPayroll oFormerPayroll = null;
        SDataFormerPayrollEmp oFormerPayrollEmployee = null;
        SDbPayroll payroll = null;
        SDbPayrollReceipt payrollReceipt = null;
        SDataCurrency cur = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR, new int[] { miClient.getSessionXXX().getParamsErp().getFkCurrencyId() }, SLibConstants.EXEC_MODE_SILENT);

        cfd.ver32.DElementComprobante comprobante = null;
        HashMap<String, Object> map = null;

        ArrayList aPercepciones = null;
        ArrayList aDeducciones = null;
        ArrayList aTiempoExtra = null;
        ArrayList aIncapacidades = null;

        DecimalFormat oFixedFormat = new DecimalFormat(SLibUtils.textRepeat("0", 3));
        DecimalFormat oFixedFormatAux = new DecimalFormat(SLibUtils.textRepeat("0", 2));

        map = miClient.createReportParams();

        switch (cfdSubtype) {
            case SCfdConsts.CFDI_PAYROLL_VER_OLD:
                oFormerPayroll = new SDataFormerPayroll();
                oFormerPayroll.read(new int[] { cfd.getFkPayrollPayrollId_n() }, miClient.getSession().getStatement());
                
                oFormerPayrollEmployee = new SDataFormerPayrollEmp();
                oFormerPayrollEmployee.read(new int[] { cfd.getFkPayrollPayrollId_n(), cfd.getFkPayrollEmployeeId_n() }, miClient.getSession().getStatement());
                
                sSql = "SELECT id_pay, pay_note AS f_pay_nts FROM hrs_sie_pay WHERE id_pay = " + oFormerPayroll.getPkPayrollId();
                break;
            case SCfdConsts.CFDI_PAYROLL_VER_CUR:
                payroll = new SDbPayroll();
                payroll.read(miClient.getSession(), new int[] { cfd.getFkPayrollReceiptPayrollId_n() });
                
                payrollReceipt = new SDbPayrollReceipt();
                payrollReceipt.read(miClient.getSession(), new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n() });
                
                sSql = "SELECT id_pay, nts AS f_pay_nts FROM hrs_pay WHERE id_pay = " + payroll.getPkPayrollId();
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        SCfdXmlCatalogs catalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        comprobante = DCfdUtils.getCfdi32(cfd.getDocXml());
        
        map.put("sSql", sSql);
        map.put("sCfdFecha", SLibUtils.DbmsDateFormatDatetime.format(comprobante.getAttFecha().getDatetime()));
        map.put("sCfdFormaDePago", comprobante.getAttFormaDePago().getOption());
        map.put("sCfdNoCuentaPago", comprobante.getAttNumCtaPago().getString());
        map.put("sCfdCondicionesDePagoOpc", comprobante.getAttCondicionesDePago().getOption());
        map.put("dCfdSubtotal", comprobante.getAttSubTotal().getDouble());
        map.put("dCfdDescuento", comprobante.getAttDescuento().getDouble());
        map.put("dCfdTotal", comprobante.getAttTotal().getDouble());
        map.put("sCfdMetodoDePagoOpc", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY, comprobante.getAttMetodoDePago().getString()));
        map.put("sExpedidoEn", comprobante.getAttLugarExpedicion().getString());
        map.put("sCfdTipoComprobante", comprobante.getAttTipoDeComprobante().getOption());
        map.put("sCfdNoCuentaPago", comprobante.getAttNumCtaPago().getString());
        map.put("sCfdNoCertificado", comprobante.getAttNoCertificado().getString());
        map.put("sEmiRegimenFiscal", comprobante.getEltEmisor().getEltHijosRegimenFiscal().get(0).getAttRegimen().getString());
        map.put("sEmiRfc", comprobante.getEltEmisor().getAttRfc().getString());
        map.put("sRecRfc", comprobante.getEltReceptor().getAttRfc().getString());
        map.put("dCfdTotal", comprobante.getAttTotal().getDouble());
        map.put("sDocTotalConLetra", SLibUtilities.translateValueToText(comprobante.getAttTotal().getDouble(), miClient.getSessionXXX().getParamsErp().getDecimalsValue(), miClient.getSessionXXX().getParamsErp().getFkLanguageId(),
                cur.getTextSingular(), cur.getTextPlural(), cur.getTextPrefix(), cur.getTextSuffix()));
        map.put("sCfdMoneda", comprobante.getAttMoneda().getString());
        map.put("dCfdTipoCambio", comprobante.getAttTipoCambio().getDouble());
        map.put("ReceptorNombre", comprobante.getEltReceptor().getAttNombre().getString());
        map.put("bIsAnnulled", cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED);
        map.put("nPkPayrollId", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayroll.getPkPayrollId() : payroll.getPkPayrollId());
        map.put("NominaNumTipo", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? (oFormerPayroll.getNumber() + " " + oFormerPayroll.getType()) : (payroll.getNumber() + " " + payroll.getAuxPaymentType()));
        map.put("NominaFolio", comprobante.getAttSerie().getString() + "-" + comprobante.getAttFolio().getString());
        map.put("sXmlBaseDir", miClient.getSessionXXX().getParamsCompany().getXmlBaseDirectory());
        map.put("sCfdVersion", comprobante.getAttVersion().getString());

        map.put("dCfdConceptoCantidad", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttCantidad().getDouble());
        map.put("sCfdConceptoUnidad", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttUnidad().getString());
        map.put("sCfdConceptoNoIdentifiacion", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttNoIdentificacion().getString());
        map.put("sCfdConceptoDescripcion", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttDescripcion().getString());
        map.put("dCfdConceptoValorUnitario", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttValorUnitario().getDouble());
        map.put("dCfdConceptoImporte", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttImporte().getDouble());
        
        for (DElement element : comprobante.getEltOpcComplemento().getElements()) {

            if (element.getName().compareTo("nomina12:Nomina") == 0) {

                map.put("TipoNomina", ((cfd.ver3.nom12.DElementNomina) element).getAttTipoNomina().getString());
                map.put("FechaPago", SLibUtils.DateFormatDate.format(((cfd.ver3.nom12.DElementNomina) element).getAttFechaPago().getDate()));
                map.put("FechaInicialPago", SLibUtils.DateFormatDate.format(((cfd.ver3.nom12.DElementNomina) element).getAttFechaInicialPago().getDate()));
                map.put("FechaFinalPago", SLibUtils.DateFormatDate.format(((cfd.ver3.nom12.DElementNomina) element).getAttFechaFinalPago().getDate()));
                map.put("NumDiasPagados", ((cfd.ver3.nom12.DElementNomina) element).getAttNumDiasPagados().getDouble());
                map.put("NumDiasNoLaborados", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getDaysNotWorked() : payrollReceipt.getDaysNotWorked_r());
                map.put("NumDiasLaborados", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getDaysWorked() : payrollReceipt.getDaysWorked()); // XXX Optional
                map.put("NumDiasPagar", 0d); // Calculate?, navalos (2014-03-13)
                
                // Emisor:

                i = 0;
                if (((cfd.ver3.nom12.DElementNomina) element).getEltEmisor()!= null) {
                    map.put("RegistroPatronal", ((cfd.ver3.nom12.DElementNomina) element).getEltEmisor().getAttRegistroPatronal().getString());
                }

                // Receptor:

                String antigüedad = "";
                
                i = 0;
                if (((cfd.ver3.nom12.DElementNomina) element).getEltReceptor() != null) {
                    map.put("CURP", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttCurp().getString());
                    map.put("NumSeguridadSocial", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttNumSeguridadSocial().getString());
                    
                    if (((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttFechaInicioRelLaboral().getDate() != null) {
                        map.put("FechaInicioRelLaboral", SLibUtils.DateFormatDate.format(((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttFechaInicioRelLaboral().getDate()));
                    }
                    
                    if (!((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttAntiguedad().getString().isEmpty()) {
                        antigüedad = ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttAntiguedad().getString();
                        antigüedad = antigüedad.substring(1, antigüedad.length() - 1); // regex: /P[0-9]+W/
                    }
                    
                    map.put("Antiguedad", SLibUtils.parseInt(antigüedad)); // seniority in absolute months from most recent hiring date
                    map.put("TipoContrato", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttTipoContrato().getString());
                    // Sindicalizado 
                    map.put("TipoJornada", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttTipoJornada().getString());
                    //map.put("TipoRegimen", SCfdConsts.RegimenMap.get(SLibUtils.parseInt(((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttTipoRegimen().getString())));
                    map.put("TipoRegimen", miClient.getSession().readField(SModConsts.HRSS_TP_REC_SCHE, new int[] { SLibUtils.parseInt(((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttTipoRegimen().getString()) }, SDbRegistry.FIELD_NAME));
                    map.put("NumEmpleado", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttNumEmpleado().getString());
                    map.put("Departamento", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttDepartamento().getString());
                    map.put("Puesto", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttPuesto().getString());
                    //map.put("RiesgoPuesto", SCfdConsts.RiesgoMap.get(SLibUtils.parseInt(((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttRiesgoPuesto().getString())));
                    if (!((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttRiesgoPuesto().getString().isEmpty()) {
                        map.put("RiesgoPuesto", miClient.getSession().readField(SModConsts.HRSS_TP_POS_RISK, new int[] { SLibUtils.parseInt(((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttRiesgoPuesto().getString()) }, SDbRegistry.FIELD_NAME));
                    }
                    map.put("PeriodicidadPago", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttPeriodicidadPago().getString());
                    //map.put("Banco", SCfdConsts.BancoMap.get(SLibUtils.parseInt(((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttBanco().getString())));
                    if (!((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttBanco().getString().isEmpty()) {
                        map.put("Banco", miClient.getSession().readField(SModConsts.HRSS_BANK, new int[] { SLibUtils.parseInt(((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttBanco().getString()) }, SDbRegistry.FIELD_NAME));
                    }
                    map.put("CLABE", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttCuentaBancaria().getString());
                    map.put("SalarioBaseCotApor", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttSalarioBaseCotApor().getDouble());
                    map.put("SalarioDiarioIntegrado", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttSalarioDiarioIntegrado().getDouble());
                    map.put("ClaveEstado", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttClaveEntFed().getString());
                    map.put("TipoPago", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? SModSysConsts.HRSS_TP_PAY_FOR : payrollReceipt.getFkPaymentTypeId());
                    map.put("Sueldo", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getSalary() : payrollReceipt.getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_WEE ? payrollReceipt.getSalary() : payrollReceipt.getWage());
                    map.put("TipoEmpleado", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getEmployeeType() : miClient.getSession().readField(SModConsts.HRSU_TP_EMP, new int[] { payrollReceipt.getFkEmployeeTypeId() }, SDbRegistry.FIELD_CODE));
                    map.put("Categoria", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getEmployeeCategory() : miClient.getSession().readField(SModConsts.HRSU_TP_WRK, new int[] { payrollReceipt.getFkWorkerTypeId() }, SDbRegistry.FIELD_CODE));
                    map.put("TipoSalario", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getSalaryType() : miClient.getSession().readField(SModConsts.HRSS_TP_SAL, new int[] { payrollReceipt.getFkSalaryTypeId() }, SDbRegistry.FIELD_NAME));
                    map.put("Ejercicio", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? (oFormerPayroll.getYear() + "-" + oFixedFormatAux.format(oFormerPayroll.getPeriod())) : (payroll.getPeriodYear() + "-" + oFixedFormatAux.format(payroll.getPeriod())));
                    
                    if (payrollReceipt.getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_FOR) {
                        dIngresoAux = cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getSalary() : payrollReceipt.getWage();
                        dIngresoDiario = dIngresoAux * SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS;
                        map.put("IngresoDiario", SLibUtils.round(dIngresoDiario, SLibConsts.DATA_TYPE_DEC));
                    }
                }

                aPercepciones = new ArrayList();
                aDeducciones = new ArrayList();
                aTiempoExtra = new ArrayList();
                aIncapacidades = new ArrayList();

                dTotalPercepciones = 0;
                dTotalDeducciones = 0;
                nTotalTiempoExtra = 0;
                dTotalTiempoExtraPagado = 0;
                dTotalIncapacidades = 0;
                dTotalIsr = 0;

                // Perceptions:

                i = 0;
                if (((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones() != null) {
                    for (i = 0; i < ((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().size(); i++) {
                        aPercepciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttTipoPercepcion().getString());
                        aPercepciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString());
                        aPercepciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttConcepto().getString());
                        aPercepciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteGravado().getDouble() +
                                ((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteExento().getDouble());
                        aPercepciones.add(null); // pending to be used, navalos (2014-03-13)

                        dTotalPercepciones += ((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteGravado().getDouble() +
                                ((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteExento().getDouble();
                    
                        // ExtraTimes:

                        overTime = 0;
                        for (; overTime < ((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getEltHijosHorasExtra().size(); overTime++) {
                            aTiempoExtra.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getEltHijosHorasExtra().get(overTime).getAttTipoHoras().getString().compareTo(SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE_CODE) == 0 ?
                            SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE : SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE);
                            aTiempoExtra.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getEltHijosHorasExtra().get(overTime).getAttDias().getInteger());
                            aTiempoExtra.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getEltHijosHorasExtra().get(overTime).getAttHorasExtra().getInteger());
                            aTiempoExtra.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getEltHijosHorasExtra().get(overTime).getAttImportePagado().getDouble());

                            nTotalTiempoExtra += ((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getEltHijosHorasExtra().get(overTime).getAttHorasExtra().getInteger();
                            dTotalTiempoExtraPagado += ((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getEltHijosHorasExtra().get(overTime).getAttImportePagado().getDouble();
                        }
                    }
                }

                // Other payment:
                
                if (((cfd.ver3.nom12.DElementNomina) element).getEltOtrosPagos() != null) {
                    for (i = 0; i < ((cfd.ver3.nom12.DElementNomina) element).getEltOtrosPagos().getEltHijosOtroPago().size(); i++) {
                        aPercepciones.add(""); // is blank because it is not earning
                        aPercepciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltOtrosPagos().getEltHijosOtroPago().get(i).getAttClave().getString());
                        aPercepciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltOtrosPagos().getEltHijosOtroPago().get(i).getAttConcepto().getString());
                        aPercepciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltOtrosPagos().getEltHijosOtroPago().get(i).getAttImporte().getDouble());
                        aPercepciones.add(null); // pending to be used, navalos (2014-03-13)
                    }
                }
                
                for (int j = i; j < 10; j++) {
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                }

                for (int j = overTime; j < 5; j++) {
                    aTiempoExtra.add(null);
                    aTiempoExtra.add(null);
                    aTiempoExtra.add(null);
                    aTiempoExtra.add(null);
                }

                // Deductions:

                i = 0;
                if (((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones() != null) {
                    for (i = 0; i < ((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().size(); i++) {

                        aDeducciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttTipoDeduccion().getString());
                        aDeducciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttClave().getString());
                        aDeducciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttConcepto().getString());
                        aDeducciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporte().getDouble());
                        aDeducciones.add(null); // pending to be used, navalos (2014-03-13)

                        // Obtain isr tax

                        if (((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttClave().getString().compareTo(SCfdConsts.PAYROLL_PER_ISR) == 0 &&
                            SLibUtils.parseInt(((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttTipoDeduccion().getString()) == SModSysConsts.HRSS_TP_DED_TAX) {
                            dTotalIsr += ((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporte().getDouble();
                        }

                        dTotalDeducciones += ((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporte().getDouble();
                    }
                }

                for (int j = i; j < 10; j++) {
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                }

                // Incapacities:

                i = 0;
                if (((cfd.ver3.nom12.DElementNomina) element).getEltIncapacidades() != null) {
                    for (i = 0; i < ((cfd.ver3.nom12.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().size(); i++) {
                        sCodeDisability = ((cfd.ver3.nom12.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttTipoIncapacidad().getString();

                        aIncapacidades.add(sCodeDisability);
                        aIncapacidades.add(SHrsUtils.getDisabilityName((SGuiClient) miClient, sCodeDisability));
                        aIncapacidades.add(((cfd.ver3.nom12.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttDiasIncapacidad().getInteger());
                        aIncapacidades.add(((cfd.ver3.nom12.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttImporteMonetario().getDouble());

                        dTotalIncapacidades += ((cfd.ver3.nom12.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttImporteMonetario().getDouble();
                    }
                }

                for (int j = i; j < 5; j++) {
                    aIncapacidades.add(null);
                    aIncapacidades.add(null);
                    aIncapacidades.add(null);
                    aIncapacidades.add(null);
                }

                map.put("oPerceptions", aPercepciones);
                map.put("oDeductions", aDeducciones);
                map.put("oExtratimes", aTiempoExtra);
                map.put("oIncapacities", aIncapacidades);
                map.put("TotalPercepcionesGravado", dTotalPercepciones);
                map.put("TotalPercepcionesExento", null);
                map.put("TotalDeduccionesGravado", dTotalDeducciones);
                map.put("TotalDeduccionesExento", null);
                map.put("TotalTiempoExtra", nTotalTiempoExtra);
                map.put("TotalTiempoExtraPagado", dTotalTiempoExtraPagado);
                map.put("TotalIncapacidades", dTotalIncapacidades);
                map.put("dCfdTotalIsr", dTotalIsr);
            }
            else if (element.getName().compareTo("tfd:TimbreFiscalDigital") == 0) {
                cfd.ver32.DElementTimbreFiscalDigital tfd = (cfd.ver32.DElementTimbreFiscalDigital) element;
                map.put("sCfdiVersion", tfd.getAttVersion().getString());
                map.put("sCfdiUuid", tfd.getAttUUID().getString());
                map.put("sCfdiSelloCFD", tfd.getAttSelloCFD().getString());
                map.put("sCfdiSelloSAT", tfd.getAttSelloSAT().getString());
                map.put("sCfdiNoCertificadoSAT", tfd.getAttNoCertificadoSAT().getString());
                map.put("sCfdiFechaTimbre", tfd.getAttFechaTimbrado().getString());
            }
        }
        
        computeReport(cfd, SDataConstantsSys.REP_TRN_CFDI_PAYROLL, map, printMode, numCopies);
    }
    
    /**
     * Prints payroll CFDI 3.3 with complement 1.2.
     * @param cfd
     * @param printMode Constants defined in SDataConstantsPrint.PRINT_MODE_...
     * @param numCopies
     * @param cfdSubtype Constants defined in SCfdConsts.CFDI_PAYROLL_VER_...
     * @throws java.lang.Exception 
     */
    public void printPayrollReceipt33_12(final SDataCfd cfd, final int printMode, final int numCopies, final int cfdSubtype) throws java.lang.Exception {
        String sql = "";
        SDataFormerPayroll formerPayroll = null;
        SDataFormerPayrollEmp formerPayrollEmp = null;
        SDbPayroll payroll = null;
        SDbPayrollReceipt payrollReceipt = null;
        HashMap<String, Object> paramsMap = miClient.createReportParams();

        switch (cfdSubtype) {
            case SCfdConsts.CFDI_PAYROLL_VER_OLD:
                formerPayroll = new SDataFormerPayroll();
                formerPayroll.read(new int[] { cfd.getFkPayrollPayrollId_n() }, miClient.getSession().getStatement());
                
                formerPayrollEmp = new SDataFormerPayrollEmp();
                formerPayrollEmp.read(new int[] { cfd.getFkPayrollPayrollId_n(), cfd.getFkPayrollEmployeeId_n() }, miClient.getSession().getStatement());
                
                sql = "SELECT id_pay, pay_note AS f_pay_nts FROM hrs_sie_pay WHERE id_pay = " + formerPayroll.getPkPayrollId();
                break;
                
            case SCfdConsts.CFDI_PAYROLL_VER_CUR:
                payroll = SCfdUtils.retrieveDataSetPayroll(miClient.getSession(), cfd.getFkPayrollReceiptPayrollId_n()); // streamline payroll retrieval
                
                // define the best way to retrieve the payroll receipt:
                
                Integer keyCfd = (Integer) SCfdUtils.DataSet.get(SCfdUtils.KEY_CFD);
                
                if (keyCfd != null && keyCfd == SCfdUtils.KEY_CFD_PROCESSING) {
                    // it is safe to take receipt from payroll object in memory, CFD is not been stamped:
                    payrollReceipt = payroll.getChildPayrollReceipt(new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n() });
                }
                else {
                    // it is not safe to take receipt from memory, CFD has just been stamped:
                    payrollReceipt = (SDbPayrollReceipt) miClient.getSession().readRegistry(SModConsts.HRS_PAY_RCP, new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n() });
                }
                
                sql = "SELECT id_pay, nts AS f_pay_nts FROM hrs_pay WHERE id_pay = " + payroll.getPkPayrollId();
                break;
                
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        SCfdXmlCatalogs catalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        cfd.ver33.DElementComprobante comprobante = DCfdUtils.getCfdi33(cfd.getDocXml());
        cfd.ver33.DElementEmisor emisor = comprobante.getEltEmisor(); // convenience variable
        cfd.ver33.DElementReceptor receptor = comprobante.getEltReceptor(); // convenience variable
        
        paramsMap.put("sSql", sql);
        paramsMap.put("sCfdFecha", SLibUtils.DbmsDateFormatDatetime.format(comprobante.getAttFecha().getDatetime()));
        paramsMap.put("sCfdFormaDePago", comprobante.getAttMetodoPago().getString());
        paramsMap.put("sCfdNoCuentaPago", "");
        paramsMap.put("sCfdCondicionesDePagoOpc", comprobante.getAttCondicionesDePago().getString());
        paramsMap.put("dCfdSubtotal", comprobante.getAttSubTotal().getDouble());
        paramsMap.put("dCfdDescuento", comprobante.getAttDescuento().getDouble());
        paramsMap.put("dCfdTotal", comprobante.getAttTotal().getDouble());
        paramsMap.put("sCfdMetodoDePagoOpc", comprobante.getAttFormaPago().getString());
        paramsMap.put("sExpedidoEn", comprobante.getAttLugarExpedicion().getString());
        paramsMap.put("sCfdTipoComprobante", comprobante.getAttTipoDeComprobante().getString());
        paramsMap.put("sCfdNoCertificado", comprobante.getAttNoCertificado().getString());
        paramsMap.put("sEmiRegimenFiscal", emisor.getAttRegimenFiscal().getString());
        paramsMap.put("sEmiRfc", emisor.getAttRfc().getString());
        paramsMap.put("sRecRfc", receptor.getAttRfc().getString());
        paramsMap.put("dCfdTotal", comprobante.getAttTotal().getDouble());
        
        SDataCurrency cur = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR, new int[] { miClient.getSessionXXX().getParamsErp().getFkCurrencyId() }, SLibConstants.EXEC_MODE_SILENT);
        paramsMap.put("sDocTotalConLetra", SLibUtilities.translateValueToText(
                comprobante.getAttTotal().getDouble(), 
                SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits(), 
                miClient.getSessionXXX().getParamsErp().getFkLanguageId(),
                cur.getTextSingular(), 
                cur.getTextPlural(), 
                cur.getTextPrefix(), 
                cur.getTextSuffix()));
        
        paramsMap.put("sCfdMoneda", comprobante.getAttMoneda().getString());
        paramsMap.put("dCfdTipoCambio", comprobante.getAttTipoCambio().getDouble());
        paramsMap.put("ReceptorNombre", receptor.getAttNombre().getString());
        paramsMap.put("bIsAnnulled", cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED);
        paramsMap.put("nPkPayrollId", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? formerPayroll.getPkPayrollId() : payroll.getPkPayrollId());
        paramsMap.put("NominaNumTipo", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? (formerPayroll.getNumber() + " " + formerPayroll.getType()) : (payroll.getNumber() + " " + payroll.getAuxPaymentType()));
        paramsMap.put("NominaFolio", comprobante.getAttSerie().getString() + "-" + comprobante.getAttFolio().getString());
        paramsMap.put("sXmlBaseDir", miClient.getSessionXXX().getParamsCompany().getXmlBaseDirectory());
        paramsMap.put("sCfdVersion", "" + comprobante.getVersion());

        cfd.ver33.DElementConcepto concepto = comprobante.getEltConceptos().getEltConceptos().get(0); // convenience variable
        
        paramsMap.put("dCfdConceptoCantidad", concepto.getAttCantidad().getDouble());
        paramsMap.put("sCfdConceptoUnidad", concepto.getAttUnidad().getString());
        paramsMap.put("sCfdConceptoNoIdentifiacion", concepto.getAttNoIdentificacion().getString());
        paramsMap.put("sCfdConceptoDescripcion", concepto.getAttDescripcion().getString());
        paramsMap.put("dCfdConceptoValorUnitario", concepto.getAttValorUnitario().getDouble());
        paramsMap.put("dCfdConceptoImporte", concepto.getAttImporte().getDouble());

        // adds related CFDI's:
        if (comprobante.getEltOpcCfdiRelacionados() != null) {
            if (!comprobante.getEltOpcCfdiRelacionados().getEltCfdiRelacionados().isEmpty()) {
                // if any, there must be only one related CFDI:
                paramsMap.put("sCfdiRelacionadoTipoRelacion", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_REL_TP, comprobante.getEltOpcCfdiRelacionados().getAttTipoRelacion().getString()));
                paramsMap.put("sCfdiRelacionadoUuid", comprobante.getEltOpcCfdiRelacionados().getEltCfdiRelacionados().get(0).getAttUuid().getString().toUpperCase());
            }
        }

        String sello = "";
        
        for (DElement element : comprobante.getEltOpcComplemento().getElements()) {
            switch (element.getName()) {
                case "nomina12:Nomina":
                    cfd.ver3.nom12.DElementNomina nomina = (cfd.ver3.nom12.DElementNomina) element; // convenience variable

                    paramsMap.put("TipoNomina", nomina.getAttTipoNomina().getString());
                    paramsMap.put("FechaPago", SLibUtils.DateFormatDate.format(nomina.getAttFechaPago().getDate()));
                    paramsMap.put("FechaInicialPago", SLibUtils.DateFormatDate.format(nomina.getAttFechaInicialPago().getDate()));
                    paramsMap.put("FechaFinalPago", SLibUtils.DateFormatDate.format(nomina.getAttFechaFinalPago().getDate()));
                    paramsMap.put("NumDiasPagados", nomina.getAttNumDiasPagados().getDouble());
                    paramsMap.put("NumDiasNoLaborados", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? formerPayrollEmp.getDaysNotWorked() : payrollReceipt.getDaysNotWorked_r());
                    paramsMap.put("NumDiasLaborados", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? formerPayrollEmp.getDaysWorked() : payrollReceipt.getDaysWorked()); // XXX Optional
                    paramsMap.put("NumDiasPagar", 0d); // Calculate?, navalos (2014-03-13)

                    // Emisor:

                    cfd.ver3.nom12.DElementEmisor nomEmisor = nomina.getEltEmisor(); // convenience variable

                    if (nomEmisor != null) {
                        paramsMap.put("RegistroPatronal", nomEmisor.getAttRegistroPatronal().getString());
                    }

                    // Receptor:

                    cfd.ver3.nom12.DElementReceptor nomReceptor = nomina.getEltReceptor(); // convenience variable

                    if (nomReceptor != null) {
                        paramsMap.put("CURP", nomReceptor.getAttCurp().getString());
                        paramsMap.put("NumSeguridadSocial", nomReceptor.getAttNumSeguridadSocial().getString());

                        if (nomReceptor.getAttFechaInicioRelLaboral().getDate() != null) {
                            paramsMap.put("FechaInicioRelLaboral", SLibUtils.DateFormatDate.format(nomReceptor.getAttFechaInicioRelLaboral().getDate()));
                        }

                        String antigüedad = "";
                        if (!nomReceptor.getAttAntiguedad().getString().isEmpty()) {
                            antigüedad = nomReceptor.getAttAntiguedad().getString();
                            antigüedad = antigüedad.substring(1, antigüedad.length() - 1); // regex: /P[0-9]+W/
                        }

                        paramsMap.put("Antiguedad", SLibUtils.parseInt(antigüedad)); // seniority in absolute months from most recent hiring date
                        paramsMap.put("TipoContrato", nomReceptor.getAttTipoContrato().getString());
                        paramsMap.put("TipoJornada", nomReceptor.getAttTipoJornada().getString());
                        paramsMap.put("TipoRegimen", miClient.getSession().readField(SModConsts.HRSS_TP_REC_SCHE, new int[] { SLibUtils.parseInt(nomReceptor.getAttTipoRegimen().getString()) }, SDbRegistry.FIELD_NAME));
                        paramsMap.put("NumEmpleado", nomReceptor.getAttNumEmpleado().getString());
                        paramsMap.put("Departamento", nomReceptor.getAttDepartamento().getString());
                        paramsMap.put("Puesto", nomReceptor.getAttPuesto().getString());
                        if (!nomReceptor.getAttRiesgoPuesto().getString().isEmpty()) {
                            paramsMap.put("RiesgoPuesto", miClient.getSession().readField(SModConsts.HRSS_TP_POS_RISK, new int[] { SLibUtils.parseInt(nomReceptor.getAttRiesgoPuesto().getString()) }, SDbRegistry.FIELD_NAME));
                        }
                        paramsMap.put("PeriodicidadPago", nomReceptor.getAttPeriodicidadPago().getString());
                        if (!nomReceptor.getAttBanco().getString().isEmpty()) {
                            paramsMap.put("Banco", miClient.getSession().readField(SModConsts.HRSS_BANK, new int[] { SLibUtils.parseInt(nomReceptor.getAttBanco().getString()) }, SDbRegistry.FIELD_NAME));
                        }
                        paramsMap.put("CLABE", nomReceptor.getAttCuentaBancaria().getString());
                        paramsMap.put("SalarioBaseCotApor", nomReceptor.getAttSalarioBaseCotApor().getDouble());
                        paramsMap.put("SalarioDiarioIntegrado", nomReceptor.getAttSalarioDiarioIntegrado().getDouble());
                        paramsMap.put("ClaveEstado", nomReceptor.getAttClaveEntFed().getString());
                        paramsMap.put("TipoPago", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? SModSysConsts.HRSS_TP_PAY_FOR : payrollReceipt.getFkPaymentTypeId());
                        paramsMap.put("TipoEmpleado", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? formerPayrollEmp.getEmployeeType() : miClient.getSession().readField(SModConsts.HRSU_TP_EMP, new int[] { payrollReceipt.getFkEmployeeTypeId() }, SDbRegistry.FIELD_CODE));
                        paramsMap.put("Categoria", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? formerPayrollEmp.getEmployeeCategory() : miClient.getSession().readField(SModConsts.HRSU_TP_WRK, new int[] { payrollReceipt.getFkWorkerTypeId() }, SDbRegistry.FIELD_CODE));
                        paramsMap.put("TipoSalario", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? formerPayrollEmp.getSalaryType() : miClient.getSession().readField(SModConsts.HRSS_TP_SAL, new int[] { payrollReceipt.getFkSalaryTypeId() }, SDbRegistry.FIELD_NAME));
                        paramsMap.put("Ejercicio", cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? (SLibUtils.DecimalFormatCalendarYear.format(formerPayroll.getYear()) + "-" + SLibUtils.DecimalFormatCalendarMonth.format(formerPayroll.getPeriod())) : (SLibUtils.DecimalFormatCalendarYear.format(payroll.getPeriodYear()) + "-" + SLibUtils.DecimalFormatCalendarMonth.format(payroll.getPeriod())));

                        double sueldo;
                        double salario;

                        switch (payrollReceipt.getFkPaymentTypeId()) {
                            case SModSysConsts.HRSS_TP_PAY_WEE:
                                sueldo = 0;
                                salario = cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? formerPayrollEmp.getSalary() : payrollReceipt.getSalary();
                                paramsMap.put("Sueldo", sueldo);
                                paramsMap.put("Salario", salario);
                                paramsMap.put("IngresoDiario", salario);
                                break;

                            case SModSysConsts.HRSS_TP_PAY_FOR:
                                SDbConfig config = (SDbConfig) miClient.getSession().readRegistry(SModConsts.HRS_CFG, new int[] { SUtilConsts.BPR_CO_ID });

                                sueldo = cfdSubtype == SCfdConsts.CFDI_PAYROLL_VER_OLD ? formerPayrollEmp.getSalary() : payrollReceipt.getWage();
                                if (config.isFortnightStandard()) {
                                    salario = SLibUtils.roundAmount(sueldo / (SHrsConsts.YEAR_DAYS_FORTNIGHTS_FIXED / SHrsConsts.YEAR_MONTHS));
                                }
                                else {
                                    salario = SLibUtils.roundAmount(sueldo * SHrsConsts.YEAR_MONTHS / SHrsConsts.YEAR_DAYS);
                                }
                                paramsMap.put("Sueldo", sueldo);
                                paramsMap.put("Salario", salario);
                                paramsMap.put("IngresoDiario", salario);
                                break;

                            default:
                        }
                    }

                    ArrayList<Object> perceptions = new ArrayList<>();  // WTF!, an array of objects, damn!
                    ArrayList<Object> deductions = new ArrayList<>();   // WTF!, an array of objects, damn!
                    ArrayList<Object> overtimes = new ArrayList<>();    // WTF!, an array of objects, damn!
                    ArrayList<Object> absences = new ArrayList<>();     // WTF!, an array of objects, damn!

                    double totalPerceptions = 0;
                    double totalDeductions = 0;
                    int totalOvertimeHrs = 0;
                    double totalOvertimePay = 0;
                    double dTotalIncapacidades = 0;
                    double dTotalIsr = 0;

                    // Perceptions:

                    int idxPerception = 0;
                    int idxOvertime = 0;

                    if (nomina.getEltPercepciones() != null) {
                        // Perception entries:

                        Vector<cfd.ver3.nom12.DElementPercepcion> hijosPercepcion = nomina.getEltPercepciones().getEltHijosPercepcion();

                        // report has rows of 5 fields:
                        for (idxPerception = 0; idxPerception < hijosPercepcion.size(); idxPerception++) {
                            perceptions.add(hijosPercepcion.get(idxPerception).getAttTipoPercepcion().getString());
                            perceptions.add(hijosPercepcion.get(idxPerception).getAttClave().getString());
                            perceptions.add(hijosPercepcion.get(idxPerception).getAttConcepto().getString());
                            perceptions.add(SLibUtils.roundAmount(hijosPercepcion.get(idxPerception).getAttImporteGravado().getDouble() + hijosPercepcion.get(idxPerception).getAttImporteExento().getDouble()));
                            perceptions.add(null); // pending to be used, navalos (2014-03-13)

                            totalPerceptions += SLibUtils.roundAmount(hijosPercepcion.get(idxPerception).getAttImporteGravado().getDouble() + hijosPercepcion.get(idxPerception).getAttImporteExento().getDouble());

                            // Overtime entries:

                            // report has rows of 4 fields:
                            for (idxOvertime = 0; idxOvertime < hijosPercepcion.get(idxPerception).getEltHijosHorasExtra().size(); idxOvertime++) {
                                overtimes.add(hijosPercepcion.get(idxPerception).getEltHijosHorasExtra().get(idxOvertime).getAttTipoHoras().getString().compareTo(SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE_CODE) == 0 ?
                                SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE : SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE);
                                overtimes.add(hijosPercepcion.get(idxPerception).getEltHijosHorasExtra().get(idxOvertime).getAttDias().getInteger());
                                overtimes.add(hijosPercepcion.get(idxPerception).getEltHijosHorasExtra().get(idxOvertime).getAttHorasExtra().getInteger());
                                overtimes.add(hijosPercepcion.get(idxPerception).getEltHijosHorasExtra().get(idxOvertime).getAttImportePagado().getDouble());

                                totalOvertimeHrs += hijosPercepcion.get(idxPerception).getEltHijosHorasExtra().get(idxOvertime).getAttHorasExtra().getInteger();
                                totalOvertimePay += hijosPercepcion.get(idxPerception).getEltHijosHorasExtra().get(idxOvertime).getAttImportePagado().getDouble();
                            }
                        }
                    }

                    // Other payments:

                    cfd.ver3.nom12.DElementOtrosPagos otrosPagos = nomina.getEltOtrosPagos();

                    if (otrosPagos != null) {
                        // report has rows of 5 fields:
                        for (idxPerception = 0; idxPerception < otrosPagos.getEltHijosOtroPago().size(); idxPerception++) {
                            String extra = "";
                            DElementOtroPago otroPago = otrosPagos.getEltHijosOtroPago().get(idxPerception);
                            
                            if (otroPago.getEltSubsidioEmpleo() != null) {
                                extra = otroPago.getEltSubsidioEmpleo().toString();
                            }
                            else if (otroPago.getEltCompensacionSaldosFavor() != null) {
                                extra = otroPago.getEltCompensacionSaldosFavor().toString();
                            }

                            perceptions.add(""); // is blank because it is not earning
                            perceptions.add(otroPago.getAttClave().getString());
                            perceptions.add(otroPago.getAttConcepto().getString() + (extra.isEmpty() ? "" : "/ " + extra));
                            perceptions.add(otroPago.getAttImporte().getDouble());
                            perceptions.add(null); // pending to be used, navalos (2014-03-13)
                        }
                    }

                    // complete with 5 null values (each iteration corresponds to one row in report):
                    for (int i = idxPerception; i < CFDI33_NOM12_MAX_LINES_DETAIL; i++) {
                        perceptions.add(null);
                        perceptions.add(null);
                        perceptions.add(null);
                        perceptions.add(null);
                        perceptions.add(null);
                    }

                    // complete with 4 null values (each iteration corresponds to one row in report):
                    for (int i = idxOvertime; i < CFDI33_NOM12_MAX_LINES_EXTRA; i++) {
                        overtimes.add(null);
                        overtimes.add(null);
                        overtimes.add(null);
                        overtimes.add(null);
                    }

                    // Deductions:

                    int idxDeduction = 0;

                    if (nomina.getEltDeducciones() != null) {
                        // Deduction entries:

                        Vector<cfd.ver3.nom12.DElementDeduccion> hijosDeduccion = nomina.getEltDeducciones().getEltHijosDeduccion();

                        // report has rows of 5 fields:
                        for (idxDeduction = 0; idxDeduction < hijosDeduccion.size(); idxDeduction++) {
                            deductions.add(hijosDeduccion.get(idxDeduction).getAttTipoDeduccion().getString());
                            deductions.add(hijosDeduccion.get(idxDeduction).getAttClave().getString());
                            deductions.add(hijosDeduccion.get(idxDeduction).getAttConcepto().getString());
                            deductions.add(hijosDeduccion.get(idxDeduction).getAttImporte().getDouble());
                            deductions.add(null); // pending to be used, navalos (2014-03-13)

                            // Income tax:

                            if (hijosDeduccion.get(idxDeduction).getAttClave().getString().compareTo(SCfdConsts.PAYROLL_PER_ISR) == 0 &&
                                SLibUtils.parseInt(hijosDeduccion.get(idxDeduction).getAttTipoDeduccion().getString()) == SModSysConsts.HRSS_TP_DED_TAX) {
                                dTotalIsr += hijosDeduccion.get(idxDeduction).getAttImporte().getDouble();
                            }

                            totalDeductions += hijosDeduccion.get(idxDeduction).getAttImporte().getDouble();
                        }
                    }

                    // complete with 5 null values (each iteration corresponds to one row in report):
                    for (int i = idxDeduction; i < CFDI33_NOM12_MAX_LINES_DETAIL; i++) {
                        deductions.add(null);
                        deductions.add(null);
                        deductions.add(null);
                        deductions.add(null);
                        deductions.add(null);
                    }

                    // Absences:

                    int idxAbsence = 0;

                    if (nomina.getEltIncapacidades() != null) {
                        // Absence entries:

                        Vector<cfd.ver3.nom12.DElementIncapacidad> hijosIncapacidad = nomina.getEltIncapacidades().getEltHijosIncapacidad();

                        // report has rows of 4 fields:
                        for (idxAbsence = 0; idxAbsence < hijosIncapacidad.size(); idxAbsence++) {
                            String code = hijosIncapacidad.get(idxAbsence).getAttTipoIncapacidad().getString();
                            absences.add(code);
                            absences.add(SHrsUtils.getDisabilityName((SGuiClient) miClient, code));
                            absences.add(hijosIncapacidad.get(idxAbsence).getAttDiasIncapacidad().getInteger());
                            absences.add(hijosIncapacidad.get(idxAbsence).getAttImporteMonetario().getDouble());

                            dTotalIncapacidades += hijosIncapacidad.get(idxAbsence).getAttImporteMonetario().getDouble();
                        }
                    }

                    // complete with 4 null values (each iteration corresponds to one row in report):
                    for (int i = idxAbsence; i < CFDI33_NOM12_MAX_LINES_EXTRA; i++) {
                        absences.add(null);
                        absences.add(null);
                        absences.add(null);
                        absences.add(null);
                    }

                    paramsMap.put("oPerceptions", perceptions);
                    paramsMap.put("oDeductions", deductions);
                    paramsMap.put("oExtratimes", overtimes);
                    paramsMap.put("oIncapacities", absences);
                    paramsMap.put("TotalPercepcionesGravado", totalPerceptions);
                    paramsMap.put("TotalPercepcionesExento", null);
                    paramsMap.put("TotalDeduccionesGravado", totalDeductions);
                    paramsMap.put("TotalDeduccionesExento", null);
                    paramsMap.put("TotalTiempoExtra", totalOvertimeHrs);
                    paramsMap.put("TotalTiempoExtraPagado", totalOvertimePay);
                    paramsMap.put("TotalIncapacidades", dTotalIncapacidades);
                    paramsMap.put("dCfdTotalIsr", dTotalIsr);
                    
                    break;
                    
                case "tfd:TimbreFiscalDigital":
                    cfd.ver33.DElementTimbreFiscalDigital tfd = (cfd.ver33.DElementTimbreFiscalDigital) element;
                    paramsMap.put("sCfdiVersion", tfd.getAttVersion().getString());
                    paramsMap.put("sCfdiUuid", tfd.getAttUUID().getString());
                    paramsMap.put("sCfdiSelloCFD", sello = tfd.getAttSelloCFD().getString());
                    paramsMap.put("sCfdiSelloSAT", tfd.getAttSelloSAT().getString());
                    paramsMap.put("sCfdiNoCertificadoSAT", tfd.getAttNoCertificadoSAT().getString());
                    paramsMap.put("sCfdiFechaTimbre", tfd.getAttFechaTimbrado().getString());
                    paramsMap.put("sCfdiRfcProvCertif", tfd.getAttRfcProvCertif().getString());
                    
                    break;
                    
                default:
            }
        }
        
        // params needed by erp.server.SSessionServer.requestFillReport() to generate QR code:
        
        paramsMap.put("sSelloCfdiUltDig", sello.isEmpty() ? SLibUtils.textRepeat("0", DCfdi33Consts.STAMP_LAST_CHARS) : sello.substring(sello.length() - DCfdi33Consts.STAMP_LAST_CHARS, sello.length()));
        
        computeReport(cfd, SDataConstantsSys.REP_TRN_CFDI_PAYROLL, paramsMap, printMode, numCopies);
    }

    /**
     * 
     * @param cfd
     * @param printMode
     * @param subtypeCfd
     * @throws java.lang.Exception 
     */
    public void printCancelAck(final erp.mtrn.data.SDataCfd cfd, final int printMode, final int subtypeCfd) throws java.lang.Exception {
        Map<String, Object> map = miClient.createReportParams();

        switch (cfd.getFkCfdTypeId()) {
            case SDataConstantsSys.TRNS_TP_CFD_INV:
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, new int[] { cfd.getFkDpsYearId_n(), cfd.getFkDpsDocId_n() }, SLibConstants.EXEC_MODE_SILENT);
                
                map.put("sCfdiDate", dps.getDate());
                map.put("sCfdiFolio", dps.getDpsNumber());
                break;
                
            case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                map.put("sCfdiDate", cfd.getTimestamp());
                map.put("sCfdiFolio", cfd.getCfdNumber());
                break;
                
            case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                switch (subtypeCfd) {
                    case SCfdConsts.CFDI_PAYROLL_VER_OLD:
                        SDataFormerPayrollEmp payrollEmp = (SDataFormerPayrollEmp) SDataUtilities.readRegistry(miClient, SDataConstants.HRS_SIE_PAY_EMP, new int[] { cfd.getFkPayrollPayrollId_n(), cfd.getFkPayrollEmployeeId_n() }, SLibConstants.EXEC_MODE_SILENT);
                        map.put("sCfdiFolio", payrollEmp.getFormerPayrollEmpNumber());
                        break;
                        
                    case SCfdConsts.CFDI_PAYROLL_VER_CUR:
                        SDbPayrollReceipt payrollReceipt = (SDbPayrollReceipt) miClient.getSession().readRegistry(SModConsts.HRS_PAY_RCP, new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n() });
                        map.put("sCfdiFolio", payrollReceipt.getChildPayrollReceiptIssue().getPayrollReceiptIssueNumber());
                        break;
                        
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                
                map.put("sCfdiDate", cfd.getTimestamp());
                break;
                
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        // Acknowledgment cancellation:

        Node node;
        Node nodeChild;
        NamedNodeMap namedNodeMap;
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = docBuilder.parse(new ByteArrayInputStream(cfd.getAcknowledgmentCancellationXml().getBytes("UTF-8")));
        
        node = SXmlUtils.extractElements(doc, "CancelaCFDResult").item(0);
        if (node == null) {
            node = SXmlUtils.extractElements(doc, "ns2:CancelaCFDResult").item(0);  // try again
        }
        namedNodeMap = node.getAttributes();

        map.put("sEmiRfc", SXmlUtils.extractAttributeValue(namedNodeMap, "RfcEmisor", true));
        map.put("sDateRequest", SXmlUtils.extractAttributeValue(namedNodeMap, "Fecha", true));
        map.put("sDateCancel", SXmlUtils.extractAttributeValue(namedNodeMap, "Fecha", true));

        if (SXmlUtils.hasChildElement(node, "Folios")) {
            node = SXmlUtils.extractChildElements(node, "Folios").get(0);
        }
        else {
            node = SXmlUtils.extractChildElements(node, "ns2:Folios").get(0);      // try again
        }

        if (SXmlUtils.hasChildElement(node, "UUID")) {
            nodeChild = SXmlUtils.extractChildElements(node, "UUID").get(0);
        }
        else {
            nodeChild = SXmlUtils.extractChildElements(node, "ns2:UUID").get(0);   // try again
        }
        map.put("sUuid", nodeChild.getTextContent());

        if (SXmlUtils.hasChildElement(node, "EstatusUUID")) {
            nodeChild = SXmlUtils.extractChildElements(node, "EstatusUUID").get(0);    // try again
        }
        else {
            nodeChild = SXmlUtils.extractChildElements(node, "ns2:EstatusUUID").get(0);    // try again
        }
        map.put("sStatusCfdi", nodeChild.getTextContent().compareTo(SCfdConsts.UUID_CANCEL_OK) == 0 ? "Cancelado" : "Desconocido");

        node = SXmlUtils.extractElements(doc, "CancelaCFDResult").item(0);
        if (node == null) {
            node = SXmlUtils.extractElements(doc, "ns2:CancelaCFDResult").item(0);  // try again
        }

        node = SXmlUtils.extractChildElements(node, "Signature").get(0);
        nodeChild = SXmlUtils.extractChildElements(node, "SignatureValue").get(0);

        map.put("sCfdiSelloSAT", nodeChild.getTextContent());
        map.put("nPkCfdId", cfd.getPkCfdId());
        
        computeReport(cfd, SDataConstantsSys.REP_TRN_CFDI_ACK_CAN, map, printMode, 1);
    }
}
