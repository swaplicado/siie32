/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.api.db;

import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.db.SMySqlClass;
import erp.mod.trn.api.data.SWebDpsEty;
import erp.mod.trn.api.data.SWebDpsNote;
import erp.mod.trn.api.data.SWebDpsRow;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que gestiona consultas relacionadas con documentos (DPS) desde la base
 * de datos. Proporciona métodos para obtener documentos y filas procesadas
 * según filtros específicos.
 *
 * @author Edwin Carmona
 */
public class STrnDBCore {

    // Consulta base para la obtención de un DPS
    final static String BASE_QUERY = "SELECT  "
            + "    id_year, "
            + "    id_doc, "
            + "    dps.dt, "
            + "    dt_doc, "
            + "    IF(LENGTH(num_ser) > 0, "
            + "        CONCAT(num_ser, '-', dps.num), "
            + "        dps.num) AS dps_num, "
            + "    dps.num, "
            + "    num_ref, "
            + "    bp.bp, "
            + "    bp.fiscal_id, "
            + "    stot_prov_r, "
            + "    disc_doc_r, "
            + "    stot_r, "
            + "    tax_charged_r, "
            + "    tax_retained_r, "
            + "    dps.tot_r, "
            + "    tcur.cur_key, "
            + "    mr.num AS mr_num, "
            + "    mr.dt AS mr_dt, "
            + "    mrusr.usr AS mr_user, "
            + "    mrusr.id_usr AS mr_id_usr, "
            + "    comms_r, "
            + "    exc_rate, "
            + "    exc_rate_sys, "
            + "    stot_prov_cur_r, "
            + "    disc_doc_cur_r, "
            + "    stot_cur_r, "
            + "    tax_charged_cur_r, "
            + "    tax_retained_cur_r, "
            + "    tot_cur_r, "
            + "    comms_cur_r, "
            + "    fid_cur, "
            + "    dusr.usr AS dps_user, "
            + "    dusr.id_usr AS dps_user_id "
            + "FROM "
            + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS dps "
            + "        INNER JOIN "
            + "    " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON dps.fid_bp_r = bp.id_bp "
            + "        INNER JOIN "
            + "    " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS tcur ON dps.fid_cur = tcur.id_cur "
            + "        INNER JOIN "
            + "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS dusr ON dps.fid_usr_new = dusr.id_usr "
            + "        LEFT JOIN "
            + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " dps_mr ON dps.id_year = dps_mr.fid_dps_year "
            + "        AND dps.id_doc = dps_mr.fid_dps_doc "
            + "        LEFT JOIN "
            + "    " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS mr ON dps_mr.fid_mat_req = mr.id_mat_req "
            + "        LEFT JOIN "
            + "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS mrusr ON mr.fk_usr_ins = mrusr.id_usr ";

    /**
     * Obtiene documentos entre un rango de fechas y para un usuario específico.
     *
     * @param startDate Fecha inicial (YYYY-MM-DD).
     * @param endDate Fecha final (YYYY-MM-DD).
     * @param idUser ID del usuario para filtrar.
     * @return Lista de objetos {@code SWebDpsRow} con los datos obtenidos.
     */
    public static ArrayList<SWebDpsRow> getDocuments(String startDate, String endDate, int idUser) {
        try {
            SMySqlClass mdb = new SMySqlClass();
            String dbName = mdb.getMainDatabaseName();
            Connection conn = mdb.connect("", "", dbName, "", "");

            if (conn == null) {
                return null;
            }

            String query = BASE_QUERY
                    + "WHERE "
                    + "    NOT dps.b_del AND dps.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] + " "
                    + "        AND dps.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] + " "
                    + "        AND dps.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] + " "
                    + "        AND dps.dt_doc BETWEEN '" + startDate + "' AND '" + endDate + "' "
                    + "        AND num_ser <> 'S' "
                    + "        AND mr.id_mat_req IS NOT NULL "
                    + "GROUP BY id_year , id_doc "
                    + "ORDER BY dps.dt ASC;";

            Statement st = conn.createStatement();
            System.out.println(query);
            ResultSet res = st.executeQuery(query);

            ArrayList<SWebDpsRow> lDocuments = new ArrayList<>();

            while (res.next()) {
                SWebDpsRow oDoc = STrnDBCore.getDataFromRow(res);
                lDocuments.add(oDoc);
            }

            return lDocuments;
        } catch (SConfigException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ArrayList<>();
    }

    /**
     * Obtiene un documento por su clave primaria.
     *
     * @param idYear Año del documento.
     * @param idDoc ID del documento.
     * @return Objeto {@code SWebDpsRow} con los datos obtenidos.
     */
    public static SWebDpsRow getDocumentByPk(int idYear, int idDoc) {
        try {
            SMySqlClass mdb = new SMySqlClass();
            String dbName = mdb.getMainDatabaseName();
            Connection conn = mdb.connect("", "", dbName, "", "");

            if (conn == null) {
                return null;
            }

            String query = BASE_QUERY
                    + "WHERE "
                    + "    dps.id_year = " + idYear + " "
                    + "    AND dps.id_doc = " + idDoc + " "
                    + "GROUP BY id_year , id_doc;";

            Statement st = conn.createStatement();
            System.out.println(query);
            ResultSet res = st.executeQuery(query);

            if (res.next()) {
                return STrnDBCore.getDataFromRow(res);
            }
        } catch (SConfigException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Crea un objeto {@code SWebDpsRow} a partir de los datos de una fila de
     * resultado.
     *
     * @param res Objeto {@code ResultSet} que contiene los datos de la
     * consulta.
     * @return Objeto {@code SWebDpsRow} con los datos mapeados.
     */
    private static SWebDpsRow getDataFromRow(ResultSet res) {
        try {
            SWebDpsRow oDoc = new SWebDpsRow();

            oDoc.setIdYear(res.getInt("id_year"));
            oDoc.setIdDoc(res.getInt("id_doc"));
            oDoc.setDt(res.getString("dt"));
            oDoc.setDtDoc(res.getString("dt_doc"));
            oDoc.setDpsFolio(res.getString("dps_num"));
            oDoc.setDpsNumRef(res.getString("num_ref"));
            oDoc.setProvider(res.getString("bp"));
            oDoc.setProviderFiscalId(res.getString("fiscal_id"));
            oDoc.setSubTotal(res.getDouble("stot_r"));
//            oDoc.setDiscount(res.getDouble("disc_doc_r"));
            oDoc.setTaxCharged(res.getDouble("tax_charged_r"));
            oDoc.setTaxRetained(res.getDouble("tax_retained_r"));
            oDoc.setTotal(res.getDouble("tot_r"));
            oDoc.setCurrencyId(res.getInt("fid_cur"));
            oDoc.setCurrency(res.getString("cur_key"));
            oDoc.setExchangeRate(res.getDouble("exc_rate"));
            oDoc.setMatReqFolio(res.getString("mr_num"));
            oDoc.setMatReqDt(res.getString("mr_dt"));
            oDoc.setMatReqUser(res.getString("mr_user"));
            oDoc.setMatReqUserId(res.getInt("mr_id_usr"));
            // oDoc.setComms(res.getDouble("comms_r"));
            // oDoc.setExcRateSys(res.getDouble("exc_rate_sys"));
            oDoc.setSubTotalCur(res.getDouble("stot_cur_r"));
            // oDoc.setDiscountCur(res.getDouble("disc_doc_cur_r"));
            oDoc.setTotalCur(res.getDouble("tot_cur_r"));
            oDoc.setTaxChargedCur(res.getDouble("tax_charged_cur_r"));
            oDoc.setTaxRetainedCur(res.getDouble("tax_retained_cur_r"));
            oDoc.setDpsUser(res.getString("dps_user"));
            oDoc.setDpsUserId(res.getInt("dps_user_id"));

            return oDoc;
        } catch (SQLException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static ArrayList<SWebDpsEty> getWebDpsEties(int idYear, int idDoc) {
        try {
            SMySqlClass mdb = new SMySqlClass();
            String dbName = mdb.getMainDatabaseName();
            Connection conn = mdb.connect("", "", dbName, "", "");

            if (conn == null) {
                return null;
            }

            String query = "SELECT  "
                    + "    ety.*, "
                    + "    i.item_key, "
                    + "    i.item, "
                    + "    i.part_num, "
                    + "    u.unit, "
                    + "    u.symbol "
                    + "FROM "
                    + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS dps "
                    + "        INNER JOIN "
                    + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS ety ON dps.id_year = ety.id_year "
                    + "        AND dps.id_doc = ety.id_doc " + //
                    "        INNER JOIN " + //
                    "    " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON ety.fid_item = i.id_item "
                    + "        INNER JOIN " + //
                    "    " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON ety.fid_unit = u.id_unit "
                    + "WHERE " + //
                    "   NOT dps.b_del AND NOT ety.b_del " +
                    "   AND dps.id_year = " + idYear + " " +
                    "   AND dps.id_doc = " + idDoc + ";";

            Statement st = conn.createStatement();
            System.out.println(query);
            ResultSet res = st.executeQuery(query);
            ArrayList<SWebDpsEty> lEties = new ArrayList<>();
            while (res.next()) {
                SWebDpsEty oEty = new SWebDpsEty();
                oEty.setIdYear(res.getInt("id_year"));
                oEty.setIdDoc(res.getInt("id_doc"));
                oEty.setIdEty(res.getInt("id_ety"));
                oEty.setIdItem(res.getInt("fid_item"));
                oEty.setConceptKey(res.getString("concept_key"));
                oEty.setConcept(res.getString("concept"));
                oEty.setReference(res.getString("ref"));
                oEty.setPartNum(res.getString("part_num"));
                oEty.setIdUnit(res.getInt("fid_unit"));
                oEty.setUnitSymbol(res.getString("symbol"));
                oEty.setQuantity(res.getDouble("qty"));
                oEty.setPrice(res.getDouble("price_u"));
                oEty.setSubtotal(res.getDouble("stot_r"));
                oEty.setTaxCharged(res.getDouble("tax_charged_r"));
                oEty.setTaxRetained(res.getDouble("tax_retained_r"));
                oEty.setTotal(res.getDouble("tot_r"));

                lEties.add(oEty);
            }

            for (SWebDpsEty oEty : lEties) {
                oEty.setoMaterialRequest(STrnDBMaterialRequest.getMaterialRequestOfDpsEty(idYear, idDoc, oEty.getIdEty()));
            }

            return lEties;
        } catch (SQLException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SConfigException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ArrayList<>();
    }

    public static ArrayList<SWebDpsNote> getWebDpsNotes(int idYear, int idDoc) {
        try {
            SMySqlClass mdb = new SMySqlClass();
            String dbName = mdb.getMainDatabaseName();
            Connection conn = mdb.connect("", "", dbName, "", "");

            if (conn == null) {
                return null;
            }

            String query = "SELECT "
                    + "    nts.id_nts, "
                    + "    nts.nts "
                    + "FROM "
                    + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_NTS) + " AS nts "
                    + "WHERE "
                    + "    NOT nts.b_del "
                    + "    AND nts.id_year = " + idYear + " "
                    + "    AND nts.id_doc = " + idDoc + ";";

            Statement st = conn.createStatement();
            System.out.println(query);
            ResultSet res = st.executeQuery(query);
            ArrayList<SWebDpsNote> lNotes = new ArrayList<>();
            while (res.next()) {
                SWebDpsNote oNote = new SWebDpsNote(idYear, idDoc, res.getInt("id_nts"), res.getString("nts"));
                lNotes.add(oNote);
            }

            return lNotes;
        } catch (SQLException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SConfigException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(STrnDBCore.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ArrayList<>();
    }

}
