/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.trn.db.SMaterialRequestUtils;
import erp.mod.trn.form.SDialogAuthorizationCardex;
import erp.mod.trn.form.SDialogMaterialRequestDocsCardex;
import erp.mod.trn.form.SDialogMaterialRequestEstimation;
import erp.mod.trn.form.SDialogMaterialRequestEstimationCardex;
import erp.mod.trn.form.SDialogMaterialRequestLogsCardex;
import erp.mod.trn.form.SDialogMaterialRequestSegregation;
import erp.mod.trn.form.SDialogMaterialRequestSupply;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
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
 * @author Isabel Servín, Edwin Carmona
 */
public class SViewMaterialRequestPending extends SGridPaneView implements ActionListener {

    private JButton jbPrint;
    private JButton jbAuthCardex;
    private JButton jbLogCardex;
    private JButton jbSegregate;
    private JButton mjbSupply;
    private JButton mjbCloseOpenSupply;
    private JButton mjbCloseOpenPurchase;
    private JButton mjbToNew;
    private JButton mjbToSupply;
    private JButton mjbToPur;
    private JButton mjbToEstimate;
    private JButton jbDocsCardex;
    private JButton mjbToSearch;
    private JButton mjbCleanSearch;
    //private JButton mjbEstimationKardex;
    //private JButton mjbClose;
    //private JButton mjbOpen;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SDialogAuthorizationCardex moDialogAuthCardex;
    private SDialogMaterialRequestSegregation moDialogSegregations;
    private SDialogMaterialRequestSupply moDialogSupply;
    private SDialogMaterialRequestEstimation moDialogEstimate;
    private SDialogMaterialRequestLogsCardex moDialogLogsCardex;
    private SDialogMaterialRequestDocsCardex moDialogDocsCardex;
    private SDialogMaterialRequestEstimationCardex moDialogEstimationKardex;
    private boolean mbHasAdmRight;
    private String msSeekQueryText;
    private JTextField moTextToSearch;
    
    boolean hasRightMatReqProv;
    boolean hasRightMatReqPur;
    
    /**
     * @param client GUI client.
     * @param type
     * @param subtype
     * @param title View's GUI tab title.
     * @param params
     */
    public SViewMaterialRequestPending(SGuiClient client, int type, int subtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, type, subtype, title, params);
        initComponents();
    }
    
    private void initComponents() {
        
        hasRightMatReqProv =((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface)miClient, SDataConstantsSys.PRV_INV_REQ_MAT_PROV).HasRight;
        hasRightMatReqPur = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface)miClient, SDataConstantsSys.PRV_INV_REQ_MAT_PUR).HasRight;
        
        jbPrint = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT), "Imprimir", this);
        jbAuthCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver cárdex de autorizaciones", this);
        jbLogCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_detail.gif")), "Bitácora de cambios", this);
        jbSegregate = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_lock.gif")), "Apartar/Liberar", this);
        mjbSupply = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_dps_stk_out.gif")), "Suministrar", this);
        mjbCloseOpenSupply = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_rm_con_del.gif")), 
                (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED || mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED_DETAIL ? "Abrir suministro" : "Cerrar suministro"), this);
        mjbCloseOpenPurchase = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_rm_con.gif")), 
                (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_PUR ? "Cerrar compras" : "Abrir compras"), this);        
        mjbToSupply = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_left.gif")), "Regresar a suministro", this);
        mjbToPur = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_move_right.gif")), "Enviar a compra", this);
        mjbToEstimate = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_money_out.gif")), "Cotizar", this);
        //mjbEstimationKardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex_money.gif")), "Ver solicitudes de cotización", this);
        jbDocsCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_type.gif")), "Ver documentos", this);
        mjbToSearch = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/switch_filter.gif")), "Filtar", this);
        mjbCleanSearch = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif")), "Quitar filtro", this);
        mjbToNew = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_return.gif")), "Regresar al solicitante", this);
//        mjbClose = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_CLOSE));
//        mjbOpen = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_OPEN));
//        mjbClose.setToolTipText("Cerrar para suministro");
//        mjbOpen.setToolTipText("Abrir para suministro");
//        mjbClose.setSize(23, 23);
//        mjbOpen.setSize(23, 23);
        mbHasAdmRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_ADMOR).HasRight;
        
        jbRowNew.setEnabled(false);
        jbRowDelete.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowCopy.setEnabled(false);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrint);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthCardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbLogCardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSegregate);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbSupply);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbCloseOpenSupply);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbCloseOpenPurchase);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbToSupply);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbToPur);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbToEstimate);
//        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbEstimationKardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbDocsCardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbToNew);
        moTextToSearch = new JTextField("");
        moTextToSearch.setPreferredSize(new Dimension(150, 23));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moTextToSearch);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbToSearch);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbCleanSearch);
        
//        jbPrint.setEnabled(true);
//        jbAuthCardex.setEnabled(true);
//        jbLogCardex.setEnabled(true);
//        jbSegregate.setEnabled(true);
//        mjbSupply.setEnabled(mnGridSubtype == SLibConstants.UNDEFINED || mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_DETAIL );
//        mjbCloseOpenSupply.setEnabled(mnGridSubtype == SLibConstants.UNDEFINED || mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED);
//        mjbToSupply.setEnabled(mnGridType == SModConsts.TRNX_MAT_REQ_PEND_PUR);
//        mjbToPur.setEnabled(mnGridType == SModConsts.TRNX_MAT_REQ_PEND_SUP && mnGridSubtype == SLibConsts.UNDEFINED);
//        mjbToEstimate.setEnabled(mnGridType == SModConsts.TRNX_MAT_REQ_PEND_PUR);
//        mjbToNew.setEnabled(mnGridSubtype == SLibConsts.UNDEFINED);
//        jbDocsCardex.setEnabled(true);
//        
        if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_SUP && (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED || mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED_DETAIL)){
            moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
//        
        if (mnGridType == SModConsts.TRNX_MAT_REQ_CLO_PUR) {
            moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
//            mjbSupply.setEnabled(false);
//            jbSegregate.setEnabled(false);
//            jbRowEdit.setEnabled(false);
        }
        
        jbPrint.setEnabled(false);
        jbAuthCardex.setEnabled(false);
        jbLogCardex.setEnabled(false);
        jbSegregate.setEnabled(false);
        mjbSupply.setEnabled(false);
        mjbCloseOpenSupply.setEnabled(false);
        mjbCloseOpenPurchase.setEnabled(false);
        mjbToNew.setEnabled(false);
        mjbToSupply.setEnabled(false);
        mjbToPur.setEnabled(false);
        mjbToEstimate.setEnabled(false);
        jbDocsCardex.setEnabled(false);
        mjbToSearch.setEnabled(false);
        mjbCleanSearch.setEnabled(false);
        
        moDialogSupply = new SDialogMaterialRequestSupply(miClient, "Surtidos de la requisición");
        moDialogLogsCardex = new SDialogMaterialRequestLogsCardex(miClient, "Bitácora de cambios");
        moDialogDocsCardex = new SDialogMaterialRequestDocsCardex(miClient, "Documentos de la requisición");
        moDialogAuthCardex = new SDialogAuthorizationCardex(miClient, "Cardex de autorizaciones");
        moDialogSegregations = new SDialogMaterialRequestSegregation(miClient, "Apartados de la requisición");
        //moDialogEstimationKardex = new SDialogMaterialRequestEstimationCardex(miClient, "Solicitudes de cotización");
        
        if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_SUP && hasRightMatReqProv) {
            jbPrint.setEnabled(true);
            jbAuthCardex.setEnabled(true);
            jbLogCardex.setEnabled(true);
            mjbToSupply.setEnabled(false);
            mjbToEstimate.setEnabled(false);
            //mjbEstimationKardex.setEnabled(false);
            jbDocsCardex.setEnabled(true);
            mjbToSearch.setEnabled(true);
            mjbCleanSearch.setEnabled(true);
            mjbCloseOpenPurchase.setEnabled(false);
            if (mnGridSubtype == SLibConstants.UNDEFINED) {
                jbSegregate.setEnabled(true);
                mjbSupply.setEnabled(true);
                mjbCloseOpenSupply.setEnabled(true);
                mjbToNew.setEnabled(true);
                mjbToPur.setEnabled(true);
            }
            else if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_DETAIL) {
                jbSegregate.setEnabled(false);
                mjbSupply.setEnabled(true);
                mjbCloseOpenSupply.setEnabled(true);
                mjbToNew.setEnabled(false);
                mjbToPur.setEnabled(false);
            }
            else {
                jbSegregate.setEnabled(false);
                mjbSupply.setEnabled(true);
                mjbCloseOpenSupply.setEnabled(true);
                mjbToNew.setEnabled(false);
                mjbToPur.setEnabled(false);
            }
        }
        if ((mnGridType == SModConsts.TRNX_MAT_REQ_CLO_PUR || mnGridType == SModConsts.TRNX_MAT_REQ_PEND_PUR) && hasRightMatReqPur) {
            jbPrint.setEnabled(true);
            jbAuthCardex.setEnabled(true);
            jbLogCardex.setEnabled(true);
            mjbToEstimate.setEnabled(false);
            jbDocsCardex.setEnabled(true);
            mjbToSearch.setEnabled(true);
            mjbCleanSearch.setEnabled(true);
            mjbToPur.setEnabled(false);
            jbSegregate.setEnabled(false);
            mjbSupply.setEnabled(false);
            mjbCloseOpenSupply.setEnabled(false);
            mjbCloseOpenPurchase.setEnabled(true);
            if (mnGridSubtype == SLibConstants.UNDEFINED) {
                mjbToSupply.setEnabled(true);
                mjbToNew.setEnabled(true);
                mjbToEstimate.setEnabled(true);
            }
            else if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_DETAIL) {
                mjbToNew.setEnabled(true);
                mjbToSupply.setEnabled(true);
                mjbToEstimate.setEnabled(true);
                
            }
        }

        msSeekQueryText = "";
    }
    
    private void actionSupply() {
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
                    int[] key = (int[]) gridRow.getRowPrimaryKey();
                    
                    moDialogSupply.setFormParams(key, SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE);
                    moDialogSupply.setVisible(true);
                    
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionCloseOpenToSupply() {
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
                int[] key = (int[]) gridRow.getRowPrimaryKey();
                String message = SMaterialRequestUtils.openOrCloseToSupply(miClient.getSession(), key);
                if (! message.isEmpty()) {
                    miClient.showMsgBoxError(message);
                }

                miClient.getSession().notifySuscriptors(mnGridType);
            }
        }
    }
    
    private void actionCloseOpenToPurchase() {
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
                int[] key = (int[]) gridRow.getRowPrimaryKey();
                String message = SMaterialRequestUtils.openOrCloseToPurchase(miClient.getSession(), key);
                if (! message.isEmpty()) {
                    miClient.showMsgBoxError(message);
                }

                miClient.getSession().notifySuscriptors(mnGridType);
            }
        }
    }
    
    private void actionToNew() {
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
                    int[] key = (int[]) gridRow.getRowPrimaryKey();
                    boolean est = SMaterialRequestUtils.hasMatReqEstimation(miClient.getSession(), key);
                    if (miClient.showMsgBoxConfirm((est ? "Al regresar al solicitante se corre el riesgo de que se eliminen la(s) solicitud(es) de cotización al haber cambio de ítem o eliminación de la partida.\n" : "") + 
                            "¿Esta seguro/a de regresar al solicitante?") == JOptionPane.OK_OPTION) {
                        String message = SMaterialRequestUtils.hasLinksMaterialRequest(miClient.getSession(), key);
                        if (! message.isEmpty()) {
                            miClient.showMsgBoxInformation("No se pudo completar la acción.\n" + message);
                        }
                        else {
                            message = SMaterialRequestUtils.updateStatusOfMaterialRequest(miClient.getSession(), key, SModSysConsts.TRNS_ST_MAT_REQ_NEW);
                            if (! message.isEmpty()) {
                                miClient.showMsgBoxError(message);
                            }
                            
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                }
                catch (SQLException ex) {
                    Logger.getLogger(SViewMaterialRequestPending.class.getName()).log(Level.SEVERE, null, ex);
                    SLibUtils.showException(this, ex);
                }
            }
        }
    }
    
    private void actionLog() {
        int[] key;
        
        if (jbLogCardex.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else {
                        key = (int[]) gridRow.getRowPrimaryKey();
                    
                        moDialogLogsCardex.setFormParams(key[0]);
                        moDialogLogsCardex.setVisible(true);
                    }
                }
                catch (Exception ex) {
                    Logger.getLogger(SViewMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void actionSegregateOrRelease() {
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
                    int[] key = (int[]) gridRow.getRowPrimaryKey();
                    
                    moDialogSegregations.setFormParams(key);
                    moDialogSegregations.setVisible(true);
                    
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionPrint() {
        if (jbPrint.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    try {
                        print(gridRow.getRowPrimaryKey()[0]);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }
    
    private void actionCardex() {
        int[] key;
        
        if (jbAuthCardex.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else {
                        key = (int[]) gridRow.getRowPrimaryKey();
                    
                        moDialogAuthCardex.setFormParams(SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST, SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ), key);
                        moDialogAuthCardex.setVisible(true);
                    }
                }
                catch (Exception ex) {
                    Logger.getLogger(SViewMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void print(int idMatReq) throws Exception {
        HashMap<String, Object> params;
        
        params = miClient.createReportParams();
        params.put("nMatReqId", idMatReq);
        
        miClient.getSession().printReport(SModConsts.TRN_MAT_REQ, SLibConsts.UNDEFINED, null, params);
    }
    
    private void actionToPur() {
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
                if (miClient.showMsgBoxConfirm("¿Esta seguro/a de enviar la requisición a compra?") == JOptionPane.OK_OPTION) {
                    int[] key = (int[]) gridRow.getRowPrimaryKey();
                    String message = SMaterialRequestUtils.updateStatusOfMaterialRequest(miClient.getSession(), key, SModSysConsts.TRNS_ST_MAT_REQ_PUR);
                    if (! message.isEmpty()) {
                        miClient.showMsgBoxError(message);
                    }

                    miClient.getSession().notifySuscriptors(mnGridType);
                }
            }
        }
    }
    
    private void actionToSupply() {
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
                if (miClient.showMsgBoxConfirm("¿Esta seguro/a de regresar la requisición a suministro?") == JOptionPane.OK_OPTION) {
                    int[] key = (int[]) gridRow.getRowPrimaryKey();
                    String message = SMaterialRequestUtils.updateStatusOfMaterialRequest(miClient.getSession(), key, SModSysConsts.TRNS_ST_MAT_REQ_PROV);
                    if (! message.isEmpty()) {
                        miClient.showMsgBoxError(message);
                    }

                    miClient.getSession().notifySuscriptors(mnGridType);
                }
            }
        }
    }
    
    private void actionToEstimate() {
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
                moDialogEstimate = new SDialogMaterialRequestEstimation(miClient, "Cotizar requisición de materiales");
                int[] key = (int[]) gridRow.getRowPrimaryKey();
                moDialogEstimate.setValue(SModConsts.TRN_MAT_REQ, key);
                moDialogEstimate.setVisible(true);

                miClient.getSession().notifySuscriptors(mnGridType);
            }
        }
    }
    
    private void actionDocsKardex() {
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
                    int[] key = (int[]) gridRow.getRowPrimaryKey();
                    
                    moDialogDocsCardex.setFormParams(key[0]);
                    moDialogDocsCardex.setVisible(true);
                    
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionSearch() {
        if (jtTable.getRowCount() > 1) {
            String text = moTextToSearch.getText().trim();
            if (text.length() > 0) {
                msSeekQueryText = "(LPAD(v.num, 6, 0) LIKE '%" + text + "%' OR "
                        + "v.dt LIKE '%" + text + "%' OR "
                        + "ur.usr LIKE '%" + text + "%' OR "
                        + "ref LIKE '%" + text + "%' OR "
                        + "bmu.bp LIKE '%" + text + "%' OR "
                        + "smr.name LIKE '%" + text + "%' ";
                
                if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_DETAIL || mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED_DETAIL) {
                    msSeekQueryText += "OR i.item LIKE '%" + text + "%' "
                            + "OR i.item_key LIKE '%" + text + "%' "
                            + "OR u.unit LIKE '%" + text + "%' "
                            + "OR IF(ISNULL(entc.name), trn_get_cons_info(v.id_mat_req, 1), entc.name) LIKE '%" + text + "%' ";
                }
                
                msSeekQueryText += ") ";
            }
            else {
                msSeekQueryText = "";
            }

            moTextToSearch.requestFocus();
            actionGridReload();
        }
    }
    
    private void actionCleanSearch() {
        moTextToSearch.setText("");
        msSeekQueryText = "";
        moTextToSearch.requestFocus();
        actionGridReload();
    }
    
    private void actionEstRequestKardex() {
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
                moDialogEstimationKardex = new SDialogMaterialRequestEstimationCardex(miClient, "Solicitudes de cotización");
                int[] key = (int[]) gridRow.getRowPrimaryKey();
                moDialogEstimationKardex.setValue(SModConsts.TRN_MAT_REQ_ETY, key);
                moDialogEstimationKardex.setVisible(true);
            }
        }
    }
    
    @Override
    public void prepareSqlQuery() {
        String where = "";
        String subWhere = "";
        String join = "";
        String groupOrderBy = "";
        String subGroupOrderBy = "";
        String select = "";
        String having = "";
        Object filter;
        int usrId = miClient.getSession().getUser().getPkUserId();
        
        moPaneSettings = new SGridPaneSettings(1);

        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "v.b_del = 0 AND ve.b_del = 0 ";
        }

        
        where += (where.isEmpty() ? "" : "AND ") + "(cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + 
                ", '" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.id_mat_req, NULL, NULL, NULL, NULL) = " +
                SAuthorizationUtils.AUTH_STATUS_AUTHORIZED + " " +
                "OR cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + 
                ", '" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.id_mat_req, NULL, NULL, NULL, NULL) = " +
                SAuthorizationUtils.AUTH_STATUS_NA + ") ";
        
        if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_SUP) {
            if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_DETAIL) {
                select = "i.item, i.item_key, i.part_num, u.unit, ve.id_ety, "
                        + "SUM(ve.qty) AS org_qty, "
                        + "COALESCE(SUM(de.sumi_qty), 0) AS sumi_qty, "
                        + "COALESCE(SUM(ve.qty) - de.sumi_qty, SUM(ve.qty)) AS pen_sumi_qty, "
                        + "COALESCE(SUM(de.sumi_qty), 0) / SUM(ve.qty) AS per, "
                        + "rpe.name AS ety_pty, "
                        + "ve.dt_req_n, ";
                join += "INNER JOIN erp.itmu_item AS i ON ve.fk_item = i.id_item "
                        + "INNER JOIN erp.itmu_unit AS u ON ve.fk_unit = u.id_unit "
                        + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS rpe ON ve.fk_mat_req_pty_n = rpe.id_mat_req_pty ";
                groupOrderBy = "ve.id_mat_req, ve.id_ety ";
                subGroupOrderBy = "de.fid_mat_req_n, de.fid_mat_req_ety_n ";
                where += "AND v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PROV + " AND NOT v.b_clo_prov AND v.tp_req = '" + SModSysConsts.TRNS_MAT_REQ_TP_C + "' ";
//                subWhere += "AND v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PROV + " AND NOT v.b_clo_prov  ";
                having = "HAVING per < 1 "; // Descomentar para mostrar unicamente los que faltan por suministrar
            }
            else if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED) {
                select = "COUNT(ve.id_ety) AS ety, " 
                        + "COALESCE(SUM(de.sumi_qty), 0) / SUM(ve.qty) AS per_sumi, " 
                        + "1 - COALESCE(SUM(de.sumi_qty), 0) / SUM(ve.qty) AS per_x_sumi, ";
                where += (where.isEmpty() ? "" : "AND ") + " v.tp_req = '" + SModSysConsts.TRNS_MAT_REQ_TP_C + "' ";
                groupOrderBy = "v.id_mat_req, v.dt, v.num ";
                subGroupOrderBy = "de.fid_mat_req_n ";
                having = "HAVING (per_sumi >= 1 OR v.b_clo_prov) && v.b_clo_pur ";
                if (moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD) != null) {
                    filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
                    if (filter != null) {
                        where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
                    }
                }
            }
            else if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED_DETAIL) {
                select = "i.item, i.item_key, i.part_num, u.unit, ve.id_ety, "
                        + "SUM(ve.qty) AS org_qty, "
                        + "COALESCE(SUM(de.sumi_qty), 0) AS sumi_qty, "
                        + "COALESCE(SUM(ve.qty) - de.sumi_qty, SUM(ve.qty)) AS pen_sumi_qty, "
                        + "COALESCE(SUM(de.sumi_qty), 0) / SUM(ve.qty) AS per, "
                        + "rpe.name AS ety_pty, "
                        + "ve.dt_req_n, ";
                join += "INNER JOIN erp.itmu_item AS i ON ve.fk_item = i.id_item "
                        + "INNER JOIN erp.itmu_unit AS u ON ve.fk_unit = u.id_unit "
                        + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS rpe ON ve.fk_mat_req_pty_n = rpe.id_mat_req_pty ";
                groupOrderBy = "ve.id_mat_req, ve.id_ety ";
                where += (where.isEmpty() ? "" : "AND ") + " v.tp_req = '" + SModSysConsts.TRNS_MAT_REQ_TP_C + "' ";
//                subWhere += "AND v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PROV + " AND NOT v.b_clo_prov  ";
                having = "HAVING (per >= 1 OR v.b_clo_prov) && v.b_clo_pur ";
                if (moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD) != null) {
                    filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
                    if (filter != null) {
                        where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
                    }
                }
            }
            else if (mnGridSubtype == SLibConsts.UNDEFINED) {
                select = "COUNT(ve.id_ety) AS ety, " 
                        + "COALESCE(SUM(de.sumi_qty), 0) / SUM(ve.qty) AS per_sumi, " 
                        + "1 - COALESCE(SUM(de.sumi_qty), 0) / SUM(ve.qty) AS per_x_sumi, ";
                where += "AND v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PROV + " AND NOT v.b_clo_prov AND v.tp_req = '" + SModSysConsts.TRNS_MAT_REQ_TP_C + "' ";
//                subWhere += "AND v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PROV + " AND NOT v.b_clo_prov  ";
                groupOrderBy = "v.id_mat_req, v.dt, v.num ";
                having = "HAVING per_sumi < 1 ";
            }
        }
        else if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_PUR || mnGridType == SModConsts.TRNX_MAT_REQ_CLO_PUR) {
            select = "SUM(ve.qty) AS org_qty, "
                    + "COALESCE(SUM(de.sumi_qty), 0) AS sumi_qty, "
                    + "COALESCE(SUM(de.sumi_qty), 0) / SUM(ve.qty) AS per_sumi, " 
                    + "1 - COALESCE(SUM(de.sumi_qty), 0) / SUM(ve.qty) AS per_x_sumi, "
                    + "IF(COALESCE(SUM(req_pur.pur_qty), 0) > (SUM(ve.qty) - COALESCE(SUM(de.sumi_qty), 0)), (SUM(ve.qty) - COALESCE(SUM(de.sumi_qty), 0)), COALESCE(SUM(req_pur.pur_qty), 0)) / (SUM(ve.qty) - COALESCE(SUM(de.sumi_qty), 0)) AS per_pur, "
                    + "IF((1 - COALESCE(SUM(req_pur.pur_qty), 0) / (SUM(ve.qty) - COALESCE(SUM(de.sumi_qty), 0))) < 0, 0, "
                    + "1 - COALESCE(SUM(req_pur.pur_qty), 0) / (SUM(ve.qty) - COALESCE(SUM(de.sumi_qty), 0))) AS per_x_pur, ";
            
            if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_DETAIL) {
                select += "i.item, i.item_key, i.part_num, u.unit, ve.id_ety, "
                        + "COALESCE(SUM(ve.qty) - de.sumi_qty, SUM(ve.qty)) AS pen_sumi_qty, "
                        + "COALESCE(SUM(de.sumi_qty), 0) / SUM(ve.qty) AS per, "
                        + "COALESCE(SUM(req_pur.pur_qty), 0) AS pur_qty,"
                        + "COALESCE(IF((SUM(ve.qty) - COALESCE(SUM(de.sumi_qty), 0) - COALESCE(SUM(req_pur.pur_qty), 0)) < 0, 0, (SUM(ve.qty) - COALESCE(SUM(de.sumi_qty), 0) - COALESCE(SUM(req_pur.pur_qty), 0))), "
                        + "SUM(ve.qty) - COALESCE(SUM(de.sumi_qty), 0)) AS pen_pur_qty, "
                        + "rpe.name AS ety_pty, "
                        + "ve.dt_req_n, ";
                join += "INNER JOIN erp.itmu_item AS i ON ve.fk_item = i.id_item "
                        + "INNER JOIN erp.itmu_unit AS u ON ve.fk_unit = u.id_unit "
                        + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS rpe ON ve.fk_mat_req_pty_n = rpe.id_mat_req_pty ";
                groupOrderBy = "ve.id_mat_req, ve.id_ety ";
                //subWhere += "AND v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PROV + " AND NOT v.b_clo_prov  ";
                //having = "HAVING per_pur < 1 "; // Descomentar para mostrar unicamente los que faltan por suministrar
            }
            else if (mnGridSubtype == SLibConsts.UNDEFINED) {
                select += "COUNT(ve.id_ety) AS ety, ";
                groupOrderBy = "v.id_mat_req, v.dt, v.num ";
                //having = "HAVING per_pur < 1 ";
            }
            
            if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_PUR) {
                where += "AND v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PUR + " "
                        + "AND v.fk_st_mat_pur = " + SModSysConsts.TRNS_ST_MAT_PUR_PEND + " "
                        + "AND NOT v.b_clo_pur ";
                
                having += "HAVING (org_qty - sumi_qty) > COALESCE(SUM(req_pur.pur_qty), 0) ";
            }
            else if (mnGridType == SModConsts.TRNX_MAT_REQ_CLO_PUR) {
                // Se comenta el filtro del estatus ya que actualmente no se actualiza por sí solo cuando una RM está completamente "comprada"
//                where += "AND (v.fk_st_mat_pur = " + SModSysConsts.TRNS_ST_MAT_PUR_DONE + ") ";
                filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
                if (filter != null) {
                    where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
                }
                
                having += "HAVING (COALESCE(SUM(req_pur.pur_qty), 0) >= (org_qty - sumi_qty) AND COALESCE(SUM(req_pur.pur_qty), 0) > 0) "
                        + "OR (v.b_clo_pur AND v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PUR + ") ";
            }
        }
        
        subGroupOrderBy = "de.fid_mat_req_n, de.fid_mat_req_ety_n ";
        
        subWhere += "AND d.fid_ct_iog IN (" + SModSysConsts.TRNS_TP_IOG_IN_DEV_CONS[0] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_SUPP_CONS[0] + ") ";
        subWhere += "AND d.fid_cl_iog IN (" + SModSysConsts.TRNS_TP_IOG_IN_DEV_CONS[1] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_SUPP_CONS[1] + ") ";
        subWhere += "AND d.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_IN_DEV_CONS[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_SUPP_CONS[2] + ") ";
//        Se quita esta consulta porque demoraba mas de 26 segundos en ejecutarse, se optimizo po el EXISTS42          
//        if (usrId != 2 || !mbHasAdmRight) { // SUPER
//            join += "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT_USR) + " AS peu ON "  
//                    +  "pe.id_mat_prov_ent = peu.id_mat_prov_ent ";
//            where += (where.isEmpty() ? "" : "AND ") + "peu.id_usr = " + usrId + " ";
//        }
        
        if (usrId != 2 || !mbHasAdmRight) { // SUPER          
            where += (where.isEmpty() ? "" : "AND ") + " EXISTS (SELECT 1 FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT_USR) + " AS peu " +
            "WHERE pe.id_mat_prov_ent = peu.id_mat_prov_ent AND peu.id_usr = " + usrId + " )" ;
            
        }
        
        if (msSeekQueryText.length() > 0) {
            where += (where.isEmpty() ? "" : "AND ") + msSeekQueryText;
        }
        
        msSql = "SELECT v.id_mat_req AS " + SDbConsts.FIELD_ID + "1, "
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "LPAD(v.num, 6, 0) AS folio, "
                + "v.dt_req_n, "
                + "v.ref, "
                + "v.b_ext_sys, "
                + "v.b_clo_prov, "
                + "v.b_clo_pur, "
                + select
                + "pe.name AS prov_ent, "
                + "rp.name AS req_pty, "
                + "smr.name AS status, "
                + "ur.usr AS usr_req, "
                + "bmu.bp AS contractor, "
                + "IF(ISNULL(entc.name), trn_get_cons_info(v.id_mat_req, 1), entc.name) AS ent_cons, "
                + "IF(ISNULL(sentc.name), trn_get_cons_info(v.id_mat_req, 2), sentc.name) AS s_ent_cons, "
                + "IF(ISNULL(fcc.id_cc), trn_get_cons_info(v.id_mat_req, 3), fcc.id_cc) AS f_cc, "
                + "(SELECT  " +
                    "COUNT(*) > 0 " +
                    "FROM " +
                    "" + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " dmr " +
                    "INNER JOIN " +
                    "" + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " ety ON dmr.fid_dps_year = ety.id_year " +
                    "AND dmr.fid_dps_doc = ety.id_doc " +
                    "AND dmr.fid_dps_ety = ety.id_ety " +
                    "INNER JOIN " +
                    "" + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " dps ON ety.id_year = dps.id_year " +
                    "AND ety.id_doc = dps.id_doc " +
                    "WHERE " +
                    "NOT ety.b_del AND NOT dps.b_del " +
                    "AND dmr.fid_mat_req = v.id_mat_req " +
                    "AND ((dps.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[0] + " " +
                    "AND dps.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[1] + " " +
                    "AND dps.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_ORD[2] + ") " +
                    "OR (dps.fid_ct_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[0] + " " +
                    "AND dps.fid_cl_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[1] + " " +
                    "AND dps.fid_tp_dps = " + SModSysConsts.TRNU_TP_DPS_PUR_INV[2] + "))) AS comp_doc, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_st_mat_req, "
                + "v.fk_usr_clo_prov, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_clo_prov, "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "uc.usr AS usr_clo, "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS v "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " AS ve ON "
                + "v.id_mat_req = ve.id_mat_req "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT) + " AS pe ON "
                + "v.fk_mat_prov_ent = pe.id_mat_prov_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS rp ON "
                + "v.fk_mat_req_pty = rp.id_mat_req_pty "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_REQ) + " AS smr ON "
                + "v.fk_st_mat_req = smr.id_st_mat_req "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON "
                + "v.fk_usr_req = ur.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAINT_USER) + " AS mu ON "
                + "v.fk_contractor_n = mu.id_maint_user "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT) + " AS entc ON "
                + "ve.fk_ent_mat_cons_ent_n = entc.id_mat_cons_ent "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT) + " AS sentc ON "
                + "ve.fk_subent_mat_cons_ent_n = sentc.id_mat_cons_ent AND ve.fk_subent_mat_cons_subent_n = sentc.id_mat_cons_subent "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " AS fcc ON "
                + "ve.fk_cc_n = fcc.pk_cc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bmu ON "
                + "mu.id_maint_user = bmu.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON "
                + "v.fk_usr_clo_prov = uc.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT JOIN (SELECT de.fid_mat_req_n, de.fid_mat_req_ety_n, SUM(de.qty * IF(d.fid_ct_iog = 1, -1, 1)) AS sumi_qty "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS v ON de.fid_mat_req_n = v.id_mat_req " 
                + "WHERE de.fid_mat_req_n IS NOT NULL AND de.fid_mat_req_ety_n IS NOT NULL " 
                + "AND NOT de.b_del AND NOT d.b_del " 
                + subWhere
                + "GROUP BY " + subGroupOrderBy + " " 
                + "ORDER BY " + subGroupOrderBy + " ) AS de ON " 
                + "ve.id_mat_req = de.fid_mat_req_n AND ve.id_ety = de.fid_mat_req_ety_n ";
        
        msSql += "LEFT JOIN (SELECT "
                + "ddmr.fid_mat_req, "
                + "ddmr.fid_mat_req_ety, "
                + "SUM(ddmr.qty) AS pur_qty "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " AS ddmr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS dpsety ON "
                + "ddmr.fid_dps_year = dpsety.id_year AND ddmr.fid_dps_doc = dpsety.id_doc AND ddmr.fid_dps_ety = dpsety.id_ety "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS dps ON "
                + "dpsety.id_year = dps.id_year AND dpsety.id_doc = dps.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " AS mre ON "
                + "ddmr.fid_mat_req = mre.id_mat_req AND ddmr.fid_mat_req_ety = mre.id_ety "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS mr ON "
                + "mre.id_mat_req = mr.id_mat_req "
                + "WHERE "
                + "NOT dps.b_del AND NOT dpsety.b_del AND NOT mr.b_del "
                + "AND dps.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] + " "
                + "AND dps.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] + " "
                + "AND dps.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] + " "
                + "GROUP BY ddmr.fid_mat_req " + (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_DETAIL || mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED_DETAIL ? ", ddmr.fid_mat_req_ety " : "")
                + "ORDER BY ddmr.fid_mat_req) AS req_pur ON "
                + "ve.id_mat_req = req_pur.fid_mat_req AND ve.id_ety = req_pur.fid_mat_req_ety ";
        msSql += join
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "GROUP BY " + groupOrderBy + " " 
                + having
                + "ORDER BY " + groupOrderBy ;
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "prov_ent", "Centro Suministro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "folio", "Folio"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_req", "Solicitante"));
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_DETAIL || mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED_DETAIL) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "id_ety", "Número partida"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "ety_pty", "Prioridad partida"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "ve.dt_req_n", "Fecha requerida partida"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "i.item_key", "Clave"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "i.item", "Ítem"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "i.part_num", "# parte"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_3D, "org_qty", "Cant. requerida"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_3D, "sumi_qty", "Cant. suministrada"));
            if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_SUP) {
                columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_3D, "pen_sumi_qty", "Cant. pendiente suministrar"));
            }
            if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_PUR || mnGridType == SModConsts.TRNX_MAT_REQ_CLO_PUR) {
                columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_3D, "pur_qty", "Cant. comprada"));
                columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_3D, "pen_pur_qty", "Cant. pendiente comprar"));
            }
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "unit", "Unidad"));
            if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_SUP) {
                columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "per", "% suministro"));
            }
        }
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED || mnGridSubtype == SLibConsts.UNDEFINED) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "req_pty", "Prioridad"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_req_n", "Fecha requerida"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "contractor", "Contratista"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_INT_2B, "ety", "Partidas"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "per_sumi", "% suministro"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "per_x_sumi", "% x suministrar"));
        }
        if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_PUR || mnGridType == SModConsts.TRNX_MAT_REQ_CLO_PUR) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "per_pur", "% comprado"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_PER_2D, "per_x_pur", "% x comprar"));
        }
        
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_DETAIL || mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED_DETAIL) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "ent_cons", "Centro consumo"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "s_ent_cons", "Subcentro consumo"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_cc", "Centro de costo"));
        }
        
        if (mnGridType == SModConsts.TRNX_MAT_REQ_PEND_PUR || mnGridType == SModConsts.TRNX_MAT_REQ_CLO_PUR) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_clo_pur", "Cerrado compras"));
        }
        
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "comp_doc", "Doc compras"));
        
        if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PROVIDED) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b_clo_prov", "Cerrado suministro"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_clo", "Usr cerrado"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_usr_clo_prov", "Usr TS cerrado"));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        }
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.TRNU_MAT_REQ_PTY);
        moSuscriptionsSet.add(SModConsts.TRNS_ST_MAT_REQ);
        moSuscriptionsSet.add(SModConsts.TRN_MAINT_USER);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_PROV_ENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_ENT);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_CONS_SUBENT);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
        moSuscriptionsSet.add(SModConsts.CFGU_AUTHORN_STEP);
        moSuscriptionsSet.add(SModConsts.TRN_DIOG);
        moSuscriptionsSet.add(SModConsts.TRN_DIOG_ETY);
        moSuscriptionsSet.add(SModConsts.TRN_MAT_REQ);
        moSuscriptionsSet.add(SModConsts.TRNX_MAT_REQ_PEND_SUP);
        moSuscriptionsSet.add(SModConsts.TRNX_MAT_REQ_PEND_PUR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjbSupply) {
                actionSupply();
            }
            else if (button == mjbCloseOpenSupply) {
                actionCloseOpenToSupply();
            }
            else if (button == mjbCloseOpenPurchase) {
                actionCloseOpenToPurchase();
            }
            else if (button == mjbToNew) {
                actionToNew();
            }
            else if (button == jbPrint) {
                actionPrint();
            }
            else if (button == jbAuthCardex) {
                actionCardex();
            }
            else if (button == jbLogCardex) {
                actionLog();
            }
            else if (button == jbSegregate) {
                actionSegregateOrRelease();
            }
            else if (button == mjbToSupply) {
                actionToSupply();
            }
            else if (button == mjbToPur) {
                actionToPur();
            }
            else if (button == mjbToEstimate) {
                actionToEstimate();
            }
            else if (button == jbDocsCardex) {
                actionDocsKardex();
            }
            else if (button == mjbToSearch) {
                actionSearch();
            }
            else if (button == mjbCleanSearch) {
                actionCleanSearch();
            }
//            else if (button == mjbEstimationKardex) {
//                actionEstRequestKardex();
//            }
        }
    }
}
