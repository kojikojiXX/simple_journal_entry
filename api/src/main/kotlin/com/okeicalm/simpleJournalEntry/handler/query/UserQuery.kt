package com.okeicalm.simpleJournalEntry.handler.query

import com.expediagroup.graphql.server.operations.Query
import com.okeicalm.simpleJournalEntry.handler.type.UserType
import com.okeicalm.simpleJournalEntry.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserQuery(private val userRepository: UserRepository):Query {
    fun allUsers(): List<UserType> {
        return userRepository.findAll().map { UserType((it)) }
    }
}
