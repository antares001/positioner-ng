package pmr.mvd.positioner.controller.MenuSelectedEvents;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.Devices;
import pmr.mvd.positioner.bean.GroupDev;
import pmr.mvd.positioner.bean.UserSettings;
import pmr.mvd.positioner.controller.ButtonEvents.AddDevice;
import pmr.mvd.positioner.controller.ButtonEvents.CloseWindow;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminDevicesMenu implements MenuBar.Command{
    private SqlDao dao = new SqlDao();

    private Window window = new Window("Управление транспортными средствами");
    private Table tabDevice = new Table("Транспортные средства");
    private HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
    private Button deleteDevice = new Button("Удалить");
    private Button devGroupButton = new Button("Пользователи");

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

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        window.setWidth(600.0f, Sizeable.Unit.PIXELS);
        window.setHeight(400.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);
        final FormLayout formLayout = new FormLayout();

        VerticalLayout verticalLayout = new VerticalLayout();

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

        devGroupButton.addClickListener(new DevGroup());
        devGroupButton.setEnabled(false);

        deleteDevice.addClickListener(new DeleteDevice());
        deleteDevice.setEnabled(false);

        tabDevice.addValueChangeListener(new DeviceTableListener());

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

    private class DeviceTableListener implements Property.ValueChangeListener{
        @Override
        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
            deleteDevice.setEnabled(true);
            devGroupButton.setEnabled(true);
            hidden.pullDown("delete_device", String.valueOf(valueChangeEvent.getProperty().getValue()));
        }
    }

    private class DeleteDevice implements Button.ClickListener{
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            ArrayList<Devices> devices = dao.GetDevices();
            try {
                Devices d = devices.get(Integer.parseInt(hidden.pullUp("delete_device")) - 1);

                HashMap<String,String> params = new HashMap<String, String>();
                params.put("name", d.getName());

                if (dao.ExecuteOperation(params, "del_device"))
                    Notification.show("Удалено транс. средство: " + d.getName() + "");
                else
                    Notification.show("Ошибка удаления ТС");
            } catch (Exception e){
                Notification.show("ТС уже удалено");
            }
        }
    }

    private class DevGroup implements Button.ClickListener{
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            final Window winChangeDev = new Window("Список пользователей для данного ТС");
            winChangeDev.setWidth(600.0f, Sizeable.Unit.PIXELS);
            winChangeDev.setHeight(400.0f, Sizeable.Unit.PIXELS);
            winChangeDev.setModal(true);

            FormLayout chDevLayaout = new FormLayout();

            VerticalLayout vDev = new VerticalLayout();

            final Table tabDevGroup = new Table("Список пользователей");
            tabDevGroup.setSelectable(true);

            tabDevGroup.setPageLength(5);
            tabDevGroup.setWidth(550.0f, Sizeable.Unit.PIXELS);

            tabDevGroup.addContainerProperty("Пользователь", String.class, null);

            ArrayList<Devices> devices = dao.GetDevices();
            Devices d = devices.get(Integer.parseInt(hidden.pullUp("delete_device")) - 1);
            final String namedevice = d.getName();

            final ArrayList<GroupDev> userGroup = dao.GetGroupDev(namedevice);
            for (GroupDev groupDev : userGroup){
                try{
                    String name = groupDev.getUser();

                    Object newItem = tabDevGroup.addItem();
                    Item row = tabDevGroup.getItem(newItem);
                    row.getItemProperty("Пользователь").setValue(name);
                }catch (NullPointerException ignored){}
            }

            vDev.addComponent(tabDevGroup);

            CustomLayout customDevGroup = new CustomLayout("usergroup");

            final Button add = new Button("Добавить", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    final Window winAddGroupUser = new Window("Новый пользователь");
                    winAddGroupUser.setModal(true);
                    winAddGroupUser.setWidth(400.0f, Sizeable.Unit.PIXELS);
                    winAddGroupUser.setHeight(200.0f, Sizeable.Unit.PIXELS);

                    FormLayout addGroupUserLayout = new FormLayout();
                    CustomLayout layout0 = new CustomLayout("changegroup");

                    final ComboBox comboBoxUsers = new ComboBox();
                    comboBoxUsers.setWidth(200.0f, Sizeable.Unit.PIXELS);
                    comboBoxUsers.setImmediate(true);
                    comboBoxUsers.setNullSelectionAllowed(false);

                    final ArrayList<UserSettings> listUsers = dao.GetUsers();
                    for (UserSettings us : listUsers){
                        comboBoxUsers.addItem(us.getUsername());
                    }
                    layout0.addComponent(comboBoxUsers, "group");

                    final Button saveGroupUser = new Button("Добавить", new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent clickEvent) {
                            try{
                                String nameuser = comboBoxUsers.getValue().toString();
                                boolean newUser = true;
                                for(GroupDev settingsUser : userGroup){
                                    if (nameuser.equals(settingsUser.getUser()))
                                        newUser = false;
                                }
                                if (newUser) {
                                    HashMap<String,String> params = new HashMap<String, String>();
                                    params.put("user", nameuser);
                                    params.put("device", namedevice);

                                    if (dao.ExecuteOperation(params, "add_group_user")) {
                                        winAddGroupUser.close();

                                        tabDevGroup.removeAllItems();

                                        final ArrayList<GroupDev> userGroup = dao.GetGroupDev(namedevice);
                                        for (GroupDev groupDev : userGroup){
                                            try{
                                                String name = groupDev.getUser();

                                                Object newItem = tabDevGroup.addItem();
                                                Item row = tabDevGroup.getItem(newItem);
                                                row.getItemProperty("Пользователь").setValue(name);
                                            }catch (NullPointerException ignored){}
                                        }
                                    }else
                                        Notification.show("Ошибка добавления пользователя");
                                } else
                                    Notification.show("Такой пользователь уже есть");
                            } catch (NullPointerException e){
                                Notification.show("Не выбран пользователь");
                            }
                        }
                    });
                    layout0.addComponent(saveGroupUser, "save");

                    final Button closeGroupUser = new Button("Отмена", new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent clickEvent) {
                            winAddGroupUser.close();
                        }
                    });
                    layout0.addComponent(closeGroupUser, "close");

                    addGroupUserLayout.addComponent(layout0);

                    winAddGroupUser.setContent(addGroupUserLayout);
                    UI.getCurrent().addWindow(winAddGroupUser);
                }
            });
            customDevGroup.addComponent(add, "add");

            final Button del = new Button("Удалить", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    String uName = hidden.pullUp("delete_groupuser");
                    String nmas = userGroup.get(Integer.parseInt(uName) - 1).getUser();

                    HashMap<String,String> params = new HashMap<String, String>();
                    params.put("user", nmas);
                    params.put("device", namedevice);

                    if (dao.ExecuteOperation(params, "del_group_dev")){
                        tabDevGroup.removeAllItems();

                        final ArrayList<GroupDev> userGroup = dao.GetGroupDev(namedevice);
                        for (GroupDev groupDev : userGroup){
                            try{
                                String name = groupDev.getUser();

                                Object newItem = tabDevGroup.addItem();
                                Item row = tabDevGroup.getItem(newItem);
                                row.getItemProperty("Пользователь").setValue(name);
                            }catch (NullPointerException ignored){}
                        }
                    } else {
                        Notification.show("Ошибка удаления пользователя");
                    }
                }
            });
            del.setEnabled(false);
            customDevGroup.addComponent(del, "delete");

            tabDevGroup.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    del.setEnabled(true);
                    hidden.pullDown("delete_groupuser", String.valueOf(valueChangeEvent.getProperty().getValue()));
                }
            });

            final Button close = new Button("Закрыть", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    winChangeDev.close();
                }
            });
            customDevGroup.addComponent(close, "exit");
            vDev.addComponent(customDevGroup);

            chDevLayaout.addComponent(vDev);
            winChangeDev.setContent(chDevLayaout);
            UI.getCurrent().addWindow(winChangeDev);
        }
    }
}
