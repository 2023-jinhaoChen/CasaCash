package com.jinhao.casacash

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.Context

class DataBaseAPP(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {

    val create_users_table = "CREATE TABLE Users" +
                            "(USER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "USER_NAME VARCHAR(50) NOT NULL," +
                            "USER_PASSWORD VARCHAR(50) NOT NULL," +
                            "USER_EMAIL VARCHAR(50) NOT NULL UNIQUE)"

    val create_families_table = "CREATE TABLE Families" +
                            "(FAMILY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "FAMILY_NAME VARCHAR(50) NOT NULL," +
                            "FAMILY_BUDGET REAL DEFAULT 0," +
                            "FAMILY_ADMIN_ID INTEGER," +
                            "FOREIGN KEY(FAMILY_ADMIN_ID) REFERENCES Users(USER_ID))"

    val create_spendings_table = "CREATE TABLE Spendings" +
                            "(SPENDING_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "SPENDING_TITLE VARCHAR(50) NOT NULL," +
                            "SPENDING_AMOUNT REAL NOT NULL," +
                            "SPENDING_DESCRIPTION TEXT," +
                            "SPENDING_DATE DATETIME NOT NULL DEFAULT CURRENT_DATE," +
                            "SPENDING_IMAGE_URI TEXT," +
                            "USER_ID INTEGER," +
                            "FAMILY_ID INTEGER," +
                            "FOREIGN KEY(USER_ID) REFERENCES Users(USER_ID)," +
                            "FOREIGN KEY (FAMILY_ID) REFERENCES Families(FAMILY_ID))"

    val create_history_table = "CREATE TABLE History" +
                            "(HISTORY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "FAMILY_ID INTEGER NOT NULL," +
                            "HISTORY_MONTH TEXT," +
                            "START_BUDGET REAL," +
                            "REMAIN_BUDGET REAL," +
                            "FOREIGN KEY(FAMILY_ID) REFERENCES Families(FAMILY_ID))"

    val create_default_family_table = "CREATE TABLE Default_Family" +
                                    "(USER_ID INTEGER," +
                                    "FAMILY_ID INTEGER," +
                                    "FOREIGN KEY(USER_ID) REFERENCES Users(USER_ID)," +
                                    "FOREIGN KEY(FAMILY_ID) REFERENCES Families(FAMILY_ID))"

    val create_user_spending_table = "CREATE TABLE UserSpending" +
            "(SPENDING_ID INTEGER," +
            "USER_ID INTEGER," +
            "PRIMARY KEY(SPENDING_ID, USER_ID)," +
            "FOREIGN KEY(USER_ID) REFERENCES Users(USER_ID)," +
            "FOREIGN KEY(SPENDING_ID) REFERENCES Spendings(SPENDING_ID))"

    val defaultUsers = "INSERT INTO Users(USER_NAME, USER_PASSWORD, USER_EMAIL) VALUES" +
            "('Admin', '123456', 'admin@casacash.com')," +
            "('Jinhao', '123456', 'jinhao@casacash.com')," +
            "('Guifré', '123456', 'guifre@casacash.com')," +
            "('David', '123456', 'david@casacash.com')," +
            "('Sergio', '123456', 'sergio@casacash.com')," +
            "('Javier', '123456', 'javier@casacash.com')," +
            "('Juan', '123456', 'juan@casacash.com');"

    val defaultFamily = "INSERT INTO Families(FAMILY_NAME, FAMILY_BUDGET, FAMILY_ADMIN_ID) VALUES" +
            "('Family 1', 1000, 2)," +
            "('Family Barcelona', 2523.78, 5)," +
            "('Family de papa', 769, 2);"

    val defaultSpendings = "INSERT INTO Spendings(SPENDING_TITLE, SPENDING_AMOUNT, SPENDING_DESCRIPTION, SPENDING_DATE, SPENDING_IMAGE_URI, USER_ID, FAMILY_ID) VALUES" +
            "('Comestibles', 150.0, 'Compras de comestibles semanales', CURRENT_DATE, null, 2, 1)," +
            "('Cena fuera', 25.0, 'Cena en un restaurante local', CURRENT_DATE, null, 3, 2)," +
            "('Comestibles', 50.0, 'Compras de comestibles', CURRENT_DATE, null, 4, 1)," +
            "('Cena fuera', 30.0, 'Cena en un restaurante local', CURRENT_DATE, null, 2, 1)," +
            "('Comestibles', 50.0, 'Compras de comestibles', CURRENT_DATE, null, 3, 2)," +
            "('Cena fuera', 40.0, 'Cena en un restaurante local', CURRENT_DATE, null, 5, 3)," +
            "('Comestibles', 50.0, 'Compras de comestibles', CURRENT_DATE, null,2, 1)," +
            "('Cena fuera', 20.0, 'Cena en un restaurante local', CURRENT_DATE, null, 3, 1)," +
            "('Comestibles', 50.0, 'Compras de comestibles', CURRENT_DATE, null, 5, 1)," +
            "('Comestibles', 50.0, 'Compras de comestibles semanales', CURRENT_DATE, null, 6, 1)," +
            "('Cena fuera', 60.0, 'Cena en un restaurante local', CURRENT_DATE, null, 7, 1)," +
            "('Noche de cine', 30.0, 'Entradas y snacks para la noche de cine', CURRENT_DATE, null, 5, 1);"

    val defaultUserFamily = "INSERT INTO Default_Family(USER_ID, FAMILY_ID) VALUES" +
                            "(1, 1)," +
                            "(2, 2)," +
                            "(3, 3)"



    override fun onCreate(db: SQLiteDatabase?) {
        //create database tables
        db?.execSQL(create_users_table)
        db?.execSQL(create_families_table)
        db?.execSQL(create_spendings_table)
        db?.execSQL(create_history_table)
        db?.execSQL(create_default_family_table)


        //insert data into tables
        db?.execSQL(defaultUsers)
        db?.execSQL(defaultFamily)
        db?.execSQL(defaultSpendings)
        db?.execSQL(defaultUserFamily)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}