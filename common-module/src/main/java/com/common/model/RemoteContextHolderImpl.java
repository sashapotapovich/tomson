package com.common.model;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import javax.naming.Context;

public class RemoteContextHolderImpl implements RemoteContextHolder {

    private static final long serialVersionUID = 2896539148290100009L;

    public RemoteContextHolderImpl(JmsConnectionFactory context) {
        this.context = context;
    }

    private JmsConnectionFactory context;

    public JmsConnectionFactory getConnection() {
        return context;
    }

}
