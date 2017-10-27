/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mitm.form.SPanelFilterItemGeneric;
import java.awt.Dimension;
import javax.swing.JButton;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SViewItemSimplified extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private int mnFormType;
    private int[] manItemClassKey;
    private javax.swing.JButton jbCopy;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.mitm.form.SPanelFilterItemGeneric moPanelFilterItemGeneric;

    public SViewItemSimplified(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType) {
        super(client, tabTitle, SDataConstants.ITMX_ITEM_SIMPLE, auxType);

        if (SLibUtilities.belongsTo(mnTabTypeAux01, new int[] { SDataConstants.ITMX_ITEM_IDX_SAL_PRO, SDataConstants.ITMX_ITEM_IDX_PUR_CON })) {
            mnFormType = SDataConstants.ITMU_ITEM;
        }
        else {
            mnFormType = SDataConstants.ITMX_ITEM_SIMPLE;
        }

        switch (mnTabTypeAux01) {
            case SDataConstants.ITMX_ITEM_IDX_SAL_PRO:
                manItemClassKey = SDataConstantsSys.ITMS_CL_ITEM_SAL_PRO;
                break;
            case SDataConstants.ITMX_ITEM_IDX_SAL_SRV:
                manItemClassKey = SDataConstantsSys.ITMS_CL_ITEM_SAL_SRV;
                break;
            case SDataConstants.ITMX_ITEM_IDX_ASS_ASS:
                manItemClassKey = SDataConstantsSys.ITMS_CL_ITEM_ASS_ASS;
                break;
            case SDataConstants.ITMX_ITEM_IDX_PUR_CON:
                manItemClassKey = SDataConstantsSys.ITMS_CL_ITEM_PUR_CON;
                break;
            case SDataConstants.ITMX_ITEM_IDX_PUR_EXP:
                manItemClassKey = SDataConstantsSys.ITMS_CL_ITEM_PUR_EXP;
                break;
            case SDataConstants.ITMX_ITEM_IDX_EXP_MFG:
                manItemClassKey = SDataConstantsSys.ITMS_CL_ITEM_EXP_MFG;
                break;
            case SDataConstants.ITMX_ITEM_IDX_EXP_OPE:
                manItemClassKey = SDataConstantsSys.ITMS_CL_ITEM_EXP_OPE;
                break;
            default:
        }

        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        jbCopy = new JButton(miClient.getImageIcon(SLibConstants.ICON_COPY));
        jbCopy.setPreferredSize(new Dimension(23, 23));
        jbCopy.setToolTipText("Copiar");
        jbCopy.addActionListener(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbCopy);

        moTabFilterDeleted = new STabFilterDeleted(this);
        moPanelFilterItemGeneric = new SPanelFilterItemGeneric(miClient, this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moPanelFilterItemGeneric);

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_ITM_ITEM).Level;

        jbNew.setEnabled(levelRightEdit >= SDataConstantsSys.USRS_TP_LEV_AUTHOR);
        jbEdit.setEnabled(levelRightEdit >= SDataConstantsSys.USRS_TP_LEV_AUTHOR);
        jbCopy.setEnabled(levelRightEdit >= SDataConstantsSys.USRS_TP_LEV_AUTHOR);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[21];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "i.id_item");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 300);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 300);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ig.igen", "Ítem genérico", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "il.line", "Línea ítems", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_ref", "Referencia", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_pre_pay", "Anticipo", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ae.tp_acc_ebitda", "Cuenta EBITDA", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_fiscal_acc_inc", "Código agrupador SAT (ingresos)", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_fiscal_acc_exp", "Código agrupador SAT (egresos)", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cfdps_code", "ClaveProdServ SAT", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cfdps_name", "ProdServ SAT", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.tariff", "Fracc. arancelaria", 55);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "si.name", "Estatus", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "i.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "i.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "i.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "i.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.ITMU_IGEN);
        mvSuscriptors.add(SDataConstants.ITMU_UNIT);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).showForm(mnFormType, mnTabTypeAux01, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).refreshCatalogues(SDataConstants.ITMU_ITEM);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).showForm(mnFormType, mnTabTypeAux01, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                        miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).refreshCatalogues(SDataConstants.ITMU_ITEM);
                }
            }
        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    public void actionCopy() {
        if (jbCopy.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).showFormForCopy(mnFormType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).refreshCatalogues(SDataConstants.ITMU_ITEM);
                }
            }
        }
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "i.b_del = 0 ";
            }

            if (setting.getType() == STableConstants.SETTING_FILTER_ITM_ITEM_GENERIC) {
                if (((int[]) setting.getSetting())[0] != SLibConstants.UNDEFINED) {
                    sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "i.fid_igen = " + ((int[]) setting.getSetting())[0] + " ";
                }
            }
        }

        msSql = "SELECT i.id_item, i.item, i.item_key, i.b_ref, i.b_pre_pay, i.tariff, i.b_del, i.ts_new, i.ts_edit, i.ts_del, " +
                "ig.igen, si.name, u.symbol, ae.tp_acc_ebitda, CONCAT(fai.code, ' - ', fai.name) AS f_fiscal_acc_inc, CONCAT(fae.code, ' - ', fae.name) AS f_fiscal_acc_exp, " +
                "un.usr, ue.usr, ud.usr, il.line, cfdps.code AS _cfdps_code, cfdps.name AS _cfdps_name " +
                "FROM erp.itmu_item AS i " +
                "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " +
                "INNER JOIN erp.itms_st_item AS si ON i.fid_st_item = si.id_st_item " +
                "INNER JOIN erp.itmu_unit AS u ON i.fid_unit = u.id_unit " +
                "INNER JOIN erp.finu_tp_acc_ebitda AS ae ON i.fid_tp_acc_ebitda = ae.id_tp_acc_ebitda " +
                "INNER JOIN erp.fins_fiscal_acc AS fai ON i.fid_fiscal_acc_inc = fai.id_fiscal_acc " +
                "INNER JOIN erp.fins_fiscal_acc AS fae ON i.fid_fiscal_acc_exp = fae.id_fiscal_acc " +
                "INNER JOIN erp.usru_usr AS un ON i.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON i.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON i.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN erp.itmu_line AS il ON i.fid_line_n = il.id_line " +
                "LEFT OUTER JOIN erp.itms_cfd_prod_serv AS cfdps ON " +
                "i.fid_cfd_prod_serv_n = cfdps.id_cfd_prod_serv " +
                "WHERE ig.fid_ct_item = " + manItemClassKey[0] + " AND ig.fid_cl_item = " + manItemClassKey[1] + " " +
                (sqlWhere.isEmpty() ? "" : " AND " + sqlWhere) + " " +
                "ORDER BY " + (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                    "i.item_key, i.item, " :
                    "i.item, i.item_key, ") + "i.id_item ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbCopy) {
                actionCopy();
            }
        }
    }
}
