package controllers;

import java.util.Random;

import play.mvc.Controller;

public class MyFirstController extends Controller{
	
		public static void controllerExercise(){
			Random r=new Random();
			renderArgs.put("ertekem",r.nextInt(100));
			render("@Application.dummyPage1");
		}
		
		public static void add(Integer a, Integer b){
			renderArgs.put("ertekem",a+b);
			render("@Application.dummyPage1");
		}
}
