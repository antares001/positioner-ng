package pmr.mvd.positioner.controller.MenuSelectedEvents;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.Devices;
import pmr.mvd.positioner.bean.GroupDev;
import pmr.mvd.positioner.bean.UserSettings;
import pmr.mvd.positioner.controller.ButtonEvents.AddUser;
import pmr.mvd.positioner.controller.ButtonEvents.ChangeGroup;
import pmr.mvd.positioner.controller.ButtonEvents.ChangePassword;
import pmr.mvd.positioner.controller.ButtonEvents.DeleteUserConfirm;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.ArrayList;

public class AdminUsersMenu implements MenuBar.Command{
    private SqlDao dao = new SqlDao();

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        final HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        final Window windowAddUser = new Window("Управление пользователями");
        windowAddUser.setWidth(850.0f, Sizeable.Unit.PIXELS);
        windowAddUser.setHeight(400.0f, Sizeable.Unit.PIXELS);
        windowAddUser.setModal(true);
        final FormLayout formLayout = new FormLayout();

        VerticalLayout vertical = new VerticalLayout();

        Table tabUsers = new Table("Пользователи");
        tabUsers.setSelectable(true);

        tabUsers.addContainerProperty("Логин", String.class, null);
        tabUsers.addContainerProperty("Группа", String.class, null);

        final ArrayList<UserSettings> users = dao.GetUsers();
        for(UserSettings settings : users){
            String group = settings.getGroup();
            if (group.equals("1"))
                group = "Администратор";
            else if (group.equals("0"))
                group = "Пользователь";

            Object newItem = tabUsers.addItem();
            Item row = tabUsers.getItem(newItem);
            row.getItemProperty("Логин").setValue(settings.getUsername());
            row.getItemProperty("Группа").setValue(group);
        }

        tabUsers.setPageLength(5);
        tabUsers.setWidth(800.0f, Sizeable.Unit.PIXELS);

        vertical.addComponent(tabUsers);

        final CustomLayout custom = new CustomLayout("buttons");

        Button addNewUser = new Button("Добавить", new AddUser());

        final Button changePass = new Button("Сменить пароль", new ChangePassword());
        changePass.setEnabled(false);

        final Button changeGroup = new Button("Сменить группу", new ChangeGroup());
        changeGroup.setEnabled(false);

        final Button changeDev = new Button("ТС", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                final Window winChangeDev = new Window("Список транспортных средств пользователя");
                winChangeDev.setWidth(600.0f, Sizeable.Unit.PIXELS);
                winChangeDev.setHeight(400.0f, Sizeable.Unit.PIXELS);
                winChangeDev.setModal(true);

                FormLayout chDevLayaout = new FormLayout();

                VerticalLayout vDev = new VerticalLayout();

                Table tabDevGroup = new Table("Транспортные средства");
                tabDevGroup.setSelectable(true);

                tabDevGroup.setPageLength(5);
                tabDevGroup.setWidth(550.0f, Sizeable.Unit.PIXELS);

                tabDevGroup.addContainerProperty("Транспортное средство", String.class, null);

                final String nameuser = hidden.pullUp("selected_user");

                final ArrayList<GroupDev> devGroup = dao.GetGroupUser(nameuser);
                for (GroupDev groupDev : devGroup){
                    try {
                        String name = groupDev.getDevice();

                        Object newItem = tabDevGroup.addItem();
                        Item row = tabDevGroup.getItem(newItem);
                        row.getItemProperty("Транспортное средство").setValue(name);
                    } catch (NullPointerException ignored){}
                }

                vDev.addComponent(tabDevGroup);

                CustomLayout customDevGroup = new CustomLayout("usergroup");

                final Button add = new Button("Добавить", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        final Window winTSUser = new Window("Добавление транспортного средства для пользователя");
                        winTSUser.setModal(true);
                        winTSUser.setWidth(400.0f, Sizeable.Unit.PIXELS);
                        winTSUser.setHeight(200.0f, Sizeable.Unit.PIXELS);

                        FormLayout flTsUsers = new FormLayout();
                        CustomLayout clTsUsers = new CustomLayout("changegroup");

                        final ComboBox comboBoxTsDev = new ComboBox();
                        comboBoxTsDev.setWidth(200.0f, Sizeable.Unit.PIXELS);
                        comboBoxTsDev.setImmediate(true);
                        comboBoxTsDev.setNullSelectionAllowed(false);

                        final ArrayList<Devices> tsDevices = dao.GetDevices();
                        for(Devices ts : tsDevices){
                            comboBoxTsDev.addItem(ts.getName());
                        }
                        clTsUsers.addComponent(comboBoxTsDev, "group");

                        final Button saveDevGroup = new Button("Добавить", new Button.ClickListener() {
                            @Override
                            public void buttonClick(Button.ClickEvent clickEvent) {
                                try {
                                    String namedevice = comboBoxTsDev.getValue().toString();
                                    boolean newDevice = true;
                                    for (GroupDev gd : devGroup) {
                                        if (namedevice.equals(gd.getDevice()))
                                            newDevice = false;
                                    }

                                    if (newDevice) {
                                        if (dao.AddGroupUser(nameuser, namedevice))
                                            winTSUser.close();
                                        else
                                            Notification.show("Ошибка добавления ТС");
                                    } else {
                                        Notification.show("Такое ТС уже есть");
                                    }
                                } catch (NullPointerException e){
                                    Notification.show("Не выбрано ТС");
                                }
                            }
                        });
                        clTsUsers.addComponent(saveDevGroup, "save");

                        final Button closeDevGroup = new Button("Отмена", new Button.ClickListener() {
                            @Override
                            public void buttonClick(Button.ClickEvent clickEvent) {
                                winTSUser.close();
                            }
                        });
                        clTsUsers.addComponent(closeDevGroup, "close");

                        flTsUsers.addComponent(clTsUsers);
                        winTSUser.setContent(flTsUsers);

                        UI.getCurrent().addWindow(winTSUser);
                    }
                });
                customDevGroup.addComponent(add, "add");

                final Button del = new Button("Удалить", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        String mDev = hidden.pullUp("delete_groupdevice");
                        String nn = devGroup.get(Integer.parseInt(mDev) - 1).getDevice();
                        if (dao.DelGroupDev(nameuser, nn)){
                            winChangeDev.close();
                        } else {
                            Notification.show("Ошибка удаления транспортного средства");
                        }
                    }
                });
                del.setEnabled(false);
                customDevGroup.addComponent(del, "delete");

                tabDevGroup.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                        del.setEnabled(true);
                        hidden.pullDown("delete_groupdevice", String.valueOf(valueChangeEvent.getProperty().getValue()));
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
        changeDev.setEnabled(false);

        final Button delete = new Button("Удалить", new DeleteUserConfirm());
        delete.setEnabled(false);

        tabUsers.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                changePass.setEnabled(true);
                changeGroup.setEnabled(true);
                changeDev.setEnabled(true);
                delete.setEnabled(true);
                String numUser = String.valueOf(valueChangeEvent.getProperty().getValue());
                int k = 1;
                for (UserSettings user : users){
                    if (numUser.equals(String.valueOf(k)))
                        setSelectedUsers(user.getUsername());
                    k++;
                }
            }
        });

        Button exit = new Button("Закрыть",new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                windowAddUser.close();
            }
        });

        custom.addComponent(addNewUser, "addButton");
        custom.addComponent(changePass, "changePass");
        custom.addComponent(changeGroup, "changeGroup");
        custom.addComponent(changeDev, "groupDev");
        custom.addComponent(delete, "deleteButton");
        custom.addComponent(exit, "close");

        vertical.addComponent(custom);

        formLayout.addComponent(vertical);

        windowAddUser.setContent(formLayout);
        UI.getCurrent().addWindow(windowAddUser);
    }

    private void setSelectedUsers(String arg){
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        hidden.pullDel("selected_user");
        hidden.pullDown("selected_user", arg);
    }
}
