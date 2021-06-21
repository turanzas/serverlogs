package com.test.service;

import com.test.entity.ServerLogEntity;
import com.test.repository.ServerLogEntityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Test for {@link ServerLogEntityService}
 */
@ExtendWith(MockitoExtension.class)
class ServerLogEntityServiceTest {

    @InjectMocks
    ServerLogEntityServiceImpl serverLogEntityService;

    @Mock
    ServerLogEntityRepository serverLogEntityRepository;

    @Test
    @DisplayName("Test a new entity is successfully saved")
    void testGivenNewEntityWhenSaveThenSuccess() {
        // given
        ServerLogEntity serverLogEntity = new ServerLogEntity();
        serverLogEntity.setId("not already saved");

        // when
        when(this.serverLogEntityRepository.exists(anyString())).thenReturn(false);
        this.serverLogEntityService.save(serverLogEntity);

        // then
        verify(this.serverLogEntityRepository).exists(anyString());
        verify(this.serverLogEntityRepository).insert(any());
        verifyNoMoreInteractions(this.serverLogEntityRepository);
    }

    @Test
    @DisplayName("Test an existing entity is ignored")
    void testGivenExistingEntityWhenSaveThenIgnores() {
        // given
        ServerLogEntity serverLogEntity = new ServerLogEntity();
        serverLogEntity.setId("already saved");

        // when
        when(this.serverLogEntityRepository.exists(anyString())).thenReturn(true);
        this.serverLogEntityService.save(serverLogEntity);

        // then
        verify(this.serverLogEntityRepository).exists(anyString());
        verifyNoMoreInteractions(this.serverLogEntityRepository);
    }

}