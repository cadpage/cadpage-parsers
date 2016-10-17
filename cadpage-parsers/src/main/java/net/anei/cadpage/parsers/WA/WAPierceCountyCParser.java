package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WAPierceCountyCParser extends FieldProgramParser {

  public WAPierceCountyCParser() {
    super(CITY_LIST, "PIERCE COUNTY", "WA", 
         "CALL ADDR UNIT DATETIME! INFO/N+");
  }

  public boolean parseMsg(String subject, String body, Data data) {
    //check subject
    if (!subject.equals("Notification")) return false;
    
    //parse fields
    boolean b = parseFields(body.split("\n"), data);
    return b;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME"))  return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern JUNK_PTN = Pattern.compile(" *\\(\\d+\\.\\d+\\)(?=,|$)");
  private static final Pattern ZIP_PTN = Pattern.compile("\\d{5}");
  private static final Pattern NEAR_PTN = Pattern.compile("(.*?);? *\\b(Near:.*)");

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      // We are not sure what the long decimal number in parens is, but things
      // go better if we get rid if it up front
      field = JUNK_PTN.matcher(field).replaceAll("");
      
      //  Strip trailing cross street
      if (field.endsWith(")")) {
        int pt =  field.lastIndexOf('(');
        String cross = field.substring(pt+1, field.length()-1).trim();
        field = field.substring(0,pt).trim();
        
        Matcher mat = NEAR_PTN.matcher(cross);
        if (mat.matches()) {
          cross = mat.group(1).trim();
          data.strPlace = mat.group(2);
        }
        
        data.strCross = cross;
      }
      
      // Start working from tail end of address breaking 
      // pieces separated by semicolons
      Parser p = new Parser(field);
      String part;
      while (true) {
        part = p.getLast(',');
        
        // If zip code, and we do not have a city yet, save in city
        if (ZIP_PTN.matcher(part).matches()) {
          if (data.strCity.length() == 0) data.strCity = part;
          continue;
        }
        
        // If state, just ignore it
        if (part.equalsIgnoreCase("WA")) continue;
        
        // If recognized city, save in city field
        if (isCity(part)) {
          data.strCity = part;
          continue;
        }
        
        // Otherwise break out of loop
        break;
      }
      
      // See what we have left
      String addr = p.get();
      if (addr.length() == 0) {
        addr = part;
      }
      else if (data.strCross.length() == 0) {
        data.strCross = part;
      }
      else {
        data.strAddress = append(addr, ", ", part);
      }
      
      // If address starts with @, it is a place name
      // and the real address ended up in the cross street
      if (addr.startsWith("@")) {
        data.strPlace = append(addr.substring(1).trim(), "; ", data.strPlace);
        addr = data.strCross;
        data.strCross = "";
      }
      
      // and parse final address with or without city 
      int flags = FLAG_ANCHOR_END;
      if (data.strCity.length() > 0) flags |= FLAG_NO_CITY;
      parseAddress(StartType.START_ADDR, flags, addr, data);
      
      // strip leading and trailing slashes from cross street
      data.strCross = stripFieldStart(data.strCross, "/");
      data.strCross = stripFieldEnd(data.strCross, "/");
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames() + " CITY X";
    }
  }

  public static final String[] CITY_LIST = new String[]{
    "AUBURN",
    "BONNEY LAKE",
    "BUCKLEY",
    "CARBONADO",
    "DUPONT",
    "EATONVILLE",
    "EDGEWOOD",
    "ENUMCLAW",
    "FIFE",
    "FIRCREST",
    "GIG HARBOR",
    "LAKEWOOD",
    "MILTON",
    "ORTING",
    "PACIFIC",
    "PIERCE COUNTY",
    "PUYALLUP",
    "ROY",
    "RUSTON",
    "SOUTH PRAIRIE",
    "STEILACOOM",
    "SUMNER",
    "TACOMA",
    "UNIVERSITY PLACE",
    "WILKERSON", //very common typo, google autocorrects to wilkeson though so its ok
    "WILKESON",
  };
}
