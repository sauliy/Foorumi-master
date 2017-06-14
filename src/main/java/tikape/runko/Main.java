package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AihealueDao;
import tikape.runko.database.Database;
import tikape.runko.database.KayttajaDao;
import tikape.runko.database.ViestiketjuDao;
import tikape.runko.domain.Aihealue;
import tikape.runko.domain.Kayttaja;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:foorumitesti3.db");
        database.init();

        AihealueDao aihealueDao = new AihealueDao(database);
        KayttajaDao kayttajaDao = new KayttajaDao(database);
        ViestiketjuDao viestiketjuDao = new ViestiketjuDao(database);
        
        
        
        

        
        
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        post("/register", (req, res) -> {
            String username = req.queryParams("nimimerkki");
            String password = req.queryParams("salasana");

            Kayttaja kayttaja = kayttajaDao.findOneNimimerkilla(username);

            if (kayttaja == null && username.length() < 21 && password.length() < 21 && !password.isEmpty() && !username.isEmpty()) {
                database.lisaaKayttaja(username, password);
                res.redirect("/");
                return "";
            }
            res.redirect("/");
            return "";

        });

        post("/login", (req, res) -> {
            String username = req.queryParams("nimimerkki");
            String password = req.queryParams("salasana");

            Kayttaja kayttaja = kayttajaDao.findOneNimimerkillaJaSalasanalla(username, password);

            if (kayttaja == null) {
                res.redirect("/");
                return "";
            }
            
            req.session(true).attribute("user", kayttaja);
            
            res.redirect("/s/foorumi");
            return "";
        });
        
        before((req, res) -> {
            if (!req.url().contains("/s/")) {
                return;
            }

            Session sess = req.session();
            if (sess.attribute("user") == null) {
                sess.invalidate();
                res.redirect("/");
            }
        });

        get("/s/foorumi", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");
            return new ModelAndView(map, "foorumi");
        }, new ThymeleafTemplateEngine());

        get("/s/foorumi/aihealueet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("aihealueet", aihealueDao.findAll());

            return new ModelAndView(map, "aihealueet");
        }, new ThymeleafTemplateEngine());
        
        get("/s/foorumi/:kuvaus", (req,res) -> {
            HashMap map = new HashMap<>();
            Aihealue aihealue = aihealueDao.findOneKuvauksella(req.params("kuvaus"));
            Integer aihealueId = aihealue.getId();
            
            req.session(true).attribute("aihealueTunnus", aihealueId);
            req.session(true).attribute("aihealueKuvaus", aihealue.getKuvaus());
            
            System.out.println(aihealueId);
            map.put("aihealue", aihealue);
            map.put("viestiketjut", viestiketjuDao.findAllAihealueesta(aihealueId));
            
            return new ModelAndView(map, "aihealue");
        }, new ThymeleafTemplateEngine());
        
        get("/s/foorumi/:kuvaus/:id", (req,res) -> {
            HashMap map = new HashMap<>();
            map.put("kuvaus", aihealueDao.findOneKuvauksella(req.params("kuvaus")));
            map.put("id", viestiketjuDao.findOne(Integer.parseInt(req.params("id"))));
            
            return new ModelAndView(map, "viestiketju");
        }, new ThymeleafTemplateEngine());

        get("/s/kayttajat/:nimi", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kayttaja", kayttajaDao.findOneNimimerkilla((req.params("nimi"))));

            return new ModelAndView(map, "käyttäjä");
        }, new ThymeleafTemplateEngine());

        post("/logout", (req, res) -> {
            req.session().invalidate();
            res.redirect("/");
            return "";
        });
        
        post("/s/foorumi/viestiketjunlisays", (req,res) -> {
            String viestiketjuAihe = req.queryParams("aihe");
            
            Session sess = req.session();
            Integer aihealueTunnus = sess.attribute("aihealueTunnus");
            String aihealueKuvaus = sess.attribute("aihealueKuvaus");
            
            if (viestiketjuAihe.length() < 101) {
                database.lisaaViestiketju(aihealueTunnus,viestiketjuAihe);
            }
            
            res.redirect("/s/foorumi/"+aihealueKuvaus);
            return "";
        });

        

        get("/kayttajat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kayttajat", kayttajaDao.findAll());

            return new ModelAndView(map, "kayttajat");
        }, new ThymeleafTemplateEngine());

    }
}
