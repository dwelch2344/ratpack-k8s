package com.example.playground;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ratpack.handling.Handler;
import ratpack.http.Request;
import ratpack.jackson.JsonRender;
import ratpack.spring.config.EnableRatpack;
import ratpack.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ratpack.jackson.Jackson.json;

@EnableRatpack
@SpringBootApplication
public class Main {

    public static void main(String... args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public KubernetesClient k8s(){
        return new DefaultKubernetesClient();
    }

    @Bean
    public Handler handler() {

        KubernetesClient k8s = k8s();

        return context -> {
            Request req = context.getRequest();
            MultiValueMap<String, String> params = req.getQueryParams();

            List<ReplicationController> rcs = k8s.replicationControllers().list().getItems();
            List<Pod> pods = k8s.pods().list().getItems();

            context.byMethod(s -> {
                s.get(() -> {
                    context.render(
                            new MapBuilder()
                                    .put("rcs", rcs)
                                    .put("pods", pods)
                                    .toJson()
                    );
                });
            });
        };
    }

    public class MapBuilder<K, V>{
        private Map<K, V> map;

        public MapBuilder(){
            this(new HashMap<>());
        }

        public MapBuilder(Map<K, V> map){
            this.map = map;
        }

        public MapBuilder<K, V> put(K key, V value){
            map.put(key, value);
            return this;
        }

        public Map<K, V> build(){
            return map;
        }

        public JsonRender toJson(){
            return json(map);
        }
    }
}