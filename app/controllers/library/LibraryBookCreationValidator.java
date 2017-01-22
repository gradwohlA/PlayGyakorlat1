package controllers.library;

import models.Library;
import play.data.validation.Validation;
import util.ValidationUtil;

/**
 * A custom validációkat itt írjuk meg, így a controller beli kód csak egy hívást fog csak tartalmazni.
 * Ezáltal olvasható, lényegretörő kódot kapunk, amit könnyebb tesztelni, több, kisebb egységből áll.
 * 
 * @author janoszsolt
 *
 */
public class LibraryBookCreationValidator {

	public boolean isValidRequest(Validation validation, Long libraryId, String ean, Integer pageNumber){
		
		if (ValidationUtil.isNotEmpty(ean)){
			if (!ean.startsWith("978")){
				validation.addError("ean", "Az EAN könyvek esetén 978-cal kezdődik!");
			}
			
			if (ean.length() != 13){
				validation.addError("ean", "Az EAN mindig 13 karakter hosszú!"); 
			}
		} 
		
		if (pageNumber != null && pageNumber <= 0){
			validation.addError("pageNumber", "Az oldaszám mindig pozitív!"); 
		}
		
		Library library = Library.findById(libraryId);
		if (library == null){
			validation.addError("libraryId", "A kiválasztott könyvtár nem létezik!"); 
		}
		
		
		return validation.hasErrors();
	}
}