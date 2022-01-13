/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.view;

import erp.SClientUtils;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import erp.mod.log.db.SLogBillOfLadingAnnul;
import erp.mtrn.data.SCfdBolUtils;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SCfdUtilsHandler;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.form.SDialogAnnulCfdi;
import erp.print.SDataConstantsPrint;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Isabel Servín
 */
public class SViewBillOfLading extends SGridPaneView implements ActionListener {

    private erp.mtrn.form.SDialogAnnulCfdi moDialogAnnulCfdi;
    
    private SGridFilterDatePeriod moFilterDatePeriod;
    private javax.swing.JButton jbPrint;
    private javax.swing.JButton jbPrintCancelAck;
    private javax.swing.JButton jbGetXml;
    private javax.swing.JButton jbGetXmlCancelAck;
    private javax.swing.JButton jbSignXml;
    private javax.swing.JButton jbVerifyCfdi;
    private javax.swing.JButton jbGetCfdiStatus;
    private javax.swing.JButton jbSendCfdi;
    private javax.swing.JButton jbRestoreCfdStamped;
    private javax.swing.JButton jbRestoreCfdCancelAck;
    private javax.swing.JButton jbDeactivateFlags;
    private javax.swing.JButton jbAnnul;
    
    public SViewBillOfLading(SGuiClient client, int subType, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.LOG_BOL, subType, title);
        setRowButtonsEnabled(true, false, true, false, true);
        
        initComponetsCustom();
    }
    
    private void dislabeButtons() {
        jbPrint.setEnabled(false);
        jbPrintCancelAck.setEnabled(false);
        jbGetXml.setEnabled(false);
        jbGetXmlCancelAck.setEnabled(false);
        jbSignXml.setEnabled(false);
        jbVerifyCfdi.setEnabled(false);
        jbGetCfdiStatus.setEnabled(false);
        jbSendCfdi.setEnabled(false);
        jbRestoreCfdStamped.setEnabled(false);
        jbRestoreCfdCancelAck.setEnabled(false);
        jbDeactivateFlags.setEnabled(false);
        jbAnnul.setEnabled(false);
    }

    private void initComponetsCustom() {
        
        moDialogAnnulCfdi = new SDialogAnnulCfdi((SClientInterface) miClient);
        
        jbPrint = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        jbPrint.setPreferredSize(new Dimension(23, 23));
        jbPrint.addActionListener(this);
        jbPrint.setToolTipText("Imprimir comprobante");
        
        jbPrintCancelAck = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT_ACK_CAN));
        jbPrintCancelAck.setPreferredSize(new Dimension(23, 23));
        jbPrintCancelAck.addActionListener(this);
        jbPrintCancelAck.setToolTipText("Imprimir acuse de cancelación");

        jbGetXml = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML));
        jbGetXml.setPreferredSize(new Dimension(23, 23));
        jbGetXml.addActionListener(this);
        jbGetXml.setToolTipText("Obtener XML del comprobante");

        jbGetXmlCancelAck = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML_CANCEL));
        jbGetXmlCancelAck.setPreferredSize(new Dimension(23, 23));
        jbGetXmlCancelAck.addActionListener(this);
        jbGetXmlCancelAck.setToolTipText("Obtener XML del acuse de cancelación del CFDI");

        jbSignXml = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_XML_SIGN));
        jbSignXml.setPreferredSize(new Dimension(23, 23));
        jbSignXml.addActionListener(this);
        jbSignXml.setToolTipText("Timbrar CFDI");

        jbVerifyCfdi = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_ok.gif")));
        jbVerifyCfdi.setPreferredSize(new Dimension(23, 23));
        jbVerifyCfdi.addActionListener(this);
        jbVerifyCfdi.setToolTipText("Verificar timbrado o cancelación del CFDI");
        
        jbGetCfdiStatus = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif")));
        jbGetCfdiStatus.setPreferredSize(new Dimension(23, 23));
        jbGetCfdiStatus.addActionListener(this);
        jbGetCfdiStatus.setToolTipText("Checar estatus cancelación del CFDI");
        
        jbSendCfdi = new JButton(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")));
        jbSendCfdi.setPreferredSize(new Dimension(23, 23));
        jbSendCfdi.addActionListener(this);
        jbSendCfdi.setToolTipText("Enviar comprobante vía mail");

        jbRestoreCfdStamped = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert.gif")));
        jbRestoreCfdStamped.setPreferredSize(new Dimension(23, 23));
        jbRestoreCfdStamped.addActionListener(this);
        jbRestoreCfdStamped.setToolTipText("Insertar XML timbrado del CFDI");

        jbRestoreCfdCancelAck = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_insert_annul.gif")));
        jbRestoreCfdCancelAck.setPreferredSize(new Dimension(23, 23));
        jbRestoreCfdCancelAck.addActionListener(this);
        jbRestoreCfdCancelAck.setToolTipText("Insertar PDF del acuse de cancelación del CFDI");

        jbDeactivateFlags = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif")));
        jbDeactivateFlags.setPreferredSize(new Dimension(23, 23));
        jbDeactivateFlags.addActionListener(this);
        jbDeactivateFlags.setToolTipText("Limpiar inconsistencias de timbrado o cancelación del CFDI");
        
        jbAnnul = new JButton(miClient.getImageIcon(SLibConstants.ICON_ANNUL));
        jbAnnul.setPreferredSize(new Dimension(23, 23));
        jbAnnul.addActionListener(this);
        jbAnnul.setToolTipText("Anular documento");
        
        if (mnGridSubtype == SDataConstantsSys.TRNS_TP_CFD_INV){
            dislabeButtons();
        }

        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrint);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrintCancelAck);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGetXml);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGetXmlCancelAck);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSignXml);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbVerifyCfdi);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbGetCfdiStatus);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbSendCfdi);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbRestoreCfdStamped);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbRestoreCfdCancelAck);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbDeactivateFlags);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbAnnul);
    }
    
    private void actionPrint() {
        if (jbPrint.isEnabled()) {
            if (jtTable.getSelectedRow() < 0) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDataCfd cfd = SCfdUtils.getCfd((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_BOL, gridRow.getRowPrimaryKey()); 
                    SCfdUtils.printCfd((SClientInterface) miClient, cfd, SLibConstants.UNDEFINED, SDataConstantsPrint.PRINT_MODE_VIEWER, 1, false);
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionPrintCancelAck() {
        if (jbPrintCancelAck.isEnabled()) {
            if (jtTable.getSelectedRow() < 0) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDataCfd cfd = SCfdUtils.getCfd((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_BOL, gridRow.getRowPrimaryKey()); 
                    SCfdUtils.printCfdCancelAck((SClientInterface) miClient, cfd, 0, SDataConstantsPrint.PRINT_MODE_VIEWER);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }
    
    private void actionGetXml() {
        if (jbGetXml.isEnabled()) {
            if (jtTable.getSelectedRow() < 0) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDataCfd cfd = SCfdUtils.getCfd((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_BOL, gridRow.getRowPrimaryKey()); 
                    SCfdUtils.downloadXmlCfd((SClientInterface) miClient, cfd);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionGetCancelAck() {
        if (jbGetXmlCancelAck.isEnabled()) {
            if (jtTable.getSelectedRow() < 0) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDataCfd cfd = SCfdUtils.getCfd((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_BOL, gridRow.getRowPrimaryKey()); 
                    SCfdUtils.getAcknowledgmentCancellationCfd((SClientInterface) miClient, cfd);
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionSignXml() {
        if (jbSignXml.isEnabled()) {
           if (jtTable.getSelectedRow() < 0) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SCfdBolUtils.sign((SClientInterface) miClient, gridRow.getRowPrimaryKey());
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }
    
    private void actionVerifyCfdi() {
        if (jbVerifyCfdi.isEnabled()) {
           if (jtTable.getSelectedRow() < 0) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDataCfd cfd = SCfdUtils.getCfd((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_BOL, gridRow.getRowPrimaryKey()); 
                    if (SCfdUtils.validateCfdi((SClientInterface)miClient, cfd, 0, true)) {
                        ((SClientInterface) miClient).getGuiModule(SModConsts.LOG_BOL).refreshCatalogues(mnGridType);
                    }
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionGetCfdiStatus() {
        if (jbGetCfdiStatus.isEnabled()) {
           if (jtTable.getSelectedRow() < 0) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDataCfd cfd = SCfdUtils.getCfd((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_BOL, gridRow.getRowPrimaryKey()); 
                    miClient.showMsgBoxInformation(new SCfdUtilsHandler((SClientInterface) miClient).getCfdiSatStatus(cfd).getDetailedStatus());
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
            }
        }
    }

    private void actionSendCfdi() {
        if (jbSendCfdi.isEnabled()) {
            if (jtTable.getSelectedRow() < 0) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDataCfd cfd = SCfdUtils.getCfd((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_BOL, gridRow.getRowPrimaryKey()); 
                    SCfdUtils.sendCfd((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_BOL, cfd, 0, true, false, true);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionRestoreSignedXml() {
        if (jbRestoreCfdStamped.isEnabled()) {
           if (jtTable.getSelectedRow() < 0) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDataCfd cfd = SCfdUtils.getCfd((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_BOL, gridRow.getRowPrimaryKey()); 
                    boolean needUpdate = SCfdUtils.restoreCfdStamped((SClientInterface) miClient, cfd, 0, true);

                    if (needUpdate) {
                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionRestoreSignedXmlCancelAck() {
        if (jbRestoreCfdCancelAck.isEnabled()) {
           if (jtTable.getSelectedRow() < 0) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDataCfd cfd = SCfdUtils.getCfd((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_BOL, gridRow.getRowPrimaryKey()); 
                    boolean needUpdate = SCfdUtils.restoreCfdCancelAck((SClientInterface)miClient, cfd, 0, true);

                    if (needUpdate) {
                        miClient.getSession().notifySuscriptors(mnGridType);
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionDeactivateFlags() {
        if (jbDeactivateFlags.isEnabled()) {
           if (jtTable.getSelectedRow() < 0) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();
                    SDataCfd cfd = SCfdUtils.getCfd((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_BOL, gridRow.getRowPrimaryKey()); 
                    SCfdUtils.resetCfdiDeactivateFlags((SClientInterface)miClient, cfd);
                    miClient.getSession().notifySuscriptors(mnGridType);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }
    
    private void actionAnnul() {
        boolean needUpdate = false;
        SLogBillOfLadingAnnul bolAnnul;
        
        if (jbAnnul.isEnabled()) {
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
                        
                        SDataCfd cfd = SCfdUtils.getCfd((SClientInterface) miClient, SDataConstantsSys.TRNS_TP_CFD_BOL, gridRow.getRowPrimaryKey()); 

                        moDialogAnnulCfdi.formReset();
                        moDialogAnnulCfdi.formRefreshCatalogues();
                        moDialogAnnulCfdi.setValue(SGuiConsts.PARAM_DATE, cfd.getTimestamp());
                        moDialogAnnulCfdi.setValue(SModConsts.TRNS_TP_CFD, SDataConstantsSys.TRNS_TP_CFD_BOL);
                        moDialogAnnulCfdi.setVisible(true);

                        if (moDialogAnnulCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                            bolAnnul = new SLogBillOfLadingAnnul((SClientInterface) miClient, cfd, moDialogAnnulCfdi.getDate(), moDialogAnnulCfdi.getAnnulSat(), moDialogAnnulCfdi.getDpsAnnulationType(), SDataConstantsSys.TRNS_TP_CFD_BOL);
                            bolAnnul.annulBillOfLading();
                        }

                        if (needUpdate) {
                            miClient.getSession().notifySuscriptors(mnGridType);
                        }
                    }
                    catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    
    /*
     * Public methods:
     */

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(1);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        moPaneSettings.setDisableableApplying(false);
        moPaneSettings.setDateApplying(false);

        jbRowDisable.setEnabled(false);

        filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "b.b_del = 0 ";
        }

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("b.dt", (SGuiDate) filter);
        
        String complementaryDbName = "";
        
        try {
            complementaryDbName = SClientUtils.getComplementaryDdName((SClientInterface) miClient);
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
        
        switch (mnGridSubtype) {
            case SDataConstantsSys.TRNS_TP_CFD_INV:
                sql += (sql.isEmpty() ? "" : "AND ") + "b.bol_tp = 'I' ";
                break;
            case SDataConstantsSys.TRNS_TP_CFD_BOL:
               sql += (sql.isEmpty() ? "" : "AND ") + "b.bol_tp = 'T' ";
               break; 
        }

        msSql = "SELECT " 
            + "b.id_bol AS " + SDbConsts.FIELD_ID + "1, " 
            + "'' AS " + SDbConsts.FIELD_CODE + ", "
            + "IF(b.ser <> '', CONCAT(b.ser, '-', b.num), b.num) AS " + SDbConsts.FIELD_NAME + ", "
            + "b.dt AS " + SDbConsts.FIELD_DATE + ", " 
            + "b.int_bol, " 
            + "b.input_output_bol, " 
            + "b.input_output_way_key, " 
            + "b.distance_tot, " 
            + "v.name, " 
            + "v.plate, " 
            + "tra1.plate, " 
            + "tra1.name, " 
            + "tra2.plate, " 
            + "tra2.name, " 
            + "b.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " 
            + "b.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", " 
            + "b.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", " 
            + "b.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", " 
            + "b.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", " 
            + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
            + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + ", " 
            + "IF(b.fk_st_bol = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " + SGridConsts.ICON_ANNUL + ", " + SGridConsts.ICON_NULL + ") AS f_ico, " 
            + "IF(c.fid_st_xml IS NULL, " + SGridConsts.ICON_NULL + ", " /* not have CFDI associated */
            + "IF(c.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " OR LENGTH(c.uuid) = 0, " + SGridConsts.ICON_XML_PEND + ", " /* CFDI pending sign */
            + "IF(LENGTH(xc.ack_can_xml) = 0 AND xc.ack_can_pdf_n IS NULL, " + SGridConsts.ICON_XML_ISSU + ", " /* CFDI signed, canceled only SIIE */
            + "IF(LENGTH(xc.ack_can_xml) != 0, " + SGridConsts.ICON_XML_ANNUL + ", " /* CFDI canceled with cancellation acknowledgment in XML format */
            + "IF(xc.ack_can_pdf_n IS NOT NULL, " + SGridConsts.ICON_XML_ANNUL + ", " /* CFDI canceled with cancellation acknowledgment in PDF format */
            + SGridConsts.ICON_XML_ISSU + " " /* CFDI signed, canceled only SIIE */
            + "))))) AS _ico_xml "
            + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL) + " AS b " 
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_TRANSP_MODE) + " AS t ON b.id_bol = t.id_bol " 
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON b.fk_usr_ins = ui.id_usr "
            + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON b.fk_usr_upd = uu.id_usr "
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_VEH) + " AS v ON t.fk_veh_n = v.id_veh " 
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_TRAILER) + " AS tra1 ON t.fk_veh_trailer_1_n = tra1.id_trailer " 
            + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.LOG_TRAILER) + " AS tra2 ON t.fk_veh_trailer_2_n = tra2.id_trailer "
            + "LEFT OUTER JOIN trn_cfd AS c ON b.id_bol = c.fid_bol_n "
            + "LEFT OUTER JOIN " + complementaryDbName + ".trn_cfd AS xc ON c.id_cfd = xc.id_cfd " 
            + (sql.isEmpty() ? "" : "WHERE " + sql)
            + "ORDER BY b.num, b.dt ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        int col = 0;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        SGridColumnView[] columns = new SGridColumnView[19];

        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_NAME, "Folio");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "f_ico", "Status");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_INT_ICON, "_ico_xml", "CFD");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "b.int_bol", "Transporte internacional");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "b.input_output_bol", "E/S");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "b.input_output_way_key", "Vía E/S");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "b.distance_tot", "Total distancia recorrida");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "v.name", "Vehiculo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_PER, "v.plate", "Placa vehiculo");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "tra1.name", "Remolque 1");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_PER, "tra1.plate", "Placa remolque 1");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_S, "tra2.name", "Remolque 2");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_PER, "tra2.plate", "Placa remolque 2");
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS);
        gridColumnsViews.addAll(Arrays.asList((SGridColumnView[]) columns));

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.LOG_BOL);
        moSuscriptionsSet.add(SModConsts.LOG_BOL_TRANSP_MODE);
        moSuscriptionsSet.add(SModConsts.LOG_VEH);
        moSuscriptionsSet.add(SModConsts.LOG_TRAILER);
        moSuscriptionsSet.add(SModConsts.TRN_CFD);
        moSuscriptionsSet.add(SModConsts.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPrint) {
                actionPrint();
            }
            else if (button == jbPrintCancelAck) {
                actionPrintCancelAck();
            }
            else if (button == jbGetXml) {
                actionGetXml();
            }
            else if (button == jbGetXmlCancelAck) {
                actionGetCancelAck();
            }
            else if (button == jbSignXml) {
                actionSignXml();
            }
            else if (button == jbVerifyCfdi) {
                actionVerifyCfdi();
            }
            else if (button == jbGetCfdiStatus) {
                actionGetCfdiStatus();
            }
            else if (button == jbSendCfdi) {
                actionSendCfdi();
            }
            else if (button == jbRestoreCfdStamped) {
                actionRestoreSignedXml();
            }
            else if (button == jbRestoreCfdCancelAck) {
                actionRestoreSignedXmlCancelAck();
            }
            else if (button == jbDeactivateFlags) {
                actionDeactivateFlags();
            }
            else if (button == jbAnnul) {
                actionAnnul();
            }
        }
    }
}
