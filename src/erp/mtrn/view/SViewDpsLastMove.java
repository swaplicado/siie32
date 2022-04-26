package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDate;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;

/**
 * (2022-04-21 Sergio Flores) Esta vista pretende mostrar el movimiento más reciente de asociados de negocios con operaciones de contado.
 * Sin embargo, la consulta SQL está mal planteada, y tiene varios parámetros con constantes en duro que deben ser removidas.
 * 
 * @author Adrián Avilés
 */
public class SViewDpsLastMove extends erp.lib.table.STableTab implements java.awt.event.ActionListener{

    private erp.lib.table.STabFilterDate moTabFilterDate;
    
    public SViewDpsLastMove(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_LAST_MOV, auxType01);
        initComponents();
    }
    
     private void initComponents() {
        int i;

        moTabFilterDate = new STabFilterDate(miClient, this);
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDate);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL ? 9 : 8];
        
        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "e.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "e.id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "Cliente", "Cliente", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "DT", "Ultima actividad", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "Folio", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "Tot_anticipo", "Anticipo", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "tot_r", "Total", STableConstants.WIDTH_VALUE);
        if (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "Agent", "Agente de Vtas.", 150);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "Dia_actual", "Dia actual", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "Inactividad", "Inactividad", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "Saldo", "Saldo", STableConstants.WIDTH_VALUE);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
        setIsSummaryApplying(true);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_TP_BP);

        populateTable();
    }
    
    @Override
    public void createSqlQuery() {
        
        msSql = "SELECT e.id_year, e.id_doc," +
                
                "(SELECT bp.bp FROM erp_aeth.trn_dps AS d " +
                "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " +
                "WHERE d.id_year = 2021 AND d.id_doc = e.id_doc) AS Cliente, " +

                "(SELECT d.dt " +
                "FROM erp_aeth.trn_dps AS d " +
                "WHERE d.id_year = 2021 AND d.id_doc = e.id_doc) AS DT, " +
                    
                "(SELECT d.num " +
                "FROM erp_aeth.trn_dps AS d " +
                "WHERE d.id_year = 2021 AND d.id_doc = e.id_doc) AS Folio, " +
                
                "e.fid_item, e.tot_r, " +

                "SUM(IF(fid_item = 9516 OR fid_item = 9518,1,0)) AS fid_anticipo, " +

                "SUM(IF(fid_item = 9516 OR fid_item = 9518, e.tot_r, 0)) AS Tot_anticipo, " +

                "(SELECT DATEDIFF(NOW(),d.dt) " +
                "FROM erp_aeth.trn_dps AS d " +
                "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " +
                "WHERE d.id_year = 2021 " +
                "AND d.id_doc = e.id_doc) AS Inactividad, " +

                "CURRENT_DATE AS Dia_actual, " +
                
                "(SELECT b.bp " +
                "FROM erp.bpsu_bp AS b " +
                "WHERE b.id_bp = COALESCE((SELECT d.fid_sal_agt_n FROM erp_aeth.trn_dps AS d WHERE d.id_year = 2021 AND d.id_doc = e.id_doc), 0)) AS Agent, " +
                    
                "(SELECT SUM(re.debit - re.credit) " +
                "FROM erp_aeth.fin_rec_ety AS re " +
                "WHERE re.id_year = 2021 AND re.fid_dps_doc_n = e.id_doc " +
                "GROUP BY re.fid_dps_doc_n) AS Saldo " +
                
                "FROM erp_aeth.trn_dps_ety AS e " +
                "WHERE e.id_year = 2021 " +
                "AND NOT e.b_del " +
                "AND e.id_doc = (select d.id_doc " +
                                "FROM erp_aeth.trn_dps AS d " +
                                "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " +
                                "where id_year = 2021 AND d.fid_ct_dps = 2 AND d.fid_cl_dps = 3 AND d.fid_tp_dps = 1 AND NOT d.b_del AND d.fid_st_dps != 3 " +
                                "AND d.dt = (select max(d1.dt) " +
                                            "from erp_aeth.trn_dps AS d1 " +
                                            "where id_year = 2021 AND d1.fid_bp_r = d.fid_bp_r) " +
                                "AND e.id_doc = d.id_doc)" +
                "GROUP BY id_doc " +
                "ORDER BY Cliente; ";

    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        
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
    
}
