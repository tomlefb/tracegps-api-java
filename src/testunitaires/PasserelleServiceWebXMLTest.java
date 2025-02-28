package testunitaires;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;

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
		fail("Not yet implemented");
	}
	
	@Test
	public void testDemanderUneAutorisation() {
		fail("Not yet implemented");	
	}	
	
	@Test
	public void testRetirerUneAutorisation() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testEnvoyerPosition() throws ParseException {
		fail("Not yet implemented");
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
		String pseudo = "europa";
		String mdpSha1 = Outils.sha1("mdputilisateur");

		// Étape 1 : Créer un parcours fictif
		Trace laTrace = new Trace();
		String msg = PasserelleServicesWebXML.demarrerEnregistrementParcours(pseudo, mdpSha1, laTrace);

		System.out.println("Réponse API lors de la création du parcours : " + msg);
		assertEquals("Trace créée.", msg);

		int idTrace = laTrace.getId();
		assertTrue(idTrace > 0);

		// Étape 2 : Vérifier que la trace est bien en cours
		ArrayList<Trace> lesTraces = new ArrayList<>();
		msg = PasserelleServicesWebXML.getLesParcoursDunUtilisateur(pseudo, mdpSha1, pseudo, lesTraces);
		assertTrue(lesTraces.stream().anyMatch(trace -> trace.getId() == idTrace && !trace.getTerminee()));

		// Étape 3 : Arrêter l'enregistrement du parcours
		msg = PasserelleServicesWebXML.arreterEnregistrementParcours(pseudo, mdpSha1, idTrace);
		assertEquals("Enregistrement terminé.", msg);

		// 🚀 Attendre un peu pour laisser le serveur enregistrer la mise à jour
		Thread.sleep(1000);

		// Étape 4 : Vérifier que la trace est bien terminée
		lesTraces.clear();
		msg = PasserelleServicesWebXML.getLesParcoursDunUtilisateur(pseudo, mdpSha1, pseudo, lesTraces);

		// 🔍 Debugging : Affichage des traces après arrêt
		System.out.println("Traces après arrêt :");
		for (Trace t : lesTraces) {
			System.out.println("ID: " + t.getId() + ", Terminée: " + t.getTerminee());
		}

		assertTrue(lesTraces.stream().anyMatch(trace -> trace.getId() == idTrace && trace.getTerminee()));

		System.out.println("✅ Test terminé avec succès : la trace " + idTrace + " a bien été arrêtée.");
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
