def getOrCreateUserVertex(g, userId) { 
    def userVertex;

    userTraversal = g.V().has('user', 'userId', userId);
    if (userTraversal.hasNext()) {
        userVertex = userTraversal.next();
    } else {
        userVertex = g.addV('user').property('userId', userId).next();
    }

    return userVertex;
}

// Create an empty graph
graph = TinkerGraph.open();
// Create an index for the "userId" property
graph.createIndex('userId', Vertex.class);
// Create the graph traversal source
g = graph.traversal();

lines = new File('/home/pawanjindal/Documents/File/data.txt').readLines();
for (line in lines) {
    if (!line.startsWith("#")) {
        lineParts = line.split('\t');
        user1Id = lineParts[0];
        user2Id = lineParts[1];

        user1Vertex = getOrCreateUserVertex(g, user1Id);
        user2Vertex = getOrCreateUserVertex(g, user2Id);
        
        // Add a "votesFor" edge going from user1 to user2.
        g.addE('votesFor').from(user1Vertex).to(user2Vertex).iterate();
    }
}

// Print the number of vertices and the number of edges in the database
numberOfVertices = g.V().count().next();
numberOfEdges = g.E().count().next();
println 'Database contains ' + numberOfVertices + ' vertices and ' + numberOfEdges + ' edges.';
















/*
package com.amazonaws;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.structure.T;

public class App
{
    public static void main( String[] args )
    {
        Cluster.Builder builder = Cluster.build();
        builder.addContactPoint("your-neptune-endpoint");
        builder.port(8182);
        builder.enableSsl(true);
        builder.keyCertChainFile("SFSRootCAG2.pem");

        Cluster cluster = builder.create();

        GraphTraversalSource g = traversal().withRemote(DriverRemoteConnection.using(cluster));

        // Add a vertex.
        // Note that a Gremlin terminal step, e.g. next(), is required to make a request to the remote server.
        // The full list of Gremlin terminal steps is at https://tinkerpop.apache.org/docs/current/reference/#terminal-steps
        g.addV("Person").property("Name", "Justin").next();

        // Add a vertex with a user-supplied ID.
        g.addV("Custom Label").property(T.id, "CustomId1").property("name", "Custom id vertex 1").next();
        g.addV("Custom Label").property(T.id, "CustomId2").property("name", "Custom id vertex 2").next();

        g.addE("Edge Label").from(g.V("CustomId1")).to(g.V("CustomId2")).next();

        // This gets the vertices, only.
        GraphTraversal t = g.V().limit(3).valueMap();

        t.forEachRemaining(
            e ->  System.out.println(e)
        );

        cluster.close();
    }
}
*/