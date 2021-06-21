package com.test.service;

import com.test.model.ServerLogEntry;
import com.test.model.State;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Test for {@link ServerLogEntryService}
 */
@ExtendWith(MockitoExtension.class)
class ServerLogEntryServiceTest {

    @InjectMocks
    ServerLogEntryServiceImpl serverLogEntryService;

    @Mock
    ServerLogCacheService serverLogCacheService;

    @Mock
    ServerLogEntityService serverLogEntityService;

    @Mock
    Logger logger;

    @Test
    @DisplayName("Test blank log line")
    void testGivenBlankLineWhenParsedThenLogsError() {
        // given
        String line = "";

        // when
        serverLogEntryService.process(line);

        // then
        verify(logger).error(anyString(), (Throwable) any());
        verifyNoMoreInteractions(this.serverLogCacheService, this.serverLogEntityService);
    }

    @Test
    @DisplayName("Test invalid log line")
    void testGivenInvalidLineWhenParsedThenLogsError() {
        // given
        String line = "invalid log line";

        // when
        serverLogEntryService.process(line);

        // then
        verify(logger).error(anyString(), (Throwable) any());
        verifyNoMoreInteractions(this.serverLogCacheService, this.serverLogEntityService);
    }

    @Test
    @DisplayName("Test valid log line is parsed and cached")
    void testGivenValidLineWhenProcessThenSuccess() {
        // given
        String line = "{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\", \"host\":\"12345\", \"timestamp\":1491377495212}";

        // when
        when(this.serverLogCacheService.get(anyString())).thenReturn(null);
        serverLogEntryService.process(line);

        // then
        verify(this.serverLogCacheService).get(anyString());
        verify(this.serverLogCacheService).cache(any());
        verifyNoMoreInteractions(this.serverLogCacheService, this.serverLogEntityService);
    }

    @Test
    @DisplayName("Test valid already cached log line is parsed, computed and saved")
    void testGivenCachedLineWhenProcessThenSuccess() {
        // given
        ServerLogEntry started = new ServerLogEntry();
        started.setId("scsmbstgra");
        started.setState(State.STARTED);
        started.setType("APPLICATION_LOG");
        started.setHost("12345");
        started.setTimestamp(1491377495212L);
        String finished = "{\"id\":\"scsmbstgra\", \"state\":\"FINISHED\", \"type\":\"APPLICATION_LOG\", \"host\":\"12345\", \"timestamp\":1491377495217}";

        // when
        when(this.serverLogCacheService.get(anyString())).thenReturn(started);
        serverLogEntryService.process(finished);

        // then
        verify(this.serverLogCacheService).get(anyString());
        verify(this.serverLogCacheService).evict(any());
        verify(this.serverLogEntityService).save(any());
        verifyNoMoreInteractions(this.serverLogCacheService, this.serverLogEntityService);
    }

}