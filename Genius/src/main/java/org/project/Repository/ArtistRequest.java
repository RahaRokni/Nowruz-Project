package org.project.Repository;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ArtistRequest {
    private String name;
    private String username;
    private String email;
    private int age;

    @JsonCreator
    public ArtistRequest(
            @JsonProperty("name") String name,
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("age") int age) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.age = age;
    }

    public ArtistRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}