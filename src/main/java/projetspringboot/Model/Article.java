/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetspringboot.Model;

import java.sql.Connection;
import projetspringboot.annotation.Colonne;
import projetspringboot.annotation.Id;
import projetspringboot.annotation.Table;
import projetspringboot.repository.Genericdao;

/**
 *
 * @author Murphy
 */

@Table(name = "article")
public class Article {
    
    @Id(serial = true)
    Integer id;
    @Colonne(name = "titre")
    String titre;
    @Colonne(name = "idphoto")
    Integer idphoto;
    @Colonne(name = "idcategorie")
    Integer idcategorie;
    @Colonne(name = "resume")
    String resume;
    @Colonne(name = "contenu")
    String contenu;

    Photo photo;
    Categorie categorie;
    String url;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Integer getIdcategorie() {
        return idcategorie;
    }

    public void setIdcategorie(Integer idcategorie) {
        this.idcategorie = idcategorie;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Integer getIdphoto() {
        return idphoto;
    }

    public void setIdphoto(Integer idphoto) {
        this.idphoto = idphoto;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public void setPhoto (Genericdao genericdao,Connection connection) throws Exception {
        Photo p = new Photo();
        p.setId(idphoto);
        p = (Photo)genericdao.select(p,connection).get(0);
        setPhoto(p);
    }
    
    public void setCategorie (Genericdao genericdao,Connection connection) throws Exception {
        Categorie c = new Categorie();
        c.setId(idcategorie);
        c = (Categorie)genericdao.select(c,connection).get(0);
        setCategorie(c);
    }
    
    public void setUrl () {
        String url = categorie.getLibelle()+"/"+titre+"-"+id;
        url = url.replaceAll("[,\\s\']+","-");
        setUrl(url);
    }
    
}
