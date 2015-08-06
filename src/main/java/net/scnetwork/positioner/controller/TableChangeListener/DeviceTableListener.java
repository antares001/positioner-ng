package net.scnetwork.positioner.controller.TableChangeListener;

import com.vaadin.data.Property;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import net.scnetwork.positioner.controller.MenuSelectedEvents.AdminDevicesMenu;
import net.scnetwork.positioner.utils.HiddenVariable;

public class DeviceTableListener implements Property.ValueChangeListener{
    private AdminDevicesMenu adminDevicesMenu;

    public DeviceTableListener(AdminDevicesMenu arg){
        this.adminDevicesMenu = arg;
    }

    @Override
    public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        Button deleteDevice = adminDevicesMenu.getDeleteDevice();
        Button devGroupButton = adminDevicesMenu.getDevGroupButton();

        deleteDevice.setEnabled(true);
        devGroupButton.setEnabled(true);
        hidden.pullDown("delete_device", String.valueOf(valueChangeEvent.getProperty().getValue()));
    }
}