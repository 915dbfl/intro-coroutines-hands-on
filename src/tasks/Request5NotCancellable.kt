package tasks

import contributors.*
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

suspend fun loadContributorsNotCancellable(service: GitHubService, req: RequestData): List<User> {
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .body() ?: emptyList()

    // coroutineScope 밖이니까 return이 필요함.
    return repos.map { repo ->
        // 상속받을 coroutineScope가 없으므로 별도의 Scope로 async 호출
        // cancel하면 부모부터 cancel이 될텐데 해당 globalScope는 독자적인 코드이므로 취소가 되지 않는다.
        GlobalScope.async {
            log("starting loading for ${repo.name}")
            delay(3000)
            service
                .getRepoContributors(req.org, repo.name)
                .also { logUsers(repo, it) }
                .bodyList()
        }
    }.awaitAll().toList().flatten().aggregate()
    // List<List<User>> -> flatten() -> List<User>
}