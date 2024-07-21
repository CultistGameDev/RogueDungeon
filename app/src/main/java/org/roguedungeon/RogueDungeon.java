package org.roguedungeon;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import org.roguedungeon.config.Configuration;
import org.roguedungeon.models.Model;
import org.roguedungeon.render.Shader;
import org.roguedungeon.render.Window;

import java.nio.FloatBuffer;

import static org.lwjgl.system.MemoryUtil.*;


public class RogueDungeon extends Window {
    private Model model;
    private Shader shader;
    private boolean isSetup = false;

    private Configuration config;

    public static String getVersion() {
        return "1.0.SNAPSHOT";
    }

    protected RogueDungeon() {
        config = Configuration.config();
        init(config);
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
                System.err.println(e.getLocalizedMessage());
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
        if (ImGui.begin("Demo", ImGuiWindowFlags.AlwaysAutoResize)) {
            ImGui.text("Hello World!");
        }
        ImGui.end();
        model.render(shader);
    }

    @Override
    protected void postProcess() {
    }
}