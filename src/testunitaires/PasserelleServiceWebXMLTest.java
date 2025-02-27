package testunitaires;

import static org.junit.Assert.*;

import java.text.ParseException;

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
