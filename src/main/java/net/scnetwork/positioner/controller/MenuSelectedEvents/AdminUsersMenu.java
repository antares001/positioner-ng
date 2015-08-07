package net.scnetwork.positioner.controller.MenuSelectedEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;

public class AdminUsersMenu implements MenuBar.Command{
    private Table tabUsers;
    private Window window;

    private Button changePass;
    private Button changeGroup;
    private Button changeDev;
    private Button delete;


    public Table getTabUsers(){
        return this.tabUsers;
    }

    public void setTabUsers(Table arg){
        this.tabUsers = arg;
    }

    public Window getWindowAddUser(){
        return this.window;
    }

    public void setWindowAddUser(Window arg){
        this.window = arg;
    }

    public Button getChangePass(){
        return this.changePass;
    }

    public void setChangePass(Button arg){
        this.changePass = arg;
    }

    public Button getChangeGroup(){
        return this.changeGroup;
    }

    public void setChangeGroup(Button arg){
        this.changeGroup = arg;
    }

    public Button getChangeDev(){
        return this.changeDev;
    }

    public void setChangeDev(Button arg){
        this.changeDev = arg;
    }

    public Button getDelete(){
        return this.delete;
    }

    public void setDelete(Button arg){
        this.delete = arg;
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
