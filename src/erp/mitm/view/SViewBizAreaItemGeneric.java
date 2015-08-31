/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.view;

import javax.swing.JButton;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableColumn;

/**
 *
 * @author Alfonso Flores
 */
public class SViewBizAreaItemGeneric extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    public SViewBizAreaItemGeneric(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.ITMU_IGEN_BA);
        initComponents();
    }

    private void initComponents() {
        int i;

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[2];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bi.id_igen");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bi.id_ba");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.ba", "Área negocios", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.igen", "Ítem genérico", 300);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_IGEN);
        mvSuscriptors.add(SDataConstants.BPSU_BA);

        populateTable();
    }

    @Override
    public void createSqlQuery() {
        msSql = "SELECT bi.id_igen, bi.id_ba, b.ba, i.igen " +
                "FROM erp.itmu_igen_ba AS bi " +
                "INNER JOIN erp.bpsu_ba AS b ON " +
                "bi.id_ba = b.id_ba " +
                "INNER JOIN erp.itmu_igen AS i ON " +
                "bi.id_igen = i.id_igen " +
                "ORDER BY b.ba, i.igen ";
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {

        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {

        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();
        }
    }
}
