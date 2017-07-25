package com.example.jhon.smiserviciostablet.Net;

import android.os.AsyncTask;

import com.example.jhon.smiserviciostablet.Interfaces.QueryInterface;
import com.example.jhon.smiserviciostablet.Models.Users;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.concurrent.ExecutionException;

/**
 * Created by jhon on 9/08/16.
 */
public class UsersDao  {

    public static final int INSERT_CORRECT = 0;
    public static final int INSERT_FAILED = 1;

    MobileServiceClient mClient;
    MobileServiceTable<Users> mTable;
    MobileServiceList<Users> mList;
    QueryInterface queryInterface;
    UsersDaoUpdateInterface usersDaoUpdateInterface;
    Users users1;


    public interface UsersDaoUpdateInterface{
        void OnUserUpdate(int type, int state, String e, Users users);
    }

    public UsersDao(MobileServiceClient mClient,QueryInterface  queryInterface, UsersDaoUpdateInterface usersDaoUpdateInterface ) {
        this.mClient = mClient;
        this.queryInterface = queryInterface;
        this.usersDaoUpdateInterface = usersDaoUpdateInterface;
        mTable = mClient.getTable(Users.class);
    }

    public void getAllUsers(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mList = mTable.where().field("isvalid").eq(0).execute().get();
                } catch (InterruptedException e) {
                    queryInterface.OnQueryFinish(INSERT_FAILED,null);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    queryInterface.OnQueryFinish(INSERT_FAILED,null);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                queryInterface.OnQueryFinish(INSERT_CORRECT,mList);
            }
        }.execute();
    }

    public void getAllUsersAvaliable(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mList = mTable.where().field("isvalid").eq(0).or().field("isvalid").eq(1).execute().get();
                } catch (InterruptedException e) {
                    queryInterface.OnQueryFinish(INSERT_FAILED,null);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    queryInterface.OnQueryFinish(INSERT_FAILED,null);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                queryInterface.OnQueryFinish(INSERT_CORRECT,mList);
            }
        }.execute();
    }



    public void getUserById(final String id){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mList = mTable.where().field("id").eq(id).execute().get();
                } catch (InterruptedException e) {
                    queryInterface.OnQueryFinish(INSERT_FAILED,null);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    queryInterface.OnQueryFinish(INSERT_FAILED,null);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                queryInterface.OnQueryFinish(INSERT_CORRECT,mList);
            }
        }.execute();
    }

    public void updateUser(final Users users){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    users1 = mTable.update(users).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    usersDaoUpdateInterface.OnUserUpdate(0,INSERT_FAILED,e.toString(),users);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    usersDaoUpdateInterface.OnUserUpdate(0,INSERT_FAILED,e.toString(),users);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (users1.isvalid() == 0){
                    usersDaoUpdateInterface.OnUserUpdate(0,INSERT_FAILED,"desconocido",users);
                }
                else {
                    usersDaoUpdateInterface.OnUserUpdate(users1.isvalid(),INSERT_CORRECT,null,users);
                }

            }
        }.execute();
    }
}
