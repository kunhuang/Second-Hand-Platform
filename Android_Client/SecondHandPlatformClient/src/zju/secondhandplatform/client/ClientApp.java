package zju.secondhandplatform.client;

import android.app.Application;

public class ClientApp extends Application {

    private String email = "";
    private int id=-1;
    private String password = "";
    
    public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}  
	
	
	public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();    
    }   
}
