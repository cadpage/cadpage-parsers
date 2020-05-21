package net.anei.cadpage.parsers.AL;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Talladega County AL
 */
public class ALTalladegaCountyParser extends FieldProgramParser {
  public ALTalladegaCountyParser() {
    super(CITY_LIST, "TALLADEGA COUNTY", "AL",
          "ADDR/S16 UNIT? ID TIME CALL! geo:GPS? INFO+");
    setupCities(MISSPELLED_CITY_TABLE);
    setupMultiWordStreets("FERRY ROAD");
    setupProtectedNames("1/4");
    removeWords("COVE");
  }
  
  @Override
  public String getFilter() {
    return "Talladega_County_9-1-1@TalladegaCo911.com,Talladega_County_9-1-1@talladega911.org,dispatchtext@talladega911.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Talladega_County_9-1-1:");
    return parseFields(body.split(";", -1), data);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " X PLACE";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressCityField();
    if (name.equals("ID")) return new IdField("\\d{4}\\-\\d{6}", true);
    if (name.equals("TIME")) return new TimeField("(?:\\d{2}\\:){2}\\d{2}", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "1 ");
      super.parse(field.replace("@", "&").replace("//", "&"), data);
      data.strCity = convertCodes(data.strCity, MISSPELLED_CITY_TABLE);
      if (data.strCity.equals("CHILDERBURG")) data.strCity = "CHILDERSBURG";
    }
  }
  
  @Override
  public boolean isNotExtraApt(String field) {
    if (NOT_EXTRA_APT_PTN.matcher(field).matches()) return true;
    return super.isNotExtraApt(field);
  }
  
  private static final Pattern NOT_EXTRA_APT_PTN = Pattern.compile("(?:[/0-9]+ MILE|AND|AREA|BETWEEN|BY|GOING|JUST|OFF RAMP|ON RAMP|ONTO|OR|PASSED|TOWARD)\\b.*");
  
  private static final Properties MISSPELLED_CITY_TABLE = buildCodeTable(new String[]{
      "EASTBOGA",       "EASTABOGA",
      "CHILDERBURG",    "CHILDERSBURG",
      "SYLACAUCA",      "SYLACAUCA",
      "TALLADGEA",      "TALLADEGA",
      "WEGULFKA",       "WEOGUFKA",
      "WINTERBRO",      "WINTERBORO"
  });
  
  private static final String[] CITY_LIST = {
      
      // Cities
      "CHILDERSBURG",
      "LINCOLN",
      "OXFORD",
      "SYLACAUCA",
      "SYLACAUGA",
      "TALLADEGA",

      // Towns
      "BON AIR",
      "MUNFORD",
      "OAK GROVE",
      "TALLADEGA SPRINGS",
      "VINCENT",
      "WALDO",

      // Census-designated places
      "FAYETTEVILLE",
      "MIGNON",

      // Unincorporated communities
      "ALPINE",
      "BEMISTON",
      "CURRY",
      "EASTABOGA",
      "FISHTRAP",
      "HOPEFUL",
      "KAHATCHIE",
      "KENTUCK",
      "LIBERTY HILL",
      "MARDISVILLE",
      "OLD EASTABOGA",
      "SYCAMORE",
      "WINTERBORO",

      // Ghost town
      "GANTTS QUARRY",
      
      // Coosa County
      "COOSA CO",
      "COOSA COUNTY",
      "GOODWATER",
      "WEOGUFKA",
      
      // Calhoun County
      "CALHOUN CO",
      "CALHOUN COUNTY",
      "ANNISTON",
      
      // Clay County
      "CLAY CO",
      "CLAY COUNTY",

      // Marengo County
      "MARENGO CO",
      "MARENGO COUNTY",
      "PROVIDENCE",
    
      // Tallapoosa County
      "TALLAPOOSA CO",
      "TALLAPOOSA COUNTY",
      "ALEXANDER CITY"
  };
}
