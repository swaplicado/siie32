/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.SSwapUtils;
import erp.mod.cfg.swap.SSyncType;
import erp.mod.cfg.swap.utils.SExportPayments;
import erp.mod.cfg.swap.utils.SExportUtils;
import erp.mod.cfg.swap.utils.SResponses;
import erp.mod.fin.db.SDbPayment;
import erp.mod.fin.db.SDbPaymentEntry;
import erp.mod.fin.db.SDbPaymentFile;
import erp.mod.fin.form.SDialogPaymentChangeSched;
import erp.mod.hrs.utils.SDocUtils;
import erp.mod.view.SViewFilter;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
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
public class SViewPayment extends SGridPaneView implements ActionListener {
    
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SViewFilter moFilterFunc;
    private SViewFilter moFilterCur;
    
    private JButton jbAuthWebLoadFiles;
    private JButton jbAuthWebStartAuth;
    private JButton jbAuthWebDownloadFiles;
    private JButton jbAuthWebClearFiles;
    
    private JButton jbPaymentReschedule;
    private JButton jbPaymentBlock;
    private JButton jbPaymentUnblock;
    private JButton jbPaymentCancel;
    
    private JButton jbExportDataToSwapServices;
    
    private JFileChooser moAuthWebFileChooser;
    private SDialogPaymentChangeSched moDialogChangeSched;

    public SViewPayment(SGuiClient client, int subType, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.FIN_PAY, subType, title);
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        if (mnGridSubtype == SLibConstants.UNDEFINED) {
            setRowButtonsEnabled(true, false, true, false, true);
        }
        else {
            setRowButtonsEnabled(false);
        }
        
        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        moFilterFunc = new SViewFilter(miClient, this, SModConsts.CFGU_FUNC);
        moFilterFunc.initFilter(null);
        moFilterCur = new SViewFilter(miClient, this, SModConsts.CFGU_CUR);
        moFilterCur.initFilter(null);
        
        jbAuthWebLoadFiles = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_add_ora.gif")));
        jbAuthWebLoadFiles.setPreferredSize(new Dimension(23, 23));
        jbAuthWebLoadFiles.addActionListener(this);
        jbAuthWebLoadFiles.setToolTipText("Cargar archivos a la solicitud");
        
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
        jbPaymentReschedule.setToolTipText("Cambiar fecha programada");
        
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
        
        if (SLibUtils.belongsTo(mnGridSubtype, new int[] { SLibConsts.UNDEFINED, SModSysConsts.FINS_ST_PAY_REJC, SModSysConsts.FINS_ST_PAY_CANC })) {
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFunc);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterCur);
        
        switch (mnGridSubtype) {
            case SLibConstants.UNDEFINED:
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebLoadFiles);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebStartAuth);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebDownloadFiles);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthWebClearFiles);
                break;
            case SModSysConsts.FINS_ST_PAY_IN_AUTH:
                break;
            case SModSysConsts.FINS_ST_PAY_EXEC_P:
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentReschedule);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentBlock);
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentCancel);
                break;
            case SModSysConsts.FINS_ST_PAY_REJC:
                break;
            case SModSysConsts.FINS_ST_PAY_BLOC:
                getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPaymentUnblock);
                break;
            case SModSysConsts.FINS_ST_PAY_CANC:
                break;
            default:
                // nothing
        }
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbExportDataToSwapServices);
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
                        SDbPayment payment = new SDbPayment();
                        payment.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                        if (payment.getFkStatusPaymentId() != SModSysConsts.FINS_ST_PAY_NEW) {
                            miClient.showMsgBoxInformation("No se puede enviar la solicitud de pago porque ya fue enviado anteriormente.");
                        }
                        else {
                            if (miClient.showMsgBoxConfirm("Se enviará la solicitud de pago a un proceso de autorización.\n¿Desea continuar?")== JOptionPane.OK_OPTION) {
                                new SExportPayments(miClient, payment).start();
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
                            miClient.showMsgBoxWarning("La solicitud no tiene archivos.");
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

                                miClient.showMsgBoxInformation("Se " + (files == 1 ? "descargó 1 archivo" : "descargaron " + files + " archivos") + " de soporte de la orden seleccionada.");
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
                            miClient.showMsgBoxWarning("La solicitud no tiene archivos.");
                        }
                        else {
                            if (miClient.showMsgBoxConfirm("¿Está seguro que desea eliminar todos los archivos de la solicitud seleccionada?") == JOptionPane.YES_OPTION) {
                                int cant = payment.getFiles().size();
                                payment.deleteFiles(miClient.getSession());

                                miClient.showMsgBoxInformation("Se eliminaron un total de " + cant + " archivos de soporte.");
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
    
    private void actionPaymentCancel() {
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
                    if (miClient.showMsgBoxConfirm("El pagó será cancelado, esta acción no se puede deshacer ¿Desea continuar?") == JOptionPane.OK_OPTION) {
                        SDbPayment moPayment = new SDbPayment();
                        moPayment.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                        moPayment.updatePaymentStatus(miClient.getSession(), SModSysConsts.FINS_ST_PAY_CANC_P);
                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                }
                catch (Exception e) {
                    miClient.showMsgBoxError(e.getMessage());
                }                
            }
        }
    }
    
    private void actionPatmentRescheddule() {
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
                    if (moDialogChangeSched == null) {
                        moDialogChangeSched = new SDialogPaymentChangeSched(miClient, "Cambio de fecha programada");
                    }
                    SDbPayment moPayment = new SDbPayment();
                    moPayment.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                    moDialogChangeSched.setRegistry(moPayment);
                    moDialogChangeSched.setVisible(true);
                    if (moDialogChangeSched.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                        moPayment.setAuxReloadEntries(false);
                        moPayment.setDateSchedule_n(moDialogChangeSched.getDateSched());
                        moPayment.setFkStatusPaymentId(SModSysConsts.FINS_ST_PAY_SCHED_P);
                        moPayment.save(miClient.getSession());
                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                }
                catch (Exception e) {
                    miClient.showMsgBoxError(e.getMessage());
                }                
            }
        }
    }
    
    private void actionPatmentBlock() {
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
                    if (miClient.showMsgBoxConfirm("El pagó será bloqueado, podra regresarlo a programado en la vista de 'Pagos bloqueados'.\n¿Desea continuar?") == JOptionPane.OK_OPTION) {
                        SDbPayment moPayment = new SDbPayment();
                        moPayment.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                        moPayment.updatePaymentStatus(miClient.getSession(), SModSysConsts.FINS_ST_PAY_BLOC_P);
                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                }
                catch (Exception e) {
                    miClient.showMsgBoxError(e.getMessage());
                }                
            }
        }
    }
    
    private void actionPatmentUnblock() {
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
                    if (miClient.showMsgBoxConfirm("El pago regresará a programado. ¿Desea continuar?") == JOptionPane.OK_OPTION) {
                        SDbPayment moPayment = new SDbPayment();
                        moPayment.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                        moPayment.updatePaymentStatus(miClient.getSession(), SModSysConsts.FINS_ST_PAY_SCHED);
                        miClient.getSession().notifySuscriptors(mnGridType);
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
                SResponses responses = SExportUtils.exportData(miClient.getSession(), SSyncType.PUR_PAYMENT, true, true);
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
        
        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "NOT v.b_del ";
        }
        
        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt_app", (SGuiDate) filter);
        
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
            case SLibConstants.UNDEFINED:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay NOT IN (" 
                + SModSysConsts.FINS_ST_PAY_EXEC + ", " 
                + SModSysConsts.FINS_ST_PAY_EXEC_P + ", " 
                + SModSysConsts.FINS_ST_PAY_RCPT + ", " 
                + SModSysConsts.FINS_ST_PAY_RCPT_P + ", " 
                + SModSysConsts.FINS_ST_PAY_SUBR + ", " 
                + SModSysConsts.FINS_ST_PAY_SUBR_P + ") ";
                break;
            case SModSysConsts.FINS_ST_PAY_IN_AUTH:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_IN_AUTH + ") ";
                break;
            case SModSysConsts.FINS_ST_PAY_EXEC_P:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_SCHED + ", "
                        + SModSysConsts.FINS_ST_PAY_IN_TREAS + ") ";
                break;
            case SModSysConsts.FINS_ST_PAY_REJC:
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay IN ("
                        + SModSysConsts.FINS_ST_PAY_REJC + ", "
                        + SModSysConsts.FINS_ST_PAY_REJC_P + ") ";
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
                sql += (sql.isEmpty() ? "" : "AND ") + "v.fk_st_pay = " + mnGridSubtype + " "; // just in case!
        }
        
        msSql = "SELECT "
                + "v.id_pay AS " + SDbConsts.FIELD_ID + "1, " 
                + "b.bp AS " + SDbConsts.FIELD_NAME + ", "  
                + "v.dt_app AS " + SDbConsts.FIELD_DATE + ", " 
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "   
                + "v.pay_cur, "   
                + "c.cur_key, "   
                + "c.cur, "   
                + "v.pay_exc_rate_app, "   
                + "v.pay_app, "   
                + "v.dt_req, " 
                + "v.nts, "   
                + "f.name AS func, "
                + "fs.name AS func_s, "
                + "sp.name AS status, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " 
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", " 
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "IF((SELECT COUNT(*) FROM " + SModConsts.TablesMap.get(SModConsts.FIN_PAY_FILE) + " AS f "
                + " WHERE v.id_pay = f.id_pay) = 0, " + SGridConsts.ICON_NULL + ", " + SGridConsts.ICON_FOLDER + ")"
                + " AS doc, "
                + "IF(fk_st_pay >= " + SModSysConsts.FINS_ST_PAY_REJC_P + ", " + SGridConsts.ICON_WAIT + ", " + SGridConsts.ICON_NULL + ")"
                + " AS proc, "
                + "IF(ve.ety_tp = '" + SDbPaymentEntry.ENTRY_TYPE_ADVANCE + "' , 'PAGO ANTICIPO', 'PAGO A DOCUMENTO') AS ety_tp, "
                + "IF(v.priority = 0 , 'NORMAL', 'URGENTE') AS pty, "
                + "ve.ety_pay_cur, "
                + "ve.ety_pay_app, "
                + "ve.conv_rate_app, "
                + "ve.des_pay_app_ety_cur, "
                + "ve.install, "
                + "ve.doc_bal_prev_app_cur, "
                + "ve.doc_bal_unpd_app_cur_r, "
                + "ce.cur_key AS ety_cur, "
                + "IF(d.num_ser = '', d.num, CONCAT(d.num_ser, '-', d.num)) AS dps, "
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
                + "ORDER BY v.num, v.dt_app, b.bp ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, SDbConsts.FIELD_CODE, "Folio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "ety_tp", "Tipo pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, SDbConsts.FIELD_NAME, "Beneficiario pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "dps", "Documento pago"));
        SGridColumnView column = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "des_pay_app_ety_cur", "Monto a pagar $");
        column.setSumApplying(true);
        gridColumnsViews.add(column);
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "ety_cur", "Moneda a pagar"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_req", "Fecha requerida pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "cur", "Moneda requerida pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "pty", "Prioridad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "nts", "Instrucciones pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "pay_cur", "Monto moneda requerida pago $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CUR, "cur_key", "Moneda requerida pago"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_EXC_RATE, "pay_exc_rate_app", "TC moneda requerida pago $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "pay_app", "Monto moneda local $"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "func", "Área funcional"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "func_s", "Subárea funcional"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "doc", "Archivos"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "status", "Estatus"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "proc", "En proceso"));
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
                actionPatmentRescheddule();
            }
            else if (button == jbPaymentBlock) {
                actionPatmentBlock();
            }
            else if (button == jbPaymentUnblock) {
                actionPatmentUnblock();
            }
            else if (button == jbExportDataToSwapServices) {
                actionExportDataToSwapServices();
            }
        }
    }
}
