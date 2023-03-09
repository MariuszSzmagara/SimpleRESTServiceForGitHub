package pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GitHubRepositoryRestTemplateConfiguration {

    @Bean
    @Autowired
    @Qualifier("gitHubRepositoryTemplateWithErrorHandling")
    public RestTemplate gitHubRepositoryTemplateWithErrorHandling(@Value("${github.host.url}") String gitHubRepositoryHostUrl) {
        return new RestTemplateBuilder()
                .rootUri(gitHubRepositoryHostUrl)
                .errorHandler(new GitHubRepositoryRestTemplateResponseErrorHandler())
                .build();
    }
}