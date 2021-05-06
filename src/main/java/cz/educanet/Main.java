package cz.educanet;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        String maze = LoadTXT.loadTxt("Collision2.txt");

        String[] linesOfMaze = maze.split("\n");


        //region: Window init
        GLFW.glfwInit();

        // Tell GLFW what version of OpenGL we want to use.
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        // TODO: Add support for macOS

        // Create the window...
        // We can set multiple options with glfwWindowHint ie. fullscreen, resizability etc.
        long window = GLFW.glfwCreateWindow(800, 600, "Maze", 0, 0);
        if (window == 0) {
            GLFW.glfwTerminate();
            throw new Exception("Can't open window");
        }
        GLFW.glfwMakeContextCurrent(window);

        // Tell GLFW, that we are using OpenGL
        GL.createCapabilities();
        GL33.glViewport(0, 0, 800, 600);

        // Resize callback
        GLFW.glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
            GL33.glViewport(0, 0, w, h);
        });
        //endregion

        // Main game loop
        //Game.init(window);
        Shaders.initShaders();

        // Draw in polygon mod
        //GL33.glPolygonMode(GL33.GL_FRONT_AND_BACK, GL33.GL_LINE);
        ArrayList<Square> squares = new ArrayList<>();

        for (int i = 0; i < linesOfMaze.length; i++) {
            String[] coords = linesOfMaze[i].split(";");
            Square square = new Square(Float.parseFloat(coords[0]), Float.parseFloat(coords[1]), Float.parseFloat(coords[2]));
            squares.add(square);
        }

        Square movingSquare = new Square(0f, 0f, 0.25f);


        while (!GLFW.glfwWindowShouldClose(window)) {
            // Key input management
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS)
                GLFW.glfwSetWindowShouldClose(window, true); // Send a shutdown signal...

            // Change the background color
            GL33.glClearColor(0f, 0f, 0f, 1f);
            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);

            /*Game.render(window);
            Game.update(window);*/
            //squareOne.render();

            for (int i = 0; i < squares.size(); i++) {
                squares.get(i).render();
            }


            for (int i = 0; i < squares.size(); i++) {
                if (doesCollide(movingSquare, squares.get(i))) break;
            }
            movingSquare.update(window);

            movingSquare.render();

            // Swap the color buffer -> screen tearing solution
            GLFW.glfwSwapBuffers(window);
            // Listen to input
            GLFW.glfwPollEvents();
        }

        // Don't forget to cleanup
        GLFW.glfwTerminate();
    }
    public static boolean doesCollide(Square movingSquare, Square square) {
        if (isIn(movingSquare, square)) {
            movingSquare.green();
            return true;
        }
        else movingSquare.red();
        return false;
    }
    public static boolean isIn(Square movingSquare, Square square) {
        /*System.out.print("moving square:" + movingSquare.getX() + ";" + movingSquare.getY());
        System.out.println("square:" + square.getX() + ";" + square.getY());*/


        return (((movingSquare.getX() < (square.getX() + square.getSize()) && movingSquare.getX() > square.getX())
                || (((movingSquare.getX() + movingSquare.getSize()) < (square.getX() + square.getSize()) && (movingSquare.getX() + movingSquare.getSize()) > square.getX())))
                && ((movingSquare.getY() < (square.getY()) && movingSquare.getY() > square.getY() - square.getSize()) // levy horni bod
                || (((movingSquare.getY() - movingSquare.getSize()) > (square.getY() - square.getSize()) && (movingSquare.getY() - movingSquare.getSize()) < square.getY()))));
    }





}
