package com.avgjoe;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Path("/identities")
public class Identity {

	@POST
	@Consumes("application/json")
	public Response createIdentity (String data) throws Exception{
		
		JSONObject json = new JSONObject(data) ;
		JSONObject identity = (JSONObject)json.get("identity") ;
		String name = identity.getString("name").toLowerCase() ;
		int age = identity.getInt( "age" ) ;
		String sex = identity.getString( "sex") ;
		JSONObject location = identity.getJSONObject( "location" ) ;
		String channel = identity.getString( "channel" ) ;
		String city = location.getString("city") ;
		String state = location.getString( "state" ) ;
		String country = location.getString( "country" ) ;
		
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService() ;
		//Insert Country
		Key countryKey = KeyFactory.createKey( "Country", country.hashCode() ) ;
		Entity countryEntity = new Entity( countryKey ) ;
		countryEntity.setProperty("country" , country ) ;
		ds.put(countryEntity) ;
		
		//Insert State
		Key stateKey = KeyFactory.createKey( countryKey,"State", state.hashCode() ) ;
		Entity stateEntity = new Entity( stateKey) ;
		stateEntity.setProperty("state" , state ) ;
		ds.put(stateEntity) ;
		
		//Insert City
		Key cityKey = KeyFactory.createKey( stateKey,"City", city.hashCode() ) ;
		Entity cityEntity = new Entity( cityKey) ;
		cityEntity.setProperty("city" , city ) ;
		ds.put(cityEntity) ;
		
		//Insert Entity
		Entity identityEntity = new Entity( "Identity" ) ;
		identityEntity.setProperty( "name", name ) ;
		identityEntity.setProperty( "age", age ) ;
		identityEntity.setProperty( "sex", sex ) ;
		identityEntity.setProperty( "channel", channel ) ;
		identityEntity.setProperty( "cityKey", cityKey) ;
		Key idenityKey = ds.put(identityEntity ) ;
		
		Response res = Response.ok(idenityKey.toString()).build() ;  
		return res ;
	}
	
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response getIdentity (@PathParam("id") String id ) throws Exception{
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService() ;
		Key key = KeyFactory.createKey("Identity", Long.parseLong(id)) ;
		Entity identityEntity = null ;
		try {
			identityEntity = ds.get( key ) ;
			JSONObject identity = new JSONObject() ;
			identity.put( "name", identityEntity.getProperty("name") ) ;
			identity.put( "age", identityEntity.getProperty("age") ) ;
			identity.put( "sex", identityEntity.getProperty("sex") ) ;
			identity.put( "channel", identityEntity.getProperty("channel") ) ;
			
			Key cityKey =  (Key) identityEntity.getProperty( "cityKey" )  ;
			Entity cityEntity = ds.get(cityKey) ;
			Key stateKey = cityEntity.getParent() ;
			Entity stateEntity = ds.get( stateKey ) ;
			Key countryKey = stateEntity.getParent() ;
			Entity countryEntity = ds.get(countryKey) ;
			
			JSONObject location = new JSONObject() ;
			location.put( "city", cityEntity.getProperty("city") ) ;
			location.put( "state", stateEntity.getProperty("state") ) ;
			location.put( "country", countryEntity.getProperty("country") ) ;
			
			identity.put( "location", location ) ;
				
			Response res = Response.ok( identity.toString() ).build() ;
			return res ;
			
		} catch (EntityNotFoundException e) {

			e.printStackTrace();
		}
		return null;
	}
}
 