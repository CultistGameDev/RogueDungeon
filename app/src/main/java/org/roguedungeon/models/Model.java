package org.roguedungeon.models;


import org.joml.Matrix4f;
import org.roguedungeon.interfaces.Disposable;
import org.roguedungeon.render.Camera;
import org.roguedungeon.render.Shader;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Model implements Disposable {
    private final int vao;
    private final int vbo;
    private final int vertexCount;

    public Model(float data[]) {
        FloatBuffer buffer = null;
        try {
            buffer = memAllocFloat(data.length);
            vertexCount = data.length;
            buffer.put(data).flip();

            vao = glGenVertexArrays();
            glBindVertexArray(vao);

            vbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glBindVertexArray(0);
        } finally {
            if (buffer != null) {
                memFree(buffer);
            }
        }
    }

    public Matrix4f modelMatrix() {
        return new Matrix4f();
    }

    private void enable() {
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
    }

    private void disable() {
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("model", modelMatrix());
        shader.setUniform("proj", camera.getProjection());
        shader.setUniform("view", camera.getView());
        enable();
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        disable();
        shader.unbind();
    }

    @Override
    public void dispose() {
        disable();
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
    }
}
