import com.google.gson.annotations.SerializedName;

public class Holiday {
    @SerializedName("date")
    private String date;

    @SerializedName("localName")
    private String localName;

    @SerializedName("name")
    private String name;

    // Getters
    public String getDate() { return date; }
    public String getLocalName() { return localName; }
    public String getName() { return name; }
}