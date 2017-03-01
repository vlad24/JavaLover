package builderexamples;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Here we have an example of Builder pattern
 * We have some mandatory fields like login passwordHash, and not mandatory - age, email
 * The object can be constructed only using Builder class
 * 	For that we have a private constructor of User and static Builder with public constructor
 */

public class User {
	private String login;
	private String passwordHash;
	private String email;
	private int age;
	
	public static class UserBuilder{
		private final String login;
		private String passwordHash;
		private String email;
		private int age;
		
		public UserBuilder(String login, String password){
			this.login = login;
			MessageDigest md5Master = null;
			try {
				md5Master = MessageDigest.getInstance("MD5");
				this.passwordHash = new String(md5Master.digest(password.getBytes("UTF-8")), "UTF-8");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
		}
		
		public UserBuilder email(String email){
			this.email = email;
			return this;
		}
		
		public UserBuilder age(final int age){
			this.age = age;
			return this;
		}
		
		public User toUser(){
			//Some logic may be here!
			return new User(this.login, this.passwordHash, this.email, this.age);
		}
	}
	
	
	private User(String login, String passwordHash, String email, int age){
		this.login = login;
		this.passwordHash = passwordHash;
		this.email = email;
		this.age = age;
	}
	
	public String getLogin() {
		return login;
	}
	
	public String getPasswordHash() {
		return passwordHash;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
}
