package org.roguedungeon;

import imgui.ImGui;
import imgui.app.Application;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.roguedungeon.models.Model;
import org.roguedungeon.render.Shader;
import org.roguedungeon.render.Window;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class RogueDungeon extends Window {
    private Model model;
    private Shader shader;
    private boolean isSetup = false;

    public String getVersion() {
        return "1.0.SNAPSHOT";
    }

    protected RogueDungeon() {
        init();
    }

    public static void main(String[] args) {
        new RogueDungeon().run();
    }

    public void setupData() {
        if (!isSetup) {
            try {
                shader = new Shader("shaders/vert.shader", "shaders/frag.shader");
                shader.createShader();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            float[] vertices = new float[]{
                    0.0f, 0.5f, 0.0f,
                    -0.5f, -0.5f, 0.0f,
                    0.5f, -0.5f, 0.0f
            };

            FloatBuffer verticiesBuffer = memAllocFloat(vertices.length);
            verticiesBuffer.put(vertices).flip();

            model = new Model(verticiesBuffer);
            memFree(verticiesBuffer);

            isSetup = true;
        }
    }

    @Override
    protected void preProcess() {
        setupData();
    }

    @Override
    protected void process() {
        model.render(shader);
    }

    @Override
    protected void postProcess() {

    }
}