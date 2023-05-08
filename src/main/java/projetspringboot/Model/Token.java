/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetspringboot.Model;

import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import projetspringboot.annotation.Colonne;
import projetspringboot.annotation.Id;
import projetspringboot.annotation.Table;

/**
 *
 * @author Murphy
 */

@Table(name = "token")
public class Token {
    
    @Id(serial = true)
    Integer id;
    @Colonne(name = "idutilisateur")
    Integer idutilisateur;
    @Colonne(name = "token")
    String token;
    @Colonne(name = "delai")
    Timestamp delai;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdutilisateur() {
        return idutilisateur;
    }

    public void setIdutilisateur(Integer idutilisateur) {
        this.idutilisateur = idutilisateur;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getDelai() {
        return delai;
    }

    public void setDelai(Timestamp delai) {
        this.delai = delai;
    }
    
    public void setDelai (int hour,int minutes) {
        Date now = new Date();
        Timestamp delai = new Timestamp(now.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(delai);
        cal.add(Calendar.HOUR,hour);
        cal.add(Calendar.MINUTE,minutes);
        delai = new Timestamp(cal.getTimeInMillis());
        setDelai(delai);
    }
    
    public void setToken () throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(idutilisateur.toString().getBytes());
        byte[] byteData = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        setToken(sb.toString());
        setDelai(1,30);
    }
    
}
