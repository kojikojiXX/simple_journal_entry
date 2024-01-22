package com.okeicalm.simpleJournalEntry.handler.dataloader

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.execution.KotlinDataLoader
import com.okeicalm.simpleJournalEntry.handler.type.JournalType
import com.okeicalm.simpleJournalEntry.repository.JournalRepository
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class JournalsDataLoader(val repository: JournalRepository) : KotlinDataLoader<ID, List<JournalType>> {
    override val dataLoaderName = "JournalsDataLoader"
    override fun getDataLoader(): DataLoader<ID, List<JournalType>> = DataLoaderFactory.newDataLoader { ids ->
        CompletableFuture.supplyAsync {
            ids.map { id ->
                repository
                    .findByUserId(id.toString().toLong())
                    .map { JournalType(it) }
            }
        }
    }
}
