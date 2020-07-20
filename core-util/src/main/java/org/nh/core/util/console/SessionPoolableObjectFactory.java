package org.nh.core.util.console;
import com.gome.gscm.diamond.Diamond;
import com.gome.gscm.diamond.DiamondException;
import com.jcraft.jsch.*;
import org.apache.commons.pool.PoolableObjectFactory;

/**
 * Created by root on 5/5/17.
 */
public class SessionPoolableObjectFactory implements PoolableObjectFactory<Session> {

    private String host;
    private String user;
    private String password;

    public SessionPoolableObjectFactory(String host, String user, String password) {
        this.host = host;
        this.user = user;
        this.password = password;
    }

    @Override
    public Session makeObject() throws Exception {
        Session session;
        try {
            JSch jsch = new GscmJsch();
            session = jsch.getSession(
                    user, host, Diamond.getInteger("gscm.script.port"));
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("PreferredAuthentications","password");
            session.setConfig(config);
            session.setTimeout(60000);
            if (Diamond.getInteger("gscm.script.password.require") == 1)
                session.setPassword(password);
            session.connect();
        } catch (JSchException | DiamondException e) {
            throw new RuntimeException("ERROR: Unrecoverable error when trying to connect to serverDetails : " +
                    host + ", " + user,  e);
        }
        return session;
    }

    @Override
    public void destroyObject(Session session) throws Exception {
        if (session != null)
            session.disconnect();
    }


    @Override
    public boolean validateObject(Session session) {
        if(session instanceof GscmSession){
            GscmSession s = ((GscmSession)session);
            if(s.isAvailiable() && s.isConnected() && s.isSocketConnected())
                return true;
        }
        return false;
    }
    @Override
    public void activateObject(Session session) throws Exception {}
    @Override
    public void passivateObject(Session session) throws Exception {}
}