package pdm.pokemonstop.model;

import com.google.gson.annotations.SerializedName;



public class Sprites {

    @SerializedName("front_default")
    private String frontDefault;
    @SerializedName("versions")
    private Versions versions;

    public String getFrontDefault() {
        return frontDefault;
    }

    public void setFrontDefault(String frontDefault) {
        this.frontDefault = frontDefault;
    }

    public Versions getVersions() {
        return versions;
    }

    public void setVersions(Versions verions) {
        this.versions = verions;
    }
}
