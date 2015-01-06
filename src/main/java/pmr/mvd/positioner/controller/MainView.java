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
import pmr.mvd.positioner.controller.ButtonEvents.AddUser;
import pmr.mvd.positioner.controller.ButtonEvents.ChangeGroup;
import pmr.mvd.positioner.controller.ButtonEvents.ChangePassword;
import pmr.mvd.positioner.controller.ButtonEvents.DeleteUserConfirm;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.text.SimpleDateFormat;
import java.util.*;

@Push
public class MainView extends CustomComponent implements View, Action.Handler, Property.ValueChangeListener{
    public static final String NAME = "main";

    private SqlDao dao = new SqlDao();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private String oneTS = "";
    private String oneFirstDate = sdf.format(new Date());
    private String oneLastDate = sdf.format(new Date());
    private String dev = "";
    private String delDev = "0";
    
    private Table statusCar = new Table("Статус утройства");
    private GoogleMap googleMap = new GoogleMap(null,null,null);
    private GoogleMapPolyline polyline;

    Label text = new Label();

    private List<Component> layers = new LinkedList<Component>();

    private String databaseResult;

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
        //tracks.addItem("Задать период отображения трека", null);

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

                    Button addNewDevice = new Button("Добавить", new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent clickEvent) {
                            final Window addTs = new Window("Добавить");
                            final FormLayout formLayout = new FormLayout();
                            addTs.setWidth(400.0f, Unit.PIXELS);
                            addTs.setHeight(200.0f, Unit.PIXELS);
                            addTs.setModal(true);

                            final CustomLayout layout = new CustomLayout("newuser");

                            final TextField username = new TextField();
                            layout.addComponent(username, "nameInput");

                            final TextField idname = new TextField();
                            layout.addComponent(idname, "idInput");

                            final Button add = new Button("Добавить", new Button.ClickListener() {
                                @Override
                                public void buttonClick(Button.ClickEvent clickEvent) {
                                    if (username.getValue().equals("")){
                                        Notification.show("Не введено имя транспортного средства");
                                    } else if (idname.getValue().equals("")){
                                        Notification.show("Не введен уникальный идентификатор");
                                    } else {
                                        dao.AddNewDevice(username.getValue(), idname.getValue());
                                        addTs.close();
                                    }
                                }
                            });
                            layout.addComponent(add, "addbutton");

                            Button close = new Button("Закрыть", new Button.ClickListener() {
                                @Override
                                public void buttonClick(Button.ClickEvent clickEvent) {
                                    addTs.close();
                                }
                            });
                            layout.addComponent(close, "close");

                            formLayout.addComponent(layout);

                            addTs.setContent(formLayout);
                            UI.getCurrent().addWindow(addTs);
                        }
                    });

                    final Button deleteDevice = new Button("Удалить", new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent clickEvent) {
                            ArrayList<Devices> devices = dao.GetDevices();
                            try {
                                Devices d = devices.get(Integer.parseInt(delDev) - 1);
                                dao.DelDevice(d.getName());
                                Notification.show("Удалено транс. средство: " + d.getName() + "");
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
                            delDev = String.valueOf(valueChangeEvent.getProperty().getValue());
                        }
                    });

                    Button exit = new Button("Закрыть",new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            windowAddTs.close();
                        }
                    });

                    custom.addComponent(addNewDevice, "addButton");
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
                    windowAddUser.setWidth(800.0f, Unit.PIXELS);
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
                    tabUsers.setSizeFull();

                    vertical.addComponent(tabUsers);

                    final CustomLayout custom = new CustomLayout("buttons");

                    Button addNewUser = new Button("Добавить", new AddUser());

                    final Button changePass = new Button("Сменить пароль", new ChangePassword());
                    changePass.setEnabled(false);

                    final Button changeGroup = new Button("Сменить группу", new ChangeGroup());
                    changeGroup.setEnabled(false);

                    final Button delete = new Button("Удалить", new DeleteUserConfirm());
                    delete.setEnabled(false);

                    tabUsers.addValueChangeListener(new Property.ValueChangeListener() {
                        @Override
                        public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                            changePass.setEnabled(true);
                            changeGroup.setEnabled(true);
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
                    final Window windowControl = new Window("Управление");
                    windowControl.setWidth(800.0f, Unit.PIXELS);
                    windowControl.setHeight(600.0f, Unit.PIXELS);
                    windowControl.setModal(true);
                    final FormLayout formLayout = new FormLayout();
                    windowControl.setContent(formLayout);
                    UI.getCurrent().addWindow(windowControl);
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
                ArrayList<Positions> positionses = dao.GetPositions(dev);

                try {
                    Positions pos = positionses.get(Integer.parseInt(message) - 1);
                    googleMap.setCenter(new LatLon(Double.parseDouble(pos.getLatitude()), Double.parseDouble(pos.getLongitude())));

                    Collection points = googleMap.getMarkers();
                    for (Object marker : points) {
                        googleMap.removeMarker((GoogleMapMarker) marker);
                    }
                    googleMap.addMarker(dev, new LatLon(Double.parseDouble(pos.getLatitude()), Double.parseDouble(pos.getLongitude())), false, null);

                    Notification.show("Транспортное средство: " + dev + ", широта: " + pos.getLatitude() + ", долгота: " + pos.getLongitude());
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
        @Override
        public void menuSelected(MenuBar.MenuItem selectedItem) {
            Collection items = statusCar.getItemIds();
            if(items.size() != 0){
                statusCar.removeAllItems();
            }
            ArrayList<Positions> positionses = dao.GetPositions(selectedItem.getText());
            dev = selectedItem.getText();
            if (positionses.size() != 0) {
                Positions first = positionses.get(0);
                googleMap.setCenter(new LatLon(Double.parseDouble(first.getLatitude()), Double.parseDouble(first.getLongitude())));
                Collection points = googleMap.getMarkers();

                for (Object marker : points) {
                    googleMap.removeMarker((GoogleMapMarker) marker);
                }
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

        @Override
        public void menuSelected(MenuBar.MenuItem menuItem) {
            ArrayList<LatLon> pathPoints = dao.GetPathDevice(dev);
            try{googleMap.removePolyline(polyline);} catch (NullPointerException e){}
            polyline = new GoogleMapPolyline(pathPoints, "#ff0000", 0.5, 5);
            googleMap.addPolyline(polyline);
        }
    }

    private class OneFirstDate implements Property.ValueChangeListener{
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            oneFirstDate = sdf.format(event.getProperty().getValue());
        }
    }

    private class OneLastDate implements Property.ValueChangeListener{
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            oneLastDate = sdf.format(event.getProperty().getValue());
        }
    }

    private class OneTsName implements Property.ValueChangeListener{
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            oneTS = (String) event.getProperty().getValue();
        }
    }

    private class PrintOneReport implements Button.ClickListener{
        @Override
        public void buttonClick(Button.ClickEvent event) {
            System.out.println("select * from positions where name='" + oneTS + "' and time > '" + oneFirstDate + "' and time < '" + oneLastDate + "'");
            ArrayList<Report> report = dao.GetReport(oneTS, oneFirstDate, oneLastDate);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //TODO: Основная форма
        String username = "admin";
        text.setValue("Hello " + username);
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
