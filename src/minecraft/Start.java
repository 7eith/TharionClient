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
        		"--token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlNua2giLCJpcCI6Ijo6ZmZmZjoxMjcuMC4wLjEiLCJpYXQiOjE1Njc5MjM0NzksImV4cCI6MTU2ODAwOTg3OX0.QrbSAgnjiaGQ8d0iG4_nazEYJavc84w32g0BZj_3tkE",
        		"--debug", "1"
        }, args));
    }

    public static <T> T[] concat(T[] first, T[] second)
    {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
