/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STabFilterStatus;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.mmfg.data.SDataBom;
import java.awt.Cursor;
import java.awt.Dimension;
import java.util.Map;
import javax.swing.JButton;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Néstor Ávalos, César Orozco  
 */
public class SViewBom extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterStatus moTabFilterStatus;

    private javax.swing.JButton jbPrint;

    public SViewBom(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.MFG_BOM);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterStatus = new STabFilterStatus(this, miClient);
        jbPrint = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        jbPrint.setPreferredSize(new Dimension(23, 23));
        jbPrint.addActionListener(this);
        jbPrint.setToolTipText("Imprimir lista de ingredientes");

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterStatus);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbPrint);

        jbNew.setEnabled(true);
        jbEdit.setEnabled(true);
        //jbDelete.setEnabled(false);

        erp.lib.table.STableField[] aoKeyFields = new STableField[1];
        erp.lib.table.STableColumn[] aoTableColumns = new STableColumn[17];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "b.id_bom");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal empresa", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Producto", 250);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bom", "Referencia", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "b.qty", "Lote sugerido", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "b.cost_per", "Cto. asig.", STableConstants.WIDTH_PERCENTAGE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererPercentage());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "si.name", "Estatus", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "b.ts_start", "Ini. vigencia", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "b.ts_end_n", "Fin. vigencia", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "b.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "b.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "b.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_MFG).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_MFG ).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
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
        java.lang.String sqlWhere = " b.fid_item_n is null ";
        erp.lib.table.STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "b.b_del = FALSE ";
            }
            if (setting.getType() == STableConstants.SETTING_FILTER_STATUS && setting.getStatus() == STableConstants.STATUS_ON && setting.getSetting().equals("ACTIVO") ){
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "si.name = 'ACTIVO' ";
            }
            if (setting.getType() == STableConstants.SETTING_FILTER_STATUS && setting.getStatus() == STableConstants.STATUS_ON && setting.getSetting().equals("RESTRINGIDO") ){
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "si.name = 'RESTRINGIDO' ";
            }
            if (setting.getType() == STableConstants.SETTING_FILTER_STATUS && setting.getStatus() == STableConstants.STATUS_ON && setting.getSetting().equals("INACTIVO") ){
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "si.name = 'INACTIVO' ";
            }
        }

        msSql = "SELECT b.*, i.item_key, i.item, u.symbol, bpb.bpb, un.usr, ue.usr, ud.usr, si.name " +
                "FROM mfg_bom as b " +
                "INNER JOIN erp.itmu_item AS i ON b.fid_item = i.id_item " +
                "INNER JOIN erp.itms_st_item AS si ON " +
                "i.fid_st_item = si.id_st_item " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON b.fid_cob = bpb.id_bpb " +
                "INNER JOIN erp.itmu_unit AS u ON b.fid_unit = u.id_unit " +
                "INNER JOIN erp.usru_usr AS un ON b.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON b.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON b.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY bpb.bpb, " +
                (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                        "i.item_key, i.item " : "i.item, i.item_key ");
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbPrint) {
                actionPrint();
            }
        }
    }

    public void actionPrint() {
        SDataBom oBom = null;
        Cursor cursor = getCursor();
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;

        if (moTablePane.getSelectedTableRow() != null) {
            oBom = (SDataBom) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_BOM, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

            try {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));

                map = miClient.createReportParams();
                map.put("sCompanyBranch", oBom.getDbmsCompanyBranch());
                map.put("sFinishedGood", oBom.getDbmsItem());
                map.put("sReference", oBom.getBom());
                map.put("nQuantity", oBom.getQuantity());
                map.put("sUnit", oBom.getDbmsItemUnitSymbol());
                map.put("sLevel", "0");
                map.put("tDateStart", oBom.getDateStart());
                map.put("tDateEnd_n", oBom.getDateEnd_n());
                map.put("nSqlRoot", oBom.getRoot());
                map.put("nSqlTempTable", "temp" + new java.util.Date());
                map.put("nSqlSortItemKey", miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId());

                jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_MFG_BOM, map);
                jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Impresión lista de ingredientes/materiales de la fórmula");
                jasperViewer.setVisible(true);
            }
            catch (Exception e) {
                System.out.println("Mensaje de error " + e.getMessage());
            }
            finally {
                setCursor(cursor);
            }
        }
    }
}
