package com.synezia.client.utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.synezia.client.Client;

import net.minecraft.world.World;

public class ColorsAdapter extends TypeAdapter<Colors> {

	@Override
	public Colors read(JsonReader reader) throws IOException {
		if(reader.peek() == JsonToken.NULL) {
			reader.nextNull();
			return null;
		}
		
		Map<String, Object> keys = Client.i.getGson().fromJson(reader.nextString(), new TypeToken<Map<String, Object>>(){}.getType());
		Integer lightColor = Integer.parseInt((String)keys.get("light"));
		Integer darkColor  = Integer.parseInt((String)keys.get("dark"));
		return new Colors(lightColor, darkColor);
	}

	@Override
	public void write(JsonWriter writer, Colors colors) throws IOException {
		Map<String, Object> serial = new HashMap<String, Object>();
		serial.put("light", Integer.toString(colors.getLightColor()));
		serial.put("dark", Integer.toString(colors.getDarkColor()));
		
		writer.value(Client.i.getGson().toJson(serial));
	}

}
