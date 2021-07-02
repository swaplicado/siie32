/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDateCutOff;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mtrn.data.STrnDeliveryAckUtilities;
import erp.mtrn.form.SDialogDpsDeliveryAckReasign;
import erp.mtrn.form.SDialogDpsDeliveryAckSend;
import erp.mtrn.form.SDialogDpsDeliveryAckViewer;
import erp.table.SFilterConstants;
import erp.table.STabFilterCompanyBranch;
import erp.table.STabFilterFunctionalArea;
import java.awt.Dimension;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Daniel López, Sergio Flores
 */
public class SViewDpsDeliveryAck extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton mjbDeliveryAckClose;
    private javax.swing.JButton mjbDeliveryAckOpen;
    private javax.swing.JButton mjbUploadFile;
    private javax.swing.JButton mjbDownloadFiles;
    private javax.swing.JButton mjbDeleteFiles;
    private javax.swing.JButton mjbReasignFiles;
    private javax.swing.JButton mjbViewFiles;
    private javax.swing.JButton mjbSendFiles;
    private erp.table.STabFilterCompanyBranch moTabFilterCompanyBranch;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.lib.table.STabFilterDateCutOff moTabFilterDateCutOff;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;

    private erp.mtrn.form.SDialogDpsDeliveryAckReasign moDialogDpsDeliveryAckReasign;
    private erp.mtrn.form.SDialogDpsDeliveryAckViewer moDialogDpsDeliveryViewer;
    private erp.mtrn.form.SDialogDpsDeliveryAckSend moDialogDpsDeliveryAckSend;

    private boolean mbIsPending;

    /**
     * Creates new view for DPS delivery acknowledgments.
     *
     * @param client GUI client.
     * @param tabTitle View tab title.
     * @param dpsCategory DPS category. Can be SDataConstantsSys.TRNS_CT_DPS_PUR
     * or SDataConstantsSys.TRNS_CT_DPS_SAL.
     * @param dpsAckStatus DPS delivery acknowledgment status. Can be
     * SUtilConsts.PROC or SUtilConsts.PROC_PEND.
     */
    public SViewDpsDeliveryAck(SClientInterface client, String tabTitle, int dpsCategory, int dpsAckStatus) {
        super(client, tabTitle, SDataConstants.TRN_DPS_ACK, dpsCategory, dpsAckStatus);
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        mbIsPending = false;    // opened or closed DPS for appending delivery acknowledgment files
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;

        switch (mnTabTypeAux02) {
            case SUtilConsts.PROC_PEND:
                mbIsPending = true;
                moTabFilterDateCutOff = new STabFilterDateCutOff(miClient, this, SLibTimeUtilities.getEndOfYear(miClient.getSessionXXX().getWorkingDate()));
                break;
            case SUtilConsts.PROC:
                mbIsPending = false;
                moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
                break;
            default:
        }
        
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);

        mjbDeliveryAckClose = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_CLOSE));
        mjbDeliveryAckOpen = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_OPEN));
        mjbUploadFile = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_ADD));
        mjbDownloadFiles = new JButton(miClient.getImageIcon(SLibConstants.ICON_ARROW_DOWN));
        mjbDeleteFiles = new JButton(miClient.getImageIcon(SLibConstants.ICON_DELETE));
        mjbReasignFiles = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_DELIVERY));
        mjbViewFiles = new JButton(miClient.getImageIcon(SLibConstants.ICON_LOOK));
        mjbSendFiles = new JButton(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")));

        mjbDeliveryAckClose.setPreferredSize(new Dimension(23, 23));
        mjbDeliveryAckOpen.setPreferredSize(new Dimension(23, 23));
        mjbUploadFile.setPreferredSize(new Dimension(23, 23));
        mjbDownloadFiles.setPreferredSize(new Dimension(23, 23));
        mjbDeleteFiles.setPreferredSize(new Dimension(23, 23));
        mjbReasignFiles.setPreferredSize(new Dimension(23, 23));
        mjbViewFiles.setPreferredSize(new Dimension(23, 23));
        mjbSendFiles.setPreferredSize(new Dimension(23, 23));

        mjbDeliveryAckClose.addActionListener(this);
        mjbDeliveryAckOpen.addActionListener(this);
        mjbUploadFile.addActionListener(this);
        mjbDownloadFiles.addActionListener(this);
        mjbDeleteFiles.addActionListener(this);
        mjbReasignFiles.addActionListener(this);
        mjbViewFiles.addActionListener(this);
        mjbSendFiles.addActionListener(this);

        mjbDeliveryAckClose.setToolTipText("Cerrar factura");
        mjbDeliveryAckOpen.setToolTipText("Abrir factura");
        mjbUploadFile.setToolTipText("Agregar acuse");
        mjbDownloadFiles.setToolTipText("Descargar acuses");
        mjbDeleteFiles.setToolTipText("Eliminar acuses");
        mjbReasignFiles.setToolTipText("Reasignar acuses");
        mjbViewFiles.setToolTipText("Ver acuses");
        mjbSendFiles.setToolTipText("Enviar acuses");

        moTabFilterCompanyBranch = new STabFilterCompanyBranch(miClient, this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        addTaskBarUpperComponent(mjbDeliveryAckClose);
        addTaskBarUpperComponent(mjbDeliveryAckOpen);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mbIsPending ? moTabFilterDateCutOff : moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranch);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbUploadFile);
        addTaskBarUpperComponent(mjbDownloadFiles);
        addTaskBarUpperComponent(mjbDeleteFiles);
        addTaskBarUpperComponent(mjbReasignFiles);
        addTaskBarUpperComponent(mjbViewFiles);
        addTaskBarUpperComponent(mjbSendFiles);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterFunctionalArea);

        if (!mbIsPending) {
            mjbDeliveryAckOpen.setEnabled(true);
            mjbDeliveryAckClose.setEnabled(false);
            mjbReasignFiles.setEnabled(false);
            mjbUploadFile.setEnabled(false);
            mjbDownloadFiles.setEnabled(true);
            mjbDeleteFiles.setEnabled(false);
            mjbViewFiles.setEnabled(true);
            mjbSendFiles.setEnabled(true);
        }
        else {
            mjbDeliveryAckOpen.setEnabled(false);
            mjbDeliveryAckClose.setEnabled(true);
            mjbReasignFiles.setEnabled(true);
            mjbUploadFile.setEnabled(true);
            mjbDownloadFiles.setEnabled(true);
            mjbDeleteFiles.setEnabled(true);
            mjbViewFiles.setEnabled(true);
            mjbSendFiles.setEnabled(false);
        }

        aoKeyFields = new STableField[2];
        aoTableColumns = new STableColumn[11];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dps.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dps.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.id_bp", "Clave cliente", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bb.bpb", "Sucursal cliente", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_FLOAT, "dps.tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cur.cur_key", "Moneda", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "_count_ack", "Acuses adjuntos", 50);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.TRN_DPS);

        getTablePane().setDoubleClickAction(this, "actionEdit");

        populateTable();
    }

    private void actionDeliveryAckDocStatus(boolean close) {
        Vector<Object> params = null;
        int[] pk = null;

        if (mjbDeliveryAckClose.isEnabled() || mjbDeliveryAckOpen.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                if (miClient.showMsgBoxConfirm("La factura quedará " + (close ? "cerrada" : "abierta") + " para agregar acuses de entrega.\n" + SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION) {
                    params = new Vector<Object>();

                    /*
                     *Parameters for procedure:
                     *id_year
                     *id_doc
                     *Dps acknowledgement flag (for procedure differentiation)
                     *value to update in field b_dps_ack, table trn_dps
                     *Id of the user modifying
                     */
                    pk = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();
                    params.add(pk[0]);
                    params.add(pk[1]);
                    params.add(SDataConstantsSys.UPD_DPS_FL_DPS_ACK);
                    params.add(close ? 1 : 0);
                    params.add(miClient.getSession().getUser().getPkUserId());
                    params = SDataUtilities.callProcedure((SClientInterface) miClient, SProcConstants.TRN_DPS_UPD, params, SLibConstants.EXEC_MODE_SILENT);

                    miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    private void actionUploadFile() {
        if (mjbUploadFile.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else if (miClient.getGuiModule(SDataConstants.MOD_SAL).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
            }
        }
    }

    private void actionDownloadFiles() {
        if (mjbDownloadFiles.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    STrnDeliveryAckUtilities.downloadFiles(miClient, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                    miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                } catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionDeleteFiles() {
        if (mjbDeleteFiles.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                try {
                    STrnDeliveryAckUtilities.deleteFiles(miClient, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                    miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                } catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    private void actionReasignFiles() {
        if (mjbReasignFiles.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                if (moDialogDpsDeliveryAckReasign == null) {
                    moDialogDpsDeliveryAckReasign = new SDialogDpsDeliveryAckReasign(miClient);
                }

                moDialogDpsDeliveryAckReasign.formReset();
                moDialogDpsDeliveryAckReasign.setRegistry(SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE));
                moDialogDpsDeliveryAckReasign.setFormVisible(true);

                miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
            }
        }
    }

    private void actionViewFiles() {
        if (mjbViewFiles.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                if (moDialogDpsDeliveryViewer == null) {
                    moDialogDpsDeliveryViewer = new SDialogDpsDeliveryAckViewer(miClient);
                }

                moDialogDpsDeliveryViewer.formReset();
                moDialogDpsDeliveryViewer.setRegistry(SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE));
                moDialogDpsDeliveryViewer.setFormVisible(true);
            }
        }
    }

    private void actionSendFiles() {
        if (mjbSendFiles.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                if (moDialogDpsDeliveryAckSend == null) {
                    moDialogDpsDeliveryAckSend = new SDialogDpsDeliveryAckSend(miClient);
                }

                moDialogDpsDeliveryAckSend.formReset();
                moDialogDpsDeliveryAckSend.setRegistry(SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE));
                moDialogDpsDeliveryAckSend.setFormVisible(true);
            }
        }
    }

    @Override
    public void createSqlQuery() {
        int[] keyDpsType = null;
        String sqlWhere = "";
        String sqlFunctAreas = "";
        STableSetting setting = null;

        switch (mnTabTypeAux01) {
            case SDataConstantsSys.TRNS_CT_DPS_PUR:
                keyDpsType = SDataConstantsSys.TRNU_TP_DPS_PUR_INV;
                break;
            case SDataConstantsSys.TRNS_CT_DPS_SAL:
                keyDpsType = SDataConstantsSys.TRNU_TP_DPS_SAL_INV;
                break;
            default:
        }

        sqlWhere += "AND dps.fid_ct_dps = " + keyDpsType[0] + " AND dps.fid_cl_dps = " + keyDpsType[1] + " AND dps.fid_tp_dps = " + keyDpsType[2] + " ";
        sqlWhere += "AND dps.b_dps_ack = " + (mbIsPending ? "0" : "1") + " ";   // opened or closed DPS for appending delivery acknowledgment files

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                if (mbIsPending) {
                    sqlWhere += setting.getSetting() == null ? "" : "AND dps.dt <= '" + (new java.sql.Date(((java.util.Date) setting.getSetting()).getTime())) + "' ";
                }
                else {
                    sqlWhere += "AND " + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "dps.dt") + " ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    sqlFunctAreas += (sqlFunctAreas.isEmpty() ? "" : "AND ") + " dps.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB) {
                sqlWhere += ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "AND dps.fid_cob = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB) {
                sqlWhere += ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "AND dps.fid_cob = " + (Integer) setting.getSetting() + " ");
            }
        }

        msSql = "SELECT dps.id_year, dps.id_doc, tp.code, "
                + "CONCAT(dps.num_ser, IF(length(dps.num_ser) = 0, '', '-'), dps.num) AS _num, "
                + "dps.num_ref, "
                + "dps.dt, "
                + "bpb.code, "
                + "bp.bp, "
                + "bp.id_bp, "
                + "bb.bpb, "
                + "dps.tot_cur_r, "
                + "cur.cur_key, "
                + "(SELECT COUNT(*) FROM trn_dps_ack AS ack WHERE ack.fid_year = dps.id_year AND ack.fid_doc = dps.id_doc AND NOT ack.b_del) AS _count_ack "
                + "FROM trn_dps as dps "
                + "INNER JOIN erp.trnu_tp_dps AS tp ON dps.fid_ct_dps = tp.id_ct_dps AND dps.fid_cl_dps = tp.id_cl_dps AND dps.fid_tp_dps = tp.id_tp_dps "
                + "INNER JOIN erp.bpsu_bp AS bp ON dps.fid_bp_r = bp.id_bp "
                + "INNER JOIN erp.bpsu_bpb AS bpb ON dps.fid_cob = bpb.id_bpb "
                + "INNER JOIN erp.bpsu_bpb AS bb ON dps.fid_bpb = bb.id_bpb "
                + "INNER JOIN erp.cfgu_cur AS cur ON dps.fid_cur = cur.id_cur "
                + "LEFT OUTER JOIN trn_dps_ack AS ack ON dps.id_year = ack.fid_year AND dps.id_doc = ack.fid_doc "
                + "WHERE dps.b_del = 0 AND fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " + sqlWhere
                + "GROUP BY dps.id_year, dps.id_doc "
                + "ORDER BY tp.code, _num, dps.dt, dps.id_year, dps.id_doc;";
    }

    @Override
    public void actionNew() {

    }

    @Override
    public void actionEdit() {

    }

    @Override
    public void actionDelete() {
        
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == mjbDeliveryAckClose) {
                actionDeliveryAckDocStatus(true);
            }
            else if (button == mjbDeliveryAckOpen) {
                actionDeliveryAckDocStatus(false);
            }
            else if (button == mjbUploadFile) {
                actionUploadFile();
            }
            else if (button == mjbDownloadFiles) {
                actionDownloadFiles();
            }
            else if (button == mjbDeleteFiles) {
                actionDeleteFiles();
            }
            else if (button == mjbReasignFiles) {
                actionReasignFiles();
            }
            else if (button == mjbViewFiles) {
                actionViewFiles();
            }
            else if (button == mjbSendFiles) {
                actionSendFiles();
            }
        }
    }
}
