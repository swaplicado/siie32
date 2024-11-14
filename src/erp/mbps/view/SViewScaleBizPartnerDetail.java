/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mbps.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;

/**
 *
 * @author Isabel Servín
 */
public class SViewScaleBizPartnerDetail extends erp.lib.table.STableTab {

    public SViewScaleBizPartnerDetail(SClientInterface client, String tabTitle, int subType) {
        super(client, tabTitle, SDataConstants.BPSX_SCA_BP_DET, subType);
        initComponents();
    }
    
    private void initComponents() {
        int i;
        
        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[7];

        i = 0;

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sca", "Báscula", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sca_key", "Clave báscula", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_sca", "Empresa báscula", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_siie", "Asociado negocios", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_def", "Predeterminado", STableConstants.WIDTH_BOOLEAN_3X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ts", "Creación", STableConstants.WIDTH_DATE_TIME);
      
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
        
        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);
        
        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_SCA_BP);
        mvSuscriptors.add(SDataConstants.BPSU_SCA_BP_MAP);
        mvSuscriptors.add(SDataConstants.CFGU_SCA);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void createSqlQuery() {
        msSql = "SELECT cs.sca_key, "
                + "cs.sca, "
                + "sb.name AS bp_sca, "
                + "bp.bp AS bp_siie, "
                + "b_def, "
                + "u.usr, "
                + "ts "
                + "FROM erp.bpsu_sca_bp AS sb " 
                + "INNER JOIN erp.bpsu_sca_bp_map AS sbm ON " 
                + "sb.id_sca = sbm.id_sca AND sb.id_sca_bp = sbm.id_sca_bp " 
                + "INNER JOIN erp.cfgu_sca AS cs ON " 
                + "sb.id_sca = cs.id_sca " 
                + "INNER JOIN erp.bpsu_bp AS bp ON " 
                + "sbm.id_bp = bp.id_bp "
                + "INNER JOIN erp.usru_usr AS u ON " 
                + "sbm.fid_usr = u.id_usr " 
                + "WHERE " + (mnTabTypeAux01 == SDataConstants.MOD_SAL ? "bp.b_cus " : "bp.b_sup ") + " "
                + "ORDER BY cs.sca, cs.sca_key, sb.name, bp.bp";
    }

    @Override
    public void actionNew() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionDelete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
