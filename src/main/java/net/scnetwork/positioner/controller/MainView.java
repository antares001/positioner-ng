package net.scnetwork.positioner.controller;

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

        MenuBar.MenuItem tracks = menuBar.addItem("Данные", null);
        tracks.addItem("Подключиться к БД", new ConnectDatabases(this));
        tracks.addItem("Загрузить файл", new LoadFile(this));

        if (isAdmin.equals("1")) {
            MenuBar.MenuItem admins = menuBar.addItem("Администрирование", null);

            admins.addItem("Базы данных", new DatabasesProfile());

            admins.addItem("Пользователи", new AdminUsersMenu());

            admins.addItem("Управление системой", new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    getUI().getNavigator().navigateTo(Settings.NAME);
                }
            });
        }

        MenuBar.Command exitCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getSession().setAttribute("user", null);
                getUI().getNavigator().navigateTo(NAME);
            }
        };

        MenuBar.MenuItem exit = menuBar.addItem("Выход", exitCommand);

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Tree treeDevices = new Tree("Треки");

        treeDevices.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                Notification.show(valueChangeEvent.getProperty().getValue().toString());
            }
        });
        treeDevices.setWidth(200, Unit.PIXELS);

        horizontalLayout.addComponent(treeDevices);

        setGoogleMap(new GoogleMap(null, null, null));
        googleMap.setCenter(new LatLon(46.85, 29.60));
        googleMap.setZoom(14);
        googleMap.setHeight(1020, Unit.PIXELS);
        googleMap.setWidth(1700, Unit.PIXELS);

        horizontalLayout.addComponent(googleMap);
        main.addComponent(horizontalLayout);
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
