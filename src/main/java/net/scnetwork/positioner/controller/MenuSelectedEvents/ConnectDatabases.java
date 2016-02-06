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
        window.setWidth(800.0f, Sizeable.Unit.PIXELS);

        CustomLayout connectLayout = new CustomLayout("databaseConnect");
        ComboBox typeConnection = new ComboBox();
        typeConnection.addItems("PostgreSQL");
        typeConnection.addItems("MySQL");
        connectLayout.addComponent(typeConnection, "typeConnect");

        TextField username = new TextField();
        connectLayout.addComponent(username, "loginConnect");

        PasswordField password = new PasswordField();
        connectLayout.addComponent(password, "passConnect");

        TextField database = new TextField();
        connectLayout.addComponent(database, "nameDatabaseConnect");

        window.setContent(connectLayout);

        UI.getCurrent().addWindow(window);
    }
}
