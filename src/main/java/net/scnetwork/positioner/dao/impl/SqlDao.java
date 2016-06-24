package net.scnetwork.positioner.dao.impl;

import net.scnetwork.positioner.dao.ISqlDao;
import net.scnetwork.positioner.domain.BeanSettings;
import net.scnetwork.positioner.domain.Positions;

public class SqlDao implements ISqlDao{

    @Override
    public BeanSettings getCredentials(String username) {
        return null;
    }

    @Override
    public boolean insertPath(Positions positions) {
        return false;
    }
}
