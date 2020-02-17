package com.common.model;

import java.io.Serializable;
import java.rmi.Remote;
import javax.naming.Context;

public class RemoteContextHolder implements Remote, Serializable {

    private static final long serialVersionUID = 2896539148290100009L;

    public RemoteContextHolder(Context context) {
        this.context = context;
    }

    private Context context;

    public Context getConnection() {
        return context;
    }

}
