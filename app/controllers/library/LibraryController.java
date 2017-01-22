package controllers.library;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import models.Library;
import models.LibraryBook;
import play.mvc.Controller;

public class LibraryController extends Controller {
	
	public static final List<String> LIBRARY_NAMES = new ArrayList<>();
	
	static {
		LIBRARY_NAMES.add("Petőfi Sándor");
		LIBRARY_NAMES.add("József Attila");
		LIBRARY_NAMES.add("Központi");
		LIBRARY_NAMES.add("Városi");
		LIBRARY_NAMES.add("Nemzeti");
		LIBRARY_NAMES.add("Ady Endre");
		LIBRARY_NAMES.add("Babits Miháy");
	}
	
	/**
	 * Logolás - log4j frameworkkel
	 * Configban:
	 * 
	 * 
	 	application.log=DEBUG
		application.log.path=/log4j.properties
		application.log.system.out=off
		
	 */
	private static final Logger LOGGER = Logger.getLogger(LibraryController.class);

	public static void libraryBooks(Long libraryId){
		List<Library> libraries;
		
		if (libraryId == null){
			libraries = Library.findAll();
		} else {
			//libraries = Library.find(" libraryId = ? ", libraryId).fetch();
			
			/*
			 * Elsődleges kulcsra a findById alapján keresünk
			 */
						
			libraries = new ArrayList<Library>(); 
			
			Library library = Library.findById(libraryId);
			if (library != null){ //ha nem null, akkor megtalálta. Ha nincs ilyen rekord, akkor null
				libraries.add(library);
			}
		}
		
		for (Library library : libraries){ //erre nincs szükségünk most, de írjuk csak ki, hogy mi van itt.
			LOGGER.debug("Library processing: " + library.libraryId + " - " + library.libraryName);
			LOGGER.debug("Library has " + library.books.size() + " books");
			for (LibraryBook book : library.books){
				if (!book.isRaktaron){
					LOGGER.debug("Library has " + book.ean + " book out of stock");
				}
			}
		}
		
		renderArgs.put("libraries",libraries);
		render("@Application.library.libraryBooks");
	}
	
	public static void addRandomLibrary(){
		Random random = new Random();
		//listából egy random elem elkérése index alapján
		//a randomnak a felső határa pedig a lista hossza
		String randomLibraryName = LIBRARY_NAMES.get(random.nextInt(LIBRARY_NAMES.size())); 
		
		
		Library library = Library.find(" libraryName = ? ", randomLibraryName).first();
		
		if (library == null){
			library = new Library();
			library.libraryName = randomLibraryName;
		}
		//1000 és 9999 között adhat vissza értéket
		library.libraryPostcode = random.nextInt(9000) + 1000;
		
		//a save egyszerre INSERT és UPDATE - tudja, hogy létezik-e az entitás a DB-ben vagy nem.
		library.save();
		
		libraryBooks(null);
	}

	public static void addRandomLibraryBook(){
		Random random = new Random();
		Library randomLibrary = selectRandomLibrary();

		if (randomLibrary != null){ //ha nincs könyvtár, nem csinálunk semmit. Normál esetben valamilyen hibát is kiírhatnánk, de most nem erre fókuszálunk
			LibraryBook libraryBook = new LibraryBook();
			libraryBook.owningLibrary = randomLibrary;
			libraryBook.ean = StringUtils.leftPad(new Integer(random.nextInt(9999999)).toString(), 13, '9'); //13 hosszú, balról 9es karakterrel kiegészített kamu-vonalkkód
			libraryBook.isRaktaron = random.nextInt(10) % 2 == 0; //ha páros a random, raktáron lesz
			libraryBook.pageNumber = random.nextInt(500) + 100; // 100 és 600 között generálunk
			libraryBook.title = RandomStringUtils.random(30, true, false);  //random string , semmi értelme nem lesz!
			libraryBook.author = RandomStringUtils.random(30, true, false); //random string, semmi értelme nem lesz!

			libraryBook.save();
		}

		libraryBooks(null); //átirányítás
	}

	public static void deleteRandomLibrary(){
		Library randomLibrary = selectRandomLibrary();

		if (randomLibrary != null){
			for (LibraryBook book : randomLibrary.books){
				book.delete();
			}
			randomLibrary.delete();
		}

		libraryBooks(null); //átirányítás
	}

	private static Library selectRandomLibrary(){
		List<Library> libraries = Library.findAll();
		Library library = null;
		if (libraries.size() > 0){
			Random random = new Random();
			library = libraries.get(random.nextInt(libraries.size())); //a random szám alapján válasszunk egy könyvtárat
		}
		return library;
	}

}