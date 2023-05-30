package pdm.pokemonstop.model;

import com.google.gson.annotations.SerializedName;

public class Versions {
    public GenerationV getGenerationV() {
        return generationV;
    }

    public void setGenerationV(GenerationV generationV) {
        this.generationV = generationV;
    }

    @SerializedName("generation-v")
    private GenerationV generationV;
}
