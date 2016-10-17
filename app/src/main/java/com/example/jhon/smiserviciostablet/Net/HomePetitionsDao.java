package com.example.jhon.smiserviciostablet.Net;

import android.os.AsyncTask;

import com.example.jhon.smiserviciostablet.Interfaces.QueryInterface;
import com.example.jhon.smiserviciostablet.Models.Homepetitions;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.concurrent.ExecutionException;

/**
 * Created by jhon on 17/08/16.
 */
public class HomePetitionsDao {

    public static final int INSERT_CORRECT = 0;
    public static final int INSERT_FAILED = 1;

    MobileServiceClient mClient;
    MobileServiceTable<Homepetitions> mTable;
    MobileServiceList<Homepetitions> mList;
    QueryInterfaceHomePetitions queryInterfaceHomePetitions;
    UpdateHomePetitionsInterface updateHomePetitionsInterface;
    Homepetitions homepetitions1;

    public interface QueryInterfaceHomePetitions {
        void OnQueryFinishHome(int state, MobileServiceList<Homepetitions> list);
    }

    public interface UpdateHomePetitionsInterface{
        void OnUpdateFinishHome(int state, String e, Homepetitions homepetitions, int i);
    }

    public HomePetitionsDao(MobileServiceClient mClient,QueryInterfaceHomePetitions  queryInterfaceHomePetitions, UpdateHomePetitionsInterface updateHomePetitionsInterface ) {
        this.mClient = mClient;
        this.queryInterfaceHomePetitions = queryInterfaceHomePetitions;
        this.mTable = mClient.getTable(Homepetitions.class);
        this.updateHomePetitionsInterface = updateHomePetitionsInterface;
    }

    public void getAllHomePetitions(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mList = mTable.where().field("state").eq(0).execute().get();
                } catch (InterruptedException e) {
                    queryInterfaceHomePetitions.OnQueryFinishHome(INSERT_FAILED,null);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    queryInterfaceHomePetitions.OnQueryFinishHome(INSERT_FAILED,null);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                queryInterfaceHomePetitions.OnQueryFinishHome(INSERT_CORRECT,mList);
            }
        }.execute();
    }

    public void updatePetition(final Homepetitions homepetitions){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    homepetitions1 = mTable.update(homepetitions).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    updateHomePetitionsInterface.OnUpdateFinishHome(INSERT_FAILED,e.toString(),null,0);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    updateHomePetitionsInterface.OnUpdateFinishHome(INSERT_FAILED,e.toString(),null,0);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (homepetitions1.getState() == 1){
                    updateHomePetitionsInterface.OnUpdateFinishHome(INSERT_CORRECT,null,homepetitions1,0);
                }
                else if (homepetitions1.getState() == 2){
                    updateHomePetitionsInterface.OnUpdateFinishHome(INSERT_CORRECT,null,homepetitions,0);
                }

                if (homepetitions1.getState() == 0){
                    updateHomePetitionsInterface.OnUpdateFinishHome(INSERT_FAILED,null,homepetitions,0);
                }


            }
        }.execute();
    }
}
