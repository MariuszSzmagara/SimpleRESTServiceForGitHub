package pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository.exception.GitHubRepositoryApiResponseException;
import pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository.exception.GitHubRepositoryDoesntExistsException;
import pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository.exception.GitHubRepositoryRestTemplateException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GitHubRepositoryControllerAdvice {
    @ResponseBody
    @ExceptionHandler(GitHubRepositoryDoesntExistsException.class)
    public ResponseEntity<GitHubRepositoryApiResponseException> repositoryDoesntExistsHandler
            (GitHubRepositoryDoesntExistsException repositoryDoesntExistsException) {
        GitHubRepositoryApiResponseException gitHubApiResponseErrorBuilder = GitHubRepositoryApiResponseException.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(repositoryDoesntExistsException.getMessage())
                .timeStamp(LocalDateTime.now())
                .error_code(HttpStatus.NOT_FOUND.toString())
                .build();
        return new ResponseEntity<>(gitHubApiResponseErrorBuilder, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(GitHubRepositoryRestTemplateException.class)
    public String restTemplateErrorHandler(GitHubRepositoryRestTemplateException customException) {
        return customException.getMessage();
    }
}