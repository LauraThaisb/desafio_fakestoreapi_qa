package br.com.fakestore.configuration;

public class Configuration {

    private ReadProperties readProperties;

    public Configuration() {
        readProperties = new ReadProperties();
    }

    public String getBaseURI() {
        return readProperties.getValue("base.uri");
    }

    public String getBasePath() {
        return readProperties.getValue("base.path");
    }


}
