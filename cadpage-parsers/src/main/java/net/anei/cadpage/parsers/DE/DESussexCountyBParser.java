package net.anei.cadpage.parsers.DE;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DESussexCountyBParser extends FieldProgramParser {
  
  private static final Pattern ALT_START_SEQ = Pattern.compile("^Sta: +Inc#: +([-\\d]+) +(.*)");
  private  static final Pattern DELIM = Pattern.compile("(?=(?:Sta|Call at|Loc|City|Problem|Inc#|Lat|Long|DISP|Cross St):)");
  
  public DESussexCountyBParser() {
    super(CITY_CODES, "SUSEX COUNTY", "DE",
           "Inc0:ID? Sta:SRC! Call_at:ADDR! Loc:PLACE! City:CITY! Problem:CALL! Inc#:ID? Lat:GPS? Long:GPS? DISP:TIME Cross_St:X");
  }
  
  @Override
  public String getFilter() {
    return "@c-msg.net,cad@sussexcountyde.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // CodeMessaging does some data massaging.  Which we will try to reverse
    if(body.startsWith("Sta: ")) {
      body = body.replace(" Loc::", " Call at:").replace(" ---:", " Problem:").replace(" City: :", " City:");
    } else {
      body = "Sta: " + body;
    }
    
    body = ALT_START_SEQ.matcher(body).replaceAll("Inc0: $1 Sta: $2");
    body = body.replace(" Loc::", " Loc0:").replace("City: :", "City:");
    if (!super.parseFields(DELIM.split(body), data)) return false;
    setGPSLoc(data.strGPSLoc, data);
    return true;
  }
  
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("0 ")) field = field.substring(2).trim();
      super.parse(field, data);
    }
  }
  
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      int len = field.length();
      if (len >= 6) {
        field = field.substring(0,len-6) + '.' + field.substring(len-6);
      }
      data.strGPSLoc = append(data.strGPSLoc, " ", field);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "19330", "BETHANY BEACH",
      "19331", "BETHEL",
      "19333", "BRIDGEVILLE",
      "19339", "DAGSBORO",
      "19933", "BRIDGEVILLE",
      "19940", "DELMAR",
      "19941", "ELLENDALE",
      "19944", "FENWICK ISLAND",
      "19945", "FRANKFORD",
      "19947", "GEORGETOWN",
      "19950", "GREENWOOD",
      "19951", "HARBESON",
      "19952", "HARRINGTON",
      "19956", "LAUREL",
      "19958", "LEWES",
      "19960", "LINCOLN",
      "19963", "MILFORD",
      "19966", "MILLSBORO",
      "19967", "MILLVILLE",
      "19968", "MILTON",
      "19969", "NASSAU",
      "19970", "OCEAN VIEW",
      "19971", "REHOBOTH BEACH",
      "19973", "SEAFORD",
      "19975", "SELBYVILLE"
  });
}


