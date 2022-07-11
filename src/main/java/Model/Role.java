package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Role implements Serializable {
    private String name;
    private ArrayList<Ability> abilities = new ArrayList<>();

    public Role(String name) {
        this.name = name;
    }

    public void addAbility(Ability ability) {
        abilities.add(ability);
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }

    public String getName() {
        return name;
    }
}
