package net.anei.cadpage.parsers.DE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Kent County, DE
 */
public class DEKentCountyCParser extends DEKentCountyBaseParser {
  
  public DEKentCountyCParser() {
    super("KENT COUNTY", "DE",
          "Unit:UNIT? Status:ADDR/SCP! Venue:CITY! Dev/Sub:PLACE! Xst's:X Caller:NAME Lat:GPS1 Long:GPS2");
  }
  
  @Override
  public String getFilter() {
    return "kentcenter@state.de.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "- ");
    if (!body.startsWith("Unit:") && !body.startsWith("Status:")) {
      body = "Status: Dispatched " + body;
    }
    return super.parseMsg(body, data);
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }

  private static final Pattern STATUS_PTN = Pattern.compile("^(?:Dispatched|Enroute|At Scene) +");
  private static final Pattern CODE_PTN = Pattern.compile("^(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?) (?:- )?");
  private static final Pattern ADDR_SPLIT_PTN = Pattern.compile(" *: *");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      Matcher match = STATUS_PTN.matcher(field);
      if (!match.lookingAt()) abort();
      field = field.substring(match.end());
      
      match = CODE_PTN.matcher(field);
      if (match.find()) {
        data.strCode = match.group(1);
        field = field.substring(match.end()).trim();
      }
      
      String[] parts = ADDR_SPLIT_PTN.split(field, -1);
      if (parts.length > 3) abort();
      if (parts.length >=2) {
        data.strCall = parts[0];
        parseAddress(parts[1], data);
        if (parts.length == 3) data.strPlace = parts[2];
      }
      else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL ADDR APT PLACE";
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      adjustCityState(data);
    }
    
    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      data.strPlace = append(data.strPlace, " - ", field);
    }
  }
}


