package elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.json.JSONObject;

import pipeline.Utils;

/**
 * A collection of convenient methods for call an elastic search node
 * 
 * @author florent delmotte
 *
 */
public class ElasticsSearchHelper {
    // TODO exporter la configuration dans le repertoire de configuration
    private static String host = "172.17.0.2";

    public static void index(String index, String type, String id,
            JSONObject soapDatainJsonObject) throws Exception {
        Utils.log("ElasticSearchHelper index ");

        Client client = TransportClient.builder().build().addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName(host),
                        9300));
        Utils.log("ElasticSearchHelper index send ");

        IndexResponse response = client.prepareIndex(index, type, id)
                .setSource(soapDatainJsonObject.toString().getBytes()).get();
        client.close();
        boolean created = response.isCreated();
        Utils.log("ElasticSearchHelper created " +created);
        if (!created) {
            throw new Exception("erreur lors de la creation de l'index"
                    + response.getIndex() + " type: " + response.getType()
                    + " id:" + response.getId()+ response.getShardInfo() + created);
        }
        

    }

}
