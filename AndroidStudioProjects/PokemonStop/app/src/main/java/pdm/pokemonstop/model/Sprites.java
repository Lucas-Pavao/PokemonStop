package pdm.pokemonstop.model;

import com.google.gson.annotations.SerializedName;



public class Sprites {

    @SerializedName("front_default")
    private String frontDefault;

    public String getFrontDefault() {
        return frontDefault;
    }

    public void setFrontDefault(String frontDefault) {
        this.frontDefault = frontDefault;
    }
}
