package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.UserSettings;
import pmr.mvd.positioner.dao.SqlDao;

import java.util.ArrayList;

public class AddDevGroup implements Button.ClickListener{
    private SqlDao dao = new SqlDao();
    private Window window;
    private ComboBox comboBoxUsers;
    private DevGroup devGroup;

    public AddDevGroup(DevGroup arg){
        this.devGroup = arg;
    }

    public Window getWindow(){
        return this.window;
    }

    public void setWindow(Window arg){
        this.window = arg;
    }

    public ComboBox getComboBoxUsers(){
        return this.comboBoxUsers;
    }

    public void setComboBoxUsers(ComboBox arg){
        this.comboBoxUsers = arg;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        setWindow(new Window("Новый пользователь"));
        window.setModal(true);
        window.setWidth(400.0f, Sizeable.Unit.PIXELS);
        window.setHeight(200.0f, Sizeable.Unit.PIXELS);

        FormLayout addGroupUserLayout = new FormLayout();
        CustomLayout layout0 = new CustomLayout("changegroup");

        setComboBoxUsers(new ComboBox());
        comboBoxUsers.setWidth(200.0f, Sizeable.Unit.PIXELS);
        comboBoxUsers.setImmediate(true);
        comboBoxUsers.setNullSelectionAllowed(false);

        final ArrayList<UserSettings> listUsers = dao.GetUsers();
        for (UserSettings us : listUsers){
            comboBoxUsers.addItem(us.getUsername());
        }
        layout0.addComponent(comboBoxUsers, "group");

        final Button saveGroupUser = new Button("Добавить");
        saveGroupUser.addClickListener(new SaveAddDevGroup(this, devGroup));
        layout0.addComponent(saveGroupUser, "save");

        final Button closeGroupUser = new Button("Отмена", new CloseWindow(window));
        layout0.addComponent(closeGroupUser, "close");

        addGroupUserLayout.addComponent(layout0);

        window.setContent(addGroupUserLayout);
        UI.getCurrent().addWindow(window);
    }
}
