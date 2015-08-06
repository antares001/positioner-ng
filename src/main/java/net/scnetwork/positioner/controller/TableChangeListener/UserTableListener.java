package net.scnetwork.positioner.controller.TableChangeListener;

import com.vaadin.data.Property;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import net.scnetwork.positioner.controller.ButtonEvents.DevGroup;
import net.scnetwork.positioner.utils.HiddenVariable;

public class UserTableListener implements Property.ValueChangeListener{
    private DevGroup devGroup;

    public UserTableListener(DevGroup arg){
        this.devGroup = arg;
    }

    @Override
    public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());

        Button del = devGroup.getDel();
        del.setEnabled(true);
        hidden.pullDown("delete_groupuser", String.valueOf(valueChangeEvent.getProperty().getValue()));
    }
}
