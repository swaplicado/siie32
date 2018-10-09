/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mmfg.data.explosion;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.mmfg.data.SDataBom;
import erp.mmfg.data.SDataExplotionMaterialsEntry;
import erp.mmfg.form.SDialogExplotionMaterialsRawMaterial;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Carmona
 */
public class SExplosionMaterialsProcess {
    
    public final int ID_BOOM = 0;
    public final int REF_BOOM = 1;
    public final int ROOT_BOOM = 2;
    
    /**
     * Principal method
     * 
     * @param miClient
     * @param fileName file to read
     * @param iBranch id of branch to stock
     * @param oDate date of evaluation
     * 
     * @return String with errors ocurred, if the string is empty there were no errors
     */
    public String explodeFile(final erp.client.SClientInterface miClient, String fileName, final int iBranch, Date oDate) {
        String sResult = "";
        
        try {
            ArrayList<SInputData> lFile = SCsvFileManager.readFile(fileName);
            HashMap<Integer, SDataExplotionMaterialsEntry> lIngredients = new HashMap();

            for (SInputData oData : lFile) {
                SDataBom oBom = this.getBomByItemKey(miClient, oData.getMsItemKey());
                if (oBom != null) {
                    lIngredients = this.explode(miClient, oBom, oData.getMnQuantity(), lIngredients, true);
                }
            }
            
            Vector<Object> vStkParams = new Vector<Object>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(oDate);
            
            for (Map.Entry<Integer, SDataExplotionMaterialsEntry> entry : lIngredients.entrySet()) {
                SDataExplotionMaterialsEntry ingredient = entry.getValue();
                
                vStkParams.removeAllElements();
                vStkParams.add(SLibTimeUtilities.digestYear(oDate)[0]); // Year
                vStkParams.add(ingredient.getPkItemId()); // Item
                vStkParams.add(ingredient.getPkUnitId()); // Unid
                vStkParams.add(null); // Lot
                vStkParams.add(iBranch); // Company branch
                vStkParams.add(null); // Warehouse
                vStkParams.add(null); // maintenance user
                vStkParams.add("'" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(oDate) + "'"); // Date
                vStkParams = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_STK_GET, vStkParams, SLibConstants.EXEC_MODE_SILENT);

                if (vStkParams.size() > 0) {
                    ingredient.setAvailable((Double) vStkParams.get(0));
                }
                
                 // Get purchase order:
                try {
                    ingredient.setBackorder(SDataUtilities.obtainBackorderItem(miClient, SDataConstantsSys.TRNS_CT_DPS_PUR, iBranch, ingredient.getPkItemId(), oDate));
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                    sResult += "Error al consultar el backorder";
                    break;
                }
            }
            
            sResult += this.writeExplosion(lIngredients);
            
        }
        catch (SQLException ex) {
            Logger.getLogger(SDialogExplotionMaterialsRawMaterial.class.getName()).log(Level.SEVERE, null, ex);
            sResult += "Error al consultar la explosión";
        }
        catch (Exception ex) {
            Logger.getLogger(SDialogExplotionMaterialsRawMaterial.class.getName()).log(Level.SEVERE, null, ex);
            sResult += "Error al explosionar";
        }
        
        return sResult;
    }
    
    /**
     * Add the ingredients of bom recevied to lExplosionList
     * 
     * @param client
     * @param oBom
     * @param dQuantity to produce of item
     * @param lExplosionList list of ingredients
     * @param bGroup group by item (the function to do not group is not ready)
     * 
     * @return HashMap of ingredients (Integer = id of item)
     * @throws Exception 
     */
    private HashMap<Integer, SDataExplotionMaterialsEntry> explode(final erp.client.SClientInterface client, final SDataBom oBom, final double dQuantity, HashMap<Integer, SDataExplotionMaterialsEntry> lExplosionList, final boolean bGroup) throws Exception {
        for (SDataBom oIngredient : oBom.getDbmsLevel()) {
            Object[] oAux = null;
            
            oAux = SDataUtilities.isBillOfMaterials(client, (Integer) oIngredient.getFkItemId());
            if ((Integer) oAux[this.ID_BOOM] > 0) {
                SDataBom moBom = (SDataBom) SDataUtilities.readRegistry(client, SDataConstants.MFG_BOM, new int[] { ((Integer) oAux[this.ID_BOOM]) }, SLibConstants.EXEC_MODE_VERBOSE);
                lExplosionList = this.explode(client, moBom, (dQuantity * oIngredient.getQuantity()), lExplosionList, bGroup);
            }
            else {
                SDataExplotionMaterialsEntry oEntry = null;
            
                if (bGroup && lExplosionList.containsKey(oIngredient.getFkItemId())) {
                    oEntry = lExplosionList.get(oIngredient.getFkItemId());
                    double dReq = oIngredient.getQuantity() * dQuantity;
                    double dReqPrevious = oEntry.getGrossReq();
                    oEntry.setGrossReq(dReqPrevious + dReq);
                }
                else {
                    oEntry = new SDataExplotionMaterialsEntry();

                    oEntry.setPkItemId(oIngredient.getFkItemId());
                    oEntry.setPkUnitId(oIngredient.getFkUnitId());
                    oEntry.setGrossReq(oIngredient.getQuantity() * dQuantity);
                    oEntry.setDbmsItem(oIngredient.getDbmsItem().replace(',', ' '));
                    oEntry.setDbmsItemUnitSymbol(oIngredient.getDbmsItemUnitSymbol());
                    oEntry.setDbmsItemKey(oIngredient.getDbmsItemKeyRm());

                    lExplosionList.put(oIngredient.getFkItemId(), oEntry);
                }
            }
        }
        
        return lExplosionList;
    }
    
    /**
     * Obtain the SDataBom object based on item key
     * 
     * @param client
     * @param sKey key of item
     * @return SDataBom Object
     * @throws SQLException 
     */
    private SDataBom getBomByItemKey(final erp.client.SClientInterface client, String sKey) throws SQLException {
        String sql = "";
        ResultSet resultSet = null;
        SDataBom moBom = null;
        
        sql = "SELECT id_bom "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.MFG_BOM) + " AS b "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON b.fid_item = i.id_item "
                + "WHERE b.root = i.id_item AND b.lev = 0 AND i.item_key = '" + sKey + "' "
                + "AND b.b_del = 0 ORDER BY id_bom DESC LIMIT 1;";
        
        resultSet = client.getSession().getStatement().executeQuery(sql);
        
        if (resultSet.next() && resultSet.getInt(1) > 0) {
            int iBom = resultSet.getInt("id_bom");
            
            if (iBom > 0) {
                moBom = (SDataBom) SDataUtilities.readRegistry(client, SDataConstants.MFG_BOM, new int[] { iBom }, SLibConstants.EXEC_MODE_VERBOSE);
            }
        }
        
        return moBom;
    }
    
    /**
     * Write the list of exploded ingredients to csv file
     * 
     * @param lExplosionList
     * @return String with errors ocurred, if the string is empty there were no errors
     */
    private String writeExplosion(HashMap<Integer, SDataExplotionMaterialsEntry> lExplosionList) {
      String sResult = SCsvFileManager.writeCsvFile(lExplosionList);
      
      return sResult;
    }
    
}
