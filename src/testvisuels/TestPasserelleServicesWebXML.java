package testvisuels;

import java.text.ParseException;
import java.util.ArrayList;

import classes.Outils;
import classes.PasserelleServicesWebXML;
import classes.PointDeTrace;
import classes.Trace;
import classes.Utilisateur;

public class TestPasserelleServicesWebXML {

	public static void main(String[] args) throws ParseException {
		
		String msg;
	
		// test visuel de la méthode getTousLesUtilisateurs


		// test visuel de la méthode getLesUtilisateursQueJautorise


		// test visuel de la méthode getLesUtilisateursQuiMautorisent

		
//		// test visuel de la méthode getLesParcoursDunUtilisateur
//		ArrayList<Trace> lesTraces = new ArrayList<Trace>();
//		msg = PasserelleServicesWebXML.getLesParcoursDunUtilisateur("europa", Outils.sha1("mdputilisateur"), "callisto", lesTraces);
//		// Affichage de la réponse
//		System.out.println(msg);
//		// Affichage du nombre de traces
//		System.out.println("Nombre de traces : " + lesTraces.size());
//		// Affichage de toutes les traces
//		for (Trace uneTrace : lesTraces) {
//			System.out.println(uneTrace.toString());
//		}


		// Test visuel de la méthode getUnParcoursEtSesPoints
		System.out.println("=== Test getUnParcoursEtSesPoints ===");
		Trace laTrace = new Trace();
		msg = PasserelleServicesWebXML.getUnParcoursEtSesPoints("europa", Outils.sha1("mdputilisateur"), 4, laTrace);
		// Affichage de la réponse
		System.out.println("Réponse API : " + msg);
		// Affichage de la trace récupérée
		System.out.println("Détails de la trace :\n" + laTrace.toString());


	}// fin Main
} // fin class
