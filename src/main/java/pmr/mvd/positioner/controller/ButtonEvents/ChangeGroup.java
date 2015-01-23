package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

import java.util.HashMap;

public class ChangeGroup implements Button.ClickListener{
    private SqlDao dao = new SqlDao();

    private ComboBox comboBox = new ComboBox();
    private HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
    private Window window = new Window("Изменение группы для " + hidden.pullUp("selected_user"));

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        window.setWidth(400.0f, Sizeable.Unit.PIXELS);
        window.setHeight(200.0f, Sizeable.Unit.PIXELS);
        window.setModal(true);

        FormLayout chLayout = new FormLayout();
        CustomLayout layout = new CustomLayout("changegroup");

        comboBox.setFilteringMode(FilteringMode.CONTAINS);
        comboBox.setNullSelectionAllowed(false);
        comboBox.setImmediate(true);

        comboBox.addItem("Пользователь");
        comboBox.addItem("Администратор");
        layout.addComponent(comboBox, "group");

        final Button save = new Button("Сменить");
        save.addClickListener(new Change());
        layout.addComponent(save, "save");

        final Button close = new Button("Отмена", new CloseWindow(window));
        layout.addComponent(close, "close");

        chLayout.addComponent(layout);
        window.setContent(chLayout);
        UI.getCurrent().addWindow(window);
    }

    private class Change implements Button.ClickListener{
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            final HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());
            try {
                final String value = comboBox.getValue().toString();

                String group = "";
                if (value.equals("Администратор"))
                    group = "1";
                else if (value.equals("Пользователь"))
                    group = "0";

                HashMap<String,String> params = new HashMap<String, String>();
                params.put("user", hidden.pullUp("selected_user"));
                params.put("group", group);

                if (dao.ExecuteOperation(params, "change_group")) {
                    window.close();
                } else
                    Notification.show("Ошибка смены группы");
            } catch (NullPointerException e) {
                Notification.show("Не выбрана группа");
            }
        }
    }
}
