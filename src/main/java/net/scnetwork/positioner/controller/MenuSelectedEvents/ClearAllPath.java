package net.scnetwork.positioner.controller.MenuSelectedEvents;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.MenuBar;
import net.scnetwork.positioner.controller.MainView;

public class ClearAllPath implements MenuBar.Command{
    private MainView mainView;

    public ClearAllPath(MainView arg){
        this.mainView = arg;
    }

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        GoogleMap googleMap = mainView.getGoogleMap();
        GoogleMapPolyline polyline = mainView.getPolyline();
        try{
            googleMap.removePolyline(polyline);
        } catch (NullPointerException ignored){}
    }
}
