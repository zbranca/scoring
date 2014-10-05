package ro.zbranca.scoring;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;


public class MessageView extends Panel implements View {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String NAME = "message";

    public MessageView() {
    	super(new VerticalLayout());
        setCaption("Messages");
    }

    @Override
    public void enter(ViewChangeEvent event) {
       if(event.getParameters() != null){
           // split at "/", add each part as a label
           String[] msgs = event.getParameters().split("/");
           for (String msg : msgs) { 
        	   ((Layout)getContent()).addComponent(new Label(msg));
           }
       }        
    }

}