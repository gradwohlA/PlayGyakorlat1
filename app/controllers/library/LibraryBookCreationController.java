
package controllers.library;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import models.Library;
import models.LibraryBook;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;
import util.ValidationUtil;

public class LibraryBookCreationController extends Controller{
	
	/**
	 * A Before annotáció segítségével lefuttathatunk egy bizonyos kódrészletet még azelőtt, hogy a controller lefutna
	 * Ez akkor jó, ha van olyan kód, ami mindkét esetben kell
	 */
	@Before
	public static void preparePage(){
		List<Library> libraries = Library.findAll();
		renderArgs.put("libraries", libraries);
	}
	
	public static void createLibraryBookForm(){
		List<Library> libraries = (List<Library>) renderArgs.get("libraries"); //ez biztos meglesz, hiszen a preparePage metódus @Before annotációval lefut a controllerünk előtt!
		if (libraries.size() == 0){
			flash.put("errorMessage", "Nincsenek könyvtárak!");
			LibraryController.libraryBooks(null);
		} else {
			render("@Application.library.createLibraryBook");
		}
	}
	
	/**
	 * Figyeljük az annotációkat - kötelező adatokat pofonegyszerűen tudunk ellenőrizni!
	 * @param libraryName
	 * @param libraryPostcode
	 */
	public static void createLibraryBook(	@Required(message = "A könyvtár választása kötelező!") Long libraryId, 
											@Required(message = "Az EAN kötelező!") String ean,
											@Required(message = "A cím kitöltése kötelező!") String author,
											@Required(message = "A szerző kitöltése kötelező!") String title,
											@Required(message = "Az oldalszám kitöltése kötelező!") Integer pageNumber,
											Boolean isRaktaron										
									){
		
		LibraryBookCreationValidator validator = new LibraryBookCreationValidator();
		boolean isValid=validator.isValidRequest(validation, libraryId, ean, pageNumber);
				
		if (isValid){ //
			params.flash(); // add http parameters to the flash scope
			render("@Application.library.createLibraryBook");
		} else {
			
			LibraryBook libraryBook = new LibraryBook();
			libraryBook.owningLibrary = Library.findById(libraryId);
			libraryBook.ean = ean;
			libraryBook.isRaktaron = isRaktaron==null?false:isRaktaron;
			libraryBook.pageNumber = pageNumber;
			libraryBook.title = title;
			libraryBook.author = author;
		
			libraryBook.save();
			
			LibraryDetailsControllers.libraryDetails(libraryId);
		}
		
	}
}