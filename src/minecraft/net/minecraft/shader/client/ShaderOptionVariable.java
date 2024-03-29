package net.minecraft.shader.client;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.optifine.Config;
import net.minecraft.optifine.StrUtils;

public class ShaderOptionVariable extends ShaderOption
{
    public static final Pattern PATTERN_VARIABLE = Pattern.compile("^\\s*#define\\s+(\\w+)\\s+(-?[0-9\\.Ff]+|\\w+)\\s*(//.*)?$");

    public ShaderOptionVariable(String name, String description, String value, String[] values, String path)
    {
        super(name, description, value, values, value, path);
        this.setVisible(this.getValues().length > 1);
    }

    public String getSourceLine()
    {
        return "#define " + this.getName() + " " + this.getValue() + " // Shader option " + this.getValue();
    }

    public String getValueColor(String val)
    {
        return "\u00a7a";
    }

    public boolean matchesLine(String line)
    {
        Matcher m = PATTERN_VARIABLE.matcher(line);

        if (!m.matches())
        {
            return false;
        }
        else
        {
            String defName = m.group(1);
            return defName.matches(this.getName());
        }
    }

    public static ShaderOption parseOption(String line, String path)
    {
        Matcher m = PATTERN_VARIABLE.matcher(line);

        if (!m.matches())
        {
            return null;
        }
        else
        {
            String name = m.group(1);
            String value = m.group(2);
            String description = m.group(3);
            String vals = StrUtils.getSegment(description, "[", "]");

            if (vals != null && vals.length() > 0)
            {
                description = description.replace(vals, "").trim();
            }

            String[] values = parseValues(value, vals);

            if (name != null && name.length() > 0)
            {
                path = StrUtils.removePrefix(path, "/shaders/");
                ShaderOptionVariable so = new ShaderOptionVariable(name, description, value, values, path);
                return so;
            }
            else
            {
                return null;
            }
        }
    }

    public static String[] parseValues(String value, String valuesStr)
    {
        String[] values = new String[] {value};

        if (valuesStr == null)
        {
            return values;
        }
        else
        {
            valuesStr = valuesStr.trim();
            valuesStr = StrUtils.removePrefix(valuesStr, "[");
            valuesStr = StrUtils.removeSuffix(valuesStr, "]");
            valuesStr = valuesStr.trim();

            if (valuesStr.length() <= 0)
            {
                return values;
            }
            else
            {
                String[] parts = Config.tokenize(valuesStr, " ");

                if (parts.length <= 0)
                {
                    return values;
                }
                else
                {
                    if (!Arrays.asList(parts).contains(value))
                    {
                        parts = (String[])((String[])Config.addObjectToArray(parts, value, 0));
                    }

                    return parts;
                }
            }
        }
    }
}
