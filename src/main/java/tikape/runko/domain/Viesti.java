/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

/**
 *
 * @author Sauliy
 */
public class Viesti {

    private String nimimerkki;
    private String sisalto;
    private int viestiketjuid;
    private int kayttajaid;

    public Viesti(int kayttaja, String sisaltoa, int viestiketju) {
        kayttajaid = kayttaja;
        sisalto = sisaltoa;
        viestiketjuid = viestiketju;

    }

    public String getNimimerkki() {
        return nimimerkki;
    }

    public int getKayttajaid() {
        return kayttajaid;
    }

    public int getViestiketjuid() {
        return viestiketjuid;
    }

    public String getSisalto() {
        return sisalto;
    }

}
