package pmr.mvd.positioner.controller.TableChangeListener;

import com.vaadin.data.Property;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import pmr.mvd.positioner.bean.UserSettings;
import pmr.mvd.positioner.controller.MenuSelectedEvents.AdminUsersMenu;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.ArrayList;

public class ListAdminUsers implements Property.ValueChangeListener{
    private AdminUsersMenu adminUsersMenu;

    public ListAdminUsers(AdminUsersMenu arg){
        this.adminUsersMenu = arg;
    }

    @Override
    public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        Button changePass = adminUsersMenu.getChangePass();
        Button changeGroup = adminUsersMenu.getChangeGroup();
        Button changeDev = adminUsersMenu.getChangeDev();
        Button delete = adminUsersMenu.getDelete();
        ArrayList<UserSettings> users = adminUsersMenu.getUsers();

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
