package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Session;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AihealueDao;
import tikape.runko.database.Database;
import tikape.runko.database.KayttajaDao;
import tikape.runko.domain.Kayttaja;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:foorumitesti3.db");
        database.init();

        AihealueDao aihealueDao = new AihealueDao(database);
        KayttajaDao kayttajaDao = new KayttajaDao(database);
        
        

        List<Kayttaja> kayttajat = kayttajaDao.findAll();
        for (Kayttaja kayttaja : kayttajat) {
            System.out.println(kayttaja);
        }

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        post("/register", (req, res) -> {
            String username = req.queryParams("nimimerkki");
            String password = req.queryParams("salasana");

            Kayttaja kayttaja = kayttajaDao.findOneNimimerkilla(username);

            if (kayttaja == null) {
                database.lisaaKayttaja(username, password);
                res.redirect("/s/foorumi");
                return "";
            }
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
            map.put("aihealue", aihealueDao.findOneKuvauksella(req.params("kuvaus")));
            
            return new ModelAndView(map, "aihealue");
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

        

        get("/kayttajat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kayttajat", kayttajaDao.findAll());

            return new ModelAndView(map, "käyttäjät");
        }, new ThymeleafTemplateEngine());

    }
}
