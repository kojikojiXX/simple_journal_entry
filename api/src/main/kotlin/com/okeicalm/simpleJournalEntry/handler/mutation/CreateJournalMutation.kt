package com.okeicalm.simpleJournalEntry.handler.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.okeicalm.simpleJournalEntry.handler.type.JournalEntryType
import com.okeicalm.simpleJournalEntry.handler.type.JournalType
import com.okeicalm.simpleJournalEntry.usecase.journal.JournalCreateUseCase
import com.okeicalm.simpleJournalEntry.usecase.journal.JournalCreateUseCaseInput
import com.okeicalm.simpleJournalEntry.usecase.journal.JournalEntryInputData
import org.springframework.stereotype.Component
import java.time.LocalDate

data class CreateJournalInput(
    val userId: ID,
    val incurredOn: Int,
    val createJournalEntryInput: List<CreateJournalEntryInput>
)
data class CreateJournalEntryInput(val side: Int, val accountID: ID, val value: Int)

@Component
class CreateJournalMutation(private val journalCreateUseCase: JournalCreateUseCase) : Mutation {
    fun createJournal(input: CreateJournalInput): JournalType? {
        val journalEntryInputDatum = input.createJournalEntryInput.map {
            JournalEntryInputData(
                side = it.side.toByte(),
                accountID = it.accountID.toString().toLong(),
                value = it.value
            )
        }
        val inputData = JournalCreateUseCaseInput(
            userId = input.userId.toString().toLong(),
            incurredOn = LocalDate.now(), // TODO: dummy
            journalEntryInputDatum = journalEntryInputDatum
        )
        val journal = journalCreateUseCase.call(inputData).journal ?: return null

        return JournalType(
            id = ID(journal.id.toString()),
            userId = ID(journal.userId.toString()),
            incurredOn = journal.incurredOn,
            journalEntries = journal.journalEntries.map { JournalEntryType(it) }
        )
    }
}
