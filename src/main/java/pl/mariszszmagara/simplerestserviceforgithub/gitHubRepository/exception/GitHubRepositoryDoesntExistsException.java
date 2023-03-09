package pl.mariszszmagara.simplerestserviceforgithub.gitHubRepository.exception;

public class GitHubRepositoryDoesntExistsException extends RuntimeException {

    public GitHubRepositoryDoesntExistsException(String ownerName) {
        super("Repository for given owner: " + ownerName + " doesn't exist");
    }
}
