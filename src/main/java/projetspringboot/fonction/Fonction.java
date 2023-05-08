/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetspringboot.fonction;

import projetspringboot.annotation.Colonne;
import projetspringboot.annotation.Id;
import projetspringboot.annotation.Table;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Murphy
 */
public class Fonction {
    
    public static Connection getConnection(String username, String password, String database) throws Exception {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, username, password);
        } catch (Exception e) {
            throw e;
        }
        return connection;
    }
    
    public static String getTableName (Object object) {
        return object.getClass().getAnnotation(Table.class).name();
    }
    
    public static String getColonneName (Field field) {
        if (field.getAnnotation(Colonne.class) != null) {
            return field.getAnnotation(Colonne.class).name();
        }
        if (field.getAnnotation(Id.class) != null ) {
            return field.getAnnotation(Id.class).name();
        }
        return null;
    }
    
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase().concat(str.substring(1));
    }
    
    public static String getMotCleId (Field field) {
        if (field.getAnnotation(Id.class) == null) {
            return null;
        }
        return field.getAnnotation(Id.class).motcle();
    }
    
    public static String getIdName (Field field) {
        if (field.getAnnotation(Id.class) == null) {
            return null;
        }
        return field.getAnnotation(Id.class).name();
    }
    
    public static Object getValueField (Object object,Field field) throws Exception {
        return object.getClass().getMethod("get"+capitalize(field.getName())).invoke(object);
    } 
        
    public static String getSequenceName (Object object) {
        return getTableName(object)+"_seq";
    }
    
    public static boolean isSerial (Field field) {
        if (field.getAnnotation(Id.class) == null) {
            return false;
        }
        return field.getAnnotation(Id.class).serial();
    }
    
    public static String getSqlId  (Object object) {
        String sql = "";
        Field pk = getPrimaryKey(object);
        if (Fonction.isSerial(pk)) {
            sql += "default";
            return sql;
        }
        if (pk.getType().equals(String.class)) {
            if (!getMotCleId(pk).equals("")) {
                sql +=  "'"+Fonction.getMotCleId(pk)+"'||";
            }
        }
        String sequencename = getSequenceName(object);
        sql += "nextval('"+sequencename+"')";
        return sql;
    } 
    
    public static List<Field> getAllFields (Object object) {
        List<Field> resultat = new Vector<>();
        List<Field> allField = new Vector<Field>();
        allField.addAll(Arrays.asList(object.getClass().getDeclaredFields()));
        Class classMere = object.getClass().getSuperclass();
        while (classMere != Object.class) {  
            if (classMere.getDeclaredFields() != null) {
                allField.addAll(Arrays.asList(classMere.getDeclaredFields()));
            }
            classMere = classMere.getSuperclass();
        }
        for (Field field : allField) {
            if (getColonneName(field) != null) {
                resultat.add(field);
            }
        }
        return resultat;
    }
    
    public static Field getPrimaryKey (Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field : fields) {
            if ( field.getAnnotation(Id.class) != null) {
                return field;
            }
        }
        return null;
    }
    
    public static List<Field> getFieldNotNull (Object object) throws Exception {
        List<Field> resultat = new Vector<>();
        List<Field> fields = getAllFields(object);
        for (Field field : fields) {
            if (getColonneName(field) != null && getValueField(object,field) != null ) {
                resultat.add(field);
            }
        }
        return resultat;
    }
    
    public static List getAttributValue (Object object) throws Exception {
        List resultat = new Vector();
        for (Field field : getFieldNotNull(object) ) {
            resultat.add(getValueField(object,field));
        }
        return resultat;
    }
    
    
    public static void executeUpdate(String sql, List objectList, Connection connection) throws Exception {
        PreparedStatement ps = connection.prepareStatement(sql);
        int i = 1;
        for (Object object : objectList) {
            ps.setObject(i, object);
            i++;
        }
        ps.execute();
        ps.close();
    }
    
    public static List executeQuery(Object object, String sql, List objectList, Connection connection) throws Exception {
        List resultat = new Vector();
        PreparedStatement ps = connection.prepareStatement(sql);
        int i = 1;
        for (Object object1 : objectList) {
            ps.setObject(i, object1);
            i++;
        }
        ResultSet resultset = ps.executeQuery();
        List<Field> fields = getAllFields(object);
        while (resultset.next()) {
            Object newObject = object.getClass().newInstance();
            for (Field field : fields) {
                newObject.getClass().getMethod("set" + capitalize(field.getName()), field.getType()).invoke(newObject, resultset.getObject(getColonneName(field)));
            }
            resultat.add(newObject);
        }
        resultset.close();
        ps.close();
        return resultat;
    }
    
}
