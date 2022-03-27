package io.opencodehub.covidtracker.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class LocationStats
{
    private String country;
    private String state;
    private int latestTotalCases;
    private int newCases;

    @Override public String toString ()
    {
        return "LocationStats{" + "country='" + country + '\'' + ", state='" + state
            + '\'' + ", latestTotalCases=" + latestTotalCases + ", previousDayTotalCases="
            + newCases + '}';
    }

    @Override public boolean equals (Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LocationStats stats = (LocationStats)o;
        return country.equals(stats.country) && state.equals(stats.state);
    }

    @Override public int hashCode ()
    {
        return Objects.hash(country, state);
    }
}
