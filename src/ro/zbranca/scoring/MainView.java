package ro.zbranca.scoring;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Title("Scoring application")
@Theme("valo")
@SuppressWarnings("serial")
public class MainView extends Panel implements View {

    public static final String NAME = "";
    
    private Button logOut;

    public MainView(final Navigator navigator) {

        VerticalLayout layout = new VerticalLayout();

        Link lnk = new Link("Count", new ExternalResource("#!" + PersonalView.NAME));
        layout.addComponent(lnk);

        lnk = new Link("Message: Hello", new ExternalResource("#!"
                + MessageView.NAME + "/Hello"));
        layout.addComponent(lnk);

        lnk = new Link("Message: Bye", new ExternalResource("#!"
                + MessageView.NAME + "/Bye/Goodbye"));
        layout.addComponent(lnk);

        lnk = new Link("Private message: Secret", new ExternalResource("#!"
                + SecretView.NAME + "/Secret"));
        layout.addComponent(lnk);

        lnk = new Link("Private message: Topsecret", new ExternalResource("#!"
                + SecretView.NAME + "/Topsecret"));
        layout.addComponent(lnk);

        logOut = new Button("Logout", new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {

            	((ScoringUI)UI.getCurrent()).setLoggedInUser(null);
            	logOut.setCaption("Login");
            	navigator.navigateTo(LoginView.NAME);
                
            }
        });
        layout.addComponent(logOut);
        setContent(layout);
    }

    @Override
    public void enter(ViewChangeEvent event) {
               
    }
}