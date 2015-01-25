package pmr.mvd.positioner.controller.TableChangeListener;

import com.vaadin.data.Property;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import pmr.mvd.positioner.controller.ButtonEvents.DevGroup;
import pmr.mvd.positioner.utils.HiddenVariable;

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
