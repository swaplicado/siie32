/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.human;

/**
 *
 * @author Cesar Orozco
 */
public class SHumanResponse {

    private boolean success;
    private int statusCode;
    private String responseBody;
    private String error;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public String getResponseBody() { return responseBody; }
    public void setResponseBody(String responseBody) { this.responseBody = responseBody; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
