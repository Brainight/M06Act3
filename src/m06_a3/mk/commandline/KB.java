package m06_a3.mk.commandline;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;


import javax.swing.JPasswordField;
import javax.swing.JOptionPane;

public class KB {

	static BufferedReader br = new BufferedReader(new  InputStreamReader(System.in));
	
	
	public static String askString(String text, boolean println){
		try {
			System.out.print(text + ((println) ? "\n" : ""));
			return br.readLine();
		}catch(IOException ioe) {
			handleIOException(ioe);
			return "";
		}	
	}
	
	public static char[] askPassword(String msg, boolean useAWT) {
		Console c = System.console();
		if(c != null) {
			System.out.print(msg);
			return c.readPassword();
		}
		if(useAWT){
			JPasswordField pf;
			int r = 0;
			do {
				 pf = new JPasswordField(); 
			 r = JOptionPane.showConfirmDialog( null, pf, msg,
			    JOptionPane.OK_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE ); 
			 	if(r != JOptionPane.OK_OPTION) {
			 		System.out.println("Debe introducir un password!!");
			 	}
			}while(r != JOptionPane.OK_OPTION);
			return pf.getPassword();
		}
		
		return askString(msg, false).toCharArray();
	}
	
	public static int askInt(String text, String error){
		boolean exit = false;
		int num = 0;
		while(!exit) {
			System.out.print(text);
			try {
				num = Integer.parseInt(br.readLine());
				exit = true;
			}catch(NumberFormatException nfe) {
				System.out.println(error);
				exit = false;
			}catch(IOException ioe) {
				exit = false;
				handleIOException(ioe);
			}
		}
		return num;
	}
	
	public static void pressEnter(){
		askString("\n\n--- Press Enter to Continue ---", true);
	}
	
	public static int askIntMinMax(int min, int max, String text, String error){
		int num = 0;
		do {
			 num = askInt(text, error);
			 if(num < min || num > max)
				 System.out.println(error);
			 
		}while(num < min || num > max);
		return num;
	}
	
	public static double askDouble(String text, String error){
		boolean exit = false;
		double num = 0;
		while(!exit) {
			System.out.print(text);
			try {
				num = Double.parseDouble(br.readLine());
				exit = true;
			}catch(NumberFormatException nfe) {
				System.out.println(error);
				exit = false;
			}catch(IOException ioe) {
				handleIOException(ioe);
			}
		}
		return num;
	}
	
	private static void handleIOException(IOException ioe) {
		System.out.println("FATAL ERROR: Exiting!");
		ioe.printStackTrace();
		System.exit(1);
	}
}
