/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.sql.Timestamp;

/**
 *
 * @author Sauliy
 */
public class Viesti {

    private String nimimerkki;
    private String sisalto;
    private int viestiketjuid;
    private int kayttajaid;
    private Timestamp aika;

    public Viesti(int kayttaja, String sisaltoa, int viestiketju) {
        kayttajaid = kayttaja;
        sisalto = sisaltoa;
        viestiketjuid = viestiketju;

    }
    
    public Viesti(int kayttaja ,String sisaltoa, int viestiketju, Timestamp aika,  String nimimerkki) {
        kayttajaid = kayttaja;
        sisalto = sisaltoa;
        viestiketjuid = viestiketju;
        this.aika = aika;  
        this.nimimerkki = nimimerkki;
        
    }

    public String getNimimerkki() {
        return nimimerkki;
    }

    public int getKayttajaid() {
        return kayttajaid;
    }
    
    public Timestamp getAika() {
        return aika;
    }

    public int getViestiketjuid() {
        return viestiketjuid;
    }

    public String getSisalto() {
        return sisalto+"jaahas";
    }
    
    public String toString() {
        return sisalto;
    }

}
