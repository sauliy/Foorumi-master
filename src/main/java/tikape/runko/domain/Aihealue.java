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
public class Aihealue {

    private String kuvaus;
    private Integer id;

    public Aihealue(Integer id, String kuvaus) {
        this.id = id;
        this.kuvaus = kuvaus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String nimi) {
        this.kuvaus = nimi;
    }

    public String toString() {
        return this.kuvaus;
    }
}
