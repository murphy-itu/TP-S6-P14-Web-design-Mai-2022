/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetspringboot.Model;

import java.sql.Connection;
import java.util.List;
import projetspringboot.annotation.Colonne;
import projetspringboot.annotation.Id;
import projetspringboot.annotation.Table;
import projetspringboot.repository.Genericdao;

/**
 *
 * @author Murphy
 */

@Table(name = "photo")
public class Photo {
    
    @Id(serial = true)
    Integer id;
    @Colonne(name = "nom")
    String nom;
    @Colonne(name = "base64")
    String base64;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
       
    public  Photo getPhoto (Genericdao genericdao,Connection connection) throws Exception {
        Photo photo = new Photo();
        photo.setBase64(base64);
        List list = genericdao.select(photo,connection);
        if (!list.isEmpty()) {
            return (Photo)list.get(0);
        }
        genericdao.insert(this,connection);
        return (Photo)genericdao.select(this,connection).get(0);
    }
    
}
