package pmr.mvd.positioner.controller.MenuSelectedEvents;

import com.vaadin.server.VaadinSession;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import pmr.mvd.positioner.bean.Positions;
import pmr.mvd.positioner.controller.MainView;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.ArrayList;
import java.util.Collection;

public class SetDataDevices implements MenuBar.Command{
    private SqlDao dao = new SqlDao();
    private MainView mainView;

    public SetDataDevices(MainView arg){
        this.mainView = arg;
    }

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());

        Table statusCar = mainView.getStatusCar();
        Collection items = statusCar.getItemIds();
        if(items.size() != 0){
            statusCar.removeAllItems();
        }
        ArrayList<Positions> positionses = dao.GetPositions(menuItem.getText());
        hidden.pullDown("device", menuItem.getText());

        GoogleMap googleMap = mainView.getGoogleMap();
        if (positionses.size() != 0) {
            Positions first = positionses.get(0);

            googleMap.setCenter(new LatLon(Double.parseDouble(first.getLatitude()), Double.parseDouble(first.getLongitude())));
            Collection points = googleMap.getMarkers();

            try{
                for (Object marker : points) {
                    googleMap.removeMarker((GoogleMapMarker) marker);
                }
            } catch (Exception ignored){}

            googleMap.addMarker(menuItem.getText(), new LatLon(Double.parseDouble(first.getLatitude()), Double.parseDouble(first.getLongitude())), false, null);
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
