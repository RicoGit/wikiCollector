package wiki.entity;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  15:55
 */


public enum Type {

    SUB_CATEGORY("subcat"),
    PAGE("page");

    final String name;

    Type(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

}
