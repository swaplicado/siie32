/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.utils;

import erp.mod.SModConsts;
import erp.mod.cfg.db.SDbAuthorizationPath;
import erp.mod.trn.db.SDbMaterialRequest;
import erp.mod.trn.db.SDbMaterialRequestCostCenter;
import erp.mod.trn.db.SDbMaterialRequestEntry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public abstract class SMatRequestAuthorizationUtils {
    
    public static final String CASE_CONSUME_ENTITY = "ConsumeEntity";
    public static final String CASE_MAT_REQUEST_USER = "MatReqUser";
    public static final String CASE_CREATOR_USER_POS = "CreatorUserPosition";
    
    /**
     * Retorna un array list de los elementos contenidos en un string con el formato: [elem1, elem2, elem3]
     * 
     * @param input
     * @return 
     */
    public static ArrayList<String> stringToIntArray(String input) {
        // Eliminar los corchetes al principio y al final del string
        input = input.substring(1, input.length() - 1);
        
        // Dividir el string en elementos separados por comas
        String[] elements = input.split(",");
        
        // Crear un arreglo de enteros y convertir los elementos de string a int
        ArrayList<String> aArray = new ArrayList<>();
        for (String sElement : elements) {
            aArray.add(sElement.trim());
        }
        
        return aArray;
    }
    
    /**
     * Obtiene las entidades de consumo relacionadas con la requisición de materiales.
     * Contempla las entidades tanto a nivel encabezado como de las partidas
     * 
     * @param oMatReq
     * 
     * @return Lista de ids de las entidades de consumo
     */
    public static ArrayList<Integer> getConsumeEntitys(SDbMaterialRequest oMatReq) {
        ArrayList<Integer> consumeEntitys = new ArrayList<>();
        for (SDbMaterialRequestCostCenter element : oMatReq.getChildCostCenters()) {
            consumeEntitys.add(element.getPkEntMatConsumptionEntityId());
        }
        for (SDbMaterialRequestEntry oEty : oMatReq.getChildEntries()) {
            if (oEty.getFkEntMatConsumptionEntityId_n() > 0) {
                consumeEntitys.add(oEty.getFkEntMatConsumptionEntityId_n());
            }
        }
        
        return consumeEntitys;
    }
    
    /**
     * Determina si la configuración recibida aplica para la requisición de materiales actual.
     * 
     * @param session
     * @param idMatReq
     * @param oCfg 
     * @return 
     */
    public static String applyCfg(SGuiSession session, final int idMatReq, SDbAuthorizationPath oCfg) {
        ArrayList<SCondition> lConditions = oCfg.getAuthorizationConfigObject().getConditions();
        if (lConditions.isEmpty()) {
            return "";
        }
        
        SDbMaterialRequest oMatReq = new SDbMaterialRequest();
        try {
            oMatReq.read(session, new int[] { idMatReq });
        }
        catch (Exception ex) {
            Logger.getLogger(SMatRequestAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
            return "Ocurrión un error al leer la requisición" + ex.getMessage();
        }
        
        if (oMatReq.getQueryResultId() != SDbConsts.READ_OK) {
            return "Ocurrión un error al leer la requisición";
        }
          
        for (SCondition oCondition : lConditions) {
            switch (oCondition.getKeyName()) {
                // Entidad de consumo igual a, o diferente a
                case CASE_CONSUME_ENTITY:
                    ArrayList<Integer> lEntities = SMatRequestAuthorizationUtils.getConsumeEntitys(oMatReq);
                    int consumeEntity = Integer.parseInt(oCondition.getStrValue());
                    boolean apply = false;
                    switch (oCondition.getOperator()) {
                        case "=":
                            for (Integer idEntity : lEntities) {
                                if (idEntity == consumeEntity) {
                                    apply = true;
                                    break;
                                }
                            }
                            break;
                        case "!=":
                            for (Integer iEntity : lEntities) {
                                if (iEntity != consumeEntity) {
                                    apply = true;
                                    break;
                                }
                            }
                            break;
                    }
                    
                    if (! apply) {
                        return "-1";
                    }
                    
                    break;
                    
                // Usuario de la requisición igual a, o diferente a
                case CASE_MAT_REQUEST_USER:
                    int userReqMat = Integer.parseInt(oCondition.getStrValue());
                    boolean applyUsr = false;
                    switch (oCondition.getOperator()) {
                        case "=":
                            if (oMatReq.getFkUserRequesterId() == userReqMat) {
                                applyUsr = true;
                            }
                            break;
                        case "!=":
                            if (oMatReq.getFkUserRequesterId() != userReqMat) {
                                applyUsr = true;
                            }
                            break;
                    }
                    
                    if (! applyUsr) {
                        return "-1";
                    }
                    
                    break;
                    
                // Puesto del usuario en sesión, que ESTÉ EN o NO ESTÉ EN 
                case CASE_CREATOR_USER_POS:
                    // El valor del objeto string debe tener el formato [elem1, elem2, elem3]
                    String elements = oCondition.getStrValue();
                    ArrayList<String> lPositions = SMatRequestAuthorizationUtils.stringToIntArray(elements);
                    boolean applyJob = false;
                    int iPosition;
                    try {
                        iPosition = SAuthorizationUtils.getPositionOfUser(session.getStatement().getConnection(), oMatReq.getFkUserRequesterId());
                    }
                    catch (SQLException ex) {
                        Logger.getLogger(SMatRequestAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
                        return "Ocurrió un error al leer el puesto de trabajo del usuario en sesión";
                    }
                    switch (oCondition.getOperator()) {
                        case "IN":
                            applyJob = lPositions.contains(iPosition + "");
                            break;
                        case "NOT IN":
                            applyJob = ! lPositions.contains(iPosition + "");
                            break;
                    }
                    
                    if (! applyJob) {
                        return "-1";
                    }
                    break;
                    
                default:
                    return "-1";
            }
        }
        
        return "";
    }
    
    /**
     * Obtiene el cuerpo del correo electrónico si el dps tiene requisiciones de materiales asociadas.
     *
     * @param oStatement
     * @param pkDps
     * 
     * @return String con el extracto del cuerpo para el correo electrónico
     */
    public static String getMaterailRequestBodyOfDps(java.sql.Statement oStatement, int[] pkDps) {
        String sBody = "";
        String sQuery = "SELECT DISTINCT  " +
                        "    u.usr, mr.id_mat_req, tp_req, num, dt " +
                        "FROM " +
                        "    " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_MAT_REQ) + " AS dmr " +
                        "        INNER JOIN " +
                        "    " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS mr ON dmr.fid_mat_req = mr.id_mat_req " +
                        "        INNER JOIN " +
                        "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS u ON mr.fk_usr_req = u.id_usr " +
                        "WHERE " +
                        "    dmr.fid_dps_year = " + pkDps[0] + " " +
                        "        AND dmr.fid_dps_doc = " + pkDps[1] + ";";

        ResultSet oResultSet = null;
        try {
            oResultSet = oStatement.executeQuery(sQuery);
            while (oResultSet.next()) {
                sBody += "<p>" + SLibUtils.textToHtml("Requisición de materiales") + ": <b>" + String.format("%0" + 6 + "d", oResultSet.getInt("num"))+ "</b>" +
                        " del " + SLibUtils.textToHtml(SLibUtils.DateFormatDate.format(oResultSet.getDate("dt"))) + 
                        " (<b>" + SLibUtils.textToHtml(oResultSet.getString("usr")) + "</b>)</p>";
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SMatRequestAuthorizationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sBody;
    }
}
