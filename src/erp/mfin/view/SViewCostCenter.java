/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mcfg.data.SDataParamsCompany;
import java.awt.Dimension;
import java.util.Vector;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Sergio Flores
 */
public class SViewCostCenter extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton mjbNewMajor;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewCostCenter(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.FIN_CC);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        mjbNewMajor = new JButton(miClient.getImageIcon(SLibConstants.ICON_NEW_MAIN));

        mjbNewMajor.setPreferredSize(new Dimension(23, 23));

        mjbNewMajor.addActionListener(this);

        mjbNewMajor.setToolTipText("Crear centro principal");

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbNewMajor);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        STableField[] aoKeyFields = new STableField[1];
        STableField[] aoAditionalFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[14];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.id_cc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoAditionalFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "f_is_major");
        for (i = 0; i < aoAditionalFields.length; i++) {
            moTablePane.getAditionalFields().add(aoAditionalFields[i]);
        }

        i = 0;
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cc", "No. centro costo", STableConstants.WIDTH_ACCOUNT_ID);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererStyle());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.cc", "Centro costo", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererStyle());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "c.deep", "Profundidad", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "c.lev", "Nivel", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "c.dt_start", "Ini. vigencia", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "c.dt_end_n", "Fin. vigencia", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "c.b_act", "Activo", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "c.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "c.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_CC).Level;

        jbNew.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        mjbNewMajor.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    public void actionNewMajor() {
        if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(SDataConstants.FINX_CC_MAJOR, null) == SLibConstants.DB_ACTION_SAVE_OK) {
            miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(mnTabType);
        }
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            int formType = mnTabType;

            if (moTablePane.getSelectedTableRow() != null) {
                if ((Boolean) ((Object[]) moTablePane.getSelectedTableRow().getData())[0]) {
                    formType = SDataConstants.FINX_CC_MAJOR;
                }

                if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(formType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        STableSetting setting = null;
        String costCenter = miClient.getSessionXXX().getParamsErp().getFormatCostCenterId().replace('9', '0');
        Vector<Integer> levels = SDataUtilities.getAccountLevels(costCenter);

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "c.b_del = FALSE ";
            }
        }

        msSql = "SELECT c.id_cc, f_acc_usr(" + ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter() + ", c.code) AS f_cc, c.cc, c.deep, c.lev, c.dt_start, c.dt_end_n, c.b_act, c.b_del, " +
                "c.ts_new, c.ts_edit, c.ts_del, un.usr, ue.usr, ud.usr, c.lev = 1 AS f_is_major, " +
                "IF(c.lev = 1, " + STableConstants.STYLE_BOLD + ", IF(c.lev < cm.deep, " + STableConstants.STYLE_ITALIC + ", " + STableConstants.UNDEFINED + ")) AS f_style " +
                "FROM fin_cc AS c " +
                "INNER JOIN fin_cc AS cm ON " +
                "CONCAT(LEFT(c.id_cc, " + (levels.get(1) - 1) + "), '" + costCenter.substring(levels.get(1) - 1) + "') = cm.id_cc " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "c.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "c.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "c.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY c.id_cc ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjbNewMajor) {
                actionNewMajor();
            }
        }
    }
}
