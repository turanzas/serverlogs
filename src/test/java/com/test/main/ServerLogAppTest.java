package com.test.main;

import com.test.exception.ServiceException;
import com.test.service.ServerLogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Test for {@link ServerLogApp}
 */
@ExtendWith(MockitoExtension.class)
class ServerLogAppTest {

    @InjectMocks
    ServerLogApp serverLogApp;

    @Mock
    ServerLogService serverLogService;

    @Mock
    Logger logger;

    @Test
    @DisplayName("Test when no path is given app fails")
    void testGivenNoArgumentsWhenAppRunsThenFails() {
        // given
        String[] args = new String[0];

        // when
        serverLogApp.run(args);

        // then
        verify(logger).error(anyString(), (Throwable) any());
    }

    @Test
    @DisplayName("Test when a path is given the app runs")
    void testGivenValidPathWhenAppRunsThenSuccess() {
        // given
        String[] args = { "/path/" };

        // when
        serverLogApp.run(args);

        // then
        verify(this.serverLogService).processFile(anyString());
        verifyNoMoreInteractions(this.serverLogService);
    }

}