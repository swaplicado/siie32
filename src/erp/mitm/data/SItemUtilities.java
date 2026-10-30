/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mitm.data;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.lib.form.SFormComponentItem;
import erp.mod.bps.db.SBpsUtils;
import erp.mtrn.data.STrnDpsUtilities;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import sa.lib.SLibUtils;

/**
 *
 * @author Isabel Servín, Edwin Carmona, Sergio Flores, Rodrigo Ayala
 */
public abstract class SItemUtilities {
    
    /**
     * Obtiene el código del ProdServ a partir del ID del catálogo de productos y servicios del SAT.
     * @param statement
     * @param claveProdServId
     * @return clave
     * @throws java.lang.Exception
     */
    public static String getClaveProdServ(final Statement statement, final int claveProdServId) throws Exception {
        String clave = "";
        
        String sql = "SELECT code "
                + "FROM erp.itms_cfd_prod_serv "
                + "WHERE id_cfd_prod_serv = " + claveProdServId + ";";

        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                clave = resultSet.getString("code");
            }
        }
        
        return clave;
    }
    
    /**
     * Obtiene el ID el empate preexistente de item vs. concepto de asociado de negocios.
     * @param client Cliente GUI.
     * @param conceptKey Clave del concepto que maneja el asociado de negocios, definido por él.
     * @param conceptProdServ Clave de Producto o Servico del concepto que maneja el asociado de negocios, definido por el SAT.
     * @param bizPartnerId ID del asociado de negocios requerido.
     * @return ID del empate preexistente. En caso de no existir, se devuelve cero. 
     */
    public static int getMatchingItemBizPartnerConceptId(final SClientInterface client, final String conceptKey, final String conceptProdServ, final int bizPartnerId){
        return getMatchingItemBizPartnerConceptId(client, conceptKey, conceptProdServ, bizPartnerId, 0);
    }
    
    /**
     * Obtiene el ID el empate preexistente de item vs. concepto de asociado de negocios.
     * @param client Cliente GUI.
     * @param conceptKey Clave del concepto que maneja el asociado de negocios, definido por él.
     * @param conceptProdServ Clave de Producto o Servico del concepto que maneja el asociado de negocios, definido por el SAT.
     * @param bizPartnerId ID del asociado de negocios requerido.
     * @param itemId ID del ítem requerido.
     * @return ID del empate preexistente. En caso de no existir, se devuelve cero. 
     */
    public static int getMatchingItemBizPartnerConceptId(final SClientInterface client, final String conceptKey, final String conceptProdServ, final int bizPartnerId, final int itemId) {
        int id = 0;
        
        try {
            String sql = "SELECT id_match "
                    + "FROM erp.itmu_match_item_cpt_bp "
                    + "WHERE cpt_key = '" + conceptKey + "' "
                    + "AND cpt_prod_serv = '" + conceptProdServ + "' "
                    + "AND fid_bp = " + bizPartnerId + " "
                    + "AND NOT b_del "
                    + (itemId != 0 ? "AND fid_item = " + itemId + " " : "");
            try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
                if (resultSet.next()){
                    id = resultSet.getInt(1);
                }
            }
        }
        catch (Exception e) {
            SLibUtils.printException(SBpsUtils.class.getName(), e);
        }
        
        return id;
    }
    
    /**
     * Obtiene las unidades de medida equivalentes con su factor de conversión para una unidad base según su tipo de unidad (itmu_tp_unit).
     * @param client Cliente GUI
     * @param idUnitBase ID de la unidad base
     * @return Vector con los items para llenar el ComboBox
     */
    public static java.util.Vector<SFormComponentItem> getUnitEquivalences(final erp.client.SClientInterface client, final int idUnitBase) {
        java.util.Vector<SFormComponentItem> items = new java.util.Vector<>();
        // Asiganmos el valor por default:
        SFormComponentItem defaultItem = new SFormComponentItem(new int[] { idUnitBase }, "Unidad origen");
        defaultItem.setComplement(1.0);
        items.add(defaultItem);

        try {
            String sql = "SELECT u.id_unit AS id_unit_equiv, u.unit, u.symbol, " +
                         "IF(u.unit_base_equiv = 0, 0, (1.00000000 / (u_base.unit_base_equiv / u.unit_base_equiv))) AS equiv " +
                         "FROM " + SDataConstants.TablesMap.get(SDataConstants.ITMU_UNIT) + " AS u_base " +
                         "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.ITMU_TP_UNIT) + " AS tu ON u_base.fid_tp_unit = tu.id_tp_unit " +
                         "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.ITMU_UNIT) + " AS u ON tu.id_tp_unit = u.fid_tp_unit " +
                         "WHERE u_base.id_unit = " + idUnitBase + " " +
                         "AND u.id_unit <> " + idUnitBase + " " +
                         "AND u_base.b_del = 0 AND u.b_del = 0 AND tu.b_del = 0 " +
                         "ORDER BY u.symbol;";
            
            java.sql.ResultSet rs = client.getSession().getStatement().executeQuery(sql);
            while (rs.next()) {
                SFormComponentItem item = new SFormComponentItem(new int[] { rs.getInt("id_unit_equiv") }, rs.getString("unit") + " (" + rs.getString("symbol") + ")");
                item.setComplement(rs.getDouble("equiv")); // Factor oculto
                items.add(item);
            }
        } catch (java.sql.SQLException e) {
            sa.lib.SLibUtils.printException(STrnDpsUtilities.class.getName(), e);
        }
        
        return items;
    }
    
    public static ArrayList<SDataUnitEquivalence> getUnitEquivalencesRows(final SClientInterface client, final int unitId) {
        try {
            String sql = "SELECT id_unit_equiv FROM " + SDataConstants.TablesMap.get(SDataConstants.ITMU_UNIT_EQUIV) + " "
                    + "WHERE NOT b_del AND id_unit = " + unitId + ";";
            try (ResultSet resultSet = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql)) {
                ArrayList<SDataUnitEquivalence> rows = new ArrayList<>();
                SDataUnitEquivalence oData;
                while (resultSet.next()){
                    oData = new SDataUnitEquivalence();
                    oData.read(new int[] { unitId, resultSet.getInt("id_unit_equiv") }, client.getSession().getStatement());
                    rows.add(oData);
                }
                
                return rows;
            }
        }
        catch (Exception e) {
            SLibUtils.printException(SBpsUtils.class.getName(), e);
        }
        
        return new ArrayList<>();
    }
    
    public static boolean getUnitEquivalenceExists(final SClientInterface client, final int unitId, final int unitEquivId) {
        try {
            String sql = "SELECT id_unit_equiv FROM " + SDataConstants.TablesMap.get(SDataConstants.ITMU_UNIT_EQUIV) + " "
                    + "WHERE (id_unit = " + unitId + " AND id_unit_equiv = " + unitEquivId + ") OR "
                        + "(id_unit_equiv = " + unitId + " AND id_unit = " + unitEquivId + ");";
            try (ResultSet resultSet = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql)) {
                return (resultSet.next());
            }
        }
        catch (Exception e) {
            SLibUtils.printException(SBpsUtils.class.getName(), e);
        }
        
        return false;
    }
}
