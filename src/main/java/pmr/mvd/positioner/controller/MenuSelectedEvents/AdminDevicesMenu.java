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
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.ArrayList;

public class AdminDevicesMenu implements MenuBar.Command{
    private SqlDao dao = new SqlDao();

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        final HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        final Window windowAddTs = new Window("Управление транспортными средствами");
        windowAddTs.setWidth(600.0f, Sizeable.Unit.PIXELS);
        windowAddTs.setHeight(400.0f, Sizeable.Unit.PIXELS);
        windowAddTs.setModal(true);
        final FormLayout formLayout = new FormLayout();

        VerticalLayout verticalLayout = new VerticalLayout();

        Table tabDevice = new Table("Транспортные средства");
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

        final Button addNewDevice = new Button("Добавить", new AddDevice());

        final Button devGroupButton = new Button("Пользователи", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                final Window winChangeDev = new Window("Список пользователей для данного ТС");
                winChangeDev.setWidth(600.0f, Sizeable.Unit.PIXELS);
                winChangeDev.setHeight(400.0f, Sizeable.Unit.PIXELS);
                winChangeDev.setModal(true);

                FormLayout chDevLayaout = new FormLayout();

                VerticalLayout vDev = new VerticalLayout();

                Table tabDevGroup = new Table("Список пользователей");
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
                                        if (dao.AddGroupUser(nameuser, namedevice))
                                            winAddGroupUser.close();
                                        else
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

                        if (dao.DelGroupDev(nmas, namedevice)){
                            winChangeDev.close();
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
        });
        devGroupButton.setEnabled(false);

        final Button deleteDevice = new Button("Удалить", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                ArrayList<Devices> devices = dao.GetDevices();
                try {
                    Devices d = devices.get(Integer.parseInt(hidden.pullUp("delete_device")) - 1);
                    if (dao.DelDevice(d.getName()))
                        Notification.show("Удалено транс. средство: " + d.getName() + "");
                    else
                        Notification.show("Ошибка удаления ТС");
                } catch (Exception e){
                    Notification.show("ТС уже удалено");
                }
            }
        });
        deleteDevice.setEnabled(false);

        tabDevice.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                deleteDevice.setEnabled(true);
                devGroupButton.setEnabled(true);
                hidden.pullDown("delete_device", String.valueOf(valueChangeEvent.getProperty().getValue()));
            }
        });

        Button exit = new Button("Закрыть",new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                windowAddTs.close();
            }
        });

        custom.addComponent(addNewDevice, "addButton");
        custom.addComponent(devGroupButton, "groupDev");
        custom.addComponent(deleteDevice, "deleteButton");
        custom.addComponent(exit, "close");

        verticalLayout.addComponent(custom);

        formLayout.addComponent(verticalLayout);

        windowAddTs.setContent(formLayout);
        UI.getCurrent().addWindow(windowAddTs);
    }
}
