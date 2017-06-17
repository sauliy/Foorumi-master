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
import tikape.runko.database.ViestiDao;
import tikape.runko.database.ViestiketjuDao;
import tikape.runko.domain.Aihealue;
import tikape.runko.domain.Kayttaja;
import tikape.runko.domain.Viestiketju;

public class Main {

    public static void main(String[] args) throws Exception {

         // asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }

        // käytetään oletuksena paikallista sqlite-tietokantaa
        String jdbcOsoite = "jdbc:sqlite:foorumitesti10.db";
        // jos heroku antaa käyttöömme tietokantaosoitteen, otetaan se käyttöön
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        }

        Database database = new Database(jdbcOsoite);


        database.init();

        AihealueDao aihealueDao = new AihealueDao(database);
        KayttajaDao kayttajaDao = new KayttajaDao(database);
        ViestiketjuDao viestiketjuDao = new ViestiketjuDao(database);
        ViestiDao viestiDao = new ViestiDao(database);

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
            } req.session(true).attribute("user", kayttaja);

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

            return new ModelAndView(map, "Aihealueet");
        }, new ThymeleafTemplateEngine());

        post("/s/foorumi/aihealueenlisays", (req, res) -> {
            String aihealueAihe = req.queryParams("aihe");

            Session sess = req.session();
            if (sess.attribute("user").toString().equals("Sauli")) {
                database.lisaaAihealue(aihealueAihe);
            }
            res.redirect("/s/foorumi/aihealueet");
            return "";
        });

        post("/s/foorumi/aihealueenpoisto", (req, res) -> {
            String aihealueAihe = req.queryParams("aihe");

            Session sess = req.session();
            if (sess.attribute("user").toString().equals("Sauli")) {
                database.poistaAihealue(aihealueAihe);

            }
            res.redirect("/s/foorumi/aihealueet");
            return "";
        });

        post("/s/foorumi/viestienpoisto", (req, res) -> {
            String aihealueAihe = req.queryParams("aihe");

            Session sess = req.session();
            if (sess.attribute("user").toString().equals("Sauli")) {
                database.poistaViestit();

            }
            res.redirect("/s/foorumi/aihealueet");
            return "";
        });

        get("/s/foorumi/salaisuus", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "salaisuus");
        }, new ThymeleafTemplateEngine());

        get("/s/foorumi/:kuvaus", (req, res) -> {
            HashMap map = new HashMap<>();
            Aihealue aihealue = aihealueDao.findOneKuvauksella(req.params("kuvaus"));
            Integer aihealueId = aihealue.getId();

            req.session(true).attribute("aihealueTunnus", aihealueId);
            req.session(true).attribute("aihealueKuvaus", aihealue.getKuvaus());


            map.put("aihealue", aihealue);
            map.put("viestiketjut", viestiketjuDao.findAllAihealueesta(aihealueId));

            return new ModelAndView(map, "Aihealue");
        }, new ThymeleafTemplateEngine());

        get("/s/foorumi/:kuvaus/:id", (req,res) -> {
            Session sess = req.session();
            HashMap map = new HashMap<>();
            map.put("kuvaus", aihealueDao.findOneKuvauksella(req.params("kuvaus")));
            map.put("viestiketju", viestiketjuDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("viestit", viestiDao.findAllViestiketjusta(Integer.parseInt(req.params("id"))));
            map.put("kayttaja", sess.attribute("user"));

            Viestiketju viestiketju = (Viestiketju) map.get("viestiketju");


            req.session(true).attribute("viestiketjuId", viestiketju.getId());
            req.session(true).attribute("kuvaus", aihealueDao.findOneKuvauksella(req.params("kuvaus")).getKuvaus());


            return new ModelAndView(map, "Viestiketju");
        }, new ThymeleafTemplateEngine());


        get("/s/kayttajat/:nimi", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kayttaja", kayttajaDao.findOneNimimerkilla((req.params("nimi"))));

            return new ModelAndView(map, "kayttaja");
        }, new ThymeleafTemplateEngine());

        post("/logout", (req, res) -> {
            req.session().invalidate();
            res.redirect("/");
            return "";
        });

        post("/s/foorumi/viestiketjunlisays", (req, res) -> {
            String viestiketjuAihe = req.queryParams("aihe");

            Session sess = req.session();
            Integer aihealueTunnus = sess.attribute("aihealueTunnus");
            String aihealueKuvaus = sess.attribute("aihealueKuvaus");

            if (viestiketjuAihe.length() < 101) {
                database.lisaaViestiketju(aihealueTunnus, viestiketjuAihe);
            }

            res.redirect("/s/foorumi/" + aihealueKuvaus);
            return "";
        });

        post("/s/foorumi/viestinlisays", (req,res) -> {
            String sisalto = req.queryParams("sisalto");

            Session sess = req.session();
            Integer viestiketjuId = sess.attribute("viestiketjuId");
            Kayttaja kayttaja = sess.attribute("user");
            Integer kayttajaId = kayttaja.getId();
            String nimimerkki = kayttaja.getNimi();

            if (sisalto.length() < 301) {
                database.lisaaViesti(kayttajaId,sisalto,viestiketjuId, nimimerkki);
            }

            res.redirect("/s/foorumi/" + sess.attribute("kuvaus")+"/"+viestiketjuId);
            return "";
        });


        get("/kayttajat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kayttajat", kayttajaDao.findAll());

            return new ModelAndView(map, "kayttajat");
        }, new ThymeleafTemplateEngine());

    }
}
