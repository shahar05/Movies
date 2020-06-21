package com.company;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomListSerializer extends StdSerializer<List<Movie>> {

    protected CustomListSerializer(Class<List<Movie>> t) {
        super(t);
    }

    protected CustomListSerializer(JavaType type) {
        super(type);
    }

    @Override
    public void serialize(
            List<Movie> movies,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException, JsonProcessingException {

        List<Long> ids = new ArrayList<>();
        for (Movie movie : movies) {
            ids.add(movie.getId());
        }
        generator.writeObject(ids);
    }
}
