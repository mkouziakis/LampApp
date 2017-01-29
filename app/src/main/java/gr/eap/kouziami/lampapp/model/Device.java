package gr.eap.kouziami.lampapp.model;

import java.io.Serializable;

/**
 * Device type
 *
 * Created by KouziaMi on 21/11/2015.
 */

public class Device implements Serializable{
    private String id;
    private String name;
    private String type;

    public Device(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }
}
