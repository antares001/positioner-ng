package pmr.mvd.positioner.controller;

import com.vaadin.annotations.Push;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.*;
import pmr.mvd.positioner.controller.ButtonEvents.*;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.text.SimpleDateFormat;
import java.util.*;

@Push
public class MainView extends CustomComponent implements View, Action.Handler, Property.ValueChangeListener{
    public static final String NAME = "main";

    private SqlDao dao = new SqlDao();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    
    private Table statusCar = new Table("Статус утройства");
    private GoogleMap googleMap = new GoogleMap(null,null,null);
    private GoogleMapPolyline polyline;

    public MainView(){
        final HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        String isAdmin = "0";
        String username = "";
        try{
            isAdmin = hidden.pullUp("admin");
            username = hidden.pullUp("username");
            if (null == isAdmin)
                isAdmin = "0";
        }catch (NullPointerException ignored){}

        final VerticalLayout main = new VerticalLayout();
        main.setSpacing(true);

        MenuBar menuBar = new MenuBar();
        main.addComponent(menuBar);

        MenuBar.MenuItem automobile = menuBar.addItem("Транспортные средства", null);
        
        ArrayList<Devices> deviceses = dao.GetDevices();
        final MenuBar.Command setDataDevices = new SetDataDevices();
        for(Devices device : deviceses){
            automobile.addItem(device.getName(), setDataDevices);
        }

        MenuBar.MenuItem tracks = menuBar.addItem("Треки", null);
        tracks.addItem("Показать трек выбранного ТС", new SetPathDevice());
        tracks.addItem("Убрать все треки", new ClearAllPaths());

        if (isAdmin.equals("1")) {
            MenuBar.MenuItem admins = menuBar.addItem("Администрирование", null);

            MenuBar.Command addTs = new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    final Window windowAddTs = new Window("Управление транспортными средствами");
                    windowAddTs.setWidth(600.0f, Unit.PIXELS);
                    windowAddTs.setHeight(400.0f, Unit.PIXELS);
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
                            winChangeDev.setWidth(600.0f, Unit.PIXELS);
                            winChangeDev.setHeight(400.0f, Unit.PIXELS);
                            winChangeDev.setModal(true);

                            FormLayout chDevLayaout = new FormLayout();

                            VerticalLayout vDev = new VerticalLayout();

                            Table tabDevGroup = new Table("Список пользователей");
                            tabDevGroup.setSelectable(true);

                            tabDevGroup.setPageLength(5);
                            tabDevGroup.setWidth(550.0f, Unit.PIXELS);

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
                                    winAddGroupUser.setWidth(400.0f, Unit.PIXELS);
                                    winAddGroupUser.setHeight(200.0f, Unit.PIXELS);

                                    FormLayout addGroupUserLayout = new FormLayout();
                                    CustomLayout layout0 = new CustomLayout("changegroup");

                                    final ComboBox comboBoxUsers = new ComboBox();
                                    comboBoxUsers.setWidth(200.0f, Unit.PIXELS);
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
            };
            admins.addItem("Транспортные средства", addTs);

            MenuBar.Command addUser = new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    final Window windowAddUser = new Window("Управление пользователями");
                    windowAddUser.setWidth(850.0f, Unit.PIXELS);
                    windowAddUser.setHeight(400.0f, Unit.PIXELS);
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
                    tabUsers.setWidth(800.0f, Unit.PIXELS);

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
                            winChangeDev.setWidth(600.0f, Unit.PIXELS);
                            winChangeDev.setHeight(400.0f, Unit.PIXELS);
                            winChangeDev.setModal(true);

                            FormLayout chDevLayaout = new FormLayout();

                            VerticalLayout vDev = new VerticalLayout();

                            Table tabDevGroup = new Table("Транспортные средства");
                            tabDevGroup.setSelectable(true);

                            tabDevGroup.setPageLength(5);
                            tabDevGroup.setWidth(550.0f, Unit.PIXELS);

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
                                    winTSUser.setWidth(400.0f, Unit.PIXELS);
                                    winTSUser.setHeight(200.0f, Unit.PIXELS);

                                    FormLayout flTsUsers = new FormLayout();
                                    CustomLayout clTsUsers = new CustomLayout("changegroup");

                                    final ComboBox comboBoxTsDev = new ComboBox();
                                    comboBoxTsDev.setWidth(200.0f, Unit.PIXELS);
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
            };
            admins.addItem("Пользователи", addUser);

            MenuBar.Command control = new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    getUI().getNavigator().navigateTo(Settings.NAME);
                }
            };
            admins.addItem("Управление системой", control);
        }
        MenuBar.MenuItem print = menuBar.addItem("Отчеты",null);

        MenuBar.Command exitCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getSession().setAttribute("user", null);
                getUI().getNavigator().navigateTo(NAME);
            }
        };

        MenuBar.MenuItem exit = menuBar.addItem("Выход", exitCommand);

        /**
         * Окно для задания параметров печати отчета для одного транспортного средства.
         */
        MenuBar.Command printOne = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                final Window windowAddTs = new Window("Отчет для одного транспортного средства");
                windowAddTs.setWidth(750.0f, Unit.PIXELS);
                windowAddTs.setHeight(230.0f, Unit.PIXELS);
                windowAddTs.setModal(true);
                final FormLayout formLayout = new FormLayout();
                final CustomLayout customLayout = new CustomLayout("printone");

                final ComboBox comboBoxName = new ComboBox();
                comboBoxName.setWidth(100.0f, Unit.PERCENTAGE);
                comboBoxName.setFilteringMode(FilteringMode.CONTAINS);
                comboBoxName.setImmediate(true);
                comboBoxName.setNullSelectionAllowed(false);

                ArrayList<Devices> dev = dao.GetDevices();
                for(Devices d : dev){
                    comboBoxName.addItem(d.getName());
                }
                OneTsName oneTsName = new OneTsName();
                comboBoxName.addValueChangeListener(oneTsName);
                customLayout.addComponent(comboBoxName, "comboBoxName");

                final PopupDateField fromDate = new PopupDateField();
                fromDate.setValue(new Date());
                fromDate.setWidth(100.0f,Unit.PERCENTAGE);
                fromDate.setImmediate(true);
                fromDate.setTimeZone(TimeZone.getTimeZone("Europe/Tiraspol"));
                fromDate.setLocale(Locale.forLanguageTag("RU"));
                fromDate.setResolution(Resolution.MINUTE);
                OneFirstDate firstDate = new OneFirstDate();
                fromDate.addValueChangeListener(firstDate);
                customLayout.addComponent(fromDate, "fromDate");

                final PopupDateField toDate = new PopupDateField();
                toDate.setWidth(100.0f,Unit.PERCENTAGE);
                toDate.setValue(new Date());
                toDate.setImmediate(true);
                toDate.setTimeZone(TimeZone.getTimeZone("Europe/Tiraspol"));
                toDate.setLocale(Locale.forLanguageTag("RU"));
                toDate.setResolution(Resolution.MINUTE);
                OneLastDate lastDate = new OneLastDate();
                toDate.addValueChangeListener(lastDate);
                customLayout.addComponent(toDate, "toDate");

                final Button printReport = new Button("Распечатать");
                PrintOneReport printOneReport = new PrintOneReport();
                printReport.addClickListener(printOneReport);
                customLayout.addComponent(printReport, "printReport");

                final Button closeWindow = new Button("Закрыть", new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        windowAddTs.close();
                    }
                });
                customLayout.addComponent(closeWindow, "closeWindow");

                formLayout.addComponent(customLayout);
                windowAddTs.setContent(formLayout);
                UI.getCurrent().addWindow(windowAddTs);
            }
        };
        print.addItem("Отчет для одного ТС", printOne);

        MenuBar.Command printGroup = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                final Window windowPrintGroup = new Window("Отчет для всей группы ТС");
                windowPrintGroup.setWidth(800.0f, Unit.PIXELS);
                windowPrintGroup.setModal(true);
                final FormLayout formLayout = new FormLayout();
                windowPrintGroup.setContent(formLayout);
                UI.getCurrent().addWindow(windowPrintGroup);
            }
        };
        print.addItem("Отчет для группы ТС", printGroup);


        googleMap.setCenter(new LatLon(46.85, 29.60));
        googleMap.setZoom(14);
        googleMap.setSizeFull();
        googleMap.setHeight("700px");

        ArrayList<DevPoint> points = dao.getLastPosition(username);
        if (points.size() != 0) {
            for (DevPoint point : points) {
                googleMap.addMarker(point.getName(), new LatLon(Double.parseDouble(point.getLat()), Double.parseDouble(point.getLon())), false, null);
            }
        }

        main.addComponent(googleMap);

        statusCar.setSelectable(true);
        statusCar.setHeight("300px");
        statusCar.setWidth("100%");

        statusCar.addContainerProperty("Широта", String.class, null);
        statusCar.addContainerProperty("Долгота", String.class, null);
        statusCar.addContainerProperty("Направление", String.class, null);
        statusCar.addContainerProperty("Скорость", String.class, null);
        statusCar.addContainerProperty("Время", String.class, null);

        statusCar.setPageLength(statusCar.size());
        
        statusCar.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                String message = "1";

                try {message = String.valueOf(event.getProperty().getValue());} catch (NumberFormatException ignored){}
                ArrayList<Positions> positionses = dao.GetPositions(hidden.pullUp("device"));

                try {
                    Positions pos = positionses.get(Integer.parseInt(message) - 1);
                    googleMap.setCenter(new LatLon(Double.parseDouble(pos.getLatitude()), Double.parseDouble(pos.getLongitude())));

                    Collection points = googleMap.getMarkers();
                    try{
                        for (Object marker : points) {
                            googleMap.removeMarker((GoogleMapMarker) marker);
                        }
                    } catch (Exception e){}

                    googleMap.addMarker(hidden.pullUp("device"), new LatLon(Double.parseDouble(pos.getLatitude()), Double.parseDouble(pos.getLongitude())), false, null);

                    Notification.show("Транспортное средство: " + hidden.pullUp("device") + ", широта: " + pos.getLatitude() + ", долгота: " + pos.getLongitude());
                } catch (NumberFormatException ignored){}
            }
        });
        main.addComponent(statusCar);

        setCompositionRoot(main);
    }

    private void setSelectedUsers(String arg){
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        hidden.pullDel("selected_user");
        hidden.pullDown("selected_user", arg);
    }
    
    private class SetDataDevices implements MenuBar.Command{
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        @Override
        public void menuSelected(MenuBar.MenuItem selectedItem) {
            Collection items = statusCar.getItemIds();
            if(items.size() != 0){
                statusCar.removeAllItems();
            }
            ArrayList<Positions> positionses = dao.GetPositions(selectedItem.getText());
            hidden.pullDown("device", selectedItem.getText());
            if (positionses.size() != 0) {
                Positions first = positionses.get(0);
                googleMap.setCenter(new LatLon(Double.parseDouble(first.getLatitude()), Double.parseDouble(first.getLongitude())));
                Collection points = googleMap.getMarkers();

                try{
                    for (Object marker : points) {
                        googleMap.removeMarker((GoogleMapMarker) marker);
                    }
                } catch (Exception e){}

                googleMap.addMarker(selectedItem.getText(), new LatLon(Double.parseDouble(first.getLatitude()), Double.parseDouble(first.getLongitude())), false, null);
                int k = 1;
                for (Positions position : positionses) {
                    statusCar.addItem(new Object[]{position.getLatitude(), position.getLongitude(), position.getCourse(), position.getSpeed(), position.getTime()}, k);
                    k++;
                }
            } else {
                Collection points = googleMap.getMarkers();
                for (Object marker : points) {
                    googleMap.removeMarker((GoogleMapMarker) marker);
                }
                Notification.show("Для данного ТС нет данных");
            }
        }
    }

    private class SetPathDevice implements MenuBar.Command{
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        @Override
        public void menuSelected(MenuBar.MenuItem menuItem) {
            ArrayList<LatLon> pathPoints = dao.GetPathDevice(hidden.pullUp("device"));
            try{googleMap.removePolyline(polyline);} catch (NullPointerException ignored){}
            polyline = new GoogleMapPolyline(pathPoints, "#ff0000", 0.5, 5);
            googleMap.addPolyline(polyline);
        }
    }

    private class ClearAllPaths implements MenuBar.Command{
        @Override
        public void menuSelected(MenuBar.MenuItem menuItem) {
            try{
                googleMap.removePolyline(polyline);
            } catch (NullPointerException ignored){}
        }
    }

    private class OneFirstDate implements Property.ValueChangeListener{
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            hidden.pullDown("first_date", sdf.format(event.getProperty().getValue()));
        }
    }

    private class OneLastDate implements Property.ValueChangeListener{
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            hidden.pullDown("last_date", sdf.format(event.getProperty().getValue()));
        }
    }

    private class OneTsName implements Property.ValueChangeListener{
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            hidden.pullDown("combo_device", (String) event.getProperty().getValue());
        }
    }

    private class PrintOneReport implements Button.ClickListener{
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        @Override
        public void buttonClick(Button.ClickEvent event) {
            System.out.println("select * from positions where name='" + hidden.pullUp("combo_device") + "' and time > '" +
                    hidden.pullUp("first_date") + "' and time < '" + hidden.pullUp("last_date") + "'");
            ArrayList<Report> report = dao.GetReport(hidden.pullUp("combo_device"), hidden.pullUp("first_date"), hidden.pullUp("last_date"));
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //TODO: Основная форма
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        return new Action[0];
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        Notification.show("Click");
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {

    }
}
