package testunitaires;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import classes.Outils;
import classes.PasserelleServicesWebXML;
import classes.Point;
import classes.PointDeTrace;
import classes.Trace;
import classes.Utilisateur;

public class PasserelleServiceWebXMLTest {

	@Test
	public void testConnecter() {
		String msg = PasserelleServicesWebXML.connecter("admin", "adminnnnnnnn");
		assertEquals("Erreur : authentification incorrecte.", msg);

		msg = PasserelleServicesWebXML.connecter("admin", Outils.sha1("mdpadmin"));
		assertEquals("Administrateur authentifié.", msg);

		msg = PasserelleServicesWebXML.connecter("europa", Outils.sha1("mdputilisateur"));
		assertEquals("Utilisateur authentifié.", msg);
	}

	@Test
	public void testCreerUnUtilisateur() {
		String pseudoTest = "nouvelUserTest";
		String emailTest = "validemail@gmail.com";
		String telTest = "1122334455";

		// 1. Vérifier qu'un pseudo trop court ou existant ne fonctionne pas
		String msg = PasserelleServicesWebXML.creerUnUtilisateur("jim", "email@gmail.com", "1122334455");
		assertEquals("Erreur : pseudo trop court (8 car minimum) ou déjà existant.", msg);

		// 2. Créer un utilisateur
		msg = PasserelleServicesWebXML.creerUnUtilisateur(pseudoTest, emailTest, telTest);
		assertEquals("Enregistrement effectué ; vous allez recevoir un courriel avec votre mot de passe.", msg);

		// 3. Supprimer l'utilisateur juste après
		msg = PasserelleServicesWebXML.supprimerUnUtilisateur("admin", Outils.sha1("mdpadmin"), pseudoTest);
		assertEquals("Suppression effectuée ; un courriel va être envoyé à l'utilisateur.", msg);
	}

	@Test
	public void testSupprimerUnUtilisateur() {
		String pseudoTest = "utilisateurTest";
		String emailTest = "utilisateurTest@gmail.com";
		String telTest = "0606060606";

		// 1. Construire l'URL manuellement et afficher
		String urlCreation = "http://127.0.0.1/ws-php-dp/TraceGPS/src/api/CreerUnUtilisateur?pseudo=" + pseudoTest + "&adrMail=" + emailTest + "&numTel=" + telTest;
		System.out.println("URL Création : " + urlCreation);

		// 2. Essayer de créer l'utilisateur
		String msg = PasserelleServicesWebXML.creerUnUtilisateur(pseudoTest, emailTest, telTest);
		System.out.println("Réponse API Création : " + msg);
		assertEquals("Enregistrement effectué ; vous allez recevoir un courriel avec votre mot de passe.", msg);

		// 3. Supprimer l'utilisateur
		String urlSuppression = "http://127.0.0.1/ws-php-dp/TraceGPS/src/api/SupprimerUnUtilisateur?pseudo=admin&mdp=" + Outils.sha1("mdpadmin") + "&pseudoAsupprimer=" + pseudoTest;
		System.out.println("URL Suppression : " + urlSuppression);

		msg = PasserelleServicesWebXML.supprimerUnUtilisateur("admin", Outils.sha1("mdpadmin"), pseudoTest);
		System.out.println("Réponse API Suppression : " + msg);
		assertEquals("Suppression effectuée ; un courriel va être envoyé à l'utilisateur.", msg);
	}

	@Test
	public void testChangerDeMdp() {
		String msg = PasserelleServicesWebXML.changerDeMdp("europa", Outils.sha1("mdputilisateur"), "passepasse", "passepassepasse");
		assertEquals("Erreur : le nouveau mot de passe et sa confirmation sont différents.", msg);

		msg = PasserelleServicesWebXML.changerDeMdp("europa", Outils.sha1("mdputilisateur"), "mdputilisateur", "mdputilisateur");
		assertEquals("Enregistrement effectué ; vous allez recevoir un courriel de confirmation.", msg);
	}

	@Test
	public void testDemanderMdp() {
		// Cas où le pseudo n'existe pas
		String msg = PasserelleServicesWebXML.demanderMdp("jim");
		assertEquals("Erreur : pseudo inexistant.", msg);

		// Cas où le pseudo existe
		msg = PasserelleServicesWebXML.demanderMdp("helios");
		assertEquals("Vous allez recevoir un courriel avec votre nouveau mot de passe.", msg);

	}

	@Test
	public void testDemanderUneAutorisation() {
		String msg = PasserelleServicesWebXML.demanderUneAutorisation("europa", Outils.sha1("mdputilisateurrrrrr"), "luna", "", "");
		assertEquals("Erreur : données incomplètes.", msg);

		msg = PasserelleServicesWebXML.demanderUneAutorisation("europa", Outils.sha1("mdputilisateurrrrrr"), "luna", "coucou", "charles-edouard");
		assertEquals("Erreur : authentification incorrecte.", msg);

		msg = PasserelleServicesWebXML.demanderUneAutorisation("europa", Outils.sha1("mdputilisateur"), "totototototo", "coucou", "charles-edouard");
		assertEquals("Erreur : pseudo utilisateur inexistant.", msg);

		// Avant de créer l'autorisation, on retire si elle existe déjà
		// Retirer l'autorisation si elle existe pour "galileo"
		msg = PasserelleServicesWebXML.retirerUneAutorisation("europa", Outils.sha1("mdputilisateur"), "galileo", "coucou");
		assertTrue(msg.contains("Autorisation supprimée") || msg.contains("Erreur : l'autorisation n'était pas accordée."));

		// Maintenant on crée l'autorisation
		msg = PasserelleServicesWebXML.demanderUneAutorisation("europa", Outils.sha1("mdputilisateur"), "galileo", "coucou", "charles-edouard");
		assertEquals("galileo va recevoir un courriel avec votre demande.", msg);
	}


	@Test
	public void testRetirerUneAutorisation() {
		String msg = PasserelleServicesWebXML.retirerUneAutorisation("europa", Outils.sha1("mdputilisateurrrrrr"), "toto", "coucou");
		assertEquals("Erreur : authentification incorrecte.", msg);

		msg = PasserelleServicesWebXML.retirerUneAutorisation("europa", Outils.sha1("mdputilisateur"), "toutoutoutou", "coucou");
		assertEquals("Erreur : pseudo utilisateur inexistant.", msg);

		msg = PasserelleServicesWebXML.retirerUneAutorisation("europa", Outils.sha1("mdputilisateur"), "juno", "coucou");
		assertEquals("Erreur : l'autorisation n'était pas accordée.", msg);

		msg = PasserelleServicesWebXML.demanderUneAutorisation("neon", Outils.sha1("mdputilisateur"), "oxygen", "coucou", "test");
		// Vérifie que l'autorisation a bien été ajoutée
		assertEquals("oxygen va recevoir un courriel avec votre demande.", msg);

		msg = PasserelleServicesWebXML.retirerUneAutorisation("neon", Outils.sha1("mdputilisateur"), "oxygen", "coucou");
		assertEquals("Autorisation supprimée ; oxygen va recevoir un courriel de notification.", msg);

	}


	@Test
	public void testEnvoyerPosition() throws ParseException {
		Date date = Outils.convertirEnDateHeure("24/01/2018 13:42:21");

		PointDeTrace pt = new PointDeTrace(23, 0, 48.15, -1.68, 50, date, 80);
		String msg = PasserelleServicesWebXML.envoyerPosition("europa", Outils.sha1("mdputilisateurrrrrr"), pt);
		assertEquals("Erreur : authentification incorrecte.", msg);

		pt = new PointDeTrace(2333, 0, 48.15, -1.68, 50, date, 80);
		msg = PasserelleServicesWebXML.envoyerPosition("europa", Outils.sha1("mdputilisateur"), pt);
		assertEquals("Erreur : le numéro de trace n'existe pas.", msg);

		pt = new PointDeTrace(22, 0, 48.15, -1.68, 50, date, 80);
		msg = PasserelleServicesWebXML.envoyerPosition("europa", Outils.sha1("mdputilisateur"), pt);
		assertEquals("Erreur : le numéro de trace ne correspond pas à cet utilisateur.", msg);

		pt = new PointDeTrace(4, 0, 48.15, -1.68, 50, date, 80);
		msg = PasserelleServicesWebXML.envoyerPosition("europa", Outils.sha1("mdputilisateur"), pt);
		assertEquals("Point enregistré.", msg);
	}


	@Test
	public void testDemarrerEnregistrementParcours() {
		String pseudo = "europa";
		String mdpSha1 = Outils.sha1("mdputilisateur");

		// Cas 1 : Mauvais mot de passe
		Trace laTrace = new Trace();
		String msg = PasserelleServicesWebXML.demarrerEnregistrementParcours(pseudo, Outils.sha1("mauvaismdp"), laTrace);
		assertEquals("Erreur : authentification incorrecte.", msg);

		// Cas 2 : Bon mot de passe, vérification de la création
		laTrace = new Trace();
		msg = PasserelleServicesWebXML.demarrerEnregistrementParcours(pseudo, mdpSha1, laTrace);

		// 🔍 Debugging : Vérifions ce que l'API retourne vraiment
		System.out.println("Réponse API lors de la création du parcours : " + msg);

		// Comparaison avec la valeur attendue
		assertEquals("Trace créée.", msg);

		// Vérification que la trace a bien un ID
		int idTrace = laTrace.getId();
		assertTrue(idTrace > 0);

		// 🔍 Debugging : Vérifions l'ID récupéré
		System.out.println("ID de la nouvelle trace : " + idTrace);
	}


	@Test
	public void testArreterEnregistrementParcours() throws InterruptedException {
		String msg;

		// Test : Authentification incorrecte
		msg = PasserelleServicesWebXML.arreterEnregistrementParcours("europa", Outils.sha1("mauvaismdp"), 23);
		assertEquals("Erreur : authentification incorrecte.", msg);

		// Test : Parcours inexistant
		msg = PasserelleServicesWebXML.arreterEnregistrementParcours("europa", Outils.sha1("mdputilisateur"), 230);
		assertEquals("Erreur : parcours inexistant.", msg);

		// Test : Le numéro de trace ne correspond pas à cet utilisateur
		msg = PasserelleServicesWebXML.arreterEnregistrementParcours("europa", Outils.sha1("mdputilisateur"), 5);
		assertEquals("Erreur : le numéro de trace ne correspond pas à cet utilisateur.", msg);

		// Test : Trace déjà terminée
		int traceId = 4; // ID de la trace qui est déjà terminée
		msg = PasserelleServicesWebXML.arreterEnregistrementParcours("europa", Outils.sha1("mdputilisateur"), traceId);
		assertEquals("Enregistrement terminé.", msg);

		// Test : Cas de réussite, création et terminaison d'une nouvelle trace
		Trace nouvelleTrace = new Trace();

		// Démarrer l'enregistrement
		msg = PasserelleServicesWebXML.demarrerEnregistrementParcours("europa", Outils.sha1("mdputilisateur"), nouvelleTrace);
		assertEquals("Trace créée.", msg);

		// Vérifier que l'ID de trace a bien été généré
		assertTrue(nouvelleTrace.getId() > 0);

		// Ajout d'un petit délai pour s'assurer que le serveur a bien traité la création
		Thread.sleep(1000);

		// Terminer l'enregistrement de la trace
		msg = PasserelleServicesWebXML.arreterEnregistrementParcours("europa", Outils.sha1("mdputilisateur"), nouvelleTrace.getId());

		// Debug : Afficher le message si le test échoue
		System.out.println("Message reçu lors de l'arrêt de la trace : " + msg);

		assertEquals("Enregistrement terminé.", msg);
	}







	@Test
	public void testSupprimerUnParcours() {
		String pseudo = "europa";
		String mdpSha1 = Outils.sha1("mdputilisateur");

		// Étape 1 : Créer un parcours fictif
		Trace nouvelleTrace = new Trace();
		String msg = PasserelleServicesWebXML.demarrerEnregistrementParcours(pseudo, mdpSha1, nouvelleTrace);

		// 🔍 Debugging : Vérifions ce que l'API retourne vraiment
		System.out.println("Réponse API lors de la création du parcours : " + msg);

		System.out.println("🔍 Réponse brute de l'API : '" + msg + "'");

		// Comparaison avec la valeur attendue
		assertEquals("Trace créée.", msg);

		// Vérification que la trace a bien un ID
		int idTrace = nouvelleTrace.getId();
		assertTrue(idTrace > 0);

		// Étape 2 : Vérifier que le parcours existe bien avant suppression
		ArrayList<Trace> lesTraces = new ArrayList<>();
		msg = PasserelleServicesWebXML.getLesParcoursDunUtilisateur(pseudo, mdpSha1, pseudo, lesTraces);
		assertTrue(lesTraces.stream().anyMatch(trace -> trace.getId() == idTrace));

		// Étape 3 : Supprimer le parcours
		msg = PasserelleServicesWebXML.supprimerUnParcours(pseudo, mdpSha1, idTrace);
		assertEquals("Parcours supprimé.", msg);

		// Étape 4 : Vérifier que le parcours n'existe plus
		lesTraces.clear();
		msg = PasserelleServicesWebXML.getLesParcoursDunUtilisateur(pseudo, mdpSha1, pseudo, lesTraces);
		assertFalse(lesTraces.stream().anyMatch(trace -> trace.getId() == idTrace));
	}

	
}
