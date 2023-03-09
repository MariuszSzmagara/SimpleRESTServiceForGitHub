package pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestTemplate;
import pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository.exception.GitHubRepositoryDoesntExistsException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubRepositoryServiceImplementationTest {

    @Mock
    RestTemplate gitHubRepositoryRestTemplate;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    GitHubRepositoryServiceImplementation gitHubRepositoryService;

    private GitHubRepository expectedGitHubRepository;
    private GitHubRepositoryDto expectedGitHubRepositoryDto;

    private final String owner = "Owner";
    private final String userName = "User";
    private final String repositoryName = "Repository";

    private final GitHubRepository[] emptyArrayOfGitHubRepositories = {};

    @BeforeEach
    public void setUpBeforeEach() {
        expectedGitHubRepository = GitHubRepository.builder()
                .name("RepositoryOne")
                .fullName("GitHubRepository")
                .description("Java Application")
                .cloneUrl("=https://api.github.com")
                .fork(false)
                .forks(10)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();
        expectedGitHubRepositoryDto = modelMapper.map(
                expectedGitHubRepository,
                GitHubRepositoryDto.class);
    }

    @Test
    public void shouldReturnGitHubRepositoryDtoByGivenOwnerAndRepositoryName() {

        //given + when
        when(gitHubRepositoryRestTemplate.getForObject(
                "/repos/{owner}/{repositoryName}",
                GitHubRepository.class,
                owner,
                repositoryName))
                .thenReturn(expectedGitHubRepository);
        when(modelMapper.map(
                expectedGitHubRepository,
                GitHubRepositoryDto.class))
                .thenReturn(expectedGitHubRepositoryDto);

        //then
        GitHubRepositoryDto actualGitHubRepositoryDto =
                gitHubRepositoryService.getGitHubRepositoryDtoByGivenOwnerAndRepositoryName(
                        owner,
                        repositoryName);
        verify(gitHubRepositoryRestTemplate, times(1)).getForObject(
                anyString(),
                ArgumentMatchers.eq(GitHubRepository.class),
                ArgumentMatchers.eq(owner),
                ArgumentMatchers.eq(repositoryName));
        verify(modelMapper, times(2)).map(
                expectedGitHubRepository,
                GitHubRepositoryDto.class);
        assertThat(expectedGitHubRepositoryDto).isEqualTo(actualGitHubRepositoryDto);
    }

    @Test
    public void shouldThrowExceptionWhenGetNullFromRestTemplateWhenCallGetGitHubRepositoryDtoByGivenOwnerAndRepositoryName() {

        //given + when
        when(gitHubRepositoryRestTemplate.getForObject(
                "/repos/{owner}/{repositoryName}",
                GitHubRepository.class,
                owner,
                repositoryName))
                .thenReturn(null);

        //then
        assertThrows(GitHubRepositoryDoesntExistsException.class,
                () -> gitHubRepositoryService
                        .getGitHubRepositoryDtoByGivenOwnerAndRepositoryName(owner, repositoryName));
    }

    @Test
    public void shouldReturnUserGitHubRepositoriesDtoByGivenRegularExpressionForRepositoryName() {

        //given
        GitHubRepository expectedGitHubRepositoryByGivenRegexForRepositoryName = GitHubRepository.builder()
                .name("RepositoryTwo")
                .fullName("GitHubRepository")
                .description("Java Application")
                .cloneUrl("=https://api.github.com")
                .fork(false)
                .forks(10)
                .createdAt(LocalDateTime.now())
                .build();
        GitHubRepositoryDto expectedGitHubRepositoryDtoByGivenRegexForRepositoryName = modelMapper.map(
                expectedGitHubRepositoryByGivenRegexForRepositoryName,
                GitHubRepositoryDto.class);
        GitHubRepository[] arrayOfGitHubRepositories = {
                expectedGitHubRepository,
                expectedGitHubRepositoryByGivenRegexForRepositoryName
        };

        //when
        when(gitHubRepositoryRestTemplate.getForObject(
                "/users/{userName}/repos?per_page=30&page=0",
                GitHubRepository[].class,
                userName))
                .thenReturn(arrayOfGitHubRepositories);
        when(gitHubRepositoryRestTemplate.getForObject(
                "/users/{userName}/repos?per_page=30&page=1",
                GitHubRepository[].class,
                userName))
                .thenReturn(emptyArrayOfGitHubRepositories);
        when(modelMapper.map(
                expectedGitHubRepositoryByGivenRegexForRepositoryName,
                GitHubRepositoryDto.class))
                .thenReturn(expectedGitHubRepositoryDtoByGivenRegexForRepositoryName);

        //then
        List<GitHubRepositoryDto> actualListOfGitHubRepositoryByGivenRegexForRepositoryName =
                gitHubRepositoryService.getUserGitHubRepositoriesDtoByGivenRegularExpressionForRepositoryName(
                        userName,
                        expectedGitHubRepositoryByGivenRegexForRepositoryName.getName());
        verify(gitHubRepositoryRestTemplate, times(2)).getForObject(
                anyString(),
                ArgumentMatchers.eq(GitHubRepository[].class),
                ArgumentMatchers.eq(userName));
        verify(modelMapper, times(1)).map(
                expectedGitHubRepository,
                GitHubRepositoryDto.class);
        assertThat(actualListOfGitHubRepositoryByGivenRegexForRepositoryName.size()).isEqualTo(1);
        assertThat(expectedGitHubRepositoryDto).isEqualTo(actualListOfGitHubRepositoryByGivenRegexForRepositoryName.get(0));
    }

///////////
    @Test
    public void shouldThrowExceptionWhenGitHubRepositoriesDtoDoesntExistByGivenRegularExpressionForRepositoryName() {

        //given
        GitHubRepository expectedGitHubRepositoryByGivenRegexForRepositoryName = GitHubRepository.builder()
                .name("RepositoryTwo")
                .fullName("GitHubRepository")
                .description("Java Application")
                .cloneUrl("=https://api.github.com")
                .fork(false)
                .forks(10)
                .createdAt(LocalDateTime.now())
                .build();
        GitHubRepository[] arrayOfGitHubRepositories = {
                expectedGitHubRepository,
                expectedGitHubRepositoryByGivenRegexForRepositoryName
        };

        //when
        when(gitHubRepositoryRestTemplate.getForObject(
                "/users/{userName}/repos?per_page=30&page=0",
                GitHubRepository[].class,
                userName))
                .thenReturn(arrayOfGitHubRepositories);
        when(gitHubRepositoryRestTemplate.getForObject(
                "/users/{userName}/repos?per_page=30&page=1",
                GitHubRepository[].class,
                userName))
                .thenReturn(emptyArrayOfGitHubRepositories);

        //then
        assertThrows(GitHubRepositoryDoesntExistsException.class,
                () -> gitHubRepositoryService.getUserGitHubRepositoriesDtoByGivenRegularExpressionForRepositoryName(
                        userName,
                        "RepositoryTwoo"));
    }

    @Test
    public void shouldReturnTheOldestGitHubRepositoryDtoForGivenUserName() {

        //given
        GitHubRepository[] arrayOfGitHubRepositories = {
                expectedGitHubRepository
        };

        //when
        when(gitHubRepositoryRestTemplate.getForObject(
                "/users/{userName}/repos?sort=created_at&direction=asc&per_page=1",
                GitHubRepository[].class,
                userName))
                .thenReturn(arrayOfGitHubRepositories);
        when(modelMapper.map(
                expectedGitHubRepository,
                GitHubRepositoryDto.class))
                .thenReturn(expectedGitHubRepositoryDto);

        //then
        GitHubRepositoryDto actualGitHubRepositoryDto =
                gitHubRepositoryService.getTheOldestRepositoryDtoForGivenUserName(userName);
        verify(gitHubRepositoryRestTemplate, times(1)).getForObject(
                anyString(),
                ArgumentMatchers.eq(GitHubRepository[].class),
                ArgumentMatchers.eq(userName));
        verify(modelMapper, times(2)).map(
                expectedGitHubRepository,
                GitHubRepositoryDto.class);
        assertThat(expectedGitHubRepositoryDto).isEqualTo(actualGitHubRepositoryDto);
    }

    @Test
    public void shouldThrowExceptionForEmptyArrayFromRestTemplateWhenCallGetTheOldestRepositoryDtoForGivenUserName() {

        //given + when
        when(gitHubRepositoryRestTemplate.getForObject(
                "/users/{userName}/repos?sort=created_at&direction=asc&per_page=1",
                GitHubRepository[].class,
                userName))
                .thenReturn(emptyArrayOfGitHubRepositories);

        //then
        assertThrows(GitHubRepositoryDoesntExistsException.class,
                () -> gitHubRepositoryService.getTheOldestRepositoryDtoForGivenUserName(userName));
    }

    @Test
    public void shouldThrowExceptionForNullFromRestTemplateWhenCallGetTheOldestRepositoryDtoForGivenUserName() {

        //given + when
        when(gitHubRepositoryRestTemplate.getForObject(
                "/users/{userName}/repos?sort=created_at&direction=asc&per_page=1",
                GitHubRepository[].class,
                userName))
                .thenReturn(null);

        //then
        assertThrows(GitHubRepositoryDoesntExistsException.class,
                () -> gitHubRepositoryService.getTheOldestRepositoryDtoForGivenUserName(userName));
    }

    @Test
    public void shouldReturnMostForkedAndMostRecentRepositoryDtoForGivenUserName() {

        //given
        GitHubRepository expectedGitHubRepositoryMostForkedAndMostRecent = GitHubRepository.builder()
                .name("RepositoryOne")
                .fullName("GitHubRepository")
                .description("Java Application")
                .cloneUrl("=https://api.github.com")
                .fork(false)
                .forks(10)
                .createdAt(LocalDateTime.now())
                .build();
        GitHubRepositoryDto expectedGitHubRepositoryDtoMostForkedAndMostRecent = modelMapper.map(
                expectedGitHubRepositoryMostForkedAndMostRecent,
                GitHubRepositoryDto.class);
        GitHubRepository[] arrayOfGitHubRepositories = {
                expectedGitHubRepository,
                expectedGitHubRepositoryMostForkedAndMostRecent
        };

        //when
        when(gitHubRepositoryRestTemplate.getForObject(
                "/users/{userName}/repos?per_page=30&page=0",
                GitHubRepository[].class,
                userName))
                .thenReturn(arrayOfGitHubRepositories);
        when(gitHubRepositoryRestTemplate.getForObject(
                "/users/{userName}/repos?per_page=30&page=1",
                GitHubRepository[].class,
                userName))
                .thenReturn(emptyArrayOfGitHubRepositories);
        when(modelMapper.map(
                expectedGitHubRepositoryMostForkedAndMostRecent,
                GitHubRepositoryDto.class))
                .thenReturn(expectedGitHubRepositoryDtoMostForkedAndMostRecent);

        //then
        GitHubRepositoryDto actualGitHubRepositoryDtoMostForkedAndMostRecent =
                gitHubRepositoryService.getMostForkedAndMostRecentRepositoryDtoForGivenUserName(userName);
        verify(gitHubRepositoryRestTemplate, times(2)).getForObject(
                anyString(),
                ArgumentMatchers.eq(GitHubRepository[].class),
                ArgumentMatchers.eq(userName));
        verify(modelMapper, times(1)).map(
                expectedGitHubRepository,
                GitHubRepositoryDto.class);
        assertThat(expectedGitHubRepositoryDto).isEqualTo(actualGitHubRepositoryDtoMostForkedAndMostRecent);
    }

    @Test
    public void shouldThrowExceptionForEmptyArrayFromRestTemplateWhenCallMostForkedAndMostRecentRepositoryDtoForGivenUserName() {

        //given + when
        when(gitHubRepositoryRestTemplate.getForObject(
                "/users/{userName}/repos?per_page=30&page=0",
                GitHubRepository[].class,
                userName))
                .thenReturn(emptyArrayOfGitHubRepositories);
        //then
        assertThrows(GitHubRepositoryDoesntExistsException.class,
                () -> gitHubRepositoryService.getMostForkedAndMostRecentRepositoryDtoForGivenUserName(userName));
    }
}