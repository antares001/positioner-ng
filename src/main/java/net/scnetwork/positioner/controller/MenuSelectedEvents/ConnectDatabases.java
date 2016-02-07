package net.scnetwork.positioner.controller.MenuSelectedEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import net.scnetwork.positioner.controller.MainView;
import net.scnetwork.positioner.utils.HiddenVariable;

public class ConnectDatabases implements MenuBar.Command{
    private MainView mainView;
    private Window window;

    public ConnectDatabases(MainView arg){
        this.mainView = arg;
    }

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        window = new Window("Подключение к базе");
        window.setModal(true);
        window.setHeight(300.0f, Sizeable.Unit.PIXELS);
        window.setWidth(600.0f, Sizeable.Unit.PIXELS);

        CustomLayout connectLayout = new CustomLayout("databaseConnect");
        ComboBox typeConnection = new ComboBox();
        typeConnection.addItems("PostgreSQL");
        typeConnection.addItems("MySQL");
        typeConnection.setInvalidAllowed(false);
        typeConnection.setNullSelectionAllowed(false);
        connectLayout.addComponent(typeConnection, "typeConnect");

        TextField username = new TextField();
        connectLayout.addComponent(username, "loginConnect");

        PasswordField password = new PasswordField();
        connectLayout.addComponent(password, "passConnect");

        TextField database = new TextField();
        connectLayout.addComponent(database, "nameDatabaseConnect");

        Button ok = new Button("Подключение");
        connectLayout.addComponent(ok, "okbutton");

        Button cancel = new Button("Отмена");
        connectLayout.addComponent(cancel, "cancelbutton");

        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                window.close();
            }
        });

        window.setContent(connectLayout);

        UI.getCurrent().addWindow(window);
    }
}
