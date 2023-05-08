/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetspringboot.Model;

import projetspringboot.annotation.Colonne;
import projetspringboot.annotation.Id;
import projetspringboot.annotation.Table;

/**
 *
 * @author Murphy
 */

@Table(name = "utilisateur")
public class Utilisateur {
    
    @Id(serial = true)
    Integer id;
    @Colonne(name = "nom")
    String nom;
    @Colonne(name = "prenom")
    String prenom;
    @Colonne(name = "email")
    String email;
    @Colonne(name = "pseudo")
    String pseudo;
    @Colonne(name = "motdepasse")
    String motdepasse;
    @Colonne(name = "type")
    String type;

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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    
    
    
    
    
}
