package pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository.exception.GitHubRepositoryDoesntExistsException;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GitHubRepositoryServiceImplementation implements GitHubRepositoryService {
    private static final int THE_OLDEST_GIT_HUB_REPOSITORY = 0;
    private final RestTemplate gitHubRepositoryTemplateWithErrorHandling;
    private final ModelMapper modelMapper;

    @Override
    public GitHubRepositoryDto getGitHubRepositoryDtoByGivenOwnerAndRepositoryName
            (String owner, String repositoryName) {
        Optional<GitHubRepository> optionalGitHubRepositoryForGivenOwnerAndRepositoryName =
                Optional.ofNullable(gitHubRepositoryTemplateWithErrorHandling.getForObject(
                        "/repos/{owner}/{repositoryName}",
                        GitHubRepository.class, owner, repositoryName));
        return getGitHubRepositoryDto(owner, optionalGitHubRepositoryForGivenOwnerAndRepositoryName);
    }

    private GitHubRepositoryDto getGitHubRepositoryDto(
            String userName,
            Optional<GitHubRepository> optionalGitHubRepository) {
        if (optionalGitHubRepository.isEmpty()) {
            throw new GitHubRepositoryDoesntExistsException(userName);
        }
        return modelMapper.map(
                optionalGitHubRepository.get(),
                GitHubRepositoryDto.class);
    }

    @Override
    public List<GitHubRepositoryDto> getUserGitHubRepositoriesDtoByGivenRegularExpressionForRepositoryName(
            String userName, String regularExpressionForName) {
        Stream<GitHubRepository> gitHubRepositoryStreamForGivenUserName =
                prepareRepositoriesStreamForGivenUserName(userName);
        List<GitHubRepository> listOfGitHubRepositoryForGivenUserName =
                gitHubRepositoryStreamForGivenUserName
                .filter((GitHubRepository gitHubRepository) ->
                        gitHubRepository.getName().matches(regularExpressionForName))
                .collect(Collectors.toList());
        return getListOfGitHubRepositoryDto(userName, listOfGitHubRepositoryForGivenUserName);
    }

    private List<GitHubRepositoryDto> getListOfGitHubRepositoryDto(
            String userName,
            List<GitHubRepository> listOfGitHubRepository) {
        if (listOfGitHubRepository.isEmpty()) {
            throw new GitHubRepositoryDoesntExistsException(userName);
        }
        return listOfGitHubRepository
                .stream()
                .map((GitHubRepository gitHubRepository) -> modelMapper.map(
                        gitHubRepository,
                        GitHubRepositoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public GitHubRepositoryDto getTheOldestRepositoryDtoForGivenUserName(String userName) {
        GitHubRepository[] arrayWithTheOldestGitHubRepository =
                gitHubRepositoryTemplateWithErrorHandling.getForObject(
                        "/users/{userName}/repos?sort=created_at&direction=asc&per_page=1",
                        GitHubRepository[].class, userName);
        if (arrayWithTheOldestGitHubRepository == null || arrayWithTheOldestGitHubRepository.length == 0){
            throw new GitHubRepositoryDoesntExistsException(userName);
        }
        GitHubRepository theOldestGitHubRepository =
                arrayWithTheOldestGitHubRepository[THE_OLDEST_GIT_HUB_REPOSITORY];
        return modelMapper.map(theOldestGitHubRepository, GitHubRepositoryDto.class);
    }

    @Override
    public GitHubRepositoryDto getMostForkedAndMostRecentRepositoryDtoForGivenUserName(String userName) {
        Stream<GitHubRepository> gitHubRepositoriesStreamForGivenUserName =
                prepareRepositoriesStreamForGivenUserName(userName);
        Optional<GitHubRepository> optionalMostForkedAndRecentRepositoryForGivenUserName =
                gitHubRepositoriesStreamForGivenUserName
                .max(Comparator.comparing(GitHubRepository::getForks)
                        .thenComparing(GitHubRepository::getCreatedAt));
        return getGitHubRepositoryDto(userName, optionalMostForkedAndRecentRepositoryForGivenUserName);
    }

    private Stream<GitHubRepository> prepareRepositoriesStreamForGivenUserName(String userName) {
        Supplier<List<GitHubRepository>> streamSupplier = new Supplier<>() {
            int pageNumber = 0;
            @Override
            public List<GitHubRepository> get() {
                List<GitHubRepository> gitHubRepositoriesForGivenUserName =
                        Arrays.asList(Objects.requireNonNull(gitHubRepositoryTemplateWithErrorHandling.getForObject(
                                "/users/{userName}/repos?per_page=30&page=" + pageNumber,
                                GitHubRepository[].class,
                                userName)));
                pageNumber++;
                return gitHubRepositoriesForGivenUserName;
            }
        };
        Stream<List<GitHubRepository>> generate = Stream.generate(streamSupplier);
        return generate.takeWhile(it -> it.size() > 0)
                .flatMap(List::stream);
    }
}