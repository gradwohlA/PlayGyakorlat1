package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity //Entitásként megjelöljük
@Table(name = "library_book") //Tábla neve
public class LibraryBook extends GenericModel{
	
	@Id
	@GeneratedValue
	@Column(name="library_book_id")
	public Long libraryBookId;
	
	@Column //itt nem is kell más, mert a Java-s változó neve megegyik az oszlopnévvel
	public String ean;
	
	@ManyToOne
	@JoinColumn(name = "library_id",referencedColumnName="library_id")
	public Library owningLibrary;
	
	@Column //itt nem is kell más, mert a Java-s változó neve megegyik az oszlopnévvel
	public String title;
	
	@Column //itt nem is kell más, mert a Java-s változó neve megegyik az oszlopnévvel
	public String author;
	
	@Column(name = "page_number")
	public Integer pageNumber;
	
	@Column(name = "is_Raktaron")
	public boolean isRaktaron;
	

}