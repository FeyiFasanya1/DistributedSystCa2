	package resource;
	
	import entity.User;
	import javax.persistence.EntityManager;
	import javax.persistence.PersistenceContext;
	import javax.ws.rs.*;
	import javax.ws.rs.core.MediaType;
	import javax.ws.rs.core.Response;
	
	@Path("/users")
	public class UserResource {
	
	    @PersistenceContext
	    private EntityManager em;
	
	    // Endpoint for user sign-up
	    @POST
	    @Path("/signup")
	    @Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response signUp(User user) {
	        try {
	            if (user.getUsername() == null || user.getPassword() == null) {
	                return Response.status(Response.Status.BAD_REQUEST)
	                        .entity("Username and password must not be null").build();
	            }
	
	            // Check if username already exists
	            long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
	                    .setParameter("username", user.getUsername())
	                    .getSingleResult();
	
	            if (count > 0) {
	                return Response.status(Response.Status.CONFLICT)
	                        .entity("Username already exists").build();
	            }
	
	            // Save the user
	            em.getTransaction().begin();
	            em.persist(user);
	            em.getTransaction().commit();
	
	            return Response.status(Response.Status.CREATED).entity("User registered successfully").build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("Error registering user: " + e.getMessage()).build();
	        }
	    }
	
	    // Endpoint for user login
	    @POST
	    @Path("/login")
	    @Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response login(User user) {
	        try {
	            User existingUser = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
	                    .setParameter("username", user.getUsername())
	                    .getSingleResult();
	
	            if (existingUser == null || !existingUser.getPassword().equals(user.getPassword())) {
	                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid username or password").build();
	            }
	
	            return Response.ok("Login successful").build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("Error during login: " + e.getMessage()).build();
	        }
	    }
	}
