/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.SSwapUtils;
import erp.mod.cfg.swap.SSyncType;
import erp.mod.cfg.swap.form.SDialogPdfViewer;
import erp.mod.cfg.swap.form.SDocumentUtils;
import erp.mod.cfg.swap.utils.SDataRejectResource;
import erp.mod.cfg.swap.utils.SExportDataAuthActor;
import erp.mod.cfg.swap.utils.SExportUtils;
import erp.mod.cfg.swap.utils.SResponses;
import erp.mod.cfg.swap.utils.SServicesUtils;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.fin.db.SDbPayment;
import erp.mod.fin.db.SDbPaymentEntry;
import erp.mod.fin.db.SDbPaymentFile;
import erp.mod.fin.form.SDialogPaymentChangeStatus;
import erp.mod.hrs.utils.SDocUtils;
import erp.mod.view.SViewFilter;
import erp.mtrn.view.SViewDps;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Isabel Servín, Sergio Flores, Adrián Avilés
 */
public class SViewPayment extends SGridPaneView implements ActionListener, ItemListener {
    
    private static final String SUGGESTION_SPEED_UP = "SUGERENCIA: Si urge acelerar la aplicación de esta modificación, haga clic en el botón ";
    
    private JLabel jlDate;
    private JRadioButton jrbDateApp;
    private JRadioButton jrbDateReq;
    private JRadioButton jrbDateSched;
    private ButtonGroup bgDate;
    
    private boolean mbAppliesFilterDatePeriod;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SViewFilter moFilterFuncArea;
    private SViewFilter moFilterCurrency;
    private SDialogPdfViewer moDialogPdfViewer;
    
    private boolean mbAppliesDateRequired;
    
    private JButton jbDocShowCfdiXml;
    private JButton jbDocGetCfdiXml;
    private JButton jbDocShowDocPdf;
    private JButton jbDocGetDocPdf;
    
    private JButton jbAuthWebLoadSupportFiles;
    private JButton jbAuthWebStartAuth;
    private JButton jbAuthWebViewAuthLog;
    private JButton jbAuthWebDownloadSupportFiles;
    private JButton jbAuthWebClearSupportFiles;
    private JButton jbAuthWebAnnullAuth;
    
    private JButton jbPaymentReschedule;
    private JButton jbPaymentChangeCurrency;
    private JButton jbPaymentMarkAsPaid;
    private JButton jbPaymentBlock;
    private JButton jbPaymentUnblock;
    private JButton jbPaymentCancel;
    
    private JButton jbExportDataToSwapServices;
    
    private JFileChooser moAuthWebFileChooser;
    private SDialogPaymentChangeStatus moDialogPaymentChangeStatus;

    public SViewPayment(SGuiClient client, int subType, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.FIN_PAY, subType, title);
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        if (mnGridSubtype == SLibConsts.UNDEFINED) {
            // payments management:
            setRowButtonsEnabled(true, false, true, false, true);
            jtbFilterDeleted.setEnabled(true);
        }
        else {
            // payments monitoring:
            setRowButtonsEnabled(false);
            jtbFilterDeleted.setEnabled(false);
        }
        
        
        jlDate = new JLabel("Fecha:");
        jrbDateApp = new JRadioButton("Solicitud");
        jrbDateApp.setToolTipText("Filtrar por la fecha de la solicitud de pago");

        jrbDateReq = new JRadioButton("Requerida");
        jrbDateReq.setToolTipText("Filtrar por la fecha requerida de pago");

        jrbDateSched = new JRadioButton("Programada");
        jrbDateSched.setToolTipText("Filtrar por la fecha programada de pago");

        bgDate = new ButtonGroup();
        bgDate.add(jrbDateApp);
        bgDate.add(jrbDateReq);
        bgDate.add(jrbDateSched);

        bgDate.setSelected(jrbDateApp.getModel(), true);

        jrbDateApp.addItemListener(this);
        jrbDateReq.addItemListener(this);
        jrbDateSched.addItemListener(this);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        moFilterFuncArea = new SViewFilter(miClient, this, SModConsts.CFGU_FUNC);
        moFilterFuncArea.initFilter(null);
        moFilterCurrency = new SViewFilter(miClient, this, SModConsts.CFGU_CUR);
        moFilterCurrency.initFilter(null);
        
        jbDocShowCfdiXml = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon-file-xml.png")),
                "Ver XML del CFDI del documento", this);
        jbDocGetCfdiXml = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML),
                "Obtener XML del CFDI del documento", this);
        jbDocShowDocPdf = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon-file-pdf.png")),
                "Ver PDF del documento", this);
        jbDocGetDocPdf = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_DOC_TYPE),
                "Obtener PDF del documento", this);
        
        jbAuthWebLoadSupportFiles = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_add_ora.gif")),
                "Cargar archivos de soporte a la solicitud", this);
        jbAuthWebStartAuth = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_up_ora.gif")),
                "Iniciar autorización de la solicitud en app web", this);
        jbAuthWebViewAuthLog = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_upl_notes_ora.gif")),
                "Ver estatus de autorización de la solicitud en app web", this);
        jbAuthWebDownloadSupportFiles = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_down_ora.gif")),
                "Descargar archivos de soporte de la solicitud", this);
        jbAuthWebClearSupportFiles = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_rem_ora.gif")),
                "Eliminar archivos de soporte de la solicitud", this);
        jbAuthWebAnnullAuth = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_down_red.jpg")),
                "Anular autorización de la solicitud en app web", this);
        
        jbPaymentReschedule = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/gui_cal.gif")),
                "Cambiar fecha requerida o programada", this);
        jbPaymentChangeCurrency = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_money.gif")),
                "Cambiar moneda de pago", this);
        jbPaymentMarkAsPaid = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")),
                "Marcar como operado", this);
        jbPaymentBlock = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_lock.gif")),
                "Bloquear solicitud", this);
        jbPaymentUnblock = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_unlock.gif")),
                "Desbloquear solicitud", this);
        jbPaymentCancel = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_annul.gif")),
                "Cancelar solicitud", this);
        
        jbExportDataToSwapServices = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_up_ind.gif")),
                "Exportar registros '" + SSwapUtils.translateSyncType(SSyncType.PUR_PAYMENT, SLibConsts.LAN_ISO639_ES) + "' a " + SSwapConsts.SWAP_SERVICES, this);
        
        if (SLibUtils.belongsTo(mnGridSubtype, new int[] { SLibConsts.UNDEFINED, SModSysConsts.FINS_ST_PAY_REJC, SModSysConsts.FINS_ST_PAY_SCHED, SModSysConsts.FINS_ST_PAY_CANC })) {
            mbAppliesFilterDatePeriod = true;
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
//            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jlDate);
//            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jrbDateApp);
//            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jrbDateReq);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jrbDateApp);
getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jrbDateReq);
getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jrbDateSched);
        }
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFuncArea);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterCurrency);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbDocShowCfdiXml);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbDocGetCfdiXml);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbDocShowDocPdf);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbDocGetDocPdf);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JLabel());
        
        switch (mnGridSubtype) {
            case SLibConsts.UNDEFINED: // payments management
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebLoadSupportFiles);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebStartAuth);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebViewAuthLog);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebDownloadSupportFiles);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebClearSupportFiles);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebAnnullAuth);
                
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JLabel());
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExportDataToSwapServices);
                break;
                
            case SModSysConsts.FINS_ST_PAY_IN_AUTH:
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebViewAuthLog);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebAnnullAuth);
                break;
                
            case SModSysConsts.FINS_ST_PAY_REJC:
                jbPaymentReschedule.setToolTipText("Cambiar fecha requerida");

                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebViewAuthLog);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentReschedule);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentCancel);
                
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JLabel());
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExportDataToSwapServices);
                break;
                
            case SModSysConsts.FINS_ST_PAY_SCHED:
                jbPaymentReschedule.setToolTipText("Cambiar fecha programada");

                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebViewAuthLog);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentReschedule);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentChangeCurrency);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentMarkAsPaid);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentBlock);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentCancel);
                
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JLabel());
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExportDataToSwapServices);
                break;
                
            case SModSysConsts.FINS_ST_PAY_BLOC:
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentUnblock);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentCancel);
                
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JLabel());
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExportDataToSwapServices);
                break;
                
            case SModSysConsts.FINS_ST_PAY_CANC:
                break;
                
            default:
                throw new UnsupportedOperationException(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
    }
    
    private int[] getDocKeyOfSelectedPayment() throws Exception {
        int[] docKey = null;
        SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
        
        if (gridRow != null) {
            SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());
            SDbPaymentEntry singleEntry = payment.getSingleEntry();
            
            if (singleEntry.getEntryType().equals(SDbPaymentEntry.TYPE_PAYMENT)) {
                docKey = singleEntry.getDocKey();
            }
            else {
                throw new Exception("La solicitud de pago '" + payment.getFolio()+ "' debe ser de tipo '" + SDbPaymentEntry.DESC_ENTRY_TYPE_PAYMENT+ "'.");
            }
        }
        
        return docKey;
    }
    
    private void actionDocShowCfdiXml() {
        if(jbDocShowCfdiXml.isEnabled()) {
            if (isRowDataSelected()) {
                try {
                    SViewDps.showCfdiXml((SClientInterface) miClient, getDocKeyOfSelectedPayment());
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionDocGetCfdiXml() {
        if (jbDocGetCfdiXml.isEnabled()) {
            if (isRowDataSelected()) {
                try {
                    SViewDps.getCfdiXml((SClientInterface) miClient, getDocKeyOfSelectedPayment());
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionDocShowDocPdf() {
        if (jbDocShowDocPdf.isEnabled()) {
            if (isRowDataSelected()) {
                try {
                    if (moDialogPdfViewer == null) {
                        moDialogPdfViewer = new SDialogPdfViewer((SGuiClient) miClient, true);
                    }
                    
                    SViewDps.showDocPdf((SClientInterface) miClient, getDocKeyOfSelectedPayment(), moDialogPdfViewer);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionDocGetDocPdf() {
        if (jbDocGetDocPdf.isEnabled()) {
            if (isRowDataSelected()) {
                try {
                    SViewDps.getDocPdf((SClientInterface) miClient, getDocKeyOfSelectedPayment());
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionAuthWebLoadSupportFiles() {
        if (jbAuthWebLoadSupportFiles.isEnabled()) {
            if (isRowDataNonSystemUpdatableSelected()) {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    
                    SGuiParams params = new SGuiParams();
                    params.setKey(gridRow.getRowPrimaryKey());
                    miClient.getSession().getModule(SModConsts.MOD_FIN_N).showForm(SModConsts.FIN_PAY_FILE, SLibConsts.UNDEFINED, params);
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionAuthWebStartAuth() {
        if (jbAuthWebStartAuth.isEnabled()) {
            if (isRowDataNonSystemUpdatableSelected()) {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    
                    if (SAuthorizationUtils.sendAuthornPaymentsAppWeb((SClientInterface) miClient, gridRow.getRowPrimaryKey())) {
                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionAuthWebViewAuthLog() {
        if (jbAuthWebViewAuthLog.isEnabled()) {
            if (isRowDataSelected()) {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else {
                        SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());
                        int status = payment.getFkStatusPaymentId(); // convenience variable

                        if (status != SModSysConsts.FINS_ST_PAY_NEW) {
                            SServicesUtils.AuthFlowStatus authFlowStatus = SServicesUtils.getAuthFlowStatus(miClient.getSession(), 
                                                                                    SSwapConsts.RESOURCE_TYPE_PUR_PAYMENT, 
                                                                                    payment.getPkPaymentId() + "");
                            miClient.showMsgBoxInformation(authFlowStatus.toString());
                        }
                        else {
                            miClient.showMsgBoxInformation("El estatus de solicitud de pago '" + payment.getFolio()+ "' debe ser distinto de '" + SDbPayment.ST_IN_AUTH + "' para poderse ver su estatus de autorización.");
                        }
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionAuthWebDownloadSupportFiles() {
        if (jbAuthWebDownloadSupportFiles.isEnabled()) {
            if (isRowDataNonSystemUpdatableSelected()) {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());

                    if (payment.getFiles().isEmpty()) {
                        miClient.showMsgBoxInformation("La solicitud de pago '" + payment.getFolio() + "' no tiene archivos de soporte.");
                    }
                    else {
                        if (moAuthWebFileChooser == null) {
                            moAuthWebFileChooser = new JFileChooser();
                            moAuthWebFileChooser.setAcceptAllFileFilterUsed(false);
                            moAuthWebFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                            moAuthWebFileChooser.setDialogTitle("Seleccionar directorio para descargar archivos...");
                        }

                        if (moAuthWebFileChooser.showSaveDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                            int files = 0;

                            for (SDbPaymentFile file : payment.getFiles()) {
                                if (!file.getFilevaultId().isEmpty()) {
                                    boolean returnPath = false;
                                    SDocUtils.downloadFile(miClient.getSession(), SDocUtils.BUCKET_DOC_DPS_SUPPLIER, file.getFilevaultId(), moAuthWebFileChooser.getSelectedFile(), returnPath);
                                    files++;
                                }
                            }

                            miClient.showMsgBoxInformation("Se " + (files == 1 ? "descargó 1 archivo" : "descargaron " + files + " archivos") + " de soporte de la solicitud de pago seleccionada.");
                        }
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionAuthWebClearSupportFiles() {
        if (jbAuthWebClearSupportFiles.isEnabled()) {
            if (isRowDataNonSystemUpdatableSelected()) {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());

                    if (payment.getFiles().isEmpty()) {
                        miClient.showMsgBoxInformation("La solicitud de pago '" + payment.getFolio() + "' no tiene archivos de soporte.");
                    }
                    else {
                        String confirm = "¿Está seguro que desea eliminar todos los archivos de soporte de la solicitud de pago '" + payment.getFolio() + "'?";

                        if (miClient.showMsgBoxConfirm(confirm) == JOptionPane.YES_OPTION) {
                            int cant = payment.getFiles().size();
                            payment.deleteFiles(miClient.getSession());

                            miClient.showMsgBoxInformation("Se " + (cant == 1 ? "eliminó 1 archivo" : "eliminaron " + cant + " archivos") + " de soporte.");
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionAuthWebAnnullAuth() {
        if (jbAuthWebAnnullAuth.isEnabled()) {
            if (isRowDataUpdatableSelected()) {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());
                    int status = payment.getFkStatusPaymentId(); // convenience variable

                    if (status == SModSysConsts.FINS_ST_PAY_IN_AUTH) {
                        String confirm = "La autorización de la solicitud de pago '" + payment.getFolio() + "' será anulada, y esta acción no se puede revertir.\n"
                                + "La solicitud de pago se eliminará de manera automática del " + SSwapConsts.PURCHASE_PORTAL + ".\n"
                                + "IMPORTATE: La solicitud de pago quedará con el estatus '" + SDbPayment.ST_REJC + "'.\n"
                                + SGuiConsts.MSG_CNF_CONT;

                        if (miClient.showMsgBoxConfirm(confirm) == JOptionPane.OK_OPTION) {
                            SServicesUtils.RejectData rejectData = SServicesUtils.askForRejectData(miClient.getSession());

                            if (rejectData != null) {
                                confirm = "Se anulará la autorización de la solicitud de pago '" + payment.getFolio() + "',\n"
                                        + "por el usuario: " + rejectData.User + ",\n"
                                        + "con los siguientes comentarios:\n"
                                        + "\"" + rejectData.Notes + "\"\n"
                                        + SGuiConsts.MSG_CNF_CONT;

                                if (miClient.showMsgBoxConfirm(confirm) == JOptionPane.YES_OPTION) {
                                    SDataRejectResource data = new SDataRejectResource();

                                    data.id_external_system = SSwapConsts.SIIE_EXT_SYS_ID;
                                    data.id_company = miClient.getSession().getConfigCompany().getCompanyId();
                                    data.id_resource_type = SSwapConsts.RESOURCE_TYPE_PUR_PAYMENT;
                                    data.siie_resource_id = "" + payment.getPkPaymentId();
                                    data.id_actor_type = SExportDataAuthActor.ACTOR_TYPE_USER;
                                    data.external_user_id = rejectData.UserId;
                                    data.notes = rejectData.Notes;

                                    SServicesUtils.requestCancelFlow(miClient.getSession(), SModConsts.FIN_PAY, data);
                                    miClient.getSession().notifySuscriptors(mnGridType);

                                    miClient.showMsgBoxInformation("La autorización de la solicitud de pago '" + payment.getFolio() + "' acaba de ser anulada.");
                                }
                            }
                        }
                    }
                    else {
                        String statusDescrip = (String) miClient.getSession().readField(SModConsts.FINS_ST_PAY, new int[] { status }, SDbRegistry.FIELD_NAME);
                        miClient.showMsgBoxInformation("El estatus de la solicitud de pago '" + payment.getFolio()+ "' es '" + statusDescrip + "', solo si es '" + SDbPayment.ST_IN_AUTH + "' se puede anular su autorización.");
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionPaymentReschedule() {
        if (jbPaymentReschedule.isEnabled()) {
            if (isRowDataUpdatableSelected()) {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());
                    int status = payment.getFkStatusPaymentId(); // convenience variable

                    if (status == SModSysConsts.FINS_ST_PAY_REJC || status == SModSysConsts.FINS_ST_PAY_SCHED) {
                        int formCase = 0;

                        switch (status) {
                            case SModSysConsts.FINS_ST_PAY_REJC:
                                formCase = SDialogPaymentChangeStatus.CASE_REACTIVATE;
                                break;
                            case SModSysConsts.FINS_ST_PAY_SCHED:
                                formCase = SDialogPaymentChangeStatus.CASE_RESCHEDULE;
                                break;
                            default:
                                // nothing
                        }

                        if (moDialogPaymentChangeStatus == null) {
                            moDialogPaymentChangeStatus = new SDialogPaymentChangeStatus(miClient, "");
                        }

                        moDialogPaymentChangeStatus.setFormCase(formCase);
                        moDialogPaymentChangeStatus.setRegistry(payment);
                        moDialogPaymentChangeStatus.setVisible(true);

                        if (moDialogPaymentChangeStatus.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                            int newCurrencyId;

                            payment.setAuxReloadEntries(false);

                            switch (status) {
                                case SModSysConsts.FINS_ST_PAY_REJC:
                                    // validate that currency can be changed, if necessary:
                                    newCurrencyId = (int) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_CURRENCY);
                                    if (newCurrencyId != payment.getFkCurrencyId()) {
                                        payment.changePaymentCurrency(miClient.getSession(), newCurrencyId);
                                    }

                                    // comple reschedule:
                                    payment.setFkStatusPaymentId(SModSysConsts.FINS_ST_PAY_NEW);
                                    payment.setDateRequired((Date) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_DATE));
                                    payment.setPriority((int) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_PRIORITY));
                                    payment.setNotes((String) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_NOTES));
                                    payment.setNotesAuthorization((String) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_NOTES_AUTH));

                                    if (payment.isSystem()) {
                                        miClient.showMsgBoxInformation("La solicitud de pago '" + payment.getFolio() + "' se enviará nuevamente a autorizar de manera automática al " + SSwapConsts.PURCHASE_PORTAL + ".\n"
                                                + SUGGESTION_SPEED_UP + "'" + jbExportDataToSwapServices.getToolTipText() + "'.");
                                    }
                                    else {
                                        miClient.showMsgBoxInformation("La solicitud de pago '" + payment.getFolio() + "' requiere enviarse nuevamente a autorizar de manera manual.\n"
                                                + "Favor de hacerlo en la vista 'Solicitudes de pago'.");
                                    }
                                    break;

                                case SModSysConsts.FINS_ST_PAY_SCHED:
                                    // validate that currency can be changed, if necessary:
                                    newCurrencyId = (int) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_CURRENCY);
                                    if (newCurrencyId != payment.getFkCurrencyId()) {
                                        payment.changePaymentCurrency(miClient.getSession(), newCurrencyId);
                                    }

                                    // comple reschedule:
                                    payment.setFkStatusPaymentId(SModSysConsts.FINS_ST_PAY_SCHED_P);
                                    payment.setDateSchedule_n((Date) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_DATE));
                                    payment.setRescheduled(true);
                                    payment.setFkUserRescheduleId(miClient.getSession().getUser().getPkUserId());
                                    payment.setNotes((String) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_NOTES));

                                    miClient.showMsgBoxInformation("La solicitud de pago '" + payment.getFolio() + "' se actualizará de manera automática en el " + SSwapConsts.PURCHASE_PORTAL + ".\n"
                                            + SUGGESTION_SPEED_UP + "'" + jbExportDataToSwapServices.getToolTipText() + "'.");
                                    break;

                                default:
                                    // nothing
                            }

                            payment.save(miClient.getSession());
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                    else {
                        switch (status) {
                            case SModSysConsts.FINS_ST_PAY_REJC_P:
                                miClient.showMsgBoxInformation("La solicitud de pago '" + payment.getFolio() + "' está en proceso de quedar rechazada.\n"
                                        + "Intente más tarde de favor.");
                                break;
                            case SModSysConsts.FINS_ST_PAY_SCHED_P:
                                miClient.showMsgBoxInformation("La solicitud de ppago '" + payment.getFolio() + "' está en proceso de quedar autorizada.\n"
                                        + "Intente más tarde de favor.");
                                break;
                            default:
                                throw new UnsupportedOperationException(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionPaymentChangeCurrency() {
        if (jbPaymentChangeCurrency.isEnabled()) {
            if (isRowDataUpdatableSelected()) {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());
                    int status = payment.getFkStatusPaymentId(); // convenience variable

                    if (status == SModSysConsts.FINS_ST_PAY_SCHED) {
                        if (moDialogPaymentChangeStatus == null) {
                            moDialogPaymentChangeStatus = new SDialogPaymentChangeStatus(miClient, "");
                        }

                        moDialogPaymentChangeStatus.setFormCase(SDialogPaymentChangeStatus.CASE_CHANGE_CURRENCY);
                        moDialogPaymentChangeStatus.setRegistry(payment);
                        moDialogPaymentChangeStatus.setVisible(true);

                        if (moDialogPaymentChangeStatus.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                            payment.setAuxReloadEntries(false);

                            payment.changePaymentCurrency(miClient.getSession(), (int) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_CURRENCY));

                            payment.save(miClient.getSession());
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                    else {
                        switch (status) {
                            case SModSysConsts.FINS_ST_PAY_SCHED_P:
                                miClient.showMsgBoxInformation("La solicitud de ppago '" + payment.getFolio() + "' está en proceso de quedar autorizada.\n"
                                        + "Intente más tarde de favor.");
                                break;
                            default:
                                throw new UnsupportedOperationException(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionPaymentMarkAsPaid() {
        if (jbPaymentMarkAsPaid.isEnabled()) {
            if (isRowDataUpdatableSelected()) {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());
                    int status = payment.getFkStatusPaymentId(); // convenience variable

                    if (status == SModSysConsts.FINS_ST_PAY_SCHED) {
                        if (moDialogPaymentChangeStatus == null) {
                            moDialogPaymentChangeStatus = new SDialogPaymentChangeStatus(miClient, "");
                        }

                        moDialogPaymentChangeStatus.setFormCase(SDialogPaymentChangeStatus.CASE_MARK_AS_PAID);
                        moDialogPaymentChangeStatus.setRegistry(payment);
                        moDialogPaymentChangeStatus.setVisible(true);

                        if (moDialogPaymentChangeStatus.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                            Date date = (Date) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_DATE);
                            double exchangeRate = SDocumentUtils.getExchangeRate(miClient.getSession(), payment.getFkCurrencyId(), date);
                            double amount = (double) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_PAYMENT);
                            int[] paymentBankKey = (int[]) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_PAYMENT_BANK);
                            int[] benefBankKey = (int[]) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_BENEFIT_BANK);
                            SDbPaymentEntry singleEntry = payment.getSingleEntry();

                            payment.setAuxReloadEntries(false);

                            payment.setFkStatusPaymentId(SModSysConsts.FINS_ST_PAY_EXEC_P);
                            payment.setDateExecution_n(date);
                            payment.setExecutedManually(true);
                            payment.setFkUserExecutiondId(miClient.getSession().getUser().getPkUserId());
                            
                            if (paymentBankKey != null) {
                                payment.setFkPayerCashBizPartnerBranchId_n(paymentBankKey[0]);
                                payment.setFkPayerCashAccountingCashId_n(paymentBankKey[1]);    
                            }
                            else {
                                payment.setFkPayerCashBizPartnerBranchId_n(0);
                                payment.setFkPayerCashAccountingCashId_n(0);
                            }
                            
                            if (benefBankKey != null) {
                                payment.setFkBeneficiaryBankBizParterBranchId_n(benefBankKey[0]);
                                payment.setFkBeneficiaryBankAccountCashId_n(benefBankKey[1]);
                            }
                            else {
                                payment.setFkBeneficiaryBankBizParterBranchId_n(0);
                                payment.setFkBeneficiaryBankAccountCashId_n(0);
                            }

                            payment.processPaymentAtExecution(miClient.getSession(), amount, exchangeRate, singleEntry.getDocInstallment(), singleEntry.getDocBalancePreviousCy());

                            miClient.showMsgBoxInformation("La solicitud de pago '" + payment.getFolio() + "' se actualizará de manera automática en el " + SSwapConsts.PURCHASE_PORTAL + ".\n"
                                    + SUGGESTION_SPEED_UP + "'" + jbExportDataToSwapServices.getToolTipText() + "'.");

                            payment.save(miClient.getSession());
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                    else {
                        switch (status) {
                            case SModSysConsts.FINS_ST_PAY_SCHED_P:
                                miClient.showMsgBoxInformation("La solicitud de pago '" + payment.getFolio() + "' está en proceso de quedar autorizada.\n"
                                        + "Intente más tarde de favor.");
                                break;
                            default:
                                throw new UnsupportedOperationException(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionPaymentBlock() {
        if (jbPaymentBlock.isEnabled()) {
            if (isRowDataUpdatableSelected()) {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());
                    int status = payment.getFkStatusPaymentId(); // convenience variable

                    if (status == SModSysConsts.FINS_ST_PAY_SCHED) {
                        String confirm = "La solicitud de pago '" + payment.getFolio()+ "' será bloqueada.\n"
                                + "Podrá desbloquearla en la vista 'Solicitudes de pago bloqueadas'.\n"
                                + SGuiConsts.MSG_CNF_CONT;

                        if (miClient.showMsgBoxConfirm(confirm) == JOptionPane.YES_OPTION) {
                            payment.updatePaymentStatus(miClient.getSession(), SModSysConsts.FINS_ST_PAY_BLOC);
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                    else {
                        String statusDescrip = (String) miClient.getSession().readField(SModConsts.FINS_ST_PAY, new int[] { status }, SDbRegistry.FIELD_NAME);
                        miClient.showMsgBoxInformation("El estatus de la solicitud de pago '" + payment.getFolio()+ "' es '" + statusDescrip + "', pero debe ser '" + SDbPayment.ST_SCHED + "' para poderse bloquear.");
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionPaymentUnblock() {
        if (jbPaymentUnblock.isEnabled()) {
            if (isRowDataUpdatableSelected()) {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());
                    int status = payment.getFkStatusPaymentId(); // convenience variable

                    if (status == SModSysConsts.FINS_ST_PAY_BLOC) {
                        String confirm = "La solicitud de pago '" + payment.getFolio() + "' será desbloqueada, y quedará nuevamente con estatus '" + SDbPayment.ST_SCHED + "'.\n"
                                + SGuiConsts.MSG_CNF_CONT;

                        if (miClient.showMsgBoxConfirm(confirm) == JOptionPane.OK_OPTION) {
                            payment.updatePaymentStatus(miClient.getSession(), SModSysConsts.FINS_ST_PAY_SCHED);
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                    else {
                        String statusDescrip = (String) miClient.getSession().readField(SModConsts.FINS_ST_PAY, new int[] { status }, SDbRegistry.FIELD_NAME);
                        miClient.showMsgBoxInformation("El estatus de la solicitud de pago '" + payment.getFolio()+ "' es '" + statusDescrip + "', pero debe ser '" + SDbPayment.ST_BLOCK + "' para poderse desbloquear.");
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionPaymentCancel() {
        if (jbPaymentCancel.isEnabled()) {
            if (isRowDataUpdatableSelected()) {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());
                    int status = payment.getFkStatusPaymentId(); // convenience variable

                    if (status == SModSysConsts.FINS_ST_PAY_REJC || status == SModSysConsts.FINS_ST_PAY_SCHED || status == SModSysConsts.FINS_ST_PAY_BLOC) {
                        String confirm = "La solicitud de pago '" + payment.getFolio()+ "' será cancelada, y esta acción no se puede revertir.\n"
                                + "La solicitud de pago se eliminará de manera automática del " + SSwapConsts.PURCHASE_PORTAL + ".\n"
                                + SUGGESTION_SPEED_UP + "'" + jbExportDataToSwapServices.getToolTipText() + "'.\n"
                                + SGuiConsts.MSG_CNF_CONT;

                        if (miClient.showMsgBoxConfirm(confirm) == JOptionPane.OK_OPTION) {
                            payment.updatePaymentStatus(miClient.getSession(), SModSysConsts.FINS_ST_PAY_CANC_P);
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                    else {
                        String statusDescrip = (String) miClient.getSession().readField(SModConsts.FINS_ST_PAY, new int[] { status }, SDbRegistry.FIELD_NAME);
                        miClient.showMsgBoxInformation("El estatus de la solicitud de pago '" + payment.getFolio()+ "' es '" + statusDescrip + "', pero debe ser '" + SDbPayment.ST_REJC + "', '" + SDbPayment.ST_SCHED + "' o '" + SDbPayment.ST_BLOCK + "' para poderse cancelar.");
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionExportDataToSwapServices() {
        if (jbExportDataToSwapServices.isEnabled()) {
            try {
                miClient.getFrame().getRootPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                SResponses responses = SExportUtils.exportData(miClient.getSession(), SSyncType.PUR_PAYMENT, true, SExportUtils.EXPORT_MODE_CONFIRM);
                SExportUtils.processResponses(miClient.getSession(), responses, 0, 0);
                miClient.getSession().notifySuscriptors(SModConsts.FIN_PAY);
            }
            catch (Exception e) {
                SLibUtilities.printOutException(this, e);
            }
            finally {
                miClient.getFrame().getRootPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        moPaneSettings.setSystemApplying(true);

        if (mnGridSubtype == SLibConsts.UNDEFINED) {
            filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
            if ((Boolean) filter) {
                sql += (sql.isEmpty() ? "" : "AND ") + "NOT v.b_del ";
            }
        }
        else {
            sql += (sql.isEmpty() ? "" : "AND ") + "NOT v.b_del ";
        }

        if (mbAppliesFilterDatePeriod) {
            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();

            if (jrbDateApp.isSelected()) {
                sql += (sql.isEmpty() ? "" : "AND ") +
                       SGridUtils.getSqlFilterDate("v.dt_app", (SGuiDate) filter);
            }
            else if (jrbDateReq.isSelected()) {
                sql += (sql.isEmpty() ? "" : "AND ") +
                       SGridUtils.getSqlFilterDate("v.dt_req", (SGuiDate) filter);
            }
            else if (jrbDateSched.isSelected()) {
                sql += (sql.isEmpty() ? "" : "AND ") +
                       SGridUtils.getSqlFilterDate(
                           "CASE WHEN v.dt_sched_n IS NOT NULL THEN v.dt_sched_n ELSE v.dt_req END",
                           (SGuiDate) filter);
            }
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.CFGU_FUNC)).getValue();
        if (filter != null && !((String) filter).isEmpty()) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_func IN (" + filter + ") ";
        }

        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.CFGU_CUR)).getValue();
        if (filter != null && !((String) filter).isEmpty()) {
            sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_cur IN (" + filter + ") ";
        }
        setShowSums(filter != null && ((String) filter).split(",").length == 1);

        switch (mnGridSubtype) {
            case SLibConsts.UNDEFINED:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                + SModSysConsts.FINS_ST_PAY_NEW + ", "
                + SModSysConsts.FINS_ST_PAY_IN_AUTH + ", "
                + SModSysConsts.FINS_ST_PAY_REJC + ", "
                + SModSysConsts.FINS_ST_PAY_REJC_P + ", "
                + SModSysConsts.FINS_ST_PAY_SCHED + ", "
                + SModSysConsts.FINS_ST_PAY_SCHED_P + ", "
                + SModSysConsts.FINS_ST_PAY_SUBR + ", "
                + SModSysConsts.FINS_ST_PAY_SUBR_P + ", "
                + SModSysConsts.FINS_ST_PAY_BLOC + ", "
                + SModSysConsts.FINS_ST_PAY_BLOC_P + ", "
                + SModSysConsts.FINS_ST_PAY_CANC + ", "
                + SModSysConsts.FINS_ST_PAY_CANC_P + ") ";
                break;

            case SModSysConsts.FINS_ST_PAY_IN_AUTH:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_IN_AUTH + ") ";
                break;

            case SModSysConsts.FINS_ST_PAY_REJC:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_REJC + ", "
                        + SModSysConsts.FINS_ST_PAY_REJC_P + ") ";
                break;

            case SModSysConsts.FINS_ST_PAY_SCHED:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_SCHED + ", "
                        + SModSysConsts.FINS_ST_PAY_SCHED_P + ") ";
                break;

            case SModSysConsts.FINS_ST_PAY_BLOC:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_BLOC + ", "
                        + SModSysConsts.FINS_ST_PAY_BLOC_P + ") ";
                break;

            case SModSysConsts.FINS_ST_PAY_CANC:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_CANC + ", "
                        + SModSysConsts.FINS_ST_PAY_CANC_P + ") ";
                break;

            default:
        }

        String sqlDateField = "";

        if (jrbDateApp.isSelected()) {
            sqlDateField = "v.dt_app";
        }
        else if (jrbDateReq.isSelected()) {
            sqlDateField = "v.dt_req";
        }
        else {
            sqlDateField = "v.dt_sched_n";
        }

        String sqlOrders = "SELECT GROUP_CONCAT(DISTINCT CONCAT(ord.num_ser, IF(ord.num_ser = '', '', '-'), ord.num) ORDER BY ord.num_ser, ord.num SEPARATOR '; ' ) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " AS dds "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS ord ON ord.id_year = dds.id_src_year AND ord.id_doc = dds.id_src_doc "
                + "WHERE NOT ord.b_del AND ord.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " AND dds.id_des_year = d.id_year AND dds.id_des_doc = d.id_doc";

        msSql = "SELECT "
                + "v.id_pay AS " + SDbConsts.FIELD_ID + "1, "
                + "CONCAT(v.ser, IF(v.ser = '', '', '-'), v.num) AS " + SDbConsts.FIELD_CODE + ", "
                + "b.bp AS " + SDbConsts.FIELD_NAME + ", "
                + sqlDateField + " AS " + SDbConsts.FIELD_DATE + ", "   // <-- SIEMPRE DATE
                + "v.pay_exc_rate_app, "
                + "v.dt_req, "
                + (jrbDateSched.isSelected() ? "CASE WHEN v.dt_sched_n IS NOT NULL THEN DATE_FORMAT(v.dt_sched_n, '%d/%m/%Y') " 
                + "ELSE CONCAT(DATE_FORMAT(v.dt_req, '%d/%m/%Y'), '* SIN AUTORIZAR') END " : "'' " )
                + "AS dt_sched_n, "
                + "v.b_resched, "
                + "v.dt_exec_n, "
                + "v.b_exec_man, "
                + "v.nts, "
                + "v.nts_auth, "
                + "v.nts_auth_flow, "
                + "c.cur, "
                + "c.cur_key, "
                + "f.name AS _func, "
                + "fs.name AS _func_sub, "
                + "sp.name AS _status, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "IF((SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.FIN_PAY_FILE) + " AS f "
                + " WHERE v.id_pay = f.id_pay) = 0, " + SGridConsts.ICON_NULL + ", " + SGridConsts.ICON_FOLDER + ") AS _ico_files, "
                + "IF(fk_st_pay >= " + SModSysConsts.FINS_ST_PAY_REJC_P + ", " + SGridConsts.ICON_WAIT + ", " + SGridConsts.ICON_NULL + ") AS _ico_proc, "
                + "IF(ve.ety_tp = '" + SDbPaymentEntry.TYPE_ADVANCE + "' , '" + SDbPaymentEntry.DESC_ENTRY_TYPE_ADVANCE + "', '" + SDbPaymentEntry.DESC_ENTRY_TYPE_PAYMENT + "') AS _ety_tp, "
                + "IF(v.priority = 1 , '" + SDbPayment.DESC_PRIORITY_URGENT + "', '" + SDbPayment.DESC_PRIORITY_NORMAL + "') AS _priority, "
                + "ve.ety_pay_app_cur, "
                + "ve.ety_pay_app, "
                + "ve.conv_rate_app, "
                + "ve.des_pay_app_ety_cur, "
                + "ve.install, "
                + "ve.doc_bal_prev_app_cur, "
                + "ve.doc_bal_unpd_app_cur_r, "
                + "ce.cur_key, "
                + "CONCAT(d.num_ser, IF(d.num_ser = '', '', '-'), d.num) AS _dps, "
                + "v.ts_usr_sched, "
                + "v.ts_usr_resched, "
                + "v.ts_usr_exec, "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
                + "us.usr AS _usr_sched, "
                + "urs.usr AS _usr_resched, "
                + "ue.usr AS _usr_exec, "
                + "(" + sqlOrders + ") AS _orders, "
                + "COALESCE(nat.dps_nat, 'S/DOC') AS nat "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_PAY) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_PAY_ETY) + " AS ve ON "
                + "v.id_pay = ve.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON "
                + "v.fk_ben = b.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON "
                + "v.fk_cur = c.id_cur "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS f ON "
                + "v.fk_func = f.id_func "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC_SUB) + " AS fs ON "
                + "v.fk_func_sub = fs.id_func_sub "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_ST_PAY) + " AS sp ON "
                + "v.fk_st_pay = sp.id_st_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS us ON "
                + "v.fk_usr_sched = us.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS urs ON "
                + "v.fk_usr_resched = urs.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ue ON "
                + "v.fk_usr_exec = ue.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS ce ON "
                + "ve.fk_ety_cur = ce.id_cur "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON "
                + "ve.fk_doc_year_n = d.id_year AND ve.fk_doc_doc_n = d.id_doc "
                + "LEFT JOIN erp.TRNU_DPS_NAT AS nat ON d.fid_dps_nat = nat.id_dps_nat "        + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.ser, LPAD(v.num, 9, '0'), "
                + (jrbDateApp.isSelected() ? "v.dt_app" : jrbDateReq.isSelected() ? "v.dt_req" : "CASE WHEN v.dt_sched_n IS NOT NULL THEN v.dt_sched_n ELSE v.dt_req END")
                + ", b.bp ";

    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_CODE, "Folio solicitud", 75));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha solicitud"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_ety_tp", "Tipo pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, SDbConsts.FIELD_NAME, "Beneficiario pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "_dps", "Documento pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "_orders", "Pedidos documento pago", 65));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "ve.des_pay_app_ety_cur", "Monto a pagar $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "ce.cur_key", "Moneda a pagar"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_req", "Fecha requerida pago"));
        column= new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "dt_sched_n", "Fecha programada pago");
        gridColumnsViews.add(column);
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "v.dt_exec_n", "Fecha operación pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "c.cur", "Moneda requerida pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_priority", "Prioridad pago", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.nts", "Instrucciones pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.nts_auth", "Notas para autorización"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_status", "Estatus pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_ico_proc", "En proceso"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_ico_files", "Archivos soporte"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "v.nts_auth_flow", "Comentarios respuesta autorización"));
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "ve.ety_pay_app_cur", "Monto moneda requerida pago $");
        column.setSumApplying(true);
        gridColumnsViews.add(column);
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "c.cur_key", "Moneda requerida pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_EXC_RATE, "v.pay_exc_rate_app", "TC moneda requerida pago"));
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "ve.ety_pay_app", "Monto moneda local $");
        column.setSumApplying(true);
        gridColumnsViews.add(column);
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_func", "Área funcional"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_func_sub", "Subárea funcional"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "nat", "Naturaleza doc"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "_usr_sched", "Usr aut pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_usr_sched", "Usr TS aut pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_resched", "Reprogramado"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "_usr_resched", "Usr rep pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_usr_resched", "Usr TS rep pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_exec_man", "Pagado manualmente"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "_usr_exec", "Usr oper pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "v.ts_usr_exec", "Usr TS oper pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.FIN_PAY_ETY);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.CFGU_FUNC);
        moSuscriptionsSet.add(SModConsts.USRU_USR);     
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbDocShowCfdiXml) {
                actionDocShowCfdiXml();
            }
            else if (button == jbDocGetCfdiXml) {
                actionDocGetCfdiXml();
            }
            else if (button == jbDocShowDocPdf) {
                actionDocShowDocPdf();
            }
            else if (button == jbDocGetDocPdf) {
                actionDocGetDocPdf();
            }
            else if (button == jbAuthWebLoadSupportFiles) {
                actionAuthWebLoadSupportFiles();
            }
            else if (button == jbAuthWebStartAuth) {
                actionAuthWebStartAuth();
            }
            else if (button == jbAuthWebViewAuthLog) {
                actionAuthWebViewAuthLog();
            }
            else if (button == jbAuthWebDownloadSupportFiles) {
                actionAuthWebDownloadSupportFiles();
            }
            else if (button == jbAuthWebClearSupportFiles) {
                actionAuthWebClearSupportFiles();
            }
            else if (button == jbAuthWebAnnullAuth) {
                actionAuthWebAnnullAuth();
            }
            else if (button == jbPaymentReschedule) {
                actionPaymentReschedule();
            }
            else if (button == jbPaymentChangeCurrency) {
                actionPaymentChangeCurrency();
            }
            else if (button == jbPaymentMarkAsPaid) {
                actionPaymentMarkAsPaid();
            }
            else if (button == jbPaymentBlock) {
                actionPaymentBlock();
            }
            else if (button == jbPaymentUnblock) {
                actionPaymentUnblock();
            }
            else if (button == jbPaymentCancel) {
                actionPaymentCancel();
            }
            else if (button == jbExportDataToSwapServices) {
                actionExportDataToSwapServices();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JRadioButton && e.getStateChange() == ItemEvent.SELECTED) {
            JRadioButton radioButton = (JRadioButton) e.getSource();
            
            if (radioButton == jrbDateApp || radioButton == jrbDateReq || radioButton == jrbDateSched) {
                refreshGridWithRefresh();
                
            }
        }
    }
}
