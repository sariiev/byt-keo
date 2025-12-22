package com.group3.keo.publications.base.visibility;

import com.group3.keo.publications.base.PublicationBase;
import com.group3.keo.users.User;

public class PublicVisibility implements PublicationVisibility {
    @Override
    public boolean canView(User user) {
        return true;
    }

    @Override
    public void onStart(PublicationBase publication) {

    }

    @Override
    public void onEnd(PublicationBase publication) {

    }
}
