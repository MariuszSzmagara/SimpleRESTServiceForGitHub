# SimpleRESTServiceForGitHub
Simple REST service that returns the Github repository's details.
Details include:

•	full name of the repository
•	description of the repository
•	git clone url
•	information if it is forked
•	number of forks
•	date of creation (ISO format)

Response format:
{
  "fullName": "...",
  "description": "...",
  "cloneUrl": "...",
  "forked": "...",
  "forks": 0,
  "createdAt": "..."
}

The API of the service looks as follows:

GET /repositories/{owner}/{repositoryName}
Get GitHub repository for given owner and repository name.

GET /users/{userName}/repos/{repositoryName}/{regularExpresionForRepositoryName}
Get list of GitHub repositories by given regular expression for repository name.

GET /users/{userName}/repos/oldes
Get the oldest GitHub repository for given user name.

GET /users/{userName}/repos/mostedForkedAndMostRecent
Get most forked and most recent GitHub repository for given user name.

To get GitHub repositories by given regular expression for repository name or to get most forked and most recent GitHub repositories for given user name first we have to get all GitHub repositories for given user name before we start filter. Problem to get all GitHub repositories is unknown size of GitHub repositories so I use this construction
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
to deliver all GitHub repositories to filter them by most forked and most recent in 
public GitHubRepositoryDto getMostForkedAndMostRecentRepositoryDtoForGivenUserName(String userName)
and by given regular expression for repository name in 
public List<GitHubRepositoryDto> getUserGitHubRepositoriesDtoByGivenRegularExpressionForRepositoryName(
        String userName, String regularExpressionForName).


