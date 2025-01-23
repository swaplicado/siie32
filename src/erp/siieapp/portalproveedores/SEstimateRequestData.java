/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.portalproveedores;

/**
 *
 * @author César Orozco
 */
public class SEstimateRequestData {
    int idEstimateRequest;
    int idYear;
    int number;
    int idBp;
    String nameBp;
    String body;
    String mailsTo;
    String subject;
    String date;

    public int getIdEstimateRequest() {
        return idEstimateRequest;
    }

    public int getNumber() {
        return number;
    }

    public int getIdBp() {
        return idBp;
    }

    public String getBody() {
        return body;
    }

    public String getMailsTo() {
        return mailsTo;
    }

    public String getNameBp() {
        return nameBp;
    }

    public String getSubject() {
        return subject;
    }

    public int getIdYear() {
        return idYear;
    }

    public String getDate() {
        return date;
    }
    
    public void setIdEstimateRequest(int idEstimateRequest) {
        this.idEstimateRequest = idEstimateRequest;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setIdBp(int idBp) {
        this.idBp = idBp;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setMailsTo(String mailsTo) {
        this.mailsTo = mailsTo;
    }

    public void setNameBp(String nameBp) {
        this.nameBp = nameBp;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setIdYear(int idYear) {
        this.idYear = idYear;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
