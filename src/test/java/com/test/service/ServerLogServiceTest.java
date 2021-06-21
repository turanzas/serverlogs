package com.test.service;

import com.test.exception.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Test for {@link ServerLogService}
 */
@ExtendWith(MockitoExtension.class)
class ServerLogServiceTest {

    @InjectMocks
    ServerLogServiceImpl serverLogService;

    @Mock
    ServerLogEntryService serverLogEntryService;

    @Test
    @DisplayName("Invalid path throws exception")
    void testGivenInvalidPathWhenProcessThenFails() {
        // when
        String invalidPath = "/fake/";

        // then
        assertThrows(ServiceException.class, () -> serverLogService.processFile(invalidPath));
    }

    @Test
    @DisplayName("Valid path process lines")
    void testGivenValidPathWhenProcessThenSuccess() {
        // when
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(ServerLogServiceImpl.LOG_FILE_NAME).getFile());
        String path = file.getParent();

        // act
        serverLogService.processFile(path);

        // then
        verify(this.serverLogEntryService, times(6)).process(anyString());
        verifyNoMoreInteractions(this.serverLogEntryService);
    }

}