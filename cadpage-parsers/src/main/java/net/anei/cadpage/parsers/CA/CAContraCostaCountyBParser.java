package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CAContraCostaCountyBParser extends FieldProgramParser {

  public CAContraCostaCountyBParser() {
    super(CITY_CODES, "CONTRA COSTA COUNTY", "CA", 
          "CALL! ALARM:PRI! LOC:ADDR/S? XST1:X! XST2:X! MAP:MAP! TIME:TIME! EMD:CODE! PLACE:CITY! LAT:GPS1! LONG:GPS2! STA:SRC!");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //check subject
    if (!subject.equals("IPS I/Page Notification")) return false;
   
    //remove trailing "/CAD iPage"
    if (!body.endsWith("/CAD iPage")) return false;
    body = body.substring(0, body.length()-10).trim();
      
    //return false if parse fails
    if (!super.parseMsg(body, data)) return false;
    
    //if ADDR empty, substitute and remove CROSS
    if (data.strAddress.length() == 0) {
      parseAddress(data.strCross, data);
      data.strCross = "";
    }
    
    //yay we made it
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("MAP")) return new MapField("[A-Z]\\d{2}[A-Z] \\d{4} \\d{3}|", true);
    if (name.equals("TIME")) return new TimeField("\\d{2}:\\d{2}:\\d{2}", true);
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("GPS1")) return new GPSField(1, "(?:-?\\d{1,3}(?:\\.\\d+))?", true); //can be empty string
    if (name.equals("GPS2")) return new GPSField(2, "(?:-?\\d{1,3}(?:\\.\\d+))?", true);
    return super.getField(name);
  }
  
  private static Pattern ADDR_APT = Pattern.compile("(.*?)[:,](?:APT|STE|RM|ROOM|) *(.+?)", Pattern.CASE_INSENSITIVE);
  public class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      boolean first = true;
      for (String part : field.split(": @")) {
        part = part.trim();
        String apt = "";
        Matcher mat = ADDR_APT.matcher(part);
        if (mat.matches()) {
          part = mat.group(1).trim();
          apt = mat.group(2);
        }
        if (first) {
          super.parse(part, data);
          first = false;
        } else {
          data.strPlace = append(data.strPlace," - ", part);
        }
        data.strApt = append(data.strApt, "-", apt);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  //enter ADDR in X's field names in case optional ADDR? field in the program string isn't picked up
  public class MyCrossField extends CrossField {
    @Override
    public String getFieldNames() {
      return "ADDR " + super.getFieldNames();
    }
  }
  
  //ignore "default" codes
  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      if (!field.equals("default")) super.parse(field, data);
    }
  }
  
  
  //ignore empty cities
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() > 0) super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "ALCO", "ALAMEDA COUNTY",
      "CTY",  "CONTRA COSTA COUNTY",
      "DAN",  "DANVILLE",
      "SR",   "SAN RAMON",
    });

}
