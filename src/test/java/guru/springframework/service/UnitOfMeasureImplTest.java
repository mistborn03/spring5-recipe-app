package guru.springframework.service;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitOfMeasureImplTest {

  UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand =
      new UnitOfMeasureToUnitOfMeasureCommand();
  UnitOfMeasureServiceImpl service;

  @Mock UnitOfMeasureRepository unitOfMeasureRepository;

  @BeforeEach
  void setUp() {
    service =
        new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand);
  }

  @Test
  void listAllUoms() throws Exception {
    Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
    UnitOfMeasure uom1 = new UnitOfMeasure();
    uom1.setId(1L);
    unitOfMeasures.add(uom1);

    UnitOfMeasure uom2 = new UnitOfMeasure();
    uom2.setId(2L);
    unitOfMeasures.add(uom2);

    when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);

    Set<UnitOfMeasureCommand> commands = service.listAllUoms();

    assertEquals(2, commands.size());
    verify(unitOfMeasureRepository, times(1)).findAll();
  }
}
