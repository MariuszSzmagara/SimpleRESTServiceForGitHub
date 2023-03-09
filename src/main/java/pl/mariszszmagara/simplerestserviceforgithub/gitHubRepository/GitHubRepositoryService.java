package pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository;

import java.util.List;

public interface GitHubRepositoryService {
    GitHubRepositoryDto getGitHubRepositoryDtoByGivenOwnerAndRepositoryName(String owner, String repositoryName);
    List<GitHubRepositoryDto> getUserGitHubRepositoriesDtoByGivenRegularExpressionForRepositoryName(String userName, String regularExpressionForName);
    GitHubRepositoryDto getTheOldestRepositoryDtoForGivenUserName(String userName);
    GitHubRepositoryDto getMostForkedAndMostRecentRepositoryDtoForGivenUserName(String userName);
}
