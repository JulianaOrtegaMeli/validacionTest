package com.mercadolibre.fashion.validator.plugin.beans.rest.fashion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.catalog.commons.restclient.GenericRestClient;
import com.mercadolibre.fashion.validator.plugin.dto.InputJsonDTO;
import com.mercadolibre.fashion.validator.plugin.dto.OutputJsonDTO;
import com.mercadolibre.restclient.http.Headers;
import com.mercadolibre.restclient.retry.NoopRetryStrategy;
import com.newrelic.api.agent.Trace;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

public class ColliderRestClient extends GenericRestClient {

  public static final String FILTERABLE_URL_TEMPLATE = "validation-hub/items/normalize";

  private static final String RESOURCE_NAME = "sd-hub";

  private static final String ENV_QUERY_PARAM = "env";

  private final String environment;

  @Autowired
  public ColliderRestClient(ObjectMapper objectMapper, String baseUrl,
      @Value("${sd.api.chart.socket-timeout:100}") int socketTimeout, @Value("${sd.api.chart.max-connections:100}") int maxTotal,
      @Value("${sd.api.sd-hub.env}") String environment) throws IOException {
    super(objectMapper, Headers::new, baseUrl, RESOURCE_NAME, NoopRetryStrategy.INSTANCE, maxTotal, socketTimeout);
    this.environment = environment;
  }

  @Trace
  public OutputJsonDTO searchFilterableSize(InputJsonDTO item) {
    Map<String, String> params = new ConcurrentHashMap<>();
    params.put("department", "structured-data");
    params.put("validation", "fashion-size-consistency");
    params.put(ENV_QUERY_PARAM, "core-test");
    params.put("forwarded_client_id", "2860837171021627");
    //+ buildUrl(params)
    String variable = "https://internal-api.mercadolibre.com/validation-hub/items/normalize?department=structured-data&validation=fashion-size-consistency&forwarded_client_id=2860837171021627";
    System.out.println("ES EL SCOPE: ");
    System.out.println(System.getenv("SCOPE"));
    return this.post(variable, item, OutputJsonDTO.class);
  }

  private String buildUrl(Map<String, String> params) {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
    if (StringUtils.isNotEmpty(this.environment)) {
      uriBuilder.queryParam(ENV_QUERY_PARAM, this.environment);
    }
    params.forEach(uriBuilder::queryParam);
    return uriBuilder.toUriString();
  }



}
