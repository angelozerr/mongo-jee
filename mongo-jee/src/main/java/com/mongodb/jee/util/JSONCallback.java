// JSONCallback.java

/**
 *      Copyright (C) 2008 10gen Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.mongodb.jee.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bson.BSON;
import org.bson.BSONObject;
import org.bson.BasicBSONCallback;
import org.bson.types.BSONTimestamp;
import org.bson.types.Code;
import org.bson.types.CodeWScope;
import org.bson.types.MaxKey;
import org.bson.types.MinKey;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;

public class JSONCallback extends BasicBSONCallback {
    
    @Override
    public BSONObject create(){
        return new BasicDBObject();
    }
    
    @Override
    protected BSONObject createList() {
        return new BasicDBList();
    }
    
    public void objectStart(boolean array, String name){
        _lastArray = array;
        super.objectStart( array , name );
    }

    public Object objectDone(){
        String name = curName();
        Object o = super.objectDone();
	BSONObject b = (BSONObject)o;

        // override the object if it's a special type
        if (!_lastArray) {
            if (b.containsField("$oid")) {
                o = new ObjectId((String) b.get("$oid"));
                if (!isStackEmpty()) {
                    gotObjectId(name, (ObjectId) o);
                } else {
                    setRoot(o);
                }
            } else if (b.containsField("$date")) {

                if(b.get("$date") instanceof Number){
                    o = new Date(((Number)b.get("$date")).longValue());
                }else {
                    SimpleDateFormat format = new SimpleDateFormat(_msDateFormat);
                    format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
                    o = format.parse(b.get("$date").toString(), new ParsePosition(0));

                    if (o == null) {
                        // try older format with no ms
                        format = new SimpleDateFormat(_secDateFormat);
                        format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
                        o = format.parse(b.get("$date").toString(), new ParsePosition(0));
                    }
                }
                if (!isStackEmpty()) {
                    cur().put(name, o);
                } else {
                    setRoot(o);
                }
            } else if ( b.containsField( "$regex" ) ) {
		o = Pattern.compile( (String)b.get( "$regex" ), 
				     BSON.regexFlags( (String)b.get( "$options" )) );
		if (!isStackEmpty()) {
		    cur().put( name, o );
		} else {
		    setRoot(o);
		}
	    } else if ( b.containsField( "$ts" ) ) {
                Long ts = ((Number)b.get("$ts")).longValue();
                Long inc = ((Number)b.get("$inc")).longValue();
		o = new BSONTimestamp(ts.intValue(), inc.intValue());
		if (!isStackEmpty()) {
		    cur().put( name, o );
		} else {
		    setRoot(o);
		}
	    } else if ( b.containsField( "$code" ) ) {
                if (b.containsField("$scope")) {
                    o = new CodeWScope((String)b.get("$code"), (DBObject)b.get("$scope"));
                } else {
                    o = new Code((String)b.get("$code"));
                }
		if (!isStackEmpty()) {
		    cur().put( name, o );
		} else {
		    setRoot(o);
		}
	    } else if ( b.containsField( "$ref" ) ) {
                o = new DBRef(null, (String)b.get("$ref"), b.get("$id"));
		if (!isStackEmpty()) {
		    cur().put( name, o );
		} else {
		    setRoot(o);
		}
	    } else if ( b.containsField( "$minKey" ) ) {
                o = new MinKey();
		if (!isStackEmpty()) {
		    cur().put( name, o );
		} else {
		    setRoot(o);
		}
	    } else if ( b.containsField( "$maxKey" ) ) {
                o = new MaxKey();
		if (!isStackEmpty()) {
		    cur().put( name, o );
		} else {
		    setRoot(o);
		}
	    } else if ( b.containsField( "$uuid" ) ) {
                o = UUID.fromString((String)b.get("$uuid"));
		if (!isStackEmpty()) {
		    cur().put( name, o );
		} else {
		    setRoot(o);
		}
	    }
	}
        return o;
    }
    
    private boolean _lastArray = false;
    
    public static final String _msDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String _secDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
}
