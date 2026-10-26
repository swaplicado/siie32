/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.utils;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.fin.utils.SPaymentUtils;
import erp.swap.SSwapConsts;
import erp.swap.SSwapHttpException;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Edwin Carmona
 */
public class SSwapHttpUtils {
    
    /**
     * Update status of authorizable resource.
     *
     * @param statement
     * @param companyDbName
     * @param resourceType
     * @param resourceId
     * @param authStatusId
     * @param userId
     * @param notes
     * @param newAmount
     * @param newDate
     * @return
     */
    public static SResourceResponse updateResourceStatus(final Statement statement, final String companyDbName, final int resourceType, final String resourceId, final int authStatusId,
            final int userId, final String notes, final double newAmount, final String newDate) {
        SResourceResponse oResponse;

        try {
            oResponse = new SResourceResponse();
            String sTable = "";
            String sWhere = "";
            String sUpdate = "";
            String sSecondQuery = "";

            switch (resourceType) {
                case SSwapConsts.RESOURCE_TYPE_PUR_PAYMENT:
                    sTable = SModConsts.TablesMap.get(SModConsts.FIN_PAY);

                    switch (authStatusId) {
                        case SSwapConsts.AUTHZ_STATUS_OK:
                            sUpdate = "fk_st_pay = " + SModSysConsts.FINS_ST_PAY_SCHED_P + ", "
                                    + "fk_usr_sched = " + userId + ", "
                                    + "ts_usr_sched = NOW(), ";
                            if (newAmount > 0) {
                                // en partida: des_pay_app_ety_cur
                                sSecondQuery = "UPDATE " + companyDbName + "." + SModConsts.TablesMap.get(SModConsts.FIN_PAY_ETY) + " SET "
                                        + "des_pay_app_ety_cur = " + newAmount + " "
                                        + "WHERE (id_pay = " + resourceId + ") and (id_ety = 1);";
                            }
                            if (newDate != null && !newDate.isEmpty()) {
                                sUpdate += "dt_sched_n = '" + newDate + "', ";
                            }
                            else {
                                sUpdate += "dt_sched_n = dt_req, ";
                            }
                            break;

                        case SSwapConsts.AUTHZ_STATUS_REJECTED:
                            sUpdate = "fk_st_pay = " + SModSysConsts.FINS_ST_PAY_REJC_P + ", "
                                    + "dt_sched_n = NULL, "
                                    + "fk_usr_sched = " + SUtilConsts.USR_NA_ID + ", "
                                    + "ts_usr_sched = NOW(), "; // XXX se puede actualizar este TS o no en el rechazo, por lo pronto se deja
                            break;

                        default:
                            oResponse.status_code = HttpURLConnection.HTTP_BAD_REQUEST;
                            oResponse.message = "El tipo de estatus de autorización es desconocido (" + authStatusId + ").";
                            oResponse.error = "Tipo de estatus de autorización desconocido.";

                            return oResponse;
                    }

                    sUpdate += "nts_auth_flow = '" + SLibUtils.textToSql(notes) + "', "
                            + "fk_usr_upd = " + userId + ", "
                            + "ts_usr_upd = NOW() ";

                    sWhere = "WHERE id_pay = " + resourceId;
                    break;

                default:
                    oResponse.status_code = HttpURLConnection.HTTP_BAD_REQUEST;
                    oResponse.message = "No se encontró tipo de recurso.";
                    oResponse.error = "No se encontró tipo de recurso.";

                    return oResponse;
            }

            String sql = "UPDATE " + companyDbName + "." + sTable + " SET " + sUpdate + sWhere + ";";
            Logger.getLogger(SExportDataUtils.class.getName()).log(Level.INFO, "ACTUALIZAR PAGO, company: {0}. QUERY: {1} ", new Object[]{companyDbName, sql});

            // Iniciar transacción
            Connection conn = statement.getConnection();
            boolean autoCommit = conn.getAutoCommit();
            try {
                conn.setAutoCommit(false);

                int res = statement.executeUpdate(sql);
                if (sSecondQuery != null && !sSecondQuery.isEmpty()) {
                    Logger.getLogger(SExportDataUtils.class.getName()).log(Level.INFO, "ACTUALIZAR PARTIDA PAGO, company: {0}. QUERY{1}: ", new Object[]{companyDbName, sSecondQuery});
                    res = statement.getConnection().createStatement().executeUpdate(sSecondQuery);
                }
                if (res != 1) {
                    conn.rollback();
                    oResponse.status_code = HttpURLConnection.HTTP_INTERNAL_ERROR;
                    oResponse.message = "No se realizó ninguna actualización.";
                    oResponse.error = "No se realizó ninguna actualización.";
                }
                else {
                    conn.commit();
                    oResponse.status_code = HttpURLConnection.HTTP_OK;
                    oResponse.message = "OK";
                }
            }
            catch (SQLException ex) {
                Logger.getLogger(SExportDataUtils.class.getName()).log(Level.SEVERE, null, ex);
                conn.rollback();
                throw ex;
            }
            finally {
                conn.setAutoCommit(autoCommit);
            }
        }
        catch (SQLException ex) {
            oResponse = new SResourceResponse();
            oResponse.status_code = HttpURLConnection.HTTP_INTERNAL_ERROR;
            oResponse.message = "Error al actualizar el estatus del recurso.";
            oResponse.error = ex.getMessage();
            Logger.getLogger(SExportDataUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return oResponse;
    }

    public static SResourceResponse updateResource(final Statement statement, final String companyDbName, final int resourceType,
            final String resourceId, final int userId, final HashMap<String, Object> lAttributes) {
        SResourceResponse oResponse;

        try {
            oResponse = new SResourceResponse();
            String sTable = "";
            String sWhere = "";
            String sUpdate = "";
            String sSecondQuery = "";
            String sResourceName = "";

            switch (resourceType) {
                case SSwapConsts.RESOURCE_TYPE_PUR_PAYMENT:
                    sTable = SModConsts.TablesMap.get(SModConsts.FIN_PAY);
                    sResourceName = "PAGO";
                    String sAttribute = "";

                    for (HashMap.Entry<String, Object> attr : lAttributes.entrySet()) {
                        sAttribute = attr.getKey();
                        switch (sAttribute) {
                            case "dt_sched_n":
                                int paymentStatusId;
                                try {
                                    String statusName = "";
                                    paymentStatusId = SPaymentUtils.getPaymentStatus(statement.getConnection(), Integer.parseInt(resourceId));
                                    if (paymentStatusId != SModSysConsts.FINS_ST_PAY_SCHED) {
                                        switch (paymentStatusId) {
                                            case 0:
                                                statusName = "DESCONOCIDO";
                                                break;
                                                
                                            case SModSysConsts.FINS_ST_PAY_NEW:
                                                statusName = "NUEVO";
                                                break;
                                                
                                            case SModSysConsts.FINS_ST_PAY_IN_AUTH:
                                                statusName = "EN AUTORIZACIÓN";
                                                break;
                                                
                                            case SModSysConsts.FINS_ST_PAY_REJC_P:
                                                statusName = "RECHAZADO";
                                                break;

                                            case SModSysConsts.FINS_ST_PAY_EXEC:
                                                statusName = "OPERADO";
                                                break;

                                            case SModSysConsts.FINS_ST_PAY_SUBR:
                                                statusName = "POR OPERARSE";
                                                break;

                                            case SModSysConsts.FINS_ST_PAY_RCPT:
                                                statusName = "COMPROBADO";
                                                break;
                                        
                                            default:
                                                statusName = "DESCONOCIDO";
                                                break;
                                        }
                                        
                                        throw new SSwapHttpException("El pago tiene estatus " + statusName + ", no puede actualizarse");
                                    }
                                }
                                catch (SQLException ex) {
                                    throw new SQLException("Ocurrió un error al leer el estatus del pago. " + ex.getMessage());
                                }
                                catch (SSwapHttpException ex) {
                                    throw ex;
                                }
                                sUpdate += "dt_sched_n = '" + attr.getValue().toString() + "', "
                                        + "fk_usr_sched = " + userId + ", "
                                        + "ts_usr_sched = NOW(), "
                                        + "fk_st_pay = " + SModSysConsts.FINS_ST_PAY_SCHED_P + ", ";
                                break;

                            default:
                                break;
                        }
                    }

                    sUpdate += "fk_usr_upd = " + userId + ", "
                            + "ts_usr_upd = NOW() ";

                    sWhere = "WHERE id_pay = " + resourceId;
                    break;

                case SSwapConsts.RESOURCE_TYPE_PUR_ORDER:
                    break;
            }
            String sql = "UPDATE " + companyDbName + "." + sTable + " SET " + sUpdate + sWhere + ";";
            Logger.getLogger(SExportDataUtils.class.getName()).log(Level.INFO, "ACTUALIZAR " + sResourceName + 
                    ", company: {0}. QUERY: {1} ", new Object[]{companyDbName, sql});

            // Iniciar transacción
            Connection conn = statement.getConnection();
            boolean autoCommit = conn.getAutoCommit();
            try {
                conn.setAutoCommit(false);

                int res = statement.executeUpdate(sql);
                if (!sSecondQuery.isEmpty()) {
                    Logger.getLogger(SExportDataUtils.class.getName()).log(Level.INFO, "Actualización complementaria, company: {0}. QUERY{1}: ", 
                        new Object[]{companyDbName, sSecondQuery});
                    res = statement.getConnection().createStatement().executeUpdate(sSecondQuery);
                }
                if (res != 1) {
                    conn.rollback();
                    oResponse.status_code = HttpURLConnection.HTTP_INTERNAL_ERROR;
                    oResponse.message = "No se realizó ninguna actualización.";
                    oResponse.error = "No se realizó ninguna actualización.";
                }
                else {
                    conn.commit();
                    oResponse.status_code = HttpURLConnection.HTTP_OK;
                    oResponse.message = "OK";
                }
            }
            catch (SQLException ex) {
                Logger.getLogger(SExportDataUtils.class.getName()).log(Level.SEVERE, null, ex);
                conn.rollback();
                throw ex;
            }
            finally {
                conn.setAutoCommit(autoCommit);
            }
        }
        catch (Exception ex) {
            oResponse = new SResourceResponse();
            oResponse.status_code = HttpURLConnection.HTTP_INTERNAL_ERROR;
            oResponse.message = "Error al actualizar el estatus del recurso.";
            oResponse.error = ex.getMessage();
            Logger.getLogger(SExportDataUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return oResponse;
    }
}
