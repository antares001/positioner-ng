package pmr.mvd.positioner.controller;

import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.Devices;
import pmr.mvd.positioner.bean.Positions;
import pmr.mvd.positioner.bean.Report;
import pmr.mvd.positioner.dao.SqlDao;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainView extends CustomComponent implements View, Action.Handler, Property.ValueChangeListener{
    public static final String NAME = "main";

    private SqlDao dao = new SqlDao();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private String oneTS = "";
    private String oneFirstDate = sdf.format(new Date());
    private String oneLastDate = sdf.format(new Date());
    
    private Table statusCar = new Table("Статус утройства");
    private GoogleMap googleMap = new GoogleMap(null,null,null);

    Label text = new Label();

    private List<Component> layers = new LinkedList<Component>();

    public MainView(){
        final VerticalLayout main = new VerticalLayout();
        main.setSpacing(true);

        MenuBar menuBar = new MenuBar();
        main.addComponent(menuBar);

        MenuBar.MenuItem automobile = menuBar.addItem("Транспортные средства", null);
        
        ArrayList<Devices> deviceses = dao.GetDevices();
        MenuBar.Command setDataDevices = new SetDataDevices();
        for(Devices device : deviceses){
            automobile.addItem(device.getName(), setDataDevices);
        }
        
        
        MenuBar.MenuItem admins = menuBar.addItem("Администрирование",null);
        MenuBar.MenuItem print = menuBar.addItem("Отчеты",null);

        MenuBar.Command exitCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getSession().setAttribute("user", null);
                getUI().getNavigator().navigateTo(NAME);
            }
        };
        MenuBar.MenuItem exit = menuBar.addItem("Выход", exitCommand);

        MenuBar.Command addTs = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                final Window windowAddTs = new Window("Добавление нового транспортного средства");
                windowAddTs.setWidth(800.0f, Unit.PIXELS);
                windowAddTs.setHeight(600.0f, Unit.PIXELS);
                windowAddTs.setModal(true);
                final FormLayout formLayout = new FormLayout();
                windowAddTs.setContent(formLayout);
                UI.getCurrent().addWindow(windowAddTs);
            }
        };
        admins.addItem("Добавить ТС", addTs);

        MenuBar.Command addUser = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                final Window windowAddUser = new Window("Добавление нового пользователя");
                windowAddUser.setWidth(800.0f, Unit.PIXELS);
                windowAddUser.setHeight(600.0f, Unit.PIXELS);
                windowAddUser.setModal(true);
                final FormLayout formLayout = new FormLayout();
                windowAddUser.setContent(formLayout);
                UI.getCurrent().addWindow(windowAddUser);
            }
        };
        admins.addItem("Добавить пользователя", addUser);

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
        admins.addItem("Управление", control);

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
                final Window windowPrintGroup = new Window("Отчет для группы ТС");
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
                Notification.show(statusCar.getId());
            }
        });
        main.addComponent(statusCar);

        setCompositionRoot(main);
    }
    
    private class SetDataDevices implements MenuBar.Command{
        @Override
        public void menuSelected(MenuBar.MenuItem selectedItem) {
            Collection items = statusCar.getItemIds();
            if(items.size() != 0){
                statusCar.removeAllItems();
            }
            ArrayList<Positions> positionses = dao.GetPositions(selectedItem.getText());
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
