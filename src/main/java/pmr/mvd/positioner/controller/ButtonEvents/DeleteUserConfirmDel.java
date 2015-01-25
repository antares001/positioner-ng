package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

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
