/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mitm.data;

import erp.client.SClientInterface;
import erp.mod.bps.db.SBpsUtils;
import java.sql.ResultSet;
import java.sql.Statement;
import sa.lib.SLibUtils;

/**
 *
 * @author Isabel Servín
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
     * Obtiene el código de la unidad a travez del ID del catálogo de unidades del SAT.
     * @param statement
     * @param claveUnidadId
     * @return 
     * @throws java.lang.Exception
     */
    public static String getClaveUnidad(final Statement statement, final int claveUnidadId) throws Exception {
        String clave = "";
        
        String sql = "SELECT code "
                + "FROM erp.itms_cfd_unit "
                + "WHERE id_cfd_unit = " + claveUnidadId + ";";

        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                clave = resultSet.getString("code");
            }
        }
        
        return clave;
    }
    
    /**
     * Obtiene el ID el empate preexistente.
     * @param client
     * @param conceptKey
     * @param bizPartnerId
     * @return ID del empate preexistente. En caso de no existir, se devuelve cero. 
     */
    public static int getMatchItemBizPartnerId(final SClientInterface client, final String conceptKey, final int bizPartnerId){
        int id = 0;
        
        try {
            String sql = "SELECT id_match FROM erp.itmu_match_item_cpt_bp "
                    + "WHERE cpt_key = '" + conceptKey + "' "
                    + "AND fid_bp = " + bizPartnerId + " ";
            try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
                if(resultSet.next()){
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
     * Obtiene el ID el empate preexistente.
     * @param client
     * @param conceptKey
     * @param bizPartnerId
     * @param itemId
     * @param taxRegId
     * @param unitId
     * @return ID del empate preexistente. En caso de no existir, se devuelve cero. 
     */
    public static int getMatchItemBizPartnerId(final SClientInterface client, final String conceptKey, final int bizPartnerId, final int itemId, final int unitId, final int taxRegId) {
        int id = 0;
        
        try {
            String sql = "SELECT id_match FROM erp.itmu_match_item_cpt_bp "
                    + "WHERE cpt_key = '" + conceptKey + "' "
                    + "AND fid_bp = " + bizPartnerId + " "
                    + "AND fid_item = " + itemId + " "
                    + "AND fid_unit = " + unitId + " "
                    + "AND fid_tax_reg = " + taxRegId + " ";
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
}
