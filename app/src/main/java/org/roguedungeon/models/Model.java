package org.roguedungeon.models;

import org.roguedungeon.render.Shader;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;

public class Model {
    private final int vao;

    public Model(FloatBuffer buffer) {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        final int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private void enable() {
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
    }

    private void disable() {
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public void render(Shader shader) {
        shader.bind();
        enable();
        glDrawArrays(GL_TRIANGLES, 0, 3);
        disable();
        shader.unbind();
    }
}
