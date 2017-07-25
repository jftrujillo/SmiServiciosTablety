package com.example.jhon.smiserviciostablet.Net;

import android.os.AsyncTask;

import com.example.jhon.smiserviciostablet.Models.CarBorrow;
import com.example.jhon.smiserviciostablet.Models.driverpetitions;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.concurrent.ExecutionException;

/**
 * Created by jhon on 12/28/16.
 */

public class CarBorrowDao {

    public static final int INSERT_CORRECT = 0;
    public static final int INSERT_FAILED = 1;

    MobileServiceClient mClient;
    MobileServiceTable<CarBorrow> mTable;
    MobileServiceList<CarBorrow> mList;
    QueryInterfaceCarBorrowPetitions queryInterfaceCarBorrowPetitions;
    UpdateCarBorrowPetitionsInterface updateDriverPetitionsInterface;
    CarBorrow carBorrow;

    public interface QueryInterfaceCarBorrowPetitions {
        void OnQueryFinishCarBorrow(int state, MobileServiceList<CarBorrow> list);
    }


    public interface UpdateCarBorrowPetitionsInterface {
        void OnUpdateFinishCarBorrow(int state, String e, CarBorrow carBorrow, int i);
    }

    public CarBorrowDao(MobileServiceClient mClient, QueryInterfaceCarBorrowPetitions queryInterfaceCarBorrowPetitions, UpdateCarBorrowPetitionsInterface updateDriverPetitionsInterface) {
        this.mClient = mClient;
        this.queryInterfaceCarBorrowPetitions = queryInterfaceCarBorrowPetitions;
        this.mTable = mClient.getTable(CarBorrow.class);
        this.updateDriverPetitionsInterface = updateDriverPetitionsInterface;
    }

    public void getAllCarBorrowPetitions() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mList = mTable.where().field("state").eq(0).execute().get();
                } catch (InterruptedException e) {
                    queryInterfaceCarBorrowPetitions.OnQueryFinishCarBorrow(INSERT_FAILED, null);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    queryInterfaceCarBorrowPetitions.OnQueryFinishCarBorrow(INSERT_FAILED, null);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                queryInterfaceCarBorrowPetitions.OnQueryFinishCarBorrow(INSERT_CORRECT, mList);
            }
        }.execute();
    }

    public void getTakenCarBorrowPetitions() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mList = mTable.where().field("state").eq(1).execute().get();
                } catch (InterruptedException e) {
                    queryInterfaceCarBorrowPetitions.OnQueryFinishCarBorrow(INSERT_FAILED, null);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    queryInterfaceCarBorrowPetitions.OnQueryFinishCarBorrow(INSERT_FAILED, null);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                queryInterfaceCarBorrowPetitions.OnQueryFinishCarBorrow(INSERT_CORRECT, mList);
            }
        }.execute();
    }

    public void getCarBorrowPetitionById(final String id){

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mList = mTable.where().field("id").eq(id).execute().get();
                } catch (InterruptedException e) {
                    queryInterfaceCarBorrowPetitions.OnQueryFinishCarBorrow(INSERT_FAILED, null);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    queryInterfaceCarBorrowPetitions.OnQueryFinishCarBorrow(INSERT_FAILED, null);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                queryInterfaceCarBorrowPetitions.OnQueryFinishCarBorrow(INSERT_CORRECT, mList);
            }
        }.execute();

    }

    public void updatePetition(final CarBorrow carBorrow2,final int pos) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    carBorrow = mTable.update(carBorrow2).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    updateDriverPetitionsInterface.OnUpdateFinishCarBorrow(INSERT_FAILED, e.toString(), null, pos);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    updateDriverPetitionsInterface.OnUpdateFinishCarBorrow(INSERT_FAILED, e.toString(), null, pos);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (carBorrow.getState() == 1) {
                    updateDriverPetitionsInterface.OnUpdateFinishCarBorrow(INSERT_CORRECT, null, carBorrow, pos);
                } else if (carBorrow.getState() == 2) {
                    updateDriverPetitionsInterface.OnUpdateFinishCarBorrow(INSERT_FAILED, "fallo", null, pos);
                }

                if (carBorrow.getState() == 0) {
                    updateDriverPetitionsInterface.OnUpdateFinishCarBorrow(INSERT_FAILED, "fallo", null, pos);
                }


            }
        }.execute();
    }
}
