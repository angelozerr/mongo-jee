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
DBObject, DBCursor etc to JSON stream but it works only with StringBuilder. On other words if you wish to write JSON to HTTP response writer, JAX-RS response OutputStream, 
you must build a JSON String before and write this String to the writer/OutputStream.

Mongo JEE provides com.mongodb.jee.util.[JSON](https://github.com/mongodb/mongo-java-driver/blob/master/src/main/com/mongodb/util/JSON.java)
which works with Writer/OutputStream, on other words it provides :

 * JSON#serialize(Object o, Writer writer)
 * JSON#serialize(Object o, OutputStream out)

      	HttpServletResponse response
        

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


