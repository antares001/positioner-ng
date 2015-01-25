package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.data.Item;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.UserSettings;
import pmr.mvd.positioner.controller.MenuSelectedEvents.AdminUsersMenu;
import pmr.mvd.positioner.dao.SqlDao;

import java.util.ArrayList;
import java.util.HashMap;

public class AddNewUser implements Button.ClickListener{
    private AddUser addUser;
    private AdminUsersMenu adminUsersMenu;
    private SqlDao dao = new SqlDao();

    public AddNewUser(AddUser arg0, AdminUsersMenu arg1){
        this.addUser = arg0;
        this.adminUsersMenu = arg1;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        TextField userName = addUser.getUserName();
        PasswordField passWord = addUser.getPassWord();
        PasswordField repeatPass = addUser.getRepeatPass();
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
                ComboBox comboBox = addUser.getComboBox();
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
                    Window window = addUser.getWindow();
                    window.close();

                    Table table = adminUsersMenu.getTabUsers();
                    table.removeAllItems();

                    ArrayList<UserSettings> usersNew = dao.GetUsers();
                    for(UserSettings settings : usersNew){
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
}
