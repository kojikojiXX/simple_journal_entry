package com.okeicalm.simpleJournalEntry.handler.type

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.scalars.ID
import com.okeicalm.simpleJournalEntry.entity.Journal
import java.time.LocalDate

const val journalTypeGraphQLName = "Journal"

@GraphQLName(journalTypeGraphQLName)
data class JournalType(
    val id: ID,
    val incurredOn: LocalDate,
    val journalEntries: List<JournalEntryType>,
    @GraphQLIgnore val userId: ID
) {
    constructor(journal: Journal) : this(
        id = ID(journal.id.toString()),
        incurredOn = journal.incurredOn,
        journal.journalEntries.map { JournalEntryType(it) },
        ID(journal.userId.toString())
    )
}
