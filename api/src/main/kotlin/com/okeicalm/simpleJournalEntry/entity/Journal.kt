package com.okeicalm.simpleJournalEntry.entity

import java.time.LocalDate

data class Journal(
    val id: Long,
    val userId: Long,
    val incurredOn: LocalDate,
    val journalEntries: List<JournalEntry>,
) {
    companion object {
        fun create(
            userId: Long,
            incurredOn: LocalDate,
            journalEntries: List<JournalEntry>,
        ): Journal {
            return Journal(
                id = 0,
                userId = userId,
                incurredOn = incurredOn,
                journalEntries = journalEntries,
            )
        }
    }
}
