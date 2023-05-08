/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetspringboot.repository;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetspringboot.fonction.Fonction;

/**
 * 
 *
 * @author Murphy
 */

@Component
public class Genericdao {
    
    public  void insert(Object object,Connection connection) throws Exception {
        String sql = "insert into "+Fonction.getTableName(object)+" (";
        List<Field> fields = Fonction.getFieldNotNull(object);
        Field pk = Fonction.getPrimaryKey(object);
        fields.removeIf(p -> p.equals(pk));
        fields.add(0, pk);
        List value = new Vector();
        int i = 0;
        for (; i < fields.size()-1 ; i++) {
            sql += Fonction.getColonneName(fields.get(i))+",";
        }
        sql += Fonction.getColonneName(fields.get(i));
        sql += ") values (";
        sql += Fonction.getSqlId(object);
        int j=1;
        for (; j < fields.size() ; j++) {
            sql += ",?";
            value.add(Fonction.getValueField(object,fields.get(j)));
        }
        sql += ")";
        System.out.println(sql);
        Fonction.executeUpdate(sql,value,connection);
    }
    
    public List select (Object object,Connection connection) throws Exception {
        String sql = "select * from "+Fonction.getTableName(object)+" where 0=0";
        List<Field> fields = Fonction.getFieldNotNull(object);
        List value = new Vector();
        for (Field field : fields) {
            sql += " and "+Fonction.getColonneName(field)+"="+"?";
            value.add(Fonction.getValueField(object,field));
        }
        System.out.println(sql);
        return Fonction.executeQuery(object,sql, value, connection);
    }
    
    public void update (Object object,Connection connection,String... attribut) throws Exception {
        String sql = "update "+Fonction.getTableName(object)+" set ";
        List<String> listattribut = Arrays.asList(attribut);
        List<Field> setfields = Fonction.getFieldNotNull(object).stream().filter( p -> !listattribut.contains(Fonction.getColonneName(p))).collect(Collectors.toList());
        List<Field> wherefields = Fonction.getFieldNotNull(object).stream().filter( p -> listattribut.contains(Fonction.getColonneName(p))).collect(Collectors.toList());
        List value = new Vector();
        int i = 0;
        for (; i < setfields.size()-1 ; i++) {
            sql += Fonction.getColonneName(setfields.get(i))+"=?,";
            value.add(Fonction.getValueField(object,setfields.get(i)));
        }
        sql += Fonction.getColonneName(setfields.get(i))+"="+"?";
        value.add(Fonction.getValueField(object,setfields.get(i)));        
        sql += " where 0=0";
        for (Field wherefield : wherefields) {
            sql += " and "+Fonction.getColonneName(wherefield)+"=?";
            value.add(Fonction.getValueField(object, wherefield));
        }
        System.out.println(sql);
        Fonction.executeUpdate(sql, value, connection);
    }
       
    public int count (Object object,Connection connection) throws Exception {
        return select(object, connection).size();
    }
    
    public List pagination (Object object,Connection connection,Integer page,Integer limit)  throws Exception{
        String sql = "select * from " + Fonction.getTableName(object) ;
        List<Field> fields = Fonction.getFieldNotNull(object);
        List value = new Vector();
        for (Field field : fields) {
            sql += " and "+Fonction.getColonneName(field)+"="+"?";
            value.add(Fonction.getValueField(object,field));
        }
        sql += " limit ? offset ? ";
        value.add(limit);
        value.add((page-1)*limit);
        System.out.println(sql);
        return Fonction.executeQuery(object,sql, value, connection);
    }
    
    
}
