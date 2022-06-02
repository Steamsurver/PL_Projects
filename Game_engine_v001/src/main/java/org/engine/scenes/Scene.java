package org.engine.scenes;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.engine.Editor.FileLoader;
import org.engine.GameController.MouseListener;
import org.engine.GameElements.Camera;
import org.engine.Physics2D.Physics2D;
import org.engine.Rendering.Objects.Components.*;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Rendering.Renderer;
import org.engine.Resources.Utils.*;
import org.joml.Vector2f;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Scene {

    private final SceneInitializer sceneInitializer;//инициализатор для сцены
    private  Renderer renderer;
    private Camera camera;
    private boolean isRunning;
    private List<GameObject> gameObjects;
    private List<Sprite> sprites; //набор спрайтов на текущей сцене
    private List<SpriteSheet> spriteSheets; //набор спрайтов на текущей сцене
    private Physics2D physics2D;
    private GameObject renderObject;//через этот объект рендерим текстуры

    public Scene(SceneInitializer sceneInitializer){
        this.sceneInitializer = sceneInitializer;
        this.physics2D = new Physics2D();
        this.renderer = new Renderer();
        this.gameObjects = new ArrayList<>();
        this.isRunning = false;
        this.sprites = new ArrayList<>();
        this.spriteSheets = new ArrayList<>();

        renderObject = new GameObject("Render Object");
        Transform tr = new Transform();
        tr.zIndex = 0;
        renderObject.addComponent(tr);
        renderObject.addComponent(new SpriteRender());
    }

    public void init(){
        this.camera = new Camera(new Vector2f(0, 0));

        this.sceneInitializer.loadResources(this);
        this.sceneInitializer.init(this);
    }

    public void destroy(){
        for(GameObject go : gameObjects){
            go.destroy();
        }
    }

    public void start(){
        for(int i = 0; i < gameObjects.size(); i++){
            GameObject go = gameObjects.get(i);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
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
            this.physics2D.add(go);
        }

    }

    public void update(float dt) {//апдейтер игрового поля

        this.physics2D.update(dt);
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.update(dt);


            if (go.isDead()){//удаление объектов со включенным флагом
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics2D.destroyGameObject(go);
                 i--;
            }
        }
    }

    public void editorUpdate(float dt){//апдейтер движка
        this.camera.adjustProjection();//обновляем проекцию матрицы


        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.editorUpdate(dt);

            if (go.isDead()){//удаление объектов со включенным флагом
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics2D.destroyGameObject(go);
                i--;
            }
        }
    }


    public void render(){
        this.renderer.render();
    }

    public Camera getCamera() {
        return camera;
    }

    public void imGui(){//отрисовка графического интерфейса движка, привязанного к текущей сцене
        this.sceneInitializer.imGui();
    }

    public void save(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting().registerTypeAdapter(Component.class, new Deserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();


        try{
            FileWriter writer = new FileWriter("src/main/java/org/engine/Config/level.txt");

            List<GameObject> objsToSerialize = new ArrayList<>();//массив с объектами, которые нужно сериализовыввать
            for(GameObject obj : this.gameObjects){
                if(obj.doSerialization()){
                    objsToSerialize.add(obj);
                }
            }

            writer.write(gson.toJson(objsToSerialize));
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public GameObject createGameObject(String name){//через этот метод можно создавать класс игрового объекта
        GameObject go = new GameObject(name);
        go.addComponent(new Transform());
        go.transform = go.getComponent(Transform.class);
        return go;
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


        //=================================================Подгрузка игровых объектов=========================================================
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
        }

        FileLoader.load();
    }

    public GameObject getGameObject(int gameObjectId) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUid() == gameObjectId)
                .findFirst();
        return result.orElse(null);
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }
}

