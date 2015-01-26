package pmr.mvd.positioner.controller.MenuSelectedEvents;

import com.vaadin.server.VaadinSession;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.MenuBar;
import pmr.mvd.positioner.controller.MainView;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.ArrayList;

public class SetPathDevice implements MenuBar.Command{
    private SqlDao dao = new SqlDao();
    private MainView mainView;

    public SetPathDevice(MainView arg){
        this.mainView = arg;
    }

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());

        GoogleMap googleMap = mainView.getGoogleMap();
        GoogleMapPolyline polyline = mainView.getPolyline();

        ArrayList<LatLon> pathPoints = dao.GetPathDevice(hidden.pullUp("device"));
        try{googleMap.removePolyline(polyline);} catch (NullPointerException ignored){}
        polyline = new GoogleMapPolyline(pathPoints, "#ff0000", 0.5, 5);
        googleMap.addPolyline(polyline);
    }
}
