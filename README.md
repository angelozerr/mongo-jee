What is Mongo JEE?
=========

[Mongo Java Driver] (https://github.com/mongodb/mongo-java-driver) gives you the capability to connect and use NoSQL MongoDB with Java code.

**Mongo JEE** is a JAR which provides simply but usefull Java classes to use this driver in JEE context like using JAX-RS with [com.mongodb.DBObject](https://github.com/mongodb/mongo-java-driver/blob/master/src/main/com/mongodb/DBObject.java),
connect/disconnect to Mongo with ServletContextListener, etc

Here the detailled features of the Mongo JEE:

* JAX-RS support for MongoDB to use Mongo Java classes (DBObject, DBCursor, etc) in the methods of REST service. Here a sample to use com.mongodb.DBObject : 
  	
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

The JAX-RS support provides several JAX-RS Provider (MesageBodyReader/MesageBodyWriter) which serialize/deserialize DBObject, DBCursor, etc to JSON.

* JSON streaming to use Writer (ex : Writer of a Srevlet) or OutputStream (ex:OutputStream of JAX-RS MessageBodyWriter).

 * JSON#serialize(Object o, Writer writer)
 * JSON#serialize(Object o, OutputStream out)

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


