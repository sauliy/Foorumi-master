/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Kayttaja;

public class KayttajaDao implements Dao<Kayttaja, Integer> {

    private Database database;

    public KayttajaDao(Database database) {
        this.database = database;
    }

    @Override
    public Kayttaja findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kayttaja WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimimerkki");
        String salasana = rs.getString("salasana");

        Kayttaja o = new Kayttaja(id, nimi, salasana);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    public Kayttaja findOneNimimerkilla(String nimimerkki) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kayttaja WHERE nimimerkki = ?");
        stmt.setObject(1, nimimerkki);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimimerkki");
        String salasana = rs.getString("salasana");

        Kayttaja o = new Kayttaja(id, nimi, salasana);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    public Kayttaja findOneNimimerkillaJaSalasanalla(String nimimerkki, String salasana) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kayttaja WHERE nimimerkki = ?");
        stmt.setObject(1, nimimerkki);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String oikeaSalasana = rs.getString("salasana");
        if (oikeaSalasana.equals(salasana)) {
            Kayttaja o = new Kayttaja(id, nimimerkki, salasana);

        rs.close();
        stmt.close();
        connection.close();

        return o;
            
        }
        

        
        return null;
    }


        @Override
        public List<Kayttaja> findAll() throws SQLException {

            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kayttaja");

            ResultSet rs = stmt.executeQuery();
            List<Kayttaja> kayttajat = new ArrayList<>();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String nimi = rs.getString("nimimerkki");
                String salasana = rs.getString("salasana");

                kayttajat.add(new Kayttaja(id, nimi, salasana));
            }

            rs.close();
            stmt.close();
            connection.close();

            return kayttajat;
        }

        @Override
        public void delete
        (Integer key) throws SQLException {
            // ei toteutettu
        }

    }
