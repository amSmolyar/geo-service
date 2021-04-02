package ru.netology.sender;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static ru.netology.entity.Country.*;

public class MessageSenderImplTest {

    // 1. Проверка, что MessageSenderImpl всегда отправляет только русский текст,
    // если ip относится к российскому сегменту адресов
    public static Stream<String> getRusIp() {
        Random random = new Random();
        String[] ip = new String[99];
        for (int ii = 0; ii < ip.length; ii++) {
            ip[ii] = "172" +
                    "." + String.valueOf(random.nextInt(255)) +
                    "." + String.valueOf(random.nextInt(255)) +
                    "." + String.valueOf(random.nextInt(255));
        }
        return Arrays.stream(ip);
    }

    @ParameterizedTest
    @MethodSource("getRusIp")
    public void MessageSenderImpl_russianSegment_test(String ip) {
        Map<String, String> headers = new HashMap<>();

        GeoServiceImpl geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp("172." + anyString()))
                .thenReturn(new Location("Moscow", RUSSIA, null, 0));

        LocalizationServiceImpl localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(RUSSIA)).thenReturn("Добро пожаловать");

        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

        String actual = messageSender.send(headers);
        String expected = "Добро пожаловать";

        assertEquals(actual, expected);
    }

    // 2. Проверка, что MessageSenderImpl всегда отправляет только английский текст,
    // если ip относится к американскому сегменту адресов
    public static Stream<String> getUSAIp() {
        Random random = new Random();
        String[] ip = new String[99];
        for (int ii = 0; ii < ip.length; ii++) {
            ip[ii] = "96" +
                    "." + String.valueOf(random.nextInt(255)) +
                    "." + String.valueOf(random.nextInt(255)) +
                    "." + String.valueOf(random.nextInt(255));
        }
        return Arrays.stream(ip);
    }

    @ParameterizedTest
    @MethodSource("getUSAIp")
    public void MessageSenderImpl_englishSegment_test(String ip) {
        Map<String, String> headers = new HashMap<>();

        GeoServiceImpl geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp("96." + anyString()))
                .thenReturn(new Location("New York", Country.USA, null,  0));

        LocalizationServiceImpl localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(USA)).thenReturn("Welcome");

        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

        String actual = messageSender.send(headers);
        String expected = "Welcome";

        assertEquals(actual, expected);
    }


}