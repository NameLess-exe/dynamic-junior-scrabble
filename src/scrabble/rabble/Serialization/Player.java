package com.leoxiong.clientservertest.Serialization;

import java.util.List;

/**
 * Created by Leo on 5/14/2014.
 */
public class Player extends Sendable {

    private String name;
    private List<String> tiles;

    public Player(String name, List<String> tiles) {
        this.name = name;
        this.tiles = tiles;
    }

    @Override
    public Type getType() {
        return null;
    }
}
