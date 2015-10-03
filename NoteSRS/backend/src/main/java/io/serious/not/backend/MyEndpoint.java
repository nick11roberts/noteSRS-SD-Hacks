/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package io.serious.not.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import javax.inject.Named;

import static io.serious.not.backend.OfyService.ofy;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.not.serious.io",
                ownerName = "backend.not.serious.io",
                packagePath = ""
        )
)
public class MyEndpoint {

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "correctText")
    public StringWrapper correctText(@Named("input") String input) {
        StringWrapper response = new StringWrapper();
        response.setData("Hi, " + input);

        //This is where we use that lib thing that Nideesh is making

        return response;
    }

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "insertAutoCucumberListItem")
    public StringWrapper insertAutoCucumberListItem(@Named("word") String word, @Named("correction") String correction) {
        StringWrapper success = new StringWrapper();
        // Get the Datastore Service
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        /* check if original word exists in datastore
         * if it does not exist,
         *      then create an OriginalWord node containing the original word
         * create a new ReplacementWord node such that its parent is the original word
         */
        // Use class Query to assemble a query
        OriginalWord newWord = new OriginalWord(word);
        ReplacementWord newReplacement = new ReplacementWord(correction);
        Query.Filter exactWordFilter = new Query.FilterPredicate("word", Query.FilterOperator.EQUAL, word);
        Query q = new Query("OriginalWord").setFilter(exactWordFilter);
        PreparedQuery pq = datastore.prepare(q);
        Entity result = pq.asSingleEntity();
        if(result == null){
            ofy().save().entity(newWord).now();
        }

        newReplacement.setOriginalWordRef(newWord);
        ofy().save().entity(newReplacement).now();

        success.setData(word);
        return success;
    }

}


