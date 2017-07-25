package com.example.jhon.smiserviciostablet.Net;

import android.os.AsyncTask;

import com.example.jhon.smiserviciostablet.Models.Homepetitions;
import com.example.jhon.smiserviciostablet.Models.driverpetitions;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.concurrent.ExecutionException;

/**
 * Created by jhon on 31/08/16.
 */
public class DriverPetitionsDao {

    public static final int INSERT_CORRECT = 0;
    public static final int INSERT_FAILED = 1;

    MobileServiceClient mClient;
    MobileServiceTable<driverpetitions> mTable;
    MobileServiceList<driverpetitions> mList;
    QueryInterfaceDriverPetitions queryInterfaceDriverPetitions;
    UpdateDriverPetitionsInterface updateDriverPetitionsInterface;
    driverpetitions driverpetitions;

    public interface QueryInterfaceDriverPetitions {
        void OnQueryFinishDriver(int state, MobileServiceList<driverpetitions> list);
    }

    public interface UpdateDriverPetitionsInterface {
        void OnUpdateFinishDriver(int state, String e, driverpetitions homepetitions, int i);
    }

    public DriverPetitionsDao(MobileServiceClient mClient, QueryInterfaceDriverPetitions queryInterfaceDriverPetitions, UpdateDriverPetitionsInterface updateDriverPetitionsInterface) {
        this.mClient = mClient;
        this.queryInterfaceDriverPetitions = queryInterfaceDriverPetitions;
        this.mTable = mClient.getTable(driverpetitions.class);
        this.updateDriverPetitionsInterface = updateDriverPetitionsInterface;
    }

    public void getAllDriverPetitions() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mList = mTable.where().field("state").eq(0).execute().get();
                } catch (InterruptedException e) {
                    queryInterfaceDriverPetitions.OnQueryFinishDriver(INSERT_FAILED, null);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    queryInterfaceDriverPetitions.OnQueryFinishDriver(INSERT_FAILED, null);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                queryInterfaceDriverPetitions.OnQueryFinishDriver(INSERT_CORRECT, mList);
            }
        }.execute();
    }

    public void  getTakenDriverPetitions() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mList = mTable.where().field("state").eq(1).execute().get();
                } catch (InterruptedException e) {
                    queryInterfaceDriverPetitions.OnQueryFinishDriver(INSERT_FAILED, null);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    queryInterfaceDriverPetitions.OnQueryFinishDriver(INSERT_FAILED, null);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                queryInterfaceDriverPetitions.OnQueryFinishDriver(INSERT_CORRECT, mList);
            }
        }.execute();
    }

    public void getDriverPetitionById(final String id){

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mList = mTable.where().field("id").eq(id).execute().get();
                } catch (InterruptedException e) {
                    queryInterfaceDriverPetitions.OnQueryFinishDriver(INSERT_FAILED, null);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    queryInterfaceDriverPetitions.OnQueryFinishDriver(INSERT_FAILED, null);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                queryInterfaceDriverPetitions.OnQueryFinishDriver(INSERT_CORRECT, mList);
            }
        }.execute();

    }

    public void updatePetition(final driverpetitions homepetitions,final int pos) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    driverpetitions = mTable.update(homepetitions).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    updateDriverPetitionsInterface.OnUpdateFinishDriver(INSERT_FAILED, e.toString(), null, pos);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    updateDriverPetitionsInterface.OnUpdateFinishDriver(INSERT_FAILED, e.toString(), null, pos);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (driverpetitions.getState() == 1) {
                    updateDriverPetitionsInterface.OnUpdateFinishDriver(INSERT_CORRECT, null, driverpetitions, pos);
                } else if (driverpetitions.getState() == 2) {
                    updateDriverPetitionsInterface.OnUpdateFinishDriver(INSERT_CORRECT, null, driverpetitions, pos);
                }

                if (driverpetitions.getState() == 0) {
                    updateDriverPetitionsInterface.OnUpdateFinishDriver(INSERT_FAILED, "fallo", null, pos);
                }
            }
        }.execute();
    }
}

