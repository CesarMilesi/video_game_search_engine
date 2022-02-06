package fr.lernejo.search.api;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class GameInfoListener {

    private final RestHighLevelClient restHighLevelClient;

    public GameInfoListener(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @RabbitListener(queues = "game_info")
    public void onMessage(String message, @Header("game_id") String gameId) {
        IndexRequest request = new IndexRequest("games");
        request.id(gameId);
        request.source(message, XContentType.JSON);
        try {
            this.restHighLevelClient.index(request, RequestOptions.DEFAULT);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
