/*
 * Utilidades para la gestión de roles de usuario en el sistema.
 */
package erp.mod.cfg.swap.utils;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import erp.mod.cfg.swap.SSwapConsts;
import erp.musr.data.SDataUser;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import sa.lib.gui.SGuiSession;

/**
 * Clase de utilidades para obtener información de usuarios para SWAP Services.
 * 
 * @author Edwin Carmona, Sergio Flores
 */
public abstract class SUserUtils {
    
    /**
     * Obtiene los roles del usuario requerido.
     * 
     * @param session Sesión de usuario.
     * @param userId ID del usuario.
     * @return Lista de roles del usuario requerido. Valores: constantes SSwapConsts.ROL_...
     */
    public static ArrayList<Integer> getUserRoles(final SGuiSession session, int userId) {
        SDataUser user = new SDataUser();
        user.read(new int[] { userId }, session.getStatement()); // Carga los datos del usuario desde la base de datos
        
        ArrayList<Integer> roles = new ArrayList<>();
        
        // revisar si el usuario es administrador:
        if (user.isAdministrator()) {
            // si el usuario es administrador, igualmente es Administrador (y también los demás roles):
            roles.add(SSwapConsts.ROL_ADMINISTRATOR);
            roles.add(SSwapConsts.ROL_BUYER);
            roles.add(SSwapConsts.ROL_ACCOUNTANT);
            roles.add(SSwapConsts.ROL_PAYER);
        }
        else {
            // iterar sobre todas las empresas configuradas para SWAP Services:
            for (int companyId : (int[]) ((SClientInterface) session.getClient()).getSwapServicesSetting(SSwapConsts.CFG_NVP_COMPANIES)) {
                if (user.hasAccessToModule(SDataConstants.MOD_PUR, companyId)) {
                    // si el usuario tiene acceso al módulo de compras, es Comprador:
                    roles.add(SSwapConsts.ROL_BUYER);
                    break;
                }
            }

            if (user.hasRole(new int[] { SDataConstantsSys.ROL_FIN_ADMOR, SDataConstantsSys.ROL_FIN_ACCT, SDataConstantsSys.ROL_FIN_ACCT_AUX })) {
                // si el usuario tiene alguno de los roles del módulo contabilidad, es Contador:
                roles.add(SSwapConsts.ROL_ACCOUNTANT);
            }

            if (user.hasRole(SDataConstantsSys.ROL_FIN_ACCT_CASH)) {
                // si el usuario tiene el rol de contador cajero, es Pagador:
                roles.add(SSwapConsts.ROL_PAYER);
            }
        }
        
        return roles;
    }
    
    /**
     * Obtiene las empresas a las que tiene acceso el usuario requerido.
     * 
     * @param session Sesión de usuario.
     * @param userId ID del usuario.
     * @return Lista de empresas a las que tiene acceso el usuario requerido. Valores: ID de la empresa.
     * @throws java.sql.SQLException
     */
    public static ArrayList<Integer> getUserAccesibleCompanies(final SGuiSession session, int userId) throws SQLException {
        SDataUser user = new SDataUser();
        user.read(new int[] { userId }, session.getStatement()); // Carga los datos del usuario desde la base de datos
        
        ArrayList<Integer> accesibleCompanies = new ArrayList<>();
        
        // revisar si el usuario es administrador o si tiene acceso universal:
        if (user.isAdministrator() || user.getIsUniversal()) {
            int[] companies = (int[]) ((SClientInterface) session.getClient()).getSwapServicesSetting(SSwapConsts.CFG_NVP_COMPANIES);
            accesibleCompanies.addAll(Arrays.stream(companies).boxed().collect(Collectors.toList()));
        }
        else {
            String sql = "SELECT id_co "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.USRU_ACCESS_CO) + " "
                    + "WHERE id_usr = " + userId + " AND id_co IN (" + SExportUtils.getSwapCompaniesForSqlQuery(session) + ") "
                    + "ORDER BY id_co;";
            
            try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
                while (resultSet.next()) {
                    accesibleCompanies.add(resultSet.getInt("id_co"));
                }
            }
        }
        
        return accesibleCompanies;
    }
    
    /**
     * Obtiene las subáreas funcionales que tiene asignadas el usuario requerido.
     * 
     * @param session Sesión de usuario.
     * @param userId ID del usuario.
     * @return Lista de subáreas funcionales que tiene asignadas el usuario requerido. Valores (<code>int[]</code>): ID de la empresa, ID de la subárea funcional.
     * @throws java.sql.SQLException
     */
    public static ArrayList<int[]> getUserAsignedFunctionalSubAreas(final SGuiSession session, int userId) throws SQLException {
        ArrayList<int[]> asignedFunctionalSubAreas = new ArrayList<>();
        
        HashMap<Integer, String> databasesMap = SExportUtils.getSwapCompaniesDatabasesMap(session);

        for (Integer companyId : databasesMap.keySet()) {
            String database = databasesMap.get(companyId);

            String sql = "SELECT ufs.id_func_sub "
                    + "FROM " + database + "." + SModConsts.TablesMap.get(SModConsts.USR_USR_FUNC_SUB) + " AS ufs "
                    + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC_SUB) + " AS fs ON fs.id_func_sub = ufs.id_func_sub "
                    + "INNER JOIN " + database + "." + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS f ON f.id_func = fs.fk_func "
                    + "WHERE ufs.id_usr = " + userId + " AND NOT fs.b_del AND NOT f.b_del "
                    + "ORDER BY ufs.id_func_sub;";

            try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
                while (resultSet.next()) {
                    asignedFunctionalSubAreas.add(new int[] { companyId, resultSet.getInt("ufs.id_func_sub") });
                }
            }
        }
        
        return asignedFunctionalSubAreas;
    }
}
