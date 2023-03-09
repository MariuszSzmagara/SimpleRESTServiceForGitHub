package pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class GitHubRepositoryApiResponseException {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime timeStamp;
    private HttpStatus status;
    private String error_code;
    private String message;
}
