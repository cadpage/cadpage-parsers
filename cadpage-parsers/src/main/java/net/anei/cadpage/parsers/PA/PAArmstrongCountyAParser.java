package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Armstrong County, PA
 */
public class PAArmstrongCountyAParser extends FieldProgramParser {

  // Marker is time and run number at end of message
  private static final Pattern MARKER_PATTERN1 = Pattern.compile(" +(\\d+) +(\\d\\d:\\d\\d)$");
  
  public PAArmstrongCountyAParser() {
    super(CITY_CODES, "ARMSTRONG COUNTY", "PA",
          "CALL PLACE ( ADDR2/ZiS GPS! | ADDR1 CITY! ) X+");
  }
  
  @Override
  public String getFilter() {
    return "911Dispatch@co.armstrong.pa.us,2183500107";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Someone is adding some clever post parsing that we need to undo
    int pt = body.indexOf(" Unformatted Message: ");
    if (pt >= 0) {
      subject = "Dispatch";
      body = body.substring(pt+22).trim();
      body = stripFieldEnd(body, " stop");
    }
    
    // There are two different formats we process.
    // First look for the current one
    if (!subject.equals("Dispatch")) return false;
    Matcher match = MARKER_PATTERN1.matcher(body);
    if (!match.find()) return false;
    data.strCallId = match.group(1);
    data.strTime = match.group(2);
    body = body.substring(0,match.start());
    
    if (!parseFields(body.split("//"), data)) return false;
    if (data.strAddress.length() == 0) {
      data.strAddress = data.strGPSLoc;
      data.strGPSLoc = "";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " ID TIME";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR1")) return new AddressField("(.+?) *(?:&|(?i)AND)", false);
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("GPS")) return new GPSField("-361 -361|(\\d+\\.\\d+ +-\\d+\\.\\d+)", true);
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private static final Pattern CALL_BRK_PTN = Pattern.compile(" *\n *");
  private static final Pattern CALL_CH_PLACE_PTN = Pattern.compile("(.*?) *\\b((?:EMS )?TAC ?\\d{1,2}|FIRE TAC ?\\d{1,2}(?: EMS TAC ?\\d{1,2})?|EMS ?\\d{1,2}|ET ?\\d{1,2}|CW ?\\d{1,2}|FIRE FT\\d{1,2}|FI?RE \\d{1,2}(?: EMS ?\\d{1,2})?)\\b *(.*?)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = CALL_BRK_PTN.matcher(field).replaceAll(" ");
      Matcher match = CALL_CH_PLACE_PTN.matcher(field);
      if (match.matches()) {
        field = append(match.group(1), " - ", match.group(3));
        data.strChannel = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL CH INFO";
    }
  }
  
  private static final Pattern CITY_BORO_PTN = Pattern.compile("(.*?) +BORO", Pattern.CASE_INSENSITIVE);
  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf('&');
      if (pt >= 0) {
        String city = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        Matcher match = CITY_BORO_PTN.matcher(city);
        if (match.matches())city = match.group(1);
        data.strCity = convertCodes(city, CITY_CODES);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_BORO_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "KITTG",     "KITTANNING",
      "KITTG TWP", "KITTANNING TWP"
  });
}
