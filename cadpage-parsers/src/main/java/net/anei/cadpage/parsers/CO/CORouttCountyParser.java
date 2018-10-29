package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class CORouttCountyParser extends FieldProgramParser {
  
  public CORouttCountyParser() {
    super(CITY_CODES, "ROUTT COUNTY", "CO", 
          "SRC CALL ADDRCITY MAP UNIT! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "777";
  }
  
  
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }
  
  private static final Pattern MARKER = Pattern.compile(" ?ROUTT COUNTY 911: (\\d)/(\\d) ");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    int lpt = -1;
    Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) {
      StringBuilder sb = new StringBuilder();
      do {
        data.expectMore = !match.group(1).equals(match.group(2));
        if (lpt >= 0) {
          sb.append(cleanPart(body.substring(lpt, match.start())));
        }
        lpt = match.end();
      } while (match.find(lpt));
      if (lpt >= 0) {
        sb.append(cleanPart(body.substring(lpt)));
      }
      
      body = sb.toString();
    }
    
    else {
      if (!body.startsWith("ROUTT COUNTY 911:")) return false;
      body = body.substring(17).trim();
    }
    
    return parseFields(body.split("\n"), data);
  }
  
  private static final Pattern TRAIL_JUNK_PTN = Pattern.compile("(?:\n \\d{4} [A-Z][a-z]+)+$");
  
  private String cleanPart(String line) {
    Matcher match = TRAIL_JUNK_PTN.matcher(line);
    if (match.find()) line = line.substring(0, match.start());
    return line;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("MAP")) return new MapField("[A-Z0-9]+");
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9]+");
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = convertCodes(field.substring(pt+1).trim(), CITY_CODES);
        field = field.substring(0, pt).trim();
      }
      pt = field.indexOf(';');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      
      parseAddress(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CLK",  "CLARK",
      "SS",   "STEAMBOAT SPRINGS"
  });

}
