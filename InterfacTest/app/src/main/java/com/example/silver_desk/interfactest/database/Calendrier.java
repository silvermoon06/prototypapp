package com.example.silver_desk.interfactest.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by silver-desk on 01/04/2018.
 */
@Entity(tableName = "calendrier_table")
public class Calendrier {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id ;

    @ColumnInfo(name = "titre")
    @NonNull
    private String titre ;

    @ColumnInfo(name = "visibilite")
    @NonNull
    private boolean visibilite ;



    @ColumnInfo(name = "couleur")
    private int couleur;


    @ColumnInfo(name="description")
    private String description;

    public Calendrier() {
    }

    public Calendrier(@NonNull String titre, @NonNull boolean visibilite, int couleur,  String description) {
        this.titre = titre;
        this.visibilite = visibilite;
        this.couleur = couleur;
        this.description = description;
    }

    public Calendrier(int id, @NonNull String titre, @NonNull boolean visibilite, int couleur,  String description) {
        this.id = id;
        this.titre = titre;
        this.visibilite = visibilite;

        this.couleur = couleur;

        this.description=description;
    }

    @NonNull
    public boolean isVisibilite() {
        return visibilite;
    }

    public void setVisibilite(@NonNull boolean visibilite) {
        this.visibilite = visibilite;
    }



    public Calendrier(String titre) {
this.titre=titre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getTitre() {
        return titre;
    }

    public void setTitre(@NonNull String titre) {
        this.titre = titre;
    }


    public int getCouleur() {
        return couleur;
    }

    public void setCouleur(int couleur) {
        this.couleur = couleur;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
