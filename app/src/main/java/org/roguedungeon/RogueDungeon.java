package org.roguedungeon;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector3f;
import org.roguedungeon.config.Configuration;
import org.roguedungeon.models.Model;
import org.roguedungeon.render.Camera;
import org.roguedungeon.render.Shader;
import org.roguedungeon.render.Window;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class RogueDungeon extends Window {
    private Model model;
    private Shader shader;
    private boolean isSetup = false;
    private Camera camera;

    private Configuration config;

    private static String version = null;

    public static String getVersion() {
        return version;
    }

    protected RogueDungeon() {
        detectVersion();
        config = Configuration.config();
        init(config);
        setupData();
    }

    private void detectVersion() {
        try {
            Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                try {
                    Manifest manifest = new Manifest(resources.nextElement().openStream());
                    Attributes attributes = manifest.getMainAttributes();
                    String title = attributes.getValue("Implementation-Title");
                    if (title != null && title.equals("RogueDungeon")) {
                        version = manifest.getMainAttributes().getValue("Implementation-Version");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (version == null) {
            version = "<dev build>";
        }
    }

    public static void main(String[] args) {
        RogueDungeon game = new RogueDungeon();
        System.out.println("Rogue Dungeon: " + RogueDungeon.getVersion());
        game.run();
        game.dispose();
    }

    public void setupData() {
        if (!isSetup) {
            try {
                shader = new Shader("shaders/vert.shader", "shaders/frag.shader");
                shader.createShader();
                shader.createUniform("model");
                shader.createUniform("view");
                shader.createUniform("proj");
            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
                return;
            }

            float[] vertices = new float[]{
                    -0.5f, 0.5f, 0.0f,
                    -0.5f, -0.5f, 0.0f,
                    0.5f, 0.5f, 0.0f,
                    0.5f, 0.5f, 0.0f,
                    -0.5f, -0.5f, 0.0f,
                    0.5f, -0.5f, 0.0f,
            };

            model = new Model(vertices);

            camera = new Camera(config.windowWidth(), config.windowHeight(), new Vector3f(0.0f, 0.0f, -1.0f));

            isSetup = true;
        }
    }

    @Override
    protected void preProcess() {
    }

    @Override
    protected void process() {
        if (ImGui.begin("Demo", ImGuiWindowFlags.AlwaysAutoResize)) {
            ImGui.text("Hello World!");
        }
        ImGui.end();
        model.render(shader, camera);
    }

    @Override
    protected void postProcess() {
    }
}