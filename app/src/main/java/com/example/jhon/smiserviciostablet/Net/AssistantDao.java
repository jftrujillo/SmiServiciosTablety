package com.example.jhon.smiserviciostablet.Net;

import android.os.AsyncTask;

import com.example.jhon.smiserviciostablet.Models.Assistants;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.example.jhon.smiserviciostablet.Models.driverpetitions;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.concurrent.ExecutionException;

/**
 * Created by jhonfredy on 18/10/2016.
 */

public class AssistantDao {
    public static final int INSERT_CORRECT = 0;
    public static final int INSERT_FAILED = 1;

    MobileServiceClient mClient;
    MobileServiceTable<Assistants> mTable;
    MobileServiceList<Assistants> mList;
    Assistants assistants;
    QueryInterfaceAssistant queryInterfaceAssistant;

    public interface QueryInterfaceAssistant {
        void OnQueryFinishAsistant(int state, MobileServiceList<Assistants> list);
    }

    public AssistantDao(MobileServiceClient mClient, QueryInterfaceAssistant queryInterfaceAssistant) {
        this.mClient = mClient;
        mTable = mClient.getTable(Assistants.class);
        this.queryInterfaceAssistant = queryInterfaceAssistant;
    }

    public void getAllAsisstant() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mList = mTable.where().execute().get();
                } catch (InterruptedException e) {
                    queryInterfaceAssistant.OnQueryFinishAsistant(INSERT_FAILED, null);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    queryInterfaceAssistant.OnQueryFinishAsistant(INSERT_FAILED, null);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                queryInterfaceAssistant.OnQueryFinishAsistant(INSERT_CORRECT, mList);
            }
        }.execute();
    }
}
