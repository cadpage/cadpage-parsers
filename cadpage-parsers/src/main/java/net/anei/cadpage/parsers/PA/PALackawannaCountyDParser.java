package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PALackawannaCountyDParser extends FieldProgramParser {

  public PALackawannaCountyDParser() {
    super(CITY_LIST, "LACKAWANNA COUNTY", "PA",
          "Call_Type:CALL! Location:ADDRCITY! PLACE Units:UNIT! Full_Transcript:INFO! INFO/N+");
    setupCities(MISSPELLED_CITIES);
  }

  @Override
  public String getFilter() {
    return "lackawannafirepager@gmail.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Alert")) return false;
    int pt = body.indexOf("Full Transcript:");
    body = body.substring(0,pt).replace('\n', ' ') + body.substring(pt);
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private static final Pattern ADDR_DELIM_PTN = Pattern.compile(" *(?:,|\\b(?:at|in)\\b) *", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_ST_PTN = Pattern.compile("[A-Z]{2}");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(?:APT|APARTMENT|RM|ROOM|LOT) +(.*)|(\\d{1,4}[A-Z]?|[A-Z])", Pattern.CASE_INSENSITIVE);

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match;
      for (String part : ADDR_DELIM_PTN.split(field)) {
        if (data.strAddress.isEmpty()) {
          parseAddress(part, data);
        } else if (ADDR_ST_PTN.matcher(part).matches()) {
          data.strState = part;
        } else if ((match = ADDR_APT_PTN.matcher(part)).matches()) {
          String apt = match.group(1);
          if (apt == null) apt = match.group(2);
          data.strApt = append(data.strApt, "-", apt);
        } else if (isCity(part)) {
          String city = MISSPELLED_CITIES.getProperty(part.toUpperCase());
          if (city == null) city = part;
          data.strCity = city;
        } else if (isValidAddress(part)) {
          data.strCross = append(data.strCross, " & ", part);
        } else {
          data.strPlace = append(data.strPlace, " - ", part);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST PLACE X";
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CARBONDALE",
      "SCRANTON",

      // Boroughs
      "ARCHBALD",
      "BLAKELY",
      "CLARKS GREEN",
      "CLARKS SUMMIT",
      "DALTON",
      "DICKSON CITY",
      "DUNMORE",
      "JERMYN",
      "JESSUP",
      "MAYFIELD",
      "MOOSIC",
      "MOSCOW",
      "OLD FORGE",
      "OLYPHANT",
      "TAYLOR",
      "THROOP",
      "VANDLING",

      // Townships
      "BENTON",
      "CARBONDALE",
      "CLIFTON",
      "COVINGTON",
      "ELMHURST",
      "FELL",
      "GLENBURN",
      "GREENFIELD",
      "JEFFERSON",
      "LA PLUME",
      "MADISON",
      "NEWTON",
      "NORTH ABINGTON",
      "RANSOM",
      "ROARING BROOK",
      "SCOTT",
      "SOUTH ABINGTON",
      "SPRING BROOK",
      "THORNHURST",
      "WAVERLY",
      "WEST ABINGTON",

      // Census-designated places
      "BIG BASS LAKE",
      "CHINCHILLA",
      "EAGLE LAKE",
      "GLENBURN",
      "MOUNT COBB",
      "SIMPSON",
      "WAVERLY",

      // Unincorporated communities
      "DALEVILLE",
      "MILWAUKEE",
      "WINTON",

      "EAST MOUNTAIN",
      "LACKAWANNA COUNTY"
  };

  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[] {
      "LAKAWANA COUNTY",    "Lackawanna County",
      "LAQUANA COUNTY",     "Lackawanna County"

  });
}
