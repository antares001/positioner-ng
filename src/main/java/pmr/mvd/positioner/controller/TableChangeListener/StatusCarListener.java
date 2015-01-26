package pmr.mvd.positioner.controller.TableChangeListener;

import com.vaadin.data.Property;
import com.vaadin.server.VaadinSession;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Notification;
import pmr.mvd.positioner.bean.Positions;
import pmr.mvd.positioner.controller.MainView;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.ArrayList;
import java.util.Collection;

public class StatusCarListener implements Property.ValueChangeListener{
    private SqlDao dao = new SqlDao();
    private MainView mainView;

    public StatusCarListener(MainView arg){
        this.mainView = arg;
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        String message = "1";

        try {message = String.valueOf(event.getProperty().getValue());} catch (NumberFormatException ignored){}
        ArrayList<Positions> positionses = dao.GetPositions(hidden.pullUp("device"));

        try {
            Positions pos = positionses.get(Integer.parseInt(message) - 1);

            GoogleMap googleMap = mainView.getGoogleMap();
            googleMap.setCenter(new LatLon(Double.parseDouble(pos.getLatitude()), Double.parseDouble(pos.getLongitude())));

            Collection points = googleMap.getMarkers();
            try{
                for (Object marker : points) {
                    googleMap.removeMarker((GoogleMapMarker) marker);
                }
            } catch (Exception ignored){}

            googleMap.addMarker(hidden.pullUp("device"), new LatLon(Double.parseDouble(pos.getLatitude()), Double.parseDouble(pos.getLongitude())), false, null);

            Notification.show("Транспортное средство: " + hidden.pullUp("device") + ", широта: " + pos.getLatitude() + ", долгота: " + pos.getLongitude());
        } catch (NumberFormatException ignored){}
    }
}
