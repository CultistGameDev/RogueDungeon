package org.roguedungeon.render;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import org.tinylog.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Scanner;

import static org.lwjgl.opengl.GL30.*;

public class Shader {
    private final int programId;
    private int vertShaderId;
    private int fragShaderId;

    private final HashMap<String, Integer> uniforms;

    private final String vertShaderFilename;
    private final String fragShaderFilename;

    public Shader(String vertShaderFilename, String fragShaderFilename) throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
        uniforms = new HashMap<>();
        this.vertShaderFilename = vertShaderFilename;
        this.fragShaderFilename = fragShaderFilename;
    }

    public void createUniform(String name) throws Exception {
        int loc = glGetUniformLocation(programId, name);
        if (loc < 0) {
            throw new Exception("Could not find uniform: " + name);
        }
        uniforms.put(name, loc);
    }

    public void setUniform(String name, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            matrix.get(fb);
            glUniformMatrix4fv(uniforms.get(name), false, fb);
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    public void createShader() throws Exception {
        int vertShader = 0;
        try {
            vertShader = compileShader(vertShaderFilename, GL_VERTEX_SHADER);
        } catch (Exception e) {
            Logger.error("Could not create shader {}", vertShaderFilename);
            Logger.error(e.getStackTrace());
        }

        int fragShader = 0;
        try {
            fragShader = compileShader(fragShaderFilename, GL_FRAGMENT_SHADER);
        } catch (Exception e) {
            Logger.error("Could not create shader {}", fragShaderFilename);
            Logger.error(e.getStackTrace());
        }

        glAttachShader(programId, vertShader);
        glAttachShader(programId, fragShader);

        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertShader != 0) {
            glDetachShader(programId, vertShader);
        }
        if (fragShader != 0) {
            glDetachShader(programId, fragShader);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            Logger.error("Warning validating Shader code: {}", glGetProgramInfoLog(programId, 1024));
            throw new Exception("Error validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    private int compileShader(String filename, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        String contents;
        try (InputStreamReader isr = new InputStreamReader(getInputStream(filename));
             Scanner scanner = new Scanner(isr)) {
            contents = scanner.useDelimiter("\\Z").next();
        } catch (Exception e) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, contents);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new Exception("Error creating shader. Shader code:\n" + glGetShaderInfoLog(shaderId, 1024));
        }

        return shaderId;
    }

    private InputStream getInputStream(String filename) throws Exception {
        InputStream ioStream = getClass().getClassLoader().getResourceAsStream(filename);
        if (ioStream == null) {
            throw new Exception("Failed to load file");
        }
        return ioStream;
    }
}
