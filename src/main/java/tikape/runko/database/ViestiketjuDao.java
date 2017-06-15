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
import tikape.runko.domain.Aihealue;
import tikape.runko.domain.Viestiketju;

/**
 *
 * @author Sauliy
 */
public class ViestiketjuDao implements Dao<Viestiketju, Integer> {
    
    private Database database;
    
    public ViestiketjuDao(Database database){
        this.database = database;
    }

    @Override
    public Viestiketju findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viestiketju WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String aihe = rs.getString("aihe");
        Integer aihealue = rs.getInt("aihealue");
        
        Viestiketju o = new Viestiketju(id, aihealue, aihe);

        rs.close();
        stmt.close();
        connection.close();

        return o; //
    }

    
    public List<Viestiketju> findAllAihealueesta(Integer aihealue) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viestiketju WHERE aihealue = ?");
        stmt.setObject(1, aihealue);
        ResultSet rs = stmt.executeQuery();
        List<Viestiketju> viestiketjut = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String aihe = rs.getString("aihe");
            Integer aihealueId = rs.getInt("aihealue");
            
            viestiketjut.add(new Viestiketju(id, aihealue, aihe));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestiketjut;//
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Viestiketju> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
