package cz.educanet;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

public class Main {

    public static void main(String[] args) throws Exception {
        String maze = LoadTXT.haf();

        String[] linesOfMaze = maze.split("\n");
        int lengthOfMaze = linesOfMaze.length;
        float sizeofMaze = 2 / (float) lengthOfMaze;

        char[][] zerosAndOnes = new char[lengthOfMaze][lengthOfMaze];
        for (int i = 0; i < lengthOfMaze; i++) {
            for (int j = 0; j < lengthOfMaze; j++) {
                zerosAndOnes[i][j] = linesOfMaze[i].charAt(j);
            }
        }



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
        Square squareOne = new Square(0, 0, lengthOfMaze);

        // Draw in polygon mod
        //GL33.glPolygonMode(GL33.GL_FRONT_AND_BACK, GL33.GL_LINE);

        Square[][] squares = new Square[lengthOfMaze][lengthOfMaze];
        for (int i = 0; i < zerosAndOnes.length; i++) {
            for (int j = 0; j < lengthOfMaze; j++) {
                    if (zerosAndOnes[i][j] == '1') {
                        squares[i][j] = new Square((j * sizeofMaze - 1), 1 - (i * sizeofMaze), sizeofMaze);
                    } else {
                        squares[i][j] = null;
                    }
            }
        }

        /*

                for (int k = 0; i < lengthOfMaze; k++) {
                    for (int j = 0; j < lengthOfMaze; j++) {
                        if (zeroSAndOnes[i].equals("1")) {
                            squares[k][j] = new Square((k * sizeofMaze) - 1, 1 - (j * sizeofMaze), sizeofMaze);
                        } else {
                            squares[k][j] = new Square(0f, 0f, 0f);
                        }
                    }
                }
         */
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

            for (int i = 0; i < squares.length; i++) {
                for (int j = 0; j < squares.length; j++) {
                    if (squares[i][j] != null) squares[i][j].render();
                }
            }


            // Swap the color buffer -> screen tearing solution
            GLFW.glfwSwapBuffers(window);
            // Listen to input
            GLFW.glfwPollEvents();
        }

        // Don't forget to cleanup
        GLFW.glfwTerminate();
    }

}
