What is Mongo JEE?
=========

[Mongo Java Driver] (https://github.com/mongodb/mongo-java-driver) gives you the capability to connect and use NoSQL MongoDB with Java code.

**Mongo JEE** is a JAR which provides simply but usefull Java classes to use this driver in JEE context like using JAX-RS with [com.mongodb.DBObject](https://github.com/mongodb/mongo-java-driver/blob/master/src/main/com/mongodb/DBObject.java),
connect/disconnect to Mongo with ServletContextListener, etc

Here the list of the features provided by Mongo JEE:

* [JAX-RS support for MongoDB] (https://github.com/angelozerr/mongo-jee/wiki/JAX-RS-support-for-MongoDB) to use Mongo Java structures (DBObject, DBCursor, etc) in the methods of your REST service. Here a sample service which returns the com.mongodb.DBObject : 
  	
  	  	@Path("/")
  	  	public class ProductService {

  	  	  	@GET
  	  	  	@Path("/findOne")
  	  	  	@Produces(MediaType.APPLICATION_JSON)
  	  	  	public DBObject findOne(){
      	  	  DB db = mongo.getDB("ecommerce");
      	  	  DBCollection col = db.getCollection("products");
        	  	return col.findOne();            
            }
  	  	}

The JAX-RS support provides several JAX-RS Provider (MesageBodyReader/MesageBodyWriter) which serialize/deserialize DBObject, DBCursor, etc to JSON stream.

* JSON streaming : Mongo Java  Driver provides com.mongodb.util.[JSON](https://github.com/mongodb/mongo-java-driver/blob/master/src/main/com/mongodb/util/JSON.java) helper to serialize
DBObject, DBCursor etc to JSON stream but it works only with StringBuilder. Here a sample to write DBObject in HTTP response Write 

        HttpServletResponse response = ...
        DBObject o = ...
        // Serialize the JSON DBOBject in a String
        StringBuilder json = new StringBuilder()
        com.mongodb.util.JSON.serialize(o, json);
        // Write the JSON string
        response.getWriter().write(json.toString());

Mongo JEE provides com.mongodb.jee.util.[JSON](https://github.com/angelozerr/mongo-jee/blob/master/src/main/java/com/mongodb/jee/util/JSON.java)
which works with Writer/OutputStream, 

        HttpServletResponse response = ...
        DBObject o = ...
        // Serialize the JSON DBOBject in the HTTP response Writer
        com.mongodb.jee.util.JSON.serialize(o, response.getWriter());

on other words it provides 2 methods :

 * JSON#serialize(Object o, Writer writer)
 * JSON#serialize(Object o, OutputStream out)
      
This idea was suggested to Mongo Java Driver in the [JAVA-709 issue](https://jira.mongodb.org/browse/JAVA-709)

* initialize Mongo instance with ServletContextListener.  


      	<listener>		
      	  <listener-class>com.mongodb.jee.servlet.MongoServletContextListener
       	  </listener-class>
      	</listener>

      	<context-param>
      	  <param-name>mongoURI</param-name>
      	  <param-value>mongodb://localhost:12345</param-value>
      	</context-param>
        
After that you can use MongoProvider.getMongo() anywhere

Download
=========

Only source are available.


