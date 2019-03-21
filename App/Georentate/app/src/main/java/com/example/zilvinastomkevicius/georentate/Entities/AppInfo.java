package com.example.zilvinastomkevicius.georentate.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class AppInfo {

    @JsonProperty("PrivacyPolicy")
    public String PrivacyPolicy;
    @JsonProperty("About")
    public String About;
    @JsonProperty("Conditions")
    public String Conditions;
    @JsonProperty("Version")
    public String Version;
    @JsonProperty("FbLink")
    public String FBLink;
    @JsonProperty("WebLink")
    public String WebLink;
}
