/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.form.SFormCapturingNotes;
import erp.gui.grid.SGridFilterPanelMatReqStatus;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.trn.db.SDbMaterialRequest;
import erp.mod.trn.db.SDbMaterialRequestNote;
import erp.mod.trn.db.SMaterialRequestUtils;
import erp.mod.trn.form.SDialogAuthorizationCardex;
import erp.mod.trn.form.SDialogDocumentAuthornComments;
import erp.mod.trn.form.SDialogMaterialRequestDocsCardex;
import erp.mod.trn.form.SDialogMaterialRequestLogsCardex;
import erp.mod.trn.form.SDialogMaterialRequestSegregation;
import erp.mod.trn.form.SFormMaterialRequest;
import erp.mtrn.data.STrnStockSegregationUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import sa.lib.grid.SGridRow;
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
public class SViewMaterialRequest extends SGridPaneView implements ActionListener {

    private JButton jbNewSupReq;
    private JButton jbPrint;
    private JButton jbAuthCardex;
    private JButton jbLogCardex;
    private JButton jbAuthorize;
    private JButton jbHardAuthorize;
    private JButton jbReject;
    private JButton jbSegregate;
    private JButton jbDocsCardex;
    private JButton jbToNew;
    private JButton jbAuthComments;
    private JButton jbEditNotes;
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SGridFilterPanelMatReqStatus moFilterMatReqStatus;
    private SDialogAuthorizationCardex moDialogAuthCardex;
    private SDialogMaterialRequestLogsCardex moDialogLogsCardex;
    private SDialogMaterialRequestSegregation moDialogSegregations;
    private SDialogMaterialRequestDocsCardex moDialogDocsCardex;
    
    private boolean hasSupReq;
    private boolean hasAuthRight;
    private boolean hasPetSupRight;
    private boolean hasMatReqAdmorRight;
    private boolean hasMatReqPurRight;
    private boolean hasMatReqProvRight;
    private boolean hasMatReqReclassRight;
    
    /**
     * @param client GUI client.
     * @param subType
     * @param title View's GUI tab title.
     * @param params
     */
    public SViewMaterialRequest(SGuiClient client, int subType, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, subType != SModConsts.TRNX_MAT_REQ_RECLASS ? SModConsts.TRN_MAT_REQ : SModConsts.TRNX_MAT_REQ_RECLASS, subType, title, params);
        initComponents();
    }
    
    private void initComponents() {
        setRowButtonsEnabled(true);
        
        hasSupReq = (((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_REQ).HasRight && 
                ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_PROV).HasRight);
        hasAuthRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_REV).HasRight;
        hasPetSupRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_REQ).HasRight &&
                ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_PROV).HasRight;
        hasMatReqAdmorRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_ADMOR).HasRight;
        hasMatReqPurRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_PUR).HasRight;
        hasMatReqProvRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_PROV).HasRight;
        hasMatReqReclassRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_RECLASS).HasRight;
        
        jbNewSupReq = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_NEW_MAIN), "Nueva RM de resurtido", this);
        jbPrint = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_PRINT), "Imprimir", this);
        jbAuthCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver cárdex de autorizaciones", this);
        jbLogCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_detail.gif")), "Ver bitácora de cambios", this);
        jbAuthorize = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_thumbs_up.gif")), "Autorizar", this);
        jbHardAuthorize = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_thumbs_up_c.gif")), "Autorización forzada", this);
        jbReject = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_thumbs_down.gif")), "Rechazar", this);
        jbSegregate = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_lock.gif")), "Apartar/Liberar", this);
        jbDocsCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_doc_type.gif")), "Ver documentos relacionados", this);
        jbToNew = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_return.gif")), "Regresar al solicitante", this);
        jbAuthComments = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_auth_notes_ora.gif")), "Ver comentarios de autorización de pedidos de compras", this);
        jbEditNotes = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_edit_ro.gif")), "Modificar notas", this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_LEFT).add(jbNewSupReq);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrint);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthCardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbLogCardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthorize);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbReject);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbHardAuthorize);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSegregate);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbDocsCardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbToNew);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAuthComments);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbEditNotes);
        
        jbNewSupReq.setEnabled(true);
        jbPrint.setEnabled(true);
        jbAuthCardex.setEnabled(true);
        jbLogCardex.setEnabled(true);
        jbAuthorize.setEnabled(false);
        jbReject.setEnabled(false);
        jbHardAuthorize.setEnabled(false);
        jbSegregate.setEnabled(false);
        jbDocsCardex.setEnabled(true);
        jbToNew.setEnabled(hasMatReqPurRight || hasMatReqProvRight || hasMatReqAdmorRight);
        jbEditNotes.setEnabled(mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_PET);

        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        moFilterMatReqStatus = new SGridFilterPanelMatReqStatus(miClient, this);
        moFilterMatReqStatus.initFilter(mnGridMode);
        
        if (mnGridMode == SLibConsts.UNDEFINED 
                || mnGridMode == SModConsts.TRN_MAT_CONS_ENT_USR
                || mnGridMode == SModConsts.TRN_MAT_PROV_ENT_USR) {
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterMatReqStatus);
        }
        else if (mnGridMode == SModSysConsts.TRNX_MAT_REQ_AUTHO
                || mnGridMode == SModSysConsts.TRNX_MAT_REQ_AUTHO_RECH
                || mnGridMode == SModSysConsts.TRNX_MAT_REQ_RECLASS) {
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
        
        moDialogAuthCardex = new SDialogAuthorizationCardex(miClient, "Cardex de autorizaciones");
        moDialogLogsCardex = new SDialogMaterialRequestLogsCardex(miClient, "Bitácora de cambios");
        moDialogSegregations = new SDialogMaterialRequestSegregation(miClient, "Apartados de la requisición");
        moDialogDocsCardex = new SDialogMaterialRequestDocsCardex(miClient, "Documentos de la requisición");
        
        if (mnGridMode != SModSysConsts.TRNS_ST_MAT_REQ_NEW) {
            jbRowNew.setEnabled(false);
            jbRowCopy.setEnabled(false);
            jbRowDisable.setEnabled(false);
            jbRowDelete.setEnabled(false);
            jbNewSupReq.setEnabled(false);
        }
        if (mnGridMode == SModSysConsts.TRNS_ST_MAT_REQ_AUTH && mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_REV) {
            jbAuthorize.setEnabled(hasAuthRight);
            jbReject.setEnabled(hasAuthRight);
            //jbSegregate.setEnabled(hasAuthRight);
        }
        
        if (mnGridMode == SModSysConsts.TRNS_ST_MAT_REQ_AUTH && mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_ADM) {
            moFilterMatReqStatus.initFilter(SModSysConsts.TRNS_ST_MAT_REQ_AUTH);
            moFilterMatReqStatus.setEnabled(false);
            jbHardAuthorize.setEnabled(hasMatReqAdmorRight);
            jbAuthorize.setEnabled(false);
            jbReject.setEnabled(false);
        }
        else if (mnGridMode != SModSysConsts.TRNX_MAT_REQ_RECLASS) {
            moFilterMatReqStatus.setEnabled(true);
            jbAuthorize.setEnabled(hasAuthRight);
            jbReject.setEnabled(hasAuthRight);
        }
        
        if (mnGridMode == SModSysConsts.TRNX_MAT_REQ_RECLASS) {
            jbRowEdit.setEnabled(hasMatReqReclassRight);
            jbToNew.setEnabled(false);
        }
        
        if (mnGridSubtype == SModConsts.TRNX_MAT_REQ_ALL) {
            jbRowEdit.setEnabled(false);
            jbAuthorize.setEnabled(false);
            jbReject.setEnabled(false);
            jbReject.setEnabled(false);
            jbToNew.setEnabled(false);
        }
        
        jbRowDisable.setToolTipText("Cancelar");
    }
    
    private void actionNewSupReq() {
        if (jbNewSupReq.isEnabled()) {
            try {
                SFormMaterialRequest form = new SFormMaterialRequest(miClient, "Requisición de materiales", SModConsts.TRNX_MAT_REQ_STK_SUP);
                form.setRegistry(new SDbMaterialRequest());
                form.setVisible(true);
                if (form.validateForm().isValid()) {
                    form.getRegistry().save(miClient.getSession());
                    this.refreshGridWithRefresh();
                }
            }
            catch(Exception e) {
                miClient.showMsgBoxError(e.getMessage());
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
                        HashMap<String, Object> params = SMaterialRequestUtils.createMatReqParamsMapPdf(miClient, gridRow.getRowPrimaryKey()[0]);
                        miClient.getSession().printReport(SModConsts.TRN_MAT_REQ, SLibConsts.UNDEFINED, null, params);
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
    
    private void actionAuthorizeOrRejectResource(final int iAction) {
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
                    SDbMaterialRequest req = new SDbMaterialRequest();
                    req.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                    String response = SAuthorizationUtils.authorizeOrReject(miClient.getSession(), 
                                                                    req.getPrimaryKey(), 
                                                                    SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST, 
                                                                    iAction);
                    if (response.length() > 0) {
                            miClient.showMsgBoxError(response);
                    }
                    else {
                        miClient.showMsgBoxInformation((iAction == SAuthorizationUtils.AUTH_ACTION_AUTHORIZE ? "Autorizado" : "Rechazado") + 
                                " con éxito");
                        req.save(miClient.getSession());
                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void actionHardAuthorize() {
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
                    SDbMaterialRequest req = new SDbMaterialRequest();
                    req.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                    String response = SAuthorizationUtils.hardAuthorize(miClient.getSession(), 
                                                                    req.getPrimaryKey(), 
                                                                    SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST,
                                                                    miClient.getSession().getUser().getPkUserId());
                    if (response.length() > 0) {
                        miClient.showMsgBoxError(response);
                    }
                    else {
                        miClient.showMsgBoxInformation("Autorizado con éxito");
                        req.save(miClient.getSession());
                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
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
                    if (miClient.showMsgBoxConfirm("¿Esta seguro/a de devolver la requisición a estatus de nuevo?\nEsta acción no se puede deshacer.") == JOptionPane.OK_OPTION) {
                        int[] key = (int[]) gridRow.getRowPrimaryKey();
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
    
    public void actionAuthComments() {
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
                SDialogDocumentAuthornComments dialog = new SDialogDocumentAuthornComments((SGuiClient) miClient, "Comentarios de autorización");
                dialog.setValue(SModConsts.TRN_MAT_REQ, gridRow.getRowPrimaryKey()[0]);
                dialog.setVisible(true);
            }
        }
    }
    
    public void actionEditNotes() {
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
                    SDbMaterialRequest req = new SDbMaterialRequest();
                    req.read(miClient.getSession(), gridRow.getRowPrimaryKey());
                    String message = "No se puede modificar las notas de la RM seleccionada debido a:";
                    if (req.getFkMatRequestStatusId() == SModSysConsts.TRNS_ST_MAT_REQ_PUR) {
                        boolean canEdit = true;
                        String sql = "SELECT DISTINCT " +
                                "IF(d.num_ser = '', d.num, CONCAT(d.num_ser, '-', d.num)) invoice, " +
                                "IF(d.fid_st_dps_authorn = " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_PENDING + ", " +
                                "'" + SDataConstantsSys.StatusDpsAuthorn.get(SDataConstantsSys.TRNS_ST_DPS_AUTHORN_PENDING) + "', " +
                                "'" + SDataConstantsSys.StatusDpsAuthorn.get(SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN) + "') stat " +
                                "FROM trn_dps_mat_req AS dr " +
                                "INNER JOIN trn_mat_req AS r on dr.fid_mat_req = r.id_mat_req " +
                                "INNER JOIN trn_dps AS d ON dr.fid_dps_year = d.id_year AND dr.fid_dps_doc = d.id_doc " +
                                "WHERE d.fid_ct_dps = 1 AND d.fid_cl_dps = 2 AND d.fid_tp_dps = 1 " +
                                "AND (d.fid_st_dps_authorn = " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_PENDING + " " +
                                "OR d.fid_st_dps_authorn = " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN + ")" + 
                                "AND r.id_mat_req = " + req.getPkMatRequestId() + ";";
                        ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
                        while (resultSet.next()) {
                            canEdit = false;
                            message += "\n- El estatus de autorización del pedido de compras con folio " + resultSet.getString("invoice") + ", relacionado con la RM, es " + resultSet.getString("stat") + ".";
                        }
                        if (canEdit) {
                            SFormCapturingNotes form = new SFormCapturingNotes((SClientInterface) miClient);
                            form.setValue(SFormCapturingNotes.NOTE_LEN, 1000);
                            form.setValue(SFormCapturingNotes.NUMBER, req.getNumber() + "");
                            if (req.getChildNotes() != null && req.getChildNotes().size() > 0) {
                                form.setValue(SFormCapturingNotes.NOTE_TEXT, req.getChildNotes().get(0).getNotes());
                                form.setFormVisible(true);
                                if (form.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                    req.getChildNotes().get(0).setNotes((String) form.getValue(SFormCapturingNotes.NOTE_TEXT));
                                    req.getChildNotes().get(0).save(miClient.getSession());
                                }
                            }
                            else {
                                form.setFormVisible(true);
                                if (form.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                    SDbMaterialRequestNote note = new SDbMaterialRequestNote();
                                    note.setPkMatRequestId(req.getPkMatRequestId());
                                    note.setPkNotesId(1);
                                    note.setNotes((String) form.getValue(SFormCapturingNotes.NOTE_TEXT));
                                    note.save(miClient.getSession());
                                }
                            }
                        }
                        else {
                            miClient.showMsgBoxInformation(message);
                        }
                    }
                    else {
                        miClient.showMsgBoxInformation(message + "\n- La RM no está en Compras.");
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }
    
    @Override
    public void actionRowDelete() {
        if (jbRowDelete.isEnabled()) {
            if (jtTable.getSelectedRowCount() == 0) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROWS);
            } else if (miClient.showMsgBoxConfirm(SGridConsts.MSG_CONFIRM_REG_DEL) == JOptionPane.YES_OPTION) {
                boolean updates = false;
                SGridRow[] gridRows = getSelectedGridRows();

                for (SGridRow gridRow : gridRows) {
                    if (((SGridRowView) gridRow).getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    } else if (((SGridRowView) gridRow).isRowSystem()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_IS_SYSTEM);
                    } else if (!((SGridRowView) gridRow).isDeletable()) {
                        miClient.showMsgBoxWarning(SDbConsts.MSG_REG_ + gridRow.getRowName() + SDbConsts.MSG_REG_NON_DELETABLE);
                    } else {
                        try {
                            int[] key = (int[]) gridRow.getRowPrimaryKey();

                            SDbMaterialRequest mr = new SDbMaterialRequest();
                            mr.read(miClient.getSession(), key);

                            boolean hasRefs = STrnStockSegregationUtils.hasSegregationsRefs(miClient.getSession().getStatement().getConnection(), 
                                                                                            mr.getChildEntries());
                            if (hasRefs) {
                                throw new Exception("No se puede eliminar la RM, tiene apartados en almacén");
                            }
                            if (miClient.getSession().getModule(mnModuleType, mnModuleSubtype).deleteRegistry(mnGridType, gridRow.getRowPrimaryKey()) == SDbConsts.SAVE_OK) {
                                updates = true;
                            }
                        } catch (Exception e) {
                            SLibUtils.showException(this, e);
                        }
                    }
                }

                if (updates) {
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
            }
        }
    }
        
    @Override
    public void actionRowDisable() {
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
                if (miClient.showMsgBoxConfirm("¿Está seguro/a que desea cancelar la RM?\nEsta acción no se puede deshacer.") == JOptionPane.OK_OPTION) {
                    try {
                        int[] key = (int[]) gridRow.getRowPrimaryKey();

                        SDbMaterialRequest mr = new SDbMaterialRequest();
                        mr.read(miClient.getSession(), key);
                        mr.cancel(miClient.getSession());

                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }
    
    @Override
    public void prepareSqlQuery() {
        String select = "";
        String where = "";
        String authJoin = "";
        String provJoin = "";
        boolean needAuthJoin = false;
        Object filter;
        int usrId = miClient.getSession().getUser().getPkUserId();
        
        moPaneSettings = new SGridPaneSettings(1);

        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DELETED)).getValue();
        if ((Boolean) filter) {
            where += (where.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
        }

        switch (mnGridMode) {
            case SModSysConsts.TRNS_ST_MAT_REQ_NEW:
                where += (where.isEmpty() ? "" : "AND ") + "v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_NEW + " ";
                if (usrId != 2 ) { // SUPER
                    where += (where.isEmpty() ? "" : "AND ") + "(v.fk_usr_req = " + usrId + " OR v.fk_usr_ins = " + usrId + ") ";
                }
                break;
            case SModSysConsts.TRNS_ST_MAT_REQ_AUTH:
                needAuthJoin = true;
                authJoin += "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS aut ON "
                        + "v.id_mat_req = aut.res_pk_n1_n AND NOT aut.b_del ";
                where += (where.isEmpty() ? "" : "AND ") + "v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_AUTH + " ";
                break;
            case SModSysConsts.TRNX_MAT_REQ_AUTHO:
                needAuthJoin = true;
                select += "uaut.usr AS autorizo, ";
                authJoin += "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS aut ON "
                        + "v.id_mat_req = aut.res_pk_n1_n AND NOT aut.b_del "
                        + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uaut ON " 
                        + "aut.fk_usr_authorn_n = uaut.id_usr "
                        + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS urej ON " 
                        + "aut.fk_usr_reject_n = urej.id_usr ";
                filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
                if (filter != null) {
                    where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
                }
                where += (where.isEmpty() ? "" : "AND ") + "aut.fk_usr_authorn_n IS NOT NULL AND aut.fk_usr_step = " + usrId + " ";
                break;
            case SModSysConsts.TRNX_MAT_REQ_AUTHO_RECH:
                needAuthJoin = true;
                select += "urej.usr AS rechazo, ";
                authJoin += "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS aut ON "
                        + "v.id_mat_req = aut.res_pk_n1_n AND NOT aut.b_del "
                        + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uaut ON " 
                        + "aut.fk_usr_authorn_n = uaut.id_usr "
                        + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS urej ON " 
                        + "aut.fk_usr_reject_n = urej.id_usr ";
                filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
                if (filter != null) {
                    where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
                }
                where += (where.isEmpty() ? "" : "AND ") + "cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
                                                            + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.id_mat_req, "
                                                            + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_REJECTED + " "
                                                            + "AND aut.fk_usr_step = " + usrId + " ";
                break;
            case SModSysConsts.TRNS_ST_MAT_REQ_PROV:
                switch (mnGridSubmode) {
                    case SModSysConsts.TRNX_ST_MAT_REQ_PROV_PROV:
                        where += (where.isEmpty() ? "" : "AND ") + "v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PROV + " ";
                        break;
                    case SModSysConsts.TRNX_ST_MAT_REQ_PROV_PUR:
                        where += (where.isEmpty() ? "" : "AND ") + "v.fk_st_mat_req = " + SModSysConsts.TRNS_ST_MAT_REQ_PUR + " " ;
                        break;
                }
                break;
            case SModConsts.TRN_MAT_CONS_ENT_USR:
                if (usrId != 2 ) { // SUPER
                    where += (where.isEmpty() ? "" : "AND ") + "v.id_mat_req IN ( " +
                        "SELECT DISTINCT rc.id_mat_req FROM trn_mat_req_cc AS rc " +
                        "INNER JOIN " +
                        "(SELECT s.id_mat_cons_ent, s.id_mat_cons_subent FROM trn_mat_cons_ent_usr AS u " +
                        "INNER JOIN trn_mat_cons_subent AS s ON s.id_mat_cons_ent = u.id_mat_cons_ent " +
                        "WHERE u.id_link = 1 and u.id_ref = " + usrId + " " +
                        "UNION " +
                        "SELECT s.id_mat_cons_ent, s.id_mat_cons_subent FROM trn_mat_cons_subent_usr AS u " +
                        "INNER JOIN trn_mat_cons_subent AS s ON s.id_mat_cons_ent = u.id_mat_cons_ent AND s.id_mat_cons_subent = u.id_mat_cons_subent " +
                        "WHERE u.id_link = 1 and u.id_ref = " + usrId + ") AS c ON " +
                        "rc.id_mat_ent_cons_ent = c.id_mat_cons_ent " +
                        "AND rc.id_mat_subent_cons_ent = c.id_mat_cons_ent AND rc.id_mat_subent_cons_subent = c.id_mat_cons_subent) ";
                }
                filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.TRNS_ST_MAT_REQ)).getValue();
                if (filter != null && ((int[]) filter).length == 1) {
                    where += (where.isEmpty() ? "" : "AND ") + "v.fk_st_mat_req = " + ((int[]) filter)[0] + " ";
                }
                filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
                if (filter != null) {
                    where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
                }

            break;
            case SModConsts.TRN_MAT_PROV_ENT_USR:
                if (usrId != 2 ) { // SUPER
                    where += (where.isEmpty() ? "" : "AND ") + "(peu.id_usr = " + usrId + ") ";
                    provJoin = "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT_USR) + " AS peu ON "
                            + "v.fk_mat_prov_ent = peu.id_mat_prov_ent AND peu.id_usr = " + usrId + " ";
                }
                filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.TRNS_ST_MAT_REQ)).getValue();
                if (filter != null && ((int[]) filter).length == 1) {
                    where += (where.isEmpty() ? "" : "AND ") + "v.fk_st_mat_req = " + ((int[]) filter)[0] + " ";
                }
                filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
                if (filter != null) {
                    where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
                }
            break;
            default:
                filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.TRNS_ST_MAT_REQ)).getValue();
                if (filter != null && ((int[]) filter).length == 1) {
                    where += (where.isEmpty() ? "" : "AND ") + "v.fk_st_mat_req = " + ((int[]) filter)[0] + " ";
                }
                filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
                if (filter != null) {
                    where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
                }
            break;
        }
        
        if (usrId != 2) { // SUPER
            // Verificar si el modo de la cuadrícula no es ni TRN_MAT_CONS_ENT_USR ni TRN_MAT_PROV_ENT_USR
            // y si el subtipo de la cuadrícula no es TRNX_MAT_REQ_REV ni TRNX_MAT_REQ_ADM
            if (mnGridMode != SModConsts.TRN_MAT_CONS_ENT_USR &&
                    mnGridMode != SModConsts.TRN_MAT_PROV_ENT_USR &&
                    mnGridSubtype != SModSysConsts.TRNX_MAT_REQ_REV &&
                    mnGridSubtype != SModSysConsts.TRNX_MAT_REQ_ADM) {
                // Verificar si el usuario no tiene privilegios de administrador de requisitos de material
                if (!hasMatReqAdmorRight && !hasMatReqPurRight && !hasMatReqProvRight && !hasMatReqReclassRight) {
                    where += (where.isEmpty() ? "" : "AND ") + "(v.fk_usr_req = " + usrId + ") ";
                }
                else {
                    needAuthJoin = false;
                }
            }
            // Verificar si el subtipo de la cuadrícula es TRNX_MAT_REQ_REV
            if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_REV) {
                needAuthJoin = false;
                // Verificar si el usuario no tiene privilegios de administrador de requisitos de material
                if (!hasMatReqAdmorRight) {
                    needAuthJoin = true;
                    where += (where.isEmpty() ? "" : "AND ") + "(aut.fk_usr_step = " + usrId + ") ";
                }
            }
            // Verificar si el subtipo de la cuadrícula es TRNX_MAT_REQ_ADM
            if (mnGridSubtype == SModSysConsts.TRNX_MAT_REQ_ADM) {
                // Condiciones que aplican cuando la vista es RM administrador
                needAuthJoin = false;
            }
        }
        
        msSql = "SELECT v.id_mat_req AS " + SDbConsts.FIELD_ID + "1, "
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "v.num AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "v.tp_req, "
                + "LPAD(v.num, " + SDataConstantsSys.NUM_LEN_MAT_REQ + ", 0) AS folio, "
                + "v.dt_req_n, "
                + "v.dt_delivery_n, "
                + "v.ref, "
                + "v.b_ext_sys, "
                + "v.b_clo_prov, "
                + "v.b_clo_pur, "
                + "pe.code AS prov_ent, "
                + "rp.name AS req_pty, "
                + "smr.name AS req_status, "
                + "smp.name AS sum_status, "
                + "smpu.name AS comp_status, "
                + "dn.dps_nat, "
                + "ur.usr AS usr_req, "
                + "bmu.bp AS contractor, "
                + "iref.item_key, "
                + "cob.ent, "
                + select
                + "CASE "
                    + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
                    + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.id_mat_req, "
                    + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_AUTHORIZED + " THEN 'AUTORIZADO' "
                    + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
                    + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.id_mat_req, "
                    + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_REJECTED + " THEN 'RECHAZADO' "
                    + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
                    + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.id_mat_req, "
                    + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_PENDING + " THEN 'PENDIENTE' "
                    + "WHEN cfg_get_st_authorn(" + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + ", "
                    + "'" + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + "', v.id_mat_req, "
                    + "NULL, NULL, NULL, NULL) = " + SAuthorizationUtils.AUTH_STATUS_IN_PROCESS + " THEN 'EN PROCESO' "
                    + "ELSE '(NO APLICA)' "
                + "END AS auth_status, "
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
                + "v.fk_usr_clo_prov, "
                + "v.fk_usr_clo_pur, "
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_clo_prov, "
                + "v.ts_usr_clo_pur, "
                + "v.ts_usr_chg, "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "uc.usr AS usr_clo, "
                + "ucp.usr AS usr_pur, "
                + "uch.usr AS usr_chg, "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT) + " AS pe ON "
                + "v.fk_mat_prov_ent = pe.id_mat_prov_ent "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_MAT_REQ_PTY) + " AS rp ON "
                + "v.fk_mat_req_pty = rp.id_mat_req_pty "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_REQ) + " AS smr ON "
                + "v.fk_st_mat_req = smr.id_st_mat_req "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_PROV) + " AS smp ON "
                + "v.fk_st_mat_prov = smp.id_st_mat_prov "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_PUR) + " AS smpu ON "
                + "v.fk_st_mat_pur = smpu.id_st_mat_pur "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNU_DPS_NAT) + " AS dn ON "
                + "v.fk_dps_nat = dn.id_dps_nat "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON "
                + "v.fk_usr_req = ur.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAINT_USER) + " AS mu ON "
                + "v.fk_contractor_n = mu.id_maint_user "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS cob ON "
                + "v.fk_whs_cob_n = cob.id_cob AND v.fk_whs_whs_n = cob.id_ent "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS iref ON "
                + "v.fk_item_ref_n = iref.id_item "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bmu ON "
                + "mu.id_maint_user = bmu.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uc ON "
                + "v.fk_usr_clo_prov = uc.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ucp ON "
                + "v.fk_usr_clo_pur = ucp.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uch ON "
                + "v.fk_usr_chg = uch.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (needAuthJoin ? authJoin : "")
                + (provJoin)
                + (where.isEmpty() ? "" : "WHERE " + where)
                + "ORDER BY v.dt, v.num ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "prov_ent", "Cen suministro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "folio", "Folio"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_req", "Solicitante"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "tp_req", "Tipo requisición", 20));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "item_key", "Concepto/gasto"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "contractor", "Contratista"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "dps_nat", "Naturaleza doc."));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "ref", "Referencia"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_req_n", "Fecha requerida"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_delivery_n", "Fecha entrega estimada"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "req_pty", "Prioridad"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "req_status", "Estatus"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "auth_status", "Autorización"));
        if (mnGridMode == SModSysConsts.TRNX_MAT_REQ_AUTHO) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "autorizo", "Autorizó"));
        }
        if (mnGridMode == SModSysConsts.TRNX_MAT_REQ_AUTHO_RECH) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "rechazo", "Rechazó"));
        }
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "sum_status", "Suministro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "comp_status", "Compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "comp_doc", "Doc compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "cob.ent", "Almacén"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_L, "b_clo_prov", "Terminado suministro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_clo", "Usr terminado suministro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_usr_clo_prov", "Usr TS terminado suministro"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_L, "b_clo_pur", "Terminado compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_clo", "Usr terminado compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_usr_clo_prov", "Usr TS terminado compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        if (mnGridMode == SModSysConsts.TRNX_MAT_REQ_RECLASS) {
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "usr_chg", "Usr cambio contb."));
            columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "ts_usr_chg", "Usr TS cambio contb."));
        }
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        
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
        moSuscriptionsSet.add(SModConsts.TRN_MAT_REQ);
        moSuscriptionsSet.add(SModConsts.TRNX_MAT_REQ_PEND_SUP);
        moSuscriptionsSet.add(SModConsts.TRNX_MAT_REQ_PEND_PUR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbNewSupReq) {
                actionNewSupReq();
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
            else if (button == jbAuthorize) {
                actionAuthorizeOrRejectResource(SAuthorizationUtils.AUTH_ACTION_AUTHORIZE);
            }
            else if (button == jbReject) {
                actionAuthorizeOrRejectResource(SAuthorizationUtils.AUTH_ACTION_REJECT);
            }
            else if (button == jbHardAuthorize) {
                actionHardAuthorize();
            }
            else if (button == jbSegregate) {
                actionSegregateOrRelease();
            }
            else if (button == jbDocsCardex) {
                actionDocsKardex();
            }
            else if (button == jbToNew) {
                actionToNew();
            }
            else if (button == jbAuthComments) {
                actionAuthComments();
            }
            else if (button == jbEditNotes) {
                actionEditNotes();
            }
        }
    }
}
