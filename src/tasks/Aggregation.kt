package tasks

import contributors.User

/*
TODO: Write aggregation code.

 In the initial list each user is present several times, once for each
 repository he or she contributed to.
 Merge duplications: each user should be present only once in the resulting list
 with the total value of contributions for all the repositories.
 Users should be sorted in a descending order by their contributions.

 The corresponding test can be found in test/tasks/AggregationKtTest.kt.
 You can use 'Navigate | Test' menu action (note the shortcut) to navigate to the test.
*/
fun List<User>.aggregate(): List<User> {
    groupBy { it.login } // login 정보로 group by, login을 기준으로 묶었기 때문에 key = login, value = 그룹으로 묶여진 값들(유저들)
        .map { (login, users) -> User( login, users.sumOf { it.contributions }) } // 하나의 로그인에 대한 전체 contribution 구해 하나의 유저로 만들기
        .sortedByDescending { it.contributions }

    return this
    TODO()
}