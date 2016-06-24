package net.scnetwork.positioner.dao;

import net.scnetwork.positioner.domain.BeanSettings;
import net.scnetwork.positioner.domain.Positions;

public interface ISqlDao {

    BeanSettings getCredentials(String username);

    boolean insertPath(Positions positions);
}
