package com.example.picpicb.coacheat;

import java.util.ArrayList;

/**
 * Created by romai_000 on 06/03/2017.
 */

public class recette {
    private String Ingredients;
    private String nomR;
    private String etape;
    private String type;

    public String getIngredients() {
        return Ingredients;
    }

    public String getNomR() {
        return nomR;
    }

    public String getEtape() {
        return etape;
    }

    public String getType() {
        return type;
    }

    public String getCal() {
        return cal;
    }

    private String cal;

    public recette(String Ingredients, String nomR,String etape, String type, String cal ){
        this.Ingredients = Ingredients;
        this.nomR = nomR;
        this.etape = etape;
        this.type = type;
        this.cal = cal;
    }


}
