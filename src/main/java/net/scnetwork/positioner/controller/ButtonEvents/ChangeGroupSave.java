package net.scnetwork.positioner.controller.ButtonEvents;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import net.scnetwork.positioner.dao.SqlDao;
import net.scnetwork.positioner.utils.HiddenVariable;

import java.util.HashMap;

public class ChangeGroupSave implements Button.ClickListener{
    private SqlDao dao = new SqlDao();
    private ChangeGroup changeGroup;

    public ChangeGroupSave(ChangeGroup arg){
        this.changeGroup = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        final HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        try {
            ComboBox comboBox = changeGroup.getComboBox();
            final String value = comboBox.getValue().toString();

            String group = "";
            if (value.equals("Администратор"))
                group = "1";
            else if (value.equals("Пользователь"))
                group = "0";

            HashMap<String,String> params = new HashMap<String, String>();
            params.put("user", hidden.pullUp("selected_user"));
            params.put("group", group);

            if (dao.ExecuteOperation(params, "change_group")) {
                Window window = changeGroup.getWindow();
                window.close();
            } else
                Notification.show("Ошибка смены группы");
        } catch (NullPointerException e) {
            Notification.show("Не выбрана группа");
        }
    }
}
