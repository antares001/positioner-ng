package pmr.mvd.positioner.controller.MenuSelectedEvents;

import com.vaadin.data.Item;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;

import pmr.mvd.positioner.bean.UserSettings;
import pmr.mvd.positioner.controller.ButtonEvents.*;
import pmr.mvd.positioner.controller.TableChangeListener.ListAdminUsers;
import pmr.mvd.positioner.dao.SqlDao;

import java.util.ArrayList;

public class AdminUsersMenu implements MenuBar.Command{
    private SqlDao dao = new SqlDao();
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

        VerticalLayout vertical = new VerticalLayout();
        setTabUsers(new Table("Пользователи"));
        tabUsers.setSelectable(true);

        tabUsers.addContainerProperty("Логин", String.class, null);
        tabUsers.addContainerProperty("Группа", String.class, null);

        ArrayList<UserSettings> users = dao.GetUsers();
        for(UserSettings settings : users){
            String group = settings.getGroup();
            if (group.equals("1"))
                group = "Администратор";
            else if (group.equals("0"))
                group = "Пользователь";

            Object newItem = tabUsers.addItem();
            Item row = tabUsers.getItem(newItem);
            row.getItemProperty("Логин").setValue(settings.getUsername());
            row.getItemProperty("Группа").setValue(group);
        }

        tabUsers.setPageLength(5);
        tabUsers.setWidth(800.0f, Sizeable.Unit.PIXELS);

        vertical.addComponent(tabUsers);

        final CustomLayout custom = new CustomLayout("buttons");

        Button addNewUser = new Button("Добавить", new AddUser(this));

        setChangePass(new Button("Сменить пароль", new ChangePassword()));
        changePass.setEnabled(false);

        setChangeGroup(new Button("Сменить группу", new ChangeGroup()));
        changeGroup.setEnabled(false);

        setChangeDev(new Button("ТС", new GroupListDevices()));
        changeDev.setEnabled(false);

        setDelete(new Button("Удалить", new DeleteUserConfirm()));
        delete.setEnabled(false);

        tabUsers.addValueChangeListener(new ListAdminUsers(this));

        Button exit = new Button("Закрыть", new CloseWindow(window));

        custom.addComponent(addNewUser, "addButton");
        custom.addComponent(changePass, "changePass");
        custom.addComponent(changeGroup, "changeGroup");
        custom.addComponent(changeDev, "groupDev");
        custom.addComponent(delete, "deleteButton");
        custom.addComponent(exit, "close");

        vertical.addComponent(custom);

        formLayout.addComponent(vertical);

        window.setContent(formLayout);
        UI.getCurrent().addWindow(window);
    }
}
