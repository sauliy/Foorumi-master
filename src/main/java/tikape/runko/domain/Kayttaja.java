package tikape.runko.domain;

public class Kayttaja {

    private Integer id;
    private String nimi;
    private String salasana;
    private String liittymisaika;

    public Kayttaja(Integer id, String nimi, String salasana) {
        this.id = id;
        this.nimi = nimi;
        this.salasana = salasana;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    public String toString() {
        return this.nimi;
    }

}
