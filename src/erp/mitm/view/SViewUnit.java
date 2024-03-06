/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mitm.form.SFormUnitEquivalences;
import java.awt.Dimension;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Alfonso Flores, Juan Barajas, Sergio Flores
 */
public class SViewUnit extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private SFormUnitEquivalences moUnitEquivalencesForm;
    
    private JButton jbEditAttributes;

    public SViewUnit(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.ITMU_UNIT);
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        
        jbEditAttributes = new JButton(miClient.getImageIcon(SLibConstants.ICON_KARDEX));
        jbEditAttributes.setPreferredSize(new Dimension(23, 23));
        jbEditAttributes.setToolTipText("Modificar atributos");
        jbEditAttributes.addActionListener(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbEditAttributes);

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[19];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "unit.id_unit");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_unit.tp_unit", "Tipo unidad", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "unit.unit", "Unidad", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "unit.symbol", "Símbolo", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cfdu_code", "ClaveUnidad SAT", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cfdu_name", "Unidad SAT", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "unit.unit_custs", "Nombre aduana", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "unit.symbol_custs", "Código aduana", 50);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "unit.unit_base_equiv", "Equiv. unidad base", 150);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererBaseEquivalence());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_unit.unit_base", "Unidad base", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "unit.sort_pos", "Ordenamiento", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "unit.b_can_edit", "Modificable", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "unit.b_can_del", "Eliminable", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "unit.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "unit.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "unit.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "unit.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_ITM_UNIT).Level;

        jbNew.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbDelete.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_TP_UNIT);
        mvSuscriptors.add(SDataConstants.USRU_USR);
        
        moUnitEquivalencesForm = new SFormUnitEquivalences(miClient);

        populateTable();
    }
    
    private void actionModifyAttributes() {
        if (jbEditAttributes.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                moUnitEquivalencesForm.setIdUnit(((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0]);
                moUnitEquivalencesForm.setVisible(true);
            }
        }
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (!moTablePane.getSelectedTableRow().getIsEditable()) {
                    miClient.showMsgBoxWarning(STableConstants.MSG_WAR_REGISTRY_NO_EDITABLE);
                }
                else {
                    if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                        miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).refreshCatalogues(mnTabType);
                    }
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

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "unit.b_del = FALSE ";
            }
        }

        msSql = "SELECT unit.id_unit, unit.unit, unit.symbol, unit.unit_base_equiv, unit.sort_pos, unit.b_can_edit, unit.b_can_del, unit.b_del, " +
                "unit.unit_custs, unit.symbol_custs, unit.b_can_edit AS " + STableConstants.FIELD_IS_EDITABLE + ", " +
                "tp_unit.tp_unit, tp_unit.unit_base, cfdu.code AS _cfdu_code, cfdu.name AS _cfdu_name, unit.fid_usr_new, unit.fid_usr_edit, unit.fid_usr_del, unit.ts_new, unit.ts_edit, unit.ts_del, " +
                "un.usr, ue.usr, ud.usr " +
                "FROM erp.itmu_unit AS unit " +
                "INNER JOIN erp.itmu_tp_unit AS tp_unit ON " +
                "unit.fid_tp_unit = tp_unit.id_tp_unit " +
                "INNER JOIN erp.itms_cfd_unit AS cfdu ON " +
                "unit.fid_cfd_unit = cfdu.id_cfd_unit " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "unit.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "unit.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "unit.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY tp_unit.tp_unit, tp_unit.id_tp_unit, unit.sort_pos, unit.unit, unit.id_unit ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();
            
            if (button == jbEditAttributes) {
                actionModifyAttributes();
            }
        }
    }
}
