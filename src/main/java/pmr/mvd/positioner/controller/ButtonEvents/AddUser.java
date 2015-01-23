package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.data.Item;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.UserSettings;
import pmr.mvd.positioner.controller.MenuSelectedEvents.AdminUsersMenu;
import pmr.mvd.positioner.dao.SqlDao;

import java.util.ArrayList;
import java.util.HashMap;

public class AddUser implements Button.ClickListener{
    private SqlDao dao = new SqlDao();

    private Window window = new Window("Добавить");
    private AdminUsersMenu adminUsersMenu;

    public AddUser(AdminUsersMenu arg) {
        this.adminUsersMenu = arg;
    }

    public Window getWindow(){
        return this.window;
    }

    public void setWindow(Window arg){
        this.window = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        window.setWidth(500.0f, Sizeable.Unit.PIXELS);
        window.setHeight(300.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);

        final FormLayout formLayout = new FormLayout();
        final CustomLayout customLayout = new CustomLayout("adduser");

        final TextField userName = new TextField();
        customLayout.addComponent(userName, "name");

        final PasswordField passWord = new PasswordField();
        customLayout.addComponent(passWord, "password");

        final PasswordField repeatPass = new PasswordField();
        customLayout.addComponent(repeatPass, "repeat");

        final ComboBox comboBox = new ComboBox();
        comboBox.setImmediate(true);
        comboBox.setNullSelectionAllowed(false);

        comboBox.addItem("Пользователь");
        comboBox.addItem("Администратор");
        customLayout.addComponent(comboBox, "group");

        final Button addNewUser = new Button("Добавить", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (userName.getValue().equals(""))
                    Notification.show("Введите имя пользователя");
                else if (passWord.getValue().equals(""))
                    Notification.show("Введите пароль");
                else if (repeatPass.getValue().equals(""))
                    Notification.show("Повторите пароль");
                else if (!passWord.getValue().equals(repeatPass.getValue()))
                    Notification.show("Пароли не совпадают");
                else {
                    boolean existsUser = true;
                    final ArrayList<UserSettings> users = dao.GetUsers();
                    for (UserSettings user : users) {
                        if (user.getUsername().equals(userName.getValue()))
                            existsUser = false;
                    }

                    if (existsUser) {
                        final String value = comboBox.getValue().toString();

                        String group = "";
                        if (value.equals("Администратор"))
                            group = "1";
                        else if (value.equals("Пользователь"))
                            group = "0";

                        HashMap<String,String> params = new HashMap<String, String>();
                        params.put("role", group);
                        params.put("value", userName.getValue());
                        params.put("value1", passWord.getValue());

                        if (dao.ExecuteOperation(params, "add_new_user")){
                            window.close();

                            Table table = adminUsersMenu.getTabUsers();
                            table.removeAllItems();

                            ArrayList<UserSettings> usersNew = dao.GetUsers();
                            for(UserSettings settings : users){
                                String groups = settings.getGroup();
                                if (groups.equals("1"))
                                    groups = "Администратор";
                                else if (group.equals("0"))
                                    groups = "Пользователь";

                                Object newItem = table.addItem();
                                Item row = table.getItem(newItem);
                                row.getItemProperty("Логин").setValue(settings.getUsername());
                                row.getItemProperty("Группа").setValue(groups);
                            }
                        } else {
                            Notification.show("Ошибка добавления пользователя");
                        }
                    } else
                        Notification.show("Пользователь существует");
                }
            }
        });
        customLayout.addComponent(addNewUser, "addbutton");

        final Button closeNewUser = new Button("Отмена", new CloseWindow(window));
        customLayout.addComponent(closeNewUser, "close");

        formLayout.addComponent(customLayout);
        window.setContent(formLayout);
        UI.getCurrent().addWindow(window);
    }
}
