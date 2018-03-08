package net.anei.cadpage.parsers.PA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PALackawannaCountyBParser extends FieldProgramParser {
  
  public PALackawannaCountyBParser() {
    super(CITY_CODES, "LACKAWANNA COUNTY", "PA", 
          "Assigned_Units:UNIT! Call_Type:CALL! Radio_Channel:CH! Address:ADDRCITY/S6! Muni:CITY! Common_Name:PLACE! LAT/LON:GPS! Closest_Intersection:X! Call_Time:DATETIME! Nature_of_Call:CALL/SDS! Primary_Incident:ID!");
  }
  
  @Override
  public String getFilter() {
    return "aegispage@lackawannacounty.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    body = body.replace(" Muni:", "\nMuni:");
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "2",  "South Abington Twp",
      "4",  "Clarks Summit",
      "6",  "Dunmore",
      "7",  "Moscow",
      "8",  "Newton Twp",
      "14", "Covington Twp",
      "15", "Roaring Brook Twp",
      "16", "Elmhurst",
      "18", "Clifford Twp",
      "21", "Archbald",
      "23", "Dickson Twp",
      "24", "Greenfield Twp",
      "25", "Jessup",
      "26", "Olyphant",
      "27", "Throop",
      "28", "Scott Twp",
      "29", "Jefferson Twp",
      "33", "Eynon",
      "41", "Forest City",
      "50", "Scranton",
      "51", "Carbondale",
      "52", "Ransom Twp",
      "53", "Springbrook Twp",
      "54", "Thornhurst",
      "60", "Carbondale Twp",
      "61", "Fell/Simpson",
      "64", "North Abington Twp",
      "65", "West Abington Twp",
      "66", "Laplume",
      "93", "Old Forge",
      "94", "Moosic",
      "95", "Taylor",
  });
}
