/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mmkt.form.SDialogCommissionsCalculate;
import erp.mod.mkt.db.SDbCommission;
import java.awt.Dimension;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewCommissions extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private SDialogCommissionsCalculate moDialogComputeCommisions;

    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    private javax.swing.JButton jbComputeCommisions;

    public SViewCommissions(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.MKT_COMMS, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        moDialogComputeCommisions = new SDialogCommissionsCalculate(miClient, SDataConstants.TRNX_DPS_COMMS_PEND);

        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moTabFilterDeleted = new STabFilterDeleted(this);

        jbComputeCommisions = new JButton(miClient.getImageIcon(SLibConstants.ICON_ACTION));
        jbComputeCommisions.setPreferredSize(new Dimension(23, 23));
        jbComputeCommisions.setToolTipText("Calcular comisiones");
        jbComputeCommisions.addActionListener(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbComputeCommisions);

        STableField[] aoKeyFields = new STableField[4];
        STableColumn[] aoTableColumns = new STableColumn[mnTabTypeAux01 != SDataConstants.MKTX_COMMS_RES ? 23 : 14];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_doc");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_ety");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_sal_agt");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "c.dt_comms", "Fecha coms", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "c.dt", "Fecha calc", STableConstants.WIDTH_DATE);
        if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave cliente", 50);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 200);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave cliente", 50);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb1.bpb", "Sucursal cliente", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "t.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_doc", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.stot_r", "Subtotal $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key", "Moneda local", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "agt.bp", "Agente ventas", 150);

        if (mnTabTypeAux01 != SDataConstants.MKTX_COMMS_RES) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", STableConstants.WIDTH_ITEM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Ítem clave", STableConstants.WIDTH_ITEM_KEY);
        }

        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_comms", "Comisión", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);

        if (mnTabTypeAux01 != SDataConstants.MKTX_COMMS_RES) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "f_comms_man", "Manual", STableConstants.WIDTH_BOOLEAN);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uc.usr", "Usr. cerrado", STableConstants.WIDTH_USER);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_usr_clo", "Cerrado", STableConstants.WIDTH_DATE_TIME);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_usr_ins", "Creación", STableConstants.WIDTH_DATE_TIME);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_usr_upd", "Modificación", STableConstants.WIDTH_DATE_TIME);
        }

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MKT_COMMS).Level;

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR && mnTabTypeAux01 != SDataConstants.MKTX_COMMS_RES ? true : false);
        jbComputeCommisions.setEnabled(true);


        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.MKT_COMMS_PAY);
        mvSuscriptors.add(SDataConstants.MKT_COMMS_LOG);
        mvSuscriptors.add(SDataConstants.CFGU_CUR);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    /*
     * Private methods:
     */

    public void actionComputeCommisions() {
        if (jbComputeCommisions.isEnabled()) {

            moDialogComputeCommisions.formReset();
            moDialogComputeCommisions.setVisible(true);
            if (moDialogComputeCommisions.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MKT).refreshCatalogues(mnTabType);
            }
        }
    }

    /*
     * Public methods:
     */

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_MKT).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MKT).refreshCatalogues(mnTabType);
            }
	}
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_MKT).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MKT).refreshCatalogues(mnTabType);
            }
	}
    }

    @Override
    public void actionDelete() {
        SDbCommission commission = null;

        if (jbDelete.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {

                    try {
                        commission = new SDbCommission();
                        commission.read(miClient.getSession(), (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                        if (commission.canDelete(miClient.getSession())) {

                            commission.delete(miClient.getSession());
                            miClient.getGuiModule(SDataConstants.MOD_MKT).refreshCatalogues(mnTabType);
                        }
                        else {
                            miClient.showMsgBoxWarning(commission.getQueryResult());
                        }

                    } catch (Exception e) {
                        SLibUtilities.printOutException(this, e);
                    }
                }
            }
	}
    }

    @Override
    public void createSqlQuery() {
        int[] period = null;
        Date tSqlDateCut = miClient.getSessionXXX().getWorkingDate();
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "c.b_del = FALSE ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                period = (int[]) setting.getSetting();
                switch (period.length) {
                    case 1:
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "YEAR(c.dt_comms) = " + period[0] + " ";
                        break;
                    case 2:
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "YEAR(c.dt_comms) = " + period[0] + " AND MONTH(c.dt_comms) = " + period[1] + " ";
                        break;
                    case 3:
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "YEAR(c.dt_comms) = " + period[0] + " AND MONTH(c.dt_comms) = " + period[1] + " AND DAY(c.dt_comms) = " + period[2] + " ";
                        break;
                    default:
                        break;
                }
            }
        }

        msSql = "SELECT c.id_year, c.id_doc, c.id_ety, c.id_sal_agt, agt.bp, t.code, " +
            "CONCAT(d.num_ser, IF(LENGTH(d.num_ser) = 0, '', '-'), d.num) AS f_num, d.dt_doc, d.num_ref, " +
            "bpb.bpb, bpb.code, b.bp, ct.bp_key, bpb1.bpb, " +
            "d.stot_r, agt.bp, c.dt, c.dt_comms, c.b_man AS f_comms_man, SUM(comms) AS f_comms, '" + miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey() + "' AS f_cur_key, " +
            "i.item, i.item_key, uc.usr, un.usr, ue.usr, c.ts_usr_clo, c.ts_usr_ins, c.ts_usr_upd " +
            "FROM mkt_comms AS c " +
            "INNER JOIN trn_dps AS d ON " +
            "c.id_year = d.id_year AND c.id_doc = d.id_doc " +
            "INNER JOIN trn_dps_ety AS de ON " +
            "d.id_year = de.id_year AND d.id_doc = de.id_doc AND c.id_ety = de.id_ety " +
            "INNER JOIN erp.itmu_item AS i ON " +
            "c.fk_item = i.id_item " +
            "INNER JOIN erp.bpsu_bp AS b ON " +
            "d.fid_bp_r = b.id_bp " +
            "INNER JOIN erp.bpsu_bp_ct AS ct ON " +
            "b.id_bp = ct.id_bp AND ct.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_CUS + " " +
            "INNER JOIN erp.trnu_tp_dps AS t ON " +
            "d.fid_ct_dps = t.id_ct_dps AND d.fid_cl_dps = t.id_cl_dps AND d.fid_tp_dps = t.id_tp_dps " +
            "INNER JOIN erp.bpsu_bpb AS bpb ON " +
            "d.fid_cob = bpb.id_bpb " +
            "INNER JOIN erp.bpsu_bpb AS bpb1 ON " +
            "d.fid_bpb = bpb1.id_bpb " +
            "INNER JOIN erp.bpsu_bp AS agt ON " +
            "c.id_sal_agt = agt.id_bp " +
            "INNER JOIN erp.usru_usr AS uc ON " +
            "c.fk_usr_clo = uc.id_usr " +
            "INNER JOIN erp.usru_usr AS un ON " +
            "c.fk_usr_ins = un.id_usr " +
            "INNER JOIN erp.usru_usr AS ue ON " +
            "c.fk_usr_upd = ue.id_usr " +
            (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) + " " +
            "GROUP BY " + (mnTabTypeAux01 != SDataConstants.MKTX_COMMS_RES ? "c.id_year, c.id_doc, c.id_ety, c.id_sal_agt " : "c.id_year, c.id_doc, c.id_sal_agt ") +
            "ORDER BY c.dt_comms, c.dt, b.bp, b.id_bp, ct.bp_key, bpb1.bpb, bpb1.id_bpb, t.code, f_num, d.num_ref, d.dt_doc ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbComputeCommisions) {
                actionComputeCommisions();
            }
        }
    }
}
