/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetspringboot.fonction;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Murphy
 */
public class Connexion {
    
    public static Connection getConnection () throws Exception {
        return Fonction.getConnection("postgres","T..Murphy","testgeneric");
    }
    
}
