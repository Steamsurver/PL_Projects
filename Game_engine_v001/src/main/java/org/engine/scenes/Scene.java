package org.engine.scenes;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import org.engine.GameElements.Camera;
import org.engine.Rendering.Objects.Components.Component;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Rendering.Renderer;
import org.engine.Resources.Utils.Deserializer;
import org.engine.Resources.Utils.GameObjectDeserializer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;
    protected boolean levelLoaded = false;

    public Scene(){

    }

    public int getGOSize(){
        return gameObjects.size();
    }

    public void init(){

    }

    public void start(){
        for(GameObject go : gameObjects){
            go.start();
            this.renderer.add(go);
        }

        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go){
        if(!isRunning){
            gameObjects.add(go);
        }else
        {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }

    }

    public abstract void update(float dt);

    public Camera getCamera() {
        return camera;
    }

    public void sceneImGui(){
        if(activeGameObject != null){
            ImGui.begin("Inspector");
            activeGameObject.imGui();
            ImGui.end();
        }

        imGui();
    }

    public void imGui(){

    }

    public void saveExit(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting().registerTypeAdapter(Component.class, new Deserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();


        try{
            FileWriter writer = new FileWriter("src/main/java/org/engine/Config/level.txt");
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public void load(){
        //подгрузка параметров/Десериализация
        Gson gson = new GsonBuilder()
                .setPrettyPrinting().registerTypeAdapter(Component.class, new Deserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
        String inFile = "";
        try{
            inFile = new String(Files.readAllBytes(Paths.get("src/main/java/org/engine/Config/level.txt")));
        }catch (IOException e){
            e.printStackTrace();
        }



        if(!inFile.equals("")){
            int maxGoId = -1;
            int maxCompId = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for(int i = 0; i < objs.length; i++){
                addGameObjectToScene(objs[i]);

                for(Component c : objs[i].getAllComponents()){
                    if(c.getUid() > maxCompId){
                        maxCompId = c.getUid();
                    }
                }
                if(objs[i].getUid() > maxGoId){
                    maxGoId = objs[i].getUid();
                }


            }

            maxGoId++;
            maxCompId++;
            GameObject.init(maxGoId);
            Component.init(maxCompId);
            this.levelLoaded = true;
        }
    }

}

