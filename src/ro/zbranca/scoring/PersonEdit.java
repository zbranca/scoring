package ro.zbranca.scoring;

import java.sql.SQLException;

import ro.zbranca.scoring.data.DatabaseHelper;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

class PersonEdit extends Window {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Item personalItem;
	private FormLayout editorLayout = new FormLayout();
	private FieldGroup editorFields = new FieldGroup();


	public PersonEdit(Item personalItem) {

		this.personalItem = personalItem;
		center();

		editorFields.setItemDataSource(personalItem);

		// Some basic content for the window
		VerticalLayout content = new VerticalLayout();

		TextField emailText = new TextField("Email");
		editorLayout.addComponent(emailText);
		editorFields.bind(emailText, "email");
		emailText.setNullRepresentation("");
		emailText.setImmediate(true);
		emailText.addValidator(new EmailValidator("A new email adress must be provided"));



		TextField nameText = new TextField("Name");
		editorLayout.addComponent(nameText);
		editorFields.bind(nameText, "full_name");
		nameText.setNullRepresentation("");
		nameText.setImmediate(true);
		nameText.addValidator(new StringLengthValidator("Empty fields not allowed",1,100, true));


		NativeSelect roleSelect = new NativeSelect("Role");
		editorLayout.addComponent(roleSelect);
		editorFields.bind(roleSelect, "role");
		roleSelect.addItems("admin","score");
		roleSelect.addValidator(new StringLengthValidator("Empty fields not allowed",1,10, true));
		roleSelect.setImmediate(true);
		roleSelect.setNullSelectionAllowed(false);

		content.addComponent(editorLayout);
		content.setMargin(true);
		setContent(content);
		// Disable the close button
		setClosable(false);

		// logic for closing the sub-window
		HorizontalLayout bottomButtons = new HorizontalLayout();
		Button save = new Button("Save");
		save.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (!editorFields.isValid()) {
					Notification.show("Cannot save with errors");
					return;
				}
				try {
					editorFields.commit();
				} catch (CommitException e) {
					e.printStackTrace();
				}
				try {
					DatabaseHelper.getPersonalContainer().commit();
				} catch (UnsupportedOperationException | SQLException e) {
					e.printStackTrace();
					Notification.show("Choose another email.This one already exist in database.");
					return;
				}
				DatabaseHelper.getPersonalContainer().refresh();
				close();

			}
		});
		bottomButtons.addComponent(save);

		Button cancel = new Button("Cancel");
		cancel.setDescription("Close window without saving");
		cancel.addClickListener(new ClickListener() {
			/**
			 * 	
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				try {
					DatabaseHelper.getPersonalContainer().rollback();
				} catch (UnsupportedOperationException | SQLException e) {
					e.printStackTrace();
				}
				DatabaseHelper.getPersonalContainer().refresh();
				close(); // Close the sub-window
			}
		});
		bottomButtons.addComponent(cancel);
		bottomButtons.setSpacing(true);
		bottomButtons.setMargin(true);

		content.addComponent(bottomButtons);

		setCaption(buildCaption()); // Set window caption



	}

	private String buildCaption() {
		if (personalItem.getItemProperty("email").getValue() != null) {
			return String.format("%s %s", personalItem.getItemProperty("full_name").getValue(),
					personalItem.getItemProperty("email").getValue());
		} else {
			return new String("New user");
		}
	}

}