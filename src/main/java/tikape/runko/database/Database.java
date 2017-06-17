package tikape.runko.database;

import java.net.URI;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import tikape.runko.domain.Kayttaja;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        if (this.databaseAddress.contains("postgres")) {
            try {
                URI dbUri = new URI(databaseAddress);

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                return DriverManager.getConnection(dbUrl, username, password);
            } catch (Throwable t) {
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        }

        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = null;
        if (this.databaseAddress.contains("postgres")) {
            lauseet = postgreLauseet();
        } else {
            lauseet = sqliteLauseet();
        }

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    public void lisaaKayttaja(String nimimerkki, String salasana) {
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();
            String kasky = "INSERT INTO Kayttaja (nimimerkki, salasana) VALUES ('" + nimimerkki + "','" + salasana + "');";

            System.out.println("Running command >> " + kasky);
            st.executeUpdate(kasky);

        } catch (Throwable t) {

            System.out.println("Error >> " + t.getMessage());
        }
    }

    public void lisaaViestiketju(Integer aihealue, String aihe) {
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();
            String kasky = "INSERT INTO Viestiketju (aihe, aihealue) VALUES ('" + aihe + "','" + aihealue + "');";

            System.out.println("Running command >> " + kasky);
            st.executeUpdate(kasky);

        } catch (Throwable t) {

            System.out.println("Error >> " + t.getMessage());
        }
    }

    public void lisaaAihealue(String kuvaus) {
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            String kasky = "INSERT INTO Aihealue (kuvaus) VALUES ('" + kuvaus + "');";

            System.out.println("Running command >> " + kasky);
            st.executeUpdate(kasky);

        } catch (Throwable t) {

            System.out.println("Error >> " + t.getMessage());
        }
    }

    public void poistaKayttaja(String nimimerkki) {
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();
            String kasky = "DELETE FROM Kayttaja WHERE nimimerkki = '" + nimimerkki + "';";

            System.out.println("Running command >> " + kasky);
            st.executeUpdate(kasky);

        } catch (Throwable t) {

            System.out.println("Error >> " + t.getMessage());
        }
    }

    public void poistaAihealue(String aihealueKuvaus) {
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();
            String kasky = "DELETE FROM Aihealue WHERE kuvaus = '" + aihealueKuvaus + "';";

            System.out.println("Running command >> " + kasky);
            st.executeUpdate(kasky);

        } catch (Throwable t) {

            System.out.println("Error >> " + t.getMessage());
        }
    }

    public void poistaViestit() {
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();
            
            String kasky = "DELETE FROM Viesti;";

            System.out.println("Running command >> " + kasky);
            st.executeUpdate(kasky);

        } catch (Throwable t) {

            System.out.println("Error >> " + t.getMessage());
        }
        
    }

    public void lisaaViesti(Integer kayttajaid, String sisalto, Integer viestiketjuid) {
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();
            Timestamp aika = new Timestamp(System.currentTimeMillis());
            String kasky = "INSERT INTO Viesti (viestiketju, kayttaja, sisalto, lahetysaika) VALUES ('" + viestiketjuid + "','" + kayttajaid + "','" + sisalto + "','" + aika + "');";

            System.out.println("Running command >> " + kasky);
            st.executeUpdate(kasky);

        } catch (Throwable t) {

            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> postgreLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        lista.add("CREATE TABLE Kayttaja (id integer SERIAL PRIMARY KEY, nimimerkki varchar(20) NOT NULL, salasana varchar(20) NOT NULL, liittymisaika timestamp);");
        lista.add("CREATE TABLE Aihealue (id integer SERIAL PRIMARY KEY, kuvaus varchar(100));");
        lista.add("CREATE TABLE Viestiketju (id integer SERIAL PRIMARY KEY, aihe varchar (100), aihealue integer NOT NULL, FOREIGN KEY(aihealue) REFERENCES Aihealue(id));");
        lista.add("CREATE TABLE Viesti (viestiketju integer NOT NULL, kayttaja integer NOT NULL, sisalto varchar(300) NOT NULL, lahetysaika timestamp, FOREIGN KEY(viestiketju) REFERENCES Viestiketju(id), FOREIGN KEY(kayttaja) REFERENCES Kayttaja(id))");
        lista.add("INSERT INTO Kayttaja(id,nimimerkki, salasana) VALUES ('1','Sauli', '123');");
        lista.add("INSERT INTO Aihealue(id,kuvaus) VALUES ('1','Yleinen alue');");
        lista.add("INSERT INTO Viestiketju(id,aihe, aihealue) VALUES ('1','Nyt keskustellaan','1');");
        lista.add("INSERT INTO Viesti(viestiketju, kayttaja, sisalto) VALUES ('1', '1', 'joojo');");
        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä

        // heroku käyttää SERIAL-avainsanaa uuden tunnuksen automaattiseen luomiseen
        return lista;
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Calendar cal = Calendar.getInstance();
        //String time = dateFormat.format(cal); //2016/11/16 12:08:43
        // tietokantataulujen luomiseen tarvittavat komennot suoritusjarjestyksessa

        lista.add("CREATE TABLE Kayttaja (id integer PRIMARY KEY, nimimerkki varchar(20) NOT NULL, salasana varchar(20) NOT NULL, liittymisaika timestamp);");
        lista.add("CREATE TABLE Aihealue (id integer PRIMARY KEY, kuvaus varchar(100));");
        lista.add("CREATE TABLE Viestiketju (id integer PRIMARY KEY, aihe varchar (100), aihealue integer NOT NULL, FOREIGN KEY(aihealue) REFERENCES Aihealue(id));");
        lista.add("CREATE TABLE Viesti (viestiketju integer NOT NULL, kayttaja integer NOT NULL, sisalto varchar(300) NOT NULL, lahetysaika timestamp, FOREIGN KEY(viestiketju) REFERENCES Viestiketju(id), FOREIGN KEY(kayttaja) REFERENCES Kayttaja(id))");
        lista.add("INSERT INTO Kayttaja(id,nimimerkki, salasana) VALUES ('1','Sauli', '123');");
        lista.add("INSERT INTO Aihealue(id,kuvaus) VALUES ('1','Yleinen alue');");
        lista.add("INSERT INTO Viestiketju(id,aihe, aihealue) VALUES ('1','Nyt keskustellaan','1');");
        lista.add("INSERT INTO Viesti(viestiketju, kayttaja, sisaltö) VALUES ('1', '1', 'joojo');");
        return lista;
    }
}
