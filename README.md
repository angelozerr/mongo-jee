mongo-jee
=========

mongo-jee is a JEE support for MongoDB :

* JAX-RS support for MongoDB to use Mongo Java classes (DBObject, DBCursor, etc) in the methods of REST service. Here a sample to use com.mongodb.DBObject : 
  	
  	  	@Path("/")
  	  	public class ProductService {

  	  	  	@GET
  	  	  	@Path("/findOne")
  	  	  	@Produces(MediaType.APPLICATION_JSON)
  	  	  	public DBObject findOne(){
      	  	  DB db = MongoProvider.getMongo().getDB("ecommerce");
      	  	  DBCollection col = db.getCollection("products");
        	  	return col.findOne();            
            }
  	  	}

The JAX-RS support provides several JAX-RS Provider (MesageBodyReader/MesageBodyWriter) which serialize/deserialize DBObject, DBCursor, etc to JSON.

* JSON streaming to use Writer (ex : Writer of a Srevlet) or OutputStream (ex:OutputStream of JAX-RS MessageBodyWriter).

* initialize Mongo instance with ServletContextListener.

