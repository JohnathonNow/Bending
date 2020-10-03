package com.johnwesthoff.bending.app.avatar;

import com.johnwesthoff.bending.util.network.ConnectToDatabase;

public class AvatarServiceFactory {

    /**
     * Create an AvatarService instance
     *
     * @return AvatarService
     */
    public static AvatarService create() {
        return new AvatarService(
            ConnectToDatabase.INSTANCE()
        );
    }

}
