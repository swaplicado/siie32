/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data.cfd;

import cfd.DCfd;
import cfd.DCfdConsts;
import cfd.DCfdUtils;
import cfd.ver33.DCfdi33Catalogs;
import cfd.ver33.DCfdi33Consts;
import cfd.ver40.DCfdi40Catalogs;
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
import erp.mtrn.data.SDataDps;
import erp.mtrn.form.SDialogCfdiImport33;
import erp.mtrn.form.SDialogCfdiImport40;
import erp.swap.form.SDialogPdfViewer;
import erp.swap.form.SDocumentInfo;
import erp.swap.utils.SImportUtils;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.xml.SXmlUtils;

/**
 * Muestra el CFDI y se permite la validación cuando sea necesaria ante el SAT.
 * @author Isabel Servín, Sergio Flores
 */
public final class SDialogCfdRenderer implements ActionListener {
    
    private final SClientInterface miClient;
    private int mnDocumentType;
    private String msCfdiXml;
    private File moCfdiFile;
    private File moPdfFile;
    private SDataDps moDpsToLink;
    private int mnBizPartnerCategory;
    private float mfCfdiVersion;
    private cfd.ver40.DElementComprobante moComprobante40;
    private cfd.ver33.DElementComprobante moComprobante33;
    private ArrayList<cfd.ver40.DElementConcepto> moConceptos40; 
    private ArrayList<cfd.ver33.DElementConcepto> moConceptos33; 
    private HashMap<String, Object> moParamsMap;
    private boolean mbCreateProcessingButtons;
    private JButton mjViewPdf;
    private JButton mjProcessCfd;
    private JButton mjClose;
    private SDataDps moDpsRendered;
    private JDialog moCfdiViewer;
    protected SDialogPdfViewer moDialogPdfViewer;
    protected DCfdUtils.CfdEssentials moCfdEssentials;
    
    /**
     * Clase que se encarga de mostrar el CFDI y hacer las validaciones previo 
     *   al empate de los conceptos del CFDI y de SIIE.
     * @param client 
     */
    public SDialogCfdRenderer(final SClientInterface client) {
        miClient = client;
    }
    
    /*
     * Private methods.
     */
    
    private boolean isInvoice() {
        return mnDocumentType == SDataConstantsSys.TRNX_TP_DPS_DOC;
    }
    
    private String getDpsToLinkName() {
        return isInvoice() ? "orden de compra" : "factura de compra";
    }

    private void showCfdiViewer() throws Exception {
        // Prepare viewer:
        
        if (moCfdiViewer == null) {
            // create dialog:

            moCfdiViewer = new JDialog(miClient.getFrame(),"Visor de CFDI", true);
            moCfdiViewer.setSize(1000, 640);
            moCfdiViewer.setLocationRelativeTo(null);
            moCfdiViewer.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            if (mbCreateProcessingButtons) {
                // allow further processing of CFDI, in order to be recorded:

                mjViewPdf = new JButton(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon-file-pdf.png")));
                mjViewPdf.setBounds(440, 1, 25, 25);
                mjViewPdf.addActionListener(this);
                mjViewPdf.setToolTipText("Ver PDF del CFDI...");
                moCfdiViewer.add(mjViewPdf);

                mjProcessCfd = new JButton("Continuar");
                mjProcessCfd.setBounds(480, 1, 100, 25);
                mjProcessCfd.addActionListener(this);
                    mjProcessCfd.setToolTipText("Continuar con la captura del CFDI...");
                moCfdiViewer.add(mjProcessCfd);

                mjViewPdf.setEnabled(moPdfFile != null);
            }
            else {
                // setup this dialog as a "floating" window, accessible all the time:

                moCfdiViewer.setModalityType(Dialog.ModalityType.MODELESS);
                moCfdiViewer.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
            }

            mjClose = new JButton("Cerrar");
            mjClose.setBounds(850, 1, 100, 25);
            mjClose.addActionListener(this);
            mjClose.setToolTipText("Cerrar el visor de CFDI");
            moCfdiViewer.add(mjClose);
        }
        else {
            // remove previous rendering panel:

            JPanel panel = null;

            for (Component component : moCfdiViewer.getContentPane().getComponents()) {
                if (component instanceof JPanel) {
                    panel = (JPanel) component;
                    break;
                }
            }

            if (panel != null) {
                moCfdiViewer.getContentPane().remove(panel);
            }
        }

        // prepare CFDI rendering:

        File fileTemplate;
        
        mfCfdiVersion = DCfdUtils.getCfdiVersion(msCfdiXml);
        
        if (mfCfdiVersion == DCfdConsts.CFDI_VER_40) {
            parseCfdiAndCreateParamsMap40();
            fileTemplate = new File("reps/view_cfdi_40.jasper");
        }
        else {
            parseCfdiAndCreateParamsMap33();
            fileTemplate = new File("reps/view_cfdi_33.jasper");
        }

        JasperReport jasperReport = (JasperReport)JRLoader.loadObject(fileTemplate);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(mfCfdiVersion == DCfdConsts.CFDI_VER_40 ? moConceptos40 : moConceptos33); // data source made of Java 7 Objects!
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, moParamsMap, dataSource);
        JasperViewer jasperViewer = new JasperViewer(jasperPrint, true);

        // render CFDI:

        moCfdiViewer.getContentPane().add(jasperViewer.getContentPane());
        moCfdiViewer.setVisible(true);
    }
    
    private SDocumentInfo createDocumentInfo() throws Exception {
        if (moCfdEssentials == null) {
            moCfdEssentials = DCfdUtils.getCfdi40Essentials(msCfdiXml);
        }
        
        return new SDocumentInfo(moCfdEssentials.Serie, moCfdEssentials.Folio, moCfdEssentials.Uuid, moCfdEssentials.Fecha, moCfdEssentials.Emisor);
    }
    
    private SFormValidation validateCfdi40() throws Exception {
        SFormValidation validation = new SFormValidation();
        
        // validar CFDI:
        
        try {
            SImportUtils.validateCfdi40(miClient, moComprobante40, isInvoice() ? DCfdi40Catalogs.CFD_TP_I : DCfdi40Catalogs.CFD_TP_E, true);
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
            validation.setMessage(e.getMessage());
        }
        
        // validar receptor del CFDI:
        
        if (!validation.getIsError()) {
            String receptor;

            if (moComprobante40.getEltReceptor().getAttNombre() == null || moComprobante40.getEltReceptor().getAttNombre().getString().isEmpty()) {
                receptor = moComprobante40.getEltReceptor().getAttRfc().getString();
            }
            else {
                receptor = moComprobante40.getEltReceptor().getAttNombre().getString() + " (" + moComprobante40.getEltReceptor().getAttRfc().getString() + ")";
            }

            if (!moComprobante40.getEltReceptor().getAttRfc().getString().equals(miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId())) {
                validation.setMessage("¡El receptor '" + receptor + "' del CFDI no corresponde a la empresa " + miClient.getSessionXXX().getCompany().getDbmsDataCompany().getBizPartner() + "!");
            }
            else {
                // validar emisor del CFDI:

                String emisor;

                if (moComprobante40.getEltEmisor().getAttNombre() == null || moComprobante40.getEltEmisor().getAttNombre().getString().isEmpty()) {
                    emisor = moComprobante40.getEltEmisor().getAttRfc().getString();
                }
                else {
                    emisor = moComprobante40.getEltEmisor().getAttNombre().getString() + " (" + moComprobante40.getEltEmisor().getAttRfc().getString() + ")";
                }

                int idEmisor = SBpsUtils.getBizParterIdByFiscalId(miClient.getSession().getStatement(), 
                        moComprobante40.getEltEmisor().getAttRfc().getString(), "", SDataConstantsSys.BPSS_CT_BP_SUP);
                
                if (idEmisor == 0) {
                    idEmisor = SBpsUtils.getBizParterIdByFiscalId(miClient.getSession().getStatement(), 
                            moComprobante40.getEltEmisor().getAttRfc().getString(), "", 0);
                }

                if (idEmisor == 0) {
                    validation.setMessage("¡El emisor '" + emisor + "' del CFDI no existe como proveedor ni tampoco como asociado de negocios!");
                }
                else {
                    SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, 
                            SDataConstants.BPSU_BP, new int[] { idEmisor }, SLibConstants.EXEC_MODE_SILENT);
                    SDataBizPartnerCategory bizPartnerCategory = bizPartner.getDbmsCategorySettingsSup(); // variable de conveniencia

                    if (bizPartner.getIsDeleted()) {
                        validation.setMessage("¡El emisor '" + emisor + "' del CFDI está eliminado como asociado de negocios!");
                    }
                    else if (!bizPartner.getIsSupplier() || bizPartnerCategory == null) {
                        validation.setMessage("¡El emisor '" + emisor + "' del CFDI no es proveedor!");
                    }
                    else if (bizPartnerCategory.getIsDeleted()) {
                        validation.setMessage("¡El emisor '" + emisor + "' del CFDI está eliminado como proveedor!");
                    }
                    else if (SDataUtilities.obtainIsBizPartnerBlocked(miClient, bizPartner.getPkBizPartnerId(), mnBizPartnerCategory)) {
                        validation.setMessage("!El emisor '" + emisor + "' del CFDI está bloqueado!");
                    }
                    else {
                        if (miClient.getSessionXXX().getParamsErp().getIsPurchasesCreditInvoice()) { // aplicar política de crédito, si está activa
                            int risk = bizPartnerCategory.getEffectiveRiskTypeId();

                            if (risk == SModSysConsts.BPSS_RISK_D_BLOCKED) {
                                validation.setMessage(SLibConstants.MSG_INF_BP_BLOCKED);
                            }
                            else if (risk == SModSysConsts.BPSS_RISK_E_TRIAL_WO_OPS) {
                                validation.setMessage(SLibConstants.MSG_INF_BP_TRIAL_WO_OPS);
                            }
                            else if (risk == SModSysConsts.BPSS_RISK_F_TRIAL_W_OPS) {
                                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_INF_BP_TRIAL_W_OPS + "\n" + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                                    validation.setMessage(SLibConstants.MSG_INF_BP_TRIAL_W_OPS);
                                }
                            }
                        }
                    }
                }
                
                if (!validation.getIsError()) {
                    int[] key = SDataUtilities.obtainDpsKeyForBizPartner(miClient, moComprobante40.getAttSerie().getString(), moComprobante40.getAttFolio().getString(),
                            isInvoice() ? SDataConstantsSys.TRNS_CL_DPS_PUR_DOC : SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ, new int[] { idEmisor });

                    if (key != null) {
                        SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, key, SLibConstants.EXEC_MODE_VERBOSE);
                        Object[] primaryKey = (Object[]) dps.getDbmsRecordKey();

                        validation.setMessage("El CFDI '" + dps.getDpsNumber() + "' ya está registrado en la siguiente póliza contable:\n" +
                            "Fecha de la póliza: " + miClient.getSessionXXX().getFormatters().getDateFormat().format(dps.getDbmsRecordDate()) + "\n" +
                            "Período contable: " + primaryKey[0] + "-" + miClient.getSessionXXX().getFormatters().getMonthFormat().format(primaryKey[1]) + "\n" +
                            "Número de póliza: " + primaryKey[3] + "-" + primaryKey[4]);
                    }

                    if (!validation.getIsError()) {
                        cfd.ver40.DElementTimbreFiscalDigital tfd = moComprobante40.getEltOpcComplementoTimbreFiscalDigital();
                        if (tfd != null) {
                            if (SCfdUtils.getCfdIdByUuid(miClient, tfd.getAttUUID().getString()) != 0) {
                                validation.setMessage("El UUID del CFDI '" + tfd.getAttUUID().getString() + "' ya existe en la base de datos.");
                            }
                        }
                    }

                    if (!validation.getIsError()) {
                        int[] systemMonth = SLibTimeUtils.digestMonth(miClient.getSession().getSystemDate());

                        if (!SLibTimeUtils.isBelongingToPeriod(moComprobante40.getAttFecha().getDatetime(), systemMonth[0], systemMonth[1])) {
                            String confirm = "La fecha del CFDI '" + SLibUtils.DateFormatDate.format(moComprobante40.getAttFecha().getDatetime()) + "' "
                                    + "no es del mes en curso, " + SLibUtils.DateFormatDateMonthYearLong.format(miClient.getSession().getSystemDate()) + ".\n"
                                    + SGuiConsts.MSG_CNF_CONT;

                            if (miClient.showMsgBoxConfirm(confirm) != JOptionPane.YES_OPTION) {
                                validation.setMessage("El CFDI no debería ser de " + SLibUtils.DateFormatDateMonthYearLong.format(moComprobante40.getAttFecha().getDatetime()) + ", "
                                        + "sino del mes en curso, " + SLibUtils.DateFormatDateMonthYearLong.format(miClient.getSession().getSystemDate()) + ".");
                            }
                        }
                    }

                    if (!validation.getIsError() && moDpsToLink != null) {
                        if (moDpsToLink.getDate().after(moComprobante40.getAttFecha().getDatetime())) {
                            String message = "El CFDI no puede ser anterior a la " + getDpsToLinkName() + ".\n"
                                    + "Fecha " + getDpsToLinkName() + ": " + SLibUtils.DateFormatDate.format(moDpsToLink.getDate()) + "\n"
                                    + "Fecha CFDI: " + SLibUtils.DateFormatDate.format(moComprobante40.getAttFecha().getDatetime());

                            if (isInvoice() && miClient.showMsgBoxConfirm(message + "\n" + SGuiConsts.MSG_CNF_CONT_OMIT_VAL) != JOptionPane.YES_OPTION) {
                                validation.setMessage("El CFDI no puede ser anterior a " + SLibUtils.DateFormatDate.format(moDpsToLink.getDate()) + ".");
                            }
                            else {
                                validation.setMessage(message);
                            }
                        }

                        if (!validation.getIsError()) {
                            int currencyId = 0;
                            
                            try {
                                currencyId = SImportUtils.getCurrencyId(moComprobante40.getAttMoneda().getString());
                            }
                            catch (Exception e) {
                                SLibUtils.printException(this, e);
                            }
                            
                            if (moDpsToLink.getFkCurrencyId() != currencyId) {
                                validation.setMessage("La moneda del CFDI '" + moComprobante33.getAttMoneda().getString() + "' no coincide con la de la " + getDpsToLinkName() + ".");
                            }
                        }
                    }

                    if (!validation.getIsError()) {
                        moCfdiViewer.setVisible(false);

                        SDialogCfdiImport40 dialog = new SDialogCfdiImport40(miClient, moDpsToLink, moCfdiFile, moPdfFile, createDocumentInfo());
                        dialog.setComprobante(moComprobante40);
                        dialog.setVisible(true);
                        moDpsRendered = dialog.getNewDps();
                    }
                }
            }
        }
        
        return validation;
    }

    @Deprecated
    private SFormValidation validateCfdi33() throws Exception {
        SFormValidation validation = new SFormValidation();
        
        // validar CFDI:
        
        try {
            SImportUtils.validateCfdi33(miClient, moComprobante33, isInvoice() ? DCfdi33Catalogs.CFD_TP_I : DCfdi33Catalogs.CFD_TP_E, true);
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
            validation.setMessage(e.getMessage());
        }

        // validar receptor del CFDI:
        
        if (!validation.getIsError()) {
            String receptor;

            if (moComprobante33.getEltReceptor().getAttNombre() == null || moComprobante33.getEltReceptor().getAttNombre().getString().isEmpty()) {
                receptor = moComprobante33.getEltReceptor().getAttRfc().getString();
            }
            else {
                receptor = moComprobante33.getEltReceptor().getAttNombre().getString() + " (" + moComprobante33.getEltReceptor().getAttRfc().getString() + ")";
            }

            if (!moComprobante33.getEltReceptor().getAttRfc().getString().equals(miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId())) {
                validation.setMessage("¡El receptor '" + receptor + "' del CFDI no corresponde a la empresa " + miClient.getSessionXXX().getCompany().getDbmsDataCompany().getBizPartner() + "!");
            }
            else {
                // validar emisor del CFDI:

                String emisor;

                if (moComprobante33.getEltEmisor().getAttNombre() == null || moComprobante33.getEltEmisor().getAttNombre().getString().isEmpty()) {
                    emisor = moComprobante33.getEltEmisor().getAttRfc().getString();
                }
                else {
                    emisor = moComprobante33.getEltEmisor().getAttNombre().getString() + " (" + moComprobante33.getEltEmisor().getAttRfc().getString() + ")";
                }

                int idEmisor = SBpsUtils.getBizParterIdByFiscalId(miClient.getSession().getStatement(), 
                        moComprobante33.getEltEmisor().getAttRfc().getString(), "", SDataConstantsSys.BPSS_CT_BP_SUP);
                
                if (idEmisor == 0) {
                    idEmisor = SBpsUtils.getBizParterIdByFiscalId(miClient.getSession().getStatement(), 
                            moComprobante33.getEltEmisor().getAttRfc().getString(), "", SDataConstantsSys.BPSS_CT_BP_SUP);
                }

                if (idEmisor == 0) {
                    validation.setMessage("¡El emisor '" + emisor + "' del CFDI no existe como proveedor ni tampoco como asociado de negocios!");
                }
                else {
                    SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, 
                            SDataConstants.BPSU_BP, new int[] { idEmisor }, SLibConstants.EXEC_MODE_SILENT);
                    SDataBizPartnerCategory bizPartnerCategory = bizPartner.getDbmsCategorySettingsSup(); // variable de conveniencia

                    if (bizPartner.getIsDeleted()) {
                        validation.setMessage("¡El emisor '" + emisor + "' del CFDI está eliminado como asociado de negocios!");
                    }
                    else if (!bizPartner.getIsSupplier() || bizPartnerCategory == null) {
                        validation.setMessage("¡El emisor '" + emisor + "' del CFDI no es proveedor!");
                    }
                    else if (bizPartnerCategory.getIsDeleted()) {
                        validation.setMessage("¡El emisor '" + emisor + "' del CFDI está eliminado como proveedor!");
                    }
                    else if (SDataUtilities.obtainIsBizPartnerBlocked(miClient, bizPartner.getPkBizPartnerId(), mnBizPartnerCategory)) {
                        validation.setMessage("!El emisor '" + emisor + "' del CFDI está bloqueado!");
                    }
                    else {
                        if (miClient.getSessionXXX().getParamsErp().getIsPurchasesCreditInvoice()) {
                            int risk = bizPartnerCategory.getEffectiveRiskTypeId();

                            if (risk == SModSysConsts.BPSS_RISK_D_BLOCKED) {
                                validation.setMessage(SLibConstants.MSG_INF_BP_BLOCKED);
                            }
                            else if (risk == SModSysConsts.BPSS_RISK_E_TRIAL_WO_OPS) {
                                validation.setMessage(SLibConstants.MSG_INF_BP_TRIAL_WO_OPS);
                            }
                            else if (risk == SModSysConsts.BPSS_RISK_F_TRIAL_W_OPS) {
                                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_INF_BP_TRIAL_W_OPS + "\n" + SGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                                    validation.setMessage(SLibConstants.MSG_INF_BP_TRIAL_W_OPS);
                                }
                            }
                        }
                    }
                }
                
                if (!validation.getIsError()) {
                    int[] key = SDataUtilities.obtainDpsKeyForBizPartner(miClient, moComprobante33.getAttSerie().getString(), moComprobante33.getAttFolio().getString(),
                            isInvoice() ? SDataConstantsSys.TRNS_CL_DPS_PUR_DOC : SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ, new int[] { idEmisor });

                    if (key != null) {
                        SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, key, SLibConstants.EXEC_MODE_VERBOSE);
                        Object[] primaryKey = (Object[]) dps.getDbmsRecordKey();

                        validation.setMessage("El CFDI '" + dps.getDpsNumber() + "' ya está registrado en la siguiente póliza contable:\n" +
                            "Fecha de la póliza: " + miClient.getSessionXXX().getFormatters().getDateFormat().format(dps.getDbmsRecordDate()) + "\n" +
                            "Período contable: " + primaryKey[0] + "-" + miClient.getSessionXXX().getFormatters().getMonthFormat().format(primaryKey[1]) + "\n" +
                            "Número de póliza: " + primaryKey[3] + "-" + primaryKey[4]);
                    }

                    if (!validation.getIsError()) {
                        cfd.ver33.DElementTimbreFiscalDigital tfd = moComprobante33.getEltOpcComplementoTimbreFiscalDigital();
                        if (tfd != null) {
                            if (SCfdUtils.getCfdIdByUuid(miClient, tfd.getAttUUID().getString()) != 0) {
                                validation.setMessage("El UUID del CFDI '" + tfd.getAttUUID().getString() + "' ya existe en la base de datos.");
                            }
                        }
                    }

                    if (!validation.getIsError()) {
                        int[] systemMonth = SLibTimeUtils.digestMonth(miClient.getSession().getSystemDate());

                        if (!SLibTimeUtils.isBelongingToPeriod(moComprobante33.getAttFecha().getDatetime(), systemMonth[0], systemMonth[1])) {
                            String confirm = "La fecha del CFDI '" + SLibUtils.DateFormatDate.format(moComprobante33.getAttFecha().getDatetime()) + "' "
                                    + "no es del mes en curso, " + SLibUtils.DateFormatDateMonthYearLong.format(miClient.getSession().getSystemDate()) + ".\n"
                                    + SGuiConsts.MSG_CNF_CONT;

                            if (miClient.showMsgBoxConfirm(confirm) != JOptionPane.YES_OPTION) {
                                validation.setMessage("El CFDI no debería ser de " + SLibUtils.DateFormatDateMonthYearLong.format(moComprobante33.getAttFecha().getDatetime()) + ", "
                                        + "sino del mes en curso, " + SLibUtils.DateFormatDateMonthYearLong.format(miClient.getSession().getSystemDate()) + ".");
                            }
                        }
                    }

                    if (!validation.getIsError() && moDpsToLink != null) {
                        if (moDpsToLink.getDate().after(moComprobante33.getAttFecha().getDatetime())) {
                            String message = "El CFDI no puede ser anterior a la " + getDpsToLinkName() + ".\n"
                                    + "Fecha " + getDpsToLinkName() + ": " + SLibUtils.DateFormatDate.format(moDpsToLink.getDate()) + "\n"
                                    + "Fecha CFDI: " + SLibUtils.DateFormatDate.format(moComprobante40.getAttFecha().getDatetime());

                            if (isInvoice() && miClient.showMsgBoxConfirm(message + "\n" + SGuiConsts.MSG_CNF_CONT_OMIT_VAL) != JOptionPane.YES_OPTION) {
                                validation.setMessage("El CFDI no puede ser anterior a " + SLibUtils.DateFormatDate.format(moDpsToLink.getDate()) + ".");
                            }
                            else {
                                validation.setMessage(message);
                            }
                        }

                        if (!validation.getIsError()) {
                            int currencyId = 0;
                            
                            try {
                                currencyId = SImportUtils.getCurrencyId(moComprobante33.getAttMoneda().getString());
                            }
                            catch (Exception e) {
                                SLibUtils.printException(this, e);
                            }
                            
                            if (moDpsToLink.getFkCurrencyId() != currencyId) {
                                validation.setMessage("La moneda del CFDI '" + moComprobante33.getAttMoneda().getString() + "' no coincide con la de la " + getDpsToLinkName() + ".");
                            }
                        }
                    }

                    if (!validation.getIsError()) {
                        moCfdiViewer.setVisible(false);

                        SDialogCfdiImport33 dialog = new SDialogCfdiImport33(miClient, moDpsToLink, moCfdiFile);
                        dialog.setComprobante(moComprobante33); 
                        dialog.setFormVisible(true);
                        moDpsRendered = dialog.getNewDps();
                    }
                }
            }
        }
        
        return validation;
    }
    
    private void parseCfdiAndCreateParamsMap40() throws Exception {
        moComprobante40 = DCfdUtils.getCfdi40(msCfdiXml);

        if (moComprobante40.getVersion() != DCfdConsts.CFDI_VER_40) {
            throw new Exception("El CFDI no corresponde a la versión 4.0.");
        }
        
        SCfdXmlCatalogs catalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        
        moParamsMap = new HashMap<>();

        moParamsMap.put("sCfdVersion", "" + moComprobante40.getVersion());
        moParamsMap.put("sCfdSerieOpc", moComprobante40.getAttSerie().getString());
        moParamsMap.put("sCfdFolio", moComprobante40.getAttFolio().getString());
        moParamsMap.put("sCfdFecha", SLibUtils.DbmsDateFormatDatetime.format(moComprobante40.getAttFecha().getDatetime()));
        moParamsMap.put("sCfdSello", moComprobante40.getAttSello().getString());
        moParamsMap.put("sCfdFormaDePago", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY, moComprobante40.getAttFormaPago().getString()));
        moParamsMap.put("sCfdMoneda", moComprobante40.getAttMoneda().getString());
        moParamsMap.put("dCfdTipoCambio", moComprobante40.getAttTipoCambio() != null ? moComprobante40.getAttTipoCambio().getDouble() : 1.0);
        moParamsMap.put("sCfdNoCertificado", moComprobante40.getAttNoCertificado().getString());
        moParamsMap.put("sCfdCondicionesDePagoOpc", moComprobante40.getAttCondicionesDePago().getString());
        moParamsMap.put("dCfdSubTotal", moComprobante40.getAttSubTotal().getDouble());
        moParamsMap.put("dCfdDescuento", moComprobante40.getAttDescuento() != null ? moComprobante40.getAttDescuento().getDouble() : 0.0);
        moParamsMap.put("dCfdTotal", moComprobante40.getAttTotal().getDouble());
        moParamsMap.put("sCfdMetodoDePagoOpc", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_PAY_MET, moComprobante40.getAttMetodoPago().getString()));
        moParamsMap.put("sExpedidoEn", moComprobante40.getAttLugarExpedicion().getString());
        moParamsMap.put("sCfdTipoDeComprobante", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_CFD_TP, moComprobante40.getAttTipoDeComprobante().getString()));
        moParamsMap.put("sExportacion", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_EXP, moComprobante40.getAttExportacion().getString()));
        moParamsMap.put("oEtlOpcImpuestos", moComprobante40.getEltOpcImpuestos());

        // Emisor:

        moParamsMap.put("sEmiRfc", moComprobante40.getEltEmisor().getAttRfc().getString());
        moParamsMap.put("sEmiNombre", moComprobante40.getEltEmisor().getAttNombre().getString());
        moParamsMap.put("sEmiRegimenFiscal", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, moComprobante40.getEltEmisor().getAttRegimenFiscal().getString()));

        // Receptor:

        moParamsMap.put("sRecRfc", moComprobante40.getEltReceptor().getAttRfc().getString());
        moParamsMap.put("sRecNombreOpc", moComprobante40.getEltReceptor().getAttNombre().getString());
        moParamsMap.put("sFiscalId", moComprobante40.getEltReceptor().getAttNumRegIdTrib().getString());
        moParamsMap.put("sRecRegimenFiscal", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, moComprobante40.getEltReceptor().getAttRegimenFiscalReceptor().getString()));
        moParamsMap.put("sRecDomicilioFiscal", moComprobante40.getEltReceptor().getAttDomicilioFiscalReceptor().getString());
        moParamsMap.put("sCfdUsoCFDI", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_CFD_USE, moComprobante40.getEltReceptor().getAttUsoCFDI().getString()));

        moConceptos40 = moComprobante40.getEltConceptos().getEltConceptos();

        // Stamp:

        String sello = "";
        cfd.ver40.DElementTimbreFiscalDigital tfd = moComprobante40.getEltOpcComplementoTimbreFiscalDigital();
        if (tfd != null) {
            moParamsMap.put("sCfdiVersion", tfd.getAttVersion().getString());
            moParamsMap.put("sCfdiUuid", tfd.getAttUUID().getString());
            moParamsMap.put("sCfdiSelloCFD", sello = tfd.getAttSelloCFD().getString());
            moParamsMap.put("sCfdiSelloSAT", tfd.getAttSelloSAT().getString());
            moParamsMap.put("sCfdiNoCertificadoSAT", tfd.getAttNoCertificadoSAT().getString());
            moParamsMap.put("sCfdiFechaTimbre", tfd.getAttFechaTimbrado().getString());
            moParamsMap.put("sCfdiRfcProvCertif", tfd.getAttRfcProvCertif().getString());
            moParamsMap.put("sCfdiLeyenda", tfd.getAttLeyenda().getString());
        }
        moParamsMap.put("sSelloCfdiUltDig", sello.isEmpty() ? SLibUtils.textRepeat("0", DCfdi33Consts.STAMP_LAST_CHARS) : sello.substring(sello.length() - DCfdi33Consts.STAMP_LAST_CHARS, sello.length()));

        BufferedImage biQrCode = null;
        if (Float.parseFloat((String) moParamsMap.get("sCfdVersion")) == DCfdConsts.CFDI_VER_40) {
            biQrCode = DCfd.createQrCodeBufferedImageCfdi40((String) moParamsMap.get("sCfdiUuid"), (String) moParamsMap.get("sEmiRfc"), (String) moParamsMap.get("sRecRfc"), Double.parseDouble("" + moParamsMap.get("dCfdTotal")), (String) moParamsMap.get("sSelloCfdiUltDig"));    
        }
        if (biQrCode != null) {
            moParamsMap.put("oCfdiQrCode", biQrCode.getScaledInstance(biQrCode.getWidth(), biQrCode.getHeight(), Image.SCALE_DEFAULT));
        }
    }
    
    @Deprecated
    private void parseCfdiAndCreateParamsMap33() throws Exception {
        moComprobante33 = DCfdUtils.getCfdi33(msCfdiXml);

        if (moComprobante33.getVersion() != DCfdConsts.CFDI_VER_33) {
            throw new Exception("El CFDI no corresponde a la versión 3.3.");
        }

        SCfdXmlCatalogs catalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        
        moParamsMap = new HashMap<>();

        moParamsMap.put("sCfdVersion", "" + moComprobante33.getVersion());
        moParamsMap.put("sCfdSerieOpc", moComprobante33.getAttSerie().getString());
        moParamsMap.put("sCfdFolio", moComprobante33.getAttFolio().getString());
        moParamsMap.put("sCfdFecha", SLibUtils.DbmsDateFormatDatetime.format(moComprobante33.getAttFecha().getDatetime()));
        moParamsMap.put("sCfdSello", moComprobante33.getAttSello().getString());
        moParamsMap.put("sCfdFormaDePago", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY, moComprobante33.getAttFormaPago().getString()));
        moParamsMap.put("sCfdMoneda", moComprobante33.getAttMoneda().getString());
        moParamsMap.put("dCfdTipoCambio", moComprobante33.getAttTipoCambio() != null ? moComprobante33.getAttTipoCambio().getDouble() : 1.0);
        moParamsMap.put("sCfdNoCertificado", moComprobante33.getAttNoCertificado().getString());
        moParamsMap.put("sCfdCondicionesDePagoOpc", moComprobante33.getAttCondicionesDePago().getString());
        moParamsMap.put("dCfdSubTotal", moComprobante33.getAttSubTotal().getDouble());
        moParamsMap.put("dCfdDescuento", moComprobante33.getAttDescuento() != null ? moComprobante33.getAttDescuento().getDouble() : 0.0);
        moParamsMap.put("dCfdTotal", moComprobante33.getAttTotal().getDouble());
        moParamsMap.put("sCfdMetodoDePagoOpc", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_PAY_MET, moComprobante33.getAttMetodoPago().getString()));
        moParamsMap.put("sExpedidoEn", moComprobante33.getAttLugarExpedicion().getString());
        moParamsMap.put("sCfdTipoDeComprobante", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_CFD_TP, moComprobante33.getAttTipoDeComprobante().getString()));
        moParamsMap.put("oEtlOpcImpuestos", moComprobante33.getEltOpcImpuestos());

        // Emisor:

        moParamsMap.put("sEmiRfc", moComprobante33.getEltEmisor().getAttRfc().getString());
        moParamsMap.put("sEmiNombre", moComprobante33.getEltEmisor().getAttNombre().getString());
        moParamsMap.put("sEmiRegimenFiscal", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, moComprobante33.getEltEmisor().getAttRegimenFiscal().getString()));

        // Receptor:

        moParamsMap.put("sRecRfc", moComprobante33.getEltReceptor().getAttRfc().getString());
        moParamsMap.put("sRecNombreOpc", moComprobante33.getEltReceptor().getAttNombre().getString());
        moParamsMap.put("sFiscalId", moComprobante33.getEltReceptor().getAttNumRegIdTrib().getString());
        moParamsMap.put("sCfdUsoCFDI", catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_CFD_USE, moComprobante33.getEltReceptor().getAttUsoCFDI().getString()));

        moConceptos33 = moComprobante33.getEltConceptos().getEltConceptos();

        // Stamp:

        String sello = "";
        cfd.ver33.DElementTimbreFiscalDigital tfd = moComprobante33.getEltOpcComplementoTimbreFiscalDigital();
        if (tfd != null) {
            moParamsMap.put("sCfdiVersion", tfd.getAttVersion().getString());
            moParamsMap.put("sCfdiUuid", tfd.getAttUUID().getString());
            moParamsMap.put("sCfdiSelloCFD", sello = tfd.getAttSelloCFD().getString());
            moParamsMap.put("sCfdiSelloSAT", tfd.getAttSelloSAT().getString());
            moParamsMap.put("sCfdiNoCertificadoSAT", tfd.getAttNoCertificadoSAT().getString());
            moParamsMap.put("sCfdiFechaTimbre", tfd.getAttFechaTimbrado().getString());
            moParamsMap.put("sCfdiRfcProvCertif", tfd.getAttRfcProvCertif().getString());
            moParamsMap.put("sCfdiLeyenda", tfd.getAttLeyenda().getString());
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
    
    private void actionPerformedViewPdf() throws Exception {
        if (moPdfFile != null && mjViewPdf.isEnabled()) {
            if (moDialogPdfViewer == null) {
                moDialogPdfViewer = new SDialogPdfViewer((SGuiClient) miClient, false);
            }

            moDialogPdfViewer.setPdf(createDocumentInfo(), moPdfFile);
            moDialogPdfViewer.setVisible(true);
        }
    }
      
    private void actionPerformedProcessCfd() throws Exception {
        SFormValidation validation = null;
        
        if (mfCfdiVersion == DCfdConsts.CFDI_VER_40) {
            validation = validateCfdi40();
        }
        else if (mfCfdiVersion == DCfdConsts.CFDI_VER_33) {
            validation = validateCfdi33();
        }
        else {
            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION + "\nVersión CFD: " + mfCfdiVersion + ".");
        }
        
        if (validation.getIsError()) {
            miClient.showMsgBoxWarning(validation.getMessage());
        }
    }
    
    private void actionPerformedClose() {
        moCfdiViewer.setVisible(false);
    }
    
    /*
     * Public methods.
     */
    
    /**
     * Recibe el XML de un CFDI y lo muestra en pantalla en un diálogo "flotante", accesible todo el tiempo.
     * @param xml XML del CFDI.
     * @throws Exception
     */
    public void renderCfdXml(final String xml) throws Exception {
        mbCreateProcessingButtons = false;
        
        mnDocumentType = 0;
        msCfdiXml = xml;
        moCfdiFile = null;
        moPdfFile = null;
        moDpsToLink = null;
        mnBizPartnerCategory = 0;
        
        showCfdiViewer();
    }
    
    /**
     * Recibe los archivos XML y PDF de un CFDI, los muestra en pantalla en un diálogo "modal", para procesarlo y contabilizarlo.
     * @param documentType GUI document type. Either SDataConstantsSys.TRNX_TP_DPS_DOC (invoice) or SDataConstantsSys.TRNX_TP_DPS_ADJ (credit note)
     * @param cfdiFile Archivo con el XML del CFDI.
     * @param pdfFile Archivo con el PDF del CFDI.
     * @param dpsToLink Documento a vincular, orden de compra o factura, relacionado con el CFDI. Puede ser <code>null</code>.
     * @param bizPartnerCategory
     * @return Documento nuevo recién creado a partir del CFDI.
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public SDataDps renderCfdAndCreateDps(final int documentType, final File cfdiFile, final File pdfFile, final SDataDps dpsToLink, final int bizPartnerCategory) throws Exception {
        mbCreateProcessingButtons = true;
        
        if (SLibUtils.belongsTo(documentType, new int[] { SDataConstantsSys.TRNX_TP_DPS_DOC, SDataConstantsSys.TRNX_TP_DPS_ADJ })) {
            mnDocumentType = documentType;
        }
        else {
            mnDocumentType = 0;
            throw new Exception("El tipo de documento " + documentType + " no está soportado.");
        }
        
        if (mnDocumentType != 0) {
            try {
                msCfdiXml = SXmlUtils.readXml(cfdiFile.getAbsolutePath());
            } 
            catch (Exception e) {
                msCfdiXml = "";
                throw new Exception("El XML no es válido:\n" + e);
            }

            if (!msCfdiXml.isEmpty()) {
                moCfdiFile = cfdiFile;
                moPdfFile = pdfFile;
                moDpsToLink = dpsToLink;
                mnBizPartnerCategory = bizPartnerCategory;

                showCfdiViewer();
            }
        }
        
        return moDpsRendered;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() instanceof javax.swing.JButton) {
                JButton button = (JButton) e.getSource();
                
                if (button == mjViewPdf) {
                    actionPerformedViewPdf();
                }
                else if (button == mjProcessCfd) {
                    actionPerformedProcessCfd();
                }
                else if (button == mjClose) {
                    actionPerformedClose();
                }
            }
        }
        catch (Exception ex) {
            SLibUtilities.renderException(this, ex);
        }
    }
}
