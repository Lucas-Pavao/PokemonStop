package pdm.pokemonstop.model;

import com.google.gson.annotations.SerializedName;

public class BlackWhite {
    @SerializedName("animated")
    private Animated animated;

    public Animated getAnimated() {
        return animated;
    }

    public void setAnimated(Animated animated) {
        this.animated = animated;
    }
}
