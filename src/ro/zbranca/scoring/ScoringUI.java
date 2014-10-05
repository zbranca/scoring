package ro.zbranca.scoring;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@PreserveOnRefresh
@SuppressWarnings("serial")
@Theme("scoring")
public class ScoringUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = ScoringUI.class)
	public static class Servlet extends VaadinServlet {
	}
	
	Navigator navigator;
	String loggedInUser;

	@Override
	protected void init(VaadinRequest request) {
		 // Create Navigator, make it control the ViewDisplay
        navigator = new Navigator(this, this);

        // Add some Views
        // no fragment for main view
        navigator.addView(MainView.NAME, new MainView(navigator));

        // #count will be a new instance each time we navigate to it, counts:
        navigator.addView(PersonalView.NAME, PersonalView.class);
        
        // #message adds a label with whatever it receives as a parameter
        navigator.addView(MessageView.NAME, new MessageView());

        // #secret works as #message, but you need to be logged in
        navigator.addView(SecretView.NAME, new SecretView());
        
        // #login will navigate to the main view if invoked via this mechanism
        navigator.addView(LoginView.NAME, new LoginView(navigator,
                MainView.NAME));

        // we'll handle permissions with a listener here, you could also do
        // that in the View itself.
        navigator.addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
            	if (((ScoringUI)UI.getCurrent()).getLoggedInUser() == null) {
            		// Show to LoginView instead, pass intended view
            		String fragmentAndParameters = event.getViewName();
            		if (event.getParameters() != null) {
            			fragmentAndParameters += "/";
            			fragmentAndParameters += event.getParameters();
            		}
            		navigator.getDisplay().showView(new LoginView(navigator,
            				fragmentAndParameters));
            		return false;

            	} else {
            		return true;
            	}
              }
                  
              @Override
              public void afterViewChange(ViewChangeEvent event) {

              }          
        });
    }
    
    public String getLoggedInUser(){ return loggedInUser; } 
    public void setLoggedInUser(String user){ loggedInUser = user; }
}