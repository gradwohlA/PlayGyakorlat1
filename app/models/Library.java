package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

/**
 * 
 * Vegyük észre ( tudom... :) ) --> 
 * 
 * a Java-s változónevek más konvenciókat követnek
 * Az osztály leszármazottja egy JPA-s osztálynak!
 * 
 * @author janoszsolt
 *
 */
@Entity //Entitásként megjelöljük
@Table(name = "library") //Tábla neve
public class Library extends GenericModel{

	@Id //elsődleges kulcs
	@GeneratedValue //autogenenrált
	@Column(name="library_id") //oszlophoz rendelés
	public Long libraryId;
	
	@Column(name="library_name")
	public String libraryName;
	
	@Column(name="library_postcode")
	public Integer libraryPostcode;
	
	@OneToMany(mappedBy="owningLibrary")
	public List<LibraryBook> books;
}
