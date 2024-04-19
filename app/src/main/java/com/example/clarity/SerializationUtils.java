package com.example.clarity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class SerializationUtils {
    /*
    Utility class with static methods for serializing and deserializing objects
     */

    /**
     * Serializes an object into a String
     * @param obj Object to be serialized, needs to be of a class that implements Serializable
     * @return Serialized string
     * @throws IOException
     */
    public static String serializeToString(Serializable obj) throws IOException {
        // Serialize objects (that implement Serializable) into Strings
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        return byteArrayOutputStream.toString("ISO-8859-1"); // Convert byte array to string using ISO-8859-1 encoding
    }

    /**
     * Deserializes a string into its Object
     * @param str String to deserialize
     * @return the deserialized Object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserializeFromString(String str) throws IOException, ClassNotFoundException {
        byte[] byteData = str.getBytes(StandardCharsets.ISO_8859_1); // Convert string to byte array using ISO-8859-1 encoding
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteData);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }
}
