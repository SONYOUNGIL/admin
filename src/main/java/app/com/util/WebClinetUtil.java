package app.com.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public class WebClinetUtil {

  /**
   * webClientSelect
   * @param uri
   * @param paramMap
   * @return
   */
  public static List<Map> webClientSelect(String uri, Map paramMap){
    String ip = getServerIp();
    WebClient webClient = WebClient.create("http://"+ip+":8080");
    Mono<Map> mono = Mono.just(paramMap);
    List<Map> resultList = webClient.post()
                                    .uri(uri)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .body(mono, Map.class)
                                    .retrieve()
                                    .bodyToMono(List.class)
                                    .block();
    return resultList;
  }

  /**
   * webClientSave
   * @param uri
   * @param list
   * @return 
   */
  public static String webClientSave(String uri, List<Map> list) {
    String ip = getServerIp();
    WebClient webClient = WebClient.create("http://"+ip+":8080");
    Mono<List> mono = Mono.just(list);
    Mono<String> result  = webClient.post()
                                    .uri(uri)
                                    .body(mono, List.class) 
                                    .retrieve() 
                                    .bodyToMono(String.class);
    result.subscribe(response -> { }, e -> {System.out.println("error message : " + e.getMessage()); });
    return "success";
  }

  private static String getServerIp(){
    String ip = "";
    try {
      ip =  InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return ip;
  }
}
