package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHGeaugaCountyBParser extends FieldProgramParser {
 
  public OHGeaugaCountyBParser() {
    super("GEAUGA COUNTY", "OH",
          "SRC! Addr:ADDR! Type:CALL_INFO!");
  }
  
  @Override
  public String getFilter() {
    return "OH_GC_ENS@CO.GEAUGA.OH.US,CAD@co.geauga.oh.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    body = stripFieldStart(body, "CAD:");
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{4}", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL_INFO")) return new MyCallInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(?:Apt|Rm|Room|Lot|Suite) *(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      String apt = "";
      while (true) {
        int pt = field.lastIndexOf(';');
        if (pt < 0) break;
        String tmp = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        if (data.strCity.length() == 0) {
          String city = cleanCityName(tmp);
          if (city != null) {
            data.strCity = city;
            continue;
          }
        }
        Matcher match = ADDR_APT_PTN.matcher(tmp);
        if (match.matches()) {
          apt = append(match.group(1), "-", apt);
          continue;
        }
        data.strPlace = append(tmp, "-", data.strPlace);
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE APT CITY";
    }
  }
  
  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *\\d\\d:\\d\\d:\\d\\d \\d\\d/\\d\\d/\\d{4} - (?:[A-Za-z' ]+(?: (?:BB|CHPD|GCSO|SO)|\\.\\.\\.))? *");
  private static final Pattern CALL_INFO_PTN = Pattern.compile("([-A-Za-z0-9 ]+?) - "); 
  private class MyCallInfoField extends Field {

    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n");
      Matcher match = CALL_INFO_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strCall = match.group(1).trim();
        data.strSupp = field.substring(match.end()).trim();
      } else {
        int pt = field.indexOf('\n');
        if (pt >= 0) {
          data.strCall = stripFieldEnd(field.substring(0,pt).trim(), "-");
          data.strSupp = field.substring(pt+1).trim();
        } else {
          data.strCall = stripFieldEnd(field, "-");
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "CALL INFO";
    }
    
  }

  private String cleanCityName(String city) {
    
    // Clean up extraneous prefixs and suffixs
    city = stripFieldStart(city, "City of ");
    city = stripFieldStart(city, "City Of ");
    city = stripFieldEnd(city, " #1");
    city = stripFieldEnd(city, " #2");
    city = stripFieldEnd(city, " Village");
    
    // Now comes the hard part. 
    // Checking to see if this is a truncated city that needs
    // to be expanded.
    String result = null;
    for (String tCity : CITY_LIST) {
      if (city.equals(tCity)) return city;
      if (tCity.startsWith(city)) {
        if (result == null) {
          result = tCity;
        } else {
          if (!tCity.equals(result + " Twp")) {
            result = "";
            break;
          }
        }
      }
    }
    
    return result;
  }
  
  private static final String[] CITY_LIST = new String[]{
    // Cities
    "Aquilla",
    "Burton",
    "Chardon",
    "Hunting Valley",
    "Middlefield",
    "South Russell",

    // Townships
    "Auburn Twp",
    "Bainbridge Twp",
    "Burton Twp",
    "Chardon Twp",
    "Chester Twp",
    "Claridon Twp",
    "Hambden Twp",
    "Huntsburg Twp",
    "Middlefield Twp",
    "Montville Twp",
    "Munson Twp",
    "Newbury Twp",
    "Parkman Twp",
    "Russell Twp",
    "Thompson Twp",
    "Troy Twp",

    // Census-designated places
    "Bainbridge",
    "Chesterland",

    // Other localities
    "East Claridon",
    "Materials Park",
    "Montville",
    "Parkman",
    "Novelty",
    "Welshfield"
  };
}
