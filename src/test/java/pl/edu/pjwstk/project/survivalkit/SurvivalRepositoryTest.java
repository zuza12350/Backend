package pl.edu.pjwstk.project.survivalkit;

import com.google.gson.JsonArray;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class SurvivalRepositoryTest {

    @Mock
    private SurvivalRepository survivalRepository;

    @Test
    public void testSetSurvivalKitFromFile() {
        survivalRepository.setSurvivalKitFromFile();
        verify(survivalRepository).setSurvivalKitFromFile();
    }

    @Test
    public void testAddSurvivalTip() {
        SurvivalKitRequest request = new SurvivalKitRequest();
        request.setName("Zestaw podróżny");
        request.setGuide("Zestaw przeznaczony do zabrania podczas podróży samochodem, samolotem lub innym środkiem transportu.");

        survivalRepository.addSurvivalTip(request);
        verify(survivalRepository).addSurvivalTip(request);
    }

    @Test
    public void testRemoveSurvivalTip() {
        String name = "Zestaw podróżny";
        survivalRepository.removeSurvivalTip(name);
        verify(survivalRepository).removeSurvivalTip(name);
    }

    @Test
    public void testGetSurvivalData() {
        JsonArray expectedData = new JsonArray();
        when(survivalRepository.getSurvivalTips()).thenReturn(expectedData);

        JsonArray actualData = survivalRepository.getSurvivalTips();
        assertEquals(expectedData, actualData);
    }

}
