package com.test.service;

import com.test.exception.ServiceException;
import com.test.model.ServerLogEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test for {@link ServerLogCacheServiceImpl}
 */
@ExtendWith(MockitoExtension.class)
class ServerLogCacheServiceTest {

    @InjectMocks
    ServerLogCacheServiceImpl serverLogCacheService;

    @Test
    @DisplayName("Test gets valid by its id")
    void testGivenValidIdWhenGetThenReturnsEntry() {
        // given
        ServerLogEntry serverLogEntry = new ServerLogEntry();
        serverLogEntry.setId("cached");
        serverLogCacheService.cache(serverLogEntry);

        // when
        ServerLogEntry existingServerLogEntry = serverLogCacheService.get(serverLogEntry.getId());

        // then
        assertNotNull(existingServerLogEntry);
        assertEquals(serverLogEntry.getId(), existingServerLogEntry.getId());
    }

    @Test
    @DisplayName("Test gets null by wrong id")
    void testGivenWrongIdWhenGetReturnsNull() {
        // given
        String id = "invalid id";

        // when
        ServerLogEntry serverLogEntry = serverLogCacheService.get(id);

        // then
        assertNull(serverLogEntry);
    }

    @Test
    @DisplayName("Test puts entry hasn't been cached")
    void testGivenNonCachedEntryWhenPutsThenSuccess() {
        // given
        ServerLogEntry serverLogEntry = new ServerLogEntry();

        // when
        serverLogCacheService.cache(serverLogEntry);

        // then
    }

    @Test
    @DisplayName("Test puts entry cached fails")
    void testGivenCachedEntryWhenPutsThenFails() {
        // given
        ServerLogEntry serverLogEntry = new ServerLogEntry();
        serverLogEntry.setId("cached");
        serverLogCacheService.cache(serverLogEntry);

        // when
        assertThrows(ServiceException.class, () -> serverLogCacheService.cache(serverLogEntry));
    }

    @Test
    @DisplayName("Test evicts entry")
    void testGivenEntryWhenEvictsThenSuccess() {
        // given
        ServerLogEntry serverLogEntry = new ServerLogEntry();
        serverLogEntry.setId("cached");
        serverLogCacheService.cache(serverLogEntry);

        // when
        serverLogCacheService.evict(serverLogEntry.getId());

        // then
        assertNull(serverLogCacheService.get(serverLogEntry.getId()));
    }

}