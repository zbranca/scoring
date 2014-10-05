package ro.zbranca.scoring;

import ro.zbranca.scoring.data.DatabaseHelper;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class LoginView extends Panel implements View {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "login";	
	final TextField email = new TextField("Email");
	final PasswordField password = new PasswordField("Password");

    public LoginView(final Navigator navigator,
            final String fragmentAndParameters) {
    	
    	VerticalLayout layout = new VerticalLayout();
    	layout.setMargin(true);
    	layout.setSpacing(true);
    	
    	setCaption("<div align=\"left\" style=\"font-size:20pt;\">"
    	+ FontAwesome.KEY.getHtml()
    	+ " &nbsp; Scoring application login</div>");

        layout.addComponent(email);
        email.addValidator(new EmailValidator("Please give a valid email adress"));
        email.setImmediate(true);

        layout.addComponent(password);

        final Button login = new Button("Login", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				
				if (checkPassword()){
                Notification.show("Welcome " + email.getValue());

                // indicate the user is logged in
                ((ScoringUI)UI.getCurrent()).setLoggedInUser(email.getValue());

                // navigate back to the main window
                navigator.navigateTo("/");
                
				} else {
					Notification.show("Login failed. Please try again.");
					email.setRequired(true);	
					password.setRequired(true);
					email.focus();
				}
            }
        });
        
        layout.addComponent(login);
        setContent(layout);
        email.focus();
    }
    
    private boolean checkPassword(){
    	boolean loginSucces = false;
    	String emailProvided = (String) email.getValue();
    	String passwordProvided = (String) password.getValue();
    	
    	SQLContainer users = DatabaseHelper.getFreeFormQueryContainer("SELECT * FROM personal WHERE email = '" + 
    			emailProvided + "' AND password = '" + passwordProvided + "'");
    	
    	Integer user = users.size();

    	if(user > 0) { loginSucces = true;}
    	return loginSucces;
    }
   
    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub
        
    }
}