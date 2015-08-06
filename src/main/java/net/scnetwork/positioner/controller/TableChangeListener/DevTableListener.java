package net.scnetwork.positioner.controller.TableChangeListener;


import com.vaadin.data.Property;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import net.scnetwork.positioner.utils.HiddenVariable;
import net.scnetwork.positioner.controller.ButtonEvents.GroupListDevices;

public class DevTableListener implements Property.ValueChangeListener{
    private GroupListDevices groupListDevices;

    public DevTableListener(GroupListDevices arg){
        this.groupListDevices = arg;
    }

    @Override
    public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
        Button del = groupListDevices.getDel();

        del.setEnabled(true);
        hidden.pullDown("delete_groupdevice", String.valueOf(valueChangeEvent.getProperty().getValue()));
    }
}
