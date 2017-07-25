package com.example.jhon.smiserviciostablet.Net;

import android.os.AsyncTask;

import com.example.jhon.smiserviciostablet.Models.Roadpetitions;
import com.example.jhon.smiserviciostablet.Models.driverpetitions;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.concurrent.ExecutionException;

/**
 * Created by jhon on 4/09/16.
 */
public class RoadPetitionsDao {
    public static final int INSERT_CORRECT = 0;
    public static final int INSERT_FAILED = 1;

    MobileServiceClient mClient;
    MobileServiceTable<Roadpetitions> mTable;
    MobileServiceList<Roadpetitions> mList;
    QueryInterfaceRoadPetitions queryInterfaceRoadPetitions;
    UpdateRoadPetitionsInterface updateRoadPetitionsInterface;
    Roadpetitions driverpetitions;

    public interface QueryInterfaceRoadPetitions {
        void OnQueryFinishRoad(int state, MobileServiceList<Roadpetitions> list);
    }

    public interface UpdateRoadPetitionsInterface {
        void OnUpdateFinishRoad(int state, String e, Roadpetitions roadpetitions, int i);
    }

    public RoadPetitionsDao(MobileServiceClient mClient, QueryInterfaceRoadPetitions queryInterfaceRoadPetitions, UpdateRoadPetitionsInterface updateRoadPetitionsInterface) {
        this.mClient = mClient;
        this.queryInterfaceRoadPetitions = queryInterfaceRoadPetitions;
        this.mTable = mClient.getTable(Roadpetitions.class);
        this.updateRoadPetitionsInterface = updateRoadPetitionsInterface;
    }

    public void getAllRoadPetitions() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mList = mTable.where().field("state").eq(0).execute().get();
                } catch (InterruptedException e) {
                    queryInterfaceRoadPetitions.OnQueryFinishRoad(INSERT_FAILED, null);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    queryInterfaceRoadPetitions.OnQueryFinishRoad(INSERT_FAILED, null);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                queryInterfaceRoadPetitions.OnQueryFinishRoad(INSERT_CORRECT, mList);
            }
        }.execute();
    }

    public void getTakenRoadPetitions() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mList = mTable.where().field("state").eq(1).execute().get();
                } catch (InterruptedException e) {
                    queryInterfaceRoadPetitions.OnQueryFinishRoad(INSERT_FAILED, null);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    queryInterfaceRoadPetitions.OnQueryFinishRoad(INSERT_FAILED, null);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                queryInterfaceRoadPetitions.OnQueryFinishRoad(INSERT_CORRECT, mList);
            }
        }.execute();
    }

    public void getRoadPetitionById(final String id){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mList = mTable.where().field("id").eq(id).execute().get();
                } catch (InterruptedException e) {
                    queryInterfaceRoadPetitions.OnQueryFinishRoad(INSERT_FAILED,null);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    queryInterfaceRoadPetitions.OnQueryFinishRoad(INSERT_FAILED,null);
            }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                queryInterfaceRoadPetitions.OnQueryFinishRoad(INSERT_CORRECT,mList);
            }
        }.execute();

    }

    public void updatePetition(final Roadpetitions roadPetitions,final int pos) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    driverpetitions = mTable.update(roadPetitions).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    updateRoadPetitionsInterface.OnUpdateFinishRoad(INSERT_FAILED, e.toString(), null, pos);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    updateRoadPetitionsInterface.OnUpdateFinishRoad(INSERT_FAILED, e.toString(), null, pos);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (driverpetitions.getState() == 1 || driverpetitions.getState() == 2 ) {
                    updateRoadPetitionsInterface.OnUpdateFinishRoad(INSERT_CORRECT, null, roadPetitions, pos);
                }

                if (driverpetitions.getState() == 0) {
                    updateRoadPetitionsInterface.OnUpdateFinishRoad(INSERT_FAILED, "fallo", null, pos);
                }


            }
        }.execute();
    }
}
