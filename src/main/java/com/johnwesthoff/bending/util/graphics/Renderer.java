package com.johnwesthoff.bending.util.graphics;

import com.johnwesthoff.bending.logic.World;
import com.johnwesthoff.bending.util.network.ResourceLoader;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import com.johnwesthoff.bending.Constants;
import org.lwjgl.system.MemoryUtil;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class Renderer {

    private int shaderProgram;
    private int vao;
    private int terrainDataTexture;
    private int textureAtlasTexture;

    public void init(TextureAtlas textureAtlas) {
        GL.createCapabilities();

        // Create and compile the shaders
        try {
            String vertexShaderSource = ResourceLoader.loadResourceAsString("/shaders/terrain.vert");
            String fragmentShaderSource = ResourceLoader.loadResourceAsString("/shaders/terrain.frag");
            shaderProgram = createShaderProgram(vertexShaderSource, fragmentShaderSource);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shaders", e);
        }


        // Create the quad
        float[] vertices = {
                // positions         // texture coords
                1.0f,  1.0f, 0.0f,  1.0f, 1.0f, // top right
                1.0f, -1.0f, 0.0f,  1.0f, 0.0f, // bottom right
                -1.0f, -1.0f, 0.0f, 0.0f, 0.0f, // bottom left
                -1.0f,  1.0f, 0.0f, 0.0f, 1.0f  // top left
        };
        int[] indices = {
                0, 1, 3, // first triangle
                1, 2, 3  // second triangle
        };

        vao = glGenVertexArrays();
        int vbo = glGenBuffers();
        int ebo = glGenBuffers();

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(verticesBuffer);


        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(indicesBuffer);

        // position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        // texture coord attribute
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // Create the texture atlas texture
        textureAtlasTexture = createTextureFromBufferedImage(textureAtlas.getAtlas());

        // Create the terrain data texture
        terrainDataTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, terrainDataTexture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    private int createTextureFromBufferedImage(BufferedImage image) {
        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        MemoryUtil.memFree(buffer);

        return textureId;
    }

    public void draw(World world, TextureAtlas textureAtlas) {
        // Update terrain data texture
        glBindTexture(GL_TEXTURE_2D, terrainDataTexture);
        ByteBuffer buffer = MemoryUtil.memAlloc(world.wIdTh * world.hEigHt);
        for (int y = 0; y < world.hEigHt; y++) {
            for (int x = 0; x < world.wIdTh; x++) {
                buffer.put(world.ground.cellData[x][y]);
            }
        }
        buffer.flip();
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, world.wIdTh, world.hEigHt, 0, GL_RED, GL_UNSIGNED_BYTE, buffer);
        MemoryUtil.memFree(buffer);


        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glUseProgram(shaderProgram);

        // Bind textures
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, terrainDataTexture);
        glUniform1i(glGetUniformLocation(shaderProgram, "terrainData"), 0);

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, textureAtlasTexture);
        glUniform1i(glGetUniformLocation(shaderProgram, "textureAtlas"), 1);

        glUniform2i(glGetUniformLocation(shaderProgram, "viewPos"), world.viewX, world.viewY);

        // Set uniforms
        float[] atlasMapping = new float[128 * 4];

        // This mapping is based on the order in TextureAtlas.TEXTURE_FILES
        // and the constants in Constants.java
        float[] grassCoords = textureAtlas.getTextureCoordinates("GrassTexture.jpg");
        atlasMapping[Constants.GROUND * 4 + 0] = grassCoords[0];
        atlasMapping[Constants.GROUND * 4 + 1] = grassCoords[1];
        atlasMapping[Constants.GROUND * 4 + 2] = grassCoords[2] - grassCoords[0];
        atlasMapping[Constants.GROUND * 4 + 3] = grassCoords[3] - grassCoords[1];
        atlasMapping[Constants.UGROUND * 4 + 0] = grassCoords[0];
        atlasMapping[Constants.UGROUND * 4 + 1] = grassCoords[1];
        atlasMapping[Constants.UGROUND * 4 + 2] = grassCoords[2] - grassCoords[0];
        atlasMapping[Constants.UGROUND * 4 + 3] = grassCoords[3] - grassCoords[1];

        float[] sandCoords = textureAtlas.getTextureCoordinates("SandTexture.jpg");
        atlasMapping[Constants.SAND * 4 + 0] = sandCoords[0];
        atlasMapping[Constants.SAND * 4 + 1] = sandCoords[1];
        atlasMapping[Constants.SAND * 4 + 2] = sandCoords[2] - sandCoords[0];
        atlasMapping[Constants.SAND * 4 + 3] = sandCoords[3] - sandCoords[1];

        float[] stoneCoords = textureAtlas.getTextureCoordinates("StoneTexture.jpg");
        atlasMapping[Constants.STONE * 4 + 0] = stoneCoords[0];
        atlasMapping[Constants.STONE * 4 + 1] = stoneCoords[1];
        atlasMapping[Constants.STONE * 4 + 2] = stoneCoords[2] - stoneCoords[0];
        atlasMapping[Constants.STONE * 4 + 3] = stoneCoords[3] - stoneCoords[1];
        atlasMapping[Constants.USTONE * 4 + 0] = stoneCoords[0];
        atlasMapping[Constants.USTONE * 4 + 1] = stoneCoords[1];
        atlasMapping[Constants.USTONE * 4 + 2] = stoneCoords[2] - stoneCoords[0];
        atlasMapping[Constants.USTONE * 4 + 3] = stoneCoords[3] - stoneCoords[1];

        float[] barkCoords = textureAtlas.getTextureCoordinates("BarkTexture.jpg");
        atlasMapping[Constants.TREE * 4 + 0] = barkCoords[0];
        atlasMapping[Constants.TREE * 4 + 1] = barkCoords[1];
        atlasMapping[Constants.TREE * 4 + 2] = barkCoords[2] - barkCoords[0];
        atlasMapping[Constants.TREE * 4 + 3] = barkCoords[3] - barkCoords[1];

        float[] iceCoords = textureAtlas.getTextureCoordinates("iceTexture.png");
        atlasMapping[Constants.ICE * 4 + 0] = iceCoords[0];
        atlasMapping[Constants.ICE * 4 + 1] = iceCoords[1];
        atlasMapping[Constants.ICE * 4 + 2] = iceCoords[2] - iceCoords[0];
        atlasMapping[Constants.ICE * 4 + 3] = iceCoords[3] - iceCoords[1];
        atlasMapping[Constants.UICE * 4 + 0] = iceCoords[0];
        atlasMapping[Constants.UICE * 4 + 1] = iceCoords[1];
        atlasMapping[Constants.UICE * 4 + 2] = iceCoords[2] - iceCoords[0];
        atlasMapping[Constants.UICE * 4 + 3] = iceCoords[3] - iceCoords[1];

        float[] crystalCoords = textureAtlas.getTextureCoordinates("crystalTexture.png");
        atlasMapping[Constants.CRYSTAL * 4 + 0] = crystalCoords[0];
        atlasMapping[Constants.CRYSTAL * 4 + 1] = crystalCoords[1];
        atlasMapping[Constants.CRYSTAL * 4 + 2] = crystalCoords[2] - crystalCoords[0];
        atlasMapping[Constants.CRYSTAL * 4 + 3] = crystalCoords[3] - crystalCoords[1];

        float[] lavaCoords = textureAtlas.getTextureCoordinates("lavalandTexture.png");
        atlasMapping[Constants.LAVA * 4 + 0] = lavaCoords[0];
        atlasMapping[Constants.LAVA * 4 + 1] = lavaCoords[1];
        atlasMapping[Constants.LAVA * 4 + 2] = lavaCoords[2] - lavaCoords[0];
        atlasMapping[Constants.LAVA * 4 + 3] = lavaCoords[3] - lavaCoords[1];

        float[] etherCoords = textureAtlas.getTextureCoordinates("ether.png");
        atlasMapping[Constants.ETHER * 4 + 0] = etherCoords[0];
        atlasMapping[Constants.ETHER * 4 + 1] = etherCoords[1];
        atlasMapping[Constants.ETHER * 4 + 2] = etherCoords[2] - etherCoords[0];
        atlasMapping[Constants.ETHER * 4 + 3] = etherCoords[3] - etherCoords[1];

        FloatBuffer atlasMappingBuffer = MemoryUtil.memAllocFloat(atlasMapping.length);
        atlasMappingBuffer.put(atlasMapping).flip();
        glUniform4fv(glGetUniformLocation(shaderProgram, "atlasMapping"), atlasMappingBuffer);
        MemoryUtil.memFree(atlasMappingBuffer);


        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
    }

    public void destroy() {
        // Clean up resources
        glDeleteVertexArrays(vao);
        //glDeleteBuffers(vbo);
        //glDeleteBuffers(ebo);
        glDeleteProgram(shaderProgram);
    }

    private int createShaderProgram(String vertexShaderSource, String fragmentShaderSource) {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Failed to compile vertex shader: " + glGetShaderInfoLog(vertexShader));
        }

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Failed to compile fragment shader: " + glGetShaderInfoLog(fragmentShader));
        }

        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Failed to link shader program: " + glGetProgramInfoLog(shaderProgram));
        }

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        return shaderProgram;
    }
}
