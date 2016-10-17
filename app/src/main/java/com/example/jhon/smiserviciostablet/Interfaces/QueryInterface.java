package com.example.jhon.smiserviciostablet.Interfaces;

import com.example.jhon.smiserviciostablet.Models.Users;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

import java.util.List;

/**
 * Created by jhon on 9/08/16.
 */
public interface QueryInterface {
    void OnQueryFinish(int state, MobileServiceList<Users> list);
}
