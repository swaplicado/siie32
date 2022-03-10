/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.server;

import erp.lib.SLibConstants;
import erp.mcfg.data.SDataCompany;
import erp.mcfg.data.SDataParamsCompany;
import erp.mcfg.data.SDataSysEntityCategory;
import erp.musr.data.SDataUser;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Sergio Flores
 */
public class SServiceSessions extends UnicastRemoteObject {

    private SServer moServer;
    private int mnCurrentSessionId;
    private Vector<SSessionServer> mvServerSessions;
    private Vector<SDataSysEntityCategory> mvEntityCategories;
    private Statement moStatement;

    private final String msService = "[Sessions Srv]: ";

    public SServiceSessions(SServer server) throws RemoteException {
        moServer = server;

        mnCurrentSessionId = 0;
        mvServerSessions = new Vector<SSessionServer>();
        mvEntityCategories = new Vector<SDataSysEntityCategory>();

        try {
            moStatement = moServer.getDatabase().getConnection().createStatement();
        }
        catch (SQLException e) {
            moServer.renderMessageLn(msService + e);
        }

        readEntityCategories();
    }

    /*
     * Private functions:
     */

    private void readEntityCategories() {
        String sql = "";
        ResultSet resultSet = null;
        Statement statementAux = null;

        mvEntityCategories.clear();

        try {
            statementAux = moStatement.getConnection().createStatement();

            sql = "SELECT id_ct_ent FROM erp.cfgs_ct_ent ";
            resultSet = moStatement.executeQuery(sql);
            while (resultSet.next()) {
                SDataSysEntityCategory category = new SDataSysEntityCategory();
                if (category.read(new int[] { resultSet.getInt("id_ct_ent")}, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ);
                }
                else {
                    mvEntityCategories.add(category);
                }
            }
        }
        catch (SQLException e) {
            moServer.renderMessageLn(msService + e);
        }
        catch (Exception e) {
            moServer.renderMessageLn(msService + e);
        }
    }

    /*
     * Public functions:
     */

    public synchronized void evaluateTimeouts() {
        int i = 0;

        moServer.renderMessageLn(msService + "Updating server session timeouts...");

        while (i < mvServerSessions.size()) {
            if (moServer.getSystemDatetime().getTime() - mvServerSessions.get(i).getTimestamp().getTime() >= SServerConstants.TIMEOUT_SESSION) {
                mvServerSessions.remove(i);
            }
            else {
                i++;    // evaluate next session
            }
        }

        moServer.renderMessageLn(msService + "Server session timeouts updated!");
    }

    public synchronized void renderSessions() {
        moServer.renderMessage(msService + "Server sessions:");

        for (int i = 0; i < mvServerSessions.size(); i++) {
            moServer.renderMessage("- Timestamp: [" + moServer.getDatetimeFormat().format(mvServerSessions.get(i).getTimestamp()) + "], " +
                    "session ID: [" + mvServerSessions.get(i).getSessionId() + "], " +
                    "user: [" + mvServerSessions.get(i).getUserName() + "]");
        }

        moServer.renderMessageLn(msService + "Active server sessions: [" + mvServerSessions.size() + "]");
    }

    public synchronized boolean isSessionAlive(final int sessionId) {
        boolean alive = false;

        for (int i = 0; i < mvServerSessions.size(); i++) {
            if (sessionId == mvServerSessions.get(i).getSessionId()) {
                alive = true;
                break;
            }
        }

        return alive;
    }

    public synchronized SSessionServer getSession(final int sessionId) {
        SSessionServer session = null;

        for (int i = 0; i < mvServerSessions.size(); i++) {
            if (sessionId == mvServerSessions.get(i).getSessionId()) {
                session = mvServerSessions.get(i);
                break;
            }
        }

        return session;
    }

    public synchronized SLoginResponse login(final SLoginRequest request) {
        int userId = 0;
        int responseType = SLibConstants.UNDEFINED;
        boolean access = false;
        String sql = "";
        String pswd = "";
        ResultSet resultSet = null;
        SSessionXXX session = null;

        try {
            moServer.renderMessageLn(msService + "Login attempt for user [" + request.getUserName() + "]");

            sql = "SELECT PASSWORD('" + request.getUserPassword() + "') AS f_pswd ";
            resultSet = moStatement.executeQuery(sql);
            if (resultSet.next()) {
                pswd = resultSet.getString("f_pswd");
            }

            sql = "SELECT id_usr, usr_pswd, b_univ, b_act, b_del FROM usru_usr WHERE usr = '" + request.getUserName() + "' ";
            resultSet = moStatement.executeQuery(sql);
            if (!resultSet.next()) {
                responseType = SLibConstants.LOGIN_ERROR_USR_INVALID;
            }
            else if (!resultSet.getBoolean("b_act")) {
                responseType = SLibConstants.LOGIN_ERROR_USR_INACTIVE;
            }
            else if (resultSet.getBoolean("b_del")) {
                responseType = SLibConstants.LOGIN_ERROR_USR_DELETED;
            }
            else if (resultSet.getString("usr_pswd").compareTo(pswd) != 0) {
                responseType = SLibConstants.LOGIN_ERROR_USR_PSWD_INVALID;
            }
            else {
                userId = resultSet.getInt("id_usr");

                if (resultSet.getBoolean("b_univ")) {
                    access = true;
                }
                else {
                    sql = "SELECT COUNT(*) AS f_count FROM usru_access_co WHERE id_usr = " + userId + " AND id_co = " + request.getCompanyId() + " ";
                    resultSet = moStatement.executeQuery(sql);
                    if (resultSet.next()) {
                        if (resultSet.getInt("f_count") > 0) {
                            access = true;
                        }
                        else {
                            responseType = SLibConstants.LOGIN_ERROR_USR_CO_INVALID;
                        }
                    }
                }

                if (!access) {
                    moServer.renderMessageLn(msService + "Login attempt for user [" + request.getUserName() + "] denied!");
                }
                else {
                    // Create and export to RMI session server for client:

                    SSessionServer sessionServer = new SSessionServer(moServer, ++mnCurrentSessionId, userId, request.getUserName(), request.getCompanyId(), moServer.getCompanyDatabaseName(request.getCompanyId()));

                    exportObject(sessionServer);
                    mvServerSessions.add(sessionServer);

                    // Create session for client:

                    SDataUser user = new SDataUser();
                    SDataCompany company = new SDataCompany();
                    SDataParamsCompany paramsCompany = new SDataParamsCompany();

                    if (user.read(new int[] { userId }, sessionServer.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ);
                    }
                    if (company.read(new int[] { request.getCompanyId() }, sessionServer.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ);
                    }
                    if (paramsCompany.read(new int[] { request.getCompanyId() }, sessionServer.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ);
                    }

                    session = new SSessionXXX();
                    session.setSessionId(sessionServer.getSessionId());
                    session.setSystemDate(moServer.getSystemDatetime());
                    session.setUser(user);
                    session.setCompany(company);
                    session.setParamsCompany(paramsCompany);
                    session.setParamsErp(moServer.getParamsErp());
                    session.setFormatters(new SFormatters(moServer.getParamsErp()));
                    session.setSessionServer(sessionServer);
                    session.getEntityCategories().addAll(mvEntityCategories);
                    session.prepareAccess();

                    responseType = SLibConstants.LOGIN_OK;

                    moServer.renderMessageLn(msService + "Login attempt for user [" + request.getUserName() + "] gained!");
                }
            }
        }
        catch (RemoteException e) {
            moServer.renderMessageLn(msService + e);
        }
        catch (SQLException e) {
            moServer.renderMessageLn(msService + e);
        }
        catch (Exception e) {
            moServer.renderMessageLn(msService + e);
        }

        return new SLoginResponse(responseType, session);
    }

    public synchronized void logout(final int sessionId) {
        boolean logout = false;
        SSessionServer session = null;
        moServer.renderMessageLn(msService + "Logout attempt for session [" + sessionId + "]");

        for (int i = 0; i < mvServerSessions.size(); i++) {
            if (sessionId == mvServerSessions.get(i).getSessionId()) {
                logout = true;

                moServer.getServiceDataLocks().terminateLocks(sessionId, true);

                session = mvServerSessions.remove(i);
                session.closeSession();
                moServer.renderMessageLn(msService + "Logout attempt for session [" + sessionId + "], user [" + session.getUserName() + "] done!");
                break;
            }
        }

        if (!logout) {
            moServer.renderMessageLn(msService + "Logout attempt for session [" + sessionId + "] failed!");
        }
    }

    public synchronized void terminateService() {
        SSessionServer session = null;

        moServer.renderMessage(msService + "Terminating service...");

        try {
            while (mvServerSessions.size() > 0) {
                try {
                    session = mvServerSessions.remove(0);
                    session.closeSession();
                }
                catch (Exception e) {
                    moServer.renderMessage(msService + e);
                }
            }

            if (moStatement != null && !moStatement.isClosed()) {
                moStatement.close();
            }
        }
        catch (SQLException e) {
            moServer.renderMessage(msService + e);
        }
        catch (Exception e) {
            moServer.renderMessage(msService + e);
        }

        moServer.renderMessage(msService + "Service terminated!");
    }
}
