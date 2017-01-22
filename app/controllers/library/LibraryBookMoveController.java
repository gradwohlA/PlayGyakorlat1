package controllers.library;

import java.util.List;

import models.Library;
import models.LibraryBook;
import play.data.validation.Required;
import play.mvc.Controller;

public class LibraryBookMoveController extends Controller {
	
	private static String prepareData(Long libraryBookId){
		String errorMessage = null;
		LibraryBook libraryBook = null;
		if (libraryBookId != null){
			libraryBook = LibraryBook.findById(libraryBookId);
			if (libraryBook == null){
				errorMessage = "A törlendő könyv nem létezik!";
			}
		} else {
			errorMessage = "Üres könyvazonosító!";
		}
		
		if (errorMessage == null){
			List<Library> libraries = Library.find(" libraryId != ? ", libraryBook.owningLibrary.libraryId).fetch();
			renderArgs.put("libraries", libraries);
			renderArgs.put("book",libraryBook);
		}
		
		return errorMessage;
	}


	public static void moveLibraryBookForm(Long libraryBookId){
		String errorMessage = prepareData(libraryBookId);
		
		if (errorMessage != null){
			flash.put("errorMessage", errorMessage);
			LibraryController.libraryBooks(null);
		} else {
			render("@Application.library.moveLibraryBook");
		}	
	}
	
	public static void moveLibraryBook(@Required Long libraryBookId,
									   @Required(message = "A könyvtár választása kötelező!") Long libraryId 
									  ){
		
		String errorMessage = prepareData(libraryBookId);
		if (errorMessage != null){
			flash.put("errorMessage", errorMessage);
			LibraryController.libraryBooks(null);
		} else {
			
			Library library = Library.findById(libraryId);
			if (library == null){
				validation.addError("libraryId", "A kiválasztott könyvtár nem létezik!"); 
			}
			
			if (validation.hasErrors()){ //
				params.flash(); // add http parameters to the flash scope
				render("@Application.library.moveLibraryBook");
			} else {
				LibraryBook book = (LibraryBook) renderArgs.get("book");
				book.owningLibrary  = library;
				book.save();
				LibraryDetailsControllers.libraryDetails(library.libraryId);
			}
		}
	}
}