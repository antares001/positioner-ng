package net.scnetwork.positioner.controller.TableChangeListener;

import com.vaadin.data.Property;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import net.scnetwork.positioner.bean.UserSettings;
import net.scnetwork.positioner.dao.SqlDao;
import net.scnetwork.positioner.utils.HiddenVariable;
import net.scnetwork.positioner.controller.MenuSelectedEvents.AdminUsersMenu;

import java.util.ArrayList;

public class ListAdminUsers implements Property.ValueChangeListener{
    private AdminUsersMenu adminUsersMenu;
    private SqlDao dao = new SqlDao();

    public ListAdminUsers(AdminUsersMenu arg){
        this.adminUsersMenu = arg;
    }

    @Override
    public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        Button changePass = adminUsersMenu.getChangePass();
        Button changeGroup = adminUsersMenu.getChangeGroup();
        Button changeDev = adminUsersMenu.getChangeDev();
        Button delete = adminUsersMenu.getDelete();
        ArrayList<UserSettings> users = dao.GetUsers();

        changePass.setEnabled(true);
        changeGroup.setEnabled(true);
        changeDev.setEnabled(true);
        delete.setEnabled(true);
        String numUser = String.valueOf(valueChangeEvent.getProperty().getValue());
        int k = 1;
        for (UserSettings user : users){
            if (numUser.equals(String.valueOf(k))) {
                HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
                hidden.pullDel("selected_user");
                hidden.pullDown("selected_user", user.getUsername());
            }
            k++;
        }
    }
}
