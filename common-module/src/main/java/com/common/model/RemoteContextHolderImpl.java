package com.common.model;

import javax.jms.ConnectionFactory;

public class RemoteContextHolderImpl implements RemoteContextHolder {
    private static final long serialVersionUID = 2896539148290100009L;
    private ConnectionFactory connectionFactory;

    public RemoteContextHolderImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public ConnectionFactory getConnection() {
        return connectionFactory;
    }

}
