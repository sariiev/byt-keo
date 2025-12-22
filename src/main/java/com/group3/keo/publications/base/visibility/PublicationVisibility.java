package com.group3.keo.publications.base.visibility;

import com.group3.keo.publications.base.PublicationBase;
import com.group3.keo.users.User;

public interface PublicationVisibility {
    boolean canView(User user);
    void onStart(PublicationBase publication);
    void onEnd(PublicationBase publication);
}
