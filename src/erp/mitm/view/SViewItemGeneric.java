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
import erp.lib.table.STableCellRendererNumber;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import java.awt.Dimension;
import java.text.DecimalFormat;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;
/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SViewItemGeneric extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbCopy;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewItemGeneric(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.ITMU_IGEN);
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

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_ITM_ITEM).Level;

        jbNew.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbCopy.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[42];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ig.id_igen");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ifam.ifam", "Familia ítems", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "igrp.igrp", "Grupo ítems", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ig.igen", "Ítem genérico", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ig.code", "Código", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ict.ct_item", "Categoría ítem", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "icl.cl_item", "Clase ítem", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "itp.tp_item", "Tipo ítem", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cfd_pro_ser", "Producto-servicio SAT", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_bulk", "Granel", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_inv", "Inventariable", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_lot", "Lote", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tu.tp_unit", "Tipo unidad", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tuc.tp_unit", "Tipo unidad cont.", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tuv.tp_unit", "Tipo unidad virt.", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tunc.tp_unit", "Tipo unidad cont. neto", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tuncu.tp_unit", "Tipo unidad cont. neto u.", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "ig.days_exp", "Días anaquel", 50);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "ig.surplus_per", "Excedente predeterminado", STableConstants.WIDTH_PERCENTAGE);
        aoTableColumns[i++].setCellRenderer(new STableCellRendererNumber(new DecimalFormat("#,##0" + "." + SLibUtilities.textRepeat("0", 4) + "%")));
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "snr.tp_snr", "Tipo no. serie", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_weight_gross", "Peso bruto", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_weight_delivery", "Peso flete", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_line", "Línea ítems", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_item_key_edit", "Cve. modif.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_item_name_edit", "Nom. modif.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_item_short", "Nom. corto", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ig.igen_short", "Ítem genérico (corto)", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_free_disc_u", "S/desc. u.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_free_disc_ety", "S/desc. par.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_free_disc_doc", "S/desc. doc.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_free_price", "S/precio", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_free_disc", "S/desc.", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_free_comms", "S/comisión", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_ship_dom", "Embarque nacional", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_ship_int", "Embarque internacional", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_ship_qlt", "Calidad", STableConstants.WIDTH_BOOLEAN_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ig.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ig.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ig.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ig.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_IFAM);
        mvSuscriptors.add(SDataConstants.ITMU_IGRP);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
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
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                        miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).refreshCatalogues(mnTabType);
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
                if (miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).showFormForCopy(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).refreshCatalogues(mnTabType);
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "ig.b_del = FALSE ";
            }
        }

        msSql = "SELECT ig.id_igen, igen,igen, ig.igen_short, ig.code, ig.b_line, ig.b_item_short, ig.b_item_name_edit, ig.b_item_key_edit, " +
                "ig.b_inv, ig.b_lot, ig.b_bulk, ig.days_exp, ig.b_weight_gross, ig.b_weight_delivery, " +
                "ig.b_free_price, ig.b_free_disc, ig.b_free_disc_u, ig.b_free_disc_ety, ig.b_free_disc_doc, ig.b_free_comms, ig.b_ship_dom, ig.b_ship_int, ig.b_ship_qlt, ig.b_del, ig.surplus_per, " +
                "ict.ct_item, icl.cl_item, itp.tp_item, igrp.igrp, ifam.ifam, cfd_pro_ser.name AS _cfd_pro_ser, tu.tp_unit, tuc.tp_unit, tuv.tp_unit, tunc.tp_unit, tuncu.tp_unit, snr.tp_snr, " +
                "ig.ts_new, ig.ts_edit, ig.ts_del, un.usr, ue.usr, ud.usr " +
                "FROM erp.itmu_igen AS ig " +
                "INNER JOIN erp.itms_ct_item AS ict ON " +
                "ig.fid_ct_item = ict.id_ct_item " +
                "INNER JOIN erp.itms_cl_item AS icl ON " +
                "ig.fid_ct_item = icl.id_ct_item AND ig.fid_cl_item = icl.id_cl_item " +
                "INNER JOIN erp.itms_tp_item AS itp ON " +
                "ig.fid_ct_item = itp.id_ct_item AND ig.fid_cl_item = itp.id_cl_item AND ig.fid_tp_item = itp.id_tp_item " +
                "INNER JOIN erp.itmu_igrp AS igrp ON " +
                "ig.fid_igrp = igrp.id_igrp " +
                "INNER JOIN erp.itmu_ifam AS ifam ON " +
                "igrp.fid_ifam = ifam.id_ifam " +
                "INNER JOIN erp.itmu_tp_unit AS tu ON " +
                "ig.fid_tp_unit = tu.id_tp_unit " +
                "INNER JOIN erp.itmu_tp_unit AS tuc ON " +
                "ig.fid_tp_unit_units_cont = tuc.id_tp_unit " +
                "INNER JOIN erp.itmu_tp_unit AS tuv ON " +
                "ig.fid_tp_unit_units_virt = tuv.id_tp_unit " +
                "INNER JOIN erp.itmu_tp_unit AS tunc ON " +
                "ig.fid_tp_unit_net_cont = tunc.id_tp_unit " +
                "INNER JOIN erp.itmu_tp_unit AS tuncu ON " +
                "ig.fid_tp_unit_net_cont_u = tuncu.id_tp_unit " +
                "INNER JOIN erp.itms_tp_snr AS snr ON " +
                "ig.fid_tp_snr = snr.id_tp_snr " +
                "INNER JOIN erp.itms_cfd_prod_serv AS cfd_pro_ser ON " +
                "ig.fid_cfd_prod_serv = cfd_pro_ser.id_cfd_prod_serv " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "ig.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "ig.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "ig.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY ifam.ifam, igrp.fid_ifam, igrp.igrp, ig.fid_igrp, ig.igen, ig.id_igen ";
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
