package pmr.mvd.positioner.controller;

import com.vaadin.annotations.Push;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.*;
import pmr.mvd.positioner.bean.DevPoint;
import pmr.mvd.positioner.bean.GroupDev;
import pmr.mvd.positioner.controller.MenuSelectedEvents.*;
import pmr.mvd.positioner.controller.TableChangeListener.StatusCarListener;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.ArrayList;

@Push
public class MainView extends CustomComponent implements View, Action.Handler, Property.ValueChangeListener{
    public static final String NAME = "main";

    private SqlDao dao = new SqlDao();
    
    private Table statusCar;
    private GoogleMap googleMap;
    private GoogleMapPolyline polyline;

    public Table getStatusCar(){
        return this.statusCar;
    }

    public void setStatusCar(Table arg){
        this.statusCar = arg;
    }

    public GoogleMap getGoogleMap(){
        return this.googleMap;
    }

    public void setGoogleMap(GoogleMap arg){
        this.googleMap = arg;
    }

    public GoogleMapPolyline getPolyline(){
        return this.polyline;
    }

    public void setPolyline(GoogleMapPolyline arg){
        this.polyline = arg;
    }

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
        
        ArrayList<GroupDev> deviceses = dao.GetDevices(username);
        for(GroupDev device : deviceses){
            automobile.addItem(device.getDevice(), new SetDataDevices(this));
        }

        MenuBar.MenuItem tracks = menuBar.addItem("Треки", null);
        tracks.addItem("Показать трек выбранного ТС", new SetPathDevice(this));
        tracks.addItem("Убрать все треки", new ClearAllPath(this));

        if (isAdmin.equals("1")) {
            MenuBar.MenuItem admins = menuBar.addItem("Администрирование", null);

            admins.addItem("Транспортные средства", new AdminDevicesMenu());

            admins.addItem("Пользователи", new AdminUsersMenu());

            admins.addItem("Управление системой", new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    getUI().getNavigator().navigateTo(Settings.NAME);
                }
            });
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
        print.addItem("Отчет для одного ТС", new PrintOne());
        print.addItem("Отчет для группы ТС", new PrintGroup());


        setGoogleMap(new GoogleMap(null, null, null));
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

        setStatusCar(new Table("Статус утройства"));
        statusCar.setSelectable(true);
        statusCar.setHeight("300px");
        statusCar.setWidth("100%");

        statusCar.addContainerProperty("Широта", String.class, null);
        statusCar.addContainerProperty("Долгота", String.class, null);
        statusCar.addContainerProperty("Направление", String.class, null);
        statusCar.addContainerProperty("Скорость", String.class, null);
        statusCar.addContainerProperty("Время", String.class, null);

        statusCar.setPageLength(statusCar.size());
        
        statusCar.addValueChangeListener(new StatusCarListener(this));
        main.addComponent(statusCar);

        setCompositionRoot(main);
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
