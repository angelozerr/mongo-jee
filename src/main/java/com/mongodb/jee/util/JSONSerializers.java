package com.mongodb.jee.util;

import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DBObject;
import com.mongodb.DBRefBase;
import com.mongodb.util.Base64Codec;

import org.bson.types.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * Defines static methods for getting <code>ObjectSerializer</code> instances that produce various flavors of
 * JSON.
 */
public class JSONSerializers {

    private JSONSerializers() {
    }

    /**
     * Returns an <code>ObjectSerializer</code> that mostly conforms to the strict JSON format defined in
     * <a href="http://www.mongodb.org/display/DOCS/Mongo+Extended+JSON", but with a few differences to keep
     * compatibility with previous versions of the driver.  Clients should generally prefer
     * <code>getStrict</code> in preference to this method.
     *
     * @return object serializer
     * @see #getStrict()
     */
    public static ObjectSerializer getLegacy() {

        ClassMapBasedObjectSerializer serializer = addCommonSerializers();

        serializer.addObjectSerializer(Date.class, new LegacyDateSerializer(serializer));
        serializer.addObjectSerializer(BSONTimestamp.class, new LegacyBSONTimestampSerializer(serializer));
        serializer.addObjectSerializer(Binary.class, new LegacyBinarySerializer());
        serializer.addObjectSerializer(byte[].class, new LegacyBinarySerializer());
        return serializer;
    }

    /**
     * Returns an <code>ObjectSerializer</code> that conforms to the strict JSON format defined in
     * <a href="http://www.mongodb.org/display/DOCS/Mongo+Extended+JSON".
     *
     * @return object serializer
     */
    public static ObjectSerializer getStrict() {

        ClassMapBasedObjectSerializer serializer = addCommonSerializers();

        serializer.addObjectSerializer(Date.class, new DateSerializer(serializer));
        serializer.addObjectSerializer(BSONTimestamp.class, new BSONTimestampSerializer(serializer));
        serializer.addObjectSerializer(Binary.class, new BinarySerializer(serializer));
        serializer.addObjectSerializer(byte[].class, new ByteArraySerializer(serializer));

        return serializer;
    }

    static ClassMapBasedObjectSerializer addCommonSerializers() {
        ClassMapBasedObjectSerializer serializer = new ClassMapBasedObjectSerializer();

        serializer.addObjectSerializer(Object[].class, new ObjectArraySerializer(serializer));
        serializer.addObjectSerializer(Boolean.class, new ToStringSerializer());
        serializer.addObjectSerializer(Code.class, new CodeSerializer(serializer));
        serializer.addObjectSerializer(CodeWScope.class, new CodeWScopeSerializer(serializer));
        serializer.addObjectSerializer(DBObject.class, new DBObjectSerializer(serializer));
        serializer.addObjectSerializer(DBRefBase.class, new DBRefBaseSerializer(serializer));
        serializer.addObjectSerializer(Iterable.class, new IterableSerializer(serializer));
        serializer.addObjectSerializer(Map.class, new MapSerializer(serializer));
        serializer.addObjectSerializer(MaxKey.class, new MaxKeySerializer(serializer));
        serializer.addObjectSerializer(MinKey.class, new MinKeySerializer(serializer));
        serializer.addObjectSerializer(Number.class, new ToStringSerializer());
        serializer.addObjectSerializer(ObjectId.class, new ObjectIdSerializer(serializer));
        serializer.addObjectSerializer(Pattern.class, new PatternSerializer(serializer));
        serializer.addObjectSerializer(String.class, new StringSerializer());
        serializer.addObjectSerializer(UUID.class, new UUIDSerializer(serializer));
        return serializer;
    }

    private abstract static class CompoundObjectSerializer extends AbstractObjectSerializer {
        protected final ObjectSerializer serializer;

        CompoundObjectSerializer(ObjectSerializer serializer) {
            this.serializer = serializer;
        }
    }

    private static class LegacyBinarySerializer extends AbstractObjectSerializer {

    	@Override
    	protected void serialize(Object obj, Writer writer, OutputStream out)
    			throws IOException {
    		JSON.append("<Binary Data>", writer, out);
    		
    	}
    }

    private static class ObjectArraySerializer extends CompoundObjectSerializer {

        ObjectArraySerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {
        	JSON.append("[ ", writer, out);
            for (int i = 0; i < Array.getLength(obj); i++) {
                if (i > 0)
                	JSON.append(" , ", writer, out);
                JSON.serialize(serializer, Array.get(obj, i), writer, out);               
            }
            JSON.append("]", writer, out);
        }        
    }

    private static class ToStringSerializer extends AbstractObjectSerializer {

    	@Override
    	protected void serialize(Object obj, Writer writer, OutputStream out)
    			throws IOException {
    		JSON.append(obj.toString(), writer, out);    		
    	}        
    }

    private static class LegacyBSONTimestampSerializer extends CompoundObjectSerializer {

        LegacyBSONTimestampSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {
        	BSONTimestamp t = (BSONTimestamp) obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$ts", Integer.valueOf(t.getTime()));
            temp.put("$inc", Integer.valueOf(t.getInc()));
            
            JSON.serialize(serializer, temp, writer, out);      
        }        
    }

    private static class CodeSerializer extends CompoundObjectSerializer {

        CodeSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {
        	   Code c = (Code) obj;
               BasicDBObject temp = new BasicDBObject();
               temp.put("$code", c.getCode());
               
               JSON.serialize(serializer, temp, writer, out);               
        }
    }

    private static class CodeWScopeSerializer extends CompoundObjectSerializer {

        CodeWScopeSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {
        	   CodeWScope c = (CodeWScope) obj;
               BasicDBObject temp = new BasicDBObject();
               temp.put("$code", c.getCode());
               temp.put("$scope", c.getScope());
               
               JSON.serialize(serializer, temp, writer, out);
        	
        }
    }

    private static class LegacyDateSerializer extends CompoundObjectSerializer {

        LegacyDateSerializer(ClassMapBasedObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {        
            Date d = (Date) obj;
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            format.setCalendar(new GregorianCalendar(
                    new SimpleTimeZone(0, "GMT")));
            JSON.serialize(serializer,
                    new BasicDBObject("$date", format.format(d)), writer, out);
        }

    }

    private static class DBObjectSerializer extends CompoundObjectSerializer {

        DBObjectSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {        
            boolean first = true;
            JSON.append("{ ", writer, out);
            DBObject dbo = (DBObject) obj;
            String name;

            for (final String s : dbo.keySet()) {
                name = s;

                if (first)
                    first = false;
                else
                    JSON.append(" , ", writer, out);

                JSON.string(name, writer, out);
                JSON.append(" : ", writer, out);
                JSON.serialize(serializer, dbo.get(name), writer, out);
            }

            JSON.append("}", writer, out);
        }

    }

    private static class DBRefBaseSerializer extends CompoundObjectSerializer {

        DBRefBaseSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {        
            DBRefBase ref = (DBRefBase) obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$ref", ref.getRef());
            temp.put("$id", ref.getId());
            JSON.serialize(serializer, temp, writer, out);
        }

    }

    private static class IterableSerializer extends CompoundObjectSerializer {

        IterableSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {        
            boolean first = true;
            JSON.append("[ ", writer, out);

            for (final Object o : ((Iterable) obj)) {
                if (first)
                    first = false;
                else
                    JSON.append(" , ", writer, out);

                JSON.serialize(serializer,o, writer, out);
            }
            JSON.append("]", writer, out);
        }
    }

    private static class MapSerializer extends CompoundObjectSerializer {

        MapSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {        
            boolean first = true;
            JSON.append("{ ", writer, out);
            Map m = (Map) obj;
            Entry entry;

            for (final Object o : m.entrySet()) {
                entry = (Entry) o;
                if (first)
                    first = false;
                else
                    JSON.append(" , ", writer, out);
                JSON.string(entry.getKey().toString(), writer, out);
                JSON.append(" : ", writer, out);
                JSON.serialize(serializer, entry.getValue(), writer, out);
            }

            JSON.append("}", writer, out);
        }

    }

    private static class MaxKeySerializer extends CompoundObjectSerializer {

        MaxKeySerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {        
            JSON.serialize(serializer, new BasicDBObject("$maxKey",
                    1), writer, out);
        }

    }

    private static class MinKeySerializer extends CompoundObjectSerializer {

        MinKeySerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {        
            JSON.serialize(serializer, new BasicDBObject("$minKey",
                    1), writer, out);
        }

    }

    private static class ObjectIdSerializer extends CompoundObjectSerializer {

        ObjectIdSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {        
            JSON.serialize(serializer,
                    new BasicDBObject("$oid", obj.toString()), writer, out);
        }
    }

    private static class PatternSerializer extends CompoundObjectSerializer {

        PatternSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {        
            DBObject externalForm = new BasicDBObject();
            externalForm.put("$regex", obj.toString());
            if (((Pattern) obj).flags() != 0)
                externalForm.put("$options",
                        Bytes.regexFlags(((Pattern) obj).flags()));
            JSON.serialize(serializer, externalForm, writer, out);
        }
    }

    private static class StringSerializer extends AbstractObjectSerializer {

    	@Override
    	protected void serialize(Object obj, Writer writer, OutputStream out)
    			throws IOException {
            JSON.string((String) obj, writer, out);
        }
    }

    private static class UUIDSerializer extends CompoundObjectSerializer {

        UUIDSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {        
            UUID uuid = (UUID) obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$uuid", uuid.toString());
            JSON.serialize(serializer,temp, writer, out);
        }
    }

    private static class BSONTimestampSerializer extends CompoundObjectSerializer {

        BSONTimestampSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {
            BSONTimestamp t = (BSONTimestamp) obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$t", Integer.valueOf(t.getTime()));
            temp.put("$i", Integer.valueOf(t.getInc()));
            BasicDBObject timestampObj = new BasicDBObject();
            timestampObj.put("$timestamp", temp);
            JSON.serialize(serializer, timestampObj, writer, out);
        }

    }

    private static class DateSerializer extends CompoundObjectSerializer {

        DateSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {
        	 Date d = (Date) obj;
        	JSON.serialize(serializer,
                    new BasicDBObject("$date", d.getTime()), writer, out);
        }
    }

    private abstract static class BinarySerializerBase extends CompoundObjectSerializer {
        BinarySerializerBase(ObjectSerializer serializer) {
            super(serializer);
        }

        protected void serialize(byte[] bytes, byte type, Writer writer, OutputStream out) throws IOException {
            DBObject temp = new BasicDBObject();
            temp.put("$binary",
                    (new Base64Codec()).encode(bytes));
            temp.put("$type", type);
            JSON.serialize(serializer, temp, writer, out);
        }
    }

    private static class BinarySerializer extends BinarySerializerBase {
        BinarySerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {
        	Binary bin = (Binary) obj;
            serialize(bin.getData(), bin.getType(), writer, out);
        }
    }

    private static class ByteArraySerializer extends BinarySerializerBase {
        ByteArraySerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        protected void serialize(Object obj, Writer writer, OutputStream out)
        		throws IOException {
        	serialize((byte[]) obj, (byte) 0,  writer, out);
        }
    }
}

