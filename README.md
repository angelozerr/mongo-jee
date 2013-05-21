# What is Mongo JEE?

[Mongo Java Driver] (https://github.com/mongodb/mongo-java-driver) gives you the capability to connect and use NoSQL MongoDB with Java code.

**Mongo JEE** is a JAR which provides simply but usefull Java classes to use this driver in JEE context like using JAX-RS with [com.mongodb.DBObject](https://github.com/mongodb/mongo-java-driver/blob/master/src/main/com/mongodb/DBObject.java),
connect/disconnect to Mongo with ServletContextListener, etc

# Features

Here the list of the features provided by Mongo JEE:

## JAX-RS support for MongoDB

[JAX-RS support for MongoDB] (https://github.com/angelozerr/mongo-jee/wiki/JAX-RS-support-for-MongoDB) to use Mongo Java structures (DBObject, DBCursor, etc) in the methods of your REST service. Here a sample service which returns the com.mongodb.DBObject : 
  	
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

The JAX-RS support provides [several JAX-RS Provider] (https://github.com/angelozerr/mongo-jee/tree/master/mongo-jee/src/main/java/com/mongodb/jee/jaxrs/providers) which serialize/deserialize DBObject, DBCursor, etc to JSON stream.

##JSON streaming

[JSON streaming](https://github.com/angelozerr/mongo-jee/wiki/JSON-Streaming) : Mongo Java  Driver provides com.mongodb.util.[JSON](https://github.com/mongodb/mongo-java-driver/blob/master/src/main/com/mongodb/util/JSON.java) helper to serialize
DBObject, DBCursor etc to JSON stream but it works only with StringBuilder. Here a sample to write DBObject in HTTP response Write 

        HttpServletResponse response = ...
        DBObject o = ...
        // Serialize the JSON DBOBject in a String
        StringBuilder json = new StringBuilder()
        com.mongodb.util.JSON.serialize(o, json);
        // Write the JSON string
        response.getWriter().write(json.toString());

Mongo JEE provides com.mongodb.jee.util.[JSON](https://github.com/angelozerr/mongo-jee/blob/master/mongo-jee/src/main/java/com/mongodb/jee/util/JSON.java)
which works with Writer/OutputStream:

        HttpServletResponse response = ...
        DBObject o = ...
        // Serialize the JSON DBOBject in the HTTP response Writer
        com.mongodb.jee.util.JSON.serialize(o, response.getWriter());
      
This idea was suggested to Mongo Java Driver in the [JAVA-709 issue](https://jira.mongodb.org/browse/JAVA-709)

## MongoHolder

Mongo Java Driver provides com.mongodb.[Mongo.Holder](https://github.com/mongodb/mongo-java-driver/blob/master/src/main/com/mongodb//Mongo.java) helper to hold several 
static Mongo instances for several Mongo uri. However, this holder doesn't manage deconnection, default Mongo instance.

Mongo JEE provides the com.mongodb.jee.[MongoHolder](https://github.com/angelozerr/mongo-jee/blob/master/mongo-jee/src/main/java/com/mongodb/jee/MongoHolder.java) which provides 
several features like deconnection, default Mongo instance. 

Please read [Using MogoHolder](https://github.com/angelozerr/mongo-jee/wiki/Using-MongoHolder) section for more information.

## Initialize Mongo with ServletContextListener

[Initialize Mongo with ServletContextListener] (https://github.com/angelozerr/mongo-jee/wiki/Initialize-Mongo-with-ServletContextListener):  

      	<listener>		
      	  <listener-class>com.mongodb.jee.servlet.MongoServletContextListener
       	  </listener-class>
      	</listener>

      	<context-param>
      	  <param-name>mongoURI</param-name>
      	  <param-value>mongodb://localhost:12345</param-value>
      	</context-param>
        
When MongoServletContextListener is started it initializes default Mongo connection with com.mongodb.jee.[MongoHolder](https://github.com/angelozerr/mongo-jee/blob/master/mongo-jee/src/main/java/com/mongodb/jee/MongoHolder.java).
After that, you can retrieve this Mongo .connection anywhere in your code like this:

      	Mongo mongo = MongoHolder.connect();
        
## Pagination

[Pagination](https://github.com/angelozerr/mongo-jee/wiki/Pagination) to help pagination with MongoDB and client (like Dojo) to consumes the paginated list.

# Download

You can download [mongo-jee-1.0.0.jar](http://search.maven.org/remotecontent?filepath=fr/opensagres/mongodb/mongo-jee/1.0.0/mongo-jee-1.0.0.jar) or use maven : 

        <dependency>
            <groupId>fr.opensagres.mongodb</groupId>
            <artifactId>mongo-jee</artifactId>
            <version>1.0.0</version>
        </dependency>

# License

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)

# Article

You can read following [articles](http://angelozerr.wordpress.com/about/mongo_jee/) about Mongo JEE.

# Demo

Mongo JEE provides the [Mongo JEE Demo with Apache CXF and Dojo] (https://github.com/angelozerr/mongo-jee/wiki/Mongo-JEE-Demo-with-Apache-CXF-and-Dojo)
which uses the features of Mongo JEE. This demo displays a paginated list of products. This WebApps deploys a JAX-RS REST service ProductsService which returns the paginated JSON products:

![WebApps Overview](https://github.com/angelozerr/mongo-jee/wiki/images/WebAppsMongoDB_Overview.png)


