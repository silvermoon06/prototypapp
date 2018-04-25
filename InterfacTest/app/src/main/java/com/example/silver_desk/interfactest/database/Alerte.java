package com.example.silver_desk.interfactest.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;



/**
 * Created by silver-desk on 06/04/2018.
 */
@Entity(tableName = "alerte_table",
        foreignKeys = {@ForeignKey(entity = Evenement.class,
                    parentColumns = "id",
                    childColumns = "evenementId")
                    })
public class Alerte {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id ;

    @ColumnInfo(name = "description")
    private String description ;

    @ColumnInfo(name = "heure")
    @NonNull

    private long heure ;

    @ColumnInfo(name = "sonnerie")
    private String sonnerie ;

    @ColumnInfo(name = "delai")

    private long delai;

    @ColumnInfo(name = "etat")
    private boolean etat;

    @ColumnInfo(name = "evenementId")
    private int evenementId;


    //Constructeurs




    public Alerte(String description, @NonNull long heure, String sonnerie, long delai, boolean etat, int evenementId) {
        this.description = description;
        this.heure = heure;
        this.sonnerie = sonnerie;
        this.delai = delai;
        this.etat = etat;
        this.evenementId = evenementId;
    }


    //Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    public long getHeure() {
        return heure;
    }

    public void setHeure(@NonNull long heure) {
        this.heure = heure;
    }

    public String getSonnerie() {
        return sonnerie;
    }

    public void setSonnerie(String sonnerie) {
        this.sonnerie = sonnerie;
    }

    public long getDelai() {
        return delai;
    }

    public void setDelai(long delai) {
        this.delai = delai;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public int getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(int evenementId) {
        this.evenementId = evenementId;
    }
}
