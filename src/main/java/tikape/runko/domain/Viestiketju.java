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
public class Viestiketju {
    
    private Integer id;
    private String aihe;
    private Integer aihealue;
    private String aihealueKuvaus;
    
    public Viestiketju(Integer id, Integer aihealue, String aihe) {
        this.id = id;
        this.aihealue = aihealue;
        this.aihe=aihe;
    }
    
    public Viestiketju(Integer id, Integer aihealue, String aihe, String aihealueKuvaus) {
        this.id = id;
        this.aihealue = aihealue;
        this.aihe=aihe;
        this.aihealueKuvaus = aihealueKuvaus;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAihe() {
        return aihe;
    }
    
    public String getAihealueKuvaus() {
        return aihealueKuvaus;
    }

    public void setAihe(String nimi) {
        this.aihe = nimi;
    }

    public String toString() {
        return this.aihe;
    }
}
