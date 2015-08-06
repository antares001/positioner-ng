package net.scnetwork.positioner.controller;

import com.github.wolfie.refresher.Refresher;
import com.vaadin.annotations.Push;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.*;
import net.scnetwork.positioner.controller.MenuSelectedEvents.*;
import net.scnetwork.positioner.utils.HiddenVariable;

import java.util.ArrayList;

@Push
public class MainView extends CustomComponent implements View, Action.Handler, Property.ValueChangeListener{
    public static final String NAME = "main";

    //private SqlDao dao = new SqlDao();

    private GoogleMap googleMap;
    private GoogleMapPolyline polyline;
    private String username;

    private ArrayList<GoogleMapMarker> list;

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
        //String username = "";
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

        MenuBar.MenuItem tracks = menuBar.addItem("Треки", null);
        tracks.addItem("Показать трек выбранных ТС", new SetPathDevice(this));
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

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        /*Tree treeDevices = new Tree("Транспортные. средства");
        ArrayList<GroupDev> deviceses = dao.GetDevices(username);
        for (GroupDev groupDev : deviceses){
            treeDevices.addItem(groupDev.getDevice());
        }
        treeDevices.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                Notification.show(valueChangeEvent.getProperty().getValue().toString());
            }
        });
        treeDevices.setWidth(200, Unit.PIXELS);

        horizontalLayout.addComponent(treeDevices);*/

        setGoogleMap(new GoogleMap(null, null, null));
        googleMap.setCenter(new LatLon(46.85, 29.60));
        googleMap.setZoom(14);
        googleMap.setHeight(1020, Unit.PIXELS);
        googleMap.setWidth(1700, Unit.PIXELS);

        RefreshData(username);

        horizontalLayout.addComponent(googleMap);
        main.addComponent(horizontalLayout);

        Refresher refresher = new Refresher();
        refresher.setRefreshInterval(60000);
        refresher.addListener(new Refresher.RefreshListener() {
            @Override
            public void refresh(Refresher refresher) {
                if (list.size() != 0){
                    for (int i = 0; i < list.size(); i++){
                        GoogleMapMarker marker = list.get(i);
                        googleMap.removeMarker(marker);
                        list.remove(i);
                    }
                    RefreshData(username);
                }
            }
        });

        addExtension(refresher);
        setCompositionRoot(main);
    }

    private void RefreshData(String username){
        /*ArrayList<DevPoint> points = dao.getLastPosition(username);
        if (points.size() != 0) {
            list = new ArrayList<GoogleMapMarker>();
            for (DevPoint point : points) {
                GoogleMapMarker googleMapMarker = googleMap.addMarker(point.getName(), new LatLon(Double.parseDouble(point.getLat()), Double.parseDouble(point.getLon())), false, null);
                list.add(googleMapMarker);
            }
        }*/
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
