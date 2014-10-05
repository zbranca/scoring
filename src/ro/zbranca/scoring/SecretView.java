package ro.zbranca.scoring;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;

public class SecretView extends MessageView implements View {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String NAME = "secret";

    public SecretView() {
        setCaption("Private messages");

        ((Layout) getContent()).addComponent(new Label("Some private stuff."));
    }

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}