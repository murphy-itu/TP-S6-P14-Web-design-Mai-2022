/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetspringboot.Controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import projetspringboot.Model.Article;
import projetspringboot.Model.Categorie;
import projetspringboot.Model.Photo;
import projetspringboot.Model.Utilisateur;
import projetspringboot.repository.Genericdao;

/**
 *
 * @author Murphy
 */

@Controller
public class MyController {
    
    @Autowired
    DataSource datasource;
    
    @Autowired
    Genericdao genericdao;
    
    List<Article>  listarticles;
    
    @RequestMapping("/")
    public String index (@RequestParam HashMap<String,String> params,Model model,HttpSession session) throws Exception {
        if (!params.isEmpty()) {
            Connection connection = null ;
            try {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setPseudo(params.get("pseudo"));
                connection = datasource.getConnection();
                List list = genericdao.select(utilisateur,connection);
                if (!list.isEmpty()) {
                    utilisateur.setMotdepasse(params.get("motdepasse"));
                    list = genericdao.select(utilisateur,connection);
                    if (list.isEmpty()) {
                        throw new Exception("erreur de mot de passe !");
                    } else {
                        session.setAttribute("utilisateur",(Utilisateur)list.get(0));
                        return "redirect:/liste-articles-sur-les-meuilleurs-informations-sur-intelligence-artificielle-IA";
                    }
                } else {
                    throw new Exception("votre compte nexiste pas !");
                }
                 
            } catch (Exception e) {
                model.addAttribute("error",e.getMessage());
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
            
        }
        return "login";
    }
    
    
    @RequestMapping("/inscription")
    public String inscription (@RequestParam HashMap<String,String> params,Model model) throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        if (!params.isEmpty()) {
            Connection connection = null ;
            try {
                utilisateur.setNom(params.get("nom"));
                utilisateur.setPrenom(params.get("prenom"));
                utilisateur.setEmail(params.get("email"));
                utilisateur.setPseudo(params.get("pseudo"));
                utilisateur.setMotdepasse(params.get("motdepasse"));
                
                connection = datasource.getConnection();
                genericdao.insert(utilisateur,connection);
                
                model.addAttribute("message","votre compte est creer avec succes");
                
            } catch (Exception e) {
                model.addAttribute("error",e.getMessage());
            } finally {
                if (connection != null) {
                    connection.close();
                }
                model.addAttribute("utilisateur",params);
            }
            
        } else {
            model.addAttribute("utilisateur",utilisateur);
        }
        return "inscription";
    }
    
    @RequestMapping("/deconnecter")
    public String deconnecter (HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    @RequestMapping("/ajouter-article-sur-intelligence-artificielle-IA")
    public String ajoutcontenu (Article article,Model model,HttpSession session,@RequestParam(name = "file",required = false) MultipartFile file) throws Exception {
        Connection connection = null;
        try {
            if (session.getAttribute("utilisateur") == null) {
                return "redirect:/";
            }
            connection = datasource.getConnection();
            if ((!file.isEmpty() && article.getTitre() != null)) {
                
                connection.setAutoCommit(false);
                byte[] bytes = file.getBytes();
                
                Photo photo = new Photo();
                photo.setNom(file.getOriginalFilename());
                photo.setBase64(Base64.getEncoder().encodeToString(bytes));
                
                photo = photo.getPhoto(genericdao,connection);
                
                article.setIdphoto(photo.getId());
                
                genericdao.insert(article,connection);
                connection.commit();
                model.addAttribute("message","contenu inserer avec succes");
                
            }
        } catch (Exception e) {
            if (!connection.getAutoCommit()) {
                connection.rollback();
            }
            model.addAttribute("error", e.getMessage());
        } finally {
            List<Categorie>  listcategorie = (List<Categorie>)genericdao.select(new Categorie(),connection);
            model.addAttribute("categorie",listcategorie);
            if (connection != null) {
                connection.close();
            }
        }
        return "ajoutercontenu";
    }
    
    @RequestMapping("/liste-articles-sur-les-meuilleurs-informations-sur-intelligence-artificielle-IA")
    public String listecontenu (Model model,HttpSession session,@RequestParam(required = false) String recherche) throws Exception {
        Connection connection = null;
        try {
            if (session.getAttribute("utilisateur") == null) {
                return "redirect:/";
            }
            connection = datasource.getConnection();
            listarticles = (List<Article>)genericdao.select(new Article(),connection);
            
            for (Article listarticle : listarticles) {
                listarticle.setPhoto(genericdao,connection);
                listarticle.setCategorie(genericdao,connection);
                listarticle.setUrl();
            }
            
            model.addAttribute("articles",listarticles);
            
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return "listecontenu";
    }
    
    @RequestMapping("/articles/{categorie}/{titre:.+}-{id}")
    public String detailcontenu (@PathVariable Integer id,@PathVariable String titre,Model model,HttpSession session) throws Exception {
        Connection connection = null;
        try {
            if (session.getAttribute("utilisateur") == null) {
                return "redirect:/";
            }
            connection = datasource.getConnection();
            
            Article article = new Article();
            article.setId(id);
            article = (Article) genericdao.select(article,connection).get(0);
            article.setPhoto(genericdao,connection);
            article.setCategorie(genericdao,connection);
            
            model.addAttribute("article", article);
        } catch (Exception e) {
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return "detailcontenu";
    }
    
    @RequestMapping("/listes-articles-sur-les-meuilleurs-informations-sur-intelligence-artificielle-IA")
    public String listecontenufront (Model model,@RequestParam(required = false) String recherche) throws Exception {
        Connection connection = null;
        try {
            
            connection = datasource.getConnection();
            listarticles = (List<Article>)genericdao.select(new Article(),connection);
            
            for (Article listarticle : listarticles) {
                listarticle.setPhoto(genericdao,connection);
                listarticle.setCategorie(genericdao,connection);
                listarticle.setUrl();
            }
            
            model.addAttribute("articles",listarticles);
            
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return "listecontenufront";
    }
        
    @RequestMapping("/article/{categories}/{titre:.+}-{id}")
    public String detailcontenufront (@PathVariable Integer id,@PathVariable String titre,Model model) throws Exception {
        Connection connection = null;
        try {
           
            connection = datasource.getConnection();
            
            Article article = new Article();
            article.setId(id);
            article = (Article) genericdao.select(article,connection).get(0);
            article.setPhoto(genericdao,connection);
            article.setCategorie(genericdao,connection);
            
            model.addAttribute("article", article);
        } catch (Exception e) {
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return "detailcontenufront";
    }
    
}
