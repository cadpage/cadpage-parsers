package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class FLLeeCountyCParser extends FieldProgramParser {
  
  public FLLeeCountyCParser() {
    super("LEE COUNTY", "FL", 
          "INC#:ID TYP:CALL! PRI:PRI! LOC:ADDR! ( TIME/DATE:DATETIME! | TIME:TIME! ) Comments:INFO Disp:UNIT%");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean mixedMsgOrder() { return true; }
    };
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (body.length() > 1000 & data.strUnit.length() == 0) data.expectMore = true;
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_SPLIT_PTN = Pattern.compile("(.*)([,;:])(.*)");
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*?) +([A-Z_]{4,})");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      String apt = "";
      while (true) {
        Matcher match = ADDR_SPLIT_PTN.matcher(field);
        if (!match.matches()) break;
        field = match.group(1);
        String typ = match.group(2);
        String part = match.group(3).trim();
        if (typ.equals(":")) {
          data.strPlace = append(stripFieldStart(part, "@"), " - ", data.strPlace);
        } else {
          if (!apt.equals(part)) apt = append(part, "-", apt);
        }
      }
      
      Matcher match = ADDR_CITY_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strCity = match.group(2).replace('_', ' ').trim();
        if (data.strCity.equalsIgnoreCase("COLLI")) data.strCity = "COLLIER COUNTY";
      }
      
      field = stripFieldEnd(field, data.strPlace);
      
      parseAddress(StartType.START_ADDR, field, data);
      String place = getLeft();
      if (place.length() > 0 && !data.strPlace.contains(place)) {
        data.strPlace = append(place, " - ", data.strPlace);
      }
      if (!apt.equals(data.strApt)) data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY APT PLACE";
    }
  }
  
  private static final Pattern INFO_PH_GPS_PTN = Pattern.compile("ALT#(\\d{3}-\\d{3}-\\d{4}) (\\d{2})(\\d{6}) (\\d{2})(\\d{6})\\b *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PH_GPS_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strPhone = match.group(1);
        setGPSLoc(match.group(2)+'.'+match.group(3)+','+match.group(4)+'.'+match.group(5), data);
        field = field.substring(match.end());
      } else {
        field = stripFieldStart(field, "ALT# - -");
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PHONE GPS " + super.getFieldNames();
    }
  }
}
