package cz.educanet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoadTXT {

    public static String haf() {
        String maze = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("Maze.txt"));
            String line = br.readLine();
            for (int i = 0; line != null; i++) {
                maze += line + "\n";
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maze;
    }
}
