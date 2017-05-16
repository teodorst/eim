package ro.pub.cs.systems.eim.colocviumodel2.model;

public class WeatherInformation {
    private String weather, humidity, city;
    private Double temperature, pressure;

    public WeatherInformation(String weather, String humidity, String city, Double temperature, Double pressure) {
        this.weather = weather;
        this.humidity = humidity;
        this.city = city;
        this.temperature = temperature;
        this.pressure = pressure;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getTemperature() {

        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public Double getPressure() {
        return pressure;
    }
}
