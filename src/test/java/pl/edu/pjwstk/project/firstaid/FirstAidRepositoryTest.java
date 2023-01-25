package pl.edu.pjwstk.project.firstaid;

import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirstAidRepositoryTest {

    @Mock
    private FirstAidRepository firstAidRepository;

    @InjectMocks
    private FirstAidService firstAidService;

    @Test
    public void testSetFirstAidFromFile() {
        firstAidRepository.loadFirstAidFromFile();
        verify(firstAidRepository).loadFirstAidFromFile();
    }

    @Test
    public void testGetFirstAidContent() {
        JsonObject expectedContent = new JsonObject();
        when(firstAidRepository.getFirstAidContent()).thenReturn(expectedContent);

        JsonObject actualContent = firstAidRepository.getFirstAidContent();
        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAddFirstAidKitToFile() {
        FirstAidKitRequest request = new FirstAidKitRequest();
        request.setName("Apteczka przemysłowa");
        request.setDescription("Apteczka zawierająca zestaw materiałów opatrunkowych i innych elementów niezbędnych w udzielaniu pierwszej pomocy przygotowany specjalnie pod kątem biur, szkół, fabryk i zakładów przemysłowych.");
        request.setElements(Arrays.asList("plaster", "opaska elastyczna", "nożyczki", "kompres", "gaza", "chlorheksydyna", "środek odkażający", "chusta trójkątna", "termometr", "płyn do dezynfekcji", "latarka", "rękawice jednorazowe", "leki przeciwbólowe", "leki przeciwgorączkowe", "leki przeciwalergiczne", "leki przeciwskurczowe", "leki rozkurczowe", "leki przeciwhistaminowe", "leki na kaszel", "leki na ból brzucha", "leki na biegunkę", "leki przeciwhistaminowe", "leki przeciwbólowe", "leki uspokajające", "leki na zaparcia", "preparaty na odporność", "środki na oparzenia", "maść na ukąszenia"));

        when(firstAidRepository.addFirstAidKitToFile(request)).thenReturn(true);

        boolean result = firstAidRepository.addFirstAidKitToFile(request);
        assertTrue(result);
    }

    @Test
    public void testAddLifeSupportActionToFile() {
        LifeSupportActionRequest request = new LifeSupportActionRequest();
        request.setName("CPR");
        request.setDescription("Zespół czynności stosowanych w wypadku ustania czynności serca z utratą świadomości i bezdechem.");
        request.setElements(Arrays.asList("Oceń swoje bezpieczeństwo",
                "Sprawdź, czy poszkodowany reaguje",
                "Głośno wołaj o pomoc",
                "Udrożnij drogi oddechowe i sprawdź oddech – lekko odchyl głowę do tyłu i unieś żuchwę",
                "Sprawdź, czy oddycha prawidłowo",
                "Zadzwoń pod numer 112",
                "Wykonaj 30 uciśnięć klatki piersiowej",
                "Wykonaj 2 oddechy",
                "Kontynuuj resuscytację aż do przyjazdu pogotowia"));

        when(firstAidRepository.addLifeSupportActionToFile(request)).thenReturn(true);

        boolean result = firstAidRepository.addLifeSupportActionToFile(request);
        assertTrue(result);
    }

    @Test
    public void testRemoveLifeSupportActionFromFile() {
        String name = "CPR";
        when(firstAidRepository.removeLifeSupportActionFromFile(name)).thenReturn(true);

        boolean result = firstAidRepository.removeLifeSupportActionFromFile(name);
        assertTrue(result);
    }

    @Test
    public void testRemoveFirstAidKitFromFile() {
        String name = "Band-Aid";
        when(firstAidRepository.removeFirstAidKitFromFile(name)).thenReturn(true);

        boolean result = firstAidRepository.removeFirstAidKitFromFile(name);
        assertTrue(result);
    }
}
