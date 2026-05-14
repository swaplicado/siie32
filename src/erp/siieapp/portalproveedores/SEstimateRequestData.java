/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.portalproveedores;

/**
 * Representa una solicitud de cotización enviada a un proveedor.
 * <p>
 * Contiene los datos de cabecera de la solicitud: proveedor destinatario,
 * asunto, cuerpo del correo y fecha de creación. Las partidas de la solicitud
 * se manejan en {@link SEstimateRequestEtyData}.
 * </p>
 *
 * @author César Orozco
 */
public class SEstimateRequestData {

    /**
     * ID de la solicitud de cotización.
     */
    int idEstimateRequest;
    /**
     * Año fiscal de la solicitud.
     */
    int idYear;
    /**
     * Número consecutivo de la solicitud.
     */
    int number;
    /**
     * ID del socio de negocio (proveedor) destinatario.
     */
    int idBp;
    /**
     * Nombre del proveedor destinatario.
     */
    String nameBp;
    /**
     * Cuerpo del correo enviado al proveedor.
     */
    String body;
    /**
     * Dirección(es) de correo a las que se envió la solicitud.
     */
    String mailsTo;
    /**
     * Asunto del correo enviado al proveedor.
     */
    String subject;
    /**
     * Fecha y hora de creación de la solicitud.
     */
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
