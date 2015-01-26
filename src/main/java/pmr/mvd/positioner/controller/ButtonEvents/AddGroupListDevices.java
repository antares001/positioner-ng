package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.Devices;
import pmr.mvd.positioner.dao.SqlDao;

import java.util.ArrayList;

public class AddGroupListDevices implements Button.ClickListener{
    private SqlDao dao = new SqlDao();
    private GroupListDevices groupListDevices;

    private Window window;
    private ComboBox comboBox;

    public Window getWindow(){
        return this.window;
    }

    public void setWindow(Window arg){
        this.window = arg;
    }

    public ComboBox getComboBox(){
        return this.comboBox;
    }

    public void setComboBox(ComboBox arg){
        this.comboBox = arg;
    }

    public AddGroupListDevices(GroupListDevices arg){
        this.groupListDevices = arg;
    }

    /**
     * Администрирование->Управление пользователями->ТС->Добавить
     * @param clickEvent событие
     */
    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        setWindow(new Window("Добавление транспортного средства для пользователя"));
        setComboBox(new ComboBox());

        window.setModal(true);
        window.setWidth(400.0f, Sizeable.Unit.PIXELS);
        window.setHeight(200.0f, Sizeable.Unit.PIXELS);

        FormLayout flTsUsers = new FormLayout();
        CustomLayout clTsUsers = new CustomLayout("changegroup");

        comboBox.setWidth(200.0f, Sizeable.Unit.PIXELS);
        comboBox.setImmediate(true);
        comboBox.setNullSelectionAllowed(false);

        final ArrayList<Devices> tsDevices = dao.GetDevices();
        for (Devices ts : tsDevices) {
            comboBox.addItem(ts.getName());
        }
        clTsUsers.addComponent(comboBox, "group");

        final Button saveDevGroup = new Button("Добавить");
        saveDevGroup.addClickListener(new AddGroupListDevicesSave(this, groupListDevices));
        clTsUsers.addComponent(saveDevGroup, "save");

        final Button closeDevGroup = new Button("Отмена", new CloseWindow(window));
        clTsUsers.addComponent(closeDevGroup, "close");

        flTsUsers.addComponent(clTsUsers);
        window.setContent(flTsUsers);

        UI.getCurrent().addWindow(window);
    }
}
