package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import play.mvc.Controller;

public class HomeWorkController extends Controller {
	public static void stuff(String first, String second)
	{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date = new Date();
		
		renderArgs.put("currentDate", dateFormat.format(date));
		renderArgs.put("bigString", first+second);
		
		render("@Application.homeWork");
		
	}
}
