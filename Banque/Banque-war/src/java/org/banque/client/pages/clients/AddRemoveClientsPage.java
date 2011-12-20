/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages.clients;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.banque.client.BanqueService;
import org.banque.client.BanqueServiceAsync;
import org.banque.client.pages.WebPage;
import org.banque.client.ui.PopupWidget;
import org.banque.dtos.ClientDTO;
import org.banque.dtos.PersonDTO.Gender;
import org.banque.exceptions.BanqueException;
import org.banque.server.BanqueServiceImpl;

/**
 *
 * @author bjurkovski
 */
public class AddRemoveClientsPage implements WebPage {
    private VerticalPanel vPanel;
    private Label pageTitle;
    private Label addTitle;
    private Label removeTitle;
    private Grid form;
    private TextBox firstNameBox;
    private TextBox lastNameBox;
    private RadioButton genderMRadio;
    private RadioButton genderFRadio;
    private TextBox addressBox;
    private TextBox emailBox;
    private TextBox dateOfBirthBox;
    private PasswordTextBox passwordBox;
    private PasswordTextBox confirmPasswordBox;
    private ListBox clientsList;
    private Button addClientButton;
    private Button removeClientButton;
    private PopupWidget popupWidget;
    
    public AddRemoveClientsPage() {
        vPanel = new VerticalPanel();
        pageTitle = new Label("Manage Clients");
        addTitle = new Label("Add Client");
        removeTitle = new Label("Remove Clients");
        form = new Grid(10, 4);
        firstNameBox = new TextBox();
        lastNameBox = new TextBox();
        genderMRadio = new RadioButton("gender", "M");
        genderFRadio = new RadioButton("gender", "F");
        genderMRadio.setValue(true);
        addressBox = new TextBox();
        emailBox = new TextBox();
        dateOfBirthBox = new TextBox();
        passwordBox = new PasswordTextBox();
        confirmPasswordBox = new PasswordTextBox();
        clientsList = new ListBox();
        clientsList.setVisibleItemCount(15);
        addClientButton = new Button("Add Client");
        removeClientButton = new Button("Remove Client");
        
        pageTitle.setStyleName("title");
        addTitle.setStyleName("title");
        removeTitle.setStyleName("title");
        
        final AsyncCallback<Void> updateClientsCallback = new AsyncCallback<Void>() {
            public void onSuccess(Void result) {
                updateClientsList();
            }
            
            public void onFailure(Throwable caught) {
            }
        };
        
        addClientButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ClientDTO client = new ClientDTO(firstNameBox.getValue(),
                                                lastNameBox.getValue(),
                                                passwordBox.getValue(),
                                                Gender.MALE,
                                                new Date(),
                                                addressBox.getValue(),
                                                emailBox.getValue());
                if(validateForm()) {
                    getService().createClient(client, updateClientsCallback);
                }
            }
        });
        
        removeClientButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                try {
                    int idx = clientsList.getSelectedIndex();
                    String idStr = clientsList.getValue(idx);
                    getService().removeClient(Long.decode(idStr), updateClientsCallback);
                }
                catch(IndexOutOfBoundsException e) {
                }
            }
        });
        
        vPanel.add(pageTitle);
        form.setWidget(0, 0, addTitle);
        form.setWidget(1, 0, new Label("First Name: "));
        form.setWidget(1, 1, firstNameBox);
        form.setWidget(2, 0, new Label("Last Name: "));
        form.setWidget(2, 1, lastNameBox);
        form.setWidget(3, 0, new Label("Date of Birth: "));
        form.setWidget(3, 1, dateOfBirthBox);
        form.setWidget(4, 0, new Label("Address: "));
        form.setWidget(4, 1, addressBox);
        form.setWidget(5, 0, new Label("e-mail: "));
        form.setWidget(5, 1, emailBox);
        form.setWidget(6, 0, new Label("Password: "));
        form.setWidget(6, 1, passwordBox);
        form.setWidget(7, 0, new Label("Confirm Password: "));
        form.setWidget(7, 1, confirmPasswordBox);
        HorizontalPanel genderPanel = new HorizontalPanel();
        genderPanel.add(genderMRadio);
        genderPanel.add(genderFRadio);
        form.setWidget(8, 0, new Label("Gender: "));
        form.setWidget(8, 1, genderPanel);
        form.setWidget(9, 0, addClientButton);
        //String password, Gender gender, Date dateOfBirth
        
        form.setWidget(0, 2, removeTitle);
        form.setWidget(1, 2, new Label("Client: "));
        form.setWidget(1, 3, clientsList);
        form.setWidget(2, 2, removeClientButton);
        vPanel.add(form);
        
        updateClientsList();
    }
    
    private boolean validateForm() {
        popupWidget = new PopupWidget("Invalid data!", false);
        popupWidget.show();
        popupWidget.addOKHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                popupWidget.hide();
            }
        });
        return false;
    }
    
    private void clearForm() {
        firstNameBox.setText("");
        lastNameBox.setText("");
        addressBox.setText("");
        emailBox.setText("");
        dateOfBirthBox.setText("");
        passwordBox.setText("");
        confirmPasswordBox.setText("");
    }
    
    private void updateClientsList() {
        final AsyncCallback<List<ClientDTO>> updateClientsListCallback = new AsyncCallback<List<ClientDTO>>() {
            public void onSuccess(List<ClientDTO> result) {
                clientsList.clear();
                for(Iterator<ClientDTO> i = result.iterator(); i.hasNext(); ) {
                    ClientDTO client = i.next();
                    clientsList.addItem(client.getName() + " " + client.getLastName(), 
                                        String.valueOf(client.getId()));
                }
            }

            public void onFailure(Throwable caught) {
            }
        };
        
        getService().findAllClients(updateClientsListCallback);
    }
    
    public static BanqueServiceAsync getService() {
        return GWT.create(BanqueService.class);
    }
    
    @Override
    public Widget getWidget() {
        return vPanel;
    }
}