package controllers.library;

import models.Library;
import play.mvc.Controller;

public class LibraryDetailsControllers extends Controller{

	public static void libraryDetails(Long libraryId){
		Library library = null;
		
		if (libraryId != null){
			library = Library.findById(libraryId);
		}
		
		if (library == null){
			LibraryController.libraryBooks(null);
		} else {
			renderArgs.put("library", library);
			render("@Application.library.libraryDetails");
		}
	}
}