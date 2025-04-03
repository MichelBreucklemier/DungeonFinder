package obj;

import main.Window;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ObjectDataLoader {
    ArrayList<String> settings = new ArrayList<String>();
    ArrayList<Obj> objects = new ArrayList<Obj>();
    Window window;
    public ObjectDataLoader(Window window) {
        this.window = window;
        loadMap();
    }
    public void loadMap() {
        try{
            File map = new File(Paths.get("res\\rooms\\"+ Window.currentRoom).toAbsolutePath().toString());
            Scanner reader = new Scanner(map);
            //Looks only for the __OBJECT_DATA__ because that's all we care about
            while(reader.hasNext() )
                if(reader.nextLine().contains("__Objects"))
                    break;

            //Add every line after that
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                //End when we reach the props data section
                if(line.contains("__Props"))
                    break;

                System.out.println(line);
                settings.add(line);
            }

        }catch(IOException e){
            System.out.println("Failed loading object data");
        } finally {
            process();
        }
    }
    //This will take each individual element of the settings and distribute it to proper methods
    public void process(){
        System.out.println(settings);
        for(int j = 0; j < settings.size(); j++){
            String[] elements = settings.get(j).split("\\{");
            System.out.println(Arrays.toString(elements));
            //To get rid of the end } we use this
            for(int i = 0; i < elements.length; i++){
                elements[i] = elements[i].replace("}","");
            }
            //Now what we can do is treat each setting as a indudual setting instead of the whole line
            for(String element : elements){
                if(!element.isEmpty()) {
                    //all we need to see is the type of the object then hand it off to the method
                    System.out.println(element);
                    String type = element.split("type:\"")[1].split("\",")[0];

                    switch (type) {
                        case "key" -> processKey(element);
                        case "door" -> processDoor(element);
                    }
                }
            }
        }
    }
    private int[] getStartingPos(String setting){
        String pos = setting.split("\\(")[1].split("\\)")[0];
        int x = Integer.parseInt(pos.split(",")[0]);
        int y = Integer.parseInt(pos.split(",")[1]);

        return new int[]{x, y};
    }
    private int getId(String setting){
        return Integer.parseInt(setting.split("id:")[1].split("[,}]")[0]);
    }
    public void processDoor(String setting){
        String sType = setting.split("doorType:")[1].split("[,}]")[0];
        int type = Integer.parseInt(sType);
        int[] pos = getStartingPos(setting);
        objects.add(new OBJ_Door(pos[0],pos[1],type,getId(setting),window.tileManager.currentTiles[pos[1]][pos[0]]));

    }

    public void processKey(String setting){
        //Because the pos of the setting looks like (x,y) we just split it
        int[] pos = getStartingPos(setting);
        objects.add(new OBJ_Key(pos[0],pos[1],getId(setting)));
    }
    public ArrayList<Obj> getObjects() {
        return objects;
    }
}
