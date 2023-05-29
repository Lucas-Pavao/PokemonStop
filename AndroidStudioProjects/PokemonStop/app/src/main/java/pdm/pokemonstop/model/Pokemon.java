package pdm.pokemonstop.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pokemon {
    @SerializedName("name")
    private String name;

    @SerializedName("height")
    private int height;

    @SerializedName("weight")
    private int weight;

    @SerializedName("id")
    private int id;

    @SerializedName("sprites")
    private Sprites sprites;
    @SerializedName("types")
    private List<SlotType> types;

    public Pokemon() {

    }

    public Pokemon(String name, int height, int weight, int id, Sprites sprites, List<SlotType> types) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.id = id;
        this.sprites = sprites;
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public List<SlotType> getTypes() {
        return types;
    }

    public void setTypes(List<SlotType> types) {
        this.types = types;
    }


    public String getTypesToString() {
        String t = "";
        for (int i = 0; i < types.size(); i++) {
            if(i > 0)
                t += ", ";
            t += types.get(i).getType().getName();
        }

        return t;
    }
}
