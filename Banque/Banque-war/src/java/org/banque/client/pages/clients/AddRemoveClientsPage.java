/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.client.pages.clients;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
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
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.sun.imageio.plugins.common.I18N;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.banque.client.BanqueService;
import org.banque.client.BanqueServiceAsync;
import org.banque.client.SessionManager;
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
public class AddRemoveClientsPage extends WebPage {
    private VerticalPanel vPanel;
    private Label pageTitle;
    private Label addTitle;
    private Label removeTitle;
    private Grid addClientsGrid;
    private Grid removeClientsGrid;
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
    
    private void showErrorPopup(String errorMsg) {
        popupWidget = new PopupWidget("Invalid data!", false);
        popupWidget.setContent(new Label(errorMsg));
        popupWidget.show();
        popupWidget.addOKHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                popupWidget.hide();
            }
        });
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
    
    public AddRemoveClientsPage() {
        setupPage();
    }
    
    public void setupPage() {
        vPanel = new VerticalPanel();
        pageTitle = new Label("Manage Clients");
        addTitle = new Label("Add Client");
        removeTitle = new Label("Remove Clients");
        addClientsGrid = new Grid(10, 2);
        removeClientsGrid = new Grid(3, 2);
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
                clearForm();
                updateClientsList();
            }
            
            public void onFailure(Throwable caught) {
                showErrorPopup(caught.getMessage());
            }
        };
        
        addClientButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ClientDTO.Gender gender = ClientDTO.Gender.FEMALE;
                if(genderMRadio.getValue() == true) {
                    gender = ClientDTO.Gender.MALE;
                }
                
                if(passwordBox.getValue().equals("")
                        ||(passwordBox.getValue() != confirmPasswordBox.getValue())) {
                    showErrorPopup("Passwords don't match.");
                    return;
                }
                
                try {
                    String dateStr = dateOfBirthBox.getValue();
                    DateTimeFormat dd = DateTimeFormat.getFormat("dd/MM/yyyy");
                    Date dateOfBirth = dd.parse(dateStr);
                
                    ClientDTO client = new ClientDTO(firstNameBox.getValue(),
                                                    lastNameBox.getValue(),
                                                    passwordBox.getValue(),
                                                    gender,
                                                    dateOfBirth,
                                                    addressBox.getValue(),
                                                    emailBox.getValue());
                    
                    getService().createClient(client, updateClientsCallback);
                }
                catch(Exception e) {
                    showErrorPopup("Date must be on the format dd/mm/yyy. E.g.: 01/01/1985");
                }
            }
        });
        
        removeClientButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                try {
                    popupWidget = new PopupWidget("Confirmation box", true);
                    popupWidget.setContent(new Label("Are you sure?"));
                    popupWidget.show();
                    
                    popupWidget.addOKHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            int idx = clientsList.getSelectedIndex();
                            String idStr = clientsList.getValue(idx);
                            getService().removeClient(Long.decode(idStr), updateClientsCallback);
                            popupWidget.hide();
                        }
                    });
                    
                    popupWidget.addCancelHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            popupWidget.hide();
                        }
                    });
                }
                catch(IndexOutOfBoundsException e) {
                }
            }
        });
        
        vPanel.add(pageTitle);
        addClientsGrid.setWidget(0, 0, addTitle);
        addClientsGrid.setWidget(1, 0, new Label("First Name: "));
        addClientsGrid.setWidget(1, 1, firstNameBox);
        addClientsGrid.setWidget(2, 0, new Label("Last Name: "));
        addClientsGrid.setWidget(2, 1, lastNameBox);
        addClientsGrid.setWidget(3, 0, new Label("Date of Birth: "));
        addClientsGrid.setWidget(3, 1, dateOfBirthBox);
        addClientsGrid.setWidget(4, 0, new Label("Address: "));
        addClientsGrid.setWidget(4, 1, addressBox);
        addClientsGrid.setWidget(5, 0, new Label("e-mail: "));
        addClientsGrid.setWidget(5, 1, emailBox);
        addClientsGrid.setWidget(6, 0, new Label("Password: "));
        addClientsGrid.setWidget(6, 1, passwordBox);
        addClientsGrid.setWidget(7, 0, new Label("Confirm Password: "));
        addClientsGrid.setWidget(7, 1, confirmPasswordBox);
        HorizontalPanel genderPanel = new HorizontalPanel();
        genderPanel.add(genderMRadio);
        genderPanel.add(genderFRadio);
        addClientsGrid.setWidget(8, 0, new Label("Gender: "));
        addClientsGrid.setWidget(8, 1, genderPanel);
        addClientsGrid.setWidget(9, 0, addClientButton);
        //String password, Gender gender, Date dateOfBirth
        
        removeClientsGrid.setWidget(0, 0, removeTitle);
        removeClientsGrid.setWidget(1, 0, new Label("Client: "));
        removeClientsGrid.setWidget(1, 1, clientsList);
        removeClientsGrid.setWidget(2, 0, removeClientButton);
        
        final HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.add(addClientsGrid);
        hPanel.add(removeClientsGrid);
        vPanel.add(hPanel);
        
        updateClientsList();
    }
    
    @Override
    public Widget getWidget() {
        return vPanel;
    }
}
