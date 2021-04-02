package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.startsWith;
import static ru.netology.entity.Country.*;
import static ru.netology.entity.Country.RUSSIA;

public class GeoServiceImplTest {

    @ParameterizedTest
    @ValueSource(strings = {GeoServiceImpl.LOCALHOST,
            GeoServiceImpl.MOSCOW_IP,
            GeoServiceImpl.NEW_YORK_IP,
            "172.34.23.76",
            "172.174.74.2",
            "96.234.23.13",
            "96.54.239.3"})
    public void GeoServiceImplTest_byIp_notNull_test(String ip) {
        GeoServiceImpl geoService = new GeoServiceImpl();

        GeoServiceImpl geoServiceMock = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoServiceMock.byIp(anyString()))
                .thenAnswer(
                        new Answer() {
                            public Object answer(InvocationOnMock invocation) {
                                Object[] args = invocation.getArguments();
                                String ip = (String) args[0];
                                switch (ip) {
                                    case GeoServiceImpl.LOCALHOST:
                                        return new Location(null, null, null, 0);
                                    case GeoServiceImpl.MOSCOW_IP:
                                        return new Location("Moscow", RUSSIA, "Lenina", 15);
                                    case GeoServiceImpl.NEW_YORK_IP:
                                        return new Location("New York", USA, " 10th Avenue", 32);
                                    default:
                                        if (ip.startsWith("172."))
                                            return new Location("Moscow", RUSSIA, null, 0);
                                        else if (ip.startsWith("96."))
                                            return new Location("New York", Country.USA, null, 0);
                                        else
                                            return null;
                                }
                            }
                        });

        Country expected = geoServiceMock.byIp(ip).getCountry();
        Country actual = geoService.byIp(ip).getCountry();

        assertEquals(actual, expected);
    }

}