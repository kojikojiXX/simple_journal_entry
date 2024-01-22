package com.okeicalm.simpleJournalEntry.handler.type

import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.okeicalm.simpleJournalEntry.entity.User
import graphql.schema.DataFetchingEnvironment
import java.util.concurrent.CompletableFuture

const val userTypeGraphQLName = "User"

@GraphQLName(userTypeGraphQLName)
data class UserType(
    val id: ID,
    val name: String,
) {
    constructor(user: User) : this(
        ID(user.id.toString()),
        user.name,
    )

    fun journals(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<JournalType> {
        return dataFetchingEnvironment.getValueFromDataLoader("JournalsDataLoader", id)
    }
}
