package com.okeicalm.simpleJournalEntry.repository

import com.okeicalm.simpleJournalEntry.entity.Journal
import com.okeicalm.simpleJournalEntry.entity.JournalEntry
import com.okeicalm.simpleJournalEntry.infra.db.tables.references.JOURNALS
import com.okeicalm.simpleJournalEntry.infra.db.tables.references.JOURNAL_ENTRIES
import com.okeicalm.simpleJournalEntry.infra.db.tables.references.USERS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

interface JournalRepository {
    fun findAll(): List<Journal>
    fun findById(id: Long): Journal?
    fun isUserExist(userId: Long): Boolean
    fun findByUserId(userId: Long): List<Journal>
    fun create(journal: Journal): Journal
}

@Repository
class JournalRepositoryImpl(private val dslContext: DSLContext) : JournalRepository {
    override fun findAll(): List<Journal> {
        val records = dslContext
            .select(
                JOURNALS.ID,
                JOURNALS.INCURRED_ON,
                JOURNAL_ENTRIES.ID,
                JOURNAL_ENTRIES.JOURNAL_ID,
                JOURNAL_ENTRIES.ACCOUNT_ID,
                JOURNAL_ENTRIES.SIDE,
                JOURNAL_ENTRIES.VALUE,
            )
            .from(JOURNALS)
            .join(JOURNAL_ENTRIES)
            .on(JOURNALS.ID.eq(JOURNAL_ENTRIES.JOURNAL_ID))
            .fetch()

        val journalMap = records.groupBy { it[JOURNALS.ID] }

        return journalMap.map { j ->
            val journalEntries = j.value.map { je ->
                JournalEntry(
                    id = je.getValue(JOURNAL_ENTRIES.ID)!!,
                    journalId = je.getValue(JOURNAL_ENTRIES.JOURNAL_ID)!!,
                    accountId = je.getValue(JOURNAL_ENTRIES.ACCOUNT_ID)!!,
                    side = je.getValue(JOURNAL_ENTRIES.SIDE)!!,
                    value = je.getValue(JOURNAL_ENTRIES.VALUE)!!,
                )
            }
            Journal(
                id = j.key!!,
                incurredOn = j.value.first().getValue(JOURNALS.INCURRED_ON)!!,
                journalEntries = journalEntries,
                userId = j.value.first().getValue(JOURNALS.USER_ID)!!
            )
        }
    }

    override fun findById(id: Long): Journal? {
        return dslContext
            .fetchOne(JOURNALS, JOURNALS.ID.eq(id))
            ?.into(Journal::class.java)
    }

    override fun isUserExist(userId: Long): Boolean {
        return dslContext
            .fetchExists(USERS, USERS.ID.eq(userId))
    }

    override fun findByUserId(userId: Long): List<Journal> {
        return dslContext
            .select()
            .from(JOURNALS)
            .where(JOURNALS.USER_ID.eq(userId))
            .fetchInto(Journal::class.java)
    }

    override fun create(journal: Journal): Journal {
        // For Journal
        val record = dslContext
            .newRecord(JOURNALS)
            .apply {
                userId = journal.userId
                incurredOn = journal.incurredOn
            }
        record.store()

        val journalId = record.id!!

        val journalEntryWithJournalId = journal.journalEntries.map {
            it.copy(journalId = journalId)
        }
        // For JournalEntry
        bulkInsertJournalEntry(journalEntryWithJournalId)

        val createdJournalEntries = dslContext
            .select()
            .from(JOURNAL_ENTRIES)
            .where(JOURNAL_ENTRIES.JOURNAL_ID.eq(journalId))
            .fetch {
                JournalEntry(
                    id = it.getValue(JOURNAL_ENTRIES.ID)!!,
                    journalId = it.getValue(JOURNAL_ENTRIES.JOURNAL_ID)!!,
                    accountId = it.getValue(JOURNAL_ENTRIES.ACCOUNT_ID)!!,
                    side = it.getValue(JOURNAL_ENTRIES.SIDE)!!,
                    value = it.getValue(JOURNAL_ENTRIES.VALUE)!!,
                )
            }

        return journal.copy(id = journalId, journalEntries = createdJournalEntries)
    }

    private fun bulkInsertJournalEntry(journalEntries: List<JournalEntry>) {
        val queries = journalEntries.map {
            dslContext.insertInto(
                JOURNAL_ENTRIES,
                JOURNAL_ENTRIES.JOURNAL_ID,
                JOURNAL_ENTRIES.SIDE,
                JOURNAL_ENTRIES.ACCOUNT_ID,
                JOURNAL_ENTRIES.VALUE
            )
                .values(it.journalId, it.side, it.accountId, it.value)
        }
        dslContext.batch(queries).execute()
    }
}
