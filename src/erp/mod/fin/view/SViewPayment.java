/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.client.SClientInterface;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.SSwapUtils;
import erp.mod.cfg.swap.SSyncType;
import erp.mod.cfg.swap.utils.SExportUtils;
import erp.mod.cfg.swap.utils.SResponses;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.fin.db.SDbPayment;
import erp.mod.fin.db.SDbPaymentEntry;
import erp.mod.fin.db.SDbPaymentFile;
import erp.mod.fin.form.SDialogPaymentChangeStatus;
import erp.mod.hrs.utils.SDocUtils;
import erp.mod.view.SViewFilter;
import java.awt.Cursor;
import java.awt.Dimension;
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
 * @author Isabel Servín, Sergio Flores
 */
public class SViewPayment extends SGridPaneView implements ActionListener, ItemListener {
    
    private JLabel jlDate;
    private JRadioButton jrbDateApp;
    private JRadioButton jrbDateReq;
    private ButtonGroup bgDate;
    
    private boolean mbAppliesFilterDatePeriod;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SViewFilter moFilterFuncArea;
    private SViewFilter moFilterCurrency;
    
    private boolean mbAppliesDateRequired;
    
    private JButton jbAuthWebLoadFiles;
    private JButton jbAuthWebStartAuth;
    private JButton jbAuthWebDownloadFiles;
    private JButton jbAuthWebClearFiles;
    
    private JButton jbPaymentReschedule;
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
        if (mnGridSubtype == SLibConstants.UNDEFINED) {
            setRowButtonsEnabled(true, false, true, false, true);
            jtbFilterDeleted.setEnabled(true);
        }
        else {
            setRowButtonsEnabled(false);
            jtbFilterDeleted.setEnabled(false);
        }
        
        jlDate = new JLabel("Fecha:");
        jrbDateApp = new JRadioButton("Solicitud");
        jrbDateApp.setToolTipText("Filtrar por la fecha de la solicitud de pago");
        
        mbAppliesDateRequired = mnGridSubtype == SLibConsts.UNDEFINED || mnGridSubtype == SModSysConsts.FINS_ST_PAY_REJC;
        
        if (mbAppliesDateRequired) {
            jrbDateReq = new JRadioButton("Requerida");
            jrbDateReq.setToolTipText("Filtrar por la fecha requerida de pago");
        }
        else {
            jrbDateReq = new JRadioButton("Programada");
            jrbDateReq.setToolTipText("Filtrar por la fecha programada de pago");
        }
        
        bgDate = new ButtonGroup();
        bgDate.add(jrbDateApp);
        bgDate.add(jrbDateReq);
        bgDate.setSelected(jrbDateApp.getModel(), true);
        
        jrbDateApp.addItemListener(this);
        jrbDateReq.addItemListener(this);
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        moFilterFuncArea = new SViewFilter(miClient, this, SModConsts.CFGU_FUNC);
        moFilterFuncArea.initFilter(null);
        moFilterCurrency = new SViewFilter(miClient, this, SModConsts.CFGU_CUR);
        moFilterCurrency.initFilter(null);
        
        jbAuthWebLoadFiles = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_add_ora.gif")));
        jbAuthWebLoadFiles.setPreferredSize(new Dimension(23, 23));
        jbAuthWebLoadFiles.addActionListener(this);
        jbAuthWebLoadFiles.setToolTipText("Cargar archivos de soporte a la solicitud");
        
        jbAuthWebStartAuth = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_up_ora.gif")));
        jbAuthWebStartAuth.setPreferredSize(new Dimension(23, 23));
        jbAuthWebStartAuth.addActionListener(this);
        jbAuthWebStartAuth.setToolTipText("Iniciar autorización de la solicitud en app web");
        
        jbAuthWebDownloadFiles = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_down_ora.gif")));
        jbAuthWebDownloadFiles.setPreferredSize(new Dimension(23, 23));
        jbAuthWebDownloadFiles.addActionListener(this);
        jbAuthWebDownloadFiles.setToolTipText("Descargar archivos de soporte de la solicitud");

        jbAuthWebClearFiles = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_rem_ora.gif")));
        jbAuthWebClearFiles.setPreferredSize(new Dimension(23, 23));
        jbAuthWebClearFiles.addActionListener(this);
        jbAuthWebClearFiles.setToolTipText("Eliminar archivos de soporte de la solicitud");

        jbPaymentReschedule = new JButton(new ImageIcon(getClass().getResource("/erp/img/gui_cal.gif")));
        jbPaymentReschedule.setPreferredSize(new Dimension(23, 23));
        jbPaymentReschedule.addActionListener(this);
        jbPaymentReschedule.setToolTipText("Cambiar fecha requerida o programada");
        
        jbPaymentMarkAsPaid = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_money.gif")));
        jbPaymentMarkAsPaid.setPreferredSize(new Dimension(23, 23));
        jbPaymentMarkAsPaid.addActionListener(this);
        jbPaymentMarkAsPaid.setToolTipText("Marcar como operado");
        
        jbPaymentBlock = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_lock.gif")));
        jbPaymentBlock.setPreferredSize(new Dimension(23, 23));
        jbPaymentBlock.addActionListener(this);
        jbPaymentBlock.setToolTipText("Bloquear solicitud");
        
        jbPaymentUnblock = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_unlock.gif")));
        jbPaymentUnblock.setPreferredSize(new Dimension(23, 23));
        jbPaymentUnblock.addActionListener(this);
        jbPaymentUnblock.setToolTipText("Desbloquear solicitud");
        
        jbPaymentCancel = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_annul.gif")));
        jbPaymentCancel.setPreferredSize(new Dimension(23, 23));
        jbPaymentCancel.addActionListener(this);
        jbPaymentCancel.setToolTipText("Cancelar solicitud");
        
        jbExportDataToSwapServices = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_up_ind.gif")),
            "Exportar registros '" + SSwapUtils.translateSyncType(SSyncType.PUR_PAYMENT, SLibConsts.LAN_ISO639_ES) + "' a " + SSwapConsts.SWAP_SERVICES, this);
        
        if (SLibUtils.belongsTo(mnGridSubtype, new int[] { SLibConsts.UNDEFINED, SModSysConsts.FINS_ST_PAY_REJC, SModSysConsts.FINS_ST_PAY_SCHED, SModSysConsts.FINS_ST_PAY_CANC })) {
            mbAppliesFilterDatePeriod = true;
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jlDate);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jrbDateApp);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jrbDateReq);
        }
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFuncArea);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterCurrency);
        
        switch (mnGridSubtype) {
            case SLibConstants.UNDEFINED:
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebLoadFiles);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebStartAuth);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebDownloadFiles);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebClearFiles);
                
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExportDataToSwapServices);
                break;
                
            case SModSysConsts.FINS_ST_PAY_IN_AUTH:
                break;
                
            case SModSysConsts.FINS_ST_PAY_REJC:
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentReschedule);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentCancel);
                
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExportDataToSwapServices);
                break;
                
            case SModSysConsts.FINS_ST_PAY_SCHED:
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentReschedule);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentMarkAsPaid);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentBlock);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentCancel);
                
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExportDataToSwapServices);
                break;
                
            case SModSysConsts.FINS_ST_PAY_BLOC:
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentUnblock);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentCancel);
                
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExportDataToSwapServices);
                break;
                
            case SModSysConsts.FINS_ST_PAY_CANC:
                break;
                
            default:
                throw new UnsupportedOperationException(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
    }
    
    private void actionAuthWebLoadFile() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else if (gridRow.isRowSystem()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
            }
            else if (!gridRow.isUpdatable()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
            }
            else {
                try {
                    SGuiParams params = new SGuiParams();
                    params.setKey(gridRow.getRowPrimaryKey());
                    miClient.getSession().getModule(SModConsts.MOD_FIN_N).showForm(SModConsts.FIN_PAY_FILE, SLibConstants.UNDEFINED, params);
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
                catch (Exception e) {
                    miClient.showMsgBoxError(e.getMessage());
                }                
            }
        }
    }

    private void actionAuthWebStartAuth() {
        try {
            if (jbAuthWebStartAuth.isEnabled()) {
                if (jtTable.getSelectedRowCount() != 1) {
                    miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
                }
                else {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else if (gridRow.isRowSystem()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                    }
                    else if (!gridRow.isUpdatable()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                    }
                    else {
                        if (SAuthorizationUtils.sendAuthornPaymentsAppWeb((SClientInterface) miClient, gridRow.getRowPrimaryKey())) {
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxWarning(e.getMessage());
        }
    }
    
    private void actionAuthWebDownloadFiles() {
        try {
            if (jbAuthWebDownloadFiles.isEnabled()) {
                if (jtTable.getSelectedRowCount() != 1) {
                    miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
                }
                else {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else if (gridRow.isRowSystem()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                    }
                    else if (!gridRow.isUpdatable()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                    }
                    else {
                        SDbPayment payment = new SDbPayment();
                        payment.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                        if (payment.getFiles().isEmpty()) {
                            miClient.showMsgBoxWarning("La solicitud de pago '" + payment.getFolio() + "' no tiene archivos de soporte.");
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
                }
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxWarning(e.getMessage());
        }
    }

    private void actionAuthWebClearFiles() {
        try {
            if (jbAuthWebClearFiles.isEnabled()) {
                if (jtTable.getSelectedRowCount() != 1) {
                    miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
                }
                else {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else if (gridRow.isRowSystem()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                    }
                    else if (!gridRow.isUpdatable()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
                    }
                    else {
                        SDbPayment payment = new SDbPayment();
                        payment.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                        if (payment.getFiles().isEmpty()) {
                            miClient.showMsgBoxWarning("La solicitud de pago '" + payment.getFolio() + "' no tiene archivos de soporte.");
                        }
                        else {
                            if (miClient.showMsgBoxConfirm("¿Está seguro que desea eliminar todos los archivos de soporte de la solicitud de pago '" + payment.getFolio() + "'?") == JOptionPane.YES_OPTION) {
                                int cant = payment.getFiles().size();
                                payment.deleteFiles(miClient.getSession());

                                miClient.showMsgBoxInformation("Se " + (cant == 1 ? "eliminó 1 archivo" : "eliminaron " + cant + " archivos") + " de soporte.");
                                miClient.getSession().notifySuscriptors(mnGridType);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxWarning(e.getMessage());
        }
    }
    
    private void actionPaymentReschedule() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else if (!gridRow.isUpdatable()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
            }
            else {
                try {
                    SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());
                    int status = payment.getFkStatusPaymentId(); // convenience variable
                    
                    if (status == SModSysConsts.FINS_ST_PAY_REJC || status == SModSysConsts.FINS_ST_PAY_SCHED) {
                        if (moDialogPaymentChangeStatus == null) {
                            moDialogPaymentChangeStatus = new SDialogPaymentChangeStatus(miClient, "");
                        }

                        moDialogPaymentChangeStatus.setRegistry(payment);
                        moDialogPaymentChangeStatus.setFormType(status);
                        moDialogPaymentChangeStatus.setVisible(true);

                        if (moDialogPaymentChangeStatus.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                            payment.setAuxReloadEntries(false);

                            switch (status) {
                                case SModSysConsts.FINS_ST_PAY_REJC:
                                    payment.setFkStatusPaymentId(SModSysConsts.FINS_ST_PAY_NEW);
                                    payment.setDateRequired((Date) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_DATE));
                                    payment.setNotes((String) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_NOTES));
                                    payment.setNotesAuthorization((String) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_NOTES_AUTH));

                                    if (payment.isSystem()) {
                                        miClient.showMsgBoxInformation("La solicitud de pago '" + payment.getFolio() + "' se enviará nuevamente a autorizar de manera automática al " + SSwapConsts.PURCHASE_PORTAL + ".\n"
                                                + "(Si es indispensable, puede acelerar el envío con la opción '" + jbExportDataToSwapServices.getToolTipText() + "').");
                                    }
                                    else {
                                        miClient.showMsgBoxInformation("La solicitud de pago '" + payment.getFolio() + "' requiere enviarse nuevamente a autorizar de manera manual.\n"
                                                + "Favor de hacerlo en la vista 'Solicitudes de pago'.");
                                    }
                                    break;

                                case SModSysConsts.FINS_ST_PAY_SCHED:
                                    payment.setFkStatusPaymentId(SModSysConsts.FINS_ST_PAY_SCHED_P);
                                    payment.setDateSchedule_n((Date) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_DATE));
                                    
                                    miClient.showMsgBoxInformation("La solicitud de pago '" + payment.getFolio() + "' se actualizará de manera automática en el " + SSwapConsts.PURCHASE_PORTAL + ".\n"
                                            + "(Si es indispensable, puede acelerar la actualización con la opción '" + jbExportDataToSwapServices.getToolTipText() + "').");
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
                    miClient.showMsgBoxError(e.getMessage());
                }                
            }
        }
    }
    
    private void actionPaymentMarkAsPaid() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else if (!gridRow.isUpdatable()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
            }
            else {
                try {
                    SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());
                    int status = payment.getFkStatusPaymentId(); // convenience variable
                    
                    if (status == SModSysConsts.FINS_ST_PAY_SCHED) {
                        if (moDialogPaymentChangeStatus == null) {
                            moDialogPaymentChangeStatus = new SDialogPaymentChangeStatus(miClient, "");
                        }

                        moDialogPaymentChangeStatus.setRegistry(payment);
                        moDialogPaymentChangeStatus.setFormType(SModSysConsts.FINS_ST_PAY_EXEC);
                        moDialogPaymentChangeStatus.setVisible(true);

                        if (moDialogPaymentChangeStatus.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                            payment.setAuxReloadEntries(false);

                            payment.setFkStatusPaymentId(SModSysConsts.FINS_ST_PAY_EXEC_P);
                            payment.setDateExecution_n((Date) moDialogPaymentChangeStatus.getValue(SDialogPaymentChangeStatus.VALUE_DATE));

                            miClient.showMsgBoxInformation("La solicitud de pago '" + payment.getFolio() + "' se actualizará de manera automática en el " + SSwapConsts.PURCHASE_PORTAL + ".\n"
                                    + "(Si es indispensable, puede acelerar la actualización con la opción '" + jbExportDataToSwapServices.getToolTipText() + "').");

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
                    miClient.showMsgBoxError(e.getMessage());
                }                
            }
        }
    }
    
    private void actionPaymentBlock() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else if (!gridRow.isUpdatable()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
            }
            else {
                try {
                    SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());
                    int status = payment.getFkStatusPaymentId(); // convenience variable
                    
                    if (status == SModSysConsts.FINS_ST_PAY_SCHED) {
                        if (miClient.showMsgBoxConfirm("La solicitud de pago '" + payment.getFolio()+ "' será bloqueada.\n"
                                + "Podrá desbloquearla en la vista 'Solicitudes de pago bloqueadas'.\n"
                                + SGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION) {
                            payment.updatePaymentStatus(miClient.getSession(), SModSysConsts.FINS_ST_PAY_BLOC);
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                    else {
                        String statusDescrip = (String) miClient.getSession().readField(SModConsts.FINS_ST_PAY, new int[] { status }, SDbRegistry.FIELD_NAME);
                        miClient.showMsgBoxInformation("El estatus de solicitud de pago '" + payment.getFolio()+ "' es '" + statusDescrip + "', pero debe ser '" + SDbPayment.ST_SCHED + "' para poderse bloquear.");
                    }
                }
                catch (Exception e) {
                    miClient.showMsgBoxError(e.getMessage());
                }                
            }
        }
    }
    
    private void actionPaymentUnblock() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else if (!gridRow.isUpdatable()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
            }
            else {
                try {
                    SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());
                    int status = payment.getFkStatusPaymentId(); // convenience variable
                    
                    if (status == SModSysConsts.FINS_ST_PAY_BLOC) {
                        if (miClient.showMsgBoxConfirm("La solicitud de pago '" + payment.getFolio() + "' será desbloqueada, y quedará nuevamente con estatus '" + SDbPayment.ST_SCHED + "'.\n"
                                + SGuiConsts.MSG_CNF_CONT) == JOptionPane.OK_OPTION) {
                            payment.updatePaymentStatus(miClient.getSession(), SModSysConsts.FINS_ST_PAY_SCHED);
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                    else {
                        String statusDescrip = (String) miClient.getSession().readField(SModConsts.FINS_ST_PAY, new int[] { status }, SDbRegistry.FIELD_NAME);
                        miClient.showMsgBoxInformation("El estatus de solicitud de pago '" + payment.getFolio()+ "' es '" + statusDescrip + "', pero debe ser '" + SDbPayment.ST_BLOCK + "' para poderse desbloquear.");
                    }
                }
                catch (Exception e) {
                    miClient.showMsgBoxError(e.getMessage());
                }                
            }
        }
    }
    
    private void actionPaymentCancel() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else if (!gridRow.isUpdatable()) {
                miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_UPDATABLE);
            }
            else {
                try {
                    SDbPayment payment = (SDbPayment) miClient.getSession().readRegistry(SModConsts.FIN_PAY, gridRow.getRowPrimaryKey());
                    int status = payment.getFkStatusPaymentId(); // convenience variable
                    
                    if (status == SModSysConsts.FINS_ST_PAY_REJC || status == SModSysConsts.FINS_ST_PAY_SCHED || status == SModSysConsts.FINS_ST_PAY_BLOC) {
                        if (miClient.showMsgBoxConfirm("La solicitud de pago '" + gridRow.getRowCode() + "' será cancelada, y esta acción no se puede revertir.\n"
                                + "La solicitud de pago se eliminará de manera automática del " + SSwapConsts.PURCHASE_PORTAL + ".\n"
                                + "(Si es indispensable, puede acelerar la eliminación con la opción '" + jbExportDataToSwapServices.getToolTipText() + "').\n"
                                + SGuiConsts.MSG_CNF_CONT) == JOptionPane.OK_OPTION) {
                            payment.updatePaymentStatus(miClient.getSession(), SModSysConsts.FINS_ST_PAY_CANC_P);
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                    else {
                        String statusDescrip = (String) miClient.getSession().readField(SModConsts.FINS_ST_PAY, new int[] { status }, SDbRegistry.FIELD_NAME);
                        miClient.showMsgBoxInformation("El estatus de solicitud de pago '" + payment.getFolio()+ "' es '" + statusDescrip + "', pero debe ser '" + SDbPayment.ST_REJC + "', '" + SDbPayment.ST_SCHED + "' o '" + SDbPayment.ST_BLOCK + "' para poderse cancelar.");
                    }
                }
                catch (Exception e) {
                    miClient.showMsgBoxError(e.getMessage());
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
                sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt_app", (SGuiDate) filter);
            }
            else {
                if (mbAppliesDateRequired) {
                    sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt_req", (SGuiDate) filter);
                }
                else {
                    sql += (sql.isEmpty() ? "" : "AND ") + "(v.dt_sched_n IS NOT NULL AND " + SGridUtils.getSqlFilterDate("v.dt_sched_n", (SGuiDate) filter) + ") ";
                }
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
        setShowSums(filter != null && ((String) filter).split(",").length == 1); // only if a single currency is selected!
        
        switch (mnGridSubtype) {
            case SLibConstants.UNDEFINED:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                + SModSysConsts.FINS_ST_PAY_NEW + ", " 
                + SModSysConsts.FINS_ST_PAY_IN_AUTH + ", " 
                + SModSysConsts.FINS_ST_PAY_REJC + ", " 
                + SModSysConsts.FINS_ST_PAY_REJC_P + ", "
                + SModSysConsts.FINS_ST_PAY_SCHED + ", " 
                + SModSysConsts.FINS_ST_PAY_SCHED_P + ", " 
                + SModSysConsts.FINS_ST_PAY_SUBR + ", "
                + SModSysConsts.FINS_ST_PAY_SUBR_P + ") ";
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
                // nothing
        }
        
        msSql = "SELECT "
                + "v.id_pay AS " + SDbConsts.FIELD_ID + "1, "
                + "b.bp AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt_app AS " + SDbConsts.FIELD_DATE + ", "
                + "CONCAT(v.ser, IF(v.ser = '', '', '-'), v.num) AS " + SDbConsts.FIELD_CODE + ", "
                + "v.pay_cur, "
                + "c.cur_key, "
                + "c.cur, "
                + "v.pay_exc_rate_app, "
                + "v.pay_app, "
                + "v.dt_req, "
                + "v.dt_sched_n, "
                + "v.dt_exec_n, "
                + "v.nts, "
                + "v.nts_auth, "
                + "v.nts_auth_flow, "
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
                + "IF(ve.ety_tp = '" + SDbPaymentEntry.ENTRY_TYPE_ADVANCE + "' , '" + SDbPaymentEntry.DESC_ENTRY_TYPE_ADVANCE + "', '" + SDbPaymentEntry.DESC_ENTRY_TYPE_PAYMENT + "') AS _ety_tp, "
                + "IF(v.priority = 0 , '" + SDbPayment.DESC_PRIORITY_NORMAL + "', '" + SDbPayment.DESC_PRIORITY_URGENT + "') AS _priority, "
                + "ve.ety_pay_cur, "
                + "ve.ety_pay_app, "
                + "ve.conv_rate_app, "
                + "ve.des_pay_app_ety_cur, "
                + "ve.install, "
                + "ve.doc_bal_prev_app_cur, "
                + "ve.doc_bal_unpd_app_cur_r, "
                + "ce.cur_key AS _ety_cur_key, "
                + "CONCAT(d.num_ser, IF(d.num_ser = '', '', '-'), d.num) AS _dps, "
                + "v.ts_usr_sched, "
                + "v.ts_usr_exec, "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + ", "
                + "us.usr AS usr_sched, "
                + "us.usr AS usr_exec "
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
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ue ON " 
                + "v.fk_usr_exec = ue.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS ce ON " 
                + "ve.fk_ety_cur = ce.id_cur " 
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON " 
                + "ve.fk_doc_year_n = d.id_year AND ve.fk_doc_doc_n = d.id_doc " 
                + (sql.isEmpty() ? "" : "WHERE " + sql)
                + "ORDER BY v.ser, LPAD(v.num, 9, '0'), " + (jrbDateApp.isSelected() ? "v.dt_app" : "COALESCE(dt_sched_n, dt_req)") + ", b.bp "; // ): sort up to 999,999,999
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView column;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_CODE, "Folio solicitud", 75));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha solicitud"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_ety_tp", "Tipo solicitud"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, SDbConsts.FIELD_NAME, "Beneficiario pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "_dps", "Documento pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "des_pay_app_ety_cur", "Monto a pagar $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "_ety_cur_key", "Moneda a pagar"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_req", "Fecha requerida pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_sched_n", "Fecha programada pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_exec_n", "Fecha operación pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "cur", "Moneda requerida pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_priority", "Prioridad", 50));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "nts", "Instrucciones pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "nts_auth", "Notas para autorización"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_status", "Estatus"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_ico_proc", "En proceso"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_ico_files", "Archivos soporte"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "nts_auth_flow", "Comentarios respuesta autorización"));
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "pay_cur", "Monto moneda requerida pago $");
        column.setSumApplying(true);
        gridColumnsViews.add(column);
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "cur_key", "Moneda requerida pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_EXC_RATE, "pay_exc_rate_app", "TC moneda requerida pago"));
        column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "pay_app", "Monto moneda local $");
        column.setSumApplying(true);
        gridColumnsViews.add(column);
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_func", "Área funcional"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_func_sub", "Subárea funcional"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_sched", "Usr aut pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_usr_sched", "Usr TS aut pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_exec", "Usr oper pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_usr_exec", "Usr TS oper pago"));
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
            
            if (button == jbAuthWebLoadFiles) {
                actionAuthWebLoadFile();
            }
            else if (button == jbAuthWebStartAuth) {
                actionAuthWebStartAuth();
            }
            else if (button == jbAuthWebDownloadFiles) {
                actionAuthWebDownloadFiles();
            }
            else if (button == jbAuthWebClearFiles) {
                actionAuthWebClearFiles();
            }
            else if (button == jbPaymentCancel) {
                actionPaymentCancel();
            }
            else if (button == jbPaymentReschedule) {
                actionPaymentReschedule();
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
            else if (button == jbExportDataToSwapServices) {
                actionExportDataToSwapServices();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JRadioButton && e.getStateChange() == ItemEvent.SELECTED) {
            JRadioButton radioButton = (JRadioButton) e.getSource();
            
            if (radioButton == jrbDateApp || radioButton == jrbDateReq) {
                refreshGridWithRefresh();
            }
        }
    }
}
