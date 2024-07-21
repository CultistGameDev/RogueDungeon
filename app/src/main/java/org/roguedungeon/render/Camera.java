package org.roguedungeon.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projection;
    private Matrix4f view;

    private float fov = (float) Math.toRadians(80);
    private final float zNear = 0.01f;
    private final float zFar = 1000.0f;
    private int width;
    private int height;

    private Vector3f position;
    private Vector3f up;
    private Vector3f rotation;

    public Camera(int width, int height) {
        this.width = width;
        this.height = height;
        view = new Matrix4f();
        projection = new Matrix4f();
        position = new Vector3f();
        up = new Vector3f(0.0f, 1.0f, 0.0f);
        rotation = new Vector3f();
    }

    public Camera(int width, int height, Vector3f position) {
        this.width = width;
        this.height = height;
        view = new Matrix4f();
        projection = new Matrix4f();
        this.position = position;
        up = new Vector3f(0.0f, 1.0f, 0.0f);
        rotation = new Vector3f();
    }

    public Matrix4f getProjection() {
        float aspectRatio = (float) width / (float) height;
        projection.identity().perspective(fov, aspectRatio, zNear, zFar);
        return projection;
    }

    public Matrix4f getView() {
        view.identity()
//                .rotate((float) Math.toRadians(rotation.x), new Vector3f(1.0f, 0.0f, 0.0f))
//                .rotate((float) Math.toRadians(rotation.y), new Vector3f(0.0f, 1.0f, 0.0f))
                .translate(position);
        return view;
    }
}
