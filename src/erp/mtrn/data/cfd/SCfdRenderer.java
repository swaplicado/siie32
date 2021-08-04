/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data.cfd;

import cfd.DCfd;
import cfd.DCfdConsts;
import cfd.DCfdUtils;
import cfd.DElement;
import cfd.ver33.DCfdi33Consts;
import cfd.ver33.DElementConcepto;
import erp.cfd.SCfdConsts;
import erp.cfd.SCfdXmlCatalogs;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormValidation;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerCategory;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SBpsUtils;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SCfdUtilsHandler;
import erp.mtrn.data.SDataDps;
import erp.mtrn.form.SDialogCfdiImport;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import sa.lib.SLibUtils;
import sa.lib.xml.SXmlUtils;

/**
 * Muestra el CFDI y se permite la validación cuando sea necesaria ante el SAT.
 * @author Isabel Servín
 */
public final class SCfdRenderer implements java.awt.event.ActionListener{
    
    private final SClientInterface miClient;
    private String msCfdiXml;
    private File moCfdiFile;
    private ArrayList<DElementConcepto> moConceptos;
    private HashMap<String, Object> moParamsMap;
    private JButton mjbValidate;
    private SDataDps moPurchaseOrder;
    private SDataDps moDpsRendered;
    private int mnBizCategory; 
    private JDialog moCfdiViewer;
    private boolean mbShowValidateButton;
    
    /**
     * Clase que se encarga de mostrar el CFDI y hacer las validaciones previo 
     *   al empate de los conceptos del CFDI y de SIIE.
     * @param client 
     */
    public SCfdRenderer(final SClientInterface client) {
        miClient = client;
        msCfdiXml = "";
    }
    
    /**
     * Obtiene un archivo xml para la vista previa en PDF y el empate de conceptos.
     * @param file
     * @param dps si es nulo, significa que no tiene orden de compra.
     * @param category
     * @return Dps renderizado
     * @throws Exception
     */
    public SDataDps renderCfdi(File file, SDataDps dps, int category) throws Exception {
        try {
            msCfdiXml = SXmlUtils.readXml(file.getAbsolutePath()); 
        } 
        catch (Exception e) {
            throw new Exception("El XML no es válido.");
        }
        
        moCfdiFile = file; 
        moPurchaseOrder = dps;
        mnBizCategory = category;
        mbShowValidateButton = true;
        createParamsMap();
        showCfdi();
        return moDpsRendered;
    }
      
    private void validateCfdi(File cfdiFile) throws Exception{
        SFormValidation validation = new SFormValidation();
        // obtener CFDI: 
        
        try {
            msCfdiXml = SXmlUtils.readXml(cfdiFile.getAbsolutePath());
        } 
        catch(Exception e) {
            SLibUtilities.renderException(this, e);
        }
        
        cfd.ver33.DElementComprobante comprobante = DCfdUtils.getCfdi33(msCfdiXml);
        
        if (!validation.getIsError()) {
            String cfdiStatus = new SCfdUtilsHandler(miClient).getCfdiSatStatus(comprobante).getCfdiStatus(); 
            if (!cfdiStatus.equals(SCfdConsts.STATUS_VALID)) {
                validation.setMessage("No se puede importar el CFDI ya que su estatus es : " + cfdiStatus + ".");
            }
        }
        
        if (!validation.getIsError()) {
            if (!comprobante.getAttTipoDeComprobante().getString().toUpperCase().equals("I")) {
                validation.setMessage("No se puede importar el CFDI ya que no es una factura de ingresos.");
            }
        }

        // validar receptor del CFDI:
        
        int idEmisor = 0;
        if(!validation.getIsError()) {
            String receptor;

            if (comprobante.getEltReceptor().getAttNombre() == null || comprobante.getEltReceptor().getAttNombre().getString().isEmpty()) {
                receptor = comprobante.getEltReceptor().getAttRfc().getString();
            }
            else {
                receptor = comprobante.getEltReceptor().getAttNombre().getString() + " (" + comprobante.getEltReceptor().getAttRfc().getString() + ")";
            }

            if (!comprobante.getEltReceptor().getAttRfc().getString().equals(miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId())) {
                validation.setMessage("¡El receptor '" + receptor + "' no corresponde a la empresa!");
            }
            else {
                // validar emisor del CFDI:

                String emisor;

                if (comprobante.getEltEmisor().getAttNombre() == null || comprobante.getEltEmisor().getAttNombre().getString().isEmpty()) {
                    emisor = comprobante.getEltEmisor().getAttRfc().getString();
                }
                else {
                    emisor = comprobante.getEltEmisor().getAttNombre().getString() + " (" + comprobante.getEltEmisor().getAttRfc().getString() + ")";
                }

                idEmisor = SBpsUtils.getBizParterIdByFiscalId(miClient.getSession().getStatement(), 
                        comprobante.getEltEmisor().getAttRfc().getString(), "", 0);

                if (idEmisor == 0) {
                    validation.setMessage("¡El emisor '" + emisor + "' no existe en los catálogos de proveedores ni de asociados de negocios!");
                }
                else {
                    idEmisor = SBpsUtils.getBizParterIdByFiscalId(miClient.getSession().getStatement(), 
                            comprobante.getEltEmisor().getAttRfc().getString(), "", SDataConstantsSys.BPSS_CT_BP_SUP);

                    if (idEmisor == 0) {
                        validation.setMessage("¡El emisor '" + emisor + "' no existe en el catálogo de proveedores!");
                    }
                    else {
                        SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, 
                                SDataConstants.BPSU_BP, new int[] { idEmisor }, SLibConstants.EXEC_MODE_SILENT);
                        SDataBizPartnerCategory bizPartnerCategory = bizPartner.getDbmsCategorySettingsSup(); // variable de conveniencia

                        if (bizPartner.getIsDeleted()) {
                            validation.setMessage("¡El emisor '" + emisor + "' está eliminado como asociado de negocios!");
                        }
                        else if (bizPartnerCategory == null) {
                            validation.setMessage("¡El emisor '" + emisor + "' no es proveedor!");
                        }
                        else if (bizPartnerCategory.getIsDeleted()) {
                            validation.setMessage("¡El emisor '" + emisor + "' está eliminado como proveedor!");
                        }
                        else if (SDataUtilities.obtainIsBizPartnerBlocked(miClient, bizPartner.getPkBizPartnerId(), mnBizCategory)) {
                            validation.setMessage("!El emisor '" + emisor + "' está bloqueado!");
                        }
                        else {
                            if (miClient.getSessionXXX().getParamsErp().getIsPurchasesCreditInvoice()) {
                                int risk = bizPartnerCategory.getEffectiveRiskTypeId();

                                if (risk == SModSysConsts.BPSS_RISK_D_BLK) {
                                    validation.setMessage(SLibConstants.MSG_INF_BP_BLOCKED);
                                }
                                else if (risk == SModSysConsts.BPSS_RISK_E_TRL) {
                                    validation.setMessage(SLibConstants.MSG_INF_BP_TRIAL);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if (!validation.getIsError()) {
            int[] key;
            SDataDps dps;
            key = SDataUtilities.obtainDpsKeyForBizPartner(miClient, comprobante.getAttSerie().getString(), comprobante.getAttFolio().getString(), SDataConstantsSys.TRNS_CL_DPS_PUR_DOC, new int[] { idEmisor });
            if (key != null) {
                dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, key, SLibConstants.EXEC_MODE_VERBOSE);
                //moDps.poliza contable año-mes sucursal poliz, num de poliza getdbmsregisrtry
                Object[] primaryKey = (Object[]) dps.getDbmsRecordKey();
                validation.setMessage("El documento ya existe en la siguiente póliza contable:\n" +
                    "Fecha de la póliza: " + miClient.getSessionXXX().getFormatters().getDateFormat().format(dps.getDbmsRecordDate()) + "\n" +
                    "Período contable: " + primaryKey[0] + "-" + miClient.getSessionXXX().getFormatters().getMonthFormat().format(primaryKey[1]) + "\n" +
                    "Número de póliza: " + primaryKey[3] + "-" + primaryKey[4]);
            }
            
            if (!validation.getIsError()) {
                if (comprobante.getEltOpcComplemento() != null) {
                    for (DElement element : comprobante.getEltOpcComplemento().getElements()) {
                        if (element.getName().compareTo("tfd:TimbreFiscalDigital") == 0) {
                            cfd.ver33.DElementTimbreFiscalDigital tfd = (cfd.ver33.DElementTimbreFiscalDigital) element;
                            if (SCfdUtils.getCfdIdByUuid(miClient, tfd.getAttUUID().getString()) != 0){
                                validation.setMessage("El UUID del documento ya existe en la base de datos.");
                            }
                        }
                    }
                }
            }
            
            if (!validation.getIsError() && moPurchaseOrder != null) {
                if (moPurchaseOrder.getDate().after(comprobante.getAttFecha().getDatetime())) {
                    validation.setMessage("El documento no puede tener una fecha anterior a la de la orden de compra. \n"
                            + "Fecha OC: " + SLibUtils.DateFormatDate.format(moPurchaseOrder.getDate()) + "\n"
                            + "Fecha CFDI: " + SLibUtils.DateFormatDate.format(comprobante.getAttFecha().getDatetime()));
                }
                if (!validation.getIsError()) {
                    int idCur = 0;
                    try {
                        String sql = "SELECT id_cur FROM erp.cfgu_cur WHERE cur_key = '" + comprobante.getAttMoneda().getString() + "'";
                        try (ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql)) {
                            if (resultSet.next()) {
                                idCur = resultSet.getInt(1);
                            }
                        }
                    }
                    catch (Exception e){
                        SLibUtils.printException(this, e);
                    }
                    if (moPurchaseOrder.getFkCurrencyId() != idCur) {
                        validation.setMessage("La moneda del documento no coincide con el de la orden de compra.");
                    }
                }
            }
            
            if (!validation.getIsError()) {
                moCfdiViewer.setVisible(false);
                SDialogCfdiImport dialog = new SDialogCfdiImport(miClient, moPurchaseOrder, moCfdiFile);
                dialog.setComprobante(comprobante); 
                dialog.setFormVisible(true);
                moDpsRendered = dialog.getDps();
            }
        }
        
        if (validation.getIsError()) {
            miClient.showMsgBoxWarning(validation.getMessage());
        }
    }
    
    /**
     * Recibe un archivo xml para visualizarlo en PDF.
     * @param xml CFDI del doc.
     */
    public void showCfdi(String xml){
        mbShowValidateButton = false;
        msCfdiXml = xml;
        createParamsMap();
        showCfdi();
    }
    
    private void createParamsMap() {
        try {
            moParamsMap = new HashMap<>();
            moConceptos = new ArrayList<>();
            
            SCfdXmlCatalogs catalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
            cfd.ver33.DElementComprobante comprobante = DCfdUtils.getCfdi33(msCfdiXml);
            
            if (comprobante.getVersion() != DCfdConsts.CFDI_VER_33) {
                throw new Exception("El CFDI no corresponde a la versión 3.3.");
            }
            
            moParamsMap.put("sCfdVersion", "" + comprobante.getVersion());
            moParamsMap.put("sCfdSerieOpc", comprobante.getAttSerie().getString());
            moParamsMap.put("sCfdFolio", comprobante.getAttFolio().getString());
            moParamsMap.put("sCfdFecha", SLibUtils.DbmsDateFormatDatetime.format(comprobante.getAttFecha().getDatetime()));
            moParamsMap.put("sCfdSello", comprobante.getAttSello().getString());
            moParamsMap.put("sCfdFormaDePago", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY, comprobante.getAttFormaPago().getString()));
            moParamsMap.put("sCfdMoneda", comprobante.getAttMoneda().getString());
            moParamsMap.put("dCfdTipoCambio", comprobante.getAttTipoCambio() != null ? comprobante.getAttTipoCambio().getDouble() : 1.0);
            moParamsMap.put("sCfdNoCertificado", comprobante.getAttNoCertificado().getString());
            moParamsMap.put("sCfdCondicionesDePagoOpc", comprobante.getAttCondicionesDePago().getString());
            moParamsMap.put("dCfdSubTotal", comprobante.getAttSubTotal().getDouble());
            moParamsMap.put("dCfdDescuento", comprobante.getAttDescuento() != null ? comprobante.getAttDescuento().getDouble() : 0.0);
            moParamsMap.put("dCfdTotal", comprobante.getAttTotal().getDouble());
            moParamsMap.put("sCfdMetodoDePagoOpc", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_PAY_MET, comprobante.getAttMetodoPago().getString()));
            moParamsMap.put("sExpedidoEn", comprobante.getAttLugarExpedicion().getString());
            moParamsMap.put("sCfdTipoDeComprobante", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_CFD_TP, comprobante.getAttTipoDeComprobante().getString()));
            moParamsMap.put("oEtlOpcImpuestos", comprobante.getEltOpcImpuestos());
            
            // Emisor:
            
            moParamsMap.put("sEmiRfc", comprobante.getEltEmisor().getAttRfc().getString());
            moParamsMap.put("sEmiNombre", comprobante.getEltEmisor().getAttNombre().getString());
            moParamsMap.put("sEmiRegimenFiscal", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, comprobante.getEltEmisor().getAttRegimenFiscal().getString()));
            
            // Receptor:
            
            moParamsMap.put("sRecRfc", comprobante.getEltReceptor().getAttRfc().getString());
            moParamsMap.put("sRecNombreOpc", comprobante.getEltReceptor().getAttNombre().getString());
            moParamsMap.put("sFiscalId", comprobante.getEltReceptor().getAttNumRegIdTrib().getString());
            moParamsMap.put("sCfdUsoCFDI", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_CFD_USE, comprobante.getEltReceptor().getAttUsoCFDI().getString()));
            
            moConceptos = comprobante.getEltConceptos().getEltConceptos();
            
            // Stamp:
            
            String sello = "";
            if (comprobante.getEltOpcComplemento() != null) {
                for (DElement element : comprobante.getEltOpcComplemento().getElements()) {
                    if (element.getName().compareTo("tfd:TimbreFiscalDigital") == 0) {
                        cfd.ver33.DElementTimbreFiscalDigital tfd = (cfd.ver33.DElementTimbreFiscalDigital) element;
                        moParamsMap.put("sCfdiVersion", tfd.getAttVersion().getString());
                        moParamsMap.put("sCfdiUuid", tfd.getAttUUID().getString());
                        moParamsMap.put("sCfdiSelloCFD", sello = tfd.getAttSelloCFD().getString());
                        moParamsMap.put("sCfdiSelloSAT", tfd.getAttSelloSAT().getString());
                        moParamsMap.put("sCfdiNoCertificadoSAT", tfd.getAttNoCertificadoSAT().getString());
                        moParamsMap.put("sCfdiFechaTimbre", tfd.getAttFechaTimbrado().getString());
                        moParamsMap.put("sCfdiRfcProvCertif", tfd.getAttRfcProvCertif().getString());
                        moParamsMap.put("sCfdiLeyenda", tfd.getAttLeyenda().getString());
                    }
                }
            }
            moParamsMap.put("sSelloCfdiUltDig", sello.isEmpty() ? SLibUtils.textRepeat("0", DCfdi33Consts.STAMP_LAST_CHARS) : sello.substring(sello.length() - DCfdi33Consts.STAMP_LAST_CHARS, sello.length()));
            
            BufferedImage biQrCode = null;
            if (Float.parseFloat((String) moParamsMap.get("sCfdVersion")) == DCfdConsts.CFDI_VER_33) {
                biQrCode = DCfd.createQrCodeBufferedImageCfdi33((String) moParamsMap.get("sCfdiUuid"), (String) moParamsMap.get("sEmiRfc"), (String) moParamsMap.get("sRecRfc"), Double.parseDouble("" + moParamsMap.get("dCfdTotal")), (String) moParamsMap.get("sSelloCfdiUltDig"));    
            }
            if (biQrCode != null) {
                moParamsMap.put("oCfdiQrCode", biQrCode.getScaledInstance(biQrCode.getWidth(), biQrCode.getHeight(), Image.SCALE_DEFAULT));
            }
        }
        catch (Exception ex) {
            SLibUtilities.renderException(this, ex);
        }
    }

    private void showCfdi() {
        try {
            moCfdiViewer = new JDialog(new JFrame(),"CFDI", true);
            moCfdiViewer.setSize(1000,800);
            moCfdiViewer.setLocationRelativeTo(null);
            if (mbShowValidateButton) {
                mjbValidate = new JButton(); 
                mjbValidate.setText("Continuar");
                mjbValidate.setBounds(430, 1, 100, 25);
                mjbValidate.addActionListener(this);
                mjbValidate.setToolTipText("Continuar con la captura del CFDI");
                moCfdiViewer.add(mjbValidate);
            }
            File fileTemplate = new File("reps/view_cfdi_33.jasper");
            JasperReport relatoriosJasper =
            (JasperReport)JRLoader.loadObject(fileTemplate);
            JasperPrint jasperPrint = JasperFillManager.fillReport(relatoriosJasper, moParamsMap, new JRBeanCollectionDataSource(moConceptos));
            JasperViewer jrViewer = new JasperViewer(jasperPrint, true);
            moCfdiViewer.getContentPane().add(jrViewer.getContentPane());
            moCfdiViewer.setVisible(true);
        } 
        catch (HeadlessException | JRException e){
            SLibUtilities.renderException(this, e);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() instanceof javax.swing.JButton) {
                JButton button = (JButton) e.getSource();
                if (button == mjbValidate) {
                    validateCfdi(moCfdiFile);
                }
            }
        }
        catch (Exception ex){
            SLibUtilities.renderException(this, ex);
        }
    }
}
