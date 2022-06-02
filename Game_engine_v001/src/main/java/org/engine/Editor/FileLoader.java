package org.engine.Editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.engine.EventSystem.Events.Event;
import org.engine.EventSystem.Events.EventType;
import org.engine.EventSystem.Observers.EventSystem;
import org.engine.GameController.KeyListener;
import org.engine.GameElements.Window;
import org.engine.Rendering.Assets.Texture.Texture;
import org.engine.Rendering.Objects.Components.Component;
import org.engine.Rendering.Objects.Components.Sprite;
import org.engine.Rendering.Objects.Components.SpriteSheet;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Resources.Utils.*;
import org.engine.scenes.Scene;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.stb.STBImage.stbi_load;

public class FileLoader {
    private ImBoolean close;
    private ImString source;//путь на физическои диске до файла
    private ImString URL_path;//ссылка на файл
    private ImString URL_filename;//ссылка на файл
    private ImInt sizeX;//ширина текстуры
    private ImInt sizeY;//высота текстуры
    private ImInt numSpr;//кол-во спрайтов для листа
    private ImInt space;//спэйсинг для листа

    private static LoadThreadURL classThreadURL = new LoadThreadURL();


    private static FileLoader instance = null;


    private FileLoader(){
        close = new ImBoolean(true);
        source = new ImString();
        URL_path = new ImString();
        URL_filename = new ImString();
        sizeX = new ImInt(0);
        sizeY = new ImInt(0);
        numSpr = new ImInt(0);
        space = new ImInt(0);

        FileLoader.classThreadURL.start();
    }

    private static FileLoader getThis(){
        if(FileLoader.instance == null){
            FileLoader.instance = new FileLoader();
        }

        return FileLoader.instance;
    }


    private static void save(){ //Сэйвер
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Sprite.class, new SpriteDeserializer()).create();


        try{
            FileWriter writer = new FileWriter("src/main/java/org/engine/Config/Textures.txt");
            FileWriter writerSprS = new FileWriter("src/main/java/org/engine/Config/SpriteSheets.txt");

            writer.write(gson.toJson(AssetsPool.getToArrayOfSprite()));
            writerSprS.write(gson.toJson(AssetsPool.getToArrayOfSpriteSheets()));

            writer.close();
            writerSprS.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public static void load(){

        String inFileSprite = "";
        String inFileSpriteSheet = "";
        try{
            inFileSprite = new String(Files.readAllBytes(Paths.get("src/main/java/org/engine/Config/Textures.txt")));
            inFileSpriteSheet = new String(Files.readAllBytes(Paths.get("src/main/java/org/engine/Config/SpriteSheets.txt")));
        }catch (IOException e){
            e.printStackTrace();
        }


        //=================================================Подгрузка текстур=========================================================
        if(!inFileSprite.equals("")){
            Gson gsonS = new GsonBuilder()
                    .setPrettyPrinting().registerTypeAdapter(Sprite.class, new SpriteDeserializer())
                    .create();

            Sprite[] spritesJ = gsonS.fromJson(inFileSprite, Sprite[].class);

            ArrayList<Sprite> temp = new ArrayList<>(Arrays.asList(spritesJ));

            AssetsPool.reloadArrayOfSprite(temp);
        }


        //=================================================Подгрузка листов спрайтов==================================================
        if(!inFileSpriteSheet.equals("")){
            Gson gsonS = new GsonBuilder()
                    .setPrettyPrinting().registerTypeAdapter(SpriteSheet.class, new SpriteSheetDeserializer())
                    .create();

            SpriteSheet[] spritesSheetJ = gsonS.fromJson(inFileSpriteSheet, SpriteSheet[].class);

            ArrayList<SpriteSheet> temp = new ArrayList<>(Arrays.asList(spritesSheetJ));

            AssetsPool.reloadArrayOfSpriteSheets(temp);
        }
    }


    public static boolean loadTexture(){//загрузка 1-го конкретного спрайта
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);


        ImGui.begin("Load Texture", getThis().close);

        ImGui.inputText("  :Input path", getThis().source);

        if(ImGui.button("Load")){
            //проверка на существование
            ByteBuffer file = stbi_load(getThis().source.get(), width, height,channels, 0);
            if(file == null){
                getThis().source.set("Bad way!");
            }

            if(file != null){
                Sprite sprite = new Sprite();
                sprite.setTexture(AssetsPool.getTexture(getThis().source.get()));//подтягиваем текстуру из файла

                if(getThis().sizeX.get() != 0){
                    sprite.setWidth(getThis().sizeX.get());
                }

                if(getThis().sizeY.get() != 0){
                    sprite.setHeight(getThis().sizeY.get());
                }

                AssetsPool.getToArrayOfSprite().add(sprite);

                FileLoader.save();//сохранение в файле
                EventSystem.notify(null, new Event(EventType.SaveLevel));
                EventSystem.notify(null, new Event(EventType.LoadLevel));

                getThis().close.set(false);
                ImGui.end();
                return false;//закрывает флаг на запрос открыть окно
            }

        }

        ImGui.inputInt("  :SizeX", getThis().sizeX);
        ImGui.inputInt("  :SizeY", getThis().sizeY);



        //Закрытие окна через пробел
        if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)){
            getThis().close.set(false);
            ImGui.end();
            return false;//закрывает флаг на запрос открыть окно
        }

        ImGui.end();
        return true;
    }


    public static boolean loadSpriteSheet(){//загрузка листа спрайта
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);


        ImGui.begin("Load SpriteSheet", getThis().close);

        ImGui.inputText("  :Input path", getThis().source);

        if(ImGui.button("Load")){
            //проверка на существование
            ByteBuffer file = stbi_load(getThis().source.get(), width, height,channels, 0);
            if(file == null){
                getThis().source.set("Bad way!");
            }

            if(file != null){

                AssetsPool.addSpriteSheet(getThis().source.get(),
                        new SpriteSheet(AssetsPool.getTexture(getThis().source.get()),
                                getThis().sizeX.get(), getThis().sizeY.get(), getThis().numSpr.get(), getThis().space.get()));

                AssetsPool.getToArrayOfSpriteSheets().add(AssetsPool.getSpriteSheet(getThis().source.get()));

                FileLoader.save();//сохранение в файле


                EventSystem.notify(null, new Event(EventType.SaveLevel));
                EventSystem.notify(null, new Event(EventType.LoadLevel));
                getThis().close.set(false);
                ImGui.end();
                return false;//закрывает флаг на запрос открыть окно
            }

        }

        ImGui.inputInt("  :SizeX", getThis().sizeX);
        ImGui.inputInt("  :SizeY", getThis().sizeY);
        //ImGui.inputInt("  :Number sprites", getThis().numSpr);
        //ImGui.inputInt("  :Spacing", getThis().space);


        //Закрытие окна через пробел
        if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)){
            getThis().close.set(false);
            ImGui.end();
            return false;//закрывает флаг на запрос открыть окно
        }

        ImGui.end();
        return true;
    }



    public static boolean loadURLImage(){
        //https://64.media.tumblr.com/56898cac9ff033ed6c7c527430478e19/tumblr_muq5j4RD9n1r9hnaao1_400.png
        //https://d11kvfv4kxw5s4.cloudfront.net/wp-content/uploads/sites/15/2021/04/20112008/silver-notes.jpg
        //https://www.mutualart.com/img/homePageSliderImg/never_miss_an_exhibition.png
        ImGui.begin("Load URL Image", getThis().close);

        ImGui.inputText("  :Input URL path", getThis().URL_path);
        ImGui.inputText("  :Input File name", getThis().URL_filename);

        if(ImGui.button("Load")){
            try{
                FileLoader.classThreadURL.setDownload(true);// ЗАГРУЗКА В ОТДЕЛЬНОМ ПОТОКЕ <-----------------------------------------

                EventSystem.notify(null, new Event(EventType.SaveLevel));
                EventSystem.notify(null, new Event(EventType.LoadLevel));
                getThis().close.set(false);
                ImGui.end();
                return false;//закрывает флаг на запрос открыть окно

            }catch(Exception e){
                System.out.println("Bad path or unexpected error: " + e.getMessage());
            }
        }


        //Закрытие окна через пробел
        if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)){
            getThis().close.set(false);
            ImGui.end();
            return false;//закрывает флаг на запрос открыть окно
        }

        ImGui.end();
        return true;
    }


    private static void LoadImageFromURL(){//метод загрузки изображения
        try{

            if(getThis().URL_path.get() != "") {
                URL url_file = new URL(getThis().URL_path.get());
                ReadableByteChannel rbc = Channels.newChannel(url_file.openStream());
                FileOutputStream fos = new FileOutputStream("src/main/java/org/engine/Resources/Textures/Abstraction/" + getThis().URL_filename.get());//путь выгрузки

                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();
            }

        }catch(Exception e){
            System.out.println("Bad path or unexpected error: " + e.getMessage());
        }
    }

    static class LoadThreadURL extends Thread{
        private boolean isDownload = false;

        public void run() {
            while(true) {
                Thread loadCurrent = Thread.currentThread();
                while (isDownload) {
                    System.out.println("Downloading");
                    FileLoader.LoadImageFromURL();
                    isDownload = false;
                }
            }
        }

        public void setDownload(boolean download){
            this.isDownload = download;
        }
    }
}
