// Projet TraceGPS - API Java
// Fichier : Passerelle.java
// Cette classe abstraite fournit les outils permettant d'obtenir un document XML à partir d'un fichier ou d'un service web
// Dernière mise à jour : 26/3/2018 par Jim

package classes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public abstract class PasserelleXML {

    // méthode protégée statique pour obtenir un flux en lecture (java.io.InputStream)
    // à partir de l'adresse d'un fichier ou de l'URL d'un service web
	protected static InputStream getFluxEnLecture(String payload) {
		InputStream unFluxEnLecture = null;
		try {
			if (payload.startsWith("http")) {
				HttpURLConnection urlConnection = (HttpURLConnection) new URL(payload).openConnection();
				int code = urlConnection.getResponseCode();

				System.out.println("HTTP Code : " + code); // Debug

				if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED) { // 🔥 Accepter aussi 201
					unFluxEnLecture = urlConnection.getInputStream();

					// Ajout d'un affichage du contenu du flux
					java.util.Scanner s = new java.util.Scanner(unFluxEnLecture).useDelimiter("\\A");
					String response = s.hasNext() ? s.next() : "";
					System.out.println("Réponse API : \n" + response);
					s.close();

					// On recrée un flux car `Scanner` consomme l'original
					unFluxEnLecture = new java.io.ByteArrayInputStream(response.getBytes("UTF-8"));
				} else {
					unFluxEnLecture = urlConnection.getErrorStream();
				}
			} else {
				unFluxEnLecture = new FileInputStream(new File(payload));
			}
			return unFluxEnLecture;
		} catch (Exception ex) {
			System.out.println("Erreur dans getFluxEnLecture : " + ex.getMessage());
			return null;
		}
	}

    // méthode protégée statique pour obtenir document XML (org.w3c.dom.Document)
    // à partir d'un flux de données en lecture (java.io.InputStream)
	protected static Document getDocumentXML(InputStream unFluxEnLecture) {
		try {
			if (unFluxEnLecture == null) {
				System.out.println("Erreur : le flux XML est null !");
				return null;
			}

			// Création d'une instance de DocumentBuilderFactory et DocumentBuilder
			DocumentBuilderFactory leDBF = DocumentBuilderFactory.newInstance();
			DocumentBuilder leDB = leDBF.newDocumentBuilder();

			// On crée un nouveau document XML avec en argument le flux XML
			Document leDocument = leDB.parse(unFluxEnLecture);

			System.out.println("Document XML bien analysé !");
			return leDocument;
		} catch (Exception ex) {
			System.out.println("Erreur dans getDocumentXML : " + ex.getMessage());
			return null;
		}
	}

}
