package ru.netology.i18n;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.entity.Country.*;

public class LocalizationServiceImplTest {

    @ParameterizedTest
    @EnumSource(Country.class)
    public void LocalizationServiceImplTest_russia_test(Country country) {
        LocalizationServiceImpl localizationService = new LocalizationServiceImpl();
        LocalizationServiceImpl localizationServiceMock = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationServiceMock.locale(country)).thenAnswer(
                new Answer() {
                    public Object answer(InvocationOnMock invocation) {
                        Object[] args = invocation.getArguments();
                        Country someCountry = (Country) args[0];
                        switch (someCountry) {
                            case RUSSIA:
                                return "Добро пожаловать";
                            default:
                                return "Welcome";
                        }
                    }
                });

    String actual = localizationService.locale(country);
    String expected = localizationServiceMock.locale(country);

    assertEquals(expected,actual);

}
}