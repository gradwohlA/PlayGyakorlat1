package controllers.library;

import models.Library;
import play.data.validation.Required;
import play.mvc.Controller;
import util.ValidationUtil;

public class LibraryCreationController extends Controller {
	
	public static void createLibraryForm(){
		render("@Application.library.createLibrary");
	}
	
	/**
	 * Figyeljük az annotációkat - kötelező adatokat pofonegyszerűen tudunk ellenőrizni!
	 * @param libraryName
	 * @param libraryPostcode
	 */
	public static void createLibrary(@Required(message = "A név kötelező!") String libraryName, @Required(message = "Az irányítószám kötelező!")String libraryPostcode){
		
		Integer postCode = null;
		if (!ValidationUtil.isInteger(libraryPostcode)){ 
			validation.addError("libraryPostcode", "Az írányítószám nem lehet szöveges adat!"); //így adunk hozzá custom validációt
		} else {
			postCode = Integer.valueOf(libraryPostcode);
			
			if (postCode >= 10000 || postCode < 1000){
				validation.addError("libraryPostcode", "Az irányítószám 1000 és 9999 közötti szám lehet!"); 
			}
		}
		
		Library library = Library.find(" libraryName = ?", libraryName).first();
		if (library != null){
			validation.addError("libraryName", "Ilyen néven már létezik könyvtár!");
		}
				
		if (validation.hasErrors()){ //
			params.flash(); // add http parameters to the flash scope
			render("@Application.library.createLibrary");
		} else {
			
		
			library = new Library();
			library.libraryName = libraryName;
			library.libraryPostcode = postCode;
			library.save();
			
			LibraryController.libraryBooks(null);
		}
		
	}

}