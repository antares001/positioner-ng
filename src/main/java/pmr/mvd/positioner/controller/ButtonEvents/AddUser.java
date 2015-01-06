package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.UserSettings;
import pmr.mvd.positioner.dao.SqlDao;

import java.util.ArrayList;

public class AddUser implements Button.ClickListener{
    private SqlDao dao = new SqlDao();

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        final Window addUser = new Window("Добавить");
        addUser.setWidth(500.0f, Sizeable.Unit.PIXELS);
        addUser.setHeight(300.0f, Sizeable.Unit.PIXELS);
        addUser.setModal(true);

        final FormLayout formLayout = new FormLayout();
        final CustomLayout customLayout = new CustomLayout("adduser");

        final TextField userName = new TextField();
        customLayout.addComponent(userName, "name");

        final PasswordField passWord = new PasswordField();
        customLayout.addComponent(passWord, "password");

        final PasswordField repeatPass = new PasswordField();
        customLayout.addComponent(repeatPass, "repeat");

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
                        if (dao.AddNewUser(userName.getValue(), passWord.getValue())) {
                            addUser.close();
                        } else {
                            Notification.show("Ошибка добавления пользователя");
                        }
                    } else
                        Notification.show("Пользователь существует");
                }
            }
        });
        customLayout.addComponent(addNewUser, "addbutton");

        final Button closeNewUser = new Button("Отмена", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                addUser.close();
            }
        });
        customLayout.addComponent(closeNewUser, "close");

        formLayout.addComponent(customLayout);
        addUser.setContent(formLayout);
        UI.getCurrent().addWindow(addUser);
    }
}
