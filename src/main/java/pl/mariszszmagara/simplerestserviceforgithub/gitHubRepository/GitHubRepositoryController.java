package pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ResponseStatus(HttpStatus.OK)
@RestController
@RequiredArgsConstructor
public class GitHubRepositoryController {

    private final GitHubRepositoryService gitHubRepositoryService;

    @GetMapping("/repositories/{owner}/{repositoryName}")
    public GitHubRepositoryDto getGitHubRepositoryByGivenOwnerAndRepositoryName(
            @PathVariable String owner,
            @PathVariable String repositoryName) {
        return gitHubRepositoryService.getGitHubRepositoryDtoByGivenOwnerAndRepositoryName(
                owner,
                repositoryName);
    }

    @GetMapping("/users/{userName}/repos/{regularExpressionForRepositoryName}")
    public List<GitHubRepositoryDto> getUserGitHubRepositoriesByGivenRegularExpressionForRepositoryName(
            @PathVariable String userName, @PathVariable String regularExpressionForRepositoryName) {
        return gitHubRepositoryService.getUserGitHubRepositoriesDtoByGivenRegularExpressionForRepositoryName(
                userName,
                regularExpressionForRepositoryName);
    }

    @GetMapping("/users/{userName}/repos/oldest")
    public GitHubRepositoryDto getTheOldestRepositoryForGivenUserName(
            @PathVariable String userName) {
        return gitHubRepositoryService.getTheOldestRepositoryDtoForGivenUserName(userName);
    }

    @GetMapping("/users/{userName}/repos/mostForkedAndMostRecent")
    public GitHubRepositoryDto getMostForkedAndMostRecentGitHubRepositoryForGivenOwner(
            @PathVariable String userName) {
        return gitHubRepositoryService.getMostForkedAndMostRecentRepositoryDtoForGivenUserName(userName);
    }
}