package wiki.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  17:45
 */


public class Member implements Comparable<Member> {

    @JsonProperty("pageid")
    private int id;
    private String title;
    private Type type;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Type getType() {
        return type;
    }

    @Override
    public int compareTo(Member member) {
        return this.getTitle().compareTo(member.getTitle());
    }
}


