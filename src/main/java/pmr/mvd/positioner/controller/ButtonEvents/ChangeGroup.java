package pmr.mvd.positioner.controller.ButtonEvents;

import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import pmr.mvd.positioner.dao.SqlDao;
import pmr.mvd.positioner.utils.HiddenVariable;

public class ChangeGroup implements Button.ClickListener{
    private SqlDao dao = new SqlDao();

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        final HiddenVariable hidden = HiddenVariable.getInstance(VaadinSession.getCurrent().getSession().getId());

        final Window winChGroup = new Window("Изменение группы для " + hidden.pullUp("selected_user"));
        winChGroup.setWidth(400.0f, Sizeable.Unit.PIXELS);
        winChGroup.setHeight(200.0f, Sizeable.Unit.PIXELS);
        winChGroup.setModal(true);

        FormLayout chLayout = new FormLayout();
        CustomLayout layout = new CustomLayout("changegroup");

        final ComboBox comboBox = new ComboBox();
        comboBox.setFilteringMode(FilteringMode.CONTAINS);
        comboBox.setNullSelectionAllowed(false);
        comboBox.setImmediate(true);

        comboBox.addItem("Пользователь");
        comboBox.addItem("Администратор");
        layout.addComponent(comboBox, "group");

        final Button save = new Button("Сменить", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    final String value = comboBox.getValue().toString();

                    String group = "";
                    if (value.equals("Администратор"))
                        group = "1";
                    else if (value.equals("Пользователь"))
                        group = "0";

                    if (dao.ChangeGroup(hidden.pullUp("selected_user"), group))
                        winChGroup.close();
                    else
                        Notification.show("Ошибка смены группы");
                } catch (NullPointerException e) {
                    Notification.show("Не выбрана группа");
                }
            }
        });
        layout.addComponent(save, "save");

        final Button close = new Button("Отмена", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                winChGroup.close();
            }
        });
        layout.addComponent(close, "close");

        chLayout.addComponent(layout);
        winChGroup.setContent(chLayout);
        UI.getCurrent().addWindow(winChGroup);
    }
}
