package net.scnetwork.positioner.controller.MenuSelectedEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;

public class AdminUsersMenu implements MenuBar.Command{
    private Window window;

    public Window getWindowAddUser(){
        return this.window;
    }

    public void setWindowAddUser(Window arg){
        this.window = arg;
    }

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        setWindowAddUser(new Window("Управление пользователями"));
        window.setWidth(850.0f, Sizeable.Unit.PIXELS);
        window.setHeight(400.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);
        final FormLayout formLayout = new FormLayout();

        window.setContent(formLayout);
        UI.getCurrent().addWindow(window);
    }
}
