package net.scnetwork.positioner.controller.ButtonEvents;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import net.scnetwork.positioner.dao.SqlDao;
import net.scnetwork.positioner.utils.HiddenVariable;

import java.util.HashMap;

public class DeleteUserConfirmDel implements Button.ClickListener{
    private SqlDao dao = new SqlDao();
    private DeleteUserConfirm deleteUserConfirm;

    public DeleteUserConfirmDel(DeleteUserConfirm arg){
        this.deleteUserConfirm = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("user", hidden.pullUp("selected_user"));

        if (dao.ExecuteOperation(params, "delete_user")) {
            Window window = deleteUserConfirm.getWindow();
            window.close();
        } else
            Notification.show("Ошибка удаления пользователя");
    }
}
