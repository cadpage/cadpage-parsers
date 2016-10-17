package net.anei.cadpage.parsers.ZCABC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZCABCPrinceGeorgeParser extends FieldProgramParser { 
  public ZCABCPrinceGeorgeParser() {
    super(CITY_LIST,  "PRINCE GEORGE", "BC",
          "Date:DATETIME! Type:CALL! Address:ADDRCITY/S! Latitude:LAT Longitude:LONG Units_Responding:UNIT");
  }
  
  @Override
  public String getFilter() {
    return "donotreply@princegeorge.ca,donotreply@city.pg.bc.ca";
  }
 
  @Override 
  public boolean parseMsg(String subject, String body, Data data) {    
    if(!subject.equals("CAD Incident Message") && !subject.equals("Incident Message")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTime();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("LAT")) return new MyLatitudeField();
    if (name.equals("LONG")) return new MyLongitudeField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d) (\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTime extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime =match.group(4);
    }
  }
  
  private static final Pattern INITIAL_APT_PATTERN
    = Pattern.compile("#(\\d+) *- *(.*)");
  private static final Pattern DRAGON_LAKE_PATTERN
    = Pattern.compile("(.*?), *RED +BLUFF *\\/ *DRAGON +LAKE(?:, *BC)?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = INITIAL_APT_PATTERN.matcher(field);
      if (m.matches()) {
        data.strApt = m.group(1);
        field = m.group(2);
      }
      m = DRAGON_LAKE_PATTERN.matcher(field);
      if (m.matches()) {
        data.strPlace = append(data.strPlace, " - ", "RED BLUFF / DRAGON LAKE");
        field = m.group(1)+", QUESNEL";
      }
      super.parse(field.replace(", BC", ""), data);
    }
    
    @Override
    public String getFieldNames() {
      return append (super.getFieldNames(), " ", "PLACE");
    }
  }
  
  private class MyLatitudeField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      data.strGPSLoc = field;
    }
  }
  
  private class MyLongitudeField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      super.parse(append(data.strGPSLoc, ",", field), data);
    }
  }
  
  private static final String[] CITY_LIST = {
    "QUESNEL",
    "RED BLUFF"
  };
}
