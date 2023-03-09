package pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GitHubRepositoryDto {
    private String fullName;
    private String description;
    private String cloneUrl;
    private boolean forked;
    private int forks;
    private LocalDateTime createdAt;
}