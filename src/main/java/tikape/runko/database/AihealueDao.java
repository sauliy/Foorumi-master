/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import tikape.runko.domain.Aihealue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Kayttaja;

/**
 *
 * @author Sauliy
 */
public class AihealueDao implements Dao<Aihealue, Integer> {
    
    private Database database;
    
    public AihealueDao(Database database) {
        this.database = database;
    }
    
    @Override
    public List<Aihealue> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihealue");

        ResultSet rs = stmt.executeQuery();
        List<Aihealue> aihealueet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String kuvaus = rs.getString("kuvaus");
            
            aihealueet.add(new Aihealue(id, kuvaus));
        }

        rs.close();
        stmt.close();
        connection.close();

        return aihealueet;
    }
    
    public Aihealue findOneKuvauksella(String kuvaus) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihealue WHERE kuvaus = ?");
        stmt.setObject(1, kuvaus);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String kuvaus1 = rs.getString("kuvaus");
        

        Aihealue o = new Aihealue(id, kuvaus1);

        rs.close();
        stmt.close();
        connection.close();

        return o;
    }

    @Override
    public Aihealue findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
