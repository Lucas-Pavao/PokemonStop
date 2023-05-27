package pdm.pokemonStop.model;

public class Pokemon {
    private String name;
    private String number;
    public Pokemon(String name, String number) {
        this.name = name;
        this.number = number;
    }
    public String getName() {
        return this.name;
    }
    public String getNumber() {
        return this.number;
    }
}

