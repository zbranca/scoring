package ro.zbranca.scoring.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class EncriptionTest {
	
		
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException{
		
		for (int i=0; i<10; i++) {

		String passToStore = PasswordHash.createHash("scr3am");
		System.out.println(passToStore);
		}
	}
}
