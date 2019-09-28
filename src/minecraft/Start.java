import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

import net.minecraft.client.main.Main;

public class Start
{
    public static void main(String[] args)
    {
        Main.main(concat(new String[] {
        		"--version", "1.0.0", 
        		"--accessToken", "0",
        		"--assetsDir", "assets",
        		"--assetIndex", "1.7.10",
        		"--userProperties", "{}",
        		"--username", "Snkh",
        		"--sessionToken", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlNua2giLCJpcCI6Ijo6ZmZmZjoxMjcuMC4wLjEiLCJpYXQiOjE1Njk2ODk3NTUsImV4cCI6MTU2OTc3NjE1NX0.7Tnes_LSL5RTQfCIbO38aWm0saFucHge0gGmnUSkE90"
        }, args));
    }

    public static <T> T[] concat(T[] first, T[] second)
    {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
