package ro.zbranca.scoring;

import java.sql.SQLException;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.annotations.Title;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ro.zbranca.scoring.data.DatabaseHelper;;

@SuppressWarnings("serial")
@Title("Personal")
public class PersonalView extends Panel implements View {

	/*set name of view*/
	public static final String NAME = "personal";


	/* initialize user interface components*/
	private Table personalList = new Table();
	private TextField searchField = new TextField();
	private Button addButton = new Button (FontAwesome.PLUS);
	private Button removeButton = new Button(FontAwesome.MINUS);
	private Button editButton = new Button(FontAwesome.EDIT);
	private String textFilter;


	public PersonalView() {

		initLayout();
		initPersonalList();
		initSearch();
		initAddRemoveButtons();
	}

	private void initLayout() {

		setCaption("<div align=\"left\" style=\"font-size:20pt;\">" + FontAwesome.GROUP.getHtml() + " &nbsp; Users administration</div>");

		/* Root of the user interface component tree is set */
		VerticalLayout panelLayout= new VerticalLayout();
		panelLayout.setMargin(true);
		setContent(panelLayout);

		/* Build the component tree */   	
		HorizontalLayout topCommandItems = new HorizontalLayout();
		topCommandItems.addComponent(addButton);
		addButton.setDescription("Add new user");
		topCommandItems.addComponent(removeButton);
		removeButton.setDescription("Select a user to remove<br>");
		removeButton.setEnabled(false);
		topCommandItems.addComponent(editButton);
		editButton.setDescription("Select a user to edit");
		editButton.setEnabled(false);
		topCommandItems.addComponent(searchField);
		searchField.setWidth("100%");
		panelLayout.addComponent(topCommandItems);
		panelLayout.addComponent(personalList);

	}


	private void initSearch() {

		/* tool tip to show at prompt */
		searchField.setInputPrompt("Search personal");

		/*lazy mode that send data to server after stop writing*/
		searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);
		searchField.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange( final TextChangeEvent event) {
				textFilter = event.getText();
				updateFilters();
			}
		});
	}

	/*
	 * A custom filter for searching in the
	 * contactContainer.
	 */
	private void updateFilters() {
		DatabaseHelper.getPersonalContainer().removeAllContainerFilters();
		if (textFilter != null && !textFilter.equals("")) {
			Or or = new Or(new Like("email","%" + textFilter + "%", false),
					new Like("full_name", "%" + textFilter + "%", false),
					new Like("role", "%" + textFilter + "%", false));
			DatabaseHelper.getPersonalContainer().addContainerFilter(or);
		}
		DatabaseHelper.getPersonalContainer().refresh();
	}

	private void initAddRemoveButtons() {

		addButton.addClickListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {
				DatabaseHelper.getPersonalContainer().removeAllContainerFilters();
				Object personalId = DatabaseHelper.getPersonalContainer().addItem();
				personalList.select(personalId);
				openPersonEdit();

			}
		});

		removeButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				ConfirmDialog.show(UI.getCurrent(),"Are you shure that want to delete a user?",
						new ConfirmDialog.Listener() {

					@Override
					public void onClose(ConfirmDialog dialog) {
						if(dialog.isConfirmed()) {
				            removeButton.setEnabled(false);
				            editButton.setEnabled(false);
							Object personalId = personalList.getValue();
							DatabaseHelper.getPersonalContainer().removeItem(personalId);
							try {
								DatabaseHelper.getPersonalContainer().commit();
							} catch (UnsupportedOperationException | SQLException e) {
								e.printStackTrace();
							}
							DatabaseHelper.getPersonalContainer().refresh();
						} else {
							DatabaseHelper.getPersonalContainer().refresh();
						}

					}
				});

			}
		});



		editButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (personalList.getValue() == null) {personalList.select(personalList.firstItemId());}
				openPersonEdit();
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void initPersonalList(){
		/*get table container and clean the filters*/
		DatabaseHelper.getPersonalContainer().removeAllContainerFilters();
		personalList.setContainerDataSource(DatabaseHelper.getPersonalContainer());
		personalList.setVisibleColumns(DatabaseHelper.PERSONAL_NATURAL_COL_ORDER);
		personalList.setColumnHeaders(DatabaseHelper.PERSONAL_COL_HEADERS_EN);
		personalList.setSelectable(true);
		personalList.setImmediate(true);
		personalList.setPageLength(10);
		personalList.setNullSelectionAllowed(false);
		personalList.focus();
		personalList.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                setModificationsEnabled(event.getProperty().getValue() != null);
            }

            private void setModificationsEnabled(boolean b) {
                removeButton.setEnabled(b);
                editButton.setEnabled(b);
            }
        });

	}

		public void openPersonEdit() {
            removeButton.setEnabled(false);
            editButton.setEnabled(false);
			PersonEdit sub = new PersonEdit(personalList.getItem(personalList.getValue()));
			sub.setWidth("300px");
			sub.setModal(true);
			// Add it to the root component
			UI.getCurrent().addWindow(sub);
		}



		@Override
		public void enter(ViewChangeEvent event) {
			// TODO Auto-generated method stub				
		}
	}
