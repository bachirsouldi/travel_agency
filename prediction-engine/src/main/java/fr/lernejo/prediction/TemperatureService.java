package fr.lernejo.prediction;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TemperatureService {

    private final Map<CaseInsensitiveString, TemperatureGenerationData> temperatureDatasByCountry;
    private final Random random = new Random();

    TemperatureService() {
        Stream<String> lines = new ClassPathFileLoader().readLines("countriesTempData.csv");

        temperatureDatasByCountry = lines
            .skip(1)
            .map(TemperatureGenerationData::parseCsv)
            .collect(Collectors.toMap(v -> new CaseInsensitiveString(v.country()), Function.identity()));
    }

    public double getTemperature(String country) throws UnknownCountryException {
        TemperatureGenerationData data = temperatureDatasByCountry.get(new CaseInsensitiveString(country));
        if (data == null) {
            throw new UnknownCountryException(country);
        }
        return generateBelievableTemperature(data);
    }

    private double generateBelievableTemperature(TemperatureGenerationData data) {
        DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));
        int avg = data.avg();
        double delta = ((double) random.nextInt(data.variance() * 2 * 100)) / 100 - data.variance();
        return Double.parseDouble(df.format(avg + delta));
    }

    record TemperatureGenerationData(String country, int avg, int variance) {
        public static TemperatureGenerationData parseCsv(String csvLine) {
            String[] split = csvLine.split(";");
            return new TemperatureGenerationData(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        }
    }

    record CaseInsensitiveString(String value) {
        @Override
        public int hashCode() {
            return value == null ? 0 : Objects.hash(value.toLowerCase(Locale.ROOT));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CaseInsensitiveString that = (CaseInsensitiveString) o;
            return Objects.equals(value.toLowerCase(Locale.ROOT), that.value.toLowerCase(Locale.ROOT));
        }
    }
}
