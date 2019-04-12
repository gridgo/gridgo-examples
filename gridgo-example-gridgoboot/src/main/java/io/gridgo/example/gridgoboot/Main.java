package io.gridgo.example.gridgoboot;

import io.gridgo.boot.GridgoApplication;
import io.gridgo.boot.support.annotations.EnableComponentScan;
import io.gridgo.boot.support.annotations.Registries;

/**
 * Created by nauh94@gmail.com on 2019-04-12
 */
@EnableComponentScan
@Registries(defaultProfile = "example")
public class Main {

    public static void main(String[] args) {
        GridgoApplication.run(Main.class, args);
    }
}
