package com.example.jhon.smiserviciostablet.Models;

/**
 * Created by jhon on 12/29/16.
 */

public class UserBorrowCar {
    CarBorrow carBorrow;
    Users users;

    public CarBorrow getCarBorrow() {
        return carBorrow;
    }

    public void setCarBorrow(CarBorrow carBorrow) {
        this.carBorrow = carBorrow;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}
