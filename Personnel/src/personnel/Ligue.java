package personnel;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Représente une ligue. Chaque ligue est reliée à une liste
 * d'employés dont un administrateur. Comme il n'est pas possible
 * de créer un employé sans l'affecter à une ligue, le root est 
 * l'administrateur de la ligue jusqu'à ce qu'un administrateur 
 * lui ait été affecté avec la fonction {@link #setAdministrateur}.
 */

public class Ligue implements Serializable, Comparable<Ligue>
{
	private static final long serialVersionUID = 1L;
	private int id = -1;
	private String nom;
	private SortedSet<Employe> employes;
	private Employe administrateur;
	private GestionPersonnel gestionPersonnel;
	public int getId;
	
	/**
	 * Crée une ligue.
	 * @param nom le nom de la ligue.
	 */
	
	Ligue(GestionPersonnel gestionPersonnel, String nom) throws SauvegardeImpossible
	{
		this(gestionPersonnel, -1, nom);
		this.id = gestionPersonnel.insert(this); 
	}

	Ligue(GestionPersonnel gestionPersonnel, int id, String nom)
	{
		this.nom = nom;
		employes = new TreeSet<>();
		this.gestionPersonnel = gestionPersonnel;
		administrateur = gestionPersonnel.getRoot();
		this.id = id;
	}

	/**
	 * Retourne le nom de la ligue.
	 * @return le nom de la ligue.
	 */

	public String getNom()
	{
		return nom;
	}

	/**
	 * Change le nom.
	 * @param nom le nouveau nom de la ligue.
	 * @throws SauvegardeImpossible 
	 */

	public void setNom(String nom) throws SauvegardeImpossible
	{
		this.nom = nom;
		gestionPersonnel.update(this);
	}

	/**
	 * Retourne l'administrateur de la ligue.
	 * @return l'administrateur de la ligue.
	 */
	
	public Employe getAdministrateur()
	{
		return administrateur;
	}

	public int getId() {
		return this.id;
	}
	
	/**
	 * Fait de administrateur l'administrateur de la ligue.
	 * Lève DroitsInsuffisants si l'administrateur n'est pas 
	 * un employé de la ligue ou le root. Révoque les droits de l'ancien 
	 * administrateur.
	 * @param administrateur le nouvel administrateur de la ligue.
	 */
	
	public void setAdministrateur(Employe administrateur)
	{
		Employe root = gestionPersonnel.getRoot();
		if (administrateur != root && administrateur.getLigue() != this)
			throw new DroitsInsuffisants();
		this.administrateur = administrateur;
	}

	/**
	 * Retourne les employés de la ligue.
	 * @return les employés de la ligue dans l'ordre alphabétique.
	 */
	
	public SortedSet<Employe> getEmployes()
	{
		return Collections.unmodifiableSortedSet(employes);
	}

	/**
	 * Ajoute un employé dans la ligue. Cette méthode 
	 * est le seul moyen de créer un employé.
	 * @param nom le nom de l'employé.
	 * @param prenom le prénom de l'employé.
	 * @param mail l'adresse mail de l'employé.
	 * @param password le password de l'employé.
	 * @param Depart 
	 * @param Arrivee 
	 * @param id2 
	 * @return l'employé créé. 
	 */

	public Employe addEmploye(String nom, String prenom, String mail, String password, LocalDate Arrivee, LocalDate Depart, int id2) {
	    Employe employe = new Employe(this.gestionPersonnel, this, nom, prenom, mail, password, Depart, Arrivee);
	    employes.add(employe);
	    try {
			gestionPersonnel.insert(employe);
			
		} catch (SauvegardeImpossible e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Insérer l'employé dans la base de données
	    return employe;
	}
	
	public Employe addEmploye(String nom, String prenom, String mail, String password, LocalDate date_arrivee,LocalDate date_depart) {
		Employe employe = new Employe(this.gestionPersonnel, this, nom, prenom, mail, password, date_arrivee != null ? date_arrivee : LocalDate.now(), date_depart != null ? date_depart : LocalDate.now());
        employes.add(employe);
        return employe;
	}	
	public void remove(Employe employe)
	{
		employes.remove(employe);
		
		
	}
	
	/**
	 * Supprime la ligue, entraîne la suppression de tous les employés
	 * de la ligue.
	 */
	

	@Override
	public int compareTo(Ligue autre)
	{
		return getNom().compareTo(autre.getNom());
	}
	public void remove()
	{
		try {
			gestionPersonnel.remove(this);
		} catch (SauvegardeImpossible e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void removeAdmin()
	{
		administrateur = gestionPersonnel.getRoot();
	}
	public void update() throws SQLException
	{
		try {
			gestionPersonnel.update(this);
		} catch (SauvegardeImpossible e) {
			
			e.printStackTrace();
		}
	}
	@Override
	public String toString()
	{
		return nom;
	}	
	
	
}
