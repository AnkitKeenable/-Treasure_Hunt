package object;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class OBJ_Boots extends SuperObject {
    public OBJ_Boots(){
        name="Boots";
        try {
            image= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/objects/boots.png")));

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
