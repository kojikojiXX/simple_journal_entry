/*
 * This file is generated by jOOQ.
 */
package com.okeicalm.simpleJournalEntry.infra.db.indexes


import com.okeicalm.simpleJournalEntry.infra.db.tables.JournalEntries
import com.okeicalm.simpleJournalEntry.infra.db.tables.Journals

import org.jooq.Index
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// INDEX definitions
// -------------------------------------------------------------------------

val JOURNAL_ENTRIES_FK_ACCOUNT: Index = Internal.createIndex(DSL.name("fk_account"), JournalEntries.JOURNAL_ENTRIES, arrayOf(JournalEntries.JOURNAL_ENTRIES.ACCOUNT_ID), false)
val JOURNAL_ENTRIES_FK_JOURNAL: Index = Internal.createIndex(DSL.name("fk_journal"), JournalEntries.JOURNAL_ENTRIES, arrayOf(JournalEntries.JOURNAL_ENTRIES.JOURNAL_ID), false)
val JOURNALS_FK_USER: Index = Internal.createIndex(DSL.name("fk_user"), Journals.JOURNALS, arrayOf(Journals.JOURNALS.USER_ID), false)
