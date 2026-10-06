// Java
package org.springframework.samples.petclinic.vet;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VetControllerTest {

    private VetRepository vetRepository;
    private VetController vetController;

    private Vet helenLeary;
    private Vet sharonJenkins;
    private Vet jamesCarter;

    @Before
    void setUp() {
        vetRepository = mock(VetRepository.class);
        vetController = new VetController(vetRepository);

        helenLeary = createVet(1L, "Helen", "Leary");
        sharonJenkins = createVet(2L, "Sharon", "Jenkins");
        jamesCarter = createVet(3L, "James", "Carter");

        when(vetRepository.findAll()).thenReturn(List.of(helenLeary, sharonJenkins, jamesCarter));
    }

    @Test
    void searchVetsMatchesPartialFirstName() {
        Vets result = vetController.searchVets("hel");

        assertEquals(1, result.getVetList().size());
        assertSame(helenLeary, result.getVetList().get(0));
        verify(vetRepository).findAll();
    }

    @Test
    void searchVetsMatchesPartialLastName() {
        Vets result = vetController.searchVets("jen");

        assertEquals(1, result.getVetList().size());
        assertSame(sharonJenkins, result.getVetList().get(0));
        verify(vetRepository).findAll();
    }

    @Test
    void searchVetsIsCaseInsensitive() {
        Vets result = vetController.searchVets("HELEN");

        assertEquals(1, result.getVetList().size());
        assertSame(helenLeary, result.getVetList().get(0));
        verify(vetRepository).findAll();
    }

    @Test
    void searchVetsReturnsEmptyListWhenThereAreNoMatches() {
        Vets result = vetController.searchVets("unknown");

        assertEquals(0, result.getVetList().size());
        verify(vetRepository).findAll();
    }

    @Test
    void searchVetsReturnsAllVetsForEmptySearchTerm() {
        Vets result = vetController.searchVets("");

        assertEquals(3, result.getVetList().size());
        assertSame(helenLeary, result.getVetList().get(0));
        assertSame(sharonJenkins, result.getVetList().get(1));
        assertSame(jamesCarter, result.getVetList().get(2));
        verify(vetRepository).findAll();
    }

    private Vet createVet(long id, String firstName, String lastName) {
        Vet vet = new Vet();
        vet.setId(id);
        vet.setFirstName(firstName);
        vet.setLastName(lastName);
        return vet;
    }
}