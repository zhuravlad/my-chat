package com.mindlinksoft.recruitment.mychat;

import com.google.gson.*;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.Instant;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link ConversationExporter}.
 */
public class ConversationExporterTests {
    /**
     * Tests that exporting a conversation will export the conversation correctly.
     * @throws Exception When something bad happens.
     */
    @Test
    public void testExportingConversationExportsConversation() throws Exception {
        ConversationExporter exporter = new ConversationExporter();
        
        //Store all features in a string array and iterate through it, testing it one by one
        String feature[] = new String[] {"read", "username", "keyword", "keyword_redact", "numbers", "obfuscate"};
        for(int i = 0; i < feature.length; ++i) {
            exporter.exportConversation("chat.txt", "chat.json", feature[i]);
        }

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Instant.class, new InstantDeserializer());

        Gson g = builder.create();

        Conversation c = g.fromJson(new InputStreamReader(new FileInputStream("chat.json")), Conversation.class);

        assertEquals("My Conversation", c.name);

        assertEquals(7, c.messages.size());

        Message[] ms = new Message[c.messages.size()];
        c.messages.toArray(ms);

        assertEquals(ms[0].timestamp, Instant.ofEpochSecond(1448470901));
        assertEquals(ms[0].senderId, "bob");
        assertEquals(ms[0].content, "Hello there!");

        assertEquals(ms[1].timestamp, Instant.ofEpochSecond(1448470905));
        assertEquals(ms[1].senderId, "mike");
        assertEquals(ms[1].content, "how are you?");

        assertEquals(ms[2].timestamp, Instant.ofEpochSecond(1448470906));
        assertEquals(ms[2].senderId, "bob");
        assertEquals(ms[2].content, "I'm good thanks, do you like pie?");

        assertEquals(ms[3].timestamp, Instant.ofEpochSecond(1448470910));
        assertEquals(ms[3].senderId, "mike");
        assertEquals(ms[3].content, "no, let me ask Angus...");

        assertEquals(ms[4].timestamp, Instant.ofEpochSecond(1448470912));
        assertEquals(ms[4].senderId, "angus");
        assertEquals(ms[4].content, "Hell yes! Are we buying some pie?");

        assertEquals(ms[5].timestamp, Instant.ofEpochSecond(1448470914));
        assertEquals(ms[5].senderId, "bob");
        assertEquals(ms[5].content, "No, just want to know if there's anybody else in the pie society...");

        assertEquals(ms[6].timestamp, Instant.ofEpochSecond(1448470915));
        assertEquals(ms[6].senderId, "angus");
        assertEquals(ms[6].content, "YES! I'm the head pie eater there...");
        
        assertEquals(ms[7].timestamp, Instant.ofEpochSecond(1448470917));
        assertEquals(ms[7].senderId, "bob");
        assertEquals(ms[7].content, "What is your phone number?");
        
        assertEquals(ms[8].timestamp, Instant.ofEpochSecond(1448470919));
        assertEquals(ms[8].senderId, "angus");
        assertEquals(ms[8].content, "07532145940");
        
        assertEquals(ms[9].timestamp, Instant.ofEpochSecond(1448470920));
        assertEquals(ms[9].senderId, "bob");
        assertEquals(ms[9].content, "And your card number?");
        
        assertEquals(ms[10].timestamp, Instant.ofEpochSecond(1448470921));
        assertEquals(ms[10].senderId, "mike");
        assertEquals(ms[10].content, "This is strange...");
        
        assertEquals(ms[11].timestamp, Instant.ofEpochSecond(1448470923));
        assertEquals(ms[11].senderId, "angus");
        assertEquals(ms[11].content, "4731200898761234");
        
        assertEquals(ms[12].timestamp, Instant.ofEpochSecond(1448470927));
        assertEquals(ms[12].senderId, "bob");
        assertEquals(ms[12].content, "By the way, this is scam");
                
    }

    class InstantDeserializer implements JsonDeserializer<Instant> {

        @Override
        public Instant deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!jsonElement.isJsonPrimitive()) {
                throw new JsonParseException("Expected instant represented as JSON number, but no primitive found.");
            }

            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();

            if (!jsonPrimitive.isNumber()) {
                throw new JsonParseException("Expected instant represented as JSON number, but different primitive found.");
            }

            return Instant.ofEpochSecond(jsonPrimitive.getAsLong());
        }
    }
}
