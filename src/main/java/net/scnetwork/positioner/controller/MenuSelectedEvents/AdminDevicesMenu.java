package net.scnetwork.positioner.controller.MenuSelectedEvents;

import com.vaadin.data.Item;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import net.scnetwork.positioner.bean.Devices;
import net.scnetwork.positioner.controller.ButtonEvents.AddDevice;
import net.scnetwork.positioner.controller.ButtonEvents.CloseWindow;
import net.scnetwork.positioner.controller.ButtonEvents.DeleteDevice;
import net.scnetwork.positioner.controller.ButtonEvents.DevGroup;
import net.scnetwork.positioner.controller.TableChangeListener.DeviceTableListener;
import net.scnetwork.positioner.dao.SqlDao;

import java.util.ArrayList;

public class AdminDevicesMenu implements MenuBar.Command{
    private SqlDao dao = new SqlDao();

    private Window window;
    private Table tabDevice;
    private Button deleteDevice;
    private Button devGroupButton;

    public Window getWindow(){
        return this.window;
    }

    public void setWindow(Window arg){
        this.window = arg;
    }

    public Table getTabDevice(){
        return this.tabDevice;
    }

    public void setTabDevice(Table arg){
        this.tabDevice = arg;
    }

    public Button getDeleteDevice(){
        return this.deleteDevice;
    }

    public void setDeleteDevice(Button arg){
        this.deleteDevice = arg;
    }

    public Button getDevGroupButton(){
        return this.devGroupButton;
    }

    public void setDevGroupButton(Button arg){
        this.devGroupButton = arg;
    }

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        setWindow(new Window("Управление транспортными средствами"));
        window.setWidth(600.0f, Sizeable.Unit.PIXELS);
        window.setHeight(400.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);
        final FormLayout formLayout = new FormLayout();

        VerticalLayout verticalLayout = new VerticalLayout();

        setTabDevice(new Table("Транспортные средства"));
        tabDevice.setSelectable(true);

        tabDevice.addContainerProperty("id",String.class, null);
        tabDevice.addContainerProperty("Имя", String.class, null);
        tabDevice.addContainerProperty("Уникальный идентификатор", String.class, null);

        ArrayList<Devices> devices = dao.GetDevices();
        for (Devices device : devices){
            try {
                String id = device.getId();
                String name = device.getName();
                String positions = device.getUniq();

                Object newItem = tabDevice.addItem();
                Item row = tabDevice.getItem(newItem);
                row.getItemProperty("id").setValue(id);
                row.getItemProperty("Имя").setValue(name);
                row.getItemProperty("Уникальный идентификатор").setValue(positions);
            }catch (NullPointerException ignored){}
        }

        tabDevice.setPageLength(5);
        tabDevice.setSizeFull();

        verticalLayout.addComponent(tabDevice);

        final CustomLayout custom = new CustomLayout("buttons");

        final Button addNewDevice = new Button("Добавить", new AddDevice(this));

        setDevGroupButton(new Button("Пользователи"));
        devGroupButton.addClickListener(new DevGroup());
        devGroupButton.setEnabled(false);

        setDeleteDevice(new Button("Удалить"));
        deleteDevice.addClickListener(new DeleteDevice(this));
        deleteDevice.setEnabled(false);

        tabDevice.addValueChangeListener(new DeviceTableListener(this));

        Button exit = new Button("Закрыть", new CloseWindow(window));

        custom.addComponent(addNewDevice, "addButton");
        custom.addComponent(devGroupButton, "groupDev");
        custom.addComponent(deleteDevice, "deleteButton");
        custom.addComponent(exit, "close");

        verticalLayout.addComponent(custom);

        formLayout.addComponent(verticalLayout);

        window.setContent(formLayout);
        UI.getCurrent().addWindow(window);
    }
}
