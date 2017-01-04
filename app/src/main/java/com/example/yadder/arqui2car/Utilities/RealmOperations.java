package com.example.yadder.arqui2car.Utilities;

import android.content.Context;

import com.example.yadder.arqui2car.Models.Driver;
import com.example.yadder.arqui2car.Models.Excercise;
import com.example.yadder.arqui2car.Models.Static;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Yadder on 20/12/2016.
 */
public class RealmOperations {

    private static Realm realm;
    public static Context context;

    /**
     * Create dummy data for the app
     */
    public static void createDummyData(){

        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        Driver driver = new Driver();
        driver.setFullName("Billy Lopez");
        driver.setAge(17);
        driver.setBestScore(-1);
        driver.setDriverId("DRV-0001");
        driver.setExercises(0);
        saveDriver(driver);

        driver.setFullName("Odalis Carrillo");
        driver.setAge(19);
        driver.setBestScore(-1);
        driver.setDriverId("DRV-0002");
        driver.setExercises(0);
        saveDriver(driver);

        driver.setFullName("Elmer Gonzalez");
        driver.setAge(30);
        driver.setBestScore(-1);
        driver.setDriverId("DRV-0003");
        driver.setExercises(0);
        saveDriver(driver);

        Excercise excercise = new Excercise();
        excercise.setDownShocks(2);
        excercise.setUpperShocks(2);
        excercise.setLeftShocks(1);
        excercise.setRightShocks(0);
        excercise.setDriverId("DRV-0003");
        excercise.setWeek(1);
        saveExcercise(excercise);

        excercise.setDownShocks(2);
        excercise.setUpperShocks(1);
        excercise.setLeftShocks(1);
        excercise.setRightShocks(0);
        excercise.setDriverId("DRV-0003");
        excercise.setWeek(2);
        saveExcercise(excercise);

        excercise.setDownShocks(1);
        excercise.setUpperShocks(1);
        excercise.setLeftShocks(1);
        excercise.setRightShocks(4);
        excercise.setDriverId("DRV-0002");
        excercise.setWeek(1);
        saveExcercise(excercise);

        excercise.setDownShocks(2);
        excercise.setUpperShocks(0);
        excercise.setLeftShocks(0);
        excercise.setRightShocks(0);
        excercise.setDriverId("DRV-0002");
        excercise.setWeek(2);
        saveExcercise(excercise);

        excercise.setDownShocks(6);
        excercise.setUpperShocks(0);
        excercise.setLeftShocks(0);
        excercise.setRightShocks(5);
        excercise.setDriverId("DRV-0001");
        excercise.setWeek(1);
        saveExcercise(excercise);

        excercise.setDownShocks(6);
        excercise.setUpperShocks(0);
        excercise.setLeftShocks(0);
        excercise.setRightShocks(5);
        excercise.setDriverId("DRV-0003");
        excercise.setWeek(3);
        saveExcercise(excercise);

    }

    /**
     * Verify that exist a record driver
     * @param driverId
     * @return
     */
    public static boolean existsDriver(String driverId){
        if(context != null){
            Realm.init(context);
            realm = Realm.getDefaultInstance();
            Driver driver = realm.where(Driver.class).equalTo("driverId", driverId).findFirst();
            boolean exists = (driver != null);
            realm.close();
            return exists;
        }
        return false;
    }

    /**
     * Verify tha the driver doen't exist. If it doesn't, save a new driver.
     * @param driver
     */
    public static void saveDriver(Driver driver){
        if(context != null && !existsDriver(driver.getDriverId())) {
            Realm.init(context);
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealm(driver);
            realm.commitTransaction();
            realm.close();
        }
    }

    /**
     * Return dthe driver object
     * @param driverId
     * @return
     */
    public static Driver getDriver(String driverId){
        Driver driverRealm = null;
        Driver driver = null;
        if(existsDriver(driverId)){
            Realm.init(context);
            realm = Realm.getDefaultInstance();
            driverRealm = realm.where(Driver.class).equalTo("driverId", driverId).findFirst();
            driver = realm.copyFromRealm(driverRealm);
            realm.close();
        }
        return driver;
    }

    /**
     * Return a list of stats according the week number to find
     * @param week
     * @return
     */
    public static ArrayList<Static> getWeekStats(int week){
        ArrayList<Static> stats = new ArrayList<Static>();

        Realm.init(context);
        realm = Realm.getDefaultInstance();

        RealmResults<Excercise> excercisesRealm = realm.where(Excercise.class).equalTo("week", week).findAll();
        List<Excercise> excercises = realm.copyFromRealm(excercisesRealm);
        realm.close();

        Collections.sort(excercises, new Comparator<Excercise>() {
            @Override
            public int compare(Excercise excercise1, Excercise excercise2) {
                return (excercise1.getTotalShocks() > excercise2.getTotalShocks())? 1 : -1;
            }
        });

        for(int idx = 0; idx < excercises.size(); idx++){
            Static stat = new Static();
            Excercise excercise = excercises.get(idx);
            Driver driver = getDriver(excercise.getDriverId());

            stat.setFullName(driver.getFullName());
            stat.setDriverId(driver.getDriverId());
            stat.setDownShocks(excercise.getDownShocks());
            stat.setLeftShocks(excercise.getLeftShocks());
            stat.setRightShocks(excercise.getRightShocks());
            stat.setUpperShocks(excercise.getUpperShocks());
            stat.setTotalShock(excercise.getTotalShocks());
            stat.setPosition(idx+1);

            stats.add(stat);
        }

        return stats;
    }


    /**
     * Save the excercise
     * @param excercise
     */
    public static void saveExcercise(Excercise excercise){
        if(context != null && !existsExcercise(excercise)) {
            Realm.init(context);
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealm(excercise);
            realm.commitTransaction();
            realm.close();
        }
    }


    /**
     * Verify that the excercise exists
     * @param excercise
     * @return
     */
    public static boolean existsExcercise(Excercise excercise){
        if(context != null){
            Realm.init(context);
            realm = Realm.getDefaultInstance();
            Excercise excerciseRealm = realm.where(Excercise.class)
                    .equalTo("driverId", excercise.getDriverId())
                    .equalTo("week", excercise.getWeek())
                    .findFirst();
            boolean exists = (excerciseRealm != null);
            realm.close();
            return exists;
        }
        return false;
    }


}
