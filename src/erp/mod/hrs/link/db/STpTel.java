/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

/**
 *
 * @author Cesar Orozco
 */
public class STpTel {
    private int idTel = 0;
  
    private String tpTel = "";

    public int getIdTel() {
      return this.idTel;
    }

    public String getTpTel() {
      return this.tpTel;
    }

    public void setIdTel(int idTel) {
      this.idTel = idTel;
    }

    public void setTpTel(String tpTel) {
      this.tpTel = tpTel;
    }
}
