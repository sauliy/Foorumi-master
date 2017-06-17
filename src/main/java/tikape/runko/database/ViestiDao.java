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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Viesti;

/**
 *
 * @author Sauliy
 */
public class ViestiDao implements Dao<Viesti, Integer> {
 
    private Database database;
 
    public ViestiDao(Database database) {
        this.database = database;
    }
 
    @Override
    public Viesti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE id = ?");
        stmt.setObject(1, key);
 
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        Integer viestiketjuId = rs.getInt("viestiketju");
        String sisalto = rs.getString("aihe");
        Integer kayttajaId = rs.getInt("sisalto");
        Timestamp aika = rs.getTimestamp("lahetysaika");
 
        Viesti o = new Viesti(kayttajaId, sisalto, viestiketjuId, aika);
 
        rs.close();
        stmt.close();
        connection.close();
 
        return o; //
    }
 
    public List<Viesti> findAllViestiketjusta(Integer viestiketju) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE viestiketju = ?");
        stmt.setObject(1, viestiketju);
        ResultSet rs = stmt.executeQuery();
        List<Viesti> Viestit = new ArrayList<>();
        while (rs.next()) {
 
            String sisalto = rs.getString("sisalto");
            Integer kayttajaId = rs.getInt("kayttaja");
            Timestamp aika = rs.getTimestamp("lahetysaika");
            
 
            Viestit.add(new Viesti(kayttajaId, sisalto, viestiketju, aika));
        }
 
        rs.close();
        stmt.close();
        connection.close();
 
        return Viestit;//
    }
 
    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
    @Override
    public List<Viesti> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
}

