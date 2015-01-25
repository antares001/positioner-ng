package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Window;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.HashMap;

public class ChangePasswordSave implements Button.ClickListener{
    private SqlDao dao = new SqlDao();
    private ChangePassword changePassword;

    public ChangePasswordSave(ChangePassword arg){
        this.changePassword = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        PasswordField chPassText = changePassword.getChPassText();
        PasswordField chRepeatText = changePassword.getChRepeatText();
        if (chPassText.getValue().equals(""))
            Notification.show("Введите пароль");
        else if (chRepeatText.getValue().equals(""))
            Notification.show("Повторите пароль");
        else if (!chPassText.getValue().equals(chRepeatText.getValue()))
            Notification.show("Пароли не совпадают");
        else {
            HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
            HashMap<String,String> params = new HashMap<String, String>();
            params.put("value", chPassText.getValue());
            params.put("user", hidden.pullUp("selected_user"));

            if (dao.ExecuteOperation(params, "change_password")) {
                Window window = changePassword.getWindow();
                window.close();
            } else
                Notification.show("Ошибка смены пароля");
        }
    }
}
