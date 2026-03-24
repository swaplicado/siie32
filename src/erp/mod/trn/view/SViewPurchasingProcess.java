/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.cfg.swap.SHttpConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.swap.utils.SExportUtils;
import erp.mod.cfg.utils.SAuthJsonUtils;
import erp.mod.view.SViewFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Adrian Aviles
 */
public class SViewPurchasingProcess extends SGridPaneView implements ActionListener, ItemListener {
    
    private JLabel jlDate;
    private JRadioButton jrbDateReq;
    private JRadioButton jrbDateOc;
    private ButtonGroup bgDate;
    
    private SGridFilterDatePeriod moFilterDatePeriod;
    private SViewFilter moFilterFuncArea;
    
    private String sFilterInit;
    private SGuiDate moFilterTimeInit;
    
    public SViewPurchasingProcess(SGuiClient client, int subType, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.TRNX_MAT_REQ_PUR_PROC , subType, title, params);
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        setRowButtonsEnabled(true);
        
        jlDate = new JLabel("Fecha:");
        jrbDateReq = new JRadioButton("Requisición");
        jrbDateReq.setToolTipText("Filtrar por la fecha de la requisición");

        jrbDateOc = new JRadioButton("Pedido compras");
        jrbDateOc.setToolTipText("Filtrar por la fecha de la orden de compra");

        bgDate = new ButtonGroup();
        bgDate.add(jrbDateReq);
        bgDate.add(jrbDateOc);

        bgDate.setSelected(jrbDateReq.getModel(), true);
        
        jrbDateReq.addItemListener(this);
        jrbDateOc.addItemListener(this);
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jrbDateReq);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jrbDateOc);
        
        setRowButtonsEnabled(false);
        
        jbRowDelete.setEnabled(false);

        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        
        moFilterFuncArea = new SViewFilter(miClient, this, SModConsts.CFGU_FUNC);
        moFilterFuncArea.initFilter(null);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterFuncArea);
        
        jbRowNew.setEnabled(false);
        jbRowCopy.setEnabled(false);
        jbRowDisable.setEnabled(false);
        jbRowDelete.setEnabled(false);
        jbRowEdit.setEnabled(false);
        jtbFilterDeleted.setEnabled(false);
        
        sFilterInit = ((SGridFilterValue) moFiltersMap.get(SModConsts.CFGU_FUNC)).getValue().toString();
        moFilterTimeInit = new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime());
        
    }
    
    /**
     * Metodo para crear el JSON para la consulta al backend transacciones (get-info-purchasing-process)
     * @param idsOC de tipo List<String[]> conformado [[id_year_oc, id_doc_oc],...]
     * @return 
     * @throws java.lang.Exception
    */
    public String crearJson(List<String[]> idsOC) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        
        // Crear el nodo principal
        ObjectNode rootNode = mapper.createObjectNode();
        
        // Crear el array "items"
        ArrayNode itemsArray = mapper.createArrayNode();
        
        // Recorrer la lista de arrays
        for (String[] datos : idsOC) {
            ObjectNode itemNode = mapper.createObjectNode();
            
            // Agregar company_id fijo
            itemNode.put("company_id", miClient.getSession().getConfigCompany().getCompanyId());
            
            // Agregar los valores del array
            // Asumiendo que el array tiene [id_year_oc, id_doc_oc]
            if (datos.length >= 2) {
                itemNode.put("id_year", Integer.parseInt(datos[0]));
                itemNode.put("id_doc", Integer.parseInt(datos[1]));
            }
            
            itemsArray.add(itemNode);
        }
        
        rootNode.set("items", itemsArray);
        
        // Convertir a String JSON
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }
    
    /**
     * @param mywhere String de consulta where
     * @throws java.sql.SQLException
     */
    public void crearYInsertarTablaTemporal(String mywhere) throws SQLException {
        // 1. Eliminar la tabla temporal si ya existe en esta sesión
        String dropTableSQL = "DROP TEMPORARY TABLE IF EXISTS tmp_reporte_compras";
        try (Statement stmt = miClient.getSession().getStatement()) {
            stmt.execute(dropTableSQL);
            // System.out.println("Tabla temporal eliminada (si existía)");
        }

        // 2. Crear la tabla temporal
        String createTableSQL = "CREATE TEMPORARY TABLE tmp_reporte_compras (" +
            // Requisición (RM)
            "    id_rm INT,\n" +
            "    f_id_1 INT,\n" +
            "    f_code INT,\n" +
            "    f_name INT,\n" +
            "    folio VARCHAR(50),\n" +
            "    f_date DATETIME,\n" +
            "    auth_status VARCHAR(50),\n" +
            "    code_rm VARCHAR(50),\n" +
            "    name_rm VARCHAR(50),\n" +
            "    b_del_rm TINYINT(1),\n" +
            "    folio_rm VARCHAR(50),\n" +
            "    fecha_envio_rm DATETIME,\n" +
            "    date_rm DATETIME,\n" +
            "    fk_usr_ins_rm INT,\n" +
            "    fk_usr_upd_rm INT,\n" +
            "    ts_usr_ins_rm DATETIME,\n" +
            "    ts_usr_upd_rm DATETIME,\n" +
            "    f_usr_ins_rm VARCHAR(100),\n" +
            "    f_usr_upd_rm VARCHAR(100),\n" +
            "    auth_status_rm VARCHAR(20),\n" +
            // Orden de Compra (OC)
            "    id_year_oc INT,\n" +
            "    id_doc_oc INT,\n" +
            "    b_del_oc TINYINT(1),\n" +
            "    folio_oc VARCHAR(100),\n" +
            "    date_oc DATETIME,\n" +
            "    proveedor_oc VARCHAR(200),\n" +
            "    auth_status_oc VARCHAR(20),\n" +
            "    user_in_turn_oc VARCHAR(500),\n" +
            "    fecha_auth_oc DATETIME,\n" +
            "    fid_func_oc INT,\n" +
            // Entradas Almacén
            "    entrada_almacen VARCHAR(50),\n" +
            // Facturas
            "    id_year_fac INT,\n" +
            "    id_doc_fac INT,\n" +
            "    b_del_fac TINYINT(1),\n" +
            "    folio_fac VARCHAR(100),\n" +
            "    date_fac DATETIME,\n" +
            "    auth_status_fac VARCHAR(20),\n" +
            "    user_in_turn_fac VARCHAR(500),\n" +
            "    fac_contabilizada VARCHAR(2),\n" +
            "    id_fac_pc INT,\n" +
            // Solicitudes pagos
            "    id_pay INT,\n" +
            "    folio_pago VARCHAR(100),\n" +
            "    fecha_programada_pago DATE,\n" +
            "    pago_manual TINYINT(1),\n" +
            "    estatus_solicitud_pago VARCHAR(100),\n" +
            // Pagos
            "    id_pay_p INT,\n" +
            "    folio_pago_p VARCHAR(100),\n" +
            "    fecha_ejecucion_pago_p DATE,\n" +
            "    estatus_pago_p VARCHAR(100), " +
            // CRP
            "    requiere_crp VARCHAR(2),\n" +
            "    num_crp VARCHAR(20),\n" +
            "    estatus_crp VARCHAR(100) " +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

        try (Statement stmt = miClient.getSession().getStatement()) {
            stmt.execute(createTableSQL);
            // System.out.println("Tabla temporal creada");
        }
        // System.out.println("Insertando datos en tabla temporal");
        // 3. Insertar los datos
        String insertSQL = "INSERT INTO tmp_reporte_compras " +
                            "(" + 
                            "id_rm, f_id_1, f_code, f_name, folio, f_date, auth_status, code_rm, name_rm, b_del_rm, folio_rm, fecha_envio_rm, " +
                            "date_rm, fk_usr_ins_rm, fk_usr_upd_rm, ts_usr_ins_rm, ts_usr_upd_rm, " +
                            "f_usr_ins_rm, f_usr_upd_rm, auth_status_rm, " +
                            "id_year_oc, id_doc_oc, b_del_oc, folio_oc, date_oc, proveedor_oc, " +
                            "auth_status_oc, user_in_turn_oc, fecha_auth_oc, fid_func_oc, " +
                            "entrada_almacen, " +
                            "id_year_fac, id_doc_fac, b_del_fac, folio_fac, date_fac, " +
                            "auth_status_fac, user_in_turn_fac, fac_contabilizada, id_fac_pc, " +
                            "id_pay, folio_pago, fecha_programada_pago, pago_manual, estatus_solicitud_pago, " +
                            "id_pay_p, folio_pago_p, fecha_ejecucion_pago_p, estatus_pago_p, " +
                            "requiere_crp, num_crp, estatus_crp " +
                            ") " +
                               "SELECT\n" +
                                "    v.id_mat_req AS id_rm,\n" +
                                "    NULL AS f_id_1,\n" +
                                "    NULL AS f_code,\n" +
                                "    NULL AS f_name,\n" +
                                "    NULL AS folio,\n" +
                                "    NOW() AS f_date,\n" +
                                "    NULL AS auth_status,\n" +
                                "    v.num AS code_rm,\n" +
                                "    v.num AS name_rm,\n" +
                                "    v.b_del AS b_del_rm,\n" +
                                "    LPAD(v.num, 6, 0) AS folio_rm,\n" +
                                "    NULL AS fecha_envio_rm,\n" +
                                "    v.dt AS date_rm,\n" +
                                "    v.fk_usr_ins AS fk_usr_ins_rm,\n" +
                                "    v.fk_usr_upd AS fk_usr_upd_rm,\n" +
                                "    v.ts_usr_ins AS ts_usr_ins_rm,\n" +
                                "    v.ts_usr_upd AS ts_usr_upd_rm,\n" +
                                "    ui.usr AS f_usr_ins_rm,\n" +
                                "    uu.usr AS f_usr_upd_rm,\n" +
                                "\n" +
                                "    CASE\n" +
                                "        WHEN cfg_get_st_authorn(1, 'trn_mat_req', v.id_mat_req, NULL, NULL, NULL, NULL) = 4 THEN 'AUTORIZADO'\n" +
                                "        WHEN cfg_get_st_authorn(1, 'trn_mat_req', v.id_mat_req, NULL, NULL, NULL, NULL) = 5 THEN 'RECHAZADO'\n" +
                                "        WHEN cfg_get_st_authorn(1, 'trn_mat_req', v.id_mat_req, NULL, NULL, NULL, NULL) = 2 THEN 'PENDIENTE'\n" +
                                "        WHEN cfg_get_st_authorn(1, 'trn_mat_req', v.id_mat_req, NULL, NULL, NULL, NULL) = 3 THEN 'EN PROCESO'\n" +
                                "        ELSE 'NA'\n" +
                                "    END AS auth_status_rm,\n" +
                                "\n" +
                                /* ================= OC ================= */
                                "\n" +
                                "    oc.id_year AS id_year_oc,\n" +
                                "    oc.id_doc AS id_doc_oc,\n" +
                                "    oc.b_del AS b_del_oc,\n" +
                                "    CONCAT(oc.num_ser, IF(LENGTH(oc.num_ser) = 0, '', '-'), oc.num) AS folio_oc,\n" +
                                "    oc.dt AS date_oc,\n" +
                                "\n" +
                                "    bp.bp AS proveedor_oc,\n" +
                                "\n" +
                                "    CASE oc.fid_st_dps_authorn\n" +
                                "        WHEN 1 THEN 'NUEVA'\n" +
                                "        WHEN 2 THEN 'PENDIENTE'\n" +
                                "        WHEN 3 THEN 'RECHAZADO'\n" +
                                "        WHEN 4 THEN 'AUTORIZADO'\n" +
                                "        ELSE 'N/D'\n" +
                                "    END AS auth_status_oc,\n" +
                                "\n" +
                                /* USER IN TURN OC */
                                "\n" +
                                "    IF (\n" +
                                "        (\n" +
                                "            SELECT COUNT(*)\n" +
                                "            FROM cfgu_authorn_step stp\n" +
                                "            WHERE NOT stp.b_del\n" +
                                "            AND stp.res_tab_name_n = 'trn_dps'\n" +
                                "            AND stp.fk_tp_authorn = 2\n" +
                                "            AND stp.res_pk_n1_n = oc.id_year\n" +
                                "            AND stp.res_pk_n2_n = oc.id_doc\n" +
                                "        ) > 0,\n" +
                                "\n" +
                                "        IF(oc.fid_st_dps_authorn = 3,\n" +
                                "\n" +
                                "            COALESCE(\n" +
                                "                (\n" +
                                "                    SELECT GROUP_CONCAT(u.usr SEPARATOR ',')\n" +
                                "                    FROM cfgu_authorn_step steps1\n" +
                                "                    INNER JOIN erp.usru_usr u ON steps1.fk_usr_step = u.id_usr\n" +
                                "                    WHERE NOT steps1.b_del\n" +
                                "                    AND steps1.res_tab_name_n = 'trn_dps'\n" +
                                "                    AND steps1.fk_tp_authorn = 2\n" +
                                "                    AND steps1.res_pk_n1_n = oc.id_year\n" +
                                "                    AND steps1.res_pk_n2_n = oc.id_doc\n" +
                                "                    AND NOT steps1.b_authorn\n" +
                                "                    AND steps1.b_reject\n" +
                                "                    AND steps1.lev = (\n" +
                                "                        SELECT step2.lev\n" +
                                "                        FROM cfgu_authorn_step step2\n" +
                                "                        WHERE NOT step2.b_del\n" +
                                "                        AND step2.res_tab_name_n = 'trn_dps'\n" +
                                "                        AND step2.fk_tp_authorn = 2\n" +
                                "                        AND step2.res_pk_n1_n = oc.id_year\n" +
                                "                        AND step2.res_pk_n2_n = oc.id_doc\n" +
                                "                        AND NOT step2.b_authorn\n" +
                                "                        AND step2.b_reject\n" +
                                "                        ORDER BY step2.lev DESC\n" +
                                "                        LIMIT 1\n" +
                                "                    )\n" +
                                "                ), ''\n" +
                                "            ),\n" +
                                "\n" +
                                "            IF(oc.fid_st_dps_authorn = 4,\n" +
                                "\n" +
                                "                (\n" +
                                "                    SELECT usr\n" +
                                "                    FROM erp.usru_usr\n" +
                                "                    WHERE id_usr = oc.fid_usr_authorn\n" +
                                "                ),\n" +
                                "\n" +
                                "                COALESCE(\n" +
                                "                    (\n" +
                                "                        SELECT GROUP_CONCAT(u.usr SEPARATOR ',')\n" +
                                "                        FROM cfgu_authorn_step steps1\n" +
                                "                        INNER JOIN erp.usru_usr u ON steps1.fk_usr_step = u.id_usr\n" +
                                "                        WHERE NOT steps1.b_del\n" +
                                "                        AND steps1.res_tab_name_n = 'trn_dps'\n" +
                                "                        AND steps1.fk_tp_authorn = 2\n" +
                                "                        AND steps1.res_pk_n1_n = oc.id_year\n" +
                                "                        AND steps1.res_pk_n2_n = oc.id_doc\n" +
                                "                        AND NOT steps1.b_authorn\n" +
                                "                        AND NOT steps1.b_reject\n" +
                                "                        AND steps1.lev = (\n" +
                                "                            SELECT step2.lev\n" +
                                "                            FROM cfgu_authorn_step step2\n" +
                                "                            WHERE NOT step2.b_del\n" +
                                "                            AND step2.res_tab_name_n = 'trn_dps'\n" +
                                "                            AND step2.fk_tp_authorn = 2\n" +
                                "                            AND step2.res_pk_n1_n = oc.id_year\n" +
                                "                            AND step2.res_pk_n2_n = oc.id_doc\n" +
                                "                            AND NOT step2.b_authorn\n" +
                                "                            AND NOT step2.b_reject\n" +
                                "                            ORDER BY step2.lev ASC\n" +
                                "                            LIMIT 1\n" +
                                "                        )\n" +
                                "                    ), ''\n" +
                                "                )\n" +
                                "            )\n" +
                                "        ),\n" +
                                "\n" +
                                "        COALESCE(\n" +
                                "            (\n" +
                                "                SELECT ug.usr\n" +
                                "                FROM cfgu_authorn_step gstp\n" +
                                "                LEFT JOIN erp.usru_usr ug ON gstp.fk_usr_step = ug.id_usr\n" +
                                "                WHERE NOT gstp.b_del\n" +
                                "                AND gstp.res_tab_name_n = 'trn_dps'\n" +
                                "                AND gstp.fk_tp_authorn = 3\n" +
                                "                AND gstp.res_pk_n1_n = oc.id_year\n" +
                                "                AND gstp.res_pk_n2_n = oc.id_doc\n" +
                                "                LIMIT 1\n" +
                                "            ),\n" +
                                "            'N/D'\n" +
                                "        )\n" +
                                "    ) AS user_in_turn_oc,\n" +
                                "    CASE \n" +
                                "       WHEN oc.fid_st_dps_authorn IN (3, 4) \n" +
                                "       THEN oc.ts_authorn\n" +
                                "    ELSE NULL\n" +
                                "    END AS fecha_auth_oc,\n" +
                                "    oc.fid_func AS fid_func_oc,\n" +
                                "    \n" +
                                /* ================= ENTRADAS ALMACEN ================= */
                                "\n" +
                                "    NULL AS entrada_almacen,\n" +
                                "\n" +
                                /* ================= FACTURAS ================= */
                                "\n" +
                                "    fac.id_year AS id_year_fac,\n" +
                                "    fac.id_doc AS id_doc_fac,\n" +
                                "    fac.b_del AS b_del_fac,\n" +
                                "    CONCAT(fac.num_ser, IF(LENGTH(fac.num_ser) = 0, '', '-'), fac.num) AS folio_fac,\n" +
                                "    fac.dt AS date_fac,\n" +
                                "    NULL AS auth_status_fac,\n" +
                                "    NULL AS user_in_turn_fac,\n" +
                                "\n" +
                                "    CASE\n" +
                                "        WHEN EXISTS (\n" +
                                "            SELECT 1\n" +
                                "            FROM trn_dps_rec dr\n" +
                                "            INNER JOIN fin_rec fr\n" +
                                "                ON fr.id_year = dr.fid_rec_year\n" +
                                "                AND fr.id_per = dr.fid_rec_per\n" +
                                "                AND fr.id_bkc = dr.fid_rec_bkc\n" +
                                "                AND fr.id_tp_rec = dr.fid_rec_tp_rec\n" +
                                "                AND fr.id_num = dr.fid_rec_num\n" +
                                "                AND fr.b_del = 0\n" +
                                "            WHERE dr.id_dps_year = fac.id_year\n" +
                                "            AND dr.id_dps_doc = fac.id_doc\n" +
                                "        )\n" +
                                "        THEN '1'\n" +
                                "        ELSE '0'\n" +
                                "    END AS fac_contabilizada,\n" +
                                "    fac_pc.ext_data_id as id_fac_pc,\n" +
                                "\n" +
                                /* ================= SOLICITUDES PAGOS ================= */
                                "\n" +
                                "    pay.id_pay,\n" +
                                "    CONCAT(pay.ser, IF(LENGTH(pay.ser) = 0, '', '-'), pay.num) AS folio_pago,\n" +
                                "    pay.dt_sched_n AS fecha_programada_pago,\n" +
                                "    pay.b_exec_man AS pago_manual,\n" +
                                "    st_pay.name AS estatus_solicitud_pago,\n" +
                                /* ================= PAGOS ================= */
                                "    NULL AS id_pay_p,\n" +
                                "    NULL AS folio_pago_p,\n" +
                                "    NULL AS fecha_ejecucion_pago_p,\n" +
                                "    NULL AS estatus_pago_p, " +
                                /* ================= CRP ================= */
                                "    NULL AS requiere_crp, " +
                                "    NULL AS num_crp, " +
                                "    NULL AS estatus_crp " +
                                "\n" +
                                "FROM trn_mat_req v\n" +
                                "\n" +
                                "INNER JOIN erp.usru_usr ui\n" +
                                "    ON v.fk_usr_ins = ui.id_usr\n" +
                                "\n" +
                                "INNER JOIN erp.usru_usr uu\n" +
                                "    ON v.fk_usr_upd = uu.id_usr\n" +
                                "\n" +
                                "LEFT JOIN (\n" +
                                "    SELECT DISTINCT\n" +
                                "        fid_mat_req,\n" +
                                "        fid_dps_year,\n" +
                                "        fid_dps_doc\n" +
                                "    FROM trn_dps_mat_req oc_vs_r\n" +
                                "    INNER JOIN trn_dps oc_r\n" +
                                "       ON oc_r.id_year = oc_vs_r.fid_dps_year\n" +
                                "       AND oc_r.id_doc = oc_vs_r.fid_dps_doc\n" +
                                "       AND oc_r.fid_ct_dps = 1\n" +
                                "       AND oc_r.fid_cl_dps = 2\n" +
                                "       AND oc_r.fid_tp_dps = 1\n" +
                                ") r\n" +
                                "    ON v.id_mat_req = r.fid_mat_req\n" +
                                "\n" +
                                "LEFT JOIN trn_dps oc\n" +
                                "    ON oc.id_year = r.fid_dps_year\n" +
                                "    AND oc.id_doc = r.fid_dps_doc\n" +
                                "    AND oc.fid_ct_dps = 1\n" +
                                "    AND oc.fid_cl_dps = 2\n" +
                                "    AND oc.fid_tp_dps = 1\n" +
                                "    AND oc.b_del = 0\n" +
                                "\n" +
                                "LEFT JOIN erp.bpsu_bp bp\n" +
                                "    ON oc.fid_bp_r = bp.id_bp\n" +
                                "\n" +
                                "LEFT JOIN (\n" +
                                "    SELECT DISTINCT\n" +
                                "        id_src_year,\n" +
                                "        id_src_doc,\n" +
                                "        id_des_year,\n" +
                                "        id_des_doc\n" +
                                "    FROM trn_dps_dps_supply\n" +
                                ") s\n" +
                                "    ON s.id_src_year = oc.id_year\n" +
                                "    AND s.id_src_doc = oc.id_doc\n" +
                                "\n" +
                                "LEFT JOIN trn_dps fac\n" +
                                "    ON fac.id_year = s.id_des_year\n" +
                                "    AND fac.id_doc = s.id_des_doc\n" +
                                "    AND fac.fid_ct_dps = 1\n" +
                                "    AND fac.fid_cl_dps = 3\n" +
                                "    AND fac.fid_tp_dps = 1\n" +
                                "    AND fac.b_del = 0\n" +
                                "\n" +
                                "LEFT JOIN (\n" +
                                "    SELECT DISTINCT\n" +
                                "        fk_dps_year_n,\n" +
                                "        fk_dps_doc_n,\n" +
                                "        ext_data_id\n" +
                                "    FROM trn_swap_data_prc\n" +
                                "    WHERE data_type = 'INV'\n" +
                                ") fac_pc\n" +
                                "    ON fac_pc.fk_dps_doc_n = fac.id_doc\n" +
                                "    AND fac_pc.fk_dps_year_n = fac.id_year" +
                                "\n" +
                                "LEFT JOIN (\n" +
                                "    SELECT DISTINCT\n" +
                                "        pe.fk_doc_year_n,\n" +
                                "        pe.fk_doc_doc_n,\n" +
                                "        p.id_pay,\n" +
                                "        p.ser,\n" +
                                "        p.num,\n" +
                                "        p.dt_sched_n,\n" +
                                "        p.b_exec_man,\n" +
                                "        p.fk_st_pay\n" +
                                "    FROM fin_pay_ety pe\n" +
                                "    INNER JOIN fin_pay p\n" +
                                "        ON p.id_pay = pe.id_pay\n" +
                                "        AND p.b_del = 0\n" +
                                "        AND p.pay_tp = 'R'\n" +
                                ") pay\n" +
                                "    ON pay.fk_doc_year_n = fac.id_year\n" +
                                "    AND pay.fk_doc_doc_n = fac.id_doc\n" +
                                "\n" +
                                "LEFT JOIN erp.fins_st_pay st_pay\n" +
                                "    ON pay.fk_st_pay = st_pay.id_st_pay\n" +
                                "\n" +
                                "WHERE\n" +
                                "    v.b_del = 0\n" +
                                "    AND\n" +
                                mywhere +
                                "    AND EXISTS (\n" +
                                "        SELECT 1\n" +
                                "        FROM trn_mat_req_st_log l\n" +
                                "        WHERE l.id_mat_req = v.id_mat_req\n" +
                                "        AND l.fk_st_mat_req = 4\n" +
                                "    )\n" +
                                "\n" +
                                "ORDER BY\n" +
                                "    v.dt,\n" +
                                "    v.num,\n" +
                                "    oc.id_year,\n" +
                                "    oc.id_doc,\n" +
                                "    fac.id_year,\n" +
                                "    fac.id_doc,\n" +
                                "    pay.id_pay;";

        try (Statement stmt = miClient.getSession().getStatement()) {
            // System.out.println("Ejecutando query");
            int registros = stmt.executeUpdate(insertSQL);
            // System.out.println("Insertados " + registros + " registros en tabla temporal");
        }
        
        List<String[]> idsOC = new ArrayList<>();
        
        try (Statement stmt = miClient.getSession().getStatement()) {
            
            String sqlSelectIds = "SELECT id_year_oc, id_doc_oc, id_pay, pago_manual FROM tmp_reporte_compras WHERE id_doc_oc IS NOT NULL";
            
            ResultSet rs = stmt.executeQuery(sqlSelectIds);
            
            while (rs.next()) {                
                String[] claveCompuesta = new String[2];
                claveCompuesta[0] = rs.getString("id_year_oc");
                claveCompuesta[1] = rs.getString("id_doc_oc");
                idsOC.add(claveCompuesta);
            }
        }
        
        try (Statement stmt = miClient.getSession().getStatement()) {
            // System.out.println("Consultando api transacciones...");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode config = mapper.readTree(SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG));

            String baseUrl = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_SRV, SSwapConsts.CFG_ATT_URL);
            String token = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_SRV, SSwapConsts.CFG_ATT_TOKEN);
            String apiKey = SAuthJsonUtils.getValueOfElementAsText(config, SSwapConsts.CFG_OBJ_TXN_SRV, SSwapConsts.CFG_ATT_API_KEY);

            String url = baseUrl + "/api/documents/get-info-purchasing-process/";
            String jsonBody = crearJson(idsOC);

            String responseBody = SExportUtils.requestSwapService("", url, SHttpConsts.METHOD_POST, jsonBody, token, apiKey, SSwapConsts.TIME_180_SEC);
            JsonNode responseJson = mapper.readTree(responseBody);
            JsonNode results = NullNode.getInstance();
            if (responseJson.has("results")) {
                results = responseJson.get("results");
            }
            
            // System.out.println("Datos obtenidos de transacciones, insertando en tabla...");
            for (JsonNode item : results) {

                JsonNode reference = item.get("reference");
                JsonNode document = item.get("document");
                JsonNode authorization = item.get("authorization");
                JsonNode payment = item.get("payment");

                if (reference != null && document != null && authorization != null && payment != null) {

                    int documentId = document.get("id").asInt();
                    String folioFac = null;
                    String dateFac = null;

                    JsonNode folioNode = document.get("folio");
                    if (folioNode != null && !folioNode.isNull()) {
                        folioFac = folioNode.asText().replace("'", "''");
                    }

                    JsonNode dateNode = document.get("date");
                    if (dateNode != null && !dateNode.isNull()) {
                        dateFac = dateNode.asText();
                    }

                    // Obtener OC desde external_id (formato: year_doc)
                    String externalId = reference.get("external_id").asText();
                    String[] parts = externalId.split("_");

                    int idYearOc = Integer.parseInt(parts[0]);
                    int idDocOc = Integer.parseInt(parts[1]);

                    // auth_status_fac
                    String authStatus = null;
                    JsonNode flowStatus = authorization.get("flow_status");
                    // user_in_turn_fac
                    String userInTurn = null;
                       
                    if (flowStatus != null && !flowStatus.isNull()) {
                        JsonNode last_turn_action = flowStatus.get("last_turn_action");
                        if (last_turn_action != null && !last_turn_action.isNull()) {
                            JsonNode actors_of_action = last_turn_action.get("actors_of_action");
                            authStatus = last_turn_action.get("flow_status_name").asText().replace("'", "''");
                            if (actors_of_action != null && !actors_of_action.isNull()) {
                                for (JsonNode actor : actors_of_action) {
                                    if (actor != null && !actor.isNull()) {
                                        userInTurn = actor.get("full_name").asText().replace("'", "''");
                                    }
                                    break;
                                }
                            }
                        }
                    }

                    // requiere_crp
                    boolean requiereCrp = payment.get("requires_receipt").asBoolean();
                    String requiereCrpStr = requiereCrp ? "1" : "0";

                    // num_crp y estatus_crp
                    String numCrp = null;
                    String statusCrp = null;

                    JsonNode receipts = payment.get("receipts");

                    if (receipts != null && receipts.isArray() && receipts.size() > 0) {

                        JsonNode firstReceipt = receipts.get(0);

                        JsonNode folio = firstReceipt.get("folio");
                        if (folio != null && !folio.isNull()) {
                            numCrp = folio.asText().replace("'", "''");
                        }

                        JsonNode authz = firstReceipt.get("authz");
                        if (authz != null && !authz.isNull()) {
                            statusCrp = authz.asText().replace("'", "''");
                            if (statusCrp != null) {
                                switch (statusCrp) {
                                    case "1":
                                        statusCrp = "PENDIENTE";
                                        break;
                                    case "2":
                                        statusCrp = "EN PROCESO";
                                        break;
                                    case "8":
                                        statusCrp = "RECHAZADO";
                                        break;
                                    case "9":
                                        statusCrp = "OK";
                                        break;
                                    default:
                                        statusCrp = " ";
                                    break;
                                }
                            }
                        }
                    }

                    // =============================
                    // 1. Verificar si factura existe
                    // =============================

                    String sqlCheckFactura = "SELECT COUNT(*) FROM tmp_reporte_compras WHERE id_fac_pc = " + documentId;

                    ResultSet rsFactura = stmt.executeQuery(sqlCheckFactura);
                    rsFactura.next();

                    int facturaExiste = rsFactura.getInt(1);

                    if (facturaExiste > 0) {
                        // UPDATE normal
                        String sql = buildUpdateSQL(documentId, authStatus, userInTurn, requiereCrpStr, numCrp, statusCrp);
                        stmt.executeUpdate(sql);

                        // System.out.println("Factura actualizada: " + documentId);

                    } else {

                        // ==========================================
                        // 2. Verificar si existe OC sin factura
                        // ==========================================

                        String sqlCheckOc =
                                "SELECT COUNT(*) FROM tmp_reporte_compras " +
                                "WHERE id_year_oc = " + idYearOc +
                                " AND id_doc_oc = " + idDocOc +
                                " AND id_fac_pc IS NULL";

                        ResultSet rsOc = stmt.executeQuery(sqlCheckOc);
                        rsOc.next();

                        int ocSinFactura = rsOc.getInt(1);

                        if (ocSinFactura > 0) {

                            // UPDATE del renglón existente

                            String sqlUpdateOc =
                                        "UPDATE tmp_reporte_compras SET " +
                                        "id_fac_pc = " + documentId + ", " +
                                        "folio_fac = " + (folioFac != null ? "'" + folioFac + "'" : "NULL") + ", " +
                                        "date_fac = " + (dateFac != null ? "'" + dateFac + "'" : "NULL") + ", " +
                                        "auth_status_fac = " + (authStatus != null ? "'" + authStatus + "'" : "NULL") + ", " +
                                        "user_in_turn_fac = " + (userInTurn != null ? "'" + userInTurn + "'" : "NULL") + ", " +
                                        "requiere_crp = '" + requiereCrpStr + "', " +
                                        "num_crp = " + (numCrp != null ? "'" + numCrp + "'" : "NULL") + ", " +
                                        "estatus_crp = " + (statusCrp != null ? "'" + statusCrp + "'" : "NULL") + " " +
                                        "WHERE id_year_oc = " + idYearOc +
                                        " AND id_doc_oc = " + idDocOc +
                                        " AND id_fac_pc IS NULL ";

                            stmt.executeUpdate(sqlUpdateOc);

                            // System.out.println("Factura agregada a OC existente: " + documentId);

                        } else {

                            // ======================================
                            // 3. Insertar nuevo renglón copiando RM y OC
                            // ======================================

                            // 1. crear tabla temporal auxiliar
                            String sqlCreateTmp =
                                    "CREATE TEMPORARY TABLE tmp_reporte_aux AS " +
                                    "SELECT * FROM tmp_reporte_compras " +
                                    "WHERE id_year_oc = " + idYearOc +
                                    " AND id_doc_oc = " + idDocOc;

                            stmt.executeUpdate(sqlCreateTmp);

                            // 2. insertar desde la tabla auxiliar
                            String sqlInsert =
                                    "INSERT INTO tmp_reporte_compras (" +
                                    "id_rm,f_id_1,f_code,f_name,folio,f_date,auth_status,code_rm,name_rm,b_del_rm," +
                                    "folio_rm,fecha_envio_rm,date_rm,fk_usr_ins_rm,fk_usr_upd_rm,ts_usr_ins_rm,ts_usr_upd_rm," +
                                    "f_usr_ins_rm,f_usr_upd_rm,auth_status_rm," +
                                    "id_year_oc,id_doc_oc,b_del_oc,folio_oc,date_oc,proveedor_oc," +
                                    "auth_status_oc,user_in_turn_oc,fecha_auth_oc, fid_func_oc," +
                                    "id_fac_pc,folio_fac,date_fac,auth_status_fac,user_in_turn_fac,requiere_crp,num_crp,estatus_crp" +
                                    ") " +
                                    "SELECT " +
                                    "id_rm,f_id_1,f_code,f_name,folio,f_date,auth_status,code_rm,name_rm,b_del_rm," +
                                    "folio_rm,fecha_envio_rm,date_rm,fk_usr_ins_rm,fk_usr_upd_rm,ts_usr_ins_rm,ts_usr_upd_rm," +
                                    "f_usr_ins_rm,f_usr_upd_rm,auth_status_rm," +
                                    "id_year_oc,id_doc_oc,b_del_oc,folio_oc,date_oc,proveedor_oc," +
                                    "auth_status_oc,user_in_turn_oc,fecha_auth_oc,fid_func_oc," +
                                    documentId + ", " +
                                    (folioFac != null ? "'" + folioFac + "'" : "NULL") + ", " +
                                    (dateFac != null ? "'" + dateFac + "'" : "NULL") + ", " +
                                    (authStatus != null ? "'" + authStatus + "'" : "NULL") + ", " +
                                    (userInTurn != null ? "'" + userInTurn + "'" : "NULL") + ", " +
                                    "'" + requiereCrpStr + "', " +
                                    (numCrp != null ? "'" + numCrp + "'" : "NULL") + ", " +
                                    (statusCrp != null ? "'" + statusCrp + "'" : "NULL") + " " +
                                    "FROM tmp_reporte_aux " +
                                    "LIMIT 1";

                            stmt.executeUpdate(sqlInsert);

                            // 3. eliminar tabla auxiliar
                            stmt.executeUpdate("DROP TEMPORARY TABLE tmp_reporte_aux");

                            // System.out.println("Nueva factura insertada para OC: " + documentId);
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
            miClient.showMsgBoxWarning(
                    "Lamentablemente no se pudo consultar la información del sistema " + SSwapConsts.PURCHASE_PORTAL + ".\n" +
                    "La información mostrada es la que tiene " + SSwapConsts.SIIE + " en este momento.\n" + 
                    "Los datos que pueden faltar son:\n" +
                    "* Facturas sin contabilizar.\n" +
                    "* Estatus de autorización de las facturas.\n" +
                    "* Comprobantes de recepción de pago."
            );
        }

        try (Statement stmt = miClient.getSession().getStatement()) {

            /*
            ==============================
            PAGOS AUTOMÁTICOS (pay request)
            ==============================
            */

            String sqlUpdatePayRequest =
                "UPDATE tmp_reporte_compras t " +
                "JOIN fin_pay_ety pety ON pety.fk_pay_req_n = t.id_pay " +
                "JOIN fin_pay pay ON pay.id_pay = pety.id_pay " +
                "JOIN erp.fins_st_pay stpay ON stpay.id_st_pay = pay.fk_st_pay " +
                "SET " +
                "t.id_pay_p = pay.id_pay, " +
                "t.folio_pago_p = CONCAT(pay.ser, IF(LENGTH(pay.ser)=0,'','-'), pay.num), " +
                "t.fecha_ejecucion_pago_p = DATE(pay.dt_exec_n), " +
                "t.estatus_pago_p = stpay.name " +
                "WHERE pay.b_del = 0 " +
                "AND t.pago_manual = 0";

            int rowsAuto = stmt.executeUpdate(sqlUpdatePayRequest);
            // System.out.println("Pagos automáticos actualizados: " + rowsAuto);

            /*
            ==============================
            PAGOS MANUALES
            ==============================
            */

            String sqlUpdatePayManual =
                "UPDATE tmp_reporte_compras t " +
                "JOIN fin_pay pay ON pay.id_pay = t.id_pay " +
                "JOIN erp.fins_st_pay stpay ON stpay.id_st_pay = pay.fk_st_pay " +
                "SET " +
                "t.id_pay_p = pay.id_pay, " +
                "t.folio_pago_p = CONCAT(pay.ser, IF(LENGTH(pay.ser)=0,'','-'), pay.num), " +
                "t.fecha_ejecucion_pago_p = DATE(pay.dt_exec_n), " +
                "t.estatus_pago_p = stpay.name " +
                "WHERE pay.b_del = 0 " +
                "AND t.pago_manual = 1";

            int rowsManual = stmt.executeUpdate(sqlUpdatePayManual);
            // System.out.println("Pagos manuales actualizados: " + rowsManual);
            
            /*
            ==============================
            ENTRADAS A ALMACÉN
            ==============================
            */

            // Inicialmente marcar todo como "No"
            String sqlEntradaNo =
                "UPDATE tmp_reporte_compras " +
                "SET entrada_almacen = '0'";

            stmt.executeUpdate(sqlEntradaNo);

            // Si existe registro en trn_stk marcar "Si"
            String sqlEntradaSi =
                "UPDATE tmp_reporte_compras t " +
                "INNER JOIN trn_stk stk ON stk.fid_dps_year_n = t.id_year_oc " +
                "AND stk.fid_dps_doc_n = t.id_doc_oc " +
                "AND stk.b_del = 0 " +
                "SET t.entrada_almacen = '1'";

            int rowsEntrada = stmt.executeUpdate(sqlEntradaSi);
            // System.out.println("Entradas a almacén detectadas: " + rowsEntrada);
            
            /*
            ==============================
            FECHA ENVÍO REQUISICIÓN (RM)
            ==============================
            */

            String sqlFechaEnvioRM =
                "UPDATE tmp_reporte_compras t " +
                "INNER JOIN (" +
                    "SELECT res_pk_n1_n, MIN(id_authorn_step) AS min_step " +
                    "FROM cfgu_authorn_step " +
                    "WHERE res_tab_name_n = 'trn_mat_req' " +
                    "AND fk_tp_authorn = 1 " +
                    "AND b_del = 0 " +
                    "GROUP BY res_pk_n1_n" +
                ") x ON x.res_pk_n1_n = t.id_rm " +
                "INNER JOIN cfgu_authorn_step st " +
                    "ON st.id_authorn_step = x.min_step " +
                "SET t.fecha_envio_rm = st.ts_usr_ins";

            int rowsRM = stmt.executeUpdate(sqlFechaEnvioRM);
            // System.out.println("Fechas de envío RM actualizadas: " + rowsRM);
        }
    }
    
    private static String buildUpdateSQL(int documentId, String authStatus, String userInTurn, 
                                        String requiereCrp, String numCrp, String statusCrp) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE tmp_reporte_compras SET ");
        
        List<String> sets = new ArrayList<>();
        
        // auth_status_fac
        if (authStatus != null) {
            sets.add("auth_status_fac = '" + authStatus + "'");
        } else {
            sets.add("auth_status_fac = NULL");
        }
        
        // user_in_turn_fac
        if (userInTurn != null) {
            sets.add("user_in_turn_fac = '" + userInTurn + "'");
        } else {
            sets.add("user_in_turn_fac = NULL");
        }
        
        // requiere_crp
        sets.add("requiere_crp = '" + requiereCrp + "'");
        
        // num_crp
        if (numCrp != null) {
            sets.add("num_crp = '" + numCrp + "'");
        } else {
            sets.add("num_crp = NULL");
        }
        
        //estatus crp
        if (statusCrp != null) {
            sets.add("estatus_crp = '" + statusCrp + "'");
        } else {
            sets.add("estatus_crp = NULL");
        }
        
        sql.append(String.join(", ", sets));
        sql.append(" WHERE id_fac_pc = ").append(documentId);
        
        return sql.toString();
    }

    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;
        
        moPaneSettings = new SGridPaneSettings(1);

        filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
        if (filter != null) {
            SGuiDate oFilter = (SGuiDate) filter;
            if (oFilter.getGuiType() == SGuiConsts.GUI_DATE_YEAR) {
                miClient.showMsgBoxInformation("No se puede seleccionar mas de un mes.");
                moFilterDatePeriod.initFilter(moFilterTimeInit);
                return;
            }

            if (jrbDateReq.isSelected()) {
                where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);
                where = where.replaceFirst("^\\(", "").replaceFirst("\\)\\s*$", "");
            }
            else if (jrbDateOc.isSelected()) {
                where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("oc.dt", (SGuiDate) filter);
                where = where.replaceFirst("^\\(", "").replaceFirst("\\)\\s*$", "");
            }
            moFilterTimeInit = new SGuiDate(oFilter.getGuiType(), oFilter.getTime());
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.CFGU_FUNC)).getValue();
        if (filter != null && !((String) filter).isEmpty()) {
            if (sFilterInit.equals(filter.toString())) {
                where += (where.isEmpty() ? "" : " AND ") + " ( oc.fid_func IN (" + filter + ") OR oc.fid_func IS NULL ) ";
            }
            else {
                where += (where.isEmpty() ? "" : " AND ") + " oc.fid_func IN (" + filter + ") ";
            }
        }
        
        try {
            crearYInsertarTablaTemporal(where);
        } catch (SQLException ex) {
            Logger.getLogger(SViewPurchasingProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        msSql = "SELECT * from tmp_reporte_compras";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();

        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "folio_rm", "Folio requisición"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "date_rm", "Fecha requisición"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "fecha_envio_rm", "Inicio autorización requisición"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "auth_status_rm", "Estatus requisición"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "folio_oc", "Folio pedido compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "date_oc", "Fecha pedido compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "proveedor_oc", "Proveedor pedido compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "user_in_turn_oc", "Usr. turno pedido compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "fecha_auth_oc", "Autorización pedido compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "auth_status_oc", "Estatus pedido compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "entrada_almacen", "Tiene entrada almacen pedido compras"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "folio_fac", "Folio factura"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "date_fac", "Fecha factura"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "user_in_turn_fac", "Usr. turno factura"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "auth_status_fac", "Estatus factura"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "fac_contabilizada", "Factura contabilizada"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "folio_pago", "Folio solicitud Pago"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "estatus_solicitud_pago", "Estatus solicitud pago"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "fecha_programada_pago", "Fecha programada pago"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "folio_pago_p", "Folio Pago"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "estatus_pago_p", "Estatus pago"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "fecha_ejecucion_pago_p", "Fecha operación pago"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "requiere_crp", "Requiere CRP"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "num_crp", "Folio CRP"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "estatus_crp", "Estatus CRP"));
        
        return columns;
    }

    @Override

    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("No supported yet"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JRadioButton && e.getStateChange() == ItemEvent.SELECTED) {
            JRadioButton radioButton = (JRadioButton) e.getSource();
            
            if (radioButton == jrbDateReq || radioButton == jrbDateOc) {
                refreshGridWithRefresh();
            }
        }
    }
}