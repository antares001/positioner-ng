package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.HashMap;

public class ChangePassword implements Button.ClickListener{
    private SqlDao dao = new SqlDao();

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        final HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());

        final Window winChangePass = new Window("Смена пароля пользователя " + hidden.pullUp("selected_user"));
        winChangePass.setWidth(500.0f, Sizeable.Unit.PIXELS);
        winChangePass.setHeight(200.0f, Sizeable.Unit.PIXELS);
        winChangePass.setModal(true);

        final FormLayout fLayout = new FormLayout();
        final CustomLayout chPass = new CustomLayout("changepass");

        final PasswordField chPassText = new PasswordField();
        chPass.addComponent(chPassText, "password");

        final PasswordField chRepeatText = new PasswordField();
        chPass.addComponent(chRepeatText, "repeat");

        final Button changePassButton = new Button("Сменить", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (chPassText.getValue().equals(""))
                    Notification.show("Введите пароль");
                else if (chRepeatText.getValue().equals(""))
                    Notification.show("Повторите пароль");
                else if (!chPassText.getValue().equals(chRepeatText.getValue()))
                    Notification.show("Пароли не совпадают");
                else {
                    HashMap<String,String> params = new HashMap<String, String>();
                    params.put("value", chPassText.getValue());
                    params.put("user", hidden.pullUp("selected_user"));

                    if (dao.ExecuteOperation(params, "change_password"))
                        winChangePass.close();
                    else
                        Notification.show("Ошибка смены пароля");
                }
            }
        });
        chPass.addComponent(changePassButton, "addbutton");

        final Button closeChangePass = new Button("Отмена", new CloseWindow(winChangePass));
        chPass.addComponent(closeChangePass, "close");

        fLayout.addComponent(chPass);
        winChangePass.setContent(fLayout);
        UI.getCurrent().addWindow(winChangePass);
    }
}
