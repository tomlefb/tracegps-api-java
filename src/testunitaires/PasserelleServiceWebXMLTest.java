package testunitaires;

import static org.junit.Assert.*;

import java.text.ParseException;
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
		String msg = PasserelleServicesWebXML.creerUnUtilisateur("jim", "email@gmail.com", "1122334455");
		assertEquals("Erreur : pseudo trop court (8 car minimum) ou déjà existant.", msg);

		msg = PasserelleServicesWebXML.creerUnUtilisateur("nouvelUser", "validemail@gmail.com", "1122334455");
		assertEquals("Enregistrement effectué ; vous allez recevoir un courriel avec votre mot de passe.", msg);
	}

	@Test
	public void testSupprimerUnUtilisateur() {
		String msg = PasserelleServicesWebXML.supprimerUnUtilisateur("admin", Outils.sha1("mdpadmin"), "turlututu");
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
		String msg = PasserelleServicesWebXML.demanderUneAutorisation("europa",
				Outils.sha1("mdputilisateurrrrrr"), "toto", "", "");
		assertEquals("Erreur : données incomplètes.", msg);
		msg = PasserelleServicesWebXML.demanderUneAutorisation("europa", Outils.sha1("mdputilisateurrrrrr"),
				"toto", "coucou", "charles-edouard");
		assertEquals("Erreur : authentification incorrecte.", msg);
		msg = PasserelleServicesWebXML.demanderUneAutorisation("europa", Outils.sha1("mdputilisateur"), "toto",
				"coucou", "charles-edouard");
		assertEquals("Erreur : pseudo utilisateur inexistant.", msg);
		msg = PasserelleServicesWebXML.demanderUneAutorisation("europa", Outils.sha1("mdputilisateur"),
				"galileo", "coucou", "charles-edouard");
		assertEquals("galileo va recevoir un courriel avec votre demande.", msg);
		msg = PasserelleServicesWebXML.demanderUneAutorisation("europa", Outils.sha1("mdputilisateur"),
				"galileo", "coucou", "charles-edouard");
		assertEquals("Erreur : l'envoi du courriel de demande d'autorisation a rencontré un problème.", msg);
	}

	@Test
	public void testRetirerUneAutorisation() {
		String msg = PasserelleServicesWebXML.retirerUneAutorisation("europa", Outils.sha1("mdputilisateurrrrrr"),
				"toto", "coucou");
		assertEquals("Erreur : authentification incorrecte.", msg);
		msg = PasserelleServicesWebXML.retirerUneAutorisation("europa", Outils.sha1("mdputilisateur"), "toto",
				"coucou");
		assertEquals("Erreur : pseudo utilisateur inexistant.", msg);
		msg = PasserelleServicesWebXML.retirerUneAutorisation("europa", Outils.sha1("mdputilisateur"), "juno",
				"coucou");
		assertEquals("Erreur : l'autorisation n'était pas accordée.", msg);
		msg = PasserelleServicesWebXML.retirerUneAutorisation("neon", Outils.sha1("mdputilisateur"), "oxygen",
				"coucou");
		assertEquals("Autorisation supprimée ; oxygen va recevoir un courriel de notification.", msg);
		msg = PasserelleServicesWebXML.retirerUneAutorisation("neon", Outils.sha1("mdputilisateur"), "photon", "");
		assertEquals("Autorisation supprimée.", msg);
	}

	@Test
	public void testEnvoyerPosition() throws ParseException {
		Date laDate = Outils.convertirEnDateHeure("24/01/2018 13:42:21");
		PointDeTrace lePoint = new PointDeTrace(23, 0, 48.15, -1.68, 50, laDate, 80);
		String msg = PasserelleServicesWebXML.envoyerPosition("europa", Outils.sha1("mdputilisateurrrrrr"), lePoint);
		assertEquals("Erreur : authentification incorrecte.", msg);
		lePoint = new PointDeTrace(2333, 0, 48.15, -1.68, 50, laDate, 80);
		msg = PasserelleServicesWebXML.envoyerPosition("europa", Outils.sha1("mdputilisateur"), lePoint);
		assertEquals("Erreur : le numéro de trace n'existe pas.", msg);
		lePoint = new PointDeTrace(22, 0, 48.15, -1.68, 50, laDate, 80);
		msg = PasserelleServicesWebXML.envoyerPosition("europa", Outils.sha1("mdputilisateur"), lePoint);
		assertEquals("Erreur : le numéro de trace ne correspond pas à cet utilisateur.", msg);
		lePoint = new PointDeTrace(4, 0, 48.15, -1.68, 50, laDate, 80);
		msg = PasserelleServicesWebXML.envoyerPosition("europa", Outils.sha1("mdputilisateur"), lePoint);
		assertEquals("Point enregistré.", msg);
	}

	@Test
	public void testDemarrerEnregistrementParcours() {
		fail("Not yet implemented");
	}

	@Test
	public void testArreterEnregistrementParcours() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testSupprimerUnUnParcours() {
		fail("Not yet implemented");
	}
	
} // fin du test
