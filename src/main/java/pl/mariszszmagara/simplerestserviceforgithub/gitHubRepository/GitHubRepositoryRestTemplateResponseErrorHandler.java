package pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository.exception.GitHubRepositoryRestTemplateException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Scanner;

@Slf4j
public class GitHubRepositoryRestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        HttpStatusCode status = clientHttpResponse.getStatusCode();
        return status.is4xxClientError() || status.is5xxServerError();    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        String responseAsString = toString(clientHttpResponse.getBody());
        log.error(clientHttpResponse.getStatusCode() +
                " ResponseBody: {} Date: {}", responseAsString, clientHttpResponse.getHeaders().get(HttpHeaders.DATE));

        throw new GitHubRepositoryRestTemplateException(responseAsString);
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse clientHttpResponse) throws IOException {
        String responseAsString = toString(clientHttpResponse.getBody());
        log.error(clientHttpResponse.getStatusCode() + " URL: {}, HttpMethod: {}, ResponseBody: {}, Date {}",
                url, method, responseAsString, clientHttpResponse.getHeaders().get(HttpHeaders.DATE));
        throw new GitHubRepositoryRestTemplateException(responseAsString);
    }

    String toString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}