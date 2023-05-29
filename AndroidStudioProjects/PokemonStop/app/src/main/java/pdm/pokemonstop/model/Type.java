package pdm.pokemonstop.model;


import com.google.gson.annotations.SerializedName;

public class Type {
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;

    public Type(String name) {
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
