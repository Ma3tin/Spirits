package cz.educanet;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Square {
    public static int uniformMatrixLocation;
    private final int[] indices = {
            0, 1, 3, // First triangle
            1, 2, 3 // Second triangle
    };

    public int squareVaoId;
    public int squareVboId;
    public Matrix4f matrix;
    public FloatBuffer matrixFloatBuffer;
    public float[] red;
    public float[] green;
    public FloatBuffer floatBufferColors;
    public float[] textureIndices;
    private float[] vertices;
    private int squareEboId;
    private int squareColorId;
    private int textureId;
    private float x;
    private float y;
    private float size;
    public int frame = 0;
    public FloatBuffer tb = BufferUtils.createFloatBuffer(8);


    public Square(float x, float y, float size) {
        this.x = x;
        this.y = y;
        this.size = size;
        matrix = new Matrix4f().identity();
        matrixFloatBuffer = BufferUtils.createFloatBuffer(16);

        float longest = (float) Math.sqrt(2);
        float c1 = (float) Math.sqrt(((x + size) * (x + size)) + (y * y));
        float c2 = (float) Math.sqrt(((x + size) * (x + size)) + ((y - size) * (y - size)));
        float c3 = (float) Math.sqrt((x * x) + ((y - size) * (y - size)));
        float c4 = (float) Math.sqrt((x * x) + (y * y));
        float percent1 = (c1 / longest);
        float percent2 = (c2 / longest);
        float percent3 = (c3 / longest);
        float percent4 = (c4 / longest);

        float[] colors = {
                1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f,
        };

        float[] textureIndices = {
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,
                0.0f, 0.0f
        };

        red = new float[]{
                1f, 0f, 0f, 1f,
                1f, 0f, 0f, 1f,
                1f, 0f, 0f, 1f,
                1f, 0f, 0f, 1f,
        };

        green = new float[]{
                0f, 1f, 0f, 1f,
                0f, 1f, 0f, 1f,
                0f, 1f, 0f, 1f,
                0f, 1f, 0f, 1f,
        };

        float[] vertices = {
                x + size, y, 0.0f, // 0 -> Top right
                x + size, y - size, 0.0f, // 1 -> Bottom right
                x, y - size, 0.0f, // 2 -> Bottom left
                x, y, 0.0f, // 3 -> Top left
        };

        floatBufferColors = BufferUtils.createFloatBuffer(red.length).put(red).flip();
        this.vertices = vertices;

        squareVaoId = GL33.glGenVertexArrays();
        squareEboId = GL33.glGenBuffers();
        squareVboId = GL33.glGenBuffers();
        squareColorId = GL33.glGenBuffers();
        textureId = GL33.glGenBuffers();

        loadImage();

        uniformMatrixLocation = GL33.glGetUniformLocation(Shaders.shaderProgramId, "matrix");

        GL33.glBindVertexArray(squareVaoId);

        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, squareEboId);
        IntBuffer ib = BufferUtils.createIntBuffer(indices.length)
                .put(indices)
                .flip();
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW);


        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareColorId);
        FloatBuffer cfb = BufferUtils.createFloatBuffer(colors.length).put(colors).flip();


        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cfb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 4, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);

        MemoryUtil.memFree(cfb);

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareVboId);

        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
                .put(vertices)
                .flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);

        GL33.glUseProgram(Shaders.shaderProgramId);
        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);

        MemoryUtil.memFree(fb);
        MemoryUtil.memFree(ib);

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, textureId);
        tb.put(textureIndices).flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, tb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(2, 2, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(2);

    }

    public void render() {
        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);


        GL33.glUseProgram(Shaders.shaderProgramId);

        GL33.glBindVertexArray(squareVaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }


    public void update(long window) {
        int move = frame % 6;
        switch (move) {
            case 0: textureIndices = new float[]{
                    0.15f, 0.0f,
                    0.15f, 0.15f,
                    0.0f, 0.15f,
                    0.0f, 0.0f
            };
            case 1: textureIndices = new float[]{
                    0.3f, 0.15f,
                    0.3f, 0.3f,
                    0.15f, 0.3f,
                    0.15f, 0.15f
            };
            case 2: textureIndices = new float[]{
                    0.45f, 0.3f,
                    0.45f, 0.45f,
                    0.3f, 0.45f,
                    0.3f, 0.3f
            };
            case 3: textureIndices = new float[]{
                    0.6f, 0.45f,
                    0.6f, 0.6f,
                    0.45f, 0.6f,
                    0.45f, 0.6f
            };
            case 4: textureIndices = new float[]{
                    0.75f, 0.6f,
                    0.75f, 0.75f,
                    0.6f, 0.75f,
                    0.6f, 0.6f
            };
            case 5: textureIndices = new float[]{
                    0.9f, 0.75f,
                    0.9f, 0.9f,
                    0.75f, 0.9f,
                    0.75f, 0.75f
            };
        }
        move++;
        tb.put(textureIndices).flip();

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, textureId);
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, tb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(2, 2, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(2);
    }


    private void loadImage() {
        MemoryStack stack = MemoryStack.stackPush();
        IntBuffer wide = stack.mallocInt(1);
        IntBuffer height = stack.mallocInt(1);
        IntBuffer comp = stack.mallocInt(1);

        ByteBuffer img = STBImage.stbi_load("resources/Cyborg_run.png", wide, height, comp, 3);
        if (img != null) {
            img.flip();
            GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);
            GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGB, wide.get(), height.get(), 0, GL33.GL_RGB, GL33.GL_UNSIGNED_BYTE, img);
            GL33.glGenerateMipmap(GL33.GL_TEXTURE_2D);
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSize() {
        return size;
    }


}
