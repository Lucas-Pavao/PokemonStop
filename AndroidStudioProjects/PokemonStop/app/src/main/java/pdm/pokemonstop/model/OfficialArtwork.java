package pdm.pokemonstop.model;

import com.google.gson.annotations.SerializedName;

public class OfficialArtwork {
        @SerializedName("front_default")
        private String front_default;

        public String getFront_default() {
                return front_default;
        }

        public void setFront_default(String front_default) {
                this.front_default = front_default;
        }
}
